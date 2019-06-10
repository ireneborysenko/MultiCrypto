package com.borysenko.multicrypto.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.borysenko.multicrypto.crypto.SigShare;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 05/06/19
 * Time: 16:50
 */
public class Extensions {
    public static String getRealPathFromURI(Context mContext, Uri contentURI) {
        String result;
        Cursor cursor = mContext.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public static BigInteger factorial(final int l) {
        BigInteger x = BigInteger.valueOf(1L);
        for (int i = 1; i <= l; i++)
            x = x.multiply(BigInteger.valueOf(i));
        return x;
    }

    public static BigInteger lambda(final int ik, final SigShare[] S,
                                     final BigInteger delta) {
        // Compute lagarange interpolation points Reference
        // lambda(id,l) = PI {id!=j, 0<j<=l} (i-j')/(id-j')
        BigInteger value = delta;

        for (final SigShare element : S)
            if (element.getId() != ik)
                value = value.multiply(BigInteger.valueOf(element.getId()));

        for (final SigShare element : S)
            if (element.getId() != ik)
                value = value.divide(BigInteger.valueOf((element.getId() - ik)));

        return value;
    }

    public static String openFile(String filepath) throws IOException {
        FileInputStream inputStream;
        inputStream = new FileInputStream(filepath);
        int data = inputStream.read();
        char content;
        StringBuilder finalString = new StringBuilder();
        while (data != -1) {
            content = (char) data;
            data = inputStream.read();
            finalString.append(content);
        }
        inputStream.close();
        return finalString.toString();
    }

    public static void writeFile(String filePath, String fileContent) {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d("errorSD", "SD card is unavailable " + Environment.getExternalStorageState());
            return;
        }
        File sdFile = new File(filePath);

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            bw.write(fileContent);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveSymKey(String fileName, String secretKey, Context mContext) {
        SharedPreferences sPref;
        sPref = mContext.getSharedPreferences("MySymKeys", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(fileName, secretKey);
        ed.apply();
    }

    public static String loadSymKey(String fileName, Context mContext) {
        SharedPreferences sPref;
        sPref = mContext.getSharedPreferences("MySymKeys", MODE_PRIVATE);
        return sPref.getString(fileName, "");
    }
}
