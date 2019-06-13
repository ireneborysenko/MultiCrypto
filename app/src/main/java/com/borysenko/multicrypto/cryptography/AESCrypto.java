package com.borysenko.multicrypto.cryptography;

import android.util.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.borysenko.multicrypto.tools.BigConst.CHARSET;
import static com.borysenko.multicrypto.tools.BigConst.CIPHER_MODE;
import static com.borysenko.multicrypto.tools.BigConst.IV;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 13/06/19
 * Time: 11:16
 */
public class AESCrypto {

    private Cipher cipher;
    private SecretKey secretKey;
    private IvParameterSpec ivSpec;

    public AESCrypto(String currSecretKey) throws NoSuchPaddingException, NoSuchAlgorithmException {
        secretKey = new SecretKeySpec(currSecretKey.getBytes(CHARSET), "AES");
        ivSpec = new IvParameterSpec(IV.getBytes(CHARSET));
        cipher = Cipher.getInstance(CIPHER_MODE);
    }

    public String decrypt(String input) throws InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        return new String(cipher.doFinal(Base64.decode(input, Base64.DEFAULT)));
    }

    public String encrypt(String input) throws InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        return Base64.encodeToString(cipher.doFinal(input.getBytes(CHARSET)), Base64.DEFAULT);
    }
}
