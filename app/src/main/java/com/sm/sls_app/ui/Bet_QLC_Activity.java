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
import com.sm.sls_app.ui.adapter.MyBetLotteryListQLCAdapter;
import com.sm.sls_app.ui.adapter.Qlc_Gridview_Adapter;
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

/**
 * 功能：投注页面 版本
 * 
 * @author Kinwee 修改时间 2014-12-3
 * 
 */

public class Bet_QLC_Activity extends Activity implements OnClickListener {

	private final static String TAG = "Bet_QLC_Activity";
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

	private Intent intent;
	private List<String> list_red;
	private long sumCount, totalMoney; // 方案总注数 // 方案总金额

	private EditText et_bei, et_qi; // 用户输入的倍数 // 用户输入的期数

	private MyHandler myHandler;
	private MyAsynTask myAsynTask;

	private CheckBox bet_cb_stopfollow;
	private int isStopChase = 1;

	private final int playType = 1301;

	public MyBetLotteryListQLCAdapter adapter;
	private boolean backHome = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bet);
		App.activityS.add(this);
		App.activityS1.add(this);
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

		btn_playtype.setText("七乐彩投注单");

		adapter = new MyBetLotteryListQLCAdapter(Bet_QLC_Activity.this,
				AppTools.list_numbers);
		// 隐藏与显示
		btn_help.setVisibility(View.GONE);
		btn_clearall.setVisibility(View.GONE);
		iv_up_down.setVisibility(View.GONE);
		btn_follow.setVisibility(View.VISIBLE);

	}

	/** 初始化属性 */
	private void init() {
		if (null != Qlc_Gridview_Adapter.mSelected_numbers
				&& Qlc_Gridview_Adapter.mSelected_numbers.size() != 0) {
			SelectedNumbers numbers = new SelectedNumbers();
			// 保存红球
			List<String> redList = new ArrayList<String>();
			for (String st : Qlc_Gridview_Adapter.mSelected_numbers) {
				redList.add(st);
			}
			Collections.sort(redList);
			numbers.setRedNumbers(redList);
			numbers.setPlayType(playType);
			numbers.setLotteryNumber(redList.toString().replace("[", "")
					.replace("]", "").replace(",", ""));

			numbers.setCount(AppTools.totalCount);
			numbers.setMoney(AppTools.totalCount * 2);
			AppTools.list_numbers.add(0, numbers);
			adapter.notifyDataSetChanged();
		}
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
			Intent intent2 = new Intent(Bet_QLC_Activity.this,
					Select_QlcActivity.class);
			Bundle bundle = new Bundle();

			AppTools.totalCount = num.getCount();

			bundle.putStringArrayList("mSelected_numbers",
					(ArrayList<String>) num.getRedNumbers());
			// 将Bundle 放入Intent

			intent2.putExtra("bundle", bundle);

			AppTools.list_numbers.remove(position);

			Bet_QLC_Activity.this.startActivity(intent2);
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
				MyToast.getToast(Bet_QLC_Activity.this, "请先选择投注内容").show();
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
			Intent intent = new Intent(Bet_QLC_Activity.this,
					PlayDescription.class);
			intent.putExtra("type", 0);
			Bet_QLC_Activity.this.startActivity(intent);
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
		intent = new Intent(Bet_QLC_Activity.this, JoinActivity.class);
		intent.putExtra("totalMoney", total + "");
		intent.putExtra("isStopChase", isStopChase);
		Bet_QLC_Activity.this.startActivity(intent);
	}

	/** 手选按钮点击事件 */
	private void btn_handClick() {
		Qlc_Gridview_Adapter.mSelected_numbers.clear();
		AppTools.totalCount = 0;
		Intent intent = new Intent(Bet_QLC_Activity.this,
				Select_QlcActivity.class);
		Bet_QLC_Activity.this.startActivity(intent);
	}

	/** 机选按钮点击事件 */
	private void btn_randomClick(int count) {
		// 得到红球的随机数
		list_red = NumberTools.getRandomNum7(7, 30, true);

		SelectedNumbers numbers = new SelectedNumbers();
		// 排序
		Collections.sort(list_red);
		numbers.setRedNumbers(list_red);
		// numbers.setType("普通投注");
		numbers.setCount(count);
		numbers.setMoney(2 * count);
		// System.out.println("listtostring--> "+list_red.toString());
		numbers.setPlayType(playType);
		numbers.setLotteryNumber(list_red.toString().replace("[", "")
				.replace("]", "").replace(",", ""));

		AppTools.list_numbers.add(numbers);
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
						Qlc_Gridview_Adapter.mSelected_numbers.clear();
						AppTools.totalCount = 0;
						for (int i = 0; i < App.activityS1.size(); i++) {
							App.activityS1.get(i).finish();
						}
					}
				}
			});
		} else {
			AppTools.list_numbers.clear();
			Qlc_Gridview_Adapter.mSelected_numbers.clear();
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
		String error = "-1001";

		@Override
		protected String doInBackground(Void... params) {
			if (AppTools.list_numbers.size() > 0) {
				time = RspBodyBaseBean.getTime();
				imei = RspBodyBaseBean.getIMEI(getApplicationContext());
				Log.i("x", "倍-" + AppTools.bei + "----期" + AppTools.qi);

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

					Log.i("x", "qilecai======info-----" + info);

					crc = RspBodyBaseBean.getCrc(time, imei, key, info,
							AppTools.user.getUid());
					auth = RspBodyBaseBean.getAuth(crc, time, imei,
							AppTools.user.getUid());
					String values[] = { opt, auth, info };
					String result = HttpUtils.doPost(AppTools.names, values,
							AppTools.path);

					System.out.println("Bet_QLC result" + result);
					if ("-500".equals(result))
						return result;

					if (result == null) {
						error = "-1001";
					}
					try {
						JSONObject object = new JSONObject(result);

						if ("0".equals(object.optString("error"))) {
							AppTools.user.setBalance(object
									.optDouble("balance"));
							AppTools.user.setFreeze(object.optDouble("freeze"));
							AppTools.schemeId = object.optInt("schemeids");
							AppTools.lottery.setChaseTaskID(object
									.optInt("chasetaskids"));
						}
						error = object.optString("error");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						error = "-1001";
					}
				} else {
					error = AppTools.ERROR_UNLONGIN + "";
				}
			} else {
				error = AppTools.ERROR_TOTAL + "";
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
			switch (msg.what) {
			case AppTools.ERROR_SUCCESS:
				AppTools.list_numbers.clear();
				AppTools.totalCount = 0;
				Intent intent = new Intent(getApplicationContext(),
						PaySuccessActivity.class);
				Select_QlcActivity.clearHashSet();
				intent.putExtra("paymoney", totalMoney);
				Bet_QLC_Activity.this.startActivity(intent);
				break;
			case AppTools.ERROR_MONEY:
				FileUtils.showMoneyLess(Bet_QLC_Activity.this, totalMoney);
				break;
			/** 尚未登陆 **/
			case AppTools.ERROR_UNLONGIN:
				MyToast.getToast(Bet_QLC_Activity.this, "请先登陆").show();
				intent = new Intent(Bet_QLC_Activity.this, LoginActivity.class);
				intent.putExtra("loginType", "bet");
				Bet_QLC_Activity.this.startActivity(intent);
				break;
			/** 点击付款时 所选注数为 0 **/
			case AppTools.ERROR_TOTAL:
				MyToast.getToast(Bet_QLC_Activity.this, "请至少选择一注").show();
				intent = new Intent(Bet_QLC_Activity.this,
						Select_QlcActivity.class);
				intent.putExtra("loginType", "bet");
				Bet_QLC_Activity.this.startActivity(intent);
				break;
			case -500:
				MyToast.getToast(Bet_QLC_Activity.this, "连接超时").show();
				break;
			default:
				Toast.makeText(Bet_QLC_Activity.this, "网络异常，购买失败。请重新点击付款购买。",
						Toast.LENGTH_SHORT).show();

				break;
			}
			setEnabled(true);
			super.handleMessage(msg);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!backHome) {
			if (AppTools.list_numbers == null)
				AppTools.list_numbers = new ArrayList<SelectedNumbers>();
			findView();
			init();
			setListener();
			changeTextShow();
		}
		backHome = false;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		super.onNewIntent(intent);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		backHome = true;
	}
}
