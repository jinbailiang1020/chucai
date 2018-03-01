package com.sm.sls_app.ui;

import org.json.JSONObject;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.StringUtils;
import com.sm.sls_app.view.MyToast;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author 作者 : hxj
 * @version 创建时间：2015-12-15 上午8:48:31 类说明
 */
public class PayAccountActivity extends Activity implements OnClickListener {
	private String opt, auth, info, time, imei, crc; // 格式化后的参数
	private MyAsynTask myAsynTask;
	private MyHandler mHandler;
	private ProgressDialog proDialog;
	private Intent intent;
	private RelativeLayout alipay_bt, bank_bt;
	private TextView alipay_text, bank_text;
	private ImageButton back;
	/** 是否绑定支付宝 */
	public static boolean hasAlipay = false;
	/** 是否绑定银行卡 */
	public static boolean hasBank = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_account);
		App.activityS.add(this);
		mHandler = new MyHandler();
		initView();
		myAsynTask = new MyAsynTask();
		myAsynTask.execute();
		setListener();
	}

	private void setListener() {
		back.setOnClickListener(this);
		alipay_bt.setOnClickListener(this);
		bank_bt.setOnClickListener(this);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		back = (ImageButton) findViewById(R.id.pay_account_btn_back);
		alipay_bt = (RelativeLayout) findViewById(R.id.pay_account_alipay_btn_name);
		bank_bt = (RelativeLayout) findViewById(R.id.pay_account_bank_btn_name);
		alipay_text = (TextView) findViewById(R.id.pay_account_alipay);
		bank_text = (TextView) findViewById(R.id.pay_account_bank);
	}

	/** 初始化参数 */
	private void init() {
		opt = "41";
		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(this);
		info = "{}";
		String key = MD5.md5(AppTools.user.getUserPass() + AppTools.MD5_key);
		crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		auth = RspBodyBaseBean.getAuth(crc, time, imei, AppTools.user.getUid());

	}

	/** 异步任务 */
	class MyAsynTask extends AsyncTask<Integer, Integer, String> {
		String error = "-1";

		@Override
		protected String doInBackground(Integer... params) {
			if (null == AppTools.user.getRealityName()
					|| AppTools.user.getRealityName().length() == 0) {
				return error;
			}
			init();
			String[] values = { opt, auth, info };
			String result = HttpUtils.doPost(AppTools.names, values,
					AppTools.path);
			System.out.println("提款账户：" + result);
			try {
				JSONObject object = new JSONObject(result);
				String isBinded = object.optString("isBinded");
				String alipayName = object.optString("alipayName");
				AppTools.user.setSecurityquestion(object
						.optString("securityquestion"));
				if ("Yes".equals(isBinded) || !StringUtils.isEmpty(alipayName)) {
					if ("Yes".equals(isBinded)) {
						hasBank = true;
						AppTools.user.setBangNum(object
								.optString("bankCardNumber"));
						AppTools.user.setProvinceName(object
								.optString("provinceName"));
						AppTools.user.setCityName(object.optString("cityName"));
						AppTools.user.setBankName(object
								.optString("bankTypeName"));
						AppTools.user.setFullName(object
								.optString("branchBankName"));

						String str2 = AppTools.user.getBangNum();
						AppTools.user.setBangNum(str2.substring(0, 3) + "***"
								+ str2.substring(str2.length() - 3));
					} else {
						hasBank = false;
					}
					if (!StringUtils.isEmpty(alipayName)) {
						hasAlipay = true;
						AppTools.user.setAlipayName(alipayName);
					} else {
						hasAlipay = false;
					}
					error = "0";
				} else {
					hasAlipay = false;
					hasBank = false;
					error = "-2";
					Log.d("x", "银行卡和支付宝都没绑定");
				}
			} catch (Exception e) {
				e.printStackTrace();
				error = "1";
				Log.e("x", "PayAcciuntActivity异常" + e.getMessage());
			}
			return error;
		}

		/** 最开始执行的 */
		@Override
		protected void onPreExecute() {
			proDialog = new ProgressDialog(PayAccountActivity.this);
			proDialog.setCancelable(false);
			proDialog.show();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			mHandler.sendEmptyMessage(Integer.parseInt(result));
			super.onPostExecute(result);
		}
	}

	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			proDialog.dismiss();
			switch (msg.what) {
			// 至少绑定了一个intent = new Intent(context, WithdrawalActivity.class);
			case 0:
				if (hasAlipay) {
					alipay_text.setText(AppTools.user.getAlipayName());
				}
				if (hasBank) {
					bank_text.setText(AppTools.user.getBangNum());
				}
				break;
			// 都没绑定
			case -2:

				break;
			// 异常了
			case -1:
				MyToast.getToast(PayAccountActivity.this, "请先绑定身份证信息").show();
				intent = new Intent(PayAccountActivity.this,
						IdCardActivity.class);
				PayAccountActivity.this.startActivity(intent);
				finish();
				break;
			}

			super.handleMessage(msg);

		}

	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		if (id == R.id.pay_account_btn_back) {
			finish();
		} else if (id == R.id.pay_account_alipay_btn_name) {
			// 绑定了支付宝
			if (hasAlipay) {
				intent = new Intent(PayAccountActivity.this,
						AlipayWithdrawalActivity.class);
				PayAccountActivity.this.startActivity(intent);
			} else {
				MyToast.getToast(PayAccountActivity.this, "请先绑定支付宝信息").show();
				intent = new Intent(PayAccountActivity.this,
						AlipayInfoActivity.class);
				PayAccountActivity.this.startActivity(intent);
				PayAccountActivity.this.finish();
			}
		} else if (id == R.id.pay_account_bank_btn_name) {
			// 绑定了银行卡
			if (hasBank) {
				intent = new Intent(PayAccountActivity.this,
						WithdrawalActivity.class);
				PayAccountActivity.this.startActivity(intent);
			} else {
				MyToast.getToast(PayAccountActivity.this, "请先绑定银行卡信息").show();
				intent = new Intent(PayAccountActivity.this,
						BankInfoActivity.class);
				PayAccountActivity.this.startActivity(intent);
				PayAccountActivity.this.finish();
			}
		}
	}
}
