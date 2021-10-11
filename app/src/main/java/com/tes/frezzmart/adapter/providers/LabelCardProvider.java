package com.tes.frezzmart.adapter.providers;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.tes.frezzmart.R;
import com.tes.frezzmart.adapter.CardAdapter;
import com.tes.frezzmart.adapter.CardMap;
import com.tes.frezzmart.adapter.CommonVh;
import com.tes.frezzmart.adapter.ItemViewProvider;
import com.tes.frezzmart.adapter.card.LabelCard;
import com.tes.frezzmart.utils.AppUtil;

import butterknife.BindView;
import butterknife.OnClick;

@CardMap(LabelCard.class)
public class LabelCardProvider extends ItemViewProvider<LabelCard, LabelCardProvider.ViewHolder> {



    public LabelCardProvider(CardAdapter.OnItemClickListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_card_label, parent, false), mOnItemClickListener);
    }


    public static class ViewHolder extends CommonVh<LabelCard> {

        public ViewHolder(View itemView) {
            this(itemView, null);
        }

        public ViewHolder(View itemView, CardAdapter.OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
        }

        @OnClick(R.id.tvLabel)
        void onLabelClick() {
//            if (this.onItemClickListener instanceof CardAdapter.OnItemClickListener) {
//                ((CardAdapter.OnItemClickListener) this.onItemClickListener).onItemOnclick(getAdapterPosition());
//            }

            if (baseCard != null && baseCard.textViewWidth == -1) {
                onItemViewClick(itemView);
            }
        }

        @BindView(R.id.tvLabel)
        TextView tvLabel;

        @Override
        public void bind(LabelCard card) {
            super.bind(card);
            if (card.loading) {
//                tvLabel.setText(StringUtil.getBlankString(10));
                tvLabel.setMinEms(12);
                showPlaceholders();
                return;
            }

            tvLabel.setMinEms(0);
            Typeface preferTypeface = AppUtil.getFont(itemView.getContext(), card.fontResId);
            tvLabel.setTypeface(preferTypeface);

            if (card.boldTextWhileNormalHint) {
//                Typeface boldTf = AppUtil.getFont(itemView.getContext(), R.font.roboto_bold);
//                tvLabel.setTypeface(TextUtils.isEmpty(card.text) ? preferTypeface : boldTf);
            }

            tvLabel.setHint(card.hint);
            tvLabel.setText(card.text);

            if (card.lines > 0) {
                tvLabel.setLines(card.lines);
                tvLabel.setSingleLine(card.lines == 1);
            } else {
                tvLabel.setMaxLines(Integer.MAX_VALUE);
                tvLabel.setMinLines(0);
                tvLabel.setSingleLine(false);
            }

            tvLabel.setGravity(card.gravity);
            ((LinearLayout) itemView).setGravity(card.layout_gravity);

            tvLabel.setLineSpacing(card.lineSpacingExtra, 1f);
            tvLabel.setHintTextColor(card.textColorHint);
            tvLabel.setTextSize(card.textSize);
            tvLabel.setCompoundDrawablePadding(card.drawablePadding);

            if (card.textColorState != null) {
                tvLabel.setTextColor(card.textColorState);
            } else {
                tvLabel.setTextColor(card.textColor);
            }

            tvLabel.setBackgroundResource(card.textViewBgRes);
            itemView.setBackgroundResource(card.backgroundRes);
            itemView.setSelected(card.selected);
            tvLabel.setSelected(card.selected);

            if (card.textViewPadding != null) {
                tvLabel.setPadding(card.textViewPadding.left, card.textViewPadding.top, card.textViewPadding.right, card.textViewPadding.bottom);
            } else {
                tvLabel.setPadding(0, 0, 0, 0);
            }

            tvLabel.setCompoundDrawablesWithIntrinsicBounds(card.leftIcon, 0, card.rightIcon, 0);

            ViewGroup.LayoutParams params = tvLabel.getLayoutParams();
            params.width = card.textViewWidth;
            tvLabel.setLayoutParams(params);
        }



    }
}
