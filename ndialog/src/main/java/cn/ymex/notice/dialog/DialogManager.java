package cn.ymex.notice.dialog;

import android.util.Log;

import java.util.LinkedList;

/**
 * Dialog 管理类
 */

public class DialogManager {

    private static final String TAG = "DialogManager";

    private LinkedList<Priority> dialogs;

    public DialogManager() {
        getDialogs();
    }

    public LinkedList<Priority> getDialogs() {
        if (dialogs == null) {
            dialogs = new LinkedList<>();
        }
        return dialogs;
    }


    /**
     * 加入 DialogManager 管理中，优先级重复将替换。
     *
     * @param dialog
     * @return
     */
    public DialogManager add(Priority dialog) {

        if (dialogs.contains(dialog)) {
            return this;
        }
        int index = -1;
        for (int i = 0; i < getDialogs().size(); i++) {

            if (dialog.priority() >= getDialogs().get(i).priority()) {
                index = i;
                break;
            }

        }

        if (index >= 0) {
            if (dialog.priority() == getDialogs().get(index).priority()) {
                Log.e(TAG, "already have a same priority! priority = " + dialog.priority());
                getDialogs().set(index, dialog);
            } else {
                getDialogs().add(index, dialog);
            }
        } else {
            getDialogs().add(dialog);
        }


        return this;
    }

    public void show(int priority) {
        Priority p = getPriority(priority);
        if (null == p) {
            return;
        }
        show(p);
    }


    /**
     * 显示
     *
     * @param dialog
     */
    public void show(Priority dialog) {
        int cp = -1;
        for (Priority pr : getDialogs()) {
            if (pr.isWorking()) {
                cp = pr.priority();
            }
        }
        if (cp < 0) {
            if (!dialog.isWorking()) {
                dialog.display();
            }
        } else {
            Priority p = getPriority(cp);
            if (p.priority() > dialog.priority()) {
                if (!p.isWorking()) {
                    p.display();
                }
            } else {
                p.hideAway();
                dialog.display();
            }
        }
    }

    public Priority getPriority(int priority) {
        for (Priority p : getDialogs()) {
            if (p.priority() == priority) {
                return p;
            }
        }
        return null;
    }

    /**
     * 优先级
     */
    public interface Priority {
        int priority();//级别

        boolean isWorking();//是否正在显示

        void hideAway();//隐藏

        void display();//显示
    }

    public void destroy() {
        for (Priority priority : getDialogs()) {
            if (priority.isWorking()) {
                priority.hideAway();
            }
        }
        getDialogs().clear();
        dialogs = null;
    }
}
