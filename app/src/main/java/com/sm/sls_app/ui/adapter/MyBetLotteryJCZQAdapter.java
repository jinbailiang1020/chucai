package com.sm.sls_app.ui.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.DtMatch;
import com.sm.sls_app.ui.Bet_JCZQ_Activity;
import com.sm.sls_app.ui.adapter.ExpandAdapterJCZQPassMore.ChildViewHolder;
import com.sm.sls_app.utils.ColorUtil;
import com.sm.sls_app.view.MyToast;
import com.sm.sls_app.view.SelectJCZQDialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 功能：显示竞彩足球 的投注 ListView的Adapter 版本
 * 
 * @author Kinwee 修改日期2014-12-11
 * 
 */
@SuppressLint("UseSparseArrays")
public class MyBetLotteryJCZQAdapter extends BaseAdapter {

	private final static String TAG = "MyBetLotteryJCZQAdapter";

	// 上下文本
	private Context context;
	private Bet_JCZQ_Activity activity;
	/** 玩法 **/
	private int playtype = 5;
	private int passtype = 0;
	/** 子项 **/
	public List<DtMatch> bet_List_Matchs = new ArrayList<DtMatch>();
	public HashMap<Integer, HashMap<Integer, Integer>> index = new HashMap<Integer, HashMap<Integer, Integer>>();// 记录每一场对阵的下标位置

	public int selectDanCount = 0;

	/** 构造方法 实现初始化 */
	public MyBetLotteryJCZQAdapter(Context context, int playtype, int passtype) {
		this.context = context;
		activity = (Bet_JCZQ_Activity) context;
		this.playtype = playtype;
		this.passtype = passtype;
		setBetListMatchs();
	}

