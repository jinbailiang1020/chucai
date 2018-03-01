package com.sm.sls_app.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.Schemes;
import com.sm.sls_app.fragment.loaderclass.FollowDateLoadeEach;
import com.sm.sls_app.fragment.loaderclass.FollowDateLoadeRecord;
import com.sm.sls_app.fragment.loaderclass.FollowDateLoadeSchedule;
import com.sm.sls_app.fragment.loaderclass.FollowDateLoadeScheme;
import com.sm.sls_app.indicator.widget.IconPagerAdapter;
import com.sm.sls_app.indicator.widget.TabPageIndicator;
import com.sm.sls_app.indicator.widget.TabPageIndicator.OnTabReselectedListener;
import com.sm.sls_app.ui.FollowHelpActivity;
import com.sm.sls_app.ui.LoginActivity;
import com.sm.sls_app.ui.SettingActivity;
import com.sm.sls_app.ui.adapter.MyMenuGridViewAdapter;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.PopupWindowUtil;
import com.sm.sls_app.view.MyMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 合买大厅
 * 
 * @author Kinwee 修改日期 2014-12-3
 * 
 */

@SuppressLint("NewApi")
public class FollowFragment extends Fragment implements OnClickListener {
	private final static String TAG = "FollowFragment";
	/* 头部 */
	private RelativeLayout layout_top_select;// 顶部布局
	private LinearLayout layout_select_playtype;
	private ImageButton btn_back; // 返回
	private ImageView iv_up_down;// 玩法提示图标
	private Button btn_playtype;// 玩法
	private ImageButton btn_help;// 帮助
	private Context context;

	private FollowDateLoadeSchedule scheduleLoader;
	private FollowDateLoadeScheme schemeLoader;
	private FollowDateLoadeEach eachleLoader;
	private FollowDateLoadeRecord recordLoader;

	private Integer sort = 1; // 排序方式 0.升序 1.降序类型
	private Integer sortType = 1; // 0.进度 1.金额 2.每份金额 3.战绩

	private boolean sort_schedule = true;
	private boolean sort_scheme = true;
	private boolean sort_each = true;
	private boolean sort_record = true;

	private int selectIndex = -1;// 选中的页卡

	private static final String[] CONTENT = new String[] { "参与进度", "方案金额",
			"每份金额", "战绩" };
	private static final int[] ICONS = new int[] {
			R.drawable.perm_group_sort_down, R.drawable.perm_group_sort_down,
			R.drawable.perm_group_sort_down, R.drawable.perm_group_sort_down };

	private static final int[] REVERSE_ICONS = new int[] {
			R.drawable.perm_group_sort_up, R.drawable.perm_group_sort_up,
			R.drawable.perm_group_sort_up, R.drawable.perm_group_sort_up };
	private TabPageIndicator indicator;

	private List<View> listViews;
	private View view_schedule, view_scheme, view_each, view_record;// 参与进度
																	// ,方案金额,
																	// 每份金额,战绩
	private LayoutInflater mInflater;

	private Intent intent;

	// 最后可见条目的索引
	private int lastVisibleIndex;

	/** 要更改的 listView foodview image **/
	private LinearLayout ll;
	private ProgressBar pb;

	public static List<Schemes> listSchemes = new ArrayList<Schemes>();
	public static String lotteryId; // 查询合买的彩种ID
	public int max = 0; // 判断是否数据已经加载完毕
	public static Integer index; // 存储滚轮的下标

	/** 自定义Menu **/
	private MyMenu mMenu;
	private MyMenuGridViewAdapter menuAdapter;
	private List<Integer> menuList = new ArrayList<Integer>();

	private Map<Integer, Map<Integer, String>> data = new HashMap<Integer, Map<Integer, String>>();// 弹出框数据
	private Animation animation = null;
	private PopupWindowUtil popUtil;
	private int parentIndex;
	private int itemIndex;
	private String all_lottery;
	private Map<Integer, String> lotteryIds = new HashMap<Integer, String>();

	private ViewPager pager;

