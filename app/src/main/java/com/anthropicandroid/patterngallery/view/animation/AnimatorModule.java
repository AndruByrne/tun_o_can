package com.anthropicandroid.patterngallery.view.animation;

/*
 * Created by Andrew Brin on 7/17/2016.
 */

import android.app.Application;

import com.anthropicandroid.patterngallery.routers.gallery.BackPressedRepo;
import com.anthropicandroid.patterngallery.routers.gallery.GalleryActivityScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class AnimatorModule {

    @Provides
    @GalleryActivityScope
    GalleryToDetailAnimator getGalleryToDetailAnimator(
            BackPressedRepo backPressedRepo,
            @Named("StatusBarHeight") int statusBarHeight,
            DetailToGalleryAnimator detailToGalleryAnimator,
            Application context) {
        return new GalleryToDetailAnimator(
                backPressedRepo,
                detailToGalleryAnimator,
                statusBarHeight,
                context.getResources());
    }

    @Provides
    @GalleryActivityScope
    DetailToGalleryAnimator getDetailToGalleryAnimator(
            @Named("StatusBarHeight") int statusBarHeight,
            @Named("DetailHeight") int imageViewHeight,
            @Named("ScreenWidth") int screenWidth,
            Application context) {
        return new DetailToGalleryAnimator(
                context.getResources(), statusBarHeight,
                imageViewHeight,
                screenWidth
        );
    }

    @Provides
    @GalleryActivityScope
    ToolBarFlippingAnimator getToolBarFlippingAnimator(
            Application context,
            @Named("StatusBarHeight") int statusBarHeight,
            @Named("ActionBarHeight") int actionBarHeight) {
        return new ToolBarFlippingAnimator(
                context.getResources(),
                statusBarHeight,
                actionBarHeight);
    }
}
