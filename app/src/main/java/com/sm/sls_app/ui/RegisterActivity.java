package com.sm.sls_app.ui;

import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.Users;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.BaseHelper;
import com.sm.sls_app.utils.NetWork;
import com.sm.sls_app.view.MyToast;

/**
 * 功能：注册界面，实现注册操作 版本
 *
 * @author Administrator 修改日期：2013-3-11
 */
public class RegisterActivity extends Activity implements OnClickListener {

	private Button btn_login, btn_reg;

	private ImageButton btn_back;

	private EditText et_name, et_pass, et_upass;

	private String userName, userPass, userRePass;

	private String opt, auth, info, time, imei, crc; // 格式化后的参数

	private MyAsynTask myAsynTask;

	private MyHandler myHandler;

	private ProgressDialog mProgress = null;

	private SharedPreferences settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);

		App.activityS.add(this);
		init();
		findView();
		setListener();
	}

	/** 初始化UI */
	private void findView() {
		myHandler = new MyHandler();
		btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		btn_login = (Button) this.findViewById(R.id.reg_top_btn_login);
		btn_reg = (Button) this.findViewById(R.id.reg_btn_reg);

		et_name = (EditText) this.findViewById(R.id.reg_et_userName);
		et_pass = (EditText) this.findViewById(R.id.reg_et_userPass);
		et_upass = (EditText) this.findViewById(R.id.reg_et_reUserPass);
	}

	/** 初始化参数 */
	private void init() {
		opt = "1";
		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(RegisterActivity.this);
	}

	/** 绑定监听 */
	private void setListener() {
		btn_back.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		btn_reg.setOnClickListener(this);
		et_name.setOnFocusChangeListener(new MyEditTextFocusChangeListener());
		et_pass.setOnFocusChangeListener(new MyEditTextFocusChangeListener());
		et_upass.setOnFocusChangeListener(new MyEditTextFocusChangeListener());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reg_top_btn_login:
			doLogin();
			break;
		case R.id.reg_btn_reg:
			if (NetWork.isConnect(RegisterActivity.this)) {
				myAsynTask = new MyAsynTask();
				myAsynTask.execute();
			} else {
				MyToast.getToast(RegisterActivity.this, "网络连接异常，注册失败！").show();
			}
			break;
		case R.id.btn_back:
			Intent intent = new Intent(RegisterActivity.this,
					LoginActivity.class);
			RegisterActivity.this.startActivity(intent);
			RegisterActivity.this.finish();
			break;
		}
	}

	/** 跳到登录页面 */
	private void doLogin() {
		Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
		RegisterActivity.this.startActivity(intent);
		this.finish();
	}

	/** 当文本改变时 **/
	class MyEditTextFocusChangeListener implements OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (!hasFocus) {
				String str = ((EditText) v).getText().toString();
				if (null == str)
					Toast.makeText(RegisterActivity.this, "数据不能为空",
							Toast.LENGTH_SHORT).show();
			}
		}
	}

	/** 异步任务 用来后台获取数据 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> {
		String error = "-1";

		@Override
		protected String doInBackground(Void... params) {
			userName = et_name.getText().toString().trim();
			userPass = et_pass.getText().toString().trim();
			userRePass = et_upass.getText().toString().trim();
			if (userName.length() < 1)
				return "-11";
			if (userPass.length() < 6)
				return "-10";
			String name_verification = "^[\u4e00-\u9fa5_a-zA-Z0-9]{3,16}$";
			if (!userName.matches(name_verification))
				return "-12";
			if (userPass.equals(userRePass)) {
				String key = MD5.md5(AppTools.MD5_key);
				info = RspBodyBaseBean.changeRegister_info(userName,
						MD5.md5(userPass + AppTools.MD5_key));
				crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");
				auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");

				String values[] = { opt, auth, info };
				String result = HttpUtils.doPost(AppTools.names, values,
						AppTools.path);

				Log.i("x", "注册info---------" + info);
				Log.i("x", "注册时的result----" + result);

				if ("-500".equals(result))
					return result;

				System.out.println("注册得到的信息" + result);

				try {
					JSONObject ob = new JSONObject(result);
					error = ob.getString("error");
					if ("0".equals(error)) {
						AppTools.user = new Users();
						AppTools.user.setUid(ob.optString("uid"));
						AppTools.user.setName(ob.optString("name"));
						AppTools.user.setRealityName(ob
								.optString("realityName"));
						AppTools.user.setBalance(ob.optLong("balance"));
						AppTools.user.setFreeze(ob.optDouble("freeze"));
						AppTools.user.setEmail(ob.optString("email"));
//						AppTools.user.setIdcardnumber(ob
//								.optString("idcardnumber"));
						AppTools.user.setMobile(ob.optString("mobile"));
						AppTools.user.setMsgCount(ob.optInt("msgCount"));
						AppTools.user.setMsgCountAll(ob.optInt("msgCountAll"));
						AppTools.user.setScoring(ob.optInt("scoring"));
						// 用户密码 （没加密的）
						AppTools.user.setUserPass(userPass);
						return AppTools.ERROR_SUCCESS + "";
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					error = "-1";
				}

			} else {
				error = "-2";
			}
			return error;
		}

		/** 最开始执行的 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			mProgress = BaseHelper.showProgress(RegisterActivity.this, null,
					"注册中....", true, true);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			Log.i("x", "发送消息。。。。。");
			myHandler.sendEmptyMessage(Integer.parseInt(result));
			super.onPostExecute(result);
		}
	}

	/** 重写返回键事件 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(RegisterActivity.this,
					LoginActivity.class);
			RegisterActivity.this.startActivity(intent);
			RegisterActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/** 处理页面显示的 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			mProgress.dismiss();
			switch (msg.what) {
			case 0:
				MyToast.getToast(RegisterActivity.this, "注册成功").show();
				// doLogin();
				RegisterActivity.this.finish();
				MainActivity.toCenter();
				// 将用户名记住
				settings = getSharedPreferences("app_user", 0);
				Editor editor = settings.edit();
				editor.putString("name", userName);
				editor.putString("pass", userPass);
				editor.putBoolean("isLogin", true);
				editor.commit();
				break;
			case 1:
				MyToast.getToast(RegisterActivity.this, "网络不稳定，请稍后再试").show();
				break;
			case -2:
				MyToast.getToast(RegisterActivity.this, "两次密码不正确，请重新输入").show();
				break;
			case -10:
				MyToast.getToast(RegisterActivity.this, "密码长度必须大于等于6").show();
				break;
			case -11:
				MyToast.getToast(RegisterActivity.this, "用户名不能为空").show();
				break;
			case -12:
				MyToast.getToast(RegisterActivity.this, "用户名不符合规则").show();
				break;
			case -500:
				MyToast.getToast(RegisterActivity.this, "连接超时").show();
				break;
			case -110:
				MyToast.getToast(RegisterActivity.this, "该用户名已存在。注册失败").show();
				break;
			default:
				MyToast.getToast(RegisterActivity.this, "注册失败").show();
				Log.i("x", "-----注册错误-----:" + msg.what);
				break;
			}
			super.handleMessage(msg);
		}
	}

}
