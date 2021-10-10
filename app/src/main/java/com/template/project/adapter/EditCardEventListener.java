package com.template.project.adapter;

import android.view.View;

public interface EditCardEventListener extends CardAdapter.OnItemClickListener {
    void onFocusChange(View v, View itemView, boolean hasFocus, int position);

    boolean popSelectPhotoTips(String key, boolean isFirst);

    void onTextChanged(View v, String text, int position);
}
