package com.borysenko.multicrypto.crypto;

import java.math.BigInteger;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 13/06/19
 * Time: 13:33
 */
class SecretShare {
    SecretShare(final int number, final BigInteger share)
    {
        this.number = number;
        this.share = share;
    }

    int getNumber()
    {
        return number;
    }

    BigInteger getShare()
    {
        return share;
    }

    private final int number;
    private final BigInteger share;
}
