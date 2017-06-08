package com.anthropicandroid.patterngallery.entities.ui;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.anthropicandroid.patterngallery.BR;

/*
 * Created by Andrew Brin on 7/18/2016.
 */
public class SVGDetailViewModel
        extends BaseObservable {

    String description;
    String detailUri;
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
    public String getDetailUri() {
        return detailUri;
    }

    public void setDetailUri(String detailUri) {
        this.detailUri = detailUri;
        notifyPropertyChanged(BR.detailUri);
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
