package com.anthropicandroid.patterngallery.entities.ui;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.anthropicandroid.patterngallery.BR;

/*
 * Created by Andrew Brin on 7/16/2016.
 */
public class SVGItemViewModel
        extends BaseObservable {

    private String name;
    private int maxChildWidth;
    private int lastKnownWidth;
    private int lastKnownHeight;
    private String uri;
    private boolean wellBehaved;
    private int colorResId;

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
    public String getUri() {
        return uri;
    }

    public void setUri(
            String uri
    ) {
        this.uri = uri;
        notifyPropertyChanged(BR.uri);
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
}
