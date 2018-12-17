package com.qin.aidl_keeplive.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Create by qindl
 * on 2018/12/17
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_MUSIC = "create table Music(" +
            "id integer primary key autoincrement," +
            "musicId," +
            "musicName text," +
            "musicAuthor text" +
            ")";

    public DataBaseHelper(Context context) {
        super(context, "Music.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MUSIC);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
