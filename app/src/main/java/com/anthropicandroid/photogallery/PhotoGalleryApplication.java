package com.anthropicandroid.photogallery;

import android.app.Application;

import com.anthropicandroid.photogallery.injectionmodules.AppModule;
import com.anthropicandroid.photogallery.injectionmodules.ApplicationComponent;
import com.anthropicandroid.photogallery.injectionmodules.DaggerApplicationComponent;
import com.anthropicandroid.photogallery.injectionmodules.DaggerGalleryActivityComponent;
import com.anthropicandroid.photogallery.injectionmodules.GalleryActivityComponent;
import com.anthropicandroid.photogallery.injectionmodules.RealmModule;
import com.anthropicandroid.photogallery.injectionmodules.ScreenWidthModule;
import com.anthropicandroid.photogallery.injectionmodules.ThumbnailRepositoryModule;
import com.anthropicandroid.photogallery.injectionmodules.UserActionHandlersModule;

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
                .realmModule(new RealmModule(this))
                .userActionHandlersModule(new UserActionHandlersModule())
                .build();
        // load images into Realm
    }

    public GalleryActivityComponent getGalleryActivityComponent() {
        // create component to be ties to the activity lifespan
        if (galleryActivityComponent == null) {
            galleryActivityComponent = DaggerGalleryActivityComponent
                    .builder()
                    .applicationComponent(applicationComponent)
                    .screenWidthModule(new ScreenWidthModule())
                    .thumbnailRepositoryModule(new ThumbnailRepositoryModule())
                    .build();
        }
        return galleryActivityComponent;
    }

    public void releaseGalleryActivityComponent() {
//        release reference to activity component
        galleryActivityComponent = null;
    }
}
