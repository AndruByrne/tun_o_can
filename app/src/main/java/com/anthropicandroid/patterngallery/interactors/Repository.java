package com.anthropicandroid.patterngallery.interactors;

/*
 * Created by Andrew Brin on 7/14/2016.
 */


import com.anthropicandroid.patterngallery.entities.framework.PatternMetaData;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class Repository {

    private Observable<Realm> realmObservable;

    public Repository(Observable<Realm> realmObservable) {
        this.realmObservable = realmObservable;
    }

    public Observable<PatternMetaData> getImage(final Integer imageIndex) {
        if (imageIndex == null) return Observable.empty();
        return realmObservable
                // Get Observable of RealmResults for index.
                .flatMap(new Func1<Realm, Observable<RealmResults<PatternMetaData>>>() {
                    @Override
                    public Observable<RealmResults<PatternMetaData>> call(Realm realm) {
                        return realm
                                .where(PatternMetaData.class)
                                .equalTo("index", imageIndex)
                                .findAllAsync()
                                .asObservable();
                    }
                })
                // Filter out Results that are not actually results
                .filter(new Func1<RealmResults<PatternMetaData>, Boolean>() {
                    @Override
                    public Boolean call(RealmResults<PatternMetaData> patternMetaDatas) {
                        return patternMetaDatas.isLoaded();
                    }
                })
                .map(new Func1<RealmResults<PatternMetaData>, PatternMetaData>() {
                    @Override
                    public PatternMetaData call(RealmResults<PatternMetaData> patternMetaDatas) {
                        return patternMetaDatas.first();
                    }
                });
    }

    public Boolean addImage(final PatternMetaData patternMetaData) {
        if (patternMetaData == null) return false;
        // Unused; repo populator does this with transaction bounding in its "Transaction" closure
        return realmObservable
                .flatMap(new Func1<Realm, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(final Realm realm) {
                        return Observable.create(new Observable.OnSubscribe<Boolean>() {
                            @Override
                            public void call(Subscriber<? super Boolean> subscriber) {
                                try {
                                    realm.beginTransaction();
                                    realm.copyToRealmOrUpdate(patternMetaData);
                                    realm.commitTransaction();
                                    subscriber.onNext(true);
                                } catch (Exception e) {
                                    subscriber.onError(e);
                                }
                                subscriber.onCompleted();
                            }
                        });
                    }
                })
                .take(1).toBlocking().first();
    }
}
