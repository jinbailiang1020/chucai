package com.sm.sls_app.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.MyMessage;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.utils.AppTools;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SmsHandler;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 设置
 * 
 * @author SLS003
 */
public class SettingActivity extends Activity implements OnClickListener {
	private Context context = SettingActivity.this;
	private RelativeLayout rl_msg, rl_suggest, rl_share;
	private ImageButton btn_random, btn_vibrator, btn_back;
	private Button setting_exit;
	private String opt = "16"; // 格式化后的 opt
	private String auth, info, time, imei, crc; // 格式化后的参数

	private TextView tv_count;

	private MyAsynTask myAsynTask;
	private MyHandler myHandler;

	private SharedPreferences settings;
	private Editor editor;
	private long count = 0;
	private UMSocialService umSocialService;
	private static final String SHARE_PAGE = "com.umeng.share";
	private static final String SHARE_CONTENT = "我使用了umeng分享接口。。。";
	private static final String QZ_APPID = "100424468";
	private static final String QZ_APPKEY = "c7394704798a158208a74ab60104f0ba";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_appsetting);
		findView();
		initBtn();
		setListener();
	}

	private void findView() {

		// 设置新浪SSO handler
		umSocialService = UMServiceFactory.getUMSocialService(SHARE_PAGE);
		QZoneSsoHandler qz = new QZoneSsoHandler(this, QZ_APPID, QZ_APPKEY);
		qz.addToSocialSDK();
		SmsHandler sms = new SmsHandler();
		sms.addToSocialSDK();
		EmailHandler email = new EmailHandler();
		email.addToSocialSDK();
		umSocialService.setShareContent("我使用了快速分享接口（UMServiceFactory.share）");
		umSocialService.setShareContent(SHARE_CONTENT);
		myHandler = new MyHandler();
		rl_msg = (RelativeLayout) this.findViewById(R.id.setting_ll_message);
		rl_suggest = (RelativeLayout) this
				.findViewById(R.id.setting_ll_suggest);
		rl_share = (RelativeLayout) this.findViewById(R.id.setting_ll_share);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		tv_count = (TextView) this.findViewById(R.id.settring_tv_msgCount);
		btn_random = (ImageButton) this.findViewById(R.id.setting_btn_random);
		btn_vibrator = (ImageButton) this
				.findViewById(R.id.setting_btn_vibrator);
		setting_exit = (Button) findViewById(R.id.setting_exit);

		settings = getSharedPreferences("app_user", 0);
		editor = settings.edit();

	}

	private void initBtn() {
		if (settings.contains("isSensor")) {
			AppTools.isSensor = settings.getBoolean("isSensor", true);
		}
		if (settings.contains("isVibrator")) {
			AppTools.isVibrator = settings.getBoolean("isVibrator", true);
		}

		if (!AppTools.isSensor)
			btn_random.setSelected(false);
		else
			btn_random.setSelected(true);

		if (!AppTools.isVibrator)
			btn_vibrator.setSelected(false);
		else
			btn_vibrator.setSelected(true);
	}

	private void setListener() {
		// TODO Auto-generated method stub
		rl_suggest.setOnClickListener(this);
		rl_msg.setOnClickListener(this);
		rl_share.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		btn_random.setOnClickListener(this);
		btn_vibrator.setOnClickListener(this);
		setting_exit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_ll_message:
			if (null == AppTools.user) {
				Intent intent = new Intent(SettingActivity.this,
						LoginActivity.class);
				intent.putExtra("loginType", "msg");
				SettingActivity.this.startActivity(intent);
			} else {
				toMessage();
			}
			break;
		case R.id.setting_ll_suggest:
			Intent intent = new Intent(SettingActivity.this,
					SuggestActivity.class);
			SettingActivity.this.startActivity(intent);
			break;
		case R.id.setting_ll_share:

			umSocialService.openShare(SettingActivity.this, false);
			break;
		/** 全程关闭打开摇一摇 **/
		case R.id.setting_btn_random:
			AppTools.isSensor = !AppTools.isSensor;
			editor.putBoolean("isSensor", AppTools.isSensor);
			editor.commit();
			setBtnText(btn_random, AppTools.isSensor);
			break;
		/** 全程关闭打开震动 **/
		case R.id.setting_btn_vibrator:
			AppTools.isVibrator = !AppTools.isVibrator;
			editor.putBoolean("isVibrator", AppTools.isVibrator);
			editor.commit();
			setBtnText(btn_vibrator, AppTools.isVibrator);
			break;
		case R.id.setting_exit:
			exitLogin();
			break;
		case R.id.btn_back:
			finish();
			break;
		}
	}

	private void setBtnText(ImageButton btn, boolean isOpen) {
		if (isOpen)
			btn.setSelected(true);
		else
			btn.setSelected(false);
	}

	private void toMessage() {
		Intent intent = new Intent(SettingActivity.this,
				StationMessageActivity.class);
		SettingActivity.this.startActivity(intent);
	}

	/*** 异步任务 用来后台获取数据 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> {
		/** 在后台执行的程序 */
		@Override
		protected String doInBackground(Void... params) {
			String error = "-1";
			String key = MD5
					.md5(AppTools.user.getUserPass()==null?"":AppTools.user.getUserPass() + AppTools.MD5_key);
			info = RspBodyBaseBean.changeMsg_info(-1, 1, 10, 0);

			Log.i("x", "info-----" + info);
			crc = RspBodyBaseBean.getCrc(time, imei, key, info,
					AppTools.user.getUid());
			auth = RspBodyBaseBean.getAuth(crc, time, imei,
					AppTools.user.getUid());
			String value[] = { opt, auth, info };
			String result = HttpUtils.doPost(AppTools.names, value,
					AppTools.path);
			Log.i("x", "信息----------" + result);

			if ("-500".equals(result))
				return "-500";
			try {
				JSONObject object = new JSONObject(result);
				String smsList = object.getString("stationSMSList");
				JSONArray array = new JSONArray(smsList);
				error = object.getString("error");
				MyMessage msg = null;
				Log.i("x", "array-------" + array);
				for (int i = 0; i < array.length(); i++) {
					JSONObject item = array.getJSONObject(i);
					count = item.getLong("RecordCount");
					return "0";
				}
			} catch (Exception e) {
				Log.i("x", "拿站内信错误" + e.getMessage());
			}
			return error;
		}

		@Override
		protected void onPostExecute(String result) {
			myHandler.sendEmptyMessage(Integer.parseInt(result));
			super.onPostExecute(result);
		}
	}

	/** 处理页面显示的 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AppTools.ERROR_SUCCESS:
				if (0 == count)
					return;
				tv_count.setText("您有" + count + "条未读消息");
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
		tv_count.setText("");
		if (null == AppTools.user) {
			tv_count.setText("您还没登录");
		} else {
			myAsynTask = new MyAsynTask();
			myAsynTask.execute();
		}
		super.onResume();
	}

	/** 退出登陆 */
	private void exitLogin() {
		AppTools.user = null;
		Intent intent = new Intent(context, LoginActivity.class);
		context.startActivity(intent);
		settings = context.getSharedPreferences("app_user", 0);
		Editor editor = settings.edit();
		editor.putBoolean("isLogin", false);
		editor.commit();
		finish();
	}
}
