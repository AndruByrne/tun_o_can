package com.anthropicandroid.photogallery.ViewModel;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.anthropicandroid.photogallery.InjectionModules.ActivityComponent;
import com.anthropicandroid.photogallery.PhotoGalleryApplication;
import com.anthropicandroid.photogallery.R;
import com.anthropicandroid.photogallery.databinding.ActivityGalleryBinding;

import javax.inject.Inject;

public class GalleryActivity extends Activity {

    @Inject UserActionHandlers userActionHandlers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhotoGalleryApplication application = (PhotoGalleryApplication)getApplication();
        ActivityComponent activityComponent = application.getActivityComponent();
        activityComponent.inject(this); //  bootstrap into dependency graph
        ActivityGalleryBinding galleryActivityBinding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_gallery,
                activityComponent);
        // set the user action handlers for the main view model (use separate one for bottom nav)
        galleryActivityBinding.setActionHandlers(userActionHandlers);
        // set app bar with relative layout base
        setActionBar(galleryActivityBinding.appBar);
    }
}
