package com.anthropicandroid.patterngallery;

import android.app.Application;

import com.anthropicandroid.patterngallery.injectionmodules.ActionHandlersModule;
import com.anthropicandroid.patterngallery.injectionmodules.AnimatorModule;
import com.anthropicandroid.patterngallery.injectionmodules.AppModule;
import com.anthropicandroid.patterngallery.injectionmodules.ApplicationComponent;
import com.anthropicandroid.patterngallery.injectionmodules.DaggerApplicationComponent;
import com.anthropicandroid.patterngallery.injectionmodules.DaggerGalleryActivityComponent;
import com.anthropicandroid.patterngallery.injectionmodules.GalleryActivityComponent;
import com.anthropicandroid.patterngallery.injectionmodules.OnBackPressedModule;
import com.anthropicandroid.patterngallery.injectionmodules.RealmModule;
import com.anthropicandroid.patterngallery.injectionmodules.RepositoryModule;
import com.anthropicandroid.patterngallery.injectionmodules.ScreenMetricsModule;

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
