package com.borysenko.multicrypto.dagger.file;

import com.borysenko.multicrypto.ui.file.FileActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 05/06/19
 * Time: 22:48
 */
@Singleton
@Component(modules = FileScreenModule.class)
public interface FileScreenComponent {
    void inject(FileActivity fileActivity);
}
