package com.sm.sls_app.ui.adapter;

import java.util.ArrayList;
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

public class GridViewky481Adapter extends BaseAdapter {
	
	// 上下文本
	private Context context;
	// 装彩票的集合
	private String [] Numbers;
	
	// 存放红球的集合
	private List<String> oneSet = new ArrayList<String>();

	/** 构造方法 实现初始化*/
	public GridViewky481Adapter(Context context,String[] numbers) 
	{
		this.context = context;
		this.Numbers = numbers;
		
	}
	
	public List<String> getOneSet()
	{
		return oneSet;
	}
	
	public int getOneSetSize()
	{
		if(null == oneSet)
			return 0;
		return oneSet.size();
	}
	
	public void addOne(String num)
	{
		oneSet.add(num);
	}
	
	public void removeOne(String num)
	{
		oneSet.remove(num);
	}
	
	public void setOneSet(ArrayList<String> list)
	{
		if(null == list)
			return;
		oneSet.clear();
		for(String s : list)
		{
			oneSet.add(s);
			Log.i("x","增加的�?"+s);
		}
		Log.i("x","增加后的大小"+oneSet.size());
	}
	
	public void clear()
	{
		oneSet.clear();
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
		holder.btn.setText(Numbers[position]);
		// 设置字体显示颜色 和背景图片
		holder.btn.setTextColor(context.getResources().getColor(R.color.red));

		holder.btn.setBackgroundResource(R.drawable.ball_gray);
		
		String str = (position + 1) + "";
		if (oneSet.contains(str)) 
		{
			holder.btn.setBackgroundResource(R.drawable.ball_red);
			holder.btn.setTextColor(Color.WHITE);
		}
		
		return view;
	}


	/** 静态类  */
	static class ViewHolder {
		TextView btn;
	}
}
