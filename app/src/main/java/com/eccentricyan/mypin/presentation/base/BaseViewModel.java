package com.eccentricyan.mypin.presentation.base;

import android.content.Context;
import android.databinding.BaseObservable;

import com.eccentricyan.mypin.di.component.ActivityComponent;
import com.eccentricyan.mypin.infra.api.RestfulApi;
import com.google.gson.Gson;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.realm.Realm;

/**
 * Created by shiyanhui on 2017/04/17.
 */

public class BaseViewModel extends BaseObservable {
    @Inject
    protected Context context;
    @Inject
    protected LifecycleProvider<ActivityEvent> lifecycleProvider;
    @Inject
    protected RestfulApi api;
    @Inject
    public CompositeDisposable compositeDisposable;
    @Inject
    protected EventBus eventBus;
    @Inject
    protected Gson gson;
    @Inject
    protected Realm realm;
    @Inject
    protected Scheduler subscribeScheduler;


    public BaseViewModel(ActivityComponent component) {
        component.inject(this);
    }

    public BaseViewModel() {

    }

}