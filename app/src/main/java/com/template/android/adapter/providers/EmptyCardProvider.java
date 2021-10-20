package com.template.android.adapter.providers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.template.android.R;
import com.template.android.adapter.CardAdapter;
import com.template.android.adapter.CardMap;
import com.template.android.adapter.CommonVh;
import com.template.android.adapter.ItemViewProvider;
import com.template.android.adapter.card.EmptyCard;

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
