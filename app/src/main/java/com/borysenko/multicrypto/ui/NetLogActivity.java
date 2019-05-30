package com.borysenko.multicrypto.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.borysenko.multicrypto.R;

public class NetLogActivity extends AppCompatActivity {

    private TextView netLogTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_log);

        netLogTextView = (TextView)findViewById(R.id.net_log_textview);
        netLogTextView.setText(null);
    }
}
