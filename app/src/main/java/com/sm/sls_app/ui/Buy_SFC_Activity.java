package com.sm.sls_app.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.TeamArray;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.ui.adapter.SFC_TeamArrayAdapter;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.NetWork;
import com.sm.sls_app.view.ConfirmDialog;
import com.sm.sls_app.view.VibratorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 胜负彩 的选球页面 功能：选号界面，实现选号功能 版本
 * 
 * @author Administrator
 */
public class Buy_SFC_Activity extends Activity implements OnClickListener {
	private final static String TAG = "Buy_SFC_Activity";

	/* 头部 */
	private RelativeLayout layout_top_select;// 顶部布局
	private ImageButton btn_back; // 返回
	private LinearLayout layout_select_playtype;// 玩法选择
	private ImageView iv_up_down;// 玩法提示图标
	private Button btn_playtype;// 玩法
	private ImageButton btn_help;// 帮助
	private ConfirmDialog dialog;// 提示框

	/* 尾部 */
	private Button btn_clearall; // 清除全部
	private Button btn_submit; // 选好了
	public TextView tv_tatol_count;// 总注数
	public TextView tv_tatol_money;// 总钱数

	private Bundle bundle;

	private int type = 1;

	private ListView listView;
	private SFC_TeamArrayAdapter Adapter;
	private List<TeamArray> lisTeamArrays = new ArrayList<TeamArray>();
	private MyAsynTask myAsynTask;
	private Context context;

	/** 要更改的 **/
	private LinearLayout ll;
	private ProgressBar pb;

	private String opt = "10"; // 格式化后的 opt

	private String auth, info, time, imei, crc; // 格式化后的参数
	
