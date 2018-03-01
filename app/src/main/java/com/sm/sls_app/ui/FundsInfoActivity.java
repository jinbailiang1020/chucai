package com.sm.sls_app.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.fragment.FunsdsFragment;
import com.sm.sls_app.indicator.widget.TabPageIndicator;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.view.MyFundsInfoDialog;
import com.sm.sls_app.wheel.widget.ArrayWheelAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class FundsInfoActivity extends FragmentActivity implements
		OnClickListener {
	private final String[] TITLES = new String[] { "全部", "收入", "支出" };
	private final int[] CONDITIONS = new int[] { -1, 1, 2 };
	private List<Fragment> list_fragments = new ArrayList<Fragment>();
	private ImageButton btn_back, funds_info_date_btn;
	private TabPageIndicator funds_info_indicator;
	private ViewPager funds_info_viewPager;
	private MyPagerAdapter adapter;

	private MyFundsInfoDialog myFundsDialog;
	private ArrayWheelAdapter<String> yearAdapter;
	private ArrayWheelAdapter<String> monthAdapter;

	private String[] yearItems = { "2009", "2010", "2011", "2012", "2013",
			"2014", "2015", "2016", "2017"};

	private String[] monthItems = { "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "10", "11", "12", };

	private int year;
	private int month;
	private int day;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_fundsinfo);
		findView();
		init();
		setListener();
	}

	private void findView() {
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		funds_info_date_btn = (ImageButton) findViewById(R.id.funds_info_date_btn);
		funds_info_indicator = (TabPageIndicator) findViewById(R.id.funds_info_indicator);
		funds_info_viewPager = (ViewPager) findViewById(R.id.funds_info_viewPager);
	}

	private void init() {
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		funds_info_viewPager.setAdapter(adapter);
		funds_info_indicator.setViewPager(funds_info_viewPager, 0);

		Calendar now = Calendar.getInstance();
		year = now.get(Calendar.YEAR);
		month = now.get(Calendar.MONTH) + 1;
		day = AppTools.getLastDay(year, month);

		yearAdapter = new ArrayWheelAdapter<String>(yearItems);
		monthAdapter = new ArrayWheelAdapter<String>(monthItems);
		myFundsDialog = new MyFundsInfoDialog(this, R.style.dialog,
				yearAdapter, monthAdapter, year, month);
	}

	private void setListener() {
		btn_back.setOnClickListener(this);
		funds_info_date_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;

		case R.id.funds_info_date_btn:
			myFundsDialog.show();
			break;
		}

	}

	private class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			FunsdsFragment fragment = FunsdsFragment.newInstance(year, month,
					day, CONDITIONS[arg0]);
			list_fragments.add(fragment);
			return fragment;
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position % TITLES.length];
		}
	}

	/**
	 * 根据时间刷新所有的fragment
	 * 
	 * @param year
	 * @param month
	 * @param day
	 */
	public void refresh(int year, int month, int day) {
		if (list_fragments != null && !list_fragments.isEmpty()) {
			for (Fragment iterable_element : list_fragments) {
				((FunsdsFragment) iterable_element).Refresh(year, month, day);
			}
		}
	}
}
