package com.sm.sls_app.view;

import com.sm.sls_app.ui.adapter.MyMenuGridViewAdapter;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;

public class MyMenu extends PopupWindow{

	private Context context;
	private int size;
	private GridView gridView;
	private LinearLayout layout;
	
	public MyMenu(Context context,MyMenuGridViewAdapter adapter,OnItemClickListener listener,int size)
	{
		super(context);
		this.context = context;
		layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		
		gridView=new GridView(context);
		gridView.setNumColumns(size);
		gridView.setAdapter(adapter);
		gridView.setGravity(Gravity.CENTER_HORIZONTAL);
		
		//绑定监听
		gridView.setOnItemClickListener(listener);
		
		layout.addView(gridView);
		
		super.setContentView(layout);
		super.setWidth(LayoutParams.FILL_PARENT);
		super.setHeight(LayoutParams.WRAP_CONTENT);
		super.setFocusable(true);
	}
	
}
