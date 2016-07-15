package com.anthropicandroid.photogallery.injectionmodules;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import android.app.Application;

import com.anthropicandroid.photogallery.viewmodel.UserActionHandlers;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.Realm;
import rx.Observable;

@Singleton //  Singleton is the annotation for the Application scope
@Component(modules = {
        AppModule.class,
        UserActionHandlersModule.class,
        RealmModule.class})
public interface ApplicationComponent {

    Application getApplication();

    UserActionHandlers getUserActionHandlers();

    Observable<Realm> getRealmObs();
}