package cn.ymex.popup.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.ymex.popup.R;
import cn.ymex.popup.dialog.PopupDialog;
import cn.ymex.popup.widget.TransparentDrawable;

/**
 * Created by ymexc on 2017/11/20.
 * About:toast
 */

public class ToastController implements DialogControllable {

    private ToastController() {
        super();
    }

    public static ToastController build() {
        return new ToastController();
    }


    private String message;

    @Override
    public View createView(Context cotext, ViewGroup parent) {
        return LayoutInflater.from(cotext).inflate(R.layout.notice_text_toast, parent, false);
    }

    public ToastController setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public PopupDialog.OnBindViewListener bindView() {
        return new PopupDialog.OnBindViewListener() {
            @Override
            public void onCreated(PopupDialog dialog, View layout) {
                dialog.backgroundDrawable(new TransparentDrawable());
//                dialog.backPressedHide(true);
//                dialog.outsideTouchHide(true);

                TextView tv = (TextView) layout.findViewById(R.id.tv_toast);
                tv.setText(getMessage());
            }
        };
    }
}
