package com.anthropicandroid.photogallery.InjectionModules;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;

@Singleton //  Singleton is the annotation for the Application scope
@Component(modules = {
        AppModule.class,
        UserActionHandlersModule.class })
public interface ApplicationComponent {

    Application getContext();

    UserActionHandlers getUserActionHandlers();

}