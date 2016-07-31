package com.lizi.shanghaisandtmuseums;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lizi.shanghaisandtmuseums.adapter.MMuseumListAdapter;
import com.lizi.shanghaisandtmuseums.adapter.MuseumNewsModel;
import com.lizi.shanghaisandtmuseums.utils.ConfigUtil;
import com.lizi.shanghaisandtmuseums.utils.HttpUtil;

@SuppressLint("NewApi")
public class MuseumsListActivity extends Activity implements
		OnItemClickListener {
	private ListView lvNews;
	private MMuseumListAdapter mListAdapter;
	private List<MuseumNewsModel> newsList;
	private String categoryIntentInfo;
	@SuppressLint("HandlerLeak")
	private Handler getNewsHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String strJSON = (String) msg.obj;
			// Log.d("test", strJSON);
			try {
				JSONArray jsonArray = new JSONArray(strJSON);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					String title = object.getString("title");
					String museum = object.getString("museum");
					String category = object.getString("category");
					String time = object.getString("time");
					String content_url = object.getString("content_url");
					String pic_url = object.getString("pic_url");
					if (category.equals(categoryIntentInfo))
						newsList.add(new MuseumNewsModel(title, museum, category,
								time, content_url, pic_url));
				}
				mListAdapter.notifyDataSetChanged();
			} catch (JSONException e) {
				e.printStackTrace();
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_museums);
		Intent intent = getIntent();
		categoryIntentInfo = intent.getStringExtra("info");

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle(categoryIntentInfo);

		newsList = new ArrayList<MuseumNewsModel>();

		lvNews = (ListView) findViewById(R.id.lv_news);
		mListAdapter = new MMuseumListAdapter(this, newsList);
		lvNews.setAdapter(mListAdapter);
		lvNews.setOnItemClickListener(this);
		HttpUtil.getNewsJSON(ConfigUtil.GET_NEWS_URL, getNewsHandler);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		MuseumNewsModel newsModel = newsList.get(position);
		Intent intent = new Intent();
		intent.putExtra("content_url", newsModel.getContent_url());
		intent.setClass(this, NewsBrowseActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.other_menu, menu);
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
			startActivity(new Intent(this, LocationPagerActivity.class));
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
