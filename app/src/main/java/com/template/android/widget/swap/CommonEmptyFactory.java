package com.template.android.widget.swap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.template.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by smileCloud on 16/10/25.
 */

public class CommonEmptyFactory {

    public static final int COMMON_EMPTY = 0;
    public static final int CAMPAIGN_LIST_EMPTY = 1;
    public static final int COMMON_BLANK = 2;


    public static RecyclerView.ViewHolder emptyHolder(ViewGroup parent, int emptyType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (emptyType) {
            case COMMON_EMPTY:
            case COMMON_BLANK:
                view = inflater.inflate(R.layout.common_empty_layout, parent, false);
                return new CommonHolder(view);
            case CAMPAIGN_LIST_EMPTY:
                view = inflater.inflate(R.layout.list_empty_layout, parent, false);
                return new CampaignListHolder(view);
            default:
                throw new RuntimeException("there is no type that matches the type " + emptyType + " + make sure your using types correctly");
        }
    }

    static class CommonHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text)
        public TextView tvText;

        public CommonHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class CampaignListHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text)
        public TextView tvText;

        @BindView(R.id.btn)
        public Button btn;

        public CampaignListHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
