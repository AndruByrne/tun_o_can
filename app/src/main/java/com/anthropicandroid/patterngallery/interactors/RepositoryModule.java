package com.anthropicandroid.patterngallery.interactors;

/*
 * Created by Andrew Brin on 7/14/2016.
 */

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    PatternRepository getRepository(
            Application context
    ) {
        return new PatternRepository(context.getSharedPreferences("pattern_repository", Context.MODE_PRIVATE));
    }

}
