package com.sm.sls_app.ui;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.ui.adapter.MyGridViewAdapter;
import com.sm.sls_app.ui.adapter.Qlc_Gridview_Adapter;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.NetWork;
import com.sm.sls_app.utils.NumberTools;
import com.sm.sls_app.view.ConfirmDialog;
import com.sm.sls_app.view.MyGridView;
import com.sm.sls_app.view.SmanagerView;
import com.sm.sls_app.view.VibratorView;

/**
 * 七乐彩 的选球页面 功能：选号界面，实现选号功能 版本
 * 
 * @author Kinwee 修改日期：2014-12-3
 * 
 */

public class Select_QlcActivity extends Activity implements OnClickListener,
		SensorEventListener {
	private final static String TAG = "Select_QlcActivity";

	/* 头部 */
	private RelativeLayout layout_top_select;// 顶部布局
	private ImageButton btn_back; // 返回
	private LinearLayout layout_select_playtype;// 玩法选择
	private ImageView iv_up_down;// 玩法提示图标
	private Button btn_playtype;// 玩法
	private ImageButton btn_help;// 帮助
	private ConfirmDialog dialog;// 提示框

	/* 尾部 */
	private Button btn_clearall; // 清除全部
	private Button btn_submit; // 选好了
	public TextView tv_tatol_count;// 总注数
	public TextView tv_tatol_money;// 总钱数

	/* 中间部分 */
	private ScrollView sv_show_ball;
	private TextView tv_lotteryname;// 彩种名
	// 中奖的红色蓝色号码
	private TextView tv_selected_redball, tv_selected_blueball;
	private LinearLayout layout_shake;// 摇一摇
	private ImageView iv_shake;// 摇一摇
	private TextView tv_shake;// 摇一摇

	private MyGridView mgv;
	private Qlc_Gridview_Adapter mgv_Adapter;

	private String selected_redball;// 中奖的红球号码 tv_selected_redball
	private String selected_blueball; // 中奖的蓝球号码 tv_selected_blueball

	private boolean spinnerIsSelect = false; // 下拉框是否被点击

	private Bundle bundle;

	public Vibrator vibrator; // 震动器
	private SensorManager mSmanager; // 传感器

	private Integer[] reds = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
			15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30 };

	/** 传感器 */
	float bx = 0;
	float by = 0;
	float bz = 0;
	long btime = 0;// 这一次的时间
	private long vTime = 0; // 震动的时间

	private SharedPreferences settings;
	private Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_select_number_qlc);
		App.activityS.add(this);
		App.activityS1.add(this);
		findView();
		init();
		setListener();
	}

	/** 初始化UI */
	private void findView() {
		bundle = new Bundle();
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_playtype = (Button) findViewById(R.id.btn_playtype);
		btn_help = (ImageButton) findViewById(R.id.btn_help);
		iv_up_down = (ImageView) findViewById(R.id.iv_up_down);
		layout_select_playtype = (LinearLayout) findViewById(R.id.layout_select_playtype);
		btn_clearall = (Button) findViewById(R.id.btn_clearall);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		tv_lotteryname = (TextView) findViewById(R.id.tv_lotteryname);
		layout_shake = (LinearLayout) findViewById(R.id.layout_shake);
		iv_shake = (ImageView) findViewById(R.id.iv_shake);
		tv_shake = (TextView) findViewById(R.id.tv_shake);
		tv_selected_redball = (TextView) findViewById(R.id.tv_selected_redball);
		tv_selected_blueball = (TextView) findViewById(R.id.tv_selected_blueball);
		tv_tatol_count = (TextView) this.findViewById(R.id.tv_tatol_count);
		tv_tatol_money = (TextView) this.findViewById(R.id.tv_tatol_money);
		mSmanager = (SensorManager) getSystemService(SENSOR_SERVICE);
		layout_top_select = (RelativeLayout) findViewById(R.id.layout_top_select);
		sv_show_ball = (ScrollView) findViewById(R.id.sv_show_ball);
		mgv = (MyGridView) this.findViewById(R.id.number_sv_center_gv_showRed);
		tv_lotteryname.setText("七乐彩");
		btn_playtype.setText("七乐彩");
		// 隐藏
		iv_up_down.setVisibility(View.GONE);
	}

	/** 初始化属性 上期开奖号码 */
	private void init() {
		SmanagerView.registerSensorManager(mSmanager, getApplicationContext(),
				this);// 注册传感器
		vibrator = VibratorView.getVibrator(getApplicationContext());
		if (NetWork.isConnect(Select_QlcActivity.this) == true) {
			if (AppTools.lottery != null) {
				if (AppTools.lottery.getLastWinNumber() != null) {
					String num = AppTools.lottery.getLastWinNumber();
					String str[] = num.split("-");
					if (str.length == 2) {
						selected_redball = str[0];
						selected_blueball = str[1];
					} else {
						selected_redball = num;
					}
				}
			}
			if(null!=selected_redball){
				selected_redball=selected_redball.replace(" ", "  ");
				tv_selected_redball.setText(selected_redball);
			}else{
				tv_selected_redball.setText("");
			}
		} else {
			Toast.makeText(Select_QlcActivity.this, "网络连接异常，获得数据失败！",
					Toast.LENGTH_SHORT).show();
		}
		mgv_Adapter = new Qlc_Gridview_Adapter(this, reds, Color.RED,
				VibratorView.getVibrator(getApplicationContext()));
		btn_back.setOnClickListener(this);
		btn_help.setOnClickListener(this);
		btn_clearall.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		layout_shake.setOnClickListener(this);
		iv_shake.setOnClickListener(this);
		tv_shake.setOnClickListener(this);
		settings = getSharedPreferences("app_user", 0);// 获取SharedPreference对象
		editor = settings.edit();// 获取编辑对象
		dialog = new ConfirmDialog(this, R.style.dialog);
	}

	/** 绑定监听 */
	private void setListener() {
		mgv.setAdapter(mgv_Adapter);
	}

	/** 公共点击监听 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/** 返回 **/
		case R.id.btn_back:
			exit();
			break;
		/** 提交号码 **/
		case R.id.btn_submit:
			submitNumber();
			break;
		/** 清空 **/
		case R.id.btn_clearall:
			clear();
			break;
		/** 玩法说明 **/
		case R.id.btn_help:
			playExplain();
			break;
		/** 机选 **/
		case R.id.layout_shake:
		case R.id.iv_shake:
		case R.id.tv_shake:
			if (null != vibrator)
				vibrator.vibrate(300);
			selectRandom();// 机选
			break;
		}
	}

	/** 从投注页面跳转过来 将投注页面的值 显示出来 */
	public void getItem() {
		Bundle bundle = Select_QlcActivity.this.getIntent()
				.getBundleExtra("bundle");
		sv_show_ball.scrollTo(0, 0);
		if (null != bundle) {
			Qlc_Gridview_Adapter.mSelected_numbers.clear();
			for (String str : bundle.getStringArrayList("mSelected_numbers")) {
				Qlc_Gridview_Adapter.mSelected_numbers.add(str);
			}
			updateAdapter();
		}
	}

	/** 玩法说明 */
	private void playExplain() {
		Intent intent = new Intent(Select_QlcActivity.this,
				PlayDescription.class);
		Select_QlcActivity.this.startActivity(intent);
	}

	/** 机选 按钮点击 */
	public void selectRandom() {
		mgv_Adapter.setMSelectedNumber(NumberTools.getRandomNum7(7, 30, true));
		updateAdapter();
		AppTools.totalCount = mgv_Adapter.getInvestmentCount();
		mgv_Adapter.notifyDataSetChanged();
		tv_tatol_count.setText(+AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}

	/** 提交号码 */
	private void submitNumber() {
		if (AppTools.totalCount < 1) {
			if (Qlc_Gridview_Adapter.mSelected_numbers.size() == 0) {
				mgv_Adapter.setMSelectedNumber(NumberTools.getRandomNum7(7, 30,
						true));
				updateAdapter();
				AppTools.totalCount = mgv_Adapter.getInvestmentCount();
				tv_tatol_count.setText(+AppTools.totalCount + "");
				tv_tatol_money.setText((AppTools.totalCount * 2) + "");
			} else {
				Toast.makeText(Select_QlcActivity.this, "请至少选择一注",
						Toast.LENGTH_SHORT).show();
			}
			return;
		}
		Intent intent = new Intent(Select_QlcActivity.this,
				Bet_QLC_Activity.class);
		Select_QlcActivity.this.startActivity(intent);
	}

	/** 清空 */
	private void clear() {
		clearHashSet();
		updateAdapter();
		AppTools.totalCount = 0;
		tv_tatol_count.setText(+AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}
	public void register() {
		getItem();
		tv_tatol_count.setText(+AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
		
			SmanagerView.registerSensorManager(mSmanager,
					getApplicationContext(), this);// 注册传感器
			vibrator = VibratorView.getVibrator(getApplicationContext());
		
	}
	public void unregister(){
		clear();
		vibrator = null;
		SmanagerView.unregisterSmanager(mSmanager, this);
		super.onStop();
	}
	/** 注册传感器 和 振动器 */
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(0== AppTools.totalCount ){
			Qlc_Gridview_Adapter.mSelected_numbers.clear();
			if(null!=mgv_Adapter){
				mgv_Adapter.notifyDataSetChanged();
			}
		}
		getItem();
		setInvestmentInfo();
		super.onResume();
	}

	/** 精确传感器 状态改变 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	/** 当传感器 状态改变的时候 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		// 现在检测时间
		long currentUpdateTime = System.currentTimeMillis();
		if (vTime == 0) {
			vTime = currentUpdateTime;
			Log.i("x", "执行了vTime---===");
		}
		// 两次检测的时间间隔
		long timeInterval = currentUpdateTime - btime;
		// 判断是否达到了检测时间间隔
		if (timeInterval < 150)
			return;
		// 现在的时间变成last时间
		btime = currentUpdateTime;
		// 获得x,y,z坐标
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		// 获得x,y,z的变化值
		float deltaX = x - bx;
		float deltaY = y - by;
		float deltaZ = z - bz;
		// 将现在的坐标变成last坐标
		bx = x;
		by = y;
		bz = z;

		double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
				* deltaZ)
				/ timeInterval * 10000;
		// Log.i("x", "间隔==  "+(vTime - currentUpdateTime));
		// 达到速度阀值，发出提示
		if (speed >= 900 && currentUpdateTime - vTime > 700) {
			vTime = System.currentTimeMillis();
			Log.i("x", "时间 间隔==== " + timeInterval);
			Log.i("x", "速度=== " + speed);
			if (null != vibrator)
				vibrator.vibrate(300);
			selectRandom();
			// //修改页面机选号码号码后面的值
			// setRandomNum(0,1);
			// setRandomNum(0,2);
		}
	}

	/** 重写返回键事件 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK)
			exit();
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		if (AppTools.list_numbers == null
				|| (AppTools.list_numbers != null && AppTools.list_numbers
				.size() == 0)) {
			if (Qlc_Gridview_Adapter.mSelected_numbers.size() != 0) {
				dialog.show();
				dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
					@Override
					public void getResult(int resultCode) {
						// TODO Auto-generated method stub
						if (1 == resultCode) {// 确定
							Select_QlcActivity.this.clearHashSet();
							AppTools.totalCount = 0;
							for (int i = 0; i < App.activityS1.size(); i++) {
								App.activityS1.get(i).finish();
							}
						}
					}
				});
			} else {
				Select_QlcActivity.this.clearHashSet();
				AppTools.totalCount = 0;
				for (int i = 0; i < App.activityS1.size(); i++) {
					App.activityS1.get(i).finish();
				}
			}
		} else if (AppTools.list_numbers != null
				&& AppTools.list_numbers.size() != 0) {
			if (Qlc_Gridview_Adapter.mSelected_numbers.size() != 0) {
				dialog.show();
				dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
					@Override
					public void getResult(int resultCode) {
						// TODO Auto-generated method stub
						if (1 == resultCode) {// 确定
							Select_QlcActivity.this.clearHashSet();
							AppTools.totalCount = 0;
							Select_QlcActivity.this.startActivity(new Intent(
									Select_QlcActivity.this,
									Bet_QLC_Activity.class));
							Select_QlcActivity.this.finish();
						}
					}
				});
			} else {
				Select_QlcActivity.this.clearHashSet();
				AppTools.totalCount = 0;
				Select_QlcActivity.this.startActivity(new Intent(
						Select_QlcActivity.this, Bet_QLC_Activity.class));
				Select_QlcActivity.this.finish();
			}
		}
	}

	public void setInvestmentInfo() {
		tv_tatol_count.setText(AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}
	
	/** 刷新Adapter */
	public void updateAdapter() {
		mgv_Adapter.notifyDataSetChanged();
		tv_tatol_count.setText(AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}

	/** 清空选中情况 */
	public static void clearHashSet() {
		if(null!=Qlc_Gridview_Adapter.mSelected_numbers){
			Qlc_Gridview_Adapter.mSelected_numbers.clear();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		super.onNewIntent(intent);
	}

}
