package com.zmj.tools;

import android.content.Context;

import com.zmj.sqlite.DBHelper;
import com.zmj.sqlite.po.WeiboContent;
import com.zmj.sqlite.po.WeiboId;

import java.util.List;

/**
 * Created by ZMJ on 2018/4/23.
 */

/*本类包含一些持续化的操作，主要涉及本地存储*/
public class ToolsFactory {

    public static List<WeiboId> getWeiboIds(Context context){
        return DBHelper.getDBHelper(context).getWeiboIds();
    }
    public static void deleteWeiboId(String weiboId,Context context){
        DBHelper.getDBHelper(context).deleteWeiboId(weiboId);
    }
    public static void addWeiboId(String weiboId,Context context){
        DBHelper.getDBHelper(context).addWeiboId(weiboId);
    }

    public static List<WeiboContent> getWeiboContents(String weiboId,Context context){
        return DBHelper.getDBHelper(context).getWeiboContents(weiboId);
    }
    public static void addWeiboContent(String weiboId,String weiboText,Context context){
        DBHelper.getDBHelper(context).addWeiboContent(weiboId,weiboText);
    }
}
