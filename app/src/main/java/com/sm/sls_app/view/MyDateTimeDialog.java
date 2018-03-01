package com.sm.sls_app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.wheel.widget.NumericWheelAdapter;
import com.sm.sls_app.wheel.widget.OnWheelChangedListener;
import com.sm.sls_app.wheel.widget.OnWheelScrollListener;
import com.sm.sls_app.wheel.widget.WheelAdapter;
import com.sm.sls_app.wheel.widget.WheelView;

/** 自定义对话框 用于开奖详情时间筛选 */
public class MyDateTimeDialog extends Dialog {

	private Context context;

	private WheelAdapter year_adapter;
	private WheelAdapter month_adapter;
	private WheelAdapter day_adapter;

	private OnWheelScrollListener scrollListYear;
	private OnWheelScrollListener scrollListMonth;
	private OnWheelScrollListener scrollListDay;

	private Button btn_ok;
	private Button btn_quit;

	private WheelView wheel_year;
	private WheelView wheel_month;
	private WheelView wheel_day;

	public int year_currentId = 0;
	public int month_currentId = 0;
	public int day_currentId = 0;

	public String content;
	public String content2;
	public String content3;

	public int y = 2013;
	public int m = 1;
	public int d = 1;
	
	private android.view.View.OnClickListener clickListener;

	public MyDateTimeDialog(Context context, int theme,
			WheelAdapter year_adapter, WheelAdapter month_adapter,
			WheelAdapter day_adapter,android.view.View.OnClickListener _clickLister) 
	{
		super(context, theme);
		this.context = context;
	
		this.year_adapter = year_adapter;
		this.month_adapter = month_adapter;
		this.day_adapter = day_adapter;
		this.clickListener =_clickLister;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.datetime_dialog);
		findView();
		setListener();
	}

	public void initDay(int y, int m, int d) 
	{
		this.y = y;
		this.d = d;
		this.m = m;
	}

	/** 初始化UI */
	private void findView()
	{
		wheel_year = (WheelView) this.findViewById(R.id.year_wheel);
		wheel_year.setAdapter(year_adapter);

		wheel_month = (WheelView) this.findViewById(R.id.month_wheel);
		wheel_month.setAdapter(month_adapter);

		wheel_day = (WheelView) this.findViewById(R.id.day_wheel);
		wheel_day.setAdapter(day_adapter);

		btn_ok = (Button) this.findViewById(R.id.funds_btn_ok);
		btn_quit = (Button) this.findViewById(R.id.funds_btn_no);

		wheel_year.setCyclic(true);
		wheel_month.setCyclic(true);
		wheel_day.setCyclic(true);

		scrollListYear = new OnWheelScrollListener() 
		{
			@Override
			public void onScrollingStarted(WheelView wheel) {
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				System.out.println("1");
				year_currentId = wheel.getCurrentItem() + 1;
			}
		};

		scrollListMonth = new OnWheelScrollListener() 
		{
			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				System.out.println("3");
				month_currentId = wheel.getCurrentItem() + 1;
			}
		};

		scrollListDay = new OnWheelScrollListener() 
		{
			@Override
			public void onScrollingStarted(WheelView wheel) {
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				day_currentId = wheel.getCurrentItem();
			}
		};

		wheel_month.addChangingListener(new OnWheelChangedListener() 
		{
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue)
			{
				if(month_adapter.getItem(newValue).length() == 0)
					return;
				m = Integer.parseInt(month_adapter.getItem(newValue));
				setDayAdapter();
			}
		});

		wheel_year.addChangingListener(new OnWheelChangedListener()
		{
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue)
			{
				if(year_adapter.getItem(newValue).length() == 0)
					return;
				y = Integer.parseInt(year_adapter.getItem(newValue));
				setDayAdapter();
			}
		});

		wheel_day.addChangingListener(new OnWheelChangedListener() 
		{
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) 
			{   	

				if(day_adapter.getItem(newValue).length() == 0)
					return;
				d = Integer.parseInt(day_adapter.getItem(newValue));				
			}
		});
		setCheckItem();
	}

	public void setCheckItem() {
		for (int i = 0; i < year_adapter.getItemsCount(); i++)
		{
			if (y == Integer.parseInt(year_adapter.getItem(i)))
			{
				wheel_year.setCurrentItem(i);
			}
		}
		for (int i = 0; i < month_adapter.getItemsCount(); i++) 
		{
			if (m == Integer.parseInt(month_adapter.getItem(i))) 
			{
				wheel_month.setCurrentItem(i);
			}
		}
		for (int i = 0; i < day_adapter.getItemsCount(); i++) 
		{
			if (d == Integer.parseInt(day_adapter.getItem(i))) 
			{
				wheel_day.setCurrentItem(i);
			}
		}
	}

	public void setDayAdapter() 
	{
		wheel_day.setAdapter(new NumericWheelAdapter(1, AppTools.getLastDay(
				y, m)));
		wheel_day.setCurrentItem(0);
	}
	

	/** 绑定监听 */
	private void setListener() 
	{
		btn_ok.setOnClickListener(clickListener);
		btn_quit.setOnClickListener(clickListener);

		wheel_year.addScrollingListener(scrollListYear);
		wheel_month.addScrollingListener(scrollListMonth);
		wheel_day.addScrollingListener(scrollListDay);
	}
	public static boolean compareData(int curY,int curM,int curD,int selY,int selM,int selD){
		boolean istrue=true;
		if(selY>curY){
			istrue=false;
		}else if(selY==curY&&selM>curM){
			istrue=false;
		}else if((selY==curY&&selM==curM)&&selD>curD){
			istrue=false;
		}
		return istrue;
	} 
}
