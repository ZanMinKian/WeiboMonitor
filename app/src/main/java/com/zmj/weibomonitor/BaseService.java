package com.zmj.weibomonitor;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zmj.tools.Utils;

/**
 * Created by ZMJ on 2018/4/27.
 */

public abstract class BaseService extends Service {
    protected abstract String getServiceClassName();//自己的类名
    protected abstract String getAnotherProcessName();//另一条进程的进程名
    protected abstract Thread getTaskThread();

    private int onStartCommandCount=0;

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        Log.d(getServiceClassName(),"onStartCommand running!"+onStartCommandCount);

        //每个Service有两个线程，一个是TaskThread，一个是WakeUpThread
        if(0==onStartCommandCount++) {
            getTaskThread().start();
            getWakeUpThread(2000).start();//监听KeepAlive是否死掉的线程
        }

        return START_STICKY;
    }

    /*唤醒另一条进程*/
    private void sendBroadcastToWakeUp(){
        Bundle bundle=new Bundle();
        bundle.putString(WakeBroadcastReceiver.SENDER,getServiceClassName());

        Intent intent=new Intent();
        intent.putExtras(bundle);
        intent.setComponent(new ComponentName(this,WakeBroadcastReceiver.class));
        sendBroadcast(intent);
    }
    /*新建一个线程监听KeepAliveService是否活着*/
    private Thread getWakeUpThread(final int sleepTime){
        Thread wakeUpThread=new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if(!Utils.isProessRunning(BaseService.this,getAnotherProcessName())){//若另一条进程没有在运行，则唤醒他
                        sendBroadcastToWakeUp();
                    }
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return wakeUpThread;
    }

    //当系统杀进程时就通知另一条进程，保持进程活性，避免被杀
    @Override
    public void onTrimMemory(int level){
        Log.d(getServiceClassName(),"onTrimMemory"+level);
        sendBroadcastToWakeUp();
    }


    @Override
    public void onCreate(){
        Log.d(getServiceClassName(),"onCreate running!");
    }
    @Override
    public void onDestroy(){
        Log.w(getServiceClassName(),"onDestroy running!");
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
