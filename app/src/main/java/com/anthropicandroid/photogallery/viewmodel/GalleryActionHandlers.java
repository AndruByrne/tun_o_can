package com.anthropicandroid.photogallery.viewmodel;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

final public class GalleryActionHandlers {

    public static final String TAG = GalleryActionHandlers.class.getSimpleName();

    public boolean gridItemTouched(View view, MotionEvent motionEvent){
        //  check for databinding
        Log.d(TAG, "got clikc on grid item");
//        Activity context = (Activity) view.getContext();
//        PhotoGalleryApplication application = (PhotoGalleryApplication) context.getApplication();
        return true;
    }

}
