package com.anthropicandroid.photogallery.injectionmodulz;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface GalleryActivityScope {
}