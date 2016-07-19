package com.anthropicandroid.photogallery.model;

/*
 * Created by Andrew Brin on 7/14/2016.
 */


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

    public Observable<GalleryImage> getImage(final Integer imageIndex) {
        if(imageIndex == null) return Observable.empty();
        return realmObservable
                // Get Observable of RealmResults for index.
                .flatMap(new Func1<Realm, Observable<RealmResults<GalleryImage>>>() {
                    @Override
                    public Observable<RealmResults<GalleryImage>> call(Realm realm) {
                        return realm
                                .where(GalleryImage.class)
                                .equalTo("index", imageIndex)
                                .findAllAsync()
                                .asObservable();
                    }
                })
                // Filter out Results that are not actually results
                .filter(new Func1<RealmResults<GalleryImage>, Boolean>() {
                    @Override
                    public Boolean call(RealmResults<GalleryImage> galleryImages) {
                        return galleryImages.isLoaded();
                    }
                })
                .map(new Func1<RealmResults<GalleryImage>, GalleryImage>() {
                    @Override
                    public GalleryImage call(RealmResults<GalleryImage> galleryImages) {
                        return galleryImages.first();
                    }
                });
    }

    public Boolean addImage(final GalleryImage galleryImage) {
        if(galleryImage == null) return false;
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
                                    realm.copyToRealmOrUpdate(galleryImage);
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
