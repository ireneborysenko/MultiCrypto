package com.borysenko.multicrypto.crypto;

import com.borysenko.multicrypto.tools.BigConst;

import java.math.BigInteger;

class Poly {
    private BigInteger[] coefficient;

    private int size;

    Poly(final BigInteger d, final int size, final BigInteger m) {
        final int bitLength = m.bitLength();

        this.size = size;
        coefficient = new BigInteger[size];
        coefficient[0] = d;
        for (int i = 1; i < size; i++)
            coefficient[i] = (new BigInteger(bitLength, BigConst.getRandom())).mod(m);
    }

    private BigInteger eval(final BigInteger x) {
        BigInteger retVal = coefficient[size - 1];

        for (int i = size - 2; i >= 0; i--)
            retVal = (retVal.multiply(x)).add(coefficient[i]);
        return retVal;
    }

    BigInteger eval(final int x) {
        final BigInteger bx = BigInteger.valueOf(x);
        return this.eval(bx);
    }
}
