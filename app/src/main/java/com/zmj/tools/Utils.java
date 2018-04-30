package com.zmj.tools;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by ZMJ on 2018/4/25.
 */

public class Utils {
    public static boolean isProessRunning(Context context, String proessName) {

        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : lists) {
            if (info.processName.equals(proessName)) {
                isRunning = true;
                break;
            }
        }

        return isRunning;
    }
}
