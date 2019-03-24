package com.example.anirudhbharti.newsapp.di.component;

import android.content.Context;

import com.example.anirudhbharti.newsapp.ApiService;
import com.example.anirudhbharti.newsapp.MyApplication;
import com.example.anirudhbharti.newsapp.di.module.ContextModule;
import com.example.anirudhbharti.newsapp.di.module.RetrofitModule;
import com.example.anirudhbharti.newsapp.di.qualifier.ApplicationContext;
import com.example.anirudhbharti.newsapp.di.scope.ApplicationScope;

import dagger.Component;

@ApplicationScope
@Component(modules = {ContextModule.class, RetrofitModule.class})
public interface ApplicationComponent {

    ApiService getApiService();

    @ApplicationContext
    Context getContext();

    void injectApplication(MyApplication application);

}