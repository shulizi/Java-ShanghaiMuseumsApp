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
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapTouchListener;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.lizi.shanghaisandtmuseums.adapter.MLocationListAdapter;
import com.lizi.shanghaisandtmuseums.adapter.MViewPagerAdapter;
import com.lizi.shanghaisandtmuseums.mview.MLocationListener;
import com.lizi.shanghaisandtmuseums.utils.ConfigUtil;

@SuppressLint("NewApi")
public class LocationPagerActivity extends Activity {
	MapView mMapView = null;
	private Boolean isFirstSearch;
	private RadioButton radio0;
	private RadioButton radio1;
	private ViewPager mPager;
	private BaiduMap mBaiduMap;
	private LatLng currentLocationLatLng;
	private EditText editSearch;
	private ImageView imageSearch;
	private ImageView imageClear;
	private ImageView imageNextPage;
	private int currentSelectNum = -1;

	private int mapZoomLevel;
	protected int currentPageNum = 0;
	private ActionBar actionBar;
	private LocationClient mLocationClient = null;
	private List<View> pagerListViews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_pager_location);
		mLocationClient = new LocationClient(getApplicationContext());
		mLocationClient.registerLocationListener(myListener);
		mLocationClient.start();
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle(ConfigUtil.DISTRICT);

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

		pagerListViews = new ArrayList<View>();
		
		onLocationPagerCreate();
		onMapPagerCreate();
		
		mPager = (ViewPager) findViewById(R.id.vp_location);
		mPager.setAdapter(new MViewPagerAdapter(pagerListViews));
		mPager.setOnTouchListener(null);
		mPager.setCurrentItem(0);

		
	}
	private void onLocationPagerCreate(){
		LayoutInflater lInflater = getLayoutInflater();
		ViewGroup locationLayout = (ViewGroup) lInflater.inflate(
				R.layout.pager_location, null);
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
		locationList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long arg3) {
				radio1.setChecked(true);
				actionBar.setTitle(ConfigUtil.SHANGHAI_DISTRICTS[index]);
				currentSelectNum = index;
				mPager.setCurrentItem(1);
				searchPoi(editSearch.getText().toString(), currentPageNum);
				isFirstSearch = true;
			}
		});
		pagerListViews.add(locationLayout);
		
	}
	
	private void onMapPagerCreate(){
		LayoutInflater lInflater = getLayoutInflater();
		ViewGroup mapLayout = (ViewGroup) lInflater.inflate(R.layout.pager_map,
				null);

		mMapView = (MapView) mapLayout.findViewById(R.id.bmapView);
		mMapView.setLogoPosition(LogoPosition.logoPostionleftTop);
		mMapView.setZoomControlsPosition(new Point(100, 100));
		mMapView.showZoomControls(false);

		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMyLocationEnabled(true);
		mapZoomLevel = mMapView.getMapLevel();

		mBaiduMap.setOnMapTouchListener(new OnMapTouchListener() {

			@Override
			public void onTouch(MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					// Log.d("test",lastMapLevel+" "+mMapView.getMapLevel());
					if (mapZoomLevel != mMapView.getMapLevel()) {
						searchPoi(editSearch.getText().toString(),
								currentPageNum);
						mapZoomLevel = mMapView.getMapLevel();
					}
					break;
				default:
					break;
				}

			}
		});
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
		editSearch.setOnEditorActionListener(onEditorActionListener);
		editSearch.addTextChangedListener(textWatcher);
		editSearch.setText(ConfigUtil.MUSEUM);
		imageNextPage = (ImageView) mapLayout.findViewById(R.id.imageNextPage);
		imageNextPage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				searchPoi(editSearch.getText().toString(), ++currentPageNum);
			}
		});
		imageNextPage.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					imageNextPage.getDrawable().setAlpha(100);
					break;
				case MotionEvent.ACTION_UP:
					imageNextPage.getDrawable().setAlpha(255);
					break;
				default:
					break;
				}
				return false;
			}
		});
		pagerListViews.add(mapLayout);
		
	}
	
	public void myLocationClick(View v) {
		radio1.setChecked(true);
		actionBar.setTitle(getResources().getString(R.string.my_location));
		currentSelectNum = -1;
		currentPageNum = 0;
		mPager.setCurrentItem(1);
		searchPoi(editSearch.getText().toString(), currentPageNum);
		isFirstSearch = true;
	}

	public void cityClick(View v) {
		radio1.setChecked(true);
		actionBar.setTitle(getResources().getString(R.string.shanghai));
		currentSelectNum = -2;
		currentPageNum = 0;
		mPager.setCurrentItem(1);
		searchPoi(editSearch.getText().toString(), currentPageNum);
		isFirstSearch = true;
	}

	public void locationRadioClick(View v) {
		mPager.setCurrentItem(0);

	}

	public void mapRadioClick(View v) {
		mPager.setCurrentItem(1);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
//		mBaiduMap.setMyLocationEnabled(false);

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

	private void searchPoi(String skeyword, int pageNumber) {
		switch (currentSelectNum) {
		case -2:
			PoiSearch mPoiSearch = PoiSearch.newInstance();
			mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
			mPoiSearch.searchInCity((new PoiCitySearchOption())
					.city(ConfigUtil.SHANGHAI).keyword(skeyword)
					.pageCapacity(50).pageNum(pageNumber));
			break;
		case -1:
			if (currentLocationLatLng != null)
				searchPoi(skeyword, pageNumber, currentLocationLatLng, 5000);
			break;
		default:
			searchDistrict();
			break;
		}
	}


	private void searchPoi(String skeyword, int pageNumber, LatLng location,
			int radius) {
		PoiSearch mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
		PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
		nearbySearchOption.location(location);
		nearbySearchOption.keyword(skeyword);
		nearbySearchOption.radius(radius);// 检索半径，单位是米
		if (currentPageNum >= 0)
			nearbySearchOption.pageNum(currentPageNum);
		mPoiSearch.searchNearby(nearbySearchOption);// 发起附近检索请求

	}

	private void searchDistrict() {
		if (currentSelectNum < 0)
			return;
		DistrictSearch mDistrictSearch;
		mDistrictSearch = DistrictSearch.newInstance();
		mDistrictSearch
				.setOnDistrictSearchListener(districSearchResultListener);
		mDistrictSearch.searchDistrict(new DistrictSearchOption().cityName(
				ConfigUtil.SHANGHAI).districtName(
				ConfigUtil.SHANGHAI_DISTRICTS[currentSelectNum]));

	}
	private TextWatcher textWatcher = new TextWatcher() {

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
			currentPageNum = 0;
			searchPoi(editSearch.getText().toString(), currentPageNum);
		}
	};
	private BDLocationListener myListener = new MLocationListener( new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			BDLocation location = (BDLocation) msg.obj;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			currentLocationLatLng = new LatLng(location.getLatitude(), location
					.getLongitude());
			MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
					.newLatLng(currentLocationLatLng);
			try {
				mBaiduMap.animateMapStatus(mapStatusUpdate);
				searchPoi(ConfigUtil.MUSEUM, currentPageNum);
			} catch (Exception e) {
			}
			
			return false;
		}
	}));
	private OnEditorActionListener onEditorActionListener = new OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView text, int arg1,
				KeyEvent event) {
			if (event == null) {
				currentPageNum = 0;
				searchPoi(text.getText().toString(), currentPageNum);
				imageSearch.getDrawable().setAlpha(100);
			}

			return false;
		}
	};
	private OnGetDistricSearchResultListener districSearchResultListener = new OnGetDistricSearchResultListener() {

		@Override
		public void onGetDistrictResult(DistrictResult districtResult) {
			mBaiduMap.clear();
			if (districtResult == null) {
				return;
			}
			if (districtResult.error == SearchResult.ERRORNO.NO_ERROR) {
				List<List<LatLng>> polyLines = districtResult.getPolylines();
				if (polyLines == null) {
					return;
				}
				LatLngBounds.Builder builder = new LatLngBounds.Builder();
				for (List<LatLng> polyline : polyLines) {
					OverlayOptions ooPolyline11 = new PolylineOptions()
							.width(10).points(polyline).dottedLine(true)
							.color(Color.BLUE);
					mBaiduMap.addOverlay(ooPolyline11);
					OverlayOptions ooPolygon = new PolygonOptions()
							.points(polyline).stroke(new Stroke(5, 0xAA00FF88))
							.fillColor(0x22FFFF00);
					mBaiduMap.addOverlay(ooPolygon);
					for (LatLng latLng : polyline) {
						builder.include(latLng);
					}
				}
				if (isFirstSearch) {
					mBaiduMap.setMapStatus(MapStatusUpdateFactory
							.newLatLngBounds(builder.build()));
					isFirstSearch = false;
				}
				searchPoi(
						editSearch.getText().toString(),
						currentPageNum,
						new LatLng(
								ConfigUtil.DISTRICTS_LOCATION[currentSelectNum][1],
								ConfigUtil.DISTRICTS_LOCATION[currentSelectNum][0]),
						(int) ConfigUtil.DISTRICTS_LOCATION[currentSelectNum][2]);
				// searchPoi(editSearch.getText().toString(), currentPageNum,
				// builder.build());
			}
		}
	};

	private OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
		public void onGetPoiResult(final PoiResult result) {
			// 获取POI检索结果
			if (result == null
					|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
				currentPageNum = -1;
				return;
			} else if (result.error == SearchResult.ERRORNO.NO_ERROR) {
				// mBaiduMap.clear();

				new AsyncTask<Void, Void, Boolean[]>() {
					@Override
					protected Boolean[] doInBackground(Void... arg0) {
						PointF[] pointLocation = new PointF[result.getAllPoi()
								.size()];
						int index = 0;
						Boolean isDrawText[] = new Boolean[result.getAllPoi()
								.size()];
						pointLocation[0] = new PointF();
						index = 0;
						for (int i = 0; i < result.getAllPoi().size(); i++) {
							pointLocation[index] = new PointF();
							float currentLatitude = (float) result.getAllPoi()
									.get(i).location.latitude;
							float currentLongitude = (float) result.getAllPoi()
									.get(i).location.longitude;
							int j = 0;
							for (j = 0; j < index; j++) {
								// Log.d("test",latitudes[j]+"---"+currentLatitude+"-----"+j);
								if (Math.abs(pointLocation[j].x
										- currentLatitude) * 5000 < 0.025 * mMapView
										.getMapLevel()
										&& Math.abs(pointLocation[j].y
												- currentLongitude) * 5000 < 0.025 * mMapView
												.getMapLevel())
									break;
							}
							if (j != index) {
								isDrawText[i] = false;
								continue;
							}
							isDrawText[i] = true;
							pointLocation[index].x = currentLatitude;
							pointLocation[index].y = currentLongitude;
							index++;

						}
						return isDrawText;
					}

					@Override
					protected void onPostExecute(Boolean[] asynResult) {
						if (currentSelectNum < 0)
							mBaiduMap.clear();
						// 创建PoiOverlay
						PoiOverlay overlay = new PoiOverlay(mBaiduMap,
								LocationPagerActivity.this, currentSelectNum);
						// 设置overlay可以处理标注点击事件
						mBaiduMap.setOnMarkerClickListener(overlay);
						// 设置PoiOverlay数据
						overlay.setData(result);
						// 添加PoiOverlay到地图中
						overlay.addToMap();
						// overlay.zoomToSpan();
						for (int i = 0; i < asynResult.length; i++) {
							if (asynResult[i] == false)
								continue;
							double currentLatitude = result.getAllPoi().get(i).location.latitude;
							double currentLongitude = result.getAllPoi().get(i).location.longitude;
							LatLng llText = new LatLng(currentLatitude,
									currentLongitude);
							// 构建文字Option对象，用于在地图上添加文字
							OverlayOptions textOption = new TextOptions()
									.fontSize(24).fontColor(Color.BLACK)
									.text(result.getAllPoi().get(i).name)
									.position(llText);
							// 在地图上添加该文字对象并显示
							mBaiduMap.addOverlay(textOption);

						}
					};
				}.execute();

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
