package com.sm.sls_app.fragment.loaderclass;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.LotteryContent;
import com.sm.sls_app.dataaccess.Schemes;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.fragment.loaderclass.FollowDateLoadeSchedule.MyAsynTask;
import com.sm.sls_app.fragment.loaderclass.FollowDateLoadeSchedule.MyHandler;
import com.sm.sls_app.fragment.loaderclass.FollowDateLoadeSchedule.listItemClicl;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.ui.FollowInfoActivity;
import com.sm.sls_app.ui.adapter.MyFollowAdapter;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.FileUtils;
import com.sm.sls_app.utils.NetWork;
import com.sm.sls_app.view.MyListView;
import com.sm.sls_app.view.MyToast;
import com.sm.sls_app.view.MyListView.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

public class FollowDateLoadeEach implements OnScrollListener {
	private static final String TAG = "FollowDateLoadeEach";
	private View view;
	private PullToRefreshListView followListView;
	private TextView follow_hint;
	private MyFollowAdapter fAdapter;
	private Context context;

	private MyHandler myHandler;
	public MyAsynTask myAsynTask;

	private List<String> list = new ArrayList<String>();
	private String opt = "14"; // 格式化后的 opt
	private String auth, info, time, imei, crc, key; // 格式化后的参数

	private String uid = "-1";
	private int pageSize = 20;
	private int pageIndex = 1;

	private Integer sort = 1; // 类型 0.进度 1.金额 2.每份金额 3.战绩
	private Integer sortType = 0; // 排序方式 0.升序 1.降序

	public String lotteryId; // 查询合买的彩种ID

	public int max = 0; // 判断是否数据已经加载完毕

	/** 要更改的 **/
	private LinearLayout ll;
	private ProgressBar pb;
	private int isEnd = 0;

	// 最后可见条目的索引
	private int lastVisibleIndex;

	public List<Schemes> listSchemes = new ArrayList<Schemes>();
	public List<Schemes> temp_listSchemes = new ArrayList<Schemes>();

	public FollowDateLoadeEach(String lotteryId, Context context, View v,
			int sort, int sortType) {
		this.lotteryId = lotteryId;
		this.sortType = sortType;
		this.view = v;
		this.sort = sort;
		this.context = context;
		myHandler = new MyHandler();
		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(context);
		init();
		setListener();
	}

