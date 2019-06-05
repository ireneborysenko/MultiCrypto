package com.borysenko.multicrypto.ui.file;

import com.borysenko.multicrypto.db.CryptFile;
import com.borysenko.multicrypto.db.DataBaseCallBack;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 05/06/19
 * Time: 22:41
 */
public class FilePresenter implements FileScreen.Presenter, DataBaseCallBack {

    private FileScreen.View mView;

    @Inject
    FilePresenter(FileScreen.View mView) {
        this.mView = mView;
    }

    @Override
    public void onUsersLoaded(List<CryptFile> cryptFiles) {

    }

    @Override
    public void onUpdateRecyclerView() {

    }
}
