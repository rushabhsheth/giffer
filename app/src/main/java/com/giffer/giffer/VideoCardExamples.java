package com.giffer.giffer;


import android.content.Context;
import android.content.res.Resources;

/**
 * Created by archanaarunkumar on 8/10/17.
 */

public class VideoCardExamples {

    private String gifImageView;
    private String videoTitle;
    private String videoDescription;
    private String idChannel;
    private String timePosted;
    private String originalLink;
    private String profile_image;
    private String userName;
    private String userPostCount;
    private String userUpvotesReceived;


    public VideoCardExamples(Context context, String gifImageView, String videoTitle, String idChannel, String timePosted, String userName){
        this.gifImageView = gifImageView;
        this.videoTitle = videoTitle;
        this.idChannel = idChannel;
        this.timePosted = timePosted;
        this.userName = userName;

        this.videoDescription = "Some text here";
        this.originalLink = "<a href='http://wapo.st/2uikFTQ'> Washington Post </a>";
        this.profile_image = "rushabh_profile";
        this.userPostCount = "42 Posts";
        this.userUpvotesReceived = "42k Upvotes";
    }

    public String getGifImageView() {
        return gifImageView;
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

    public String getUserPostCount() {
        return userPostCount;
    }

    public String getUserUpvotesReceived() {
        return userUpvotesReceived;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public String getVideoTitle() {
        return videoTitle;
    }
}
