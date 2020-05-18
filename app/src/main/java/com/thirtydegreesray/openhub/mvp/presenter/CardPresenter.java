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
        loadCards(false);
    }

    @Override
    public void loadCards(final boolean isReload) {
        boolean readCacheFirst = !isReload;
        loadCards(isReload, readCacheFirst);
    }

    private void loadCards(final boolean isReload, final boolean readCacheFirst){

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
                        handleError(error);
                    }

                    @Override
                    public void onSuccess(HttpResponse<ArrayList<Card>> response) {
                        handleCardsSuccess(response.body(), isReload, readCacheFirst);
                    }
                };

        generalRxHttpExecute(new IObservableCreator<ArrayList<Card>>() {
            @Override
            public Observable<Response<ArrayList<Card>>> createObservable(boolean forceNetWork) {
                return getProjectService().getColumnCards(forceNetWork, columnId, "not_archived");
            }
        }, httpObserver, readCacheFirst);
    }

    private void handleError(Throwable error){
        mView.hideLoading();
        if(!StringUtils.isBlankList(cards)){
            mView.showErrorToast(getErrorTip(error));
        } else if(error instanceof HttpPageNoFoundError){
            mView.showCards(new ArrayList<Card>());
        }else{
            mView.showLoadError(getErrorTip(error));
        }
    }

    private void handleCardsSuccess(ArrayList<Card> resultCards, boolean isReload, boolean readCacheFirst){
        cards = resultCards;
        mView.setCanLoadMore(false);

        if (cards.size() > 0) {
            CardContentDownloader contentDownloader = new CardContentDownloader(cards, isReload, readCacheFirst);
            contentDownloader.LoadNext();
        }
        else{
            handleIssueDownloadComplete();
        }
    }

    private void handleIssueDownloadComplete() {
        mView.hideLoading();
        if (cards.size() > 0) {
            mView.showCards(cards);
        }
    }

    private class CardContentDownloader {

        private ArrayList<Card> cards;
        private boolean isReload;
        private boolean readCacheFirst;
        private int offset;

        public CardContentDownloader(ArrayList<Card> cards, boolean isReload, boolean readCacheFirst) {
            this.cards = cards;
            this.isReload = isReload;
            this.readCacheFirst = readCacheFirst;
            offset = 0;
        }

        public void LoadNext() {

            // Skip things we don't know how to download.
            while (offset < cards.size() && cards.get(offset).getCardType() != Card.CardType.Issue) {
                offset++;
            }
            if (offset >= cards.size())
            {
                handleIssueDownloadComplete();
                return;
            }

            Card card = cards.get(offset);
            String issuePath = card.getContentUrl().substring("https://api.github.com/".length());

            HttpObserver<Issue> httpObserver =
                    new HttpObserver<Issue>() {
                        @Override
                        public void onError(Throwable error) {
                            handleIssueError(error);
                        }

                        @Override
                        public void onSuccess(HttpResponse<Issue> response) {
                            handleIssueSuccess(response.body(), isReload, readCacheFirst);
                        }
                    };

            generalRxHttpExecute(new IObservableCreator<Issue>() {
                @Override
                public Observable<Response<Issue>> createObservable(boolean forceNetWork) {
                    return getIssueService().getIssueInfo(forceNetWork, issuePath);
                }
            }, httpObserver, readCacheFirst);
        }

        private void handleIssueError(Throwable error) {
            offset++;
            LoadNext();
        }

        private void handleIssueSuccess(Issue issue, boolean isReload, boolean readCacheFirst) {
            Card card = cards.get(offset);
            card.setIssue(issue);
            offset++;
            LoadNext();
        }
    }
}
