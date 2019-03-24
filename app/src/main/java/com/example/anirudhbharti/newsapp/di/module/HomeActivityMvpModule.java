package com.example.anirudhbharti.newsapp.di.module;

import com.example.anirudhbharti.newsapp.ApiService;
import com.example.anirudhbharti.newsapp.ui.HomeActivityContract;
import com.example.anirudhbharti.newsapp.ui.HomePresenterImpl;
import com.example.anirudhbharti.newsapp.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeActivityMvpModule {

    private HomeActivityContract.View mView;

    public HomeActivityMvpModule(HomeActivityContract.View mView){
        this.mView = mView;
    }

    @Provides
    @ActivityScope
    HomeActivityContract.View provideView(){
        return mView;
    }

    @Provides
    @ActivityScope
    HomePresenterImpl providePresenter(ApiService apiService, HomeActivityContract.View mView){
        return new HomePresenterImpl(apiService, mView);
    }

}