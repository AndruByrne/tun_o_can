package com.anthropicandroid.photogallery.injectionmodules;

/*
 * Created by Andrew Brin on 7/17/2016.
 */

import android.app.Application;

import com.anthropicandroid.photogallery.viewmodel.BackPressedRepo;
import com.anthropicandroid.photogallery.viewmodel.animation.DetailToGalleryAnimator;
import com.anthropicandroid.photogallery.viewmodel.animation.GalleryToDetailAnimator;
import com.anthropicandroid.photogallery.viewmodel.animation.ToolBarFlippingAnimator;

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
            DetailToGalleryAnimator detailToGalleryAnimator) {
        return new GalleryToDetailAnimator(
                backPressedRepo,
                detailToGalleryAnimator,
                statusBarHeight);
    }

    @Provides
    @GalleryActivityScope
    DetailToGalleryAnimator getDetailToGalleryAnimator(
            @Named("StatusBarHeight") int statusBarHeight,
            @Named("DetailHeight") int imageViewHeight,
            @Named("ScreenWidth") int screenWidth) {
        return new DetailToGalleryAnimator(statusBarHeight, imageViewHeight, screenWidth);
    }

    @Provides
    @GalleryActivityScope
    ToolBarFlippingAnimator getToolBarFlippingAnimator(
            Application context,
            @Named("StatusBarHeight") int statusBarHeight,
            @Named("ActionBarHeight") int actionBarHeight) {
        return new ToolBarFlippingAnimator(context, statusBarHeight, actionBarHeight);
    }
}
