package com.sm.sls_app.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.RecyclerListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.Schemes;
import com.sm.sls_app.view.MyListView2;

public class CenterFollowLotteryAdapter extends BaseAdapter{

	private Context mContext;
	private HashMap<String , List<String> > mMap;
	
	private List<String> dath;
	private List<List<Schemes>> mList;
	
	private OnItemClickListener listener;
	
	public CenterFollowLotteryAdapter(Context context,List<String> list, List<List<Schemes>> mlist
			,OnItemClickListener listener)
	{
		this.mContext = context;
		this.listener = listener;
		setDate(list, mlist);
	}
	
	public void setDate(List<String> list,List<List<Schemes>> mlist)
	{
		if(null == dath)
			dath = new ArrayList<String>();
		if(null == mList)
			mList = new ArrayList<List<Schemes>>();
		dath.clear();
		mList.clear();
		
		for(String str : list)
			dath.add(str);
		
		for(List<Schemes> li : mlist)
		{
			List<Schemes> l = new  ArrayList<Schemes>();
			for(Schemes str : li)
			{
				l.add(str);
			}
			
			mList.add(l);
		}
	}
	
	/** 给集合赋值 */
	public void setHashMap(HashMap<String, List<String> > set)
	{
		for(Entry<String, List<String>> entry : set.entrySet())
		{
			List<String> l=new ArrayList<String>() ;
			for(String s : entry.getValue())
			{
				l.add(s);
			}
			this.mMap.put( entry.getKey() , l );
		}
	}
	
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return dath.size();
	}

	@Override
	public Object getItem(int arg0) 
	{
		// TODO Auto-generated method stub
		return dath.get(arg0);
	}

	@Override
	public long getItemId(int arg0)
	{
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(null == view)
		{
			LayoutInflater inflact = LayoutInflater.from(mContext);
			view = inflact.inflate(R.layout.center_lottery_item, null);
			holder = new ViewHolder();
			holder.tv_month = (TextView) view.findViewById(R.id.tv_month);
			holder.tv_day = (TextView) view.findViewById(R.id.tv_day);
			holder.listView = (MyListView2) view.findViewById(R.id.item_listView);
			view.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) view.getTag();
		}
		
		holder.listView.setAdapter(new MyListViewAdapter( mList.get(position) ,position)  );
		holder.listView.setOnItemClickListener(listener);
		
		holder.tv_month.setText( dath.get(position).split("-")[1]+"月" );
		holder.tv_day.setText( dath.get(position).split("-")[2] );
		
		return view;
	}
	
	static class ViewHolder
	{
		TextView tv_month,tv_day;
		MyListView2 listView;
	}
	
	class MyListViewAdapter extends BaseAdapter
	{
		private List<Schemes> aList;
		private int  id;
		
		public MyListViewAdapter(List<Schemes> aList, int id)
		{
			this.aList = aList;
			this.id = id;
		}
		
		@Override
		public int getCount() 
		{
			// TODO Auto-generated method stub
			return aList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return aList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder2 holder;
			if(null == convertView )
			{
				LayoutInflater inflact = LayoutInflater.from(mContext);
				convertView = inflact.inflate(R.layout.center_lottery_item_item, null);
				holder = new ViewHolder2();
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_lotteryName);
				holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
				holder.tv_type =  (TextView) convertView.findViewById(R.id.tv_playType);
				holder.tv_win =  (TextView) convertView.findViewById(R.id.tv_isWin);
				holder.tv_id = (TextView) convertView.findViewById(R.id.tv_id);
				holder.img_logo = (ImageView) convertView.findViewById(R.id.img_win);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder2) convertView.getTag();
			}
			
			holder.tv_id.setText( id +"");
			holder.tv_id.setVisibility(View.GONE);
			holder.img_logo.setVisibility(View.GONE);
			
			Schemes scheme = aList.get(position);
			
			holder.tv_name.setText(scheme.getLotteryName());
			holder.tv_money.setText(scheme.getMoney()+"元");
			
			if ("False".equals(scheme.getIsPurchasing())) 
			{
				holder.tv_type.setText("合买");
			} 
			else if("True".equals(scheme.getIsPurchasing()))
			{
				if (scheme.getIsChase() == 0)
					holder.tv_type.setText("普通");
				else
					holder.tv_type.setText("追号");
			}
			
			if (0 != scheme.getQuashStatus()) 
			{
				holder.tv_win.setText("已撤单");
			} 
			else 
			{
				if ("False".equals(scheme.getSchemeIsOpened())) 
				{
					holder.tv_win.setText("未开奖");
					holder.tv_win.setTextColor(Color.BLACK);
				} 
				else if ("True".equals(scheme.getSchemeIsOpened())) 
				{
					if (scheme.getWinMoneyNoWithTax() > 0)
					{
						holder.tv_win.setText("中奖"
								+ scheme.getWinMoneyNoWithTax() + "元");
						// 设置中奖颜色为红色
						holder.tv_win.setTextColor(Color.RED);
						holder.img_logo.setVisibility(View.VISIBLE);
					} 
					else 
					{
						holder.tv_win.setText("未中奖");
						holder.tv_win.setTextColor(Color.BLACK);
					}
				}
			}
			
			return convertView;
		}
	}
	
	
	static class ViewHolder2
	{
		TextView tv_name,tv_money,tv_type,tv_win,tv_id;
		ImageView img_logo;
	}
	
}
