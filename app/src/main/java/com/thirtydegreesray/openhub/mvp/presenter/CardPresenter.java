package com.thirtydegreesray.openhub.mvp.presenter;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.AppData;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.http.error.HttpPageNoFoundError;
import com.thirtydegreesray.openhub.mvp.contract.ICardsContract;
import com.thirtydegreesray.openhub.mvp.contract.IIssuesContract;
import com.thirtydegreesray.openhub.mvp.model.Card;
import com.thirtydegreesray.openhub.mvp.model.Issue;
import com.thirtydegreesray.openhub.mvp.model.SearchResult;
import com.thirtydegreesray.openhub.mvp.model.filter.IssuesFilter;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePagerPresenter;
import com.thirtydegreesray.openhub.util.StringUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by ThirtyDegreesRay on 2017/9/20 14:56:49
 */

public class CardPresenter extends BasePagerPresenter<ICardsContract.View>
        implements ICardsContract.Presenter {

    @AutoAccess int columnId;
    private ArrayList<Card> cards;

    @Inject
    public CardPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
    }

    @Override
    protected void loadData() {
        loadCards(1, false);
    }

    @Override
    public void loadCards(final int page, final boolean isReload) {
        boolean readCacheFirst = page == 1 && !isReload;
        loadCards(page, isReload, readCacheFirst);
    }

    private void loadCards(final int page, final boolean isReload, final boolean readCacheFirst){

//        HttpSubscriber<ResponseBody> subscriber = new HttpSubscriber<ResponseBody>(
//                new HttpObserver<ResponseBody>() {
//                    @Override
//                    public void onError(Throwable error) {
//                        error.toString();
//                    }
//
//                    @Override
//                    public void onSuccess(HttpResponse<ResponseBody> response) {
//                        response.body();
//                    }
//                }
//        );
//        generalRxHttpExecute(getSearchService().searchIssues(false, "state:open", "created", "desc", page), subscriber);

        mView.showLoading();
        HttpObserver<ArrayList<Card>> httpObserver =
                new HttpObserver<ArrayList<Card>>() {
                    @Override
                    public void onError(Throwable error) {
                        mView.hideLoading();
                        handleError(error);
                    }

                    @Override
                    public void onSuccess(HttpResponse<ArrayList<Card>> response) {
                        mView.hideLoading();
                        handleSuccess(response.body(), isReload, readCacheFirst);
                    }
                };

        generalRxHttpExecute(new IObservableCreator<ArrayList<Card>>() {
            @Override
            public Observable<Response<ArrayList<Card>>> createObservable(boolean forceNetWork) {
                return getProjectService().getColumnCards(forceNetWork, columnId);
            }
        }, httpObserver, readCacheFirst);
    }

    private void handleError(Throwable error){
        if(!StringUtils.isBlankList(cards)){
            mView.showErrorToast(getErrorTip(error));
        } else if(error instanceof HttpPageNoFoundError){
            mView.showCards(new ArrayList<Card>());
        }else{
            mView.showLoadError(getErrorTip(error));
        }
    }

    private void handleSuccess(ArrayList<Card> resultCards, boolean isReload, boolean readCacheFirst){
        if (isReload || cards == null || readCacheFirst) {
            cards = resultCards;
        } else {
            cards.addAll(resultCards);
        }
        if (resultCards.size() == 0 && cards.size() != 0) {
            mView.setCanLoadMore(false);
        } else {
            mView.showCards(cards);
        }
    }
}
