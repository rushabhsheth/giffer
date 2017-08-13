package com.giffer.giffer.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by archanaarunkumar on 8/12/17.
 */

public class TestDb extends AndroidTestCase {
    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(VideoDbHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(VideoContract.VideoEntry.TABLE_NAME);

        mContext.deleteDatabase(VideoDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new VideoDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + VideoContract.VideoEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> newsColumnHashSet = new HashSet<String>();
        newsColumnHashSet.add(VideoContract.VideoEntry._ID);
        newsColumnHashSet.add(VideoContract.VideoEntry.COLUMN_NEWS_IMAGE);
        newsColumnHashSet.add(VideoContract.VideoEntry.COLUMN_NEWS_TITLE);
        newsColumnHashSet.add(VideoContract.VideoEntry.COLUMN_NEWS_DESCRIPTION);

        newsColumnHashSet.add(VideoContract.VideoEntry.COLUMN_USER_NAME);
        newsColumnHashSet.add(VideoContract.VideoEntry.COLUMN_USER_PHOTO);

        int columnNameIndex = c.getColumnIndex("user_name");
        do {
            String columnName = c.getString(columnNameIndex);
            newsColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required news entry columns",
                newsColumnHashSet.isEmpty());
        db.close();
    }
}
