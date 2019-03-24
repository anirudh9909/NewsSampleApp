package com.example.anirudhbharti.newsapp;

import android.app.Activity;
import android.app.Application;

import com.example.anirudhbharti.newsapp.di.component.ApplicationComponent;
import com.example.anirudhbharti.newsapp.di.component.DaggerApplicationComponent;
import com.example.anirudhbharti.newsapp.di.module.ContextModule;

public class MyApplication extends Application {
    ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        dependencyInjection();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public static MyApplication get(Activity activity){
        return (MyApplication) activity.getApplication();
    }

    private void dependencyInjection(){
        applicationComponent = DaggerApplicationComponent.builder()
                .contextModule(new ContextModule(this)).build();

        applicationComponent.injectApplication(this);
    }
}