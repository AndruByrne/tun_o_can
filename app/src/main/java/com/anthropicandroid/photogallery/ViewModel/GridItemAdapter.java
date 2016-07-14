package com.anthropicandroid.photogallery.ViewModel;

/*
 * Created by Andrew Brin on 7/13/2016.
 */

import android.databinding.BindingAdapter;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.anthropicandroid.photogallery.InjectionModules.GalleryActivityComponent;

public class GridItemAdapter {

    public static final String TAG = GridItemAdapter.class.getSimpleName();

    @BindingAdapter("imageIndex")
    public static void setImageIndex(
            GalleryActivityComponent galleryActivityComponent,
            ImageView imageView,
            Integer imageIndex) {
        // check if we need rxJava Answer: this returns true
        Log.d(TAG, "we are on the main thread: "+(Looper.myLooper() == Looper.getMainLooper()));
    }
}
