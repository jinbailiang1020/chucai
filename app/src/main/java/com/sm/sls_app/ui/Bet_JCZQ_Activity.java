package com.sm.sls_app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.DtMatch;
import com.sm.sls_app.dataaccess.SelectedNumbers;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.ui.adapter.ExpandAdapterJCZQPassMore;
import com.sm.sls_app.ui.adapter.ExpandAdapterJCZQPassSingle;
import com.sm.sls_app.ui.adapter.MyBetLotteryJCZQAdapter;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.ColorUtil;
import com.sm.sls_app.utils.NumberTools;
import com.sm.sls_app.view.ConfirmDialog;
import com.sm.sls_app.view.MyListView2;
import com.sm.sls_app.view.MyToast;
import com.sm.sls_app.view.SelectPassTypePopupWindow;

/**
 * 功能：投注页面 版本
 * 
 * @author Kinwee 修改日期2014-12-11
 * 
 */
public class Bet_JCZQ_Activity extends Activity implements OnClickListener {

	private final static String TAG = "Bet_JCZQ_Activity";
	/* 头部 */
	private RelativeLayout layout_main;// 主
	private ImageButton btn_back; // 返回
	private ImageView iv_up_down;// 玩法提示图标
	private Button btn_playtype;// 玩法
	private ImageButton btn_help;// 帮助
	private ConfirmDialog dialog;// 提示框

	/* 尾部 */
	private Button btn_follow; // 发起合买
	private Button btn_clearall; // 清除全部
	private Button btn_submit; // 付款
	public TextView tv_tatol_count;// 总注数
	public TextView tv_tatol_money;// 总钱数

	private String opt = "11"; // 格式化后的 opt
	private String auth, info, time, imei, crc; // 格式化后的参数
	private LinearLayout btn_continue_select;// 继续选择比赛
	private ImageView bet_btn_deleteall;// 清空
	private TextView bet_tv_guize; // 委托投注规则

	private RelativeLayout layout_notjc;// 非竞彩布局
	private RelativeLayout layout_jc;// 竞彩布局
	private TextView tv_show_passway;// 过关
	private RelativeLayout layout_cbs;// 复选框布局

	private MyListView2 bet_lv_scheme;// 自定义listview
	public MyBetLotteryJCZQAdapter adapter;

	private Intent intent;

	private int playtype;
	private int passtype;

	private long sumCount, totalMoney; // 方案总注数 // 方案总金额

	private EditText et_bei; // 用户输入的倍数
	private MyHandler myHandler;
	private MyAsynTask myAsynTask;

	private CheckBox bet_cb_stopfollow;
	private int isStopChase = 1;

	private SelectPassTypePopupWindow selectPTpop;// 选择过关方式
	private ArrayList<String> selectPasstype = new ArrayList<String>();// 选择的过关方式

	private int viewPagerCurrentIndex = 0;// 过关类型 0.多串1 1.多串多
	private List<String> eachSelectDT;

