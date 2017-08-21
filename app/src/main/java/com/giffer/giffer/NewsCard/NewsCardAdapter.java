package com.giffer.giffer.NewsCard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.giffer.giffer.AddNewsActivity;
import com.giffer.giffer.R;
import com.giffer.giffer.SelectChannelActivity;
import com.giffer.giffer.WebViewActivity;

import java.util.List;

/**
 * Created by archanaarunkumar on 8/9/17.
 */

public class NewsCardAdapter extends RecyclerView.Adapter<NewsCardAdapter.ViewHolder> {

    private static final String TAG = "NewsCardAdapter";

    private List<NewsCard> mDataSet;
    private int lastPosition = -1;

    Context mContext;
    String mOriginalLink;
    String mOriginalLinkText;

    //Viewholder to hold item_main
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView gifImageView;
        private final TextView videoTitle;
        private final TextView videoDescription;
        private final TextView idChannel;
        private final TextView timePosted;
        private final TextView originalLink;
        private final de.hdodenhof.circleimageview.CircleImageView profile_image;
        private final TextView userName;
        private final TextView userPostCount;
        private final TextView userUpvotesReceived;

        private final ImageButton mUpvoteButton;
        private final ImageButton mDownvoteButton;
        private final ImageButton mCommentButton;
        private final ImageButton mShareButton;
        private final ImageButton mBookmarkButton;


