package com.giffer.giffer.Animation;


import android.graphics.PointF;
import android.icu.text.LocaleDisplayNames;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

/**
 * Created by archanaarunkumar on 8/12/17.
 */



public class GifferSnapHelper extends SnapHelper {

    private static final String TAG = "GifferSnapHelper";

    private static final float MILLISECONDS_PER_INCH_GIFFER = 25f;

    private static final int MAX_SCROLL_ON_FLING_DURATION = 100; // ms

    RecyclerView mmRecyclerView;
    RecyclerView.LayoutManager mmLayoutManager;

    SlideUpTransformer mSlideUpTransformer;

    // Orientation helpers are lazily created per LayoutManager.
    @Nullable
    private OrientationHelper mVerticalHelper;
    @Nullable
    private OrientationHelper mHorizontalHelper;

    public GifferSnapHelper(){
        mSlideUpTransformer = new SlideUpTransformer();
    }

    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView)
            throws IllegalStateException {
        super.attachToRecyclerView(recyclerView);

        if (mmRecyclerView == recyclerView) {
            return; // nothing to do
        }
        if (mmRecyclerView != null) {
            destroyGifferCallbacks();
        }
        mmRecyclerView = recyclerView;
        if (mmRecyclerView != null) {
            setupGifferCallbacks();
        }
    }

    @Nullable
    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager,
                                              @NonNull View targetView) {
        int[] out = new int[2];
        if (layoutManager.canScrollHorizontally()) {
            out[0] = distanceToCenter(layoutManager, targetView,
                    getHorizontalHelper(layoutManager));
        } else {
            out[0] = 0;
        }

        if (layoutManager.canScrollVertically()) {
            out[1] = distanceToCenter(layoutManager, targetView,
                    getVerticalHelper(layoutManager));
        } else {
            out[1] = 0;
        }
        return out;
    }

    @Nullable
    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager.canScrollVertically()) {
            return findCenterView(layoutManager, getVerticalHelper(layoutManager));
        } else if (layoutManager.canScrollHorizontally()) {
            return findCenterView(layoutManager, getHorizontalHelper(layoutManager));
        }
        return null;
    }

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX,
                                      int velocityY) {
        final int itemCount = layoutManager.getItemCount();
        if (itemCount == 0) {
            return RecyclerView.NO_POSITION;
        }

        View mStartMostChildView = null;
        if (layoutManager.canScrollVertically()) {
            mStartMostChildView = findStartView(layoutManager, getVerticalHelper(layoutManager));
        } else if (layoutManager.canScrollHorizontally()) {
            mStartMostChildView = findStartView(layoutManager, getHorizontalHelper(layoutManager));
        }

        if (mStartMostChildView == null) {
            return RecyclerView.NO_POSITION;
        }
        final int centerPosition = layoutManager.getPosition(mStartMostChildView);
        if (centerPosition == RecyclerView.NO_POSITION) {
            return RecyclerView.NO_POSITION;
        }

        final boolean forwardDirection;
        if (layoutManager.canScrollHorizontally()) {
            forwardDirection = velocityX > 0;
        } else {
            forwardDirection = velocityY > 0;
        }
        boolean reverseLayout = false;
        if ((layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            RecyclerView.SmoothScroller.ScrollVectorProvider vectorProvider =
                    (RecyclerView.SmoothScroller.ScrollVectorProvider) layoutManager;
            PointF vectorForEnd = vectorProvider.computeScrollVectorForPosition(itemCount - 1);
            if (vectorForEnd != null) {
                reverseLayout = vectorForEnd.x < 0 || vectorForEnd.y < 0;
            }
        }
        return reverseLayout
                ? (forwardDirection ? centerPosition - 1 : centerPosition)
                : (forwardDirection ? centerPosition + 1 : centerPosition);
    }

    @Override
    protected LinearSmoothScroller createSnapScroller(RecyclerView.LayoutManager layoutManager) {
        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            return null;
        }
        return new LinearSmoothScroller(mmRecyclerView.getContext()) {
            @Override
            protected void onTargetFound(View targetView, RecyclerView.State state, Action action) {
                int[] snapDistances = calculateDistanceToFinalSnap(mmRecyclerView.getLayoutManager(),
                        targetView);
                final int dx = snapDistances[0];
                final int dy = snapDistances[1];
                //Log.d(TAG,"Distance to center of next view: "+ String.valueOf(dy) );
                final int time = calculateTimeForDeceleration(Math.max(Math.abs(dx), Math.abs(dy)));
                if (time > 0) {
                    action.update(dx, dy, time, mDecelerateInterpolator);
                }

            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH_GIFFER / displayMetrics.densityDpi;
            }

            @Override
            protected int calculateTimeForScrolling(int dx) {
                return Math.min(MAX_SCROLL_ON_FLING_DURATION, super.calculateTimeForScrolling(dx));
            }
        };
    }

    private int distanceToCenter(@NonNull RecyclerView.LayoutManager layoutManager,
                                 @NonNull View targetView, OrientationHelper helper) {
        final int childCenter = helper.getDecoratedStart(targetView)
                + (helper.getDecoratedMeasurement(targetView) / 2);
        final int containerCenter;
        if (layoutManager.getClipToPadding()) {
            containerCenter = helper.getStartAfterPadding() + helper.getTotalSpace() / 2;
        } else {
            containerCenter = helper.getEnd() / 2;
        }
        return childCenter - containerCenter;
    }

    /**
     * Return the child view that is currently closest to the center of this parent.
     *
     * @param layoutManager The {@link RecyclerView.LayoutManager} associated with the attached
     *                      {@link RecyclerView}.
     * @param helper The relevant {@link OrientationHelper} for the attached {@link RecyclerView}.
     *
     * @return the child view that is currently closest to the center of this parent.
     */
    @Nullable
    private View findCenterView(RecyclerView.LayoutManager layoutManager,
                                OrientationHelper helper) {
        int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return null;
        }

        View closestChild = null;
        final int center;
        if (layoutManager.getClipToPadding()) {
            center = helper.getStartAfterPadding() + helper.getTotalSpace() / 2;
        } else {
            center = helper.getEnd() / 2;
        }
        int absClosest = Integer.MAX_VALUE;

        for (int i = 0; i < childCount; i++) {
            final View child = layoutManager.getChildAt(i);
            int childCenter = helper.getDecoratedStart(child)
                    + (helper.getDecoratedMeasurement(child) / 2);
            int absDistance = Math.abs(childCenter - center);

            /** if child center is closer than previous closest, set it as closest  **/
            if (absDistance < absClosest) {
                absClosest = absDistance;
                closestChild = child;
            }
        }
        return closestChild;
    }

    /**
     * Return the child view that is currently closest to the start of this parent.
     *
     * @param layoutManager The {@link RecyclerView.LayoutManager} associated with the attached
     *                      {@link RecyclerView}.
     * @param helper The relevant {@link OrientationHelper} for the attached {@link RecyclerView}.
     *
     * @return the child view that is currently closest to the start of this parent.
     */
    @Nullable
    private View findStartView(RecyclerView.LayoutManager layoutManager,
                               OrientationHelper helper) {
        int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return null;
        }

        View closestChild = null;
        int startest = Integer.MAX_VALUE;

        for (int i = 0; i < childCount; i++) {
            final View child = layoutManager.getChildAt(i);
            int childStart = helper.getDecoratedStart(child);

            /** if child is more to start than previous closest, set it as closest  **/
            if (childStart < startest) {
                startest = childStart;
                closestChild = child;
            }
        }
        return closestChild;
    }

    @NonNull
    private OrientationHelper getVerticalHelper(@NonNull RecyclerView.LayoutManager layoutManager) {
        if (mVerticalHelper == null) {
            mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
        }
        return mVerticalHelper;
    }

    @NonNull
    private OrientationHelper getHorizontalHelper(
            @NonNull RecyclerView.LayoutManager layoutManager) {
        if (mHorizontalHelper == null) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return mHorizontalHelper;
    }


    // Handles the transformation on scroll case.
    private final RecyclerView.OnScrollListener mmScrollListener =
            new RecyclerView.OnScrollListener() {
                boolean mScrolling = false;

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        //mScrolling = false;

                    }

                    if(newState == RecyclerView.SCROLL_STATE_IDLE && mScrolling){
                        mScrolling = false;
                        endTransformation();
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dx != 0 || dy != 0) {
                        mScrolling = true;
                        //Log.d(TAG, "Dragging ");
                        transformNextView();

                    }
                }
            };


    void transformNextView() {

        int thisPosition = 0;
        int nextPosition = 1;//findTargetSnapPositionGiffer(layoutManager);

        if (nextPosition == RecyclerView.NO_POSITION) {
            return;
        }

        if (thisPosition == RecyclerView.NO_POSITION) {
            return;
        }

        //Log.d(TAG,"Next View is : "+String.valueOf(layoutManager.getChildCount()) );

        View mNextView = mmLayoutManager.getChildAt(nextPosition);
        if (mNextView == null) {
            //Log.d(TAG, "Next Views is NULL");
            return;
        }

        View mThisView = mmLayoutManager.getChildAt(thisPosition);
        if (mThisView == null) {
            //Log.d(TAG, "Next Views is NULL");
            return;
        }

        int[] snapDistance = calculateDistanceToFinalSnap(mmLayoutManager, mNextView);
        int viewHeight = mNextView.getHeight();
        float percentage = 1f- (((float) snapDistance[1])/((float) viewHeight));

        if (snapDistance[0] != 0 || snapDistance[1] != 0) {
            mSlideUpTransformer.transformItem(mThisView, mNextView, percentage,viewHeight);
        }

    }

    void endTransformation(){
        mSlideUpTransformer.endTransform(mmLayoutManager);
    }

    /**
     * Called when the instance of a {@link RecyclerView} is detached.
     */
    private void destroyGifferCallbacks() {
        mmRecyclerView.removeOnScrollListener(mmScrollListener);
        // mmRecyclerView.setOnFlingListener(null);
    }

    private void setupGifferCallbacks() throws IllegalStateException {
        mmRecyclerView.addOnScrollListener(mmScrollListener);
        mmLayoutManager = mmRecyclerView.getLayoutManager();
    }



