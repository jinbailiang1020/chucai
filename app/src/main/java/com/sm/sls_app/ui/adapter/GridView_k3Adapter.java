package com.sm.sls_app.ui.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.sm.sls_app.activity.R;


import android.content.Context;
import android.graphics.Color;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;

import android.widget.TextView;


public class GridView_k3Adapter extends BaseAdapter {
	


	// 上下文本
	private Context context;
	// 装彩票的集合
	public  Integer[] Numbers;
	
	
	// 存放选球
	public static  List<String> oneSet = new ArrayList<String>();

	/** 构�?方法 实现初始�?*/
	public GridView_k3Adapter(Context context,Integer[] numbers) 
	{
		this.context = context;
		this.Numbers = numbers;
		
	}
	
	public List<String> getSet()
	{
		return oneSet;
	}
	
	public void add(String num)
	{
		oneSet.add(num);
	}
	
	public void remove(String num)
	{
		oneSet.remove(num);
	}
	
	public void setSet(ArrayList<String> list)
	{
		if(null == list)
			return;
		oneSet.clear();
		for(String s : list)
		{
			oneSet.add(s);
		}
	}
	
	public void clear()
	{
		oneSet.clear();
	}
	public int getSetSize()
	{
		if(null == oneSet)
			return 0;
		return oneSet.size();
	}

	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return Numbers.length;
	}

	@Override
	public Object getItem(int arg0) 
	{
		// TODO Auto-generated method stub
		return Numbers[arg0];
	}

	@Override
	public long getItemId(int arg0) 
	{
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		// 判断View是否为空
		if (view == null) 
		{
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			// 得到布局文件
			view = inflater.inflate(R.layout.gridview_items, null);

			// 得到控件
			holder.btn = (TextView) view.findViewById(R.id.btn_showNum);
			view.setTag(holder);
		} 
		else 
		{
			holder = (ViewHolder) view.getTag();
		}

		// 给控件赋值
		if (Numbers[position] < 10) 
		{
			holder.btn.setText("0" + Numbers[position] + "");
		} 
		else 
		{
			holder.btn.setText(Numbers[position] + "");
		}

		// 设置字体显示颜色 和背景图�?

		holder.btn.setBackgroundResource(R.drawable.ball_gray);
		holder.btn.setTextColor(context.getResources()
				.getColor(R.color.red));
		String str = (position + 1) + "";
		if (position < 9) 
		{
			str = "0" + (position + 1);
		}	
		//看是所选号码是否存在onset中
		if (oneSet.contains(str)) 
		{
			holder.btn.setBackgroundResource(R.drawable.ball_red);
			holder.btn.setTextColor(Color.WHITE);
		}
		
		
		return view;
	}


	/** 静�?�?*/
	static class ViewHolder {
		TextView btn;
	}
}
