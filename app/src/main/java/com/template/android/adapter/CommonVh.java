package com.template.android.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.template.android.router.IntentUtil;

import java.util.List;

import butterknife.ButterKnife;
import me.samlss.broccoli.Broccoli;
import me.samlss.broccoli.BroccoliGradientDrawable;
import me.samlss.broccoli.PlaceholderParameter;


public class CommonVh<T extends BaseCard> extends RecyclerView.ViewHolder {

    protected T baseCard;
    protected boolean init = false;

    protected CardAdapter.OnItemClickListener onItemClickListener;

    public CommonVh(View itemView) {
        this(itemView, null);
    }

    public CommonVh(View itemView, CardAdapter.OnItemClickListener onItemClickListener) {
        super(itemView);
        this.onItemClickListener = onItemClickListener;
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this::onItemViewClick);
    }

    protected void onItemViewClick(View v) {
        if (onItemClickListener != null && getAdapterPosition() >= 0) {
            onItemClickListener.onItemOnclick(getAdapterPosition());
        }

        if (baseCard != null && !TextUtils.isEmpty(baseCard.uri)) {
            IntentUtil.intentToUri(v.getContext(), baseCard.uri, baseCard.uriRequestCode);
        }
    }

    public void setSelected(boolean b) {

    }

    public void bind(T card) {
        this.baseCard = card;
        setPadding(card.padding);
        setMargin(card.margin);
        if (itemView.getTag() instanceof Broccoli) {
            ((Broccoli) itemView.getTag()).removeAllPlaceholders();
        }
    }

    /**
     * 展示占位图加载模式
     *
     * @param excludeViews 不显示占位图的view
     */
    protected void showPlaceholders(View... excludeViews) {
        Broccoli broccoli = new Broccoli();
        showPlaceholders(broccoli, itemView);

        if (excludeViews != null) {
            for (int i = 0; i < excludeViews.length; i++) {
                removePlaceholder(broccoli, excludeViews[i]);
            }
        }

        broccoli.show();
        itemView.setTag(broccoli);
    }

    private void removePlaceholder(Broccoli broccoli, View v) {
        if (v instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) v).getChildCount(); i++) {
                removePlaceholder(broccoli, ((ViewGroup) v).getChildAt(i));
            }
        } else {
            broccoli.removePlaceholder(v);
        }
    }

    private void showPlaceholders(Broccoli broccoli, View view) {
        if (view.getVisibility() != View.VISIBLE) return;

        if (view instanceof ViewGroup) {
            final ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                showPlaceholders(broccoli, vp.getChildAt(i));
            }
        } else {
            broccoli.addPlaceholder(new PlaceholderParameter.Builder()
                    .setView(view)
                    .setDrawable(new BroccoliGradientDrawable(Color.parseColor("#DDDDDD"),
                            Color.parseColor("#CCCCCC"), 0, 1000, new LinearInterpolator()))
                    .build());
        }
    }

    public void bind(T card, @NonNull List payloads) {
        this.baseCard = card;
    }

    protected void setPadding(BaseCard.Padding padding) {
        if (padding == null) {
            /** 针对背景图是 .9.png的情况 在此处调用的时候由于图片中包含的其padding数据有可能没有渲染进itemView 所以需要等itemView 包含padding数据的时候 进行操作 防止.9.png 图片中的padding 数据被覆盖  ***/
            if (itemView.getPaddingLeft() > 0 || itemView.getPaddingTop() > 0 || itemView.getPaddingRight() > 0 || itemView.getPaddingBottom() > 0) {
                this.baseCard.paddingFromLayout = true;
                this.baseCard.padding = BaseCard.newPadding(itemView.getPaddingLeft(), itemView.getPaddingTop(), itemView.getPaddingRight(), itemView.getPaddingBottom());
            }
            return;
        }

        boolean changed = false;
        if (itemView.getPaddingLeft() != padding.left) {
            changed = true;
        }
        if (itemView.getPaddingRight() != padding.right) {
            changed = true;
        }
        if (itemView.getPaddingBottom() != padding.bottom) {
            changed = true;
        }
        if (itemView.getPaddingTop() != padding.top) {
            changed = true;
        }

        if (changed) {
            itemView.setPadding(padding.left, padding.top, padding.right, padding.bottom);
        }

    }

    protected void setMargin(BaseCard.Margin margin) {
        boolean changed = false;
        ViewGroup.MarginLayoutParams params;
        if (itemView.getLayoutParams() == null) {
            changed = true;
            params = new ViewGroup.MarginLayoutParams(-1, -2);
        } else if (itemView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            params = (ViewGroup.MarginLayoutParams) itemView.getLayoutParams();
        } else {
            changed = true;
            params = new ViewGroup.MarginLayoutParams(itemView.getLayoutParams().width, itemView.getLayoutParams().height);
            params.layoutAnimationParameters = itemView.getLayoutParams().layoutAnimationParameters;
        }

        if (margin == null) {
            this.baseCard.margin = BaseCard.newMargin(params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin);
            if (changed) {
                itemView.setLayoutParams(params);
            }
            return;
        }


        if (params.leftMargin != margin.left) {
            changed = true;
            params.leftMargin = margin.left;
        }

        if (params.rightMargin != margin.right) {
            changed = true;
            params.rightMargin = margin.right;
        }


        if (params.bottomMargin != margin.bottom) {
            changed = true;
            params.bottomMargin = margin.bottom;
        }

        if (params.topMargin != margin.top) {
            changed = true;
            params.topMargin = margin.top;
        }

        if (changed) {
            itemView.setLayoutParams(params);
        }

    }
}