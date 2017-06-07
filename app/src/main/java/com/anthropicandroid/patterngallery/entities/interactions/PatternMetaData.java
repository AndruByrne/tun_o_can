package com.anthropicandroid.patterngallery.entities.interactions;

/*
 * Created by Andrew Brin on 6/04/2017.
 */

public abstract class PatternMetaData {

    public abstract String getName();

    public abstract Integer getWidth();

    public abstract Integer getHeight();

    public abstract String getOriginalUri();

    public abstract Boolean getWellBehaved();

    public abstract Integer getNumPermutations();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PatternMetaData that = (PatternMetaData) o;

        if (!getName().equals(that.getName())) return false;
        if (!getWidth().equals(that.getWidth())) return false;
        if (!getHeight().equals(that.getHeight())) return false;
        if (!getOriginalUri().equals(that.getOriginalUri())) return false;
        if (!getWellBehaved().equals(that.getWellBehaved())) return false;
        return getNumPermutations().equals(that.getNumPermutations());

    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getWidth().hashCode();
        result = 31 * result + getHeight().hashCode();
        result = 31 * result + getOriginalUri().hashCode();
        result = 31 * result + getWellBehaved().hashCode();
        result = 31 * result + getNumPermutations().hashCode();
        return result;
    }

}
