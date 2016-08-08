package com.lizi.shanghaisandtmuseums.mview;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MTabListener extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Context context = this.getActivity();

		TextView tv = new TextView(context);

		Bundle arc = this.getArguments();

		int tabs = arc.getInt("key");

		tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));

		tv.setText("hello actionbar " + tabs);

		return tv;

	}

}