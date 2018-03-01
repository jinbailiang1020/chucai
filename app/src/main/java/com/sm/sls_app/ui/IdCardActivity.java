package com.sm.sls_app.ui;

import org.json.JSONObject;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.view.MyBankDialog;
import com.sm.sls_app.view.MyToast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * 身份证信息
 * 
 * @author SLS003
 * 
 */
public class IdCardActivity extends Activity implements OnClickListener {

	private EditText et_name;
	private Button btn_ok;
	private ImageButton btn_back;

	private String opt, auth, info, time, imei, crc; // 格式化后的参数

	private String name;

	private MyAsynTask myAsyn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_idcard);

		App.activityS.add(this);
		AccountInformationActivity.list.add(this);

		findView();
		setListener();

	}

	/** 初始化属性 */
	private void init() {
		opt = "43";
		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(this);
		myAsyn = new MyAsynTask();
		myAsyn.execute();
	}

	/** 初始化UI */
	private void findView() {
		et_name = (EditText) this.findViewById(R.id.idcard_et_name);
		btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		btn_ok = (Button) this.findViewById(R.id.idcard_btn_improve);
	}

	/** 绑定监听 */
	private void setListener() {
		btn_ok.setOnClickListener(this);
		btn_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;

		case R.id.idcard_btn_improve:
			name = et_name.getText().toString().trim();
			init();
			break;
		}

	}

	/** 跳到下一个页面 */
	public void toNext() {
		Intent intent = new Intent(IdCardActivity.this, BankInfoActivity.class);
		IdCardActivity.this.startActivity(intent);
	}

	/** 异步任务 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> {
		@Override
		protected String doInBackground(Void... params) {
			if (name.length() == 0 ) {

				return "-100";
			}
			String name_verification = "^[\u4e00-\u9fa5_a-zA-Z]{1,6}$";
//			String isIDCard2_verification = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
			if (!name.matches(name_verification)) {
				return "-102";
			}

			info = RspBodyBaseBean.changeImprove_info(name);
			String key = MD5
					.md5(AppTools.user.getUserPass() + AppTools.MD5_key);
			crc = RspBodyBaseBean.getCrc(time, imei, key, info,
					AppTools.user.getUid());
			auth = RspBodyBaseBean.getAuth(crc, time, imei,
					AppTools.user.getUid());

			String[] values = { opt, auth, info };
			String result = HttpUtils.doPost(AppTools.names, values,
					AppTools.path);

			System.out.println("完善信息结果" + result);

			if ("-500".equals(result))
				return result;

			try {
				JSONObject ob = new JSONObject(result);
				String error = ob.getString("error");
				return error;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "-1";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			MyHander myHandler = new MyHander();
			myHandler.sendEmptyMessage(Integer.parseInt(result));
			super.onPostExecute(result);
		}
	}

	/** MyHander */
	@SuppressLint("HandlerLeak")
	class MyHander extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case -1:
				Toast.makeText(IdCardActivity.this, "失败", 1000).show();
				break;
			case 0:
				MyBankDialog dialog = new MyBankDialog(IdCardActivity.this,
						AppTools.user.getName(), name, R.style.dialog);
				dialog.show();
				AppTools.user.setRealityName(name);
//				AppTools.user.setIdcardnumber(card);
				break;
			case -100:
				Toast.makeText(IdCardActivity.this, "信息不能为空",
						Toast.LENGTH_SHORT).show();
				break;
			case -101:
				Toast.makeText(IdCardActivity.this, "身份证号码不正确",
						Toast.LENGTH_SHORT).show();
				break;
			case -102:
				Toast.makeText(IdCardActivity.this, "真实姓名填写有误",
						Toast.LENGTH_SHORT).show();
				break;
			case -500:
				MyToast.getToast(IdCardActivity.this, "连接超时").show();
				break;
			}
			super.handleMessage(msg);
		}
	}

}
