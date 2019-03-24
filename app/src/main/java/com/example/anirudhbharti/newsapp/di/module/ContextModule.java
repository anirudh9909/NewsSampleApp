package com.example.anirudhbharti.newsapp.di.module;

import android.content.Context;

import com.example.anirudhbharti.newsapp.di.qualifier.ApplicationContext;
import com.example.anirudhbharti.newsapp.di.scope.ApplicationScope;

import dagger.Module;
import dagger.Provides;
@Module
public class ContextModule {
    private Context context;

    public ContextModule(Context context){
        this.context = context;
    }

    @Provides
    @ApplicationScope
    @ApplicationContext
    public Context providesContext(){
        return context;
    }
}