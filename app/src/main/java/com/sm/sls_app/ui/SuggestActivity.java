package com.sm.sls_app.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.BaseHelper;
import com.sm.sls_app.view.MyToast;

public class SuggestActivity extends Activity implements OnClickListener {

	private String opt = "48"; // 格式化后的 opt
	private String auth, info, time, imei, crc; // 格式化后的参数

	private EditText et_content, et_tel, et_email, et_title;
	private Button btn;
	private ImageButton btn_back;

	private MyHandler myHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggest);
		myHandler = new MyHandler();
		findView();
	}

	/** 初始化UI **/
	private void findView() {
		// TODO Auto-generated method stub
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		et_title = (EditText) this.findViewById(R.id.suggest_et_title);
		et_content = (EditText) this.findViewById(R.id.suggest_et_context);
		et_tel = (EditText) this.findViewById(R.id.suggest_et_phoneNumber);
		et_email = (EditText) this.findViewById(R.id.suggest_et_email);

		btn = (Button) this.findViewById(R.id.suggest_btn_ok);

		btn.setOnClickListener(this);
		btn_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.suggest_btn_ok:
			String regEx = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$";
			String s = (et_email.getText() + "").trim();
			Pattern pat = Pattern.compile(regEx);
			Matcher mat = pat.matcher(s);
			boolean rs = mat.find();
			if (rs) {
				MyAsynTask myAsynTask = new MyAsynTask();
				myAsynTask.execute();
			} else {
				MyToast.getToast(getApplicationContext(), "请输入正确的邮箱").show();
			}
			break;

		case R.id.btn_back:
			finish();
			break;
		}

	}

	/** 异步任务 用来后台获取数据 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> {
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = BaseHelper.showProgress(SuggestActivity.this,
					null, "提交中", true, false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			if (et_content.getText().toString().trim().length() == 0)
				return "-300";
			time = RspBodyBaseBean.getTime();
			imei = RspBodyBaseBean.getIMEI(SuggestActivity.this);

			info = RspBodyBaseBean
					.changeSuggest_info(et_tel.getText().toString(), et_email
							.getText().toString(), et_content.getText()
							.toString(), et_title.getText().toString().trim());

			Log.i("x", "反馈建议----info--" + info);
			String uid = "-1";
			String key = MD5.md5(AppTools.MD5_key);

			if (null != AppTools.user) {
				uid = AppTools.user.getUid();
				key = MD5.md5(AppTools.user.getUserPass() + AppTools.MD5_key);
			}

			crc = RspBodyBaseBean.getCrc(time, imei, key, info, uid);

			auth = RspBodyBaseBean.getAuth(crc, time, imei, uid);
			String values[] = { opt, auth, info };
			String result = HttpUtils.doPost(AppTools.names, values,
					AppTools.path);
			if ("-500".equals(result))
				return result;

			Log.i("x", "反馈建议------result--" + result);
			JSONObject object;
			String error = "-200";
			try {
				object = new JSONObject(result);
				error = object.getString("error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				error = "-200";
			}
			Log.i("x", "反馈建议--------" + result);
			return error;
		}

		@Override
		protected void onPostExecute(String result) {
			progressDialog.dismiss();
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
			case 0:
				MyToast.getToast(SuggestActivity.this, "反馈提交成功").show();
				SuggestActivity.this.finish();
				break;
			case -500:
				MyToast.getToast(SuggestActivity.this, "连接超时").show();
				break;
			case -4801:
				MyToast.getToast(SuggestActivity.this, "距离上次反馈不足半个小时，请稍后再反馈。")
						.show();
				SuggestActivity.this.finish();
				break;
			case -300:
				MyToast.getToast(SuggestActivity.this, "反馈意见不能为空。").show();
				break;
			default:
				break;
			}
		}
	}

}
