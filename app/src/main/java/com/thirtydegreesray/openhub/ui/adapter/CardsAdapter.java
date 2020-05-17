package com.thirtydegreesray.openhub.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.common.GlideApp;
import com.thirtydegreesray.openhub.mvp.model.Card;
import com.thirtydegreesray.openhub.mvp.model.Issue;
import com.thirtydegreesray.openhub.ui.activity.ProfileActivity;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseViewHolder;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;
import com.thirtydegreesray.openhub.util.PrefUtils;
import com.thirtydegreesray.openhub.util.StringUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ThirtyDegreesRay on 2017/9/20 14:58:40
 */

public class CardsAdapter extends BaseAdapter<CardsAdapter.ViewHolder, Card> {

    @Inject
    public CardsAdapter(Context context, BaseFragment fragment) {
        super(context, fragment);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.layout_item_card;
    }

    @Override
    protected ViewHolder getViewHolder(View itemView, int viewType) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Card model = data.get(position);
        holder.note.setText(model.getNote());
        holder.contentUrl.setText(model.getContentUrl());
        holder.contentId.setText("Card #" + model.getId());
    }

    class ViewHolder extends BaseViewHolder {

        @BindView(R.id.note) TextView note;
        @BindView(R.id.contentUrl) TextView contentUrl;
        @BindView(R.id.contentId) TextView contentId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
