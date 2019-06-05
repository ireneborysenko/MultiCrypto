package com.borysenko.multicrypto.ui.main;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 04/06/19
 * Time: 19:10
 */
public interface MainScreen {

    interface View {

    }

    interface Presenter {

        void createFolder();

        void loadFilesToRecycler();

        void chooseFile(Activity activity);

        void moveFileToFolder(Intent data);
    }

}
