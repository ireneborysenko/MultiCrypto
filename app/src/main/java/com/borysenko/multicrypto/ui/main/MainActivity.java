package com.borysenko.multicrypto.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.borysenko.multicrypto.R;
import com.borysenko.multicrypto.adapters.MainRecyclerAdapter;
import com.borysenko.multicrypto.dagger.ContextModule;
import com.borysenko.multicrypto.dagger.main.DaggerMainScreenComponent;
import com.borysenko.multicrypto.dagger.main.MainScreenModule;
import com.borysenko.multicrypto.db.CryptFile;
import com.borysenko.multicrypto.ui.detection.DetectionActivity;
import com.borysenko.multicrypto.ui.file.FileActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @BindView(R.id.encrypted_recycler_view)
    RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        DaggerMainScreenComponent.builder()
                .mainScreenModule(new MainScreenModule(this))
                .contextModule(new ContextModule(this))
                .build().inject(this);

        mPresenter.createFolder();
        mPresenter.loadFiles();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_buttons,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.choose_files:
                mPresenter.chooseFile(this);
                break;
            case R.id.detection:
                openDetectionActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openDetectionActivity() {
        Intent intent = new Intent(this, DetectionActivity.class);
        startActivity(intent);
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

    @Override
    public void initRecyclerView(List<CryptFile> filesList) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final MainRecyclerAdapter mAdapter =
                new MainRecyclerAdapter(filesList);
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.recyclerViewListener(mAdapter);
    }

    @Override
    public void openFileActivity(CryptFile file) {
        Intent intent = new Intent(this, FileActivity.class);
        intent.putExtra("fileObject", file);
        startActivity(intent);
    }
}
