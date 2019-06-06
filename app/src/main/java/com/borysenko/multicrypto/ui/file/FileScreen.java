package com.borysenko.multicrypto.ui.file;

import android.widget.ListView;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 05/06/19
 * Time: 22:42
 */
public interface FileScreen {

    interface View {

        void setConnectionNotification(String discovery_started);

        ListView getDevicesListView();
    }

    interface Presenter {

        void initWifi(FileActivity activity);

        void setConnectionBetweenDevices();

        void enableReceiver();

        void disableReceiver();

        void sendButton();
    }

}
