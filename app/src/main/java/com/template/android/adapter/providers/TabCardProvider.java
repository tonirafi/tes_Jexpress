package com.template.android.adapter.providers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.material.tabs.TabLayout;
import com.template.android.R;
import com.template.android.adapter.CardAdapter;
import com.template.android.adapter.CardMap;
import com.template.android.adapter.CommonVh;
import com.template.android.adapter.ItemViewProvider;
import com.template.android.adapter.TabClickListener;
import com.template.android.adapter.card.TabCard;

import butterknife.BindView;


@CardMap(TabCard.class)
public class TabCardProvider extends ItemViewProvider<TabCard, TabCardProvider.ViewHolder> {


    public TabCardProvider(CardAdapter.OnItemClickListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_card_tab, parent, false), mOnItemClickListener);
    }

    public static class ViewHolder extends CommonVh<TabCard> {


        @BindView(R.id.tab_layout)
        TabLayout tabLayout;

        public ViewHolder(View itemView) {
            super(itemView);

        }

        public ViewHolder(View itemView, CardAdapter.OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
        }

        @Override
        public void bind(TabCard card) {
            super.bind(card);

            for (String name : card.namesTab) {
                tabLayout.addTab(tabLayout.newTab().setText(name));
            }

            tabLayout.clearOnTabSelectedListeners();
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    ((TabClickListener) onItemClickListener).tabClick(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });


        }

    }
}
