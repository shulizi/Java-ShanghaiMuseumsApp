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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.lizi.shanghaisandtmuseums.adapter.MSearchListAdapter;
import com.lizi.shanghaisandtmuseums.adapter.MuseumNewsModel;
import com.lizi.shanghaisandtmuseums.utils.ConfigUtil;
import com.lizi.shanghaisandtmuseums.utils.HttpUtil;

public class SearchListActivity extends Activity implements OnItemClickListener {
	private EditText editSearch;
	private ImageView imageSearch;
	private ImageView imageClear;
	private ListView lvNews;
	private MSearchListAdapter mListAdapter;
	private List<MuseumNewsModel> newsList;
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
					newsList.add(new MuseumNewsModel(title, museum, category,
							time, content_url, pic_url));
				}
				mListAdapter.notifyDataSetChanged();
			} catch (JSONException e) {
				e.printStackTrace();
			}

		};
	};

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle("搜索");

		newsList = new ArrayList<MuseumNewsModel>();

		lvNews = (ListView) findViewById(R.id.lv_search_result);
		mListAdapter = new MSearchListAdapter(this, newsList);
		lvNews.setAdapter(mListAdapter);
		lvNews.setOnItemClickListener(this);

		HttpUtil.getNewsJSON(ConfigUtil.GET_NEWS_URL, getNewsHandler);

		editSearch = (EditText) findViewById(R.id.editSearch);
		imageSearch = (ImageView) findViewById(R.id.imageSearch);
		imageClear = (ImageView) findViewById(R.id.imageClear);
		imageClear.getDrawable().setAlpha(100);
		imageClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				editSearch.setText("");
			}
		});
		editSearch.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView text, int arg1,
					KeyEvent event) {
				if (event == null) {
					doSearching(text.getText().toString());
					imageSearch.getDrawable().setAlpha(100);
				}

				return false;
			}
		});
		editSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				if (editSearch.getText().toString().equals("")) {
					imageClear.getDrawable().setAlpha(100);
				} else {
					imageClear.getDrawable().setAlpha(255);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				imageSearch.getDrawable().setAlpha(255);
			}
		});
	}

	private void doSearching(String query) {
		Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.action_home:
			startActivity(new Intent(this, CategoryPagerActivity.class));
			break;
		case R.id.action_location:
			startActivity(new Intent(this, LocationPagerActivity.class));
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search_menu, menu);
		return super.onCreateOptionsMenu(menu);
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

}
