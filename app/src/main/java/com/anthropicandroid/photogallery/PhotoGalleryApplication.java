package com.anthropicandroid.photogallery;

import android.app.Application;

import com.anthropicandroid.photogallery.injectionmodules.ActionHandlersModule;
import com.anthropicandroid.photogallery.injectionmodules.AppModule;
import com.anthropicandroid.photogallery.injectionmodules.ApplicationComponent;
import com.anthropicandroid.photogallery.injectionmodules.DaggerApplicationComponent;
import com.anthropicandroid.photogallery.injectionmodules.DaggerGalleryActivityComponent;
import com.anthropicandroid.photogallery.injectionmodules.GalleryActivityComponent;
import com.anthropicandroid.photogallery.injectionmodules.OnBackPressedModule;
import com.anthropicandroid.photogallery.injectionmodules.RealmModule;
import com.anthropicandroid.photogallery.injectionmodules.RepositoryModule;
import com.anthropicandroid.photogallery.injectionmodules.ScreenMetricsModule;

/*
 * Created by Andrew Brin on 7/12/2016.
 */
public class PhotoGalleryApplication extends Application {

    private ApplicationComponent applicationComponent;
    private GalleryActivityComponent galleryActivityComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        // Create and set application component
        applicationComponent = DaggerApplicationComponent
                .builder()
                .appModule(new AppModule(this))
                .realmModule(new RealmModule()) // context required for constructor
                .repositoryModule(new RepositoryModule())
                .build();
    }

    public GalleryActivityComponent getGalleryActivityComponent() {
        // create component to be ties to the activity lifespan
        if (galleryActivityComponent == null) {
            galleryActivityComponent = DaggerGalleryActivityComponent
                    .builder()
                    .applicationComponent(applicationComponent)
                    .actionHandlersModule(new ActionHandlersModule())
                    .onBackPressedModule(new OnBackPressedModule())
                    .screenMetricsModule(new ScreenMetricsModule())
                    .build();
        }
        return galleryActivityComponent;
    }

    public void releaseGalleryActivityComponent() {
//        release reference to activity component
        galleryActivityComponent = null;
    }
}
