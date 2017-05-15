package com.anthropicandroid.patterngallery.interactors.startup;

import android.app.Application;

import com.anthropicandroid.patterngallery.presenters.ActionHandlersModule;
import com.anthropicandroid.patterngallery.routers.ApplicationComponent;
import com.anthropicandroid.patterngallery.routers.DaggerApplicationComponent;
import com.anthropicandroid.patterngallery.view.animation.AnimatorModule;
import com.anthropicandroid.patterngallery.routers.gallery.DaggerGalleryActivityComponent;
import com.anthropicandroid.patterngallery.routers.gallery.GalleryActivityComponent;
import com.anthropicandroid.patterngallery.routers.gallery.OnBackPressedModule;
import com.anthropicandroid.patterngallery.frameworks.localstorage.RealmModule;
import com.anthropicandroid.patterngallery.interactors.RepositoryModule;
import com.anthropicandroid.patterngallery.view.ScreenMetricsModule;

/*
 * Created by Andrew Brin on 7/12/2016.
 */
public class PatternGalleryApplication extends Application {

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
        // create component to be tied to the activity lifespan
        if (galleryActivityComponent == null) {
            galleryActivityComponent = DaggerGalleryActivityComponent
                    .builder()
                    .applicationComponent(applicationComponent)
                    .actionHandlersModule(new ActionHandlersModule())
                    .animatorModule(new AnimatorModule())
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
