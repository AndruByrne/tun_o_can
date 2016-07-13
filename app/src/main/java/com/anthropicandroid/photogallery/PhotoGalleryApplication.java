package com.anthropicandroid.photogallery;

import android.app.Application;

import com.anthropicandroid.photogallery.InjectionModules.ActivityComponent;
import com.anthropicandroid.photogallery.InjectionModules.AppModule;
import com.anthropicandroid.photogallery.InjectionModules.ApplicationComponent;
import com.anthropicandroid.photogallery.InjectionModules.DaggerActivityComponent;
import com.anthropicandroid.photogallery.InjectionModules.DaggerApplicationComponent;
import com.anthropicandroid.photogallery.InjectionModules.UserActionHandlersModule;

/*
 * Created by Andrew Brin on 7/12/2016.
 */
public class PhotoGalleryApplication extends Application{

    private ApplicationComponent applicationComponent;
    private ActivityComponent activityComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        // create and set application component
        applicationComponent = DaggerApplicationComponent
                .builder()
                .appModule(new AppModule(this))
                .userActionHandlersModule(new UserActionHandlersModule())
                .build();
    }

    public ActivityComponent getActivityComponent() {
        if(activityComponent==null) {
            activityComponent = DaggerActivityComponent
                    .builder()
                    .applicationComponent(applicationComponent)
                    .build();
        }
        return activityComponent;
    }
}
