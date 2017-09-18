package com.sample.noticedialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

import cn.ymex.popup.dialog.DialogManager;
import cn.ymex.popup.dialog.PopupDialog;
import cn.ymex.popup.controller.AlertController;
import cn.ymex.popup.controller.DialogControllable;
import cn.ymex.popup.controller.ProgressController;

public class MainActivity extends AppCompatActivity {

    DialogManager manager = new DialogManager();

    private static int FIRST_DIALOG = 11;
    private static int SECOND_DIALOG = 22;
    private static int THREE_DIALOG = 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AlertController alertController3 = AlertController
                .build()
                .clickDismiss(false)//点击不消失
                .title("提醒")
                .message("当前对话框(优先级：" + THREE_DIALOG + ") 点击确定后，新的对话框(优先级：" + SECOND_DIALOG + ")不会被展示。")
                .positiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        manager.show(SECOND_DIALOG);
                    }
                });

        final AlertController alertController2 = AlertController
                .build()
                .title("提醒")
                .message("登录后才能评论。")
                .negativeButton("取消", null)
                .positiveButton("确定", null);


        final AlertController alertController = AlertController
                .build()
                .clickDismiss(false)//点击不消失
                .title("提醒")
                .message("才能评论登录后才能评论登录后才能评论登录后才能评论登录后才能评论。")
                .positiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        manager.show(SECOND_DIALOG);

                    }
                });


        PopupDialog.create(MainActivity.this)
                .manageMe(manager)
                .priority(SECOND_DIALOG)
                .controller(alertController2);

        PopupDialog.create(MainActivity.this)
                .priority(FIRST_DIALOG)
                .manageMe(manager)
                .controller(alertController);
        PopupDialog.create(this)
                .manageMe(manager)
                .priority(THREE_DIALOG)
                .controller(alertController3);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.destroy();//销毁
    }


    /**
     * 默认对话框
     */
    public void defalertDialog(View view) {
        PopupDialog.create(this)
                .controller(AlertController.build()
                        .title("提醒")
                        .message("登录后才能评论。")
                        .negativeButton("取消", null)
                        .positiveButton("确定", null))
                .show();
    }

    /**
     * 默认进度框 ProgressController.MODE_CIRCLE
     *
     * @param view
     */
    public void progressDialog(View view) {

        int mode = ProgressController.MODE_CIRCLE;


        Random random = new Random();
        int num = random.nextInt(3);
        switch (num) {
            case 0:
                mode = ProgressController.MODE_CIRCLE;
                break;
            case 1:
                mode = ProgressController.MODE_DOT;
                break;
            case 2:
                mode = ProgressController.MODE_SPOT;
                break;
        }
        PopupDialog.create(this)
                .controller(ProgressController.build().message("loading ... ")
                        .model(mode)).backgroundDrawable(new ColorDrawable(Color.parseColor("#66000000")))
                .show();
    }


    /**
     * 高级别Dialog 请求弹出
     *
     * @param view
     */
    public void onLargePriorityClick(View view) {
        //修改DialogController
        PopupDialog popupDialog = (PopupDialog) manager.getPriority(FIRST_DIALOG);

        AlertController alert = (AlertController) popupDialog.getDialogControllable();
        alert.title("提示").message("当前对话框(优先级：" + popupDialog.priority() + ") 点击确定后，新的对话框(优先级：" + SECOND_DIALOG + ")将取代本对话框。");

        popupDialog.controller(alert);
        popupDialog.managerShow(manager);//等同 manager.show(FIRST_DIALOG); 或 manager.show(popupDialog);
    }

    /**
     * 低级别Dialog 请求弹出
     *
     * @param view
     */
    public void onSmallPriorityClick(View view) {

        //弹出

        manager.show(THREE_DIALOG); //等同 manager.show(noticeDialog);
        //亦可使用以为弹出方式
        //PopupDialog noticeDialog = (PopupDialog) manager.getPriority(THREE_DIALOG);
        //noticeDialog.managerShow(manager);


    }

    /**
     * 定义DialogController
     *
     * @param view v
     */
    public void onCustomViewClick(View view) {
        PopupDialog.create(this)
                .controller(new CustomDialogController()).show();
    }


    class CustomDialogController implements DialogControllable {
        @Override
        public View createView(Context cotext, ViewGroup parent) {
            return LayoutInflater.from(cotext).inflate(R.layout.dialog_view, parent, false);
        }

        @Override
        public PopupDialog.OnBindViewListener bindView() {



            return new PopupDialog.OnBindViewListener() {
                @Override
                public void onCreated(PopupDialog dialog, final View layout) {

                    dialog.backPressedHide(true);
                    dialog.outsideTouchHide(false);

                    dialog.backgroundDrawable(new ColorDrawable(Color.parseColor("#66000000")));

                    layout.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText etNumber = layout.findViewById(R.id.et_number);
                            Toast.makeText(MainActivity.this, "兑换码为：" + etNumber.getText(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            };
        }
    }

    /**
     * 不 定义DialogController
     *
     * @param view v
     */
    public void onCustomXmlViewClick(View view) {
        PopupDialog.create(this)
                //设置使用的view xml
                .view(R.layout.dialog_view, new PopupDialog.OnBindViewListener() {
                    @Override
                    public void onCreated(PopupDialog dialog, View layout) {
                        //初始化控件
                        EditText etNumber = layout.findViewById(R.id.et_number);
                        etNumber.setText("100866");
                    }
                })
                //点击事件
                .click(R.id.btn_submit, new PopupDialog.OnClickListener() {
                    @Override
                    public void onClick(View layout, View view) {

                        EditText etNumber = layout.findViewById(R.id.et_number);
                        Toast.makeText(MainActivity.this, "兑换码为：" + etNumber.getText(), Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }
}
