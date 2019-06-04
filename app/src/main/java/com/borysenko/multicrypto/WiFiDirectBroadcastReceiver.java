//package com.borysenko.multicrypto;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.net.NetworkInfo;
//import android.net.wifi.p2p.WifiP2pManager;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.borysenko.multicrypto.ui.MainActivity;
//
///**
// * Created by Android Studio.
// * User: Iryna
// * Date: 03/06/19
// * Time: 13:11
// */
//public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {
//
//    private WifiP2pManager mManager;
//    private WifiP2pManager.Channel mChannel;
//    private MainActivity mActivity;
//
//    public WiFiDirectBroadcastReceiver(WifiP2pManager mManager, WifiP2pManager.Channel mChannel,
//                                       MainActivity mActivity) {
//        this.mManager = mManager;
//        this.mChannel = mChannel;
//        this.mActivity = mActivity;
//    }
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        String action = intent.getAction();
//
//        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
//
//            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
//
//            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
//                Toast.makeText(context, "Wifi is On", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(context, "Wifi is Off", Toast.LENGTH_SHORT).show();
//            }
//        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
//            Log.e("myList", "No");
//            if (mManager!=null) {
//                mManager.requestPeers(mChannel, mActivity.peerListListener);
//            }
//
//        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
//
//            if (mManager == null) return;
//
//            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
//
//            if (networkInfo.isConnected()) {
//                mManager.requestConnectionInfo(mChannel, mActivity.connectionInfoListener);
//            } else {
//                Toast.makeText(context, "Device Disconnected", Toast.LENGTH_SHORT).show();
//            }
//
//        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
//
//        }
//
//    }
//}