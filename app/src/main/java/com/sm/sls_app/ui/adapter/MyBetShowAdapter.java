package com.sm.sls_app.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.sm.sls_app.activity.R;

/***
 * 我的投注详情 页 显示 我投注的号码 Adapter
 * 
 * @author Administrator
 * 
 */
public class MyBetShowAdapter extends BaseAdapter {
	private Context context;
	private String[] num;
	private int max;
	private int layoutId;

	private String[] winNum; // 红球开奖号码
	private String[] winNum2; // 蓝球开奖号码

	public MyBetShowAdapter(Context context, int layoutId) {
		this.context = context;
		this.layoutId = layoutId;
	}

	public MyBetShowAdapter(Context context, String[] _num, int _max,
			int layoutId) {
		this.context = context;
		this.layoutId = layoutId;
		setNumber(_num, _max);
	}

	public MyBetShowAdapter(Context context, String[] _num, int _max,
			int layoutId, String[] win, String[] win2) {
		this.context = context;
		this.layoutId = layoutId;
		setNumber(_num, _max);
		setWinNumber(win, win2);
	}

	/** 设置开奖号码 */
	public void setWinNumber(String[] win, String[] win2) {
		if (null == win)
			return;
		winNum = new String[win.length];
		for (int i = 0; i < win.length; i++) {
			winNum[i] = win[i];
		}

		if (null == win2)
			return;

		winNum2 = new String[win2.length];
		for (int i = 0; i < win2.length; i++) {
			winNum2[i] = win2[i];
		}

	}

	public void setNumber(String[] _num, int _max) {
		num = new String[_num.length];
		for (int i = 0; i < _num.length; i++) {
			num[i] = _num[i];
		}
		this.max = _max;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return num.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return num[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(context);
		// 得到布局文件
		// convertView = inflater.inflate(R.layout.winball_item, null);
		convertView = inflater.inflate(layoutId, null);
		TextView tv_ball = (TextView) convertView
				.findViewById(R.id.btn_showNum);

		if (position < max) {

			tv_ball.setText(num[position] + "");
			if (null != winNum) {
				for (String st : winNum) {
					if (num[position].equals(st)) {
						tv_ball.setTextColor(context.getResources().getColor(
								R.color.red));
					}
				}
			}
		} else {
			if (position == max)
				tv_ball.setText(":" + num[position]);
			else
				tv_ball.setText(num[position]);
			if (null != winNum2) {
				for (String st : winNum2) {
					if (num[position].equals(st)) {
						tv_ball.setTextColor(context.getResources().getColor(
								R.color.blue));
					}
				}
			}
		}
		return convertView;
	}
}
