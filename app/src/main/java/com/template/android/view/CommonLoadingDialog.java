package com.template.android.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.template.android.R;

/**
 * Created by 5Mall<zhangwei> on 2018/11/9
 * Email:zhangwei@qingsongchou.com
 * 描述：
 */
public class CommonLoadingDialog extends Dialog {
    private AnimationDrawable animation;
    private ImageView loading;
    private TextView loadingText;
    private Context mContext;

    public CommonLoadingDialog(Context context) {
        super(context, R.style.CommonDialog);
        this.mContext = context;
    }

    private void initWindow() {
        Window window = this.getWindow();
        if (window == null) return;
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = 17;
        window.setAttributes(params);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initWindow();
        View view = this.getLayoutInflater().inflate(R.layout.layout_common_dialog, null);
        this.initView(view);
        this.setContentView(view);
    }

    private void initView(View view) {
        this.loading = view.findViewById(R.id.loading);
        this.loadingText = view.findViewById(R.id.loadingText);
        this.animation = (AnimationDrawable) this.loading.getDrawable();
    }

    public void show() {
        if (this.mContext instanceof Activity) {
            Activity activity = (Activity) this.mContext;
            if (!activity.isFinishing()) {
                super.show();
                if (this.loading != null && this.animation != null) {
                    this.loading.clearAnimation();
                    this.animation.start();
                }
            }
        }

    }

    public void dismiss() {
        if (this.isShowing()) {
            if (this.animation != null) {
                this.animation.stop();
            }

            if (this.loading.getAnimation() != null) {
                this.loading.clearAnimation();
            }

            super.dismiss();
        }

    }

    public void show(String text) {
        this.show();
        this.loadingText.setText(text);
    }
}