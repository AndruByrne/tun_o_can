package com.anthropicandroid.photogallery.model;

/*
 * Created by Andrew Brin on 7/14/2016.
 */

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Thumbnail extends RealmObject {

    @PrimaryKey
    private int index;

    @Required
    private byte[] thumbnail;

    public int getIndex() { return index; }

    public void setIndex(int index) { this.index = index; }

    public byte[] getThumbnail() { return thumbnail; }

    public void setThumbnail(byte[] thumbnail) { this.thumbnail = thumbnail; }

}
