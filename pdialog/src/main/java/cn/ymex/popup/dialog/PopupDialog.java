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

package cn.ymex.popup.dialog;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AnimRes;
import android.support.annotation.AttrRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import java.lang.ref.SoftReference;

import cn.ymex.popup.R;
import cn.ymex.popup.compat.Tool;
import cn.ymex.popup.controller.DialogControllable;

/**
 * popupwindow
 */

public class PopupDialog extends PopupWindow implements DialogManager.Priority {

    private static final int MESSAGE_DISMISS = 0x00;


    private ViewGroup mRootView;
    private View contextView;
    private Context mContext;
    private TimeHandler timeHandler;
    private DialogControllable dialogControllable;

    private Animation inAnimation;
    private Animation outAnimation;

    private int animationStyleRes;
    private int mPriority = 0;//弹出优先级

    private boolean outsideTouchHide = true;
    private boolean backPressedHide = true;
    private long dismissTime = -1;
    private OnShowListener onShowListener;


    private PopupDialog(Context context) {
        this(context, -1);
    }


    private PopupDialog(Context context, @StyleRes int anim) {
        super(context);
        this.init(context, anim);
    }

    public static PopupDialog create(Context context) {
        if (!(context instanceof Activity)) {
            throw new IllegalArgumentException("context is an invalid type!");
        }
        return new PopupDialog(context);
    }

    public static PopupDialog create(Fragment fragment) {
        return create(fragment.getActivity());
    }

    public static PopupDialog create(android.app.Fragment fragment) {
        return create(fragment.getActivity());
    }

