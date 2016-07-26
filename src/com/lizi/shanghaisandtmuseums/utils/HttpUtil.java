package com.lizi.shanghaisandtmuseums.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class HttpUtil {
	public static void getNewsJSON(final String url, final Handler handler) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpURLConnection conn;
				InputStream is;

				try {
					conn = (HttpURLConnection) new URL(url).openConnection();
					conn.setRequestMethod("GET");
					is = conn.getInputStream();
					BufferedReader br = new BufferedReader(
							new InputStreamReader(is));
					String line = "";

					StringBuilder sb = new StringBuilder();
					while ((line = br.readLine()) != null) {
						sb.append(line);
					}
					Message msg = new Message();
					msg.obj = sb.toString();
					handler.sendMessage(msg);
					is.close();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public static void setPicBitmap(final ImageView ivPic, final String pic_url) {
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				// System.out.println(pic_url+"  "+ivPic.getTag());
				if (ivPic.getTag().equals(pic_url)) {
					Bitmap bitmap = (Bitmap) msg.obj;
					ivPic.setImageBitmap(bitmap);
				}
			};
		};
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpURLConnection conn;
				InputStream is;

				try {
					conn = (HttpURLConnection) new URL(pic_url)
							.openConnection();
					conn.connect();
					is = conn.getInputStream();
					Bitmap bitmap = BitmapFactory.decodeStream(is);
					Message msg = new Message();
					msg.obj = bitmap;
					handler.sendMessage(msg);
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}
}
