package com.lizi.shanghaisandtmuseums.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lizi.shanghaisandtmuseums.R;
import com.lizi.shanghaisandtmuseums.mview.MCircleView;

public class MLocationListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<HashMap<String,Object>> newsList;

	// private int flagI;

	public MLocationListAdapter(Context context, ArrayList<HashMap<String,Object>> newsList) {
		this.context = context;
		this.newsList = newsList;
	}

	@Override
	public int getCount() {
		return newsList.size();
	}

	@Override
	public HashMap<String, Object> getItem(int position) {
		return newsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View mainView, ViewGroup parent) {
		HashMap<String, Object> newsModel = newsList.get(position);
		int color=0xff000000;
		String title=null;
		if(mainView==null){
			mainView=LayoutInflater.from(context).inflate(R.layout.list_location_item, null);
		}
		for (HashMap.Entry<String, Object> entry:newsModel.entrySet()){
				title = entry.getKey();
				color = (Integer) entry.getValue();
		}
		MCircleView ivPic = (MCircleView) mainView.findViewById(R.id.iv_pic);
		TextView tvTitle = (TextView) mainView.findViewById(R.id.tv_title);
		ivPic.setImageResource(R.drawable.location_item);
		ivPic.setCircleColor(color);
		tvTitle.setText(title);
		return mainView;
	}

}