

package com.thirtydegreesray.openhub.mvp.presenter;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.AppData;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.dao.Bookmark;
import com.thirtydegreesray.openhub.dao.BookmarkDao;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.dao.LocalRepo;
import com.thirtydegreesray.openhub.dao.Trace;
import com.thirtydegreesray.openhub.dao.TraceDao;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpProgressSubscriber;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.mvp.contract.IProjectContract;
import com.thirtydegreesray.openhub.mvp.contract.IRepositoryContract;
import com.thirtydegreesray.openhub.mvp.model.Branch;
import com.thirtydegreesray.openhub.mvp.model.Project;
import com.thirtydegreesray.openhub.mvp.model.ProjectColumn;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePresenter;
import com.thirtydegreesray.openhub.ui.activity.RepositoryActivity;
import com.thirtydegreesray.openhub.util.StarWishesHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ThirtyDegreesRay on 2017/8/9 21:42:47
 */

public class ProjectPresenter extends BasePresenter<IProjectContract.View>
        implements IProjectContract.Presenter {

    @AutoAccess(dataName = "project") Project project;
    private ArrayList<ProjectColumn> projectColumns;

    @AutoAccess String projectName;
    @AutoAccess int projectId;

    private boolean isStatusChecked = false;
    @AutoAccess boolean isTraceSaved = false;

    @Inject
    public ProjectPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        if (projectColumns != null) {
            projectName = project.getName();
            projectId = project.getId();
            mView.showProject(project, projectColumns);
            getColumnInfo(false);
        } else {
            getColumnInfo(true);
        }
    }

    private void getColumnInfo(final boolean isShowLoading) {
        if (isShowLoading) mView.showLoading();
        HttpObserver<ArrayList<ProjectColumn>> httpObserver =
                new HttpObserver<ArrayList<ProjectColumn>>() {
                    @Override
                    public void onError(Throwable error) {
                        if (isShowLoading) mView.hideLoading();
                        mView.showErrorToast(getErrorTip(error));
                    }

                    @Override
                    public void onSuccess(HttpResponse<ArrayList<ProjectColumn>> response) {
                        if (isShowLoading) mView.hideLoading();
                        projectColumns = response.body();
                        mView.showProject(project, projectColumns);
                    }
                };

        generalRxHttpExecute(new IObservableCreator<ArrayList<ProjectColumn>>() {
            @Override
            public Observable<Response<ArrayList<ProjectColumn>>> createObservable(boolean forceNetWork) {
                return getProjectService().getProjectColumns(forceNetWork, project.getId());
            }
        }, httpObserver, true);
    }

    public Project getProject() {
        return project;
    }

    public String getProjectName() {
        return project == null ? projectName : project.getName();
    }
}
