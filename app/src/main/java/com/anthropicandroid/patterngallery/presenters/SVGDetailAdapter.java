package com.anthropicandroid.patterngallery.presenters;

/*
 * Created by Andrew Brin on 7/17/2016.
 */

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.anthropicandroid.patterngallery.R;
import com.anthropicandroid.patterngallery.entities.interactions.PatternMetaData;
import com.anthropicandroid.patterngallery.entities.ui.SVGItemViewModel;
import com.anthropicandroid.patterngallery.routers.gallery.GalleryActivityComponent;
import com.anthropicandroid.patterngallery.entities.ui.RawBitmapMeasurement;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class SVGDetailAdapter {

    public static final String TAG = SVGDetailAdapter.class.getSimpleName();

    @BindingAdapter("svgDetailViewModel")
    public static void setSVGDetailViewModel(
            final GalleryActivityComponent galleryActivityComponent,
            final ImageView imageView,
            final SVGItemViewModel svgItemViewModel
    ) {
       imageView.setImageDrawable(imageView.getContext().getResources().getDrawable(R.drawable.ic_empty_rect_24dp));
    }
}
