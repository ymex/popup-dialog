package com.sample.noticedialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.ymex.log.L;
import cn.ymex.notice.dialog.AlerController;
import cn.ymex.notice.dialog.DialogManager;
import cn.ymex.notice.dialog.PopupDialog;

public class MainActivity extends AppCompatActivity {

    DialogManager manager = new DialogManager();

    private static int FIRST_DIALOG = 11;
    private static int SECOND_DIALOG = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AlerController alerController2 = AlerController
                .build()
                .title("提醒")
                .message("登录后才能评论。")
                .negativeButton("取消", null)
                .positiveButton("确定", null);


        final AlerController alerController = AlerController
                .build()
                .clickDismiss(false)
                .title("提醒")
                .message("才能评论登录后才能评论登录后才能评论登录后才能评论登录后才能评论。")
                .positiveButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        manager.show(SECOND_DIALOG);
                    }
                });


        PopupDialog.create(MainActivity.this)
                .manageMe(manager)
                .priority(SECOND_DIALOG)
                .controller(alerController2);

        PopupDialog.create(MainActivity.this)
                .priority(FIRST_DIALOG)
                .manageMe(manager)
                .controller(alerController);


        findViewById(R.id.btn_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PopupDialog popupDialog = (PopupDialog) manager.getPriority(FIRST_DIALOG);
                AlerController aler = (AlerController) popupDialog.getDialogControlable();
                aler.title("提示")
                        .message("如果你要调整布局，这时你要重新设置一下popupdialog controller 或 view ");
                popupDialog.controller(aler);

                manager.show(popupDialog);

            }
        });

        L.tag("---------:::").logD(manager.getDialogs().size());


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.destroy();
    }
}
