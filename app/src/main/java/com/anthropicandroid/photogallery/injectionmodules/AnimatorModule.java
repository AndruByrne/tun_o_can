package com.anthropicandroid.photogallery.injectionmodules;

/*
 * Created by Andrew Brin on 7/17/2016.
 */

import com.anthropicandroid.photogallery.viewmodel.BackPressedRepo;
import com.anthropicandroid.photogallery.viewmodel.GalleryToDetailAnimator;

import dagger.Module;
import dagger.Provides;

@Module
public class AnimatorModule {

    @Provides
    @GalleryActivityScope
    GalleryToDetailAnimator getGridToDetailAnimator(BackPressedRepo backPressedRepo){
        return new GalleryToDetailAnimator(backPressedRepo);
    }
}
