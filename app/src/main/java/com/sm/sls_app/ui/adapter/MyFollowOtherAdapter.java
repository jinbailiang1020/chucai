package com.sm.sls_app.ui.adapter;

import java.util.List;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.ChaseList;
import com.sm.sls_app.dataaccess.UserList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author 作者 : hxj
 * @version 创建时间：2016-1-28 下午1:56:36 类说明
 */
public class MyFollowOtherAdapter extends BaseAdapter {
	/** type为1 方案 2 合买 */
	private int type;
	private List<ChaseList> chaseLists;
	private List<UserList> userLists;
	private Context context;
	LayoutInflater inflater;

	public MyFollowOtherAdapter(List<ChaseList> chaseLists,
			List<UserList> userLists, Context context, int type) {
		super();
		this.type = type;
		this.chaseLists = chaseLists;
		this.userLists = userLists;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if (type == 1) {
			return chaseLists.size() + 1;
		}
		return userLists.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		if (type == 1) {
			return chaseLists.get(position);
		}
		return userLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		OtherViewHolder holder;
		if (convertView == null) {
			holder = new OtherViewHolder();
			convertView = inflater.inflate(R.layout.follow_other_item, null);
			holder.one = (TextView) convertView
					.findViewById(R.id.follow_other_one_text);
			holder.two = (TextView) convertView
					.findViewById(R.id.follow_other_two_text);
			holder.three = (TextView) convertView
					.findViewById(R.id.follow_other_three_text);
			holder.four = (TextView) convertView
					.findViewById(R.id.follow_other_four_text);
			convertView.setTag(holder);
		} else {
			holder = (OtherViewHolder) convertView.getTag();
		}
		if (position == 0) {
			if (type == 1) {
				holder.one.setText("期号");
				holder.two.setText("倍数");
				holder.three.setText("本期投入（元）");
				holder.four.setText("累计投入（元）");
			} else {
				holder.one.setText("用户名");
				holder.two.setText("认购份数（份）");
				holder.three.setText("认购金额（元）");
				holder.four.setText("所占股份");
			}
		} else {
			if (type == 1) {
				holder.one.setText(chaseLists.get(position - 1).getIsuseName());
				holder.two.setText(chaseLists.get(position - 1).getMultiple());
				holder.three.setText(chaseLists.get(position - 1).getMoney());
				holder.four.setText(chaseLists.get(position - 1).getSumMoney());
			} else {
				holder.one.setText(userLists.get(position - 1).getName());
				holder.two.setText(userLists.get(position - 1).getShare());
				holder.three.setText(userLists.get(position - 1)
						.getDetailMoney());
				Double l = userLists.get(position - 1).getScalePercent();
				// 精度需要服务端配合
				if (l < 0.001)
					holder.four.setText("小于0.1％");
				else
					holder.four.setText((float) (l * 100) + "％");
			}

		}

		return convertView;
	}

	class OtherViewHolder {
		private TextView one;
		private TextView two;
		private TextView three;
		private TextView four;
	}

}
