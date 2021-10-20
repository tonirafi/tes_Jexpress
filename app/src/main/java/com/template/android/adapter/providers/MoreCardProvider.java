package com.template.android.adapter.providers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.template.android.R;
import com.template.android.adapter.CardAdapter;
import com.template.android.adapter.CardMap;
import com.template.android.adapter.CommonVh;
import com.template.android.adapter.ItemViewProvider;
import com.template.android.adapter.card.MoreCard;

import butterknife.OnClick;


@CardMap(MoreCard.class)
public class MoreCardProvider extends ItemViewProvider<MoreCard, MoreCardProvider.ViewHolder> {

    public MoreCardProvider(CardAdapter.OnItemClickListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_card_more, parent, false), mOnItemClickListener);
    }


    public static class ViewHolder extends CommonVh<MoreCard> {

        public ViewHolder(View itemView) {
            this(itemView, null);
        }

        public ViewHolder(View itemView, CardAdapter.OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
        }

        @OnClick(R.id.viewMore)
        void onViewMore() {
//            IntentUtil.intentToSpecialProjects(itemView.getContext(),baseCard.shortLink, "Campaigner Index page", Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }

        @Override
        public void bind(MoreCard card) {
            super.bind(card);
        }
    }
}