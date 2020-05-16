

package com.thirtydegreesray.openhub.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.ui.activity.base.SingleFragmentActivity;
import com.thirtydegreesray.openhub.ui.fragment.CommitsFragment;
import com.thirtydegreesray.openhub.ui.fragment.ProjectsFragment;
import com.thirtydegreesray.openhub.util.BundleHelper;

/**
 * Created by Tratcher on 2020/05/16
 */

public class ProjectsListActivity extends SingleFragmentActivity<IBaseContract.Presenter, ProjectsFragment> {

    public static void showForRepo(@NonNull Activity activity, @NonNull String user, @NonNull String repo) {
        Intent intent = createIntentForRepo(activity, user, repo);
        activity.startActivity(intent);
    }

    public static Intent createIntentForRepo(@NonNull Activity activity, @NonNull String user, @NonNull String repo) {
        return new Intent(activity, ProjectsListActivity.class)
                .putExtras(BundleHelper.builder()
                        .put("type", ProjectsListType.Repo)
                        .put("user", user)
                        .put("repo", repo)
                        .build());
    }

    public enum ProjectsListType {
        Repo
    }

    @AutoAccess ProjectsListActivity.ProjectsListType type;
    @AutoAccess String user;
    @AutoAccess String repo;

    @AutoAccess String before;
    @AutoAccess String head;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        String repoFullName = user.concat("/").concat(repo);
        setToolbarTitle(getToolbarTitle(), repoFullName);
        setToolbarScrollAble(true);
    }

    private String getToolbarTitle(){
        if (ProjectsListType.Repo.equals(type)) {
            return getString(R.string.projects);
        } else {
            return getString(R.string.projects);
        }
    }

    @Override
    protected ProjectsFragment createFragment() {
        if (ProjectsListType.Repo.equals(type)) {
            return ProjectsFragment.createForRepo(user, repo);
        } else {
            return null;
        }
    }
}
