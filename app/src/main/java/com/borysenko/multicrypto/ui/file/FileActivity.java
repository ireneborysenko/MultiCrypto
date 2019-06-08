package com.borysenko.multicrypto.ui.file;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.borysenko.multicrypto.R;
import com.borysenko.multicrypto.dagger.ContextModule;
import com.borysenko.multicrypto.dagger.file.DaggerFileScreenComponent;
import com.borysenko.multicrypto.dagger.file.FileScreenModule;
import com.borysenko.multicrypto.db.CryptFile;
import com.borysenko.multicrypto.proto.CBC;
import com.borysenko.multicrypto.proto.GenerateParam;
import com.borysenko.multicrypto.tools.FileExtensions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.borysenko.multicrypto.tools.Constants.DECRYPTED_FILE;
import static com.borysenko.multicrypto.tools.FileExtensions.writeFile;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 05/06/19
 * Time: 22:41
 */
public class FileActivity extends AppCompatActivity implements FileScreen.View{

    @Inject
    FilePresenter mPresenter;

    @BindView(R.id.last_date)
    TextView mLastDate;

    @BindView(R.id.file_history)
    TextView mFileHistory;

    @BindView(R.id.found_devices_recycler)
    ListView mDevicesListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        ButterKnife.bind(this);

        DaggerFileScreenComponent.builder()
                .fileScreenModule(new FileScreenModule(this))
                .contextModule(new ContextModule(this))
                .build().inject(this);

        Intent intent = getIntent();
        CryptFile file = (CryptFile) intent.getSerializableExtra("fileObject");
        Objects.requireNonNull(getSupportActionBar()).setTitle(file.getFileName());

        if (file.getEncryptionType() == DECRYPTED_FILE) {
            mLastDate.setVisibility(View.GONE);
            mFileHistory.setVisibility(View.GONE);
        }

        mPresenter.initWifi();


        GenerateParam.generateInitParameters(5);

        String fileContent = null;
        try {
            fileContent = FileExtensions.openFile(file.getFilePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

//        encryptFile(fileContent, file.getFilePath());
//        decryptFile(fileContent, file.getFilePath());

    }

    private void encryptFile(String fileContent, String s) {
        try {
            String encrypted;
            String secretKey = GenerateParam.generateSymKey();
            CBC cbc = new CBC(secretKey);
            encrypted = cbc.encrypt(fileContent);
            writeFile(s, encrypted);
            FileExtensions.saveSymKey(s, secretKey, this);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException
                | BadPaddingException | IllegalBlockSizeException
                | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    private void decryptFile(String fileContent, String s) {
        try {
            String decrypted;
            String secretKey = FileExtensions.loadSymKey(s, this);
            CBC cbc = new CBC(secretKey);
            decrypted = cbc.decrypt(fileContent);
            writeFile(s, decrypted);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException
                | BadPaddingException | IllegalBlockSizeException
                | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.enableReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.disableReceiver();
    }

    @OnClick(R.id.begin_detection_button)
    void beginDetectionButton() {
        mPresenter.beginDeviceDetectionButton();
    }

    @Override
    public void setConnectionNotification(String passedString) {
        mFileHistory.setVisibility(View.VISIBLE);
        mFileHistory.setText(passedString);
    }

    @Override
    public ListView getDevicesListView() {
        return mDevicesListView;
    }

    @OnClick(R.id.connect_button)
    void connectButtonClicked() {
        mPresenter.connectButton();
    }

    @OnClick(R.id.send_button)
    void sendButtonClicked() {mPresenter.sendButton();}
}
