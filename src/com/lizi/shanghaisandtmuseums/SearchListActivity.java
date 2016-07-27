package com.lizi.shanghaisandtmuseums;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class SearchListActivity extends Activity {
	private EditText editSearch;
	private ImageView imageSearch;
	private ImageView imageClear;

	@SuppressLint("NewApi") @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle("搜索");
		
		editSearch = (EditText) findViewById(R.id.editSearch);
		imageSearch=(ImageView) findViewById(R.id.imageSearch);
		imageClear=(ImageView) findViewById(R.id.imageClear);
		imageClear.getDrawable().setAlpha(100);
		imageClear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				editSearch.setText("");
			}
		});
		editSearch.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView text, int arg1, KeyEvent event) {
				if (event==null) {
					doSearching(text.getText().toString());
					imageSearch.getDrawable().setAlpha(100);
				}
				
				return false;
			}
		});
		editSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				if(editSearch.getText().toString().equals("")){
					imageClear.getDrawable().setAlpha(100);
				}else{
					imageClear.getDrawable().setAlpha(255);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
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
			startActivity(new Intent(this,CategoryPagerActivity.class));
			break;
		case R.id.action_location:
			startActivity(new Intent(this,LocationPagerActivity.class));
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

}
