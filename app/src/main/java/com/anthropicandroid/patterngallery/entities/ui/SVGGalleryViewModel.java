package com.anthropicandroid.patterngallery.entities.ui;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.anthropicandroid.patterngallery.BR;
import com.anthropicandroid.patterngallery.interactors.PatternRepository;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created by Andrew Brin on 6/6/2017.
 */
public class SVGGalleryViewModel
        extends BaseObservable
{

    private static final BigDecimal  BIG_D_ZERO             = new BigDecimal(0);
    private static final MathContext SLIDER_VALUE_PRECISION = new MathContext(6);
    public static final  float       SLIDER_MAX_VELOCITY    = 10000;

    private BigDecimal sliderValue   = new BigDecimal(0);
    private String     abscissaValue = "1337";
    private String     ordinalValue  = "7331";

    // When either ordinal or slider value change, update views listening to ordinal
    @Bindable({
                      "ordinalValue",
                      "sliderValue"})
    public String getOrdinalValue() {
        // If slider value is not 0, display it, else display ordinal value
        return sliderValue.compareTo(BIG_D_ZERO) != 0 ? sliderValue.toPlainString() : ordinalValue;
    }

    public void setOrdinalValue(String ordinalValue) {
        this.ordinalValue = ordinalValue;
        notifyPropertyChanged(BR.ordinalValue);
    }

    @Bindable
    public String getPatternGroup() {
        return PatternRepository.READ_ONLY_PATTERNS;
    }

    @Bindable
    public String getSliderValue() {
        return sliderValue.toPlainString();
    }

    public void setSliderValue(
            float yVelocity,
            float historicalYPosition,
            float yPosition
    ) {
        if (
                historicalYPosition != 0
                        || yVelocity != 0
                ) {
            int        scale          = (int) (Math.log(Math.abs(yVelocity) / SLIDER_MAX_VELOCITY));
            BigDecimal deltaY         = new BigDecimal(historicalYPosition - yPosition);
            int        deltaYIntValue = deltaY.intValue();
            if (-10 < deltaYIntValue && deltaYIntValue < 10) sliderValue = sliderValue.add(
                    deltaY.movePointRight(scale < -6 ? -6 : scale + 1)
                          .setScale(4, BigDecimal.ROUND_DOWN),
                    SLIDER_VALUE_PRECISION);
            notifyPropertyChanged(BR.sliderValue);
            Log.i(
                    getClass().getSimpleName(),
                    "raw delta y : " + deltaY.toPlainString() +
                            " with derived scale: " + scale +
                            " and slider value: " + sliderValue);
        }
    }

    public BigDecimal clearSliderValue() {
        BigDecimal sliderValueHistoric = sliderValue;
        sliderValue = BIG_D_ZERO;
        return sliderValueHistoric;
    }

    @Bindable
    public String getAbscissaValue() {
        return abscissaValue;
    }

    public void setAbscissaValue(String abscissaValue) {
        this.abscissaValue = abscissaValue;
        notifyPropertyChanged(BR.abscissaValue);
    }
}
