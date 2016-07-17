package com.anthropicandroid.photogallery.injectionmodules;

/*
 * Created by Andrew Brin on 7/17/2016.
 */

import com.anthropicandroid.photogallery.viewmodel.BackPressedRepo;
import com.anthropicandroid.photogallery.viewmodel.GridToDetailAnimator;

import dagger.Module;
import dagger.Provides;

@Module
public class AnimatorModule {

    @Provides
    @GalleryActivityScope
    GridToDetailAnimator getGridToDetailAnimator(BackPressedRepo backPressedRepo){
        return new GridToDetailAnimator(backPressedRepo);
    }
}
