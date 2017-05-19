package com.anthropicandroid.patterngallery.routers.gallery;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import android.util.DisplayMetrics;

import com.anthropicandroid.patterngallery.routers.ApplicationComponent;
import com.anthropicandroid.patterngallery.view.animation.AnimatorModule;
import com.anthropicandroid.patterngallery.view.ScreenMetricsModule;
import com.anthropicandroid.patterngallery.interactors.Repository;
import com.anthropicandroid.patterngallery.presenters.ActionHandlersModule;
import com.anthropicandroid.patterngallery.presenters.BottomNavActionHandlers;
import com.anthropicandroid.patterngallery.presenters.DetailActionHandlers;
import com.anthropicandroid.patterngallery.presenters.GalleryActionHandlers;
import com.anthropicandroid.patterngallery.view.animation.GalleryToDetailAnimator;
import com.anthropicandroid.patterngallery.view.animation.ToolBarFlippingAnimator;

import javax.inject.Named;

import dagger.Component;

@GalleryActivityScope
@Component(
        dependencies = {ApplicationComponent.class},
        modules = {
                AnimatorModule.class,
                ActionHandlersModule.class,
                OnBackPressedModule.class,
                ScreenMetricsModule.class}
)
public interface GalleryActivityComponent
        extends android.databinding.DataBindingComponent {

    void inject(GalleryActivity galleryActivity);

    BackPressedRepo getBackPressedRepo();

    BottomNavActionHandlers getBottomNavActionHandlers();

    DetailActionHandlers getDetailActionHandlers();

    DisplayMetrics getDisplayMetrics();

    GalleryActionHandlers getGalleryActionHandlers();

    GalleryToDetailAnimator getGalleryToDetailAnimator();

    Repository getRepository();

    @Named("ScreenNarrowest")
    int getNarrowestScreenDimenInPx();

    @Named("ScreenWidth")
    int getScreenWidthInPx();

    @Named("DetailHeight")
    int getDetailHeightInPx();

    @Named("StatusBarHeight")
    int getStatusBarHeight();

    ToolBarFlippingAnimator getToolBarFlippingAnimator();
}
