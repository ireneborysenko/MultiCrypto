package com.borysenko.multicrypto.crypto;

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

public class CryptoHelper {

    private Cipher cipher;
    private SecretKey keySpec;
    private IvParameterSpec ivSpec;

    public CryptoHelper(String secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException {
        keySpec = new SecretKeySpec(secretKey.getBytes(CHARSET), "AES");
        ivSpec = new IvParameterSpec(IV.getBytes(CHARSET));
        cipher = Cipher.getInstance(CIPHER_MODE);
    }

    public String decrypt(String input) throws InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        return new String(cipher.doFinal(Base64.decode(input, Base64.DEFAULT)));
    }

    public String encrypt(String input) throws InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        return Base64.encodeToString(cipher.doFinal(input.getBytes(CHARSET)), Base64.DEFAULT);
    }
}