//    public int findTargetSnapPositionGiffer(RecyclerView.LayoutManager layoutManager){
//        final int itemCount = layoutManager.getItemCount();
//        if (itemCount == 0) {
//            return RecyclerView.NO_POSITION;
//        }
//
//        View mStartMostChildView = null;
//        if (layoutManager.canScrollVertically()) {
//            mStartMostChildView = findStartView(layoutManager, getVerticalHelper(layoutManager));
//        } else if (layoutManager.canScrollHorizontally()) {
//            mStartMostChildView = findStartView(layoutManager, getHorizontalHelper(layoutManager));
//        }
//
//        if (mStartMostChildView == null) {
//            return RecyclerView.NO_POSITION;
//        }
//        final int centerPosition = layoutManager.getPosition(mStartMostChildView);
//        if (centerPosition == RecyclerView.NO_POSITION) {
//            return RecyclerView.NO_POSITION;
//        }
//
//        boolean reverseLayout = false;
//        if ((layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
//            RecyclerView.SmoothScroller.ScrollVectorProvider vectorProvider =
//                    (RecyclerView.SmoothScroller.ScrollVectorProvider) layoutManager;
//            PointF vectorForEnd = vectorProvider.computeScrollVectorForPosition(itemCount - 1);
//            if (vectorForEnd != null) {
//                reverseLayout = vectorForEnd.x < 0 || vectorForEnd.y < 0;
//            }
//        }
//        return reverseLayout
//                ? (centerPosition - 1)
//                : (centerPosition + 1);
//    }


}