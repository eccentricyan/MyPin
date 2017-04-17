package com.eccentricyan.mypin.di.component;

import com.eccentricyan.mypin.di.module.ActivityModule;
import com.eccentricyan.mypin.di.scope.ActivityScope;
import com.eccentricyan.mypin.presentation.base.BaseActivity;
import com.eccentricyan.mypin.presentation.base.BaseFragment;
import com.eccentricyan.mypin.presentation.base.BaseViewModel;

import dagger.Subcomponent;

/**
 * Created by shiyanhui on 2017/04/17.
 */

@ActivityScope
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(BaseActivity viewModelActivity);
    void inject(BaseViewModel viewModel);
    void inject(BaseFragment fragment);
}