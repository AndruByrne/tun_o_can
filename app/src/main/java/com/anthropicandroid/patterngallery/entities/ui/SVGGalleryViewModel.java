package com.anthropicandroid.patterngallery.entities.ui;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.anthropicandroid.patterngallery.BR;
import com.anthropicandroid.patterngallery.interactors.PatternRepository;

/**
 * Created by Andrew Brin on 6/6/2017.
 */
public class SVGGalleryViewModel
        extends BaseObservable {

    private float yVelocity;
    private float yPosition;

    @Bindable
    public String getPatternGroup() {
        return PatternRepository.READ_ONLY_PATTERNS;
    }

    public void setYVelocity(float yVelocity) {
        this.yVelocity = yVelocity;
        notifyPropertyChanged(BR.yVelocity);
    }

    @Bindable
    public float getYVelocity() {
        return yVelocity;
    }

    public void setYPosition(float yPosition) {
        this.yPosition = yPosition;
        notifyPropertyChanged(BR.yPosition);
    }

    @Bindable
    public float getYPosition() {
        return yPosition;
    }

}
