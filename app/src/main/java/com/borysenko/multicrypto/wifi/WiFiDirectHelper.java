package com.borysenko.multicrypto.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static android.os.Looper.getMainLooper;
import static com.borysenko.multicrypto.tools.Constants.MESSAGE_READ;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 06/06/19
 * Time: 21:46
 */
public class WiFiDirectHelper {

    private Context mContext;

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;

    private BroadcastReceiver mReceiver;
    private IntentFilter mIntentFiler;

    private SendReceive sendReceive;

    public WiFiDirectHelper(Context mContext) {
        this.mContext = mContext;
    }

    public void initWiFiDirect(Object listenerFile, int presenterType) {

        WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        mManager = (WifiP2pManager) mContext.getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(mContext, getMainLooper(), null);

        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, listenerFile, presenterType);
        mIntentFiler = new IntentFilter();
        mIntentFiler.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFiler.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFiler.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFiler.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        if (!wifiManager.isWifiEnabled())
            wifiManager.setWifiEnabled(true);

    }

    public void connectDevice(WifiP2pDevice device) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;

        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "connected to " + device.deviceName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i) {
                Toast.makeText(mContext, "Not Connected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void foundDevices() {
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int i) {
            }
        });
    }

    public void registerWiFiReceiver() {
        mContext.registerReceiver(mReceiver, mIntentFiler);
    }

    public void unregisterWiFiReceiver() {
        mContext.unregisterReceiver(mReceiver);
    }

    public void sendData(String str) {
        sendReceive.write(str.getBytes());
    }

    public void startServer() {
        ServerClass serverClass = new ServerClass();
        serverClass.start();
    }

    public void startClient(InetAddress groupOwnerAddress) {

        ClientClass clientClass = new ClientClass(groupOwnerAddress);
        clientClass.start();
    }

    public class ServerClass extends Thread{
        Socket socket;
        ServerSocket serverSocket;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(8888);
                socket = serverSocket.accept();
                sendReceive = new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class SendReceive extends Thread {
        private Socket socket;
        private InputStream inputStream;
        private OutputStream outputStream;

        SendReceive(Socket skt) {
            socket = skt;
            try{
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (socket!=null) {
                try {
                    bytes = inputStream.read(buffer);
                    if (bytes>0) {
                        handler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        void write(byte[] bytes) {

            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public class ClientClass extends Thread {
        Socket socket;
        String hostAdd;

        ClientClass(InetAddress hostAddress) {
            hostAdd = hostAddress.getHostAddress();
            socket = new Socket();
        }

        @Override
        public void run() {
            try {
                socket.connect(new InetSocketAddress(hostAdd, 8888),500);
                sendReceive = new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == MESSAGE_READ) {
                byte[] readBuff = (byte[]) message.obj;
                String tempMessage = new String(readBuff, 0, message.arg1);
                Toast.makeText(mContext, tempMessage, Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });
}
