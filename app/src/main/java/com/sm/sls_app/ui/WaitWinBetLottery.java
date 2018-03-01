package com.sm.sls_app.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.LotteryContent;
import com.sm.sls_app.dataaccess.Schemes;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.ui.adapter.CenterLotteryAdapter;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WaitWinBetLottery implements OnScrollListener {
	private View view;
	private ListView all_listView;
	private CenterLotteryAdapter adapter;
	private Context context;
	private TextView tv_title;

	private MyHandler myHandler;
	private MyAsynTask myAsynTask;

	private List<List<Schemes>> listAllSchemes = new ArrayList<List<Schemes>>();
	private List<String> list = new ArrayList<String>();

	private String opt, auth, info, time, imei, crc, key; // 格式化后的参数

	private int size = 30;
	private int pageIndex = 1; // 页码
	private int pageSize = size; // 每页显示行数
	private int sort = 5; // 排序方式
	private int isPurchasing = 3; // 返回类型

	/** 要更改的 **/
	private LinearLayout ll;
	private ProgressBar pb;
	private int isEnd = 0;

	// 最后可见条目的索引
	private int lastVisibleIndex;

	public WaitWinBetLottery(Context context, View v) {
		this.view = v;
		this.context = context;
		myHandler = new MyHandler();
		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(context);
		key = MD5.md5(AppTools.user.getUserPass() + AppTools.MD5_key);
	}

	public void init() {
		all_listView = (ListView) view.findViewById(R.id.ll_all_listView);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText("未开奖订单");

		/** 要更改的 新加的加载图片 **/
		ll = new LinearLayout(context);
		pb = new ProgressBar(context);
		ll.setGravity(Gravity.CENTER);
		ll.addView(pb);
		/** 要加在 setAdapter之前 **/
		all_listView.addFooterView(ll);

		adapter = new CenterLotteryAdapter(context, list, listAllSchemes,
				new MyItemCLickListener());
		all_listView.setAdapter(adapter);

		myAsynTask = new MyAsynTask();
		myAsynTask.execute();
	}

	private List<Schemes> listDetail;

	/** 异步任务 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> {
		/** 在后台执行的程序 */
		@Override
		protected String doInBackground(Void... params) {
			boolean falg = true;

			info = RspBodyBaseBean.changeMyLotteryInfo_Info(AppTools.lotteryIds
					+ ",72,73", pageIndex, pageSize, sort, isPurchasing, 1);

			Log.i("x", "获得我投注记录    info  " + info);

			opt = "18";
			crc = RspBodyBaseBean.getCrc(time, imei, key, info,
					AppTools.user.getUid());
			// crc = RspBodyBaseBean.getCrc(time, imei, key, info,
			// "1");

			auth = RspBodyBaseBean.getAuth(crc, time, imei,
					AppTools.user.getUid());
			String[] values = { opt, auth, info };
			String result = HttpUtils.doPost(AppTools.names, values,
					AppTools.path);

			Log.i("x", "得到所有待中奖内容   " + result);

			if ("-500".equals(result))
				return result;

			if (null == result)
				return "1";

			try {
				JSONObject item = new JSONObject(result);

				if ("0".equals(item.optString("error"))) {
					String schemeList = item.optString("schemeList");
					JSONArray array = new JSONArray(schemeList);

					JSONArray jsonArray2 = new JSONArray(array.toString());

					Schemes scheme = null;

					if (jsonArray2.length() == 0)
						return "1";

					// 循环得到每个对象
					for (int i = 0; i < jsonArray2.length(); i++) {
						// 如果取消了 则停止
						if (this.isCancelled()) {
							Log.i("x", "取消了异步。。。。");
							return null;
						}
						JSONObject items = jsonArray2.getJSONObject(i);

						String date = items.getString("date");

						if (!list.contains(date)) {
							falg = false;
							list.add(date);
							listDetail = new ArrayList<Schemes>();
						} else
							falg = true;
						JSONArray detail = new JSONArray(
								items.getString("dateDetail"));

						for (int j = 0; j < detail.length(); j++) {
							JSONObject items2 = detail.getJSONObject(j);

							scheme = new Schemes();

							scheme.setId(items2.optString("Id"));
							scheme.setSchemeNumber(items2
									.optString("schemeNumber"));

							scheme.setAssureMoney(items2
									.optDouble("assureMoney"));
							scheme.setAssureShare(items2.optInt("assureShare"));
							scheme.setBuyed(items2.optString("buyed"));
							scheme.setInitiateName(items2
									.optString("initiateName"));
							scheme.setInitiateUserID(items2
									.optString("initiateUserID"));
							scheme.setIsPurchasing(items2
									.optString("isPurchasing"));
							scheme.setIsuseID(items2.optString("isuseID"));
							scheme.setIsuseName(items2.optString("isuseName"));
							scheme.setLevel(items2.optInt("level"));
							scheme.setLotteryID(items2.optString("lotteryID"));
							scheme.setLotteryName(items2
									.optString("lotteryName"));
							scheme.setLotteryNumber(items2
									.optString("lotteryNumber"));
							JSONArray array_contents = new JSONArray(
									items2.optString("buyContent"));
							if (array_contents != null) {
								List<LotteryContent> contents = new ArrayList<LotteryContent>();
								LotteryContent lotteryContent = null;
								for (int k = 0; k < array_contents.length(); k++) {
									lotteryContent = new LotteryContent();
									try {
										JSONArray array2 = new JSONArray(
												array_contents.optString(k));

										lotteryContent.setLotteryNumber(array2
												.getJSONObject(0).optString(
														"lotteryNumber"));
										lotteryContent.setPlayType(array2
												.getJSONObject(0).optString(
														"playType"));
										lotteryContent.setSumMoney(array2
												.getJSONObject(0).optString(
														"sumMoney"));
										lotteryContent.setSumNum(array2
												.getJSONObject(0).optString(
														"sumNum"));
										contents.add(lotteryContent);
									} catch (Exception e) {
										JSONObject array2 = new JSONObject(
												array_contents.optString(k));

										lotteryContent.setLotteryNumber(array2
												.optString("lotteryNumber"));
										lotteryContent.setPlayType(array2
												.optString("playType"));
										lotteryContent.setSumMoney(array2
												.optString("sumMoney"));
										lotteryContent.setSumNum(array2
												.optString("sumNum"));
										contents.add(lotteryContent);
									}
								}
								scheme.setContent_lists(contents);
							}

							scheme.setMoney(items2.optInt("money"));
							scheme.setPlayTypeID(items2.optInt("playTypeID"));
							scheme.setPlayTypeName(items2
									.optString("playTypeName"));
							scheme.setQuashStatus(items2.optInt("quashStatus"));
							scheme.setRecordCount(items2.optInt("RecordCount"));
							scheme.setSchedule(items2.optInt("schedule"));
							scheme.setSchemeBonusScale(items2
									.optDouble("schemeBonusScale"));
							scheme.setSchemeIsOpened(items2
									.optString("schemeIsOpened"));
							scheme.setChaseTaskID(items2.getInt("chaseTaskID"));
							scheme.setSecrecyLevel(items2
									.optInt("secrecyLevel"));
							scheme.setSerialNumber(items2
									.optInt("SerialNumber"));
							scheme.setShare(items2.optInt("share"));
							scheme.setShareMoney(items2.optInt("shareMoney"));
							scheme.setSurplusShare(items2
									.optInt("surplusShare"));
							scheme.setTitle(items2.optString("title"));
							scheme.setWinMoneyNoWithTax(items2
									.optInt("winMoneyNoWithTax"));
							scheme.setWinNumber(items2.optString("winNumber"));

							scheme.setDateTime(items2.optString("datetime"));

							scheme.setDescription(items2
									.optString("description"));

							scheme.setIsChase(items2.optInt("isChase"));

							scheme.setMultiple(items2.optInt("multiple"));
							scheme.setFromClient(items2.getInt("FromClient"));
							scheme.setMyBuyMoney(items2.getInt("myBuyMoney")
									+ "");

							scheme.setMyBuyShare(items2.optInt("myBuyShare"));

							listDetail.add(scheme);
						}
						if (!falg)
							listAllSchemes.add(listDetail);
					}

				}
				if (this.isCancelled()) {
					Log.i("x", "取消了异步。。。。");
					return null;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i("x", "myAllLottery 错误" + e.getMessage());
			}

			return "0";
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (null == result)
				return;
			myHandler.sendEmptyMessage(Integer.parseInt(result));
			super.onPostExecute(result);
		}
	}

	/** 进行页面刷新 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				getThreeMonth();
				adapter.setDate(list, listAllSchemes);
				adapter.notifyDataSetChanged();
				all_listView.setOnScrollListener(WaitWinBetLottery.this);
				if (getTotalCount() % 30 != 0)
					myHandler.sendEmptyMessage(-1);
				if (!FileUtils.checkThreeMonth(list)) {
					myHandler.sendEmptyMessage(-1);
				}
				break;
			case -1:
				all_listView.removeFooterView(ll);
				all_listView.setOnScrollListener(null);
				break;
			default:
				// all_listView.removeFooterView(ll);
				// all_listView.setOnScrollListener(null);
				break;
			}
			super.handleMessage(msg);
		}
	}

	private void getThreeMonth() {
		String now = FileUtils.getSchemeTime(System.currentTimeMillis());
		for (int i = 0; i < list.size(); i++) {
			if (!FileUtils.compareMonth(now, list.get(i))) {
				list.remove(i);
				listAllSchemes.remove(i);
			}
		}
	}

	class MyItemCLickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			// TODO Auto-generated method stub
			TextView tv_id = (TextView) view.findViewById(R.id.tv_id);
			int itemId = Integer.parseInt(tv_id.getText().toString());

			Schemes scheme = listAllSchemes.get(itemId).get(position);
			Intent intent = null;

			if (scheme.getIsChase() == 0)
				if ("72".equals(scheme.getLotteryID())
						|| "73".equals(scheme.getLotteryID()))
					intent = new Intent(context, MyCommonLotteryInfo_jc.class);
				else
					intent = new Intent(context, MyCommonLotteryInfo.class);
			else
				intent = new Intent(context, MyFollowLotteryInfo.class);

			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("scheme", scheme);
			Log.i("waitwin", "是否合买=== " + scheme.getIsPurchasing());
			context.startActivity(intent);
		}
	}

	private int getTotalCount() {
		int total = 0;
		for (List<Schemes> list : listAllSchemes) {
			total += list.size();
		}
		Log.i("x", "总条数  " + total);
		return total;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& lastVisibleIndex == adapter.getCount()) {

			if (isEnd != getTotalCount()) {
				Log.i("x", "滑到最底部");
				pageIndex++;
				isEnd = getTotalCount();
				myAsynTask = new MyAsynTask();
				myAsynTask.execute();
			} else {
				myHandler.sendEmptyMessage(-1);
			}
		}

	}

}
