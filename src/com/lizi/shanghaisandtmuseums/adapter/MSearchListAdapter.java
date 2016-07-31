package com.lizi.shanghaisandtmuseums.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lizi.shanghaisandtmuseums.R;
import com.lizi.shanghaisandtmuseums.utils.HttpUtil;

public class MSearchListAdapter extends BaseAdapter {
	private Context context;
	private List<MuseumNewsModel> newsList;

	// private int flagI;

	public MSearchListAdapter(Context context, List<MuseumNewsModel> newsList) {
		this.context = context;
		this.newsList = newsList;
	}

	@Override
	public int getCount() {
		return newsList.size();
	}

	@Override
	public MuseumNewsModel getItem(int position) {
		return newsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View mainView, ViewGroup parent) {
		MuseumNewsModel newsModel = newsList.get(position);

		if (mainView == null) {
			mainView = LayoutInflater.from(context).inflate(R.layout.list_item,
					null);
		 }
		TextView tvTitle = (TextView) mainView.findViewById(R.id.tv_title);
		TextView tvdesc = (TextView) mainView.findViewById(R.id.tv_desc);
		TextView tvTime = (TextView) mainView.findViewById(R.id.tv_time);
		ImageView ivPic = (ImageView) mainView.findViewById(R.id.iv_pic);

		tvTitle.setText(newsModel.getTitle());
		tvdesc.setText(newsModel.getMuseum()+"  "+newsModel.getCategory());
		tvTime.setText(newsModel.getTime());
		String pic_url = newsModel.getPic_url();
		// System.out.println( flagI + "   " );
		HttpUtil.setPicBitmap(ivPic, pic_url);
		ivPic.setTag(pic_url);
		// flagI++;
		return mainView;
	}

}