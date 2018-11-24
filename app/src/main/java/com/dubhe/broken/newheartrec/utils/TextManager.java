package com.dubhe.broken.newheartrec.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dubhe.broken.newheartrec.entity.TextEntity;

import java.util.ArrayList;
import java.util.List;

public class TextManager {

    public static int operation=0;

    public static void addOperation() {
        operation++;
    }

    public static void removeOperation() {
        operation--;
    }

    public static List<TextEntity> getTexts(Context context) {
        List<TextEntity> listText = new ArrayList<>();

//                查询数据
        SqliteHelper sqliteHelper = DbManager.getIntance(context);
        while (operation > 0) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Log.e("---getTexts---", e.getMessage());
            }
        }
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        Cursor cursor = null;
        String sql = "select * from " + Constant.TABLE_NAME + " order by " + Constant.ID + " desc;";
        cursor = DbManager.selectDataBySql(db, sql, null);
        if (cursor != null && db.isOpen()) {
            while (cursor.moveToNext()) {
                TextEntity text_entity = new TextEntity();
                text_entity.setId(cursor.getString(cursor.getColumnIndex("id")));
                text_entity.setTime(cursor.getString(cursor.getColumnIndex("time")));
                text_entity.setSubstance(cursor.getString(cursor.getColumnIndex("substance")));
                listText.add(text_entity);
            }
        }
        removeOperation();
        while (operation > 0) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Log.e("---getTexts---", e.getMessage());
            }
        }
        //关闭数据库
        db.close();
        return listText;
    }

    public static void delete(Context context, String idorfilename) {
        SqliteHelper sqliteHelper = DbManager.getIntance(context);
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();//打开数据库
        try {
            String sql = "delete from " + Constant.TABLE_NAME +
                    " where " + Constant.ID + "=" + idorfilename + ";";
            DbManager.execSQL(db, sql);
        } catch (Exception e) {
            Log.e("execSQL", "删除数据出错", e);
        } finally {
            removeOperation();
            while (operation > 0) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Log.e("---getTexts---", e.getMessage());
                }
            }
            //关闭数据库
            db.close();
        }
    }
}
