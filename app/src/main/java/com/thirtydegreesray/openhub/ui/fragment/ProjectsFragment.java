

package com.thirtydegreesray.openhub.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerFragmentComponent;
import com.thirtydegreesray.openhub.inject.module.FragmentModule;
import com.thirtydegreesray.openhub.mvp.contract.IProjectsContract;
import com.thirtydegreesray.openhub.mvp.model.Branch;
import com.thirtydegreesray.openhub.mvp.model.Project;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.mvp.presenter.ProjectPresenter;
import com.thirtydegreesray.openhub.ui.activity.ProjectsListActivity;
import com.thirtydegreesray.openhub.ui.activity.RepositoryActivity;
import com.thirtydegreesray.openhub.ui.adapter.ProjectAdapter;
import com.thirtydegreesray.openhub.ui.fragment.base.ListFragment;
import com.thirtydegreesray.openhub.util.BundleHelper;

import java.util.ArrayList;

/**
 * Created by Tratcher on 2020/05/16
 */

public class ProjectsFragment extends ListFragment<ProjectPresenter, ProjectAdapter>
        implements IProjectsContract.View, RepositoryActivity.RepositoryListener {

    public static ProjectsFragment createForRepo(@NonNull String user, @NonNull String repo){
        ProjectsFragment fragment = new ProjectsFragment();
        fragment.setArguments(BundleHelper.builder().put("type", ProjectsListActivity.ProjectsListType.Repo)
                .put("user", user).put("repo", repo).build());
        return fragment;
    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        super.initFragment(savedInstanceState);
        setLoadMoreEnable(ProjectsListActivity.ProjectsListType.Repo.equals(mPresenter.getType()));
    }

    @Override
    public void showProjects(ArrayList<Project> commits) {
        adapter.setData(commits);
        postNotifyDataSetChanged();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerFragmentComponent.builder()
                .appComponent(appComponent)
                .fragmentModule(new FragmentModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onReLoadData() {
        mPresenter.loadProjects(true, 1);
    }

    @Override
    protected String getEmptyTip() {
        return getString(R.string.no_commits);
    }

    @Override
    protected void onLoadMore(int page) {
        super.onLoadMore(page);
        mPresenter.loadProjects(false, page);
    }

    @Override
    public void onFragmentShowed() {
        super.onFragmentShowed();
        if(mPresenter != null) mPresenter.prepareLoadData();
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {
        super.onItemClick(position, view);
        View userAvatar = view.findViewById(R.id.user_avatar);
        // TODO:
        /*
        CommitDetailActivity.show(getActivity(), mPresenter.getUser(), mPresenter.getRepo(),
                adapter.getData().get(position), userAvatar);
        */
    }

    @Override
    public void onRepositoryInfoUpdated(Repository repository) {

    }

    @Override
    public void onBranchChanged(Branch branch) {

    }
}
