package com.anthropicandroid.patterngallery.presenters;

/*
 * Created by Andrew Brin on 7/17/2016.
 */

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.anthropicandroid.patterngallery.R;
import com.anthropicandroid.patterngallery.databinding.LayoutActivityGalleryBinding;
import com.anthropicandroid.patterngallery.entities.interactions.PatternMetaData;
import com.anthropicandroid.patterngallery.entities.ui.SVGItemViewModel;
import com.anthropicandroid.patterngallery.routers.gallery.GalleryActivityComponent;
import com.anthropicandroid.patterngallery.entities.ui.RawBitmapMeasurement;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class SVGDetailAdapter {

    public static final String TAG = SVGDetailAdapter.class.getSimpleName();

    @BindingAdapter("svgDetailPath")
    public static void setSVGDetailPath(
            final GalleryActivityComponent galleryActivityComponent,
            final ImageView imageView,
            final Path path
    ) {
        //  Keeping this RectF instantiation in the method, as it would be a static field
        RectF pathBounds = new RectF();
        CoordinatorLayout parent = (CoordinatorLayout) imageView.getParent();
        LayoutActivityGalleryBinding binding = (LayoutActivityGalleryBinding) DataBindingUtil.findBinding(parent);
        ImageView chordOutline = binding.chordOutline;
        AppBarLayout rootAppBarLayout = binding.rootAppBarLayout;

        path.computeBounds(pathBounds, false);

        int targetWidth = parent.getWidth() / 3;
        float targetHeight = targetWidth * pathBounds.height() / pathBounds.width();
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(
                targetWidth,
                (int) targetHeight);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.setMargins(200, rootAppBarLayout.getBottom() + chordOutline.getTop(), 200, 200);
        imageView.setLayoutParams(params);
    }
}
