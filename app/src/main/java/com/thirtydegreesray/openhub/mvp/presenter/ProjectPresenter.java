

package com.thirtydegreesray.openhub.mvp.presenter;

import android.support.annotation.NonNull;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.http.error.HttpPageNoFoundError;
import com.thirtydegreesray.openhub.mvp.contract.IProjectsContract;
import com.thirtydegreesray.openhub.mvp.model.CommitsComparison;
import com.thirtydegreesray.openhub.mvp.model.Project;
import com.thirtydegreesray.openhub.mvp.model.RepoCommit;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePagerPresenter;
import com.thirtydegreesray.openhub.ui.activity.CommitsListActivity;
import com.thirtydegreesray.openhub.ui.activity.ProjectsListActivity;
import com.thirtydegreesray.openhub.util.StringUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by Tratcher on 2020/05/16
 */

public class ProjectPresenter extends BasePagerPresenter<IProjectsContract.View>
        implements IProjectsContract.Presenter {

    @AutoAccess ProjectsListActivity.ProjectsListType type ;
    @AutoAccess String name ;
    @AutoAccess String description ;
    @AutoAccess String user ;
    @AutoAccess String repo ;

    private ArrayList<Project> projects;

    @Inject
    public ProjectPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    protected void loadData() {
        loadProjects(false, 1);
    }

    @Override
    public void loadProjects(final boolean isReload, final int page) {
        mView.showLoading();
        final boolean readCacheFirst = !isReload && page == 1;
        HttpObserver<ArrayList<Project>> httpObserver = new HttpObserver<ArrayList<Project>>() {
            @Override
            public void onError(Throwable error) {
                mView.hideLoading();
                if (!StringUtils.isBlankList(projects)) {
                    mView.showErrorToast(getErrorTip(error));
                } else if (error instanceof HttpPageNoFoundError) {
                    mView.showProjects(new ArrayList<Project>());
                } else {
                    mView.showLoadError(getErrorTip(error));
                }
            }

            @Override
            public void onSuccess(HttpResponse<ArrayList<Project>> response) {
                mView.hideLoading();
                if (projects == null || isReload || readCacheFirst) {
                    projects = response.body();
                } else {
                    projects.addAll(response.body());
                }
                if (response.body().size() == 0 && projects.size() != 0) {
                    mView.setCanLoadMore(false);
                } else {
                    mView.showProjects(projects);
                }
            }
        };
        generalRxHttpExecute(new IObservableCreator<ArrayList<Project>>() {
            @Override
            public Observable<Response<ArrayList<Project>>> createObservable(boolean forceNetWork) {
                return getProjectService().getRepoProjects(forceNetWork, user, repo, page);
            }
        }, httpObserver, readCacheFirst);
    }

    public ProjectsListActivity.ProjectsListType getType() {
        return type;
    }
}
