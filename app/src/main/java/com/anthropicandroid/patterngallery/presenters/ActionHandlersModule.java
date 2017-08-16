package com.anthropicandroid.patterngallery.presenters;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import android.app.Application;

import com.anthropicandroid.patterngallery.routers.gallery.BackPressedRepo;
import com.anthropicandroid.patterngallery.routers.gallery.GalleryActivityScope;
import com.anthropicandroid.patterngallery.view.animation.DetailToGalleryAnimator;
import com.anthropicandroid.patterngallery.view.animation.GalleryToDetailAnimator;

import dagger.Module;
import dagger.Provides;

@Module
public class ActionHandlersModule {
    @Provides
    @GalleryActivityScope
    GalleryActionHandlers getGalleryActionHandlers(
            GalleryToDetailAnimator galleryToDetailAnimator,
            Application context,
            DetailActionHandlers detailActionHandlers
    ) {
        return new GalleryActionHandlers(galleryToDetailAnimator, context);
    }

    @Provides
    @GalleryActivityScope
    BottomNavActionHandlers getBottomNavActionHandlers(Application context) {
        // dummy class; no actions from nav bar
        return new BottomNavActionHandlers(context);
    }

    @Provides
    @GalleryActivityScope
    DetailActionHandlers getDetailActionHandlers(
            Application context,
            BackPressedRepo backPressedRepo,
            GalleryToDetailAnimator galleryToDetailAnimator,
            DetailToGalleryAnimator detailToGalleryAnimator
    ) {
        // dummy class; no actions from nav bar
        return new DetailActionHandlers(
                context,
                backPressedRepo,
                galleryToDetailAnimator,
                detailToGalleryAnimator);
    }

}
