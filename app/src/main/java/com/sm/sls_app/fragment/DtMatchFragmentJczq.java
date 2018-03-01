package com.sm.sls_app.fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.DtMatch;
import com.sm.sls_app.indicator.widget.TabPageIndicator;
import com.sm.sls_app.indicator.widget.TabPageIndicator.OnTabReselectedListener;
import com.sm.sls_app.ui.Bet_JCZQ_Activity;
import com.sm.sls_app.ui.Select_jczqActivity;
import com.sm.sls_app.ui.adapter.ExpandAdapterJCZQPassMore;
import com.sm.sls_app.ui.adapter.ExpandAdapterJCZQPassSingle;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.view.IphoneTreeView;
import com.sm.sls_app.view.MyToast;

/**
 * 竞彩篮球
 * 
 * @author Administrator
 * 
 */
public class DtMatchFragmentJczq extends Fragment implements OnClickListener {

	private final static String TAG = "DtMatchFragmentJczq";
	private static Select_jczqActivity activity;
	private static String[] CONTENT;
	// 选过关和单关控件
	private TabPageIndicator indicator;
	public ViewPager viewPager;
	private List<View> listViews;
	private MyPagerAdapter adapter;
	// 过关
	private IphoneTreeView lv_passmore;
	private ExpandAdapterJCZQPassMore exAdapter_passmore;
	private List<List<DtMatch>> data_passmore;// 过关
	// 单关
	private IphoneTreeView lv_passsingle;
	private ExpandAdapterJCZQPassSingle exAdapter_passsingle;
	private List<List<DtMatch>> data_passsingle;// 单关
	private int playtype;

	public int total_passmore = 0;
	public int total_passsingle = 0;

	private int type = 5;// 玩法类型

	private LinearLayout layout_tip_notjc;// 非竞彩提示
	private LinearLayout layout_tip_jc;// 竞彩提示
	public TextView tv_tip_jc;// 竞彩提示
	/* 尾部 */
	private Button btn_clearall; // 清除全部
	private Button btn_submit; // 选好了
	public TextView tv_tatol_count;// 总注数
	public TextView tv_tatol_money;// 总钱数
	private int viewPagerCurrentIndex = 0;// viewpager的当前页

	private boolean isEmptPassMore = false;// 过关数据是否是空
	private boolean isEmptPassSingle = false;// 单关数据是否是空

	/**
	 * 构造方法
	 * 
	 * @param activity
	 * @param playtype
	 *            玩法id
	 * @param viewPagerCurrentIndex
	 *            页面下标
	 * @param passMoreDMList
	 *            过关对阵
	 * @param passSingleDMList
	 *            单关对阵
	 * @return
	 */
	public static DtMatchFragmentJczq newInstance(Select_jczqActivity activity,
			int playtype, int viewPagerCurrentIndex,
			List<List<DtMatch>> passMoreDMList,
			List<List<DtMatch>> passSingleDMList) {
		DtMatchFragmentJczq fragment = new DtMatchFragmentJczq();
		fragment.viewPagerCurrentIndex = viewPagerCurrentIndex;
		fragment.activity = activity;
		fragment.playtype = playtype;
		fragment.data_passmore = passMoreDMList;// 获取过关数据
		fragment.data_passsingle = passSingleDMList;// 获取单关数据
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final Context contextThemeWrapper = new ContextThemeWrapper(
				getActivity(), R.style.StyledIndicators);
		// clone the inflater using the ContextThemeWrapper
		LayoutInflater localInflater = inflater
				.cloneInContext(contextThemeWrapper);
		View parent = localInflater.inflate(R.layout.framlayout_select_jc,
				container, false);
		init();
		findView(parent);
		return parent;
	}

