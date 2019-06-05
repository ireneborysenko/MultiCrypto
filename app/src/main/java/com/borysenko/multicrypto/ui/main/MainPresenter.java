package com.borysenko.multicrypto.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import com.borysenko.multicrypto.adapters.MainRecyclerAdapter;
import com.borysenko.multicrypto.db.CryptFile;
import com.borysenko.multicrypto.db.DataBaseCallBack;
import com.borysenko.multicrypto.db.DbManager;
import com.borysenko.multicrypto.tools.Extensions;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import static com.borysenko.multicrypto.tools.Constants.CHOOSE_FILE_REQUEST_CODE;
import static com.borysenko.multicrypto.tools.Constants.MAIN_FOLDER;


/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 04/06/19
 * Time: 19:09
 */
public class MainPresenter implements MainScreen.Presenter, DataBaseCallBack {

    private MainScreen.View mView;

    @Inject
    MainPresenter(MainScreen.View mView) {
        this.mView = mView;
    }

    @Inject
    DbManager dbManager;

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
    public void loadFiles() {
        dbManager.getAllFiles(this);
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
        File from = new File(Extensions.getRealPathFromURI(mContext, filepath));
        File to =
                new File(Environment.getExternalStorageDirectory()
                        + "/" + MAIN_FOLDER + "/" + from.getName());
        from.renameTo(to);
        dbManager.insertFile(new CryptFile(from.getName()), this);
    }

    @Override
    public void recyclerViewListener(MainRecyclerAdapter mAdapter) {
        mAdapter.setOnItemClickListener((files) -> {

        });
    }

    @Override
    public void onUsersLoaded(List<CryptFile> cryptFiles) {
        mView.initRecyclerView(cryptFiles);
    }

    @Override
    public void onUpdateRecyclerView() {
        loadFiles();
    }
}
