package com.sm.sls_app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.Schemes;
import com.sm.sls_app.dataaccess.ShowDtMatch;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.ui.adapter.OrderInfoJcPlayTypeAdapter;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.BaseHelper;
import com.sm.sls_app.view.MyListView2;
import com.sm.sls_app.view.MyToast;

public class FollowNumberActivity_jc extends Activity {

	private ListView listView;
	private MyAdapter adapter;

	private Bundle bundle;

	private Schemes scheme;

	private List<ShowDtMatch> list_show;
	private List<String> playtypes = new ArrayList<String>();
	private List<List<Map<String, Object>>> playitems = new ArrayList<List<Map<String, Object>>>();
	private MyHandler myHandler;
	private MyAsynTask myAsynTask;

	private String opt = "46"; // 格式化后的 opt

	private String auth, info, time, imei, crc; // 格式化后的参数
	private ImageButton btn_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_follow_number);
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		bundle = getIntent().getBundleExtra("bundle");
		scheme = (Schemes) bundle.getSerializable("schem");
		Log.i("x", "方案ID" + scheme.getId());
		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(FollowNumberActivity_jc.this);
		myHandler = new MyHandler();
		myAsynTask = new MyAsynTask();
		myAsynTask.execute();
	}

	/** 初始化UI */
	private void findView() {
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		listView = (ListView) this.findViewById(R.id.followinfo_jc_listView);
		adapter = new MyAdapter(FollowNumberActivity_jc.this);
		listView.setAdapter(adapter);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/*** 异步任务 用来后台获取数据 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> {
		String error = "0";
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = BaseHelper.showProgress(FollowNumberActivity_jc.this,
					null, "加载中..", true, false);
			dialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			String key = MD5.md5(AppTools.MD5_key);

			info = RspBodyBaseBean.changeJC_info(scheme.getId() + "");
			crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");
			auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");

			String values[] = { opt, auth, info };
			String result = HttpUtils.doPost(AppTools.names, values,
					AppTools.path);

			System.out.println("合买竞彩结果是--" + result);

			if ("-500".equals(result))
				return result;

			if (result.length() == 0) {
				// Log.i("x", "没有得到数据--！");
				return "-1";
			}
			try {
				JSONObject object = new JSONObject(result);

				error = object.getString("error");
				if ("0".equals(object.getString("error"))) {
					String s = object.optString("informationId");
					JSONArray array = new JSONArray(s);
					if (array.length() == 0)
						return "-1";
					list_show = new ArrayList<ShowDtMatch>();
					for (int i = 0; i < array.length(); i++) {
						ShowDtMatch dt = new ShowDtMatch();
						JSONObject item = array.getJSONObject(i);
						dt.setSchemeId(item.getInt("SchemeID"));
						dt.setPlayType(item.getInt("PlayType"));
						dt.setMatchNumber(item.getString("MatchNumber"));
						dt.setGame(item.getString("Game"));
						dt.setMainTeam(item.getString("MaiTeam"));
						dt.setGuestTeam(item.getString("GuestTeam"));
						dt.setStopSelling(item.getString("StopSelling"));
						dt.setLetBile(item.getInt("LetBile"));
						dt.setScore(item.optString("Score"));
						dt.setResult(item.optString("Results"));
						dt.setPassType(item.optString("PassType"));
						String investContent = item.getString("investContent");
						String Ways = item.getString("Content");
						String[] st = investContent.split(",");

						String[] select = new String[st.length];
						double[] odds = new double[st.length];

						for (int j = 0; j < st.length; j++) {
							String[] st2 = st[j].split("-");
							Log.i("x", "选的结果---" + st2[0]);
							select[j] = st2[0];
							Log.i("x", "赔率---" + st2[1]);
							odds[j] = Double.parseDouble(st2[1]);
						}

						String[] invest_way = Ways.split(",");

						int[] investway = new int[invest_way.length];
						boolean isMixed = false;
						for (int k = 0; k < invest_way.length; k++) {
							String[] temp = invest_way[k].split("-");
							investway[k] = Integer.valueOf(temp[0]);
							if (k == 0 && !(investway[k] / 100 < 0)) {
								isMixed = true;
							}
						}

						dt.setSelect(select);
						dt.setOdds(odds);
						dt.setInvestWay(investway);
						dt.setMixed(isMixed);

						list_show.add(dt);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("x", "拿对阵报错--->" + e.getMessage());
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
			System.out.println("先接受消息");
			switch (msg.what) {
			case -1:
				MyToast.getToast(getApplicationContext(), "没有数据").show();
				break;
			case 0:
				findView();
				break;
			case -500:
				MyToast.getToast(FollowNumberActivity_jc.this, "连接超时").show();
				break;
			}
		}
	}

	/** Adapter **/
	class MyAdapter extends BaseAdapter {
		private Context context;

		public MyAdapter(Context _context) {
			this.context = _context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list_show.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list_show.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			ViewHolder holder;
			ShowDtMatch showDtmatch = list_show.get(position);
			if (null == view) {
				holder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(context);
				view = inflater.inflate(R.layout.orderinfo_jc_item_52, null);
				holder.orderinfo_jc_title = (LinearLayout) view
						.findViewById(R.id.orderinfo_jc_title);
				holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
				holder.tv_team = (TextView) view.findViewById(R.id.tv_team);
				holder.orderinfo_jc_listview = (MyListView2) view
						.findViewById(R.id.orderinfo_jc_listview);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			holder.orderinfo_jc_title
					.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
			holder.tv_time.setText(Html.fromHtml(showDtmatch.getMatchNumber()
					.substring(0, 2)
					+ "<br>"
					+ showDtmatch.getMatchNumber().substring(2, 5)));
			holder.tv_team.setText(Html.fromHtml(showDtmatch.getMainTeam()
					+ "<br>vs<br>" + showDtmatch.getGuestTeam()));
			setMixedBetingType(showDtmatch);
			holder.orderinfo_jc_listview
					.setAdapter(new OrderInfoJcPlayTypeAdapter(
							FollowNumberActivity_jc.this, playtypes, playitems));
			return view;
		}
	}

	private class ViewHolder {
		LinearLayout orderinfo_jc_title;
		TextView tv_time, tv_team;
		MyListView2 orderinfo_jc_listview;
	}

	private void setMixedBetingType(ShowDtMatch dtMatch) {
		playtypes.clear();
		playitems.clear();
		String type_normal = null;
		switch (Integer.valueOf(dtMatch.getPlayType())) {
		// 竞彩足球 混合投注
		case 7206: {
			List<Map<String, Object>> items1 = null;
			List<Map<String, Object>> items2 = null;
			List<Map<String, Object>> items3 = null;
			List<Map<String, Object>> items4 = null;
			List<Map<String, Object>> items5 = null;
			String str_type = null;
			String item_result = null;
			for (int i = 0; i < dtMatch.getInvestWay().length; i++) {
				if (!TextUtils.isEmpty(dtMatch.getResult())
						&& dtMatch.getResult().contains(",")) {
					item_result = dtMatch.getResult().split(",")[i];
				}
				switch (dtMatch.getInvestWay()[i] / 100) {
				case 1:
					str_type = "胜平负";
					items1 = setItems(str_type, items1, dtMatch.getSelect()[i],
							dtMatch.getOdds()[i], item_result);
					break;
				case 2:
					str_type = "让球";
					items2 = setItems(str_type, items2, dtMatch.getSelect()[i],
							dtMatch.getOdds()[i], item_result);
					break;
				case 3:
					str_type = "总进球数";
					items3 = setItems(str_type, items3, dtMatch.getSelect()[i],
							dtMatch.getOdds()[i], item_result);
					break;
				case 4:
					str_type = "比分";
					items4 = setItems(str_type, items4, dtMatch.getSelect()[i],
							dtMatch.getOdds()[i], item_result);
					break;
				case 5:
					str_type = "半全场";
					items5 = setItems(str_type, items5, dtMatch.getSelect()[i],
							dtMatch.getOdds()[i], item_result);
					break;
				}
			}
			for (int i = 0; i < playtypes.size(); i++) {
				if (playtypes.get(i).equals("胜平负")) {
					playitems.add(items1);
				} else if (playtypes.get(i).equals("让球")) {
					playitems.add(items2);
				} else if (playtypes.get(i).equals("总进球数")) {
					playitems.add(items3);
				} else if (playtypes.get(i).equals("比分")) {
					playitems.add(items4);
				} else {
					playitems.add(items5);
				}
			}
		}
			break;
		// 竞彩篮球 混合投注
		case 7306: {
			List<Map<String, Object>> basket_items1 = null;
			List<Map<String, Object>> basket_items2 = null;
			List<Map<String, Object>> basket_items3 = null;
			List<Map<String, Object>> basket_items4 = null;
			String str_type = null;
			String item_result = null;
			for (int i = 0; i < dtMatch.getInvestWay().length; i++) {
				if (!TextUtils.isEmpty(dtMatch.getResult())
						&& dtMatch.getResult().contains(",")) {
					item_result = dtMatch.getResult().split(",")[i];
				}
				switch (dtMatch.getInvestWay()[i] / 100) {
				case 1: // 胜负
					str_type = "胜负";
					basket_items1 = setItems(str_type, basket_items1,
							dtMatch.getSelect()[i], dtMatch.getOdds()[i],
							item_result);
					break;
				case 2:// 让分
					str_type = "让分";
					basket_items2 = setItems(str_type, basket_items2,
							dtMatch.getSelect()[i], dtMatch.getOdds()[i],
							item_result);
					break;
				case 3:// 胜分差
					str_type = "胜分差";
					basket_items3 = setItems(str_type, basket_items3,
							dtMatch.getSelect()[i], dtMatch.getOdds()[i],
							item_result);
					break;
				case 4:// 大小分
					str_type = "大小分";
					basket_items4 = setItems(str_type, basket_items4,
							dtMatch.getSelect()[i], dtMatch.getOdds()[i],
							item_result);
					break;
				}
			}

			for (int i = 0; i < playtypes.size(); i++) {
				if (playtypes.get(i).equals("胜负")) {
					playitems.add(basket_items1);
				} else if (playtypes.get(i).equals("让分")) {
					playitems.add(basket_items2);
				} else if (playtypes.get(i).equals("胜分差")) {
					playitems.add(basket_items3);
				} else {
					playitems.add(basket_items4);
				}
			}
		}
			break;
		// 非混合投注
		case 7201: // 足球胜平负
			if (type_normal == null) {
				type_normal = "胜平负";
			}

		case 7207: // 足球 让球胜平负
			if (type_normal == null) {
				type_normal = "让球";
			}

		case 7202: // 足球 比分
			if (type_normal == null) {
				type_normal = "比分";
			}
		case 7203: // 足球 总进球
			if (type_normal == null) {
				type_normal = "总进球";
			}

		case 7204: // 足球 半全场
			if (type_normal == null) {
				type_normal = "半全场";
			}
		case 7301: // 篮球 胜负
			if (type_normal == null) {
				type_normal = "胜负";
			}
		case 7302: // 篮球 让分
			if (type_normal == null) {
				type_normal = "让分";
			}
		case 7303: // 篮球 胜分差
			if (type_normal == null) {
				type_normal = "胜分差";
			}
		case 7304: // 篮球 大小分
			if (type_normal == null) {
				type_normal = "大小分";
			}
			String item_result = null;
			List<Map<String, Object>> items = null;

			for (int i = 0; i < dtMatch.getInvestWay().length; i++) {
				if (!TextUtils.isEmpty(dtMatch.getResult())
						&& dtMatch.getResult().contains(",")) {
					item_result = dtMatch.getResult().split(",")[i];
				}
				items = setItems(type_normal, items, dtMatch.getSelect()[i],
						dtMatch.getOdds()[i], item_result);
			}
			playitems.add(items);
			break;
		}
	}

	private List<Map<String, Object>> setItems(String str_type,
			List<Map<String, Object>> items, String select, double odd,
			String result) {
		if (!playtypes.contains(str_type)) {
			playtypes.add(str_type);
			items = new ArrayList<Map<String, Object>>();
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("select", select);
		map.put("odd", odd);
		map.put("result", TextUtils.isEmpty(result) ? "待定" : result);
		items.add(map);
		return items;
	}

}