	private void findView(View v) {
		// 初始化自定义控件
		btn_clearall = (Button) v.findViewById(R.id.btn_clearall);
		btn_submit = (Button) v.findViewById(R.id.btn_submit);
		tv_tatol_count = (TextView) v.findViewById(R.id.tv_tatol_count);
		tv_tatol_money = (TextView) v.findViewById(R.id.tv_tatol_money);
		layout_tip_notjc = (LinearLayout) v.findViewById(R.id.layout_tip_notjc);
		layout_tip_jc = (LinearLayout) v.findViewById(R.id.layout_tip_jc);
		tv_tip_jc = (TextView) v.findViewById(R.id.tv_tip_jc);
		// Spanned tip = Html.fromHtml("请至少选择"
		// + AppTools.changeStringColor("#e3393c", "2") + "比赛");
		// tv_tip_jc.setText(tip);
		adapter = new MyPagerAdapter();
		viewPager = (ViewPager) v.findViewById(R.id.select_vp);
		initViewPager();
		viewPager.setAdapter(adapter);
		indicator = (TabPageIndicator) v.findViewById(R.id.indicator);
		indicator.setViewPager(viewPager, viewPagerCurrentIndex);
		indicator.setOnTabReselectedListener(new OnTabReselectedListener() {
			@Override
			public void onTabReselected(int position, boolean sort_state) {
				// TODO Auto-generated method stub
			}
		});
		indicator
				.setOnMyPagerChangeListener(new TabPageIndicator.OnMyPagerChangeListener() {

					@Override
					public void pagerChanged() {
						// TODO Auto-generated method stub
						changeTextShow();
					}
				});
		if (playtype != 7206) {
			indicator.setVisibility(View.VISIBLE);// 显示
		} else {
			indicator.setVisibility(View.GONE);// 隐藏
		}
		btn_clearall.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		// 显示与隐藏
		layout_tip_notjc.setVisibility(View.GONE);
		layout_tip_jc.setVisibility(View.VISIBLE);
		tv_tatol_count.setVisibility(View.GONE);
		tv_tatol_money.setVisibility(View.GONE);
	}

