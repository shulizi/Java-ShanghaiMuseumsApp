package com.lizi.shanghaisandtmuseums;

import android.app.Activity;
import android.os.Bundle;

import com.baidu.mapapi.map.MapView;

public class LocationPagerActivity extends Activity{
	 MapView mMapView = null;  
	    @Override  
	    protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);   
	        setContentView(R.layout.activity_pager_location);
	        //在使用SDK各组件之前初始化context信息，传入ApplicationContext  
	        //注意该方法要再setContentView方法之前实现  
//	        SDKInitializer.initialize(getApplicationContext());  
//	        setContentView(R.layout.activity_map);  
//	        //获取地图控件引用  
//	        mMapView = (MapView) findViewById(R.id.bmapView);  
	    }  
//	    @Override  
//	    protected void onDestroy() {  
//	        super.onDestroy();  
//	        mMapView.onDestroy();  
//	    }  
//	    @Override  
//	    protected void onResume() {  
//	        super.onResume();  
//	        mMapView.onResume();  
//	        }  
//	    @Override  
//	    protected void onPause() {  
//	        super.onPause();  
//	        mMapView.onPause();  
//	        }  
}
