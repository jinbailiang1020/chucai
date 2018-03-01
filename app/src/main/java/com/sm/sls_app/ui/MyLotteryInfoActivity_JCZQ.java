package com.sm.sls_app.ui;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.Schemes;
import com.sm.sls_app.dataaccess.ShowDtMatch;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.view.MyToast;

/** 彩票 竞彩订单详情 */
public class MyLotteryInfoActivity_JCZQ extends Activity {

	private TextView tv_playName, tv_schemeId, tv_schemeMoney, tv_showLottery,
			tv_time, tv_lotteryMoney, tv_userName, tv_yong, tv_totalMoney,
			tv_shareMoney,tv_myMoney,tv_myShare;

	private ListView listView;
	private MyAdapter adapter;

	private List<ShowDtMatch> list_show;

	private String opt = "46"; // 格式化后的 opt

	private String auth, info, time, imei, crc; // 格式化后的参数

	private ImageView img_logo;
	private Schemes scheme;

	private LinearLayout ll_join,ll_join2,ll_title,ll_content;

	private MyAsynTask myAsynTask;
	private MyHandler myHandler;
	private ScrollView sv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.orderinfo_jczq);
		sv = (ScrollView) this.findViewById(R.id.scrollView);
		sv.setVisibility(View.GONE);
		init();
	}

	/** 初始化UI */
	private void findView()
	{

		listView = (ListView) this.findViewById(R.id.orderinfo_jc_listView);

		img_logo = (ImageView) this.findViewById(R.id.img_logo);

		tv_playName = (TextView) this.findViewById(R.id.orderinfo_top_tv_name);
		tv_schemeId = (TextView) this.findViewById(R.id.tv_orderNumber);
		tv_schemeMoney = (TextView) this
				.findViewById(R.id.orderinfo_top_tv_money);
		tv_showLottery = (TextView) this
				.findViewById(R.id.orderinfo_center_tv_winning);
		tv_time = (TextView) this.findViewById(R.id.right_bottom_tv_time);
		tv_lotteryMoney = (TextView) this.findViewById(R.id.tv_win_money2);

		tv_userName = (TextView) this.findViewById(R.id.center_tv_userName);
		tv_yong = (TextView) this.findViewById(R.id.center_tv_yong);
		tv_totalMoney = (TextView) this
				.findViewById(R.id.center_tv_schemeMoney);
		tv_shareMoney = (TextView) this.findViewById(R.id.center_tv_shareMoney);
		
		tv_myMoney = (TextView) this.findViewById(R.id.tv_my_money);
		tv_myShare = (TextView) this.findViewById(R.id.tv_my_share);

		ll_join = (LinearLayout) this.findViewById(R.id.win_num_center);
		ll_join2 = (LinearLayout) this.findViewById(R.id.orderinfo_info);
		ll_title = (LinearLayout) this.findViewById(R.id.orderinfo_schemeTitle);
		ll_content = (LinearLayout) this.findViewById(R.id.orderinfo_schemeDetail);

	}

	/** 初始化 */
	private void init() 
	{
		scheme = (Schemes) getIntent().getSerializableExtra("schemes");
		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(MyLotteryInfoActivity_JCZQ.this);
		myHandler = new MyHandler();
		myAsynTask = new MyAsynTask();
		myAsynTask.execute();
	}

	/** 给控件赋值 **/
	private void setView() 
	{
		Log.i("x", "购买类型" + scheme.getIsPurchasing()+"彩种ID"+scheme.getLotteryID()+"玩法"+scheme.getPlayTypeName());
		img_logo.setBackgroundResource(AppTools.allLotteryLogo.get(scheme
				.getLotteryID()));
		
		tv_playName.setText(scheme.getPlayTypeName());

		tv_schemeId.setText("订单号:" + scheme.getSchemeNumber());
		tv_schemeMoney.setText("" + scheme.getMoney() + "元");
		
		// 合买
		if ("False".equals(scheme.getIsPurchasing()))
		{
			ll_join.setVisibility(View.VISIBLE);
			ll_join2.setVisibility(View.VISIBLE);
			
			ll_title.setVisibility(View.VISIBLE);
			ll_content.setVisibility(View.VISIBLE);

			tv_userName.setText(scheme.getInitiateName());
			tv_yong.setText(scheme.getSchemeBonusScale() + "%");
			tv_totalMoney.setText(scheme.getMoney() + "元");
			tv_shareMoney.setText(scheme.getShareMoney() + "元");
			
			tv_myShare.setText("认购份数:"+scheme.getMyBuyShare());
			tv_myMoney.setText("认购金额:"+scheme.getMyBuyMoney()+"元");

		}
		else//代购
		{
			tv_myShare.setVisibility(View.GONE);
			tv_myMoney.setVisibility(View.GONE);
		}
		
		if ("True".equals(scheme.getSchemeIsOpened())) 
		{
			if (scheme.getWinMoneyNoWithTax() > 0)
			{
				tv_showLottery.setText("中奖" + scheme.getWinMoneyNoWithTax()
						+ "元");
				tv_lotteryMoney.setText(scheme.getWinMoneyNoWithTax() + "元");
			} 
			else 
			{
				tv_showLottery.setText("未中奖");
			}
		}
		else 
		{
			tv_showLottery.setText("未开奖");
		}

		tv_time.setText("" + scheme.getDateTime());

	}

	/** 绑定监听 */
	private void setListener()
	{
		adapter = new MyAdapter(this);
		listView.setAdapter(adapter);
		sv.smoothScrollTo(0, 20);
		AppTools.setHight(adapter, listView);
	}

	/*** 异步任务 用来后台获取数据 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> 
	{
		String error = "0";

		@Override
		protected String doInBackground(Void... params)
		{
			String key = MD5
					.md5(AppTools.user.getUserPass() + AppTools.MD5_key);

			info = RspBodyBaseBean.changeJC_info(scheme.getId() + "");
			crc = RspBodyBaseBean.getCrc(time, imei, key, info,
					AppTools.user.getUid());
			auth = RspBodyBaseBean.getAuth(crc, time, imei, AppTools.user.getUid());

			String values[] = { opt, auth, info };
			String result = HttpUtils.doPost(AppTools.names, values,AppTools.path);

			if("-500".equals(result))
				return result;
			
			if (result.length() == 0) 
			{
				// Log.i("x", "没有得到数据--！");
				return "-1";
			}
			try 
			{
				JSONObject object = new JSONObject(result);

				error = object.getString("error");
				if ("0".equals(object.getString("error"))) 
				{
					String s = object.optString("informationId");
					JSONArray array = new JSONArray(s);
					if (array.length() == 0)
						return "-1";
					list_show = new ArrayList<ShowDtMatch>();
					for (int i = 0; i < array.length(); i++) 
					{
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

						String[] st = investContent.split(",");

						String[] select = new String[st.length];
						double[] odds = new double[st.length];

						for (int j = 0; j < st.length; j++) 
						{
							String[] st2 = st[j].split("-");
							Log.i("x", "选的结果---" + st2[0]);
							select[j] = st2[0];
							Log.i("x", "赔率---" + st2[1]);
							odds[j] = Double.parseDouble(st2[1]);
						}
						dt.setSelect(select);
						dt.setOdds(odds);
						Log.i("x", "11111--select----" + select[0]);

						list_show.add(dt);
						Log.i("x", "长度----listShow---" + list_show.size());
					}
				}
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("x", "拿对阵报错--->" + e.getMessage());
				error = "-1";
			}
			return error;
		}

		@Override
		protected void onPostExecute(String result) 
		{
			myHandler.sendEmptyMessage(Integer.parseInt(result));
			super.onPostExecute(result);
		}
	}

	/** 处理页面显示的 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler 
	{
		@Override
		public void handleMessage(Message msg) 
		{
			System.out.println("先接受消息");
			switch (msg.what)
			{
			case -1:
				MyToast.getToast(getApplicationContext(), "没有数据").show();
				break;
			case 0:
				findView();
				setView();
				setListener();
				break;
			}
			sv.setVisibility(View.VISIBLE);
		}
	}

	/** Adapter **/
	class MyAdapter extends BaseAdapter 
	{

		private Context context;

		public MyAdapter(Context _context) 
		{
			this.context = _context;
		}

		@Override
		public int getCount() 
		{
			// TODO Auto-generated method stub
			return list_show.size();
		}

		@Override
		public Object getItem(int arg0) 
		{
			// TODO Auto-generated method stub
			return list_show.get(arg0);
		}

		@Override
		public long getItemId(int arg0) 
		{
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) 
		{
			ViewHolder holder;
			ShowDtMatch showDtmatch = list_show.get(position);
			if (null == view) 
			{
				holder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(context);
				view = inflater.inflate(R.layout.orderinfo_jc_item, null);
				holder.tv_dan = (TextView) view.findViewById(R.id.tv_dan);
				holder.tv_mainTeam = (TextView) view
						.findViewById(R.id.tv_mainTeam);
				holder.tv_loseBall = (TextView) view.findViewById(R.id.tv_vs);
				holder.tv_gusTeam = (TextView) view
						.findViewById(R.id.tv_gusTeam);
				holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
				holder.tv_win = (TextView) view.findViewById(R.id.tv_win);
				holder.tv_flat = (TextView) view.findViewById(R.id.tv_flat);
				holder.tv_lose = (TextView) view.findViewById(R.id.tv_lose);
				holder.tv_scroe = (TextView) view.findViewById(R.id.tv_scroe);
				holder.tv_result = (TextView) view.findViewById(R.id.tv_result);
				holder.tv_title = (TextView) view
						.findViewById(R.id.tv_schemeTitle2);
				holder.tv_content = (TextView) view
						.findViewById(R.id.tv_schemeDetail2);

				holder.tv_zjq_result = (TextView) view
						.findViewById(R.id.tv_zjq_result);
				holder.tv_cbf_result = (TextView) view
						.findViewById(R.id.tv_cbf_result);

				holder.tv_sf_win = (TextView) view.findViewById(R.id.tv_sf_win);
				holder.tv_sf_lose = (TextView) view
						.findViewById(R.id.tv_sf_lose);

				holder.ll_spf = (LinearLayout) view.findViewById(R.id.ll_spf);
				holder.ll_zjq = (LinearLayout) view.findViewById(R.id.ll_zjq);
				holder.ll_cbf = (LinearLayout) view.findViewById(R.id.ll_cbf);

				holder.ll_sf = (LinearLayout) view.findViewById(R.id.ll_sf);

				view.setTag(holder);
			} 
			else
			{
				holder = (ViewHolder) view.getTag();
			}

			holder.tv_dan.setText(showDtmatch.getLetBile() == 0 ? "胆" : " ");
			holder.tv_mainTeam.setText(showDtmatch.getMainTeam());
			holder.tv_gusTeam.setText(showDtmatch.getGuestTeam());
			holder.tv_loseBall.setText("VS");
			holder.tv_scroe.setText(showDtmatch.getScore());
			
			holder.tv_time.setText(showDtmatch.getMatchNumber().trim()
					.substring(0, 2)
					+ " " + showDtmatch.getMatchNumber().substring(2, 5));

			switch (showDtmatch.getPlayType()) 
			{
			case 7201:
			case 7207:
				// 给控件赋值
				changeVisible(holder, holder.ll_spf);

				holder.tv_win.setBackgroundColor(Color.WHITE);
				holder.tv_win.setTextColor(Color.BLACK);
				holder.tv_flat.setBackgroundColor(Color.WHITE);
				holder.tv_flat.setTextColor(Color.BLACK);
				holder.tv_lose.setBackgroundColor(Color.WHITE);
				holder.tv_lose.setTextColor(Color.BLACK);
				for (int i = 0; i < showDtmatch.getSelect().length; i++) 
				{
					if ("胜".equals(showDtmatch.getSelect()[i])) 
					{
						holder.tv_win.setText(showDtmatch.getSelect()[i] + " "
								+ showDtmatch.getOdds()[i]);
						holder.tv_win.setBackgroundColor(Color.RED);
						holder.tv_win.setTextColor(Color.WHITE);
					}
					if ("负".equals(showDtmatch.getSelect()[i])) 
					{
						holder.tv_lose.setText(showDtmatch.getSelect()[i] + " "
								+ showDtmatch.getOdds()[i]);
						holder.tv_lose.setBackgroundColor(Color.RED);
						holder.tv_lose.setTextColor(Color.WHITE);
					}
					if ("平".equals(showDtmatch.getSelect()[i])) 
					{
						holder.tv_flat.setText(showDtmatch.getSelect()[i] + " "
								+ showDtmatch.getOdds()[i]);
						holder.tv_flat.setBackgroundColor(Color.RED);
						holder.tv_flat.setTextColor(Color.WHITE);
					}
				}
				break;
			case 7202:
				changeVisible(holder, holder.ll_cbf);
				String str2 = "";
				for (int i = 0; i < showDtmatch.getSelect().length; i++) 
				{
					if (i == 0)
						str2 = showDtmatch.getSelect()[i];
					else
						str2 += "," + showDtmatch.getSelect()[i];
				}
				holder.tv_cbf_result.setText(str2);
				break;
			case 7203:
				changeVisible(holder, holder.ll_zjq);
				String str = "";
				for (int i = 0; i < showDtmatch.getSelect().length; i++) 
				{
					if (i == 0)
						str = showDtmatch.getSelect()[i];
					else
						str += "," + showDtmatch.getSelect()[i];
				}
				Log.i("x", "总进球---" + str);
				holder.tv_zjq_result.setText(str);
				break;
			case 7301:
				changeVisible(holder, holder.ll_sf);

				holder.tv_sf_win.setBackgroundColor(Color.WHITE);
				holder.tv_sf_win.setTextColor(Color.BLACK);
				holder.tv_sf_lose.setBackgroundColor(Color.WHITE);
				holder.tv_sf_lose.setTextColor(Color.BLACK);

				holder.tv_sf_win.setText("主胜");
				holder.tv_sf_lose.setText("主负");

				for (int i = 0; i < showDtmatch.getSelect().length; i++) 
				{
					if ("主胜".equals(showDtmatch.getSelect()[i]))
					{
						holder.tv_sf_win.setText(showDtmatch.getSelect()[i]
								+ " " + showDtmatch.getOdds()[i]);
						holder.tv_sf_win.setBackgroundColor(Color.RED);
						holder.tv_sf_win.setTextColor(Color.WHITE);
					}
					if ("主负".equals(showDtmatch.getSelect()[i]))
					{
						holder.tv_sf_lose.setText(showDtmatch.getSelect()[i]
								+ " " + showDtmatch.getOdds()[i]);
						holder.tv_sf_lose.setBackgroundColor(Color.RED);
						holder.tv_sf_lose.setTextColor(Color.WHITE);
					}
				}
				break;
			case 7304:
				changeVisible(holder, holder.ll_sf);
				holder.tv_sf_win.setBackgroundColor(Color.WHITE);
				holder.tv_sf_win.setTextColor(Color.BLACK);
				holder.tv_sf_lose.setBackgroundColor(Color.WHITE);
				holder.tv_sf_lose.setTextColor(Color.BLACK);
				holder.tv_sf_win.setText("大");
				holder.tv_sf_lose.setText("小");

				for (int i = 0; i < showDtmatch.getSelect().length; i++) 
				{
					if ("大".equals(showDtmatch.getSelect()[i])) 
					{
						holder.tv_sf_win.setText(showDtmatch.getSelect()[i]
								+ " " + showDtmatch.getOdds()[i]);
						holder.tv_sf_win.setBackgroundColor(Color.RED);
						holder.tv_sf_win.setTextColor(Color.WHITE);
					}
					if ("小".equals(showDtmatch.getSelect()[i])) 
					{
						holder.tv_sf_lose.setText(showDtmatch.getSelect()[i]
								+ " " + showDtmatch.getOdds()[i]);
						holder.tv_sf_lose.setBackgroundColor(Color.RED);
						holder.tv_sf_lose.setTextColor(Color.WHITE);
					}
				}
				break;
			case -500:
				MyToast.getToast(MyLotteryInfoActivity_JCZQ.this, "连接超时").show();
				break;
			default:
				break;
			}
			holder.tv_scroe.setText(showDtmatch.getScore());
			holder.tv_result.setText(showDtmatch.getResult());
			return view;
		}
	}

	private void changeVisible(ViewHolder holder, LinearLayout ll) 
	{
		holder.ll_spf.setVisibility(View.GONE);
		holder.ll_cbf.setVisibility(View.GONE);
		holder.ll_zjq.setVisibility(View.GONE);
		holder.ll_sf.setVisibility(View.GONE);

		ll.setVisibility(View.VISIBLE);
	}

	static class ViewHolder
	{
		TextView tv_dan, tv_mainTeam, tv_gusTeam, tv_loseBall, tv_scroe,
				tv_time, tv_win, tv_flat, tv_lose, tv_result, tv_title,
				tv_content;

		TextView tv_zjq_result;
		TextView tv_cbf_result;
		TextView tv_sf_win, tv_sf_lose;

		LinearLayout ll_spf, ll_cbf, ll_zjq, ll_sf;
	}

}
