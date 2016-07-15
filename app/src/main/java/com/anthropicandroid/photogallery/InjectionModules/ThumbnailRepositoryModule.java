package com.anthropicandroid.photogallery.injectionmodules;

/*
 * Created by Andrew Brin on 7/14/2016.
 */

import android.util.DisplayMetrics;

import com.anthropicandroid.photogallery.model.ThumbnailRepository;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import rx.Observable;

@Module
public class ThumbnailRepositoryModule {

    @Provides
    @GalleryActivityScope
    ThumbnailRepository getThumbnailRepository(
            Observable<Realm> realmObservable,
            @Named("ScreenWidth") int getScreenWidth,
            DisplayMetrics metrics) {
        return new ThumbnailRepository(realmObservable, getScreenWidth, metrics);
    }
}
