package com.example.untitled.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.example.untitled.model.Message;
import com.example.untitled.model.MessageType;
import com.example.untitled.services.MySQLiteHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: brian
 * Date: 8/20/13
 * Time: 8:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageDao {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {
            MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_MESSAGE_THREAD_ID,
            MySQLiteHelper.COLUMN_FROM,
            MySQLiteHelper.COLUMN_TO,
            MySQLiteHelper.COLUMN_CC,
            MySQLiteHelper.COLUMN_BODY,
            MySQLiteHelper.COLUMN_TYPE,
            MySQLiteHelper.COLUMN_CREATED_DATE
            };

    public MessageDao(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Message insert(Message message) {
        ContentValues values = new ContentValues();
//        values.put("messagethreadid", message.getMessageThreadId());
//        values.put("from", message.getFrom());
//        values.put("to", message.getTo());
//        values.put("cc", message.getCc());
        values.put("body", message.getBody());
        values.put("type",message.getType().toString());
        values.put("createdDate",message.getCreatedDate().getTime());

        long insertId = database.insert(MySQLiteHelper.TABLE_NAME, null,
                values);
        message.setId(insertId);
        return message;
    }

    public Cursor getMessagesCursor() {
        open();
       Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME,
               allColumns, null, null, null, null, null);

        close();
        return cursor;
    }

    public List<Message> getMessages() {
        open();
        List<Message> messages = new ArrayList<Message>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Message comment = cursorToMessage(cursor);
            messages.add(comment);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        close();
        return messages;
    }

    private Message cursorToMessage(Cursor cursor) {
        Message message = new Message();
        message.setId(cursor.getLong(0));
        message.setMessageThreadId(cursor.getString(1));
        message.setFrom(cursor.getString(2));
        message.setTo(cursor.getString(3));
        message.setCc(cursor.getString(4));
        message.setBody(cursor.getString(5));
        message.setType(MessageType.valueOf(cursor.getString(6)));
        message.setCreatedDate(new Date(cursor.getInt(7)));
        return message;
    }
}
