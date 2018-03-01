package com.sm.sls_app.ui.adapter;

import java.util.HashSet;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.ui.SelectNumberActivityPL5_QXC;
import com.sm.sls_app.ui.adapter.MyGridViewAdapter.ViewHolder;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.NumberTools;
import com.sm.sls_app.view.VibratorView;

import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/** 红球的显示 */
public class MyGridViewAdapterPL5_QXC extends BaseAdapter {

	public static int playType = 1; // 选号类型
	// 上下文本
	private Context context;
	// 装彩票的集合
	private Integer[] Numbers;
	private int color;
	private int type = 301; // 投注界面类型

	private Vibrator vibrator; // 震动器

	private long count = 0; // 投注注数

	// 存放胆区红球的 集合
	public static HashSet<String> baiSet = new HashSet<String>();

	public static HashSet<String> shiSet = new HashSet<String>();
	public static HashSet<String> geSet = new HashSet<String>();
	public static HashSet<String> siSet = new HashSet<String>();
	public static HashSet<String> wuSet = new HashSet<String>();
	public static HashSet<String> liuSet = new HashSet<String>();
	public static HashSet<String> qiSet = new HashSet<String>();

	// 选球界面
	private SelectNumberActivityPL5_QXC activity;

	/** 构造方法 实现初始化 */
	public MyGridViewAdapterPL5_QXC(Context context, Integer[] Numbers, int c,
			int type) {
		this.context = context;
		this.Numbers = Numbers;
		this.color = c;
		this.type = type;
		vibrator = VibratorView.getVibrator(context);
		activity = (SelectNumberActivityPL5_QXC) context;
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

	/** 得到View */
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

		// 设置字体显示颜色 和背景图片
		if (Color.RED == color) {
			holder.btn.setTextColor(context.getResources().getColor(
					R.color.main_red));
			holder.btn
					.setBackgroundResource(R.drawable.icon_ball_red_unselected);
		} else {
			holder.btn.setTextColor(context.getResources().getColor(
					R.color.blue));
			holder.btn
					.setBackgroundResource(R.drawable.icon_ball_blue_unselected);
		}

		int num = position;
		String str = num + "";
		if (type == 1) {
			// 看是否被选中
			if (baiSet.contains(str)) {
				holder.btn.setBackgroundResource(R.drawable.icon_ball_red_selected);
				holder.btn.setTextColor(Color.WHITE);
			}
		}
		if (type == 2) {
			// 看是否被选中
			if (shiSet.contains(str)) {
				holder.btn.setBackgroundResource(R.drawable.icon_ball_red_selected);
				holder.btn.setTextColor(Color.WHITE);
			}
		}
		if (type == 3) {
			// 看是否被选中
			if (geSet.contains(str)) {
				holder.btn.setBackgroundResource(R.drawable.icon_ball_red_selected);
				holder.btn.setTextColor(Color.WHITE);
			}
		}
		if (type == 4) {
			// 看是否被选中
			if (siSet.contains(str)) {
				holder.btn.setBackgroundResource(R.drawable.icon_ball_red_selected);
				holder.btn.setTextColor(Color.WHITE);
			}
		}
		if (type == 5) {
			// 看是否被选中
			if (wuSet.contains(str)) {
				holder.btn.setBackgroundResource(R.drawable.icon_ball_red_selected);
				holder.btn.setTextColor(Color.WHITE);
			}
		}
		if (type == 6) {
			// 看是否被选中
			if (liuSet.contains(str)) {
				holder.btn.setBackgroundResource(R.drawable.icon_ball_red_selected);
				holder.btn.setTextColor(Color.WHITE);
			}
		}
		if (type == 7) {
			// 看是否被选中
			if (qiSet.contains(str)) {
				holder.btn.setBackgroundResource(R.drawable.icon_ball_red_selected);
				holder.btn.setTextColor(Color.WHITE);
			}
		}

		// 给按钮添加点击事件
		holder.btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != vibrator) {
					// 震动 100 毫秒
					vibrator.vibrate(80);
				}

				// 得到选号的内容
				String content = holder.btn.getText().toString();
				selectNumber(playType, type, content);
				activity.updateAdapter();
				getTotalCount(playType);
				if (count > 10000) {
					Toast.makeText(context, "投注金额不能超过20000", Toast.LENGTH_SHORT)
							.show();
					holder.btn.performClick();
				} else {
					AppTools.totalCount = count;
				}
				// activity.tv_tatol_count.setText( AppTools.totalCount + "");
				// activity.tv_tatol_money.setText((AppTools.totalCount * 2) +
				// "");
			}
		});
		// getTotalCount(playType);
		// AppTools.totalCount = count ;
		activity.tv_tatol_count.setText(AppTools.totalCount + "");
		activity.tv_tatol_money.setText((AppTools.totalCount * 2) + "");
		return view;
	}

	/**
	 * 选球
	 * 
	 * @param playType
	 *            玩法
	 * @param type
	 *            选球位置
	 * @param content
	 *            点击的球上面显示的数字
	 */
	private void selectNumber(int playType, int type, String content) {
		MyGridViewAdapterPL5_QXC.playType = playType;
		if (playType == 1) {
			switch (type) {
			case 1:
				if (baiSet.contains(content)) {
					baiSet.remove(content);
				} else {
					baiSet.add(content);
				}
				break;
			case 2:
				if (shiSet.contains(content)) {
					shiSet.remove(content);
				} else {
					shiSet.add(content);
				}
				break;
			case 3:
				if (geSet.contains(content)) {
					geSet.remove(content);
				} else {
					geSet.add(content);
				}
				break;
			case 4:
				if (siSet.contains(content)) {
					siSet.remove(content);
				} else {
					siSet.add(content);
				}
				break;
			case 5:
				if (wuSet.contains(content)) {
					wuSet.remove(content);
				} else {
					wuSet.add(content);
				}
				break;
			case 6:
				if (liuSet.contains(content)) {
					liuSet.remove(content);
				} else {
					liuSet.add(content);
				}
				break;
			case 7:
				if (qiSet.contains(content)) {
					qiSet.remove(content);
				} else {
					qiSet.add(content);
				}
				break;
			}

		}

	}

	/** 计算总注数 */
	private void getTotalCount(int playType) {
		if ("3".equals(AppTools.lottery.getLotteryID())) {
			count = NumberTools.getCountForQXC(playType, baiSet.size(),
					shiSet.size(), geSet.size(), siSet.size(), wuSet.size(),
					liuSet.size(), qiSet.size());
		} else {
			count = NumberTools.getCountForPL5(playType, baiSet.size(),
					shiSet.size(), geSet.size(), siSet.size(), wuSet.size());
		}
	}

	/**
	 * 将传进来的值选中
	 * 
	 * @param red
	 * @param bluez
	 */
	public void setNumByRandom() {
		activity.updateAdapter();
		getTotalCount(playType);
		AppTools.totalCount = count;
		activity.tv_tatol_count.setText(AppTools.totalCount + "");
		activity.tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}

	/** 静态类 */
	static class ViewHolder {
		TextView btn;
	}

}
