

package com.thirtydegreesray.openhub.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thirtydegreesray.openhub.AppData;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.common.GlideApp;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerActivityComponent;
import com.thirtydegreesray.openhub.inject.module.ActivityModule;
import com.thirtydegreesray.openhub.mvp.contract.IProjectContract;
import com.thirtydegreesray.openhub.mvp.contract.IRepositoryContract;
import com.thirtydegreesray.openhub.mvp.model.Branch;
import com.thirtydegreesray.openhub.mvp.model.Project;
import com.thirtydegreesray.openhub.mvp.model.ProjectColumn;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.mvp.presenter.ProjectPresenter;
import com.thirtydegreesray.openhub.mvp.presenter.RepositoryPresenter;
import com.thirtydegreesray.openhub.ui.activity.base.PagerActivity;
import com.thirtydegreesray.openhub.ui.adapter.BranchesAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseViewHolder;
import com.thirtydegreesray.openhub.ui.adapter.base.FragmentPagerModel;
import com.thirtydegreesray.openhub.ui.fragment.ActivityFragment;
import com.thirtydegreesray.openhub.ui.fragment.CommitsFragment;
import com.thirtydegreesray.openhub.ui.fragment.ProjectsFragment;
import com.thirtydegreesray.openhub.ui.fragment.RepoFilesFragment;
import com.thirtydegreesray.openhub.ui.fragment.RepoInfoFragment;
import com.thirtydegreesray.openhub.util.AppOpener;
import com.thirtydegreesray.openhub.util.AppUtils;
import com.thirtydegreesray.openhub.util.BundleHelper;
import com.thirtydegreesray.openhub.util.PrefUtils;
import com.thirtydegreesray.openhub.util.StarWishesHelper;
import com.thirtydegreesray.openhub.util.StringUtils;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;

/**
 * Created by ThirtyDegreesRay on 2017/8/9 21:39:20
 */
