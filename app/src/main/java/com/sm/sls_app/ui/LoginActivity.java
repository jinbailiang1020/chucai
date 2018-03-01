package com.sm.sls_app.ui;

import java.lang.reflect.Method;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Build.VERSION;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
 * 功能：用户登录界面，实现登录功能 版本
 * 
 * @author Administrator 修改日期：2013-3-6
 */
public class LoginActivity extends Activity implements OnClickListener {

	private EditText et_name, et_pass;

	private Button btn_login;

	private ImageView activity_login_check;

	private TextView activity_login_forgetpass;

	private String userName, userPass; // 获得用户输入的用户名 // 获得用户输入的密码

	private String opt, auth, info, time, imei; // 格式化后的参数

	private SharedPreferences settings;

	private ProgressDialog mProgress = null;

	private Intent intent;

	private String tagName = "loginTag";

	private String pass;

	private MyHandler myHandler;
	private MyAsynTask myAsyn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		App.activityS.add(this);

		findView();

		init();

		setListener();
		// setting();
	}

	/**
	 * 判断用户是否登陆过
	 */
	private void init() {
		settings = this.getSharedPreferences("app_user", Context.MODE_PRIVATE);
		boolean isLogin = false;// 没有登录的标识
		String name = "";
		String pass = "";
		if (settings.contains("isLogin")) {
			isLogin = settings.getBoolean("isLogin", false);
		}
		if (settings.contains("name")) {
			name = settings.getString("name", null);
		}

		// 是否记录密码
		boolean isRecord = settings.getBoolean("isRecord", false);

		if (isRecord) {
			et_pass.setText(settings.getString("pass", null));
		}
		activity_login_check.setSelected(isRecord ? true : false);
		// 显示下划线
		activity_login_forgetpass.getPaint()
				.setFlags(Paint.UNDERLINE_TEXT_FLAG);
		// 判断是否有存 用户名
		if (isLogin) {
			// 判断是否有存 密码
			if (settings.contains("pass")) {
				pass = settings.getString("pass", null);
			}

			et_pass.setText(pass);
		}
		et_name.setText(name);
		if (null != pass && name != null && !"".equals(name)
				&& !"".equals(pass) && isLogin) {
			String type = getIntent().getStringExtra("loginType");
			if (!"genggai".equals(type))
				doLogin();
		}
	}

	/**
	 * 初始化
	 */
	private void findView() {
		myHandler = new MyHandler();

		et_name = (EditText) this.findViewById(R.id.et_userName);
		et_pass = (EditText) this.findViewById(R.id.et_userPass);

		btn_login = (Button) this.findViewById(R.id.login_btn_login);

		activity_login_check = (ImageView) this
				.findViewById(R.id.activity_login_check);
		activity_login_forgetpass = (TextView) this
				.findViewById(R.id.activity_login_forgetpass);

		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(LoginActivity.this);

	}

	/**
	 * 绑定监听
	 */
	private void setListener() {
		btn_login.setOnClickListener(this);
		activity_login_check.setOnClickListener(this);
		activity_login_forgetpass.setOnClickListener(this);
	}

	/* 设置键盘 软盘 */
	private void setting() {
		if (android.os.Build.VERSION.SDK_INT <= 10) {
			et_name.setInputType(InputType.TYPE_NULL);
			et_pass.setInputType(InputType.TYPE_NULL); // 关闭软键盘
		} else {
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			try {
				Class<EditText> cls = EditText.class;
				Method setSoftInputShownOnFocus;
				setSoftInputShownOnFocus = cls.getMethod(
						"setSoftInputShownOnFocus", boolean.class);
				setSoftInputShownOnFocus.setAccessible(true);
				setSoftInputShownOnFocus.invoke(et_name, false);
				setSoftInputShownOnFocus.invoke(et_pass, false);
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(et_name.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(et_pass.getWindowToken(), 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 公共点击监听
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn_login:
			doLogin();
			break;
		case R.id.activity_login_forgetpass:

			break;
		case R.id.activity_login_check:
			boolean isSelect = activity_login_check.isSelected();
			activity_login_check.setSelected(!isSelect);
			Editor editor = settings.edit();
			editor.putBoolean("isRecord", !isSelect);
			editor.commit();
			break;
		}
	}

	/**
	 * 登录
	 */
	private void doLogin() {
		userName = et_name.getText().toString().trim();
		userPass = et_pass.getText().toString().trim();

		if (userName.length() == 0) {
			MyToast.getToast(LoginActivity.this, "用户名不能为空").show();
			return;
		}
		if (userPass.length() == 0) {
			MyToast.getToast(LoginActivity.this, "密码不能为空").show();
			return;
		}

		if (NetWork.isConnect(LoginActivity.this)) {
			myAsyn = new MyAsynTask();
			myAsyn.execute();

		} else {
			MyToast.getToast(LoginActivity.this, "网络异常,登陆失败").show();
		}
	}

	/**
	 * 异步任务
	 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> {
		/**
		 * 在后台执行的程序
		 */
		@Override
		protected String doInBackground(Void... params) {
			// 对密码进行MD5加密
			pass = MD5.md5(userPass + AppTools.MD5_key);

			opt = "2";
			info = RspBodyBaseBean.changeLogin_Info(userName, pass);
			auth = RspBodyBaseBean.changeLogin_Auth(
					RspBodyBaseBean.getCrc(time, imei,
							MD5.md5(AppTools.MD5_key), info, "-1"), time, imei);

			String[] values = { opt, auth, info };

			String result = HttpUtils.doPost(AppTools.names, values,
					AppTools.path);

			if ("-500".equals(result))
				return "-500";
			Log.e("x", "login==" + result);
			try {
				JSONObject item = new JSONObject(result);
				/**
				 * {"uid":"582","error":"0","msgCountAll":"0","msg":"","scoring"
				 * :"882","balance":"259.17","email":"","freeze":"261.00","name"
				 * :"andtest","serverTime":"2016-01-06 10:29:01","realityName":
				 * "无名","msgCount":"0","mobile":"","idcardnumber":
				 * "410532198503262436"}
				 */
				if ("0".equals(item.optString("error"))) {
					AppTools.user = new Users();
					AppTools.user.setUid(item.optString("uid"));
					AppTools.user.setName(item.optString("name"));
					AppTools.user.setRealityName(item.optString("realityName"));
					AppTools.user.setBalance(item.optDouble("balance"));
					AppTools.user.setFreeze(item.optDouble("freeze"));
					AppTools.user.setEmail(item.optString("email"));
					// AppTools.user.setIdcardnumber(item
					// .optString("idcardnumber"));
					AppTools.user.setRechargeKey(item.optString("rechargeKey"));
					AppTools.user.setMobile(item.optString("mobile"));
					AppTools.user.setMsgCount(item.optInt("msgCount"));
					AppTools.user.setMsgCountAll(item.optInt("msgCountAll"));
					AppTools.user.setScoring(item.optInt("scoring"));
					// 用户密码 （没加密的）
					AppTools.user.setUserPass(userPass);
					AppTools.flag = true;
					return AppTools.ERROR_SUCCESS + "";
				} else {
					Log.i(tagName, item.optString("msg"));
					return item.optString("error");
				}
			} catch (Exception ex) {
				Log.i("login", "登录异常---" + ex.getMessage());
				ex.printStackTrace();
				return "-110";
			}
		}

		/**
		 * 最开始执行的
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			mProgress = BaseHelper.showProgress(LoginActivity.this, null,
					"登录中....", true, true);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			myHandler.sendEmptyMessage(Integer.parseInt(result));
			super.onPostExecute(result);
		}
	}

	/**
	 * 重写返回键事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			String str = getIntent().getStringExtra("loginType");
			if (str == null) {
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				LoginActivity.this.startActivity(intent);
			} else
				this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 处理页面显示的
	 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			mProgress.dismiss();
			switch (msg.what) {
			case 0:
				if (AppTools.user != null) {
					// 登录成功 关闭Activity
					LoginActivity.this.finish();
					String str = getIntent().getStringExtra("loginType");
					// if (str == null) {
					// // MainActivity.transaction.replace(R.id.main_center,new
					// // MyCenterFragment(), "center");
					// // MainActivity.transaction.commitAllowingStateLoss();
					// MainActivity.toCenter();
					// }
					// 将用户名记住
					settings = getSharedPreferences("app_user", 0);
					Editor editor = settings.edit();
					editor.putString("name", userName);
					Log.i("name", userName);
					editor.putString("pass", userPass);
					Log.i("pass", userPass);
					editor.putBoolean("isLogin", true);
					editor.commit();
					MyToast.getToast(LoginActivity.this, "登录成功").show();
				}
				break;
			case -1:
				MyToast.getToast(LoginActivity.this, "登录失败").show();
				break;
			case -110:
				MyToast.getToast(LoginActivity.this, "登录失败").show();
				break;
			case -500:
				MyToast.getToast(LoginActivity.this, "连接超时").show();
				break;
			case -113:
				MyToast.getToast(LoginActivity.this, "用户密码错误").show();
				break;
			case -112:
				MyToast.getToast(LoginActivity.this, "用户名不存在").show();
				break;
			default:
				break;
			}
			if (myAsyn != null
					&& myAsyn.getStatus() == AsyncTask.Status.RUNNING) {
				myAsyn.cancel(true); // 如果Task还在运行，则先取消它
			}
			Log.i("x", "执行了");
			super.handleMessage(msg);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		super.onNewIntent(intent);
	}

}
