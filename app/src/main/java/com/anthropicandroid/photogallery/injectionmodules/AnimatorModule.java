package com.anthropicandroid.photogallery.injectionmodules;

/*
 * Created by Andrew Brin on 7/17/2016.
 */

import android.app.Application;

import com.anthropicandroid.photogallery.viewmodel.BackPressedRepo;
import com.anthropicandroid.photogallery.viewmodel.animation.GalleryToDetailAnimator;
import com.anthropicandroid.photogallery.viewmodel.animation.ToolBarFlippingAnimator;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class AnimatorModule {

    @Provides
    @GalleryActivityScope
    GalleryToDetailAnimator getGridToDetailAnimator(
            BackPressedRepo backPressedRepo,
            @Named("StatusBarHeight") int statusBarHeight) {
        return new GalleryToDetailAnimator(backPressedRepo, statusBarHeight);
    }

    @Provides
    @GalleryActivityScope
    ToolBarFlippingAnimator getToolBarFlippingAnimator(Application context){
        return new ToolBarFlippingAnimator(context);
    }
}
