package com.sm.sls_app.ui.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.Information;

/**
 * 彩票资讯Adapter
 * 
 * @author SLS003
 * 
 */
public class InformationAdapter extends BaseAdapter {

	private Context context;
	// 装图片的集合

	private List<Information> list;

	public InformationAdapter(Context context, List<Information> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			// 得到布局文件
			view = inflater.inflate(R.layout.information_items, null);
			// 得到控件

			holder.txtNewType = (TextView) view.findViewById(R.id.info_newType);
			holder.txtNewTitle = (TextView) view
					.findViewById(R.id.info_newTitle);
			holder.txtDateTime = (TextView) view
					.findViewById(R.id.info_newDateTime);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		Log.i("ss", position + "");
		Information information = list.get(position);
		// 给控件赋值
		if (information != null) {
			System.out.println("shijian" + information.getDateTime());
			holder.txtNewType.setText(information.getNewType());
			holder.txtNewTitle.setText(information.getTitle());
			holder.txtDateTime.setText(information.getDateTime());
		}
		return view;
	}

	/** 静态类 */
	static class ViewHolder {
		TextView txtNewType;
		TextView txtNewTitle;
		TextView txtDateTime;
	}

}
