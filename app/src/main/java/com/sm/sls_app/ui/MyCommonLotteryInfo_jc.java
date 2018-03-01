package com.sm.sls_app.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.DtMatch;
import com.sm.sls_app.dataaccess.Schemes;
import com.sm.sls_app.dataaccess.ShowDtMatch;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.fragment.HallFragment;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.ui.adapter.MyBetLotteryAdapter;
import com.sm.sls_app.ui.adapter.OrderInfoJcPlayTypeAdapter;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.BaseHelper;
import com.sm.sls_app.utils.FileUtils;
import com.sm.sls_app.view.MyListView2;
import com.sm.sls_app.view.MyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 彩票 竞彩 投注订单详情
 * 
 * @author SLS003
 */
public class MyCommonLotteryInfo_jc extends Activity implements OnClickListener {

	private TextView tv_lotteryName, tv_money, tv_state, tv_winMoney, tv_count,
			tv_time, tv_orderId, tv_betType, tv_lotteryNum, tv_name, tv_yong,
			tv_scheme, tv_buyCount, tv_title, tv_content, tv_show,
			tv_lotteryName_issue;
	private ImageView img_logo, ll_divider;
	private ImageButton betinfo_hide_btn, btn_back;
	private LinearLayout ll_numberCount;
	private MyListView2 mListView;
	private MyBetLotteryAdapter adapter;

	private Schemes scheme;

	private RelativeLayout rl_join1, rl_join2;

	/** 竞彩 */
	private List<ShowDtMatch> list_show;
	private List<String> playtypes = new ArrayList<String>();
	private List<List<Map<String, Object>>> playitems = new ArrayList<List<Map<String, Object>>>();

	private String opt = "46"; // 格式化后的 opt

