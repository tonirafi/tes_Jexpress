package com.template.android.adapter.providers;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.template.android.R;
import com.template.android.adapter.CardAdapter;
import com.template.android.adapter.CardMap;
import com.template.android.adapter.CommonVh;
import com.template.android.adapter.ItemViewProvider;
import com.template.android.adapter.card.NewsItemCard;
import com.template.android.http.bean.ArticlesItem;
import com.template.android.router.IntentUtil;
import com.template.android.utils.PicassoUtil;

import butterknife.BindView;

@CardMap(NewsItemCard.class)
public class NewsItemCardProvider extends ItemViewProvider<NewsItemCard, NewsItemCardProvider.ViewHolder> {


    public NewsItemCardProvider(CardAdapter.OnItemClickListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_list_news, parent, false), mOnItemClickListener);
    }


    public static class ViewHolder extends CommonVh<NewsItemCard> {
        @BindView(R.id.tvJudul)
        TextView tvJudul;

        @BindView(R.id.tvDeskrip)
        TextView tvDeskrip;


        @BindView(R.id.ic_image)
        ImageView ic_image;


        public ViewHolder(View itemView) {
            this(itemView, null);
        }

        public ViewHolder(View itemView, CardAdapter.OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
        }


        @Override
        public void bind(NewsItemCard card) {
            super.bind(card);

            ArticlesItem articlesItem = card.articlesItem;
            if (articlesItem == null) {
                if (card.loading) {
                    showPlaceholders();
                }
                return;
            }

            PicassoUtil.load(articlesItem.getUrlToImage()).centerCrop().fit().into(ic_image);
            tvJudul.setText(articlesItem.getTitle());
            tvDeskrip.setText(Html.fromHtml(articlesItem.getDescription()));


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentUtil.intentToWebView(itemView.getContext(), articlesItem);
                }
            });
        }


    }
}
