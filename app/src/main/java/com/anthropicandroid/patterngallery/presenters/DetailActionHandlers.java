package com.anthropicandroid.patterngallery.presenters;

import android.app.Application;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.anthropicandroid.patterngallery.databinding.LayoutActivityGalleryBinding;
import com.anthropicandroid.patterngallery.routers.gallery.BackPressedRepo;
import com.anthropicandroid.patterngallery.view.animation.DetailToGalleryAnimator;
import com.anthropicandroid.patterngallery.view.animation.GalleryToDetailAnimator;

/*
 * Created by Andrew Brin on 7/17/2016.
 */
public class DetailActionHandlers {
    private final Application context;
    private BackPressedRepo backPressedRepo;
    private GalleryToDetailAnimator galleryToDetailAnimator;
    private final DetailToGalleryAnimator detailToGalleryAnimator;

    public DetailActionHandlers(
            Application context,
            BackPressedRepo backPressedRepo,
            GalleryToDetailAnimator galleryToDetailAnimator,
            DetailToGalleryAnimator detailToGalleryAnimator
    ) {
        this.context = context;
        this.backPressedRepo = backPressedRepo;
        this.galleryToDetailAnimator = galleryToDetailAnimator;
        this.detailToGalleryAnimator = detailToGalleryAnimator;
    }

    public void returnToGallery(
            View view
    ) {
        // give animator a hook to the UI
        detailToGalleryAnimator.returnToGallery((LayoutActivityGalleryBinding)DataBindingUtil.findBinding(view));
        // release the g3d anim
        backPressedRepo.releaseHandler(galleryToDetailAnimator);
    }

}