	private String auth, info, time, imei, crc; // 格式化后的参数
	private MyAsynTask myAsynTask;
	private MyHandler myHandler;
	private Button btn_jixu;
	private Button btn_touzhu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_betlottey_info);

		findView();

		initView();

	}

	/** myScrollView.smoothScrollTo(0,20); 初始化UI */
	private void findView() {
		tv_lotteryName = (TextView) findViewById(R.id.tv_lotteryName);
		tv_money = (TextView) findViewById(R.id.tv_money2);
		tv_state = (TextView) findViewById(R.id.tv_state2);
		tv_winMoney = (TextView) findViewById(R.id.tv_winMoney2);
		tv_count = (TextView) findViewById(R.id.tv_numberCount);
		tv_time = (TextView) findViewById(R.id.tv_time2);
		tv_orderId = (TextView) findViewById(R.id.tv_orderId2);
		tv_betType = (TextView) findViewById(R.id.tv_orderType2);
		tv_lotteryNum = (TextView) findViewById(R.id.tv_num1);
		tv_show = (TextView) findViewById(R.id.tv_show);
		tv_show.setVisibility(View.GONE);

		tv_name = (TextView) findViewById(R.id.tv_name2);
		tv_yong = (TextView) findViewById(R.id.tv_yong2);
		tv_scheme = (TextView) findViewById(R.id.tv_scheme2);
		tv_buyCount = (TextView) findViewById(R.id.tv_buy2);
		tv_title = (TextView) findViewById(R.id.tv_schemetitle2);
		tv_content = (TextView) findViewById(R.id.tv_schemeContent2);
		tv_lotteryName_issue = (TextView) findViewById(R.id.tv_lotteryName_issue);
		img_logo = (ImageView) findViewById(R.id.img_logo);
		mListView = (MyListView2) findViewById(R.id.lv_betInfo);
		mListView.setBackgroundResource(R.color.my_center_bg2);
		int px = dp2px(1);
		mListView.setPadding(px, 0, px, px);
		// gv_winNumber = (MyGridView) findViewById(R.id.gv_winNumber);

		rl_join1 = (RelativeLayout) findViewById(R.id.rl_joinInfo);
		rl_join2 = (RelativeLayout) findViewById(R.id.rl_joinInfo2);

		btn_jixu = (Button) findViewById(R.id.btn_jixu);
		btn_touzhu = (Button) findViewById(R.id.btn_touzhu);

		betinfo_hide_btn = (ImageButton) findViewById(R.id.betinfo_hide_btn);
		ll_numberCount = (LinearLayout) findViewById(R.id.ll_numberCount);
		ll_divider = (ImageView) findViewById(R.id.ll_divider);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
	}

	private int dp2px(int dp) {
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
		return (int) px;
	}



	/** 给控件赋值 */
	private void initView() {
		btn_back.setOnClickListener(this);
		betinfo_hide_btn.setOnClickListener(this);
		ll_numberCount.setOnClickListener(this);
		btn_jixu.setOnClickListener(this);
		btn_touzhu.setOnClickListener(this);
		btn_jixu.setVisibility(View.GONE);
		myHandler = new MyHandler();

		scheme = (Schemes) getIntent().getSerializableExtra("scheme");

		myAsynTask = new MyAsynTask();
		myAsynTask.execute();

		if (null == scheme)
			return;

		rl_join1.setVisibility(View.GONE);
		rl_join2.setVisibility(View.GONE);


		btn_touzhu.setText(scheme.getLotteryName() + "投注");
		tv_lotteryName.setText(FileUtils.getTitleText(scheme.getLotteryID()));
		img_logo.setBackgroundResource(AppTools.allLotteryLogo.get(scheme
				.getLotteryID()));
        tv_lotteryName_issue.setText(scheme.getIsuseName() == null ? ""
                : scheme.getIsuseName() + "期");
		tv_money.setText(scheme.getMoney() + "元");
		tv_time.setText(scheme.getDateTime());
		tv_orderId.setText(scheme.getSchemeNumber());

		if (scheme.getFromClient() == 1)
			tv_betType.setText("网页投注");
		else if (scheme.getFromClient() == 2)
			tv_betType.setText("手机APP投注");

		tv_winMoney.setText("--");

		// String [] str = null;
		// if(scheme.getLotteryNumber().contains("\n"))
		// {
		// str = scheme.getLotteryNumber().split("\\n");
		// }
		// else
		// {
		// str = new String [1];
		// str[0] = scheme.getLotteryNumber();
		// }
		// tv_count.setText(str.length+"条");
		System.out.println("---*-****");
		// 是否合买
		if (scheme.getIsPurchasing().equals("False")) {
			rl_join1.setVisibility(View.VISIBLE);
			rl_join2.setVisibility(View.VISIBLE);

			tv_name.setText(scheme.getInitiateName());
			tv_yong.setText((scheme.getSchemeBonusScale() * 100) + "%");
			tv_scheme.setText(scheme.getShare() + "份,共" + scheme.getMoney()
					+ "元,每份" + scheme.getShareMoney() + "元");
			tv_buyCount.setText(scheme.getMyBuyShare() + "份  共"
					+ scheme.getMyBuyMoney() + "元");
			tv_title.setText(scheme.getTitle());
			tv_content.setText(scheme.getDescription());
		}

		if (0 != scheme.getQuashStatus()) {
			tv_state.setText("已撤单");
		} else {
			if ("False".equals(scheme.getSchemeIsOpened())) {
				tv_state.setText("未开奖");
			} else if ("True".equals(scheme.getSchemeIsOpened())) {
				if (scheme.getWinMoneyNoWithTax() > 0) {
					tv_state.setText("中奖");
					tv_winMoney.setText(scheme.getWinMoneyNoWithTax() + "元");
				} else {
					tv_state.setText("未中奖");
				}
			}
		}

	}

	/*** 异步任务 用来后台获取数据 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> {
		String error = "0";
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = BaseHelper.showProgress(MyCommonLotteryInfo_jc.this, null,
					"加载中..", true, false);
			dialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			String key = MD5
					.md5(AppTools.user.getUserPass() + AppTools.MD5_key);

			info = RspBodyBaseBean.changeJC_info(scheme.getId() + "");
			crc = RspBodyBaseBean.getCrc(time, imei, key, info,
					AppTools.user.getUid());
			auth = RspBodyBaseBean.getAuth(crc, time, imei,
					AppTools.user.getUid());

			String values[] = { opt, auth, info };
			String result = HttpUtils.doPost(AppTools.names, values,
					AppTools.path);

			Log.i("x", "竞彩--结果    " + result);
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
							// Log.i("x", "选的结果---" + st2[0]);
							select[j] = st2[0];
							// Log.i("x", "赔率---" + st2[1]);
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
						// Log.i("x", "11111--select----" + select[0]);

						list_show.add(dt);
						// Log.i("x", "长度----listShow---" + list_show.size());
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
			Intent intent = null;
			switch (msg.what) {
			case -1:
				MyToast.getToast(getApplicationContext(), "没有数据").show();
				break;
			case 0:
				mListView.setAdapter(new MyAdapter(getApplicationContext()));
				tv_count.setText(list_show.size() + "场");
				break;
			case 100:
				List<List<DtMatch>> list_Matchs1 = AppTools.lottery
						.getList_Matchs();
				intent = new Intent(MyCommonLotteryInfo_jc.this,
						Select_jczqActivity.class);
				MyCommonLotteryInfo_jc.this.startActivity(intent);
				break;
			case 110:
				List<List<DtMatch>> list_Matchs2 = AppTools.lottery
						.getList_Matchs();
				intent = new Intent(MyCommonLotteryInfo_jc.this,
						Select_jclqActivity.class);
				MyCommonLotteryInfo_jc.this.startActivity(intent);
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
							MyCommonLotteryInfo_jc.this, playtypes, playitems));
			return view;
		}
	}

	private class ViewHolder {
		LinearLayout orderinfo_jc_title;
		TextView tv_time, tv_team;
		MyListView2 orderinfo_jc_listview;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_touzhu:// 去往本彩种投注
			String lotteryID = scheme.getLotteryID();
			goToSelectLottery(lotteryID);
			break;
		case R.id.betinfo_hide_btn:
		case R.id.ll_numberCount:
			if (!betinfo_hide_btn.isSelected()) {
				betinfo_hide_btn.setSelected(true);
				ll_divider.setVisibility(View.GONE);
				mListView.setVisibility(View.GONE);

			} else {
				betinfo_hide_btn.setSelected(false);
				ll_divider.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.btn_back:
			finish();
			break;
		}
	}

	/**
	 * 根据彩种id跳转不同选号页面
	 * 
	 * @param lotteryID
	 *            彩种id
	 */
	private void goToSelectLottery(String lotteryID) {
		int id = Integer.parseInt(lotteryID);
		Intent intent = null;
		switch (id) {
		case 72:// 竞彩足球
		case 73:// 竞彩篮球
			long currentTime_jc = System.currentTimeMillis();
			for (int i = 0; i < HallFragment.listLottery.size(); i++) {
				if ("72".equals(HallFragment.listLottery.get(i).getLotteryID())
						|| "73".equals(HallFragment.listLottery.get(i)
								.getLotteryID())) {
					if (HallFragment.listLottery.get(i).getDistanceTime()
							- currentTime_jc <= 0) {
						MyToast.getToast(getApplicationContext(),
								"该奖期已结束，请等下一期").show();
						return;
					}
					AppTools.lottery = HallFragment.listLottery.get(i);

					MyAsynTask2 myAsynTask2 = new MyAsynTask2();
					HallFragment.refreType = true;
					// 对阵信息是否为空
					if (AppTools.lottery.getDtmatch() != null
							&& AppTools.lottery.getDtmatch().length() != 0) {
						if (72 == id
								&& "72".equals(HallFragment.listLottery.get(i)
										.getLotteryID())) {// 竞彩足球
							myAsynTask2.execute(1);
							break;
						} else if (73 == id
								&& "73".equals(HallFragment.listLottery.get(i)
										.getLotteryID())) {// 竞彩篮球
							myAsynTask2.execute(2);
							break;
						}
					} else {
						MyToast.getToast(MyCommonLotteryInfo_jc.this, "没有对阵信息")
								.show();
					}
				}
			}
		}
	}

	/** 异步任务 用来后台获取数据 */
	class MyAsynTask2 extends AsyncTask<Integer, Integer, String> {
		String error = "0";
		ProgressDialog dialog = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialog = BaseHelper.showProgress(MyCommonLotteryInfo_jc.this, null,
					"加载中..", true, false);
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Integer... params) {
			switch (params[0]) {
			case 1:// 足球
				error = new HallFragment().getBallData();
				if (error.equals("0")) {
					error = "100";
				}
				break;
			case 2:// 篮球
				error = new HallFragment().getBasketball();
				if (error.equals("0")) {
					error = "110";
				}
				break;
			default:
				break;
			}
			return error;
		}

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			// TODO Auto-generated method stub
			if ("-500".equals(result)) {
				myHandler.sendEmptyMessage(-500);
				return;
			}
			if (result.equals("100")) {
				// 请求足球完成
				myHandler.sendEmptyMessage(100);
			} else if (result.equals("110")) {
				// 请求篮球完成
				myHandler.sendEmptyMessage(110);
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

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
