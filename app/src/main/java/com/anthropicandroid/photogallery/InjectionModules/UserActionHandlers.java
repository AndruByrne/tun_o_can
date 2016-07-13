package com.anthropicandroid.photogallery.InjectionModules;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import android.app.Activity;
import android.view.View;

import com.anthropicandroid.photogallery.PhotoGalleryApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

final public class UserActionHandlers {

    public static final String TAG = UserActionHandlers.class.getSimpleName();

    public void aClick(View view){
        Activity context = (Activity) view.getContext();
        PhotoGalleryApplication application = (PhotoGalleryApplication) context.getApplication();
    }

}
