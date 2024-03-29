package com.alieeen.snitch.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by alinekborges on 16/04/15.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_EVENTS = "Events";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CAMERANAME = "CameraName";
    public static final String COLUMN_DATETIME = "DateTime";
    public static final String COLUMN_NUMBER = "ImageURL";
    public static final String COLUMN_IMAGENAME = "ImageName";
    public static final String COLUMN_VIEWED = "Viewed";

    private static final String DATABASE_NAME = "snitch.db";
    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table " +
            TABLE_EVENTS + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_CAMERANAME + " text not null, " +
            COLUMN_DATETIME + " text not null, " +
            COLUMN_NUMBER + " text not null, " +
            COLUMN_IMAGENAME + " text not null, " +
            COLUMN_VIEWED + " integer not null" +
            ");";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }
}


