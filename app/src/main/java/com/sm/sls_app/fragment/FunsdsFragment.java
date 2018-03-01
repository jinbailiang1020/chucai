package com.sm.sls_app.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.FundsInfo;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.ui.FundsInfoActivity;
import com.sm.sls_app.ui.adapter.FundsInfoAdapter;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.view.MyListView2;
import com.sm.sls_app.view.MyScrollView;
import com.sm.sls_app.view.MyToast;
import com.sm.sls_app.view.MyScrollView.OnMyScrollListener;

import com.tencent.a.b.e;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.AsyncTask.Status;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.LinearLayout;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FunsdsFragment extends Fragment {
	private FundsInfoActivity activity;
	private TextView funds_detail_fragment_date, funds_detail_fragment_all;
	private MyListView2 listView;
	private MyScrollView funds_datail_fragment_scrollview;
	private String opt, auth, info, time, imei, crc; // 格式化后的参数
	public List<FundsInfo> listFunds = new ArrayList<FundsInfo>();
	public List<FundsInfo> listFunds_temp = new ArrayList<FundsInfo>();
	public FundsInfoAdapter fAdapter;
	private MyHander myHander;
	private MyAsynTask myAsynTask;
	/** 要更改的 **/
	private LinearLayout ll;
	private ProgressBar pb;
	private int index = 0;
	// 得到账户明细的参数
	private int searchCondition = -1;
	private int pageIndex = 1;
	private int pageSize = 20;

	public int year;
	public int month;
	public int day;

	private long all_in = 0;
	private long expend = 0;
	private long win = 0;

	public static FunsdsFragment newInstance(int year, int month, int day,
			int searchCondition) {
		FunsdsFragment fragment = new FunsdsFragment();
		fragment.year = year;
		fragment.month = month;
		fragment.day = day;
		fragment.searchCondition = searchCondition;
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
		View parent = inflater.inflate(R.layout.funds_detail_fragment,
				container, false);
		findView(parent);
		init();

		return parent;
	}

	private void findView(View parent) {
		activity = (FundsInfoActivity) getActivity();
		funds_datail_fragment_scrollview = (MyScrollView) parent
				.findViewById(R.id.funds_datail_fragment_scrollview);
		funds_detail_fragment_date = (TextView) parent
				.findViewById(R.id.funds_detail_fragment_date);
		funds_detail_fragment_all = (TextView) parent
				.findViewById(R.id.funds_detail_fragment_all);
		listView = (MyListView2) parent
				.findViewById(R.id.funds_detail_fragment_listview);
		myHander = new MyHander();
		funds_datail_fragment_scrollview
				.setOnScrollListener(new ScrollListener());
	}

	private void init() {
		opt = "44";
		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(activity);

		/** 要更改的 **/
		ll = new LinearLayout(activity);
		ll.setBackgroundColor(getResources().getColor(R.color.my_center_bg));
		ll.setLayoutParams(new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		pb = new ProgressBar(activity);
		ll.setGravity(Gravity.CENTER);
		ll.addView(pb);
		listView.addFooterView(ll);
		fAdapter = new FundsInfoAdapter(activity, listFunds);
		listView.setAdapter(fAdapter);

		if (listFunds.isEmpty()) {
			myAsynTask = new MyAsynTask(activity);
			myAsynTask.execute();
		} else {
			if (listFunds.size() < pageSize) {
				listView.removeFooterView(ll);
			}
		}

		funds_detail_fragment_date.setText(year + "年" + month + "月份账户明细：");

		funds_detail_fragment_all
				.setText(getRevenueString(all_in, expend, win));
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 滚动到顶部
		if (listView != null && funds_datail_fragment_scrollview != null) {
			listView.setFocusable(false);
			funds_datail_fragment_scrollview.smoothScrollTo(0, 0);
		}
	}

	private class ScrollListener implements OnMyScrollListener {

		@Override
		public void onBottom() {
			if (myAsynTask != null
					&& !myAsynTask.getStatus().equals(Status.RUNNING)) {
				pageIndex++;
				myAsynTask = new MyAsynTask(activity);
				myAsynTask.execute();
			}
		}

		@Override
		public void onTop() {
		}

		@Override
		public void onScroll() {
		}

	}

	/** 异步任务 用来后台获取数据 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> {
		String error = "-500";
		ProgressDialog pdialog;

		public MyAsynTask(Context context) {
			System.out.println("context-----" + context);
		}

		@Override
		protected String doInBackground(Void... params) {
			info = RspBodyBaseBean.changeFundsInfo_info(searchCondition,
					pageIndex, pageSize, "0", year + "-" + month
							+ "-1 00:00:00", year + "-" + month + "-" + day
							+ " 23:59:59");

			Log.i("x", "资金明细------" + info);

			String key = MD5
					.md5(AppTools.user.getUserPass() + AppTools.MD5_key);

			crc = RspBodyBaseBean.getCrc(time, imei, key, info,
					AppTools.user.getUid());

			auth = RspBodyBaseBean.getAuth(crc, time, imei,
					AppTools.user.getUid());

			String[] values = { opt, auth, info };

			String result = HttpUtils.doPost(AppTools.names, values,
					AppTools.path);

			Log.i("x", "资金明细result-------" + result);

			if ("-500".equals(result))
				return result;

			JSONObject object = null;
			try {
				object = new JSONObject(result);

				if ("0".equals(object.getString("error"))) {
					listFunds_temp.clear();
					all_in = AppTools.sum_Income_Money = object
							.optLong("sum_Income_Money");
					expend = AppTools.sum_Expense_Money = object
							.optLong("sum_Expense_Money");
					win = AppTools.sum_Bonus_Money = object
							.optLong("sum_Bonus_Money");

					String dtAccountDetails = object
							.optString("dtAccountDetails");
					JSONArray array = new JSONArray(dtAccountDetails);

					FundsInfo fInfo = null;
					for (int i = 0; i < array.length(); i++) {
						if (this.isCancelled()) {
							return null;
						}
						JSONObject item = array.getJSONObject(i);
						fInfo = new FundsInfo();
						fInfo.setId(item.getString("id"));
						fInfo.setType(item.getString("in_out"));
						fInfo.setMoney(item.getDouble("money"));
						fInfo.setContent(item.optString("memo"));
						fInfo.setTime(item.getString("datetime"));
						listFunds_temp.add(fInfo);
					}

					if (this.isCancelled()) {
						return null;
					}
					System.out.println("数据-----" + listFunds_temp);
					error = AppTools.ERROR_SUCCESS + "";
				} else {
					error = object.getString("error");
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				error = "-500";
				// System.out.println(e.getMessage());
			}
			return error;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			// myHander.sendEmptyMessage(Integer.parseInt(result));
			switch (Integer.parseInt(result)) {
			case AppTools.ERROR_SUCCESS:
				funds_detail_fragment_date.setText(year + "年" + month
						+ "月份账户明细：");
				funds_detail_fragment_all.setText(getRevenueString(all_in,
						expend, win));
				for (FundsInfo info : listFunds_temp) {
					listFunds.add(info);
				}
				if (0 != listFunds.size() % pageSize) {
					listView.removeFooterView(ll);
					MyToast.getToast(activity, "数据加载完毕").show();
				}
				if (0 == listFunds.size()) {
					listView.removeFooterView(ll);
					MyToast.getToast(activity, "本月无明细").show();
				}
				fAdapter.notifyDataSetChanged();
				break;
			case -500:
				MyToast.getToast(activity, "连接超时").show();
				listView.removeFooterView(ll);
				break;
			// default:
			// Log.i("x", "---错误----==");
			// listView.removeFooterView(ll);
			// break;
			}
			super.onPostExecute(result);
		}
	}

	class MyHander extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case AppTools.ERROR_SUCCESS:
				funds_detail_fragment_date.setText(year + "年" + month
						+ "月份账户明细：");
				funds_detail_fragment_all.setText(getRevenueString(all_in,
						expend, win));

				if (0 != listFunds.size() % pageSize) {
					listView.removeFooterView(ll);
					MyToast.getToast(activity, "数据加载完毕").show();
				}
				if (0 == listFunds.size()) {
					listView.removeFooterView(ll);
					MyToast.getToast(activity, "本月无明细").show();
				}
				fAdapter = new FundsInfoAdapter(activity, listFunds);
				listView.setAdapter(fAdapter);
				fAdapter.notifyDataSetChanged();
				break;
			case -500:
				MyToast.getToast(activity, "连接超时").show();
				listView.removeFooterView(ll);
				break;
			default:
				Log.i("x", "---错误----==" + msg.what);
				listView.removeFooterView(ll);
				break;
			}
		}
	}

	private Spanned getRevenueString(long all, long out, long win) {
		Spanned html = Html.fromHtml("总收入<font color=\"#e3393c\">" + all
				+ "</font>元  " + "支出<font color=\"#e3393c\">" + out
				+ "</font>元  " + "中奖<font color=\"#e3393c\">" + win
				+ "</font>元 ");
		return html;
	}

	public void Refresh(int year, int month, int day) {
		if (null != myAsynTask) {
			this.year = year;
			this.month = month;
			this.day = day;
			this.pageIndex = 1;
			if (listView.getFooterViewsCount() == 0) {
				listView.addFooterView(ll);
			}
			myAsynTask.cancel(true);
			listFunds.clear();
			fAdapter.clear();
			myAsynTask = new MyAsynTask(activity);
			myAsynTask.execute();
		}
	}
}
