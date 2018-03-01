package com.sm.sls_app.ui;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
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
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.BaseHelper;
import com.sm.sls_app.view.MyToast;

/**
 * 账户信息
 * 
 * @author SLS003
 */
public class AccountInformationActivity extends Activity {

	private TextView tv_name; // 真实姓名rr
	private TextView tv_bankName; // 银行名称
	// private TextView tv_bankLocation; // 开户地点
	private TextView tv_bankNum; // 银行号
	private ImageButton btn_back;
	private String opt, auth, info, time, imei, crc; // 格式化后的参数

	private Button btn_improve;
	// // 用户信息
	// private String bankCardNumber, provinceName, cityName, bankTypeName,
	// branchBankName;

	private Class activity;

	private MyAsynTask myAsynTask;
	private MyHandler myHandler;

	private ScrollView sv;

	public static List<Activity> list = new ArrayList<Activity>();
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_account_information);

		App.activityS.add(this);
		list.add(this);

		init();
		sv = (ScrollView) this.findViewById(R.id.scrollView);
		sv.setVisibility(View.GONE);
		myAsynTask = new MyAsynTask();
		myAsynTask.execute();
	}

	/** 初始化参数 */
	private void init() {
		if (AppTools.user.getUserPass() != null
				&& AppTools.user.getUid() != null) {
			myHandler = new MyHandler();
			opt = "41";
			time = RspBodyBaseBean.getTime();
			imei = RspBodyBaseBean.getIMEI(this);
			info = "{}";
			String key = MD5
					.md5(AppTools.user.getUserPass() + AppTools.MD5_key);
			crc = RspBodyBaseBean.getCrc(time, imei, key, info,
					AppTools.user.getUid());
			auth = RspBodyBaseBean.getAuth(crc, time, imei,
					AppTools.user.getUid());
		} else
			AppTools.doLogin(AccountInformationActivity.this);

	}

	/** 判断银行卡是否绑定 */
	private boolean chechBank() {
		if (null == AppTools.user.getBangNum()
				|| AppTools.user.getBangNum().length() == 0) {
			String[] values = { opt, auth, info };
			String result = HttpUtils.doPost(AppTools.names, values,
					AppTools.path);
			System.out.println("银行信息：" + result);
			try {
				JSONObject object = new JSONObject(result);
				String isBinded = object.optString("isBinded");
				AppTools.user.setSecurityquestion(object
						.optString("securityquestion"));
				if ("Yes".equals(isBinded)) {
					AppTools.user
							.setBangNum(object.optString("bankCardNumber"));
					AppTools.user.setProvinceName(object
							.optString("provinceName"));
					AppTools.user.setCityName(object.optString("cityName"));
					AppTools.user.setBankName(object.optString("bankTypeName"));
					AppTools.user.setFullName(object
							.optString("branchBankName"));
					String str2 = AppTools.user.getBangNum();
					AppTools.user.setBangNum(str2.substring(0, 3) + "***"
							+ str2.substring(str2.length() - 3));
					return true;
				} else {
					Log.d("x", "银行卡没绑定");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("x", "AccountInfornation异常" + e.getMessage());
			}
		} else {
			return true;
		}
		return false;
	}

	/** 初始化UI */
	private void findView() {
		sv.setVisibility(View.VISIBLE);
		tv_name = (TextView) this.findViewById(R.id.accInfo_tv_name2);
		// tv_bankLocation = (TextView) this
		// .findViewById(R.id.accInfo_tv_location2);
		tv_bankName = (TextView) this.findViewById(R.id.accInfo_tv_bankName2);
		tv_bankNum = (TextView) this.findViewById(R.id.accInfo_tv_bankNum2);
		btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		btn_improve = (Button) this.findViewById(R.id.accInfo_btn_improve);
		btn_back.setOnClickListener(new MyClickListener());
		btn_improve.setOnClickListener(new MyClickListener());
	}

	/** 点击监听 */
	class MyClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_back:
				finish();
				break;

			case R.id.accInfo_btn_improve:
				Log.i("x", "点击跳转-----");
				Intent intent = new Intent(AccountInformationActivity.this,
						activity);
				AccountInformationActivity.this.startActivity(intent);
				break;
			}
		}
	}

	/** 异步任务 用来后台获取数据 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialog = BaseHelper.showProgress(AccountInformationActivity.this,
					null, "加载中..", true, false);
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			// 如果有绑定银行
			if (!chechBank()) {
				activity = BankInfoActivity.class;

			} else {
				myHandler.sendEmptyMessage(1);
			}

			if (AppTools.user.getRealityName().length() != 0) {
				myHandler.sendEmptyMessage(2);

			} else {
				activity = IdCardActivity.class;
			}
			if (null != activity) {
				myHandler.sendEmptyMessage(3);
			} else {
				myHandler.sendEmptyMessage(4);
			}
			return null;
		}

	}

	/** 处理页面显示的 */
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (null != dialog) {
				dialog.dismiss();
			}
			switch (msg.what) {
			case 1:
				findView();
				sv.setVisibility(View.VISIBLE);
				// tv_bankLocation.setText(AppTools.user.getProvinceName()
				// + AppTools.user.getCityName());
				tv_bankNum.setText(AppTools.user.getBangNum());
				tv_bankName.setText(AppTools.user.getBankName());
				break;
			case 2:
				findView();
				sv.setVisibility(View.VISIBLE);
				tv_name.setText(AppTools.user.getRealityName());
				break;
			case 3:
				findView();
				MyToast.getToast(AccountInformationActivity.this,
						"您还没有绑定信息，请先完善信息").show();
				break;
			case 4:
				findView();
				sv.setVisibility(View.VISIBLE);
				btn_improve.setVisibility(View.GONE);
				break;
			}
			super.handleMessage(msg);
		}
	}

}
