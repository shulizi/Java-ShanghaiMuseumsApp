package com.lizi.shanghaisandtmuseums.mview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MViewPager extends ViewPager {
	public MViewPager(Context context) {
		super(context);
	}
	public MViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return false;
    }

}
