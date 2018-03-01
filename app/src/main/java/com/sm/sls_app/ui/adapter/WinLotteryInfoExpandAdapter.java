package com.sm.sls_app.ui.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.WinLottery;
import com.sm.sls_app.view.MyListView2;
import com.tencent.mm.sdk.platformtools.Log;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WinLotteryInfoExpandAdapter extends BaseExpandableListAdapter {

	private Context context;

	private List<WinLottery> list;

	public WinLotteryInfoExpandAdapter(Context context, List<WinLottery> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return list.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View view,
			ViewGroup parent) {
		if (list.isEmpty()) {
			return null;
		}
		ViewParentHolder holder;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewParentHolder();
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
			holder.win_lottery_expand_arrow = (ImageView) view
					.findViewById(R.id.win_lottery_expand_arrow);
			view.setTag(holder);
		} else {
			holder = (ViewParentHolder) view.getTag();
		}
		WinLottery wLottery = list.get(groupPosition);

		holder.win_lottery_expand_arrow
				.setBackgroundResource(isExpanded ? R.drawable.win_lottery_detail_up
						: R.drawable.win_lottery_detail_down);
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

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View view, ViewGroup parent) {
		if (list.isEmpty()) {
			return null;
		}
		ViewChildHolder holder;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewChildHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			// 得到布局文件
			view = inflater.inflate(R.layout.win_lottery_detail_child, null);
			// 得到控件

			holder.center_tv_payCount2 = (TextView) view
					.findViewById(R.id.center_tv_payCount2);
			holder.center_tv_lotteryMoney2 = (TextView) view
					.findViewById(R.id.center_tv_lotteryMoney2);
			holder.win_lottery_info_listview = (MyListView2) view
					.findViewById(R.id.win_lottery_info_listview);
			view.setTag(holder);
		} else {
			holder = (ViewChildHolder) view.getTag();
		}
		WinLottery wLottery = list.get(groupPosition);
		holder.center_tv_payCount2.setText(TextUtils.isEmpty(wLottery
				.getSales()) ? "-" : wLottery.getSales());
		holder.center_tv_lotteryMoney2.setText(TextUtils.isEmpty(wLottery
				.getTotalMoney()) ? "-" : wLottery.getTotalMoney());
		holder.win_lottery_info_listview.setAdapter(new WinInfoChildAdapter(
				context, wLottery.getListWinDetail()));
		return view;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	private class ViewParentHolder {

		TextView text_qi;
		TextView text_time;
		TextView text_redNum;
		TextView text_blueNum;
		ImageView win_lottery_expand_arrow;
	}

	private class ViewChildHolder {
		TextView center_tv_payCount2;
		TextView center_tv_lotteryMoney2;
		MyListView2 win_lottery_info_listview;
	}
}
