package com.example.anirudhbharti.newsapp.ui;

import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;
import com.example.anirudhbharti.newsapp.ApiService;
import com.example.anirudhbharti.newsapp.model.NewsResponseModel;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomePresenterImpl implements HomeActivityContract.Presenter {

    private ApiService apiService;
    private HomeActivityContract.View mView;

    @Inject
    public HomePresenterImpl(ApiService apiService, HomeActivityContract.View mView) {
        this.apiService = apiService;
        this.mView = mView;
    }

    @Override
    public void loadNewsData(String country, String apiKey) {
        mView.showProgress();

        apiService.getNewsResponse(country, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewsResponseModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(NewsResponseModel newsResponseModel) {
                        if (newsResponseModel != null && newsResponseModel.status != null) {
                            if (newsResponseModel.status.equalsIgnoreCase("ok")) {
                                mView.showNewsList(newsResponseModel);

                            } else {
                                mView.showError("Login error", newsResponseModel.status);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError("network error", "Error occurred");
                        Log.e("Login", e.getMessage(), e);
                        mView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideProgress();
                    }
                });
    }
}