package com.borysenko.multicrypto.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 08/06/19
 * Time: 13:50
 */
public class FileExtensions {

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
