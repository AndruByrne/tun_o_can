package com.anthropicandroid.patterngallery.entities.ui;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.util.SortedList;

import com.anthropicandroid.patterngallery.BR;
import com.anthropicandroid.patterngallery.entities.interactions.PatternMetaData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew Brin on 6/6/2017.
 */
public class SVGGalleryViewModel
        extends BaseObservable {

    String patternGroup;

    Map<String, String> patternPaths = new HashMap<>();

    @Bindable
    public String getPatternGroup() {
        return patternGroup;
    }

    public void setPatternGroup(String patternGroup) {
        this.patternGroup = patternGroup;
        notifyPropertyChanged(BR.patternGroup);
    }

    public Map<String, String> getPatternPaths() {
        return patternPaths;
    }

}
