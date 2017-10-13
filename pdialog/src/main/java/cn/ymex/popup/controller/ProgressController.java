/*
 * Copyright (C) 2016 ymex(www.ymex.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Email:ymex@foxmail.com
 */

package cn.ymex.popup.controller;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.ymex.popup.dialog.PopupDialog;
import cn.ymex.popup.R;

/**
 * progress dialog
 */

public class ProgressController implements DialogControllable {
    public static final int MODE_CIRCLE = 0x0;//android 4.0+ progressbar
    public static final int MODE_DOT = 0x1;
    public static final int MODE_SPOT = 0x2;

    private int mode = MODE_CIRCLE;
    private String message;

    private ProgressController() {
        super();
    }

    public static ProgressController build() {
        return new ProgressController();
    }

    public ProgressController message(String message) {
        this.message = message;
        return this;
    }

    /**
     * @param model
     * @return
     */
    public ProgressController model(int model) {
        this.mode = model;
        return this;
    }

    @Override
    public View createView(Context cotext, ViewGroup parent) {
        return LayoutInflater.from(cotext).inflate(R.layout.notice_progress_dialog, parent, false);

    }

    @Override
    public PopupDialog.OnBindViewListener bindView() {

        return new PopupDialog.OnBindViewListener() {
            @Override
            public void onCreated(final PopupDialog dialog, View layout) {
                dialog.backgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                dialog.outsideTouchHide(false);
                dialog.backPressedHide(true);

                TextView tvTitle = layout.findViewById(R.id.notice_dialog_title);
                if (TextUtils.isEmpty(message)) {
                    tvTitle.setVisibility(View.GONE);
                } else {
                    tvTitle.setVisibility(View.VISIBLE);
                }
                tvTitle.setText(TextUtils.isEmpty(message) ? "" : message);
                View dotView = layout.findViewById(R.id.notice_dialog_dot_progress);
                View spotView = layout.findViewById(R.id.notice_dialog_spot_progress);
                View circleView = layout.findViewById(R.id.notice_dialog_circle_progress);

                switch (mode) {
                    case MODE_CIRCLE:
                        circleView.setVisibility(View.VISIBLE);
                        dotView.setVisibility(View.GONE);
                        spotView.setVisibility(View.GONE);
                        break;
                    case MODE_DOT:
                        dotView.setVisibility(View.VISIBLE);
                        circleView.setVisibility(View.GONE);
                        spotView.setVisibility(View.GONE);
                        break;
                    case MODE_SPOT:
                        spotView.setVisibility(View.VISIBLE);
                        circleView.setVisibility(View.GONE);
                        dotView.setVisibility(View.GONE);
                        break;
                }

            }
        };
    }
}
