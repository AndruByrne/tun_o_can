package com.anthropicandroid.patterngallery.viewmodel;


/*
 * Created by Andrew Brin on 7/17/2016.
 */

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.anthropicandroid.patterngallery.BR;

public class RawBitmapMeasurement extends BaseObservable {

    public static final String TAG = RawBitmapMeasurement.class.getSimpleName();
    private int rawWidth;
    private int rawHeight;

    @Bindable
    public int getRawWidth() {
        return rawWidth;
    }

    @Bindable
    public int getRawHeight() {
        return rawHeight;
    }

    public RawBitmapMeasurement setRawWidth(int rawWidth) {
        this.rawWidth = rawWidth;
        notifyPropertyChanged(BR.rawWidth);
        return this;
    }

    public RawBitmapMeasurement setRawHeight(int rawHeight) {
        this.rawHeight = rawHeight;
        notifyPropertyChanged(BR.rawHeight);
        return this;
    }
}