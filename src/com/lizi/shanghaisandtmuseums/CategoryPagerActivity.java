package com.lizi.shanghaisandtmuseums;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lizi.shanghaisandtmuseums.Adapter.MViewPagerAdapter;
import com.lizi.shanghaisandtmuseums.utils.ConfigUtil;

@SuppressLint("NewApi") public class CategoryPagerActivity extends Activity {
	private ViewPager mPager;
	private List<View> listViews;
	private TextView tvTitle;
	private String[] strTitles = { ConfigUtil.FOUNDATION_MUSEUM,
			ConfigUtil.COMPREHENSIVE_MUSEUM, ConfigUtil.THEME_MUSEUM };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pager_category);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);

		LayoutInflater lInflater = getLayoutInflater();
		mPager = (ViewPager) findViewById(R.id.vp_main);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText(strTitles[0]);

		listViews = new ArrayList<View>();
		listViews
				.add(lInflater.inflate(R.layout.pager_foundation_museum, null));
		listViews.add(lInflater.inflate(R.layout.pager_comprehensive_museum,
				null));
		listViews.add(lInflater.inflate(R.layout.pager_theme_museum, null));
		mPager.setAdapter(new MViewPagerAdapter(listViews));
		mPager.setOnPageChangeListener(MOnPagerChangeListener);
		mPager.setCurrentItem(0);
	}

	public void foundationClick(View view) {
//		Toast.makeText(this, "foundationClick", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent();
		intent.putExtra("info", ConfigUtil.FOUNDATION_MUSEUM);
		intent.setClass(this, MuseumsListActivity.class);
		this.startActivity(intent);
	};

	public void comprehensiveClick(View view) {
//		Toast.makeText(this, "comprehensiveClick", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent();
		intent.setClass(this, MuseumsListActivity.class);
		intent.putExtra("info", ConfigUtil.COMPREHENSIVE_MUSEUM);
		this.startActivity(intent);
	};

	public void themeClick(View view) {
//		Toast.makeText(this, "themeClick", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent();
		intent.setClass(this, MuseumsListActivity.class);
		intent.putExtra("info", ConfigUtil.THEME_MUSEUM);
		this.startActivity(intent);
	};

	private OnPageChangeListener MOnPagerChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int index) {
			tvTitle.setText(strTitles[index]);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.category_menu, menu);
	        return true;
	    }
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	switch (item.getItemId()) {
			case R.id.action_search:
				startActivity(new Intent(this,SearchListActivity.class));
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
