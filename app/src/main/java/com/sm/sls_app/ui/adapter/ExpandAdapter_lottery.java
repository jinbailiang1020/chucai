package com.sm.sls_app.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.LotteryDtMatch;
import com.sm.sls_app.ui.WinLottery_jc_Activity;
import com.sm.sls_app.view.IphoneTreeView;
import com.sm.sls_app.view.IphoneTreeView.IphoneTreeHeaderAdapter;

@SuppressLint("UseSparseArrays")
public class ExpandAdapter_lottery extends BaseExpandableListAdapter implements
		IphoneTreeHeaderAdapter {

	public Context context;

	/** 组项 **/
	private List<String> mGroups;
	/** 子项 **/
	private List<List<LotteryDtMatch>> list_Matchs;

	/** 玩法 **/
	private int lotteryId = 1;
	private IphoneTreeView ip;
	private HashMap<Integer, Integer> groupStatusMap;

	public ExpandAdapter_lottery(Context context,
			List<List<LotteryDtMatch>> listMatch, int _lotteryId,
			IphoneTreeView ip) {
		this.context = context;
		setList_Matchs(listMatch);
		this.lotteryId = _lotteryId;
		this.ip = ip;
		groupStatusMap = new HashMap<Integer, Integer>();
	}

	/** 给数组赋值 */
	private void setGroup() {
		mGroups = new ArrayList<String>();
		mGroups.clear();
		for (int i = 0; i < list_Matchs.size(); i++) {
			if (list_Matchs.get(i).size() == 0) {
				continue;
			}
			System.out.println(list_Matchs.get(i).get(0).getMatchDate());

			mGroups.add(list_Matchs.get(i).get(0).getMatchDate() + "#"
					+ list_Matchs.get(i).get(0).getWeekDay());
		}
	}

	/** 设置子项集合的值 */
	private void setList_Matchs(List<List<LotteryDtMatch>> _list_Matchs) {
		list_Matchs = new ArrayList<List<LotteryDtMatch>>();
		for (List<LotteryDtMatch> list : _list_Matchs) {
			List<LotteryDtMatch> listM = new ArrayList<LotteryDtMatch>();
			for (LotteryDtMatch dt : list) {
				listM.add(dt);
			}
			list_Matchs.add(listM);
		}
		setGroup();
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
		// Log.i("x", "111父---"+groupPosition + "----长度"+list_Matchs.size());

		if (groupPosition > (list_Matchs.size() - 1))
			return null;

		GroupViewHolder holder;

		if (convertView == null) {
			holder = new GroupViewHolder();
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(
					R.layout.win_lottery_jc_detail_header, null);
			holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			holder.tv_weekday = (TextView) convertView
					.findViewById(R.id.tv_weekday);
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

		holder.tv_date.setText(mGroups.get(groupPosition).split("#")[0]);
		holder.tv_weekday.setText(mGroups.get(groupPosition).split("#")[1]);

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		if (groupPosition > (list_Matchs.size() - 1)
				|| childPosition > (list_Matchs.get(groupPosition).size() - 1))
			return null;

		final ChildViewHolder chiHolder;
		if (convertView == null) {
			chiHolder = new ChildViewHolder();
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.lottery_jc_item, null);
			chiHolder.tv_game = (TextView) convertView
					.findViewById(R.id.tv_game);
			chiHolder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			chiHolder.tv_scroll = (TextView) convertView
					.findViewById(R.id.tv_scorll);
			chiHolder.tv_mainTeam = (TextView) convertView
					.findViewById(R.id.tv_mainTeam);
			chiHolder.tv_gusTeam = (TextView) convertView
					.findViewById(R.id.tv_gusTeam);
			chiHolder.tv_loseBall = (TextView) convertView
					.findViewById(R.id.tv_loseBall);
			chiHolder.tv_result = (TextView) convertView
					.findViewById(R.id.tv_result);
			chiHolder.ll_right2 = (LinearLayout) convertView
					.findViewById(R.id.ll_right2);
			convertView.setTag(chiHolder);
		} else {
			chiHolder = (ChildViewHolder) convertView.getTag();
		}

		LotteryDtMatch dtm = list_Matchs.get(groupPosition).get(childPosition);
		chiHolder.tv_game.setText(dtm.getGame());
		chiHolder.tv_mainTeam.setText(dtm.getMainTeam());
		chiHolder.tv_gusTeam.setText(dtm.getGuestTeam());
		chiHolder.tv_time.setText(dtm.getStopSellTime().substring(10, 16));

		Log.i("x", "时间===" + dtm.getStopSellTime() + "==="
				+ dtm.getStopSellTime().length());

		int color_bg = 0;
		int color = 0;

		if ("胜".equals(dtm.getSpfResult()) || "主胜".equals(dtm.getSfResult())) {
			color_bg = context.getResources().getColor(
					R.color.win_lottery_jc_detail_score3_bg);
			color = context.getResources().getColor(
					R.color.win_lottery_jc_detail_score3_text);
			// color = context.getResources().getColor(R.color.blue);
		}
		if ("平".equals(dtm.getSpfResult())) {
			// color = Color.BLACK;
			color_bg = context.getResources().getColor(
					R.color.win_lottery_jc_detail_score1_bg);
			color = context.getResources().getColor(
					R.color.win_lottery_jc_detail_score1_text);
		}
		if ("负".equals(dtm.getSpfResult()) || "主负".equals(dtm.getSfResult())) {
			color_bg = context.getResources().getColor(
					R.color.win_lottery_jc_detail_score0_bg);
			color = context.getResources().getColor(
					R.color.win_lottery_jc_detail_score0_text);
		}
		chiHolder.ll_right2.setBackgroundColor(color_bg);
		chiHolder.tv_result.setTextColor(color);
		chiHolder.tv_scroll.setTextColor(color);

		Log.i("x", "id-----" + lotteryId);
		// 显示不同玩法界面
		switch (lotteryId) {
		case 72:
			chiHolder.tv_loseBall.setText(dtm.getLoseBall() + "");
			chiHolder.tv_result.setText(dtm.getSpfResult());
			chiHolder.tv_scroll.setText(dtm.getCbfResult());
			break;
		case 73:
			chiHolder.tv_loseBall.setText(dtm.getLoseScore() + "");
			chiHolder.tv_result.setText(dtm.getSfResult());
			chiHolder.tv_scroll.setText(dtm.getResult());
			break;
		}
		return convertView;
	}

	@Override
	public int getTreeHeaderState(int groupPosition, int childPosition) {
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
		TextView tv_date = (TextView) header.findViewById(R.id.tv_date);
		tv_date.setText(mGroups.get(groupPosition).split("#")[0]);
		TextView tv_weekday = (TextView) header.findViewById(R.id.tv_weekday);
		tv_weekday.setText(mGroups.get(groupPosition).split("#")[1]);

	}

	@Override
	public void onHeadViewClick(int groupPosition, int status) {
		groupStatusMap.put(groupPosition, status);
	}

	@Override
	public int getHeadViewClickStatus(int groupPosition) {
		if (groupStatusMap.containsKey(groupPosition)) {
			return groupStatusMap.get(groupPosition);
		} else {
			return 0;
		}
	}

	/** 组控件 */
	static class GroupViewHolder {
		TextView tv_date, tv_weekday;
		TextView tv_count;
	}

	/** 子类控件 */
	static class ChildViewHolder {
		TextView tv_game, tv_id, tv_time, tv_mainTeam, tv_gusTeam, tv_loseBall,
				tv_result, tv_scroll;
		LinearLayout ll_right2;
	}
}
