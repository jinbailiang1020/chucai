package com.sm.sls_app.fragment;

import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.ui.AccountInformationActivity;
import com.sm.sls_app.ui.BankInfoActivity;
import com.sm.sls_app.ui.FundsInfoActivity;
import com.sm.sls_app.ui.IdCardActivity;
import com.sm.sls_app.ui.IntegralActivity;
import com.sm.sls_app.ui.LoginActivity;
import com.sm.sls_app.ui.MainActivity;
import com.sm.sls_app.ui.MyAllLotteryActivity;
import com.sm.sls_app.ui.PayAccountActivity;
import com.sm.sls_app.ui.RechargeActivity;
import com.sm.sls_app.ui.SettingActivity;
import com.sm.sls_app.ui.WithdrawalActivity;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.view.MyToast;

/**
 * 个人中心
 * 
 * @author lian
 * 
 */
@SuppressLint("NewApi")
public class MyCenterFragment extends Fragment implements OnClickListener {

	private LinearLayout btn_all, btn_lottery, btn_in_lottery, btn_follow,
			btn_chipped, btn_user, btn_detail, btn_integral;
	private Button btn_draw_money;
	private ImageButton btn_back, btn_setting;
	private TextView tv_name, tv_balance, tv_freeze, tv_id;

	private SharedPreferences settings;

	private Intent intent;
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		View v = inflater
				.inflate(R.layout.activity_my_center, container, false);
		findView(v);
		setListener();
		return v;
	}

	/** 初始化UI */
	private void findView(View v) {
		// 退出按钮
		btn_back = (ImageButton) v.findViewById(R.id.btn_back);

		btn_all = (LinearLayout) v.findViewById(R.id.btn_all);
		btn_lottery = (LinearLayout) v.findViewById(R.id.btn_lottery);
		btn_in_lottery = (LinearLayout) v.findViewById(R.id.btn_in_lottery);
		btn_follow = (LinearLayout) v.findViewById(R.id.btn_follow);
		btn_chipped = (LinearLayout) v.findViewById(R.id.btn_chipped);
		btn_integral = (LinearLayout) v.findViewById(R.id.btn_integral);
		btn_user = (LinearLayout) v.findViewById(R.id.btn_userInfo);
		// btn_pay = (Button) v.findViewById(R.id.btn_pay);
		btn_draw_money = (Button) v.findViewById(R.id.btn_draw_money);
		btn_detail = (LinearLayout) v.findViewById(R.id.btn_detail);
		btn_setting = (ImageButton) v.findViewById(R.id.btn_setting);

		tv_name = (TextView) v.findViewById(R.id.center_tv_name);

		tv_balance = (TextView) v.findViewById(R.id.tv_freeze);
		tv_freeze = (TextView) v.findViewById(R.id.tv_freeze2);
		tv_id = (TextView) v.findViewById(R.id.tv_id);

	}

	/** 绑定监听 */
	private void setListener() {
		btn_back.setOnClickListener(this);
		btn_integral.setOnClickListener(this);
		btn_all.setOnClickListener(this);
		btn_lottery.setOnClickListener(this);
		btn_in_lottery.setOnClickListener(this);
		btn_follow.setOnClickListener(this);
		btn_chipped.setOnClickListener(this);

		btn_user.setOnClickListener(this);
		// btn_pay.setOnClickListener(this);
		btn_draw_money.setOnClickListener(this);
		btn_detail.setOnClickListener(this);
		btn_setting.setOnClickListener(this);
	}

	/** 公用点击监听 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			break;
		case R.id.btn_all:
			ToOtherActivity(0);
			break;
		case R.id.btn_lottery:
			ToOtherActivity(1);
			break;
		case R.id.btn_in_lottery:
			ToOtherActivity(2);
			break;
		case R.id.btn_follow:
			ToOtherActivity(3);
			break;
		case R.id.btn_chipped:
			ToOtherActivity(4);
			break;
		case R.id.btn_setting:
			Intent intent = new Intent(context, SettingActivity.class);
			context.startActivity(intent);
			break;
		case R.id.btn_userInfo:
			intent = new Intent(context, AccountInformationActivity.class);
			context.startActivity(intent);
			break;
		/** 充值 **/
		// case R.id.btn_pay:
		// // 跳到充值页面
		// intent = new Intent(context, RechargeActivity.class);
		// context.startActivity(intent);
		// break;
		case R.id.btn_draw_money:
			// myAsynTask = new MyAsynTask();
			// myAsynTask.execute();
			intent = new Intent(context, PayAccountActivity.class);
			context.startActivity(intent);
			break;
		case R.id.btn_detail:
			intent = new Intent(context, FundsInfoActivity.class);
			context.startActivity(intent);
			break;
		case R.id.btn_integral:
			intent = new Intent(context, IntegralActivity.class);
			context.startActivity(intent);
			break;
		}
	}

	/** 公用跳转方法 */
	private void ToOtherActivity(int lotteryType) {
		Intent intent = new Intent(context, MyAllLotteryActivity.class);
		intent.putExtra("index", lotteryType);
		context.startActivity(intent);
	}

	@Override
	public void onResume() {
		if (null == AppTools.user) {
			Log.i("x", "调用了。。。。。。。");
			Intent intent = new Intent(context, MainActivity.class);
			context.startActivity(intent);
		} else {
			Spanned html = Html.fromHtml("可用金额: <font color=\"#e3393c\"><big>"
					+ AppTools.user.getBalance() + "</big></font>元");

			tv_balance.setText(html);
			tv_freeze.setText("冻结金额: " + AppTools.user.getFreeze() + "元");
			tv_id.setText("ID:" + AppTools.user.getRechargeKey());
			tv_name.setText(AppTools.user.getName());
		}
		super.onResume();
	}

}
