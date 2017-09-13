package cn.ymex.notice.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * 默认 Alert Dialog
 *
 */

public class AlerController implements PopupDialog.DailogControlable {

    private String mMsg;
    private String mTitle;
    private String positiveName;
    private String negativeName;

    private View.OnClickListener positiveListener;
    private View.OnClickListener negativeListener;

    private int titleGravity = Gravity.LEFT;
    private int messageGravity = Gravity.LEFT;

    private boolean dismiss = true;

    private AlerController() {

    }

    public static AlerController build() {
        return new AlerController();
    }

    public AlerController message(String message) {
        this.mMsg = message;
        return this;
    }

    public AlerController title(String title) {
        this.mTitle = title;
        return this;
    }

    public AlerController titleGravity(int titleGravity) {
        this.titleGravity = titleGravity;
        return this;
    }

    public AlerController messageGravity(int messageGravity) {
        this.messageGravity = messageGravity;
        return this;
    }

    //positive
    public AlerController negativeButton(String text, View.OnClickListener listener) {
        this.negativeName = text;
        this.negativeListener = listener;
        return this;
    }

    public AlerController positiveButton(String text, View.OnClickListener listener) {
        this.positiveName = text;
        this.positiveListener = listener;
        return this;
    }

    @Override
    public View createView(Context context,ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.notice_alert_dialog, parent, false);
    }

    @Override
    public PopupDialog.OnBindViewListener bindView(final PopupDialog dialog) {
        return new PopupDialog.OnBindViewListener() {
            @Override
            public void onCreated(View layout) {
                if (!TextUtils.isEmpty(mTitle)) {
                    TextView tvTitle = layout.findViewById(R.id.notice_dialog_title);
                    tvTitle.setGravity(titleGravity);
                    tvTitle.setText(mTitle);
                    tvTitle.setVisibility(View.VISIBLE);
                }
                TextView tvMessage = layout.findViewById(R.id.notice_dialog_message);
                tvMessage.setGravity(messageGravity);
                tvMessage.setText(TextUtils.isEmpty(mMsg)?"":mMsg);

                if (!TextUtils.isEmpty(negativeName)) {
                    final Button btnNegative = layout.findViewById(R.id.notice_dialog_button_cancel);
                    View line = layout.findViewById(R.id.notice_dialog_divier_line);
                    line.setVisibility(View.VISIBLE);
                    btnNegative.setVisibility(View.VISIBLE);
                    btnNegative.setText(negativeName);
                    btnNegative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (negativeListener != null) {
                                negativeListener.onClick(btnNegative);
                            }
                            if (dismiss) {
                                dialog.dismiss();
                            }
                        }
                    });

                }

                final Button btnPositive = layout.findViewById(R.id.notice_dialog_button_ok);
                if (!TextUtils.isEmpty(positiveName)) {
                    btnPositive.setText(positiveName);
                }
                btnPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (positiveListener != null) {
                            positiveListener.onClick(btnPositive);
                        }
                        if (dismiss) {
                            dialog.dismiss();
                        }
                    }
                });

            }
        };
    }
}
