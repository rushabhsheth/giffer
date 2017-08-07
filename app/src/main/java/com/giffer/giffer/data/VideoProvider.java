package com.giffer.giffer.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by archanaarunkumar on 8/5/17.
 */

public class VideoProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private VideoDbHelper mOpenHelper;

    static final int VIDEO = 100;

    private static final SQLiteQueryBuilder sVideoByDateQueryBuilder;

    static{
        sVideoByDateQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sVideoByDateQueryBuilder.setTables(
                VideoContract.VideoEntry.TABLE_NAME );
    }


    //video.date >= ?
    private static final String sVideoWithStartTimeSelection =
            VideoContract.VideoEntry.TABLE_NAME+
                    "." + VideoContract.VideoEntry.COLUMN_TIME + " >= ? ";

    //video.date >= ?
    private static final String sVideoWithStartUpVotesSelection =
            VideoContract.VideoEntry.TABLE_NAME+
                    "." + VideoContract.VideoEntry.COLUMN_VID_UPVOTES + " >= ? ";

    private Cursor getVideosByTime (Uri uri, String[] projection, String sortOrder) {
        long startTime = VideoContract.VideoEntry.getVideoTimeFromUri(uri);

        String selection;
        String[] selectionArgs;


        selection = sVideoWithStartTimeSelection;
        selectionArgs = new String[]{Long.toString(startTime)};

        return sVideoByDateQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getVideosByUpvotes (Uri uri, String[] projection, String sortOrder) {
        long startUpvotes = VideoContract.VideoEntry.getVideoUpvotesFromUri(uri);

        String selection;
        String[] selectionArgs;


        selection = sVideoWithStartUpVotesSelection;
        selectionArgs = new String[]{Long.toString(startUpvotes)};

        return sVideoByDateQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = VideoContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, VideoContract.PATH_VIDEO, VIDEO);

        return matcher;

    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new VideoDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case VIDEO:
                return VideoContract.VideoEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "weather"
            case VIDEO: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        VideoContract.VideoEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case VIDEO: {
                long _id = db.insert(VideoContract.VideoEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = VideoContract.VideoEntry.buildVideoUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case VIDEO:
                rowsDeleted = db.delete(
                        VideoContract.VideoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case VIDEO:
                rowsUpdated = db.update(VideoContract.VideoEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case VIDEO:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(VideoContract.VideoEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

}
