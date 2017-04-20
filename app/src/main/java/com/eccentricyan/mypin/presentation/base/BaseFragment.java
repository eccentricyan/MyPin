package com.eccentricyan.mypin.presentation.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.eccentricyan.mypin.di.component.ActivityComponent;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by shiyanhui on 2017/04/17.
 */

public class BaseFragment extends Fragment {
    protected ActivityComponent component;

    @Inject
    protected EventBus eventBus;

    @Inject
    protected Realm realm;

    @CallSuper
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.component = ((BaseActivity) getActivity()).getComponent();
        this.component.inject(this);
    }

}