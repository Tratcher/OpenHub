

package com.thirtydegreesray.openhub.mvp.contract;

import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.mvp.contract.base.IBaseListContract;
import com.thirtydegreesray.openhub.mvp.contract.base.IBasePagerContract;
import com.thirtydegreesray.openhub.mvp.model.Project;

import java.util.ArrayList;

/**
 * Created by Tratcher on 2020/05/16
 */

public interface IProjectsContract {

    interface View extends IBaseContract.View, IBasePagerContract.View, IBaseListContract.View {
        void showProjects(ArrayList<Project> commits);
    }

    interface Presenter extends IBasePagerContract.Presenter<IProjectsContract.View>{
        void loadProjects(boolean isReload, int page);
    }

}
