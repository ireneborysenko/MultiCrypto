package com.borysenko.multicrypto.ui.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.borysenko.multicrypto.R;
import com.borysenko.multicrypto.dagger.main.DaggerMainScreenComponent;
import com.borysenko.multicrypto.dagger.main.MainScreenModule;

import javax.inject.Inject;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 04/06/19
 * Time: 22:32
 */
public class MainActivity extends AppCompatActivity implements MainScreen.View{

    @Inject
    MainPresenter searchPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaggerMainScreenComponent.builder()
                .mainScreenModule(new MainScreenModule(this))
                .build().inject(this);
    }
}
