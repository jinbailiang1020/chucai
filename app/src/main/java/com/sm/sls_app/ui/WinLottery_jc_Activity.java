package com.sm.sls_app.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.LotteryDtMatch;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.ui.adapter.ExpandAdapter_lottery;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.BaseHelper;
import com.sm.sls_app.view.IphoneTreeView;
import com.sm.sls_app.view.MyDateTimeDialog;
import com.sm.sls_app.view.MyToast;
import com.sm.sls_app.wheel.widget.NumericWheelAdapter;

/** 大乐透 期次详情Activity */
public class WinLottery_jc_Activity extends Activity implements OnClickListener {
	private Context context = WinLottery_jc_Activity.this;
	private String opt = "47"; // 格式化后的 opt
	private String auth, info, time, imei, crc; // 格式化后的参数
	private String date, lotteryId;

	private TextView tv_title;
	private IphoneTreeView exList;
	private ExpandAdapter_lottery exAdapter;

	private int year, month, days;

	private List<List<LotteryDtMatch>> listMatch;

	private MyAsynTask myAsynTask;
	private MyHandler myHandler;

	private MyDateTimeDialog dateDialog;
	private NumericWheelAdapter yearAdapter;
	private NumericWheelAdapter monthAdapter;
	private NumericWheelAdapter dayAdapter;
	private ImageButton btn_back, btn_date;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_winlottery_number_jc);
		App.activityS.add(this);
		findView();
		init();
		initDialog();

	}

	/** 初始化对话框的值 **/
	private void initDialog() {
		yearAdapter = new NumericWheelAdapter(2008, 2050);
		monthAdapter = new NumericWheelAdapter(1, 12);
		dayAdapter = new NumericWheelAdapter(1,
				AppTools.getLastDay(year, month));

		dateDialog = new MyDateTimeDialog(WinLottery_jc_Activity.this,
				R.style.dialog, yearAdapter, monthAdapter, dayAdapter,
				new MyClickListener());
		dateDialog.initDay(year, month, days);
	}

	/** Date dialog 点击监听 */
	class MyClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.funds_btn_ok:
				dateDialog.dismiss();
				setDays(dateDialog.y, dateDialog.m, dateDialog.d);
				updateAdapter();
				break;
			case R.id.funds_btn_no:
				dateDialog.dismiss();
				break;
			}
			setDays();
			dateDialog.setCheckItem();

		}
	}

	/** 初始化UI */
	private void findView() {
		btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		btn_date = (ImageButton) this.findViewById(R.id.btn_date);
		tv_title = (TextView) this.findViewById(R.id.tv_head);

		exList = (IphoneTreeView) this.findViewById(R.id.jc_exListView);
		exList.setHeaderView(getLayoutInflater().inflate(
				R.layout.win_lottery_jc_detail_header, exList, false));

		btn_date.setOnClickListener(this);
		btn_back.setOnClickListener(this);
	}

	/** 初始化属性 */
	private void init() {
		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(WinLottery_jc_Activity.this);

		lotteryId = getIntent().getStringExtra("lotteryId");
		date = getIntent().getStringExtra("date");
		if (TextUtils.isEmpty(date)) {
			// 得到Calendar类的实例。
			Calendar now = Calendar.getInstance();
			year = now.get(Calendar.YEAR);
			month = now.get(Calendar.MONTH) + 1;
			days = now.get(Calendar.DATE);
			date = year + "-" + month + "-" + days;
		} else {
			year = Integer.valueOf(date.split("-")[0]);
			month = Integer.valueOf(date.split("-")[1]);
			days = Integer.valueOf(date.split("-")[2]);
		}
		Log.i("x", "年月日----" + year + "-" + month + "-" + days);
		if ("73".equals(lotteryId))
			tv_title.setText("竞彩篮球开奖详情");
		if ("72".equals(lotteryId))
			tv_title.setText("竞彩足球开奖详情");

		myHandler = new MyHandler();
		myAsynTask = new MyAsynTask();
		myAsynTask.execute();

	}

	private List<LotteryDtMatch> setList(String arr) {
		List<LotteryDtMatch> list_m = new ArrayList<LotteryDtMatch>();
		if (arr.length() > 5) {
			JSONArray Arr;
			try {
				Arr = new JSONArray(arr);
				LotteryDtMatch dtmatch = null;
				for (int j = 0; j < Arr.length(); j++) {
					JSONObject item = Arr.getJSONObject(j);
					dtmatch = new LotteryDtMatch();
					dtmatch.setId(item.optString("id"));
					dtmatch.setMatchNumber(item.optString("matchNumber"));
					dtmatch.setWeekDay(item.getString("weekDay"));

					dtmatch.setGame(item.getString("game"));
					dtmatch.setGuestTeam(item.optString("guestTeam"));
					dtmatch.setMainTeam(item.getString("mainTeam"));
					dtmatch.setStopSellTime(item.optString("stopSellTime"));
					dtmatch.setMatchDate(item.optString("matchDate"));

					if ("72".equals(lotteryId)) {
						dtmatch.setSpfResult(item.getString("SPFresult"));
						dtmatch.setZjqResult(item.getString("ZJQresult"));
						dtmatch.setCbfResult(item.getString("CBFresult"));
						dtmatch.setLoseBall(item.getInt("mainLoseBall"));
					} else if ("73".equals(lotteryId)) {
						dtmatch.setSfResult(item.getString("SFresult"));
						dtmatch.setDxResult(item.getString("DXresult"));
						dtmatch.setLoseScore(item.getInt("giveWinLoseScore"));
						dtmatch.setResult(item.getString("result"));
					}
					list_m.add(dtmatch);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i("x", "错误" + e.getMessage());
			}
		}
		return list_m;
	}

	/*** 异步任务 用来后台获取数据 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> {
		/** 在后台执行的程序 */
		String error = "-1";
		ProgressDialog dialog = null;

		/** 最开始执行的 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = BaseHelper.showProgress(context, null, "加载中..", true,
					false);
			dialog.show();

		}

		@Override
		protected String doInBackground(Void... params) {
			listMatch = new ArrayList<List<LotteryDtMatch>>();

			String key = MD5.md5(AppTools.MD5_key);
			info = RspBodyBaseBean.changeJcLottery_info(lotteryId, date);

			Log.i("x", "info------" + info);

			crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");
			auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");

			String[] values = new String[] { opt, auth, info };
			String result = HttpUtils.doPost(AppTools.names, values,
					AppTools.path);
			Log.i("x", "result------" + result);
			if ("-500".equals(result))
				return result;

			try {
				if (null != result) {
					JSONObject object = new JSONObject(result);
					error = object.optString("error");
					if ("0".equals(error)) {
						String detail = object.optString("dtMatch");

						if (detail.length() < 5) {
							Log.i("x", "无数据");
							error = "-1";
						} else {
							// 拿到对阵信息组
							JSONArray array = new JSONArray(new JSONArray(
									detail).toString());

							for (int i = 0; i < array.length(); i++) {

								JSONObject ob = array.getJSONObject(i);

								String arr = ob.optString("table1");
								String arr2 = ob.optString("table2");
								String arr3 = ob.optString("table3");
								System.out.println("====>>" + arr);
								// 判断对阵 是否有
								listMatch.add(setList(arr));
								listMatch.add(setList(arr2));
								listMatch.add(setList(arr3));
							}
						}
					}
				} else {
					Log.i("x", "拿竞彩足球数据为空");
				}
			} catch (Exception e) {
				System.out.println("错误" + e.getMessage());
				error = "-1";
			}
			return error;
		}

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			myHandler.sendEmptyMessage(Integer.parseInt(result));
			super.onPostExecute(result);
		}
	}

	/** 处理页面显示的 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Log.i("x", "changdu------>" + listMatch.size());
			Log.i("x", "msg.what == " + msg.what);
			switch (msg.what) {
			case 0:
				exAdapter = new ExpandAdapter_lottery(
						WinLottery_jc_Activity.this, listMatch,
						Integer.parseInt(lotteryId), exList);
				exList.setAdapter(exAdapter);
				exList.expandGroup(0);
				break;
			case -500:
				MyToast.getToast(WinLottery_jc_Activity.this, "连接超时").show();
				break;
			case -1:
				MyToast.getToast(WinLottery_jc_Activity.this, "没有开奖信息").show();
				exAdapter = new ExpandAdapter_lottery(
						WinLottery_jc_Activity.this, listMatch,
						Integer.parseInt(lotteryId), exList);
				exList.setAdapter(exAdapter);
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;

		case R.id.btn_date:
			dateDialog.show();
			break;
		}
	}

	/** 设置日期 **/
	public void setDays(int y, int m, int d) {
		this.year = y;
		this.month = m;
		this.days = d;
		date = year + "-" + month + "-" + days;
	}

	public void setDays() {
		dateDialog.initDay(year, month, days);
	}

	public void updateAdapter() {
		myAsynTask = new MyAsynTask();
		myAsynTask.execute();
		exAdapter.notifyDataSetChanged();
	}
}
