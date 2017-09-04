package com.giffer.giffer.NewsCard;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.nfc.Tag;
import android.util.Log;
import android.widget.ListView;

import com.giffer.giffer.data.VideoContract;
import com.giffer.giffer.data.VideoDbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by archanaarunkumar on 8/16/17.
 */

public class NewsCardDbManager {

    private static final String TAG = "NewsCardDbManager";

    private Context mContext;

    public List<NewsCard> mNewsCardList;
    public NewsCard mNewsCardSample;

    public NewsCardDbManager(Context context){
        mContext =context;
        initdata();

    }


    public void insertNewsCard(NewsCard newsCard) {
        ContentValues valueNewsCard = new ContentValues();

        valueNewsCard.put(VideoContract.VideoEntry.COLUMN_NEWS_KEY,newsCard.getNewsCardKey());
        valueNewsCard.put(VideoContract.VideoEntry.COLUMN_NEWS_IMAGE_URI,newsCard.getNewsCardImageUri());
        valueNewsCard.put(VideoContract.VideoEntry.COLUMN_NEWS_TITLE,newsCard.getVideoTitle());
        valueNewsCard.put(VideoContract.VideoEntry.COLUMN_NEWS_DESCRIPTION, newsCard.getVideoDescription());
        valueNewsCard.put(VideoContract.VideoEntry.COLUMN_NEWS_CHANNEL,newsCard.getIdChannel());
        valueNewsCard.put(VideoContract.VideoEntry.COLUMN_NEWS_TIME,newsCard.getTimePosted());
        valueNewsCard.put(VideoContract.VideoEntry.COLUMN_NEWS_ORIGINAL_LINK, newsCard.getOriginalLink());
        valueNewsCard.put(VideoContract.VideoEntry.COLUMN_NEWS_VIEWS, newsCard.getNewsViews());
        valueNewsCard.put(VideoContract.VideoEntry.COLUMN_NEWS_UPVOTES,newsCard.getNewsUpvotes());
        valueNewsCard.put(VideoContract.VideoEntry.COLUMN_NEWS_DOWNVOTES,newsCard.getNewsDownvotes());
        valueNewsCard.put(VideoContract.VideoEntry.COLUMN_NEWS_COMMENTCOUNT,newsCard.getNewsCommentCount());

        valueNewsCard.put(VideoContract.VideoEntry.COLUMN_USER_ID,newsCard.getUserId());
        valueNewsCard.put(VideoContract.VideoEntry.COLUMN_USER_PROFILEIMAGE,newsCard.getProfile_image());
        valueNewsCard.put(VideoContract.VideoEntry.COLUMN_USER_NAME,newsCard.getUserName());
        valueNewsCard.put(VideoContract.VideoEntry.COLUMN_USER_POSTCOUNT, newsCard.getUserPostCount());
        valueNewsCard.put(VideoContract.VideoEntry.COLUMN_USER_UPVOTESRECEIVED,newsCard.getUserUpvotesReceived());

        Uri newsCardUri = mContext.getContentResolver().insert(VideoContract.VideoEntry.CONTENT_URI, valueNewsCard);
        long newsRowId = ContentUris.parseId(newsCardUri);
        if(newsRowId==-1){
            Log.e(TAG, "ERROR: Inserting NewsCard to database failed");
        }

    }

    public void bulkInsertNewsCard(List<NewsCard> newsCardList) {
        ContentValues[] bulkInsertContentValues = new ContentValues[newsCardList.size()];

        for(int i =0;i<newsCardList.size();i++) {
            NewsCard newsCard = newsCardList.get(i);
            ContentValues valueNewsCard = new ContentValues();

            valueNewsCard.put(VideoContract.VideoEntry.COLUMN_NEWS_KEY, newsCard.getNewsCardKey());
            valueNewsCard.put(VideoContract.VideoEntry.COLUMN_NEWS_IMAGE_URI, newsCard.getNewsCardImageUri());
            valueNewsCard.put(VideoContract.VideoEntry.COLUMN_NEWS_TITLE, newsCard.getVideoTitle());
            valueNewsCard.put(VideoContract.VideoEntry.COLUMN_NEWS_DESCRIPTION, newsCard.getVideoDescription());
            valueNewsCard.put(VideoContract.VideoEntry.COLUMN_NEWS_CHANNEL, newsCard.getIdChannel());
            valueNewsCard.put(VideoContract.VideoEntry.COLUMN_NEWS_TIME, newsCard.getTimePosted());
            valueNewsCard.put(VideoContract.VideoEntry.COLUMN_NEWS_ORIGINAL_LINK, newsCard.getOriginalLink());
            valueNewsCard.put(VideoContract.VideoEntry.COLUMN_NEWS_VIEWS, newsCard.getNewsViews());
            valueNewsCard.put(VideoContract.VideoEntry.COLUMN_NEWS_UPVOTES, newsCard.getNewsUpvotes());
            valueNewsCard.put(VideoContract.VideoEntry.COLUMN_NEWS_DOWNVOTES, newsCard.getNewsDownvotes());
            valueNewsCard.put(VideoContract.VideoEntry.COLUMN_NEWS_COMMENTCOUNT, newsCard.getNewsCommentCount());

            valueNewsCard.put(VideoContract.VideoEntry.COLUMN_USER_ID, newsCard.getUserId());
            valueNewsCard.put(VideoContract.VideoEntry.COLUMN_USER_PROFILEIMAGE, newsCard.getProfile_image());
            valueNewsCard.put(VideoContract.VideoEntry.COLUMN_USER_NAME, newsCard.getUserName());
            valueNewsCard.put(VideoContract.VideoEntry.COLUMN_USER_POSTCOUNT, newsCard.getUserPostCount());
            valueNewsCard.put(VideoContract.VideoEntry.COLUMN_USER_UPVOTESRECEIVED, newsCard.getUserUpvotesReceived());

            bulkInsertContentValues[i] = valueNewsCard;
        }

        int insertCount = mContext.getContentResolver().bulkInsert(VideoContract.VideoEntry.CONTENT_URI, bulkInsertContentValues);

        if(insertCount==0){
            Log.e(TAG, "ERROR: Bulk inserting NewsCard to database failed");
        }
        else
        {
            Log.d(TAG, "BULK INSERT: Inserted "+insertCount+" NewsCards");
        }

    }


