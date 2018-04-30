package com.zmj.tools;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.zmj.po.Weibo;
import com.zmj.weibomonitor.DisplayActivity;
import com.zmj.weibomonitor.R;

import java.io.UnsupportedEncodingException;

/**
 * Created by ZMJ on 2018/4/28.
 */

public class NotificationFactory {
    private static int REQUEST_CODE=1;
    private static int NOTIFICATION_ID=1;

    public static void showNotification(Context context, Weibo weibo){
        NotificationManager manager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);

        Bundle bundle=new Bundle();
        bundle.putSerializable("weibo",weibo);

        Intent intent=new Intent();
        intent.setComponent(new ComponentName(context, DisplayActivity.class));
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//一定要加这一句，否则离开应用后点击多条通知栏后者不会刷新（即不走Activity的onCreate()）

        //REQUEST_CODE一定要++，否则只有第一条能跳转到相应的activity
        PendingIntent pi = PendingIntent.getActivity(context,REQUEST_CODE++,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(weibo.getAuthorId()+"")
                .setContentText(weibo.getText())
                .setSmallIcon(R.mipmap.diao_logo)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.diao_logo))
                .setTicker(new String("启禀皇上，有新奏折"))
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pi)
                .setPriority(Notification.PRIORITY_HIGH);

        Notification notification = builder.build();

        //NOTIFICATION_ID一定要++，否则只有一条通知栏消息
        manager.notify(NOTIFICATION_ID++,notification);
    }
}
