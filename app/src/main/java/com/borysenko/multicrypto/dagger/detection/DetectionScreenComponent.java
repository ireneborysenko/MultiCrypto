package com.borysenko.multicrypto.dagger.detection;

import com.borysenko.multicrypto.dagger.ContextModule;
import com.borysenko.multicrypto.ui.detection.DetectionActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 07/06/19
 * Time: 14:29
 */
@Singleton
@Component(modules = {ContextModule.class, DetectionScreenModule.class})
public interface DetectionScreenComponent {
    void inject(DetectionActivity detectionActivity);
}