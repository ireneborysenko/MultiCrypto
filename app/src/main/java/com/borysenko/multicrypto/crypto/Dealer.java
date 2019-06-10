package com.borysenko.multicrypto.crypto;

import android.util.Log;

import com.borysenko.multicrypto.tools.BigConst;

import java.math.BigInteger;

import static com.borysenko.multicrypto.tools.Extensions.factorial;

public class Dealer {

    private int keySize;

    private KeyShare[] shares = null;

    private GroupKey groupKey;

    private final static boolean DEBUG = true;

    /**
     * Previously established key indicator
     */
    private boolean keyInit;

    Dealer(final int keySize) {
        this.keySize = keySize;
        this.keyInit = false;
    }

    /**
     * Generates keys for a (k,N) threshold signatures scheme
     * @param N - number of devices
     *          k - devices quorum
     *          p - first random BigInteger
     *          q - second random BigInteger
     *          n - p*q
     *          pe - first BigInteger for Eulers function(p - 1)
     *          qe - second BigInteger for Eulers function (q - 1)
     *          m - pe*qe
     *          e - public key
     *          d - private key
     */

    void generateKeys(final int N) {

        final int k = (N / 2) + 1;
        BigInteger p, q, n, pe, qe, m, e, d;
        BigInteger groupSize;

        p = BigInteger.probablePrime(keySize, BigConst.getRandom());
        q = BigInteger.probablePrime(keySize, BigConst.getRandom());

        pe = (p.subtract(BigConst.ONE)).divide(BigConst.TWO);
        qe = (q.subtract(BigConst.ONE)).divide(BigConst.TWO);

        n = p.multiply(q);
        m = pe.multiply(qe);

        groupSize = BigInteger.valueOf(N);
        e = new BigInteger(groupSize.bitLength() + 1, 80, BigConst.getRandom());
        d = e.modInverse(m);

        Log.e("private key", e.toString());
        Log.e("public key", d.toString());
        shares = generateKeyShares(d, m, k, N, n);

        BigInteger groupVerifier = generateVerifiers(n, shares);

        groupKey = new GroupKey(k, N, keySize, groupVerifier, e, n);
        keyInit = true;
    }

    public GroupKey getGroupKey() throws ThresholdSigException {
        checkKeyInit();
        return this.groupKey;
    }

    public KeyShare[] getShares() throws ThresholdSigException {
        checkKeyInit();
        return shares;
    }

    private void checkKeyInit() throws ThresholdSigException {
        if (!keyInit) {
            if (DEBUG)
                Log.e("message", "Key pair has not been initialized by generateKeys()");
            throw new ThresholdSigException(
                    "Key pair has not been initialized by generateKeys()");
        }
    }

    /**
     * Generates secret shares for a (k,N) threshold signatures scheme
     */

    private KeyShare[] generateKeyShares(final BigInteger d, final BigInteger m,
                                         final int k, final int N, final BigInteger n) {
        BigInteger[] secrets;
        BigInteger rand;
        int randBits;

        Poly poly = new Poly(d, k - 1, m);
        secrets = new BigInteger[N];
        randBits = n.bitLength() + BigConst.L1 - m.bitLength();
        Log.e("secretShares ", "are: ");

        for (int i = 0; i < N; i++) {
            secrets[i] = poly.eval(i + 1);
            rand = (new BigInteger(randBits, BigConst.getRandom())).multiply(m);
            secrets[i] = secrets[i].add(rand);
            Log.e("secrets: ", secrets[i].toString());
        }

        final BigInteger delta = factorial(N);

        final KeyShare[] s = new KeyShare[N];
        for (int i = 0; i < N; i++)
            s[i] = new KeyShare(i + 1, secrets[i], n, delta);

        return s;
    }

    /**
     * Creates verifiers for secret shares to prevent corrupting shares
     * Computes v[i] = v^^s[i] mod n
     */

    private BigInteger generateVerifiers(final BigInteger n, final KeyShare[] secrets) {

        BigInteger rand = null;

        Log.e("verifications keys", "are: ");
        for (final KeyShare element : secrets) {
            // rand is an element of Q*n (squares of relative primes mod n)
            while (true) {
                rand = new BigInteger(n.bitLength(), BigConst.getRandom());
                final BigInteger d = rand.gcd(n);
                if (d.compareTo(BigConst.ONE) == 0)
                    break;
            }

            rand = rand.multiply(rand).mod(n);
            Log.e("random", rand.toString());
            element.setVerifiers(rand.modPow(element.getSecret(), n), rand);
        }

        return rand;
    }
}
