package com.anthropicandroid.patterngallery.routers;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import android.app.Application;

import com.anthropicandroid.patterngallery.interactors.PatternRepository;
import com.anthropicandroid.patterngallery.interactors.RepositoryModule;
import com.anthropicandroid.patterngallery.interactors.startup.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton //  Singleton is the annotation for the Application scope
@Component(modules = {
        AppModule.class,
        RepositoryModule.class})
public interface ApplicationComponent {

    Application getApplication();

    PatternRepository getRepository();

}