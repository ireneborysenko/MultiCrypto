package com.borysenko.multicrypto.ui.file;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.Message;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.borysenko.multicrypto.wifi.WiFiDirectBroadcastReceiver;
import com.borysenko.multicrypto.db.CryptFile;
import com.borysenko.multicrypto.db.DataBaseCallBack;
import com.borysenko.multicrypto.db.DbManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static android.os.Looper.getMainLooper;
import static com.borysenko.multicrypto.tools.Constants.MESSAGE_READ;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 05/06/19
 * Time: 22:41
 */
public class FilePresenter implements FileScreen.Presenter, DataBaseCallBack {

    private FileScreen.View mView;

    private ListView mDevicesListView;

    private WifiManager wifiManager;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;

    private BroadcastReceiver mReceiver;
    private IntentFilter mIntentFiler;

    private List<WifiP2pDevice> peers = new ArrayList<>();
    private String[] deviceNameArray;
    private WifiP2pDevice[] deviceArray;


    private ServerClass serverClass;
    private ClientClass clientClass;
    private SendReceive sendReceive;

    ArrayAdapter<String> peersAdapter;

    @Inject
    DbManager dbManager;

    @Inject
    Context mContext;

    @Inject
    FilePresenter(FileScreen.View mView) {
        this.mView = mView;
    }

    @Override
    public void initWifi(FileActivity activity) {
        mDevicesListView = mView.getDevicesListView();
        wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        mManager = (WifiP2pManager) mContext.getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(mContext, getMainLooper(), null);

        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
        mIntentFiler = new IntentFilter();
        mIntentFiler.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFiler.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFiler.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFiler.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        if (!wifiManager.isWifiEnabled())
            wifiManager.setWifiEnabled(true);
    }

    @Override
    public void beginDeviceDetectionButton() {
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                mView.setConnectionNotification("Discovery Started");
            }

            @Override
            public void onFailure(int i) {
                mView.setConnectionNotification("Discovery Failed");
            }
        });
    }

    @Override
    public void enableReceiver() {
        mContext.registerReceiver(mReceiver, mIntentFiler);
    }

    @Override
    public void disableReceiver() {
        mContext.unregisterReceiver(mReceiver);
    }

    @Override
    public void sendButton() {
        String str = "My message";
        sendReceive.write(str.getBytes());
    }

    @Override
    public void connectButton() {
        SparseBooleanArray checked = mDevicesListView.getCheckedItemPositions();
        List<WifiP2pDevice> checkedDeviceArray = new ArrayList<>();

        for (int i = 0; i < checked.size(); i++) {
            checkedDeviceArray.add(deviceArray[checked.keyAt(i)]);
        }

        for (WifiP2pDevice device: checkedDeviceArray) {
            connectToDevice(device);
        }
    }

    private void connectToDevice(WifiP2pDevice device) {
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

    public WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {

        @Override
        public void onPeersAvailable(WifiP2pDeviceList peersList) {

            if (!peersList.getDeviceList().equals(peers)) {
                peers.clear();
                peers.addAll(peersList.getDeviceList());

                deviceNameArray = new String[peersList.getDeviceList().size()];
                deviceArray = new WifiP2pDevice[peersList.getDeviceList().size()];

                int index = 0;

                for (WifiP2pDevice device: peersList.getDeviceList()) {
                    deviceNameArray[index] = device.deviceName;
                    deviceArray[index] = device;
                    index++;
                }

                peersAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_multiple_choice, deviceNameArray);
                mDevicesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                mDevicesListView.setAdapter(peersAdapter);

                if (peers.size() == 0) {
                    mView.setConnectionNotification("No data found");
                    return;
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
                serverClass = new ServerClass();
                serverClass.start();
            } else if (wifiP2pInfo.groupFormed) {
                mView.setConnectionNotification("Client");
                clientClass = new ClientClass(groupOwnerAddress);
                clientClass.start();
            }
        }
    };

    @Override
    public void onUsersLoaded(List<CryptFile> cryptFiles) {

    }

    @Override
    public void onUpdateRecyclerView() {

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

        public SendReceive(Socket skt) {
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

        public void write(byte[] bytes) {

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

        public ClientClass(InetAddress hostAddress) {
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

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case MESSAGE_READ:
                    byte[] readBuff = (byte[]) message.obj;
                    String tempMessage = new String(readBuff, 0, message.arg1);
                    Toast.makeText(mContext, tempMessage, Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });
}