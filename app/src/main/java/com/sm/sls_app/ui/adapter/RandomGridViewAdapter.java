package com.sm.sls_app.ui.adapter;

import com.sm.sls_app.activity.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * 机选随机个数的 Adapter
 * 
 * @author Administrator
 * 
 */
public class RandomGridViewAdapter extends BaseAdapter {

	// 上下文本
	private Context context;
	// 装数字的集合
	private Integer[] Numbers;
	private int color;
	private int type = 1; // 投注界面类型

	private int redNumber ;
	private int blueNumber;

	/**构造方法 实现初始化*/
	public RandomGridViewAdapter(Context context, Integer[] Numbers, int c,
			int type,int num,int num2) {
		this.context = context;
		this.Numbers = Numbers;
		this.color = c;
		this.type = type;
		this.redNumber=num;
		this.blueNumber=num2;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Numbers.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return Numbers[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	/**得到View*/
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			// 得到布局文件
			view = inflater.inflate(R.layout.gridview_items, null);
			// 得到控件
			holder.btn = (TextView) view.findViewById(R.id.btn_showNum);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		// 给控件赋值
		holder.btn.setText(Numbers[position] + "");
		
		if(Color.RED == color)
			holder.btn.setTextColor(context.getResources().getColor(R.color.red));
		else
			holder.btn.setTextColor(context.getResources().getColor(R.color.blue));
		
		// 设置字体显示颜色 和背景图片
		holder.btn.setTextColor(color);
		
		holder.btn.setBackgroundResource(R.drawable.random_ball);

		holder.btn.setFocusable(false);
		holder.btn.setClickable(false);
		
		if ((redNumber) == position) {
			if (type == 1) {
				holder.btn.setTextColor(Color.WHITE);
				holder.btn.setBackgroundResource(R.drawable.random_red_ball);
			} 
		}
		if ((blueNumber) == position) {
			if (type == 2) {
				holder.btn.setTextColor(Color.WHITE);
				holder.btn.setBackgroundResource(R.drawable.random_blue_ball);
			} 
		}
		return view;
	}

	/**静态类*/
	static class ViewHolder {
		TextView btn;
	}

}
