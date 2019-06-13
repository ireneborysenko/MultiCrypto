package com.borysenko.multicrypto.cryptography;

import android.util.Log;

import com.borysenko.multicrypto.tools.BigConst;

import java.math.BigInteger;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 13/06/19
 * Time: 11:29
 */
class RSACrypto {

    private int keySize;

    private BigInteger n;
    private BigInteger e;
    private BigInteger d;

    RSACrypto(final int keySize) {
        this.keySize = keySize;
    }

    void generateKeys(final int N) {

        final int k = (N / 2) + 1;

        BigInteger groupSize;

        BigInteger p = BigInteger.probablePrime(keySize, BigConst.getRandom());
        BigInteger q = BigInteger.probablePrime(keySize, BigConst.getRandom());

        BigInteger pe = (p.subtract(BigConst.ONE)).divide(BigConst.TWO);
        BigInteger qe = (q.subtract(BigConst.ONE)).divide(BigConst.TWO);

        n = p.multiply(q);
        BigInteger m = pe.multiply(qe);

        groupSize = BigInteger.valueOf(N);
        e = new BigInteger(groupSize.bitLength() + 1, 80, BigConst.getRandom());
        d = e.modInverse(m);

        generateKeyShares(d, m, k, N, n);
    }

    BigInteger encrypt(BigInteger message) {
        return message.modPow(e, n);
    }

    BigInteger decrypt(BigInteger encrypted) {
        return encrypted.modPow(d, n);
    }

    private void generateKeyShares(final BigInteger d, final BigInteger m,
                                         final int k, final int N, final BigInteger n) {
        BigInteger[] secrets;
        BigInteger rand;
        int randBits;

        Polynomial polynomial = new Polynomial(d, k - 1, m);
        secrets = new BigInteger[N];
        randBits = n.bitLength() + BigConst.L1 - m.bitLength();

        for (int i = 0; i < N; i++) {
            secrets[i] = polynomial.evaluation(i + 1);
            rand = (new BigInteger(randBits, BigConst.getRandom())).multiply(m);
            secrets[i] = secrets[i].add(rand);
            Log.e("secrets: ", secrets[i].toString());
        }
    }
}
