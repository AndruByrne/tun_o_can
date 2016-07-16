package com.anthropicandroid.photogallery.viewmodel;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.anthropicandroid.photogallery.PhotoGalleryApplication;
import com.anthropicandroid.photogallery.R;
import com.anthropicandroid.photogallery.databinding.ActivityGalleryBinding;
import com.anthropicandroid.photogallery.injectionmodules.GalleryActivityComponent;
import com.anthropicandroid.photogallery.model.Repository;
import com.anthropicandroid.photogallery.model.utils.RepositoryPopulator;

import java.util.ArrayList;

import javax.inject.Inject;

public class GalleryActivity extends AppCompatActivity {

    public static final String TAG = GalleryActivity.class.getSimpleName();
    @Inject UserActionHandlers userActionHandlers;
    @Inject Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Bootstrap into dependency graph and bind view to view model and set variables
        super.onCreate(savedInstanceState);
        PhotoGalleryApplication application = (PhotoGalleryApplication) getApplication();
        GalleryActivityComponent galleryActivityComponent = application
                .getGalleryActivityComponent();

        DataBindingUtil.setDefaultComponent(galleryActivityComponent);
        ActivityGalleryBinding activityGalleryBinding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_gallery);
        galleryActivityComponent.inject(this);

        // set keys for image grid
        activityGalleryBinding.setEntries(RepositoryPopulator.imageIds);
        // set the user action handlers for the main view model (use separate one for bottom nav)
        activityGalleryBinding.setActionHandlers(userActionHandlers);
        // set app bar with relative layout base
        setSupportActionBar(activityGalleryBinding.appBar);
    }

    @Override
    protected void onDestroy() {
        // release associated modules
        ((PhotoGalleryApplication) getApplication()).releaseGalleryActivityComponent();
        super.onDestroy();
    }
}
