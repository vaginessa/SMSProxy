package com.github.willena.smsproxy.Utils;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by Guillaume on 09/07/2017.
 */

public class ServicesUtils {
    private static boolean isServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
