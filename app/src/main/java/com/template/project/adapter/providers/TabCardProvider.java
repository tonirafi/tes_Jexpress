package com.template.project.adapter.providers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.material.tabs.TabLayout;
import com.template.project.R;
import com.template.project.adapter.CardAdapter;
import com.template.project.adapter.CardMap;
import com.template.project.adapter.CommonVh;
import com.template.project.adapter.ItemViewProvider;
import com.template.project.adapter.TabClickListener;
import com.template.project.adapter.card.TabCard;

import butterknife.BindView;


/**
 * Created by 5Mall<zhangwei> on 2019-07-10
 * Email:zhangwei@qingsongchou.com
 * 描述：
 */
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

           for (String name : card.namesTab){
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
