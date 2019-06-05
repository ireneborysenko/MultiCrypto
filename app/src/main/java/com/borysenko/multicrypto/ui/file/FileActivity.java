package com.borysenko.multicrypto.ui.file;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.borysenko.multicrypto.R;
import com.borysenko.multicrypto.dagger.file.DaggerFileScreenComponent;
import com.borysenko.multicrypto.dagger.file.FileScreenModule;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 05/06/19
 * Time: 22:41
 */
public class FileActivity extends AppCompatActivity implements FileScreen.View{

    @Inject
    FilePresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        ButterKnife.bind(this);

        DaggerFileScreenComponent.builder()
                .fileScreenModule(new FileScreenModule(this))
                .build().inject(this);

    }
}
