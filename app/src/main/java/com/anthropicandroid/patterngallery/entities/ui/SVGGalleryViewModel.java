package com.anthropicandroid.patterngallery.entities.ui;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.util.SortedList;

import com.anthropicandroid.patterngallery.BR;
import com.anthropicandroid.patterngallery.entities.interactions.PatternMetaData;

import java.util.List;

/**
 * Created by Andrew Brin on 6/6/2017.
 */
public class SVGGalleryViewModel
        extends BaseObservable {

    SortedList<PatternMetaData> patterns;

    @Bindable
    public SortedList<PatternMetaData> getPatterns() {
        return patterns;
    }

    public void setPatterns(SortedList<PatternMetaData> patterns) {
        this.patterns = patterns;
        notifyPropertyChanged(BR.patterns);
    }

}
