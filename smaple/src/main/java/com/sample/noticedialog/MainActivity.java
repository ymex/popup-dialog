package com.sample.noticedialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.ymex.notice.dialog.AlertDailogController;
import cn.ymex.notice.dialog.PopupDialog;

public class MainActivity extends AppCompatActivity {

    AlertDailogController alertDailogController ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alertDailogController = AlertDailogController
                .message("登录后才能评论。")
                .title("广而告之")
                .negativeButton("取消", null)
                .positiveButton("确定", null);

        findViewById(R.id.btn_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PopupDialog.create(MainActivity.this)
                        .backgroundDrawable(new ColorDrawable(Color.parseColor("#66000000")))
                        .controller(alertDailogController)
                        .show();

            }
        });
        new AlertDialog.Builder(this)
                .setTitle("广而告之")
                .setMessage("登录后才能评论")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", null).show();

    }
}
