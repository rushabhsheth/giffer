package com.giffer.giffer.NewsCard;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.giffer.giffer.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by archanaarunkumar on 8/5/17.
 */

public class VideoCardFragment extends Fragment {

    private static final String TAG = "VideoCardFragment";

    protected RecyclerView mRecyclerView;
    protected VideoCardAdapter mAdapter;
    protected android.support.v7.widget.LinearLayoutManager mLayoutManager;
    protected List<VideoCardExamples> mDataset = new ArrayList<>();

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    private MediaController mMediaController;
    private VideoView gifVideoView;


    public VideoCardFragment() {
        //Empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        rootView.setTag(TAG);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_recyclerview);

        int scrollPosition = 0;
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);

        mAdapter = new VideoCardAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            Log.v("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                        }
                    }
                }
            }
        });

        return rootView;
    }

    private void initDataset() {

        mDataset.add(0, new VideoCardExamples(getActivity(), "sample1", "Video 1", "g/channel 1", "1 h", "Rushabh Sheth"));
        mDataset.add(1, new VideoCardExamples(getActivity(), "sample2", "Video 2", "g/channel 2", "2 h", "Archana Das"));
        mDataset.add(2, new VideoCardExamples(getActivity(), "sample3", "Video 3", "g/channel 3", "3 h", "Sarthik Shah"));
        mDataset.add(3, new VideoCardExamples(getActivity(), "sample4", "Video 4", "g/channel 4", "4 h", "Mahek Shah"));
        mDataset.add(4, new VideoCardExamples(getActivity(), "sample5", "Video 5", "g/channel 5", "5 h", "Pratibha"));
    }

}


