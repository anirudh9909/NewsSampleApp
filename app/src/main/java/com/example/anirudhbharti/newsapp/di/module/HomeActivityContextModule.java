package com.example.anirudhbharti.newsapp.di.module;

import android.content.Context;

import com.example.anirudhbharti.newsapp.di.qualifier.ActivityContext;
import com.example.anirudhbharti.newsapp.di.scope.ActivityScope;
import com.example.anirudhbharti.newsapp.ui.HomeActivity;

import dagger.Module;
import dagger.Provides;


@Module
public class HomeActivityContextModule {
    private HomeActivity homeActivity;
    private Context context;

    public HomeActivityContextModule(HomeActivity homeActivity){
        this.homeActivity = homeActivity;
        context = homeActivity;
    }

    @Provides
    @ActivityScope
    public HomeActivity providesLoginActivity(){
        return homeActivity;
    }

    @Provides
    @ActivityScope
    @ActivityContext
    public Context providesContext(){
        return context;
    }

}
