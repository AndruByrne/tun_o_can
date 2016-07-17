package com.anthropicandroid.photogallery.InjectionModules;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import android.util.DisplayMetrics;

import com.anthropicandroid.photogallery.ViewModel.GalleryActivity;
import com.anthropicandroid.photogallery.ViewModel.UserActionHandlers;
import com.anthropicandroid.photogallery.model.Repository;

import javax.inject.Named;

import dagger.Component;

@GalleryActivityScope
@Component(
        dependencies = {ApplicationComponent.class},
        modules = {
                ScreenMetricsModule.class}
)
public interface GalleryActivityComponent extends android.databinding.DataBindingComponent {

    void inject(GalleryActivity galleryActivity);

    DisplayMetrics getDisplayMetrics();

    Repository getRepository();

    @Named("ScreenNarrowest") int getNarrowestScreenDimenInPx();

    UserActionHandlers getUserActionHandlers();

}
