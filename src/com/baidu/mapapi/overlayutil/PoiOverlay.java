package com.baidu.mapapi.overlayutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SimpleAdapter;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.search.poi.PoiResult;
import com.lizi.shanghaisandtmuseums.NewsBrowseActivity;
import com.lizi.shanghaisandtmuseums.R;
import com.lizi.shanghaisandtmuseums.utils.ConfigUtil;
import com.lizi.shanghaisandtmuseums.utils.FuzzySearchUtil;

/**
 * 用于显示poi的overly
 */
@SuppressLint("NewApi")
public class PoiOverlay extends OverlayManager {

	private static final int MAX_POI_SIZE = 50;

	private PoiResult mPoiResult = null;
	private Context context;
	private int selectIndex;
	private String strJSON;

	/**
	 * 构造函数
	 * 
	 * @param baiduMap
	 *            该 PoiOverlay 引用的 BaiduMap 对象
	 */
	public PoiOverlay(BaiduMap baiduMap, Context context, int selectIndex) {
		super(baiduMap);
		this.context = context;
		this.selectIndex = selectIndex;
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
		for (int i = mPoiResult.getAllPoi().size() - 1; i >= 0
				&& markerSize < MAX_POI_SIZE; i--) {
			if (mPoiResult.getAllPoi().get(i).location == null
					|| (selectIndex >= 0 && Math.abs(mPoiResult.getAllPoi()
							.get(i).location.latitude
							- ConfigUtil.DISTRICTS_LOCATION[selectIndex][1]) > ConfigUtil.DISTRICTS_LOCATION[selectIndex][2] / 100000)
					|| (selectIndex >= 0 && Math.abs(mPoiResult.getAllPoi()
							.get(i).location.longitude
							- ConfigUtil.DISTRICTS_LOCATION[selectIndex][0]) > ConfigUtil.DISTRICTS_LOCATION[selectIndex][2] / 100000)) {
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
			ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();

			strJSON = ConfigUtil.JSON_RESULT;
			String[] contentUrl = null;
			try {
				JSONArray jsonArray = new JSONArray(strJSON);
				contentUrl = new String[jsonArray.length()];
				int index = 0;
				for (int j = 0; j < jsonArray.length(); j++) {
					HashMap<String, String> hMap = new HashMap<String, String>();
					JSONObject object = jsonArray.getJSONObject(j);
					String title = object.getString("title");
					String museum = object.getString("museum");
					String category = object.getString("category");
					String content_url = object.getString("content_url");
					String searchKey = mPoiResult.getAllPoi().get(i).name;

					if (FuzzySearchUtil.fuzzySearch(searchKey, 0.9, museum)) {
						hMap.put("title", title);
						hMap.put("museum", museum + " " + category);
						contentUrl[index] = content_url;
						index++;
						arrayList.add(hMap);
					}

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			final String[] contentUrl2 = contentUrl;
			if (arrayList.size() == 0) {
				HashMap<String, String> hMap = new HashMap<String, String>();
				hMap.put("title", ConfigUtil.NO_SEARCH_MUSEUM);
				arrayList.add(hMap);
			}
			SimpleAdapter sAdapter = new SimpleAdapter(context, arrayList,
					R.layout.list_map_item, new String[] { "title", "museum" },
					new int[] { R.id.tv_title, R.id.tv_content });
			ViewGroup listViewGroup = (ViewGroup) LayoutInflater.from(context)
					.inflate(R.layout.dialog_map, null);
			ListView lView = (ListView) listViewGroup
					.findViewById(R.id.lv_map_dialog);
			lView.setAdapter(sAdapter);
			lView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> list, View IND,
						int index, long arg3) {
					try {
						Intent intent = new Intent();
						intent.setClass(context, NewsBrowseActivity.class);
						intent.putExtra("content_url", contentUrl2[index]);
						context.startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("Fail to start activity");
					}

				}
			});

			Dialog dialog = new Dialog(context, R.style.dialog);
			Window window = dialog.getWindow();
			window.setWindowAnimations(R.style.dialog_animation); // 添加动画
			dialog.setContentView(listViewGroup);
			dialog.setCanceledOnTouchOutside(true);
			dialog.setTitle(mPoiResult.getAllPoi().get(i).name);
			dialog.show();
			android.view.WindowManager.LayoutParams layoutParams = window
					.getAttributes();
			layoutParams.width = LayoutParams.MATCH_PARENT;
			layoutParams.height = LayoutParams.WRAP_CONTENT;
			window.setGravity(Gravity.BOTTOM);
			window.setAttributes(layoutParams);

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
