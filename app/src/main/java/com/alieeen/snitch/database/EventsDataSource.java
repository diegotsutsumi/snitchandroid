package com.alieeen.snitch.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alieeen.snitch.model.Event;

import java.util.ArrayList;

/**
 * Created by alinekborges on 16/04/15.
 */
public class EventsDataSource {
    // Database fields
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = {
            SQLiteHelper.COLUMN_ID,
            SQLiteHelper.COLUMN_CAMERANAME,
            SQLiteHelper.COLUMN_DATETIME,
            SQLiteHelper.COLUMN_IMAGENAME,
            SQLiteHelper.COLUMN_NUMBER,
            SQLiteHelper.COLUMN_VIEWED
    };

    public EventsDataSource(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Event createEvent(String camName, String dateTime, String imgName, String number, int viewed) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_CAMERANAME, camName);
        values.put(SQLiteHelper.COLUMN_DATETIME, dateTime);
        values.put(SQLiteHelper.COLUMN_IMAGENAME, imgName);
        values.put(SQLiteHelper.COLUMN_NUMBER, number);
        values.put(SQLiteHelper.COLUMN_VIEWED, viewed);

        open();
        long insertId = database.insert(SQLiteHelper.TABLE_EVENTS, null, values);

        Cursor cursor = database.query(SQLiteHelper.TABLE_EVENTS,
                allColumns, SQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();

        Event newEvent = cursorToEvent(cursor);
        cursor.close();
        close();

        return newEvent;
    }

    public void deleteEvent(Event event) {
        long id = event.getId();

    /*database.delete(SQLiteHelper.TABLE_EVENTS, SQLiteHelper.COLUMN_ID
        + " = " + id, null);*/

        deleteEvent(id);
    }

    public void deleteEvent(long id) {
        //String whereArgs [] = {String.valueOf(id)};
        try {
            open();
            database.delete(SQLiteHelper.TABLE_EVENTS, SQLiteHelper.COLUMN_ID
                    + " = " + id, null);
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Event> getAllEvents() {
        open();
        ArrayList<Event> events = new ArrayList<Event>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_EVENTS,
                allColumns, null, null, null, null, SQLiteHelper.COLUMN_ID + " DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Event event = cursorToEvent(cursor);
            events.add(event);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        close();
        return events;
    }

    public ArrayList<Event> getEvents(int limit) {
        open();
        ArrayList<Event> events = new ArrayList<Event>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_EVENTS,
                allColumns, null, null, null, null, SQLiteHelper.COLUMN_ID + " DESC", String.valueOf(limit));

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Event event = cursorToEvent(cursor);
            events.add(event);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        close();
        return events;
    }

    public ArrayList<Event> getNextEvents(long lastId, int quantity) {
        open();
        ArrayList<Event> events = new ArrayList<Event>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_EVENTS,
                allColumns, SQLiteHelper.COLUMN_ID + " < " + String.valueOf(lastId), null, null, null,
                SQLiteHelper.COLUMN_ID + " DESC", String.valueOf(quantity));

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Event event = cursorToEvent(cursor);
            events.add(event);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        close();
        return events;
    }

    public void updateViewed(long id, boolean viewed) {
        open();

        ContentValues newValues = new ContentValues();
        newValues.put(SQLiteHelper.COLUMN_VIEWED, viewed ? 1 : 0);
        database.update(SQLiteHelper.TABLE_EVENTS, newValues, SQLiteHelper.COLUMN_ID + "=" + id, null);

        close();
    }

  /*public Event getByIndex (long index)
  {
	  Event event = null;
	  String whereClause = "OFFSET ?";
	  String args[] = new String[]{String.valueOf(index)};

	  Cursor cursor = database.query(SQLiteHelper.TABLE_EVENTS,
		        allColumns, null, null, null, null, null, "LIMIT 1 OFFSET");

	  if (cursor.moveToFirst())
		  event = cursorToEvent(cursor);

	  return event;
  }*/

    private Event cursorToEvent(Cursor cursor) {
        Event event = new Event();
        event.setId(cursor.getLong(0));
        event.setCameraName(cursor.getString(1));
        event.setDateTime(cursor.getString(2));
        event.setImageName(cursor.getString(3));
        event.setNumber(cursor.getString(4));
        event.setViewed(cursor.getInt(5) == 1 ? true : false);

        return event;
    }
}
