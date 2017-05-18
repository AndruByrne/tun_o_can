package com.anthropicandroid.patterngallery.frameworks.localstorage;

import android.app.Application;

import com.anthropicandroid.patterngallery.interactors.startup.RepositoryPopulator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import rx.Observable;

/*
 * Created by Andrew Brin on 7/14/2016.
 */

@Module
public class RealmModule {

    @Provides
    @Singleton
    Observable<Realm> getRealmObs(
            Application context,
            RepositoryPopulator repositoryPopulator
    ) {
        RealmConfiguration realmConfiguration = new RealmConfiguration
                .Builder(context)
                .initialData(repositoryPopulator)
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        return Realm.getDefaultInstance().asObservable();
    }
}
