package com.borysenko.multicrypto.dagger.file;

import com.borysenko.multicrypto.dagger.DbManagerModule;
import com.borysenko.multicrypto.ui.file.FileScreen;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 05/06/19
 * Time: 22:49
 */
@Module(includes = DbManagerModule.class)
public class FileScreenModule {
    private final FileScreen.View mView;

    public FileScreenModule(FileScreen.View mView) {
        this.mView = mView;
    }

    @Provides
    FileScreen.View providesFileScreenView() {
        return mView;
    }
}