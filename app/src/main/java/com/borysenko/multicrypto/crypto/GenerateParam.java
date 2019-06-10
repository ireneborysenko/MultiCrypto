package com.borysenko.multicrypto.crypto;

import java.math.BigInteger;
import java.security.SecureRandom;

public class GenerateParam {

    public static void generateInitParameters(int N) {
        int keySize = 1024;
        final Dealer dealer = new Dealer(keySize);
        dealer.generateKeys(N);
    }

    public static String generateSymKey() {
        SecureRandom random = new SecureRandom();
        int keySize = 104;
        BigInteger key = BigInteger.probablePrime(keySize, random);
        return key.toString();
    }
}
