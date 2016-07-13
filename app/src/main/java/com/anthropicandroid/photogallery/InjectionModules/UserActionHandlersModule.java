package com.anthropicandroid.photogallery.InjectionModules;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

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
