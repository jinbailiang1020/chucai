package com.sm.sls_app.ui.adapter;

import java.util.List;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.SelectedNumbers;
import com.sm.sls_app.ui.Bet_BJPK10Activity;
import com.sm.sls_app.ui.adapter.MyBetLotteryList11X5Adapter.ViewHolder;
import com.sm.sls_app.utils.AppTools;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author 作者 : hxj
 * @version 创建时间：2016-4-9 上午10:08:35 类说明
 */
public class MyBetLotteryPK10Adapter extends BaseAdapter {
	// 上下文本
	private Context context;
	// 装彩票的集合
	private List<SelectedNumbers> listSchemes;
	private Bet_BJPK10Activity betActivity;

	public MyBetLotteryPK10Adapter(Context context,
			List<SelectedNumbers> listSchemes) {
		super();
		this.context = context;
		this.listSchemes = listSchemes;
		this.betActivity = (Bet_BJPK10Activity) context;
	}

	@Override
	public int getCount() {
		return listSchemes.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listSchemes.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		final int index = position;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			// 得到布局文件
			view = inflater.inflate(R.layout.item_bet_lv, null);
			// 得到控件
			holder.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
			holder.tv_show_number = (TextView) view
					.findViewById(R.id.tv_show_number);

			holder.tv_type_count_money = (TextView) view
					.findViewById(R.id.tv_type_count_money);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		SelectedNumbers num = listSchemes.get(position);
		Spanned number = Html.fromHtml("<font color='#BE0205'>"
				+ num.getShowLotteryNumber() + "</FONT>");

		holder.tv_show_number.setText(number);

		Log.i("pk10>>>>>>>>>", "显示号码----" + num.getShowLotteryNumber()
				+ "-------");
		Log.i("pk10>>>>>>>>>", "类型----" + num.getPlayType() + "-------");
		int i = num.getPlayType();
		if (i == 9401) {
			holder.tv_type_count_money.setText("普通--猜冠军  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		} else if (i == 9402) {
			holder.tv_type_count_money.setText("普通--猜冠亚军  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		} else if (i == 9403) {
			holder.tv_type_count_money.setText("普通--猜前三名  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		} else if (i == 9404) {
			holder.tv_type_count_money.setText("普通--猜前四名  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		} else if (i == 9405) {
			holder.tv_type_count_money.setText("普通--猜前五名  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		}
		holder.iv_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppTools.list_numbers.remove(index);
				betActivity.adapter.notifyDataSetChanged();
				betActivity.changeTextShow();
			}
		});
		return view;
	}

	/** 静态类 */
	static class ViewHolder {
		ImageView iv_delete;
		TextView tv_show_number;
		TextView tv_type_count_money;
	}
}