	private void init() {
		if (playtype != 7206) {
			CONTENT = new String[] { "过关", "单关" };
		} else {
			CONTENT = new String[] { "过关" };
		}
		showPlayType();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/** 改变显示值 **/
	public void changeTextShow() {
		total_passmore = 0;
		total_passsingle = 0;
		Spanned tip = null;
		if (0 == viewPager.getCurrentItem()) {// 过关
			if (isEmptPassMore) {
				MyToast.getToast(DtMatchFragmentJczq.activity, "暂无过多关对阵信息")
						.show();
			}
			if (null != exAdapter_passmore) {
				switch (exAdapter_passmore.getPlayType()) {
				case 5:
					if (null != ExpandAdapterJCZQPassMore.map_hashMap_hhtz) {
						for (int i = 0; i < 3; i++) {
							if (ExpandAdapterJCZQPassMore.map_hashMap_hhtz
									.containsKey(i))
								total_passmore += ExpandAdapterJCZQPassMore.map_hashMap_hhtz
										.get(i).size();
						}
					}
					break;
				case 1:
				case 4:
					if (null != ExpandAdapterJCZQPassMore.map_hashMap_spf) {
						for (int i = 0; i < 3; i++) {
							if (ExpandAdapterJCZQPassMore.map_hashMap_spf
									.containsKey(i))
								total_passmore += ExpandAdapterJCZQPassMore.map_hashMap_spf
										.get(i).size();
						}
					}
					break;
				case 2:
					if (null != ExpandAdapterJCZQPassMore.map_hashMap_cbf) {
						for (int i = 0; i < 3; i++) {
							if (ExpandAdapterJCZQPassMore.map_hashMap_cbf
									.containsKey(i))
								total_passmore += ExpandAdapterJCZQPassMore.map_hashMap_cbf
										.get(i).size();
						}
					}
					break;
				case 6:
					if (null != ExpandAdapterJCZQPassMore.map_hashMap_bqc) {
						for (int i = 0; i < 3; i++) {
							if (ExpandAdapterJCZQPassMore.map_hashMap_bqc
									.containsKey(i))
								total_passmore += ExpandAdapterJCZQPassMore.map_hashMap_bqc
										.get(i).size();
						}
					}
					break;
				case 3:
					if (null != ExpandAdapterJCZQPassMore.map_hashMap_zjq) {
						for (int i = 0; i < 3; i++) {
							if (ExpandAdapterJCZQPassMore.map_hashMap_zjq
									.containsKey(i))
								total_passmore += ExpandAdapterJCZQPassMore.map_hashMap_zjq
										.get(i).size();
						}
					}
					break;
				default:
					break;
				}
				if (total_passmore < 2) {
					tip = Html
							.fromHtml("请至少选择"
									+ AppTools
											.changeStringColor("#e3393c", "2")
									+ "比赛");
				} else {
					tip = Html.fromHtml("您已选择"
							+ AppTools.changeStringColor("#e3393c",
									total_passmore + "") + "比赛");
				}
			}
		} else if (1 == viewPager.getCurrentItem()) {// 单关
			if (isEmptPassSingle) {
				MyToast.getToast(DtMatchFragmentJczq.activity, "暂无单关对阵信息")
						.show();
			}
			if (null != exAdapter_passsingle) {
				switch (exAdapter_passsingle.getPlayType()) {
				case 1:
				case 4:
					if (null != ExpandAdapterJCZQPassSingle.map_hashMap_spf) {
						for (int i = 0; i < 3; i++) {
							if (ExpandAdapterJCZQPassSingle.map_hashMap_spf
									.containsKey(i))
								total_passsingle += ExpandAdapterJCZQPassSingle.map_hashMap_spf
										.get(i).size();
						}
					}
					break;
				case 2:
					if (null != ExpandAdapterJCZQPassSingle.map_hashMap_cbf) {
						for (int i = 0; i < 3; i++) {
							if (ExpandAdapterJCZQPassSingle.map_hashMap_cbf
									.containsKey(i))
								total_passsingle += ExpandAdapterJCZQPassSingle.map_hashMap_cbf
										.get(i).size();
						}
					}
					break;
				case 6:
					if (null != ExpandAdapterJCZQPassSingle.map_hashMap_bqc) {
						for (int i = 0; i < 3; i++) {
							if (ExpandAdapterJCZQPassSingle.map_hashMap_bqc
									.containsKey(i))
								total_passsingle += ExpandAdapterJCZQPassSingle.map_hashMap_bqc
										.get(i).size();
						}
					}
					break;
				case 3:
					if (null != ExpandAdapterJCZQPassSingle.map_hashMap_zjq) {
						for (int i = 0; i < 3; i++) {
							if (ExpandAdapterJCZQPassSingle.map_hashMap_zjq
									.containsKey(i))
								total_passsingle += ExpandAdapterJCZQPassSingle.map_hashMap_zjq
										.get(i).size();
						}
					}
					break;
				default:
					break;
				}
			}
			Log.i(TAG, "选择了" + total_passmore + "场");
			if (total_passsingle < 1) {
				tip = Html.fromHtml("请至少选择"
						+ AppTools.changeStringColor("#e3393c", "1") + "比赛");
			} else {
				tip = Html.fromHtml("您已选择"
						+ AppTools.changeStringColor("#e3393c",
								total_passsingle + "") + "比赛");
			}
		}

		tv_tip_jc.setText(tip);
	}

	/** 根据不同玩法显示界面 **/
	private void showPlayType() {
		switch (playtype) {
		case 7201:
			type = 1;
			break;
		case 7202:
			type = 2;
			Log.i(TAG, "传过来的值");
			for (Integer i : ExpandAdapterJCZQPassMore.map_hashMap_cbf.keySet()) {
				ExpandAdapterJCZQPassMore.map_hashMap_cbf.get(i);
				for (Integer j : ExpandAdapterJCZQPassMore.map_hashMap_cbf.get(
						i).keySet()) {
					ArrayList<String> list = ExpandAdapterJCZQPassMore.map_hashMap_cbf
							.get(i).get(j);
					for (int k = 0; k < list.size(); k++) {
						Log.i(TAG, list.get(k));
					}
				}
			}
			break;
		case 7204:
			type = 6;
			break;
		case 7203:
			type = 3;
			break;
		case 7207:
			type = 4;
			break;
		case 7206:
			type = 5;
			break;
		}
	}

	/** 根据玩法筛选出 队伍 **/
	private List<List<DtMatch>> setList_Matchs(List<List<DtMatch>> _list) {
		List<List<DtMatch>> list = new ArrayList<List<DtMatch>>();
		if (null != _list) {
			for (List<DtMatch> list_dt : _list) {
				List<DtMatch> list2 = new ArrayList<DtMatch>();
				for (DtMatch dt : list_dt) {
					if (setList_Matchs(dt))
						list2.add(dt);
				}
				list.add(list2);
			}
			for (int i = 0; i < list.size(); i++) {
				if (0 == list.get(i).size()) {
					list.remove(i);
				}
			}
		}
		return list;
	}

	/** 根据类型筛选出 队伍 **/
	private boolean setList_Matchs(DtMatch dt) {
		switch (this.playtype) {
		case 7201:
			return dt.isSPF();
		case 7202:
			return dt.isCBF();
		case 7204:
			return dt.isBQC();
		case 7203:
			return dt.isZJQ();
		case 7207:
			return dt.isNewSPF();
		case 7206:
			return dt.isHHTZ();
		default:
			return false;
		}
	}

	private void initViewPager() {
		listViews = new ArrayList<View>();
		// 过关
		LayoutInflater inflate = this.activity.getLayoutInflater().from(
				activity);
		View view_passmore = inflate.inflate(
				R.layout.activity_select_jczq_vp_item, null);
		lv_passmore = (IphoneTreeView) view_passmore
				.findViewById(R.id.expandablelist);
		lv_passmore.setHeaderView(LayoutInflater.from(activity).inflate(
				R.layout.select_jczq_spf_groups, lv_passmore, false));
		// sl_passmore = (StickyLayout) view_passmore
		// .findViewById(R.id.sticky_layout);
		view_passmore.setTag("passmore");
		listViews.add(view_passmore);

		if (7206 != playtype) {
			// 单关
			View view_passsingle = inflate.inflate(
					R.layout.activity_select_jczq_vp_item, null);
			lv_passsingle = (IphoneTreeView) view_passsingle
					.findViewById(R.id.expandablelist);
			lv_passsingle.setHeaderView(LayoutInflater.from(activity).inflate(
					R.layout.select_jczq_spf_groups, lv_passsingle, false));
			view_passsingle.setTag("passsingle");
			listViews.add(view_passsingle);
		}
	}

	/**
	 * 判断根据玩法筛选出来的对阵是否为空 单关
	 * 
	 * @param list_singlepass_Matchs
	 * @return
	 */
	public boolean isEmpt(List<List<DtMatch>> list_singlepass_Matchs) {
		List<List<DtMatch>> lists = setList_Matchs(list_singlepass_Matchs);
		boolean[] flag = new boolean[lists.size()];
		for (int i = 0; i < lists.size(); i++) {
			List<DtMatch> matchs = lists.get(i);
			if (0 == matchs.size()) {
				flag[i] = false;
			} else {
				flag[i] = true;
			}
		}
		boolean isEmpt = true;
		for (int i = 0; i < flag.length; i++) {
			if (flag[i]) {
				isEmpt = false;
			}
		}
		return isEmpt;
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
			if ("passmore".equals((String) v.getTag())) {// 过多关
				if (null == exAdapter_passmore) {
					exAdapter_passmore = new ExpandAdapterJCZQPassMore(
							(Select_jczqActivity) DtMatchFragmentJczq.activity,
							1, DtMatchFragmentJczq.this, lv_passmore);
				}

				List<List<DtMatch>> lists = setList_Matchs(data_passmore);
				if (lists == null || lists.size() == 0) {
					return v;
				}
				if (lists.get(0) == null || lists.get(0).size() == 0) {
					return v;
				}

				exAdapter_passmore.setPlayType(type);
				exAdapter_passmore
						.setList_Matchs(setList_Matchs(data_passmore));// 根据玩法筛选对阵
				if (0 == setList_Matchs(data_passmore).size()) {
					isEmptPassMore = true;// 设置过关数据为空
				}
				lv_passmore.setAdapter(exAdapter_passmore);
				// 展开所有group
				for (int i = 0, count = lv_passmore.getCount(); i < count; i++) {
					lv_passmore.expandGroup(i);
				}
				// lv_passmore.setOnHeaderUpdateListener(DtMatchFragmentJczq.this);
				// sl_passmore
				// .setOnGiveUpTouchEventListener(DtMatchFragmentJczq.this);
			} else if ("passsingle".equals((String) v.getTag())) {// 过单关
				if (null == exAdapter_passsingle) {
					exAdapter_passsingle = new ExpandAdapterJCZQPassSingle(
							DtMatchFragmentJczq.activity, 2,
							DtMatchFragmentJczq.this, lv_passsingle);
				}
				boolean isEmpt = isEmpt(data_passsingle);
				if (!isEmpt) {
					// lv_passsingle.setVisibility(View.VISIBLE);// 显示控件
					// lv_passsingle.setAdapter(exAdapter_passsingle);
					List<List<DtMatch>> lists = setList_Matchs(data_passsingle);
					if (lists == null || lists.size() == 0) {
						return v;
					}
					if (lists.get(0) == null || lists.get(0).size() == 0) {
						return v;
					}

					exAdapter_passsingle.setPlayType(type);
					exAdapter_passsingle
							.setList_Matchs(setList_Matchs(data_passsingle));// 根据玩法筛选对阵
					lv_passsingle.setAdapter(exAdapter_passsingle);
					// 设置全部展开
					for (int i = 0; i < exAdapter_passsingle.getGroupCount(); i++) {
						lv_passsingle.expandGroup(i);
					}
				} else {
					isEmptPassSingle = true;
				}
				// lv_passsingle
				// .setOnHeaderUpdateListener(DtMatchFragmentJczq.this);
				// sl_passsingle
				// .setOnGiveUpTouchEventListener(DtMatchFragmentJczq.this);
			}
			changeTextShow();
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
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return CONTENT[position % CONTENT.length];
		}

	}

