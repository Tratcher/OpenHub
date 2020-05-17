package com.thirtydegreesray.openhub.mvp.contract;

import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.mvp.contract.base.IBaseListContract;
import com.thirtydegreesray.openhub.mvp.contract.base.IBasePagerContract;
import com.thirtydegreesray.openhub.mvp.model.Card;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/9/20 14:55:29
 */

public interface ICardsContract {

    interface View extends IBaseContract.View, IBasePagerContract.View, IBaseListContract.View{
        void showCards(ArrayList<Card> cards);
    }

    interface Presenter extends IBasePagerContract.Presenter<ICardsContract.View>{
        void loadCards(int page, boolean isReload);
    }
}
