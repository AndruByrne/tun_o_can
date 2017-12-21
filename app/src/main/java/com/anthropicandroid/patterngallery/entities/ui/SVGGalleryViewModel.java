package com.anthropicandroid.patterngallery.entities.ui;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.anthropicandroid.patterngallery.BR;
import com.anthropicandroid.patterngallery.interactors.PatternRepository;

/**
 * Created by Andrew Brin on 6/6/2017.
 */
public class SVGGalleryViewModel
        extends BaseObservable
{

    private static final int POWER_OF_VELOCITY = 10;
    private              float sliderValue     = 0;

    @Bindable
    public String getPatternGroup() {
        return PatternRepository.READ_ONLY_PATTERNS;
    }

    public void setValue(
            // yVelocity value assumed to be normalized to 1
            float yVelocity,
            float historicalYPosition,
            float yPosition
    ) {
        sliderValue += (yPosition - historicalYPosition) /
                //  Denominator defines "accuracy" of adjustment
                Math.pow(
                        POWER_OF_VELOCITY,
                        (1 + ( //  Do not want a "0 times position" calculation after modulo
                                1 - yVelocity //  We want our power to be the *inverse* of the velocity
                        ) * 10 % 1 //  convert to integer value normalized to 10
                        ));
        notifyPropertyChanged(BR.sliderValue);
    }

    @Bindable
    public float getSliderValue() {
        return sliderValue;
    }

}
