package com.sm.sls_app.ui;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.indicator.widget.TabPageIndicator;
import com.sm.sls_app.utils.App;

/**
 * 功能：我的全部彩票类，显示全部彩票 版本
 * 
 * @author Administrator
 */
public class MyAllLotteryActivity extends Activity {

	private ViewPager mViewPager;
	private ImageButton btn_back;
	private TabPageIndicator my_all_lottery_indicator;
	private List<View> listViews;
	private View view_all, view_follow, view_win, view_waitWin, view_join;

	private LayoutInflater mInflater;

	private AllBetLottery allBet = null;
	private ReplaceBetLottery replaceBet = null;
	private WinBetLottery winBet = null;
	private WaitWinBetLottery waitBet = null;
	private JoinBetLottery joinBet = null;

	private int currentPage = 0;
	private static final String[] TITLES = new String[] { "全部", "中奖", "未开奖",
			"代购", "合买" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_my_all_lottery);

		App.activityS.add(this);

		init();

	}

	/** 初始化 根据传入的类型进行加载数据 */
	private void init() {
		currentPage = getIntent().getIntExtra("index", 0);
		mViewPager = (ViewPager) this.findViewById(R.id.vp_myViewPager);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		initViewPager();

		mViewPager.setAdapter(new MyPagerAdapter());
		my_all_lottery_indicator = (TabPageIndicator) findViewById(R.id.my_all_lottery_indicator);
		my_all_lottery_indicator.setViewPager(mViewPager, currentPage);

		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initViewPager() {
		listViews = new ArrayList<View>();
		mInflater = getLayoutInflater();

		view_all = mInflater.inflate(R.layout.center_all_lottery, null);
		view_all.setTag("all");

		view_follow = mInflater.inflate(R.layout.center_all_lottery, null);
		view_follow.setTag("replace");

		view_win = mInflater.inflate(R.layout.center_all_lottery, null);
		view_win.setTag("win");

		view_waitWin = mInflater.inflate(R.layout.center_all_lottery, null);
		view_waitWin.setTag("wait");

		view_join = mInflater.inflate(R.layout.center_all_lottery, null);
		view_join.setTag("join");

		listViews.add(view_all);
		listViews.add(view_win);
		listViews.add(view_waitWin);
		listViews.add(view_follow);
		listViews.add(view_join);
	}

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return listViews.size();
		}

		/**
		 * 从指定的position创建page
		 * 
		 * @param container
		 *            ViewPager容器
		 * @param position
		 *            The page position to be instantiated.
		 * @return 返回指定position的page，这里不需要是一个view，也可以是其他的视图容器.
		 */
		@Override
		public Object instantiateItem(View collection, int position) {
			((ViewPager) collection).addView(listViews.get(position), 0);

			View v = listViews.get(position);
			String tag = (String) v.getTag();
			if ("all".equals(tag)) {
				if (null == allBet) {
					allBet = new AllBetLottery(getApplicationContext(), v);
					allBet.init();
				}
			}
			if ("replace".equals(tag)) {
				if (null == replaceBet) {
					replaceBet = new ReplaceBetLottery(getApplicationContext(),
							v);
					replaceBet.init();
				}
			}
			if ("win".equals(tag)) {
				if (null == winBet) {
					winBet = new WinBetLottery(getApplicationContext(), v);
					winBet.init();
				}
			}
			if ("wait".equals(tag)) {
				if (null == waitBet) {
					waitBet = new WaitWinBetLottery(getApplicationContext(), v);
					waitBet.init();
				}
			}
			if ("join".equals(tag)) {
				if (null == joinBet) {
					joinBet = new JoinBetLottery(getApplicationContext(), v);
					joinBet.init();
				}
			}
			return v;
		}

		/**
		 * 55. * <span style='font-family:
		 * "Droid Sans";'>从指定的position销毁page</span> 56. * 57. * 58. *<span
		 * style='font-family: "Droid Sans";'>参数同上</span> 59.
		 */
		@Override
		public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView(listViews.get(position));
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == (object);
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return TITLES[position % TITLES.length];
		}
	}

	/** 异步任务 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> {
		/** 在后台执行的程序 */
		@Override
		protected String doInBackground(Void... params) {
			return "0";
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (null == result)
				return;
			super.onPostExecute(result);
		}
	}

}
