package com.anthropicandroid.photogallery.InjectionModules;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import com.anthropicandroid.photogallery.ViewModel.GalleryActivity;

import dagger.Component;

@GalleryScope
@Component(
        dependencies = ApplicationComponent.class
)
public interface ActivityComponent {

    UserActionHandlers getUserActionHandlers();

    void inject(GalleryActivity galleryActivity);
}
