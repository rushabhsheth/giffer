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

    public static final String PATH_VIDEO = "news";


    /* Inner class that defines the table contents of the weather table */
    public static final class VideoEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIDEO).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEO;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEO;

        public static final String TABLE_NAME = "news";

        // Column with the video key into the video table.
        public static final String COLUMN_NEWS_KEY = "news_id";
        //News image uri
        public static final String COLUMN_NEWS_IMAGE_URI = "news_image_uri";
        //video title
        public static final String COLUMN_NEWS_TITLE = "news_title";
        //video description
        public static final String COLUMN_NEWS_DESCRIPTION = "news_description";
        //video description
        public static final String COLUMN_NEWS_CHANNEL = "news_channel";
        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_NEWS_TIME = "news_time";
        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_NEWS_ORIGINAL_LINK = "news_original_link";
        //video views
        public static final String COLUMN_NEWS_VIEWS = "news_views";
        //video upvotes
        public static final String COLUMN_NEWS_UPVOTES = "news_upvotes";
        //video upvotes
        public static final String COLUMN_NEWS_DOWNVOTES = "news_downvotes";
        //video upvotes
        public static final String COLUMN_NEWS_COMMENTCOUNT = "news_commentcount";

        // User id
        public static final String COLUMN_USER_ID = "user_id";
        // User profile
        public static final String COLUMN_USER_PROFILEIMAGE = "user_profile_image";
        // User name
        public static final String COLUMN_USER_NAME = "user_name";
        // User photo id
        public static final String COLUMN_USER_POSTCOUNT = "user_post_count";
        // User photo id
        public static final String COLUMN_USER_UPVOTESRECEIVED = "user_upvotes_received";


        public static Uri buildNewsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getNewsTimeFromUri(Uri uri) {
            String dateString = uri.getQueryParameter(COLUMN_NEWS_TIME);
            if (null != dateString && dateString.length() > 0)
                return Long.parseLong(dateString);
            else
                return 0;
        }

        public static long getNewsUpvotesFromUri(Uri uri) {
            String upvoteString = uri.getQueryParameter(COLUMN_NEWS_UPVOTES);
            if (null != upvoteString && upvoteString.length() > 0)
                return Long.parseLong(upvoteString);
            else
                return 0;
        }
    }

}
