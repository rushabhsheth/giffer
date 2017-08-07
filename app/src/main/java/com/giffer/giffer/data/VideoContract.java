package com.giffer.giffer.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;


/**
 * Created by archanaarunkumar on 8/5/17.
 */

public class VideoContract {

    public static final String CONTENT_AUTHORITY = "com.giffer.giffer";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_VIDEO = "video";


    /* Inner class that defines the table contents of the weather table */
    public static final class VideoEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIDEO).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEO;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEO;

        public static final String TABLE_NAME = "video";

        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_TIME = "time";

        // User id
        public static final String COLUMN_USER_ID = "user_id";
        // User name
        public static final String COLUMN_USER_NAME = "user_name";
        // User photo id
        public static final String COLUMN_USER_PHOTO = "user_photo";


        // Column with the video key into the video table.
        public static final String COLUMN_VID_KEY = "video_id";
        //video title
        public static final String COLUMN_VID_TITLE = "video_title";
        //video thumbnail
        public static final String COLUMN_VID_THUMBNAIL = "video_thumbnail";
        //video views
        public static final String COLUMN_VID_VIEWS = "video_views";
        //video upvotes
        public static final String COLUMN_VID_UPVOTES = "video_upvotes";


        public static Uri buildVideoUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getVideoTimeFromUri(Uri uri) {
            String dateString = uri.getQueryParameter(COLUMN_TIME);
            if (null != dateString && dateString.length() > 0)
                return Long.parseLong(dateString);
            else
                return 0;
        }

        public static long getVideoUpvotesFromUri(Uri uri) {
            String upvoteString = uri.getQueryParameter(COLUMN_VID_UPVOTES);
            if (null != upvoteString && upvoteString.length() > 0)
                return Long.parseLong(upvoteString);
            else
                return 0;
        }
    }

}
