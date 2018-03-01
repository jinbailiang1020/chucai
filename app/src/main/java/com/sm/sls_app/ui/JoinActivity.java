package com.sm.sls_app.ui;

import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.SelectedNumbers;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.view.MyToast;

/**
 * 合买
 * 
 * @author Administrator
 * 
 */
public class JoinActivity extends Activity implements OnClickListener {

	private final static String TAG = "JoinActivity";
	private ImageButton btn_back; // 返回
	private ImageView iv_up_down;// 玩法提示图标
	private Button btn_playtype;// 玩法
	/** 是否追号 */
	private int chase;
	private TextView follow_total_money, follow_total_copy; // 方案金额// 多少份

	// 每份金额
	private Button follow_btn_money1, follow_btn_money2, follow_btn_money5,
			follow_btn_money10, follow_btn_money100;// 1元，2元,5,10,100

	private EditText follow_et_yj;// 中奖佣金
	private Button follow_btn_sub_yj;// 减
	private Button follow_btn_add_yj;// 加

	private Button follow_btn_sub_wantbuy;// 减
	private Button follow_btn_add_wantbuy;// 加

	private Button follow_btn_sub_bd;// 减
	private Button follow_btn_add_bd;// 加
	/** 最低佣金 */
	private int lowBounds = 0;
	/** 中奖后是否停止追号 */
	private int isStopChase = 1;
	/** 总注数 */
	private long sumCount = 1;
	// // 显示隐藏佣金模块
	// private LinearLayout ll_yong;

	// // 佣金比
	// private Button btn_BonusScale1, btn_BonusScale2, btn_BonusScale3,
	// btn_BonusScale4, btn_BonusScale5, btn_BonusScale6, btn_BonusScale7,
	// btn_BonusScale8, btn_BonusScale9, btn_BonusScale10;

	private String opt = "11"; // 格式化后的 opt

	private String auth, info, time, imei, crc; // 格式化后的参数
	/** follow_et_wantbuy 购买份数//follow_et_bd 保底份数 */
	private EditText follow_et_wantbuy, follow_et_bd;
	private EditText follow_et_name, follow_et_description; // 方案标题// 方案描述

	// 密码设置
	private Button follow_btn_public, follow_btn_toend, follow_btn_afterwin;

	private TextView follow_tv_total_money; // 应付金额

	private Button btn_submit;
	private ImageButton btn_help;
	/** 一共有多少份 */
	private long totalCount;
	/** 方案总金额 */
	private long totalMoney;
	/** 每份金额 */
	private double shareMoney = 1;
	/** payMoney应付金额// count购买的份数 */
	private long payMoney = 0, count = 0;
	/** 佣金 */
	private int Bonus = 2;
	/** 保密设置 */
	private int secrecyLevel = 0;
	/** 保底份数 */
	private int baoCount = 0;
	/** 方案名称 */
	private String title;
	/** 方案内容 */
	private String content;

	private boolean flag = false; // 是否追号。
	/** 追多少期 */
	private int qiHao;
	/** 至少要买的份数 */
	private long fen; // 至少要买的份数

	private MyHandler myHandler;
	private MyAsynTask myAsynTask;

