package com.anthropicandroid.photogallery.ViewModel;

import android.app.Activity;
import android.os.Bundle;

import com.anthropicandroid.photogallery.R;

public class GalleryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
    }
}
