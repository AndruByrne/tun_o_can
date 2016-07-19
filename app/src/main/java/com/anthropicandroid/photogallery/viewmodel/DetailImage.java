package com.anthropicandroid.photogallery.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.anthropicandroid.photogallery.BR;

/*
 * Created by Andrew Brin on 7/18/2016.
 */
public class DetailImage extends BaseObservable{

    Integer detailIndex;
    RawBitmapMeasurement rawBitmapMeasurement;

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
