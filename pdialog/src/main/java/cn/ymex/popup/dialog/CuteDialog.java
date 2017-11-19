package cn.ymex.popup.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by ymex on 2017/11/19.
 * About: dialog
 */

public class CuteDialog extends Dialog implements DialogManager.Priority {
    public CuteDialog(@NonNull Context context) {
        super(context);
    }

    public CuteDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CuteDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public boolean isWorking() {
        return this.isShowing();
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
            this.show();
        }
    }
}
