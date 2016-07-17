package com.anthropicandroid.photogallery.injectionmodulz;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import android.util.DisplayMetrics;

import com.anthropicandroid.photogallery.model.Repository;
import com.anthropicandroid.photogallery.viewmodelz.GalleryActivity;
import com.anthropicandroid.photogallery.viewmodelz.UserActionHandlers;

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
