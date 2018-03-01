package com.sm.sls_app.view;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import com.sm.sls_app.activity.R;

public class MyLuckyView {
	private ImageView img_bg, img_up;
	private int width =150, r, height = 150;

	public MyLuckyView(final ImageView img_bg, ImageView img_up) 
	{
		this.img_bg = img_bg;
		this.img_up = img_up;
		this.width = img_bg.getWidth();
		this.height = img_bg.getHeight();
		Log.i("x", "高宽===="+width+"===="+height);
		r = 150;
		img_bg.setOnTouchListener(new WheelTouchListener());
		img_bg.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener()
				{
					public void onGlobalLayout() 
					{
						width = img_bg.getWidth();
						height = img_bg.getHeight();
						r = width / 2;
					}
				});
	}

	private class WheelTouchListener implements View.OnTouchListener 
	{
		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			double angle = getAngle(event.getX(), event.getY());
			Log.i("x", "角度---" + angle);
			Log.i("x", "x---===" + event.getX() + "--y--=" + event.getY());
			double d = Math.hypot(width/2 - event.getX(), height/2 - event.getY());
			Log.i("x", "两点之间的距离--======" + d);
			if (d <= r) {
				switch ((int) angle / 30) 
				{
				case 0:
					img_bg.setBackgroundResource(R.drawable.turntable3);
					break;
				case 1:
					img_bg.setBackgroundResource(R.drawable.turntable2);
					break;
				case 2:
					img_bg.setBackgroundResource(R.drawable.turntable1);
					break;
				case 3:
					img_bg.setBackgroundResource(R.drawable.turntable12);
					break;
				case 4:
					img_bg.setBackgroundResource(R.drawable.turntable11);
					break;
				case 5:
					img_bg.setBackgroundResource(R.drawable.turntable10);
					break;
				case 6:
					img_bg.setBackgroundResource(R.drawable.turntable9);
					break;
				case 7:
					img_bg.setBackgroundResource(R.drawable.turntable8);
					break;
				case 8:
					img_bg.setBackgroundResource(R.drawable.turntable7);
					break;
				case 9:
					img_bg.setBackgroundResource(R.drawable.turntable6);
					break;
				case 10:
					img_bg.setBackgroundResource(R.drawable.turntable5);
					break;
				case 11:
					img_bg.setBackgroundResource(R.drawable.turntable4);
					break;
				}
			}
			return false;
		}

	}

	private double getAngle(double xTouch, double yTouch) 
	{
		double x = xTouch - (width / 2d);
		double y = height - yTouch - (height / 2d);

		switch (getQuadrant(x, y))
		{
		case 1:
			return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
		case 2:
			return 180 - Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
		case 3:
			return 180 + (-1 * Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
		case 4:
			return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
		default:
			return 0;
		}
	}

	// get the quadrant of the wheel which the user has touched
	private static int getQuadrant(double x, double y)
	{
		if (x >= 0) 
		{
			return y >= 0 ? 1 : 4;
		} 
		else
		{
			return y >= 0 ? 2 : 3;
		}
	}
}
