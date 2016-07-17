package com.anthropicandroid.photogallery.viewmodel;

/*
 * Created by Andrew Brin on 7/17/2016.
 */

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class BackPressedRepo {

    public interface BackPressedHandler {
        boolean backPressedConsumed();
    }

    List<BackPressedHandler> handlers = new ArrayList<>();

    public boolean backPressedConsumed() {
        // Check handlers that have registed themselves and return false only if they are all false
        // or null
        for (BackPressedHandler handler : Lists.reverse(handlers))
            if (handler != null && handler.backPressedConsumed()) {
                handlers.remove(handler);
                return true;
            }
        return false;
    }

    public void addHandler(BackPressedHandler handler) { handlers.add(handler); }
}
