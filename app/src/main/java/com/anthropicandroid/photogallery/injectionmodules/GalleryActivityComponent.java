package com.anthropicandroid.photogallery.injectionmodules;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import android.util.DisplayMetrics;

import com.anthropicandroid.photogallery.model.Repository;
import com.anthropicandroid.photogallery.viewmodel.BackPressedRepo;
import com.anthropicandroid.photogallery.viewmodel.GalleryActionHandlers;
import com.anthropicandroid.photogallery.viewmodel.GalleryActivity;

import javax.inject.Named;

import dagger.Component;

@GalleryActivityScope
@Component(
        dependencies = {ApplicationComponent.class},
        modules = {
                OnBackPressedModule.class,
                ScreenMetricsModule.class}
)
public interface GalleryActivityComponent extends android.databinding.DataBindingComponent {

    void inject(GalleryActivity galleryActivity);

    BackPressedRepo getBackPressedRepo();

    DisplayMetrics getDisplayMetrics();

    Repository getRepository();

    @Named("ScreenNarrowest") int getNarrowestScreenDimenInPx();

    GalleryActionHandlers getGalleryActionHandlers();

}
