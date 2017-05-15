package com.anthropicandroid.patterngallery.view;

/*
 * Created by Andrew Brin on 7/14/2016.
 */

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.anthropicandroid.patterngallery.routers.gallery.GalleryActivityScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ScreenMetricsModule {

    public static final String TAG = ScreenMetricsModule.class.getSimpleName();

    @Provides
    @GalleryActivityScope
    @Named("ScreenNarrowest")
    int getNarrowestScreenDimenInPx(@Named("ScreenSize") Point size) {
        return Math.min(size.x, size.y);
    }

    @Provides
    @GalleryActivityScope
    @Named("ScreenWidth")
    int getScreenWidthInPx(@Named("ScreenSize") Point size) {
        return size.x;
    }

    @Provides
    @GalleryActivityScope
    @Named("DetailHeight")
    int getDetailHeightInPx(
            @Named("ScreenSize") Point size,
            @Named("ActionBarHeight") int actionBarHeight) {
        return size.y - actionBarHeight * 2;
    }

    @Provides
    @GalleryActivityScope
    public DisplayMetrics getDisplayMetrics(Application context) {
        return context.getResources().getDisplayMetrics();
    }

    @Provides
    @GalleryActivityScope
    @Named("ScreenSize")
    Point getSize(Application context) {
        Point size = new Point();
        Display defaultDisplay = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        defaultDisplay.getSize(size);
        return size;
    }

    @Provides
    @GalleryActivityScope
    @Named("ActionBarHeight")
    int getActionBarHeight(Application context) {
        TypedValue value = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, value, true))
            return TypedValue.complexToDimensionPixelSize(
                    value.data,
                    context.getResources().getDisplayMetrics());
        Log.e(TAG, "trouble getting action bar height attribute");
        return 0;
    }

    @Provides
    @GalleryActivityScope
    @Named("StatusBarHeight")
    int getStatusBarHeight(Application context) {
        int resId = context.getResources().getIdentifier(
                "status_bar_height",
                "dimen",
                "android");
        if (resId == 0) {
            Log.e(TAG, "resId of status bar height is 0");
            return 0;
        } else return context.getResources().getDimensionPixelSize(resId);
    }
}
