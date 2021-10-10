package com.template.project.adapter.providers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.template.project.R;
import com.template.project.adapter.CardAdapter;
import com.template.project.adapter.CardMap;
import com.template.project.adapter.CommonVh;
import com.template.project.adapter.ItemViewProvider;
import com.template.project.adapter.card.EmptyCard;

import butterknife.BindView;

@CardMap(EmptyCard.class)
public class EmptyCardProvider extends ItemViewProvider<EmptyCard, EmptyCardProvider.ViewHolder> {


    public EmptyCardProvider(CardAdapter.OnItemClickListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_card_empty, parent, false));
    }


    public static class ViewHolder extends CommonVh<EmptyCard> {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public ViewHolder(View itemView, CardAdapter.OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
        }

        @BindView(R.id.text)
        TextView text;

        @Override
        public void bind(EmptyCard card) {
            super.bind(card);
            if (card.hint != null) {
                text.setText(card.hint);
            }
            itemView.setBackgroundResource(card.backgroundRes);
        }
    }
}
