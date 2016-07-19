package com.anthropicandroid.photogallery.viewmodel.animation;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.anthropicandroid.photogallery.injectionmodules.GalleryActivityComponent;

/*
 * Created by Andrew Brin on 7/19/2016.
 */

public class ToolbarFlippingBehavior extends CoordinatorLayout.Behavior<AppBarLayout> {

    public static final String TAG = ToolbarFlippingBehavior.class.getSimpleName();
    private boolean flippedToDetail = false;
    private final ToolBarFlippingAnimator animator;

    public ToolbarFlippingBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        GalleryActivityComponent activityComponent = (GalleryActivityComponent)
                DataBindingUtil.getDefaultComponent();
        animator = activityComponent.getToolBarFlippingAnimator();
    }

    @Override
    public boolean layoutDependsOn(
            CoordinatorLayout parent, AppBarLayout appBarLayout, View dependency) {
        return dependency instanceof FrameLayout;
    }

    @Override
    public boolean onDependentViewChanged(
            CoordinatorLayout parent, AppBarLayout child, View dependency) {
        // if the view has become GONE, we are going back to the gallery
        if (dependency.getWindowVisibility() == View.GONE && flippedToDetail) {
            // unflip
            animator.flipToGallery(child);
            flippedToDetail = false;
            return true;
        }
        // if the view is getting close
        if (!flippedToDetail
                && dependency.getWindowVisibility() == View.VISIBLE
                && dependency.getScaleY()>.97f) {
            // do animation
            animator.flipToDetail(child);
            // update flag
            flippedToDetail = true;
            return true;
        }
        return false;
    }
}
