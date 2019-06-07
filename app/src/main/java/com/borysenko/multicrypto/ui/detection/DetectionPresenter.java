package com.borysenko.multicrypto.ui.detection;

import com.borysenko.multicrypto.db.CryptFile;
import com.borysenko.multicrypto.db.DataBaseCallBack;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 07/06/19
 * Time: 14:17
 */
public class DetectionPresenter implements DetectionScreen.Presenter, DataBaseCallBack {

    private DetectionScreen.View mView;

    @Inject
    DetectionPresenter(DetectionScreen.View mView) {
        this.mView = mView;
    }

    @Override
    public void onUsersLoaded(List<CryptFile> cryptFiles) {

    }

    @Override
    public void onUpdateRecyclerView() {

    }

    @Override
    public void beginDeviceDetectionButton() {

    }

    @Override
    public void connectButton() {

    }

    @Override
    public void sendButton() {

    }
}
