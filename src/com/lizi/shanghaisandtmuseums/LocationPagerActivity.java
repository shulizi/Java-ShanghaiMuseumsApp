package com.lizi.shanghaisandtmuseums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.lizi.shanghaisandtmuseums.adapter.MLocationListAdapter;
import com.lizi.shanghaisandtmuseums.adapter.MViewPagerAdapter;
import com.lizi.shanghaisandtmuseums.utils.ConfigUtil;

@SuppressLint("NewApi")
public class LocationPagerActivity extends Activity {
	MapView mMapView = null;
	private RadioButton radio0;
	private RadioButton radio1;
	private ViewPager mPager;
	private BaiduMap mBaiduMap;
	private LatLng latLng;
	private EditText editSearch;
	private ImageView imageSearch;
	private ImageView imageClear;
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_pager_location);
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle(ConfigUtil.MUSEUM_INTRODUCTION);

		radio0 = (RadioButton) findViewById(R.id.radio0);
		radio1 = (RadioButton) findViewById(R.id.radio1);

		Drawable[] drawables = radio0.getCompoundDrawables();
		drawables[1].setBounds(0, 0, 70, 70);
		radio0.setCompoundDrawables(drawables[0], drawables[1], drawables[2],
				drawables[3]);
		drawables = radio1.getCompoundDrawables();
		drawables[1].setBounds(0, 0, 70, 70);
		radio1.setCompoundDrawables(drawables[0], drawables[1], drawables[2],
				drawables[3]);
		

		LayoutInflater lInflater = getLayoutInflater();
		ViewGroup locationLayout = (ViewGroup) lInflater.inflate(
				R.layout.pager_location, null);
		ViewGroup mapLayout = (ViewGroup) lInflater.inflate(R.layout.pager_map,
				null);

		mMapView = (MapView) mapLayout.findViewById(R.id.bmapView);
		mMapView.setLogoPosition(LogoPosition.logoPostionleftTop);
		mMapView.setZoomControlsPosition(new Point(100, 100));
		mMapView.showZoomControls(false);

		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMyLocationEnabled(true);


		ListView locationList = (ListView) locationLayout
				.findViewById(R.id.lv_location);
		ArrayList<HashMap<String, Object>> newsList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < ConfigUtil.SHANGHAI_DISTRICTS.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(ConfigUtil.SHANGHAI_DISTRICTS[i], ConfigUtil.COLORS[i
					% ConfigUtil.COLORS.length]);
			newsList.add(map);

		}
		ListAdapter listAdapter = new MLocationListAdapter(this, newsList);
		locationList.setAdapter(listAdapter);

		List<View> pagerListViews = new ArrayList<View>();
		pagerListViews.add(locationLayout);
		pagerListViews.add(mapLayout);
		mPager = (ViewPager) findViewById(R.id.vp_location);
		mPager.setAdapter(new MViewPagerAdapter(pagerListViews));
		mPager.setOnTouchListener(null);
		mPager.setCurrentItem(0);
		
		editSearch = (EditText) mapLayout.findViewById(R.id.editSearch);
		imageSearch = (ImageView) mapLayout.findViewById(R.id.imageSearch);
		imageClear = (ImageView) mapLayout.findViewById(R.id.imageClear);
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
					searchPoi(text.getText().toString());
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
				searchPoi(editSearch.getText().toString());
			}
		});
	}

	public void locationRadioClick(View v) {
		mPager.setCurrentItem(0);

	}

	public void mapRadioClick(View v) {
		mPager.setCurrentItem(1);
		mLocationClient.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
		// 当不需要定位图层时关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);

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
			startActivity(new Intent(this, LocationPagerActivity.class));
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void searchPoi(String skeyword) {
		PoiSearch mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
		mPoiSearch.searchInCity((new PoiCitySearchOption()).city("上海")
				.keyword(skeyword).pageCapacity(50));
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());// 单位：公里每小时
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\nheight : ");
				sb.append(location.getAltitude());// 单位：米
				sb.append("\ndirection : ");
				sb.append(location.getDirection());// 单位度
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append("\ndescribe : ");
				sb.append("gps定位成功");

			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				// 运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
				sb.append("\ndescribe : ");
				sb.append("网络定位成功");
			} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
				sb.append("\ndescribe : ");
				sb.append("离线定位成功，离线定位结果也是有效的");
			} else if (location.getLocType() == BDLocation.TypeServerError) {
				sb.append("\ndescribe : ");
				sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
			} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
				sb.append("\ndescribe : ");
				sb.append("网络不同导致定位失败，请检查网络是否通畅");
			} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
				sb.append("\ndescribe : ");
				sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
			}
			sb.append("\nlocationdescribe : ");
			sb.append(location.getLocationDescribe());// 位置语义化信息
			List<Poi> list = location.getPoiList();// POI数据
			if (list != null) {
				sb.append("\npoilist size = : ");
				sb.append(list.size());
				for (Poi p : list) {
					sb.append("\npoi= : ");
					sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
				}
			}
			Log.i("BaiduLocationApiDem", sb.toString());
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			// 设置定位数据
			mBaiduMap.setMyLocationData(locData);

			latLng = new LatLng(location.getLatitude(), location.getLongitude());
			MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
					.newLatLng(latLng);
			mBaiduMap.animateMapStatus(mapStatusUpdate);
			
		}
	}

	OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
		public void onGetPoiResult(PoiResult result) {
			// 获取POI检索结果
			if (result == null
					|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {

				return;
			} else if (result.error == SearchResult.ERRORNO.NO_ERROR) {
				mBaiduMap.clear();
				// 创建PoiOverlay
				PoiOverlay overlay = new PoiOverlay(mBaiduMap, LocationPagerActivity.this);
				// 设置overlay可以处理标注点击事件
				mBaiduMap.setOnMarkerClickListener(overlay);
				// 设置PoiOverlay数据
				overlay.setData(result);
				// 添加PoiOverlay到地图中
				overlay.addToMap();
				// overlay.zoomToSpan();
//				double lastLatitude=0;
//				double lastLongitude=0;
				for (int i = 0; i < result.getAllPoi().size(); i++) {
					double currentLatitude=result.getAllPoi().get(i).location.latitude;
					double currentLongitude=result.getAllPoi().get(i).location.longitude;
					
//					if (Math.abs(lastLatitude-currentLatitude)<0.02 && Math.abs(lastLongitude-currentLongitude)<0.02)
//						continue;
//					lastLatitude=currentLatitude;
//					lastLongitude=currentLongitude;
					LatLng llText = new LatLng(currentLatitude,currentLongitude);
					// 构建文字Option对象，用于在地图上添加文字
					OverlayOptions textOption = new TextOptions().fontSize(24)
							.fontColor(Color.BLACK)
							.text(result.getAllPoi().get(i).name)
							.position(llText);
					// 在地图上添加该文字对象并显示
					mBaiduMap.addOverlay(textOption);
				}

				return;
			}

		}

		public void onGetPoiDetailResult(PoiDetailResult result) {
			// 获取Place详情页检索结果
		}

		@Override
		public void onGetPoiIndoorResult(PoiIndoorResult arg0) {
			// TODO Auto-generated method stub

		}
	};


}
