package com.sm.sls_app.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.ui.SelectNumberActivityPL3;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.NumberTools;
import com.sm.sls_app.view.VibratorView;

import java.util.HashSet;

/** 红球的显示 */
public class MyGridViewAdapterPL3 extends BaseAdapter {

	public static int playType = 6301; // 选号类型
	// 上下文本
	private Context context;
	// 装彩票的集合
	private Integer[] Numbers;
	private int color;
	private int type = 1; // 投注界面类型

	private Vibrator vibrator; // 震动器

	private long count = 0; // 投注注数

	public static String LotteryId = "63";

	// 存放胆区红球的 集合
	public static HashSet<String> baiSet = new HashSet<String>();
	public static HashSet<String> shiSet = new HashSet<String>();
	public static HashSet<String> geSet = new HashSet<String>();
	public static HashSet<String> hezhiSet = new HashSet<String>();
	public static HashSet<String> zixuanhezhiSet = new HashSet<String>();
	// 选球界面
	private SelectNumberActivityPL3 activity;

	/** 构造方法 实现初始化 */
	public MyGridViewAdapterPL3(Context context, Integer[] Numbers, int c,
			int type) {
		this.context = context;
		this.Numbers = Numbers;
		this.color = c;
		this.type = type;
		vibrator = VibratorView.getVibrator(context);
		activity = (SelectNumberActivityPL3) context;
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

		if (Color.RED == color){
			holder.btn.setTextColor(context.getResources()
					.getColor(R.color.main_red));
			holder.btn.setBackgroundResource(R.drawable.icon_ball_red_unselected);
		}else{
			holder.btn.setTextColor(context.getResources().getColor(
					R.color.blue));
			holder.btn.setBackgroundResource(R.drawable.icon_ball_blue_unselected);
		}

		int num = position;
		if(type == 4)
			num = position+1;
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
		if (type == 4 ) {
			// 看是否被选中
			if (hezhiSet.contains(str)) {
				holder.btn.setBackgroundResource(R.drawable.icon_ball_red_selected);
				holder.btn.setTextColor(Color.WHITE);
			}
		}
		if (type == 5) {
			// 看是否被选中
			if (zixuanhezhiSet.contains(str)) {
				holder.btn.setBackgroundResource(R.drawable.icon_ball_red_selected);
				holder.btn.setTextColor(Color.WHITE);
			}
		}

		// 给按钮添加点击事件
		holder.btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != vibrator) {
					// 震动 80 毫秒
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
				activity.tv_tatol_count.setText( AppTools.totalCount + "");
				activity.tv_tatol_money.setText((AppTools.totalCount * 2) + "");
			}
		});		
//		getTotalCount(playType);
//		activity.tv_tatol_count.setText( AppTools.totalCount + "");
//		activity.tv_tatol_money.setText((AppTools.totalCount * 2) + "");
		return view;
	}

	/**
	 * 选球
	 * @param playType
	 *            玩法
	 * @param type
	 *            选球位置
	 * @param content
	 *            点击的球上面显示的数字
	 */
	private void selectNumber(int playType, int type, String content) {
		MyGridViewAdapterPL3.playType = playType;
		if ( playType == 6301) {
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
			}
		} else if ( playType == 6302 && SelectNumberActivityPL3.flag ) {
			switch (type) {
			case 1:
				if (shiSet.contains(content)) {
					shiSet.clear();
				}
				if(geSet.contains(content)) {
					geSet.clear();
				}
				if (baiSet.contains(content)) {
					baiSet.clear();
				}
				else {
					baiSet.clear();
					baiSet.add(content);
				}
				break;
			case 2:
				if (baiSet.contains(content)) {
					baiSet.clear();
				}
				if(geSet.contains(content)) {
					geSet.clear();
				}
				if (shiSet.contains(content)) {
					shiSet.clear();
				} else {
					shiSet.clear();
					shiSet.add(content);
				}
				break;
			case 3:
				if (baiSet.contains(content)) {
					baiSet.clear();
				}
				if (shiSet.contains(content)) {
					shiSet.clear();
				}
				if (geSet.contains(content)) {
					geSet.clear();
				} else {
					geSet.clear();
					geSet.add(content);
				}
				break;

			}
		} else if (playType == 6302 && !SelectNumberActivityPL3.flag) {

			if (baiSet.contains(content)) {
				baiSet.remove(content);
			} else {
				 if (baiSet.size() >= 3) {
                     Toast.makeText(context, "只能选择3个号码",
                             Toast.LENGTH_SHORT).show();
                 } else {
                	 baiSet.add(content);
                 }
			}

		}else if ( playType == 6306) {

			if (hezhiSet.contains(content)) {
				hezhiSet.remove(content);
			} else
				hezhiSet.add(content);
		} else if (playType == 6305) {

			if (zixuanhezhiSet.contains(content)) {
				zixuanhezhiSet.remove(content);
			} else
				zixuanhezhiSet.add(content);
		} else {
			if (baiSet.contains(content)) {
				baiSet.remove(content);
			} else {
				baiSet.add(content);
			}
		}

	}

	/** 计算总注数 */
	private void getTotalCount(int playType) {
		
		count = NumberTools.getCountForFC3D(playType, baiSet.size(),
				shiSet.size(), geSet.size(),hezhiSet,zixuanhezhiSet);
		AppTools.totalCount = count;
	}

	/** 将传进来的值选中 */
	public void setNumByRandom() {
		activity.updateAdapter();
		getTotalCount(playType);
		AppTools.totalCount = count;
		activity.tv_tatol_count.setText( AppTools.totalCount + "");
		activity.tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}

	/** 静态类 */
	static class ViewHolder {
		TextView btn;
	}

}
