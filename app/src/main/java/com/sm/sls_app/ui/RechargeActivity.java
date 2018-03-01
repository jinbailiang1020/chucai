package com.sm.sls_app.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.BaseHelper;
import com.sm.sls_app.utils.MobileSecurePayHelper;
import com.sm.sls_app.utils.MobileSecurePayer;
import com.sm.sls_app.utils.ResultChecker;
import com.sm.sls_app.view.MyToast;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;

/**
 * 充值
 * 
 * @author SLS003
 */
public class RechargeActivity extends Activity implements OnClickListener {

	private EditText et_money;
	private Button btn_recharge;
	private ImageButton btn_back;
	private TextView tv_name;
	private Double money;
	private MyAsynTask myAsynTask;

	private MyHandler myHandler;
	private String opt, auth, info, time, imei, crc;

	// 04-12 15:13:30.124: INFO/System.out(925):
	// 拿到订单信息：partner="2088901133614954"&seller="zhuj@sense.com.cn"&out_trade_no="1000000506"&subject="05FB1E7970C5235A2B"&body="TicketMoney"&total_fee="0.01"&notify_url="http://app.test.sls.net.cn/Home2/Room/OnlinePay/AlipayApp/Notify.aspx"&sign="QKnktGp4KSkV1FpC/rq19gzL5WxL58XNdHdyNEgyvQl+5DAzvHSAsUYtlsujjJfWxTP3FXxUu3BKQfAa3xNDcHQbnn9tr3auVtChY7lTt+Bmd7aAsJKvlpPnTrgaj5goAxvSgCBvmtt6CwPYFU3JzqYXhXOu0Ugn6ubUUn6CU4Y="

