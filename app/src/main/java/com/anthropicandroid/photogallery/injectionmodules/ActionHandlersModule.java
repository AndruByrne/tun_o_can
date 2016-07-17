package com.anthropicandroid.photogallery.injectionmodules;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import com.anthropicandroid.photogallery.viewmodel.GalleryActionHandlers;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ActionHandlersModule {
    @Provides
    @Singleton
    GalleryActionHandlers getGalleryActionHandlers(){
        return new GalleryActionHandlers();
    }
}
