package com.giffer.giffer;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;


/**
 * Created by archanaarunkumar on 8/5/17.
 */

public class VideoCardFragment extends Fragment {

    private MediaController mMediaController;
    private Uri uri;
    private VideoView gifVideoView;

    public VideoCardFragment() {
        //Empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        view.setClipToOutline(true);

        gifVideoView = (VideoView) view.findViewById(R.id.gifVideoView);

//        uri = Uri.parse("http://i.imgur.com/p4N06EX.mp4");

        String videoName = "sample4";
        int id = getResources().getIdentifier(videoName, "raw", getActivity().getPackageName());
        final String path = "android.resource://" + getActivity().getPackageName() + "/" + id;
        uri = Uri.parse(path);

//        mMediaController = new MediaController(getActivity());
//        gifVideoView.setMediaController(mMediaController);
        gifVideoView.setVideoURI(uri);
        gifVideoView.requestFocus();

        gifVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                gifVideoView.start();
            }
        });

//        gifVideoView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
//                if(actionBar.isShowing()) {
//                    actionBar.hide();
//                    getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
//                }
//                else {
//                    actionBar.show();
//                 }
//            }
//        });

        return view;
    }
}
