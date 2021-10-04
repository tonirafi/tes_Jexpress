package com.template.project.adapter;

import android.view.View;

/**
 * Created by 5Mall<zhangwei> on 2019/1/11
 * Email:zhangwei@qingsongchou.com
 * 描述：
 */
public interface EditCardEventListener extends CardAdapter.OnItemClickListener {
    void onFocusChange(View v, View itemView, boolean hasFocus, int position);

    boolean popSelectPhotoTips(String key, boolean isFirst);

    void onTextChanged(View v, String text, int position);
}
