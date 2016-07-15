package com.anthropicandroid.photogallery.injectionmodules;

import android.app.Application;

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

    public RealmModule(Application context) {
        // using in-memory DB only
        RealmConfiguration realmConfiguration = new RealmConfiguration
                .Builder(context)
                .inMemory()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    @Provides
    @Singleton
    Observable<Realm> getRealmObs(Application context) {
        return Realm.getDefaultInstance().asObservable();
    }
}
