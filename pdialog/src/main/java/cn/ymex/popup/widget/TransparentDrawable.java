package cn.ymex.popup.widget;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

/**
 * Created by ymexc on 2017/11/20.
 * About:Transparent color drawable
 */

public class TransparentDrawable extends ColorDrawable {
    public TransparentDrawable() {
        this(Color.parseColor("#00000000"));
    }

    public TransparentDrawable(int color) {
        super(color);
    }
}
