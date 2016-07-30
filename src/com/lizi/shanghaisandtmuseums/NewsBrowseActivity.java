package com.lizi.shanghaisandtmuseums;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lizi.shanghaisandtmuseums.utils.ConfigUtil;

@SuppressLint({ "SetJavaScriptEnabled", "NewApi" }) public class NewsBrowseActivity extends Activity{
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_news);
        ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle(ConfigUtil.MUSEUM_INTRODUCTION);
		
        webView=(WebView) findViewById(R.id.wv_news);
        String url = getIntent().getStringExtra("content_url");
        webView.setWebViewClient(new WebViewClient(){
        	@Override
        	public boolean shouldOverrideUrlLoading(WebView view, String url) {
        		view.loadUrl(url);
        		return true;
        	}
        	
        });
        webView.loadUrl(url);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
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
			startActivity(new Intent(this,LocationPagerActivity.class));
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