	// @Override
	// public boolean giveUpTouchEvent(MotionEvent event) {
	// TODO Auto-generated method stub
	// if (0 == viewPager.getCurrentItem()) {// 过关
	// if (lv_passmore.getFirstVisiblePosition() == 0) {
	// View view = lv_passmore.getChildAt(0);
	// if (view != null && view.getTop() >= 0) {
	// return true;
	// }
	// }
	// } else if (1 == viewPager.getCurrentItem()) {// 单关
	// if (lv_passsingle.getFirstVisiblePosition() == 0) {
	// View view = lv_passsingle.getChildAt(0);
	// if (view != null && view.getTop() >= 0) {
	// return true;
	// }
	// }
	// }
	// return false;
	// }

	// @Override
	// public View getPinnedHeader() {
	// TODO Auto-generated method stub
	// View headerView = (ViewGroup) activity.getLayoutInflater().inflate(
	// R.layout.select_jczq_spf_groups, null);
	// headerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
	// LayoutParams.WRAP_CONTENT));
	// return headerView;
	// return null;
	// }

	// @Override
	// public void updatePinnedHeader(View headerView, int firstVisibleGroupPos)
	// {
	// TODO Auto-generated method stub
	// TextView tv_date = (TextView) headerView.findViewById(R.id.tv_date);
	// TextView tv_count = (TextView) headerView
	// .findViewById(R.id.tv_playCount);
	// if (0 == viewPager.getCurrentItem()) {// 过关
	// if (exAdapter_passmore.isEmptPassMore()) {// 数据是否为空
	// headerView.setVisibility(View.GONE);
	// } else {
	// String time = (String) exAdapter_passmore
	// .getGroup(firstVisibleGroupPos);
	// tv_date.setText(time);
	// tv_count.setText(exAdapter_passmore.list_Matchs.get(
	// firstVisibleGroupPos).size()
	// + "场比赛可投");
	// }
	// } else if (1 == viewPager.getCurrentItem()) {// 单关
	// if (exAdapter_passsingle.isEmptPassSingle()) {// 数据是否为空
	// headerView.setVisibility(View.GONE);
	// } else {
	// String time = (String) exAdapter_passsingle
	// .getGroup(firstVisibleGroupPos);
	// tv_date.setText(time);
	// tv_count.setText(exAdapter_passsingle.list_Matchs_Singlepass
	// .get(firstVisibleGroupPos).size() + "场比赛可投");
	// }
	// }
	// }

