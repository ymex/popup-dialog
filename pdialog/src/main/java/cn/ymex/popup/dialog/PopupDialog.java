package cn.ymex.popup.dialog;
/**
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 * <p>
 * Email:ymex@foxmail.com  (www.ymex.cn)
 * </p>
 *
 * @author ymex
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import cn.ymex.popup.R;
import cn.ymex.popup.controller.DialogControllable;

/**
 * popupwindow
 */

public class PopupDialog extends PopupWindow implements DialogManager.Priority {


    private ViewGroup mRootView;
    private View contextView;
    private Context mContext;

    private DialogControllable dialogControllable;

    private Animation inAnimation;
    private Animation outAnimation;
    private int mPriority = 0;//弹出优先级

    private boolean outsideTouchHide = true;
    private boolean backPressedHide = true;


    private PopupDialog(Context context) {
        this(context, R.anim.pulse_modal_in);
    }


    private PopupDialog(Context context, @AnimRes int anim) {
        super(context);
        this.init(context, anim);
    }


    private void init(Context context, @AnimRes int anim) {
        this.mContext = context;
        this.mRootView = new DocFrameLayout(context);


        this.setContentView(this.mRootView);
        this.setClippingEnabled(false);
        this.setWidth(FrameLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(FrameLayout.LayoutParams.MATCH_PARENT);
        try {
            this.inAnimation = AnimationUtils.loadAnimation(context, anim);
        } catch (Exception e) {
            this.inAnimation = AnimationUtils.loadAnimation(context, R.anim.pulse_modal_in);
        }
        this.outAnimation = AnimationUtils.loadAnimation(context, R.anim.pulse_modal_out);
        this.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#66000000")));


        this.setOutsideTouchable(backPressedHide);
        this.setFocusable(backPressedHide);


        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        this.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (outsideTouchHide) {
                    dismiss();
                }
            }
        });


        //this.mRootView.setFocusable(true);
        this.getContentView().setFocusableInTouchMode(true);

    }


    public PopupDialog clippingEnabled(boolean able) {
        this.setClippingEnabled(able);
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
        this.showAtLocation(mRootView, ViewGroup.LayoutParams.MATCH_PARENT, 0, 0);
        this.contextView.startAnimation(inAnimation);
    }


    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (onShowListener != null) {
            onShowListener.onShow(this);
        }
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (onShowListener != null) {
            onShowListener.onShow(this);
        }
        super.showAsDropDown(anchor, xoff, yoff, gravity);
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
        this.contextView.startAnimation(outAnimation);
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
                    listener.onClick(contextView, view);
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

    public interface OnBindViewListener {
        void onCreated(PopupDialog dialog, View layout);
    }

    public DialogControllable getDialogControllable() {
        return dialogControllable;
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


    private OnShowListener onShowListener;

    public interface OnShowListener {
        void onShow(PopupDialog var1);
    }


    public interface OnClickListener {
        void onClick(View layout, View view);
    }
}

