package com.anthropicandroid.patterngallery.entities.ui;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.anthropicandroid.patterngallery.BR;

/*
 * Created by Andrew Brin on 7/18/2016.
 */
public class DetailImage
        extends BaseObservable {

    String description;
    Integer detailIndex;
    RawBitmapMeasurement rawBitmapMeasurement;

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public Integer getDetailIndex() {
        return detailIndex;
    }

    public void setDetailIndex(Integer detailIndex) {
        this.detailIndex = detailIndex;
        notifyPropertyChanged(BR.detailIndex);
    }

    @Bindable
    public RawBitmapMeasurement getRawBitmapMeasurement() {
        return rawBitmapMeasurement;
    }

    public void setRawBitmapMeasurement(RawBitmapMeasurement rawBitmapMeasurement) {
        this.rawBitmapMeasurement = rawBitmapMeasurement;
        notifyPropertyChanged(BR.rawBitmapMeasurement);
    }

}
