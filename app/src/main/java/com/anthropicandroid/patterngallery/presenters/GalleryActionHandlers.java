package com.anthropicandroid.patterngallery.presenters;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import android.app.Application;
import android.databinding.DataBindingUtil;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.anthropicandroid.patterngallery.databinding.LayoutActivityGalleryBinding;
import com.anthropicandroid.patterngallery.databinding.LayoutGalleryImageBinding;
import com.anthropicandroid.patterngallery.entities.ui.SVGItemViewModel;
import com.anthropicandroid.patterngallery.view.animation.GalleryToDetailAnimator;

final public class GalleryActionHandlers {

    public static final String TAG = GalleryActionHandlers.class.getSimpleName();
    private GalleryToDetailAnimator animator;
    private final GestureDetector singleTapUpDetector;
    private final GestureDetector downCatchDetector;
    private LayoutActivityGalleryBinding activityGalleryBinding;

    public GalleryActionHandlers(
            GalleryToDetailAnimator animator,
            Application context
    ) {
        this.animator = animator;
        singleTapUpDetector = new GestureDetector(context, new SingleTapUp());
        downCatchDetector = new GestureDetector(context, new DownCatch());
    }

    public boolean gridTouched(
            View view,
            MotionEvent motionEvent
    ) {

        // only want tap ups
        if (!singleTapUpDetector.onTouchEvent(motionEvent)) return false;
        // don't want onDown
        if (downCatchDetector.onTouchEvent(motionEvent)) return true;
        return bindDataAndAnimate(
                view,
                (int) motionEvent.getRawX(),
                (int) motionEvent.getRawY());
    }

    private boolean bindDataAndAnimate(
            View tappedView,
            int clickRawX,
            int clickRawY
    ) {
        if (activityGalleryBinding == null) activityGalleryBinding = DataBindingUtil
                .findBinding((View) tappedView.getParent().getParent());

        LayoutGalleryImageBinding gridItemBinding    = DataBindingUtil.findBinding(tappedView);
        SVGItemViewModel          svgItemViewModel   = gridItemBinding.getViewModel();
        Rect                      tappedViewBounds  = new Rect();

        // measure current view
        tappedView.getGlobalVisibleRect(tappedViewBounds);

        Path path = svgItemViewModel.getPath();
        activityGalleryBinding.setDetailPath(path);
        activityGalleryBinding.setDetailName(svgItemViewModel.getName());

        //  TODO: discuss the identity between the animator and the handlers; maybe a useful concept
        animator.zoomToReplace(
                tappedViewBounds,
                activityGalleryBinding.alphaDetailMattingLayout,
                activityGalleryBinding.svgDetail,
                (float) svgItemViewModel.getLastKnownWidth() /
                        svgItemViewModel.getLastKnownHeight(),
                clickRawX,
                clickRawY,
                activityGalleryBinding.galleryGrid);
        return true;
    }

    private class SingleTapUp
            extends GestureDetector.SimpleOnGestureListener {
        // return true for event we want, and also onDown
        @Override
        public boolean onSingleTapUp(MotionEvent e) { return true; }

        @Override
        public boolean onDown(MotionEvent e) { return true; }
    }

    private class DownCatch
            extends GestureDetector.SimpleOnGestureListener {
        // return true for onDown so we can eliminate it
        @Override
        public boolean onDown(MotionEvent e) { return true; }
    }
}
