package com.example.rajatmhetre.popularmovies2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rajatmhetre on 21/08/15.
 */
public class DbHandler extends SQLiteOpenHelper {

    public static int DATABASE_VERSION = 1;

    public static String DATABASE_NAME = "movies.db";

    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + DbContract.MovieEntry.TABLE_NAME + " (" +
                DbContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DbContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                DbContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                DbContract.MovieEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                DbContract.MovieEntry.COLUMN_YEAR + " INTEGER NOT NULL, " +
                DbContract.MovieEntry.COLUMN_RATING + " REAL NOT NULL, " +
                DbContract.MovieEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                " UNIQUE (" + DbContract.MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
