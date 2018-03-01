package com.sm.sls_app.ui;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.SelectedNumbers;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.ui.adapter.MyBetLotteryListAdapter;
import com.sm.sls_app.ui.adapter.MyGridViewAdapter;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.FileUtils;
import com.sm.sls_app.utils.NumberTools;
import com.sm.sls_app.view.ConfirmDialog;
import com.sm.sls_app.view.MyListView2;
import com.sm.sls_app.view.MyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** 功能：投注页面 版本 */
public class BetActivity extends Activity implements OnClickListener {

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

	private MyListView2 bet_lv_scheme;// 自定义listview
	public MyBetLotteryListAdapter adapter;

	private Intent intent;

	private int type;

	private List<String> list_red, list_blue;

	private long sumCount, totalMoney; // 方案总注数 // 方案总金额

	private EditText et_bei, et_qi; // 用户输入的倍数 // 用户输入的期数

	private MyHandler myHandler;
	private MyAsynTask myAsynTask;

	private CheckBox bet_cb_stopfollow;
	private int isStopChase = 1;

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
		findView();
		setListener();
		init();
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

		btn_playtype.setText("双色球投注单");

		adapter = new MyBetLotteryListAdapter(BetActivity.this,
				AppTools.list_numbers);
		// 隐藏与显示
		btn_help.setVisibility(View.GONE);
		btn_clearall.setVisibility(View.GONE);
		iv_up_down.setVisibility(View.GONE);
		btn_follow.setVisibility(View.VISIBLE);

	}

	/** 初始化属性 */
	private void init() {
		// 当集合不为空时
		if (MyGridViewAdapter.redSet.size() != 0
				&& MyGridViewAdapter.redSet != null) {
			SelectedNumbers numbers = new SelectedNumbers();

			/** 保存红球 **/
			List<String> redList = new ArrayList<String>();
			for (String st : MyGridViewAdapter.redSet) {
				Log.i("x", "保存红球===" + st);
				redList.add(st);
			}

			/** 保存蓝球 **/
			List<String> blueList = new ArrayList<String>();
			for (String st : MyGridViewAdapter.blueSet) {
				Log.i("x", "保存蓝球   ----  " + st);
				blueList.add(st);
			}

			/** 排序 **/
			Collections.sort(redList);
			numbers.setRedNumbers(redList);
			Collections.sort(blueList);
			numbers.setBlueNumbers(blueList);

			// 保存红拖
			List<String> redTuoList = new ArrayList<String>();

			if (MyGridViewAdapter.playType == 501) {
				// numbers.setType("普通投注");
				btn_automatic_select.setVisibility(View.VISIBLE); // 如果是普通，显示机选一注。
				numbers.setPlayType(501);
			}

			else if (MyGridViewAdapter.playType == 502) {
				btn_automatic_select.setVisibility(View.GONE); // 如果是胆拖，隐藏机选一注。

				for (String st : MyGridViewAdapter.redTuoSet) {
					redTuoList.add(st);
				}

				// 排序
				Collections.sort(redTuoList);
				numbers.setRedTuoNum(redTuoList);
				// numbers.setType("胆拖投注");
				numbers.setPlayType(502);
			}

			// type = MyGridViewAdapter.playType;
			String num = NumberTools.changeSSQ(redList, redTuoList, blueList,
					MyGridViewAdapter.playType);

			Log.i("x", "格式化后的投注号码-----  " + num);
			numbers.setCount(AppTools.totalCount);
			numbers.setMoney(AppTools.totalCount * 2);

			numbers.setLotteryNumber(num);

			AppTools.list_numbers.add(0, numbers);
			adapter.notifyDataSetChanged();
		}
		type = MyGridViewAdapter.playType;
		if (MyGridViewAdapter.playType == 502) {
			btn_automatic_select.setVisibility(View.GONE);
		} else {
			btn_automatic_select.setVisibility(View.VISIBLE);
		}
		changeTextShow();
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

			// 跳转页面并传值
			intent = new Intent(BetActivity.this, SelectNumberActivity.class);
			Bundle bundle = new Bundle();
			AppTools.totalCount = num.getCount();

			bundle.putInt("type", num.getPlayType());

			if (null != num.getRedTuoNum())
				bundle.putStringArrayList("redTuo",
						(ArrayList<String>) num.getRedTuoNum());

			bundle.putStringArrayList("red",
					(ArrayList<String>) num.getRedNumbers());
			bundle.putStringArrayList("blue",
					(ArrayList<String>) num.getBlueNumbers());

			Log.i("x", "red====" + num.getRedNumbers().size() + "===blue==="
					+ num.getBlueNumbers().size());

			// 将Bundle 放入Intent
			intent.putExtra("bundle", bundle);

			AppTools.list_numbers.remove(position);

			BetActivity.this.startActivity(intent);
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
			if (AppTools.list_numbers.size() == 0) {
				MyToast.getToast(BetActivity.this, "请先选择投注内容").show();
				break;
			}
			dialog.show();
			dialog.setDialogContent("是否清空投注单号码");
			dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {

				@Override
				public void getResult(int resultCode) {
					// TODO Auto-generated method stub
					if (1 == resultCode) {// 确定
						AppTools.list_numbers.clear();
						adapter.notifyDataSetChanged();
						changeTextShow();
					}
				}
			});
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
			Intent intent = new Intent(BetActivity.this, PlayDescription.class);
			intent.putExtra("type", 0);
			BetActivity.this.startActivity(intent);
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
		int total = 0; // 总金额
		for (SelectedNumbers num : AppTools.list_numbers) {
			total += num.getMoney();
		}
		if (total == 0) {
			MyToast.getToast(getApplicationContext(), "您还没有选择号码").show();
			return;
		}
		total = total * AppTools.bei;
		intent = new Intent(BetActivity.this, JoinActivity.class);
		intent.putExtra("totalMoney", total + "");
		intent.putExtra("isStopChase", isStopChase);
		BetActivity.this.startActivity(intent);
	}

	/** 手选按钮点击事件 */
	private void btn_handClick() {
		MyGridViewAdapter.redSet.clear();
		MyGridViewAdapter.blueSet.clear();
		MyGridViewAdapter.redTuoSet.clear();
		AppTools.totalCount = 0;
		Intent intent = new Intent(BetActivity.this, SelectNumberActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("type", type);
		intent.putExtra("bundle", bundle);
		BetActivity.this.startActivity(intent);
	}

	/** 机选按钮点击事件 */
	private void btn_randomClick(int count) {
		// 得到红球的随机数
		list_red = NumberTools.getRandomNum7(6, 33, true);
		// 得到蓝球的随机数
		list_blue = NumberTools.getRandomNum7(1, 16, true);

		SelectedNumbers numbers = new SelectedNumbers();
		// 排序
		Collections.sort(list_blue);
		Collections.sort(list_red);

		numbers.setBlueNumbers(list_blue);
		numbers.setRedNumbers(list_red);
		numbers.setPlayType(501);
		numbers.setCount(count);
		numbers.setMoney(2 * count);
		numbers.setLotteryNumber(NumberTools.changeSSQ(list_red, null,
				list_blue, 501));

		// AppTools.list_numbers.add(numbers);
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

		sumCount = 0; // 总注数
		for (SelectedNumbers num : AppTools.list_numbers) {
			sumCount += num.getCount();
		}

		if (sumCount != 0) {
			totalMoney = sumCount * 2 * AppTools.bei * AppTools.qi;
			tv_tatol_count.setText(sumCount + "");
			tv_tatol_money.setText(totalMoney + "");
		} else {
			tv_tatol_count.setText("0");
			tv_tatol_money.setText("0");
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
						MyGridViewAdapter.redSet.clear();
						MyGridViewAdapter.redTuoSet.clear();
						MyGridViewAdapter.blueSet.clear();
						AppTools.totalCount = 0;
						for (int i = 0; i < App.activityS1.size(); i++) {
							App.activityS1.get(i).finish();
						}
					}
				}
			});
		} else {
			AppTools.list_numbers.clear();
			MyGridViewAdapter.redSet.clear();
			MyGridViewAdapter.redTuoSet.clear();
			MyGridViewAdapter.blueSet.clear();
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
					info = RspBodyBaseBean.changeBet_info2(
							AppTools.lottery.getLotteryID(),
							AppTools.lottery.getIsuseId(), AppTools.bei, 1, 1,
							0, 0, "", "", 0, totalMoney / AppTools.qi,
							sumCount, AppTools.qi > 1 ? AppTools.qi : 0,
							AppTools.qi == 1 ? 0 : totalMoney,
							AppTools.list_numbers, isStopChase);

					Log.i("x", "betActivity --info" + info);

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
				AppTools.list_numbers.clear();
				AppTools.totalCount = 0;
				SelectNumberActivity.clearHashSet();
				Intent intent = new Intent(getApplicationContext(),
						PaySuccessActivity.class);
				intent.putExtra("paymoney", totalMoney);
				BetActivity.this.startActivity(intent);
				break;
			case -160:
				for (int i = 0; i < App.activityS1.size(); i++) {
					App.activityS1.get(i).finish();
				}
				Toast.makeText(getApplicationContext(), msgs, Toast.LENGTH_LONG)
						.show();
				AppTools.list_numbers.clear();
				AppTools.totalCount = 0;
				break;
			/** 余额不足 **/
			case AppTools.ERROR_MONEY:
				FileUtils.showMoneyLess(BetActivity.this, totalMoney);
				break;
			/** 尚未登陆 **/
			case AppTools.ERROR_UNLONGIN:
				MyToast.getToast(BetActivity.this, "请先登陆").show();
				intent = new Intent(BetActivity.this, LoginActivity.class);
				intent.putExtra("loginType", "bet");
				BetActivity.this.startActivity(intent);
				break;
			/** 点击付款时 所选注数为 0 **/
			case AppTools.ERROR_TOTAL:
				MyToast.getToast(BetActivity.this, "请至少选择一注").show();
				intent = new Intent(BetActivity.this,
						SelectNumberActivity.class);
				intent.putExtra("loginType", "bet");
				BetActivity.this.startActivity(intent);
				break;
			case -500:
				MyToast.getToast(BetActivity.this, "连接超时").show();
				break;
			default:
				Toast.makeText(BetActivity.this, "网络异常，购买失败。请重新点击付款购买。",
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
		changeTextShow();
		// adapter.notifyDataSetChanged();
	}

	// @Override
	// protected void onNewIntent(Intent intent) {
	// setIntent(intent);
	// adapter = new MyBetLotteryListAdapter(BetActivity.this,
	// AppTools.list_numbers);
	// bet_lv_scheme.setAdapter(adapter);
	// super.onNewIntent(intent);
	// }

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		super.onNewIntent(intent);
	}
}
