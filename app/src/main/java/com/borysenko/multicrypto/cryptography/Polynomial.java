package com.borysenko.multicrypto.cryptography;

import com.borysenko.multicrypto.tools.BigConst;

import java.math.BigInteger;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 13/06/19
 * Time: 11:39
 */
class Polynomial {

    private BigInteger[] coefficient;

    private int size;

    Polynomial(final BigInteger d, final int size, final BigInteger m) {
        final int bitLength = m.bitLength();

        this.size = size;
        coefficient = new BigInteger[size];
        coefficient[0] = d;
        for (int i = 1; i < size; i++)
            coefficient[i] = (new BigInteger(bitLength, BigConst.getRandom())).mod(m);
    }

    private BigInteger evaluation(final BigInteger x) {
        BigInteger retVal = coefficient[size - 1];

        for (int i = size - 2; i >= 0; i--)
            retVal = (retVal.multiply(x)).add(coefficient[i]);

        return retVal;
    }

    BigInteger evaluation(final int x) {
        final BigInteger bigX = BigInteger.valueOf(x);
        return this.evaluation(bigX);
    }
}
