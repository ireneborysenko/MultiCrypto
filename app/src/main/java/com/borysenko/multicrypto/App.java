package com.borysenko.multicrypto;

import android.app.Application;

import com.borysenko.multicrypto.dagger.app.AppComponent;
import com.borysenko.multicrypto.dagger.app.AppModule;
import com.borysenko.multicrypto.dagger.app.DaggerAppComponent;


/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 04/06/19
 * Time: 19:14
 */
public class App extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getApplicationComponent() {
        return mAppComponent;
    }

}
