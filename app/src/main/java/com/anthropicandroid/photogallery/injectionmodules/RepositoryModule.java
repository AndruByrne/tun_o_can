package com.anthropicandroid.photogallery.injectionmodules;

/*
 * Created by Andrew Brin on 7/14/2016.
 */

import com.anthropicandroid.photogallery.model.Repository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import rx.Observable;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    Repository getRepository(
            Observable<Realm> realmObservable) {
        return new Repository(realmObservable);
    }
}
