package com.sample.noticedialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.ymex.notice.dialog.DialogManager;
import cn.ymex.notice.dialog.NoticeDialog;
import cn.ymex.notice.dialog.controller.AlertController;
import cn.ymex.notice.dialog.controller.ProgressController;

public class MainActivity extends AppCompatActivity {

    DialogManager manager = new DialogManager();

    private static int FIRST_DIALOG = 11;
    private static int SECOND_DIALOG = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AlertController alertController2 = AlertController
                .build()
                .title("提醒")
                .message("登录后才能评论。")
                .negativeButton("取消", null)
                .positiveButton("确定", null);


        final AlertController alertController = AlertController
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


        NoticeDialog.create(MainActivity.this)
                .manageMe(manager)
                .priority(SECOND_DIALOG)
                .controller(alertController2);

        NoticeDialog.create(MainActivity.this)
                .priority(FIRST_DIALOG)
                .manageMe(manager)
                .controller(alertController);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.destroy();//销毁
    }

    public void defalertPppDialog(View view) {
        NoticeDialog noticeDialog = (NoticeDialog) manager.getPriority(FIRST_DIALOG);
        AlertController aler = (AlertController) noticeDialog.getDialogControlable();
        aler.title("提示")
                .message("如果你要调整布局，这时你要重新设置一下popupdialog controller 或 view ");
        noticeDialog.controller(aler);
        manager.show(noticeDialog);
    }


    /**
     * 默认对话框
     */
    public void defalertDialog(View view) {
        NoticeDialog.create(this)
                .controller(AlertController.build()
                        .title("提醒")
                        .message("登录后才能评论。")
                        .negativeButton("取消", null)
                        .positiveButton("确定", null))
                .show();
    }

    /**
     * 默认进度框 ProgressController.MODE_CIRCLE
     * @param view
     */
    public void progressDialog(View view) {
        NoticeDialog.create(this)
                .controller(ProgressController.build().message("loading ... ")
                        .model(ProgressController.MODE_CIRCLE))
                .show();
    }

}
