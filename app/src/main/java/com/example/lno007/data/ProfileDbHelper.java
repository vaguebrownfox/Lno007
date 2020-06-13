package com.example.lno007.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.lno007.data.ProfileContract.ProfileEntry;

public class ProfileDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = ProfileDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "profile.db";

    private static final int DATABASE_VERSION = 1;

    public ProfileDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_PROFILES_TABLE = "CREATE TABLE " + ProfileEntry.TABLE_NAME + " ("
                + ProfileEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProfileEntry.NAME + " TEXT NOT NULL, "
                + ProfileEntry.AGE + " INTEGER NOT NULL, "
                + ProfileEntry.GENDER + " INTEGER NOT NULL, "
                + ProfileEntry.HEIGHT + " INTEGER NOT NULL, "
                + ProfileEntry.WEIGHT + " INTEGER NOT NULL ); ";

        db.execSQL(SQL_CREATE_PROFILES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // nothing
    }
}
