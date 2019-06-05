package com.borysenko.multicrypto;

import android.app.Application;

import androidx.room.Room;

import com.borysenko.multicrypto.dagger.app.AppComponent;
import com.borysenko.multicrypto.dagger.app.AppModule;
import com.borysenko.multicrypto.dagger.app.DaggerAppComponent;
import com.borysenko.multicrypto.db.AppDatabase;


/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 04/06/19
 * Time: 19:14
 */
public class App extends Application {

    public static App instance;
    private AppComponent mAppComponent;
    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "EncryptedFiles")
                .build();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        mAppComponent.inject(this);
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }

    public AppComponent getApplicationComponent() {
        return mAppComponent;
    }

}
