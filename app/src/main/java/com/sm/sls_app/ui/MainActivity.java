package com.sm.sls_app.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog.Builder;
import android.app.LocalActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.AppObject;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.fragment.FollowFragment;
import com.sm.sls_app.fragment.HallFragment;
import com.sm.sls_app.fragment.InformationFragment;
import com.sm.sls_app.fragment.MyCenterFragment;
import com.sm.sls_app.fragment.WinLotteryFragment;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.view.ConfirmDialog;

public class MainActivity extends FragmentActivity implements OnClickListener {

	public static Fragment scrollView;

	private ImageView iv_tab_hall, iv_tab_follow, iv_tab_win, iv_tab_info,
			iv_tab_center;// 主页面图标
	private LinearLayout layout_tab_hall, layout_tab_follow, layout_tab_info,
			layout_tab_win, layout_tab_center;// 图标布局

	private TextView tv_tab_hall, tv_tab_follow, tv_tab_win, tv_tab_info,
			tv_tab_center;
	public static LocalActivityManager manager;

	private String opt, auth, info, time, imei, crc; // 格 式化后的参数
	public static int density;
	private String pass;
	private String name;

	private Intent intent;

	public static FragmentTransaction transaction;

	public static LinearLayout ll_main;

	public static MyHandler genxinHandler;

	private UpdateManager mUpdateManager;

