package com.zmj.weibomonitor;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;

import com.zmj.po.Weibo;
import com.zmj.sqlite.po.WeiboId;
import com.zmj.tools.NotificationFactory;
import com.zmj.tools.ToolsFactory;
import com.zmj.weibomonitor.analyze.WeiboFetcher;


import org.json.JSONException;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;


/**
 * Created by ZMJ on 2018/4/21.
 */
/**本Service有两个线程
 * 一个是WakeUpThread，从父类继承得到，用于监视另一个Service并随时唤醒它
 * 一个是TaskThread，工作就是监听微博ID，并发邮件通知用户
 */
public class ConnectService extends BaseService{
    public static final String PROCESS_NAME="com.zmj.weibomonitor:ConnectService";
    public static final String CLASS_NAME="ConnectService";
    @Override
    protected String getServiceClassName() {
        return CLASS_NAME;
    }
    @Override
    protected String getAnotherProcessName() {
        return KeepAliveService.PROCESS_NAME;
    }

    @Override
    protected Thread getTaskThread() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                List<WeiboId> weiboIds=ToolsFactory.getWeiboIds(ConnectService.this);

                while(true) {
                    Calendar now=Calendar.getInstance();
                    int minute=now.get(Calendar.MINUTE);

                    if(minute%30>=0&&minute%30<=3){
                    //if(true){
                        for(WeiboId weiboId:weiboIds){
                            try {
                                List<Weibo> weibos=WeiboFetcher.fetch(weiboId.getWeiboId());//可能抛出IOException和JSONException

                                for(Weibo weibo:weibos){
                                    try {
                                        ToolsFactory.addWeiboContent(weiboId.getWeiboId(), weibo.getText(), ConnectService.this);
                                        NotificationFactory.showNotification(ConnectService.this,weibo);//如果没有发生异常，则通知栏通知用户
                                        Thread.sleep(2000);
                                    }catch (SQLException | InterruptedException e){//如果发生插入异常，说明原本app中有
                                        e.printStackTrace();
                                    }
                                }
                            } catch (IOException|JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    try {
                        Thread.sleep(60*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void startService(Context context){
        Intent intent=new Intent();
        intent.setComponent(new ComponentName(context,ConnectService.class));
        context.startService(intent);
    }
}
