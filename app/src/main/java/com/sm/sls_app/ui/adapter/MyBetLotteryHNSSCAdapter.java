package com.sm.sls_app.ui.adapter;

import java.util.List;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.SelectedNumbers;
import com.sm.sls_app.ui.Bet_HNSSCActivity;
import com.sm.sls_app.utils.AppTools;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 功能：显示时时彩投注的内容
 * 
 * @author Administrator
 */
public class MyBetLotteryHNSSCAdapter extends BaseAdapter {

	// 上下文本
	private Context context;
	// 装彩票的集合
	private List<SelectedNumbers> listSchemes;
	private Bet_HNSSCActivity betActivity;

	/** 构造方法 实现初始化 */
	public MyBetLotteryHNSSCAdapter(Context context,
			List<SelectedNumbers> listSchemes) {
		this.context = context;
		this.listSchemes = listSchemes;
		betActivity = (Bet_HNSSCActivity) context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listSchemes.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listSchemes.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	/** 获得视图 */
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
		Log.i("显示号码", "----(" + num.getShowLotteryNumber() + ")---");

		Log.i("x", "betAdapter==玩法类型===" + num.getType());
		if (num.getPlayType() == 9216)
			holder.tv_type_count_money.setText("胆拖--三星组三胆拖" + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (num.getPlayType() == 9217)
			holder.tv_type_count_money.setText("胆拖--三星组六胆拖  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (num.getType() == 1)
			holder.tv_type_count_money.setText("普通--一星复式  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (num.getType() == 2)
			holder.tv_type_count_money.setText("普通--二星复式  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (num.getType() == 3)
			holder.tv_type_count_money.setText("普通--二星组选  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (num.getType() == 4)
			holder.tv_type_count_money.setText("普通--三星复式  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (num.getType() == 5)
			holder.tv_type_count_money.setText("普通--三星组三  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (num.getType() == 6)
			holder.tv_type_count_money.setText("普通--三星组六  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (num.getType() == 7)
			holder.tv_type_count_money.setText("普通--五星复式  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (num.getType() == 8)
			holder.tv_type_count_money.setText("普通--五星通选  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (num.getType() == 9)
			holder.tv_type_count_money.setText("普通--大小单双  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");

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
