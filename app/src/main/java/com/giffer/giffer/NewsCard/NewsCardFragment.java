package com.giffer.giffer.NewsCard;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.MediaController;
import android.widget.VideoView;

import com.giffer.giffer.Animation.OverlapDecoration;
import com.giffer.giffer.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by archanaarunkumar on 8/5/17.
 */

public class NewsCardFragment extends Fragment {

    private static final String TAG = "NewsCardFragment";

    private RecyclerView mRecyclerView;
    private NewsCardAdapter mNewsCardAdapter;
    private List<NewsCard> mNewsCardData;
    private android.support.v7.widget.LinearLayoutManager mLayoutManager;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    public NewsCardFragment() {
        //Empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsCardData = new ArrayList<>();
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

        mNewsCardAdapter = new NewsCardAdapter(getContext(),mNewsCardData);
        mRecyclerView.setAdapter(mNewsCardAdapter);
        SnapHelper snapHelper = new PagerSnapHelper();    // LinearSnapHelper();
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
        int id = getResources().getIdentifier("sample1", "drawable", getContext().getPackageName());
        String path = "android.resource://" + getContext().getPackageName() + "/" + id;
        mNewsCardData.add(0, new NewsCard(path, "Video 1", "g/channel 1", "1 h", "Rushabh Sheth"));
//        mNewsCardData.add(1, new NewsCard("sample2", "Video 2", "g/channel 2", "2 h", "Archana Das"));
//        mNewsCardData.add(2, new NewsCard("sample3", "Video 3", "g/channel 3", "3 h", "Sarthik Shah"));
//        mNewsCardData.add(3, new NewsCard("sample4", "Video 4", "g/channel 4", "4 h", "Mahek Shah"));
//        mNewsCardData.add(4, new NewsCard("sample5", "Video 5", "g/channel 5", "5 h", "Pratibha"));
    }

    public void addNewsCard(NewsCard newsCard){
        mNewsCardData.add(newsCard);
        mNewsCardAdapter.notifyItemInserted(mNewsCardData.size() - 1);
    }

}


