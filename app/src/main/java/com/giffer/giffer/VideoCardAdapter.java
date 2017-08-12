package com.giffer.giffer;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by archanaarunkumar on 8/9/17.
 */

public class VideoCardAdapter extends RecyclerView.Adapter<VideoCardAdapter.ViewHolder> {

    private static final String TAG = "VideoCardAdapter";

    private List<VideoCardExamples> mDataSet;

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
//                        host.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
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
    }

    //Constructor
    public VideoCardAdapter(List<VideoCardExamples> dataSet) {
        mDataSet = dataSet;
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
        //        uri = Uri.parse("http://i.imgur.com/p4N06EX.mp4");

        String imageName = mDataSet.get(position).getGifImageView();

        int id = res.getIdentifier(imageName, "drawable", context.getPackageName());
        final String path = "android.resource://" + context.getPackageName() + "/" + id;
        Uri uri = Uri.parse(path);

        Glide.with(context)
                .load(uri)
                .into(finalHolder.getGifImageView());


        finalHolder.getOriginalLink().setClickable(true);
        finalHolder.getOriginalLink().setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='http://wapo.st/2uikFTQ'> Washington Post </a>";
        //noinspection deprecation
        finalHolder.getOriginalLink().setText(Html.fromHtml(text));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void addItem(VideoCardExamples dataObj, int index) {
        mDataSet.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataSet.remove(index);
        notifyItemRemoved(index);
    }


}

