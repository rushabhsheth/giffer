package com.giffer.giffer.data;

import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by archanaarunkumar on 8/16/17.
 */

public class TestVideoContract extends AndroidTestCase {

    private static final int TEST_NEWS = 0;

    public void testBuildNewsLocation() {
        Uri newsUri = VideoContract.VideoEntry.buildNewsUri(TEST_NEWS);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildWeatherLocation in " +
                        "VideoContract.",
                newsUri);
        assertEquals("Error: News not properly appended to the end of the Uri",
                String.valueOf(TEST_NEWS), newsUri.getLastPathSegment());
        assertEquals("Error: Weather location Uri doesn't match our expected result",
                newsUri.toString(),
                "content://com.giffer.giffer/news/0");
    }
}
