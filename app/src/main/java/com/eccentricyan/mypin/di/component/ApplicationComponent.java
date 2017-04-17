package com.eccentricyan.mypin.di.component;

import com.eccentricyan.mypin.di.module.ActivityModule;
import com.eccentricyan.mypin.di.module.ApplicationModule;
import com.eccentricyan.mypin.di.scope.ApplicationScope;

import dagger.Component;

/**
 * Created by shiyanhui on 2017/04/17.
 */

@ApplicationScope
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    ActivityComponent activityComponent(ActivityModule module);
}