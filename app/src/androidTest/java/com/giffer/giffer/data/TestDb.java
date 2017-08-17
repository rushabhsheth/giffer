package com.giffer.giffer.data;

import android.content.ContentValues;
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
        newsColumnHashSet.add(VideoContract.VideoEntry.COLUMN_NEWS_KEY);
        newsColumnHashSet.add(VideoContract.VideoEntry.COLUMN_NEWS_IMAGE_URI);
        newsColumnHashSet.add(VideoContract.VideoEntry.COLUMN_NEWS_TITLE);
        newsColumnHashSet.add(VideoContract.VideoEntry.COLUMN_NEWS_DESCRIPTION);
        newsColumnHashSet.add(VideoContract.VideoEntry.COLUMN_NEWS_CHANNEL);
        newsColumnHashSet.add(VideoContract.VideoEntry.COLUMN_NEWS_TIME);
        newsColumnHashSet.add(VideoContract.VideoEntry.COLUMN_NEWS_ORIGINAL_LINK);
        newsColumnHashSet.add(VideoContract.VideoEntry.COLUMN_NEWS_VIEWS);
        newsColumnHashSet.add(VideoContract.VideoEntry.COLUMN_NEWS_UPVOTES);
        newsColumnHashSet.add(VideoContract.VideoEntry.COLUMN_NEWS_DOWNVOTES);
        newsColumnHashSet.add(VideoContract.VideoEntry.COLUMN_NEWS_COMMENTCOUNT);

        newsColumnHashSet.add(VideoContract.VideoEntry.COLUMN_USER_ID);
        newsColumnHashSet.add(VideoContract.VideoEntry.COLUMN_USER_PROFILEIMAGE);
        newsColumnHashSet.add(VideoContract.VideoEntry.COLUMN_USER_NAME);
        newsColumnHashSet.add(VideoContract.VideoEntry.COLUMN_USER_POSTCOUNT);
        newsColumnHashSet.add(VideoContract.VideoEntry.COLUMN_USER_UPVOTESRECEIVED);


        int columnNameIndex = c.getColumnIndex("name");
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


    public void testWeatherTable() {
        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        VideoDbHelper dbHelper = new VideoDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step (Weather): Create weather values
        ContentValues newsValues = TestUtilities.createNewsValues();

        // Third Step (Weather): Insert ContentValues into database and get a row ID back
        long newsRowId = db.insert(VideoContract.VideoEntry.TABLE_NAME, null, newsValues);
        assertTrue(newsRowId != -1);

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor newsCursor = db.query(
                VideoContract.VideoEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue( "Error: No Records returned from location query", newsCursor.moveToFirst() );

        // Fifth Step: Validate the location Query
        TestUtilities.validateCurrentRecord("testInsertReadDb videoEntry failed to validate",
                newsCursor, newsValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from news query",
                newsCursor.moveToNext() );

        // Sixth Step: Close cursor and database
        newsCursor.close();
        dbHelper.close();
    }


}
