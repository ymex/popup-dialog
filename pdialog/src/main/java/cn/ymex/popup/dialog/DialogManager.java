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

import android.support.annotation.IntDef;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.LinkedList;


/**
 * Dialog 管理类
 */

public class DialogManager {

    public final static int MODEL_SINGLE_TOP = 0x0;
    public final static int MODEL_ONE_BY_ONE = 0x1;

    @IntDef({MODEL_SINGLE_TOP, MODEL_ONE_BY_ONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Model {
    }

    private static final String TAG = "DialogManager";

    private LinkedList<Priority> dialogs;
    private int showModel = MODEL_SINGLE_TOP;

    public DialogManager() {
        this(MODEL_SINGLE_TOP);
    }

    public DialogManager(@Model int model) {
        this.showModel = model;
        getDialogs();
    }

    private LinkedList<Priority> getDialogs() {
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

        switch (showModel) {
            case MODEL_SINGLE_TOP:
                _add_SingleTop(dialog);
                break;
            case MODEL_ONE_BY_ONE:
                _add_oneByone(dialog);
                break;
        }

        return this;
    }

    private void _add_oneByone(Priority dialog) {
        getDialogs().add(dialog);
    }

    private void _add_SingleTop(Priority dialog) {
        if (dialogs.contains(dialog)) {
            return;
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
    }


//    public synchronized void show() {
//        if (showModel != MODEL_ONE_BY_ONE || getDialogs().size() == 0) {
//            return;
//        }
//        Priority dialog = getDialogs().getFirst();
//        if (!dialog.isWorking()) {
//            dialog.display();
//        }
//    }

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

    public Priority getPriority(Priority priority) {
        for (Priority p : getDialogs()) {
            if (p == priority) {
                return p;
            }
        }
        return null;
    }

    /**
     * 优先级
     */
    public interface Priority {
        /**
         * 优先级
         *
         * @return 优先级
         */
        int priority();

        /**
         * 是否正在显示
         *
         * @return true 正在显示
         */
        boolean isWorking();

        /**
         * 隐藏窗口
         */
        void hideAway();

        /**
         * 显示窗口
         */
        void display();
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
