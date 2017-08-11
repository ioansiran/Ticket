package com.akitektuo.ticket.app;

import android.app.Application;

/**
 * Created by sirani on 8/11/2017.
 */

public class MyApplication extends Application {
    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible;
}
