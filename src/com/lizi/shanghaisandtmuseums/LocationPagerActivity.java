package com.lizi.shanghaisandtmuseums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapView;
import com.lizi.shanghaisandtmuseums.adapter.MLocationListAdapter;
import com.lizi.shanghaisandtmuseums.adapter.MViewPagerAdapter;
import com.lizi.shanghaisandtmuseums.utils.ConfigUtil;

@SuppressLint("NewApi") public class LocationPagerActivity extends Activity{
	 MapView mMapView = null;  
	 private RadioButton radio0;
	 private RadioButton radio1;
	 private ViewPager mPager;
	    @Override  
	    protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);   
	        SDKInitializer.initialize(getApplicationContext()); 
	        setContentView(R.layout.activity_pager_location);
	        
	        ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setTitle(ConfigUtil.MUSEUM_INTRODUCTION);
			
	        radio0=(RadioButton) findViewById(R.id.radio0);
	        radio1=(RadioButton) findViewById(R.id.radio1);
	        
	        Drawable[] drawables = radio0.getCompoundDrawables();
	        drawables[1].setBounds(0,0,70,70);
	        radio0.setCompoundDrawables(drawables[0],drawables[1],drawables[2],drawables[3]);
	        drawables = radio1.getCompoundDrawables();
	        drawables[1].setBounds(0,0,70,70);
	        radio1.setCompoundDrawables(drawables[0],drawables[1],drawables[2],drawables[3]);
	        
			LayoutInflater lInflater = getLayoutInflater();
			ViewGroup locationLayout = (ViewGroup) lInflater.inflate(R.layout.pager_location,null);
			ViewGroup mapLayout = (ViewGroup) lInflater.inflate(R.layout.pager_map,null);
			 
	        mMapView = (MapView) mapLayout.findViewById(R.id.bmapView);  
	        mMapView.setLogoPosition(LogoPosition.logoPostionleftTop);
	        mMapView.setZoomControlsPosition(new Point(100,100));
	        mMapView.showZoomControls(false);;
	        
	        ListView locationList = (ListView) locationLayout.findViewById(R.id.lv_location);
	        ArrayList<HashMap<String, Object>> newsList = new ArrayList<HashMap<String,Object>>();
	        for(int i=0;i<ConfigUtil.SHANGHAI_DISTRICTS.length;i++){
	        	HashMap<String, Object> map = new HashMap<String, Object>();
	        	map.put(ConfigUtil.SHANGHAI_DISTRICTS[i], ConfigUtil.COLORS[i%ConfigUtil.COLORS.length]);
	        	newsList.add(map);
	        	
	        }
	        ListAdapter  listAdapter= new MLocationListAdapter(this, newsList);
	        locationList.setAdapter(listAdapter);
	        
	        
	        List<View> pagerListViews = new ArrayList<View>();
			pagerListViews
					.add(locationLayout);
			pagerListViews.add(mapLayout);
	        mPager = (ViewPager) findViewById(R.id.vp_location);
	        mPager.setAdapter(new MViewPagerAdapter(pagerListViews));
	        mPager.setOnTouchListener(null);
			mPager.setCurrentItem(0);
	        
	    }  
		public void locationRadioClick(View v){
			mPager.setCurrentItem(0);
			
		}
		public void mapRadioClick(View v){
			mPager.setCurrentItem(1);
			
		}
		
	    @Override  
	    protected void onDestroy() {  
	        super.onDestroy();  
	        mMapView.onDestroy();  
	    }  
	    @Override  
	    protected void onResume() {  
	        super.onResume();  
	        mMapView.onResume();  
	        }  
	    @Override  
	    protected void onPause() {  
	        super.onPause();  
	        mMapView.onPause();  
	        } 
	    @Override
		public boolean onCreateOptionsMenu(Menu menu) {

			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.location_menu, menu);
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.action_search:
				startActivity(new Intent(this, SearchListActivity.class));
				break;
			case R.id.action_home:
				startActivity(new Intent(this, CategoryPagerActivity.class));
				break;
			case android.R.id.home:
				finish();
				break;
			case R.id.action_location:
				startActivity(new Intent(this,LocationPagerActivity.class));
				break;
			default:
				break;
			}
			return super.onOptionsItemSelected(item);
		}
}
