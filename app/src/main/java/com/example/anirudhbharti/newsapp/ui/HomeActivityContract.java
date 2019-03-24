package com.example.anirudhbharti.newsapp.ui;

import com.example.anirudhbharti.newsapp.model.NewsResponseModel;

public interface HomeActivityContract {

    interface View{
        void showNewsList(NewsResponseModel newsResponseModel);
        void showError(String call, String statusMessage);
        void showProgress();
        void hideProgress();
    }

    interface Presenter{
        void loadNewsData(String email, String password);
    }

}