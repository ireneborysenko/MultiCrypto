package com.borysenko.multicrypto.dagger.main;

import com.borysenko.multicrypto.ui.main.MainScreen;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 04/06/19
 * Time: 19:29
 */
@Module
public class MainScreenModule {
    private final MainScreen.View mView;

    public MainScreenModule(MainScreen.View mView) {
        this.mView = mView;
    }

    @Provides
    MainScreen.View providesMainScreenView() {
        return mView;
    }
}