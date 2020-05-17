

package com.thirtydegreesray.openhub.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.common.GlideApp;
import com.thirtydegreesray.openhub.mvp.model.Project;
import com.thirtydegreesray.openhub.ui.activity.ProfileActivity;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseViewHolder;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;
import com.thirtydegreesray.openhub.util.PrefUtils;
import com.thirtydegreesray.openhub.util.StringUtils;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Tratcher on 2020/05/16
 */

public class ProjectsAdapter extends BaseAdapter<ProjectsAdapter.ViewHolder, Project> {

    @Inject
    public ProjectsAdapter(Context context, BaseFragment fragment) {
        super(context, fragment);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.layout_item_project;
    }

    @Override
    protected ViewHolder getViewHolder(View itemView, int viewType) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Project model = data.get(position);

        holder.name.setText(model.getName());
        holder.description.setText(model.getDescription());
    }

    class ViewHolder extends BaseViewHolder {

        @BindView(R.id.project_name) TextView name;
        @BindView(R.id.project_description) TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
