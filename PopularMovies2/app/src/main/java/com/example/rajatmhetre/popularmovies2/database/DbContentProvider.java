package com.example.rajatmhetre.popularmovies2.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.rajatmhetre.popularmovies2.init;

/**
 * Created by rajatmhetre on 21/08/15.
 */
public class DbContentProvider extends ContentProvider {


    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DbHandler mOpenHelper;

    static final int MOVIES = 100;
    static final int MOVIES_WITH_ID = 101;



    private static final String sMovieIdSelection =
            DbContract.MovieEntry.TABLE_NAME +
                    "." + DbContract.MovieEntry.COLUMN_MOVIE_ID + " = ?";

    @Override
    public boolean onCreate() {
        mOpenHelper = new DbHandler(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES_WITH_ID:
                return DbContract.MovieEntry.CONTENT_ITEM_TYPE;
            case MOVIES:
                return DbContract.MovieEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Cursor retCursor;

        final int match = sUriMatcher.match(uri);

        switch (match) {
            // "movie/*"
            case MOVIES_WITH_ID:
            {
                retCursor = getMoviesById(uri, projection, sortOrder);
                break;
            }
            // "movie"
            case MOVIES: {
                retCursor = getMovies(uri, projection, selection, selectionArgs, sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    private Cursor getMoviesById(Uri uri, String[] projection, String sortOrder) {
        return mOpenHelper.getReadableDatabase().query(
                DbContract.MovieEntry.TABLE_NAME,
                projection,
                sMovieIdSelection,
                new String[]{DbContract.MovieEntry.getIdFromUri(uri)},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getMovies(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mOpenHelper.getReadableDatabase().query(
                DbContract.MovieEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        Uri returnUri;

        int match = sUriMatcher.match(uri);

        switch (match) {
            // "movie"
            case MOVIES: {
                long _id = mOpenHelper.getWritableDatabase().insert(DbContract.MovieEntry.TABLE_NAME, null, contentValues);
                if(_id > 0){
                    returnUri = DbContract.MovieEntry.buildMovieUri(_id);
                }
                else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int deletedRows = 0;

        if(selection == null) selection = "1";

        int match = sUriMatcher.match(uri);

        switch (match) {
            // "movie"
            case MOVIES: {
                deletedRows = mOpenHelper.getWritableDatabase().delete(DbContract.MovieEntry.TABLE_NAME, selection, selectionArgs);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(deletedRows > 0 ){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return deletedRows;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int match = buildUriMatcher().match(uri);

        int rowsUpdated = 0;

        if (selection == null) selection = "1";

        switch (match) {

            case MOVIES: {
                rowsUpdated = db.update(DbContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    static UriMatcher buildUriMatcher() {

        final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);


        sURIMatcher.addURI(init.CONTENT_AUTHORITY, DbContract.PATH_MOVIE, MOVIES);
        sURIMatcher.addURI(init.CONTENT_AUTHORITY, DbContract.PATH_MOVIE + "/*", MOVIES_WITH_ID);

        return sURIMatcher;
    }
}
