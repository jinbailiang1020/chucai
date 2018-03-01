package com.sm.sls_app.ui.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.Lottery;
import com.sm.sls_app.dataaccess.WinLottery;
import com.sm.sls_app.view.MyListView2;

/** 历史中奖号码详情 Adapter */
public class WinLotteryInfoAdapter extends BaseAdapter {

	private Context context;

	private List<WinLottery> list;

	private OnClickListener listener;

	public WinLotteryInfoAdapter(Context context, List<WinLottery> list,
			OnClickListener listener) {
		this.context = context;
		this.list = list;
		this.listener = listener;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			// 得到布局文件
			view = inflater.inflate(R.layout.win_lotteryinfo_items, null);
			// 得到控件

			holder.text_qi = (TextView) view.findViewById(R.id.top_tv_qi);
			holder.text_time = (TextView) view.findViewById(R.id.top_tv_time);
			holder.text_redNum = (TextView) view
					.findViewById(R.id.bottom_tv_redNum);
			holder.text_blueNum = (TextView) view
					.findViewById(R.id.bottom_tv_blueNum);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		WinLottery wLottery = list.get(position);
		holder.text_redNum.setText("");
		holder.text_blueNum.setText("");

		holder.text_qi.setTextSize(TypedValue.COMPLEX_UNIT_PX, context
				.getResources().getDimensionPixelSize(R.dimen.m_tx_size));
		holder.text_redNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, context
				.getResources().getDimensionPixelSize(R.dimen.tx_size));
		holder.text_blueNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, context
				.getResources().getDimensionPixelSize(R.dimen.tx_size));

		// 给控件赋值
		if (wLottery.getName() != null) {
			holder.text_qi.setText("第" + wLottery.getName() + "期");
		}

		holder.text_time.setText(wLottery.getEndTime());

		if (wLottery.getLotteryId().equals("39")) {
			String[] array = wLottery.getRedNum().replace(",", " ").split(" ");
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < array.length; i++) {
				list.add(array[i]);
			}
			Collections.sort(list);
			holder.text_redNum.setText(list.toString().replace(",", " ")
					.replace("[", " ").replace("]", " "));
			array = wLottery.getBlueNum().replace(",", " ").split(" ");
			list = new ArrayList<String>();
			for (int i = 0; i < array.length; i++) {
				list.add(array[i]);
			}
			Collections.sort(list);
			holder.text_blueNum.setText(list.toString().replace(",", " ")
					.replace("[", " ").replace("]", " "));
		} else {
			holder.text_redNum.setText(wLottery.getRedNum().replace(",", " "));
			holder.text_blueNum.setText(wLottery.getBlueNum());
		}
		return view;
	}

	/**
	 * 静态类
	 * 
	 * @author Administrator
	 * 
	 */
	static class ViewHolder {
		TextView text_qi;
		TextView text_time;
		TextView text_redNum;
		TextView text_blueNum;
	}

}
