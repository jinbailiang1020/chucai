package com.sm.sls_app.ui.adapter;

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

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.TeamArray;
import com.sm.sls_app.ui.Buy_RX9_Activit;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.NumberTools;
import com.sm.sls_app.view.MyToast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RX9_TeamArrayAdapter extends BaseAdapter {
	// 上下文本
	private Context context;

	private List<TeamArray> list_TeamArray;
	private Buy_RX9_Activit betActivity;
	private Vibrator vibrator; // 震动器
	private long count = 0; // 投注注数

	public static Map<Integer, String> btnMap = new HashMap<Integer, String>();

	public RX9_TeamArrayAdapter(Context context, List<TeamArray> TeamArray,
			Vibrator vibrator) {
		this.context = context;
		this.list_TeamArray = TeamArray;
		this.vibrator = vibrator;
		betActivity = (Buy_RX9_Activit) context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_TeamArray.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list_TeamArray.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		final ViewHolder holder;
		final int index = position;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			// 得到布局文件
			view = inflater.inflate(R.layout.select_lv_item, null);
			// 得到控件
			holder.select_lv_item_tv_mainteam = (TextView) view
					.findViewById(R.id.select_lv_item_tv_mainteam);
			holder.select_lv_item_tv_guessteam = (TextView) view
					.findViewById(R.id.select_lv_item_tv_guessteam);
			holder.select_lv_item_tv_num = (TextView) view
					.findViewById(R.id.select_lv_item_tv_num);
			holder.select_lv_item_tv_matchname = (TextView) view
					.findViewById(R.id.select_lv_item_tv_matchname);
			holder.select_lv_item_tv_time = (TextView) view
					.findViewById(R.id.select_lv_item_tv_time);
			holder.select_lv_item_btn_left = (Button) view
					.findViewById(R.id.select_lv_item_btn_left);
			holder.select_lv_item_btn_center = (Button) view
					.findViewById(R.id.select_lv_item_btn_center);
			holder.select_lv_item_btn_right = (Button) view
					.findViewById(R.id.select_lv_item_btn_right);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		TeamArray teamArray = list_TeamArray.get(position);
		holder.select_lv_item_tv_mainteam.setText(teamArray.getMainTeam());
		holder.select_lv_item_tv_num.setText(teamArray.getId());
		holder.select_lv_item_tv_matchname.setText(teamArray.getTime().split(" ")[0]+teamArray.getGame());
		holder.select_lv_item_tv_time.setText(teamArray.getTime().split(" ")[1]+teamArray.getMatchDate().trim());
		holder.select_lv_item_tv_guessteam.setText(teamArray.getGuestTeam());
		holder.select_lv_item_btn_left
				.setBackgroundResource(R.drawable.select_sfc_lv_item_left);
		holder.select_lv_item_btn_center
				.setBackgroundResource(R.drawable.select_sfc_lv_item_center);
		holder.select_lv_item_btn_right
				.setBackgroundResource(R.drawable.select_sfc_lv_item_right);
		holder.select_lv_item_btn_left.setTextColor(Color.GRAY);
		holder.select_lv_item_btn_center.setTextColor(Color.GRAY);
		holder.select_lv_item_btn_right.setTextColor(Color.GRAY);
		holder.select_lv_item_btn_left.setEnabled(true);
		holder.select_lv_item_btn_center.setEnabled(true);
		holder.select_lv_item_btn_right.setEnabled(true);
		if (btnMap.get(index) != null) {
			String Str = btnMap.get(index);
			if (Str.contains("1")) {
				holder.select_lv_item_btn_center
						.setBackgroundResource(R.drawable.select_sfc_lv_item_center_selected);
				holder.select_lv_item_btn_center.setTextColor(Color.WHITE);
			}
			if (Str.contains("0")) {
				holder.select_lv_item_btn_right
						.setBackgroundResource(R.drawable.select_sfc_lv_item_right_selected);
				holder.select_lv_item_btn_right.setTextColor(Color.WHITE);
			}
			if (Str.contains("3")) {
				holder.select_lv_item_btn_left
						.setBackgroundResource(R.drawable.select_sfc_lv_item_left_selected);
				holder.select_lv_item_btn_left.setTextColor(Color.WHITE);
			}
		}

		// 是否被选中
		boolean isok = false;
		if (btnMap.size() == 9) {
			Iterator i = btnMap.entrySet().iterator();
			while (i.hasNext()) {
				Entry entry = (Entry) i.next();
				int key = Integer.parseInt(entry.getKey().toString());
				if (index == key) {
					isok = true;
				}
			}
			// if (isok == false) {
			// holder.select_lv_item_btn_left.setEnabled(false);
			// holder.select_lv_item_btn_center.setEnabled(false);
			// holder.select_lv_item_btn_right.setEnabled(false);
			// // MyToast.getToast(betActivity, "您已选择9场比赛").show();
			// }
		}

		holder.select_lv_item_btn_right
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if (btnMap.size() == 9 && !btnMap.containsKey(index)) {
							MyToast.getToast(context, "您已选择9场比赛").show();
							return;
						}
						if (vibrator != null)
							vibrator.vibrate(100);

						String str = "";
						if (btnMap.get(index) != null) {
							str = btnMap.get(index);
						}

						if (str.contains("0")) {
							str = str.replace("0", "");
						} else {
							str += "0";
						}

						if (str.length() == 0) {
							btnMap.remove(index);
						} else {
							btnMap.put(index, str);
						}
						if (btnMap.size() == 9) {
							getTotalCount(btnMap);
						} else {
							count = 0;
						}
						AppTools.totalCount = count;
						betActivity.updateAdapter();
					}

				});

		holder.select_lv_item_btn_center
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (btnMap.size() == 9 && !btnMap.containsKey(index)) {
							MyToast.getToast(context, "您已选择9场比赛").show();
							return;
						}
						// TODO Auto-generated method stub
						if (vibrator != null)
							vibrator.vibrate(100);
						String str = "";
						if (btnMap.get(index) != null) {
							str = btnMap.get(index);
						}

						if (str.contains("1")) {
							str = str.replace("1", "");
						} else {
							str += "1";
						}
						if (str.length() == 0) {
							btnMap.remove(index);
						} else {
							btnMap.put(index, str);
						}

						if (btnMap.size() == 9) {
							getTotalCount(btnMap);
						} else {
							count = 0;
						}
						AppTools.totalCount = count;
						betActivity.updateAdapter();
					}
				});

		holder.select_lv_item_btn_left
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (btnMap.size() == 9 && !btnMap.containsKey(index)) {
							MyToast.getToast(context, "您已选择9场比赛").show();
							return;
						}
						if (vibrator != null)
							vibrator.vibrate(100);
						String str = "";
						if (btnMap.get(index) != null) {
							str = btnMap.get(index);
						}

						if (str.contains("3")) {
							str = str.replace("3", "");
						} else {
							str += "3";
						}
						if (str.length() == 0) {
							btnMap.remove(index);
						} else {
							btnMap.put(index, str);
						}
						if (btnMap.size() == 9) {
							getTotalCount(btnMap);
						} else {
							count = 0;
						}
						AppTools.totalCount = count;
						betActivity.updateAdapter();
					}
				});
		return view;
	}

	/** 计算总注数 */
	private void getTotalCount(Map<Integer, String> btnMap) {
		count = NumberTools.getCountForRX9(btnMap);
	}

	/** 静态类 */
	static class ViewHolder {
		TextView select_lv_item_tv_mainteam, select_lv_item_tv_guessteam,
				select_lv_item_tv_num, select_lv_item_tv_matchname,
				select_lv_item_tv_time;
		Button select_lv_item_btn_left, select_lv_item_btn_center,
				select_lv_item_btn_right;
	}
}
