package com.sm.sls_app.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;
/** 
 * 实现跑马灯自定义TextVew
 * @author Administrator
 */
public class RollTextView  extends TextView{

	public RollTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		// TODO Auto-generated method stub
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}

	public RollTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public RollTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
   public boolean isFocused(){
	   return true;
	   
   }
}
