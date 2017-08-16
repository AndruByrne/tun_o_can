package com.anthropicandroid.patterngallery.presenters;

/*
 * Created by Andrew Brin on 7/17/2016.
 */

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;

import com.anthropicandroid.patterngallery.databinding.LayoutActivityGalleryBinding;
import com.anthropicandroid.patterngallery.view.PatternDetailView;

public class SVGDetailAdapter {

    public static final String TAG = SVGDetailAdapter.class.getSimpleName();

    @BindingAdapter({"svgDetailPath"})
    public static void setSVGDetailPath(
            final PatternDetailView imageView,
            Path path
    ) {
        Log.d("SVG detail ppath", " binding adapter engaged");

        //  Keeping this RectF instantiation in the method, as it would be a static field
        RectF pathBounds = new RectF();
        CoordinatorLayout parent = (CoordinatorLayout) imageView.getParent();
        LayoutActivityGalleryBinding binding = (LayoutActivityGalleryBinding) DataBindingUtil.findBinding(parent);
        ImageView chordOutline = binding.chordOutline;
        AppBarLayout rootAppBarLayout = binding.rootAppBarLayout;

        if (path != null) {
            path.computeBounds(pathBounds, false);
            Log.d("p[ath", "did compute bounds: " + pathBounds.toShortString());

            int targetWidth = parent.getWidth() / 3;
            float targetHeight = targetWidth * pathBounds.height() / pathBounds.width();
            CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(
                    targetWidth,
                    (int) targetHeight);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            params.setMargins(0, rootAppBarLayout.getBottom() + chordOutline.getTop(), 0, 0);
            imageView.setLayoutParams(params);
            imageView.requestLayout();
            imageView.invalidate();
            imageView.setPath(path);
        }
    }
}
