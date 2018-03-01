package com.sm.sls_app.ui.adapter;

import java.util.ArrayList;
import java.util.HashSet;
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
import android.widget.Toast;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.ui.Select_QlcActivity;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.view.MyToast;

public class Qlc_Gridview_Adapter extends BaseAdapter {

	private Context context;
	private Integer[] number;
	private int colorCode;
	private Vibrator vibrator;
	private Select_QlcActivity qlcActivity;
	
	//选球集合
	public static HashSet<String> mSelected_numbers = new HashSet<String>();
	
	public Qlc_Gridview_Adapter(Context context,Integer[] number,int colorCode,Vibrator vibrator){
		this.context = context;
		this.number = number;
		this.colorCode = colorCode;
		this.vibrator =vibrator;
		qlcActivity = (Select_QlcActivity)context;
	}
	
	/**赋值**/
	public void setMSelectedNumber(ArrayList<String> list)
	{
		mSelected_numbers.clear();
		for(String str : list)
		{
			mSelected_numbers.add(str);
		}
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return number.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return number[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View itemView, ViewGroup arg2) {
		final ViewHolder holder;
		if(itemView == null)
		{
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			//得到布局文件
			itemView = inflater.inflate(R.layout.gridview_items, null);
			//得到button，并把holder保存在itemView
			holder.btn = (TextView)itemView.findViewById(R.id.btn_showNum);
			//优化listview
			itemView.setTag(holder);
		}
		else{
			holder = (ViewHolder)itemView.getTag();
		}
		
		//设置button的text属性
		holder.btn.setText(number[position] < 10 ? "0"+number[position]:""+number[position]);
		//设置字体显示颜色、背景图片
		if(Color.RED == colorCode){
			holder.btn.setTextColor(context.getResources()
					.getColor(R.color.main_red));
			holder.btn.setBackgroundResource(R.drawable.icon_ball_red_unselected);
		}else{
			holder.btn.setTextColor(context.getResources().getColor(
					R.color.blue));
			holder.btn.setBackgroundResource(R.drawable.icon_ball_blue_unselected);
		}
		
		//判断是否已被选中
		if(mSelected_numbers.contains(position + 1 < 10 ? "0"+(position + 1):"" + (position + 1))){
			holder.btn.setBackgroundResource(R.drawable.icon_ball_red_selected);
			holder.btn.setTextColor(Color.WHITE);
		}
		
		//绑定监听器
		holder.btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(vibrator!=null)
					vibrator.vibrate(100);
				if (mSelected_numbers.contains(holder.btn.getText().toString())){
					mSelected_numbers.remove(holder.btn.getText().toString());
				}
				else{
					System.out.println("---"+mSelected_numbers.size());
					if (mSelected_numbers.size() >= 15){ 
						MyToast.getToast(context, "最多允许选15个").show();
					}else{
						mSelected_numbers.add(holder.btn.getText().toString());
					}
				}
			    AppTools.totalCount =getInvestmentCount(); 
				//刷新adapter
				qlcActivity.updateAdapter();

			}
			
		});
		return itemView;
	}

	/** 计算注数*/
	public long getInvestmentCount(){
		if (mSelected_numbers.size() < 7) return 0;
		
		long f1 = 1,f2 = 1;
		for (int i = mSelected_numbers.size(); i > 7;i--)
			f1 *= i;
		for (int i = mSelected_numbers.size() - 7; i > 0; i--)
			f2 *= i;
		return f1 / f2;
	}
	
	/**静态类*/
	static class ViewHolder {
		TextView btn;
	}

}
