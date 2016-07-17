package com.anthropicandroid.photogallery.injectionmodules;

/*
 * Created by Andrew Brin on 7/14/2016.
 */

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ScreenMetricsModule {

    @Provides
    @GalleryActivityScope
    @Named("ScreenNarrowest")
    int getNarrowestScreenDimenInPx(Application context) {
        Point size = new Point();
        Display defaultDisplay = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        defaultDisplay.getSize(size);
        return Math.min(size.x, size.y);
    }

    @Provides
    @GalleryActivityScope
    public DisplayMetrics getDisplayMetrics(Application context) {
        return context.getResources().getDisplayMetrics();
    }

}
