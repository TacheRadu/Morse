package com.morse;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    final SQLiteDatabase database;

    public Database(@Nullable Context context) {
        super(context, "database.db", null, 1);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS channels (\n"
                + "	id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "	name text NOT NULL\n"
                + ");");
        db.execSQL("CREATE TABLE IF NOT EXISTS users (\n"
                + "	channel_id int NOT NULL,\n"
                + "	username text NOT NULL,\n"
                + "	password text NOT NULL,\n"
                + " CONSTRAINT users_ct FOREIGN KEY(channel_id) \n"
                + " REFERENCES channels(id) ON DELETE CASCADE ON UPDATE CASCADE"
                + ");");
        System.out.println("succesf");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
