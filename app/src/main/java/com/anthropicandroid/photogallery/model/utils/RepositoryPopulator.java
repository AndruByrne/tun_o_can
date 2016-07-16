package com.anthropicandroid.photogallery.model.utils;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.Pair;
import android.util.Log;

import com.anthropicandroid.photogallery.R;
import com.anthropicandroid.photogallery.model.GalleryImage;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import io.realm.Realm;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/*
 * Created by Andrew Brin on 7/14/2016.
 */
public class RepositoryPopulator implements Realm.Transaction {

    public static final int ALL_THE_QUALITY = 100;
    public static final int QUALITY = ALL_THE_QUALITY;
    public static final String TAG = RepositoryPopulator.class.getSimpleName();
    private Application context;

    public static ArrayList<Integer> imageIds = new ArrayList<Integer>() {{
        add(R.drawable.lounge);
        add(R.drawable.louge);
        add(R.drawable.logo);
        add(R.drawable.hive);
        add(R.drawable.air);
        add(R.drawable.desks);
        add(R.drawable.racecar);
        add(R.drawable.mountainview);
    }};

    public RepositoryPopulator(Application context) {
        this.context = context;
    }

    @Override
    public void execute(final Realm realm) {
        if (realm.where(GalleryImage.class).findAll().size() > 6) return;
        Observable<Integer> imageIdObs = Observable.from(imageIds);
        Observable<byte[]> imageObs = imageIdObs
                // get Image
                .map(new Func1<Integer, Bitmap>() {
                    @Override
                    public Bitmap call(Integer imageId) {
                        return BitmapFactory.decodeResource(context.getResources(), imageId);
                    }
                })
                .map(new Func1<Bitmap, byte[]>() {
                    @Override
                    public byte[] call(Bitmap bitmap) {
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, outputStream);
                        return outputStream.toByteArray();
                    }
                });

        // save images to repository
        Observable
                .zip(imageIdObs, imageObs, new Func2<Integer, byte[], Pair<Integer, byte[]>>() {
                    @Override
                    public Pair<Integer, byte[]> call(Integer imageId, byte[] image) {
                        return new Pair<>(imageId, image);
                    }
                })
                .subscribe(
                        new Action1<Pair<Integer, byte[]>>() {
                            @Override
                            public void call(Pair<Integer, byte[]> imageData) {
                                Log.d(TAG, "id is: " + imageData.first + " and image is " +
                                        "" + imageData.second.length + " long");
                                final GalleryImage galleryImage = new GalleryImage();
                                galleryImage.setIndex(imageData.first);
                                galleryImage.setImage(imageData.second);
                                GalleryImage image = realm.copyToRealmOrUpdate(
                                        galleryImage);
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.e(TAG, "error in inital write: " + throwable.getMessage());
                            }
                        },
                        new Action0() {
                            @Override
                            public void call() {
                            }
                        });
    }
}
