package com.qin.aidl_keeplive.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Intent;
import android.os.Process;
import android.util.Log;

import java.util.List;

/**
 * Create by qindl
 * on 2018/12/17
 */
public class APP extends Application {
    private static final String TAG = "TAG";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: " + getPeocessName());
    }

    public String getPeocessName() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo info : runningAppProcesses) {
            if (info.uid == Process.myUid()) {
                return info.processName;
            }
        }
        return null;
    }
}
