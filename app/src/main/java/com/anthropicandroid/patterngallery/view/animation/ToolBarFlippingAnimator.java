package com.anthropicandroid.patterngallery.view.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import com.anthropicandroid.patterngallery.R;
import com.anthropicandroid.patterngallery.databinding.LayoutActivityGalleryBinding;

/*
 * Created by Andrew Brin on 7/19/2016.
 */
public class ToolBarFlippingAnimator {


    public static int FULL_DURATION;
    public static int HALF_DURATION;
    public static final String TAG = ToolBarFlippingAnimator.class.getSimpleName();
    private int statusBarHeight;
    private int actionBarHeight;
    private LayoutActivityGalleryBinding galleryBinding;
    private Resources resources;

    public ToolBarFlippingAnimator(
            Resources resources,
            int statusBarHeight,
            int actionBarHeight
    ) {
        this.resources = resources;
        this.statusBarHeight = statusBarHeight;
        this.actionBarHeight = actionBarHeight;
        FULL_DURATION = resources.getInteger(R.integer.duration_app_title_rotation);
        HALF_DURATION = FULL_DURATION / 2;
    }

    public void flipToDetail(final LayoutActivityGalleryBinding galleryBinding) {
        this.galleryBinding = galleryBinding;
        // Get animated fields from data binding
        final String description = galleryBinding.getDetailName();
        RelativeLayout       toolbarLayout   = galleryBinding.toolbarLayout;
        final RelativeLayout toolbarFlipSide = galleryBinding.toolbarFlipSide;

        // spin and dissappear
        galleryBinding.appBar.setBackgroundColor(resources.getColor(
                R.color.colorDarkText));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(getAppTitleOutSet(toolbarLayout))
                .with(getDetailTitleInSet(toolbarFlipSide));
        animatorSet.setDuration(FULL_DURATION);
        animatorSet.start();
    }

    public void flipToGallery(LayoutActivityGalleryBinding binding) {
        // Just reset views to original values
        galleryBinding.appBar.setBackgroundColor(resources.getColor(android.R.color.white));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(getAppTitleInSet(binding.toolbarLayout))
                .with(getDetailTitleOutSet(binding.toolbarFlipSide));
        animatorSet.setDuration(FULL_DURATION);
        animatorSet.start();
    }

    private AnimatorSet getDetailTitleOutSet(RelativeLayout toolbarFlipSide) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator xRotation = ObjectAnimator.ofFloat(
                toolbarFlipSide,
                View.ROTATION_X,
                90f);
        xRotation.setInterpolator(new LinearInterpolator());
        ObjectAnimator yTranslation = ObjectAnimator.ofFloat(
                toolbarFlipSide,
                View.TRANSLATION_Y,
                -toolbarFlipSide.getHeight() / 2);
        yTranslation.setInterpolator(new DecelerateInterpolator());
        ObjectAnimator zTranslation = ObjectAnimator.ofFloat(
                toolbarFlipSide,
                View.TRANSLATION_Z,
                toolbarFlipSide.getHeight() / 2,
                0);
        zTranslation.setInterpolator(new AccelerateInterpolator());
        animatorSet
                .play(xRotation)
                .with(yTranslation)
                .with(zTranslation);
        return animatorSet;
    }

    private AnimatorSet getAppTitleInSet(RelativeLayout toolbarLayout) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator xRotation = ObjectAnimator.ofFloat(
                toolbarLayout,
                View.ROTATION_X,
                -90,
                0);
        xRotation.setInterpolator(new LinearInterpolator());
        ObjectAnimator yTranslation = ObjectAnimator.ofFloat(
                toolbarLayout,
                View.TRANSLATION_Y,
                toolbarLayout.getHeight() / 2,
                0);
        yTranslation.setInterpolator(new AccelerateInterpolator());
        ObjectAnimator zTranslation = ObjectAnimator.ofFloat(
                toolbarLayout,
                View.TRANSLATION_Z,
                0,
                toolbarLayout.getHeight() / 2);
        zTranslation.setInterpolator(new DecelerateInterpolator());
        animatorSet
                .play(yTranslation)
                .with(zTranslation)
                .with(xRotation);
        return animatorSet;
    }

    private AnimatorSet getDetailTitleInSet(final RelativeLayout toolbarFlipSide) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator xRotation = ObjectAnimator.ofFloat(
                toolbarFlipSide,
                View.ROTATION_X,
                90f,
                0f);
        xRotation.setInterpolator(new LinearInterpolator());
        ObjectAnimator yTranslation = ObjectAnimator.ofFloat(
                toolbarFlipSide,
                View.TRANSLATION_Y,
                -toolbarFlipSide.getHeight() / 2,
                0);
        yTranslation.setInterpolator(new AccelerateInterpolator());
        ObjectAnimator zTranslation = ObjectAnimator.ofFloat(
                toolbarFlipSide,
                View.TRANSLATION_Z,
                toolbarFlipSide.getHeight() / 2);
        zTranslation.setInterpolator(new DecelerateInterpolator());
        animatorSet
                .play(xRotation)
                .with(yTranslation)
                .with(zTranslation);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                galleryBinding.detailTitleField.setText(galleryBinding.getDetailName());
            }
        });
        return animatorSet;
    }

    private AnimatorSet getAppTitleOutSet(final RelativeLayout toolbarLayout) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator xRotation = ObjectAnimator.ofFloat(
                toolbarLayout,
                View.ROTATION_X,
                -90);
        xRotation.setInterpolator(new LinearInterpolator());
        ObjectAnimator yTranslation = ObjectAnimator.ofFloat(
                toolbarLayout,
                View.TRANSLATION_Y,
                toolbarLayout.getHeight() / 2);
        yTranslation.setInterpolator(new DecelerateInterpolator());
        ObjectAnimator zTranslation = ObjectAnimator.ofFloat(
                toolbarLayout,
                View.TRANSLATION_Z,
                toolbarLayout.getHeight() / 2,
                0);
        zTranslation.setInterpolator(new AccelerateInterpolator());
        animatorSet
                .play(yTranslation)
                .with(zTranslation)
                .with(xRotation);
        return animatorSet;
    }


    public void pullUpNavBar(final LayoutActivityGalleryBinding binding) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(ObjectAnimator.ofFloat(
                        binding.bottomNavBar,
                        View.TRANSLATION_Y,
                        2 * actionBarHeight,
                        0));
        animatorSet.setDuration(HALF_DURATION);
        animatorSet.setInterpolator(new LinearOutSlowInInterpolator());
        animatorSet.start();
    }

    public void pushDownNavBar(final LayoutActivityGalleryBinding binding) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(ObjectAnimator.ofFloat(
                        binding.bottomNavBar,
                        View.TRANSLATION_Y,
                        0,
                        2 * actionBarHeight));
        animatorSet.setDuration(HALF_DURATION);
        animatorSet.setStartDelay(HALF_DURATION);
        animatorSet.setInterpolator(new LinearOutSlowInInterpolator());
        animatorSet.start();
    }
}