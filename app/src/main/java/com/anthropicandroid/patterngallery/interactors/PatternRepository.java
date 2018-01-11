package com.anthropicandroid.patterngallery.interactors;

/*
 * Created by Andrew Brin on 7/14/2016.
 */


import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.util.Pair;

import com.anthropicandroid.patterngallery.entities.interactions.PatternMetaData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

public class PatternRepository {

    public static final String  READ_ONLY_PATTERNS    = "READ_ONLY_PATTERNS";
    public static final float   DEMO_RADIUS           = 512;
    private static final double RADIANS_IN_A_CIRCLE   = 2 * Math.PI;
    private SharedPreferences sharedPreferences;

    public PatternRepository(
            SharedPreferences sharedPreferences
    ) {
        this.sharedPreferences = sharedPreferences;
    }

    // TODO: define setters for changing the metadata of the files; they should require a PatternMetaData object to ensure good metadata accounting
    public Observable<PatternMetaData> observePatterns(
            String uriSet
    ) {
        return Observable
                .range(3, 8)
                .flatMap(new Func1<Integer, Observable<PatternMetaData>>() {
                    @Override
                    public Observable<PatternMetaData> call(final Integer numberOfPoints) {
                        final double unitOfRotation = RADIANS_IN_A_CIRCLE / numberOfPoints;
                        return Observable
                                .combineLatest(
                                        Observable.just(numberOfPoints + " sided demo object"),
                                        Observable.just(numberOfPoints % 3),
//                                        Observable.just("400,10/800,800/0,800"),
                                        Observable
                                                .range(1, numberOfPoints)
                                                .map(new Func1<Integer, Double>() {
                                                    @Override
                                                    public Double call(Integer point) {
                                                        return unitOfRotation * point;
                                                    }
                                                })
                                                .map(new Func1<Double, Pair<BigDecimal, BigDecimal>>() {
                                                    @Override
                                                    public Pair<BigDecimal, BigDecimal> call(Double radiansOfRotation) {
                                                        return new Pair<>(
                                                                new BigDecimal(DEMO_RADIUS * Math.cos(radiansOfRotation))
                                                                        .setScale(4, RoundingMode.HALF_EVEN),
                                                                new BigDecimal(DEMO_RADIUS * Math.sin(radiansOfRotation))
                                                                        .setScale(4, RoundingMode.HALF_EVEN));
                                                    }
                                                })
                                                .map(new Func1<Pair<BigDecimal, BigDecimal>, String>() {
                                                    @Override
                                                    public String call(Pair<BigDecimal, BigDecimal> cartesianXY) {
                                                        return cartesianXY.first + "," + cartesianXY.second + "/";
                                                    }
                                                })
                                                .collect(new Func0<StringBuilder>() {
                                                    @Override
                                                    public StringBuilder call() {
                                                        return new StringBuilder();
                                                    }
                                                }, new Action2<StringBuilder, String>() {
                                                    @Override
                                                    public void call(StringBuilder stringBuilder, String s) {
                                                        stringBuilder.append(s);
                                                    }
                                                })
                                                .map(new Func1<StringBuilder, String>() {
                                                    @Override
                                                    public String call(StringBuilder stringBuilder) {
                                                        return stringBuilder.toString();
                                                    }
                                                }),
                                        new Func3<String, Integer, String, PatternMetaData>() {
                                            @Override
                                            public PatternMetaData call(
                                                    final String name,
                                                    final Integer permutations,
                                                    final String serializedPoints
                                            ) {
                                                return new PatternMetaData() {
                                                    @Override
                                                    public String getName() {
                                                        return name;
                                                    }

                                                    @Override
                                                    public String getLastKnownWidth() {
                                                        return "20";
                                                    }

                                                    @Override
                                                    public String getLastKnownHeight() {
                                                        return "300";
                                                    }

                                                    @Override
                                                    public String getPathPoints() {
                                                        return serializedPoints;
                                                    }

                                                    @Override
                                                    public Boolean getWellBehaved() {
                                                        return true;
                                                    }

                                                    @Override
                                                    public Integer getNumPermutations() {
                                                        return permutations;
                                                    }
                                                };
                                            }
                                        }
                                );
                    }
                })
                .subscribeOn(Schedulers.newThread());
    }

    @NonNull
    private static Callable<Boolean> callGetBooleanForParam(
            final SharedPreferences sharedPreferences,
            final String uri,
            final String boooleanParam
    ) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call()
                    throws Exception {
                return sharedPreferences.getBoolean(boooleanParam + uri, false);
            }
        };
    }

    @NonNull
    private static Callable<Integer> callGetIntForParam(
            final SharedPreferences sharedPreferences,
            final String uri,
            final String intParam
    ) {
        return new Callable<Integer>() {
            @Override
            public Integer call()
                    throws Exception {
                return sharedPreferences.getInt(intParam + uri, 0);
            }
        };
    }

}
