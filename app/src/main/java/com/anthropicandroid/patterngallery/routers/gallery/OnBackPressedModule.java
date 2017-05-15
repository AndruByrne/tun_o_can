package com.anthropicandroid.patterngallery.routers.gallery;

/*
 * Created by Andrew Brin on 7/17/2016.
 */

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
