package com.anthropicandroid.photogallery;

import android.app.Application;

import com.anthropicandroid.photogallery.injectionmodulz.AppModule;
import com.anthropicandroid.photogallery.injectionmodulz.ApplicationComponent;
import com.anthropicandroid.photogallery.injectionmodulz.DaggerApplicationComponent;
import com.anthropicandroid.photogallery.injectionmodulz.DaggerGalleryActivityComponent;
import com.anthropicandroid.photogallery.injectionmodulz.GalleryActivityComponent;
import com.anthropicandroid.photogallery.injectionmodulz.RealmModule;
import com.anthropicandroid.photogallery.injectionmodulz.RepositoryModule;
import com.anthropicandroid.photogallery.injectionmodulz.ScreenMetricsModule;
import com.anthropicandroid.photogallery.injectionmodulz.UserActionHandlersModule;

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
                .userActionHandlersModule(new UserActionHandlersModule())
                .build();
    }

    public GalleryActivityComponent getGalleryActivityComponent() {
        // create component to be ties to the activity lifespan
        if (galleryActivityComponent == null) {
            galleryActivityComponent = DaggerGalleryActivityComponent
                    .builder()
                    .applicationComponent(applicationComponent)
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
