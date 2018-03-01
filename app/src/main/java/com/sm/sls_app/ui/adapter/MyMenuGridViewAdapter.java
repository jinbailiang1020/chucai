package com.sm.sls_app.ui.adapter;

import java.util.List;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MyMenuGridViewAdapter extends BaseAdapter{

	private Context context;
	private List<Integer> list;
	
	public MyMenuGridViewAdapter(Context context, List<Integer> list) {
		super();
		this.context = context;
		this.list = list;
	}
	

	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public View getView(int position, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		
		ImageView imageView = new ImageView(context);
		imageView.setBackgroundResource(list.get(position));
		imageView.setMaxHeight(10);
		imageView.setMaxWidth(10);
		imageView.setAdjustViewBounds(true);
		RelativeLayout layout=new RelativeLayout(context);
		
		
//		ImageView imageLine = new ImageView(context);
//		if(position !=list.size() -1){
//			imageLine.setBackgroundResource(R.drawable.h_line);
//		//	imageLine.set
//		}
		//layout.ALIGN_PARENT_RIGHT;
		layout.setGravity(Gravity.CENTER);
		layout.addView(imageView);
//		layout.addView(imageLine);
		return layout;
	}
}