	private ProgressDialog mProgress = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_recharge);

		// 检测安全支付服务是否被安装
		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(
				RechargeActivity.this);
		mspHelper.detectMobile_sp();
		findView();
		init();
		setListener();
	}

	private void init() {
		DecimalFormat df = new DecimalFormat("#####0.00");
		et_money.setText(df.format(getIntent().getDoubleExtra("money", 1.00)));
		et_money.setSelection(et_money.getText().length());
	}

	/** 初始化UI */
	private void findView() {
		myHandler = new MyHandler();
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		et_money = (EditText) this.findViewById(R.id.recahge_et_money);
		btn_recharge = (Button) this.findViewById(R.id.recharge_btn_ok);
		tv_name = (TextView) this.findViewById(R.id.recahge_name2);

		tv_name.setText(AppTools.user.getName());
	}

	/** 绑定监听 */
	private void setListener() {
		btn_back.setOnClickListener(this);
		btn_recharge.setOnClickListener(this);
		et_money.addTextChangedListener(watcher);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.recharge_btn_ok:
			MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(
					RechargeActivity.this);
			boolean isMobile_spExist = mspHelper.detectMobile_sp();
			if (!isMobile_spExist)
				return;
			myAsynTask = new MyAsynTask();
			myAsynTask.execute();
			break;
		case R.id.btn_back:
			finish();
			break;
		}
	}

	/** 文本框 监听 */
	private TextWatcher watcher = new TextWatcher() {
		@Override
		public void afterTextChanged(Editable s) {
			if ((s.toString().trim()).length() == 0) {
				return;
			}
			money = Double.parseDouble(s.toString());
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	};

	/** get the sign type we use. 获取签名方式 */
	private String getSignType() {
		String getSignType = "sign_type=" + "\"" + "RSA" + "\"";
		return getSignType;
	}

	/** 关闭进度框 */
	void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// the handler use to receive the pay result.
	// 这里接收支付结果，支付宝手机端同步通知
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {

			Log.i("x", "支付宝Handler===" + msg.what);
			try {
				String strRet = (String) msg.obj;
				Log.e("x", "strRet===" + strRet); // strRet范例：resultStatus={9000};memo={};result={partner="2088201564809153"&seller="2088201564809153"&out_trade_no="050917083121576"&subject="123456"&body="2010新款NIKE 耐克902第三代板鞋 耐克男女鞋 386201 白红"&total_fee="0.01"&notify_url="http://notify.java.jpxx.org/index.jsp"&success="true"&sign_type="RSA"&sign="d9pdkfy75G997NiPS1yZoYNCmtRbdOP0usZIMmKCCMVqbSG1P44ohvqMYRztrB6ErgEecIiPj9UldV5nSy9CrBVjV54rBGoT6VSUF/ufjJeCSuL510JwaRpHtRPeURS1LXnSrbwtdkDOktXubQKnIMg2W0PreT1mRXDSaeEECzc="}
				switch (msg.what) {
				case AppTools.RQF_PAY: {
					//
					closeProgress();
					BaseHelper.log("s", strRet);

					// 处理交易结果
					try {
						// 获取交易状态码，具体状态代码请参看文档
						String tradeStatus = "resultStatus={";
						int imemoStart = strRet.indexOf("resultStatus=");
						imemoStart += tradeStatus.length();
						int imemoEnd = strRet.indexOf("};memo=");
						tradeStatus = strRet.substring(imemoStart, imemoEnd);
						// 先验签通知
						ResultChecker resultChecker = new ResultChecker(strRet);
						int retVal = resultChecker.checkSign();
						// 验签失败
						if (retVal == ResultChecker.RESULT_CHECK_SIGN_FAILED) {
							BaseHelper.showDialog(
									RechargeActivity.this,
									"提示",
									getResources().getString(
											R.string.check_sign_failed),
									android.R.drawable.ic_dialog_alert);
						} else {// 验签成功。验签成功后再判断交易状态码
							if (tradeStatus.equals("9000"))// 判断交易状态码，只有9000表示交易成功
							{ // ----------------huanghao
								// MyAsyn myAsyn = new MyAsyn();
								// myAsyn.execute(); //异步读取修改金额。
								BaseHelper.showDialog(RechargeActivity.this,
										"提示", "支付成功。交易状态码：" + tradeStatus,
										R.drawable.infoicon);
								double a1 = Double.parseDouble(AppTools.user
										.getBalance());
								AppTools.user.setBalance(a1
										+ Double.parseDouble(et_money.getText()
												.toString()));
								RechargeActivity.this.finish();
								MainActivity.toCenter();

							} else
								BaseHelper.showDialog(RechargeActivity.this,
										"提示", "支付失败。交易状态码：" + tradeStatus,
										R.drawable.infoicon);

						}

					} catch (Exception e) {
						e.printStackTrace();
						BaseHelper.showDialog(RechargeActivity.this, "提示",
								strRet, R.drawable.infoicon);
					}
				}
					break;
				}

				super.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	/* 发送请求查询充值后的余额*--------------huanghao */
	// class MyAsyn extends AsyncTask<Void, Integer, String>{
	//
	// @Override
	// protected String doInBackground(Void... params) {
	// // TODO Auto-generated method stub
	// opt = "41";
	// time = RspBodyBaseBean.getTime();
	// imei = RspBodyBaseBean.getIMEI(RechargeActivity.this);
	// info = "{}";
	// String key = MD5.md5(AppTools.user.getUserPass() + AppTools.MD5_key);
	// crc = RspBodyBaseBean.getCrc(time, imei, key, info,
	// AppTools.user.getUid());
	// auth = RspBodyBaseBean.getAuth(crc, time, imei, AppTools.user.getUid());
	// String[] values = { opt, auth, info };
	// String result = HttpUtils.doPost(AppTools.names, values,AppTools.path);
	/* 以一定格式发送查询 返回result */
	// try {
	// JSONObject object = new JSONObject(result);
	// String isBinded = object.optString("isBinded");
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// AppTools.user.setBalance(10000);//设置新余额。
	// return "OK";
	// }
	// @Override
	// protected void onPostExecute(String result) {
	// // TODO Auto-generated method stub
	// super.onPostExecute(result);
	// if(result=="OK"){
	// myHandler.sendEmptyMessage(11);
	// }
	//
	//
	// }
	// }
	/** 拿到传到支付宝的数据 */
	private String getOrderInfo() {
		System.out.println("点击了。。");
		String[] names = { "uid", "payMoney" };
		String uid = AppTools.user.getUid();

		// String payMoney = "0.01";

		if (et_money.getText().length() == 0)
			money = 1.00;
		else
			money = Double.parseDouble(et_money.getText().toString());

		String values[] = { uid, money + "" };

		String result = HttpUtils.doPost(names, values, AppTools.zPath);

		System.out.println("支付宝---" + result);

		String strOrderInfo = "";

		String content = "";

		String sign = "";
		try {
			strOrderInfo = URLDecoder.decode(result, "UTF-8");
			System.out.println("解码后：" + strOrderInfo);

			content = strOrderInfo.substring(
					strOrderInfo.lastIndexOf("<content>") + 9,
					strOrderInfo.lastIndexOf("</content>"));
			sign = strOrderInfo.substring(
					strOrderInfo.lastIndexOf("<sign>") + 6,
					strOrderInfo.lastIndexOf("</sign>"));
			System.out.println("截取的" + content + "==" + sign);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("错误" + e.getMessage());
			return null;
		}

		sign = URLEncoder.encode(sign);
		strOrderInfo = content + "&sign=" + "\"" + sign + "\"" + "&"
				+ getSignType();
		// 对签名进行编码

		Log.i("x", "sign=1111=====  " + sign);

		Log.i("x", "拿到订单信息：" + strOrderInfo);
		return strOrderInfo;
	}

	// 06-24 16:02:13.750: I/System.out(316): 拿到订单信息：&sign=""sign_type="RSA"

	/*** 异步任务 用来后台获取数据 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> {
		/** 在后台执行的程序 */
		String error = "-1001";

		@Override
		protected String doInBackground(Void... params) {
			try {
				// prepare the order info.
				// 准备订单信息
				String orderInfo = getOrderInfo();
				if (null == orderInfo)
					return null;

				// 调用pay方法进行支付
				MobileSecurePayer msp = new MobileSecurePayer();

				boolean bRet = msp.pay(orderInfo, mHandler, AppTools.RQF_PAY,
						RechargeActivity.this);

				Log.i("x", "bRet====" + bRet);
				if (bRet) {
					// show the progress bar to indicate that we have started
					// paying.
					// 显示“正在支付”进度条
					myHandler.sendEmptyMessage(1);
				} else
					;
			} catch (Exception ex) {
				Log.i("x", "充值出错---" + ex.getMessage());
			}
			return null;
		}
	}

	/** 处理页面显示的 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case -500:
				MyToast.getToast(RechargeActivity.this, "连接超时").show();
				break;
			case 1:
				closeProgress();
				mProgress = BaseHelper.showProgress(RechargeActivity.this,
						null, "正在支付", false, true);
				break;
			default:
				break;
			}
			if (myAsynTask != null
					&& myAsynTask.getStatus() == AsyncTask.Status.RUNNING) {
				myAsynTask.cancel(true); // 如果Task还在运行，则先取消它
			}
			super.handleMessage(msg);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		try {
			int i = (Integer) getIntent().getExtras().get("CallingPid");
			MyToast.getToast(RechargeActivity.this, "充值成功，跳转页面").show();
		} catch (Exception e) {
			Log.i("x", "--错误--" + e.getMessage());
		}


		super.onNewIntent(intent);
	}

}
