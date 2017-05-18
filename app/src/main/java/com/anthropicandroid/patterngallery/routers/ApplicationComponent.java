package com.anthropicandroid.patterngallery.routers;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import android.app.Application;

import com.anthropicandroid.patterngallery.interactors.startup.AppModule;
import com.anthropicandroid.patterngallery.frameworks.localstorage.RealmModule;
import com.anthropicandroid.patterngallery.interactors.RepositoryModule;
import com.anthropicandroid.patterngallery.interactors.Repository;
import com.anthropicandroid.patterngallery.interactors.startup.RepositoryPopulator;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.Realm;
import rx.Observable;

@Singleton //  Singleton is the annotation for the Application scope
@Component(modules = {
        AppModule.class,
        RealmModule.class,
        RepositoryModule.class})
public interface ApplicationComponent {

    Application getApplication();

    Observable<Realm> getRealmObs();

    Repository getRepository();

    RepositoryPopulator getRepositoryPopulator();
}