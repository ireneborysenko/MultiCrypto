package com.borysenko.multicrypto.ui.detection;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.borysenko.multicrypto.db.CryptFile;
import com.borysenko.multicrypto.db.DataBaseCallBack;
import com.borysenko.multicrypto.db.DbManager;
import com.borysenko.multicrypto.wifi.WiFiDirectHelper;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.borysenko.multicrypto.tools.Constants.DETECTION_PRESENTER;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 07/06/19
 * Time: 14:17
 */
public class DetectionPresenter implements DetectionScreen.Presenter, DataBaseCallBack {

    private DetectionScreen.View mView;

    private ListView mDevicesListView;

    private List<WifiP2pDevice> peers = new ArrayList<>();
    private WifiP2pDevice[] deviceArray;

    private WiFiDirectHelper wiFiDirectHelper;

    @Inject
    DbManager dbManager;


    @Inject
    Context mContext;

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
    public void initWifi() {
        mDevicesListView = mView.getDevicesListView();

        wiFiDirectHelper = new WiFiDirectHelper(mContext);
        wiFiDirectHelper.initWiFiDirect(this, DETECTION_PRESENTER);
    }

    @Override
    public void beginDeviceDetectionButton() {
        wiFiDirectHelper.foundDevices();
    }

    @Override
    public void enableReceiver() {
        wiFiDirectHelper.registerWiFiReceiver();
    }

    @Override
    public void disableReceiver() {
        wiFiDirectHelper.unregisterWiFiReceiver();
    }

    @Override
    public void sendButton() {
        String str = "My message";
        wiFiDirectHelper.sendData(str);
    }

    @Override
    public void connectButton() {
        SparseBooleanArray checked = mDevicesListView.getCheckedItemPositions();
        List<WifiP2pDevice> checkedDeviceArray = new ArrayList<>();

        for (int i = 0; i < checked.size(); i++) {
            checkedDeviceArray.add(deviceArray[checked.keyAt(i)]);
        }

        for (WifiP2pDevice device: checkedDeviceArray) {
            wiFiDirectHelper.connectDevice(device);
        }
    }

    public WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {

        @Override
        public void onPeersAvailable(WifiP2pDeviceList peersList) {

            if (!peersList.getDeviceList().equals(peers)) {
                peers.clear();
                peers.addAll(peersList.getDeviceList());

                String[] deviceNameArray = new String[peersList.getDeviceList().size()];
                deviceArray = new WifiP2pDevice[peersList.getDeviceList().size()];

                int index = 0;

                for (WifiP2pDevice device: peersList.getDeviceList()) {
                    deviceNameArray[index] = device.deviceName;
                    deviceArray[index] = device;
                    index++;
                }

                ArrayAdapter<String> peersAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_multiple_choice, deviceNameArray);
                mDevicesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                mDevicesListView.setAdapter(peersAdapter);

                if (peers.size() == 0) {
                    mView.setConnectionNotification("No data found");
                }
            }
        }
    };

    public WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            final InetAddress groupOwnerAddress = wifiP2pInfo.groupOwnerAddress;

            if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
                mView.setConnectionNotification("Host");
                wiFiDirectHelper.startServer();
            } else if (wifiP2pInfo.groupFormed) {
                wiFiDirectHelper.startClient(groupOwnerAddress);
                mView.setConnectionNotification("Client");
            }
        }
    };
}
