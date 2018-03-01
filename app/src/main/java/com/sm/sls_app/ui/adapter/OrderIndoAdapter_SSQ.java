package com.sm.sls_app.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.sm.sls_app.activity.R;

public class OrderIndoAdapter_SSQ extends BaseAdapter {

	private String[] listNumbers;
	private Context mContext;
	private int bei;

	public OrderIndoAdapter_SSQ(Context context, String[] list,int bei) {
		super();
		this.listNumbers = list;
		this.mContext = context;
		this.bei =bei;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listNumbers.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listNumbers[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		final ViewHolder holder;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(mContext);
			// 得到布局文件
			view = inflater.inflate(R.layout.orderinfo_item_ssq, null);

			// 得到控件
			holder.redNum = (TextView) view
					.findViewById(R.id.orderinfo_bet_redNum);
			holder.tv_bei = (TextView) view
					.findViewById(R.id.orderinfo_bet_bei);

			view.setTag(holder);
		} 
		else 
		{
			holder = (ViewHolder) view.getTag();
		}

		if (listNumbers[position].contains("-")) {
			String[] str = listNumbers[position].split("-");
			
			// 给控件赋值
			if (str.length == 2) 
			{
				Spanned number = Html.fromHtml("<font color='#BE0205'>"
						+ str[0] + "</FONT>" + "<font color='#4060ff'>" + " "
						+ str[1] + "</FONT>");
				holder.redNum.setText(number);
			} 
			else
			{
				holder.redNum.setText(str[0]);
			}
		}
		else
		{
			holder.redNum.setText(listNumbers[position]);
			Log.i("x", "号码-------"+listNumbers[position]);
		}
		holder.tv_bei.setText(bei+"倍");
		holder.redNum.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				mContext.getResources().getDimensionPixelSize(R.dimen.tx_size)); 
		// 设置字体显示颜色 和背景图片
		return view;
	}

	/** 静态类 */
	static class ViewHolder {
		TextView redNum, tv_bei;
	}

}