	private int size;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// View v = inflater.inflate(R.layout.activity_follow, container,
		// false);
		AppTools.setLevelList();// 设置等级图片
		final Context contextThemeWrapper = new ContextThemeWrapper(
				getActivity(), R.style.StyledIndicators);

		// clone the inflater using the ContextThemeWrapper
		LayoutInflater localInflater = inflater
				.cloneInContext(contextThemeWrapper);
		View v = localInflater.inflate(R.layout.activity_follow, container,
				false);
		MyPagerAdapter adapter = new MyPagerAdapter();
		pager = (ViewPager) v.findViewById(R.id.pager);
		initViewPager();
		pager.setAdapter(adapter);
		indicator = (TabPageIndicator) v.findViewById(R.id.indicator);
		indicator.setViewPager(pager);
		indicator.setOnTabReselectedListener(new OnTabReselectedListener() {
			@Override
			public void onTabReselected(int position, boolean sort_state) {
				// TODO Auto-generated method stub
				changeDrawable(position, sort_state);
				sortType = position;
				if (sort_state) {
					sort = 1;
				} else {
					sort = 0;
				}
				if (null != scheduleLoader && null != scheduleLoader.myAsynTask) {
					scheduleLoader.myAsynTask.cancel(true);
				}
				if (null != schemeLoader && null != schemeLoader.myAsynTask) {
					schemeLoader.myAsynTask.cancel(true);
				}
				if (null != eachleLoader && null != eachleLoader.myAsynTask) {
					eachleLoader.myAsynTask.cancel(true);
				}
				if (null != recordLoader && null != recordLoader.myAsynTask) {
					recordLoader.myAsynTask.cancel(true);
				}
				switch (position) {
				case 0:
					sort_schedule = sort_state;
					scheduleLoader.updateListview(sort, lotteryId);
					break;
				case 1:
					sort_scheme = sort_state;
					schemeLoader.updateListview(sort, lotteryId);
					break;
				case 2:
					sort_each = sort_state;
					eachleLoader.updateListview(sort, lotteryId);
					break;
				case 3:
					sort_record = sort_state;
					recordLoader.updateListview(sort, lotteryId);
					break;
				}
			}
		});
		context = getActivity();
		findView(v);
		init();
		return v;
	}

	/** 初始化UI */
	private void findView(View v) {
		layout_top_select = (RelativeLayout) v
				.findViewById(R.id.layout_top_select);// 顶部布局
		btn_back = (ImageButton) v.findViewById(R.id.btn_back);// 返回
		layout_select_playtype = (LinearLayout) v
				.findViewById(R.id.layout_select_playtype);// 玩法提示图标
		iv_up_down = (ImageView) v.findViewById(R.id.iv_up_down);// 玩法提示图标
		btn_playtype = (Button) v.findViewById(R.id.btn_playtype);// 玩法
		btn_help = (ImageButton) v.findViewById(R.id.btn_help);// 帮助
		// 隐藏
		btn_back.setVisibility(View.GONE);
		btn_help.setVisibility(View.GONE);
		/** 要更改的 新加的加载图片 **/
		ll = new LinearLayout(context);
		pb = new ProgressBar(context);
		ll.setGravity(Gravity.CENTER);
		ll.addView(pb);
		Map<Integer, String> playType = new HashMap<Integer, String>();
		// 初始化弹出的框
		Set set = AppTools.allLotteryName.entrySet();
		Iterator iterator = set.iterator();
		playType.put(0, "全部");
		int i = 0;
		StringBuffer lottery_all = new StringBuffer();
		while (iterator.hasNext()) {
			i++;
			Map.Entry entry = (Map.Entry) iterator.next();
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			playType.put(i, key);
			lottery_all.append(value + ",");
		}
		size = i + 1;
		if (0 != size % 3) {
			int empty = 3 - size % 3;
			for (int j = 0; j < empty; j++) {
				i++;
				playType.put(i, "");
			}
		}
		all_lottery = lottery_all.toString().substring(0,
				lottery_all.length() - 1);
		data.put(0, playType);

	}

	/**
	 * 旋转
	 * 
	 * @param type
	 *            1.向上 2.向下
	 */
	public void rote(int type) {
		if (1 == type) {
			animation = AnimationUtils.loadAnimation(getActivity(),
					R.anim.rote_playtype_up);
		} else if (2 == type) {
			animation = AnimationUtils.loadAnimation(getActivity(),
					R.anim.rote_playtype_down);
		}
		LinearInterpolator lin = new LinearInterpolator();
		animation.setInterpolator(lin);
		animation.setFillAfter(true);
		if (iv_up_down != null) {
			iv_up_down.startAnimation(animation);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		/** 刷新 **/
		case 1:
			break;
		/** 设置 **/
		case 2:
			intent = new Intent(context, SettingActivity.class);
			intent.putExtra("loginType", "genggai");
			context.startActivity(intent);
			break;
		/** 更改账户 **/
		case 3:
			intent = new Intent(context, LoginActivity.class);
			intent.putExtra("loginType", "genggai");
			context.startActivity(intent);
			break;
		/** 退出 **/
		case 4:
			for (Activity activity : App.activityS) {
				activity.finish();
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		int sysVersion = Integer.parseInt(VERSION.SDK);
		if (sysVersion > 10) {
			menu.add(1, 1, 0, "刷新");
			menu.add(1, 2, 0, "设置");
			menu.add(1, 3, 0, "更换账户");
			menu.add(1, 4, 0, "退出");
		} else {
			menu.add(1, 1, 0, "").setIcon(R.drawable.menu_refresh_select);
			menu.add(1, 2, 0, "").setIcon(R.drawable.menu_setting_select);
			menu.add(1, 3, 0, "").setIcon(R.drawable.menu_changeuser_select);
			menu.add(1, 4, 0, "").setIcon(R.drawable.menu_exit_select);
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	/** 初始化属性 */
	private void init() {
		if (lotteryId == null)
			lotteryId = AppTools.lotteryIds;
		btn_playtype.setText("全部");
		lotteryId = all_lottery;
		layout_select_playtype.setOnClickListener(this);
		btn_playtype.setOnClickListener(this);
		iv_up_down.setOnClickListener(this);
	}

	/** 帮助说明 */
	private void helpExplain() {
		Intent intent = new Intent(this.getActivity(), FollowHelpActivity.class);
		getActivity().startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/** 选玩法 **/
		case R.id.layout_select_playtype:
		case R.id.btn_playtype:
		case R.id.iv_up_down:
			popUtil = new PopupWindowUtil(getActivity(), data,
					layout_top_select);
			popUtil.setSelectIndex(parentIndex, itemIndex);
			popUtil.createPopWindow();
			popUtil.setOnSelectedListener(new PopupWindowUtil.OnSelectedListener() {
				@Override
				public void getIndex(int parentIndex, int itemIndex) {
					// TODO Auto-generated method stub
					if (itemIndex < size) {
						if (itemIndex != FollowFragment.this.itemIndex) {
							FollowFragment.this.itemIndex = itemIndex;
							FollowFragment.this.parentIndex = parentIndex;
							String lotteryName = data.get(parentIndex).get(
									itemIndex);
							if ("全部".equals(lotteryName)) {// 如果
								lotteryId = all_lottery;
							} else {
								lotteryId = AppTools.allLotteryName
										.get(lotteryName);
							}
							btn_playtype.setText(lotteryName);
							int itemId = pager.getCurrentItem();
							updateListview(itemId);
						}
					}
					rote(2);// 旋转动画 向下
				}
			});
			rote(1);// 旋转动画 向上
			break;
		case R.id.btn_help:// 弹出帮助activity
			helpExplain();
			break;
		}
	}

	public void updateListview(int itemIndex) {
		if (null != scheduleLoader && null != scheduleLoader.myAsynTask) {
			scheduleLoader.myAsynTask.cancel(true);
		}
		if (null != schemeLoader && null != schemeLoader.myAsynTask) {
			schemeLoader.myAsynTask.cancel(true);
		}
		if (null != eachleLoader && null != eachleLoader.myAsynTask) {
			eachleLoader.myAsynTask.cancel(true);
		}
		if (null != recordLoader && null != recordLoader.myAsynTask) {
			recordLoader.myAsynTask.cancel(true);
		}
		updateAll();
		// switch (itemIndex) {
		// case 0:
		// scheduleLoader.updateListview(sort, lotteryId);
		// break;
		// case 1:
		// schemeLoader.updateListview(sort, lotteryId);
		// break;
		// case 2:
		// eachleLoader.updateListview(sort, lotteryId);
		// break;
		// case 3:
		// recordLoader.updateListview(sort, lotteryId);
		// break;
		// }
	}

	private void updateAll() {
		if (scheduleLoader != null)
			scheduleLoader.updateListview(sort, lotteryId);
		if (schemeLoader != null)
			schemeLoader.updateListview(sort, lotteryId);
		if (eachleLoader != null)
			eachleLoader.updateListview(sort, lotteryId);
		if (recordLoader != null)
			recordLoader.updateListview(sort, lotteryId);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	/**
	 * 改变图片的方向
	 * 
	 * @param position
	 * @param sort_state
	 */
	private void changeDrawable(int position, boolean sort_state) {
		TextView v = indicator.getChildTextView(position);
		v.setCompoundDrawablesWithIntrinsicBounds(0, 0,
				sort_state ? ICONS[position] : REVERSE_ICONS[position], 0);
	}

	private void initViewPager() {
		listViews = new ArrayList<View>();
		mInflater = getActivity().getLayoutInflater();

		view_schedule = mInflater.inflate(R.layout.follow_vp, null);
		view_schedule.setTag("schedule");

		view_scheme = mInflater.inflate(R.layout.follow_vp, null);
		view_scheme.setTag("scheme");

		view_each = mInflater.inflate(R.layout.follow_vp, null);
		view_each.setTag("each");

		view_record = mInflater.inflate(R.layout.follow_vp, null);
		view_record.setTag("record");

		listViews.add(view_schedule);
		listViews.add(view_scheme);
		listViews.add(view_each);
		listViews.add(view_record);
	}

	private class MyPagerAdapter extends PagerAdapter implements
			IconPagerAdapter {
		@Override
		public int getCount() {
			return listViews.size();
		}

		/**
		 * 从指定的position创建page
		 * 
		 * @param collection
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
			if ("schedule".equals(tag)) {// 进度
				sortType = 0;
				if (sort_schedule) {
					sort = 1;
				} else {
					sort = 0;
				}
				scheduleLoader = new FollowDateLoadeSchedule(lotteryId,
						context, v, sortType, sort);
			}
			if ("scheme".equals(tag)) {// 方案金额
				sortType = 1;
				if (sort_scheme) {
					sort = 1;
				} else {
					sort = 0;
				}
				schemeLoader = new FollowDateLoadeScheme(lotteryId, context, v,
						sortType, sort);
			}
			if ("each".equals(tag)) {// 每份金额
				sortType = 2;
				if (sort_each) {
					sort = 1;
				} else {
					sort = 0;
				}
				eachleLoader = new FollowDateLoadeEach(lotteryId, context, v,
						sortType, sort);
			}
			if ("record".equals(tag)) {// 战绩
				sortType = 3;
				if (sort_record) {
					sort = 1;
				} else {
					sort = 0;
				}
				recordLoader = new FollowDateLoadeRecord(lotteryId, context, v,
						sortType, sort);
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
			switch (position) {
			case 0:
				if (scheduleLoader != null) {
					scheduleLoader.finish();
				}
				break;
			case 1:
				if (schemeLoader != null) {
					schemeLoader.finish();
				}
				break;
			case 2:
				if (eachleLoader != null) {
					eachleLoader.finish();
				}
				break;
			case 3:
				if (recordLoader != null) {
					recordLoader.finish();
				}
				break;
			}
			((ViewPager) collection).removeView(listViews.get(position));
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == (object);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return CONTENT[position % CONTENT.length];
		}

		@Override
		public int getIconResId(int index) {
			// TODO Auto-generated method stub
			return ICONS[index];
		}
	}

}
