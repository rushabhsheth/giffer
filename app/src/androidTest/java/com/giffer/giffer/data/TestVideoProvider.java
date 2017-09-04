package com.giffer.giffer.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * Created by archanaarunkumar on 8/16/17.
 */

public class TestVideoProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestVideoProvider.class.getSimpleName();

    /*
       This helper function deletes all records from both database tables using the ContentProvider.
       It also queries the ContentProvider to make sure that the database has been successfully
       deleted, so it cannot be used until the Query and Delete functions have been written
       in the ContentProvider.
       Students: Replace the calls to deleteAllRecordsFromDB with this one after you have written
       the delete functionality in the ContentProvider.
     */
    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                VideoContract.VideoEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                VideoContract.VideoEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from News table during delete", 0, cursor.getCount());
        cursor.close();

    }

    /*
        Student: Refactor this function to use the deleteAllRecordsFromProvider functionality once
        you have implemented delete functionality there.
     */
    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    /*
        This test checks to make sure that the content provider is registered correctly.
        Students: Uncomment this test to make sure you've correctly registered the NewsProvider.
     */
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // WeatherProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                VideoProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: NewsProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + VideoContract.CONTENT_AUTHORITY,
                    providerInfo.authority, VideoContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: WeatherProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    /*
            This test doesn't touch the database.  It verifies that the ContentProvider returns
            the correct type for each type of URI that it can handle.
            Students: Uncomment this test to verify that your implementation of GetType is
            functioning correctly.
         */
    public void testGetType() {
        // content://com.example.android.sunshine.app/weather/
        String type = mContext.getContentResolver().getType(VideoContract.VideoEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.example.android.sunshine.app/weather
        assertEquals("Error: the NewsEntery CONTENT_URI should return NewsEntry.CONTENT_TYPE",
                VideoContract.VideoEntry.CONTENT_TYPE, type);

//        int testNews = 0;
//        // content://com.example.android.sunshine.app/weather/94074
//        type = mContext.getContentResolver().getType(
//                VideoContract.VideoEntry.buildNewsUri(testNews));
//        // vnd.android.cursor.dir/com.example.android.sunshine.app/weather
//        assertEquals("Error: the WeatherEntry CONTENT_URI with location should return WeatherEntry.CONTENT_TYPE",
//                VideoContract.VideoEntry.CONTENT_TYPE, type);
    }


    /*
        This test uses the database directly to insert and then uses the ContentProvider to
        read out the data.  Uncomment this test to see if the basic weather query functionality
        given in the ContentProvider is working correctly.
     */
    public void testBasicNewsQuery() {
        // insert our test records into the database
        VideoDbHelper dbHelper = new VideoDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createNewsValues();

        long newsRowId = db.insert(VideoContract.VideoEntry.TABLE_NAME, null, testValues);
        assertTrue("Unable to Insert WeatherEntry into the Database", newsRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor weatherCursor = mContext.getContentResolver().query(
                VideoContract.VideoEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicWeatherQuery", weatherCursor, testValues);
    }

    /*
        This test uses the provider to insert and then update the data. Uncomment this test to
        see if your update location is functioning correctly.
     */
    public void testUpdateNews() {
        // Create a new map of values, where column names are the keys
        ContentValues values = TestUtilities.createNewsValues();

        Uri newsUri = mContext.getContentResolver().
                insert(VideoContract.VideoEntry.CONTENT_URI, values);
        long newsRowId = ContentUris.parseId(newsUri);

        // Verify we got a row back.
        assertTrue(newsRowId != -1);
        Log.d(LOG_TAG, "New row id: " + newsRowId);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(VideoContract.VideoEntry._ID, newsRowId);
        updatedValues.put(VideoContract.VideoEntry.COLUMN_NEWS_TITLE, "Santa's Village");

        // Create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor newsCursor = mContext.getContentResolver().query(VideoContract.VideoEntry.CONTENT_URI, null, null, null, null);

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        newsCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                VideoContract.VideoEntry.CONTENT_URI, updatedValues, VideoContract.VideoEntry._ID + "= ?",
                new String[] { Long.toString(newsRowId)});
        assertEquals(count, 1);

        // Test to make sure our observer is called.  If not, we throw an assertion.
        //
        // Students: If your code is failing here, it means that your content provider
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();

        newsCursor.unregisterContentObserver(tco);
        newsCursor.close();

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                VideoContract.VideoEntry.CONTENT_URI,
                null,   // projection
                VideoContract.VideoEntry._ID + " = " + newsRowId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateLocation.  Error validating news entry update.",
                cursor, updatedValues);

        cursor.close();
    }


    // Make sure we can still delete after adding/updating stuff
    //
    // Student: Uncomment this test after you have completed writing the insert functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.
    public void testInsertReadProvider() {
        ContentValues testValues = TestUtilities.createNewsValues();

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(VideoContract.VideoEntry.CONTENT_URI, true, tco);
        Uri locationUri = mContext.getContentResolver().insert(VideoContract.VideoEntry.CONTENT_URI, testValues);

        // Did our content observer get called?  Students:  If this fails, your insert location
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long newsRowId = ContentUris.parseId(locationUri);

        // Verify we got a row back.
        assertTrue(newsRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                VideoContract.VideoEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating LocationEntry.",
                cursor, testValues);

    }

    // Make sure we can still delete after adding/updating stuff
    //
    // Student: Uncomment this test after you have completed writing the delete functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.
    public void testDeleteRecords() {
        testInsertReadProvider();

        // Register a content observer for our location delete.
        TestUtilities.TestContentObserver newsObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(VideoContract.VideoEntry.CONTENT_URI, true, newsObserver);

        deleteAllRecordsFromProvider();

        // Students: If either of these fail, you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
        // delete.  (only if the insertReadProvider is succeeding)
        newsObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(newsObserver);
     }


    static private final int BULK_INSERT_RECORDS_TO_INSERT = 10;
    static ContentValues[] createBulkInsertNewsValues() {
        long currentTestDate = TestUtilities.TEST_DATE;
        long millisecondsInADay = 1000*60*60*24;
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, currentTestDate+= millisecondsInADay ) {
            ContentValues newsValue = new ContentValues();
            newsValue.put(VideoContract.VideoEntry.COLUMN_NEWS_KEY,"abcd");
            newsValue.put(VideoContract.VideoEntry.COLUMN_NEWS_IMAGE_URI,"Archana is a damn");
            newsValue.put(VideoContract.VideoEntry.COLUMN_NEWS_TITLE," Test Title for the win");
            newsValue.put(VideoContract.VideoEntry.COLUMN_NEWS_DESCRIPTION, " Test description for the win");
            newsValue.put(VideoContract.VideoEntry.COLUMN_NEWS_CHANNEL,"g/AllTest");
            newsValue.put(VideoContract.VideoEntry.COLUMN_NEWS_TIME,String.valueOf(currentTestDate));
            newsValue.put(VideoContract.VideoEntry.COLUMN_NEWS_ORIGINAL_LINK, " original link");
            newsValue.put(VideoContract.VideoEntry.COLUMN_NEWS_VIEWS, i);
            newsValue.put(VideoContract.VideoEntry.COLUMN_NEWS_UPVOTES,4);
            newsValue.put(VideoContract.VideoEntry.COLUMN_NEWS_DOWNVOTES,0);
            newsValue.put(VideoContract.VideoEntry.COLUMN_NEWS_COMMENTCOUNT,100);

            newsValue.put(VideoContract.VideoEntry.COLUMN_USER_ID,"user id");
            newsValue.put(VideoContract.VideoEntry.COLUMN_USER_PROFILEIMAGE," profile image ");
            newsValue.put(VideoContract.VideoEntry.COLUMN_USER_NAME," user name ");
            newsValue.put(VideoContract.VideoEntry.COLUMN_USER_POSTCOUNT, 5);
            newsValue.put(VideoContract.VideoEntry.COLUMN_USER_UPVOTESRECEIVED,200);
            returnContentValues[i] = newsValue;
        }
        return returnContentValues;
    }

    // Student: Uncomment this test after you have completed writing the BulkInsert functionality
    // in your provider.  Note that this test will work with the built-in (default) provider
    // implementation, which just inserts records one-at-a-time, so really do implement the
    // BulkInsert ContentProvider function.
    public void testBulkInsert() {
        // first, let's create a location value
        ContentValues testValues = TestUtilities.createNewsValues();
        Uri locationUri = mContext.getContentResolver().insert(VideoContract.VideoEntry.CONTENT_URI, testValues);
        long newsRowId = ContentUris.parseId(locationUri);

        // Verify we got a row back.
        assertTrue(newsRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                VideoContract.VideoEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testBulkInsert. Error validating LocationEntry.",
                cursor, testValues);

        deleteAllRecords();
        // Now we can bulkInsert some weather.  In fact, we only implement BulkInsert for weather
        // entries.  With ContentProviders, you really only have to implement the features you
        // use, after all.
        ContentValues[] bulkInsertContentValues = createBulkInsertNewsValues();

        // Register a content observer for our bulk insert.
        TestUtilities.TestContentObserver weatherObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(VideoContract.VideoEntry.CONTENT_URI, true, weatherObserver);

        int insertCount = mContext.getContentResolver().bulkInsert(VideoContract.VideoEntry.CONTENT_URI, bulkInsertContentValues);

        // Students:  If this fails, it means that you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in your BulkInsert
        // ContentProvider method.
        weatherObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(weatherObserver);

        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);

        // A cursor is your primary interface to the query results.
        cursor = mContext.getContentResolver().query(
                VideoContract.VideoEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                VideoContract.VideoEntry.COLUMN_NEWS_VIEWS + " ASC"  // sort order == by DATE ASCENDING
        );

        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        // and let's make sure they match the ones we created
        cursor.moveToFirst();
        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext() ) {
            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating WeatherEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }
}
