package com.anthropicandroid.photogallery.viewmodel.animation;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.anthropicandroid.photogallery.databinding.LayoutActivityGalleryBinding;
import com.anthropicandroid.photogallery.injectionmodules.GalleryActivityComponent;

/*
 * Created by Andrew Brin on 7/19/2016.
 */

public class ToolbarFlippingBehavior extends CoordinatorLayout.Behavior<LinearLayout> {

    public static final String TAG = ToolbarFlippingBehavior.class.getSimpleName();
    private boolean flippedToDetail;
    private final ToolBarFlippingAnimator animator;

    public ToolbarFlippingBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        GalleryActivityComponent activityComponent = (GalleryActivityComponent)
                DataBindingUtil.getDefaultComponent();
        animator = activityComponent.getToolBarFlippingAnimator();
    }

    @Override
    public boolean layoutDependsOn(
            CoordinatorLayout parent, LinearLayout layout, View dependency) {
        return dependency instanceof FrameLayout;
    }

    @Override
    public boolean onDependentViewChanged(
            CoordinatorLayout parent, LinearLayout layout, View dependency) {
        // if the view has become GONE, we are going back to the gallery
        if (dependency.getZ() < 7 && flippedToDetail) {
            // unflip
            LayoutActivityGalleryBinding binding = DataBindingUtil.findBinding(layout);
            animator.flipToGallery(binding);
            animator.pushDownNavBar(binding);
            flippedToDetail = false;
            return true;
        }
        // if the view is getting close
        if (!flippedToDetail && dependency.getZ()>7) {
            Log.d(TAG, "got one call hwere");
            // do flip animation on toolbar
            LayoutActivityGalleryBinding galleryBinding = DataBindingUtil.findBinding(layout);
            animator.flipToDetail(galleryBinding);
            animator.pullUpNavBar(galleryBinding);
            // update flag
            flippedToDetail = true;
            return true;
        }
        return false;
    }
}
