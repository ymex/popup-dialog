package com.sample.noticedialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.ymex.notice.dialog.AlertDailogController;
import cn.ymex.notice.dialog.PopupDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PopupDialog.create(MainActivity.this)
                        .controller(AlertDailogController
                                .message("这是一个呵呵")
                                .title("广而告之"))
                        .show();


            }
        });

    }
}
