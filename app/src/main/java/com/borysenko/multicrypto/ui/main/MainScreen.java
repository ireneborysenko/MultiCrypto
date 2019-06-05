package com.borysenko.multicrypto.ui.main;

import android.app.Activity;
import android.content.Intent;

import com.borysenko.multicrypto.adapters.MainRecyclerAdapter;

import java.util.ArrayList;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 04/06/19
 * Time: 19:10
 */
public interface MainScreen {

    interface View {

        void initRecyclerView(ArrayList<String> filesList);
    }

    interface Presenter {

        void createFolder();

        void loadFilesToRecycler();

        void chooseFile(Activity activity);

        void moveFileToFolder(Intent data);

        void recyclerViewListener(MainRecyclerAdapter mAdapter);
    }

}
