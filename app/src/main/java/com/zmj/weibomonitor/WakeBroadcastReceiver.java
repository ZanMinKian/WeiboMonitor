package com.zmj.weibomonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by ZMJ on 2018/4/25.
 */

public class WakeBroadcastReceiver extends BroadcastReceiver {
    public static final String SENDER="sender";
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle=intent.getExtras();
        String sender=bundle.getString(SENDER);
        Log.d("WakeBroadcastReceiver","Sender:"+sender);

        if(ConnectService.CLASS_NAME.equals(sender)){
            KeepAliveService.startService(context);
            Log.d("WakeBroadcastReceiver","Wake up KeepAliveService");
        }else{
            ConnectService.startService(context);
            Log.d("WakeBroadcastReceiver","Wake up ConnectService");
        }

    }
}
