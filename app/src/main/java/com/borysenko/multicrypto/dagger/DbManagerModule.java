package com.borysenko.multicrypto.dagger;

import com.borysenko.multicrypto.db.DbManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 05/06/19
 * Time: 16:26
 */
@Module
public class DbManagerModule {

    @Provides
    @Singleton
    DbManager provideDbManager() {return new DbManager();}

}
