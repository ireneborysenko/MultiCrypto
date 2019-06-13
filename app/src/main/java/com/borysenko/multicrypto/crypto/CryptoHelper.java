package com.borysenko.multicrypto.crypto;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 13/06/19
 * Time: 11:27
 */
public class CryptoHelper {

    private RSACrypto rsaCrypto;
    private int numberOfDevices;

    public CryptoHelper(int N) {
        this.numberOfDevices = N;

    }

    public BigInteger generateInitParams() {
        int keySize = 1024;
        rsaCrypto = new RSACrypto(keySize);
        return rsaCrypto.generateKeys(numberOfDevices);

    }

    public BigInteger encrypt(BigInteger symKey) {
        return rsaCrypto.encrypt(symKey);
    }

    public BigInteger decrypt(BigInteger encSymKey) {
        return rsaCrypto.decrypt(encSymKey);
    }

    public BigInteger generateSymKey() {
        SecureRandom randomKey = new SecureRandom();
        int keySize = 104;
        return BigInteger.probablePrime(keySize, randomKey);
    }

    public void splitSharedKey(BigInteger privateKey) {
        SecretShare[] shares = rsaCrypto.shareKey(privateKey);
        rsaCrypto.recoverKey(shares);
    }
}
