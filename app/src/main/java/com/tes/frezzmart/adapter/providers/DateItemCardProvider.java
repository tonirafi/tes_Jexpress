package com.tes.frezzmart.adapter.providers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tes.frezzmart.R;
import com.tes.frezzmart.adapter.CardAdapter;
import com.tes.frezzmart.adapter.CardMap;
import com.tes.frezzmart.adapter.CommonVh;
import com.tes.frezzmart.adapter.ItemViewProvider;
import com.tes.frezzmart.adapter.card.DateItemCard;
import com.tes.frezzmart.http.bean.ArticlesItem;

import butterknife.BindView;

@CardMap(DateItemCard.class)
public class DateItemCardProvider extends ItemViewProvider<DateItemCard, DateItemCardProvider.ViewHolder> {



    public DateItemCardProvider(CardAdapter.OnItemClickListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_list_date, parent, false), mOnItemClickListener);
    }


    public static class ViewHolder extends CommonVh<DateItemCard> {
        @BindView(R.id.tvDate)
        TextView tvDate;



        public ViewHolder(View itemView) {
            this(itemView, null);
        }

        public ViewHolder(View itemView, CardAdapter.OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
        }


        @Override
        public void bind(DateItemCard card) {
            super.bind(card);

            ArticlesItem articlesItem=card.articlesItem;
                if (articlesItem == null) {
                    if (card.loading) {
                        showPlaceholders();
                    }
                    return;
                }

                tvDate.setText(articlesItem.getDate());


        }



    }
}
