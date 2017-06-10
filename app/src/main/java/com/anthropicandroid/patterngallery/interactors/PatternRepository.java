package com.anthropicandroid.patterngallery.interactors;

/*
 * Created by Andrew Brin on 7/14/2016.
 */


import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;

import com.anthropicandroid.patterngallery.entities.interactions.PatternMetaData;
import com.google.common.collect.Lists;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func6;

public class PatternRepository {

    public static final String IMPORTED_PATTERN_URIS = "imported_pattern_uris";
    private SharedPreferences sharedPreferences;

    public PatternRepository(
            SharedPreferences sharedPreferences
    ) {
        this.sharedPreferences = sharedPreferences;
    }

    public Observable<VectorDrawableCompat> getImage(final String imageUri) {
        if (imageUri == null) return Observable.empty();
        else return Observable.empty();
    }

    // TODO: define setters for changing the metadata of the files; they should require a PatternMetaData object to ensure good metadata accounting
    public Observable<PatternMetaData> observePatterns(
            String uriSet
    ) {
        return Observable
                .fromCallable(new Callable<Set<String>>() {
                    @Override
                    public Set<String> call()
                            throws Exception {
                        return sharedPreferences.getStringSet(IMPORTED_PATTERN_URIS, new HashSet<String>());
                    }
                })
                .flatMapIterable(new Func1<Set<String>, Iterable<? extends String>>() {
                    @Override
                    public Iterable<? extends String> call(Set<String> strings) {
                        return strings;
                    }
                })
                .flatMap(new Func1<String, Observable<PatternMetaData>>() {
                    @Override
                    public Observable<PatternMetaData> call(final String uri) {
                        return Observable.combineLatest(
                                Observable.just(uri),
                                Observable
                                        .just(uri)
                                        .flatMapIterable(new Func1<String, Iterable<? extends String>>() {
                                            @Override
                                            public Iterable<String> call(String uri) {
                                                return Lists.newArrayList(uri.split("/"));
                                            }
                                        })
                                        .last()
                                        .map(new Func1<String, String>() {
                                            @Override
                                            public String call(String s) {
                                                return s.substring(0, s.length() - 5);
                                            }
                                        }),
                                Observable.fromCallable(callGetIntForParam(sharedPreferences, uri, "height+")),
                                Observable.fromCallable(callGetIntForParam(sharedPreferences, uri, "width+")),
                                Observable.fromCallable(callGetBooleanForParam(sharedPreferences, uri, "well_behaved+")),
                                Observable.fromCallable(callGetIntForParam(sharedPreferences, uri, "num_permutations+")),
                                new Func6<String, String, Integer, Integer, Boolean, Integer, PatternMetaData>() {
                                    @Override
                                    public PatternMetaData call(
                                            final String uri,
                                            final String name,
                                            final Integer height,
                                            final Integer width,
                                            final Boolean wellBehaved,
                                            final Integer numberOfPermutations
                                    ) {
                                        return new PatternMetaData() {
                                            @Override
                                            public String getName() {
                                                return name;
                                            }

                                            @Override
                                            public Integer getLastKnownWidth() {
                                                return width;
                                            }

                                            @Override
                                            public Integer getLastKnownHeight() {
                                                return height;
                                            }

                                            @Override
                                            public String getOriginalUri() {
                                                return uri;
                                            }

                                            @Override
                                            public Boolean getWellBehaved() {
                                                return wellBehaved;
                                            }

                                            @Override
                                            public Integer getNumPermutations() {
                                                return numberOfPermutations;
                                            }
                                        };
                                    }
                                }
                        );
                    }
                });


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
