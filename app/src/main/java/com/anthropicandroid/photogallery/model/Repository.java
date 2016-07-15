package com.anthropicandroid.photogallery.model;

/*
 * Created by Andrew Brin on 7/14/2016.
 */


import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Repository {

    private Observable<Realm> realmObservable;

    public Repository(Observable<Realm> realmObservable) {
        this.realmObservable = realmObservable;
    }

    public Observable<Thumbnail> getThumbnail(final Integer imageIndex) {
        return realmObservable
                // Get Observable of RealmResults for index.
                .flatMap(new Func1<Realm, Observable<RealmResults<Thumbnail>>>() {
                    @Override
                    public Observable<RealmResults<Thumbnail>> call(Realm realm) {
                        return realm
                                .where(Thumbnail.class)
                                .equalTo("index", imageIndex)
                                .findAllAsync()
                                .asObservable();
                    }
                })
                // Filter out Results that are not actually results
                .filter(new Func1<RealmResults<Thumbnail>, Boolean>() {
                    @Override
                    public Boolean call(RealmResults<Thumbnail> thumbnails) {
                        return thumbnails.isLoaded();
                    }
                })
                .map(new Func1<RealmResults<Thumbnail>, Thumbnail>() {
                    @Override
                    public Thumbnail call(RealmResults<Thumbnail> thumbnails) {
                        return thumbnails.first();
                    }
                })

                .subscribeOn(Schedulers.io());
    }


}
