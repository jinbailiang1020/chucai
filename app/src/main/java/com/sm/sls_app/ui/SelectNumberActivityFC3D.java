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
import com.sm.sls_app.ui.SelectNumberActivityFC3D.MyAsynTask1;
import com.sm.sls_app.ui.adapter.MyGridViewAdapter;
import com.sm.sls_app.ui.adapter.MyGridViewAdapterFC3D;
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
public class SelectNumberActivityFC3D extends Activity implements
    OnClickListener, SensorEventListener {
  private static final String TAG = "SelectNumberActivityFC3D";

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

  private boolean flag; // 区分组三单式 和 组六复式

  private int playType = 601;

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
  private MyGridViewAdapterFC3D baiAdapter, shiAdapter, geAdapter, hezhi,
      zixuanhezhi, daxiaoAdapter, jiouAdapter, caistAdapter, tuolajiAdapter; // 百,十，个
                                                                             // 位Adapter

  private Integer[] bais = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

  private String[] daxiao = { "大", "小" };
  private String[] jiou = { "奇", "偶" };
  private String caist = "三同号";
  private String tuolaji = "拖拉机";
  private Integer[] hezhis = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
      14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27 };

  private Integer[] hezhiss = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
      14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27 };

  private float dpi;

  private PopupWindowUtil popUtil;

  private Map<Integer, Map<Integer, String>> data = new HashMap<Integer, Map<Integer, String>>();

  private int parentIndex;
  private int itemIndex;
  /** 玩法id **/
  public static String id_zhixuan = "601";
  public static String id_zusan = "602";
  public static String id_zuliu = "603";
  public static String id_heshu = "610";
  public static String id_1d = "604";
  public static String id_cai1d = "605";
  public static String id_2d = "606";
  public static String id_cai2dt = "607";
  public static String id_cai2dbt = "608";
  public static String id_tongxuan = "609";
  public static String id_baoxuan3 = "611";
  public static String id_baoxuan6 = "612";
  public static String id_caidx = "613";
  public static String id_caist = "614";
  public static String id_caijo = "616";
  public static String id_tuolaji = "615";

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
    if (NetWork.isConnect(SelectNumberActivityFC3D.this)) {
      if (AppTools.lottery != null) {
        mAsynTask1 = new MyAsynTask1();
        mAsynTask1.execute();
      } else {
        Toast.makeText(SelectNumberActivityFC3D.this, "网络连接异常，获得数据失败！",
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
      imei = RspBodyBaseBean.getIMEI(SelectNumberActivityFC3D.this);
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
        Toast.makeText(SelectNumberActivityFC3D.this, "获取中奖信息失败！",
            Toast.LENGTH_SHORT).show();
        break;
      default:
        break;
      }
    }
  }

  private void initNum() {
    String lottery_redNum = "";

    if (NetWork.isConnect(SelectNumberActivityFC3D.this)) {
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
            if (NetWork.isConnect(SelectNumberActivityFC3D.this) == true) {
              if (AppTools.lottery != null) {
                if (mAsynTask1 != null) {
                  mAsynTask1 = null;
                }
                mAsynTask1 = new MyAsynTask1();
                mAsynTask1.execute();
              }
            } else {
              Toast.makeText(SelectNumberActivityFC3D.this, "网络连接异常，获得数据失败！",
                  Toast.LENGTH_SHORT).show();
            }
          }

          @Override
          public void remainFiveMinutes() {

          }
        });
      } else {
        Toast.makeText(SelectNumberActivityFC3D.this, "网络连接异常，获得数据失败！",
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
    baiAdapter = new MyGridViewAdapterFC3D(SelectNumberActivityFC3D.this, bais,
        Color.RED, 1);

    shiAdapter = new MyGridViewAdapterFC3D(SelectNumberActivityFC3D.this, bais,
        Color.RED, 2);

    geAdapter = new MyGridViewAdapterFC3D(SelectNumberActivityFC3D.this, bais,
        Color.RED, 3);

    hezhi = new MyGridViewAdapterFC3D(SelectNumberActivityFC3D.this, hezhis,
        Color.RED, 4);

    zixuanhezhi = new MyGridViewAdapterFC3D(SelectNumberActivityFC3D.this,
        hezhiss, Color.RED, 5);
    daxiaoAdapter = new MyGridViewAdapterFC3D(SelectNumberActivityFC3D.this,
        daxiao, Color.RED, 6);
    jiouAdapter = new MyGridViewAdapterFC3D(SelectNumberActivityFC3D.this,
        jiou, Color.RED, 7);
    caistAdapter = new MyGridViewAdapterFC3D(SelectNumberActivityFC3D.this,
        caist, Color.RED, 8);
    tuolajiAdapter = new MyGridViewAdapterFC3D(SelectNumberActivityFC3D.this,
        tuolaji, Color.RED, 9);
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
    tv_lotteryname.setText("福彩3D");
    btn_playtype.setText("直选");
    Map<Integer, String> playType = new HashMap<Integer, String>();
    playType.put(0, "直选");
    playType.put(1, "组三");
    playType.put(2, "组六");
    playType.put(3, "和数");
    playType.put(4, "1D");
    playType.put(5, "猜1D");
    playType.put(6, "2D");
    playType.put(7, "猜2D二同号");
    playType.put(8, "猜2D二不同");
    playType.put(9, "通选");
    playType.put(10, "包选3");
    playType.put(11, "包选6");
    playType.put(12, "猜大小");
    playType.put(13, "猜三同");
    playType.put(14, "猜奇偶");
    playType.put(15, "拖拉机");
    playType.put(16, "");
    playType.put(17, "");
    Set<Integer> set = playType.keySet();
    int[] playtype_array = { 601, 602, 603, 610, 604, 605, 606, 607, 608, 609,
        611, 612, 613, 614, 616, 615 };
    for (Integer i : set) {
      if (i < 16) {
        playtypeMap.put(playtype_array[i], i);
      }
    }
    data.put(0, playType);
    dialog = new ConfirmDialog(this, R.style.dialog);
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
      popUtil = new PopupWindowUtil(this, data, layout_top_select);
      popUtil.setSelectIndex(parentIndex, itemIndex);
      popUtil.createPopWindow();
      popUtil.setOnSelectedListener(new PopupWindowUtil.OnSelectedListener() {
        @Override
        public void getIndex(int parentIndex, int itemIndex) {
          // TODO Auto-generated method stub
          if (itemIndex <= 15) {
            if (itemIndex != SelectNumberActivityFC3D.this.itemIndex) {
              SelectNumberActivityFC3D.this.parentIndex = parentIndex;
              SelectNumberActivityFC3D.this.itemIndex = itemIndex;
              changePlayType();
            }
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
    layout_shake.setVisibility(View.GONE);
    switch (itemIndex) {
    case 0:// 直选
      layout_shake.setVisibility(View.VISIBLE);
      gridView_bai.setColumnWidth((int) getResources().getDimension(
          R.dimen.icon_ball_size));
      gridView_bai.setAdapter(baiAdapter);
      MyGridViewAdapterFC3D.playType = playType = Integer.parseInt(id_zhixuan);
      setGridViewVisible(playType);
      SmanagerView.registerSensorManager(mSmanager, getApplicationContext(),
          this);
      vibrator = VibratorView.getVibrator(getApplicationContext());
      break;
    case 1:// 组三
      gridView_bai.setColumnWidth((int) getResources().getDimension(
          R.dimen.icon_ball_size));
      gridView_bai.setAdapter(baiAdapter);
      MyGridViewAdapterFC3D.playType = playType = Integer.parseInt(id_zusan);
      setGridViewVisible(playType);
      vibrator = null;
      SmanagerView.unregisterSmanager(mSmanager, this);
      break;
    case 2:// 组六
      gridView_bai.setColumnWidth((int) getResources().getDimension(
          R.dimen.icon_ball_size));
      gridView_bai.setAdapter(baiAdapter);
      MyGridViewAdapterFC3D.playType = playType = Integer.parseInt(id_zuliu);
      setGridViewVisible(playType);
      vibrator = null;
      SmanagerView.unregisterSmanager(mSmanager, this);
      break;
    case 3:// 和数
      gridView_bai.setColumnWidth((int) getResources().getDimension(
          R.dimen.icon_ball_size));
      gridView_bai.setAdapter(baiAdapter);
      MyGridViewAdapterFC3D.playType = playType = Integer.parseInt(id_heshu);
      setGridViewVisible(playType);
      vibrator = null;
      SmanagerView.unregisterSmanager(mSmanager, this);
      break;
    case 4:// 1D
      gridView_bai.setColumnWidth((int) getResources().getDimension(
          R.dimen.icon_ball_size));
      gridView_bai.setAdapter(baiAdapter);
      MyGridViewAdapterFC3D.playType = playType = Integer.parseInt(id_1d);
      setGridViewVisible(playType);
      vibrator = null;
      SmanagerView.unregisterSmanager(mSmanager, this);
      break;
    case 5:// 猜1D
      gridView_bai.setColumnWidth((int) getResources().getDimension(
          R.dimen.icon_ball_size));
      gridView_bai.setAdapter(baiAdapter);
      MyGridViewAdapterFC3D.playType = playType = Integer.parseInt(id_cai1d);
      setGridViewVisible(playType);
      vibrator = null;
      SmanagerView.unregisterSmanager(mSmanager, this);
      break;
    case 6:// 2D
      gridView_bai.setColumnWidth((int) getResources().getDimension(
          R.dimen.icon_ball_size));
      gridView_bai.setAdapter(baiAdapter);
      MyGridViewAdapterFC3D.playType = playType = Integer.parseInt(id_2d);
      setGridViewVisible(playType);
      vibrator = null;
      SmanagerView.unregisterSmanager(mSmanager, this);
      break;
    case 7:// 猜2D-二同
      gridView_bai.setColumnWidth((int) getResources().getDimension(
          R.dimen.icon_ball_size));
      gridView_bai.setAdapter(baiAdapter);
      MyGridViewAdapterFC3D.playType = playType = Integer.parseInt(id_cai2dt);
      setGridViewVisible(playType);
      vibrator = null;
      SmanagerView.unregisterSmanager(mSmanager, this);
      break;
    case 8:// 猜2D-二不同
      gridView_bai.setColumnWidth((int) getResources().getDimension(
          R.dimen.icon_ball_size));
      gridView_bai.setAdapter(baiAdapter);
      MyGridViewAdapterFC3D.playType = playType = Integer.parseInt(id_cai2dbt);
      setGridViewVisible(playType);
      vibrator = null;
      SmanagerView.unregisterSmanager(mSmanager, this);
      break;
    case 9:// 通选
      layout_shake.setVisibility(View.VISIBLE);
      gridView_bai.setColumnWidth((int) getResources().getDimension(
          R.dimen.icon_ball_size));
      gridView_bai.setAdapter(baiAdapter);
      MyGridViewAdapterFC3D.playType = playType = Integer.parseInt(id_tongxuan);
      setGridViewVisible(playType);
      SmanagerView.registerSensorManager(mSmanager, getApplicationContext(),
          this);
      vibrator = VibratorView.getVibrator(getApplicationContext());
      break;
    case 10:// 包选3
      gridView_bai.setColumnWidth((int) getResources().getDimension(
          R.dimen.icon_ball_size));
      gridView_bai.setAdapter(baiAdapter);
      MyGridViewAdapterFC3D.playType = playType = Integer.parseInt(id_baoxuan3);
      setGridViewVisible(playType);
      vibrator = null;
      SmanagerView.unregisterSmanager(mSmanager, this);
      break;
    case 11:// 包选6
      gridView_bai.setColumnWidth((int) getResources().getDimension(
          R.dimen.icon_ball_size));
      gridView_bai.setAdapter(baiAdapter);
      MyGridViewAdapterFC3D.playType = playType = Integer.parseInt(id_baoxuan6);
      setGridViewVisible(playType);
      vibrator = null;
      SmanagerView.unregisterSmanager(mSmanager, this);
      break;
    case 12:// 大小
      gridView_bai.setColumnWidth(layout_mygv.getWidth());
      gridView_bai.setAdapter(daxiaoAdapter);
      int width = gridView_bai.getWidth();
      MyGridViewAdapterFC3D.playType = playType = Integer.parseInt(id_caidx);
      setGridViewVisible(playType);
      vibrator = null;
      SmanagerView.unregisterSmanager(mSmanager, this);
      break;
    case 13:// 猜三同
      gridView_bai.setColumnWidth(layout_mygv.getWidth());
      gridView_bai.setAdapter(caistAdapter);
      MyGridViewAdapterFC3D.playType = playType = Integer.parseInt(id_caist);
      setGridViewVisible(playType);
      vibrator = null;
      SmanagerView.unregisterSmanager(mSmanager, this);
      break;
    case 14:// 奇偶
      gridView_bai.setColumnWidth(layout_mygv.getWidth());
      gridView_bai.setAdapter(jiouAdapter);
      MyGridViewAdapterFC3D.playType = playType = Integer.parseInt(id_caijo);
      setGridViewVisible(playType);
      vibrator = null;
      SmanagerView.unregisterSmanager(mSmanager, this);
      break;
    case 15:// 拖拉机
      gridView_bai.setColumnWidth(layout_mygv.getWidth());
      gridView_bai.setAdapter(tuolajiAdapter);
      MyGridViewAdapterFC3D.playType = playType = Integer.parseInt(id_tuolaji);
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
    daxiaoAdapter.notifyDataSetChanged();
    jiouAdapter.notifyDataSetChanged();
    caistAdapter.notifyDataSetChanged();
    tuolajiAdapter.notifyDataSetChanged();
  }

  /** 设置胆拖 区可见 */
  private void setGridViewVisible(int type) {
    fc3d_bai.setVisibility(View.GONE);
    fc3d_shi.setVisibility(View.GONE);
    fc3d_ge.setVisibility(View.GONE);
    fc3d_zixuanhezhi.setVisibility(View.GONE);
    fc3d_hezhi.setVisibility(View.GONE);
    switch (type) {
    /** 和数 */
    case 610:
      fc3d_hezhi.setVisibility(View.VISIBLE);
      tv_show.setText("至少选择1个和值");
      break;
    /** 组三 */
    case 602:
      fc3d_bai.setVisibility(View.VISIBLE);
      txBai.setVisibility(View.GONE);
      tv_show.setText("至少选择2个号码");
      break;
    /** 组六 */
    case 603:
      fc3d_bai.setVisibility(View.VISIBLE);
      txBai.setVisibility(View.GONE);
      tv_show.setText("至少选择3个号码");
      break;
    /** 直选 */
    case 601:
      txBai.setText("百");
      txShi.setText("十");
      fc3d_bai.setVisibility(View.VISIBLE);
      txBai.setVisibility(View.VISIBLE);
      txShi.setVisibility(View.VISIBLE);
      fc3d_shi.setVisibility(View.VISIBLE);
      fc3d_ge.setVisibility(View.VISIBLE);
      tv_show.setText("每位至少选择1个号码");
      break;
    /** 1D */
    case 604:
      txBai.setText("百");
      txShi.setText("十");
      fc3d_bai.setVisibility(View.VISIBLE);
      txBai.setVisibility(View.VISIBLE);
      txShi.setVisibility(View.VISIBLE);
      fc3d_shi.setVisibility(View.VISIBLE);
      fc3d_ge.setVisibility(View.VISIBLE);
      tv_show.setText("每位至少选择1个号码");
      break;
    /** 猜1D */
    case 605:
      fc3d_bai.setVisibility(View.VISIBLE);
      txBai.setVisibility(View.GONE);
      tv_show.setText("至少选择1个号码");
      break;
    /** 2D */
    case 606:
      txBai.setText("百");
      txShi.setText("十");
      fc3d_bai.setVisibility(View.VISIBLE);
      txBai.setVisibility(View.VISIBLE);
      txShi.setVisibility(View.VISIBLE);
      fc3d_shi.setVisibility(View.VISIBLE);
      fc3d_ge.setVisibility(View.VISIBLE);
      tv_show.setText("至少选两位，每位至少选择1个号码");
      break;
    /** 猜2D-二同号 */
    case 607:
      fc3d_bai.setVisibility(View.VISIBLE);
      txBai.setVisibility(View.GONE);
      tv_show.setText("至少选择1个号码");
      break;
    /** 猜2D-二不同号 */
    case 608:
      fc3d_bai.setVisibility(View.VISIBLE);
      txBai.setVisibility(View.GONE);
      tv_show.setText("至少选择2个号码");
      break;
    /** 通选 */
    case 609:
      txBai.setText("百");
      txShi.setText("十");
      fc3d_bai.setVisibility(View.VISIBLE);
      txBai.setVisibility(View.VISIBLE);
      txShi.setVisibility(View.VISIBLE);
      fc3d_shi.setVisibility(View.VISIBLE);
      fc3d_ge.setVisibility(View.VISIBLE);
      tv_show.setText("每位至少选择1个号码");
      break;
    /** 包选3 */
    case 611:
      txBai.setText("重");
      txShi.setText("单");
      fc3d_bai.setVisibility(View.VISIBLE);
      txBai.setVisibility(View.VISIBLE);
      fc3d_shi.setVisibility(View.VISIBLE);
      tv_show.setText("每位至少选择1个号码");
      break;
    /** 包选6 */
    case 612:
      txBai.setText("百");
      txShi.setText("十");
      fc3d_bai.setVisibility(View.VISIBLE);
      txBai.setVisibility(View.VISIBLE);
      txShi.setVisibility(View.VISIBLE);
      fc3d_shi.setVisibility(View.VISIBLE);
      fc3d_ge.setVisibility(View.VISIBLE);
      tv_show.setText("每位至少选择1个号码");
      break;
    /** 猜大小 */
    case 613:
      fc3d_bai.setVisibility(View.VISIBLE);
      txBai.setVisibility(View.VISIBLE);
      txBai.setText("选择");
      tv_show.setText("请选择大小");
      break;
    /** 猜三同 */
    case 614:
      fc3d_bai.setVisibility(View.VISIBLE);
      txBai.setVisibility(View.VISIBLE);
      txBai.setText("选择");
      tv_show.setText("开奖结果为三同号即中奖");
      break;
    /** 猜奇偶 */
    case 616:
      fc3d_bai.setVisibility(View.VISIBLE);
      txBai.setVisibility(View.VISIBLE);
      txBai.setText("选择");
      tv_show.setText("请选择奇偶");
      break;
    /** 拖拉机 */
    case 615:
      fc3d_bai.setVisibility(View.VISIBLE);
      txBai.setVisibility(View.VISIBLE);
      txBai.setText("选择");
      tv_show.setText("当期开奖号码的三个号码为以升序或降序连续排列的号码（890、098、901、109除外），即中奖。");
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
    Bundle bundle = null;
    Intent intent = getIntent();
    if (intent != null) {
      bundle = intent.getExtras();
    }
    layout_shake.setVisibility(View.GONE);
    sv_show_ball.scrollTo(0, 0);
    if (null != bundle) {
      Log.i("x", "getItem()==>playType  " + bundle.getInt("type"));
      Log.i("x", "getItem()==>bundle  " + bundle.toString());
      MyGridViewAdapterFC3D.playType = bundle.getInt("type");
      int type = MyGridViewAdapterFC3D.playType;
      itemIndex = playtypeMap.get(type);
      changePlayType();
      if (bundle.getStringArrayList("bai") != null
          && bundle.getStringArrayList("bai").size() > 0) {
        for (String str : bundle.getStringArrayList("bai")) {
          MyGridViewAdapterFC3D.baiSet.add(str);
        }
      }
      if (bundle.getStringArrayList("shi") != null
          && bundle.getStringArrayList("shi").size() > 0) {
        for (String str : bundle.getStringArrayList("shi")) {
          MyGridViewAdapterFC3D.shiSet.add(str);
        }
      }
      if (bundle.getStringArrayList("ge") != null
          && bundle.getStringArrayList("ge").size() > 0) {
        for (String str : bundle.getStringArrayList("ge")) {
          MyGridViewAdapterFC3D.geSet.add(str);
        }
      }
      if (bundle.getStringArrayList("hezhi") != null
          && bundle.getStringArrayList("hezhi").size() > 0) {
        for (String str : bundle.getStringArrayList("hezhi")) {
          MyGridViewAdapterFC3D.hezhiSet.add(str);
        }
      }
      if (bundle.getStringArrayList("zixuanhezhi") != null
          && bundle.getStringArrayList("zixuanhezhi").size() > 0) {
        for (String str : bundle.getStringArrayList("zixuanhezhi")) {
          MyGridViewAdapterFC3D.zixuanhezhiSet.add(str);
        }
      }
      if (bundle.getStringArrayList("daxiao") != null
          && bundle.getStringArrayList("daxiao").size() > 0) {
        for (String str : bundle.getStringArrayList("daxiao")) {
          MyGridViewAdapterFC3D.daxiaoSet.add(str);
        }
      }
      if (bundle.getStringArrayList("jiou") != null
          && bundle.getStringArrayList("jiou").size() > 0) {
        for (String str : bundle.getStringArrayList("jiou")) {
          MyGridViewAdapterFC3D.jiouSet.add(str);
        }
      }
      if (bundle.getBoolean("santong")) {
        MyGridViewAdapterFC3D.isCaist = true;
      } else
        MyGridViewAdapterFC3D.isCaist = false;
      if (bundle.getBoolean("tuolaji")) {
        MyGridViewAdapterFC3D.isTuolaji = true;
      } else
        MyGridViewAdapterFC3D.isTuolaji = false;
      // setGridViewVisible(MyGridViewAdapterFC3D.playType);

    }
    Log.i("x", "getItem()==>bundle为null  ");
    if (609 == MyGridViewAdapterFC3D.playType
        || 601 == MyGridViewAdapterFC3D.playType) {
      layout_shake.setVisibility(View.VISIBLE);
    }
    updateAdapter();

  }

  /** 玩法说明 */
  private void playExplain() {
    Intent intent = new Intent(SelectNumberActivityFC3D.this,
        PlayDescription.class);
    SelectNumberActivityFC3D.this.startActivity(intent);
  }

  /** 机选 按钮点击 */
  public void selectRandom() {
    switch (playType) {
    case 601:
    case 609:
      // 得到百位的随机数
      MyGridViewAdapterFC3D.baiSet = NumberTools.getRandomNum3(1, 10);
      // 得到十位的随机数
      MyGridViewAdapterFC3D.shiSet = NumberTools.getRandomNum3(1, 10);
      // 得到个位的随机数
      MyGridViewAdapterFC3D.geSet = NumberTools.getRandomNum3(1, 10);
      break;
    }
    // 随机选
    baiAdapter.setNumByRandom();
  }

  /** 提交号码 */
  private void submitNumber() {
    System.out.println("0000playType==>" + MyGridViewAdapterFC3D.playType);
    if (MyGridViewAdapterFC3D.playType == 601) {
      if (AppTools.totalCount == 0) {
        if (MyGridViewAdapterFC3D.baiSet.size() == 0
            && MyGridViewAdapterFC3D.shiSet.size() == 0
            && MyGridViewAdapterFC3D.geSet.size() == 0) {
          if (null != vibrator)
            vibrator.vibrate(300);
          selectRandom();
        } else {
          MyToast.getToast(SelectNumberActivityFC3D.this, "请选择至少一注").show();
        }

        return;
      }
    } else {
      if (AppTools.totalCount == 0) {
        MyToast.getToast(SelectNumberActivityFC3D.this, "请至少选择一注").show();
        return;
      }
    }

    Intent intent = new Intent(SelectNumberActivityFC3D.this,
        BetActivityFC3D.class);
    intent.putExtra("lotteryBundle", bundle);
    SelectNumberActivityFC3D.this.startActivity(intent);
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
      SmanagerView.registerSensorManager(mSmanager, getApplicationContext(),
          this);// 注册传感器
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
    System.out.println("233" + AppTools.totalCount);
    getItem();
    updateAdapter();
    tv_tatol_count.setText(AppTools.totalCount + "");
    tv_tatol_money.setText((AppTools.totalCount * 2) + "");
  }

  /** 销毁传感器 和 振动器 */
  @Override
  protected void onStop() {
    // TODO Auto-generated method stub
    clear();
    vibrator = null;
    SmanagerView.unregisterSmanager(mSmanager, this);
    super.onStop();
  }

  @Override
  protected void onNewIntent(Intent intent) {
    if (intent.getExtras() != null) {
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
      if (MyGridViewAdapterFC3D.baiSet.size() != 0
          || MyGridViewAdapterFC3D.shiSet.size() != 0
          || MyGridViewAdapterFC3D.geSet.size() != 0) {
        dialog.show();
        dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
        dialog
            .setDialogResultListener(new ConfirmDialog.DialogResultListener() {
              @Override
              public void getResult(int resultCode) {
                // TODO Auto-generated method stub
                if (1 == resultCode) {// 确定
                  SelectNumberActivityFC3D.this.clearHashSet();
                  AppTools.totalCount = 0;
                  for (int i = 0; i < App.activityS1.size(); i++) {
                    App.activityS1.get(i).finish();
                  }
                }
              }
            });
      } else {
        SelectNumberActivityFC3D.this.clearHashSet();
        AppTools.totalCount = 0;
        for (int i = 0; i < App.activityS1.size(); i++) {
          App.activityS1.get(i).finish();
        }
      }
    } else if (AppTools.list_numbers != null
        && AppTools.list_numbers.size() != 0) {
      if (MyGridViewAdapterFC3D.baiSet.size() != 0
          || MyGridViewAdapterFC3D.shiSet.size() != 0
          || MyGridViewAdapterFC3D.geSet.size() != 0) {

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
                  SelectNumberActivityFC3D.this.startActivity(new Intent(
                      SelectNumberActivityFC3D.this, BetActivityFC3D.class));
                  SelectNumberActivityFC3D.this.finish();
                }
              }
            });
      } else {
        clearHashSet();
        AppTools.totalCount = 0;
        Intent intent = new Intent(SelectNumberActivityFC3D.this,
            BetActivityFC3D.class);
        SelectNumberActivityFC3D.this.startActivity(intent);
        SelectNumberActivityFC3D.this.finish();
      }
    }
  }

  /** 清空选中情况 */
  public static void clearHashSet() {
    if (null != MyGridViewAdapterFC3D.baiSet) {
      MyGridViewAdapterFC3D.baiSet.clear();
    }
    if (null != MyGridViewAdapterFC3D.shiSet) {
      MyGridViewAdapterFC3D.shiSet.clear();
    }
    if (null != MyGridViewAdapterFC3D.geSet) {
      MyGridViewAdapterFC3D.geSet.clear();
    }
    if (null != MyGridViewAdapterFC3D.hezhiSet) {
      MyGridViewAdapterFC3D.hezhiSet.clear();
    }
    if (null != MyGridViewAdapterFC3D.zixuanhezhiSet) {
      MyGridViewAdapterFC3D.zixuanhezhiSet.clear();
    }
    if (null != MyGridViewAdapterFC3D.daxiaoSet) {
      MyGridViewAdapterFC3D.daxiaoSet.clear();
    }
    if (null != MyGridViewAdapterFC3D.jiouSet) {
      MyGridViewAdapterFC3D.jiouSet.clear();
    }
    MyGridViewAdapterFC3D.isCaist = false;
    MyGridViewAdapterFC3D.isTuolaji = false;
  }

}
