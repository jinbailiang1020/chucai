package com.sm.sls_app.ui.adapter;

import java.util.List;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.FundsInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 账户明细 Adapter
 * 
 * @author SLS003
 */
public class FundsInfoAdapter extends BaseAdapter {

	private Context context;
	private List<FundsInfo> listFunds;

	public FundsInfoAdapter(Context context, List<FundsInfo> list) {
		super();
		this.context = context;
		this.listFunds = list;
	}

	public void setList(List<FundsInfo> list) {
		if (list != null) {
			listFunds = list;
			notifyDataSetChanged();
		}
	}

	public void clear() {
		if (listFunds != null) {
			listFunds.clear();
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listFunds.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listFunds.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder holder;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			// 得到布局文件
			view = inflater.inflate(R.layout.fundsinfo_items, null);

			// 得到控件
			holder.tv_money = (TextView) view
					.findViewById(R.id.fundsinfo_detail_tv_money);
			holder.tv_type = (TextView) view
					.findViewById(R.id.fundsinfo_detail_tv_type);
			holder.tv_time = (TextView) view
					.findViewById(R.id.fundsinfo_detail_tv_time);
			holder.tv_content = (TextView) view
					.findViewById(R.id.fundsinfo_detail_tv_zhaiyao);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if (listFunds.isEmpty()) {
			return null;
		}
		// 给控件赋值
		FundsInfo fInfo = listFunds.get(position);
		// 赋值
		holder.tv_money.setText(fInfo.getMoney() + "");
		holder.tv_type.setText(fInfo.getType());
		holder.tv_time.setText(fInfo.getTime().replace(" ", "\n"));
		if ("管理员手动撤单".equals(fInfo.getContent())) {
			holder.tv_content.setText("已撤单");
		} else if ("销售充值".equals(fInfo.getContent())) {
			holder.tv_content.setText("在线支付");
		} else {
			holder.tv_content.setText(fInfo.getContent());
		}
		return view;
	}

	/** 静态类 */
	static class ViewHolder {
		TextView tv_money;
		TextView tv_type;
		TextView tv_time;
		TextView tv_content;
	}

}