	private int countDan;// 胆的个数
	private int dtCount;// 获取已选择的比赛场次

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bet_jc);
		App.activityS.add(this);
		App.activityS1.add(this);
		findView();
	}

	/** 初始化UI */
	private void findView() {
		myHandler = new MyHandler();
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_playtype = (Button) findViewById(R.id.btn_playtype);
		btn_help = (ImageButton) findViewById(R.id.btn_help);
		iv_up_down = (ImageView) findViewById(R.id.iv_up_down);
		btn_clearall = (Button) findViewById(R.id.btn_clearall);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_continue_select = (LinearLayout) this
				.findViewById(R.id.btn_continue_select);
		bet_btn_deleteall = (ImageView) this
				.findViewById(R.id.bet_btn_deleteall);
		btn_follow = (Button) this.findViewById(R.id.btn_follow);
		et_bei = (EditText) this.findViewById(R.id.et_bei);

		tv_tatol_count = (TextView) this.findViewById(R.id.tv_tatol_count);
		tv_tatol_money = (TextView) this.findViewById(R.id.tv_tatol_money);
		bet_cb_stopfollow = (CheckBox) this
				.findViewById(R.id.bet_cb_stopfollow);
		bet_tv_guize = (TextView) this.findViewById(R.id.bet_tv_guize);

		bet_lv_scheme = (MyListView2) this.findViewById(R.id.bet_lv_scheme);
		layout_notjc = (RelativeLayout) this.findViewById(R.id.layout_notjc);
		layout_jc = (RelativeLayout) this.findViewById(R.id.layout_jc);
		layout_main = (RelativeLayout) this.findViewById(R.id.layout_main);
		tv_show_passway = (TextView) this.findViewById(R.id.tv_show_passway);
		layout_cbs = (RelativeLayout) this.findViewById(R.id.layout_cbs);
		btn_playtype.setText("竞彩足球投注单");

		// 隐藏与显示
		btn_help.setVisibility(View.GONE);
		btn_clearall.setVisibility(View.GONE);
		iv_up_down.setVisibility(View.GONE);
		layout_notjc.setVisibility(View.GONE);
		layout_cbs.setVisibility(View.GONE);
		btn_follow.setVisibility(View.VISIBLE);
		layout_jc.setVisibility(View.VISIBLE);
	}

	/** 初始化属性 */
	private void init() {
		playtype = getIntent().getIntExtra("playtype", 5);
		passtype = getIntent().getIntExtra("passtype", 0);
		AppTools.qi = 1;
		adapter = new MyBetLotteryJCZQAdapter(Bet_JCZQ_Activity.this, playtype,
				passtype);
		changeTextShow();
		initPassType();
		btn_submit.setText("付款");
		dialog = new ConfirmDialog(this, R.style.dialog);
	}

	public void initPassType() {
		if (0 == passtype) {// 过关
			Spanned text = Html.fromHtml(AppTools.changeStringColor("#808080",
					"过关方式(")
					+ AppTools.changeStringColor("#e3393c", "必选")
					+ AppTools.changeStringColor("#808080", ")"));
			tv_show_passway.setText(text);// 过关
			tv_show_passway
					.setBackgroundResource(R.drawable.select_jc_bg_white);// 设置背景
			tv_show_passway.setEnabled(true);// 可用
		} else {// 单关
			tv_show_passway.setText("单关");// 过关
			tv_show_passway.setTextColor(ColorUtil.BET_GRAY);
			tv_show_passway
					.setBackgroundResource(R.drawable.select_jc_bg_white);// 设置背景
			tv_show_passway.setEnabled(false);// 不可用
		}
	}

	/** 设置监听 */
	private void setListener() {
		btn_continue_select.setOnClickListener(this);
		bet_btn_deleteall.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		btn_follow.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		bet_lv_scheme.setAdapter(adapter);
		et_bei.addTextChangedListener(bei_textWatcher);
		bet_tv_guize.setOnClickListener(this);
		tv_show_passway.setOnClickListener(this);
		bet_cb_stopfollow.setOnCheckedChangeListener(new MyCheckChange());
	}

	/** 复选框 **/
	class MyCheckChange implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked)
				isStopChase = 1;
			else
				isStopChase = 0;
		}
	}

	/** 当文本的值改变时 */
	private TextWatcher bei_textWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
		}

		@Override
		public void afterTextChanged(Editable edt) {
			// TODO Auto-generated method stub
			if (edt.toString().trim().length() != 0) {
				if (Integer.parseInt(edt.toString().trim()) > 999) {
					et_bei.setSelection(et_bei.getText().length());
					MyToast.getToast(getApplicationContext(), "最大倍数为999")
							.show();
					et_bei.setText("999");
				}
				if (Integer.parseInt(edt.toString().trim()) == 0) {
					et_bei.setText("1");
					et_bei.setSelection(et_bei.getText().length());
					MyToast.getToast(getApplicationContext(), "最小为1倍").show();
				}
				// if (edt.toString().substring(0, 1).equals("0")) {
				// et_bei.setText(edt.toString().subSequence(1,
				// edt.toString().length()));
				// et_bei.setSelection(0);
				// }
				if (0 != sumCount) {
					setSelectNumAndGetCount();
				}
				changeTextShow();
			}
			setCursorPosition(et_bei);
		}
	};

	public void setCursorPosition(EditText et) {
		CharSequence text = et.getText();
		if (text instanceof Spannable) {
			Selection.setSelection((Spannable) text, text.length());
		}
	}

	/** 公用Button 点击监听 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/** 手选 **/
		case R.id.btn_continue_select:
			btn_handClick();
			break;
		/** 返回 **/
		case R.id.btn_back:
			backToPre();
			break;
		/** 过关方式 **/
		case R.id.tv_show_passway:
			// Log.i(TAG, "选择过关方式");
			selectPassType();
			break;
		/** 清空 **/
		case R.id.bet_btn_deleteall:
			dialog.show();
			dialog.setDialogContent("是否清空投注单号码");
			dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {

				@Override
				public void getResult(int resultCode) {
					// TODO Auto-generated method stub
					if (1 == resultCode) {// 确定
						ExpandAdapterJCZQPassMore.clearAllDate();
						ExpandAdapterJCZQPassSingle.clearAllDate();
						AppTools.totalCount = 0;
						adapter.index = new HashMap<Integer, HashMap<Integer, Integer>>();
						adapter.bet_List_Matchs = new ArrayList<DtMatch>();
						adapter.setBetListMatchs();
						adapter.notifyDataSetChanged();
						selectPasstype.clear();// 清空过关方式
						initPassType();// 初始化过关方式
						changeTextShow();
					}
				}
			});
			break;
		/** 付款 **/
		case R.id.btn_submit:
			if (0 == passtype) {// 过关
				if (0 != selectPasstype.size()) {
					setEnabled(false);
					myAsynTask = new MyAsynTask();
					myAsynTask.execute();
				} else {// 未选过关方式
					selectPassType();
				}
			} else {// 单关
				setEnabled(false);
				myAsynTask = new MyAsynTask();
				myAsynTask.execute();
			}
			break;
		/** 发起合买 **/
		case R.id.btn_follow:
			join();
			break;
		case R.id.bet_tv_guize:
			Intent intent = new Intent(Bet_JCZQ_Activity.this,
					PlayDescription.class);
			intent.putExtra("type", 0);
			Bet_JCZQ_Activity.this.startActivity(intent);
			break;
		}
	}

	public void clearPassType() {
		selectPasstype.clear();
		initPassType();
		sumCount = 0;
		totalMoney = 0;
		changeTextShow();

	}

	/**
	 * 选择过关方式
	 */
	public void selectPassType() {
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
		countDan = adapter.selectDanCount;// 胆的个数
		dtCount = adapter.getSelectDtMatchCount(map_hashMap);// 获取已选择的比赛场次
		selectPTpop = new SelectPassTypePopupWindow(getApplicationContext(),
				countDan, dtCount, viewPagerCurrentIndex, layout_main);
		if (0 != selectPasstype.size()) {// 不为空
			selectPTpop.setSelectPassType(selectPasstype);
		}
		selectPTpop.createPopWindow();
		selectPTpop
				.setDialogResultListener(new SelectPassTypePopupWindow.DialogResultListener() {

					@Override
					public void getResult(int resultCode,
							ArrayList<String> selectResult, int type) {
						// TODO Auto-generated method stub
						if (1 == resultCode) {// 确定
							viewPagerCurrentIndex = type;
							selectPasstype = selectResult;
							if (0 != selectPasstype.size()) {// 不为空
								StringBuffer show = new StringBuffer();
								for (int i = 0; i < selectPasstype.size(); i++) {
									HashMap<String, String> passtypeMap = selectPTpop
											.getPASSTYPE_MAP();
									show.append(","
											+ passtypeMap.get(selectPasstype
													.get(i)));
								}
								tv_show_passway.setText(show.substring(1));// 过关
								tv_show_passway.setTextColor(Color.WHITE);
								tv_show_passway
										.setBackgroundResource(R.drawable.select_jc_bg_red);// 设置背景
								setSelectNumAndGetCount();// 设置投注格式,计算注数
								changeTextShow();
							} else {
								initPassType();
							}
						}
					}
				});
	}

	public void setSelectNumAndGetCount() {
		sumCount = 0;
		eachSelectDT = new ArrayList<String>();
		List<String> dan_list = new ArrayList<String>();// 存放胆的集合
		StringBuffer sb = new StringBuffer();
		int playType = 0;
		switch (playtype) {
		case 1:// 让球胜平负
			playType = 7201;
			break;
		case 2:// 比分
			playType = 7202;
			break;
		case 3:// 总进球
			playType = 7203;
			break;
		case 4:// 胜平负
			playType = 7207;
			break;
		case 5:// 混合投注
			playType = 7206;
			break;
		case 6:// 半全场
			playType = 7204;
			break;
		}
		sb.append(playType + ";");
		StringBuffer notdan_sb = new StringBuffer();// 用来拼接
		StringBuffer dan_sb = new StringBuffer();// 用来拼接
		if (0 == passtype) {// 过关
			sb.append("[");
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
				// 拼接投注格式
				if (map_hashMap.containsKey(i)) {// 包含父下标
					HashMap<Integer, Integer> cids = new HashMap<Integer, Integer>();
					Map<Integer, ArrayList<String>> map = map_hashMap.get(i);
					Set<Integer> cset = map.keySet();
					int cursor = 0;
					if (0 == countDan) {// 没有设胆
						for (Integer cid : cset) {// 遍历已选每一个对阵
							DtMatch dm = ExpandAdapterJCZQPassMore.list_Matchs
									.get(i).get(cid);
							ArrayList<String> selectNum = map.get(cid);// 一场比赛选的结果
							StringBuffer each = new StringBuffer();
							sb.append(dm.getMatchId() + "(");
							// 用于混合投注拼接特殊字符串计算注数
							StringBuffer each1 = new StringBuffer();
							StringBuffer each2 = new StringBuffer();
							StringBuffer each3 = new StringBuffer();
							StringBuffer each4 = new StringBuffer();
							StringBuffer each5 = new StringBuffer();
							for (int j = 0; j < selectNum.size(); j++) {
								String str = selectNum.get(j);
								if (7206 != playType) {// 非混合投注
									each.append("1");
									for (int n = 101; n <= 103; n++) {// 胜平负
										if (str.contains(n + "")) {// 如果包含则替换
											String old = n + "";
											str = str.replace(old, old
													.substring(
															old.length() - 1,
															old.length()));
										}
									}
									for (int h = 201; h <= 208; h++) {// 总进球数
										if (str.contains(h + "")) {// 如果包含则替换
											String old = h + "";
											str = str.replace(old, old
													.substring(
															old.length() - 1,
															old.length()));
										}
									}
									for (int k = 301; k <= 331; k++) {// 比分
										if (str.contains(k + "")) {// 如果包含则替换
											String old = k + "";
											if (k < 310) {
												str = str
														.replace(
																old,
																old.substring(
																		old.length() - 1,
																		old.length()));
											} else {
												str = str
														.replace(
																old,
																old.substring(
																		old.length() - 2,
																		old.length()));
											}
										}
									}
									for (int l = 401; l <= 409; l++) {// 半全场
										if (str.contains(l + "")) {// 如果包含则替换
											String old = l + "";
											str = str.replace(old, old
													.substring(
															old.length() - 1,
															old.length()));
										}
									}
									for (int m = 501; m <= 503; m++) {// 让球胜平负
										if (str.contains(m + "")) {// 如果包含则替换
											String old = m + "";
											str = str.replace(old, old
													.substring(
															old.length() - 1,
															old.length()));
										}
									}
								} else {// 混合投注
									for (int n = 101; n <= 103; n++) {// 胜平负
										if (str.contains(n + "")) {// 如果包含则替换
											each1.append("1,");
											String old = n + "";
											str = str
													.replace(old, (n - 1) + "");
										}
									}

									for (int h = 201; h <= 208; h++) {// 总进球数
										if (str.contains(h + "")) {// 如果包含则替换
											each2.append("2,");
											String old = h + "";
											str = str
													.replace(old, (h - 1) + "");
										}
									}
									for (int k = 301; k <= 331; k++) {// 比分
										if (str.contains(k + "")) {// 如果包含则替换
											each3.append("3,");
											String old = k + "";
											str = str
													.replace(old, (k - 1) + "");
										}
									}
									for (int l = 401; l <= 409; l++) {// 半全场
										if (str.contains(l + "")) {// 如果包含则替换
											each4.append("4,");
											String old = l + "";
											str = str
													.replace(old, (l - 1) + "");
										}
									}
									for (int m = 501; m <= 503; m++) {// 让球胜平负
										if (str.contains(m + "")) {// 如果包含则替换
											each5.append("5,");
											String old = m + "";
											str = str
													.replace(old, (m - 1) + "");
										}
									}
								}
								if (0 == j) {
									sb.append(str);
								} else {
									sb.append("," + str);
								}
							}
							Log.i(TAG, "each1"+each1);
							Log.i(TAG, "each2"+each2);
							Log.i(TAG, "each3"+each3);
							Log.i(TAG, "each4"+each4);
							Log.i(TAG, "each5"+each5);
							if (0 != each1.length()) {// 胜平负
								String str1 = "";
								if (0 == each2.length() && 0 == each3.length()
										&& 0 == each4.length()
										&& 0 == each5.length()) {// 只有胜平负
									str1 = each1.substring(0,
											each1.length()-1);
								} else {
									str1 = each1.substring(0,
											each1.length()-1) + "|";
								}
								each.append(str1);
							}
							if (0 != each2.length()) {// 总进球数
								String str2 = "";
								if (0 == each3.length() && 0 == each4.length()
										&& 0 == each5.length()) {// 没有比分，半全场，让球胜平负
									str2 = each2.substring(0,
											each2.length()-1);
								} else {
									str2 = each2.substring(0,
											each2.length()-1) + "|";
								}
								each.append(str2);
							}
							if (0 != each3.length()) {// 比分
								String str3 = "";
								if (0 == each4.length() && 0 == each5.length()) {// 没有半全场，让球胜平负
									str3 = each3.substring(0,
											each3.length()-1);
								} else {
									str3 = each3.substring(0,
											each3.length()-1) + "|";
								}
								each.append(str3);
							}
							if (0 != each4.length()) {// 半全场
								String str4 = "";
								if (0 == each5.length()) {// 没有半全场，让球胜平负
									str4 = each4.substring(0,
											each4.length()-1);
								} else {
									str4 = each4.substring(0,
											each4.length()-1) + "|";
								}
								each.append(str4);
							}
							if (0 != each5.length()) {// 让球胜平负
								String str5 = each5.substring(
										0, each5.length()-1);
								each.append(str5);
							}
							if (7206 == playType)
								Log.i(TAG, "混合投注拼接字符串" + each);
							eachSelectDT.add(each.toString());// 添加每一场对阵选的结果
							sb.append(")|");
						}
					} else {// 已设胆
						for (Integer cid : cset) {// 遍历已选每一个对阵
							DtMatch dm = ExpandAdapterJCZQPassMore.list_Matchs
									.get(i).get(cid);
							ArrayList<String> selectNum = map.get(cid);
							if (selectNum.contains("1")) {// 选胆了
								dan_sb.append(dm.getMatchId() + "(");
								StringBuffer dan_sb_list = new StringBuffer();// 加入danlist
								for (int j = 0; j < selectNum.size(); j++) {// 一场对阵
									String str = selectNum.get(j);// 对阵的一个结果
									if (!"1".equals(str)) {// 非胆
										for (int n = 101; n <= 103; n++) {// 胜平负
											if (str.contains(n + "")) {// 如果包含则替换
												String old = n + "";
												str = str
														.replace(
																old,
																old.substring(
																		old.length() - 1,
																		old.length()));
											}
										}
										for (int h = 201; h <= 208; h++) {// 总进球数
											if (str.contains(h + "")) {// 如果包含则替换
												String old = h + "";
												str = str
														.replace(
																old,
																old.substring(
																		old.length() - 1,
																		old.length()));
											}
										}
										for (int k = 301; k <= 331; k++) {// 比方
											if (str.contains(k + "")) {// 如果包含则替换
												String old = k + "";
												if (k < 310) {
													str = str
															.replace(
																	old,
																	old.substring(
																			old.length() - 1,
																			old.length()));
												} else {
													str = str
															.replace(
																	old,
																	old.substring(
																			old.length() - 2,
																			old.length()));
												}
											}
										}
										for (int l = 401; l <= 409; l++) {// 半全场
											if (str.contains(l + "")) {// 如果包含则替换
												String old = l + "";
												str = str
														.replace(
																old,
																old.substring(
																		old.length() - 1,
																		old.length()));
											}
										}
										for (int m = 501; m <= 503; m++) {// 让球胜平负
											if (str.contains(m + "")) {// 如果包含则替换
												String old = m + "";
												str = str
														.replace(
																old,
																old.substring(
																		old.length() - 1,
																		old.length()));
											}
										}
										dan_sb_list.append("1");// 将每一个结果拼接
										if (0 == j) {
											dan_sb.append(str);
										} else {
											dan_sb.append("," + str);
										}
									}
								}
								dan_sb.append(")|");// 一场比赛的所有结果
								dan_list.add(dan_sb_list.toString());// 将拼接的所选胆的结果放入集合dan_list
							} else {// 未选胆的
								notdan_sb.append(dm.getMatchId() + "(");
								StringBuffer notdan_sb_list = new StringBuffer();// 加入list
								for (int j = 0; j < selectNum.size(); j++) {// 一场对阵
									String str = selectNum.get(j);// 对阵的一个结果

									for (int n = 101; n <= 103; n++) {// 胜平负
										if (str.contains(n + "")) {// 如果包含则替换
											String old = n + "";
											str = str.replace(old, old
													.substring(
															old.length() - 1,
															old.length()));
										}
									}
									for (int h = 201; h <= 208; h++) {// 总进球数
										if (str.contains(h + "")) {// 如果包含则替换
											String old = h + "";
											str = str.replace(old, old
													.substring(
															old.length() - 1,
															old.length()));
										}
									}
									for (int k = 301; k <= 331; k++) {// 比分
										if (str.contains(k + "")) {// 如果包含则替换
											String old = k + "";
											if (k < 310) {
												str = str
														.replace(
																old,
																old.substring(
																		old.length() - 1,
																		old.length()));
											} else {
												str = str
														.replace(
																old,
																old.substring(
																		old.length() - 2,
																		old.length()));
											}
										}
									}
									for (int l = 401; l <= 409; l++) {// 半全场
										if (str.contains(l + "")) {// 如果包含则替换
											String old = l + "";
											str = str.replace(old, old
													.substring(
															old.length() - 1,
															old.length()));
										}
									}
									for (int m = 501; m <= 503; m++) {// 让球胜平负
										if (str.contains(m + "")) {// 如果包含则替换
											String old = m + "";
											str = str.replace(old, old
													.substring(
															old.length() - 1,
															old.length()));
										}
									}
									notdan_sb_list.append("1");// 将每一个结果拼接
									if (0 == j) {
										notdan_sb.append(str);
									} else {
										notdan_sb.append("," + str);
									}
								}
								notdan_sb.append(")|");// 一场比赛的所有结果
								eachSelectDT.add(notdan_sb_list.toString());// 将拼接的所选胆的结果放入集合dan_list
							}
						}
					}
				}
			}
			if (0 != dan_sb.length() && 0 != notdan_sb.length()) {
				// dan ()|()| notdan [()|()|
				Log.i(TAG, "未加胆sb" + sb);
				String dan = dan_sb.toString();
				dan = dan_sb.substring(0, dan_sb.length() - 1);// 去除最后一个“|”
				Log.i(TAG, "dan" + dan);
				sb.append(dan + "][");// 加胆右边中括号和非胆左中括号
				Log.i(TAG, "加胆sb" + sb);
				// String notdan = notdan_sb.substring(0, notdan_sb.length() -
				// 1);// 去除最后一个“|”
				String notdan = notdan_sb.toString();// 去除最后一个“|”
				Log.i(TAG, "notdan" + notdan);
				sb.append(notdan);
			}
			sb = new StringBuffer(sb.substring(0, sb.length() - 1));// 去掉“|”
			Log.i(TAG, "sb" + sb);
			sb.append("]");
		} else if (1 == passtype) {// 单关
			Log.i(TAG, "单关计算注数");
			sb.append("[");
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
						DtMatch dm = ExpandAdapterJCZQPassSingle.list_Matchs
								.get(i).get(cid);
						StringBuffer each = new StringBuffer();
						sb.append(dm.getMatchId() + "(");
						ArrayList<String> selectNum = map.get(cid);
						for (int j = 0; j < selectNum.size(); j++) {
							String str = selectNum.get(j);
							// 非混合投注
							for (int n = 101; n <= 103; n++) {// 胜平负
								if (str.contains(n + "")) {// 如果包含则替换
									String old = n + "";
									str = str.replace(
											old,
											old.substring(old.length() - 1,
													old.length()));
								}
							}
							for (int h = 201; h <= 208; h++) {// 总进球数
								if (str.contains(h + "")) {// 如果包含则替换
									String old = h + "";
									str = str.replace(
											old,
											old.substring(old.length() - 1,
													old.length()));
								}
							}
							for (int k = 301; k <= 331; k++) {// 比分
								if (str.contains(k + "")) {// 如果包含则替换
									String old = k + "";
									if (k < 310) {
										str = str
												.replace(old, old.substring(
														old.length() - 1,
														old.length()));
									} else {
										str = str
												.replace(old, old.substring(
														old.length() - 2,
														old.length()));
									}
								}
							}
							for (int l = 401; l <= 409; l++) {// 半全场
								if (str.contains(l + "")) {// 如果包含则替换
									String old = l + "";
									str = str.replace(
											old,
											old.substring(old.length() - 1,
													old.length()));
								}
							}
							for (int m = 501; m <= 503; m++) {// 让球胜平负
								if (str.contains(m + "")) {// 如果包含则替换
									String old = m + "";
									str = str.replace(
											old,
											old.substring(old.length() - 1,
													old.length()));
								}
							}
							each.append("1");
							if (0 == j) {
								sb.append(str);
							} else {
								sb.append("," + str);
							}
						}
						eachSelectDT.add(each.toString());
						sb.append(")|");
						Log.i(TAG, "每一次sb的值" + sb.toString());
					}
					Log.i(TAG, "拼接之后sb的值" + sb.toString());
				}
			}
			sb = new StringBuffer(sb.substring(0, sb.length() - 1));// 去掉“|”
			sb.append("]");
		}
		StringBuffer passtype = new StringBuffer();
		for (int i = 0; i < selectPasstype.size(); i++) {
			if (0 == i) {
				passtype.append(selectPasstype.get(i) + AppTools.bei);
			} else {

				passtype.append("," + selectPasstype.get(i) + AppTools.bei);
			}
		}
		if (1 == this.passtype) {// 单关
			passtype.append("A0" + AppTools.bei);
		}
		Log.i(TAG, "拼接字符串--" + sb.toString());
		String str = "";
		if (0 != countDan) {// 含胆
			str = sb.toString() + ";[" + passtype + "];[" + countDan + "]";// 拼接成lotteryNumber
		} else {
			str = sb.toString() + ";[" + passtype + "]";// 拼接成lotteryNumber
		}
		// Log.i(TAG, "lotteryNumber" + str);
		AppTools.list_numbers = new ArrayList<SelectedNumbers>();
		SelectedNumbers selectNumber = new SelectedNumbers();
		selectNumber.setPlayType(playType);// 设置玩法类型
		selectNumber.setLotteryNumber(str);// 设置投注号码
		StringBuffer selectpass = new StringBuffer();

		for (int i = 0; i < selectPasstype.size(); i++) {
			if (0 == i) {
				selectpass.append(selectPasstype.get(i));
			} else {
				selectpass.append(" " + selectPasstype.get(i));
			}
		}
		if (1 == this.passtype) {// 单关
			selectpass.append(passtype.toString());
		}
		// Log.i(TAG, "eachSelectDT大小--" + eachSelectDT.size() + "");
		// for (int i = 0; i < eachSelectDT.size(); i++) {
		// Log.i(TAG, "每个--" + eachSelectDT.get(i));
		// }
		// Log.i(TAG, "selectpass--" + selectpass.toString());
		// 计算注数
		if (0 == countDan) {// 无胆
			if(7206 != playType){//非混合投注
				setTotalCount(selectpass.toString(), eachSelectDT);
			}else{//混合投注
				setTotalCountHHTZ(selectpass.toString(), eachSelectDT);
			}
		} else {// 有胆
			// Log.i(TAG, "胆的集合");
			// for (int j = 0; j < dan_list.size(); j++) {
			// Log.i(TAG, "元素---" + dan_list.get(j));
			// }
			// Log.i(TAG, "对阵的集合");
			// for (int j = 0; j < eachSelectDT.size(); j++) {
			// Log.i(TAG, "元素---" + eachSelectDT.get(j));
			// }
			setTotalCount(selectpass.toString(), dan_list, eachSelectDT);
		}
		// Log.i(TAG, "总注数--" + sumCount);
		selectNumber.setCount(sumCount);// 设置投注注数
		totalMoney = sumCount * 2 * AppTools.bei;
		selectNumber.setMoney(sumCount * 2);// 设置投注钱数
		AppTools.list_numbers.add(selectNumber);
	}

	/** 设置是否可用 **/
	private void setEnabled(boolean isEna) {
		btn_submit.setEnabled(isEna);
		bet_btn_deleteall.setEnabled(isEna);
		btn_continue_select.setEnabled(isEna);
		btn_follow.setEnabled(isEna);
		bet_cb_stopfollow.setEnabled(isEna);
		et_bei.setEnabled(isEna);
	}

	/** 跳到合买 */
	private void join() {
		if (AppTools.qi > 1) {
			dialog.show();
			dialog.setDialogContent("发起合买时不能追号，是否只追一期并继续发起合买？");
			dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {

				@Override
				public void getResult(int resultCode) {
					// TODO Auto-generated method stub
					if (1 == resultCode) {// 确定
						AppTools.qi = 1;
						int total = 0; // 总金额
						for (SelectedNumbers num : AppTools.list_numbers) {
							total += num.getMoney();
						}
						total = total * AppTools.bei;
						intent = new Intent(Bet_JCZQ_Activity.this,
								JoinActivity.class);
						intent.putExtra("totalMoney", total + "");
						Bet_JCZQ_Activity.this.startActivity(intent);
					}
				}
			});
		} else {
			int total = 0; // 总金额
			for (SelectedNumbers num : AppTools.list_numbers) {
				total += num.getMoney();
			}
			if (total == 0) {
				MyToast.getToast(getApplicationContext(), "您还没有选择号码").show();
				return;
			}
			total = total * AppTools.bei;
			intent = new Intent(Bet_JCZQ_Activity.this, JoinActivity.class);
			intent.putExtra("totalMoney", total + "");
			Bet_JCZQ_Activity.this.startActivity(intent);
		}
	}

	/** 手选按钮点击事件 */
	private void btn_handClick() {
		AppTools.totalCount = 0;
		Intent intent = new Intent(Bet_JCZQ_Activity.this,
				Select_jczqActivity.class);
		intent.putExtra("passtype", passtype);
		int playType = 7201;
		switch (playtype) {
		case 1:// 让球胜平负
			playType = 7201;
			break;
		case 2:// 比分
			playType = 7202;
			break;
		case 3:// 总进球
			playType = 7203;
			break;
		case 4:// 胜平负
			playType = 7207;
			break;
		case 5:// 混合投注
			playType = 7206;
			break;
		case 6:// 半全场
			playType = 7204;
			break;

		}
		intent.putExtra("playtype", playType);
		Bet_JCZQ_Activity.this.startActivity(intent);
		Bet_JCZQ_Activity.this.finish();
	}

	/** 改变文本的值显示出来 */
	public void changeTextShow() {
		tv_tatol_count.setText(sumCount + "");// 总注数
		String str = et_bei.getText() + "";
		AppTools.bei = Integer.parseInt(str);
		totalMoney = sumCount * 2 * AppTools.bei;
		tv_tatol_money.setText(totalMoney + "");// 总钱数
	}

	/**
	 * 返回
	 */
	public void backToPre() {
		if (!Select_jczqActivity.isEmptJCZQ()) {
			dialog.show();
			dialog.setDialogContent("您退出后号码将会清空！");
			dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {

				@Override
				public void getResult(int resultCode) {
					// TODO Auto-generated method stub
					if (1 == resultCode) {
						ExpandAdapterJCZQPassMore.clearAllDate();
						ExpandAdapterJCZQPassSingle.clearAllDate();
						AppTools.totalCount = 0;
						Select_jczqActivity.clearAllData();
						for (int i = 0; i < App.activityS1.size(); i++) {
							App.activityS1.get(i).finish();
						}
					}
				}
			});
		} else {
			ExpandAdapterJCZQPassMore.clearAllDate();
			ExpandAdapterJCZQPassSingle.clearAllDate();
			AppTools.totalCount = 0;
			for (int i = 0; i < App.activityS1.size(); i++) {
				App.activityS1.get(i).finish();
			}
		}
	}

	/** 重写返回键事件 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			backToPre();
		}
		return super.onKeyDown(keyCode, event);
	}

	/*** 异步任务 用来后台获取数据 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> {
		/** 在后台执行的程序 */
		@Override
		protected String doInBackground(Void... params) {
			String error = "-1";
			if (AppTools.list_numbers.size() > 0) {
				time = RspBodyBaseBean.getTime();
				imei = RspBodyBaseBean.getIMEI(getApplicationContext());

				if (AppTools.user != null) {
					String key = MD5.md5(AppTools.user.getUserPass()
							+ AppTools.MD5_key);
					info = RspBodyBaseBean.changeBet_info2(
							AppTools.lottery.getLotteryID(),
							AppTools.lottery.getIsuseId(), AppTools.bei, 1, 1,
							0, 0, "", "", 0, totalMoney / AppTools.qi,
							sumCount, AppTools.qi > 1 ? AppTools.qi : 0,
							AppTools.qi == 1 ? 0 : totalMoney,
							AppTools.list_numbers, isStopChase);

					Log.i("x", "Bet_JCZQ_Activity --info" + info);

					crc = RspBodyBaseBean.getCrc(time, imei, key, info,
							AppTools.user.getUid());

					auth = RspBodyBaseBean.getAuth(crc, time, imei,
							AppTools.user.getUid());
					String values[] = { opt, auth, info };
					String result = HttpUtils.doPost(AppTools.names, values,
							AppTools.path);

					if ("-500".equals(result))
						return result;

					Log.i("x", "result---" + result);

					try {
						JSONObject object = new JSONObject(result);
						error = object.optString("error");
						if ("0".equals(error)) {
							AppTools.user.setBalance(object
									.optDouble("balance"));
							AppTools.user.setFreeze(object.optDouble("freeze"));
							AppTools.schemeId = object.optInt("schemeids");
							AppTools.lottery.setChaseTaskID(object
									.optInt("chasetaskids"));
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						error = "-1";
					}

				} else {
					return "-100";
				}
			} else {
				return "-102";
			}
			return error;
		}

		@Override
		protected void onPostExecute(String result) {
			myHandler.sendEmptyMessage(Integer.parseInt(result));
			super.onPostExecute(result);
		}
	}

	/** 处理页面显示的 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			setEnabled(true);
			switch (msg.what) {
			case AppTools.ERROR_SUCCESS:
				AppTools.list_numbers.clear();
				AppTools.totalCount = 0;
				// 清空所有数据
				ExpandAdapterJCZQPassMore.clearAllDate();
				ExpandAdapterJCZQPassSingle.clearAllDate();
				selectPasstype.clear();//清除过关方式
				viewPagerCurrentIndex = 0;// 过关类型 0.多串1 1.多串多
				Intent intent = new Intent(getApplicationContext(),
						PaySuccessActivity.class);
				intent.putExtra("paymoney", totalMoney);
				Bet_JCZQ_Activity.this.startActivity(intent);
				break;
			/** 余额不足 **/
			case AppTools.ERROR_MONEY:
				Toast.makeText(Bet_JCZQ_Activity.this, "余额不足",
						Toast.LENGTH_SHORT).show();
				intent = new Intent(Bet_JCZQ_Activity.this,
						RechargeActivity.class);
				intent.putExtra("money", totalMoney);
				Bet_JCZQ_Activity.this.startActivity(intent);
				break;
			/** 尚未登陆 **/
			case AppTools.ERROR_UNLONGIN:
				MyToast.getToast(Bet_JCZQ_Activity.this, "请先登陆").show();
				intent = new Intent(Bet_JCZQ_Activity.this, LoginActivity.class);
				intent.putExtra("loginType", "bet");
				Bet_JCZQ_Activity.this.startActivity(intent);
				break;
			/** 点击付款时 所选注数为 0 **/
			case AppTools.ERROR_TOTAL:
				MyToast.getToast(Bet_JCZQ_Activity.this, "请至少选择一注").show();
				intent = new Intent(Bet_JCZQ_Activity.this,
						SelectNumberActivity.class);
				intent.putExtra("loginType", "bet");
				Bet_JCZQ_Activity.this.startActivity(intent);
				break;
			case -500:
				MyToast.getToast(Bet_JCZQ_Activity.this, "连接超时").show();
				break;
			default:
				Toast.makeText(Bet_JCZQ_Activity.this, "网络异常，购买失败。请重新点击付款购买。",
						Toast.LENGTH_SHORT).show();
				break;
			}

			if (myAsynTask != null
					&& myAsynTask.getStatus() == AsyncTask.Status.RUNNING) {
				myAsynTask.cancel(true); // 如果Task还在运行，则先取消它
			}
			Log.i("x", "执行了");
			super.handleMessage(msg);

		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		init();
		setListener();
		et_bei.setText("1");
		if (1 == passtype) {// 单关
			initPassType();// 初始化过关方式
			setSelectNumAndGetCount();// 设置投注格式和注数
		}
		changeTextShow();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		super.onNewIntent(intent);
	}
	
	/** 计算总注数-混合投注 **/
	public void setTotalCountHHTZ(String type, List<String> list) {
		if (type.contains("AA"))
			this.sumCount += NumberTools.getAll2G1Mixed_hunhe(list);
		if (type.contains("AB"))
			this.sumCount += NumberTools.getAll3G1Mixed_hunhe(list);
		if (type.contains("AC"))
			this.sumCount += NumberTools.getAll3G3Mixed_hunhe(list);
		if (type.contains("AD"))
			this.sumCount += NumberTools.getAll3G4Mixed_hunhe(list);
		if (type.contains("AE"))
			this.sumCount += NumberTools.getAll4G1Mixed_hunhe(list);
		if (type.contains("AF"))
			this.sumCount += NumberTools.getAll4G4Mixed_hunhe(list);
		if (type.contains("AG"))
			this.sumCount += NumberTools.getAll4G5Mixed_hunhe(list);
		if (type.contains("AH"))
			this.sumCount += NumberTools.getAll4G6Mixed_hunhe(list);
		if (type.contains("AI"))
			this.sumCount += NumberTools.getAll4G11Mixed_hunhe(list);
		if (type.contains("AJ"))
			this.sumCount += NumberTools.getAll5G1Mixed_hunhe(list);
		if (type.contains("AK"))
			this.sumCount += NumberTools.getAll5G5Mixed_hunhe(list);
		if (type.contains("AL"))
			this.sumCount += NumberTools.getAll5G6Mixed_hunhe(list);
		if (type.contains("AM"))
			this.sumCount += NumberTools.getAll5G10Mixed_hunhe(list);
		if (type.contains("AN"))
			this.sumCount += NumberTools.getAll5G16Mixed_hunhe(list);
		if (type.contains("AO"))
			this.sumCount += NumberTools.getAll5G20Mixed_hunhe(list);
		if (type.contains("AP"))
			this.sumCount += NumberTools.getAll5G26Mixed_hunhe(list);
		if (type.contains("AQ"))
			this.sumCount += NumberTools.getAll6G1Mixed_hunhe(list);
		if (type.contains("AR"))
			this.sumCount += NumberTools.getAll6G6Mixed_hunhe(list);
		if (type.contains("AS"))
			this.sumCount += NumberTools.getAll6G7Mixed_hunhe(list);
		if (type.contains("AT"))
			this.sumCount += NumberTools.getAll6G15Mixed_hunhe(list);
		if (type.contains("AU"))
			this.sumCount += NumberTools.getAll6G20Mixed_hunhe(list);
		if (type.contains("AV"))
			this.sumCount += NumberTools.getAll6G22Mixed_hunhe(list);
		if (type.contains("AW"))
			this.sumCount += NumberTools.getAll6G35Mixed_hunhe(list);
		if (type.contains("AX"))
			this.sumCount += NumberTools.getAll6G42Mixed_hunhe(list);
		if (type.contains("AY"))
			this.sumCount += NumberTools.getAll6G50Mixed_hunhe(list);
		if (type.contains("AZ"))
			this.sumCount += NumberTools.getAll6G57Mixed_hunhe(list);
		if (type.contains("BA"))
			this.sumCount += NumberTools.getAll7G1Mixed_hunhe(list);
		if (type.contains("BB"))
			this.sumCount += NumberTools.getAll7G7Mixed_hunhe(list);
		if (type.contains("BC"))
			this.sumCount += NumberTools.getAll7G8Mixed_hunhe(list);
		if (type.contains("BD"))
			this.sumCount += NumberTools.getAll7G21Mixed_hunhe(list);
		if (type.contains("BE"))
			this.sumCount += NumberTools.getAll7G35Mixed_hunhe(list);
		if (type.contains("BF"))
			this.sumCount += NumberTools.getAll7G120Mixed_hunhe(list);
		if (type.contains("BG"))
			this.sumCount += NumberTools.getAll8G1Mixed_hunhe(list);
		if (type.contains("BH"))
			this.sumCount += NumberTools.getAll8G8Mixed_hunhe(list);
		if (type.contains("BI"))
			this.sumCount += NumberTools.getAll8G9Mixed_hunhe(list);
		if (type.contains("BJ"))
			this.sumCount += NumberTools.getAll8G28Mixed_hunhe(list);
		if (type.contains("BK"))
			this.sumCount += NumberTools.getAll8G56Mixed_hunhe(list);
		if (type.contains("BL"))
			this.sumCount += NumberTools.getAll8G70Mixed_hunhe(list);
		if (type.contains("BM"))
			this.sumCount += NumberTools.getAll8G247Mixed_hunhe(list);
	}
	

	/** 给总注数赋值 有胆的情况下 **/
	public void setTotalCount(String type, List<String> list_dan,
			List<String> list) {
		if (type.contains("AA"))
			this.sumCount += NumberTools.getAllnG1Mixed_dan(list_dan, list, 2);
		if (type.contains("AB"))
			this.sumCount += NumberTools.getAllnG1Mixed_dan(list_dan, list, 3);
		if (type.contains("AC"))
			this.sumCount += NumberTools.getAll3G3Mixed_dan(list_dan, list);
		if (type.contains("AD"))
			this.sumCount += NumberTools.getAll3G4Mixed_dan(list_dan, list);
		if (type.contains("AE"))
			this.sumCount += NumberTools.getAllnG1Mixed_dan(list_dan, list, 4);
		if (type.contains("AF"))
			this.sumCount += NumberTools.getAll4G4Mixed_dan(list_dan, list);
		if (type.contains("AG"))
			this.sumCount += NumberTools.getAll4G5Mixed_dan(list_dan, list);
		if (type.contains("AH"))
			this.sumCount += NumberTools.getAll4G6_11Mixed_dan(list_dan, list,
					6);
		if (type.contains("AI"))
			this.sumCount += NumberTools.getAll4G6_11Mixed_dan(list_dan, list,
					11);
		if (type.contains("AJ"))
			this.sumCount += NumberTools.getAllnG1Mixed_dan(list_dan, list, 5);
		if (type.contains("AK"))
			this.sumCount += NumberTools.getAll5G5Mixed_dan(list_dan, list);
		if (type.contains("AL"))
			this.sumCount += NumberTools.getAll5G6Mixed_dan(list_dan, list);
		if (type.contains("AM"))
			this.sumCount += NumberTools.getAll5G10_16_20Mixed_dan(list_dan,
					list, 10);
		if (type.contains("AN"))
			this.sumCount += NumberTools.getAll5G10_16_20Mixed_dan(list_dan,
					list, 16);
		if (type.contains("AO"))
			this.sumCount += NumberTools.getAll5G10_16_20Mixed_dan(list_dan,
					list, 20);
		if (type.contains("AP"))
			this.sumCount += NumberTools.getAll5G26Mixed_dan(list_dan, list);
		if (type.contains("AQ"))
			this.sumCount += NumberTools.getAllnG1Mixed_dan(list_dan, list, 6);
		if (type.contains("AR"))
			this.sumCount += NumberTools.getAll6G6Mixed_dan(list_dan, list);
		if (type.contains("AS"))
			this.sumCount += NumberTools.getAll6G7Mixed_dan(list_dan, list);
		if (type.contains("AT"))
			this.sumCount += NumberTools.getAll6G15_20_22_50Mixed_dan(list_dan,
					list, 15);
		if (type.contains("AU"))
			this.sumCount += NumberTools.getAll6G15_20_22_50Mixed_dan(list_dan,
					list, 20);
		if (type.contains("AV"))
			this.sumCount += NumberTools.getAll6G15_20_22_50Mixed_dan(list_dan,
					list, 22);
		if (type.contains("AW"))
			this.sumCount += NumberTools.getAll6G35Mixed_dan(list_dan, list);
		if (type.contains("AX"))
			this.sumCount += NumberTools.getAll6G42Mixed_dan(list_dan, list);
		if (type.contains("AY"))
			this.sumCount += NumberTools.getAll6G15_20_22_50Mixed_dan(list_dan,
					list, 50);
		if (type.contains("AZ"))
			this.sumCount += NumberTools.getAll6G57Mixed_dan(list_dan, list);
		if (type.contains("BA"))
			this.sumCount += NumberTools.getAllnG1Mixed_dan(list_dan, list, 7);
		if (type.contains("BB"))
			this.sumCount += NumberTools.getAll7G7_21_35Mixed_dan(list_dan,
					list, 7);
		if (type.contains("BC"))
			this.sumCount += NumberTools.getAll7G8Mixed_dan(list_dan, list);
		if (type.contains("BD"))
			this.sumCount += NumberTools.getAll7G7_21_35Mixed_dan(list_dan,
					list, 21);
		if (type.contains("BE"))
			this.sumCount += NumberTools.getAll7G7_21_35Mixed_dan(list_dan,
					list, 35);
		if (type.contains("BF"))
			this.sumCount += NumberTools.getAll7G7_21_35Mixed_dan(list_dan,
					list, 120);
		if (type.contains("BG"))
			this.sumCount += NumberTools.getAllnG1Mixed_dan(list_dan, list, 8);
		if (type.contains("BH"))
			this.sumCount += NumberTools.getAll8G8_28_56_70_247Mixed_dan(
					list_dan, list, 8);
		if (type.contains("BI"))
			this.sumCount += NumberTools.getAll8G9Mixed_dan(list_dan, list);
		if (type.contains("BJ"))
			this.sumCount += NumberTools.getAll8G8_28_56_70_247Mixed_dan(
					list_dan, list, 28);
		if (type.contains("BK"))
			this.sumCount += NumberTools.getAll8G8_28_56_70_247Mixed_dan(
					list_dan, list, 56);
		if (type.contains("BL"))
			this.sumCount += NumberTools.getAll8G8_28_56_70_247Mixed_dan(
					list_dan, list, 70);
		if (type.contains("BM"))
			this.sumCount += NumberTools.getAll8G8_28_56_70_247Mixed_dan(
					list_dan, list, 247);
	}

	/** 给总注数赋值 没胆的情况下 **/
	public void setTotalCount(String type, List<String> list) {
		if (type.contains("A0"))
			this.sumCount += NumberTools.getCountBySinglePass(list);
		if (type.contains("AA"))
			this.sumCount += NumberTools.getAll2G1Mixed(list);
		if (type.contains("AB"))
			this.sumCount += NumberTools.getAll3G1Mixed(list);
		if (type.contains("AC"))
			this.sumCount += NumberTools.getAll3G3Mixed(list);
		if (type.contains("AD"))
			this.sumCount += NumberTools.getAll3G4Mixed(list);
		if (type.contains("AE"))
			this.sumCount += NumberTools.getAll4G1Mixed(list);
		if (type.contains("AF"))
			this.sumCount += NumberTools.getAll4G4Mixed(list);
		if (type.contains("AG"))
			this.sumCount += NumberTools.getAll4G5Mixed(list);
		if (type.contains("AH"))
			this.sumCount += NumberTools.getAll4G6Mixed(list);
		if (type.contains("AI"))
			this.sumCount += NumberTools.getAll4G11Mixed(list);
		if (type.contains("AJ"))
			this.sumCount += NumberTools.getAll5G1Mixed(list);
		if (type.contains("AK"))
			this.sumCount += NumberTools.getAll5G5Mixed(list);
		if (type.contains("AL"))
			this.sumCount += NumberTools.getAll5G6Mixed(list);
		if (type.contains("AM"))
			this.sumCount += NumberTools.getAll5G10Mixed(list);
		if (type.contains("AN"))
			this.sumCount += NumberTools.getAll5G16Mixed(list);
		if (type.contains("AO"))
			this.sumCount += NumberTools.getAll5G20Mixed(list);
		if (type.contains("AP"))
			this.sumCount += NumberTools.getAll5G26Mixed(list);
		if (type.contains("AQ"))
			this.sumCount += NumberTools.getAll6G1Mixed(list);
		if (type.contains("AR"))
			this.sumCount += NumberTools.getAll6G6Mixed(list);
		if (type.contains("AS"))
			this.sumCount += NumberTools.getAll6G7Mixed(list);
		if (type.contains("AT"))
			this.sumCount += NumberTools.getAll6G15Mixed(list);
		if (type.contains("AU"))
			this.sumCount += NumberTools.getAll6G20Mixed(list);
		if (type.contains("AV"))
			this.sumCount += NumberTools.getAll6G22Mixed(list);
		if (type.contains("AW"))
			this.sumCount += NumberTools.getAll6G35Mixed(list);
		if (type.contains("AX"))
			this.sumCount += NumberTools.getAll6G42Mixed(list);
		if (type.contains("AY"))
			this.sumCount += NumberTools.getAll6G50Mixed(list);
		if (type.contains("AZ"))
			this.sumCount += NumberTools.getAll6G57Mixed(list);
		if (type.contains("BA"))
			this.sumCount += NumberTools.getAll7G1Mixed(list);
		if (type.contains("BB"))
			this.sumCount += NumberTools.getAll7G7Mixed(list);
		if (type.contains("BC"))
			this.sumCount += NumberTools.getAll7G8Mixed(list);
		if (type.contains("BD"))
			this.sumCount += NumberTools.getAll7G21Mixed(list);
		if (type.contains("BE"))
			this.sumCount += NumberTools.getAll7G35Mixed(list);
		if (type.contains("BF"))
			this.sumCount += NumberTools.getAll7G120Mixed(list);
		if (type.contains("BG"))
			this.sumCount += NumberTools.getAll8G1Mixed(list);
		if (type.contains("BH"))
			this.sumCount += NumberTools.getAll8G8Mixed(list);
		if (type.contains("BI"))
			this.sumCount += NumberTools.getAll8G9Mixed(list);
		if (type.contains("BJ"))
			this.sumCount += NumberTools.getAll8G28Mixed(list);
		if (type.contains("BK"))
			this.sumCount += NumberTools.getAll8G56Mixed(list);
		if (type.contains("BL"))
			this.sumCount += NumberTools.getAll8G70Mixed(list);
		if (type.contains("BM"))
			this.sumCount += NumberTools.getAll8G247Mixed(list);
	}

}
