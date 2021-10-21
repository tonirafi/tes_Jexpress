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
import com.template.android.adapter.card.ProdakItemCard;
import com.template.android.http.bean.ProductsItem;
import com.template.android.http.bean.Warung;
import com.template.android.router.IntentUtil;
import com.template.android.utils.PicassoUtil;
import com.template.android.utils.ToastUtil;

import butterknife.BindView;

@CardMap(ProdakItemCard.class)
public class ProdakItemCardProvider extends ItemViewProvider<ProdakItemCard, ProdakItemCardProvider.ViewHolder> {


    public ProdakItemCardProvider(CardAdapter.OnItemClickListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_list_prodak, parent, false), mOnItemClickListener);
    }


    public static class ViewHolder extends CommonVh<ProdakItemCard> {
        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.tvPrice)
        TextView tvPrice;


        @BindView(R.id.ic_image)
        ImageView ic_image;


        public ViewHolder(View itemView) {
            this(itemView, null);
        }

        public ViewHolder(View itemView, CardAdapter.OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
        }


        @Override
        public void bind(ProdakItemCard card) {
            super.bind(card);

            ProductsItem productsItem = card.productsItem;
            if (productsItem == null) {
                if (card.loading) {
                    showPlaceholders();
                }
                return;
            }

//            PicassoUtil.load(productsItem.getUrlToImage()).centerCrop().fit().into(ic_image);
            tvName.setText(productsItem.getName());
            tvPrice.setText(" "+productsItem.getPrice());



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    IntentUtil.intentToPembayaran(itemView.getContext(), productsItem);
                }
            });
        }


    }
}
