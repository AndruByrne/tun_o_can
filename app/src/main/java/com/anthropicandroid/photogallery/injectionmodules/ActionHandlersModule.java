package com.anthropicandroid.photogallery.injectionmodules;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import android.app.Application;

import com.anthropicandroid.photogallery.viewmodel.BottomNavActionHandlers;
import com.anthropicandroid.photogallery.viewmodel.DetailActionHandlers;
import com.anthropicandroid.photogallery.viewmodel.GalleryActionHandlers;
import com.anthropicandroid.photogallery.viewmodel.GalleryToDetailAnimator;

import dagger.Module;
import dagger.Provides;

@Module
public class ActionHandlersModule {
    @Provides
    @GalleryActivityScope
    GalleryActionHandlers getGalleryActionHandlers(
            GalleryToDetailAnimator galleryToDetailAnimator,
            Application context,
            DetailActionHandlers detailActionHandlers) {
        return new GalleryActionHandlers(galleryToDetailAnimator, context, detailActionHandlers);
    }

    @Provides
    @GalleryActivityScope
    BottomNavActionHandlers getBottomNavActionHandlers(Application context) {
        // dummy class; no actions from nav bar
        return new BottomNavActionHandlers(context);
    }

    @Provides
    @GalleryActivityScope
    DetailActionHandlers getDetailActionHandlers(Application context) {
        // dummy class; no actions from nav bar
        return new DetailActionHandlers(context);
    }

}