	private Intent intent;
	private MyHandler myHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_buy_rx9);
		App.activityS.add(this);
		App.activityS1.add(this);
		context = Buy_SFC_Activity.this;
		clearHashSet();
		findView();
		init();
		setListener();
	}

	/** 初始化UI */
	private void findView() {
		bundle = new Bundle();
		myHandler = new MyHandler();
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_playtype = (Button) findViewById(R.id.btn_playtype);
		btn_help = (ImageButton) findViewById(R.id.btn_help);
		iv_up_down = (ImageView) findViewById(R.id.iv_up_down);
		layout_select_playtype = (LinearLayout) findViewById(R.id.layout_select_playtype);
		btn_clearall = (Button) findViewById(R.id.btn_clearall);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		tv_tatol_count = (TextView) this.findViewById(R.id.tv_tatol_count);
		tv_tatol_money = (TextView) this.findViewById(R.id.tv_tatol_money);
		layout_top_select = (RelativeLayout) findViewById(R.id.layout_top_select);
		listView = (ListView) findViewById(R.id.bet_lv_nums);
		Adapter = new SFC_TeamArrayAdapter(Buy_SFC_Activity.this,
				lisTeamArrays,
				VibratorView.getVibrator(getApplicationContext()));

		/** 要更改的 新加的加载图片 **/
		ll = new LinearLayout(this);
		pb = new ProgressBar(this);
		ll.setGravity(Gravity.CENTER);
		ll.addView(pb);
		/** 要加在 setAdapter之前 **/
		listView.addFooterView(ll);
		// 隐藏和显示
		iv_up_down.setVisibility(View.GONE);
		dialog=new ConfirmDialog(this, R.style.dialog);

	}

	/** 初始化属性 上期开奖号码 */
	private void init() {
		btn_playtype.setText("胜负彩");
		if (NetWork.isConnect(context))
		{
			myAsynTask = new MyAsynTask();
			myAsynTask.execute();
		} 
		else 
		{
			Toast.makeText(context, "网络连接异常，请检查网络", Toast.LENGTH_SHORT).show();
		}
	}

	/** 绑定监听 */
	private void setListener() {
		btn_back.setOnClickListener(this);
		btn_help.setOnClickListener(this);
		btn_clearall.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		listView.setAdapter(Adapter);
	}

	/** 公共点击监听 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/** 返回 **/
		case R.id.btn_back:
			exit();
			break;
		/** 提交号码 **/
		case R.id.btn_submit:
			submitNumber();
			break;
		/** 清空 **/
		case R.id.btn_clearall:
			clear();
			break;
		/** 玩法说明 **/
		case R.id.btn_help:
			playExplain();
			break;
		}
	}

	/** 从投注页面跳转过来 将投注页面的值 显示出来 */
	public void getItem() {
		Intent intent = Buy_SFC_Activity.this.getIntent();
		Bundle bundle = intent.getBundleExtra("bundle");
		if (null != bundle) 
		{ 
			List<String> list_key = new ArrayList<String>();
			List<String> list_value = new ArrayList<String>();
			list_key = bundle.getStringArrayList("Key");
			list_value = bundle.getStringArrayList("Value");
			for (int i = 0; list_key.size() > i; i++) 
			{
				SFC_TeamArrayAdapter.btnMap.put(
						Integer.parseInt(list_key.get(i)), list_value.get(i));
			}
		}
		updateAdapter();
	}

	/** 玩法说明 */
	private void playExplain() {
		Intent intent = new Intent(Buy_SFC_Activity.this, PlayDescription.class);
		Buy_SFC_Activity.this.startActivity(intent);
	}

	/** 提交号码 */
	private void submitNumber() {
		if (SFC_TeamArrayAdapter.btnMap.size() < 14)
		{
			Toast.makeText(Buy_SFC_Activity.this, "请至少选择一注", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		Intent intent = new Intent(Buy_SFC_Activity.this,
				Bet_SFC_Activity.class);
		intent.putExtra("lotteryBundle", bundle);
		Buy_SFC_Activity.this.startActivity(intent);

	}

	/** 清空 */
	private void clear() {
		clearHashSet();
		updateAdapter();
		AppTools.totalCount = 0;
	}

	/** 重写返回键事件 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK)
			exit();
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {

		if (AppTools.list_numbers == null
				|| (AppTools.list_numbers != null && AppTools.list_numbers
						.size() == 0)) {
			if (SFC_TeamArrayAdapter.btnMap.size() != 0)

			{
				dialog.show();
				dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
					@Override
					public void getResult(int resultCode) {
						// TODO Auto-generated method stub
						if (1 == resultCode) {// 确定
							Buy_SFC_Activity.this.clearHashSet();
							for (int i = 0; i < App.activityS1.size(); i++) {
								App.activityS1.get(i).finish();
							}
						}
					}
				});

			} else {
				Buy_SFC_Activity.this.clearHashSet();
				for (int i = 0; i < App.activityS1.size(); i++) {
					App.activityS1.get(i).finish();
				}
			}
		} else {
			if (SFC_TeamArrayAdapter.btnMap.size() != 0) {

				dialog.show();
				dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
					@Override
					public void getResult(int resultCode) {
						// TODO Auto-generated method stub
						if (1 == resultCode) {// 确定
							Buy_SFC_Activity.this.clearHashSet();
							Intent intent = new Intent(Buy_SFC_Activity.this,
									Bet_RX9_Activity.class);
							Buy_SFC_Activity.this.startActivity(intent);
							Buy_SFC_Activity.this.finish();
						}
					}
				});
			} else {
				Buy_SFC_Activity.this.clearHashSet();
				Intent intent = new Intent(Buy_SFC_Activity.this,
						Bet_RX9_Activity.class);
				Buy_SFC_Activity.this.startActivity(intent);
				Buy_SFC_Activity.this.finish();
			}
		}
	}

	/** 刷新Adapter */
	public void updateAdapter() {
		if (null == Adapter || null == tv_tatol_count || null == tv_tatol_money)
			return;
		Adapter.notifyDataSetChanged();
		setTextShow();
	}
	
	public void setTextShow(){
		tv_tatol_count.setText(AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}

	/** 清空选中情况 */
	public static  void clearHashSet() {
		if(null!=SFC_TeamArrayAdapter.btnMap){
			SFC_TeamArrayAdapter.btnMap.clear();
		}
	}

	@Override
	protected void onDestroy() {
		App.activityS.remove(this);
		super.onDestroy();
	}

	private List<TeamArray> getTeamData() {
		if (lisTeamArrays == null)
			lisTeamArrays = new ArrayList<TeamArray>();

		if (lisTeamArrays.size() == 0) {
			String key = MD5.md5(AppTools.MD5_key);
			info = RspBodyBaseBean.changeLottery_infoAgainst("75");

			crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");
			auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");
			String[] values = { opt, auth, info };
			Log.i(TAG, "info----"+info);
			
			String result = HttpUtils.doPost(AppTools.names, values,
					AppTools.path);
			Log.i(TAG, "result----"+result);

			try {
				JSONObject isusesInfo = new JSONObject(result);

				AppTools.serverTime = isusesInfo.optString("serverTime");

				JSONArray array = new JSONArray(
						isusesInfo.getString("isusesInfo"));
				for (int i = 0; i < 1; i++) {
					JSONObject object = array.getJSONObject(i);

					if (null != object) {
						JSONArray arrayAgainst = new JSONArray(
								object.getString("dtMatch"));
						TeamArray team = null;
						for (int j = 0; j < arrayAgainst.length(); j++) {
							JSONObject objectAgainst = arrayAgainst
									.getJSONObject(j);
							team = new TeamArray();
//							team.setId(objectAgainst.optString("no"));
							team.setId(objectAgainst.optString("matchnumber"));
							team.setGame(objectAgainst.optString("game"));
							team.setTime(objectAgainst.optString("startdatetime"));
							team.setGuestTeam(objectAgainst
									.optString("questTeam"));
							team.setMainTeam(objectAgainst
									.optString("hostTeam"));
							lisTeamArrays.add(team);
						}
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d("HallActivity", "得到胜负彩对阵数据出错：" + e.getMessage());
			}

		}
		return null;
	}

	/** 异步任务 用来后台获取数据 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> {
		/** 在后台执行的程序 */
		String error = "-1001";

		@Override
		protected String doInBackground(Void... params)
		{
			if (lisTeamArrays == null)
				lisTeamArrays = new ArrayList<TeamArray>();

			if (lisTeamArrays.size() == 0) 
			{
				String key = MD5.md5(AppTools.MD5_key);
				info = RspBodyBaseBean
						.changeLottery_infoAgainst("74");

				crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");
				auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");
				String[] values = { opt, auth, info };

				String result = HttpUtils.doPost(AppTools.names, values,AppTools.path);
				
				Log.i("x", "胜负彩的结果   "+result);

				if (result.length() == 0) 
				{
					return "-1001";
				}
				try
				{
					JSONObject isusesInfo = new JSONObject(result);
					error = isusesInfo.getString("error");
					AppTools.serverTime = isusesInfo.optString("serverTime");

					JSONArray array = new JSONArray(
							isusesInfo.getString("isusesInfo"));

					for (int i = 0; i < array.length(); i++) 
					{
						if(null ==  array.get(i) || array.get(i).toString().length() < 5 )
							continue;

						JSONObject object = array.getJSONObject(i);

						if (null != object) 
						{
							JSONArray arrayAgainst = new JSONArray(
									object.getString("dtMatch"));

							TeamArray team = null;
							for (int j = 0; j < arrayAgainst.length(); j++) 
							{
								JSONObject objectAgainst = arrayAgainst
										.getJSONObject(j);
								team = new TeamArray();
                                team.setId(objectAgainst.optString("matchnumber"));
                                team.setGame(objectAgainst.optString("game"));
                                team.setTime(objectAgainst.optString("datetime"));
                                team.setGuestTeam(objectAgainst
                                        .optString("questTeam"));
                                team.setMainTeam(objectAgainst
                                        .optString("hostTeam"));
                                team.setMatchDate(objectAgainst.optString("startdatetime"));
									lisTeamArrays.add(team);
							}
						}
					}
				} 
				catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					error = "-1001";
					Log.d("x", "得到胜负彩对阵数据出错：" + e.getMessage());
				}
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
			switch (msg.what) 
			{
			case AppTools.ERROR_SUCCESS:
				Adapter = new SFC_TeamArrayAdapter(Buy_SFC_Activity.this,
						lisTeamArrays,VibratorView.getVibrator(getApplicationContext()));
				listView.removeFooterView(ll);
				ll.setVisibility(View.GONE);
				listView.setAdapter(Adapter);
				break;
			case -1:
				Toast.makeText(Buy_SFC_Activity.this, "系统异常.请稍后再试。",
						Toast.LENGTH_SHORT).show();
				// 结束所有的跳到主页面
				List<Activity> as = App.activityS;
				for (int i = 0; i < as.size(); i++) 
				{
					as.get(i).finish();
				}
				intent = new Intent(Buy_SFC_Activity.this, MainActivity.class);
				Buy_SFC_Activity.this.startActivity(intent);
				break;
				default:
					break;
			}
			super.handleMessage(msg);
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		listView.scrollTo(0, 0);
		getItem();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		super.onNewIntent(intent);
	}

}
