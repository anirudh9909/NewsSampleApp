package com.example.anirudhbharti.newsapp.di.component;

import android.content.Context;

import com.example.anirudhbharti.newsapp.di.module.HomeActivityContextModule;
import com.example.anirudhbharti.newsapp.di.module.HomeActivityMvpModule;
import com.example.anirudhbharti.newsapp.di.qualifier.ActivityContext;
import com.example.anirudhbharti.newsapp.di.scope.ActivityScope;
import com.example.anirudhbharti.newsapp.ui.HomeActivity;

import dagger.Component;

@ActivityScope
@Component(modules = {HomeActivityContextModule.class, HomeActivityMvpModule.class},
        dependencies = ApplicationComponent.class)
public interface HomeActivityComponent {

    @ActivityContext
    Context getContext();
    void injectHomeActivity(HomeActivity homeActivity);

}