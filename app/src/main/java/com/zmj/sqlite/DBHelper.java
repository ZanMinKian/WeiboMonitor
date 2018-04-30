package com.zmj.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zmj.sqlite.po.WeiboContent;
import com.zmj.sqlite.po.WeiboId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZMJ on 2018/4/21.
 */

/*笔记：一个SQLiteOpenHelper对象对应一个打开的SQLiteDatabase
* SQLiteOpenHelper顾名思义，是打开数据库的，*/
public class DBHelper extends SQLiteOpenHelper {
    private static final String CREATE_WEIBO_CONTENTS_TABLE_SQL="create table weiboContents("+
            "weiboId text not null,"+//微博博主的id
            "weiboText text not null,"+//微博博主发的一条微博
            "primary key (weiboId,weiboText))";
    private static final String CREATE_WEIBO_IDS_TABLE_SQL="create table weiboIds(weiboId text primary key)";

    private boolean isClosed=false;
    public boolean isClosed(){
        return this.isClosed;
    }
    @Override
    public void close(){
        super.close();//查看android源码，可以知道super.close()本质就是getReadableDatabase().close()
        this.isClosed=true;
        instance=null;
    }

    //--------------------------------------------------
    private static DBHelper instance=null;//单例
    private DBHelper(Context context) {
        //查看android源码注释，可以知道SQLiteDatabase是在getReadableDatabase()和getWriteableDatabase()中打开，而不是在创建SQLiteDatabase中打开
        super(context, "weibo.db", null, 1);
    }
    public static DBHelper getDBHelper(Context context){
        if(instance==null||instance.isClosed()){
            instance=new DBHelper(context);
        }
        return instance;
    }
    //--------------------------------------------------

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WEIBO_CONTENTS_TABLE_SQL);
        db.execSQL(CREATE_WEIBO_IDS_TABLE_SQL);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}


    public void addWeiboContent(String weiboId,String weiboText){
        String sql="insert into weiboContents (weiboId,weiboText) values (?,?)";
        String[] bindArgs={weiboId,weiboText};
        this.getReadableDatabase() .execSQL(sql,bindArgs);//每调用一次getReadableDatabase()就可以把它关掉，但是比较耗性能
    }
    public List<WeiboContent> getWeiboContents(String weiboId){
        List<WeiboContent> weiboContents=new ArrayList<WeiboContent>();

        String sql="select * from weiboContents where weiboId=?";
        String[] selectionArgs={weiboId};
        Cursor cursor=this.getReadableDatabase().rawQuery(sql,selectionArgs);//每调用一次getReadableDatabase()就可以把它关掉，但是比较耗性能
        if(cursor.moveToFirst()){
            do{
                WeiboContent weiboContent=new WeiboContent();
                weiboContent.setWeiboId(cursor.getString(0));
                weiboContent.setWeiboText(cursor.getString(1));
                weiboContents.add(weiboContent);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return weiboContents;
    }

    public void addWeiboId(String weiboId){
        String sql="insert into weiboIds (weiboId) values (?)";
        String[] bindArgs={weiboId};
        this.getReadableDatabase() .execSQL(sql,bindArgs);
    }
    public List<WeiboId> getWeiboIds(){
        List<WeiboId> weiboIds=new ArrayList<WeiboId>();

        String sql="select * from weiboIds";
        Cursor cursor=this.getReadableDatabase().rawQuery(sql,null);
        if(cursor.moveToFirst()){
            do{
                WeiboId weiboId=new WeiboId();
                weiboId.setWeiboId(cursor.getString(0));
                weiboIds.add(weiboId);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return weiboIds;
    }

    public void deleteWeiboId(String weiboId){
        String sql="delete from weiboIds where weiboId=?";
        String[] bindArgs={weiboId};
        this.getReadableDatabase().execSQL(sql,bindArgs);
    }
}
