package com.borysenko.multicrypto.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.Toast;

import com.borysenko.multicrypto.ui.detection.DetectionPresenter;
import com.borysenko.multicrypto.ui.file.FilePresenter;

import static com.borysenko.multicrypto.tools.Constants.DETECTION_PRESENTER;
import static com.borysenko.multicrypto.tools.Constants.FILE_PRESENTER;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 03/06/19
 * Time: 13:11
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private Object mListenerFile;
    private int mPresenterType;

    public WiFiDirectBroadcastReceiver(WifiP2pManager mManager, WifiP2pManager.Channel mChannel,
                                       Object mListenerFile, int mPresenterType) {
        this.mManager = mManager;
        this.mChannel = mChannel;
        this.mListenerFile = mListenerFile;
        this.mPresenterType = mPresenterType;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);

            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                Toast.makeText(context, "Wifi is On", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Wifi is Off", Toast.LENGTH_SHORT).show();
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            if (mManager!=null) {

                if (mPresenterType == FILE_PRESENTER)
                    mManager.requestPeers(mChannel, ((FilePresenter)mListenerFile).peerListListener);
                else if (mPresenterType == DETECTION_PRESENTER)
                    mManager.requestPeers(mChannel, ((DetectionPresenter)mListenerFile).peerListListener);
            }

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            if (mManager == null) return;

            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {
                if (mPresenterType == FILE_PRESENTER)
                    mManager.requestConnectionInfo(mChannel, ((FilePresenter)mListenerFile).connectionInfoListener);
                else if (mPresenterType == DETECTION_PRESENTER)
                    mManager.requestConnectionInfo(mChannel, ((DetectionPresenter)mListenerFile).connectionInfoListener);
            } else {
                Toast.makeText(context, "Device Disconnected", Toast.LENGTH_SHORT).show();
            }

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {

        }
    }
}
