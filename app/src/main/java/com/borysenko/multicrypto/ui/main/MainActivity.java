package com.borysenko.multicrypto.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.borysenko.multicrypto.R;
import com.borysenko.multicrypto.dagger.ContextModule;
import com.borysenko.multicrypto.dagger.main.DaggerMainScreenComponent;
import com.borysenko.multicrypto.dagger.main.MainScreenModule;

import javax.inject.Inject;

import static com.borysenko.multicrypto.tools.Constants.CHOOSE_FILE_REQUEST_CODE;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 04/06/19
 * Time: 22:32
 */
public class MainActivity extends AppCompatActivity implements MainScreen.View{

    @Inject
    MainPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaggerMainScreenComponent.builder()
                .mainScreenModule(new MainScreenModule(this))
                .contextModule(new ContextModule(this))
                .build().inject(this);

        mPresenter.createFolder();
        mPresenter.loadFilesToRecycler();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_buttons,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.choose_files) {
            mPresenter.chooseFile(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == CHOOSE_FILE_REQUEST_CODE) {
            mPresenter.moveFileToFolder(data);
        }
    }
}
