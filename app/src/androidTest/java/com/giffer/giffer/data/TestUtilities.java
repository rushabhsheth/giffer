package com.giffer.giffer.data;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

/**
 * Created by archanaarunkumar on 8/12/17.
 */

public class TestUtilities extends AndroidTestCase {

    static final long TEST_DATE = 1419033600L;  // December 20th, 2014

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }


    static ContentValues createNewsValues() {
        ContentValues newsValue = new ContentValues();
        newsValue.put(VideoContract.VideoEntry.COLUMN_NEWS_KEY,"abcd");
        newsValue.put(VideoContract.VideoEntry.COLUMN_NEWS_IMAGE_URI,"Archana is a damn");
        newsValue.put(VideoContract.VideoEntry.COLUMN_NEWS_TITLE," Test Title for the win");
        newsValue.put(VideoContract.VideoEntry.COLUMN_NEWS_DESCRIPTION, " Test description for the win");
        newsValue.put(VideoContract.VideoEntry.COLUMN_NEWS_CHANNEL,"g/AllTest");
        newsValue.put(VideoContract.VideoEntry.COLUMN_NEWS_TIME," Today 11h ago");
        newsValue.put(VideoContract.VideoEntry.COLUMN_NEWS_ORIGINAL_LINK, " original link");
        newsValue.put(VideoContract.VideoEntry.COLUMN_NEWS_VIEWS, 4);
        newsValue.put(VideoContract.VideoEntry.COLUMN_NEWS_UPVOTES,4);
        newsValue.put(VideoContract.VideoEntry.COLUMN_NEWS_DOWNVOTES,0);
        newsValue.put(VideoContract.VideoEntry.COLUMN_NEWS_COMMENTCOUNT,100);

        newsValue.put(VideoContract.VideoEntry.COLUMN_USER_ID,"user id");
        newsValue.put(VideoContract.VideoEntry.COLUMN_USER_PROFILEIMAGE," profile image ");
        newsValue.put(VideoContract.VideoEntry.COLUMN_USER_NAME," user name ");
        newsValue.put(VideoContract.VideoEntry.COLUMN_USER_POSTCOUNT, 5);
        newsValue.put(VideoContract.VideoEntry.COLUMN_USER_UPVOTESRECEIVED,200);

        return newsValue;
    }


    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }


}
