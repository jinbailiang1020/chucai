package com.sm.sls_app.ui;

import android.app.Activity;
import android.content.Context;
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
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.ui.adapter.MyGridViewAdapter;
import com.sm.sls_app.ui.adapter.MyGridViewAdapterPL5_QXC;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.NetWork;
import com.sm.sls_app.utils.NumberTools;
import com.sm.sls_app.view.ConfirmDialog;
import com.sm.sls_app.view.MyToast;
import com.sm.sls_app.view.SmanagerView;
import com.sm.sls_app.view.VibratorView;

/**
 * 选球页面 功能：选号界面，实现选号功能 版本
 * 
 * @author Administrator 修改日期：2014-12-2
 */
public class SelectNumberActivityPL5_QXC extends Activity implements
		OnClickListener, SensorEventListener {
	private final static String TAG = "SelectNumberActivityPL5_QXC";

	/* 头部 */
	private RelativeLayout layout_top_select;// 顶部布局
	private ImageButton btn_back; // 返回
	private LinearLayout layout_select_playtype;// 玩法选择
	private ImageView iv_up_down;// 玩法提示图标
	private Button btn_playtype;// 玩法
	private ImageButton btn_help;// 帮助
	private ConfirmDialog dialog;// 提示框

	private RelativeLayout fc3d_liu;
	private RelativeLayout fc3d_qi;

	private TextView txBai;
	private TextView txShi;
	private TextView txGe;
	private TextView txSi;
	private TextView txWu;
	private TextView txLiu;
	private TextView txQi;

	private int playType = 1;
	private Animation animation = null;

	private GridView gridView_bai; // 一
	private GridView gridView_shi; // 二
	private GridView gridView_ge; // 三
	private GridView gridView_si; // 四
	private GridView gridView_wu; // 五
	private GridView gridView_liu; // 六
	private GridView gridView_qi; // 七

	private MyGridViewAdapterPL5_QXC baiAdapter; // 百位Adapter
	private MyGridViewAdapterPL5_QXC shiAdapter; // 十位Adapter
	private MyGridViewAdapterPL5_QXC geAdapter; // 个位Adapter
	private MyGridViewAdapterPL5_QXC siAdapter; // 四
	private MyGridViewAdapterPL5_QXC wuAdapter; // 五
	private MyGridViewAdapterPL5_QXC liuAdapter; // 六
	private MyGridViewAdapterPL5_QXC qiAdapter; // 七

	private Integer[] bais = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	private Integer[] shis = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	private Integer[] ges = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	private Integer[] sis = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	private Integer[] wus = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	private Integer[] lius = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	private Integer[] qis = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

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
	private Bundle bundle;
	public Vibrator vibrator; // 震动器
	private SensorManager mSmanager; // 传感器

	// private PopupWindow popWindow;
	private int type = 1;

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

		setContentView(R.layout.activity_select_number_qxc_pl5);
		App.activityS.add(this);
		App.activityS1.add(this);
		clearHashSet();
		findView();
		init();
		setListener();
	}

	/** 初始化UI */
	private void findView() {
		bundle = new Bundle();
		txBai = (TextView) this.findViewById(R.id.txBai);
		txShi = (TextView) this.findViewById(R.id.txShi);
		txGe = (TextView) this.findViewById(R.id.txGe);
		txSi = (TextView) this.findViewById(R.id.txsi);
		txWu = (TextView) this.findViewById(R.id.txwu);
		txLiu = (TextView) this.findViewById(R.id.txliu);
		txQi = (TextView) this.findViewById(R.id.txqi);
		fc3d_liu = (RelativeLayout) findViewById(R.id.fc3d_liu);
		fc3d_qi = (RelativeLayout) findViewById(R.id.fc3d_qi);
		gridView_bai = (GridView) this
				.findViewById(R.id.number_sv_center_gv_showBai);
		gridView_shi = (GridView) this
				.findViewById(R.id.number_sv_center_gv_showShi);
		gridView_ge = (GridView) this
				.findViewById(R.id.number_sv_center_gv_showGe);
		gridView_si = (GridView) this
				.findViewById(R.id.number_sv_center_gv_showsi);
		gridView_wu = (GridView) this
				.findViewById(R.id.number_sv_center_gv_showwu);
		gridView_liu = (GridView) this
				.findViewById(R.id.number_sv_center_gv_showliu);
		gridView_qi = (GridView) this
				.findViewById(R.id.number_sv_center_gv_showqi);
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

		// 给Adapter赋值
		baiAdapter = new MyGridViewAdapterPL5_QXC(
				SelectNumberActivityPL5_QXC.this, bais, Color.RED, 1);
		shiAdapter = new MyGridViewAdapterPL5_QXC(
				SelectNumberActivityPL5_QXC.this, shis, Color.RED, 2);
		geAdapter = new MyGridViewAdapterPL5_QXC(
				SelectNumberActivityPL5_QXC.this, ges, Color.RED, 3);
		siAdapter = new MyGridViewAdapterPL5_QXC(
				SelectNumberActivityPL5_QXC.this, sis, Color.RED, 4);
		wuAdapter = new MyGridViewAdapterPL5_QXC(
				SelectNumberActivityPL5_QXC.this, wus, Color.RED, 5);
		liuAdapter = new MyGridViewAdapterPL5_QXC(
				SelectNumberActivityPL5_QXC.this, lius, Color.RED, 6);
		qiAdapter = new MyGridViewAdapterPL5_QXC(
				SelectNumberActivityPL5_QXC.this, qis, Color.RED, 7);
		// 隐藏
		iv_up_down.setVisibility(View.GONE);
	}

	/** 初始化属性 上期开奖号码 */
	private void init() {
		SmanagerView.registerSensorManager(mSmanager, getApplicationContext(),
				this);// 注册传感器

		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		String lottery_redNum = "";

		if (NetWork.isConnect(SelectNumberActivityPL5_QXC.this)) {
			if (AppTools.lottery.getLastWinNumber() != null) {
				String num = AppTools.lottery.getLastWinNumber();
				char[] tempStr = num.toCharArray();
				for (char c : tempStr) {
					lottery_redNum += c + " ";
				}
				lottery_redNum = lottery_redNum.trim();
			}
			tv_selected_redball.setText(lottery_redNum);
		} else {
			Toast.makeText(SelectNumberActivityPL5_QXC.this, "网络连接异常，获得数据失败！",
					Toast.LENGTH_SHORT).show();
		}
		btn_back.setOnClickListener(this);
		layout_select_playtype.setOnClickListener(this);
		iv_up_down.setOnClickListener(this);
		btn_playtype.setOnClickListener(this);
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
		// 绑定Adapter
		gridView_bai.setAdapter(baiAdapter);
		gridView_shi.setAdapter(shiAdapter);
		gridView_ge.setAdapter(geAdapter);
		gridView_si.setAdapter(siAdapter);
		gridView_wu.setAdapter(wuAdapter);
		gridView_liu.setAdapter(liuAdapter);
		gridView_qi.setAdapter(qiAdapter);

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


	/** 玩法说明 */
	private void playExplain() {
		Intent intent = new Intent(SelectNumberActivityPL5_QXC.this,
				PlayDescription.class);
		SelectNumberActivityPL5_QXC.this.startActivity(intent);
	}

	/** 机选 按钮点击 */
	public void selectRandom() {
		switch (playType) {
		case 1:
			// 得到百位的随机数
			MyGridViewAdapterPL5_QXC.baiSet = NumberTools.getRandomNum3(1, 10);
			// 得到十位的随机数
			MyGridViewAdapterPL5_QXC.shiSet = NumberTools.getRandomNum3(1, 10);
			// 得到个位的随机数
			MyGridViewAdapterPL5_QXC.geSet = NumberTools.getRandomNum3(1, 10);
			MyGridViewAdapterPL5_QXC.siSet = NumberTools.getRandomNum3(1, 10);
			MyGridViewAdapterPL5_QXC.wuSet = NumberTools.getRandomNum3(1, 10);
			if ("3".equals(AppTools.lottery.getLotteryID())) {
				MyGridViewAdapterPL5_QXC.liuSet = NumberTools.getRandomNum3(1,
						10);
				MyGridViewAdapterPL5_QXC.qiSet = NumberTools.getRandomNum3(1,
						10);
			}
			break;

		}
		// 随机选
		baiAdapter.setNumByRandom();
		SmanagerView.registerSensorManager(mSmanager, getApplicationContext(), this);
		vibrator = VibratorView.getVibrator(getApplicationContext());
	}

	/** 提交号码 */
	private void submitNumber() {
		if (MyGridViewAdapterPL5_QXC.playType == 1) {
			if (AppTools.totalCount == 0) {
				if ("3".equals(AppTools.lottery.getLotteryID())) {
                    //七星彩
					if (MyGridViewAdapterPL5_QXC.baiSet.size() == 0
							&& MyGridViewAdapterPL5_QXC.shiSet.size() == 0
							&& MyGridViewAdapterPL5_QXC.geSet.size() == 0
							&& MyGridViewAdapterPL5_QXC.siSet.size() == 0
							&& MyGridViewAdapterPL5_QXC.wuSet.size() == 0
							&& MyGridViewAdapterPL5_QXC.liuSet.size() == 0
							&& MyGridViewAdapterPL5_QXC.qiSet.size() == 0) {
						if (null != vibrator)
							vibrator.vibrate(300);
						selectRandom();
					} else {
						MyToast.getToast(SelectNumberActivityPL5_QXC.this,
								"请至少选择一注").show();
					}
				} else {
                    //排列5
					if (MyGridViewAdapterPL5_QXC.baiSet.size() == 0
							&& MyGridViewAdapterPL5_QXC.shiSet.size() == 0
							&& MyGridViewAdapterPL5_QXC.geSet.size() == 0
							&& MyGridViewAdapterPL5_QXC.siSet.size() == 0
							&& MyGridViewAdapterPL5_QXC.wuSet.size() == 0) {
						if (null != vibrator)
							vibrator.vibrate(300);
						selectRandom();
					} else {
						MyToast.getToast(SelectNumberActivityPL5_QXC.this,
								"请至少选择一注").show();
					}
				}
				return;
			}
		}

		Intent intent = new Intent(SelectNumberActivityPL5_QXC.this,
				BetActivityPL5_QXC.class);
		intent.putExtra("lotteryBundle", bundle);
		SelectNumberActivityPL5_QXC.this.startActivity(intent);
	}

	/** 清空 */
	private void clear() {
		clearHashSet();
		updateAdapter();
		AppTools.totalCount = 0;
		tv_tatol_count.setText(+AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}


	/** 注册传感器 和 振动器 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getItem();
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

	
	/**销毁传感器 和 振动器*/
	@Override
	protected void onStop() 
	{
		// TODO Auto-generated method stub
		clear();
		vibrator = null;
		SmanagerView.unregisterSmanager(mSmanager, this);
		super.onStop();
	}
	/** 将投注页面的值 显示出来 */
	private void getItem() {
		Intent intent = SelectNumberActivityPL5_QXC.this.getIntent();
		bundle = intent.getBundleExtra("bundle");
		sv_show_ball.scrollTo(0, 0);
		if (null != bundle) {
            clearHashSet();
			MyGridViewAdapterPL5_QXC.playType = bundle.getInt("type");
			if (bundle.getStringArrayList("bai") != null
					&& bundle.getStringArrayList("bai").size() > 0) {
				for (String str : bundle.getStringArrayList("bai")) {
					MyGridViewAdapterPL5_QXC.baiSet.add(str);
					Log.i("ss", "bai=" + str);
				}

			}
			if (bundle.getStringArrayList("shi") != null
					&& bundle.getStringArrayList("shi").size() > 0) {
				for (String str : bundle.getStringArrayList("shi")) {
					MyGridViewAdapterPL5_QXC.shiSet.add(str);
					Log.i("ss", "shi=" + str);
				}
			}
			if (bundle.getStringArrayList("ge") != null
					&& bundle.getStringArrayList("ge").size() > 0) {
				for (String str : bundle.getStringArrayList("ge")) {
					MyGridViewAdapterPL5_QXC.geSet.add(str);
					Log.i("ss", "ge=" + str);
				}
			}
			if (bundle.getStringArrayList("si") != null
					&& bundle.getStringArrayList("si").size() > 0) {
				for (String str : bundle.getStringArrayList("si")) {
					MyGridViewAdapterPL5_QXC.siSet.add(str);
					Log.i("ss", "si=" + str);
				}
			}
			if (bundle.getStringArrayList("wu") != null
					&& bundle.getStringArrayList("wu").size() > 0) {
				for (String str : bundle.getStringArrayList("wu")) {
					MyGridViewAdapterPL5_QXC.wuSet.add(str);
					Log.i("ss", "wu=" + str);
				}
			}

			if ("3".equals(AppTools.lottery.getLotteryID())) {
				if (bundle.getStringArrayList("liu") != null
						&& bundle.getStringArrayList("liu").size() > 0) {
					for (String str : bundle.getStringArrayList("liu")) {
						MyGridViewAdapterPL5_QXC.liuSet.add(str);
					}
				}
				if (bundle.getStringArrayList("qi") != null
						&& bundle.getStringArrayList("qi").size() > 0) {
					for (String str : bundle.getStringArrayList("qi")) {
						MyGridViewAdapterPL5_QXC.qiSet.add(str);
					}
				}
			}
			setGridViewVisible();
		}
		showLotteryName();
	}

	private void showLotteryName() {
		if ("64".equals(AppTools.lottery.getLotteryID())) {
			btn_playtype.setText("排列五");
			tv_lotteryname.setText("排列五");
			
		} else {
			btn_playtype.setText("七星彩");
			tv_lotteryname.setText("七星彩");
			
		}
		setGridViewVisible();
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
			if (MyGridViewAdapterPL5_QXC.geSet.size() != 0
					|| MyGridViewAdapterPL5_QXC.shiSet.size() != 0
					|| MyGridViewAdapterPL5_QXC.baiSet.size() != 0
					|| MyGridViewAdapterPL5_QXC.wuSet.size() != 0
					|| MyGridViewAdapterPL5_QXC.siSet.size() != 0
					|| MyGridViewAdapterPL5_QXC.liuSet.size() != 0
					|| MyGridViewAdapterPL5_QXC.qiSet.size() != 0) {
				dialog.show();
				dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
					@Override
					public void getResult(int resultCode) {
						// TODO Auto-generated method stub
						if (1 == resultCode) {// 确定
							clearHashSet();
							AppTools.totalCount = 0;
							for (int i = 0; i < App.activityS1.size(); i++) {
								App.activityS1.get(i).finish();
							}
						}
					}
				});
			} else {
				clearHashSet();
				AppTools.totalCount = 0;
				for (int i = 0; i < App.activityS1.size(); i++) {
					App.activityS1.get(i).finish();
				}
			}
		} else if (AppTools.list_numbers != null
				&& AppTools.list_numbers.size() != 0) {
			if (MyGridViewAdapterPL5_QXC.geSet.size() != 0
					|| MyGridViewAdapterPL5_QXC.shiSet.size() != 0
					|| MyGridViewAdapterPL5_QXC.baiSet.size() != 0
					|| MyGridViewAdapterPL5_QXC.wuSet.size() != 0
					|| MyGridViewAdapterPL5_QXC.siSet.size() != 0
					|| MyGridViewAdapterPL5_QXC.liuSet.size() != 0
					|| MyGridViewAdapterPL5_QXC.qiSet.size() != 0) {
				dialog.show();
				dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
					@Override
					public void getResult(int resultCode) {
						// TODO Auto-generated method stub
						if (1 == resultCode) {// 确定
							clearHashSet();
							AppTools.totalCount = 0;
							SelectNumberActivityPL5_QXC.this
									.startActivity(new Intent(
											SelectNumberActivityPL5_QXC.this,
											BetActivityPL5_QXC.class));
							SelectNumberActivityPL5_QXC.this.finish();
						}
					}
				});
			} else {
				clearHashSet();
				AppTools.totalCount = 0;
				SelectNumberActivityPL5_QXC.this.startActivity(new Intent(
						SelectNumberActivityPL5_QXC.this,
						BetActivityPL5_QXC.class));
				SelectNumberActivityPL5_QXC.this.finish();
			}
		}
	}

	/** 设置胆拖 区可见 */
	private void setGridViewVisible() {
		if ("3".equals(AppTools.lottery.getLotteryID())) {
			txBai.setText("一");
			txShi.setText("二");
			txGe.setText("三");
			txSi.setText("四");
			txWu.setText("五");
			fc3d_liu.setVisibility(View.VISIBLE);
			fc3d_qi.setVisibility(View.VISIBLE);
		} else if ("64".equals(AppTools.lottery.getLotteryID())) {
			txBai.setText("万");
			txShi.setText("千");
			txGe.setText("百");
			txSi.setText("十");
			txWu.setText("个");
			fc3d_liu.setVisibility(View.GONE);
			fc3d_qi.setVisibility(View.GONE);
		}

		SmanagerView.registerSensorManager(mSmanager, getApplicationContext(), this);
		vibrator = VibratorView.getVibrator(getApplicationContext());

		// 刷新Adapter
		updateAdapter();

		tv_tatol_count.setText(AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}

	/** 刷新Adapter */
	public void updateAdapter() {
		baiAdapter.notifyDataSetChanged();
		geAdapter.notifyDataSetChanged();
		shiAdapter.notifyDataSetChanged();
		siAdapter.notifyDataSetChanged();
		wuAdapter.notifyDataSetChanged();
		liuAdapter.notifyDataSetChanged();
		qiAdapter.notifyDataSetChanged();
	}

	/** 清空选中情况 */
	public static void clearHashSet() {
		if(null!=MyGridViewAdapterPL5_QXC.baiSet){
			MyGridViewAdapterPL5_QXC.baiSet.clear();
		}
		if(null!=MyGridViewAdapterPL5_QXC.shiSet){
			MyGridViewAdapterPL5_QXC.shiSet.clear();
		}
		if(null!=MyGridViewAdapterPL5_QXC.geSet){
			MyGridViewAdapterPL5_QXC.geSet.clear();
		}
		if(null!=MyGridViewAdapterPL5_QXC.siSet){
			MyGridViewAdapterPL5_QXC.siSet.clear();
		}
		if(null!=MyGridViewAdapterPL5_QXC.wuSet){
			MyGridViewAdapterPL5_QXC.wuSet.clear();
		}
		if(null!=MyGridViewAdapterPL5_QXC.liuSet){
			MyGridViewAdapterPL5_QXC.liuSet.clear();
		}
		if(null!=MyGridViewAdapterPL5_QXC.qiSet){
			MyGridViewAdapterPL5_QXC.qiSet.clear();
		}
	}

	@Override
	protected void onDestroy() {
		App.activityS.remove(this);
		super.onDestroy();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		super.onNewIntent(intent);
	}

}
