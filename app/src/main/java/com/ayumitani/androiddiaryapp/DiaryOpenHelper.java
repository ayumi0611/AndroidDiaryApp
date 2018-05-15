package com.ayumitani.androiddiaryapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DiaryOpenHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "myapp.db";
    public static final int DB_VERSION = 1;

    public static final String CREATE_TABLE =
            "create table diaries (" +
                    "_id integer primary key autoincrement, " +
                    "title text, " +
                    "body text, " +
                    "date datetime default current_timestamp)";
    public static final String INIT_TABLE =
            "insert into diaries (title, body) values " +
                    "('t1', 'b1')," +
                    "('t2', 'b2')," +
                    "('t3', 'b3')";
    public static final String DROP_TABLE =
            "drop table if exists " + DiaryContract.Diary.TABLE_NAME;

    public DiaryOpenHelper(Context c){
        super(c, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(INIT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
