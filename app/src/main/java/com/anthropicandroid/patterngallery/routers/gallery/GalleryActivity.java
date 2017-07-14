package com.anthropicandroid.patterngallery.routers.gallery;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.anthropicandroid.patterngallery.entities.ui.SVGItemViewModel;
import com.anthropicandroid.patterngallery.interactors.PatternRepository;
import com.anthropicandroid.patterngallery.interactors.startup.PatternGalleryApplication;
import com.anthropicandroid.patterngallery.R;
import com.anthropicandroid.patterngallery.databinding.LayoutActivityGalleryBinding;
import com.anthropicandroid.patterngallery.presenters.BottomNavActionHandlers;
import com.anthropicandroid.patterngallery.presenters.DetailActionHandlers;
import com.anthropicandroid.patterngallery.presenters.GalleryActionHandlers;

import java.util.ArrayList;

import javax.inject.Inject;

public class GalleryActivity
        extends AppCompatActivity {

    public static final String TAG = GalleryActivity.class.getSimpleName();

    @Inject BackPressedRepo backPressedRepo;
    @Inject PatternRepository patternRepository;
    @Inject GalleryActionHandlers galleryActionHandlers;
    @Inject DetailActionHandlers detailActionHandlers;
    @Inject BottomNavActionHandlers bottomNavActionHandlers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Bootstrap into dependency graph and bind view to view model and set variables
        super.onCreate(savedInstanceState);
        PatternGalleryApplication application = (PatternGalleryApplication) getApplication();
        GalleryActivityComponent galleryActivityComponent = application
                .getGalleryActivityComponent();
        // Set camera distance
        float scale = getResources().getDisplayMetrics().density;

        // Set default binding component
        DataBindingUtil.setDefaultComponent(galleryActivityComponent);
        // Inflate activity layout and bind to it
        LayoutActivityGalleryBinding activityGalleryBinding = DataBindingUtil.setContentView(
                this,
                R.layout.layout_activity_gallery);
        // Bootstrap into dependency graph
        galleryActivityComponent.inject(this);

        activityGalleryBinding.rootActivityLayout.setCameraDistance(9001 * scale);

        // set action handlers for grid view and app bar
        activityGalleryBinding.setGalleryActionHandlers(galleryActionHandlers);
        // and detail view
        activityGalleryBinding.setDetailActionHandlers(detailActionHandlers);
        activityGalleryBinding.setBottomNavActionHandlers(bottomNavActionHandlers);
        // set app bar with relative layout base
        setSupportActionBar(activityGalleryBinding.appBar);
        activityGalleryBinding.appBar.setContentInsetsAbsolute(0, 0);
        // ask for the layout to control the app bar
        getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    protected void onDestroy() {
        // release associated modules
        ((PatternGalleryApplication) getApplication()).releaseGalleryActivityComponent();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // ask action handlers if there is any animation they'd like reversed
        if (!backPressedRepo.backPressedConsumed())
            super.onBackPressed();
    }
}
