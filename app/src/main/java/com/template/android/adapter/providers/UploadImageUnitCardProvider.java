package com.template.android.adapter.providers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.template.android.R;
import com.template.android.adapter.CardAdapter;
import com.template.android.adapter.CardMap;
import com.template.android.adapter.CommonVh;
import com.template.android.adapter.ItemViewProvider;
import com.template.android.adapter.card.UploadImageUnitCard;
import com.template.android.utils.upload.image.bean.ImageBean;
import com.template.android.widget.UploadImageView;


@CardMap(UploadImageUnitCard.class)
public class UploadImageUnitCardProvider extends ItemViewProvider<UploadImageUnitCard, UploadImageUnitCardProvider.ViewHolder> {

    public UploadImageUnitCardProvider(CardAdapter.OnItemClickListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(new UploadImageView(parent.getContext()), mOnItemClickListener);
    }


    public static class ViewHolder extends CommonVh<UploadImageUnitCard> {

        public ViewHolder(View itemView) {
            this(itemView, null);
        }

        public ViewHolder(View itemView, CardAdapter.OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
            if (itemView instanceof UploadImageView) {

                ((UploadImageView) itemView).setImageListener(new UploadImageView.ImageListener() {
                    @Override
                    public void preview(ImageBean imageBean) {
                        if (onItemClickListener instanceof UploadImageUnitImageListener) {
                            ((UploadImageUnitImageListener) onItemClickListener).preview(getAdapterPosition(), imageBean);
                        }
                    }

                    @Override
                    public void remove(ImageBean imageBean) {
                        if (onItemClickListener instanceof UploadImageUnitImageListener) {
                            ((UploadImageUnitImageListener) onItemClickListener).remove(getAdapterPosition(), imageBean);
                        }
                    }

                    @Override
                    public void selectPhoto() {
                        if (onItemClickListener instanceof UploadImageUnitImageListener) {
                            ((UploadImageUnitImageListener) onItemClickListener).selectPhoto(getAdapterPosition());
                        }
                    }
                });
            }
        }

        @Override
        public void bind(UploadImageUnitCard card) {
//            super.bind(card); 禁止调用父类bind方法 会导致itemViewType改变 从而引起图片进度无更新的bug
            if (itemView instanceof UploadImageView) {
                if (card.isAddCard) {
                    ((UploadImageView) itemView).setDefaultCover(card.placeholderDrawableRes == -1 ? R.drawable.ic_add_photos : card.placeholderDrawableRes);
                } else {
//                    ((UploadImageView) itemView).setSuccessText(getAdapterPosition() == 0 ? itemView.getContext().getString(R.string.cover) : null);
                    ((UploadImageView) itemView).setSuccessText(card.successText);
                    ((UploadImageView) itemView).addImageBean(card.imageBean);
                }

            }
        }
    }


    public interface UploadImageUnitImageListener extends CardAdapter.OnItemClickListener {
        void preview(int position, ImageBean imageBean);

        void remove(int position, ImageBean imageBean);

        void selectPhoto(int position);
    }
}