	private String messgae;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_join);

		App.activityS1.add(this);

		findView();

		init();

		setListener();

	}

	/** 初始化UI */
	private void findView() {
		myHandler = new MyHandler();
		follow_total_money = (TextView) this
				.findViewById(R.id.follow_total_money);
		follow_total_copy = (TextView) this
				.findViewById(R.id.follow_total_copy);

		follow_btn_money1 = (Button) this.findViewById(R.id.follow_btn_money1);
		follow_btn_money2 = (Button) this.findViewById(R.id.follow_btn_money2);
		follow_btn_money5 = (Button) this.findViewById(R.id.follow_btn_money5);
		follow_btn_money10 = (Button) this
				.findViewById(R.id.follow_btn_money10);
		follow_btn_money100 = (Button) this
				.findViewById(R.id.follow_btn_money100);
		btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		iv_up_down = (ImageView) this.findViewById(R.id.iv_up_down);
		btn_playtype = (Button) this.findViewById(R.id.btn_playtype);
		follow_et_yj = (EditText) this.findViewById(R.id.follow_et_yj);
		follow_btn_sub_yj = (Button) this.findViewById(R.id.follow_btn_sub_yj);
		follow_btn_add_yj = (Button) this.findViewById(R.id.follow_btn_add_yj);
		follow_btn_sub_wantbuy = (Button) this
				.findViewById(R.id.follow_btn_sub_wantbuy);
		follow_btn_add_wantbuy = (Button) this
				.findViewById(R.id.follow_btn_add_wantbuy);
		follow_btn_sub_bd = (Button) this.findViewById(R.id.follow_btn_sub_bd);
		follow_btn_add_bd = (Button) this.findViewById(R.id.follow_btn_add_bd);

		follow_et_wantbuy = (EditText) this
				.findViewById(R.id.follow_et_wantbuy);
		follow_et_bd = (EditText) this.findViewById(R.id.follow_et_bd);

		follow_btn_public = (Button) this.findViewById(R.id.follow_btn_public);
		follow_btn_toend = (Button) this.findViewById(R.id.follow_btn_toend);
		follow_btn_afterwin = (Button) this
				.findViewById(R.id.follow_btn_afterwin);

		follow_et_name = (EditText) this.findViewById(R.id.follow_et_name);
		follow_et_description = (EditText) this
				.findViewById(R.id.follow_et_description);

		follow_tv_total_money = (TextView) this
				.findViewById(R.id.follow_tv_total_money);

		btn_submit = (Button) this.findViewById(R.id.btn_submit);
		btn_help = (ImageButton) this.findViewById(R.id.btn_help);
	}

	/** 初始化属性 */
	private void init() {
		Intent intent = getIntent();
		if (null != intent) {
			totalMoney = intent.getLongExtra("totalMoney", 0);
			isStopChase = intent.getIntExtra("isStopChase", 1);
			sumCount = intent.getLongExtra("sumCount", 1);
			if (intent.getStringExtra("flag") != null) {
				this.flag = true;
			} else if (intent.getStringExtra("qiHao") != null) {
				String q = intent.getStringExtra("qiHao");
				qiHao = Integer.parseInt(q);
			}
		}
		if (AppTools.qi > 1) {
			chase = 1;
		} else {
			chase = 0;
		}
		follow_total_money.setText(totalMoney  + "");
		follow_total_copy.setText(totalMoney / 1 + "");
		totalCount = totalMoney;
		if (!"".equals(AppTools.followLeastBuyScale)) {
			fen = (int) Math.ceil(totalCount
					* Double.parseDouble(AppTools.followLeastBuyScale));
		}
		String commission = AppTools.followCommissionScale;
		String num = commission.substring(commission.indexOf(".") + 1);
		shareMoney = 1.0;
		lowBounds = Integer.parseInt(num);
		follow_et_yj.setText(Bonus + "");
		count = fen;
		follow_et_wantbuy.setText(fen + "");
		setTextChange();
		baoCount = (int) (totalCount - count);
		follow_et_bd.setText(baoCount + "");
		iv_up_down.setVisibility(View.GONE);
		btn_playtype.setText("发起合买");
	}

	/** 设置监听 */
	private void setListener() {
		follow_btn_money1.setOnClickListener(this);
		follow_btn_money2.setOnClickListener(this);
		follow_btn_money5.setOnClickListener(this);
		follow_btn_money10.setOnClickListener(this);
		follow_btn_money100.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		follow_btn_public.setOnClickListener(this);
		follow_btn_toend.setOnClickListener(this);
		follow_btn_afterwin.setOnClickListener(this);

		btn_submit.setOnClickListener(this);
		btn_help.setOnClickListener(this);

		follow_et_wantbuy.addTextChangedListener(watcher_wantbuy);
		follow_btn_sub_wantbuy.setOnClickListener(this);
		follow_btn_add_wantbuy.setOnClickListener(this);
		follow_et_bd.addTextChangedListener(watcher_bd);
		follow_btn_sub_bd.setOnClickListener(this);
		follow_btn_add_bd.setOnClickListener(this);
		follow_et_yj.addTextChangedListener(yj_textWatcher);
		follow_btn_sub_yj.setOnClickListener(this);
		follow_btn_add_yj.setOnClickListener(this);
	}

	public void changEditText(EditText et, int type) {
		if (null != et) {
			String content = et.getText().toString();
			int number = Integer.parseInt(content);
			if (1 == type) {// 减
				number -= 1;
			} else if (2 == type) {// 加
				number += 1;
			}
			et.setText(number + "");
		}
	}

	/** 当佣金文本的值改变时 */
	private TextWatcher yj_textWatcher = new TextWatcher() {
		int bounds = 2;

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void afterTextChanged(Editable edt) {
			if ((edt.toString().trim()).length() == 0) {
				bounds = 0;
				follow_et_yj.setText(0 + "");
				return;
			}

			if (Integer.parseInt(edt.toString().trim()) > 10) {
				bounds = 10;
				follow_et_yj.setText(10 + "");// 最高佣金为10%
				MyToast.getToast(JoinActivity.this, "最高佣金为10‰").show();
			} else if (Integer.parseInt(edt.toString().trim()) < 0) {// 低于最低佣金
				bounds = lowBounds;
				follow_et_yj.setText(0 + "");// 最低佣金
				MyToast.getToast(JoinActivity.this, "最低佣金为" + 0 + "‰").show();
			} else if (Integer.parseInt(edt.toString().trim()) > 0) {
				if (Integer.parseInt(follow_et_wantbuy.getText().toString()) < fen) {
					count = fen;
					follow_et_wantbuy.setText(fen + "");
					follow_et_wantbuy.setSelection(follow_et_wantbuy.getText()
							.length());
				}
			} else {
				bounds = Integer.parseInt(edt.toString().trim());
			}
			Bonus = bounds;
			setCursorPosition(follow_et_yj);
		}
	};

	public void setCursorPosition(EditText et) {
		CharSequence text = et.getText();
		if (text instanceof Spannable) {
			Selection.setSelection((Spannable) text, text.length());
		}
	}

	/** 份额文本框文本 监听 */
	private TextWatcher watcher_wantbuy = new TextWatcher() {
		@Override
		public void afterTextChanged(Editable s) {
			if ((s.toString().trim()).length() == 0) {
				count = fen;
				follow_et_wantbuy.setText(count + "");
				follow_et_wantbuy.setSelection(follow_et_wantbuy.getText()
						.length());
				return;
			}
			int max = (Integer.parseInt(follow_total_copy.getText().toString()));
			if ("".equals(s.toString())) {// 为空
				count = fen;
				Log.i(TAG, "最低份数---" + fen);
				follow_et_wantbuy.setText(fen + "");
				follow_et_wantbuy.setSelection(follow_et_wantbuy.getText()
						.length());
			} else {
				if (Integer.parseInt(s.toString()) > max) {
					MyToast.getToast(JoinActivity.this, "最多只能买" + max + "份.")
							.show();
					count = max;
					follow_et_wantbuy.setText(max + "");
					follow_et_wantbuy.setSelection(follow_et_wantbuy.getText()
							.length());
				} else if (Integer.parseInt(s.toString()) < 0) {
					MyToast.getToast(JoinActivity.this, "最少购买" + 0 + "份.")
							.show();
					count = 0;
					follow_et_wantbuy.setText(0 + "");
					follow_et_wantbuy.setSelection(follow_et_wantbuy.getText()
							.length());
				} else if (Integer.parseInt(s.toString()) < fen) {
					count = Integer.parseInt(s.toString());
					Bonus = 0;
					follow_et_yj.setText(0 + "");
				} else {
					count = Integer.parseInt(s.toString());
				}
			}
			follow_et_bd.setText(totalCount - count + "");
			baoCount = (int) (totalCount - count);
			setTextChange();
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

	/** 保底份数文本框文本 监听 */
	private TextWatcher watcher_bd = new TextWatcher() {
		@Override
		public void afterTextChanged(Editable s) {
			if ((s.toString().trim()).length() == 0) {
				baoCount = 0;
				follow_et_bd.setText("0");
				follow_et_bd.setSelection(follow_et_bd.getText().length());
				return;
			}
			int buyCount = Integer.parseInt(follow_et_wantbuy.getText()
					.toString().trim());
			int max = (int) (Integer.parseInt(follow_total_copy.getText()
					.toString()) - buyCount);
			if (Integer.parseInt(s.toString()) > max) {
				if (0 == max) {
					baoCount = 0;
					follow_et_bd.setText("0");
					follow_et_bd.setSelection(follow_et_bd.getText().length());
					MyToast.getToast(JoinActivity.this, "没有可保底份数").show();
				} else {
					baoCount = max;
					follow_et_bd.setText(max + "");
					MyToast.getToast(JoinActivity.this, "最多只能保底" + max + "份")
							.show();
					follow_et_bd.setSelection(follow_et_bd.getText().length());
				}
			} else if (Integer.parseInt(s.toString()) < 0) {
				follow_et_bd.setText("0");
				baoCount = 0;
				follow_et_bd.setSelection(follow_et_bd.getText().length());
			} else if (Integer.parseInt(s.toString()) > 0) {
				if (Integer.parseInt(s.toString()) < Math.ceil(max * 0.05)) {
					baoCount = (int) Math.ceil(max * 0.05);
					MyToast.getToast(JoinActivity.this,
							"选择保底就不能低于" + baoCount + "份").show();
					follow_et_bd.setText(baoCount + "");
					follow_et_bd.setSelection(follow_et_bd.getText().length());
				}
			} else {
				baoCount = Integer.parseInt(s.toString().trim());
			}
			setTextChange();
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

	/** 选出每份 的金额 */
	public void setShareMoney(Button btn) {
		follow_btn_money1.setBackgroundResource(R.drawable.follow_btn_bg);
		follow_btn_money1.setTextColor(Color.BLACK);
		follow_btn_money1.setClickable(true);
		follow_btn_money2.setBackgroundResource(R.drawable.follow_btn_bg);
		follow_btn_money2.setTextColor(Color.BLACK);
		follow_btn_money2.setClickable(true);
		follow_btn_money5.setBackgroundResource(R.drawable.follow_btn_bg);
		follow_btn_money5.setTextColor(Color.BLACK);
		follow_btn_money5.setClickable(true);
		follow_btn_money10.setBackgroundResource(R.drawable.follow_btn_bg);
		follow_btn_money10.setTextColor(Color.BLACK);
		follow_btn_money10.setClickable(true);
		follow_btn_money100.setBackgroundResource(R.drawable.follow_btn_bg);
		follow_btn_money100.setTextColor(Color.BLACK);
		follow_btn_money100.setClickable(true);
		btn.setBackgroundResource(R.color.bet_btn_text);
		btn.setTextColor(Color.WHITE);
		btn.setClickable(false);
	}

	/** 设置投注保密性 */
	public void setPass(Button btn, int level) {
		secrecyLevel = level;
		follow_btn_public.setBackgroundResource(R.drawable.follow_btn_bg);
		follow_btn_public.setTextColor(Color.BLACK);
		follow_btn_toend.setBackgroundResource(R.drawable.follow_btn_bg);
		follow_btn_toend.setTextColor(Color.BLACK);
		follow_btn_afterwin.setBackgroundResource(R.drawable.follow_btn_bg);
		follow_btn_afterwin.setTextColor(Color.BLACK);
		btn.setBackgroundResource(R.color.bet_btn_text);
		btn.setTextColor(Color.WHITE);

	}

	private void setOne() {
		follow_et_bd.setText("0");
		shareMoney = 1.0;
		setShareMoney(follow_btn_money1);
		totalCount = totalMoney / 1;
		// Log.i(TAG, "totalCount---"+totalCount);
		follow_total_copy.setText(totalCount + "");
		setTextChange();
		// Log.i(TAG,
		// "总份数"+Integer.parseInt(follow_total_copy.getText().toString().trim()));
		// Log.i(TAG,
		// "比例----"+Double.parseDouble(AppTools.followLeastBuyScale));
		fen = (int) Math.ceil(Integer.parseInt(follow_total_copy.getText()
				.toString().trim())
				* Double.parseDouble(AppTools.followLeastBuyScale));
		// Log.i(TAG, "fen----"+fen);
		follow_et_wantbuy.setText(fen + "");
		baoCount = (int) (totalCount - count);
		follow_et_bd.setText(baoCount + "");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 每份金额
		case R.id.follow_btn_money1:
			// Log.i(TAG, "totalMoney---"+totalMoney);
			setOne();
			break;
		case R.id.follow_btn_money2:
			if (totalMoney % 2 != 0) {
				setOne();
				return;
			}
			follow_et_bd.setText("0");
			// Log.i(TAG, "totalMoney---"+totalMoney);
			shareMoney = 2.0;
			setShareMoney(follow_btn_money2);
			totalCount = totalMoney / 2;
			// Log.i(TAG, "totalCount---"+totalCount);
			follow_total_copy.setText(totalCount + "");
			setTextChange();
			// Log.i(TAG,
			// "总份数"+Integer.parseInt(follow_total_copy.getText().toString().trim()));
			// Log.i(TAG,
			// "比例----"+Double.parseDouble(AppTools.followLeastBuyScale));
			fen = (int) Math.ceil(Integer.parseInt(follow_total_copy.getText()
					.toString().trim())
					* Double.parseDouble(AppTools.followLeastBuyScale));
			// Log.i(TAG, "fen----"+fen);
			follow_et_wantbuy.setText(fen + "");
			baoCount = (int) (totalCount - count);
			follow_et_bd.setText(baoCount + "");
			break;
		case R.id.follow_btn_money5:
			if (totalMoney % 5 != 0) {
				setOne();
				return;
			}
			follow_et_bd.setText("0");
			// Log.i(TAG, "totalMoney---"+totalMoney);
			shareMoney = 5.0;
			setShareMoney(follow_btn_money5);
			totalCount = totalMoney / 5;
			// Log.i(TAG, "totalCount---"+totalCount);
			follow_total_copy.setText(totalCount + "");
			setTextChange();
			// Log.i(TAG,
			// "总份数"+Integer.parseInt(follow_total_copy.getText().toString().trim()));
			// Log.i(TAG,
			// "比例----"+Double.parseDouble(AppTools.followLeastBuyScale));
			fen = (int) Math.ceil(Integer.parseInt(follow_total_copy.getText()
					.toString().trim())
					* Double.parseDouble(AppTools.followLeastBuyScale));
			// Log.i(TAG, "fen----"+fen);
			follow_et_wantbuy.setText(fen + "");
			baoCount = (int) (totalCount - count);
			follow_et_bd.setText(baoCount + "");
			break;
		case R.id.follow_btn_money10:
			if (totalMoney % 10 != 0) {
				setOne();
				return;
			}
			follow_et_bd.setText("0");
			// Log.i(TAG, "totalMoney---"+totalMoney);
			shareMoney = 10.0;
			setShareMoney(follow_btn_money10);
			totalCount = totalMoney / 10;
			// Log.i(TAG, "totalCount---"+totalCount);
			follow_total_copy.setText(totalCount + "");
			setTextChange();
			// Log.i(TAG,
			// "总份数"+Integer.parseInt(follow_total_copy.getText().toString().trim()));
			// Log.i(TAG,
			// "比例----"+Double.parseDouble(AppTools.followLeastBuyScale));
			fen = (int) Math.ceil(Integer.parseInt(follow_total_copy.getText()
					.toString().trim())
					* Double.parseDouble(AppTools.followLeastBuyScale));
			// Log.i(TAG, "fen----"+fen);
			follow_et_wantbuy.setText(fen + "");
			baoCount = (int) (totalCount - count);
			follow_et_bd.setText(baoCount + "");
			break;
		case R.id.follow_btn_money100:
			if (totalMoney % 100 != 0) {
				setOne();
				return;
			}
			follow_et_bd.setText("0");
			// Log.i(TAG, "totalMoney---"+totalMoney);
			shareMoney = 100.0;
			setShareMoney(follow_btn_money100);
			totalCount = totalMoney / 100;
			// Log.i(TAG, "totalCount---"+totalCount);
			follow_total_copy.setText(totalCount + "");
			setTextChange();
			// Log.i(TAG,
			// "总份数"+Integer.parseInt(follow_total_copy.getText().toString().trim()));
			// Log.i(TAG,
			// "比例----"+Double.parseDouble(AppTools.followLeastBuyScale));
			fen = (int) Math.ceil(Integer.parseInt(follow_total_copy.getText()
					.toString().trim())
					* Double.parseDouble(AppTools.followLeastBuyScale));
			// Log.i(TAG, "fen----"+fen);
			follow_et_wantbuy.setText(fen + "");
			baoCount = (int) (totalCount - count);
			follow_et_bd.setText(baoCount + "");
			break;
		// // 佣金比
		case R.id.follow_btn_sub_yj:
			changEditText(follow_et_yj, 1);// 减
			break;
		case R.id.follow_btn_add_yj:
			changEditText(follow_et_yj, 2);// 加
			break;
		// // 购买份数
		case R.id.follow_btn_sub_wantbuy:
			changEditText(follow_et_wantbuy, 1);// 减
			setTextChange();
			break;
		case R.id.follow_btn_add_wantbuy:
			changEditText(follow_et_wantbuy, 2);// 加
			setTextChange();
			break;
		// // 保底
		case R.id.follow_btn_sub_bd:
			changEditText(follow_et_bd, 1);// 减
			break;
		case R.id.follow_btn_add_bd:
			changEditText(follow_et_bd, 2);// 加
			break;
		// 投注保密设置
		case R.id.follow_btn_public:
			setPass(follow_btn_public, 0);
			break;
		case R.id.follow_btn_toend:
			setPass(follow_btn_toend, 1);
			break;
		case R.id.follow_btn_afterwin:
			setPass(follow_btn_afterwin, 2);
			break;
		// 付款
		case R.id.btn_submit:
			Log.i(TAG, "选了多少份" + count);
			setEnabled(false);
			myAsynTask = new MyAsynTask();
			myAsynTask.execute();
			break;
		// 帮助
		case R.id.btn_help:
			Intent intent = new Intent(JoinActivity.this,
					JoinHelpActivity.class);
			JoinActivity.this.startActivity(intent);
			break;
		case R.id.btn_back:
			JoinActivity.this.finish();
			break;
		}
	}

	/** 设置是否可用 **/
	private void setEnabled(boolean isEna) {
		btn_submit.setEnabled(isEna);
		follow_et_bd.setEnabled(isEna);
		follow_et_wantbuy.setEnabled(isEna);
		follow_et_description.setEnabled(isEna);
		follow_et_name.setEnabled(isEna);
		follow_btn_public.setEnabled(isEna);
		follow_btn_toend.setEnabled(isEna);
		follow_btn_afterwin.setEnabled(isEna);
		follow_btn_money1.setEnabled(isEna);
		follow_btn_money2.setEnabled(isEna);
		follow_btn_money5.setEnabled(isEna);
		follow_btn_money10.setEnabled(isEna);
		follow_btn_money100.setEnabled(isEna);
	}

	/** 更改显示文本 **/
	private void setTextChange() {
		Log.i(TAG, "改变了文本显示---");
		payMoney = (long) (Integer.parseInt(follow_et_wantbuy.getText()
				.toString().trim()) * shareMoney);
		Log.i(TAG, "payMoney---" + payMoney);
		follow_tv_total_money.setText(payMoney + "");
	}

	/*** 异步任务 用来后台获取数据 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> {
		/** 在后台执行的程序 */
		String error = "1001";

		@Override
		protected String doInBackground(Void... params) {
			String error = "-1";
			if (AppTools.list_numbers.size() > 0) {
				time = RspBodyBaseBean.getTime();
				imei = RspBodyBaseBean.getIMEI(getApplicationContext());

				if (AppTools.user != null) {
					String key = MD5.md5(AppTools.user.getUserPass()
							+ AppTools.MD5_key);
					/** 方案总注数 */
					int schemeSumNum = 0;
					int schemeSumMoney = 0;
					for (SelectedNumbers num : AppTools.list_numbers) {
						schemeSumNum += num.getCount();
						schemeSumMoney += num.getMoney();
					}
					if (AppTools.qi == 1) {
						info = "{\"autoStopAtWinMoney\":\""
								+ isStopChase
								+ "\",\"lotteryId\":\""
								+ AppTools.lottery.getLotteryID()
								+ "\",\"isuseId\":\""
								+ AppTools.lottery.getIsuseId()
								+ "\","
								+ "\"multiple\":\""
								+ AppTools.bei
								+ "\",\"share\":\""
								+ totalCount
								+ "\",\"buyShare\":\""
								+ count
								+ "\","
								+ "\"assureShare\":\""
								+ baoCount
								+ "\",\"title\":\""
								+ title
								+ "\",\"description\":\""
								+ content
								+ "\",\"secrecyLevel\":"
								+ "\""
								+ secrecyLevel
								+ "\",\"schemeBonusScale\":\""
								+ Bonus
								+ "\",\"schemeSumMoney\":\""
								+ schemeSumMoney
								+ "\","
								+ "\"schemeSumNum\":\""
								+ schemeSumNum
								+ "\",\"chase\":\""
								+ chase
								+ "\",\"chaseBuyedMoney\":\""
								+ totalMoney
								+ "\","
								+ "\"buyContent\": [ "
								+ RspBodyBaseBean
										.getBuyContent(AppTools.list_numbers)
								+ "], \"chaseList\": [" + "{\"isuseId\":\""
								+ AppTools.lottery.getIsuseId()
								+ "\",\"multiple\":\""
								+ AppTools.beiList.get(0) + "\","
								+ "\"money\":\"" + AppTools.beiList.get(0)
								* sumCount * 2 + "\"}" + "] }";
					} else {
						info = "{\"autoStopAtWinMoney\":\""
								+ isStopChase
								+ "\",\"lotteryId\":\""
								+ AppTools.lottery.getLotteryID()
								+ "\",\"isuseId\":\""
								+ AppTools.lottery.getIsuseId()
								+ "\","
								+ "\"multiple\":\""
								+ AppTools.bei
								+ "\",\"share\":\""
								+ totalCount
								+ "\",\"buyShare\":\""
								+ count
								+ "\","
								+ "\"assureShare\":\""
								+ baoCount
								+ "\",\"title\":\""
								+ title
								+ "\",\"description\":\""
								+ content
								+ "\",\"secrecyLevel\":"
								+ "\""
								+ secrecyLevel
								+ "\",\"schemeBonusScale\":\""
								+ Bonus
								+ "\",\"schemeSumMoney\":\""
								+ schemeSumMoney
								+ "\","
								+ "\"schemeSumNum\":\""
								+ schemeSumNum
								+ "\",\"chase\":\""
								+ chase
								+ "\",\"chaseBuyedMoney\":\""
								+ totalMoney
								+ "\","
								+ "\"buyContent\": [ "
								+ RspBodyBaseBean
										.getBuyContent(AppTools.list_numbers)
								+ "], \"chaseList\": ["
								+ RspBodyBaseBean.getChaseList(AppTools.qi,
										sumCount) + "] }";
					}
					Log.i("x", "join_info" + info);

					crc = RspBodyBaseBean.getCrc(time, imei, key, info,
							AppTools.user.getUid());

					auth = RspBodyBaseBean.getAuth(crc, time, imei,
							AppTools.user.getUid());
					String values[] = { opt, auth, info };
					String result = HttpUtils.doPost(AppTools.names, values,
							AppTools.path);

					if ("-500".equals(result))
						return result;

					Log.i("x", "join_result=====" + result);

					try {
						JSONObject object = new JSONObject(result);
						error = object.optString("error");
						if ("0".equals(error)) {
							AppTools.user.setBalance(object
									.optDouble("balance"));
							AppTools.user.setFreeze(object.optDouble("freeze"));
							AppTools.schemeId = object.optInt("schemeids");
							AppTools.lottery.setChaseTaskID(object
									.optInt("chasetaskids"));
						} else if ("-160".equals(error)) {
							messgae = object.optString("msg");
							return "-160";
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					return "-100";
				}
			} else {
				return "-102";
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
			case 0:
				for (int i = 0; i < App.activityS1.size(); i++) {
					App.activityS1.get(i).finish();
				}
				if (null != AppTools.list_numbers)
					AppTools.list_numbers.clear();
				AppTools.totalCount = 0;
				clearAllLotterySelectData();
				Intent intent = new Intent(getApplicationContext(),
						PaySuccessActivity.class);
				intent.putExtra("paymoney", payMoney);
				JoinActivity.this.startActivity(intent);
				break;
			case AppTools.ERROR_UNLONGIN:
				MyToast.getToast(JoinActivity.this, "请先登陆").show();
				intent = new Intent(JoinActivity.this, LoginActivity.class);
				intent.putExtra("loginType", "join");
				JoinActivity.this.startActivity(intent);
				break;
			case -500:
				MyToast.getToast(JoinActivity.this, "连接超时").show();
				break;
			case -160:
				Toast.makeText(getApplicationContext(), messgae,
						Toast.LENGTH_LONG).show();
				for (int i = 0; i < App.activityS1.size(); i++) {
					App.activityS1.get(i).finish();
				}
				if (null != AppTools.list_numbers)
					AppTools.list_numbers.clear();
				AppTools.totalCount = 0;
				clearAllLotterySelectData();
				break;
			}
			setEnabled(true);
			super.handleMessage(msg);
		}

	}

	/**
	 * 清除所有彩种所选数据
	 */
	public static void clearAllLotterySelectData() {
		Buy_DLT_Activit.clearHashSet();
		Buy_RX9_Activit.clearHashSet();
		Buy_SFC_Activity.clearHashSet();
		Select_jczqActivity.clearHashSet();
		Select_QlcActivity.clearHashSet();
		SelectNumberActivity.clearHashSet();
		SelectNumberActivityFC3D.clearHashSet();
		SelectNumberActivityPL3.clearHashSet();
		SelectNumberActivityPL5_QXC.clearHashSet();
	}

}
