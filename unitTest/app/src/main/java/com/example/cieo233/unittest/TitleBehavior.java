package com.example.cieo233.unittest;

import android.content.Context;
import android.media.audiofx.LoudnessEnhancer;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.roughike.swipeselector.SwipeSelector;

/**
 * Created by Cieo233 on 12/27/2016.
 */

public class TitleBehavior extends CoordinatorLayout.Behavior<TextView> {
    private float swipeBottom = 0;



    public TitleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, TextView child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, TextView child, View target, int dx, int dy, int[] consumed) {
        swipeBottom = target.getScrollY();
        child.setY(child.getY() - swipeBottom);
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, TextView child, View target, float velocityX, float velocityY) {
        return true;
    }
}
