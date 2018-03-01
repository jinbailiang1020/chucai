package com.sm.sls_app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		View view = getChildAt(0);
		if (view.getMeasuredHeight() <= getScrollY() + getHeight()) {
			// 触底
			onScrollListener.onBottom();
		} else if (getScrollY() == 0) {
			onScrollListener.onTop();
		} else {
			onScrollListener.onScroll();
		}
	}

	public interface OnMyScrollListener {
		void onBottom();

		void onTop();

		void onScroll();
	}

	private OnMyScrollListener onScrollListener;

	public void setOnScrollListener(OnMyScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}
}
