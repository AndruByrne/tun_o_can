package com.anthropicandroid.patterngallery.injectionmodules;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import android.util.DisplayMetrics;

import com.anthropicandroid.patterngallery.model.Repository;
import com.anthropicandroid.patterngallery.viewmodel.BackPressedRepo;
import com.anthropicandroid.patterngallery.viewmodel.BottomNavActionHandlers;
import com.anthropicandroid.patterngallery.viewmodel.DetailActionHandlers;
import com.anthropicandroid.patterngallery.viewmodel.GalleryActionHandlers;
import com.anthropicandroid.patterngallery.viewmodel.GalleryActivity;
import com.anthropicandroid.patterngallery.viewmodel.animation.GalleryToDetailAnimator;
import com.anthropicandroid.patterngallery.viewmodel.animation.ToolBarFlippingAnimator;

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
