package com.anthropicandroid.photogallery.injectionmodulz;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import com.anthropicandroid.photogallery.viewmodelz.UserActionHandlers;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UserActionHandlersModule {
    @Provides
    @Singleton
    UserActionHandlers getUserActionHandlers(){
        return new UserActionHandlers();
    }
}