	/** 绑定监听 */
	@SuppressWarnings("unchecked")
	private void setListener() {
		listSchemes.clear();
		fAdapter.setList(listSchemes);
		followListView.setAdapter(fAdapter);
		followListView.setOnItemClickListener(new listItemClicl());
		followListView.setOnScrollListener(this);
		// followListView.setScrollListener(this);
		followListView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				followListView.getLoadingLayoutProxy(true, false)
						.setLastUpdatedLabel(
								"最近更新: "
										+ FileUtils.Long2DateStr_detail(System
												.currentTimeMillis()));
				if (NetWork.isConnect(context) == true) {
					listSchemes.clear();
					pageIndex = 1;
					if (myAsynTask != null) {
						myAsynTask.cancel(true);
					}
					myAsynTask = new MyAsynTask();
					myAsynTask.execute();
				} else {
					MyToast.getToast(context, "网络连接异常，请检查网络").show();
					followListView.onRefreshComplete();
				}
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				if (NetWork.isConnect(context) == true) {
					pageIndex++;
					if (myAsynTask != null) {
						myAsynTask.cancel(true);
					}
					myAsynTask = new MyAsynTask();
					myAsynTask.execute();
				} else {
					MyToast.getToast(context, "网络连接异常，请检查网络").show();
					followListView.onRefreshComplete();
				}
			}
		});
		// followListView.setOnScrollListener(this);
	}

	public void init() {
		followListView = (PullToRefreshListView) view
				.findViewById(R.id.followListView);
		follow_hint = (TextView) view.findViewById(R.id.follow_hint);

		/** 要加在 setAdapter之前 **/
		// followListView.addFooterView(ll);

		fAdapter = new MyFollowAdapter(context);
		fAdapter.setList(listSchemes);
		followListView.setAdapter(fAdapter);

		myAsynTask = new MyAsynTask();
		myAsynTask.execute();
	}

	/** listView Item 点击事件 */
	class listItemClicl implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {

			if (fAdapter.getList().size() != 0) {
				Intent intent = new Intent(context, FollowInfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("schem",
						fAdapter.getList().get(position - 1));
				intent.putExtra("bundle", bundle);
				context.startActivity(intent);
			} else {
				System.out.println("FollowSchemeActivity点击了。。。。");
			}
		}
	}

	/** 异步任务 */
	public class MyAsynTask extends AsyncTask<Void, Integer, String> {
		String error = "-1001";

		@Override
		protected String doInBackground(Void... params) {
			info = RspBodyBaseBean.changeJoin_info(lotteryId, pageIndex,
					pageSize, sort, sortType);

			Log.i("x", "Follow---info---" + info);
			// Log.i("x", "Follow---opt---" + opt);
			crc = RspBodyBaseBean.getCrc(time, imei, MD5.md5(AppTools.MD5_key),
					info, uid);
			auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");

			String[] values = { opt, auth, info };

			try {
				String result = HttpUtils.doPost(AppTools.names, values,
						AppTools.path);
				if ("-500".equals(result))
					return "-500";
				if (null != result) {
					JSONObject object = new JSONObject(result);
					if ("0".equals(object.opt("error"))) {
						if (object.optString("schemeList").equals("[]"))
							return "-2";
						JSONArray array = new JSONArray(
								object.optString("schemeList"));
						temp_listSchemes.clear();
						Schemes schemes = null;
						for (int i = 0; i < array.length(); i++) {
							if (myAsynTask.isCancelled()) {
								// Log.i("x", "取消了");
								return null;
							}

							JSONObject items = array.getJSONObject(i);

							// Log.i("x", "合买======" + items);

							schemes = new Schemes();

							schemes.setId(items.optString("Id"));
							schemes.setSchemeNumber(items
									.optString("schemeNumber"));
							schemes.setPlayTypeName(items
									.optString("PlayTypeName"));// 获取玩法名称
							schemes.setCountNotes(items.optString("CountNotes"));// 获取玩法注数
							schemes.setLotteryID(items.getString("lotteryID"));
							schemes.setLotteryName(items
									.getString("lotteryName"));
							schemes.setInitiateUserID(items
									.optString("initiateUserID"));
							schemes.setInitiateName(items
									.optString("initiateName"));
							schemes.setSumIsuseNum(items.optInt("sumIsuseNum"));
							schemes.setMoney(items.optLong("money")); // zongjiner
							schemes.setShareMoney(items.optInt("shareMoney"));
							schemes.setShare(items.optInt("share"));

							schemes.setSurplusShare(items
									.optInt("surplusShare"));
							schemes.setMultiple(items.optInt("multiple"));

							schemes.setAssureShare(items.optInt("assureShare"));
							schemes.setAssureMoney(items
									.optDouble("assureMoney")); // baoerjin
							schemes.setSchemeBonusScale(items
									.optDouble("schemeBonusScale"));
							schemes.setSecrecyLevel(items
									.optInt("secrecyLevel"));
							schemes.setQuashStatus(items.optInt("quashStatus"));
							schemes.setLevel(items.optInt("level"));
							schemes.setWinMoneyNoWithTax(items
									.optDouble("winMoneyNoWithTax"));
							// 以下是防止出现小于1，items.optInt成0的情况。
							schemes.setSchedule((int) Math.ceil(items
									.optDouble("schedule")));// fangan
							// schemes.setSchedule(items.optInt("schedule"));//fangan

							schemes.setSchemeIsOpened(items
									.optString("schemeIsOpened"));
							schemes.setTitle(items.getString("title"));
							schemes.setLotteryNumber(items
									.optString("lotteryNumber"));

							schemes.setPlayTypeID(items.optInt("PlayTypeID"));

							schemes.setSerialNumber(items
									.getInt("SerialNumber"));
							schemes.setRecordCount(items.optInt("RecordCount"));

							schemes.setDescription(items
									.getString("description"));

							// JSONArray array_contents = new JSONArray(
							// items.optString("buyContent"));
							// if (array_contents != null) {
							// List<LotteryContent> contents = new
							// ArrayList<LotteryContent>();
							// LotteryContent lotteryContent = null;
							// for (int k = 0; k < array_contents.length(); k++)
							// {
							// lotteryContent = new LotteryContent();
							// try {
							// JSONArray array2 = new JSONArray(
							// array_contents.optString(k));
							//
							// lotteryContent.setLotteryNumber(array2
							// .getJSONObject(0).optString(
							// "lotteryNumber"));
							// lotteryContent.setPlayType(array2
							// .getJSONObject(0).optString(
							// "playType"));
							// lotteryContent.setSumMoney(array2
							// .getJSONObject(0).optString(
							// "sumMoney"));
							// lotteryContent.setSumNum(array2
							// .getJSONObject(0).optString(
							// "sumNum"));
							// contents.add(lotteryContent);
							// } catch (Exception e) {
							// JSONObject array2 = new JSONObject(
							// array_contents.optString(k));
							//
							// lotteryContent.setLotteryNumber(array2
							// .optString("lotteryNumber"));
							// lotteryContent.setPlayType(array2
							// .optString("playType"));
							// lotteryContent.setSumMoney(array2
							// .optString("sumMoney"));
							// lotteryContent.setSumNum(array2
							// .optString("sumNum"));
							// contents.add(lotteryContent);
							// }
							// }
							// schemes.setContent_lists(contents);
							// }
							this.publishProgress();

							temp_listSchemes.add(schemes);
						}

						if (myAsynTask.isCancelled()) {
							return null;
						}
						error = "0";
					}
				} else {
					Log.i("x", "result == null");
					return "-1001";
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("FlooleFragment 错误" + e.getMessage());
				return "-1001";
			}
			return error;
		}

		/** 任务结束时执行 */
		@Override
		protected void onPostExecute(String result) {
			followListView.onRefreshComplete();
			myHandler.sendEmptyMessage(Integer.parseInt(result));
			super.onPostExecute(result);
		}
	}

	/** 进行页面刷新 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (pageIndex == 1) {
					listSchemes.clear();
					followListView.setMode(Mode.BOTH);
				}
				for (Schemes scheme : temp_listSchemes) {
					listSchemes.add(scheme);
				}
				fAdapter.setList(listSchemes);
				fAdapter.notifyDataSetChanged();
				// if (listSchemes.size() < pageSize) {
				// followListView.setMode(Mode.PULL_FROM_START);
				// }
				if (listSchemes.size() == 0) {
					followListView.setMode(Mode.PULL_FROM_START);
					follow_hint.setText("暂时没有该项数据");
				}
				break;
			case 2:
				listSchemes.clear();
				fAdapter.clear();
				fAdapter.notifyDataSetChanged();
				break;
			case -500:
				follow_hint.setText("连接超时");
				MyToast.getToast(context, "连接超时").show();
				followListView.setMode(Mode.PULL_FROM_START);
				break;
			case -2:
				follow_hint.setText("没有更多数据");
				MyToast.getToast(context, "没有更多数据").show();
				followListView.setMode(Mode.PULL_FROM_START);
				break;
			default:
				follow_hint.setText("数据加载失败");
				followListView.setMode(Mode.PULL_FROM_START);
				break;
			}
			super.handleMessage(msg);
		}
	}

	private int getTotalCount() {
		int total = 0;
		for (Schemes list : listSchemes) {
			total += 1;
		}
		Log.i("x", "总条数  " + total);
		return total;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& lastVisibleIndex == fAdapter.getCount() + 1) {

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

	public void updateListview(int sort, String lotteryId) {
		this.sortType = sort;
		this.lotteryId = lotteryId;
		listSchemes.clear();
		fAdapter.setList(listSchemes);
		fAdapter.notifyDataSetChanged();
		finish();
		pageIndex = 1;
		myAsynTask.cancel(true);
		myAsynTask = new MyAsynTask();
		myAsynTask.execute();
	}

	public void finish() {
		follow_hint.setText("正在加载中...");
		if (followListView != null) {
			followListView.onRefreshComplete();
			followListView.setMode(Mode.DISABLED);
		}
		if (myAsynTask != null) {
			myAsynTask.cancel(true);
		}
	}
}
