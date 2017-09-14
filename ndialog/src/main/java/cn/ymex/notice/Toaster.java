package cn.ymex.notice;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.

 * Email:ymex@foxmail.com  (www.ymex.cn)
 *
 * @author ymex
 * date: 16/4/23
 * Toast 单例toast
 */
public final class Toaster {

    private static Context sApplication;
    private static Toast mToast;


    public static void init(Context context){
        if (null == sApplication){
            if (context instanceof Activity) {
                sApplication = ((Activity) context).getApplication();
            }else {
                sApplication = context;
            }
        }
    }

    /**
     * 多次弹出时取消上次弹出，最后一次弹出为准
     * @param message
     */
    public static void show(String message){
        mToast = toast();
        mToast.setView(defaultView());
        mToast.setText(message);
        mToast.show();
    }

    /**
     * 多次弹出时取消上次弹出，最后一次弹出为准
     * @param resId
     */
    public static void show(int resId){
        mToast = toast();
        mToast.setView(defaultView());
        mToast.setText(resId);
        mToast.show();
    }

    /**
     * 显示自定义视图
     * @param view
     */
    public static void show(View view){
        mToast = toast();
        mToast.setView(view);
        mToast.show();
    }

    /**
     * 加载自定义视图xml布局
     * @param layoutId
     * @return
     */
    public static View inflate(int layoutId){
        LayoutInflater inflate =(LayoutInflater)sApplication.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(layoutId, null);
        return v;
    }


    /**
     * 生成toast
     * @return
     */

    private static Toast toast(){
        if (mToast==null){
            mToast= Toast.makeText(sApplication,"",Toast.LENGTH_SHORT);
        }
        return mToast;
    }
    /**
     * 得到系统默认文字示图
     * @return
     */
    private static View defaultView(){
        return Toast.makeText(sApplication,"",Toast.LENGTH_SHORT).getView();
    }


}
