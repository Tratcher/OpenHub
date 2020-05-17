

package com.thirtydegreesray.openhub.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerFragmentComponent;
import com.thirtydegreesray.openhub.inject.module.FragmentModule;
import com.thirtydegreesray.openhub.mvp.contract.ICardsContract;
import com.thirtydegreesray.openhub.mvp.contract.IProjectsContract;
import com.thirtydegreesray.openhub.mvp.model.Branch;
import com.thirtydegreesray.openhub.mvp.model.Card;
import com.thirtydegreesray.openhub.mvp.model.Project;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.mvp.presenter.CardPresenter;
import com.thirtydegreesray.openhub.mvp.presenter.ProjectsPresenter;
import com.thirtydegreesray.openhub.ui.activity.ProjectActivity;
import com.thirtydegreesray.openhub.ui.activity.ProjectsListActivity;
import com.thirtydegreesray.openhub.ui.activity.RepositoryActivity;
import com.thirtydegreesray.openhub.ui.adapter.CardsAdapter;
import com.thirtydegreesray.openhub.ui.adapter.ProjectsAdapter;
import com.thirtydegreesray.openhub.ui.fragment.base.ListFragment;
import com.thirtydegreesray.openhub.util.BundleHelper;

import java.util.ArrayList;

/**
 * Created by Tratcher on 2020/05/16
 */

public class CardsFragment extends ListFragment<CardPresenter, CardsAdapter>
        implements ICardsContract.View, ProjectActivity.ProjectListener {

    public static CardsFragment create(@NonNull int columnId){
        CardsFragment fragment = new CardsFragment();
        fragment.setArguments(BundleHelper.builder().put("columnId", columnId).build());
        return fragment;
    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        super.initFragment(savedInstanceState);
        // TODO: setLoadMoreEnable(ProjectsListActivity.ProjectsListType.Repo.equals(mPresenter.getType()));
    }

    @Override
    public void showCards(ArrayList<Card> cards) {
        adapter.setData(cards);
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
        mPresenter.loadCards(1, true);
    }

    @Override
    protected String getEmptyTip() {
        return getString(R.string.no_cards);
    }

    @Override
    protected void onLoadMore(int page) {
        super.onLoadMore(page);
        mPresenter.loadCards(page, false);
    }

    @Override
    public void onFragmentShowed() {
        super.onFragmentShowed();
        if(mPresenter != null) mPresenter.prepareLoadData();
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {
        super.onItemClick(position, view);
        // TODO
        // ProjectActivity.show(getActivity(), adapter.getData().get(position));
    }

    @Override
    public void onProjectInfoUpdated(Project project) {

    }
}
