package com.sm.sls_app.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.DtMatch_Basketball;
import com.sm.sls_app.ui.Select_jclqActivity;
import com.sm.sls_app.view.IphoneTreeView;
import com.sm.sls_app.view.Select_sfc_hhtz_jclq_Dialog;
import com.sm.sls_app.view.IphoneTreeView.IphoneTreeHeaderAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpandAdapter_jclq extends BaseExpandableListAdapter implements IphoneTreeHeaderAdapter{

	private Context context;

	/** 组项 **/
	private List<String> mGroups;
	/** 子项 **/
	public static List<List<DtMatch_Basketball>> list_Matchs;

	private Select_jclqActivity activity;

	private Map<Integer, Integer> groupStatusMap;
	private IphoneTreeView ip;
	private int playType = 1;

	/** 所选的集合 第一个Map装 对阵 第二个Map装 所选的 值 **/
	public static HashMap<Integer, HashMap<Integer, String>> map_hashMap_sf = new HashMap<Integer, HashMap<Integer, String>>();
	public static HashMap<Integer, HashMap<Integer, String>> map_hashMap_dx = new HashMap<Integer, HashMap<Integer, String>>();
	public static HashMap<Integer, HashMap<Integer, String>> map_hashMap_cbf = new HashMap<Integer, HashMap<Integer, String>>();
	public static HashMap<Integer, HashMap<Integer, String>> map_hashMap_rfsf = new HashMap<Integer, HashMap<Integer, String>>();
	public static HashMap<Integer, HashMap<Integer, String>> map_hashMap_hhtz = new HashMap<Integer, HashMap<Integer, String>>();

	public ExpandAdapter_jclq(Context context,
			List<List<DtMatch_Basketball>> listMatch,IphoneTreeView ip) {
		this.context = context;
		this.ip=ip;
		activity = (Select_jclqActivity) context;
		setList_Matchs(listMatch);
		groupStatusMap = new HashMap<Integer, Integer>();
	}

	/** 给数组赋值 */
	public void setGroup() {
		mGroups = new ArrayList<String>();
		mGroups.clear();
		for (int i = 0; i < list_Matchs.size(); i++) {
			if (list_Matchs.get(i).size() == 0) {
				continue;
			}
			mGroups.add(list_Matchs.get(i).get(0).getStopSellTime()
					.substring(0, 10)+"  "+list_Matchs.get(i).get(0).getMatchDate2());
		}

	}

	/** 设置子项集合的值 */
	public void setList_Matchs(List<List<DtMatch_Basketball>> _list_Matchs) {
		list_Matchs = new ArrayList<List<DtMatch_Basketball>>();
		for (List<DtMatch_Basketball> list : _list_Matchs) {
			List<DtMatch_Basketball> listM = new ArrayList<DtMatch_Basketball>();
			for (DtMatch_Basketball dt : list) {
				listM.add(dt);
			}
			list_Matchs.add(listM);
		}
		// Log.i("x", "赋值的长度"+list_Matchs.size());
		setGroup();
	}

	/** 设置 玩法 **/
	public void setPlayType(int _playType) {
		this.playType = _playType;
	}

	/** 设置 玩法 **/
	public int getPlayType() {
		return this.playType;
	}

	// 得到子项
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return list_Matchs.get(groupPosition).get(childPosition);
	}

	// 得到子项ID
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	// 得到子类的数量
	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return list_Matchs.get(arg0).size();
	}

	// 得到组
	@Override
	public Object getGroup(int position) {
		// TODO Auto-generated method stub
		// Log.i("x", "getGroup position"+position);
		return mGroups.get(position);
	}

	// 得到组的数量
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mGroups.size();
	}

	// 得到组ID
	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	// 组 是否被选中
	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	// 得到组视图
	@Override
	public View getGroupView(int groupPosition, boolean b, View convertView,
			ViewGroup arg3) {

		if (groupPosition > (list_Matchs.size() - 1))
			return null;

		GroupViewHolder holder;
		if (convertView == null) {
			holder = new GroupViewHolder();
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.select_jczq_spf_groups,
					null);
			holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			holder.tv_count = (TextView) convertView
					.findViewById(R.id.tv_playCount);
			convertView.setTag(holder);
		} else {
			holder = (GroupViewHolder) convertView.getTag();
		}

		// 更改背景
		if (b) {
			convertView
					.setBackgroundResource(R.drawable.select_jc_lv_parent_up);
		} else {
			convertView
					.setBackgroundResource(R.drawable.select_jc_lv_parent_down);
		}
		holder.tv_date.setText(mGroups.get(groupPosition));
		holder.tv_count
				.setText(list_Matchs.get(groupPosition).size() + "场比赛可投");

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// Log.i("x", "父下标"+ groupPosition+"----子下标---"+childPosition);
		// Log.i("x","父下标"+groupPosition+"---长度"+list_Matchs.size());

		if (groupPosition > (list_Matchs.size() - 1)
				|| childPosition > (list_Matchs.get(groupPosition).size() - 1))
			return null;

		final int groupId = groupPosition;
		final int index = childPosition;
		final ChildViewHolder chiHolder;
		if (convertView == null) {
			chiHolder = new ChildViewHolder();
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.select_jclq_items, null);
			chiHolder.mainTeam = (TextView) convertView
					.findViewById(R.id.ll_tv_mainTeam);
			chiHolder.guestTeam = (TextView) convertView
					.findViewById(R.id.ll_tv_cusTeam);
			chiHolder.ll_tv_vs = (TextView) convertView
					.findViewById(R.id.ll_tv_vs);
			chiHolder.mItemId = (TextView) convertView
					.findViewById(R.id.spf_tv_id);
			chiHolder.mItemName = (TextView) convertView
					.findViewById(R.id.spf_tv_name);
			chiHolder.mItemTime = (TextView) convertView
					.findViewById(R.id.spf_tv_time);
			chiHolder.ll_sf = (LinearLayout) convertView
					.findViewById(R.id.right_bottom_ll_sf);
			chiHolder.ll_dx = (LinearLayout) convertView
					.findViewById(R.id.right_bottom_ll_dxf);
			chiHolder.ll_sfc = (LinearLayout) convertView
					.findViewById(R.id.right_bottom_ll_sfc);
			chiHolder.ll_hhtz = (LinearLayout) convertView
					.findViewById(R.id.ll_hhtz);
			/** 胜负 －让分胜负 BUTTON **/
			chiHolder.btn_win = (Button) convertView
					.findViewById(R.id.btn_mainWin);
			chiHolder.btn_lose = (Button) convertView
					.findViewById(R.id.btn_lose);
			/** 大小 BUTTON **/
			chiHolder.btn_big = (Button) convertView.findViewById(R.id.btn_big);
			chiHolder.btn_small = (Button) convertView
					.findViewById(R.id.btn_small);
			/** 胜分差 BUTTON **/
			chiHolder.btn_open = (Button) convertView
					.findViewById(R.id.btn_open);
			/** 混合投注 buuton **/
			chiHolder.btn_mainwin = (Button) convertView
					.findViewById(R.id.rf_mainwin);
			chiHolder.btn_mainlos = (Button) convertView
					.findViewById(R.id.rf_mainlos);
			chiHolder.btn_bigscore = (Button) convertView
					.findViewById(R.id.dx_bigscore);
			chiHolder.btn_smallscore = (Button) convertView
					.findViewById(R.id.dx_smallscore);
			chiHolder.tv_expansion = (TextView) convertView
					.findViewById(R.id.tv_expansion);
			chiHolder.tv_rangfen = (TextView) convertView
					.findViewById(R.id.tv_rangfen);
			chiHolder.tv_daxiao_score = (TextView) convertView
					.findViewById(R.id.tv_daxiao_score);
			chiHolder.ll_rf_stop = (LinearLayout) convertView
					.findViewById(R.id.ll_stop);
			chiHolder.ll_dx_stop = (LinearLayout) convertView
					.findViewById(R.id.ll_dx_stop);
			convertView.setTag(chiHolder);
		} else {
			chiHolder = (ChildViewHolder) convertView.getTag();
		}
		final DtMatch_Basketball dtm = list_Matchs.get(groupPosition).get(
				childPosition);
		chiHolder.mItemId.setText(dtm.getMatchNumber()); // 对阵编号
		chiHolder.mainTeam.setText(dtm.getMainTeam());
		chiHolder.guestTeam.setText(dtm.getGuestTeam());
		chiHolder.mItemName.setText(dtm.getGame());
		chiHolder.mItemTime.setText(dtm.getStopSellTime().substring(
				dtm.getStopSellTime().length() - 8, 16));
		// 显示不同玩法界面
		System.out.println("playType==================" + playType);
		switch (playType) {
		case 1:
			chiHolder.ll_sf.setVisibility(View.VISIBLE);
			chiHolder.ll_dx.setVisibility(View.GONE);
			chiHolder.ll_sfc.setVisibility(View.GONE);
			chiHolder.ll_hhtz.setVisibility(View.GONE);
			chiHolder.ll_tv_vs.setText("vs");
			chiHolder.ll_tv_vs.setTextColor(activity.getResources().getColor(
					R.color.gray));
			chiHolder.btn_win.setText("主胜" + dtm.getMainWin());
			chiHolder.btn_lose.setText("主负" + dtm.getMainLose());
			chiHolder.btn_win
					.setBackgroundResource(R.drawable.select_sfc_lv_item_left);
			chiHolder.btn_lose
					.setBackgroundResource(R.drawable.select_sfc_lv_item_right);
			chiHolder.btn_win.setTextColor(activity.getResources().getColor(
					R.color.select_pop_lv_tv));
			chiHolder.btn_lose.setTextColor(activity.getResources().getColor(
					R.color.select_pop_lv_tv));
			break;
		case 2:
			chiHolder.ll_sf.setVisibility(View.GONE);
			chiHolder.ll_dx.setVisibility(View.VISIBLE);
			chiHolder.ll_sfc.setVisibility(View.GONE);
			chiHolder.ll_hhtz.setVisibility(View.GONE);
			chiHolder.ll_tv_vs.setTextColor(Color.RED);
			chiHolder.ll_tv_vs.setText(dtm.getBigSmallScore());
			chiHolder.btn_big
					.setBackgroundResource(R.drawable.select_sfc_lv_item_left);
			chiHolder.btn_small
					.setBackgroundResource(R.drawable.select_sfc_lv_item_right);
			chiHolder.btn_big.setTextColor(activity.getResources().getColor(
					R.color.select_pop_lv_tv));
			chiHolder.btn_small.setTextColor(activity.getResources().getColor(
					R.color.select_pop_lv_tv));
			chiHolder.btn_big.setText("大分" + dtm.getBig());
			chiHolder.btn_small.setText("小分" + dtm.getSmall());
			break;
		case 3:
			chiHolder.ll_sf.setVisibility(View.VISIBLE);
			chiHolder.ll_dx.setVisibility(View.GONE);
			chiHolder.ll_sfc.setVisibility(View.GONE);
			chiHolder.ll_hhtz.setVisibility(View.GONE);
			chiHolder.ll_hhtz.setVisibility(View.GONE);
			chiHolder.ll_tv_vs.setText(dtm.getLetScore());
			chiHolder.btn_win.setText("主胜" + dtm.getLetMainWin());
			chiHolder.btn_lose.setText("主负" + dtm.getLetMainLose());
			chiHolder.btn_win
					.setBackgroundResource(R.drawable.select_sfc_lv_item_left);
			chiHolder.btn_lose
					.setBackgroundResource(R.drawable.select_sfc_lv_item_right);
			chiHolder.btn_win.setTextColor(activity.getResources().getColor(
					R.color.select_pop_lv_tv));
			chiHolder.btn_lose.setTextColor(activity.getResources().getColor(
					R.color.select_pop_lv_tv));
			if (Double.parseDouble(dtm.getLetScore()) > 0)
				chiHolder.ll_tv_vs.setTextColor(activity.getResources()
						.getColor(R.color.main_red));
			else
				chiHolder.ll_tv_vs.setTextColor(activity.getResources()
						.getColor(R.color.select_jczq_tvcolor_green));
			break;
		case 4:
			chiHolder.ll_sfc.setVisibility(View.VISIBLE);
			chiHolder.ll_sf.setVisibility(View.GONE);
			chiHolder.ll_dx.setVisibility(View.GONE);
			chiHolder.ll_hhtz.setVisibility(View.GONE);
			chiHolder.ll_tv_vs.setTextColor(activity.getResources().getColor(
					R.color.gray));
			chiHolder.ll_tv_vs.setText("vs");
			chiHolder.btn_open.setText("点击展开投注区");
			chiHolder.btn_open.setTextColor(activity.getResources().getColor(
					R.color.select_pop_lv_tv));
			chiHolder.btn_open
					.setBackgroundResource(R.drawable.select_jc_bg_white);
			break;
		case 5:
			chiHolder.ll_sfc.setVisibility(View.GONE);
			chiHolder.ll_sf.setVisibility(View.GONE);
			chiHolder.ll_dx.setVisibility(View.GONE);
			chiHolder.ll_hhtz.setVisibility(View.VISIBLE);
			chiHolder.ll_tv_vs.setText("vs");
			chiHolder.btn_mainwin
					.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
			chiHolder.btn_mainlos
					.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
			chiHolder.btn_bigscore
					.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
			chiHolder.btn_smallscore
					.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
			chiHolder.tv_expansion
					.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
			chiHolder.btn_mainwin.setText("主胜" + dtm.getLetMainWin());
			chiHolder.btn_mainlos.setText("主负" + dtm.getLetMainLose());
			chiHolder.btn_bigscore.setText("大分" + dtm.getBig());
			chiHolder.btn_smallscore.setText("小分" + dtm.getSmall());
			chiHolder.tv_rangfen.setText("主" + dtm.getLetScore());
			chiHolder.tv_daxiao_score.setText(dtm.getBigSmallScore());
			chiHolder.tv_expansion.setText("展开全部");
			chiHolder.btn_mainwin.setTextColor(activity.getResources()
					.getColor(R.color.select_pop_lv_tv));
			chiHolder.btn_mainlos.setTextColor(activity.getResources()
					.getColor(R.color.select_pop_lv_tv));
			chiHolder.btn_bigscore.setTextColor(activity.getResources()
					.getColor(R.color.select_pop_lv_tv));
			chiHolder.btn_smallscore.setTextColor(activity.getResources()
					.getColor(R.color.select_pop_lv_tv));
			chiHolder.ll_rf_stop.setVisibility(View.GONE);
			chiHolder.ll_dx_stop.setVisibility(View.GONE);
			if(!dtm.isSF()){
				chiHolder.ll_rf_stop.setVisibility(View.VISIBLE);
			}
			if(!dtm.isDXF()){
				chiHolder.ll_dx_stop.setVisibility(View.VISIBLE);
			}
			if(Double.parseDouble(dtm.getLetScore())<0)
				chiHolder.tv_rangfen.setTextColor(activity.getResources().getColor(
						R.color.select_jczq_tvcolor_green));
			else
				chiHolder.tv_rangfen.setTextColor(activity.getResources().getColor(
						R.color.select_pop_lv_tv));
			chiHolder.tv_daxiao_score.setTextColor(activity.getResources()
					.getColor(R.color.select_pop_lv_tv));
			break;
		}

		HashMap<Integer, String> hashMap = map_hashMap_sf.get(groupPosition);
		HashMap<Integer, String> hashMap2 = map_hashMap_dx.get(groupPosition);
		HashMap<Integer, String> hashMap3 = map_hashMap_cbf.get(groupPosition);
		HashMap<Integer, String> hashMap4 = map_hashMap_rfsf.get(groupPosition);
		HashMap<Integer, String> hashMap5 = map_hashMap_hhtz.get(groupPosition);

		/** 改变胜负 Button 背景 **/
		if (hashMap != null && playType == 1) {
			if (hashMap.containsKey(index)) {
				if (hashMap.get(index).contains("2")) {
					chiHolder.btn_win.setTextColor(Color.WHITE);
					chiHolder.btn_win
							.setBackgroundResource(R.drawable.select_sfc_lv_item_left_selected);
				}

				if (hashMap.get(index).contains("1")) {
					chiHolder.btn_lose.setTextColor(Color.WHITE);
					chiHolder.btn_lose
							.setBackgroundResource(R.drawable.select_sfc_lv_item_right_selected);
				}
			}
		}
		/** 改变胜负 Button 背景 **/
		if (hashMap3 != null) {
			if (hashMap3.containsKey(index)) {
				String show = "";
				if (null != hashMap3.get(index)) {
					if (hashMap3.get(index).split(",").length > 0) {
						for (int i = 0; i < hashMap3.get(index).split(",").length; i++) {
							if (i == 0)
								show += get_show_sfc(hashMap3.get(index).split(
										",")[i]);
							else
								show += ","
										+ get_show_sfc(hashMap3.get(index)
												.split(",")[i]);
						}
						chiHolder.btn_open.setText(show);
						chiHolder.btn_open.setTextColor(Color.WHITE);
						chiHolder.btn_open
								.setBackgroundResource(R.drawable.select_jc_bg_red);
					}
				}
			}
		}
		/** 改变大小 Button 背景 **/
		if (hashMap4 != null && playType == 3) {
			if (hashMap4.containsKey(index)) {
				if (hashMap4.get(index).contains("2")) {
					chiHolder.btn_win.setTextColor(Color.WHITE);
					chiHolder.btn_win
							.setBackgroundResource(R.drawable.select_sfc_lv_item_left_selected);
				}
				if (hashMap4.get(index).contains("1")) {
					chiHolder.btn_lose.setTextColor(Color.WHITE);
					chiHolder.btn_lose
							.setBackgroundResource(R.drawable.select_sfc_lv_item_right_selected);
				}
			}
		}
		/** 改变大小 Button 背景 **/
		if (hashMap2 != null && playType == 2) {
			if (hashMap2.containsKey(index)) {
				if (hashMap2.get(index).contains("2")) {
					chiHolder.btn_big.setTextColor(Color.WHITE);
					chiHolder.btn_big
							.setBackgroundResource(R.drawable.select_sfc_lv_item_left_selected);
				}

				if (hashMap2.get(index).contains("1")) {
					chiHolder.btn_small.setTextColor(Color.WHITE);
					chiHolder.btn_small
							.setBackgroundResource(R.drawable.select_sfc_lv_item_right_selected);
				}
			}
		}
		/** 改变大小 Button 背景 **/
		if (hashMap5 != null && playType == 5) {
			if (null != hashMap5.get(index) && hashMap5.containsKey(index)) {
				if (hashMap5.get(index).split(",").length > 0)
					chiHolder.tv_expansion.setText("已选\n"
							+ hashMap5.get(index).split(",").length + "项");
				if (hashMap5.get(index).contains("101")) {
					chiHolder.btn_mainwin.setTextColor(Color.WHITE);
					chiHolder.btn_mainwin
							.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				}

				if (hashMap5.get(index).contains("100")) {
					chiHolder.btn_mainlos.setTextColor(Color.WHITE);
					chiHolder.btn_mainlos
							.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				}
				if (hashMap5.get(index).contains("301")) {
					chiHolder.btn_bigscore.setTextColor(Color.WHITE);
					chiHolder.btn_bigscore
							.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				}
				if (hashMap5.get(index).contains("300")) {
					chiHolder.btn_smallscore.setTextColor(Color.WHITE);
					chiHolder.btn_smallscore
							.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				}
			}
		}
		// 按钮点击事件
		chiHolder.btn_win.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HashMap<Integer, HashMap<Integer, String>> map_select = new HashMap<Integer, HashMap<Integer, String>>();
				if (playType == 1)
					map_select = map_hashMap_sf;
				else if (playType == 3)
					map_select = map_hashMap_rfsf;
				HashMap<Integer, String> hash = new HashMap<Integer, String>();
				if (map_select.containsKey(groupId))
					hash = map_select.get(groupId);
				if (hash.containsKey(index)) {
					if (hash.get(index).contains("2")) {
						hash.put(index, hash.get(index).replace("2", ""));
						chiHolder.btn_win
								.setBackgroundResource(R.drawable.select_sfc_lv_item_left);
						chiHolder.btn_win.setTextColor(activity.getResources()
								.getColor(R.color.select_pop_lv_tv));
						if (hash.get(index).length() == 0) {
							map_select.get(groupId).remove(index);
						}
						activity.changeTextShow();
						return;
					} else {
						hash.put(index, hash.get(index) + "2");
					}
				} else {
					hash.put(index, "2");
				}
				if (hash.get(index).length() == 0) {
					hash.remove(index);
				}
				map_select.put(groupId, hash);
				chiHolder.btn_win
						.setBackgroundResource(R.drawable.select_sfc_lv_item_left_selected);
				chiHolder.btn_win.setTextColor(Color.WHITE);
				if (playType == 1)
					map_hashMap_sf = map_select;
				else if (playType == 3)
					map_hashMap_rfsf = map_select;
				activity.changeTextShow();
			}
		});

		// 按钮点击事件
		chiHolder.btn_lose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HashMap<Integer, HashMap<Integer, String>> map_select = new HashMap<Integer, HashMap<Integer, String>>();
				if (playType == 1)
					map_select = map_hashMap_sf;
				else if (playType == 3)
					map_select = map_hashMap_rfsf;
				HashMap<Integer, String> hash = new HashMap<Integer, String>();
				if (map_select.containsKey(groupId))
					hash = map_select.get(groupId);
				if (hash.containsKey(index)) {
					if (hash.get(index).contains("1")) {
						hash.put(index, hash.get(index).replace("1", ""));
						chiHolder.btn_lose
								.setBackgroundResource(R.drawable.select_sfc_lv_item_right);
						chiHolder.btn_lose.setTextColor(activity.getResources()
								.getColor(R.color.select_pop_lv_tv));
						if (hash.get(index).length() == 0) {
							map_select.get(groupId).remove(index);
						}
						activity.changeTextShow();
						return;
					} else {
						hash.put(index, hash.get(index) + "1");
					}
				} else {
					hash.put(index, "1");
				}
				if (hash.get(index).length() == 0) {
					hash.remove(index);
				}
				map_select.put(groupId, hash);
				chiHolder.btn_lose
						.setBackgroundResource(R.drawable.select_sfc_lv_item_right_selected);
				chiHolder.btn_lose.setTextColor(Color.WHITE);
				if (playType == 1)
					map_hashMap_sf = map_select;
				else if (playType == 3)
					map_hashMap_rfsf = map_select;
				activity.changeTextShow();
			}
		});

		// 大分 按钮点击事件
		chiHolder.btn_big.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HashMap<Integer, String> hash = new HashMap<Integer, String>();
				if (map_hashMap_dx.containsKey(groupId))
					hash = map_hashMap_dx.get(groupId);
				if (hash.containsKey(index)) {
					if (hash.get(index).contains("2")) {
						hash.put(index, hash.get(index).replace("2", ""));
						chiHolder.btn_big
								.setBackgroundResource(R.drawable.select_sfc_lv_item_left);
						chiHolder.btn_big.setTextColor(activity.getResources()
								.getColor(R.color.select_pop_lv_tv));
						if (hash.get(index).length() == 0) {
							map_hashMap_dx.get(groupId).remove(index);
						}
						activity.changeTextShow();
						return;
					} else {
						hash.put(index, hash.get(index) + "2");
					}
				} else {
					hash.put(index, "2");
				}
				if (hash.get(index).length() == 0) {
					hash.remove(index);
				}
				map_hashMap_dx.put(groupId, hash);
				chiHolder.btn_big
						.setBackgroundResource(R.drawable.select_sfc_lv_item_left_selected);
				chiHolder.btn_big.setTextColor(Color.WHITE);
				activity.changeTextShow();
			}
		});

		// 按钮点击事件
		chiHolder.btn_small.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HashMap<Integer, String> hash = new HashMap<Integer, String>();
				if (map_hashMap_dx.containsKey(groupId))
					hash = map_hashMap_dx.get(groupId);
				if (hash.containsKey(index)) {
					if (hash.get(index).contains("1")) {
						hash.put(index, hash.get(index).replace("1", ""));
						chiHolder.btn_small
								.setBackgroundResource(R.drawable.select_sfc_lv_item_right);
						chiHolder.btn_small.setTextColor(activity
								.getResources().getColor(
										R.color.select_pop_lv_tv));
						if (hash.get(index).length() == 0) {
							map_hashMap_dx.get(groupId).remove(index);
						}
						activity.changeTextShow();
						return;
					} else {
						hash.put(index, hash.get(index) + "1");
					}
				} else {
					hash.put(index, "1");
				}
				if (hash.get(index).length() == 0) {
					hash.remove(index);
				}
				map_hashMap_dx.put(groupId, hash);
				chiHolder.btn_small
						.setBackgroundResource(R.drawable.select_sfc_lv_item_right_selected);
				chiHolder.btn_small.setTextColor(Color.WHITE);
				activity.changeTextShow();
			}
		});
		// 胜分差展开
		chiHolder.btn_open.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Select_sfc_hhtz_jclq_Dialog dialog = new Select_sfc_hhtz_jclq_Dialog(
						activity, R.style.dialog, 0, groupId, index, dtm,
						map_hashMap_cbf);
				dialog.show();
			}
		});
		// 混合投注展开 和 混合界面点击
		chiHolder.tv_expansion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Select_sfc_hhtz_jclq_Dialog dialog = new Select_sfc_hhtz_jclq_Dialog(
						activity, R.style.dialog, 1, groupId, index, dtm,
						map_hashMap_hhtz);
				dialog.show();
			}
		});
		chiHolder.btn_mainwin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				select_huntou(groupId,index,1);
			}
		});
		chiHolder.btn_mainlos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				select_huntou(groupId,index,0);
			}
		});
		chiHolder.btn_bigscore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				select_huntou(groupId,index,3);
			}
		});
		chiHolder.btn_smallscore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				select_huntou(groupId,index,2);
			}
		});
		return convertView;
	}
	private String[] select_n = {"100","101","300","301"};
	private void select_huntou(int groupId, int index , int btn_type){
		// TODO Auto-generated method stub
		List<String> result = new ArrayList<String>();
		
		if (map_hashMap_hhtz != null) {
			if (map_hashMap_hhtz.get(groupId) != null && map_hashMap_hhtz.get(groupId).get(index) != null) {
				for (int i = 0; i < map_hashMap_hhtz.get(groupId).get(index).split(",").length; i++) {
					result.add(map_hashMap_hhtz.get(groupId).get(index).split(",")[i]);
					System.out.println("setresult" + groupId + "--" + index
							+ result.get(i));
				}
			}
		}
		if (result.contains(select_n[btn_type]))
			result.remove(select_n[btn_type]);
		else
			result.add(select_n[btn_type]);

		HashMap<Integer, String> map = ExpandAdapter_jclq.map_hashMap_hhtz.get(groupId);
		if (map == null) {
			map = new HashMap<Integer, String>();
		}
		map.remove(index);
		String re = "";
		for (int i = 0; i < result.size(); i++) {
			if (i == 0)
				re += result.get(i);
			else
				re += "," + result.get(i);
			map.put(index, re);
		}
		ExpandAdapter_jclq.map_hashMap_hhtz.put(groupId, map);
		activity.updateAdapter();
	
	}
	/** 组控件 */
	static class GroupViewHolder {
		TextView tv_date;
		TextView tv_count;
	}

	/**
	 * 子类控件
	 * 
	 * @author SLS003
	 */
	static class ChildViewHolder {
		TextView mItemId, mItemName, mItemTime;
		TextView ll_tv_vs;
		TextView mainTeam, guestTeam;
		TextView tv_expansion;
		LinearLayout ll_sf, ll_dx, ll_sfc, ll_hhtz;
		Button btn_win, btn_lose, btn_big, btn_small;
		Button btn_open;// 展开胜分差
		Button btn_mainwin, btn_mainlos, btn_bigscore, btn_smallscore;// 展开胜分差
		TextView tv_rangfen, tv_daxiao_score;
		LinearLayout ll_rf_stop,ll_dx_stop;
	}

	public String get_show_sfc(String key) {
		int a = Integer.parseInt(key);
		switch (a - 1) {
		case 0:
			return "主负1-5";
		case 1:
			return "主胜1-5";
		case 2:
			return "主负6-10";
		case 3:
			return "主胜6-10";
		case 4:
			return "主负11-15";
		case 5:
			return "主胜11-15";
		case 6:
			return "主负16-20";
		case 7:
			return "主胜16-20";
		case 8:
			return "主负21-25";
		case 9:
			return "主胜21-25";
		case 10:
			return "主负26+";
		case 11:
			return "主胜26+";
		default:
			break;
		}
		return null;
	}

	@Override
	public int getTreeHeaderState(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		final int childCount = getChildrenCount(groupPosition);
		if (childPosition == childCount - 1) {
			return PINNED_HEADER_PUSHED_UP;
		} else if (childPosition == -1 && !ip.isGroupExpanded(groupPosition)) {
			return PINNED_HEADER_GONE;
		} else {
			return PINNED_HEADER_VISIBLE;
		}
	}

	@Override
	public void configureTreeHeader(View header, int groupPosition,
			int childPosition, int alpha) {
		// TODO Auto-generated method stub
		TextView tv_date = (TextView) header.findViewById(R.id.tv_date);
		TextView tv_count = (TextView) header.findViewById(R.id.tv_playCount);
		header.setBackgroundResource(R.drawable.select_jc_lv_parent_up);
//		header.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT,30));
		tv_date.setText(mGroups.get(groupPosition));
		tv_count.setText(list_Matchs.get(groupPosition).size() + "场比赛可投");
	}

	@Override
	public void onHeadViewClick(int groupPosition, int status) {
		groupStatusMap.put(groupPosition, status);

	}

	@Override
	public int getHeadViewClickStatus(int groupPosition) {
		// TODO Auto-generated method stub
		if (groupStatusMap.containsKey(groupPosition)) {
			return groupStatusMap.get(groupPosition);
		} else {
			return 0;
		}
	}

}
