package com.borysenko.multicrypto.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import static com.borysenko.multicrypto.tools.Constants.CHOOSE_FILE_REQUEST_CODE;
import static com.borysenko.multicrypto.tools.Constants.MAIN_FOLDER;


/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 04/06/19
 * Time: 19:09
 */
public class MainPresenter implements MainScreen.Presenter {

    private MainScreen.View mView;

    @Inject
    MainPresenter(MainScreen.View mView) {
        this.mView = mView;
    }

    @Inject
    Context mContext;

    @Override
    public void createFolder() {
        File f = new File(Environment.getExternalStorageDirectory(), MAIN_FOLDER);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    @Override
    public void loadFilesToRecycler() {
        ArrayList<String> filesList = new ArrayList<>();
        File folder = new File(Environment.getExternalStorageDirectory(), MAIN_FOLDER);
        File[] filesInFolder = folder.listFiles();
        for (File file : filesInFolder) {
            if (!file.isDirectory()) {
                filesList.add(file.getName());
            }
        }
    }

    @Override
    public void chooseFile(Activity activity) {
        try {
            Intent chooseFileIntent;
            PackageManager packageManager = mContext.getPackageManager();
             String mimeType = "*/*";
            do {
                chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
                chooseFileIntent.setType(mimeType);
                if (packageManager.resolveActivity(chooseFileIntent, 0) != null) {
                    break;
                }
                chooseFileIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
                chooseFileIntent.putExtra("CONTENT_TYPE", mimeType);
            } while (false);

            Intent chooserIntent = Intent.createChooser(chooseFileIntent, "Select File");
            activity.startActivityForResult(chooserIntent, CHOOSE_FILE_REQUEST_CODE);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void moveFileToFolder(Intent data) {
        Uri filepath = data.getData();
        File from = new File(getRealPathFromURI(filepath));
        File to =
                new File(Environment.getExternalStorageDirectory()
                        + "/" + MAIN_FOLDER + "/" + from.getName());
        from.renameTo(to);
    }

    private String getRealPathFromURI(Uri contentURI) {
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
}
