package com.sm.sls_app.ui;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.view.MyToast;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * @author 作者 : hxj
 * @version 创建时间：2015-12-16 上午8:55:15 类说明 积分中心
 */
public class IntegralActivity extends Activity implements OnClickListener {
	private TextView te_integral;
	private EditText et_integral;
	private Button btn_integral_1, btn_integral_2, btn_integral_5,
			btn_integral_10, btn_integral;
	private ImageButton btn_back;
	private MyHandler mHandler = new MyHandler();
	private MyAsyncTast myAsyncTast;
	private MyAsyncTast2 myAsyncTast2;
	private String opt, auth, info, time, imei, crc; // 格式化后的参数
	private int wantIntegral;// 临时变量 记录要换的积分数
	private ProgressDialog proDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.integral_activity);
		initView();
		myAsyncTast = new MyAsyncTast();
		myAsyncTast.execute();
		setListener();
	}

	private void setListener() {
		btn_back.setOnClickListener(this);
		btn_integral_1.setOnClickListener(this);
		btn_integral_2.setOnClickListener(this);
		btn_integral_5.setOnClickListener(this);
		btn_integral_10.setOnClickListener(this);
		btn_integral.setOnClickListener(this);
	}

	private void initView() {
		te_integral = (TextView) findViewById(R.id.integral_text);
		et_integral = (EditText) findViewById(R.id.integral_et);
		btn_integral_1 = (Button) findViewById(R.id.integral_btn_1);
		btn_integral_2 = (Button) findViewById(R.id.integral_btn_2);
		btn_integral_5 = (Button) findViewById(R.id.integral_btn_5);
		btn_integral_10 = (Button) findViewById(R.id.integral_btn_10);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_integral = (Button) findViewById(R.id.integral_btn);
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		switch (v.getId()) {
		case R.id.integral_btn_1:
		case R.id.integral_btn_2:
		case R.id.integral_btn_5:
		case R.id.integral_btn_10:
			// 先清空再赋值
			wantIntegral = 0;
			if (id == R.id.integral_btn_1) {
				wantIntegral = 1000;
			} else if (id == R.id.integral_btn_2) {
				wantIntegral = 2000;
			} else if (id == R.id.integral_btn_5) {
				wantIntegral = 5000;
			} else if (id == R.id.integral_btn_10) {
				wantIntegral = 10000;
			}
			if (wantIntegral > AppTools.user.getScoring()) {
				MyToast.getToast(IntegralActivity.this, "积分不足").show();
			} else {
				myAsyncTast2 = new MyAsyncTast2();
				myAsyncTast2.execute();
			}
			break;
		case R.id.btn_back:
			finish();
			break;
		case R.id.integral_btn:
			wantIntegral = 0;
			if (et_integral.getText().length() == 0) {
				return;
			}
			wantIntegral = Integer.parseInt(et_integral.getText().toString()
					.trim());
			if (wantIntegral == 0) {
				MyToast.getToast(IntegralActivity.this, "兑换积分不能为0").show();
				return;
			} else if (AppTools.user.getScoring() < 1000) {
				MyToast.getToast(IntegralActivity.this, "您的积分不足1000，目前无法兑换")
						.show();
				return;
			} else if (wantIntegral > AppTools.user.getScoring()) {
				MyToast.getToast(IntegralActivity.this, "您要兑换的积分超出您的当前积分")
						.show();
				return;
			} else if (wantIntegral % 1000 == 0 & wantIntegral >= 1000) {
				myAsyncTast2 = new MyAsyncTast2();
				myAsyncTast2.execute();
			} else {
				MyToast.getToast(IntegralActivity.this, "所填积分需大于1000且为1000的整数倍")
						.show();
			}
			break;
		default:
			break;
		}
	}

	private void init() {
		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(this);
		String key = MD5.md5(AppTools.user.getUserPass() + AppTools.MD5_key);
		crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		auth = RspBodyBaseBean.getAuth(crc, time, imei, AppTools.user.getUid());
	}

	String msgs;

	class MyAsyncTast extends AsyncTask<Integer, Integer, String> {
		@Override
		protected String doInBackground(Integer... params) {
			init();
			opt = "3";
			info = "{\"uid\":\"" + AppTools.user.getUid() + "\"}";
			String[] values = { opt, auth, info };
			String result = HttpUtils.doPost(AppTools.names, values,
					AppTools.path);
			System.out.println("获取积分：" + result);
			JSONObject object;
			try {
				object = new JSONObject(result);
				if ("0".equals(object.getString("error"))) {
					AppTools.user.setScoring(object.getInt("scoring"));
				} else {
					msgs = object.getString("msg");
				}
				return object.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "201";
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			mHandler.sendEmptyMessage(Integer.parseInt(result));
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

	}

	class MyAsyncTast2 extends AsyncTask<Integer, Integer, String> {

		@Override
		protected String doInBackground(Integer... params) {
			init();
			opt = "26";
			info = "{\"score\":" + wantIntegral + "}";
			System.out.println("兑换积分info：" + info);
			String[] values = { opt, auth, info };
			String result = HttpUtils.doPost(AppTools.names, values,
					AppTools.path);
			System.out.println("兑换积分：" + result);
			JSONObject object;
			try {
				object = new JSONObject(result);
				if ("0".equals(object.getString("error"))) {
					JSONArray userInfo = new JSONArray(
							object.getString("dtUserInfo"));
					if (userInfo != null) {
						for (int i = 0; i < userInfo.length(); i++) {
							JSONObject obj = userInfo.getJSONObject(i);
							AppTools.user.setBalance(obj.getDouble("balance"));
							AppTools.user.setScoring(obj.getInt("score"));
						}
					}
					return "1000";
				} else {
					msgs = object.getString("msg");
					return "401";
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return "301";
		}

		@Override
		protected void onPostExecute(String result) {
			if (proDialog != null) {
				proDialog.dismiss();
			}
			mHandler.sendEmptyMessage(Integer.parseInt(result));
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			proDialog = ProgressDialog.show(IntegralActivity.this, "", "正在兑换");
			proDialog.show();
			super.onPreExecute();
		}

	}

	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				te_integral.setText(AppTools.user.getScoring() + "分");
				break;
			case 1000:
				MyToast.getToast(IntegralActivity.this, "兑换成功").show();
				et_integral.setText("");
				te_integral.setText(AppTools.user.getScoring() + "分");
				break;
			case 201:
				MyToast.getToast(IntegralActivity.this, "获取当前积分失败").show();
				break;
			case 301:
				MyToast.getToast(IntegralActivity.this, "兑换失败").show();
				break;
			case 401:
				MyToast.getToast(IntegralActivity.this, msgs).show();
				break;
			default:
				MyToast.getToast(IntegralActivity.this, msgs).show();
				break;
			}
		}

	}
}
