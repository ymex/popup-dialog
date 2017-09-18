package cn.ymex.popup.controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import cn.ymex.popup.dialog.PopupDialog;

/**
 * Created by ymex on 2017/9/13.
 */

public interface DialogControllable {
    View createView(Context cotext, ViewGroup parent);

    PopupDialog.OnBindViewListener bindView();
}
