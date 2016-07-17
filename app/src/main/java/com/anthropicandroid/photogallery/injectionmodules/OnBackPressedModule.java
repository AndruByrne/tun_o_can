package com.anthropicandroid.photogallery.injectionmodules;

/*
 * Created by Andrew Brin on 7/17/2016.
 */

import com.anthropicandroid.photogallery.viewmodel.BackPressedRepo;

import dagger.Module;
import dagger.Provides;

@Module
public class OnBackPressedModule {

    @Provides
    @GalleryActivityScope
    BackPressedRepo getBackPressedRepo(){
        return new BackPressedRepo();
    }
}
