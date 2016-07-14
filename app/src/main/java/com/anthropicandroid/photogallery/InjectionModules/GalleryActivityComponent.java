package com.anthropicandroid.photogallery.InjectionModules;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import com.anthropicandroid.photogallery.ViewModel.GalleryActivity;
import com.anthropicandroid.photogallery.ViewModel.UserActionHandlers;

import dagger.Component;

@GalleryActivityScope
@Component(
        dependencies = ApplicationComponent.class
)
public interface GalleryActivityComponent extends android.databinding.DataBindingComponent {

    UserActionHandlers getUserActionHandlers();

    void inject(GalleryActivity galleryActivity);
}
