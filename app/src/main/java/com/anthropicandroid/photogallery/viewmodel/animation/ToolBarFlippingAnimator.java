package com.anthropicandroid.photogallery.viewmodel.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Application;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.anthropicandroid.photogallery.databinding.LayoutActivityGalleryBinding;

/*
 * Created by Andrew Brin on 7/19/2016.
 */
public class ToolBarFlippingAnimator {


    public static final int FULL_DURATION = 1000;
    public static final int HALF_DURATION = FULL_DURATION/2;
    public static final String TAG = ToolBarFlippingAnimator.class.getSimpleName();
    private Application context;
    private int statusBarHeight;
    private int actionBarHeight;
    private LayoutActivityGalleryBinding galleryBinding;

    public ToolBarFlippingAnimator(
            Application context,
            int statusBarHeight,
            int actionBarHeight) {
        this.context = context;
        this.statusBarHeight = statusBarHeight;
        this.actionBarHeight = actionBarHeight;
    }

    public void flipToDetail(final LayoutActivityGalleryBinding galleryBinding) {
        this.galleryBinding = galleryBinding;
        // Get animated fields from data binding
        final String description = galleryBinding.getAlphaDetailImage().getDescription();
        RelativeLayout toolbarLayout = galleryBinding.toolbarLayout;
        final RelativeLayout toolbarFlipSide = galleryBinding.toolbarFlipSide;

        // spin and dissappear
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(getAppTitleOutSet(toolbarLayout))
                .with(getDetailTitleInSet(toolbarFlipSide));
        animatorSet.start();
    }

    private AnimatorSet getDetailTitleInSet(RelativeLayout toolbarFlipSide) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(setUpIncoming(toolbarFlipSide))
                .before(getRotateIn(toolbarFlipSide))
                .with(getAlphaIn(toolbarFlipSide));
        return animatorSet;
    }

    private AnimatorSet setUpIncoming(RelativeLayout toolbarFlipSide) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(ObjectAnimator.ofFloat(toolbarFlipSide, View.ROTATION_X, 0f, 180f))
                .with(ObjectAnimator.ofFloat(toolbarFlipSide, View.ALPHA, 1f, 0f));
        animatorSet.setDuration(2);
        return animatorSet;
    }

    private AnimatorSet getAlphaIn(final RelativeLayout toolbarFlipSide) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(ObjectAnimator.ofFloat(toolbarFlipSide, View.ALPHA, 0, 1));
        animatorSet.setStartDelay(HALF_DURATION);
        animatorSet.setDuration(2);
        return animatorSet;
    }

    private AnimatorSet getRotateIn(final RelativeLayout toolbarFlipSide) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(ObjectAnimator.ofFloat(toolbarFlipSide, View.ROTATION_X, 180f, 0f))
                .with(ObjectAnimator.ofFloat(toolbarFlipSide, View.Z, 6, 14));
        animatorSet.setDuration(FULL_DURATION);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                toolbarFlipSide.setVisibility(View.VISIBLE);
                galleryBinding.detailTitleField.setText(galleryBinding.getAlphaDetailImage()
                        .getDescription());
            }
        });
        return animatorSet;
    }

    private AnimatorSet getAppTitleOutSet(final RelativeLayout toolbarLayout) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(ObjectAnimator.ofFloat(toolbarLayout, View.X, 0, 20))
                .with(ObjectAnimator
                        .ofFloat(toolbarLayout, View.ROTATION_X, 0, -180)
                        .setDuration(FULL_DURATION))
                .with(getAlphaFadeAndGone(toolbarLayout));
        return animatorSet;
    }

    private AnimatorSet getAlphaFadeAndGone(final RelativeLayout toolbarLayout) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(ObjectAnimator
                        .ofFloat(toolbarLayout, View.ALPHA, 1f, 0f))
                .with(ObjectAnimator.ofFloat(toolbarLayout, View.Z, 8, 0));
        animatorSet.setStartDelay(HALF_DURATION - HALF_DURATION / 2);
        animatorSet.setDuration(HALF_DURATION / 2);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                toolbarLayout.setVisibility(View.GONE);
            }
        });
        return animatorSet;
    }

    public void flipToGallery(LayoutActivityGalleryBinding binding) {
        // NYI
    }

    public void pullUpNavBar(final LayoutActivityGalleryBinding binding) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(ObjectAnimator.ofFloat(
                        binding.bottomNavBar,
                        View.TRANSLATION_Y,
                        actionBarHeight,
                        0));
        animatorSet.setDuration(HALF_DURATION);
        animatorSet.setStartDelay(HALF_DURATION);
        animatorSet.setInterpolator(new LinearOutSlowInInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d(TAG, "bottom nav ani ended with top: "+binding.bottomNavBar.getY()
                +" and height: "+ binding.bottomNavBar.getHeight());
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        animatorSet.start();
    }

    public void pushDownNavBar(LayoutActivityGalleryBinding binding) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(ObjectAnimator.ofFloat(
                        binding.bottomNavBar,
                        View.TRANSLATION_Y,
                        0,
                        2*actionBarHeight));
        animatorSet.setDuration(HALF_DURATION);
        animatorSet.setStartDelay(HALF_DURATION);
        animatorSet.setInterpolator(new LinearOutSlowInInterpolator());
        animatorSet.start();
    }
}