package com.borysenko.multicrypto.dagger.main;

import com.borysenko.multicrypto.dagger.ContextModule;
import com.borysenko.multicrypto.ui.main.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 04/06/19
 * Time: 19:22
 */
@Singleton
@Component(modules = {ContextModule.class, MainScreenModule.class})
public interface MainScreenComponent {
    void inject(MainActivity mainActivity);
}
