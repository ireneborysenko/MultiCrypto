package com.borysenko.multicrypto.dagger.app;

import android.content.Context;

import com.borysenko.multicrypto.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 04/06/19
 * Time: 19:27
 */
@Module
public class AppModule {

    private final App mApp;

    public AppModule(App mApp) {
        this.mApp = mApp;
    }

    @Provides
    @Singleton
    public App app() {
        return mApp;
    }

    @Provides
    @Singleton
    Context applicationContext() {
        return mApp.getApplicationContext();
    }
}
