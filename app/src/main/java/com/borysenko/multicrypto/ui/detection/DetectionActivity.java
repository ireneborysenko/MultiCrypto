package com.borysenko.multicrypto.ui.detection;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.borysenko.multicrypto.R;
import com.borysenko.multicrypto.dagger.ContextModule;
import com.borysenko.multicrypto.dagger.detection.DaggerDetectionScreenComponent;
import com.borysenko.multicrypto.dagger.detection.DetectionScreenModule;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 07/06/19
 * Time: 14:17
 */
public class DetectionActivity extends AppCompatActivity implements DetectionScreen.View{

    @Inject
    DetectionPresenter mPresenter;

    @BindView(R.id.main_history)
    TextView mMainHistory;

    @BindView(R.id.main_found_devices_recycler)
    ListView mMainDevicesListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);

        ButterKnife.bind(this);

        DaggerDetectionScreenComponent.builder()
                .detectionScreenModule(new DetectionScreenModule(this))
                .contextModule(new ContextModule(this))
                .build().inject(this);
    }

    @OnClick(R.id.main_begin_detection_button)
    void beginDetectionButton() {
        mPresenter.beginDeviceDetectionButton();
    }

    @Override
    public ListView getDevicesListView() {
        return mMainDevicesListView;
    }

    @OnClick(R.id.main_connect_button)
    void connectButtonClicked() {
        mPresenter.connectButton();
    }

    @OnClick(R.id.main_send_button)
    void sendButtonClicked() {mPresenter.sendButton();}


}
