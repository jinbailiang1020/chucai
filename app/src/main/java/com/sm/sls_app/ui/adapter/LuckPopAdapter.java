package com.sm.sls_app.ui.adapter;

import com.sm.sls_app.activity.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LuckPopAdapter extends BaseAdapter {

	private Context mContext;
	private String [] Str;
	private int select=0;

	public LuckPopAdapter(Context _context, String[] _str) {
		this.mContext = _context;
		setList(_str);
	}

	public void setList(String[] _Str) {
		this.Str = new String [_Str.length];
		for (int i = 0; i < _Str.length; i++) {
			this.Str [i] = (_Str[i]);
		}
	}
	
	public void setSelect(int _select)
	{
		if(_select > Str.length)
			this.select = 0;
		this.select = _select;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Str.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return Str[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(null == convertView)
		{
			LayoutInflater inflact =LayoutInflater.from(mContext);
			convertView = inflact.inflate(R.layout.luck_pop_item, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView) convertView.findViewById(R.id.luck_name);
			holder.img_back = (ImageView) convertView.findViewById(R.id.pop_item_img_back);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();	
		}
		
		//赋值
		//设置背景透明
		holder.img_back.setBackgroundColor(mContext.getResources().getColor(R.color.tran));
		holder.tv_name.setText(Str[position]);
		if(position == select)
		{
			holder.img_back.setBackgroundResource(R.drawable.pop_select);
		}
		
		return convertView;
	}

	static class ViewHolder 
	{
		TextView tv_name;
		ImageView img_back;
	}
}
