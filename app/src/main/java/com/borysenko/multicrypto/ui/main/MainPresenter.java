package com.borysenko.multicrypto.ui.main;

import javax.inject.Inject;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 04/06/19
 * Time: 19:09
 */
public class MainPresenter implements MainScreen.Presenter {
    private MainScreen.View mView;

    @Inject
    MainPresenter(MainScreen.View mView) {
        this.mView = mView;
    }

}
