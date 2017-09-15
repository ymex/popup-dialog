package cn.ymex.notice.dialog.controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import cn.ymex.notice.dialog.NoticeDialog;

/**
 * Created by ymex on 2017/9/13.
 */

public interface DialogControllable {
    View createView(Context cotext, ViewGroup parent);

    NoticeDialog.OnBindViewListener bindView();
}
