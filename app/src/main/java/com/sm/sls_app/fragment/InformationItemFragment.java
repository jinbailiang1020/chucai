package com.sm.sls_app.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.Information;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.ui.Information_Detail;
import com.sm.sls_app.ui.adapter.InformationAdapter;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.FileUtils;
import com.sm.sls_app.utils.NetWork;
import com.sm.sls_app.view.MyToast;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 资讯子
 * 
 * @author Administrator
 * 
 */
public class InformationItemFragment extends Fragment {

	private Activity activity;
	private PullToRefreshListView information_item_listView;
	private TextView information_item_hint;
	private ImageView information_item_bottom;
	private List<Information> informations = new ArrayList<Information>();
	private List<Information> informations_temp = new ArrayList<Information>();
	private InformationAdapter adapter;
	// 网络参数
	private int newType = 2;
	private String opt = "45";
	private String auth, info, time, imei, crc; // 格式化后的参数
	private int pageIndex = 1;
	private int pageSize = 20;
	private int isEnd = pageSize;
	private MyAsynTask myAsynTask;
	private int postion;

	public static InformationItemFragment newInstance(int postion, int newType) {
		InformationItemFragment fragment = new InformationItemFragment();
		fragment.newType = newType;
		fragment.postion = postion;
		return fragment;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.information_item_fragment,
				container, false);
		activity = getActivity();
		init(view);
		return view;
	}

	@SuppressWarnings("unchecked")
	private void init(View view) {
		imei = RspBodyBaseBean.getIMEI(activity);
		time = RspBodyBaseBean.getTime();

		information_item_listView = (PullToRefreshListView) view
				.findViewById(R.id.information_item_listView);
		information_item_hint = (TextView) view
				.findViewById(R.id.information_item_hint);
		// information_item_bottom = (ImageView) view
		// .findViewById(R.id.information_item_bottom);

		adapter = new InformationAdapter(activity, informations);// 得到资讯
		information_item_listView.setAdapter(adapter);
		if (informations.isEmpty()) {
			myAsynTask = new MyAsynTask();
			myAsynTask.execute();
		} else {
			if (informations.size() < pageSize) {
				// information_item_listView.removeFooterView(ll);
				// information_item_bottom.setVisibility(View.VISIBLE);
			}
		}
		information_item_listView
				.setOnItemClickListener(new MyItemsClickListener());

		information_item_listView
				.setOnRefreshListener(new OnRefreshListener2() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase refreshView) {
						information_item_listView.getLoadingLayoutProxy(true,
								false).setLastUpdatedLabel(
								"最近更新: "
										+ FileUtils.Long2DateStr_detail(System
												.currentTimeMillis()));
						if (NetWork.isConnect(activity)) {
							informations.clear();
							pageIndex = 1;
							if (myAsynTask != null) {
								myAsynTask.cancel(true);
							}
							myAsynTask = new MyAsynTask();
							myAsynTask.execute();
						} else {
							MyToast.getToast(activity, "网络连接异常，请检查网络").show();
							information_item_listView.onRefreshComplete();
						}
					}

					@Override
					public void onPullUpToRefresh(PullToRefreshBase refreshView) {
						if (NetWork.isConnect(activity)) {
							pageIndex++;
							if (myAsynTask != null) {
								myAsynTask.cancel(true);
							}
							myAsynTask = new MyAsynTask();
							myAsynTask.execute();
						} else {
							MyToast.getToast(activity, "网络连接异常，请检查网络").show();
							information_item_listView.onRefreshComplete();
						}
					}
				});
	}

	private class MyItemsClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			position = position - 1;

			int count = informations.size();
			if (count > 0) {
				long[] ids = new long[informations.size()];
				long currentNewId;
				currentNewId = informations.get(position).getId();
				for (int i = 0; i < informations.size(); i++) {
					ids[i] = informations.get(i).getId();
				}
				Intent intent = new Intent(activity, Information_Detail.class);
				Bundle bundle = new Bundle();
				bundle.putLongArray("ids", ids);
				bundle.putInt("newIndex", position);
				bundle.putLong("currentNewId", currentNewId);
				bundle.putInt("newType", newType);
				intent.putExtra("informationList", bundle);

				System.out.println("newtype=" + newType + " newIndex="
						+ position + "showsize=" + informations.size());

				startActivity(intent);
			}
		}

	}

	private class MyAsynTask extends AsyncTask<Void, Integer, String> {

		@Override
		protected String doInBackground(Void... params) {
			// listWinLottery.clear();
			String uid = "-1";
			String password = "";
			if (null != AppTools.user) {
				uid = AppTools.user.getUid();
				password = AppTools.user.getUserPass();
			}

			info = RspBodyBaseBean.getLotteryInformation(pageIndex, pageSize,
					newType);
			crc = RspBodyBaseBean.getCrc(time, imei,
					MD5.md5(password + AppTools.MD5_key), info, uid);
			auth = RspBodyBaseBean.getAuth(crc, time, imei, uid);

			String values[] = { opt, auth, info };
			String result = HttpUtils.doPost(AppTools.names, values,
					AppTools.path);
			Log.i("彩票资讯info-----", info);
			Log.i("咨讯result-----", result);
			if (result.equals("-500")) {
				return "-500";
			}
			try {
				JSONObject item = new JSONObject(result);

				if ("0".equals(item.optString("error"))) {
					String array = item.optString("dtInformationTitles");
					JSONArray array2 = new JSONArray(array);
					isEnd = array2.length();
					if (isEnd == 0)
						return "1";
					informations_temp.clear();
					for (int i = 0; i < array2.length(); i++) {
						JSONObject items = array2.getJSONObject(i);
						if (null != items) {
							addInformation(items);
						}
					}
					return "0";
				} else {
					Log.i("咨讯result error", "error" + item.optString("error"));
					return item.optString("error");
				}

			} catch (Exception ex) {
				Log.i("Exception-----", ex.toString());
			}
			return "-1";
		}

		private void addInformation(JSONObject items) {

			Information information = new Information();
			String titleString = items.optString("title");
			String newTypeNameString = getNewTypeStr(titleString);
			information.setId(items.optLong("id"));
			information.setNewType(newTypeNameString);
			information.setParentTypeId(newType);
			information.setDateTime(items.optString("dateTime"));
			if (!newTypeNameString.equals("")) {
				information
						.setTitle(titleString.replace(newTypeNameString, ""));
			} else {
				information.setTitle(titleString);
			}
			information.setSerialNumber(items.optInt("SerialNumber"));
			information.setRecordCount(items.optString("RecordCount"));
			informations_temp.add(information);

		}

		private String getNewTypeStr(String str) {
			String resultString = "";
			Pattern p = Pattern.compile(".*?\\[(.*?)\\].*?");
			Matcher m = p.matcher(str);
			if (m.matches()) {
				resultString = "[" + m.group(1) + "]"; // 匹配第1项
			}
			return resultString;
		}

		@Override
		protected void onPostExecute(String result) {
			information_item_listView.onRefreshComplete();
			dealResult(Integer.valueOf(result));
			super.onPostExecute(result);
		}
	}

	private void dealResult(int result) {
		switch (result) {
		case 0:
			if (pageIndex == 1) {
				informations.clear();
				setStatus(true, "加载完毕", false);
			}
			for (Information information : informations_temp) {
				informations.add(information);
			}
			adapter.notifyDataSetChanged();
			if (isEnd < pageSize && pageIndex != 1) {
				setStatus(false, "没有更多数据", true);
			}
			if (informations.size() == 0 && pageIndex == 1) {
				setStatus(false, "暂无资讯", false);
			}
			break;
		case 1:
			setStatus(false, "没有更多数据", true);
			break;
		case -1:
			setStatus(false, "数据加载出错，请重试", false);
			break;
		case -500:
			setStatus(false, "连接超时，请重试", false);
		case -4504:
			setStatus(false, "暂无资讯", false);
			break;
		}
	}

	/**
	 * 改变数据获取之后，listview的状态
	 * 
	 */
	private void setStatus(boolean status, String msg, boolean toast) {
		if (msg.equals("-1")) {
			if (information_item_listView != null
					&& information_item_hint != null && myAsynTask != null) {
				myAsynTask.cancel(true);
				information_item_listView.onRefreshComplete();
				information_item_listView.setMode(Mode.DISABLED);
				information_item_hint.setText("正在加载中..");
			}
			return;
		}
		if (status) {
			if (information_item_listView != null)
				information_item_listView.setMode(Mode.BOTH);

		} else {
			if (information_item_listView != null)
				information_item_listView.setMode(Mode.PULL_FROM_START);
		}
		if (information_item_hint != null) {
			information_item_hint.setText(msg);
		}
		if (toast) {
			MyToast.getToast(activity, msg).show();
		}
	}

	public void finish() {
		setStatus(false, "-1", false);
	}
}
