package cn.ymex.popup.controller;

import android.content.Context;
import android.support.annotation.IntDef;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

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

    private int length = Toast.LENGTH_SHORT;


    @IntDef({Toast.LENGTH_SHORT, Toast.LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    protected @interface Duration {
    }

    @Override
    public View createView(Context cotext, ViewGroup parent) {
        return LayoutInflater.from(cotext).inflate(R.layout.notice_text_toast, parent, false);
    }

    public ToastController setMessage(String message) {
        this.message = message;
        return this;
    }

    public ToastController setMessage(String message, @Duration int length) {
        this.message = message;
        this.length = length;
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
                dialog.outsideTouchHide(true);
                dialog.backPressedHide(true);
                //setPopupWindowTouchModal(dialog,true);
                if (length == Toast.LENGTH_LONG) {
                    dialog.dismissTime((long) (3.5 * 1000));
                } else {
                    dialog.dismissTime((long) (2 * 1000));
                }
                TextView tv = (TextView) layout.findViewById(R.id.tv_toast);
                tv.setText(getMessage());
            }
        };
    }

    public static void setPopupWindowTouchModal(PopupWindow popupWindow,
                                                boolean touchModal) {
        if (null == popupWindow) {
            return;
        }
        Method method;
        try {
            method = PopupWindow.class.getDeclaredMethod("setTouchModal",
                    boolean.class);
            method.setAccessible(true);
            method.invoke(popupWindow, touchModal);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
