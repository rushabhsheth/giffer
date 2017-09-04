package com.giffer.giffer.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by archanaarunkumar on 8/16/17.
 */

public class TestUriMatcher extends AndroidTestCase {

    // content://com.giffer.giffer.com/news"
    private static final Uri TEST_NEWS_DIR = VideoContract.VideoEntry.CONTENT_URI;

    /*
        Students: This function tests that your UriMatcher returns the correct integer value
        for each of the Uri types that our ContentProvider can handle.  Uncomment this when you are
        ready to test your UriMatcher.
     */
    public void testUriMatcher() {
        UriMatcher testMatcher = VideoProvider.buildUriMatcher();

        assertEquals("Error: The WEATHER URI was matched incorrectly.",
                testMatcher.match(TEST_NEWS_DIR), VideoProvider.NEWS);
          }
}
