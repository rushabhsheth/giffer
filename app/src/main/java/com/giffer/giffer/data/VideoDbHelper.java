package com.giffer.giffer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by archanaarunkumar on 8/5/17.
 */

public class VideoDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "news.db";

    public VideoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_VIDEO_TABLE = "CREATE TABLE " + VideoContract.VideoEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                VideoContract.VideoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                VideoContract.VideoEntry.COLUMN_TIME + " INTEGER NOT NULL, " +
                VideoContract.VideoEntry.COLUMN_USER_ID + " INTEGER NOT NULL, " +
                VideoContract.VideoEntry.COLUMN_USER_NAME + " TEXT NOT NULL, " +
                VideoContract.VideoEntry.COLUMN_USER_PHOTO + " INTEGER NOT NULL, " +

                VideoContract.VideoEntry.COLUMN_NEWS_KEY + " INTEGER NOT NULL, " +
                VideoContract.VideoEntry.COLUMN_NEWS_IMAGE + " INTEGER NOT NULL, " +
                VideoContract.VideoEntry.COLUMN_NEWS_TITLE + " TEXT NOT NULL, " +
                VideoContract.VideoEntry.COLUMN_NEWS_DESCRIPTION + " TEXT NOT NULL, " +
                VideoContract.VideoEntry.COLUMN_NEWS_VIEWS + " INTEGER NOT NULL, " +
                VideoContract.VideoEntry.COLUMN_NEWS_UPVOTES + " INTEGER NOT NULL" +
                " );";

                sqLiteDatabase.execSQL(SQL_CREATE_VIDEO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + VideoContract.VideoEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
