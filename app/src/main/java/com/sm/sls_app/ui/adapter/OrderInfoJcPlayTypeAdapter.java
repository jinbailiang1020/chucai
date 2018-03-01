package com.sm.sls_app.ui.adapter;

import java.util.List;
import java.util.Map;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.view.MyListView2;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderInfoJcPlayTypeAdapter extends BaseAdapter {
	private Context context;
	private List<String> playtypes;
	private List<List<Map<String, Object>>> playitems;

	public OrderInfoJcPlayTypeAdapter(Context context, List<String> playtypes,
			List<List<Map<String, Object>>> playitems) {
		this.context = context;
		this.playtypes = playtypes;
		this.playitems = playitems;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return playtypes.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return playtypes.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.orderinfo_jc_child_item, null);
			holder = new ViewHolder();
			holder.lin_playtpe = (TextView) convertView
					.findViewById(R.id.lin_playtpe);
			holder.orderinfo_jc_child_listview = (MyListView2) convertView
					.findViewById(R.id.orderinfo_jc_child_listview);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.lin_playtpe.setText(playtypes.get(position));

		holder.orderinfo_jc_child_listview.setAdapter(new InnerAdapter(
				playitems.get(position)));
		return convertView;
	}

	private class ViewHolder {
		TextView lin_playtpe;
		MyListView2 orderinfo_jc_child_listview;
	}

	private class InnerAdapter extends BaseAdapter {
		List<Map<String, Object>> maps = null;

		public InnerAdapter(List<Map<String, Object>> maps) {
			this.maps = maps;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return maps.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return maps.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			InnerViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.orderinfo_jc_child2_item, null);
				holder = new InnerViewHolder();
				holder.child_invest = (TextView) convertView
						.findViewById(R.id.child_invest);
				holder.child_result = (TextView) convertView
						.findViewById(R.id.child_result);
				convertView.setTag(holder);
			} else {
				holder = (InnerViewHolder) convertView.getTag();
			}

			holder.child_invest.setText(Html.fromHtml(maps.get(position).get(
					"select")
					+ "<br><font color=\"#008000\">" + maps.get(position).get("odd")+"</font>"));
			holder.child_result.setText(maps.get(position).get("result")
					.toString());
			return convertView;
		}

		private class InnerViewHolder {
			TextView child_invest, child_result;
		}
	}
}
