package com.anthropicandroid.patterngallery.entities.ui;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.util.SortedList;

import com.anthropicandroid.patterngallery.BR;
import com.anthropicandroid.patterngallery.entities.interactions.PatternMetaData;
import com.anthropicandroid.patterngallery.interactors.PatternRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew Brin on 6/6/2017.
 */
public class SVGGalleryViewModel
        extends BaseObservable {

    @Bindable
    public String getPatternGroup() {
        return PatternRepository.READ_ONLY_PATTERNS;
    }

}