	private String downLoadUrl;
	private ConfirmDialog dialog;// 提示框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		App.activityS.add(this);
		getVersion(); // 得到版本号
		updateversions();// 判断执行版本更新
		findView(); // 得到空间
		setListener(); // 设置监听
		MyAsynTaskScale task = new MyAsynTaskScale();
		task.execute();
	}

	/** 初始化UI */
	private void findView() {
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);

		int width = metric.widthPixels; // 宽度（PX）
		int height = metric.heightPixels; // 高度（PX）

		density = (int) metric.density; // 密度（0.75 / 1.0 / 1.5）
		int densityDpi = metric.densityDpi; // 密度DPI（120 / 160 / 240）
		// 图标
		iv_tab_hall = (ImageView) findViewById(R.id.iv_tab_hall);
		iv_tab_follow = (ImageView) findViewById(R.id.iv_tab_follow);
		iv_tab_win = (ImageView) findViewById(R.id.iv_tab_win);
		iv_tab_info = (ImageView) findViewById(R.id.iv_tab_info);
		iv_tab_center = (ImageView) findViewById(R.id.iv_tab_center);

		// 图标布局
		layout_tab_hall = (LinearLayout) findViewById(R.id.layout_tab_hall);
		layout_tab_follow = (LinearLayout) findViewById(R.id.layout_tab_follow);
		layout_tab_info = (LinearLayout) findViewById(R.id.layout_tab_info);
		layout_tab_win = (LinearLayout) findViewById(R.id.layout_tab_win);
		layout_tab_center = (LinearLayout) findViewById(R.id.layout_tab_center);

		// 文字
		tv_tab_hall = (TextView) findViewById(R.id.tv_tab_hall);
		tv_tab_follow = (TextView) findViewById(R.id.tv_tab_follow);
		tv_tab_win = (TextView) findViewById(R.id.tv_tab_win);
		tv_tab_info = (TextView) findViewById(R.id.tv_tab_info);
		tv_tab_center = (TextView) findViewById(R.id.tv_tab_center);

		ll_main = (LinearLayout) this.findViewById(R.id.main);
		System.out.println("----" + AppTools.isShow);
		if (AppTools.isShow)
			ll_main.setVisibility(View.VISIBLE);
		setFocuse();

	}

	/* 得到当前版本号 */
	public void getVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			AppTools.version = info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setFragment() {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.main_center, new HallFragment(), "hall");
		transaction.commitAllowingStateLoss();
		setFocuse();
		iv_tab_hall.setBackgroundResource(R.drawable.log_hall_selected);
		tv_tab_hall.setTextColor(getResources().getColor(R.color.main_red));
	}

	/** 绑定监听 */
	private void setListener() {
		dialog = new ConfirmDialog(this, R.style.dialog);
		layout_tab_hall.setOnClickListener(this);
		layout_tab_follow.setOnClickListener(this);
		layout_tab_win.setOnClickListener(this);
		layout_tab_info.setOnClickListener(this);
		layout_tab_center.setOnClickListener(this);
	}

	/*
	 * 版本检测版本的更新
	 */
	private void updateversions() {

		genxinHandler = new MyHandler();

		String key = MD5.md5(AppTools.MD5_key);

		time = RspBodyBaseBean.getTime();

		info = "{\"identify\":\"android\",\"appversion\":\""
				+ AppTools.getVerName(getApplicationContext()) + "\"}";

		imei = RspBodyBaseBean.getIMEI(this);

		crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");

		auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");

		opt = "0";

		MyAsynTask1 myAsynTask2 = new MyAsynTask1();

		myAsynTask2.execute();

	}

	class MyAsynTask1 extends AsyncTask<Void, Integer, String> {
		/**
		 * 在后台执行的程序 这里请是否可以跟新APP 可以优化使用对象存储返回数据。
		 * 
		 * @param params
		 * @return
		 */
		@Override
		protected String doInBackground(Void... params) {
			String[] values = { opt, auth, info };
			String result = HttpUtils.doPost(AppTools.names, values,
					AppTools.path);
			Log.i("x", "版本更新--->info" + info);
			Log.i("x", "版本更新--->result" + result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				AppTools.appobject.setUpgrade(jsonObject.optString("upgrade"));
				// AppTools.appobject.setApkName(jsonObject.optString("apkName"));
				AppTools.appobject.setVersionCode(jsonObject
						.optString("versionCode"));
				AppTools.appobject.setApkName(jsonObject.optString("apkName"));
				AppTools.appobject.setDownLoadUrl(jsonObject
						.optString("downLoadUrl"));
				AppTools.appobject.setSort(3);// 排序方式
				if (AppTools.appobject.getUpgrade().equals("True")) {
					return "SU";
				} else
					return "False";
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				return "-500";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result.equals("SU")) {
				genxinHandler.sendEmptyMessage(0);
			} else
				genxinHandler.sendEmptyMessage(-1);
		}
	}

	class MyAsynTaskScale extends AsyncTask<Void, Integer, String> {
		/**
		 * 在后台执行的程序 这里请是否可以跟新APP 可以优化使用对象存储返回数据。
		 * 
		 * @param params
		 * @return
		 */
		@Override
		protected String doInBackground(Void... params) {
			String[] values = { "53", auth, "" };
			String result = HttpUtils.doPost(AppTools.names, values,
					AppTools.path);
			Log.i("x", "MyAsynTaskScale--->result" + result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				if ("0".equals(jsonObject.optString("error"))) {
					AppTools.followCommissionScale = jsonObject
							.optString("yongjin"); // 合买佣金比列
					AppTools.followLeastBuyScale = jsonObject
							.optString("rengou"); // 合买最少购买比列
				}
				return "10000";
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				return "-500";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// 这里来检测版本是否需要更新
				mUpdateManager = new UpdateManager(MainActivity.this,
						AppTools.appobject.getDownLoadUrl());
				mUpdateManager.checkUpdateInfo();
				break;
			/* 添加资源彩种 加载fragment */
			case -1:
				if (AppTools.allLotteryLogo == null) {
					AppTools.setLogo();
				}
				// 这里来检测版本是否需要更新
				setFragment();
				break;
			default:

				break;
			}
			super.handleMessage(msg);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		transaction = getSupportFragmentManager().beginTransaction();
		switch (v.getId()) {
		// 购彩大厅
		case R.id.layout_tab_hall:
			setFocuse();
			iv_tab_hall.setBackgroundResource(R.drawable.log_hall_selected);
			tv_tab_hall.setTextColor(getResources().getColor(R.color.main_red));
			transaction.replace(R.id.main_center, new HallFragment(), "hall");
			transaction.commitAllowingStateLoss();
			break;
		// 合买大厅
		case R.id.layout_tab_follow:
			setFocuse();
			iv_tab_follow.setBackgroundResource(R.drawable.log_follow_selected);
			tv_tab_follow.setTextColor(getResources()
					.getColor(R.color.main_red));
			transaction.replace(R.id.main_center, new FollowFragment(),
					"follow");
			transaction.commitAllowingStateLoss();
			break;
		// 开奖公告
		case R.id.layout_tab_win:
			setFocuse();
			iv_tab_win.setBackgroundResource(R.drawable.log_win_selected);
			tv_tab_win.setTextColor(getResources().getColor(R.color.main_red));
			transaction.replace(R.id.main_center, new WinLotteryFragment(),
					"winLottery");
			transaction.commitAllowingStateLoss();
			break;
		// 彩票资讯
		case R.id.layout_tab_info:
			setFocuse();
			iv_tab_info.setBackgroundResource(R.drawable.log_info_selected);
			tv_tab_info.setTextColor(getResources().getColor(R.color.main_red));
			transaction.replace(R.id.main_center, new InformationFragment(),
					"infomation");
			transaction.commitAllowingStateLoss();
			break;
		// 个人中心
		case R.id.layout_tab_center:
			setFocuse();
			iv_tab_center.setBackgroundResource(R.drawable.log_center_selected);
			tv_tab_center.setTextColor(getResources()
					.getColor(R.color.main_red));
			ToMyCenterActivity();
			break;
		}
	}

	/** 重写返回键事件 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			dialog.show();
			dialog.setDialogContent("您确认退出" + getText(R.string.app_name)
					+ "系统？");
			dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
				@Override
				public void getResult(int resultCode) {
					// TODO Auto-generated method stub
					if (1 == resultCode) {// 确定
						ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
						manager.restartPackage(getPackageName());
						for (Activity activity : App.activityS) {
							activity.finish();
						}
						MainActivity.this.finish();
						android.os.Process.killProcess(android.os.Process
								.myPid());
					}
				}
			});
		}
		return super.onKeyDown(keyCode, event);
	}

	/** 点击个人中心 */
	private void ToMyCenterActivity() {
		if (null == AppTools.user) {
			intent = new Intent(MainActivity.this, LoginActivity.class);
			MainActivity.this.startActivity(intent);
		} else {
			transaction.replace(R.id.main_center, new MyCenterFragment(),
					"center");
			transaction.commitAllowingStateLoss();
		}
	}

	public static void toCenter() {
		try {
			transaction.replace(R.id.main_center, new MyCenterFragment(),
					"center");
			transaction.commitAllowingStateLoss();
		} catch (Exception e) {
			Log.i("x", "登陆错误" + e.getMessage());
		}
	}

	/** 设置未选中的状态 */
	private void setFocuse() {
		iv_tab_hall.setBackgroundResource(R.drawable.log_hall_unselected);
		tv_tab_hall.setTextColor(getResources().getColor(R.color.main_gray));
		iv_tab_follow.setBackgroundResource(R.drawable.log_follow_unselected);
		tv_tab_follow.setTextColor(getResources().getColor(R.color.main_gray));
		iv_tab_win.setBackgroundResource(R.drawable.log_win_unselected);
		tv_tab_win.setTextColor(getResources().getColor(R.color.main_gray));
		iv_tab_info.setBackgroundResource(R.drawable.log_info_unselected);
		tv_tab_info.setTextColor(getResources().getColor(R.color.main_gray));
		iv_tab_center.setBackgroundResource(R.drawable.log_center_unselected);
		tv_tab_center.setTextColor(getResources().getColor(R.color.main_gray));
	}

	@Override
	protected void onDestroy() {
		App.activityS.remove(this);
		super.onDestroy();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		setFragment();
		super.onNewIntent(intent);
	}

	public void update() {
		Fragment fragment = getSupportFragmentManager().findFragmentById(
				R.id.main_center);
		if (fragment instanceof HallFragment)
			((HallFragment) fragment).update();
	}

}
