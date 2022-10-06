package com.chh.harusalee02;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper
{

    public MySQLiteOpenHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
    // TODO Auto-generated method stub
    // SQLiteOpenHelper 가 최초 실행 되었을 때
        String sql = "create table member (" +
                "idx integer primary key autoincrement, " +
                "id text, " +
                "pw text, " +
                "name int);";
        db.execSQL(sql);

        String sql2 = "create table member2 (" +
                "idx integer primary key autoincrement, " +
                "dbjasan int);";
        db.execSQL(sql2);

        String sql3 = "create table member3 (" +
                "idx integer primary key autoincrement, " +
                "dbday text, "+
                "jicull text);";
        db.execSQL(sql3);

//        String sql4 = "create table member4 (" +
//                "idx integer primary key autoincrement, " +
//                "jicull_money int);";
//        db.execSQL(sql4);


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists member";
        String sql2 = "drop table if exists member2";
        String sql3 = "drop table if exists member3";
//        String sql4 = "drop table if exists member4";
        db.execSQL(sql);
        db.execSQL(sql2);
        db.execSQL(sql3);
//        db.execSQL(sql4);
        onCreate(db); // 테이블을 지웠으므로 다시 테이블을 만들어주는 과정
    }
}
