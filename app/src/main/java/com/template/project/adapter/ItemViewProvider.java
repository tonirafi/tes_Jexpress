package com.template.project.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Created by 5Mall<zhangwei> on 2018/7/28
 * Email:zhangwei@qingsongchou.com
 * 描述：
 */
public abstract class ItemViewProvider<C extends BaseCard, V extends CommonVh<C>> {
    protected CardAdapter.OnItemClickListener mOnItemClickListener;

    public ItemViewProvider(CardAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @NonNull
    public abstract V onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent);

//    public void onBindViewHolder(@NonNull V holder, @NonNull C card) {
//        holder.bind(card);
//    }

    public void onBindViewHolder(@NonNull V holder, @NonNull C card, List payloads) {
        if (payloads == null || payloads.isEmpty()) {
            holder.bind(card);
        } else {
            holder.bind(card, payloads);
        }
    }
}

