package com.example.cieo233.unittest;

import android.app.Application;

import com.aitangba.swipeback.ActivityLifecycleHelper;

/**
 * Created by Cieo233 on 1/13/2017.
 */

public class SwipeBackApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(ActivityLifecycleHelper.build());
    }
}