    private void init(Context context, @StyleRes int anim) {
        this.mContext = context;
        this.timeHandler = new TimeHandler(this);
        this.mRootView = new DocFrameLayout(context);

        this.setAnimationStyle(anim);
        this.setContentView(this.mRootView);
        this.setClippingEnabled(false);
        this.setWidth(FrameLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(FrameLayout.LayoutParams.MATCH_PARENT);

        this.inAnimation = AnimationUtils.loadAnimation(context, R.anim.pulse_modal_in);
        this.outAnimation = AnimationUtils.loadAnimation(context, R.anim.pulse_modal_out);

        this.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#66000000")));

        this.setOutsideTouchable(backPressedHide);
        this.setFocusable(backPressedHide);

        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        this.getContentView().setFocusableInTouchMode(true);

        this.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (outsideTouchHide) {
                    dismiss();
                }
            }
        });

    }

    public PopupDialog clippingEnabled(boolean able) {
        this.setClippingEnabled(able);
        return this;
    }

    /**
     * 显示一定时间后消失
     *
     * @param time ms
     * @return this
     */
    public PopupDialog dismissTime(long time) {
        this.dismissTime = time;
        return this;
    }

    /**
     * 添加布局
     *
     * @param view 布局
     */
    public void addView(View view) {
        if (view == null) {
            throw new IllegalArgumentException("PopupDialog content createView cont allow null!");
        }
        this.mRootView.removeAllViews();
        this.contextView = view;
        view.setVisibility(View.VISIBLE);
        view.setClickable(true);
        this.mRootView.addView(view);
    }

    /**
     * 移除布局
     */
    public void removeCurrentView() {
        if (this.contextView != null) {
            this.mRootView.removeView(this.contextView);
        }
    }

    /**
     * 当前布局
     *
     * @return 当前布局
     */
    public View getCurrentView() {
        return this.contextView;
    }

    /**
     * 获取组件
     *
     * @param id
     * @param <T>
     * @return 查找不到为 null
     */
    public <T extends View> T find(@IdRes int id) {
        return (T) contextView.findViewById(id);
    }

    /**
     * 添加布局
     *
     * @param layout 布局
     * @return
     */
    public PopupDialog view(@LayoutRes int layout) {
        return view(layout, null);
    }

    /**
     * 添加布局
     *
     * @param layout             布局
     * @param onBindViewListener 添加布局后回调
     * @return PopupDialog
     */
    public PopupDialog view(@LayoutRes int layout, OnBindViewListener onBindViewListener) {
        return view(LayoutInflater.from(this.mContext).inflate(layout, this.mRootView, false), onBindViewListener);
    }

    /**
     * @param controlable
     * @return
     */
    public PopupDialog controller(DialogControllable controlable) {
        if (controlable == null) {
            return this;
        }
        dialogControllable = controlable;
        return view(controlable.createView(this.mContext, this.mRootView), controlable.bindView());
    }


    public PopupDialog view(View view) {
        return view(view, null);
    }


    public PopupDialog view(View view, OnBindViewListener onBindViewListener) {
        this.addView(view);
        setBindViewListener(onBindViewListener);
        return this;
    }

    public PopupDialog width(int w) {
        this.setWidth(w);
        return this;
    }

    public PopupDialog height(int h) {
        this.setHeight(h);
        return this;
    }


    /**
     * 动画
     *
     * @param animationStyle animationStyle
     * @return
     */
    public PopupDialog animationStyle(@StyleRes int animationStyle) {
        super.setAnimationStyle(animationStyle);
        return this;
    }

    /**
     * show 动画
     *
     * @param anim
     * @return
     */
    public PopupDialog animationIn(@AnimRes int anim) {
        this.inAnimation = AnimationUtils.loadAnimation(this.mContext, anim);
        return this;
    }

    /**
     * dismiss 动画
     *
     * @param anim
     * @return
     */
    public PopupDialog animationOut(@AnimRes int anim) {
        //this.setAnimationStyle(anim);
        this.outAnimation = AnimationUtils.loadAnimation(this.mContext, anim);
        return this;
    }

    /**
     * 全屏背景
     *
     * @param drawable
     * @return
     */
    public PopupDialog backgroundDrawable(Drawable drawable) {
        this.setBackgroundDrawable(drawable);
        return this;
    }

    /**
     * 点击外部 是否隐藏
     *
     * @param touchable true hide
     * @return PopupDialog
     */
    public PopupDialog outsideTouchHide(boolean touchable) {
        this.outsideTouchHide = touchable;
        return this;
    }


    /**
     * 返回键是否隐藏
     *
     * @param backPressedHide true hide
     * @return PopupDialog
     */
    public PopupDialog backPressedHide(boolean backPressedHide) {
        this.backPressedHide = backPressedHide;
        this.setOutsideTouchable(this.backPressedHide);
        this.setFocusable(this.backPressedHide);
        return this;
    }

    public PopupDialog focusable(boolean focus) {
        this.setFocusable(focus);
        return this;
    }


    /**
     * 不建议使用，outsideTouchHide ,backPressedHide
     *
     * @param focusable
     * @deprecated
     */
    @Override
    public void setFocusable(boolean focusable) {
        super.setFocusable(focusable);
    }

    /**
     * 不建议使用，outsideTouchHide ,backPressedHide
     *
     * @param touchable
     * @deprecated
     */
    @Override
    public void setOutsideTouchable(boolean touchable) {
        super.setOutsideTouchable(touchable);
    }


    /**
     * 设置 OnDismissListener
     *
     * @param onDismissListener Listener
     * @return PopupDialog
     */
    public PopupDialog onDismissListener(OnDismissListener onDismissListener) {
        setOnDismissListener(onDismissListener);
        return this;
    }

    /**
     * 设置 OnShowListener
     *
     * @param onShowListener Listener
     */
    public void setOnShowListener(OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
    }


    /**
     * 设置 OnShowListener
     *
     * @param onShowListener Listener
     * @return PopupDialog
     */
    public PopupDialog onShowListener(OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
        return this;
    }


    public void show() {
        selfShow();
    }


    /**
     * 以管理的方式展示
     *
     * @param manager
     */
    public void managerShow(DialogManager manager) {
        if (manager == null) {
            throw new IllegalArgumentException("DialogManager is null object!");
        }
        DialogManager.Priority p = manager.getPriority(this);
        if (p == null) {
            throw new IllegalArgumentException("this PopupDialog is not in the DialogManager");
        }

        manager.show(this);
    }

    private void selfShow() {
        showAtLocation(getContentView(), ViewGroup.LayoutParams.MATCH_PARENT, 0, 0);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        showAsDropDown(anchor, xoff, yoff, Gravity.TOP | Gravity.START);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (onShowListener != null) {
            onShowListener.onShow(this);
        }
        super.showAtLocation(parent, gravity, x, y);
        afterShow();
    }

    private void afterShow() {
        if (dismissTime > 0) {
            this.timeHandler.sendEmptyMessageDelayed(MESSAGE_DISMISS, dismissTime);
        }
        if (inAnimation != null) {
            contextView.startAnimation(inAnimation);
        }
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (onShowListener != null) {
            onShowListener.onShow(this);
        }
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        afterShow();
    }

    public PopupDialog manageMe(DialogManager manager) {
        manager.add(this);
        return this;
    }


    @Override
    public void dismiss() {
        outAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                PopupDialog.super.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if (outAnimation != null) {
            this.contextView.startAnimation(outAnimation);
        }
    }

    /**
     * hase no Animation dismiss
     */
    public void forceDismiss() {
        super.dismiss();
    }

    /**
     * 点击事件（默认关闭弹窗）
     *
     * @param id       组件id
     * @param listener 监听
     * @return this
     */
    public PopupDialog click(@IdRes int id, OnClickListener listener) {
        return click(id, listener, true);
    }

    /**
     * 点击事件
     *
     * @param id       组件id
     * @param listener 监听
     * @param dismiss  是否关闭弹窗
     * @return this
     */
    public PopupDialog click(@IdRes int id, final OnClickListener listener, final boolean dismiss) {

        find(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(PopupDialog.this, contextView, view);
                }
                if (dismiss) {
                    dismiss();
                }
            }

        });

        return this;
    }

    /**
     * 销毁popupdialog的一切
     */
    public void destroy() {
        removeCurrentView();
        mRootView.removeAllViews();
        contextView.destroyDrawingCache();
        contextView = null;
        mRootView = null;
        mContext = null;
    }

    private void setBindViewListener(OnBindViewListener onBindViewListener) {
        if (onBindViewListener != null) {
            onBindViewListener.onCreated(this, contextView);
        }
    }

    public PopupDialog priority(int mPriority) {
        this.mPriority = mPriority;
        return this;
    }

    @Override
    public int priority() {
        return this.mPriority;
    }

    @Override
    public boolean isWorking() {
        return isShowing();
    }

    @Override
    public void hideAway() {
        if (isShowing()) {
            this.dismiss();
        }
    }

    @Override
    public void display() {
        if (!isShowing()) {
            selfShow();
        }
    }

    public DialogControllable getDialogControllable() {
        return dialogControllable;
    }

    public interface OnBindViewListener {
        void onCreated(PopupDialog dialog, View layout);
    }


    public interface OnShowListener {
        void onShow(PopupDialog var1);
    }

    public interface OnClickListener {
        void onClick(PopupDialog dialog, View layout, View view);
    }

    private static class TimeHandler extends Handler {
        SoftReference<PopupDialog> refDialog;

        public TimeHandler(PopupDialog dialog) {
            this.refDialog = new SoftReference<PopupDialog>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MESSAGE_DISMISS && refDialog.get() != null && refDialog.get().isShowing()) {
                refDialog.get().dismiss();
            }
        }
    }

    class DocFrameLayout extends FrameLayout {


        public DocFrameLayout(@NonNull Context context) {
            super(context);
        }

        public DocFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public DocFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public DocFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

    }

    /**
     * 不遮挡虚拟导航（华为等带有虚拟导航的机型）
     * @param act
     */
    public void compatShow(Activity  act) {
        this.showAtLocation(act.getWindow().getDecorView(), Gravity.BOTTOM, 0, Tool.getNavigationBarHeight(act));
    }
}

