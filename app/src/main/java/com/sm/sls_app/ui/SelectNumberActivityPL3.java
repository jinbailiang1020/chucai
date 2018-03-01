package com.sm.sls_app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.ui.adapter.MyGridViewAdapterFC3D;
import com.sm.sls_app.ui.adapter.MyGridViewAdapterPL3;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.NetWork;
import com.sm.sls_app.utils.NumberTools;
import com.sm.sls_app.utils.PopupWindowUtil;
import com.sm.sls_app.view.ConfirmDialog;
import com.sm.sls_app.view.CustomDigitalClock2;
import com.sm.sls_app.view.MyToast;
import com.sm.sls_app.view.SmanagerView;
import com.sm.sls_app.view.VibratorView;
import com.sm.sls_app.view.CustomDigitalClock2.ClockListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/** 3D选号界面，实现选号功能 版本 */
public class SelectNumberActivityPL3 extends Activity implements
    OnClickListener, SensorEventListener {
  private static final String TAG = "SelectNumberActivityPL3";

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
  private TextView tv_lotteryname, tv_lotteryqi;// 彩种名,最后开奖期号
  private CustomDigitalClock2 select_time;// 本期倒计时
  // 中奖的红色蓝色号码
  private TextView tv_selected_redball, tv_selected_blueball;
  private LinearLayout layout_shake;// 摇一摇
  private ImageView iv_shake;// 摇一摇
  private TextView tv_shake;// 摇一摇
  private RelativeLayout layout_mygv;// 网格

  /* 红球胆码 */
  private LinearLayout layout_tip_red;// 普通投注提示
  private LinearLayout layout_tip_red_tuo;// 胆拖投注提示

  public static boolean flag; // 区分组三单式 和 组六复式

  private int playType = 6301;

  private TextView txBai;
  private TextView txShi;

  private boolean spinnerIsSelect = false; // 下拉框是否被点击

  private Bundle bundle;

  public Vibrator vibrator; // 震动器
  private SensorManager mSmanager; // 传感器

  private Integer[] reds = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
      16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33 };

  private Integer[] blues = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
      15, 16 };

  // private PopupWindow popWindow;
  private int type = 1;

  private boolean isCanChange = true;
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

  private RelativeLayout fc3d_shi;
  private RelativeLayout fc3d_ge, fc3d_bai;

  private RelativeLayout fc3d_hezhi;
  private RelativeLayout fc3d_zixuanhezhi;

  private TextView tv_show;

  private GridView gridView_bai; // 百位
  private GridView gridView_shi; // 十位
  private GridView gridView_ge; // 个位
  private GridView gridView_hezhi;
  private GridView gridView_zixuanhezhi;
  private MyGridViewAdapterPL3 baiAdapter, shiAdapter, geAdapter, hezhi,
      zixuanhezhi; // 百,十，个 位Adapter

  private Integer[] bais = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

  private Integer[] hezhis = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
      15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26 };

  private Integer[] hezhiss = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
      14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27 };

  private float dpi;

  private PopupWindowUtil popUtil;

  private Map<Integer, Map<Integer, String>> data = new HashMap<Integer, Map<Integer, String>>();

  private int parentIndex;
  private int itemIndex;

  private Map<Integer, Integer> playtypeMap = new HashMap<Integer, Integer>();
  private String auth, info, time, imei, crc; // 格式化后的参数
  private String opt = "13"; // 格式化后的 opt
  private MyAsynTask1 mAsynTask1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    // 设置无标题
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_select_number_fc3d_pl3);
    App.activityS.add(this);
    App.activityS1.add(this);
    clearHashSet();
    findView();
    init();
    setListener();
    if (NetWork.isConnect(SelectNumberActivityPL3.this)) {
      if (AppTools.lottery != null) {
        mAsynTask1 = new MyAsynTask1();
        mAsynTask1.execute();
      } else {
        Toast.makeText(SelectNumberActivityPL3.this, "网络连接异常，获得数据失败！",
            Toast.LENGTH_SHORT).show();
      }
    }
  }

  /*** 异步任务 用来后台获取数据 */
  class MyAsynTask1 extends AsyncTask<Void, Integer, String> {
    /** 在后台执行的程序 */
    @Override
    protected String doInBackground(Void... params) {
      time = RspBodyBaseBean.getTime();
      imei = RspBodyBaseBean.getIMEI(SelectNumberActivityPL3.this);
      info = "{\"lotteryId\":\"" + AppTools.lottery.getLotteryID() + "\"}";
      Log.i("x", "中奖号码---lotteryId--" + AppTools.lottery.getLotteryID());
      String key = MD5.md5(AppTools.MD5_key);
      crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");
      auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");
      String values[] = { opt, auth, info };
      String result = HttpUtils.doPost(AppTools.names, values, AppTools.path);
      Log.i("x", "中奖号码---result--" + result);
      // 先把中奖号码设为空
      AppTools.lottery.setLastWinNumber("");
      try {
        JSONObject object = new JSONObject(result);
        if ("0".equals(object.optString("error"))) {
          JSONArray array = new JSONArray(object.optString("dtOpenInfo"));
          if (null != array) {
            AppTools.lottery.setLastWinNumber(array.getJSONObject(0).optString(
                "winLotteryNumber"));
            AppTools.lottery.setLastIsuseName(array.getJSONObject(0).optString(
                "isuseName"));
          }
          AppTools.lottery.setCurrIsuseEndDateTime(object
              .optString("currIsuseEndDateTime"));
          AppTools.lottery.setIsuseName(object.optString("currIsuseName"));
          long nowTime = 0;
          try {
            AppTools.serverTime = object.optString("serverTime");
            long endtime = AppTools.getTimestamp(AppTools.lottery
                .getCurrIsuseEndDateTime());
            long se = AppTools.getTimestamp(AppTools.serverTime);
            nowTime = System.currentTimeMillis();
            AppTools.lottery.setDistanceTime2(endtime - se + nowTime);
          } catch (ParseException e) {
            e.printStackTrace();
          } catch (java.text.ParseException e) {
            e.printStackTrace();
          }
          return "100";
        }
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      return "101";
    }

    @Override
    protected void onPostExecute(String result) {
      MyHandler myHandler = new MyHandler();
      myHandler.sendEmptyMessage(Integer.parseInt(result));
      super.onPostExecute(result);
    }
  }

  class MyHandler extends Handler {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
      case 100:
        initNum();
        break;
      case 101:
        Toast.makeText(SelectNumberActivityPL3.this, "获取中奖信息失败！",
            Toast.LENGTH_SHORT).show();
        break;
      default:
        break;
      }
    }
  }

  private void initNum() {
    String lottery_redNum = "";

    if (NetWork.isConnect(SelectNumberActivityPL3.this)) {
      if (AppTools.lottery != null) {
        MyGridViewAdapterFC3D.playType = playType = Integer
            .parseInt(AppTools.lottery.getLotteryID() + "01");
        if (AppTools.lottery.getLastWinNumber() != null) {
          String num = AppTools.lottery.getLastWinNumber();
          char[] tempStr = num.toCharArray();
          for (char c : tempStr) {
            lottery_redNum += c + " ";
          }
          lottery_redNum = lottery_redNum.trim();
        }
        Log.i(TAG, tv_selected_redball + "");
        Log.i(TAG, lottery_redNum);
        tv_selected_redball.setText(lottery_redNum);
        if (AppTools.lottery.getLastIsuseName() != null) {
          tv_lotteryqi.setText(AppTools.lottery.getLastIsuseName() + "期：");
        }
        if (AppTools.lottery.getIsuseName() != null
            && AppTools.lottery.getDistanceTime2() - System.currentTimeMillis() > 0) {
          select_time.setMTickStop(false);
          if (AppTools.lottery.getIsuseName() != null) {
            select_time.setQiHao(AppTools.lottery.getIsuseName());
          }
          select_time.setEndTime(AppTools.lottery.getDistanceTime2());
        }
        select_time.setClockListener(new ClockListener() {

          @Override
          public void timeEnd() {
            if (NetWork.isConnect(SelectNumberActivityPL3.this) == true) {
              if (AppTools.lottery != null) {
                if (mAsynTask1 != null) {
                  mAsynTask1 = null;
                }
                mAsynTask1 = new MyAsynTask1();
                mAsynTask1.execute();
              }
            } else {
              Toast.makeText(SelectNumberActivityPL3.this, "网络连接异常，获得数据失败！",
                  Toast.LENGTH_SHORT).show();
            }
          }

          @Override
          public void remainFiveMinutes() {

          }
        });
      } else {
        Toast.makeText(SelectNumberActivityPL3.this, "网络连接异常，获得数据失败！",
            Toast.LENGTH_SHORT).show();
      }
    }
  }

  /** 初始化UI */
  private void findView() {
    bundle = new Bundle();
    txBai = (TextView) findViewById(R.id.txBai);
    txShi = (TextView) findViewById(R.id.txShi);
    fc3d_bai = (RelativeLayout) findViewById(R.id.fc3d_bai);
    fc3d_shi = (RelativeLayout) findViewById(R.id.fc3d_shi);
    fc3d_ge = (RelativeLayout) findViewById(R.id.fc3d_ge);
    layout_mygv = (RelativeLayout) findViewById(R.id.layout_mygv);
    fc3d_hezhi = (RelativeLayout) findViewById(R.id.fc3d_hezhi);
    fc3d_zixuanhezhi = (RelativeLayout) findViewById(R.id.fc3d_zhixuanhezhi);
    tv_selected_redball = (TextView) findViewById(R.id.tv_selected_redball);
    tv_show = (TextView) findViewById(R.id.number_sv_center_tv_redShow);
    gridView_bai = (GridView) findViewById(R.id.number_sv_center_gv_showBai);
    gridView_shi = (GridView) findViewById(R.id.number_sv_center_gv_showShi);
    gridView_ge = (GridView) findViewById(R.id.number_sv_center_gv_showGe);
    gridView_hezhi = (GridView) findViewById(R.id.number_sv_center_gv_hezhi);
    gridView_zixuanhezhi = (GridView) findViewById(R.id.number_sv_center_gv_zhixuanhezhi);
    btn_back = (ImageButton) findViewById(R.id.btn_back);
    btn_playtype = (Button) findViewById(R.id.btn_playtype);
    btn_help = (ImageButton) findViewById(R.id.btn_help);
    iv_up_down = (ImageView) findViewById(R.id.iv_up_down);
    layout_select_playtype = (LinearLayout) findViewById(R.id.layout_select_playtype);
    btn_clearall = (Button) findViewById(R.id.btn_clearall);
    btn_submit = (Button) findViewById(R.id.btn_submit);
    tv_lotteryname = (TextView) findViewById(R.id.tv_lotteryname);
    tv_lotteryqi = (TextView) findViewById(R.id.tv_lotteryqi);
    select_time = (CustomDigitalClock2) findViewById(R.id.tv_selected_time);
    layout_shake = (LinearLayout) findViewById(R.id.layout_shake);
    iv_shake = (ImageView) findViewById(R.id.iv_shake);
    tv_shake = (TextView) findViewById(R.id.tv_shake);
    tv_tatol_count = (TextView) findViewById(R.id.tv_tatol_count);
    tv_tatol_money = (TextView) findViewById(R.id.tv_tatol_money);
    // 给Adapter赋值
    baiAdapter = new MyGridViewAdapterPL3(SelectNumberActivityPL3.this, bais,
        Color.RED, 1);

    shiAdapter = new MyGridViewAdapterPL3(SelectNumberActivityPL3.this, bais,
        Color.RED, 2);

    geAdapter = new MyGridViewAdapterPL3(SelectNumberActivityPL3.this, bais,
        Color.RED, 3);

    hezhi = new MyGridViewAdapterPL3(SelectNumberActivityPL3.this, hezhis,
        Color.RED, 4);

    zixuanhezhi = new MyGridViewAdapterPL3(SelectNumberActivityPL3.this,
        hezhiss, Color.RED, 5);
    mSmanager = (SensorManager) getSystemService(SENSOR_SERVICE);
    layout_top_select = (RelativeLayout) findViewById(R.id.layout_top_select);
    sv_show_ball = (ScrollView) findViewById(R.id.sv_show_ball);
  }

  /** 初始化属性 上期开奖号码 */
  private void init() {
    SmanagerView
        .registerSensorManager(mSmanager, getApplicationContext(), this);// 注册传感器

    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    settings = getSharedPreferences("app_user", 0);// 获取SharedPreference对象
    editor = settings.edit();// 获取编辑对象

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
    tv_lotteryname.setText("排列三");
    btn_playtype.setText("直选");
    Map<Integer, String> playType = new HashMap<Integer, String>();
    playType.put(0, "直选");
    playType.put(1, "组三单式");
    playType.put(2, "组三复式");
    playType.put(3, "组六复式");
    playType.put(4, "组六单式");
    playType.put(5, "组选和值");
    playType.put(6, "直选和值");
    playType.put(7, "");
    playType.put(8, "");
    Set<Integer> set = playType.keySet();
    int[] playtype_array = { 6301, 6302, 6304, 6303, 6302, 6306, 6305 };
    for (Integer i : set) {
      if (i < 7) {
        playtypeMap.put(playtype_array[i], i);
      }
    }
    data.put(0, playType);
    dialog = new ConfirmDialog(this, R.style.dialog);
    // 初始化玩法id
    MyGridViewAdapterPL3.playType = 6301;
  }

  /** 绑定监听 */
  private void setListener() {
    // 绑定Adapter
    gridView_bai.setAdapter(baiAdapter);
    gridView_shi.setAdapter(shiAdapter);
    gridView_ge.setAdapter(geAdapter);
    gridView_hezhi.setAdapter(hezhi);
    gridView_zixuanhezhi.setAdapter(zixuanhezhi);
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
      if (isCanChange) {
        popUtil = new PopupWindowUtil(this, data, layout_top_select);
        popUtil.setSelectIndex(parentIndex, itemIndex);
        popUtil.createPopWindow();
        popUtil.setOnSelectedListener(new PopupWindowUtil.OnSelectedListener() {
          @Override
          public void getIndex(int parentIndex, int itemIndex) {
            // TODO Auto-generated method stub
            if (itemIndex <= 6) {
              if (itemIndex != SelectNumberActivityPL3.this.itemIndex) {
                SelectNumberActivityPL3.this.parentIndex = parentIndex;
                SelectNumberActivityPL3.this.itemIndex = itemIndex;
                changePlayType();
              }
            }
            rote(2);// 旋转动画 向下
          }
        });
        rote(1);// 旋转动画 向上
      }
      break;
    }
  }

  public void changePlayType() {
    btn_playtype.setText(data.get(parentIndex).get(itemIndex));
    layout_shake.setVisibility(View.GONE);
    switch (itemIndex) {
    case 0:// 直选
      layout_shake.setVisibility(View.VISIBLE);
      MyGridViewAdapterPL3.playType = playType = Integer
          .parseInt(AppTools.lottery.getLotteryID() + "01");
      setGridViewVisible(playType);
      SmanagerView.registerSensorManager(mSmanager, getApplicationContext(),
          this);
      vibrator = VibratorView.getVibrator(getApplicationContext());
      break;
    case 1:// 组三单式
      MyGridViewAdapterPL3.playType = playType = Integer
          .parseInt(AppTools.lottery.getLotteryID() + "02");
      flag = true;
      setGridViewVisible(playType);
      vibrator = null;
      SmanagerView.unregisterSmanager(mSmanager, this);
      break;
    case 2:// 组三复式
      MyGridViewAdapterPL3.playType = playType = Integer
          .parseInt(AppTools.lottery.getLotteryID() + "04");
      setGridViewVisible(playType);
      vibrator = null;
      SmanagerView.unregisterSmanager(mSmanager, this);
      break;
    case 3:// 组六复式
      MyGridViewAdapterPL3.playType = playType = Integer
          .parseInt(AppTools.lottery.getLotteryID() + "03");
      setGridViewVisible(playType);
      vibrator = null;
      SmanagerView.unregisterSmanager(mSmanager, this);
      break;
    case 4:// 组六单式
      flag = false;
      MyGridViewAdapterPL3.playType = playType = Integer
          .parseInt(AppTools.lottery.getLotteryID() + "02");
      setGridViewVisible(playType);
      vibrator = null;
      SmanagerView.unregisterSmanager(mSmanager, this);
      break;
    case 5:// 组选和值
      MyGridViewAdapterPL3.playType = playType = Integer
          .parseInt(AppTools.lottery.getLotteryID() + "06");
      setGridViewVisible(playType);
      vibrator = null;
      SmanagerView.unregisterSmanager(mSmanager, this);
      break;
    case 6:// 直选和值
      MyGridViewAdapterPL3.playType = playType = Integer
          .parseInt(AppTools.lottery.getLotteryID() + "05");
      setGridViewVisible(playType);
      vibrator = null;
      SmanagerView.unregisterSmanager(mSmanager, this);
      break;
    }
    AppTools.totalCount = 0;
    clearHashSet();
    updateAdapter();
    sv_show_ball.scrollTo(0, 0);
  }

  /** 刷新Adapter */
  public void updateAdapter() {
    baiAdapter.notifyDataSetChanged();
    geAdapter.notifyDataSetChanged();
    shiAdapter.notifyDataSetChanged();
    hezhi.notifyDataSetChanged();
    zixuanhezhi.notifyDataSetChanged();
  }

  /** 设置胆拖 区可见 */
  private void setGridViewVisible(int type) {
    fc3d_bai.setVisibility(View.GONE);
    fc3d_shi.setVisibility(View.GONE);
    fc3d_ge.setVisibility(View.GONE);
    fc3d_zixuanhezhi.setVisibility(View.GONE);
    fc3d_hezhi.setVisibility(View.GONE);
    switch (type) {
    /** 直选和值 */
    case 6305:
      fc3d_zixuanhezhi.setVisibility(View.VISIBLE);
      tv_show.setText("至少选择1个和值");
      break;
    /** 和值 */
    case 6306:
      fc3d_hezhi.setVisibility(View.VISIBLE);
      tv_show.setText("至少选择1个和值");
      break;
    case 6302:
      if (flag) {
        txBai.setText("重");
        txShi.setText("单");
        fc3d_bai.setVisibility(View.VISIBLE);
        txBai.setVisibility(View.VISIBLE);
        fc3d_shi.setVisibility(View.VISIBLE);
        tv_show.setText("每位只能选择1个号码");
        break;
      } else {
        // txBai.setText("百");
        // txShi.setText("十");
        // fc3d_bai.setVisibility(View.VISIBLE);
        // txBai.setVisibility(View.VISIBLE);
        // txShi.setVisibility(View.VISIBLE);
        // fc3d_shi.setVisibility(View.VISIBLE);
        // fc3d_ge.setVisibility(View.VISIBLE);
        // tv_show.setText("每位只能选择1个号码");
        fc3d_bai.setVisibility(View.VISIBLE);
        txBai.setVisibility(View.GONE);
        tv_show.setText("只能选择3个号码");
        break;
      }
      /** 组三复式 */
    case 6304:
      fc3d_bai.setVisibility(View.VISIBLE);
      txBai.setVisibility(View.GONE);
      tv_show.setText("至少选择2个号码");
      break;
    /** 组六复式 */
    case 6303:
      fc3d_bai.setVisibility(View.VISIBLE);
      txBai.setVisibility(View.GONE);
      tv_show.setText("至少选择4个号码");
      break;
    /** 直选 */
    case 6301:
      txBai.setText("百");
      txShi.setText("十");
      fc3d_bai.setVisibility(View.VISIBLE);
      txBai.setVisibility(View.VISIBLE);
      txShi.setVisibility(View.VISIBLE);
      fc3d_shi.setVisibility(View.VISIBLE);
      fc3d_ge.setVisibility(View.VISIBLE);
      tv_show.setText("每位至少选择1个号码");
      break;
    }
    updateAdapter();
    tv_tatol_count.setText(AppTools.totalCount + "");
    tv_tatol_money.setText((AppTools.totalCount * 2) + "");
  }

  /**
   * 旋转
   * 
   * @param type
   *          1.向上 2.向下
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

  /** 将投注页面的值 显示出来 */
  private void getItem() {
    Intent intent = SelectNumberActivityPL3.this.getIntent();
    Bundle bundle = null;
    if (intent != null) {
      bundle = intent.getBundleExtra("bundle");
    }
    sv_show_ball.scrollTo(0, 0);
    if (null != bundle) {
      Log.i("x", "getItem()==>bundle  " + bundle.toString());
      MyGridViewAdapterPL3.playType = bundle.getInt("type");
      int type = MyGridViewAdapterPL3.playType;
      itemIndex = playtypeMap.get(type);
      if (MyGridViewAdapterPL3.playType == 6302) {
        // 组三单式
        if (bundle.getBoolean("flag")) {
          itemIndex = 1;
        } else {
          itemIndex = 4;
        }

      }

      changePlayType();
      if (bundle.getStringArrayList("bai") != null
          && bundle.getStringArrayList("bai").size() > 0) {
        for (String str : bundle.getStringArrayList("bai")) {
          MyGridViewAdapterPL3.baiSet.add(str);
          Log.i("ss", "bai=" + str);
        }
      }
      if (bundle.getStringArrayList("shi") != null
          && bundle.getStringArrayList("shi").size() > 0) {
        for (String str : bundle.getStringArrayList("shi")) {
          MyGridViewAdapterPL3.shiSet.add(str);
          Log.i("ss", "shi=" + str);
        }
      }
      if (bundle.getStringArrayList("ge") != null
          && bundle.getStringArrayList("ge").size() > 0) {
        for (String str : bundle.getStringArrayList("ge")) {
          MyGridViewAdapterPL3.geSet.add(str);
        }
      }
      System.out.println("=====" + bundle.getStringArrayList("hezhi"));
      if (bundle.getStringArrayList("hezhi") != null
          && bundle.getStringArrayList("hezhi").size() > 0) {
        for (String str : bundle.getStringArrayList("hezhi")) {
          System.out.println("+++" + str);
          MyGridViewAdapterPL3.hezhiSet.add(str);
        }
      }
      System.out.println("=====" + bundle.getStringArrayList("zixuanhezhi"));
      if (bundle.getStringArrayList("zixuanhezhi") != null
          && bundle.getStringArrayList("zixuanhezhi").size() > 0) {
        for (String str : bundle.getStringArrayList("zixuanhezhi")) {
          MyGridViewAdapterPL3.zixuanhezhiSet.add(str);
        }
      }
      // 赋值注数
      AppTools.totalCount = bundle.getLong("count");
      setGridViewVisible(MyGridViewAdapterPL3.playType);
    }
    updateAdapter();
    MyGridViewAdapterPL3.LotteryId = "63";
  }

  /** 玩法说明 */
  private void playExplain() {
    Intent intent = new Intent(SelectNumberActivityPL3.this,
        PlayDescription.class);
    SelectNumberActivityPL3.this.startActivity(intent);
  }

  /** 机选 按钮点击 */
  public void selectRandom() {
    switch (playType) {
    case 6301:
    case 601:
      // 得到百位的随机数
      MyGridViewAdapterPL3.baiSet = NumberTools.getRandomNum3(1, 10);
      // 得到十位的随机数
      MyGridViewAdapterPL3.shiSet = NumberTools.getRandomNum3(1, 10);
      // 得到个位的随机数
      MyGridViewAdapterPL3.geSet = NumberTools.getRandomNum3(1, 10);
      break;
    }
    // 随机选
    baiAdapter.setNumByRandom();
  }

  /** 提交号码 */
  private void submitNumber() {
    System.out.println("0000" + MyGridViewAdapterPL3.playType);
    if (MyGridViewAdapterPL3.playType == 6301) {
      System.out.println("==");
      if (AppTools.totalCount == 0) {
        System.out.println("-=");
        if (MyGridViewAdapterPL3.baiSet.size() == 0
            && MyGridViewAdapterPL3.shiSet.size() == 0
            && MyGridViewAdapterPL3.geSet.size() == 0) {
          System.out.println("--");
          if (null != vibrator)
            vibrator.vibrate(300);
          selectRandom();
        } else {
          MyToast.getToast(SelectNumberActivityPL3.this, "请选择至少一注").show();
        }

        return;
      }
    } else {
      if (AppTools.totalCount == 0) {
        MyToast.getToast(SelectNumberActivityPL3.this, "请至少选择一注").show();
        return;
      }
    }

    Intent intent = new Intent(SelectNumberActivityPL3.this,
        BetActivityPL3.class);
    intent.putExtra("lotteryBundle", bundle);
    SelectNumberActivityPL3.this.startActivity(intent);
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

    SmanagerView
        .registerSensorManager(mSmanager, getApplicationContext(), this);// 注册传感器
    vibrator = VibratorView.getVibrator(getApplicationContext());

  }

  /** 注册传感器 和 振动器 */
  @Override
  protected void onResume() {
    // TODO Auto-generated method stub
    super.onResume();
    getItem();
    updateAdapter();
    tv_tatol_count.setText(AppTools.totalCount + "");
    tv_tatol_money.setText((AppTools.totalCount * 2) + "");
  }

  @Override
  protected void onNewIntent(Intent intent) {

    if (intent.getBundleExtra("bundle") != null) {
      // 投注单页面跳转过来的
      setIntent(intent);
    } else {
      // 投注成功，继续投注跳转过来
      clear();
    }
    super.onNewIntent(intent);
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
        || (AppTools.list_numbers != null && AppTools.list_numbers.size() == 0)) {
      if (MyGridViewAdapterPL3.baiSet.size() != 0
          || MyGridViewAdapterPL3.shiSet.size() != 0
          || MyGridViewAdapterPL3.geSet.size() != 0) {
        dialog.show();
        dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
        dialog
            .setDialogResultListener(new ConfirmDialog.DialogResultListener() {
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
      if (MyGridViewAdapterPL3.baiSet.size() != 0
          || MyGridViewAdapterPL3.shiSet.size() != 0
          || MyGridViewAdapterPL3.geSet.size() != 0) {
        dialog.show();
        dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
        dialog
            .setDialogResultListener(new ConfirmDialog.DialogResultListener() {
              @Override
              public void getResult(int resultCode) {
                // TODO Auto-generated method stub
                if (1 == resultCode) {// 确定
                  clearHashSet();
                  AppTools.totalCount = 0;
                  SelectNumberActivityPL3.this.startActivity(new Intent(
                      SelectNumberActivityPL3.this, BetActivityPL3.class));
                  SelectNumberActivityPL3.this.finish();
                }
              }
            });
      } else {
        clearHashSet();
        AppTools.totalCount = 0;
        SelectNumberActivityPL3.this.startActivity(new Intent(
            SelectNumberActivityPL3.this, BetActivityPL3.class));
        SelectNumberActivityPL3.this.finish();
      }
    }
  }

  /** 清空选中情况 */
  public static void clearHashSet() {
    if (null != MyGridViewAdapterPL3.baiSet) {
      MyGridViewAdapterPL3.baiSet.clear();
    }
    if (null != MyGridViewAdapterPL3.shiSet) {
      MyGridViewAdapterPL3.shiSet.clear();
    }
    if (null != MyGridViewAdapterPL3.geSet) {
      MyGridViewAdapterPL3.geSet.clear();
    }
    if (null != MyGridViewAdapterPL3.hezhiSet) {
      MyGridViewAdapterPL3.hezhiSet.clear();
    }
    if (null != MyGridViewAdapterPL3.zixuanhezhiSet) {
      MyGridViewAdapterPL3.zixuanhezhiSet.clear();
    }
  }

}
