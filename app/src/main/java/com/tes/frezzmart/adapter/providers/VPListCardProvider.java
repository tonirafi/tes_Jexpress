package com.tes.frezzmart.adapter.providers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.tes.frezzmart.adapter.CardAdapter;
import com.tes.frezzmart.adapter.CardMap;
import com.tes.frezzmart.adapter.CardPagerAdapter;
import com.tes.frezzmart.adapter.CommonVh;
import com.tes.frezzmart.adapter.ItemViewProvider;
import com.tes.frezzmart.adapter.card.MoreCard;
import com.tes.frezzmart.adapter.card.VPListCard;
import com.tes.frezzmart.utils.DensityUtil;
import com.tes.frezzmart.widget.MyViewPager;


@CardMap(VPListCard.class)
public class VPListCardProvider extends ItemViewProvider<VPListCard, VPListCardProvider.ViewHolder> {


    public VPListCardProvider(CardAdapter.OnItemClickListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(new MyViewPager(parent.getContext()), mOnItemClickListener);
    }


    public static class ViewHolder extends CommonVh<VPListCard> implements CardAdapter.OnItemClickListener {

        MyViewPager ultraViewPager;

        public ViewHolder(View itemView) {
            this(itemView, null);
        }

        public ViewHolder(View itemView, CardAdapter.OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
            ultraViewPager = (MyViewPager) itemView;
            ultraViewPager.setScrollMode(MyViewPager.ScrollMode.HORIZONTAL);
            //UltraPagerAdapter 绑定子view到UltraViewPager
            CardPagerAdapter adapter = new CardPagerAdapter();
            adapter.setOnItemClickListener(this);
            ultraViewPager.setAdapter(adapter);


            //设定页面循环播放
//            ultraViewPager.setInfiniteLoop(true);
//            ultraViewPager.setInfiniteRatio(100);

//            ultraViewPager.setMultiScreen(0.716f);
//            ultraViewPager.setItemRatio(0.93f);
//            ultraViewPager.setRatio(1.3f);  //此方法与setAutoMeasureHeight方法 不能同时调用 取其一调用
//            ultraViewPager.setMaxHeight(DensityUtil.dp2px(300));
//            ultraViewPager.setAutoMeasureHeight(true);
//            ultraViewPager.setPageTransformer(false, new UltraScaleTransformer());
            ultraViewPager.getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if (!adapter.endWithCard(MoreCard.class)) return;

                    if (position == adapter.getCount() - 2 && positionOffset > 0) {
                        ultraViewPager.getViewPager().setCurrentItem(position, true);
                    }
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

        @Override
        public void onItemOnclick(int position) {
//            if (onItemClickListener instanceof OnVPItemClickListener) {
//                ((OnVPItemClickListener) onItemClickListener).onVPItemClick(getAdapterPosition(), position, baseCard.cards);
//            }
        }

        @Override
        public void bind(VPListCard card) {
            super.bind(card);
            itemView.setBackgroundResource(card.backgroundRes);
            //设定页面循环播放
            ultraViewPager.setInfiniteLoop(card.enableInfiniteLoop);

            ultraViewPager.setMultiScreen(card.multiScreenRatio);
            ultraViewPager.setRatio(card.screenRatio);
            ultraViewPager.setItemRatio(card.itemRatio);
            int itemSpacing = card.itemSpacing / 2;
            ultraViewPager.setScrollMargin(itemSpacing, itemSpacing);

            CardPagerAdapter adapter = (CardPagerAdapter) ultraViewPager.getAdapter();
            adapter.updateList(card.cards);
            ultraViewPager.refresh();
//            ultraViewPager.getViewPager().getAdapter().notifyDataSetChanged();

            if (!card.enableInfiniteLoop) {
                if (card.keepCenter) {
                    if (ultraViewPager.getCurrentItem() == 0)
                        ultraViewPager.scrollNextPage();
                } else {
                    ultraViewPager.getViewPager().setClipToPadding(false);
                    final int offset = (int) (DensityUtil.getDpOfSWR((1 - card.multiScreenRatio) / 2) - DensityUtil.dp2px(6));
                    ultraViewPager.setScrollMargin(-offset, offset);
                }
            }
        }
    }

}
