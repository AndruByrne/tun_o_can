package com.anthropicandroid.patterngallery.routers.gallery;

/*
 * Created by Andrew Brin on 7/17/2016.
 */


import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class BackPressedRepo {

    public interface BackPressedHandler {
        boolean backPressedConsumed();
    }

    List<BackPressedHandler> handlers = new ArrayList<>();

    public boolean backPressedConsumed() {
        // Check handlers that have registed themselves and return false only if they are all false
        // or null
        ListIterator<BackPressedHandler> iterator = handlers.listIterator(handlers.size());
        while (iterator.hasPrevious()) {
            BackPressedHandler handler = iterator.previous();
            if (
                    handler != null &&
                            handler.backPressedConsumed()
                    ) {
                handlers.remove(handler);
                return true;
            }
        }
        return false;
    }

    public void addHandler(BackPressedHandler handler) { handlers.add(handler); }

    public void releaseHandler(BackPressedHandler handler) { handlers.remove(handler); }
}
