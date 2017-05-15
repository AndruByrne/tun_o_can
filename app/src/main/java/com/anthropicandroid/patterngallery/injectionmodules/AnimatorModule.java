package com.anthropicandroid.patterngallery.injectionmodules;

/*
 * Created by Andrew Brin on 7/17/2016.
 */

import android.app.Application;

import com.anthropicandroid.patterngallery.viewmodel.BackPressedRepo;
import com.anthropicandroid.patterngallery.viewmodel.animation.DetailToGalleryAnimator;
import com.anthropicandroid.patterngallery.viewmodel.animation.GalleryToDetailAnimator;
import com.anthropicandroid.patterngallery.viewmodel.animation.ToolBarFlippingAnimator;

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
