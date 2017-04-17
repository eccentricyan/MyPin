package com.eccentricyan.mypin.di.module;

import android.content.Context;

import com.eccentricyan.mypin.di.scope.ActivityScope;
import com.eccentricyan.mypin.presentation.base.BaseActivity;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private BaseActivity activity;

    public ActivityModule(BaseActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    public Context activityContext() {
        return this.activity;
    }

    @Provides
    @ActivityScope
    public LifecycleProvider<ActivityEvent> lifecycleProvider() {
        return activity;
    }

}
