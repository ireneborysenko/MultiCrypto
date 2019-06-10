package com.borysenko.multicrypto.tools;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.SecureRandom;

public class BigConst {

    public final static String CIPHER_MODE = "AES/CBC/PKCS5Padding";
    public final static String IV = "5151515151515151";
    public final static Charset CHARSET = Charset.forName("UTF8");

    public final static BigInteger ONE = BigInteger.ONE;
    public final static BigInteger TWO = BigInteger.valueOf(2L);
    public final static BigInteger FOUR = BigInteger.valueOf(4L);

    /**
     * An arbitrary security parameter for generating secret shares
     */
    public final static int L1 = 128;

    private static final SecureRandom random = new SecureRandom();

    public static SecureRandom getRandom() {
        return random;
    }
}
