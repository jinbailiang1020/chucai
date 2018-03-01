package com.sm.sls_app.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.SelectedNumbers;
import com.sm.sls_app.ui.BetActivity;
import com.sm.sls_app.utils.AppTools;

import java.util.List;

/**
 * 功能：显示我的彩票 的 ListView的Adapter 版本
 * 
 * @author Administrator 修改日期：2013-3-7
 */
public class MyBetLotteryListAdapter extends BaseAdapter {

	// 上下文本
	private Context context;
	// 装彩票的集合
	private List<SelectedNumbers> listSchemes;
	private BetActivity BetActivity;

	/**构造方法 实现初始化*/
	public MyBetLotteryListAdapter(Context context,
			List<SelectedNumbers> listSchemes) {
		this.context = context;
		this.listSchemes = listSchemes;
		BetActivity = (BetActivity) context;
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
	
	
	/**获得视图*/
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
		} 
		else
		{
			holder = (ViewHolder) view.getTag();
		}

		SelectedNumbers num = listSchemes.get(position);
		// 给控件赋值

		String red = "";
		String blue = "";
		if (null == num.getRedTuoNum() || num.getRedTuoNum().size() == 0) 
		{
			for (String str : num.getRedNumbers()) 
			{
				red += str + " ";
			}
		} 
		else if (num.getRedTuoNum().size() != 0) 
		{
			for (int i = 0; i < num.getRedNumbers().size(); i++) 
			{
				if (i == 0)
					red = "( ";
				    
				    red += num.getRedNumbers().toArray()[i] + " ";
				    
				if (i == num.getRedNumbers().size() - 1)
					red = red +") ";
			}
			
			for (String str : num.getRedTuoNum()) 
			{
				red += str + " ";
			}
		}

		for (String str : num.getBlueNumbers()) 
		{
			blue += str + " ";
		}

		Spanned number = Html.fromHtml("<font color='#BE0205'>" + red
				+ "</FONT>" + "<font color='#4060ff'>" +" "+blue + "</FONT>");

		holder.tv_show_number.setText(number);

		if(501 == num.getPlayType())
			holder.tv_type_count_money.setText("普通投注  "+num.getCount() + "注  "+ listSchemes.get(position).getMoney() + "元");
		else if(502 == num.getPlayType())
			holder.tv_type_count_money.setText("胆拖投注  "+num.getCount() + "注  "+ listSchemes.get(position).getMoney() + "元");

		holder.iv_delete.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				AppTools.list_numbers.remove(index);
				BetActivity.adapter.notifyDataSetChanged();
				BetActivity.changeTextShow();
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

    public void clear(){
        if (listSchemes != null) {
            listSchemes.clear();
            notifyDataSetChanged();
        }
    }
}
