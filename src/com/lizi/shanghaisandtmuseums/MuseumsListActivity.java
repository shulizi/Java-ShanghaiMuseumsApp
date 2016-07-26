package com.lizi.shanghaisandtmuseums;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lizi.shanghaisandtmuseums.Adapter.MListAdapter;
import com.lizi.shanghaisandtmuseums.Adapter.NewsModel;
import com.lizi.shanghaisandtmuseums.utils.HttpUtil;

public class MuseumsListActivity extends Activity implements OnItemClickListener{
	private ListView lvNews;
	private MListAdapter mListAdapter;
	private List<NewsModel> newsList;

	public static final String GET_NEWS_URL = "http://121.42.159.177/news_connect/getJSON.php";
	@SuppressLint("HandlerLeak") private Handler getNewsHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String strJSON = (String) msg.obj;
//			Log.d("test", strJSON);
			try {
				JSONArray jsonArray = new JSONArray(strJSON);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					String title = object.getString("title");
					String desc = object.getString("desc");
					String time = object.getString("time");
					String content_url = object.getString("content_url");
					String pic_url = object.getString("pic_url");
					
					newsList.add(new NewsModel(title, desc, time, content_url,
							pic_url));
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

		newsList = new ArrayList<NewsModel>();

		lvNews = (ListView) findViewById(R.id.lv_news);
		mListAdapter = new MListAdapter(this, newsList);
		lvNews.setAdapter(mListAdapter);
		lvNews.setOnItemClickListener(this);
		HttpUtil.getNewsJSON(GET_NEWS_URL, getNewsHandler);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		NewsModel newsModel = newsList.get(position);
		Intent intent = new Intent();
		intent.putExtra("content_url", newsModel.getContent_url());
		intent.setClass(this, NewsBrowseActivity.class);
		startActivity(intent);
	}
}
