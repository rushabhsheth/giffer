package com.giffer.giffer.Animation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;


/**
 * Created by archanaarunkumar on 8/12/17.
 */

public class GifferSnapHelper extends PagerSnapHelper {

    private OrientationHelper mVerticalHelper, mHorizontalHelper;
    private int gravity;


//    public GifferSnapHelper(int gravity) {
//        if (gravity != Gravity.START && gravity != Gravity.END
//                && gravity != Gravity.BOTTOM && gravity != Gravity.TOP) {
//            throw new IllegalArgumentException("Invalid gravity value. Use START " +
//                    "| END | BOTTOM | TOP constants");
//        }
//        this.gravity = gravity;
//    }
//
//    @Override
//    public void attachToRecyclerView(@Nullable RecyclerView recyclerView)
//            throws IllegalStateException {
//
//        if (recyclerView != null
//                && (!(recyclerView.getLayoutManager() instanceof LinearLayoutManager)
//                || recyclerView.getLayoutManager() instanceof GridLayoutManager)) {
//            throw new IllegalStateException("GravityPagerSnapHelper needs a RecyclerView" +
//                    " with a LinearLayoutManager");
//        }
//
//        super.attachToRecyclerView(recyclerView);
//    }
//
//
//    @Override
//    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager,
//                                              @NonNull View targetView) {
//        int[] out = new int[2];
//
//        if (layoutManager.canScrollHorizontally()) {
//            if (gravity == Gravity.START) {
//                out[0] = distanceToStart(targetView, getHorizontalHelper(layoutManager));
//            } else { // END
//                out[0] = distanceToEnd(targetView, getHorizontalHelper(layoutManager));
//            }
//        } else {
//            out[0] = 0;
//        }
//
//        if (layoutManager.canScrollVertically()) {
//            if (gravity == Gravity.TOP) {
//                out[1] = distanceToStart(targetView, getVerticalHelper(layoutManager));
//            } else { // BOTTOM
//                out[1] = distanceToEnd(targetView, getVerticalHelper(layoutManager));
//            }
//        } else {
//            out[1] = 0;
//        }
//
//        return out;
//    }
//
//    private int distanceToStart(View targetView, OrientationHelper helper) {
//        return helper.getDecoratedStart(targetView) - helper.getStartAfterPadding();
//    }
//
//    private int distanceToEnd(View targetView, OrientationHelper helper) {
//        return helper.getDecoratedEnd(targetView) - helper.getEndAfterPadding();
//    }
//
//
//    @Override
//    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
//        View snapView = null;
//        if (layoutManager instanceof LinearLayoutManager) {
//
//            switch (gravity) {
//                case Gravity.START:
//                    snapView = findStartView(layoutManager, getHorizontalHelper(layoutManager));
//                    break;
//                case Gravity.END:
//                    snapView = findEndView(layoutManager, getHorizontalHelper(layoutManager));
//                    break;
//                case Gravity.TOP:
//                    snapView = findStartView(layoutManager, getVerticalHelper(layoutManager));
//                    break;
//                case Gravity.BOTTOM:
//                    snapView = findEndView(layoutManager, getVerticalHelper(layoutManager));
//                    break;
//            }
//        }
//
//        return snapView;
//    }
//
//    /**
//     * Returns the first view that we should snap to.
//     *
//     * @param layoutManager the recyclerview's layout manager
//     * @param helper        orientation helper to calculate view sizes
//     * @return the first view in the LayoutManager to snap to
//     */
//    private View findStartView(RecyclerView.LayoutManager layoutManager,
//                               OrientationHelper helper) {
//
//        if (layoutManager instanceof LinearLayoutManager) {
//            int firstChild = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
//            int offset = 1;
//
//
//            if (firstChild == RecyclerView.NO_POSITION) {
//                return null;
//            }
//
//            View child = layoutManager.findViewByPosition(firstChild);
//
//            float visibleWidth;
//
//            // We should return the child if it's visible width
//            // is greater than 0.5 of it's total width.
//            // In a RTL configuration, we need to check the start point and in LTR the end point
//
//            visibleWidth = (float) helper.getDecoratedEnd(child)
//                        / helper.getDecoratedMeasurement(child);
//
//            // If we're at the end of the list, we shouldn't snap
//            // to avoid having the last item not completely visible.
//            boolean endOfList = ((LinearLayoutManager) layoutManager)
//                    .findLastCompletelyVisibleItemPosition()
//                    == layoutManager.getItemCount() - 1;
//
//            if (visibleWidth > 0.5f && !endOfList) {
//                return child;
//            } else if (endOfList) {
//                return null;
//            } else {
//                // If the child wasn't returned, we need to return
//                // the next view close to the start.
//                return layoutManager.findViewByPosition(firstChild + 1);
//            }
//        }
//
//        return super.findSnapView(layoutManager);
//    }
//
//    private View findEndView(RecyclerView.LayoutManager layoutManager,
//                             OrientationHelper helper) {
//
//        if (layoutManager instanceof LinearLayoutManager) {
//            int lastChild = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
//            int offset = 1;
//
//            if (lastChild == RecyclerView.NO_POSITION) {
//                return null;
//            }
//
//            View child = layoutManager.findViewByPosition(lastChild);
//
//            float visibleWidth;
//
//
//            visibleWidth = (float) (helper.getTotalSpace() - helper.getDecoratedStart(child))
//                        / helper.getDecoratedMeasurement(child);
//
//            // If we're at the start of the list, we shouldn't snap
//            // to avoid having the first item not completely visible.
//            boolean startOfList = ((LinearLayoutManager) layoutManager)
//                    .findFirstCompletelyVisibleItemPosition() == 0;
//
//            if (visibleWidth > 0.5f && !startOfList) {
//                return child;
//            } else if (startOfList) {
//                return null;
//            } else {
//                // If the child wasn't returned, we need to return the previous view
//                return layoutManager.findViewByPosition(lastChild - 1);
//            }
//        }
//        return super.findSnapView(layoutManager);
//    }
//
//
//    private OrientationHelper getVerticalHelper(RecyclerView.LayoutManager layoutManager) {
//        if (mVerticalHelper == null) {
//            mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
//        }
//        return mVerticalHelper;
//    }
//
//    private OrientationHelper getHorizontalHelper(RecyclerView.LayoutManager layoutManager) {
//        if (mHorizontalHelper == null) {
//            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
//        }
//        return mHorizontalHelper;
//    }

}
