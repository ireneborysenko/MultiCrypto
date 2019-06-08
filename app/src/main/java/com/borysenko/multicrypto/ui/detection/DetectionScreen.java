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

        void setConnectionNotification(String no_data_found);
    }

    interface Presenter {

        void initWifi();

        void beginDeviceDetectionButton();

        void connectButton();

        void enableReceiver();

        void disableReceiver();

        void sendButton();
    }
}
