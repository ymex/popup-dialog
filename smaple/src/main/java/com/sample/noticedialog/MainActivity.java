package com.sample.noticedialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.ymex.notice.dialog.AlerController;
import cn.ymex.notice.dialog.PopupDialog;

public class MainActivity extends AppCompatActivity {

    AlerController alerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alerController = AlerController
                .build()
                .title("提醒")
                .message("登录后才能评论。")
                .negativeButton("取消", null)
                .positiveButton("确定", null);


        findViewById(R.id.btn_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PopupDialog.create(MainActivity.this)
                        //.animationIn(R.anim.pulse_modal_in)
                        //.backgroundDrawable(new ColorDrawable(Color.parseColor("#66000000")))
                        .controller(alerController)
                        .show();
//                    dialog.show();
            }
        });



    }
}
