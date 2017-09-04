package com.giffer.giffer.Animation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.giffer.giffer.data.VideoContract;

/**
 * Created by archanaarunkumar on 8/24/17.
 */

public class SlideUpTransformer {

    private float minScale;
    private float maxMinDiff;
    private int translateY;

    public SlideUpTransformer() {
        minScale = 0.8f;
        maxMinDiff = 1-minScale;
        translateY = 0;

    }

    public void transformItem(View thisView, View nextView, float percentage, int viewHeight) {
        float scale = minScale + maxMinDiff * percentage;
        nextView.setScaleX(scale);
        nextView.setScaleY(scale);
        thisView.setTranslationZ(0);
        translateY = Math.round( (viewHeight*(1f - percentage)));
        nextView.setTranslationY(-translateY);
        nextView.setTranslationZ(-1);

    }

    public void endTransform(RecyclerView.LayoutManager layoutManager){
        int itemCount = layoutManager.getChildCount();
        if (itemCount == 0){
            return;
        }
        for(int i =0; i<itemCount;i++){
            final View child = layoutManager.getChildAt(i);
            child.setScaleX(1);
            child.setScaleY(1);
            child.setTranslationY(0);
            child.setTranslationZ(0);
        }

    }

}
