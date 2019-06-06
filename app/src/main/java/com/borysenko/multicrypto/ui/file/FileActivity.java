package com.borysenko.multicrypto.ui.file;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.borysenko.multicrypto.R;
import com.borysenko.multicrypto.dagger.ContextModule;
import com.borysenko.multicrypto.dagger.file.DaggerFileScreenComponent;
import com.borysenko.multicrypto.dagger.file.FileScreenModule;
import com.borysenko.multicrypto.db.CryptFile;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.borysenko.multicrypto.tools.Constants.DECRYPTED_FILE;

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

    @BindView(R.id.set_connection_button)
    Button mConnectionButton;

    @BindView(R.id.connected_devices_recycler)
    ListView mDevicesListView;

    @BindView(R.id.encryption_button)
    Button mEncryptionButton;

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

        mPresenter.initWifi(this);
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

    @OnClick(R.id.set_connection_button)
    void setConnectionButton() {
        mPresenter.setConnectionBetweenDevices();
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

    @OnClick(R.id.encryption_button)
    void encryptionButtonClicked() {
        mPresenter.sendButton();
    }
}
