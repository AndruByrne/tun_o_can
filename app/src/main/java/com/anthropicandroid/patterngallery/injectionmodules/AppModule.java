package com.anthropicandroid.patterngallery.injectionmodules;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    Application application;

    public AppModule(Application application){ this.application = application; }

    @Provides
    @Singleton
    Application providesApplication(){
        return application;
    }
}
