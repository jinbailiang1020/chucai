package com.sm.sls_app.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.ui.BetActivity;
import com.sm.sls_app.utils.AppTools;

public class MyLuckNumber extends Dialog implements OnClickListener {

	private Context mContext;
	private Class betClass;

	public MyLuckNumber(Context context, String[] _lotteryNumbers,Class _betClass) {
		super(context);
		mContext = context;
		init(_lotteryNumbers,_betClass);
		// TODO Auto-generated constructor stub
	}

	public MyLuckNumber(Context context, int theme, String[] _lotteryNumbers,Class _betClass) {
		super(context, theme);
		mContext = context;
		init(_lotteryNumbers,_betClass);
		// TODO Auto-generated constructor stub
	}

	private ListView luckNumberListView;
	private MyLuckNumberAdapter luckAdapter;
	private Button btn_close, btn_bet;

	private String[] lotteryNumbers;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_luck_number_dialog);
		findView();
		setListener();
	}

	/** 初始化 */
	private void init(String[] _lotteryNumbers,Class _betClass) 
	{
		setLotteryNumber(_lotteryNumbers);
		betClass = _betClass;
	}

	/** 给 幸运号码 数组赋值 */
	public void setLotteryNumber(String[] _lotteryNumbers) 
	{
		this.lotteryNumbers = new String[_lotteryNumbers.length];
		for (int i = 0; i < _lotteryNumbers.length; i++) 
		{
			this.lotteryNumbers[i] = _lotteryNumbers[i];
		}
	}

	private void findView() 
	{
		luckNumberListView = (ListView) this
				.findViewById(R.id.luck_number_listView);
		btn_close = (Button) this.findViewById(R.id.close);
		btn_bet = (Button) this.findViewById(R.id.btn_toBet);
		luckAdapter = new MyLuckNumberAdapter();

		luckNumberListView.setAdapter(luckAdapter);
	}

	private void setListener() 
	{
		btn_close.setOnClickListener(this);
		btn_bet.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId()) 
		{
		case R.id.close:
			dismiss();
			AppTools.list_numbers.clear();
			break;
		case R.id.btn_toBet:
			Intent intent = new Intent(mContext,betClass);
			mContext.startActivity(intent);
			dismiss();
			break;
		default:
			break;
		}
	}

	/** listView Adapter */
	class MyLuckNumberAdapter extends BaseAdapter
	{
		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return lotteryNumbers.length;
		}

		@Override
		public Object getItem(int position)
		{
			// TODO Auto-generated method stub
			return lotteryNumbers[position];
		}

		@Override
		public long getItemId(int position)
		{
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (null == convertView) 
			{
				LayoutInflater inflact = LayoutInflater.from(mContext);
				convertView = inflact.inflate(R.layout.luck_number_item, null);
				holder = new ViewHolder();
				holder.tv_redNum = (TextView) convertView
						.findViewById(R.id.tv_redNum);
				holder.tv_blueNum = (TextView) convertView
						.findViewById(R.id.tv_blueNum);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			// 赋值
			holder.tv_redNum.setText("");
			holder.tv_blueNum.setText("");

			if (lotteryNumbers[position].split("-").length == 2)
			{
				holder.tv_redNum
						.setText(lotteryNumbers[position].split("-")[0]);
				holder.tv_blueNum.setText(" "
						+ lotteryNumbers[position].split("-")[1]);
			} 
			else 
			{
				holder.tv_redNum.setText(lotteryNumbers[position]);
			}
			return convertView;
		}
	}

	static class ViewHolder 
	{
		TextView tv_redNum, tv_blueNum;
	}

}
