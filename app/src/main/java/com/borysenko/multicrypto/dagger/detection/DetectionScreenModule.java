package com.borysenko.multicrypto.dagger.detection;

import com.borysenko.multicrypto.dagger.DbManagerModule;
import com.borysenko.multicrypto.ui.detection.DetectionScreen;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 07/06/19
 * Time: 14:29
 */
@Module(includes = DbManagerModule.class)
public class DetectionScreenModule {
    private final DetectionScreen.View mView;

    public DetectionScreenModule(DetectionScreen.View mView) {
        this.mView = mView;
    }

    @Provides
    DetectionScreen.View providesDetectionScreenView() {
        return mView;
    }
}
