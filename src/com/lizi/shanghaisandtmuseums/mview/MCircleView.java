package com.lizi.shanghaisandtmuseums.mview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MCircleView extends ImageView {
	private Paint paint;
	public MCircleView(Context context) {
		super(context);
		init();
	}
	public MCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public MCircleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	private void init(){
	    
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.FILL);
		
	}
	public void setCircleColor(int color) {
		paint.setColor(color);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawCircle(getWidth()/2, getHeight()/2, getWidth()/2, paint);
		super.onDraw(canvas);
		
	}

}
