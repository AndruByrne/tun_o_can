package com.anthropicandroid.photogallery.ViewModel;

import android.app.Activity;
import android.os.Bundle;

import com.anthropicandroid.photogallery.InjectionModules.ActivityComponent;
import com.anthropicandroid.photogallery.InjectionModules.UserActionHandlers;
import com.anthropicandroid.photogallery.PhotoGalleryApplication;

import javax.inject.Inject;

public class GalleryActivity extends Activity {

    @Inject UserActionHandlers userActionHandlers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhotoGalleryApplication application = (PhotoGalleryApplication)getApplication();
        ActivityComponent activityComponent = application.getActivityComponent();
        activityComponent.inject(this);
        userActionHandlers.toString();
    }
}
