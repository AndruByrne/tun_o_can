package com.anthropicandroid.photogallery.viewmodel.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Application;
import android.databinding.DataBindingUtil;
import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.widget.RelativeLayout;

import com.anthropicandroid.photogallery.databinding.LayoutActivityGalleryBinding;

/*
 * Created by Andrew Brin on 7/19/2016.
 */
public class ToolBarFlippingAnimator {


    public static final String TAG = ToolBarFlippingAnimator.class.getSimpleName();
    public static final int HALF_DURATION = 1000;
    public static final int FULL_DURATION = 2000;
    private Application context;

    public ToolBarFlippingAnimator(Application context) { this.context = context;}

    public void flipToDetail(final AppBarLayout appBarLayout) {
        // Get animated fields from data binding
        LayoutActivityGalleryBinding galleryBinding = DataBindingUtil.findBinding(appBarLayout);
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

    public void flipToGallery(AppBarLayout appBarLayout) {
        return;
    }
}