package com.anthropicandroid.patterngallery.entities.ui;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.v4.util.Pair;
import android.util.Log;

import com.anthropicandroid.patterngallery.BR;
import com.anthropicandroid.patterngallery.interactors.PatternRepository;
import com.google.common.collect.Lists;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;

/*
 * Created by Andrew Brin on 7/16/2016.
 */
public class SVGItemViewModel
        extends BaseObservable {

    private String name;
    private int maxChildWidth;
    private int lastKnownWidth;
    private int lastKnownHeight;
    private String pathPoints;
    private boolean wellBehaved;
    private int colorResId;
    private Path path;

    @Bindable
    public int getLastKnownWidth() {
        return lastKnownWidth;
    }

    public void setLastKnownWidth(
            int lastKnownWidth
    ) {
        this.lastKnownWidth = lastKnownWidth;
        notifyPropertyChanged(BR.lastKnownWidth);
    }

    @Bindable
    public int getLastKnownHeight() {
        return lastKnownHeight;
    }

    public void setLastKnownHeight(int lastKnownHeight) {
        this.lastKnownHeight = lastKnownHeight;
        notifyPropertyChanged(BR.lastKnownHeight);
    }

    @Bindable
    public String getPathPoints() {
        return pathPoints;
    }

    public void setPathPoints(
            String pathPoints
    ) {
        Log.d(getClass().getSimpleName(), "setting path points");
        this.pathPoints = pathPoints;
        this.path = Observable
                .just(pathPoints)
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        Log.d(getClass().getSimpleName(), "updating path");
                        notifyPropertyChanged(BR.pathPoints);
                        notifyPropertyChanged(BR.path);
                    }
                })
                .flatMapIterable(new Func1<String, Iterable<String>>() {
                    @Override
                    public Iterable<String> call(String s) {
                        return Lists.newArrayList(s.split("/"));
                    }
                })
                .map(new Func1<String, String[]>() {
                    @Override
                    public String[] call(String s) {
                        return s.split(",");
                    }
                })
//                .map(new Func1<String[], String[]>() {
//                    @Override
//                    public String[] call(String[] strings) {
//                        if(!strings[0].isEmpty()) return strings;
//                        else return new String[]{""+PatternRepository.DEMO_RADIUS, "0"};
//                    }
//                })
                .map(new Func1<String[], float[]>() {
                    @Override
                    public float[] call(String[] strings) {
                        float[] floats = new float[2];
                        floats[0] = Float.valueOf(strings[0]);
                        floats[1] = Float.valueOf(strings[1]);
                        return floats;
                    }
                })
                .collect(new Func0<Path>() {
                    @Override
                    public Path call() {
                        Path path = new Path();
                        path.moveTo(PatternRepository.DEMO_RADIUS, 0);
                        return path;  // TODO: play the first path in a Move
                    }
                }, new Action2<Path, float[]>() {
                    @Override
                    public void call(Path path, float[] floats) {
                        path.lineTo(floats[0], floats[1]);
                    }
                })
                .toBlocking()
                .first();


        PathMeasure pathMeasure = new PathMeasure(path, false);
        Log.d(getClass().getSimpleName(), "path length: " + pathMeasure.getLength());
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(
            String name
    ) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public int getColorResId() {
        return colorResId;
    }

    public void setColorResId(int color) {
        this.colorResId = color;
        notifyPropertyChanged(BR.colorResId);
    }

    @Bindable
    public boolean isWellBehaved() {
        return wellBehaved;
    }

    public void setWellBehaved(
            boolean wellBehaved
    ) {
        this.wellBehaved = wellBehaved;
        notifyPropertyChanged(BR.wellBehaved);
    }

    @Bindable
    public int getMaxChildWidth() {
        return maxChildWidth;
    }

    public void setMaxChildWidth(int maxChildWidth) {
        this.maxChildWidth = maxChildWidth;
        notifyPropertyChanged(BR.maxChildWidth);
    }

    @Bindable
    public Path getPath() {
        Log.d(getClass().getSimpleName(), "giving path");
        return path == null ? new Path() : path;
    }
}
