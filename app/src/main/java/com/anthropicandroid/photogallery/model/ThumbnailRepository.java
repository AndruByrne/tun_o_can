package com.anthropicandroid.photogallery.model;

/*
 * Created by Andrew Brin on 7/14/2016.
 */

import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import javax.inject.Named;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.anthropicandroid.photogallery.model.utils.Utils.decodeSampledBitmap;
import static com.anthropicandroid.photogallery.model.utils.Utils.dipToPixels;

public class ThumbnailRepository {

    private Observable<Realm> realmObservable;
    private int screenWidth;
    private DisplayMetrics metrics;

    public ThumbnailRepository(
            Observable<Realm> realmObservable,
            @Named("ScreenWidth") int screenWidth,
            DisplayMetrics metrics) {
        this.realmObservable = realmObservable;
        this.screenWidth = screenWidth;
        this.metrics = metrics;
    }

    public Observable<Bitmap> getThumbnail(final Integer imageIndex) {
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
                .map(new Func1<Thumbnail, Bitmap>() {
                    @Override
                    public Bitmap call(Thumbnail thumbnail) {
                        int widthAndHeight = screenWidth / 2 - dipToPixels(metrics, 20);
                        return decodeSampledBitmap(
                                thumbnail.getThumbnail(),
                                widthAndHeight,
                                widthAndHeight);
                    }
                })
                .subscribeOn(Schedulers.io());
    }





}
