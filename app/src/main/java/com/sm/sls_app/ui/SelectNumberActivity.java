package com.sm.sls_app.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
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
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
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
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.NetWork;
import com.sm.sls_app.utils.NumberTools;
import com.sm.sls_app.utils.PopupWindowUtil;
import com.sm.sls_app.view.ConfirmDialog;
import com.sm.sls_app.view.MyToast;
import com.sm.sls_app.view.SmanagerView;
import com.sm.sls_app.view.VibratorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 双色球 的选球页面 功能： 选号界面，实现选号
 * 
 * @author Kinwee 修改日期2014-12-23
 *
 */
public class SelectNumberActivity extends Activity implements OnClickListener,
		SensorEventListener {
	private final static String TAG = "SelectNumberActivity";

	/* 头部 */
	private RelativeLayout layout_top_select;// 顶部布局
	private ImageButton btn_back; // 返回
	private LinearLayout layout_select_playtype;// 玩法选择
	private ImageView iv_up_down;// 玩法提示图标
	private Button btn_playtype;// 玩法
	private ImageButton btn_help;// 帮助
	private ConfirmDialog dialog;// 提示框

	private Animation animation = null;

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

	/* 红球胆码 */
	private LinearLayout layout_tip_red;// 普通投注提示
	private LinearLayout layout_tip_red_tuo;// 胆拖投注提示
	private GridView gv_ball_red;// 红球-胆码

	/* 红球拖码 */
	private RelativeLayout layout_ball_redtuo; // 隐藏的层
	private GridView gv_ball_red_tuo; // 红球胆拖

	/* 蓝球胆码 */
	private GridView gv_ball_blue;// 蓝球

	private MyGridViewAdapter redAdapter, blueAdapter, redTuoAdapter; // 红球Adapter
	private String selected_redball;// 中奖的红球号码 tv_selected_redball
	private String selected_blueball; // 中奖的蓝球号码 tv_selected_blueball

	private Bundle bundle;

	public Vibrator vibrator; // 震动器
	private SensorManager mSmanager; // 传感器

	private Integer[] reds = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
			15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31,
			32, 33 };

	private Integer[] blues = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
			15, 16 };

	// private PopupWindow popWindow;
	private int type = 1;

	private ArrayList<String> listRed = new ArrayList<String>();
	private ArrayList<String> listBlue = new ArrayList<String>();
	private ArrayList<String> listRedTuo = new ArrayList<String>();

	/** 传感器 */
	float bx = 0;
	float by = 0;
	float bz = 0;
	long btime = 0;// 这一次的时间
	private long vTime = 0; // 震动的时间

	private SharedPreferences settings;
	private Editor editor;

	private PopupWindowUtil popUtil;

	private Map<Integer, Map<Integer, String>> data = new HashMap<Integer, Map<Integer, String>>();

	private int parentIndex;
	private int itemIndex;

	private Map<Integer, Integer> playtypeMap = new HashMap<Integer, Integer>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_select_number);

		clearHashSet();

		App.activityS.add(this);

		App.activityS1.add(this);

		findView();

		init();

		setListener();
	}

	/** 初始化UI */
	private void findView() {
		MyGridViewAdapter.playType = 501;
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
		layout_tip_red = (LinearLayout) findViewById(R.id.layout_tip_red);
		layout_tip_red_tuo = (LinearLayout) findViewById(R.id.layout_tip_red_tuo);

		layout_ball_redtuo = (RelativeLayout) this
				.findViewById(R.id.layout_ball_redtuo);
		tv_tatol_count = (TextView) this.findViewById(R.id.tv_tatol_count);
		tv_tatol_money = (TextView) this.findViewById(R.id.tv_tatol_money);
		gv_ball_red = (GridView) this.findViewById(R.id.gv_ball_red);
		gv_ball_blue = (GridView) this.findViewById(R.id.gv_ball_blue);
		gv_ball_red_tuo = (GridView) this.findViewById(R.id.gv_ball_red_tuo);
		mSmanager = (SensorManager) getSystemService(SENSOR_SERVICE);
		layout_top_select = (RelativeLayout) findViewById(R.id.layout_top_select);
		sv_show_ball = (ScrollView) findViewById(R.id.sv_show_ball);
	}

	/** 初始化属性 上期开奖号码 */
	private void init() {
		SmanagerView.registerSensorManager(mSmanager, getApplicationContext(),
				this);// 注册传感器
		vibrator = VibratorView.getVibrator(getApplicationContext());
		if (NetWork.isConnect(SelectNumberActivity.this)) {
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
			if (null != selected_redball) {
				selected_redball = selected_redball.replace(" ", "  ");
				tv_selected_redball.setText(selected_redball);
			} else {
				tv_selected_redball.setText("");
			}
			if (null != selected_blueball) {
				tv_selected_blueball.setText("  " + selected_blueball);
			} else {
				tv_selected_blueball.setText(" ");
			}
		} else {
			Toast.makeText(SelectNumberActivity.this, "网络连接异常，获得数据失败！",
					Toast.LENGTH_SHORT).show();
		}

		// 给Adapter赋值
		redAdapter = new MyGridViewAdapter(SelectNumberActivity.this, reds,
				Color.RED, 1);

		blueAdapter = new MyGridViewAdapter(SelectNumberActivity.this, blues,
				Color.BLUE, 3);

		redTuoAdapter = new MyGridViewAdapter(SelectNumberActivity.this, reds,
				Color.RED, 2);

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
		tv_lotteryname.setText("双色球");
		Map<Integer, String> playType = new HashMap<Integer, String>();
		playType.put(0, "普通投注");
		playType.put(1, "胆拖投注");
		Set<Integer> set = playType.keySet();
		int[] playtype_array = { 501, 502 };
		for (Integer i : set) {
			playtypeMap.put(playtype_array[i], i);
		}
		data.put(0, playType);
		dialog = new ConfirmDialog(this, R.style.dialog);
	}

	/** 绑定监听 */
	private void setListener() {
		// 绑定Adapter
		gv_ball_red_tuo.setAdapter(redTuoAdapter);
		gv_ball_red.setAdapter(redAdapter);
		gv_ball_blue.setAdapter(blueAdapter);

		layout_ball_redtuo.setOnClickListener(this);

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
		/** 选玩法 **/
		case R.id.layout_select_playtype:
		case R.id.btn_playtype:
		case R.id.iv_up_down:
			popUtil = new PopupWindowUtil(this, data, layout_top_select);
			popUtil.setSelectIndex(parentIndex, itemIndex);
			popUtil.createPopWindow();
			popUtil.setOnSelectedListener(new PopupWindowUtil.OnSelectedListener() {
				@Override
				public void getIndex(int parentIndex, int itemIndex) {
					// TODO Auto-generated method stub
					if (itemIndex != SelectNumberActivity.this.itemIndex) {
						SelectNumberActivity.this.parentIndex = parentIndex;
						SelectNumberActivity.this.itemIndex = itemIndex;
						changePlayType();
					}
					rote(2);// 旋转动画 向下
				}
			});
			rote(1);// 旋转动画 向上
			break;
		}
	}

	public void changePlayType() {
		btn_playtype.setText(data.get(parentIndex).get(itemIndex));
		switch (itemIndex) {
		case 0:// 普通投注
			MyGridViewAdapter.playType = 501;
			layout_tip_red_tuo.setVisibility(View.INVISIBLE);
			layout_tip_red.setVisibility(View.VISIBLE);
			setShakeBtnVisible(View.VISIBLE);
			setGridViewNotVisible();
			SmanagerView.registerSensorManager(mSmanager,
					getApplicationContext(), this);
			vibrator = VibratorView.getVibrator(getApplicationContext());
			break;
		case 1:// 胆拖投注
			MyGridViewAdapter.playType = 502;
			layout_tip_red_tuo.setVisibility(View.VISIBLE);
			layout_tip_red.setVisibility(View.INVISIBLE);
			setShakeBtnVisible(View.INVISIBLE);
			setGridViewVisible();
			vibrator = null;
			SmanagerView.unregisterSmanager(mSmanager, this);
			break;
		}
		AppTools.totalCount = 0;
		clearHashSet();
		updateAdapter();
		sv_show_ball.scrollTo(0, 0);
	}

	/**
	 * 旋转
	 * 
	 * @param type
	 *            1.向上 2.向下
	 */
	public void rote(int type) {
		if (1 == type) {
			animation = AnimationUtils.loadAnimation(getApplicationContext(),
					R.anim.rote_playtype_up);
		} else if (2 == type) {
			animation = AnimationUtils.loadAnimation(getApplicationContext(),
					R.anim.rote_playtype_down);
		}
		LinearInterpolator lin = new LinearInterpolator();
		animation.setInterpolator(lin);
		animation.setFillAfter(true);
		if (iv_up_down != null) {
			iv_up_down.startAnimation(animation);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (animation != null && iv_up_down != null && animation.hasStarted()) {
			iv_up_down.clearAnimation();
			iv_up_down.startAnimation(animation);
		}
	}

	/** 设置机选按钮是否可见 */
	private void setShakeBtnVisible(int v) {
		layout_shake.setVisibility(v);
	}

	/** 从投注页面跳转过来 将投注页面的值 显示出来 */
	public void getItem() {
		sv_show_ball.scrollTo(0, 0);
		Bundle bundle = null;
		Intent intent = SelectNumberActivity.this.getIntent();
		if (intent != null) {
			bundle = intent.getBundleExtra("bundle");
		}

		if (bundle != null) {
            clearHashSet();
			MyGridViewAdapter.playType = bundle.getInt("type");
			int type = MyGridViewAdapter.playType;
			itemIndex = playtypeMap.get(type);
			listRed = bundle.getStringArrayList("red");

			if (null != listRed) {
				for (String str : listRed) {
					MyGridViewAdapter.redSet.add(str);
				}
			} else {
				listRed = new ArrayList<String>();
			}

			listBlue = bundle.getStringArrayList("blue");
			if (null != listBlue) {
				for (String str : listBlue) {
					MyGridViewAdapter.blueSet.add(str);
				}
			} else {
				listBlue = new ArrayList<String>();
			}

			Log.i("x", "select--red==" + MyGridViewAdapter.redSet.size()
					+ "===blue===" + MyGridViewAdapter.blueSet.size());

			if (MyGridViewAdapter.playType == 501) {
				setGridViewNotVisible();
				layout_select_playtype.setEnabled(false);
			} else if (MyGridViewAdapter.playType == 502) {
				listRedTuo = bundle.getStringArrayList("redTuo");
				if (null != listRedTuo) {
					for (String str : listRedTuo) {
						MyGridViewAdapter.redTuoSet.add(str);
					}
				} else {
					listRedTuo = new ArrayList<String>();
				}
				setGridViewVisible();
				setShakeBtnVisible(View.GONE);
				layout_select_playtype.setEnabled(false);
			}
			if (AppTools.list_numbers.size() == 0) { // 判断返回式 是否原页面是否还有值。
				layout_select_playtype.setEnabled(true);
			}
			updateAdapter();
		}
		Log.i("x", "选球页面===" + MyGridViewAdapter.redSet.size() + "=="
				+ MyGridViewAdapter.blueSet.size());
	}

	/** 玩法说明 */
	private void playExplain() {
		Intent intent = new Intent(SelectNumberActivity.this,
				PlayDescription.class);
		SelectNumberActivity.this.startActivity(intent);
	}

	/** 机选 按钮点击 */
	public void selectRandom() {
		// 得到蓝球的随机数
		MyGridViewAdapter.blueSet = NumberTools.getRandomNum2(1, 16);
		// 得到红球的随机数
		MyGridViewAdapter.redSet = NumberTools.getRandomNum2(6, 33);
		// 刷新Adapter
		redAdapter.setNumByRandom();
	}

	/** 提交号码 */
	private void submitNumber() {
		if (MyGridViewAdapter.playType == 501) {
			if (AppTools.totalCount == 0
					&& MyGridViewAdapter.redSet.size() == 0
					&& MyGridViewAdapter.blueSet.size() == 0) {
				// 得到红球的随机数
				MyGridViewAdapter.redSet = NumberTools.getRandomNum2(6, 33);
				// 得到蓝球的随机数
				MyGridViewAdapter.blueSet = NumberTools.getRandomNum2(1, 16);
				// 刷新Adapter
				redAdapter.setNumByRandom();
				return;
			} else if (AppTools.totalCount == 0) {
				MyToast.getToast(SelectNumberActivity.this, "请至少选择一注").show();
				return;
			}
		} else if (MyGridViewAdapter.playType == 502) {
			if (MyGridViewAdapter.redTuoSet.size() < 2) {
				MyToast.getToast(SelectNumberActivity.this, "拖码至少选2个").show();
				return;
			} else if (AppTools.totalCount < 2) {
				MyToast.getToast(SelectNumberActivity.this, "请至少选择二注").show();
				return;
			}
		}

		Intent intent = new Intent(SelectNumberActivity.this, BetActivity.class);
		for (String str : MyGridViewAdapter.redSet) {
			listRed.add(str);
		}
		for (String str : MyGridViewAdapter.blueSet) {
			listBlue.add(str);
			Log.i("x", "蓝球  ----  " + str);
		}
		for (String str : MyGridViewAdapter.redTuoSet) {
			listRedTuo.add(str);
		}

		Log.i("x", "redNumber  " + listRed.size());
		bundle = new Bundle();
		bundle.putStringArrayList("red", listRed);
		bundle.putStringArrayList("blue", listBlue);
		bundle.putStringArrayList("redTuo", listRedTuo);

		intent.putExtra("bundle", bundle);
		SelectNumberActivity.this.startActivity(intent);
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
		if (MyGridViewAdapter.playType == 501) {
			SmanagerView.registerSensorManager(mSmanager,
					getApplicationContext(), this);// 注册传感器
			vibrator = VibratorView.getVibrator(getApplicationContext());
		}
	}

	public void unregister() {
		clear();
		vibrator = null;
		SmanagerView.unregisterSmanager(mSmanager, this);
		super.onStop();
	}

	/** 注册传感器 和 振动器 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		register();
	}

	/** 销毁传感器 和 振动器 */
	@Override
	protected void onStop() {
		
		// TODO Auto-generated method stub
		unregister();
	}

	// 进入后台 调用
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		setIntent(null);
        System.out.println("SelectNumberActivity.onSaveInstanceState()");
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
			if (MyGridViewAdapter.redSet.size() != 0
					|| MyGridViewAdapter.redTuoSet.size() != 0
					|| MyGridViewAdapter.blueSet.size() != 0) {
				dialog.show();
				
				
				dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
					@Override
					public void getResult(int resultCode) {
						// TODO Auto-generated method stub
						if (1 == resultCode) {// 确定
							clearHashSet();
							for (int i = 0; i < App.activityS1.size(); i++) {
								App.activityS1.get(i).finish();
							}
						}
					}
				});
			} else {
				clearHashSet();
				for (int i = 0; i < App.activityS1.size(); i++) {
					App.activityS1.get(i).finish();
				}
			}
		} else if (AppTools.list_numbers != null
				&& AppTools.list_numbers.size() != 0) {
			if (MyGridViewAdapter.redSet.size() != 0
					|| MyGridViewAdapter.redTuoSet.size() != 0
					|| MyGridViewAdapter.blueSet.size() != 0) {
				dialog.show();
				dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
					@Override
					public void getResult(int resultCode) {
						// TODO Auto-generated method stub
						if (1 == resultCode) {// 确定
							clearHashSet();
							Intent intent = new Intent(
									SelectNumberActivity.this,
									BetActivity.class);
							SelectNumberActivity.this.startActivity(intent);
							SelectNumberActivity.this.finish();
						}
					}
				});
			} else {
				clearHashSet();
				Intent intent = new Intent(SelectNumberActivity.this,
						BetActivity.class);
				SelectNumberActivity.this.startActivity(intent);
				SelectNumberActivity.this.finish();
			}
		}
	}

	/** 设置胆拖 区可见 */
	private void setGridViewVisible() {
		btn_playtype.setText("胆拖投注");
		MyGridViewAdapter.playType = 502;
		// tv_yao_tishi.setVisibility(View.GONE);
		// 注销传感器
		SmanagerView.unregisterSmanager(mSmanager, this);

		// 设置可见
		layout_ball_redtuo.setVisibility(View.VISIBLE);
		gv_ball_red_tuo.setVisibility(View.VISIBLE);

		// 刷新Adapter
		updateAdapter();

		tv_tatol_count.setText(AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}

	/** 设置胆拖 区不可见 */
	private void setGridViewNotVisible() {
		btn_playtype.setText("普通投注");
		MyGridViewAdapter.playType = 501;
		// 注册传感器
		SmanagerView.registerSensorManager(mSmanager, getApplicationContext(),
				this);
		// 设置可见
		layout_ball_redtuo.setVisibility(View.GONE);
		gv_ball_red_tuo.setVisibility(View.GONE);

		// 刷新Adapter
		updateAdapter();
		tv_tatol_count.setText(AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}

	/** 刷新Adapter */
	public void updateAdapter() {
		redAdapter.notifyDataSetChanged();
		redTuoAdapter.notifyDataSetChanged();
		blueAdapter.notifyDataSetChanged();
		tv_tatol_count.setText(AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}

	/** 清空选中情况 */
	public static void clearHashSet() {
		if (null != MyGridViewAdapter.redSet) {
			MyGridViewAdapter.redSet.clear();
		}
		if (null != MyGridViewAdapter.redTuoSet) {
			MyGridViewAdapter.redTuoSet.clear();
		}
		if (null != MyGridViewAdapter.blueSet) {
			MyGridViewAdapter.blueSet.clear();
		}

	}

	@Override
	protected void onDestroy() {
		App.activityS.remove(this);
		super.onDestroy();
	}

	@Override
	protected void onNewIntent(Intent intent) {
        if (intent.getBundleExtra("bundle")!= null) {
            //投注单页面跳转过来的
            setIntent(intent);
        }else{
            //投注成功，继续投注跳转过来
            clear();
        }
		super.onNewIntent(intent);
	}

}
