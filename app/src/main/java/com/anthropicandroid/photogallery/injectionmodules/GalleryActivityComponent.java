package com.anthropicandroid.photogallery.injectionmodules;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import android.util.DisplayMetrics;

import com.anthropicandroid.photogallery.model.Repository;
import com.anthropicandroid.photogallery.viewmodel.BackPressedRepo;
import com.anthropicandroid.photogallery.viewmodel.BottomNavActionHandlers;
import com.anthropicandroid.photogallery.viewmodel.DetailActionHandlers;
import com.anthropicandroid.photogallery.viewmodel.GalleryActionHandlers;
import com.anthropicandroid.photogallery.viewmodel.GalleryActivity;
import com.anthropicandroid.photogallery.viewmodel.animation.GalleryToDetailAnimator;
import com.anthropicandroid.photogallery.viewmodel.animation.ToolBarFlippingAnimator;

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
public interface GalleryActivityComponent extends android.databinding.DataBindingComponent {

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