//FIXME fix fragment not showImage from background
public class ProjectActivity extends PagerActivity<ProjectPresenter>
        implements IProjectContract.View {

    public static void show(@NonNull Context activity, @NonNull Project project) {
        Intent intent = new Intent(activity, ProjectActivity.class);
        intent.putExtra("project", project);
        activity.startActivity(intent);
    }

    @BindView(R.id.loader) ProgressBar loader;
    @BindView(R.id.description) TextView description;

    private ArrayList<ProjectColumn> projectColumns;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .activityModule(new ActivityModule(getActivity()))
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    protected int getContentView() {
        return R.layout.activity_project;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* TODO
        if(mPresenter.getRepository() != null){
            getMenuInflater().inflate(R.menu.menu_repository, menu);
            MenuItem starItem = menu.findItem(R.id.action_star);
            MenuItem bookmark = menu.findItem(R.id.action_bookmark);
            starItem.setTitle(mPresenter.isStarred() ? R.string.unstar : R.string.star);
            starItem.setIcon(mPresenter.isStarred() ?
                    R.drawable.ic_star_title : R.drawable.ic_un_star_title);
            menu.findItem(R.id.action_watch).setTitle(mPresenter.isWatched() ?
                    R.string.unwatch : R.string.watch);
            menu.findItem(R.id.action_fork).setTitle(mPresenter.isFork() ?
                    R.string.forked : R.string.fork);
            menu.findItem(R.id.action_fork).setVisible(mPresenter.isForkEnable());
            bookmark.setTitle(mPresenter.isBookmarked() ?
                    getString(R.string.remove_bookmark) : getString(R.string.bookmark));
            menu.findItem(R.id.action_wiki).setVisible(mPresenter.getRepository().isHasWiki());
        }*/
        return true;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setTransparentStatusBar();
        toolbar.setTitleTextAppearance(getActivity(), R.style.Toolbar_TitleText);
        toolbar.setSubtitleTextAppearance(getActivity(), R.style.Toolbar_Subtitle);
        setToolbarBackEnable();
        setToolbarTitle(mPresenter.getProjectName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        switch (item.getItemId()) {
            case R.id.action_star:
                starRepo(!mPresenter.isStarred());
                return true;
            case R.id.action_branch:
                mPresenter.loadBranchesAndTags();
                return true;
            case R.id.action_share:
                AppOpener.shareText(getActivity(), mPresenter.getRepository().getHtmlUrl());
                return true;
            case R.id.action_open_in_browser:
                AppOpener.openInCustomTabsOrBrowser(getActivity(), mPresenter.getRepository().getHtmlUrl());
                return true;
            case R.id.action_copy_url:
                AppUtils.copyToClipboard(getActivity(), mPresenter.getRepository().getHtmlUrl());
                return true;
            case R.id.action_copy_clone_url:
                AppUtils.copyToClipboard(getActivity(), mPresenter.getRepository().getCloneUrl());
                return true;
            case R.id.action_watch:
                mPresenter.watchRepo(!mPresenter.isWatched());
                invalidateOptionsMenu();
                showSuccessToast(mPresenter.isWatched() ?
                        getString(R.string.watched) : getString(R.string.unwatched));
                return true;
            case R.id.action_fork:
                if(!mPresenter.getRepository().isFork()) forkRepo();
                return true;
            case R.id.action_releases:
                showReleases();
                return true;
            case R.id.action_wiki:
                WikiActivity.show(getActivity(), mPresenter.getRepository().getOwner().getLogin(),
                        mPresenter.getRepository().getName());
                return true;
            case R.id.action_download_source_zip:
                AppOpener.startDownload(getActivity(), mPresenter.getZipSourceUrl(),
                        mPresenter.getZipSourceName());
                return true;
            case R.id.action_download_source_tar:
                AppOpener.startDownload(getActivity(), mPresenter.getTarSourceUrl(),
                        mPresenter.getTarSourceName());
                return true;
            case R.id.action_bookmark:
                mPresenter.bookmark(!mPresenter.isBookmarked());
                invalidateOptionsMenu();
                showSuccessToast(mPresenter.isBookmarked() ?
                        getString(R.string.bookmark_saved) : getString(R.string.bookmark_removed));
                return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProject(Project project, ArrayList<ProjectColumn> columns) {
//        setToolbarTitle(repo.getFullName(), repo.getDefaultBranch());
        description.setText(project.getDescription());
        projectColumns = columns;

        if (pagerAdapter.getCount() == 0) {
            pagerAdapter.setPagerList(FragmentPagerModel
                    .createProjectColumnsPagerList(getActivity(), project, columns, getFragments()));
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setAdapter(pagerAdapter);
            showFirstPager();

        } else {
            noticeProjectUpdated(project);
        }

        invalidateOptionsMenu();
    }

    @Override
    public void showLoading() {
        super.showLoading();
        loader.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        loader.setVisibility(View.GONE);
    }

    private void noticeProjectUpdated(Project project){
        for(FragmentPagerModel pagerModel : pagerAdapter.getPagerList()){
            if(pagerModel.getFragment() instanceof ProjectListener){
                ((ProjectListener)pagerModel.getFragment()).onProjectInfoUpdated(project);
            }
        }
    }

    public interface ProjectListener{
        void onProjectInfoUpdated(Project project);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    public int getPagerSize() {
        return projectColumns != null ? projectColumns.size() : 0;
    }

    @Override
    protected int getFragmentPosition(Fragment fragment) {
        /*
        // TODO: Dynamic Columns
        if(fragment instanceof RepoInfoFragment){
            return 0;
        }else if(fragment instanceof RepoFilesFragment){
            return 1;
        }else if(fragment instanceof CommitsFragment){
            return 2;
        }else if(fragment instanceof ActivityFragment){
            return 3;
        }else if(fragment instanceof ProjectsFragment){
            return 4;
        }else*/
            return -1;
    }
}
