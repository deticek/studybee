package com.example.studybee;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class MyApp extends Application {

    private int activityReferences = 0;
    public boolean isInForeground = false;

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) { }
            @Override public void onActivityStarted(Activity activity) {
                activityReferences++;
                isInForeground = true;
            }
            @Override public void onActivityResumed(Activity activity) { }
            @Override public void onActivityPaused(Activity activity) { }
            @Override public void onActivityStopped(Activity activity) {
                activityReferences--;
                if (activityReferences == 0) {
                    isInForeground = false;
                }
            }
            @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) { }
            @Override public void onActivityDestroyed(Activity activity) { }
        });
    }
}