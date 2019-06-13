package com.borysenko.multicrypto.crypto;

import com.borysenko.multicrypto.tools.BigConst;

import java.math.BigInteger;
import java.util.Random;

import static com.borysenko.multicrypto.tools.Extensions.modInverse;

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

    private int k;
    private int N;

    private Random random;
    private BigInteger prime;


    private static final int CERTAINTY = 256;

    RSACrypto(final int keySize) {
        this.keySize = keySize;
    }

    BigInteger generateKeys(final int N) {
        this.N = N;
        this.k = (N / 2) + 1;

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

        random = new Random();

        return d;
    }

    SecretShare[] shareKey(final BigInteger secret)
    {

        prime = new BigInteger(secret.bitLength() + 1, CERTAINTY, random);

        final BigInteger[] coefficient = new BigInteger[k -1];
        coefficient[0] = secret;
        for (int i = 1; i < k - 1; i++)
        {
            BigInteger r;
            do {
                r = new BigInteger(prime.bitLength(), random);
            } while (r.compareTo(BigInteger.ZERO) <= 0 || r.compareTo(prime) >= 0);
            coefficient[i] = r;
        }

        final SecretShare[] shares = new SecretShare[N];
        for (int x = 1; x <= N; x++)
        {
            BigInteger accumulated = secret;

            for (int exp = 1; exp < k - 1; exp++)
            {
                accumulated = accumulated.add(coefficient[exp].multiply(BigInteger.valueOf(x).pow(exp).mod(prime))).mod(prime);
            }
            shares[x - 1] = new SecretShare(x, accumulated);
        }

        return shares;
    }

    BigInteger recoverKey(final SecretShare[] shares)
    {
        BigInteger accumulated = BigInteger.ZERO;

        for(int formula = 0; formula < shares.length; formula++)
        {
            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;

            for(int count = 0; count < shares.length; count++)
            {
                if(formula == count)
                    continue;

                int startPosition = shares[formula].getNumber();
                int nextPosition = shares[count].getNumber();

                numerator = numerator.multiply(BigInteger.valueOf(nextPosition).negate()).mod(prime);
                denominator = denominator.multiply(BigInteger.valueOf(startPosition - nextPosition)).mod(prime);
            }
            BigInteger value = shares[formula].getShare();
            BigInteger tmp = value.multiply(numerator) . multiply(modInverse(denominator, prime));
            accumulated = prime.add(accumulated).add(tmp) . mod(prime);
        }

        return accumulated;
    }

    BigInteger encrypt(BigInteger message) {
        return message.modPow(e, n);
    }

    BigInteger decrypt(BigInteger encrypted) {
        return encrypted.modPow(d, n);
    }
}
