//package com.borysenko.multicrypto.ui;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.net.Uri;
//import android.net.wifi.WifiManager;
//import android.net.wifi.p2p.WifiP2pConfig;
//import android.net.wifi.p2p.WifiP2pDevice;
//import android.net.wifi.p2p.WifiP2pDeviceList;
//import android.net.wifi.p2p.WifiP2pInfo;
//import android.net.wifi.p2p.WifiP2pManager;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.provider.MediaStore;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import android.util.Log;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.borysenko.multicrypto.R;
//import com.borysenko.multicrypto.WiFiDirectBroadcastReceiver;
//import com.borysenko.multicrypto.proto.CBC;
//import com.borysenko.multicrypto.proto.GenerateParam;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.io.UnsupportedEncodingException;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.security.InvalidAlgorithmParameterException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.NoSuchPaddingException;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//public class MainActivity extends AppCompatActivity {
//
//    private final int CHOOSE_FILE_REQUEST_CODE = 0xff;
//    private SharedPreferences sPref;
//    private AlertDialog.Builder ad;
//
//    WifiManager wifiManager;
//    WifiP2pManager mManager;
//    WifiP2pManager.Channel mChannel;
//
//    BroadcastReceiver mReceiver;
//    IntentFilter mIntentFiler;
//
//    List<WifiP2pDevice> peers = new ArrayList<>();
//    String[] deviceNameArray;
//    WifiP2pDevice[] deviceArray;
//
//    static final int MESSAGE_READ = 1;
//
//    ServerClass serverClass;
//    ClientClass clientClass;
//    SendReceive sendReceive;
//
//    @BindView(R.id.set_init_params_button)
//    Button mSetInitParamsButton;
//
//    @BindView(R.id.number_of_devices)
//    EditText mNumberOfDevices;
//
//    @BindView(R.id.choose_file_button)
//    Button mChooseFileButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);
//
//        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
//        mChannel = mManager.initialize(this, getMainLooper(), null);
//
//        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
//        mIntentFiler = new IntentFilter();
//        mIntentFiler.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
//        mIntentFiler.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
//        mIntentFiler.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
//        mIntentFiler.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
//
//    }
//
//    Handler handler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message message) {
//            switch (message.what) {
//                case MESSAGE_READ:
//                    byte[] readBuff = (byte[]) message.obj;
//                    String tempMessage = new String(readBuff, 0, message.arg1);
//                    Log.e("myList", tempMessage);
//                    Toast.makeText(getApplicationContext(), tempMessage, Toast.LENGTH_SHORT).show();
//                    break;
//
//            }
//            return false;
//        }
//    });
//
//    public WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
//        @Override
//        public void onPeersAvailable(WifiP2pDeviceList peersList) {
//
//            if (!peersList.getDeviceList().equals(peers)) {
//                peers.clear();
//                peers.addAll(peersList.getDeviceList());
//
//                deviceNameArray = new String[peersList.getDeviceList().size()];
//                deviceArray = new WifiP2pDevice[peersList.getDeviceList().size()];
//
//                int index = 0;
//
//                for (WifiP2pDevice device: peersList.getDeviceList()) {
//                    deviceNameArray[index] = device.deviceName;
//                    deviceArray[index] = device;
//                    index++;
//                }
//
//                Log.e("myList", Arrays.toString(deviceArray));
//                Toast.makeText(getApplicationContext(), Arrays.toString(deviceArray), Toast.LENGTH_SHORT).show();
//
//
//                if (peers.size() == 0) {
//                    Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//            }
//        }
//    };
//
//    public WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
//        @Override
//        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
//            final InetAddress groupOwnerAddress = wifiP2pInfo.groupOwnerAddress;
//
//            if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
//                Toast.makeText(getApplicationContext(), "Host", Toast.LENGTH_SHORT).show();
//                serverClass = new ServerClass();
//                serverClass.start();
//            } else if (wifiP2pInfo.groupFormed) {
//                Toast.makeText(getApplicationContext(), "Client", Toast.LENGTH_SHORT).show();
//                clientClass = new ClientClass(groupOwnerAddress);
//                clientClass.start();
//            }
//        }
//    };
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        registerReceiver(mReceiver, mIntentFiler);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        unregisterReceiver(mReceiver);
//    }
//
//    @OnClick(R.id.enable_wifi_button)
//    void wifiManager() {
//        if (wifiManager.isWifiEnabled()) {
//            wifiManager.setWifiEnabled(false);
//        } else {
//            wifiManager.setWifiEnabled(true);
//        }
//    }
//
//    @OnClick(R.id.detect_devices_button)
//    void detectDevices() {
//        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
//            @Override
//            public void onSuccess() {
//                Toast.makeText(getApplicationContext(), "Discovery Started", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(int i) {
//                Toast.makeText(getApplicationContext(), "Discovery Failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    @OnClick(R.id.set_init_params_button)
//    void picker() {
//        //connect to the first device
//        final WifiP2pDevice device = deviceArray[0];
//        WifiP2pConfig config = new WifiP2pConfig();
//        config.deviceAddress = device.deviceAddress;
//
//        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
//            @Override
//            public void onSuccess() {
//                Toast.makeText(getApplicationContext(), "connected to " + device.deviceName, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(int i) {
//                Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//
//    public class ServerClass extends Thread{
//        Socket socket;
//        ServerSocket serverSocket;
//
//        @Override
//        public void run() {
//            try {
//                serverSocket = new ServerSocket(8888);
//                socket = serverSocket.accept();
//                sendReceive = new SendReceive(socket);
//                sendReceive.start();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//    public class SendReceive extends Thread {
//        private Socket socket;
//        private InputStream inputStream;
//        private OutputStream outputStream;
//
//        public SendReceive(Socket skt) {
//            socket = skt;
//            try{
//                inputStream = socket.getInputStream();
//                outputStream = socket.getOutputStream();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void run() {
//            byte[] buffer = new byte[1024];
//            int bytes;
//
//            while (socket!=null) {
//                try {
//                    bytes = inputStream.read(buffer);
//                    if (bytes>0) {
//                        handler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        public void write(byte[] bytes) {
//
//            try {
//                outputStream.write(bytes);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
//
//
//    public class ClientClass extends Thread {
//        Socket socket;
//        String hostAdd;
//
//        public ClientClass(InetAddress hostAddress) {
//            hostAdd = hostAddress.getHostAddress();
//            socket = new Socket();
//        }
//
//        @Override
//        public void run() {
//            try {
//                socket.connect(new InetSocketAddress(hostAdd, 8888),500);
//                sendReceive = new SendReceive(socket);
//                sendReceive.start();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
////    @OnClick(R.id.set_init_params_button)
////    void generateParameters() {
////        try {
////            int modulus = Integer.valueOf(mNumberOfDevices.getText().toString());
////            GenerateParam.generateInitParameters(modulus);
////        } catch (NumberFormatException e) {
////            e.printStackTrace();
////        }
////    }
//
//    @OnClick(R.id.choose_file_button)
//    void sendAction() {
//        String msg = "Some Text to Send";
//        sendReceive.write(msg.getBytes());
//    }
//
//
////    @OnClick(R.id.choose_file_button)
////    void chooseFile() {
////        try {
////            Intent chooseFileIntent;
////            PackageManager packageManager = getPackageManager();
////             String mimeType = "*/*";
////            do {
////                chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
////                chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
////                chooseFileIntent.setType(mimeType);
////                if (packageManager.resolveActivity(chooseFileIntent, 0) != null) {
////                    break;
////                }
////                chooseFileIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
////                chooseFileIntent.putExtra("CONTENT_TYPE", mimeType);
////            } while (false);
////
////            Intent chooserIntent = Intent.createChooser(chooseFileIntent, "Select File");
////            startActivityForResult(chooserIntent, CHOOSE_FILE_REQUEST_CODE);
////        } catch (NumberFormatException e) {
////            e.printStackTrace();
////        } catch (android.content.ActivityNotFoundException e) {
////            Toast.makeText(
////                    getApplicationContext(),
////                    "No file manager found", Toast.LENGTH_LONG).show();
////        }
////    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode != RESULT_OK) {
//            return;
//        }
//
//        if (requestCode == CHOOSE_FILE_REQUEST_CODE) {
//            Uri filepath = data.getData();
//            File fileName = new File(getRealPathFromURI(filepath));
//            onSelectedFile(fileName.toString());
//        }
//    }
//
//    private String getRealPathFromURI(Uri contentURI) {
//        String result;
//        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
//        if (cursor == null) {
//            result = contentURI.getPath();
//        } else {
//            cursor.moveToFirst();
//            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//            result = cursor.getString(idx);
//            cursor.close();
//        }
//        return result;
//    }
//
//    private void onSelectedFile(String fileName) {
//        try {
//            showDialog(fileName);
//            ad.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Toast.makeText(getApplicationContext(), fileName, Toast.LENGTH_LONG).show();
//    }
//
//    private void showDialog(final String fileName) throws IOException {
//
//        String title = "Вибір дії";
//        String message = "Обрано файл ";
//        String button1String = "Зашифрувати";
//        String button2String = "Розшифрувати";
//
//        final String fileContent = openFile(fileName);
//
//        ad = new AlertDialog.Builder(this);
//        ad.setTitle(title);
//        ad.setMessage(message);
//        ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int arg1) {
//                Toast.makeText(MainActivity.this, "файл буде шифруватись", Toast.LENGTH_LONG).show();
//                String encrypted;
//
//                try {
//                    String secretKey = GenerateParam.generateSymKey();
//                    CBC cbc = new CBC(secretKey);
//                    encrypted = cbc.encrypt(fileContent);
//                    writeFile(fileName, encrypted);
//                    saveSymKey(fileName, secretKey);
//                } catch (InvalidAlgorithmParameterException | InvalidKeyException
//                        | BadPaddingException | IllegalBlockSizeException
//                        | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//        ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int arg1) {
//                Toast.makeText(MainActivity.this, "файл буде розшифровуватись", Toast.LENGTH_LONG).show();
//                String decrypted;
//
//                try {
//                    String secretKey = loadSymKey(fileName);
//                    CBC cbc = new CBC(secretKey);
//                    decrypted = cbc.decrypt(fileContent);
//                    writeFile(fileName, decrypted);
//                } catch (InvalidAlgorithmParameterException | InvalidKeyException
//                        | BadPaddingException | IllegalBlockSizeException
//                        | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        ad.setCancelable(true);
//        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            public void onCancel(DialogInterface dialog) {
//                Toast.makeText(MainActivity.this, "Вы ничего не выбрали",
//                        Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    private String openFile(String filepath) throws IOException {
//        FileInputStream inputStream;
//        inputStream = new FileInputStream(filepath);
//        int data = inputStream.read();
//        char content;
//        StringBuilder finalString = new StringBuilder();
//        while (data != -1) {
//            content = (char) data;
//            data = inputStream.read();
//            finalString.append(content);
//        }
//        inputStream.close();
//        return finalString.toString();
//    }
//
//    private void writeFile(String filePath, String fileContent) {
//        if (!Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED)) {
//            Log.d("errorSD", "SD card is unavailable " + Environment.getExternalStorageState());
//            return;
//        }
//        File sdFile = new File(filePath);
//
//        try {
//            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
//            bw.write(fileContent);
//            bw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void saveSymKey(String fileName, String secretKey) {
//        sPref = getSharedPreferences("MySymKeys", MODE_PRIVATE);
//        SharedPreferences.Editor ed = sPref.edit();
//        ed.putString(fileName, secretKey);
//        ed.apply();
//    }
//
//    private String loadSymKey(String fileName) {
//        sPref = getSharedPreferences("MySymKeys", MODE_PRIVATE);
//        return sPref.getString(fileName, "");
//    }
//}
