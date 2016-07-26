package com.lizi.shanghaisandtmuseums.Adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class MViewPagerAdapter extends PagerAdapter{
private List<View> mListViews;
    public MViewPagerAdapter(List<View> mListViews){ 
        this.mListViews=mListViews;
    }
    @Override
    public int getCount() {
        return mListViews.size();
    }
    @Override
    public void destroyItem(View v,int index,Object o){
        ((ViewPager) v).removeView(mListViews.get(index));
    }
    @Override
    public Object instantiateItem(View v,int index){
        ((ViewPager)v).addView(mListViews.get(index),0);
        return mListViews.get(index);
    }
    @Override
    public boolean isViewFromObject(View v, Object o) {
        return v==o;
    }
}
