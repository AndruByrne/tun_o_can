package com.anthropicandroid.photogallery.ViewModel;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.anthropicandroid.photogallery.InjectionModules.GalleryActivityComponent;
import com.anthropicandroid.photogallery.PhotoGalleryApplication;
import com.anthropicandroid.photogallery.R;
import com.anthropicandroid.photogallery.databinding.ActivityGalleryBinding;

import java.util.ArrayList;

import javax.inject.Inject;

public class GalleryActivity extends Activity {

    @Inject UserActionHandlers userActionHandlers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Bootstrap into dependency graph and bind view to view model and set variables
        super.onCreate(savedInstanceState);
        PhotoGalleryApplication application = (PhotoGalleryApplication) getApplication();
        GalleryActivityComponent galleryActivityComponent = application.getGalleryActivityComponent();

        galleryActivityComponent.inject(this);

        DataBindingUtil.setDefaultComponent(galleryActivityComponent);
        ActivityGalleryBinding activityGalleryBinding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_gallery);

        // set the user action handlers for the main view model (use separate one for bottom nav)
        activityGalleryBinding.setActionHandlers(userActionHandlers);
        // set app bar with relative layout base
        setActionBar(activityGalleryBinding.appBar);

        // check if we are on main thread (and need rxJava)
        activityGalleryBinding.setEntries(new ArrayList<Integer>(){{
            add(1);
            add(2);
            add(3);
            add(4);
            add(5);
        }});
    }

    @Override
    protected void onDestroy() {
        // release associated modules
        ((PhotoGalleryApplication) getApplication()).releaseGalleryActivityComponent();
        super.onDestroy();
    }
}