	/**
	 * 获取列表需要显示的对阵信息
	 */
	public void setBetListMatchs() {
		if (0 == passtype) {// 过关
			for (int i = 0; i < ExpandAdapterJCZQPassMore.list_Matchs.size(); i++) {// 遍历过关所有对阵
				HashMap<Integer, HashMap<Integer, ArrayList<String>>> map_hashMap = new HashMap<Integer, HashMap<Integer, ArrayList<String>>>();
				switch (playtype) {// 获取选中的map信息
				case 1:// 让球胜平负
				case 4:// 胜平负
					map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_spf;
					break;
				case 2:// 比分
					map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_cbf;
					break;
				case 3:// 总进球
					map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_zjq;

					break;
				case 5:// 混合投注
					map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_hhtz;
					break;
				case 6:// 半全场
					map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_bqc;
					break;
				}
				if (map_hashMap.containsKey(i)) {// 包含父下标
					HashMap<Integer, Integer> cids = new HashMap<Integer, Integer>();
					Map<Integer, ArrayList<String>> map = map_hashMap.get(i);
					Set<Integer> cset = map.keySet();
					int cursor = 0;
					for (Integer cid : cset) {
						bet_List_Matchs
								.add(ExpandAdapterJCZQPassMore.list_Matchs.get(
										i).get(cid));// 将匹配选中的比赛对阵添加到list_Matchs
						cids.put(cursor, cid);// 将子下标放入map
						cursor++;
					}
					index.put(i, cids);// 将子下标map放入map
				}
			}
		} else if (1 == passtype) {// 单关
			for (int i = 0; i < ExpandAdapterJCZQPassSingle.list_Matchs.size(); i++) {// 遍历单关所有对阵
				HashMap<Integer, HashMap<Integer, ArrayList<String>>> map_hashMap = new HashMap<Integer, HashMap<Integer, ArrayList<String>>>();
				switch (playtype) {// 获取选中的map信息
				case 1:// 让球胜平负
				case 4:// 胜平负
					map_hashMap = ExpandAdapterJCZQPassSingle.map_hashMap_spf;
					break;
				case 2:// 比分
					map_hashMap = ExpandAdapterJCZQPassSingle.map_hashMap_cbf;
					break;
				case 3:// 总进球
					map_hashMap = ExpandAdapterJCZQPassSingle.map_hashMap_zjq;

					break;
				case 6:// 半全场
					map_hashMap = ExpandAdapterJCZQPassSingle.map_hashMap_bqc;
					break;
				}
				if (map_hashMap.containsKey(i)) {// 包含父下标
					HashMap<Integer, Integer> cids = new HashMap<Integer, Integer>();
					Map<Integer, ArrayList<String>> map = map_hashMap.get(i);
					Set<Integer> cset = map.keySet();
					int cursor = 0;
					for (Integer cid : cset) {
						bet_List_Matchs
								.add(ExpandAdapterJCZQPassSingle.list_Matchs
										.get(i).get(cid));// 将匹配选中的比赛对阵添加到list_Matchs
						cids.put(cursor, cid);// 将子下标放入map
						cursor++;
					}
					index.put(i, cids);// 将子下标map放入map
				}
			}
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return bet_List_Matchs.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return bet_List_Matchs.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	/** 获得视图 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ChildViewHolder chiHolder;
		DtMatch dtm_passmore = null;
		dtm_passmore = bet_List_Matchs.get(position);
		final Integer parentId;
		final Integer childId;
		int pid = 0;
		int cid = 0;
		// 根据位置在index中找到在对阵信息在map中的父子下标
		Set<Integer> set = index.keySet();
		for (Integer i : set) {
			HashMap<Integer, Integer> map = index.get(i);
			Set<Integer> cset = map.keySet();
			if (map.containsKey(position)) {// 找到了下标
				cid = map.get(position);// 子下标
				pid = i;
			}
		}
		parentId = pid;
		childId = cid;
		final DtMatch dtm = dtm_passmore;
		if (convertView == null) {
			chiHolder = new ChildViewHolder();
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.bet_jczq_items, null);
			chiHolder.ll_spf = (LinearLayout) convertView
					.findViewById(R.id.right_bottom_ll_spf);
			chiHolder.ll_cbf = (LinearLayout) convertView
					.findViewById(R.id.jczq_cbf_ll);
			chiHolder.ll_zjq = (LinearLayout) convertView
					.findViewById(R.id.jczq_zjq_ll);
			chiHolder.ll_hhtz = (LinearLayout) convertView
					.findViewById(R.id.jczq_zjq_hhtz);
			/** 胜负彩 BUTTON **/
			chiHolder.btn_win = (Button) convertView
					.findViewById(R.id.btn_mainWin);
			chiHolder.btn_flat = (Button) convertView
					.findViewById(R.id.btn_float);
			chiHolder.btn_lose = (Button) convertView
					.findViewById(R.id.btn_lose);
			/** 猜比分 BUTTON **/
			chiHolder.btn_open_cbf = (TextView) convertView
					.findViewById(R.id.jczq_cbf_btn_open);
			/** 混合投注 **/
			chiHolder.layout_isspf = (LinearLayout) convertView
					.findViewById(R.id.layout_isspf);
			chiHolder.hhtz_bottom = (LinearLayout) convertView
					.findViewById(R.id.hhtz_bottom);
			chiHolder.tv_tip_isnotrqspf = (TextView) convertView
					.findViewById(R.id.tv_tip_isnotrqspf);
			chiHolder.tv_tip_isnotspf = (TextView) convertView
					.findViewById(R.id.tv_tip_isnotspf);
			chiHolder.tv_hhtz_01 = (TextView) convertView
					.findViewById(R.id.tv_hhtz_1);
			chiHolder.tv_hhtz_02 = (TextView) convertView
					.findViewById(R.id.tv_hhtz_2);
			chiHolder.tv_hhtz_03 = (TextView) convertView
					.findViewById(R.id.tv_hhtz_3);
			chiHolder.tv_hhtz_04 = (TextView) convertView
					.findViewById(R.id.tv_hhtz_4);
			chiHolder.tv_hhtz_05 = (TextView) convertView
					.findViewById(R.id.tv_hhtz_5);
			chiHolder.tv_hhtz_06 = (TextView) convertView
					.findViewById(R.id.tv_hhtz_6);
			chiHolder.tv_result1 = (TextView) convertView
					.findViewById(R.id.tv_result1);
			chiHolder.tv_result2 = (TextView) convertView
					.findViewById(R.id.tv_result2);
			chiHolder.ll_btn_hhtz1 = (LinearLayout) convertView
					.findViewById(R.id.jczq_hhtz_1);
			chiHolder.ll_btn_hhtz2 = (LinearLayout) convertView
					.findViewById(R.id.jczq_hhtz_2);
			chiHolder.ll_btn_hhtz3 = (LinearLayout) convertView
					.findViewById(R.id.jczq_hhtz_3);
			chiHolder.ll_btn_hhtz4 = (LinearLayout) convertView
					.findViewById(R.id.jczq_hhtz_4);
			chiHolder.ll_btn_hhtz5 = (LinearLayout) convertView
					.findViewById(R.id.jczq_hhtz_5);
			chiHolder.ll_btn_hhtz6 = (LinearLayout) convertView
					.findViewById(R.id.jczq_hhtz_6);
			chiHolder.layout_result = (LinearLayout) convertView
					.findViewById(R.id.layout_result);
			chiHolder.layout_hhtz_rq = (LinearLayout) convertView
					.findViewById(R.id.jczq_zjq_4);
			chiHolder.tv_hhtz_rq = (TextView) convertView
					.findViewById(R.id.tv_hhtz_rq);
			chiHolder.tv_dan = (TextView) convertView.findViewById(R.id.tv_dan);
			chiHolder.iv_delete = (ImageView) convertView
					.findViewById(R.id.iv_delete);

			/** 总进球 **/
			findChildView(chiHolder, convertView);

			chiHolder.mainTeam = (TextView) convertView
					.findViewById(R.id.ll_tv_mainTeam);
			chiHolder.guestTeam = (TextView) convertView
					.findViewById(R.id.ll_tv_cusTeam);

			chiHolder.ball = (TextView) convertView.findViewById(R.id.ll_tv_vs);

			convertView.setTag(chiHolder);
		} else {
			chiHolder = (ChildViewHolder) convertView.getTag();
		}

		chiHolder.ball.setTextColor(ColorUtil.BET_GRAY);
		// 显示不同玩法界面
		switch (playtype) {
		case 5:
			chiHolder.ll_hhtz.setVisibility(View.VISIBLE);
			chiHolder.ll_spf.setVisibility(View.GONE);
			chiHolder.ll_cbf.setVisibility(View.GONE);
			chiHolder.ll_zjq.setVisibility(View.GONE);
			chiHolder.ball.setText("VS");
			break;
		case 4:
		case 1:
			chiHolder.ll_hhtz.setVisibility(View.GONE);
			chiHolder.ll_spf.setVisibility(View.VISIBLE);
			chiHolder.ll_cbf.setVisibility(View.GONE);
			chiHolder.ll_zjq.setVisibility(View.GONE);
			if (playtype == 1) {
				if (dtm.getMainLoseBall() == 0) {
					chiHolder.ball.setText("VS");
				} else if (dtm.getMainLoseBall() < 0) {
					chiHolder.ball.setTextColor(Color.GREEN);
					chiHolder.ball.setText(dtm.getMainLoseBall() + "");
				} else if (dtm.getMainLoseBall() > 0) {
					chiHolder.ball.setTextColor(Color.RED);
					chiHolder.ball.setText("+" + dtm.getMainLoseBall() + "");
				}
			}
			if (playtype == 4) {
				chiHolder.ball.setText("VS");
			}
			break;
		case 2:// 比分
		case 6:// 半全场
			chiHolder.ll_hhtz.setVisibility(View.GONE);
			chiHolder.ll_cbf.setVisibility(View.VISIBLE);
			chiHolder.ll_spf.setVisibility(View.GONE);
			chiHolder.ll_zjq.setVisibility(View.GONE);
			chiHolder.ball.setText("VS");
			break;
		case 3:
			chiHolder.ll_hhtz.setVisibility(View.GONE);
			chiHolder.ll_zjq.setVisibility(View.VISIBLE);
			chiHolder.ll_cbf.setVisibility(View.GONE);
			chiHolder.ll_spf.setVisibility(View.GONE);
			chiHolder.ball.setText("VS");
			break;
		}
		chiHolder.tv_dan.setVisibility(View.VISIBLE);//设置可见
		if (0 == passtype) {// 过关
			if (1 == playtype || 4 == playtype) {// 胜平负，让球胜平负
				if (null != ExpandAdapterJCZQPassMore.map_hashMap_spf) {
					change_spf_btn(
							chiHolder,
							ExpandAdapterJCZQPassMore.map_hashMap_spf.get(
									parentId).get(childId));
					setDanBackGround(
							chiHolder,
							ExpandAdapterJCZQPassMore.map_hashMap_spf.get(
									parentId).get(childId));
				}
			} else if (2 == playtype) {// 比分
				if (null != ExpandAdapterJCZQPassMore.map_hashMap_cbf) {

					set_CBF_BQC_Text(
							chiHolder,
							ExpandAdapterJCZQPassMore.map_hashMap_cbf.get(
									parentId).get(childId));
					setDanBackGround(
							chiHolder,
							ExpandAdapterJCZQPassMore.map_hashMap_cbf.get(
									parentId).get(childId));
				}
			} else if (6 == playtype) {// 半全场
				if (null != ExpandAdapterJCZQPassMore.map_hashMap_bqc) {

					set_CBF_BQC_Text(
							chiHolder,
							ExpandAdapterJCZQPassMore.map_hashMap_bqc.get(
									parentId).get(childId));
					setDanBackGround(
							chiHolder,
							ExpandAdapterJCZQPassMore.map_hashMap_bqc.get(
									parentId).get(childId));
				}
			} else if (5 == playtype) {// 混合投注
				chiHolder.tv_dan.setVisibility(View.GONE);//设置不可见
				if (null != ExpandAdapterJCZQPassMore.map_hashMap_hhtz) {

					change_hhtz_btn(
							chiHolder,
							ExpandAdapterJCZQPassMore.map_hashMap_hhtz.get(
									parentId).get(childId));
					setDanBackGround(
							chiHolder,
							ExpandAdapterJCZQPassMore.map_hashMap_hhtz.get(
									parentId).get(childId));
				}
				if(dtm.isNewSPF()){//胜平负
					chiHolder.layout_isspf.setVisibility(View.VISIBLE);
					chiHolder.tv_tip_isnotspf.setVisibility(View.GONE);
				}else{
					chiHolder.layout_isspf.setVisibility(View.GONE);
					chiHolder.tv_tip_isnotspf.setVisibility(View.VISIBLE);
				}
				if(dtm.isSPF()){//让球胜平负
					chiHolder.hhtz_bottom.setVisibility(View.VISIBLE);
					chiHolder.tv_tip_isnotrqspf.setVisibility(View.GONE);
				}else{
					chiHolder.hhtz_bottom.setVisibility(View.GONE);
					chiHolder.tv_tip_isnotrqspf.setVisibility(View.VISIBLE);
				}
				
			} else if (3 == playtype) {// 总进球
				if (null != ExpandAdapterJCZQPassMore.map_hashMap_zjq) {

					change_zjq_btn(
							chiHolder,
							ExpandAdapterJCZQPassMore.map_hashMap_zjq.get(
									parentId).get(childId));
					setDanBackGround(
							chiHolder,
							ExpandAdapterJCZQPassMore.map_hashMap_zjq.get(
									parentId).get(childId));
				}
			}
		} else if (1 == passtype) {// 单关
			chiHolder.tv_dan.setVisibility(View.GONE);//设置不可见
			if (1 == playtype || 4 == playtype) {// 胜平负，让球胜平负
				if (null != ExpandAdapterJCZQPassSingle.map_hashMap_spf) {

					change_spf_btn(
							chiHolder,
							ExpandAdapterJCZQPassSingle.map_hashMap_spf.get(
									parentId).get(childId));
					setDanBackGround(
							chiHolder,
							ExpandAdapterJCZQPassSingle.map_hashMap_spf.get(
									parentId).get(childId));
				}
			} else if (2 == playtype) {// 比分
				if (null != ExpandAdapterJCZQPassSingle.map_hashMap_cbf) {

					set_CBF_BQC_Text(
							chiHolder,
							ExpandAdapterJCZQPassSingle.map_hashMap_cbf.get(
									parentId).get(childId));
					setDanBackGround(
							chiHolder,
							ExpandAdapterJCZQPassSingle.map_hashMap_cbf.get(
									parentId).get(childId));
				}
			} else if (6 == playtype) {// 半全场
				if (null != ExpandAdapterJCZQPassSingle.map_hashMap_bqc) {

					set_CBF_BQC_Text(
							chiHolder,
							ExpandAdapterJCZQPassSingle.map_hashMap_bqc.get(
									parentId).get(childId));
					setDanBackGround(
							chiHolder,
							ExpandAdapterJCZQPassSingle.map_hashMap_bqc.get(
									parentId).get(childId));
				}
			} else if (3 == playtype) {// 总进球
				if (null != ExpandAdapterJCZQPassSingle.map_hashMap_zjq) {

					change_zjq_btn(
							chiHolder,
							ExpandAdapterJCZQPassSingle.map_hashMap_zjq.get(
									parentId).get(childId));
					setDanBackGround(
							chiHolder,
							ExpandAdapterJCZQPassSingle.map_hashMap_zjq.get(
									parentId).get(childId));
				}
			}
		}

		if (playtype == 5) {
			chiHolder.tv_hhtz_01.setText("主胜" + dtm.getSpfwin());
			chiHolder.tv_hhtz_02.setText("平" + dtm.getSpfflat());
			chiHolder.tv_hhtz_03.setText("主负" + dtm.getSpflose());
			int color = 1;
			if (dtm.getMainLoseBall() < 0) {
				chiHolder.tv_hhtz_rq.setText(dtm.getMainLoseBall() + "");
				color = 0xff0d9930;
				chiHolder.tv_hhtz_rq.setTextColor(color);
			} else {
				chiHolder.tv_hhtz_rq.setText("+" + dtm.getMainLoseBall());
				color = 0xffe3393c;
				chiHolder.tv_hhtz_rq.setTextColor(color);
			}
			chiHolder.tv_hhtz_04.setText("主胜" + dtm.getWin());
			chiHolder.tv_hhtz_05.setText("平" + dtm.getFlat());
			chiHolder.tv_hhtz_06.setText("主负" + dtm.getLose());
		}
		if (playtype == 1) {
			/** 胜平负 显示值绑定 **/
			chiHolder.btn_win.setText("胜" + dtm.getWin());
			chiHolder.btn_flat.setText("平" + dtm.getFlat());
			chiHolder.btn_lose.setText("负" + dtm.getLose());
		} else {
			/** 胜平负 显示值绑定 **/
			chiHolder.btn_win.setText("胜" + dtm.getSpfwin());
			chiHolder.btn_flat.setText("平" + dtm.getSpfflat());
			chiHolder.btn_lose.setText("负" + dtm.getSpflose());
		}

		/** 总进球 显示值绑定 **/
		setChildView(chiHolder, dtm);
		chiHolder.mainTeam.setText(dtm.getMainTeam());
		chiHolder.guestTeam.setText(dtm.getGuestTeam());
		/** 胜按钮点击事件 **/
		chiHolder.btn_win.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String num = "";
				if (1 == playtype) {// 让球胜平负
					num = "501";
				} else if (4 == playtype) {// 胜平负
					num = "101";
				}
				if (0 == passtype) {// 过关
					onClickChange(ExpandAdapterJCZQPassMore.map_hashMap_spf,
							chiHolder, parentId, childId, num);
				} else if (1 == passtype) {// 单关
					onClickChange(ExpandAdapterJCZQPassSingle.map_hashMap_spf,
							chiHolder, parentId, childId, num);
				}
			}
		});

		/** 平按钮点击事件 **/
		chiHolder.btn_flat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String num = "";
				if (1 == playtype) {// 让球胜平负
					num = "502";
				} else if (4 == playtype) {// 胜平负
					num = "102";
				}
				if (0 == passtype) {// 过关
					onClickChange(ExpandAdapterJCZQPassMore.map_hashMap_spf,
							chiHolder, parentId, childId, num);
				} else if (1 == passtype) {// 单关
					onClickChange(ExpandAdapterJCZQPassSingle.map_hashMap_spf,
							chiHolder, parentId, childId, num);
				}

			}
		});

		/** 负按钮点击事件 **/
		chiHolder.btn_lose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String num = "";
				if (1 == playtype) {// 让球胜平负
					num = "503";
				} else if (4 == playtype) {// 胜平负
					num = "103";
				}
				if (0 == passtype) {// 过关
					onClickChange(ExpandAdapterJCZQPassMore.map_hashMap_spf,
							chiHolder, parentId, childId, num);
				} else if (1 == passtype) {// 单关
					onClickChange(ExpandAdapterJCZQPassSingle.map_hashMap_spf,
							chiHolder, parentId, childId, num);
				}
			}
		});

		/** 展开 比分投注区 **/
		chiHolder.btn_open_cbf.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ArrayList<String> selectResult = null;
				chiHolder.dialog = new SelectJCZQDialog(activity,
						R.style.dialog, dtm);
				if (0 == passtype) {// 过关
					if (6 == playtype) {// 半全场
						if (ExpandAdapterJCZQPassMore.map_hashMap_bqc
								.containsKey(parentId)) {
							if (null != ExpandAdapterJCZQPassMore.map_hashMap_bqc
									.get(parentId)) {// 如果不为空则获取
								selectResult = ExpandAdapterJCZQPassMore.map_hashMap_bqc
										.get(parentId).get(childId);
							}
						}
						chiHolder.dialog
								.setDialogResultListener(new SelectJCZQDialog.DialogResultListener() {// 实现选择结果接口

									@Override
									public void getResult(int resultCode,
											ArrayList<String> selectResult) {
										// TODO Auto-generated method stub
										if (1 == resultCode) {// 确定
											HashMap<Integer, ArrayList<String>> map1 = null;
											map1 = ExpandAdapterJCZQPassMore.map_hashMap_bqc
													.get(parentId);
											if (null == map1) {
												map1 = new HashMap<Integer, ArrayList<String>>();
											}
											map1.put(childId, selectResult);
											int size = selectResult.size();
											if (selectResult.contains("1")) {// 含胆
												size = selectResult.size() - 1;
											}
											if (0 == size) {
												map1.remove(childId);
											}
											ExpandAdapterJCZQPassMore.map_hashMap_bqc
													.put(parentId, map1);
											if (0 == map1.size()) {
												ExpandAdapterJCZQPassMore.map_hashMap_bqc
														.remove(parentId);
											}
											set_CBF_BQC_Text(chiHolder,
													selectResult);
											activity.clearPassType();//清空过关方式
										}
									}
								});
					} else if (2 == playtype) {// 比分
						if (ExpandAdapterJCZQPassMore.map_hashMap_cbf
								.containsKey(parentId)) {
							if (null != ExpandAdapterJCZQPassMore.map_hashMap_cbf
									.get(parentId)) {// 如果不为空则获取
								selectResult = ExpandAdapterJCZQPassMore.map_hashMap_cbf
										.get(parentId).get(childId);
							}
						}
						chiHolder.dialog
								.setDialogResultListener(new SelectJCZQDialog.DialogResultListener() {// 实现选择结果接口

									@Override
									public void getResult(int resultCode,
											ArrayList<String> selectResult) {
										// TODO Auto-generated method stub
										if (1 == resultCode) {// 确定
											HashMap<Integer, ArrayList<String>> map1 = null;
											map1 = ExpandAdapterJCZQPassMore.map_hashMap_cbf
													.get(parentId);
											if (null == map1) {
												map1 = new HashMap<Integer, ArrayList<String>>();
											}

											map1.put(childId, selectResult);
											int size = selectResult.size();
											if (selectResult.contains("1")) {// 含胆
												size = selectResult.size() - 1;
											}
											if (0 == size) {
												map1.remove(childId);
											}
											ExpandAdapterJCZQPassMore.map_hashMap_cbf
													.put(parentId, map1);
											if (0 == map1.size()) {
												ExpandAdapterJCZQPassMore.map_hashMap_cbf
														.remove(parentId);
											}
											set_CBF_BQC_Text(chiHolder,
													selectResult);
											activity.clearPassType();//清空过关方式
										}
									}
								});
					}
				} else if (1 == passtype) {// 单关
					// 过关
					if (6 == playtype) {// 半全场
						if (ExpandAdapterJCZQPassSingle.map_hashMap_bqc
								.containsKey(parentId)) {
							if (null != ExpandAdapterJCZQPassSingle.map_hashMap_bqc
									.get(parentId)) {// 如果不为空则获取
								selectResult = ExpandAdapterJCZQPassSingle.map_hashMap_bqc
										.get(parentId).get(childId);
							}
						}
						chiHolder.dialog
								.setDialogResultListener(new SelectJCZQDialog.DialogResultListener() {// 实现选择结果接口

									@Override
									public void getResult(int resultCode,
											ArrayList<String> selectResult) {
										// TODO Auto-generated method stub
										if (1 == resultCode) {// 确定
											HashMap<Integer, ArrayList<String>> map1 = null;
											map1 = ExpandAdapterJCZQPassSingle.map_hashMap_bqc
													.get(parentId);
											if (null == map1) {
												map1 = new HashMap<Integer, ArrayList<String>>();
											}
											map1.put(childId, selectResult);
											int size = selectResult.size();
											if (selectResult.contains("1")) {// 含胆
												size = selectResult.size() - 1;
											}
											if (0 == size) {
												map1.remove(childId);
											}
											ExpandAdapterJCZQPassSingle.map_hashMap_bqc
													.put(parentId, map1);
											if (0 == map1.size()) {
												ExpandAdapterJCZQPassSingle.map_hashMap_bqc
														.remove(parentId);
											}
											set_CBF_BQC_Text(chiHolder,
													selectResult);
											activity.clearPassType();//清空过关方式
										}
									}
								});
					} else if (2 == playtype) {// 比分
						if (ExpandAdapterJCZQPassSingle.map_hashMap_cbf
								.containsKey(parentId)) {
							if (null != ExpandAdapterJCZQPassSingle.map_hashMap_cbf
									.get(parentId)) {// 如果不为空则获取
								selectResult = ExpandAdapterJCZQPassSingle.map_hashMap_cbf
										.get(parentId).get(index);
							}
						}
						chiHolder.dialog
								.setDialogResultListener(new SelectJCZQDialog.DialogResultListener() {// 实现选择结果接口

									@Override
									public void getResult(int resultCode,
											ArrayList<String> selectResult) {
										// TODO Auto-generated method stub
										if (1 == resultCode) {// 确定
											HashMap<Integer, ArrayList<String>> map1 = null;
											map1 = ExpandAdapterJCZQPassSingle.map_hashMap_cbf
													.get(parentId);
											if (null == map1) {
												map1 = new HashMap<Integer, ArrayList<String>>();
											}

											map1.put(childId, selectResult);
											int size = selectResult.size();
											if (selectResult.contains("1")) {// 含胆
												size = selectResult.size() - 1;
											}
											if (0 == size) {
												map1.remove(childId);
											}
											ExpandAdapterJCZQPassSingle.map_hashMap_cbf
													.put(parentId, map1);
											if (0 == map1.size()) {
												ExpandAdapterJCZQPassSingle.map_hashMap_cbf
														.remove(parentId);
											}
											set_CBF_BQC_Text(chiHolder,
													selectResult);
											activity.clearPassType();//清空过关方式
										}
									}
								});
					}

				}
				chiHolder.dialog.show();
				if (null != selectResult) {
//					Log.i(TAG, "传入弹出框的值");
					for (String str : selectResult) {
						Log.i(TAG, str);
					}
					chiHolder.dialog.setSelect(selectResult);// 将选中的结果传入弹出框
				}
				chiHolder.dialog.setSpfLayoutVisible(View.GONE);// 隐藏胜平负
				chiHolder.dialog.setBifenLayoutVisible(View.GONE);// 隐藏比分
				chiHolder.dialog.setZjqLayoutVisible(View.GONE);// 隐藏总进球
				chiHolder.dialog.setBqcLayoutVisible(View.GONE);// 隐藏半全场
				if (6 == playtype) {
					chiHolder.dialog.setBqcLayoutVisible(View.VISIBLE);// 显示半全场
				} else if (2 == playtype) {
					chiHolder.dialog.setBifenLayoutVisible(View.VISIBLE);// 显示比分
				}
			}
		});

		/** 总进球 点击 **/
		chiHolder.ll_btn_0.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (0 == passtype) {// 过关
					onClickChange(ExpandAdapterJCZQPassMore.map_hashMap_zjq,
							chiHolder, parentId, childId, "201");
				} else if (1 == passtype) {// 单关
					onClickChange(ExpandAdapterJCZQPassSingle.map_hashMap_zjq,
							chiHolder, parentId, childId, "201");
				}
			}
		});
		chiHolder.ll_btn_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (0 == passtype) {// 过关
					onClickChange(ExpandAdapterJCZQPassMore.map_hashMap_zjq,
							chiHolder, parentId, childId, "202");
				} else if (1 == passtype) {// 单关
					onClickChange(ExpandAdapterJCZQPassSingle.map_hashMap_zjq,
							chiHolder, parentId, childId, "202");
				}
			}
		});
		chiHolder.ll_btn_2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (0 == passtype) {// 过关
					onClickChange(ExpandAdapterJCZQPassMore.map_hashMap_zjq,
							chiHolder, parentId, childId, "203");
				} else if (1 == passtype) {// 单关
					onClickChange(ExpandAdapterJCZQPassSingle.map_hashMap_zjq,
							chiHolder, parentId, childId, "203");
				}
			}
		});

		chiHolder.ll_btn_3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (0 == passtype) {// 过关
					onClickChange(ExpandAdapterJCZQPassMore.map_hashMap_zjq,
							chiHolder, parentId, childId, "204");
				} else if (1 == passtype) {// 单关
					onClickChange(ExpandAdapterJCZQPassSingle.map_hashMap_zjq,
							chiHolder, parentId, childId, "204");
				}
			}
		});
		chiHolder.ll_btn_4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (0 == passtype) {// 过关
					onClickChange(ExpandAdapterJCZQPassMore.map_hashMap_zjq,
							chiHolder, parentId, childId, "205");
				} else if (1 == passtype) {// 单关
					onClickChange(ExpandAdapterJCZQPassSingle.map_hashMap_zjq,
							chiHolder, parentId, childId, "205");
				}
			}
		});
		chiHolder.ll_btn_5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (0 == passtype) {// 过关
					onClickChange(ExpandAdapterJCZQPassMore.map_hashMap_zjq,
							chiHolder, parentId, childId, "206");
				} else if (1 == passtype) {// 单关
					onClickChange(ExpandAdapterJCZQPassSingle.map_hashMap_zjq,
							chiHolder, parentId, childId, "206");
				}
			}
		});

		chiHolder.ll_btn_6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (0 == passtype) {// 过关
					onClickChange(ExpandAdapterJCZQPassMore.map_hashMap_zjq,
							chiHolder, parentId, childId, "207");
				} else if (1 == passtype) {// 单关
					onClickChange(ExpandAdapterJCZQPassSingle.map_hashMap_zjq,
							chiHolder, parentId, childId, "207");
				}
			}
		});
		chiHolder.ll_btn_7.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (0 == passtype) {// 过关
					onClickChange(ExpandAdapterJCZQPassMore.map_hashMap_zjq,
							chiHolder, parentId, childId, "208");
				} else if (1 == passtype) {// 单关
					onClickChange(ExpandAdapterJCZQPassSingle.map_hashMap_zjq,
							chiHolder, parentId, childId, "208");
				}
			}
		});
		// 混合投注玩法點擊事件
		chiHolder.ll_btn_hhtz1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (0 == passtype) {// 过关
					onClickChange(ExpandAdapterJCZQPassMore.map_hashMap_hhtz,
							chiHolder, parentId, childId, "101");
				}
			}
		});
		chiHolder.ll_btn_hhtz2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (0 == passtype) {// 过关
					onClickChange(ExpandAdapterJCZQPassMore.map_hashMap_hhtz,
							chiHolder, parentId, childId, "102");
				}
			}
		});
		chiHolder.ll_btn_hhtz3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (0 == passtype) {// 过关
					onClickChange(ExpandAdapterJCZQPassMore.map_hashMap_hhtz,
							chiHolder, parentId, childId, "103");
				}
			}
		});
		chiHolder.ll_btn_hhtz4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (0 == passtype) {// 过关
					onClickChange(ExpandAdapterJCZQPassMore.map_hashMap_hhtz,
							chiHolder, parentId, childId, "501");
				}
			}
		});
		chiHolder.ll_btn_hhtz5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (0 == passtype) {// 过关
					onClickChange(ExpandAdapterJCZQPassMore.map_hashMap_hhtz,
							chiHolder, parentId, childId, "502");
				}
			}
		});
		chiHolder.ll_btn_hhtz6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (0 == passtype) {// 过关
					onClickChange(ExpandAdapterJCZQPassMore.map_hashMap_hhtz,
							chiHolder, parentId, childId, "503");
				}
			}

		});
		chiHolder.layout_result.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ArrayList<String> selectResult = null;
				if (ExpandAdapterJCZQPassMore.map_hashMap_hhtz
						.containsKey(parentId)) {
					if (null != ExpandAdapterJCZQPassMore.map_hashMap_hhtz.get(
							parentId).get(childId)) {// 如果不为空则获取
						selectResult = ExpandAdapterJCZQPassMore.map_hashMap_hhtz
								.get(parentId).get(childId);
					}
				}
				chiHolder.dialog = new SelectJCZQDialog(context,
						R.style.dialog, dtm);
				chiHolder.dialog
						.setDialogResultListener(new SelectJCZQDialog.DialogResultListener() {// 实现选择结果接口

							@Override
							public void getResult(int resultCode,
									ArrayList<String> selectResult) {
								// TODO Auto-generated method stub
								if (1 == resultCode) {// 确定
									HashMap<Integer, ArrayList<String>> map1 = null;
									map1 = ExpandAdapterJCZQPassMore.map_hashMap_hhtz
											.get(parentId);
									if (null == map1) {
										map1 = new HashMap<Integer, ArrayList<String>>();
									}
									map1.put(childId, selectResult);
									if (0 == selectResult.size()) {
										map1.remove(childId);
									}
									ExpandAdapterJCZQPassMore.map_hashMap_hhtz
											.put(parentId, map1);
									if (0 == map1.size()) {
										ExpandAdapterJCZQPassMore.map_hashMap_hhtz
												.remove(parentId);
									}
									change_hhtz_btn(chiHolder, selectResult);// 刷新布局
									activity.clearPassType();//清空过关方式
								}
							}
						});
				chiHolder.dialog.show();
				if (null != selectResult) {
					chiHolder.dialog.setSelect(selectResult);// 将选中的结果传入弹出框
				}
				chiHolder.dialog.setSpfLayoutVisible(View.VISIBLE);// 显示胜平负
				chiHolder.dialog.setBifenLayoutVisible(View.VISIBLE);// 显示比分
				chiHolder.dialog.setZjqLayoutVisible(View.VISIBLE);// 显示总进球
				chiHolder.dialog.setBqcLayoutVisible(View.VISIBLE);// 显示半全场
			}

		});
		chiHolder.tv_dan.setOnClickListener(new OnClickListener() {// 设胆

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						HashMap<Integer, HashMap<Integer, ArrayList<String>>> map_hashMap = new HashMap<Integer, HashMap<Integer, ArrayList<String>>>();
						if (0 == passtype) {// 过关
							switch (playtype) {// 获取选中的map信息
							case 1:// 让球胜平负
							case 4:// 胜平负
								map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_spf;
								break;
							case 2:// 比分
								map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_cbf;
								break;
							case 3:// 总进球
								map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_zjq;

								break;
							case 5:// 混合投注
								map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_hhtz;
								break;
							case 6:// 半全场
								map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_bqc;
								break;
							}

						} else if (1 == passtype) {// 单关
							switch (playtype) {// 获取选中的map信息
							case 1:// 让球胜平负
							case 4:// 胜平负
								map_hashMap = ExpandAdapterJCZQPassSingle.map_hashMap_spf;
								break;
							case 2:// 比分
								map_hashMap = ExpandAdapterJCZQPassSingle.map_hashMap_cbf;
								break;
							case 3:// 总进球
								map_hashMap = ExpandAdapterJCZQPassSingle.map_hashMap_zjq;

								break;
							case 6:// 半全场
								map_hashMap = ExpandAdapterJCZQPassSingle.map_hashMap_bqc;
								break;
							}

						}
						selectDanCount=0;//获取时先置0
						getDanCount(map_hashMap);// 获取已胆的个数
						int size = getSelectDtMatchCount(map_hashMap);// 已选的对阵
//						Log.i(TAG, map_hashMap.get(parentId)+"");
//						Log.i(TAG, map_hashMap.get(parentId).get(
//								childId)+"");
//						Log.i(TAG,map_hashMap.get(parentId).get(childId)
//								.size()+"");
						if (null != map_hashMap.get(parentId)
								&& null != map_hashMap.get(parentId).get(
										childId)
								&& 0 != map_hashMap.get(parentId).get(childId)
										.size()) {// 选择了赛果才能设胆
							if (map_hashMap.get(parentId).get(childId)
									.contains("1")) {// 含胆
								changeDanBackGround(chiHolder,
										map_hashMap.get(parentId).get(childId));
								selectDanCount=0;//获取时先置0
								getDanCount(map_hashMap);// 获取已胆的个数
								Log.i(TAG, "胆的个数"+selectDanCount);
								activity.clearPassType();//清空过关方式
							} else {// 不含胆
								if (size > 2) {// 大于2才能设胆
									if (7 >= selectDanCount
											&& size - 1 > selectDanCount) {// 胆的个数不能超过当前对阵-1个，且不能超过7个
										changeDanBackGround(
												chiHolder,
												map_hashMap.get(parentId).get(
														childId));
										selectDanCount=0;//获取时先置0
										getDanCount(map_hashMap);// 获取已胆的个数
										Log.i(TAG, "胆的个数"+selectDanCount);
										activity.clearPassType();//清空过关方式
									} else {
										MyToast.getToast(activity, "当前对阵无法设胆")
												.show();
									}
								} else {// 不能设胆
									MyToast.getToast(activity, "当前对阵无法设胆")
											.show();
								}
							}
						} else {
							MyToast.getToast(activity, "当前对阵无法设胆").show();
						}
					}
				});
		chiHolder.iv_delete.setOnClickListener(new OnClickListener() {// 删除

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						HashMap<Integer, HashMap<Integer, ArrayList<String>>> map_hashMap = new HashMap<Integer, HashMap<Integer, ArrayList<String>>>();
						if (0 == passtype) {// 过关
							switch (playtype) {// 获取选中的map信息
							case 1:// 让球胜平负
							case 4:// 胜平负
								map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_spf;
								break;
							case 2:// 比分
								map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_cbf;
								break;
							case 3:// 总进球
								map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_zjq;

								break;
							case 5:// 混合投注
								map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_hhtz;
								break;
							case 6:// 半全场
								map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_bqc;
								break;
							}
						} else if (1 == passtype) {// 单关
							switch (playtype) {// 获取选中的map信息
							case 1:// 让球胜平负
							case 4:// 胜平负
								map_hashMap = ExpandAdapterJCZQPassSingle.map_hashMap_spf;
								break;
							case 2:// 比分
								map_hashMap = ExpandAdapterJCZQPassSingle.map_hashMap_cbf;
								break;
							case 3:// 总进球
								map_hashMap = ExpandAdapterJCZQPassSingle.map_hashMap_zjq;

								break;
							case 6:// 半全场
								map_hashMap = ExpandAdapterJCZQPassSingle.map_hashMap_bqc;
								break;
							}

						}
						Set<Integer> key_parent = map_hashMap.keySet();
						for (Integer key : key_parent) {
							HashMap<Integer, ArrayList<String>> map = map_hashMap.get(key);
							Set<Integer> key_child = map.keySet();
							for (Integer ckey : key_child) {
								ArrayList<String> result = map.get(ckey);
								if (result.contains("1")) {// 含胆
									result.remove("1");
								}
							}
						}
						map_hashMap.get(parentId).remove(childId);
						index = new HashMap<Integer, HashMap<Integer, Integer>>();
						bet_List_Matchs = new ArrayList<DtMatch>();
						setBetListMatchs();
						notifyDataSetChanged();
						activity.clearPassType();//清空过关方式
						if(1 == passtype){
//							Log.i(TAG, "重新计算注数");
							activity.setSelectNumAndGetCount();//单关重新计算注数
							activity.changeTextShow();
						}
					}
				});
		return convertView;
	}
	
	/**
	 * 获取当前有效对阵个数
	 * @param map_hashMap
	 * @return
	 */
	public int getSelectDtMatchCount(HashMap<Integer, HashMap<Integer, ArrayList<String>>> map_hashMap){
		int count=0;
		Set<Integer> key_parent = map_hashMap.keySet();
		for (Integer key : key_parent) {
			HashMap<Integer, ArrayList<String>> map = map_hashMap.get(key);
			Set<Integer> key_child = map.keySet();
			for (Integer ckey : key_child) {
				ArrayList<String> result = map.get(ckey);
				if (!(1==result.size()&&result.contains("1"))) {//获取有效对阵
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * 获取当前胆的个数
	 * 
	 * @param map_hashMap
	 */
	public void getDanCount(
			HashMap<Integer, HashMap<Integer, ArrayList<String>>> map_hashMap) {
		Set<Integer> key_parent = map_hashMap.keySet();
		for (Integer key : key_parent) {
			HashMap<Integer, ArrayList<String>> map = map_hashMap.get(key);
			Set<Integer> key_child = map.keySet();
			for (Integer ckey : key_child) {
				ArrayList<String> result = map.get(ckey);
				if (result.contains("1")) {// 含胆
					selectDanCount+=1;
				}
			}
		}

	}

	/**
	 * 设置胆按钮的背景
	 * 
	 * @param holder
	 * @param selectResult
	 */
	public void setDanBackGround(ChildViewHolder holder,
			ArrayList<String> selectResult) {
		holder.tv_dan.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
		holder.tv_dan.setTextColor(ColorUtil.MAIN_RED);
		if (null != selectResult && 0 != selectResult.size()) {
			if (selectResult.contains("1")) {// 如果含胆
				holder.tv_dan
						.setBackgroundResource(R.drawable.bet_btn_dan_selected);
				holder.tv_dan.setTextColor(Color.WHITE);
			}
		}
	}

	/**
	 * 改变胆按钮的背景
	 * 
	 * @param holder
	 * @param selectResult
	 */
	public void changeDanBackGround(ChildViewHolder holder,
			ArrayList<String> selectResult) {
		if (null != selectResult && 0 != selectResult.size()) {
			if (selectResult.contains("1")) {// 如果含胆
				selectResult.remove("1");// 移除
			} else {
				selectResult.add("1");
			}
		}
		setDanBackGround(holder, selectResult);// 设置背景
	}

	/**
	 * 投注的点击事件
	 * 
	 * @param chiHolder
	 *            控件
	 * @param parentId
	 *            父id
	 * @param index
	 *            子id
	 * @param num
	 */
	public void onClickChange(
			HashMap<Integer, HashMap<Integer, ArrayList<String>>> map_hashMap,
			ChildViewHolder chiHolder, int parentId, int index, String num) {
		ArrayList<String> selectResult = new ArrayList<String>();
		HashMap<Integer, ArrayList<String>> map = new HashMap<Integer, ArrayList<String>>();
		if (map_hashMap.containsKey(parentId)) {// 包含map
			map = map_hashMap.get(parentId);// 获取map
		}
		if (map.containsKey(index)) {// 包含list
			selectResult = map.get(index);// 获取list
		}
		if (selectResult.contains(num)) {// 包含结果则移除
			selectResult.remove(num);
		} else {
			selectResult.add(num);// 添加结果
		}
		map.put(index, selectResult);
		setDanBackGround(chiHolder, selectResult);// 改变胆的背景
		if (selectResult.contains("1")) {// 含胆
			if (1 == selectResult.size()) {// 集合为空
				map.remove(index);// 移除
				setDanBackGround(chiHolder, null);
			}
		} else {
			if (0 == selectResult.size()) {// 集合为空
				map.remove(index);// 移除
			}
		}
		map_hashMap.put(parentId, map);
		if (0 == map.size()) {
			map_hashMap.remove(parentId);
		}
		if (1 == playtype || 4 == playtype) {// 胜平负，让球胜平负
			change_spf_btn(chiHolder, selectResult);
		} else if (5 == playtype) {// 混合投注
			change_hhtz_btn(chiHolder, selectResult);// 刷新布局
		} else if (3 == playtype) {// 总进球
			change_zjq_btn(chiHolder, selectResult);
		} else if (2 == playtype || 6 == playtype) {// 半全场，比分
			set_CBF_BQC_Text(chiHolder, selectResult);
		}
		activity.clearPassType();//清空过关方式
	}

	/** 总进球 **/
	private void findChildView(ChildViewHolder holder, View v) {
		/** 总进球 **/
		holder.ll_btn_0 = (LinearLayout) v.findViewById(R.id.jczq_zjq_0);
		holder.ll_btn_1 = (LinearLayout) v.findViewById(R.id.jczq_zjq_1);
		holder.ll_btn_2 = (LinearLayout) v.findViewById(R.id.jczq_zjq_2);
		holder.ll_btn_3 = (LinearLayout) v.findViewById(R.id.jczq_zjq_3);
		holder.ll_btn_4 = (LinearLayout) v.findViewById(R.id.jczq_zjq_4);
		holder.ll_btn_5 = (LinearLayout) v.findViewById(R.id.jczq_zjq_5);
		holder.ll_btn_6 = (LinearLayout) v.findViewById(R.id.jczq_zjq_6);
		holder.ll_btn_7 = (LinearLayout) v.findViewById(R.id.jczq_zjq_7);

		holder.tv_0 = (TextView) v.findViewById(R.id.tv_0);
		holder.tv_1 = (TextView) v.findViewById(R.id.tv_1);
		holder.tv_2 = (TextView) v.findViewById(R.id.tv_2);
		holder.tv_3 = (TextView) v.findViewById(R.id.tv_3);
		holder.tv_4 = (TextView) v.findViewById(R.id.tv_4);
		holder.tv_5 = (TextView) v.findViewById(R.id.tv_5);
		holder.tv_6 = (TextView) v.findViewById(R.id.tv_6);
		holder.tv_7 = (TextView) v.findViewById(R.id.tv_7);

		holder.tv_0_1 = (TextView) v.findViewById(R.id.tv_0_1);
		holder.tv_1_1 = (TextView) v.findViewById(R.id.tv_1_1);
		holder.tv_2_1 = (TextView) v.findViewById(R.id.tv_2_1);
		holder.tv_3_1 = (TextView) v.findViewById(R.id.tv_3_1);
		holder.tv_4_1 = (TextView) v.findViewById(R.id.tv_4_1);
		holder.tv_5_1 = (TextView) v.findViewById(R.id.tv_5_1);
		holder.tv_6_1 = (TextView) v.findViewById(R.id.tv_6_1);
		holder.tv_7_1 = (TextView) v.findViewById(R.id.tv_7_1);
	}

	/** 改变 胜平负 /让球胜平负 背景 **/
	private void change_spf_btn(ChildViewHolder holder,
			ArrayList<String> selectList) {
		holder.btn_win
				.setBackgroundResource(R.drawable.select_sfc_lv_item_left);
		holder.btn_win.setTextColor(ColorUtil.BET_GRAY);
		holder.btn_flat
				.setBackgroundResource(R.drawable.select_sfc_lv_item_center);
		holder.btn_flat.setTextColor(ColorUtil.BET_GRAY);
		holder.btn_lose
				.setBackgroundResource(R.drawable.select_sfc_lv_item_right);
		holder.btn_lose.setTextColor(ColorUtil.BET_GRAY);
		if (null != selectList && 0 != selectList.size()) {
			if (selectList.contains("101") || selectList.contains("501")) {
				holder.btn_win
						.setBackgroundResource(R.drawable.select_sfc_lv_item_left_selected);
				holder.btn_win.setTextColor(Color.WHITE);
			}
			if (selectList.contains("102") || selectList.contains("502")) {
				holder.btn_flat
						.setBackgroundResource(R.drawable.select_sfc_lv_item_center_selected);
				holder.btn_flat.setTextColor(Color.WHITE);
			}
			if (selectList.contains("103") || selectList.contains("503")) {
				holder.btn_lose
						.setBackgroundResource(R.drawable.select_sfc_lv_item_right_selected);
				holder.btn_lose.setTextColor(Color.WHITE);
			}
		}
	}

	/** 混合投注改变 胜平负 button 背景 **/
	private void change_hhtz_btn(ChildViewHolder holder,
			ArrayList<String> selectList) {
		holder.ll_btn_hhtz1
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_hhtz_01.setTextColor(ColorUtil.BET_GRAY);
		holder.ll_btn_hhtz2
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_hhtz_02.setTextColor(ColorUtil.BET_GRAY);
		holder.ll_btn_hhtz3
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_hhtz_03.setTextColor(ColorUtil.BET_GRAY);
		holder.ll_btn_hhtz4
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_hhtz_04.setTextColor(ColorUtil.BET_GRAY);
		holder.ll_btn_hhtz5
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_hhtz_05.setTextColor(ColorUtil.BET_GRAY);
		holder.ll_btn_hhtz6
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_hhtz_06.setTextColor(ColorUtil.BET_GRAY);
		if (null != selectList && 0 != selectList.size()) {// 不为空时
			int size = selectList.size();
			if (selectList.contains("1")) {
				size = selectList.size() - 1;
			}
			holder.tv_result1.setText("已选");
			holder.tv_result1.setTextColor(Color.WHITE);
			holder.tv_result2.setText(size + "个");
			holder.tv_result2.setTextColor(Color.WHITE);
			holder.layout_result
					.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
			if (0 == size) {
				holder.tv_result1.setText("展开");
				holder.tv_result1.setTextColor(ColorUtil.BET_GRAY);
				holder.tv_result2.setText("全部");
				holder.tv_result2.setTextColor(ColorUtil.BET_GRAY);
				holder.layout_result
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
				setDanBackGround(holder, null);
			}
			if (selectList.contains("101")) {
				holder.ll_btn_hhtz1
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_hhtz_01.setTextColor(Color.WHITE);
			}
			if (selectList.contains("102")) {
				holder.ll_btn_hhtz2
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_hhtz_02.setTextColor(Color.WHITE);
			}
			if (selectList.contains("103")) {
				holder.ll_btn_hhtz3
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_hhtz_03.setTextColor(Color.WHITE);
			}
			if (selectList.contains("501")) {
				holder.ll_btn_hhtz4
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_hhtz_04.setTextColor(Color.WHITE);
			}
			if (selectList.contains("502")) {
				holder.ll_btn_hhtz5
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_hhtz_05.setTextColor(Color.WHITE);
			}
			if (selectList.contains("503")) {
				holder.ll_btn_hhtz6
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_hhtz_06.setTextColor(Color.WHITE);
			}
		} else {
			holder.tv_result1.setText("展开");
			holder.tv_result1.setTextColor(ColorUtil.BET_GRAY);
			holder.tv_result2.setText("全部");
			holder.tv_result2.setTextColor(ColorUtil.BET_GRAY);
			holder.layout_result
					.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		}
	}

	/** 改变 总进球 背景 **/
	private void change_zjq_btn(ChildViewHolder holder,
			ArrayList<String> selectList) {
		holder.ll_btn_0
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_0.setTextColor(Color.RED);
		holder.tv_0_1.setTextColor(ColorUtil.BET_GRAY);
		holder.ll_btn_1
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_1.setTextColor(Color.RED);
		holder.tv_1_1.setTextColor(ColorUtil.BET_GRAY);
		holder.ll_btn_2
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_2.setTextColor(Color.RED);
		holder.tv_2_1.setTextColor(ColorUtil.BET_GRAY);
		holder.ll_btn_3
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_3.setTextColor(Color.RED);
		holder.tv_3_1.setTextColor(ColorUtil.BET_GRAY);
		holder.ll_btn_4
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_4.setTextColor(Color.RED);
		holder.tv_4_1.setTextColor(ColorUtil.BET_GRAY);
		holder.ll_btn_5
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_5.setTextColor(Color.RED);
		holder.tv_5_1.setTextColor(ColorUtil.BET_GRAY);
		holder.ll_btn_6
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_6.setTextColor(Color.RED);
		holder.tv_6_1.setTextColor(ColorUtil.BET_GRAY);
		holder.ll_btn_7
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_7.setTextColor(Color.RED);
		holder.tv_7_1.setTextColor(ColorUtil.BET_GRAY);
		if (null != selectList) {
			if (selectList.contains("201")) {
				holder.ll_btn_0
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_0.setTextColor(Color.WHITE);
				holder.tv_0_1.setTextColor(Color.WHITE);
			}
			if (selectList.contains("202")) {
				holder.ll_btn_1
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_1.setTextColor(Color.WHITE);
				holder.tv_1_1.setTextColor(Color.WHITE);
			}
			if (selectList.contains("203")) {
				holder.ll_btn_2
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_2.setTextColor(Color.WHITE);
				holder.tv_2_1.setTextColor(Color.WHITE);
			}
			if (selectList.contains("204")) {
				holder.ll_btn_3
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_3.setTextColor(Color.WHITE);
				holder.tv_3_1.setTextColor(Color.WHITE);
			}
			if (selectList.contains("205")) {
				holder.ll_btn_4
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_4.setTextColor(Color.WHITE);
				holder.tv_4_1.setTextColor(Color.WHITE);
			}
			if (selectList.contains("206")) {
				holder.ll_btn_5
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_5.setTextColor(Color.WHITE);
				holder.tv_5_1.setTextColor(Color.WHITE);
			}
			if (selectList.contains("207")) {
				holder.ll_btn_6
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_6.setTextColor(Color.WHITE);
				holder.tv_6_1.setTextColor(Color.WHITE);
			}
			if (selectList.contains("208")) {
				holder.ll_btn_7
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_7.setTextColor(Color.WHITE);
				holder.tv_7_1.setTextColor(Color.WHITE);
			}
		}
	}

	/** 绑定总进球 显示信息 **/
	private void setChildView(ChildViewHolder holder, DtMatch dtm) {
		holder.tv_0_1.setText(dtm.getIn0());
		holder.tv_1_1.setText(dtm.getIn1());
		holder.tv_2_1.setText(dtm.getIn2());
		holder.tv_3_1.setText(dtm.getIn3());
		holder.tv_4_1.setText(dtm.getIn4());
		holder.tv_5_1.setText(dtm.getIn5());
		holder.tv_6_1.setText(dtm.getIn6());
		holder.tv_7_1.setText(dtm.getIn7());
	}

	/**
	 * 将id转换成比赛结果
	 * 
	 * @param number
	 * @return
	 */
	public String changeNumToResult(int number) {
		String result = "";
		switch (number) {
		case 401:
			result = "胜胜";
			break;
		case 402:
			result = "胜平";
			break;
		case 403:
			result = "胜负";
			break;
		case 404:
			result = "平胜";
			break;
		case 405:
			result = "平平";
			break;
		case 406:
			result = "平负";
			break;
		case 407:
			result = "负胜";
			break;
		case 408:
			result = "负平";
			break;
		case 409:
			result = "负负";
			break;
		case 301:
			result = "1:0";
			break;
		case 302:
			result = "2:0";
			break;
		case 303:
			result = "2:1";
			break;
		case 304:
			result = "3:0";
			break;
		case 305:
			result = "3:1";
			break;
		case 306:
			result = "3:2";
			break;
		case 307:
			result = "4:0";
			break;
		case 308:
			result = "4:1";
			break;
		case 309:
			result = "4:2";
			break;
		case 310:
			result = "5:0";
			break;
		case 311:
			result = "5:1";
			break;
		case 312:
			result = "5:2";
			break;
		case 313:
			result = "胜其他";
			break;
		case 314:
			result = "0:0";
			break;
		case 315:
			result = "1:1";
			break;
		case 316:
			result = "2:2";
			break;
		case 317:
			result = "3:3";
			break;
		case 318:
			result = "平其他";
			break;
		case 319:
			result = "0:1";
			break;
		case 320:
			result = "0:2";
			break;
		case 321:
			result = "1:2";
			break;
		case 322:
			result = "0:3";
			break;
		case 323:
			result = "1:3";
			break;
		case 324:
			result = "2:3";
			break;
		case 325:
			result = "0:4";
			break;
		case 326:
			result = "1:4";
			break;
		case 327:
			result = "2:4";
			break;
		case 328:
			result = "0:5";
			break;
		case 329:
			result = "1:5";
			break;
		case 330:
			result = "2:5";
			break;
		case 331:
			result = "负其他";
			break;
		}
		return result;

	}

	/** 给猜比分和半全场按钮设置显示值 **/
	private void set_CBF_BQC_Text(ChildViewHolder chiHolder,
			ArrayList<String> selectList) {
		String s = "";
		if (6 == playtype) {// 半全场
			if (null != selectList && 0 != selectList.size()) {
				s = changeOddsToResult(selectList);
				chiHolder.btn_open_cbf.setTextColor(Color.WHITE);
				chiHolder.btn_open_cbf
						.setBackgroundResource(R.drawable.select_jc_bg_red);
				if ("".equals(s)) {// 如果为空
					s = "点击展开投注区";
					chiHolder.btn_open_cbf.setTextColor(ColorUtil.BET_GRAY);
					chiHolder.btn_open_cbf
							.setBackgroundResource(R.drawable.select_jc_bg_white);
					setDanBackGround(chiHolder, null);
					selectList.remove("1");
				}
			} else {
				s = "点击展开投注区";
				chiHolder.btn_open_cbf.setTextColor(ColorUtil.BET_GRAY);
				chiHolder.btn_open_cbf
						.setBackgroundResource(R.drawable.select_jc_bg_white);
			}
		} else if (2 == playtype) {// 猜比分
			if (null != selectList && 0 != selectList.size()) {
				s = changeOddsToResult(selectList);
				chiHolder.btn_open_cbf.setTextColor(Color.WHITE);
				chiHolder.btn_open_cbf
						.setBackgroundResource(R.drawable.select_jc_bg_red);
				if ("".equals(s)) {// 如果为空
					s = "点击展开投注区";
					chiHolder.btn_open_cbf.setTextColor(ColorUtil.BET_GRAY);
					chiHolder.btn_open_cbf
							.setBackgroundResource(R.drawable.select_jc_bg_white);
					setDanBackGround(chiHolder, null);
					selectList.remove("1");
				}
			} else {
				s = "请选择比分投注";
				chiHolder.btn_open_cbf.setTextColor(ColorUtil.BET_GRAY);
				chiHolder.btn_open_cbf
						.setBackgroundResource(R.drawable.select_jc_bg_white);
			}
		}
		chiHolder.btn_open_cbf.setText(s);
	}

	/**
	 * * 转换半全场和比分数据
	 * 
	 * @return 转换后的结果
	 * @param resultlist
	 *            选择结果
	 */

	public String changeOddsToResult(ArrayList<String> resultlist) {
		StringBuffer result = new StringBuffer();
		int[] array = new int[resultlist.size()];
		for (int i = 0; i < resultlist.size(); i++) {
			array[i] = Integer.parseInt(resultlist.get(i).trim());
		}
		Arrays.sort(array);
		for (int i = 0; i < array.length; i++) {
			if (!"".equals(changeNumToResult(array[i]))) {
				result.append("|" + changeNumToResult(array[i]));
			}
//			if (array.length <= 9) {
//			} else {
//				if (i < 4 || (i > array.length - 4 && i < array.length)) {// 前四个和后四个
//					if (4 == i) {
//						result.append("...");
//					}
//					if (!"".equals(changeNumToResult(array[i]))) {
//						result.append("|" + changeNumToResult(array[i]));
//					}
//				}
//			}
		}
//		Log.i(TAG, "结果--" + result + "--");
		if ("".equals(result.toString())) {// 如果为空
			return "";
		} else {
			return result.substring(1);
		}
	}

	/** 组空间 */
	static class GroupViewHolder {
		TextView tv_date;
		TextView tv_count;
	}

	/** 子类控件 */
	static class ChildViewHolder {

		TextView mainTeam, guestTeam, ball;

		Button btn_win;
		Button btn_flat;
		Button btn_lose;
		TextView btn_open_cbf; // 展开猜比分按钮
		TextView tv_dan;// 胆
		ImageView iv_delete;// 删除按钮
		LinearLayout layout_isspf;//胜平负布局
		LinearLayout hhtz_bottom;//让球胜平负布局

		TextView tv_tip_isnotrqspf;//让球胜平负暂停销售
		TextView tv_tip_isnotspf;//胜平负暂停销售

		LinearLayout ll_spf, ll_cbf, ll_zjq, ll_hhtz;

		LinearLayout ll_btn_0, ll_btn_1, ll_btn_2, ll_btn_3, ll_btn_4,
				ll_btn_5, ll_btn_6, ll_btn_7;
		// 混合投注點擊按鈕
		LinearLayout ll_btn_hhtz1, ll_btn_hhtz2, ll_btn_hhtz3, ll_btn_hhtz4,
				ll_btn_hhtz5, ll_btn_hhtz6, layout_result, layout_hhtz_rq;
		TextView tv_hhtz_01, tv_hhtz_02, tv_hhtz_03, tv_hhtz_04, tv_hhtz_05,
				tv_hhtz_06, tv_result1, tv_result2;// 胜平负

		TextView tv_0, tv_0_1, tv_1, tv_1_1, tv_2, tv_2_1, tv_3, tv_3_1, tv_4,
				tv_4_1, tv_5, tv_5_1, tv_6, tv_6_1, tv_7, tv_7_1, tv_hhtz_rq;

		SelectJCZQDialog dialog;
	}
}
