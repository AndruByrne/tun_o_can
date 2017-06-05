package com.anthropicandroid.patterngallery.interactors.startup;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.Pair;
import android.util.Log;

import com.anthropicandroid.patterngallery.R;
import com.anthropicandroid.patterngallery.entities.framework.PatternMetaData;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/*
 * Created by Andrew Brin on 7/14/2016.
 */
public class RepositoryPopulator
        implements Realm.Transaction {

    public static final int ALL_THE_QUALITY = 100;
    public static final int QUALITY = ALL_THE_QUALITY;
    public static final String TAG = RepositoryPopulator.class.getSimpleName();
    private Application context;

    // I see the warning, but don't think this is the bottlneck in this class
    public static HashMap<Integer, String> imageIds = new HashMap<Integer, String>() {{
        put(R.drawable.lounge, "Lounge");
        put(R.drawable.louge, "Louge");
        put(R.drawable.logo, "Logo");
        put(R.drawable.hive, "Hive");
        put(R.drawable.air, "Air");
        put(R.drawable.desks, "Desks");
        put(R.drawable.racecar, "Racecar");
        put(R.drawable.mountainview, "Mountain View");
    }};

    public RepositoryPopulator(Application context) {
        this.context = context;
    }

    @Override
    public void execute(final Realm realm) {
        if (realm.where(PatternMetaData.class).findAll().size() > 6) return;
        Observable<Map.Entry<Integer, String>> imageIdObs = Observable.from(imageIds.entrySet());
        Observable<byte[]> imageObs = imageIdObs
                // get Image
                .map(new Func1<Map.Entry<Integer, String>, Bitmap>() {
                    @Override
                    public Bitmap call(Map.Entry<Integer, String> imageId) {
                        return BitmapFactory.decodeResource(
                                context.getResources(),
                                imageId.getKey());
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
                .zip(
                        imageIdObs,
                        imageObs,
                        new Func2<Map.Entry<Integer, String>, byte[], Pair<Map.Entry<Integer,
                                String>, byte[]>>() {
                            @Override
                            public Pair<Map.Entry<Integer, String>, byte[]> call(
                                    Map.Entry<Integer, String> imageId,
                                    byte[]
                                            image
                            ) {
                                return new Pair<>(imageId, image);
                            }
                        })
                .subscribe(
                        new Action1<Pair<Map.Entry<Integer, String>, byte[]>>() {
                            @Override
                            public void call(Pair<Map.Entry<Integer, String>, byte[]> imageData) {
                                Log.d(TAG, "id is: " + imageData.first + " and image is " +
                                        "" + imageData.second.length + " long");
                                // Yes this is ugly; it's the last thing I wrote =]
                                final PatternMetaData patternMetaData = new PatternMetaData();
                                patternMetaData.setIndex(imageData.first.getKey());
                                patternMetaData.setDescription(imageData.first.getValue());
                                patternMetaData.setImage(imageData.second);
                                PatternMetaData image = realm.copyToRealmOrUpdate(
                                        patternMetaData);
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
