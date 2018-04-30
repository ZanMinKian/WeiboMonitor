package com.zmj.weibomonitor;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zmj.tools.Utils;

/**
 * Created by ZMJ on 2018/4/24.
 */
/**本Service有两个线程
 * 一个是WakeUpThread，从父类继承得到，用于监视另一个Service并随时唤醒它
 * 一个是TaskThread，工作就是每隔10秒Log一次（没什么卵用）
 */
public class KeepAliveService extends BaseService {
    public static final String PROCESS_NAME="com.zmj.weibomonitor:KeepAliveService";
    public static final String CLASS_NAME="KeepAliveService";

    @Override
    protected String getServiceClassName() {
        return CLASS_NAME;
    }
    @Override
    protected String getAnotherProcessName() {
        return ConnectService.PROCESS_NAME;
    }

    @Override
    protected Thread getTaskThread() {
        return new Thread(new Runnable() {
            private int i=0;
            @Override
            public void run() {

                while(true) {
                    Log.d(CLASS_NAME,i+++"");
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    public static void startService(Context context){
        Intent intent=new Intent();
        intent.setComponent(new ComponentName(context,KeepAliveService.class));
        context.startService(intent);
    }
}
