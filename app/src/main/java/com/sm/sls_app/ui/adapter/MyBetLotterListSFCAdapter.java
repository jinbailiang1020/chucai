package com.sm.sls_app.ui.adapter;

import java.util.List;
import java.util.Map;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.SelectedNumbers;
import com.sm.sls_app.ui.Bet_SFC_Activity;
import com.sm.sls_app.utils.AppTools;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyBetLotterListSFCAdapter extends BaseAdapter{
	// 上下文本
	private Context context;
	// 装彩票的集合
	public static List<SelectedNumbers> listSchemes;
	
	private  Bet_SFC_Activity betActivity;
	
	public MyBetLotterListSFCAdapter(Context context,
			List<SelectedNumbers> listSchemes) {
		this.context = context;
		this.listSchemes = listSchemes;
		betActivity = (Bet_SFC_Activity) context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listSchemes.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listSchemes.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		final int index = position;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			// 得到布局文件
			view = inflater.inflate(R.layout.item_bet_lv, null);
			// 得到控件
			holder.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
			holder.tv_show_number = (TextView) view.findViewById(R.id.tv_show_number);

			holder.tv_type_count_money = (TextView) view.findViewById(R.id.tv_type_count_money);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		SelectedNumbers num = listSchemes.get(position);
		
		String red = "";
		for (Map<Integer, String> strMap : num.getList_Map()) {
			for(String str:strMap.values())
			{
				if(str.length()>1){
					red +="("+str+")";
				}
				else
				{
					red +=str;
				}
			}
		}
		
		// 给控件赋值
		Spanned number = Html.fromHtml("<font color='#BE0205'>" + red+ "</FONT>");
		holder.tv_show_number.setText(number);
		holder.tv_type_count_money.setText("普通投注  "+num.getCount() + "注  "+ listSchemes.get(position).getMoney() + "元");
		holder.iv_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppTools.list_numbers.remove(index);
				betActivity.adapter.notifyDataSetChanged();
				betActivity.changeTextShow();
			}
		});
		
		return view;
	}
	
	/**静态类*/
	static class ViewHolder {
		ImageView iv_delete;
		TextView tv_show_number;
		TextView tv_type_count_money;
	}

}
