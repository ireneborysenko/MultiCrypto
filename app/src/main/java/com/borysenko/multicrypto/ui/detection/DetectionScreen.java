package com.borysenko.multicrypto.ui.detection;

import android.widget.ListView;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 07/06/19
 * Time: 14:18
 */
public interface DetectionScreen {
    interface View {

        ListView getDevicesListView();
    }

    interface Presenter {

        void beginDeviceDetectionButton();

        void connectButton();

        void sendButton();
    }
}
