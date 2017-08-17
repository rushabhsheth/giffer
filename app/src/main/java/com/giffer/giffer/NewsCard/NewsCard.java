package com.giffer.giffer.NewsCard;


import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by archanaarunkumar on 8/10/17.
 */

public class NewsCard  implements Serializable{

    private static final String TAG = "NewsCard";

    private String newsCardKey;
    private String newsCardImageUri;
    private String videoTitle;
    private String videoDescription;
    private String idChannel;
    private String timePosted;
    private String originalLink;
    private int newsViews;
    private int newsUpvotes;
    private int newsDownvotes;
    private int newsCommentCount;

    private String userId;
    private String profile_image;
    private String userName;
    private int userPostCount;
    private int userUpvotesReceived;

    //Empty Constructor
    public NewsCard(){
        this.newsCardKey = "random";
        this.newsCardImageUri = "Something";
        this.videoTitle = null;
        this.videoDescription = null;
        this.idChannel = null;
        this.timePosted = null;
        this.originalLink = null;

        this.newsViews = 404;
        this.newsUpvotes = 404;
        this.newsDownvotes = 404;
        this.newsCommentCount = 404;

        this.userId = null;
        this.profile_image = null;
        this.userName = null;
        this.userPostCount = 404;
        this.userUpvotesReceived = 404;

    }


    public NewsCard(String gifImageView, String videoTitle, String idChannel, String timePosted, String userName){
        this.newsCardImageUri = gifImageView;
        this.videoTitle = videoTitle;
        this.videoDescription = null;
        this.idChannel = idChannel;
        this.timePosted = timePosted;
        this.originalLink = null;

        this.newsViews = 404;
        this.newsUpvotes = 404;
        this.newsDownvotes = 404;
        this.newsCommentCount = 404;

        this.userId = null;
        this.profile_image = null;
        this.userName = userName;
        this.userPostCount = 404;
        this.userUpvotesReceived = 404;
    }

    public String getNewsCardKey(){
        return newsCardKey;
    }

    public String getIdChannel() {
        return idChannel;
    }

    public String getOriginalLink() {
        return originalLink;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public String getTimePosted() {
        return timePosted;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserPostCount() {
        return userPostCount;
    }

    public int getUserUpvotesReceived() {
        return userUpvotesReceived;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public int getNewsViews(){
        return newsViews;
    }

    public int getNewsUpvotes(){
        return newsUpvotes;
    }

    public int getNewsDownvotes(){
        return newsDownvotes;
    }

    public int getNewsCommentCount(){
        return newsCommentCount;
    }

    public String getUserId(){
        return userId;
    }

    public String getNewsCardImageUri(){
        return newsCardImageUri;
    }


    //Setter functions
    public void setNewsCardKey (String newsCardKey){
        this.newsCardKey = newsCardKey;
    }

    public void setVideoTitle(String videoTitle){
        this.videoTitle = videoTitle;
    }

    public void setVideoDescription(String videoDescription){
        this.videoDescription = videoDescription;
    }

    public void setIdChannel(String idChannel){
        this.idChannel =idChannel;
    }

    public void setTimePosted(String timePosted){
        this.timePosted = timePosted;
    }

    public void setOriginalLink(String originalLink){
        this.originalLink =originalLink;
    }

    public void setUserName (String userName){
        this.userName = userName;
    }

    public void setProfile_image(String profile_image){
        this.profile_image = profile_image;
    }

    public void setUserPostCount(int userPostCount){
        this.userPostCount = userPostCount;
    }

    public void setUserUpvotesReceived(int userUpvotesReceived){
        this.userUpvotesReceived = userUpvotesReceived;
    }

    public void setNewsViews(int newsViews){
        this.newsViews = newsViews;
    }

    public void setNewsUpvotes(int newsUpvotes){
        this.newsUpvotes = newsUpvotes;
    }

    public void setNewsDownvotes(int newsDownvotes){
        this.newsDownvotes = newsDownvotes;
    }

    public void setNewsCommentCount(int newsCommentCount){
        this.newsCommentCount = newsCommentCount;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public void setNewsCardImageUri(String newsCardImageUri){
//        Log.d(TAG, newsCardImageUri);
        this.newsCardImageUri = newsCardImageUri;
    }
}
