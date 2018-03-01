package com.sm.sls_app.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.SelectedNumbers;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.ui.adapter.MyBetLotterySSCAdapter;
import com.sm.sls_app.ui.adapter.MyGridViewAdapter;
import com.sm.sls_app.ui.adapter.ZhuiqiAdapter;
import com.sm.sls_app.ui.adapter.ZhuiqiAdapter.Refresh;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.NumberTools;
import com.sm.sls_app.view.ConfirmDialog;
import com.sm.sls_app.view.MyListView2;
import com.sm.sls_app.view.MyToast;

/** 功能：投注页面 版本 */
public class Bet_SSCActivity extends Activity implements OnClickListener,
		Refresh {

	private final static String TAG = "BetActivity";
	/* 头部 */
	private RelativeLayout layout_top_select;// 顶部布局
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
	private LinearLayout btn_continue_select;// 继续投注
	private LinearLayout btn_automatic_select;// 机选1注
	private ImageView bet_btn_deleteall;// 清空
	private TextView bet_tv_guize; // 委托投注规则

	private MyListView2 bet_lv_scheme, bet_lv_zhuiqi;// 自定义listview
	public MyBetLotterySSCAdapter adapter;

	private List<String> list = new ArrayList<String>();

	private Intent intent;
	private int type = 1, playType;
	private long sumCount, totalMoney; // 方案总注数 // 方案总金额

	private EditText et_bei, et_qi; // 用户输入的倍数 // 用户输入的期数

	private MyHandler myHandler;
	private MyAsynTask myAsynTask;

	private CheckBox bet_cb_stopfollow;
	private int isStopChase = 1;

	private ScrollView bet_sv, bet_bei;
	private ZhuiqiAdapter zAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bet);
		App.activityS.add(this);
		App.activityS1.add(this);
		if (AppTools.list_numbers == null)
			AppTools.list_numbers = new ArrayList<SelectedNumbers>();
		if (AppTools.beiList == null) {
			AppTools.beiList = new ArrayList<Integer>(AppTools.qi);
		}
		findView();
		bet_lv_scheme.scrollTo(0, 0);
		setListener();
		changeTextShow();
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
		btn_automatic_select = (LinearLayout) this
				.findViewById(R.id.btn_automatic_select);
		bet_btn_deleteall = (ImageView) this
				.findViewById(R.id.bet_btn_deleteall);
		btn_follow = (Button) this.findViewById(R.id.btn_follow);
		et_bei = (EditText) this.findViewById(R.id.et_bei);
		et_qi = (EditText) this.findViewById(R.id.et_qi);

		tv_tatol_count = (TextView) this.findViewById(R.id.tv_tatol_count);
		tv_tatol_money = (TextView) this.findViewById(R.id.tv_tatol_money);
		bet_cb_stopfollow = (CheckBox) this
				.findViewById(R.id.bet_cb_stopfollow);
		bet_tv_guize = (TextView) this.findViewById(R.id.bet_tv_guize);

		bet_lv_scheme = (MyListView2) this.findViewById(R.id.bet_lv_scheme);
		bet_lv_zhuiqi = (MyListView2) this.findViewById(R.id.zhuiqi);
		bet_sv = (ScrollView) this.findViewById(R.id.bet_sv);
		bet_bei = (ScrollView) this.findViewById(R.id.bet_bei);

		btn_playtype.setText("重庆时时彩投注单");

		adapter = new MyBetLotterySSCAdapter(Bet_SSCActivity.this,
				AppTools.list_numbers);
		zAdapter = new ZhuiqiAdapter(Bet_SSCActivity.this,
				AppTools.lottery.getIsuseNameList());
		bet_lv_zhuiqi.setAdapter(zAdapter);
		zAdapter.setListener(this);
		// 隐藏与显示
		btn_help.setVisibility(View.GONE);
		btn_clearall.setVisibility(View.GONE);
		iv_up_down.setVisibility(View.GONE);
		btn_follow.setVisibility(View.VISIBLE);

	}

	/** 初始化属性 */
	private void init() {
		Bundle bundle = getIntent().getBundleExtra("bundle1");
		/* 专用幸运选号里面跳转过来的 */
		if (AppTools.list_numbers != null && AppTools.list_numbers.size() != 0) {
			playType = AppTools.list_numbers.get(0).getPlayType();
			type = AppTools.list_numbers.get(0).getType();
		}
		bundle = getIntent().getBundleExtra("bundle");
		if (null != bundle) {
			String one = (bundle.getString("one")).replace(", ", "").trim();
			String two = (bundle.getString("two")).replace(", ", "").trim();
			String three = (bundle.getString("three")).replace(", ", "").trim();
			String four = (bundle.getString("four")).replace(", ", "").trim();
			String five = (bundle.getString("five")).replace(", ", "").trim();
			String dan = bundle.getString("dan");
			String tuo = bundle.getString("tuo");
			String dxds = bundle.getString("dxds");

			type = bundle.getInt("type");
			playType = bundle.getInt("playType");
			Log.i("x", "投注界面得到投注类型====" + type);

			SelectedNumbers numbers = new SelectedNumbers();
			System.out.println("++++" + playType);
			if (playType == 2803 || type == 8) {
				if (one.length() > 1)
					one = "(" + one + ")";
				if (two.length() > 1)
					two = "(" + two + ")";
				if (three.length() > 1)
					three = "(" + three + ")";
				if (four.length() > 1)
					four = "(" + four + ")";
				if (five.length() > 1)
					five = "(" + five + ")";

			}
			/** 保存红球 **/
			List<String> redList = new ArrayList<String>();
			for (String st : MyGridViewAdapter.redSet) {
				Log.i("x", "保存红球===" + st);
				redList.add(st);
			}
			if (type == 9) {
				numbers.setShowLotteryNumber(dxds.replace(",", ""));
				numbers.setLotteryNumber(dxds.replace(",", ""));
				Log.i("x", "传过来的大小单双== " + dxds);
				// List list = new ArrayList();
				// list = Arrays.asList(dxds.split(","));
				// numbers.setRedNumbers(list);
				// String num = RspBodyBaseBean.getLotteryNum2(list);
				// numbers.setLotteryNumber(num);
			} else if (type == 5 || type == 6 || type == 3) {
				numbers.setLotteryNumber(one + two + three + four + five);
				numbers.setShowLotteryNumber(one + two + three + four + five);
				// 胆拖号码
			} else if (playType == 2816 || playType == 2817) {
				numbers.setShowLotteryNumber("(" + dan.replace(",", "") + ")"
						+ tuo.replace(",", ""));
				numbers.setLotteryNumber(dan.replace(",", "") + " , "
						+ tuo.replace(",", ""));
			} else {
				if (two.length() == 0) {
					numbers.setLotteryNumber("----" + one);
				} else if (three.length() == 0) {
					numbers.setLotteryNumber("---" + one + two);
				} else if (five.length() == 0) {
					numbers.setLotteryNumber("--" + one + two + three);
				} else {
					numbers.setLotteryNumber(one + two + three + four + five);
				}
				numbers.setShowLotteryNumber(one + two + three + four + five);
			}

			numbers.setNumber((one + "-" + two + "-" + three + "-" + four + "-"
					+ five + dan.replace(",", "") + "-" + tuo.replace(",", ""))
					.replace("(", "").replace(")", ""));
			numbers.setType(type);
			numbers.setPlayType(playType);
			numbers.setCount(AppTools.totalCount);
			numbers.setMoney(AppTools.totalCount * 2);

			AppTools.list_numbers.add(0, numbers);

			adapter.notifyDataSetChanged();

			changeTextShow();
			getIntent().replaceExtras(bundle);
		}
		btn_automatic_select.setVisibility(View.GONE);
		if (playType == 2803)
			btn_automatic_select.setVisibility(View.VISIBLE);
		isStopChase = 1;
		btn_submit.setText("付款");
		dialog = new ConfirmDialog(this, R.style.dialog);
	}

	/** 设置监听 */
	private void setListener() {
		btn_continue_select.setOnClickListener(this);
		btn_automatic_select.setOnClickListener(this);
		bet_btn_deleteall.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		btn_follow.setOnClickListener(this);
		btn_back.setOnClickListener(this);

		bet_lv_scheme.setAdapter(adapter);
		bet_lv_scheme.setOnItemClickListener(new listItemClick());

		et_bei.addTextChangedListener(bei_textWatcher);
		et_qi.addTextChangedListener(qi_textWatcher);
		bet_tv_guize.setOnClickListener(this);

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

	/** listView 的 item 点击监听 */
	class listItemClick implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {

			SelectedNumbers num = AppTools.list_numbers.get(position);

			playType = num.getPlayType(); // 幸运彩中需要添加的
			// 跳转页面并传值
			intent = new Intent(Bet_SSCActivity.this, Select_SSCActivity.class);
			Bundle bundle = new Bundle();
			AppTools.totalCount = num.getCount();
			bundle.putInt("playtype", num.getPlayType());
			bundle.putInt("type", num.getType());
			// 胆拖的话
			if (num.getPlayType() == 2816 || num.getPlayType() == 2817) {
				if (num.getPlayType() == 2816) {
					bundle.putString("Bet_sxzsdt", num.getLotteryNumber());
					Log.i("三星组三胆拖", "--" + num.getShowLotteryNumber() + "---");
				} else if (num.getPlayType() == 2817) {
					bundle.putString("Bet_sxzldt", num.getLotteryNumber());
					Log.i("三星组六胆拖", "--" + num.getShowLotteryNumber() + "---");
				}
			} else if (num.getType() == 9) {
				bundle.putString("Bet_dxds", num.getShowLotteryNumber());
				Log.i("大小单双", "--" + num.getShowLotteryNumber() + "---");
			} else {
				String[] str = num.getNumber().split("-");

				ArrayList<String> listOne = new ArrayList<String>();

				for (int i = 0; i < str[0].length(); i++) {
					listOne.add(str[0].substring(i, i + 1));
				}

				System.out.println("+++***" + listOne.toString());

				bundle.putStringArrayList("oneSet", listOne);

				if (str.length > 1) {

					ArrayList<String> listTwo = new ArrayList<String>();
					for (int i = 0; i < str[1].length(); i++) {
						listTwo.add(str[1].substring(i, i + 1));
					}
					bundle.putStringArrayList("twoSet", listTwo);
				}

				if (str.length > 2) {
					ArrayList<String> listThree = new ArrayList<String>();

					for (int i = 0; i < str[2].length(); i++) {
						listThree.add(str[2].substring(i, i + 1));
					}
					bundle.putStringArrayList("threeSet", listThree);
				}
				if (str.length > 3) {
					ArrayList<String> listFour = new ArrayList<String>();
					for (int i = 0; i < str[3].length(); i++) {
						listFour.add(str[3].substring(i, i + 1));
					}
					bundle.putStringArrayList("fourSet", listFour);
				}
				if (str.length > 4) {
					ArrayList<String> listFive = new ArrayList<String>();
					for (int i = 0; i < str[4].length(); i++) {
						listFive.add(str[4].substring(i, i + 1));
					}
					bundle.putStringArrayList("fiveSet", listFive);
				}
			}
			// 将Bundle 放入Intent
			intent.putExtra("SSCBundle", bundle);

			AppTools.list_numbers.remove(position);

			Bet_SSCActivity.this.startActivity(intent);
		}
	}

	/** 当文本的值改变时 */
	private TextWatcher qi_textWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void afterTextChanged(Editable edt) {
			if (edt.toString().trim().length() != 0) {
				if (Integer.parseInt(edt.toString().trim()) > AppTools.lottery
						.getDtCanChaseIsuses().size()) {
					et_qi.setText(AppTools.lottery.getDtCanChaseIsuses().size()
							+ "");
					et_qi.setSelection(et_qi.getText().length());
					MyToast.getToast(
							getApplicationContext(),
							"最多可追"
									+ AppTools.lottery.getDtCanChaseIsuses()
											.size() + "期").show();
				}
				if (Integer.parseInt(edt.toString().trim()) == 0) {

					et_qi.setText("1");
					et_qi.setSelection(et_qi.getText().length());
					MyToast.getToast(getApplicationContext(), "至少可追1期").show();
				}
				if (edt.toString().substring(0, 1).equals("0")) {
					et_qi.setText(edt.toString().subSequence(1,
							edt.toString().length()));
					et_qi.setSelection(0);
				}
			}
			changeTextShow();
			setCursorPosition(et_qi);
		}
	};

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
				if (edt.toString().substring(0, 1).equals("0")) {
					et_bei.setText(edt.toString().subSequence(1,
							edt.toString().length()));
					et_bei.setSelection(0);
				}
			}
			changeTextShow();
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
		/** 机选 **/
		case R.id.btn_automatic_select:
			btn_randomClick(1);
			break;
		/** 清空 **/
		case R.id.bet_btn_deleteall:
			if (null != AppTools.list_numbers
					&& 0 != AppTools.list_numbers.size()) {
				dialog.show();
				dialog.setDialogContent("是否清空投注单号码");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {

					@Override
					public void getResult(int resultCode) {
						// TODO Auto-generated method stub
						if (1 == resultCode) {// 确定
							AppTools.list_numbers.clear();
							AppTools.beiList.clear();
							adapter.notifyDataSetChanged();
							changeTextShow();
						}
					}
				});
			} else {
				MyToast.getToast(this, "请先选择投注内容！").show();
			}
			break;
		/** 付款 **/
		case R.id.btn_submit:
			setEnabled(false);
			myAsynTask = new MyAsynTask();
			myAsynTask.execute();
			break;
		/** 发起合买 **/
		case R.id.btn_follow:
			join();
			break;
		case R.id.bet_tv_guize:
			Intent intent = new Intent(Bet_SSCActivity.this,
					PlayDescription.class);
			intent.putExtra("type", 0);
			Bet_SSCActivity.this.startActivity(intent);
			break;
		}
	}

	/** 设置是否可用 **/
	private void setEnabled(boolean isEna) {
		btn_submit.setEnabled(isEna);
		bet_btn_deleteall.setEnabled(isEna);
		btn_continue_select.setEnabled(isEna);
		btn_automatic_select.setEnabled(isEna);
		btn_follow.setEnabled(isEna);
		bet_cb_stopfollow.setEnabled(isEna);
		et_bei.setEnabled(isEna);
		et_qi.setEnabled(isEna);
	}

	/** 跳到合买 */
	private void join() {
		// int total = 0; // 总金额
		// for (SelectedNumbers num : AppTools.list_numbers) {
		// total += num.getMoney();
		// }
		// if (total == 0) {
		// MyToast.getToast(getApplicationContext(), "您还没有选择号码").show();
		// return;
		// }
		// total = total * AppTools.bei;
		if (totalMoney == 0) {
			MyToast.getToast(getApplicationContext(), "您还没有选择号码").show();
			return;
		}
		intent = new Intent(Bet_SSCActivity.this, JoinActivity.class);
		intent.putExtra("qiHao", AppTools.qi + "");
		intent.putExtra("totalMoney", totalMoney);
		intent.putExtra("isStopChase", isStopChase);
		Bet_SSCActivity.this.startActivity(intent);
	}

	/** 手选按钮点击事件 */
	private void btn_handClick() {
		AppTools.totalCount = 0;
		intent = new Intent(Bet_SSCActivity.this, Select_SSCActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("type", type);
		bundle.putInt("playtype", playType);
		intent.putExtra("SSCBundle", bundle);
		Bet_SSCActivity.this.startActivity(intent);
	}

	/** 机选按钮点击事件 */
	private void btn_randomClick(int count) {
		System.out.println("---" + playType);
		String number = null;
		String Lotterynumber = null;
		String ShowLotterynumber = null;
		String number1 = null;
		if (playType == 2803) {
			switch (type) {
			case 1:
				list = NumberTools.getRandomNum(1, 9);
				Collections.sort(list);
				number = list.toString();
				Lotterynumber = number.replace("[", "").replace("]", "")
						.replace(",", "").replace(" ", "")
						+ "----";

				ShowLotterynumber = number.replace("[", "").replace("]", "")
						.replace(",", "").replace(" ", "");

				number1 = Lotterynumber;

				break;
			case 2:
				list = NumberTools.getRandomNum(2, 9);
				Collections.sort(list);
				number = list.toString().trim();
				Lotterynumber = "---"
						+ number.replace("[", "").replace("]", "")
								.replace(",", "").replace(" ", "");
				ShowLotterynumber = number.replace("[", "").replace("]", "")
						.replace(",", "").replace(" ", "");
				number1 = number.replace("[", "").replace("]", "")
						.replace(" ", "").split(",")[0]
						+ "-"
						+ number.replace("[", "").replace("]", "")
								.replace(" ", "").split(",")[1];
				break;
			case 4:
				list = NumberTools.getRandomNum(3, 9);
				Collections.sort(list);
				number = list.toString().trim();
				Lotterynumber = "--"
						+ number.replace("[", "").replace("]", "")
								.replace(",", "").replace(" ", "");
				ShowLotterynumber = number.replace("[", "").replace("]", "")
						.replace(",", "").replace(" ", "");
				number1 = number.replace("[", "").replace("]", "")
						.replace(" ", "").split(",")[0]
						+ "-"
						+ number.replace("[", "").replace("]", "")
								.replace(" ", "").split(",")[1]
						+ "-"
						+ number.replace("[", "").replace("]", "")
								.replace(" ", "").split(",")[2];
				break;
			case 7:
				list = NumberTools.getRandomNum(5, 9);
				Collections.sort(list);
				number = list.toString().trim();
				Lotterynumber = number.replace("[", "").replace("]", "")
						.replace(",", "").replace(" ", "");
				ShowLotterynumber = number.replace("[", "").replace("]", "")
						.replace(",", "").replace(" ", "");
				number1 = number.replace("[", "").replace("]", "")
						.replace(" ", "").split(",")[0]
						+ "-"
						+ number.replace("[", "").replace("]", "")
								.replace(" ", "").split(",")[1]
						+ "-"
						+ number.replace("[", "").replace("]", "")
								.replace(" ", "").split(",")[2]
						+ "-"
						+ number.replace("[", "").replace("]", "")
								.replace(" ", "").split(",")[3]
						+ "-"
						+ number.replace("[", "").replace("]", "")
								.replace(" ", "").split(",")[4];
				break;
			default:
				break;
			}
		}
		SelectedNumbers numbers = new SelectedNumbers();
		// 排序 设置机选参数
		numbers.setNumber(number1);
		numbers.setPlayType(playType);
		numbers.setType(type);
		numbers.setCount(1);
		numbers.setMoney(2);
		numbers.setShowLotteryNumber(ShowLotterynumber);
		numbers.setLotteryNumber(Lotterynumber);
		AppTools.list_numbers.add(0, numbers);
		// 刷新Adapter
		adapter.notifyDataSetChanged();
		changeTextShow();

	}

	/** 改变文本的值显示出来 */
	public void changeTextShow() {
		if (et_bei.getText().toString().trim().length() == 0)
			AppTools.bei = 1;
		else
			AppTools.bei = Integer.parseInt(et_bei.getText().toString().trim());

		if (et_qi.getText().toString().trim().length() == 0)
			AppTools.qi = 1;
		else
			AppTools.qi = Integer.parseInt(et_qi.getText().toString().trim());
		if (null != AppTools.beiList) {
			AppTools.beiList.clear();
		} else {
			AppTools.beiList = new ArrayList<Integer>();
		}
		for (int i = 0; i < AppTools.qi; i++) {
			AppTools.beiList.add(AppTools.bei);
		}
		sumCount = 0; // 总注数
		for (SelectedNumbers num : AppTools.list_numbers) {
			sumCount += num.getCount();
		}

		if (sumCount != 0) {
			// totalMoney = sumCount * 2 * AppTools.bei * AppTools.qi;
			tv_tatol_count.setText(sumCount + "");
			// tv_tatol_money.setText(totalMoney + "");
			changeMoney();
		} else {
			tv_tatol_count.setText("0");
			tv_tatol_money.setText("0");
		}
		bet_sv.scrollTo(0, 0);
		bet_bei.scrollTo(0, 0);
		zAdapter.setData(AppTools.qi, sumCount, AppTools.bei);
	}

	/**
	 * 改变总金额文本显示
	 */
	private void changeMoney() {
		if (sumCount != 0) {
			if (null != AppTools.beiList) {
				totalMoney = 0;
				for (int i = 0; i < AppTools.beiList.size(); i++) {
					totalMoney += sumCount * 2 * AppTools.beiList.get(i);
				}
			} else {
				totalMoney = sumCount * 2 * AppTools.bei * AppTools.qi;
			}
			tv_tatol_money.setText(totalMoney + "");
		}
	}

	/**
	 * 返回
	 */
	public void backToPre() {
		if (AppTools.list_numbers != null && AppTools.list_numbers.size() != 0) {
			dialog.show();
			dialog.setDialogContent("您退出后号码将会清空！");
			dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {

				@Override
				public void getResult(int resultCode) {
					// TODO Auto-generated method stub
					if (1 == resultCode) {// 确定
						AppTools.list_numbers.clear();
						AppTools.beiList.clear();
						AppTools.totalCount = 0;
						for (int i = 0; i < App.activityS1.size(); i++) {
							App.activityS1.get(i).finish();
						}
					}
				}
			});
		} else {
			AppTools.list_numbers.clear();
			AppTools.beiList.clear();
			AppTools.totalCount = 0;
			for (int i = 0; i < App.activityS1.size(); i++) {
				App.activityS1.get(i).finish();
			}
		}
	}

	/** 重写返回键事件 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			backToPre();
		}
		return super.onKeyDown(keyCode, event);
	}

	String msgs;

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

					// if (AppTools.list_numbers.get(0).getType() == 9)
					// {
					//
					// info = RspBodyBaseBean.changeBet_info3(
					// "28",
					// AppTools.lottery.getIsuseId(),
					// AppTools.bei, 1, 1,
					// 0, 0, "", "", 0, totalMoney / AppTools.qi,
					// sumCount, AppTools.qi > 1 ? AppTools.qi : 0,
					// AppTools.qi == 1 ? 0 : totalMoney,
					// AppTools.list_numbers, isStopChase);
					// }
					// else{
					info = RspBodyBaseBean.changeBet_info_ssc("28",
							AppTools.lottery.getIsuseId(), AppTools.bei, 1, 1,
							0, 0, "", "", 0, totalMoney / AppTools.qi,
							sumCount, AppTools.qi > 1 ? AppTools.qi : 0,
							AppTools.qi == 1 ? 0 : totalMoney,
							AppTools.list_numbers, isStopChase);
					// }

					Log.i("x", "betActivity SSC--info" + info);

					crc = RspBodyBaseBean.getCrc(time, imei, key, info,
							AppTools.user.getUid());

					auth = RspBodyBaseBean.getAuth(crc, time, imei,
							AppTools.user.getUid());
					String values[] = { opt, auth, info };
					String result = HttpUtils.doPost(AppTools.names, values,
							AppTools.path);

					if ("-500".equals(result))
						return result;

					Log.i("x", "result--11x5=====" + result);

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
						} else if ("-160".equals(error)) {
							msgs = object.optString("msg");
							return "-160";
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
				MyToast.getToast(getApplicationContext(), "购买成功").show();
				AppTools.list_numbers.clear();
				AppTools.beiList.clear();
				AppTools.totalCount = 0;
				Intent intent = new Intent(getApplicationContext(),
						PaySuccessActivity.class);
				intent.putExtra("paymoney", totalMoney);
				Bet_SSCActivity.this.startActivity(intent);
				break;
			case -160:
				for (int i = 0; i < App.activityS1.size(); i++) {
					App.activityS1.get(i).finish();
				}
				Toast.makeText(getApplicationContext(), msgs, Toast.LENGTH_LONG)
						.show();
				AppTools.list_numbers.clear();
				AppTools.beiList.clear();
				AppTools.totalCount = 0;
				break;
			/** 余额不足 **/
			case AppTools.ERROR_MONEY:
				Toast.makeText(Bet_SSCActivity.this, "余额不足", Toast.LENGTH_SHORT)
						.show();
				intent = new Intent(Bet_SSCActivity.this,
						RechargeActivity.class);
				intent.putExtra("money", totalMoney);
				Bet_SSCActivity.this.startActivity(intent);
				break;
			/** 尚未登陆 **/
			case AppTools.ERROR_UNLONGIN:
				MyToast.getToast(Bet_SSCActivity.this, "请先登陆").show();
				intent = new Intent(Bet_SSCActivity.this, LoginActivity.class);
				intent.putExtra("loginType", "bet");
				Bet_SSCActivity.this.startActivity(intent);
				break;
			/** 点击付款时 所选注数为 0 **/
			case AppTools.ERROR_TOTAL:
				MyToast.getToast(Bet_SSCActivity.this, "请至少选择一注").show();
				intent = new Intent(Bet_SSCActivity.this,
						Select_SSCActivity.class);
				intent.putExtra("loginType", "bet");
				Bet_SSCActivity.this.startActivity(intent);
				break;
			case -500:
				MyToast.getToast(Bet_SSCActivity.this, "连接超时").show();
				break;
			default:
				Toast.makeText(Bet_SSCActivity.this, "网络异常，购买失败。请重新点击付款购买。",
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

	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		super.onNewIntent(intent);
	}

	/**
	 * 追期倍投单个倍数改变的回调
	 * 
	 * @param position
	 * @param bei
	 */
	@Override
	public void refreshData() {
		changeMoney();
		System.out.println("refreshData=======" + AppTools.beiList.toString());
	}
}
