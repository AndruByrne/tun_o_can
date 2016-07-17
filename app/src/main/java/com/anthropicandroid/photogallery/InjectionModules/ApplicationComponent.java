package com.anthropicandroid.photogallery.InjectionModules;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import android.app.Application;

import com.anthropicandroid.photogallery.ViewModel.UserActionHandlers;
import com.anthropicandroid.photogallery.model.Repository;
import com.anthropicandroid.photogallery.model.utils.RepositoryPopulator;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.Realm;
import rx.Observable;

@Singleton //  Singleton is the annotation for the Application scope
@Component(modules = {
        AppModule.class,
        RealmModule.class,
        RepositoryModule.class,
        UserActionHandlersModule.class})
public interface ApplicationComponent {

    Application getApplication();

    UserActionHandlers getUserActionHandlers();

    Observable<Realm> getRealmObs();

    Repository getRepository();

    RepositoryPopulator getRepositoryPopulator();
}