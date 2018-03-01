package com.sm.sls_app.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.WinDetail;
import com.sm.sls_app.dataaccess.WinLottery;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.ui.adapter.WinLotteryInfoExpandAdapter;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.BaseHelper;
import com.sm.sls_app.utils.NetWork;
import com.sm.sls_app.view.MyToast;
import com.sm.sls_app.activity.R.id;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;

/**
 * 开奖详情Activity
 * 
 * @author SLS003
 */
public class WinLotteryInfoActivity extends Activity {

	private Context context = WinLotteryInfoActivity.this;
	private PullToRefreshExpandableListView listView;
	private WinLotteryInfoExpandAdapter adapter;
	private MyHandler handler;
	private MyAsynTask myAsynTask;

	private List<WinLottery> listWinLottery = new ArrayList<WinLottery>();

	private String opt, auth, info, time, imei, crc; // 格式化后的参数

	private int pageIndex = 1; // 查询页码
	private int pageSize = 10; // 查询条数

	private String lotteryId;

	private TextView hall_tv_advertisement;
	private ImageButton btn_back;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_win_lotteryinfo);
		App.activityS.add(this);

		lotteryId = this.getIntent().getStringExtra("lotteryId");
		findView();
		setListener();
		initHttpRes();
	}

	/** 初始化属性 */
	private void initHttpRes() {
		opt = "25";
		imei = RspBodyBaseBean.getIMEI(context);
		time = RspBodyBaseBean.getTime();

		String uid = "-1";
		String password = "";
		if (null != AppTools.user) {
			uid = AppTools.user.getUid();
			password = AppTools.user.getUserPass();
		}

		info = RspBodyBaseBean.changeWinLottery_info(-1, lotteryId, pageIndex,
				pageSize, 1, 0, 10, "", "");

		crc = RspBodyBaseBean.getCrc(time, imei,
				MD5.md5(password + AppTools.MD5_key), info, uid);

		auth = RspBodyBaseBean.getAuth(crc, time, imei, uid);

		myAsynTask = new MyAsynTask();
		myAsynTask.execute();
	}

	/** 初始化UI */
	private void findView() {
		handler = new MyHandler();
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		listView = (PullToRefreshExpandableListView) this
				.findViewById(R.id.win_listView_lottery);
		listView.getRefreshableView().setGroupIndicator(null);

		hall_tv_advertisement = (TextView) this
				.findViewById(R.id.hall_tv_advertisement);

		String titleText = setTitleText(lotteryId);
		hall_tv_advertisement.setText(titleText);

	}

	private String setTitleText(String name) {
		String lotteryNameString = "双色球";
		if ("6".equals(name)) {
			lotteryNameString = "3D";
		} else if ("63".equals(name)) {
			lotteryNameString = "排列三";
		} else if ("62".equals(name)) {
			lotteryNameString = "十一运夺金";
		} else if ("64".equals(name)) {
			lotteryNameString = "排列五";
		} else if ("3".equals(name)) {
			lotteryNameString = "七星彩";
		} else if ("39".equals(name)) {
			lotteryNameString = "大乐透";
		} else if ("13".equals(name)) {
			lotteryNameString = "七乐彩";
		} else if ("74".equals(name)) {
			lotteryNameString = "胜负彩";
		} else if ("75".equals(name)) {
			lotteryNameString = "任九场";
		} else if ("82".equals(name)) {
			lotteryNameString = "幸运彩";
		} else if ("28".equals(name)) {
			lotteryNameString = "重庆时时彩";
		} else if ("70".equals(name)) {
			lotteryNameString = "江西11选5";
		} else if ("83".equals(name)) {
			lotteryNameString = "江苏快3";
		} else if ("78".equals(name)) {
			lotteryNameString = "广东11选5";
		} else if ("61".equals(name)) {
			lotteryNameString = "江西时时彩";
		} else if ("92".equals(name)) {
			lotteryNameString = "河内时时彩";
		} else if ("93".equals(name)) {
			lotteryNameString = "印尼时时彩";
		} else if ("94".equals(name)) {
			lotteryNameString = "北京PK10";
		} else if ("66".equals(name)) {
			lotteryNameString = "新疆时时彩";
		}
		return lotteryNameString + "开奖详情";

	}

	/** 绑定监听 */
	@SuppressWarnings("unchecked")
	private void setListener() {

		listView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				listView.getLoadingLayoutProxy(true, false)
						.setLastUpdatedLabel(
								"最近更新: "
										+ Long2DateStr_detail(System
												.currentTimeMillis()));
				if (NetWork.isConnect(context) == true) {
					pageIndex = 1;
					initHttpRes();
				} else {
					MyToast.getToast(getApplicationContext(), "网络连接异常，请检查网络")
							.show();
				}
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				if (NetWork.isConnect(context) == true) {
					pageIndex++;
					initHttpRes();
				} else {
					MyToast.getToast(getApplicationContext(), "网络连接异常，请检查网络")
							.show();
				}
			}
		});
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public String Long2DateStr_detail(long time) {
		String format = "yyyy-M-d HH:mm";
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String long_time = sdf.format(date);
		return long_time;
	}

	/** ListView 点击监听 */
	class MyItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			position = position - 1;
			WinLottery wLottery = listWinLottery.get(position);
			Intent intent = null;
			switch (Integer.parseInt(wLottery.getLotteryId())) {
			case 5:
				intent = new Intent(WinLotteryInfoActivity.this,
						WinLottery_ssq_Activity.class);
				intent.putExtra("wLottery", wLottery);
				WinLotteryInfoActivity.this.startActivity(intent);
				break;
			case 62:
			case 70:
			case 78:// 广东11选5
				intent = new Intent(WinLotteryInfoActivity.this,
						WinLottery_11ydj_Activity.class);
				intent.putExtra("wLottery", wLottery);
				WinLotteryInfoActivity.this.startActivity(intent);
				break;
			case 3:
				intent = new Intent(WinLotteryInfoActivity.this,
						WinLottery_qxc_Activity.class);
				intent.putExtra("wLottery", wLottery);
				WinLotteryInfoActivity.this.startActivity(intent);
				break;
			case 63:
			case 64:
			case 69:
				intent = new Intent(WinLotteryInfoActivity.this,
						WinLottery_pl3_pl5_Activity.class);
				intent.putExtra("wLottery", wLottery);
				WinLotteryInfoActivity.this.startActivity(intent);
				break;
			case 6:
				intent = new Intent(WinLotteryInfoActivity.this,
						WinLottery_3d_Activity.class);
				intent.putExtra("wLottery", wLottery);
				WinLotteryInfoActivity.this.startActivity(intent);
				break;
			case 39:
				intent = new Intent(WinLotteryInfoActivity.this,
						WinLottery_dlt_Activity.class);
				intent.putExtra("wLottery", wLottery);
				WinLotteryInfoActivity.this.startActivity(intent);
				break;
			case 13:
				intent = new Intent(WinLotteryInfoActivity.this,
						WinLottery_qlc_Activity.class);
				intent.putExtra("wLottery", wLottery);
				WinLotteryInfoActivity.this.startActivity(intent);
				break;
			case 74:
			case 75:
				intent = new Intent(WinLotteryInfoActivity.this,
						WinLottery_sfc_Activity.class);
				intent.putExtra("wLottery", wLottery);
				WinLotteryInfoActivity.this.startActivity(intent);
				break;
			case 28:
				intent = new Intent(WinLotteryInfoActivity.this,
						WinLottery_ssc_Activity.class);
				intent.putExtra("wLottery", wLottery);
				WinLotteryInfoActivity.this.startActivity(intent);
				break;
			case 83:
				intent = new Intent(WinLotteryInfoActivity.this,
						WinLottery_k3_Activity.class);
				intent.putExtra("wLottery", wLottery);
				System.out.println(null == wLottery.getListWinDetail());
				WinLotteryInfoActivity.this.startActivity(intent);
				break;
			}
		}

	}

	private int p = 0;

	/** 异步任务 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> {
		ProgressDialog dialog = null;

		/** 最开始执行的 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if (p == 0) {
				dialog = BaseHelper.showProgress(context, null, "加载中..", true,
						false);
				dialog.show();
			}

		}

		@Override
		protected String doInBackground(Void... params) {
			String values[] = { opt, auth, info };
			System.out.println("info===" + info);
			String result = HttpUtils.doPost(AppTools.names, values,
					AppTools.path);
			System.out.println("++*()()+" + result);
			if ("-500".equals(result))
				return "-1";
			try {
				JSONObject item = new JSONObject(result);
				if ("0".equals(item.optString("error"))) {
					String array = item.optString("dtWinNumberInfo");
					JSONArray array2 = new JSONArray(array);
					WinLottery winLottery = null;
					if (pageIndex == 1) {
						listWinLottery = null;
						listWinLottery = new ArrayList<WinLottery>();
					}
					for (int i = 0; i < array2.length(); i++) {
						JSONObject items = array2.getJSONObject(i);
						if (null != items) {
							winLottery = new WinLottery();
							winLottery.setId(items.optString("id"));
							winLottery.setName(items.optString("name"));
							winLottery.setLotteryId(lotteryId);
							winLottery.setEndTime(items.getString("EndTime"));
							winLottery.setStateUpdateTime(items
									.getString("StateUpdateTime"));
							String winDetail = items.optString("WinDetail");
							List<WinDetail> listWinDetail = new ArrayList<WinDetail>();
							if (!"".equals(winDetail) && null != winDetail) {
								JSONArray winDe = new JSONArray(winDetail);
								WinDetail wDetail = null;
								//
								for (int j = 0; j < winDe.length(); j++) {
									wDetail = new WinDetail();
									JSONObject itemsDetail = winDe
											.getJSONObject(j);
									wDetail.setBonusName(itemsDetail
											.optString("bonusName"));
									wDetail.setBonusValue(itemsDetail
											.optString("bonusValue"));
									wDetail.setWinningCount(itemsDetail
											.optInt("winningCount"));
									listWinDetail.add(wDetail);
								}
							} else {
								WinDetail wDetail = new WinDetail();
								wDetail.setBonusName("-");
								wDetail.setBonusValue("-");
								wDetail.setWinningCount(-1);
								listWinDetail.add(wDetail);
							}
							winLottery.setListWinDetail(listWinDetail);
							winLottery.setTotalMoney(items
									.optString("TotalMoney"));
							winLottery.setSales(items.optString("Sales"));
							String winLotteryNumber = items
									.optString("winLotteryNumber");
							if (winLotteryNumber.contains("+")) {
								String[] str = winLotteryNumber.split("\\+");
								winLottery.setRedNum(str[0]);
								winLottery.setBlueNum(str[1]);
							} else {
								winLottery.setRedNum(winLotteryNumber);
							}
							listWinLottery.add(winLottery);

						}
					}
					return "0";
				}

			} catch (Exception ex) {
				System.out.println("WinLotteryInfoActivity--错误"
						+ ex.getMessage());
				ex.printStackTrace();
			}

			return "1";

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (p == 0) {
				dialog.dismiss();
			}
			p++;

			handler.sendEmptyMessage(Integer.parseInt(result));
			super.onPostExecute(result);
		}

	}

	/** 执行ListView 数据绑定 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 0:
				if (pageIndex == 1
						&& listView.getMode().equals(Mode.PULL_FROM_START)) {
					listView.setMode(Mode.BOTH);
				}
				System.out.println("listWinLottery大小" + listWinLottery.size()
						+ "");
				adapter = new WinLotteryInfoExpandAdapter(context,
						listWinLottery);
				listView.getRefreshableView().setAdapter(adapter);

				break;
			case 1:
				if (pageIndex != 1) {
					listView.setMode(Mode.PULL_FROM_START);
				}
				MyToast.getToast(context, "没有更多开奖信息").show();
				break;
			case -1:
				MyToast.getToast(WinLotteryInfoActivity.this, "连接超时").show();
				break;
			}
			if (listWinLottery.size() < pageSize) {
				listView.setMode(Mode.PULL_FROM_START);
			}
			if (listView.isRefreshing()) {
				listView.onRefreshComplete();
			}

			super.handleMessage(msg);
		}
	}
}
