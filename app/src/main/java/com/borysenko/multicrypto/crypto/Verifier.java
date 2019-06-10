package com.borysenko.multicrypto.crypto;

import java.math.BigInteger;

class Verifier {
    private BigInteger z;

    private BigInteger c;

    private BigInteger groupVerifier;

    private BigInteger shareVerifier;

    Verifier(final BigInteger z, final BigInteger c,
             final BigInteger shareVerifier, final BigInteger groupVerifier) {
        this.z = z;
        this.c = c;
        this.shareVerifier = shareVerifier;
        this.groupVerifier = groupVerifier;
    }

    BigInteger getZ() {
        return this.z;
    }

    BigInteger getShareVerifier() {
        return this.shareVerifier;
    }

    BigInteger getGroupVerifier() {
        return this.groupVerifier;
    }

    BigInteger getC() {
        return this.c;
    }
}