        public ViewHolder(View v) {
            super(v);

            //Clip view to drawable with rounded corners
            v.setClipToOutline(true);

            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");

                    final Activity host = (Activity) v.getContext();
                    final ActionBar actionBar = ((AppCompatActivity)host).getSupportActionBar();

                    if(actionBar.isShowing()) {
                        actionBar.hide();
                    }
                    else {
                        actionBar.show();
                        // time delay to hide actionBar
                        Handler h = new Handler();
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // DO DELAYED STUFF
                                actionBar.hide();
//                                host.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                            }
                        }, 5000); // e.g. 5000 milliseconds
                    }

                }
            });

            gifImageView = (ImageView) v.findViewById(R.id.gifImageView);
            videoTitle = (TextView) v.findViewById(R.id.videoTitle);
            videoDescription = (TextView) v.findViewById(R.id.videoDescription);
            idChannel = (TextView) v.findViewById(R.id.idChannel);
            timePosted = (TextView) v.findViewById(R.id.timePosted);
            originalLink = (TextView) v.findViewById(R.id.originalLink);
            profile_image = (de.hdodenhof.circleimageview.CircleImageView) v.findViewById(R.id.profile_image);
            userName = (TextView) v.findViewById(R.id.userName);
            userPostCount = (TextView) v.findViewById(R.id.userPostCount);
            userUpvotesReceived = (TextView) v.findViewById(R.id.userUpvotesReceived);

            mUpvoteButton = (ImageButton) v.findViewById(R.id.UpvoteButton);
            mDownvoteButton = (ImageButton) v.findViewById(R.id.DownvoteButton);
            mCommentButton = (ImageButton) v.findViewById(R.id.CommentButton);
            mShareButton = (ImageButton) v.findViewById(R.id.ShareButton);
            mBookmarkButton = (ImageButton) v.findViewById(R.id.BookmarkButton);
        }

        public ImageView getGifImageView()

        {
            return gifImageView;
        }

        public TextView getVideoTitle() {
            return videoTitle;
        }

        public TextView getVideoDescription()

        {
            return videoDescription;
        }

        public TextView getIdChannel()

        {
            return idChannel;
        }

        public TextView getTimePosted()

        {
            return timePosted;
        }

        public TextView getOriginalLink()

        {
            return originalLink;
        }

        public de.hdodenhof.circleimageview.CircleImageView getProfile_image()

        {
            return profile_image;
        }

        public TextView getUserName()

        {
            return userName;
        }

        public TextView getUserPostCount()

        {
            return userPostCount;
        }

        public TextView getUserUpvotesReceived()

        {
            return userUpvotesReceived;
        }

        public ImageButton getUpvoteButton (){
            return mUpvoteButton;
        }

        public ImageButton getDownvoteButton(){
            return mDownvoteButton;
        }

        public ImageButton getCommentButton(){
            return mCommentButton;
        }

        public ImageButton getShareButton(){
            return mShareButton;
        }

        public ImageButton getBookmarkButton(){
            return mBookmarkButton;
        }

    }

    //Constructor
    public NewsCardAdapter(Context context,List<NewsCard> dataSet) {
        this.mDataSet = dataSet;
        this.mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_main, viewGroup, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        final ViewHolder finalHolder = viewHolder;
        Context context = finalHolder.itemView.getContext();
        Resources res = finalHolder.itemView.getContext().getResources();

        final NewsCard newsCard = mDataSet.get(position);

        String imageUri = newsCard.getNewsCardImageUri();
        if (imageUri != null) {
            Uri uri = Uri.parse(imageUri);
            Glide.with(context)
                    .load(uri)
                    .into(finalHolder.getGifImageView());
        }

//        finalHolder.getOriginalLink().setClickable(true);
//        finalHolder.getOriginalLink().setMovementMethod(LinkMovementMethod.getInstance());

        if(newsCard.getOriginalLink()!=null){
            mOriginalLink = newsCard.getOriginalLink();
            mOriginalLinkText = "Washington Post";
            //noinspection deprecation
            finalHolder.getOriginalLink().setText(mOriginalLinkText);
        }
        else {
            mOriginalLink = "https://www.washingtonpost.com/world/asia_pacific/north-korea-under-no-circumstances-will-give-up-its-nuclear-weapons/2017/08/07/33b8d319-fbb2-4559-8f7d-25e968913712_story.html?utm_term=.75977c5d6d8f";

            mOriginalLinkText = "Washington Post";
            //noinspection deprecation
            finalHolder.getOriginalLink().setText(mOriginalLinkText);
        }

        finalHolder.getOriginalLink().setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext,WebViewActivity.class);
                        intent.putExtra("LinkURL",mOriginalLink);
                        intent.putExtra("LinkText",mOriginalLinkText);
                        mContext.startActivity(intent);
                    }
                }
        );

        String videoTitle = newsCard.getVideoTitle();
        if(videoTitle!=null){
            finalHolder.getVideoTitle().setText(videoTitle);
        }

        String videoDescription = newsCard.getVideoDescription();
        if(videoDescription!=null){
            finalHolder.getVideoDescription().setText(videoDescription);
        }

        String channelId = newsCard.getIdChannel();
        if(channelId != null){
            finalHolder.getIdChannel().setText(channelId);
        }

        String timePosted = newsCard.getTimePosted();
        if(timePosted != null){
            finalHolder.getTimePosted().setText(timePosted);
        }

        String userName = newsCard.getUserName();
        if(userName != null){
            finalHolder.getUserName().setText(userName);
        }

        String userProfileImage = newsCard.getProfile_image();
        if(userProfileImage!=null){
            Uri uri = Uri.parse(userProfileImage);
            Glide.with(context)
                    .load(uri)
                    .into(finalHolder.getProfile_image());
        }

        int userPosts = newsCard.getUserPostCount();
    //    Log.d(TAG,String.valueOf(userPosts));
        if(userPosts != 404){

            finalHolder.getUserPostCount().setText(String.valueOf(userPosts));
        }

        int userUpvotes = newsCard.getUserUpvotesReceived();
        if(userUpvotes!=404){
     //       Log.d(TAG, String.valueOf(userUpvotes));
            finalHolder.getUserUpvotesReceived().setText(String.valueOf(userUpvotes));
        }


        finalHolder.getUpvoteButton().setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean upvotePressed = newsCard.getUpvotePressed();
                        if (!upvotePressed) {
                            finalHolder.getUpvoteButton().setImageResource(R.mipmap.uparrow_pressed);
                            newsCard.setUpvotePressed(true);
                        }
                        else {
                            finalHolder.getUpvoteButton().setImageResource(R.mipmap.upvote);
                            newsCard.setUpvotePressed(false);
                        }
                    }
                }
        );

        finalHolder.getDownvoteButton().setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean downvotePressed = newsCard.getDownvotePressed();
                        if (!downvotePressed) {
                            finalHolder.getDownvoteButton().setImageResource(R.mipmap.downarrow_pressed);
                            newsCard.setDownvotePressed(true);
                        }
                        else {
                            finalHolder.getDownvoteButton().setImageResource(R.mipmap.downvote);
                            newsCard.setDownvotePressed(false);
                        }
                    }
                }
        );

        finalHolder.getShareButton().setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean sharePressed = newsCard.getSharePressed();
                        if (!sharePressed) {
                            finalHolder.getShareButton().setImageResource(R.mipmap.share_pressed);
                            newsCard.setSharePressed(true);
                        }
                        else {
                            finalHolder.getShareButton().setImageResource(R.mipmap.share);
                            newsCard.setSharePressed(false);
                        }
                    }
                }
        );

        finalHolder.getBookmarkButton().setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean bookmarkPressed = newsCard.getBookmarkPressed();
                        if (!bookmarkPressed) {
                            finalHolder.getBookmarkButton().setImageResource(R.mipmap.bookmark_pressed);
                            newsCard.setBookmarkPressed(true);
                        }
                        else {
                            finalHolder.getBookmarkButton().setImageResource(R.mipmap.bookmark);
                            newsCard.setBookmarkPressed(false);
                        }
                    }
                }
        );


        // setSlideUpAnimation(context, finalHolder.itemView, position);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void addItem(NewsCard dataObj, int index) {
        mDataSet.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataSet.remove(index);
        notifyItemRemoved(index);
    }


    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

//    private void setSlideUpAnimation(Context context, View viewToAnimate, int position) {
//        // If the bound view wasn't previously displayed on screen, it's animated
//        if (position > lastPosition) {
//            Animation animation = AnimationUtils.loadAnimation(context,R.anim.item_animation_slide_up);
//            viewToAnimate.startAnimation(animation);
//            lastPosition = position;
//        }
//        else
//        {
//            Animation animation = AnimationUtils.loadAnimation(context,R.anim.item_animation_slide_down);
//            viewToAnimate.startAnimation(animation);
//            lastPosition = position;
//        }
//    }
//

}

