package com.template.project.adapter;

import android.view.View;

public class CommonVPVh<T extends BaseCard> extends CommonVh<T> {

    protected int position = -1;

    public CommonVPVh(View itemView, CardAdapter.OnItemClickListener onItemClickListener) {
        super(itemView, onItemClickListener);
    }

    protected void onItemViewClick(View v) {
        super.onItemViewClick(v);
        if (getAdapterPosition() < 0 && onItemClickListener != null && position >= 0) {
            onItemClickListener.onItemOnclick(position);
        }
    }

    public void setPosition(int position) {
        this.position = position;
    }
}