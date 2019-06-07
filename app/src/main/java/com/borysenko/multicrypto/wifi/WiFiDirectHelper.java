//package com.borysenko.multicrypto.wifi;
//
//import android.app.Activity;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.IntentFilter;
//import android.net.wifi.WifiManager;
//import android.net.wifi.p2p.WifiP2pConfig;
//import android.net.wifi.p2p.WifiP2pDevice;
//import android.net.wifi.p2p.WifiP2pInfo;
//import android.net.wifi.p2p.WifiP2pManager;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.borysenko.multicrypto.ui.file.FileActivity;
//import com.borysenko.multicrypto.ui.file.FilePresenter;
//
//import java.net.InetAddress;
//
//import static android.os.Looper.getMainLooper;
//
///**
// * Created by Android Studio.
// * User: Iryna
// * Date: 06/06/19
// * Time: 21:46
// */
//public class WiFiDirectHelper {
//
//    private Context mContext;
//
//    private WifiManager wifiManager;
//    private WifiP2pManager mManager;
//    private WifiP2pManager.Channel mChannel;
//
//    private BroadcastReceiver mReceiver;
//    private IntentFilter mIntentFiler;
//
//
//    public WiFiDirectHelper(Context mContext) {
//        this.mContext = mContext;
//    }
//
//
//    public void initWiFiDirect(FilePresenter activity) {
//        wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//        mManager = (WifiP2pManager) mContext.getSystemService(Context.WIFI_P2P_SERVICE);
//        mChannel = mManager.initialize(mContext, getMainLooper(), null);
//
//        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, activity);
//        mIntentFiler = new IntentFilter();
//        mIntentFiler.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
//        mIntentFiler.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
//        mIntentFiler.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
//        mIntentFiler.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
//
//        if (!wifiManager.isWifiEnabled())
//            wifiManager.setWifiEnabled(true);
//
//    }
//
//    public void connectDevice(WifiP2pDevice wifiP2pDevice) {
//        final WifiP2pDevice device = wifiP2pDevice;
//        WifiP2pConfig config = new WifiP2pConfig();
//        config.deviceAddress = device.deviceAddress;
//
//        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
//            @Override
//            public void onSuccess() {
//                Toast.makeText(mContext, "connected to " + device.deviceName, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(int i) {
//                Toast.makeText(mContext, "Not Connected", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void foundDevices() {
//        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
//            @Override
//            public void onSuccess() {
//                Log.e("ieie", "onSuccess");
////                mView.setConnectionNotification("Discovery Started");
//            }
//
//            @Override
//            public void onFailure(int i) {
//                Log.e("ieie", "onSuccess failed");
////                mView.setConnectionNotification("Discovery Failed");
//            }
//        });
//    }
//
//    public void registerWiFiReceiver() {
//        mContext.registerReceiver(mReceiver, mIntentFiler);
//    }
//
//    public void unregisterWiFiReceiver() {
//        mContext.unregisterReceiver(mReceiver);
//    }
//
//
//
////    public WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
////        @Override
////        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
////            final InetAddress groupOwnerAddress = wifiP2pInfo.groupOwnerAddress;
////
////            if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
//////                mView.setConnectionNotification("Host");
////                serverClass = new FilePresenter.ServerClass();
////                serverClass.start();
////            } else if (wifiP2pInfo.groupFormed) {
////                mView.setConnectionNotification("Client");
////                clientClass = new FilePresenter.ClientClass(groupOwnerAddress);
////                clientClass.start();
////            }
////        }
////    };
//
//}
