package com.example.untitled.services;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.untitled.model.MessageThread;
import com.example.untitled.model.MessageType;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: brian
 * Date: 8/20/13
 * Time: 8:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class MySQLiteHelper extends SQLiteOpenHelper{
    public static final String TABLE_NAME="message";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MESSAGE_THREAD_ID = "`messagethreadid`";
    public static final String COLUMN_FROM = "`from`";
    public static final String COLUMN_TO = "`to`";
    public static final String COLUMN_CC = "`cc`";
    public static final String COLUMN_BODY = "`body`";
    public static final String COLUMN_TYPE = "`type`";
    public static final String COLUMN_CREATED_DATE = "`createdDate`";


    private static final String DATABASE_NAME = "message.db";
    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE =
            "CREATE TABLE " + TABLE_NAME +
                    "  (" +
                    "     " + COLUMN_ID + " PRIMARY_KEY auto_increment," +
                    "     " + COLUMN_MESSAGE_THREAD_ID+" INTEGER," +
                    "     " + COLUMN_FROM+"            VARCHAR(255)," +
                    "     " + COLUMN_TO+"              VARCHAR(255)," +
                    "     " + COLUMN_CC+"              VARCHAR(255)," +
                    "     " + COLUMN_BODY+"            TEXT," +
                    "     " + COLUMN_TYPE+"            VARCHAR(255)," +
                    "     " + COLUMN_CREATED_DATE+"    NUMERIC" +
                    "  );";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
