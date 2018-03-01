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

public class GridView11X5Adapter extends BaseAdapter {

	// 上下文本
	private Context context;
	// 装彩票的集合
	private String[] Numbers;

	// 存放红球的 集合
	private HashSet<String> oneSet = new HashSet<String>();

	private HashSet<Integer> indexSet = new HashSet<Integer>();
	private boolean type; // 前面是否加 0
	private boolean type2; // 数字从0 开始 还是从 1开始 true 为 1

	/** 构造方法 实现初始化 */
	public GridView11X5Adapter(Context context, String[] numbers, boolean _type) {
		this.context = context;
		this.Numbers = numbers;
		this.type = _type;
		this.type2 = true;
	}

	/** 构造方法 实现初始化 */
	public GridView11X5Adapter(Context context, String[] numbers,
			boolean _type, boolean type2) {
		this.context = context;
		this.Numbers = numbers;
		this.type = _type;
		this.type2 = type2;
	}

	public HashSet<String> getOneSet() {
		return oneSet;
	}

	public int getOneSetSize() {
		if (null == oneSet)
			return 0;
		return oneSet.size();
	}

	public HashSet<Integer> getIndexSet() {
		return indexSet;
	}

	public int getIndexSetSize() {
		if (null == indexSet)
			return 0;
		return indexSet.size();
	}

	public void removeAll() {
		oneSet.clear();
		notifyDataSetChanged();
	}

	public void removeIndexAll() {
		indexSet.clear();
		notifyDataSetChanged();
	}

	public void addAll() {
		for (String n : Numbers) {
			if (!oneSet.contains(n)) {
				oneSet.add(n);
			}
		}
		notifyDataSetChanged();
	}

	public void addOne(String num) {
		oneSet.add(num);
	}

	public void addIndex(int num) {
		indexSet.add(num);
	}

	public void removeOne(String num) {
		oneSet.remove(num);
	}

	public void removeIndex(int i) {
		indexSet.remove(i);
	}

	public void setOneSet(List<String> list) {
		if (null == list)
			return;
		oneSet.clear();
		for (String s : list) {
			oneSet.add(s);
			Log.i("x", "增加的值" + s);
		}
	}

	public void setIndexSet(ArrayList<String> list) {
		if (null == list)
			return;
		indexSet.clear();
		for (String s : list) {
			indexSet.add(Integer.parseInt(s));
		}
	}

	public void clear() {
		if (null != oneSet) {
			oneSet.clear();
		}
		if (null != indexSet) {
			indexSet.clear();
		}
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

		if (type) {
			// 给控件赋值
			if (Numbers[position].length() < 2) {
				holder.btn.setText("0" + Numbers[position]);
			} else {
				holder.btn.setText(Numbers[position]);
			}
		} else {
			holder.btn.setText(Numbers[position]);
		}

		// 设置字体显示颜色 和背景图片
		holder.btn.setTextColor(context.getResources().getColor(
				R.color.main_red));
		holder.btn.setBackgroundResource(R.drawable.icon_ball_red_unselected);

		String str = "";
		if (type) {
			if (true)
				str = (position + 1) + "";
			if (position < 9) {
				str = "0" + (position + 1);
			}
		} else {
			if (type2)
				str = (position + 1) + "";
			else
				str = position + "";
		}

		// 看是否选中
		if (oneSet.contains(str)) {
			holder.btn.setBackgroundResource(R.drawable.icon_ball_red_selected);
			holder.btn.setTextColor(Color.WHITE);
		}

		if (indexSet.contains(position)) {
			holder.btn.setTextColor(Color.WHITE);
			holder.btn.setBackgroundResource(R.drawable.icon_ball_red_selected);
		}

		return view;
	}

	/** 静态类 */
	static class ViewHolder {
		TextView btn;
	}
}