    public List<NewsCard> fetch_all_data() {

        List<NewsCard> newsCardArray = new ArrayList<>();

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                VideoContract.VideoEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    NewsCard newsCard = new NewsCard();
                    int index;

                    index = cursor.getColumnIndexOrThrow(VideoContract.VideoEntry.COLUMN_NEWS_KEY);
                    newsCard.setNewsCardKey(cursor.getString(index));

                    index = cursor.getColumnIndexOrThrow(VideoContract.VideoEntry.COLUMN_NEWS_IMAGE_URI);
                    newsCard.setNewsCardImageUri(cursor.getString(index));

                    index = cursor.getColumnIndexOrThrow(VideoContract.VideoEntry.COLUMN_NEWS_TITLE);
                    newsCard.setVideoTitle(cursor.getString(index));

                    index = cursor.getColumnIndexOrThrow(VideoContract.VideoEntry.COLUMN_NEWS_DESCRIPTION);
                    newsCard.setVideoDescription(cursor.getString(index));

                    index = cursor.getColumnIndexOrThrow(VideoContract.VideoEntry.COLUMN_NEWS_CHANNEL);
                    newsCard.setIdChannel(cursor.getString(index));

                    index = cursor.getColumnIndexOrThrow(VideoContract.VideoEntry.COLUMN_NEWS_TIME);
                    newsCard.setTimePosted(cursor.getString(index));

                    index = cursor.getColumnIndexOrThrow(VideoContract.VideoEntry.COLUMN_NEWS_ORIGINAL_LINK);
                    newsCard.setOriginalLink(cursor.getString(index));

                    index = cursor.getColumnIndexOrThrow(VideoContract.VideoEntry.COLUMN_NEWS_VIEWS);
                    newsCard.setNewsViews(cursor.getInt(index));

                    index = cursor.getColumnIndexOrThrow(VideoContract.VideoEntry.COLUMN_NEWS_UPVOTES);
                    newsCard.setNewsUpvotes(cursor.getInt(index));

                    index = cursor.getColumnIndexOrThrow(VideoContract.VideoEntry.COLUMN_NEWS_DOWNVOTES);
                    newsCard.setNewsDownvotes(cursor.getInt(index));

                    index = cursor.getColumnIndexOrThrow(VideoContract.VideoEntry.COLUMN_NEWS_COMMENTCOUNT);
                    newsCard.setNewsCommentCount(cursor.getInt(index));

                    index = cursor.getColumnIndexOrThrow(VideoContract.VideoEntry.COLUMN_USER_ID);
                    newsCard.setUserId(cursor.getString(index));

                    index = cursor.getColumnIndexOrThrow(VideoContract.VideoEntry.COLUMN_USER_PROFILEIMAGE);
                    newsCard.setProfile_image(cursor.getString(index));

                    index = cursor.getColumnIndexOrThrow(VideoContract.VideoEntry.COLUMN_USER_NAME);
                    newsCard.setUserName(cursor.getString(index));

                    index = cursor.getColumnIndexOrThrow(VideoContract.VideoEntry.COLUMN_USER_POSTCOUNT);
                    newsCard.setUserPostCount(cursor.getInt(index));
                  //  Log.d(TAG, "DEBUG: User post count is" + cursor.getInt(index));

                    index = cursor.getColumnIndexOrThrow(VideoContract.VideoEntry.COLUMN_USER_UPVOTESRECEIVED);
                    newsCard.setUserUpvotesReceived(cursor.getInt(index));
                   // Log.d(TAG, "DEBUG: User post count is" + cursor.getInt(index));


                    newsCardArray.add(newsCard);
                }
            } finally {
                cursor.close();
            }

        }
        else {
            Log.e(TAG,"ERROR: Database read gives a null cursor");
        }

        return newsCardArray;
    }

    public void delete_all(){
        mContext.getContentResolver().delete(
                VideoContract.VideoEntry.CONTENT_URI,
                null,
                null
        );
    }



    private void initdata(){

        mNewsCardList = new ArrayList<>();

        int id = mContext.getResources().getIdentifier("sample2", "drawable", mContext.getPackageName());
        String path = "android.resource://" + mContext.getPackageName() + "/" + id;

        int idUserImage = mContext.getResources().getIdentifier("rushabh_profile", "drawable", mContext.getPackageName());
        String pathUserImage = "android.resource://" + mContext.getPackageName() + "/" + String.valueOf(idUserImage);

        NewsCard newsCard1 = new NewsCard();

        newsCard1.setNewsCardKey("random1");
        newsCard1.setNewsCardImageUri(path);
        newsCard1.setVideoTitle("Video Title 2");
        newsCard1.setVideoDescription("This is description 2");
        newsCard1.setIdChannel("g/Nature");
        newsCard1.setTimePosted("2hr");
        newsCard1.setOriginalLink("https://www.washingtonpost.com/world/asia_pacific/north-korea-under-no-circumstances-will-give-up-its-nuclear-weapons/2017/08/07/33b8d319-fbb2-4559-8f7d-25e968913712_story.html?utm_term=.75977c5d6d8f");
        newsCard1.setNewsViews(2);
        newsCard1.setNewsUpvotes(22);
        newsCard1.setNewsDownvotes(22);
        newsCard1.setNewsCommentCount(12);
        newsCard1.setUserId("idRushabh");
        newsCard1.setProfile_image(pathUserImage);
        newsCard1.setUserName("Archana Das");
        newsCard1.setUserPostCount(2);
        newsCard1.setUserUpvotesReceived(42);

        mNewsCardList.add(newsCard1);

        int id2 = mContext.getResources().getIdentifier("sample3", "drawable", mContext.getPackageName());
        String path2 = "android.resource://" + mContext.getPackageName() + "/" + id2;

        NewsCard newsCard2 = new NewsCard();
        newsCard2.setNewsCardKey("random2");
        newsCard2.setNewsCardImageUri(path2);
        newsCard2.setVideoTitle("Video Title 3");
        newsCard2.setVideoDescription("This is description 3");
        newsCard2.setIdChannel("g/Politics");
        newsCard2.setTimePosted("3hr");
        newsCard2.setOriginalLink("https://www.washingtonpost.com/world/asia_pacific/north-korea-under-no-circumstances-will-give-up-its-nuclear-weapons/2017/08/07/33b8d319-fbb2-4559-8f7d-25e968913712_story.html?utm_term=.75977c5d6d8f");
        newsCard2.setNewsViews(3);
        newsCard2.setNewsUpvotes(33);
        newsCard2.setNewsDownvotes(22);
        newsCard2.setNewsCommentCount(12);
        newsCard2.setUserId("idArchana");
        newsCard2.setProfile_image(pathUserImage);
        newsCard2.setUserName("Mahek Shah");
        newsCard2.setUserPostCount(3);
        newsCard2.setUserUpvotesReceived(33);

        mNewsCardList.add(newsCard2);

        mNewsCardSample = new NewsCard();

        int id3 = mContext.getResources().getIdentifier("sample4", "drawable", mContext.getPackageName());
        String path3 = "android.resource://" + mContext.getPackageName() + "/" + id3;

        mNewsCardSample.setNewsCardKey("random3");
        mNewsCardSample.setNewsCardImageUri(path3);
        mNewsCardSample.setVideoTitle("Video Title 4");
        mNewsCardSample.setVideoDescription("This is description 4");
        mNewsCardSample.setIdChannel("g/USA");
        mNewsCardSample.setTimePosted("4hr");
        mNewsCardSample.setOriginalLink("https://www.washingtonpost.com/world/asia_pacific/north-korea-under-no-circumstances-will-give-up-its-nuclear-weapons/2017/08/07/33b8d319-fbb2-4559-8f7d-25e968913712_story.html?utm_term=.75977c5d6d8f");
        mNewsCardSample.setNewsViews(4);
        mNewsCardSample.setNewsUpvotes(37);
        mNewsCardSample.setNewsDownvotes(22);
        mNewsCardSample.setNewsCommentCount(12);
        mNewsCardSample.setUserId("idArchana");
        mNewsCardSample.setProfile_image(pathUserImage);
        mNewsCardSample.setUserName("Sarthik Shah");
        mNewsCardSample.setUserPostCount(4);
        mNewsCardSample.setUserUpvotesReceived(44);

    }

}
