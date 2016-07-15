package com.anthropicandroid.photogallery.injectionmodules;

/*
 * Created by Andrew Brin on 7/14/2016.
 */

import android.app.Application;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ScreenMetricsModule {

    @Provides
    @GalleryActivityScope
    @Named("ScreenWidth") int getScreenWidth(Application context){
        Configuration configuration = context.getResources().getConfiguration();
        return configuration.screenWidthDp;
    }

    @Provides
    @GalleryActivityScope
    public DisplayMetrics getDisplayMetrics(Application context){
        return context.getResources().getDisplayMetrics();
    }

}
