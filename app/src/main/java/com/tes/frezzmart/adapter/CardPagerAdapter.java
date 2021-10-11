package com.tes.frezzmart.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter {

    private static final List<String> mCardPool = new ArrayList<>();
    private static final List<String> mProviderPool = new ArrayList<>();
    protected SparseArray<ItemViewProvider> mProviders = new SparseArray<>(2);

    static {
        CardMapInitializer initializer = new CardMapInitializer();
        initializer.initRouterTable(mCardPool, mProviderPool);
    }


    public ItemViewProvider getProvider(BaseCard baseCard) {

        int providerPos = mCardPool.indexOf(baseCard.getClass().getName());
        if (providerPos == -1)
            return null;

        ItemViewProvider provider = mProviders.get(providerPos);
        if (provider == null) {
            try {
                Constructor c = Class.forName(mProviderPool.get(providerPos)).getConstructor(CardAdapter.OnItemClickListener.class);
                provider = (ItemViewProvider) c.newInstance(mOnItemClickListener);
                if (provider != null) {
                    mProviders.put(providerPos, provider);
                }
            } catch (Exception e) {
//                e.printStackTrace();
                return null;
            }
        }
        return provider;
    }

    protected CardAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(CardAdapter.OnItemClickListener l) {
        this.mOnItemClickListener = l;
    }

    List<BaseCard> mCards = new ArrayList<>();

//    public void clear() {
//        this.mCards.clear();
//    }

//    public void addAll(List<BaseCard> cards) {
//        this.mCards.addAll(cards);
//    }

    public void updateList(List<BaseCard> cards) {
        this.mCards.clear();
        this.mCards.addAll(cards);
        notifyDataSetChanged();
    }

    public boolean endWithCard(Class clz) {

        if (mCards != null && mCards.size() > 0) {
            return clz.isInstance(mCards.get(mCards.size() - 1));
        }

        return false;
    }

    @Override
    public int getCount() {
        return mCards.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        BaseCard card = mCards.get(position);
        ItemViewProvider itemViewProvider = getProvider(card);
        if (itemViewProvider == null) {
            throw new RuntimeException("ItemViewProvider must not be null");
        }

        CommonVh commonVh = itemViewProvider.onCreateViewHolder(LayoutInflater.from(container.getContext()), container);
        if (commonVh instanceof CommonVPVh) {
            ((CommonVPVh) commonVh).setPosition(position);
        }
        commonVh.bind(card);
        container.addView(commonVh.itemView);
//      contentView.getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, container.getContext().getResources().getDisplayMetrics());
//      contentView.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, container.getContext().getResources().getDisplayMetrics());
        return commonVh.itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