	/** 清空所选的 比赛 */
	public void clearSelect() {
		if (0 == viewPager.getCurrentItem()) {// 过关
			ExpandAdapterJCZQPassMore.map_hashMap_spf.clear();
			ExpandAdapterJCZQPassMore.map_hashMap_zjq.clear();
			ExpandAdapterJCZQPassMore.map_hashMap_cbf.clear();
			ExpandAdapterJCZQPassMore.map_hashMap_hhtz.clear();
			ExpandAdapterJCZQPassMore.map_hashMap_bqc.clear();
			changeTextShow();
			exAdapter_passmore.notifyDataSetChanged();
		} else if (1 == viewPager.getCurrentItem()) {// 单关
			ExpandAdapterJCZQPassSingle.map_hashMap_spf.clear();
			ExpandAdapterJCZQPassSingle.map_hashMap_zjq.clear();
			ExpandAdapterJCZQPassSingle.map_hashMap_cbf.clear();
			ExpandAdapterJCZQPassSingle.map_hashMap_bqc.clear();
			changeTextShow();
			exAdapter_passsingle.notifyDataSetChanged();
		}
	}

	/** 清空 */
	private void clear() {
		clearSelect();
		AppTools.totalCount = 0;
	}

	/** 提交号码 */
	private void submitNumber() {
		if (0 == viewPager.getCurrentItem()) {// 过关
			if (total_passmore < 2) {
				MyToast.getToast(activity, "请至少选泽两场比赛").show();
				return;
			}
		} else if (1 == viewPager.getCurrentItem()) {// 单关
			if (total_passsingle < 1) {
				MyToast.getToast(activity, "请至少选泽一场比赛").show();
				return;
			}
		}
		Intent intent = new Intent(activity, Bet_JCZQ_Activity.class);
		if (0 == viewPager.getCurrentItem()) {// 过关
			intent.putExtra("playtype", type);
			intent.putExtra("passtype", 0);
		} else if (1 == viewPager.getCurrentItem()) {// 单关
			intent.putExtra("playtype", type);
			intent.putExtra("passtype", 1);
		}
		if (0 == viewPager.getCurrentItem()) {// 过关
			exAdapter_passsingle.map_hashMap_cbf.clear();
			exAdapter_passsingle.map_hashMap_spf.clear();
			exAdapter_passsingle.map_hashMap_zjq.clear();
			exAdapter_passsingle.map_hashMap_bqc.clear();
		} else if (1 == viewPager.getCurrentItem()) {// 单关
			exAdapter_passmore.map_hashMap_cbf.clear();
			exAdapter_passmore.map_hashMap_spf.clear();
			exAdapter_passmore.map_hashMap_zjq.clear();
			exAdapter_passmore.map_hashMap_bqc.clear();
			exAdapter_passmore.map_hashMap_hhtz.clear();
		}
		activity.startActivity(intent);
		this.activity.finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		/** 提交号码 **/
		case R.id.btn_submit:
			submitNumber();
			break;
		/** 清空 **/
		case R.id.btn_clearall:
			clear();
			break;
		}

	}

}
