package com.anthropicandroid.patterngallery.entities.ui;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.anthropicandroid.patterngallery.BR;

/*
 * Created by Andrew Brin on 7/16/2016.
 */
public class GalleryItemViewModel
        extends BaseObservable {

    private int width;
    private int index;
    private int colorResId;
    private String description;
    private RawBitmapMeasurement rawBitmapMeasurement;

    @Bindable
    public int getWidth() {
        return width;
    }

    public void setWidth(
            int width
    ) {
        this.width = width;
        notifyPropertyChanged(BR.width);
    }

    @Bindable
    public int getIndex() {
        return index;
    }

    public void setIndex(
            int index
    ) {
        this.index = index;
        notifyPropertyChanged(BR.index);
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(
            String description
    ) {
        this.description = description;
        notifyPropertyChanged(BR.description);
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
    public RawBitmapMeasurement getRawBitmapMeasurement() {
        return rawBitmapMeasurement;
    }

    public void setRawBitmapMeasurement(
            RawBitmapMeasurement rawBitmapMeasurement
    ) {
        this.rawBitmapMeasurement = rawBitmapMeasurement;
        notifyPropertyChanged(BR.rawBitmapMeasurement);
    }
}
