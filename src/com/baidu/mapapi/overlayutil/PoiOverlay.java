package com.baidu.mapapi.overlayutil;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.search.poi.PoiResult;

/**
 * 用于显示poi的overly
 */
public class PoiOverlay extends OverlayManager {

	private static final int MAX_POI_SIZE = 50;

	private PoiResult mPoiResult = null;
	private Context context;
	/**
	 * 构造函数
	 * 
	 * @param baiduMap
	 *            该 PoiOverlay 引用的 BaiduMap 对象
	 */
	public PoiOverlay(BaiduMap baiduMap,Context context) {
		super(baiduMap);
		this.context=context;
	}

	/**
	 * 设置POI数据
	 * 
	 * @param poiResult
	 *            设置POI数据
	 */
	public void setData(PoiResult poiResult) {
		this.mPoiResult = poiResult;
	}

	@Override
	public final List<OverlayOptions> getOverlayOptions() {
		if (mPoiResult == null || mPoiResult.getAllPoi() == null) {
			return null;
		}
		List<OverlayOptions> markerList = new ArrayList<OverlayOptions>();
		int markerSize = 0;
		for (int i = 0; i < mPoiResult.getAllPoi().size()
				&& markerSize < MAX_POI_SIZE; i++) {
			if (mPoiResult.getAllPoi().get(i).location == null) {
				continue;
			}
			markerSize++;
			Bundle bundle = new Bundle();
			bundle.putInt("index", i);
			markerList
					.add(new MarkerOptions()
							.icon(BitmapDescriptorFactory
									.fromAsset("icon_gcoding.png"))
							.extraInfo(bundle)
							.position(mPoiResult.getAllPoi().get(i).location));

		}
		return markerList;
	}

	/**
	 * 获取该 PoiOverlay 的 poi数据
	 * 
	 * @return
	 */
	public PoiResult getPoiResult() {
		return mPoiResult;
	}

	/**
	 * 覆写此方法以改变默认点击行为
	 * 
	 * @param i
	 *            被点击的poi在
	 *            {@link com.baidu.mapapi.search.poi.PoiResult#getAllPoi()} 中的索引
	 * @return
	 */
	public boolean onPoiClick(int i) {
		if (mPoiResult.getAllPoi() != null
				&& mPoiResult.getAllPoi().get(i) != null) {
			Dialog dialog = new AlertDialog.Builder(context)
					.setTitle(mPoiResult.getAllPoi().get(i).name)
					.setItems(new String[]{"1","2"},new OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int index) {
							Toast.makeText(context, index+"", Toast.LENGTH_LONG)
									.show();
						}
					})
					.create();
			dialog.show();

		}
		return false;
	}

	@Override
	public final boolean onMarkerClick(Marker marker) {
		if (!mOverlayList.contains(marker)) {
			return false;
		}
		if (marker.getExtraInfo() != null) {
			return onPoiClick(marker.getExtraInfo().getInt("index"));
		}
		return false;
	}

	@Override
	public boolean onPolylineClick(Polyline polyline) {
		// TODO Auto-generated method stub
		return false;
	}
}
