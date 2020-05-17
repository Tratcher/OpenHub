

package com.thirtydegreesray.openhub.mvp.contract;

import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.mvp.model.Branch;
import com.thirtydegreesray.openhub.mvp.model.Project;
import com.thirtydegreesray.openhub.mvp.model.ProjectColumn;
import com.thirtydegreesray.openhub.mvp.model.Repository;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/8/9 21:40:25
 */

public interface IProjectContract {

    interface View extends IBaseContract.View{
        void showProject(Project project, ArrayList<ProjectColumn> columns);
        void invalidateOptionsMenu();
    }

    interface Presenter extends IBaseContract.Presenter<IProjectContract.View>{
        /*
        void starRepo(boolean star);
        void watchRepo(boolean watch);
        void createFork();
        boolean isForkEnable();
        boolean isBookmarked();
        void bookmark(boolean bookmark);
        */
    }

}
