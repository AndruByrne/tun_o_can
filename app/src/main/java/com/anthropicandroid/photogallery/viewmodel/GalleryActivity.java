package com.anthropicandroid.photogallery.viewmodel;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.anthropicandroid.photogallery.PhotoGalleryApplication;
import com.anthropicandroid.photogallery.R;
import com.anthropicandroid.photogallery.databinding.LayoutActivityGalleryBinding;
import com.anthropicandroid.photogallery.injectionmodules.GalleryActivityComponent;
import com.anthropicandroid.photogallery.model.Repository;
import com.anthropicandroid.photogallery.model.utils.RepositoryPopulator;

import javax.inject.Inject;

public class GalleryActivity extends AppCompatActivity {

    public static final String TAG = GalleryActivity.class.getSimpleName();

    @Inject BackPressedRepo backPressedRepo;
    @Inject Repository repository;
    @Inject UserActionHandlers userActionHandlers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Bootstrap into dependency graph and bind view to view model and set variables
        super.onCreate(savedInstanceState);
        PhotoGalleryApplication application = (PhotoGalleryApplication) getApplication();
        GalleryActivityComponent galleryActivityComponent = application
                .getGalleryActivityComponent();

        // Set default binding component
        DataBindingUtil.setDefaultComponent(galleryActivityComponent);
        // Inflate activity layout and bind to it
        LayoutActivityGalleryBinding activityGalleryBinding = DataBindingUtil.setContentView(
                this,
                R.layout.layout_activity_gallery);
        // Bootstrap into dependency graph
        galleryActivityComponent.inject(this);

        // set keys for image grid
        activityGalleryBinding.setEntries(RepositoryPopulator.imageIds);
        // set the user action handlers for the main view model (use separate one for bottom nav)
        activityGalleryBinding.setActionHandlers(userActionHandlers);
        // set app bar with relative layout base
        setSupportActionBar(activityGalleryBinding.appBar);
        // ask for the layout to control the app bar
        getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    protected void onDestroy() {
        // release associated modules
        ((PhotoGalleryApplication) getApplication()).releaseGalleryActivityComponent();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // ask action handlers if there is any animation they'd like reversed
        if (!backPressedRepo.backPressedConsumed())
            super.onBackPressed();
    }
}
