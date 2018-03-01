package com.sm.sls_app.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.Lottery;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.fragment.HallFragment;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.ui.Select_11X5Activity.MyAsynTask;
import com.sm.sls_app.ui.Select_11X5Activity.Myhandler;
import com.sm.sls_app.ui.Select_HNSSCActivity.MyAsynTask1;
import com.sm.sls_app.ui.adapter.GridView11X5Adapter;
import com.sm.sls_app.ui.adapter.MyGridViewAdapter;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.NetWork;
import com.sm.sls_app.utils.NumberTools;
import com.sm.sls_app.utils.PopupWindowUtil;
import com.sm.sls_app.view.ConfirmDialog;
import com.sm.sls_app.view.CustomDigitalClock2;
import com.sm.sls_app.view.MyGridView;
import com.sm.sls_app.view.MyToast;
import com.sm.sls_app.view.SmanagerView;
import com.sm.sls_app.view.VibratorView;
import com.sm.sls_app.view.CustomDigitalClock2.ClockListener;
import com.umeng.socialize.facebook.controller.utils.ToastUtil;

/**
 * 时时彩 的选球页面 功能： 选号界面，实现选号
 * 
 * @author Kinwee 修改日期2015-1-4
 * 
 */

public class Select_HNSSCActivity extends Activity implements OnClickListener,
    SensorEventListener {
  private final static String TAG = "Select_HNSSCActivity";

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
  /* 全选部分 */
  private RelativeLayout select_all_rl;
  private Button btn_select_all;// 全选按钮
  /* 中间部分 */
  private TextView tv_lotteryname, tv_lotteryqi;// 彩种名,最后开奖期号
  private CustomDigitalClock2 select_time;// 本期倒计时
  // 中奖的红色蓝色号码
  private TextView tv_selected_redball;
  private LinearLayout layout_shake;// 摇一摇
  private ImageView iv_shake;// 摇一摇
  private TextView tv_shake;// 摇一摇

  private TextView tv_show1, tv_show2, tv_show3, tv_show4, tv_show5, tv_show6,
      tv_tip;
  private RelativeLayout rl_one, rl_two, rl_three, rl_four, rl_five, rl_six,
      rl_dan, rl_tuo;
  private String selected_redball;// 中奖的红球号码 tv_selected_redball
  private String selected_blueball; // 中奖的蓝球号码 tv_selected_blueball

  private Bundle bundle;

  public Vibrator vibrator; // 震动器
  private SensorManager mSmanager; // 传感器

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

  private int parentIndex = 0;
  private int itemIndex = 3;

  private Map<Integer, Integer> playtypeMap = new HashMap<Integer, Integer>();
  private int playID = 8301;

  /**
   * 9203 92 复式 9204 92 猜大小 9205 92 五星单复式通选 9206 92 二星组选 9211 92 三星组3 9212 92
   * 三星组6 9213 92 三星直选组合复式 9216 三星组三胆拖 9217三星组六胆拖
   */
  private int playType = 9203;

  private MyGridView gv_one, gv_two, gv_three, gv_four, gv_five, gv_six,
      gv_seven, gv_dan, gv_tuo;
  private GridView11X5Adapter mAdapterOne, mAdapterTwo, mAdapterThree,
      mAdapterFour, mAdapterFive, mAdapterSix, mAdapterSeven, mAdapterDan,
      mAdapterTuo;
  private int index = 1; // 倒计时标签

  private ArrayList<String> list, list2, list3, list4, list5;
  String strDXDS = "", strSXZSDT = "", strSXZLDT = "";

  private String[] numbers = new String[] { "0", "1", "2", "3", "4", "5", "6",
      "7", "8", "9" };
  private String[] dxds = new String[] { "大", "小", "单", "双" };
  private Map<Integer, Integer> playtypeMapDan;
  private String[] dan_play = { "三星组三胆拖 ", "三星组六胆拖 " };
  private int[] ids_play_dan = { 16, 17 };
  private MyAsynTask myAsynTask;
  private MyHandler myHandler;
  private String auth, info, time, imei, crc; // 格式化后的参数
  private String opt = "13"; // 格式化后的 opt
  private MyAsynTask1 mAsynTask1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    // 设置无标题
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_select_ssc);
    App.activityS.add(this);
    App.activityS1.add(this);
    clear();
    findView();
    init();
    if (NetWork.isConnect(Select_HNSSCActivity.this) == true) {
      if (null != AppTools.lottery) {
        mAsynTask1 = new MyAsynTask1();
        mAsynTask1.execute();
      }
    } else {
      Toast.makeText(Select_HNSSCActivity.this, "网络连接异常，获得数据失败！",
          Toast.LENGTH_SHORT).show();
    }
  }

  /*** 异步任务 用来后台获取数据 */
  class MyAsynTask1 extends AsyncTask<Void, Integer, String> {
    /** 在后台执行的程序 */
    @Override
    protected String doInBackground(Void... params) {
      time = RspBodyBaseBean.getTime();
      imei = RspBodyBaseBean.getIMEI(Select_HNSSCActivity.this);
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
      myHandler.sendEmptyMessage(Integer.parseInt(result));
      super.onPostExecute(result);
    }
  }

  /** 初始化UI */
  private void findView() {
    AppTools.isCanBet = true;
    bundle = new Bundle();
    btn_back = (ImageButton) findViewById(R.id.btn_back);
    btn_playtype = (Button) findViewById(R.id.btn_playtype);
    btn_help = (ImageButton) findViewById(R.id.btn_help);
    iv_up_down = (ImageView) findViewById(R.id.iv_up_down);
    layout_select_playtype = (LinearLayout) findViewById(R.id.layout_select_playtype);
    btn_clearall = (Button) findViewById(R.id.btn_clearall);
    btn_submit = (Button) findViewById(R.id.btn_submit);
    btn_select_all = (Button) findViewById(R.id.btn_select_all);
    select_all_rl = (RelativeLayout) findViewById(R.id.select_all_rl);
    tv_lotteryname = (TextView) findViewById(R.id.tv_lotteryname);
    tv_lotteryqi = (TextView) findViewById(R.id.tv_lotteryqi);
    select_time = (CustomDigitalClock2) findViewById(R.id.tv_selected_time);
    layout_shake = (LinearLayout) findViewById(R.id.layout_shake);
    iv_shake = (ImageView) findViewById(R.id.iv_shake);
    tv_shake = (TextView) findViewById(R.id.tv_shake);
    tv_selected_redball = (TextView) findViewById(R.id.tv_selected_redball);
    tv_tatol_count = (TextView) this.findViewById(R.id.tv_tatol_count);
    tv_tatol_money = (TextView) this.findViewById(R.id.tv_tatol_money);
    mSmanager = (SensorManager) getSystemService(SENSOR_SERVICE);
    layout_top_select = (RelativeLayout) findViewById(R.id.layout_top_select);
    tv_show1 = (TextView) this.findViewById(R.id.tv_show);
    tv_show2 = (TextView) this.findViewById(R.id.tv_show2);
    tv_show3 = (TextView) this.findViewById(R.id.tv_show3);
    tv_show4 = (TextView) this.findViewById(R.id.tv_show4);
    tv_show5 = (TextView) this.findViewById(R.id.tv_show5);
    tv_show6 = (TextView) this.findViewById(R.id.tv_show6);
    tv_tip = (TextView) this.findViewById(R.id.tv_tip);
    gv_one = (MyGridView) findViewById(R.id.number_sv_center_gv_showOne);
    gv_two = (MyGridView) findViewById(R.id.number_sv_center_gv_showTwo);
    gv_three = (MyGridView) findViewById(R.id.number_sv_center_gv_showThree);
    gv_four = (MyGridView) findViewById(R.id.number_sv_center_gv_showFour);
    gv_five = (MyGridView) findViewById(R.id.number_sv_center_gv_showFive);
    gv_six = (MyGridView) findViewById(R.id.number_sv_dxds);
    gv_seven = (MyGridView) findViewById(R.id.number_sv_dxds2);
    gv_dan = (MyGridView) findViewById(R.id.number_sv_center_gv_showOne_dan);
    gv_tuo = (MyGridView) findViewById(R.id.number_sv_center_gv_showTwo_tuo);
    rl_one = (RelativeLayout) this.findViewById(R.id.number_sv_center_rlOne);
    rl_two = (RelativeLayout) this.findViewById(R.id.number_sv_center_rlTwo);
    rl_three = (RelativeLayout) this
        .findViewById(R.id.number_sv_center_rlThree);
    rl_four = (RelativeLayout) this.findViewById(R.id.number_sv_center_rlFour);
    rl_five = (RelativeLayout) this.findViewById(R.id.number_sv_center_rlFive);
    rl_six = (RelativeLayout) this.findViewById(R.id.rl_dxds);
    rl_dan = (RelativeLayout) findViewById(R.id.number_sv_center_rlOne_dan);
    rl_tuo = (RelativeLayout) findViewById(R.id.number_sv_center_rlTwo_tuo);
    mAdapterOne = new GridView11X5Adapter(Select_HNSSCActivity.this, numbers,
        false, false);
    mAdapterTwo = new GridView11X5Adapter(Select_HNSSCActivity.this, numbers,
        false, false);
    mAdapterThree = new GridView11X5Adapter(Select_HNSSCActivity.this, numbers,
        false, false);
    mAdapterFour = new GridView11X5Adapter(Select_HNSSCActivity.this, numbers,
        false, false);
    mAdapterFive = new GridView11X5Adapter(Select_HNSSCActivity.this, numbers,
        false, false);

    mAdapterSix = new GridView11X5Adapter(Select_HNSSCActivity.this, dxds,
        false);
    mAdapterSeven = new GridView11X5Adapter(Select_HNSSCActivity.this, dxds,
        false);
    mAdapterDan = new GridView11X5Adapter(Select_HNSSCActivity.this, numbers,
        false, false);
    mAdapterTuo = new GridView11X5Adapter(Select_HNSSCActivity.this, numbers,
        false, false);
    gv_one.setAdapter(mAdapterOne);
    gv_two.setAdapter(mAdapterTwo);
    gv_three.setAdapter(mAdapterThree);
    gv_four.setAdapter(mAdapterFour);
    gv_five.setAdapter(mAdapterFive);
    gv_six.setAdapter(mAdapterSix);
    gv_seven.setAdapter(mAdapterSeven);
    gv_dan.setAdapter(mAdapterDan);
    gv_tuo.setAdapter(mAdapterTuo);
    btn_select_all.setOnClickListener(this);
    gv_one.setOnItemClickListener(new MyItemClickListener_1());
    gv_two.setOnItemClickListener(new MyItemClickListener_2());
    gv_three.setOnItemClickListener(new MyItemClickListener_3());
    gv_four.setOnItemClickListener(new MyItemClickListener_4());
    gv_five.setOnItemClickListener(new MyItemClickListener_5());
    gv_six.setOnItemClickListener(new MyItemClickListener_6());
    gv_seven.setOnItemClickListener(new MyItemClickListener_7());
    gv_dan.setOnItemClickListener(new MyItemClickListener_dan());
    gv_tuo.setOnItemClickListener(new MyItemClickListener_tuo());
    myHandler = new MyHandler();
    // 跳出玩法按钮
    // 胆拖
    mSmanager = (SensorManager) getSystemService(SENSOR_SERVICE);

  }

  private void initNum() {
    if (AppTools.lottery != null) {
      if (AppTools.lottery.getLastWinNumber() != null) {
        selected_redball = AppTools.lottery.getLastWinNumber();
      }
    }
    if (null != selected_redball) {
      selected_redball = selected_redball.replace(" ", "  ");
      tv_selected_redball.setText(selected_redball);
    } else {
      tv_selected_redball.setText("");
    }
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
        if (NetWork.isConnect(Select_HNSSCActivity.this) == true) {
          if (AppTools.lottery != null) {
            if (mAsynTask1 != null) {
              mAsynTask1 = null;
            }
            mAsynTask1 = new MyAsynTask1();
            mAsynTask1.execute();
          }
        } else {
          Toast.makeText(Select_HNSSCActivity.this, "网络连接异常，获得数据失败！",
              Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void remainFiveMinutes() {

      }
    });
  }

  /** 初始化属性 上期开奖号码 */
  private void init() {
    SmanagerView
        .registerSensorManager(mSmanager, getApplicationContext(), this);// 注册传感器
    vibrator = VibratorView.getVibrator(getApplicationContext());

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
    tv_lotteryname.setText("河内时时彩");
    Map<Integer, String> playType = new HashMap<Integer, String>();
    playType.put(0, "一星复式");
    playType.put(1, "二星复式");
    playType.put(2, "二星组选");
    playType.put(3, "三星复式");
    playType.put(4, "三星组三");
    playType.put(5, "三星组六");
    playType.put(6, "");
    playType.put(7, "");
    playType.put(8, "大小单双");
    Set<Integer> set = playType.keySet();
    int[] playtype_array = { 9203, 9203, 9206, 9203, 9211, 9212, 9203, 9205,
        9204 };
    for (Integer i : set) {
      playtypeMap.put(playtype_array[i], i);
    }
    data.put(0, playType);
    Map<Integer, String> playType_dan = new HashMap<Integer, String>();
    for (int i = 0; i < dan_play.length; i++) {
      playType_dan.put(i, dan_play[i]);
    }
    Set<Integer> setDan = playType_dan.keySet();
    playtypeMapDan = new HashMap<Integer, Integer>();
    for (Integer i : setDan) {
      if (i <= 1) {
        playtypeMapDan.put(
            ids_play_dan[i] + Integer.valueOf(AppTools.lottery.getLotteryID())
                * 100, i);
      }
    }
    data.put(1, playType_dan);

    dialog = new ConfirmDialog(this, R.style.dialog);
    btn_playtype.setText(data.get(parentIndex).get(itemIndex));
    showGridView();
  }

  class MyItemClickListener_1 implements OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
        long id) {
      if (null != vibrator)
        vibrator.vibrate(100);
      TextView tv = (TextView) view.findViewById(R.id.btn_showNum);
      String str = position + "";
      if (mAdapterOne.getOneSet().contains(str)) {
        mAdapterOne.removeOne(str);
        tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
        tv.setTextColor(Select_HNSSCActivity.this.getResources().getColor(
            R.color.red));
      } else {
        mAdapterOne.addOne(str);
        tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
        tv.setTextColor(Color.WHITE);
      }
      setTotalCount();
    }
  }

  class MyItemClickListener_2 implements OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
        long id) {
      if (null != vibrator)
        vibrator.vibrate(100);
      TextView tv = (TextView) view.findViewById(R.id.btn_showNum);
      String str = position + "";
      if (mAdapterTwo.getOneSet().contains(str)) {
        mAdapterTwo.removeOne(str);
        tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
        tv.setTextColor(Select_HNSSCActivity.this.getResources().getColor(
            R.color.red));
      } else {
        mAdapterTwo.addOne(str);
        tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
        tv.setTextColor(Color.WHITE);
      }
      setTotalCount();
    }
  }

  class MyItemClickListener_3 implements OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
        long id) {
      if (null != vibrator)
        vibrator.vibrate(100);
      TextView tv = (TextView) view.findViewById(R.id.btn_showNum);
      String str = position + "";

      if (mAdapterThree.getOneSet().contains(str)) {
        mAdapterThree.removeOne(str);
        tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
        tv.setTextColor(Select_HNSSCActivity.this.getResources().getColor(
            R.color.red));
      } else {
        mAdapterThree.addOne(str);
        tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
        tv.setTextColor(Color.WHITE);
      }
      setTotalCount();
    }
  }

  class MyItemClickListener_4 implements OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
        long id) {
      if (null != vibrator)
        vibrator.vibrate(100);
      TextView tv = (TextView) view.findViewById(R.id.btn_showNum);
      String str = position + "";
      if (mAdapterFour.getOneSet().contains(str)) {
        mAdapterFour.removeOne(str);
        tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
        tv.setTextColor(Select_HNSSCActivity.this.getResources().getColor(
            R.color.red));
      } else {
        mAdapterFour.addOne(str);
        tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
        tv.setTextColor(Color.WHITE);
      }
      setTotalCount();
    }
  }

  class MyItemClickListener_5 implements OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
        long id) {
      if (null != vibrator)
        vibrator.vibrate(100);
      TextView tv = (TextView) view.findViewById(R.id.btn_showNum);
      String str = position + "";
      if (mAdapterFive.getOneSet().contains(str)) {
        mAdapterFive.removeOne(str);
        tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
        tv.setTextColor(Select_HNSSCActivity.this.getResources().getColor(
            R.color.red));
      } else {
        mAdapterFive.addOne(str);
        tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
        tv.setTextColor(Color.WHITE);
      }
      setTotalCount();
    }
  }

  class MyItemClickListener_dan implements OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
        long id) {
      if (null != vibrator)
        vibrator.vibrate(100);
      TextView tv = (TextView) view.findViewById(R.id.btn_showNum);
      String str = position + "";
      if (mAdapterDan.getOneSet().contains(str)) {
        mAdapterDan.removeOne(str);
        tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
        tv.setTextColor(Select_HNSSCActivity.this.getResources().getColor(
            R.color.red));
      } else {
        if (itemIndex == 0) {
          if (mAdapterDan.getOneSetSize() >= 1) {
            MyToast.getToast(Select_HNSSCActivity.this, "胆码最多只能选" + 1 + "个")
                .show();
            return;
          }
        } else if (itemIndex == 1) {
          if (mAdapterDan.getOneSetSize() > 1) {
            MyToast.getToast(Select_HNSSCActivity.this, "胆码最多只能选" + 2 + "个")
                .show();
            return;
          }
        }
        mAdapterTuo.removeOne(str);
        mAdapterTuo.notifyDataSetChanged();
        mAdapterDan.addOne(str);
        tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
        tv.setTextColor(Color.WHITE);
      }
      setTotalCount();
    }

  }

  class MyItemClickListener_tuo implements OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
        long id) {
      if (null != vibrator)
        vibrator.vibrate(100);
      TextView tv = (TextView) view.findViewById(R.id.btn_showNum);
      String str = position + "";
      if (mAdapterTuo.getOneSet().contains(str)) {
        mAdapterTuo.removeOne(str);
        tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
        tv.setTextColor(Select_HNSSCActivity.this.getResources().getColor(
            R.color.red));
      } else {
        if (mAdapterTuo.getOneSetSize() >= 9) {
          MyToast.getToast(Select_HNSSCActivity.this, "拖码最多只能选" + 9 + "个")
              .show();
          return;
        }
        mAdapterDan.removeOne(str);
        mAdapterDan.notifyDataSetChanged();
        mAdapterTuo.addOne(str);
        tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
        tv.setTextColor(Color.WHITE);
      }
      setTotalCount();
    }

  }

  class MyItemClickListener_6 implements OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
        long id) {
      if (null != vibrator)
        vibrator.vibrate(100);
      TextView tv = (TextView) view.findViewById(R.id.btn_showNum);
      if (mAdapterSix.getIndexSet().contains(position)) {
        mAdapterSix.removeIndex(position);
        tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
        tv.setTextColor(Select_HNSSCActivity.this.getResources().getColor(
            R.color.red));
      } else {
        mAdapterSix.removeIndexAll();
        mAdapterSix.addIndex(position);
        tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
        tv.setTextColor(Color.WHITE);
      }
      setTotalCount();
    }
  }

  class MyItemClickListener_7 implements OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
        long id) {
      if (null != vibrator)
        vibrator.vibrate(100);
      TextView tv = (TextView) view.findViewById(R.id.btn_showNum);
      if (mAdapterSeven.getIndexSet().contains(position)) {
        mAdapterSeven.removeIndex(position);
        tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
        tv.setTextColor(Select_HNSSCActivity.this.getResources().getColor(
            R.color.red));
      } else {
        mAdapterSeven.removeIndexAll();
        mAdapterSeven.addIndex(position);
        tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
        tv.setTextColor(Color.WHITE);
      }
      setTotalCount();
    }
  }

  /** 计算注数 */
  private void setTotalCount() {
    if (parentIndex == 0) {
      if (itemIndex == 8)
        AppTools.totalCount = mAdapterSix.getIndexSetSize()
            * mAdapterSeven.getIndexSetSize();
      else {
        AppTools.totalCount = NumberTools.getSSC_count(mAdapterOne.getOneSet(),
            mAdapterTwo.getOneSet(), mAdapterThree.getOneSet(),
            mAdapterFour.getOneSet(), mAdapterFive.getOneSet(), itemIndex + 1);
      }
    } else if (parentIndex == 1) {
      // 组三
      if (itemIndex == 0) {
        AppTools.totalCount = NumberTools.getSSC_Z3Count(
            mAdapterDan.getOneSetSize(), mAdapterTuo.getOneSetSize());
        // 组六
      } else if (itemIndex == 1) {
        AppTools.totalCount = NumberTools.get11X5Count_dan(
            mAdapterDan.getOneSetSize(), mAdapterTuo.getOneSetSize(), 3);
      }
    }
    tv_tatol_count.setText(+AppTools.totalCount + "");
    tv_tatol_money.setText((AppTools.totalCount * 2) + "");

  }

  /**
   * 全选功能{0={0=一星复式, 1=二星复式, 2=二星组选, 3=三星复式, 4=三星组三, 5=三星组六, 6=五星复式, 7=五星通选,
   * 8=大小单双},1={0=三星组三胆拖,1=三星组六胆拖}}
   * 
   * 
   * 一星复式，二星组选，三星组三，三星组三胆拖，三星组六，三星组六胆拖，
   */
  private void selectAll() {
    if (null != vibrator)
      vibrator.vibrate(100);
    if (parentIndex == 0) {
      if (itemIndex == 0 || itemIndex == 2 || itemIndex == 4 || itemIndex == 5) {
        mAdapterOne.addAll();
      } else {

      }
    } else if (parentIndex == 1) {
      mAdapterDan.removeAll();
      mAdapterTuo.addAll();
    }
    setTotalCount();
  }

  /** 公共点击监听 */
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.btn_select_all:
      selectAll();
      break;
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
    case R.id.layout_shake2:
    case R.id.iv_shake2:
    case R.id.tv_shake2:
      if (null != vibrator)
        vibrator.vibrate(300);
      selectRandom();// 机选
      break;
    /** 选玩法 **/
    case R.id.layout_select_playtype:
    case R.id.btn_playtype:
    case R.id.iv_up_down:
      /**
       * {0={0=一星复式, 1=二星复式, 2=二星组选, 3=三星复式, 4=三星组三, 5=三星组六, 6=五星复式, 7=五星通选,
       * 8=大小单双}}
       */
      popUtil = new PopupWindowUtil(this, data, layout_top_select);
      popUtil.setSelectIndex(parentIndex, itemIndex);
      popUtil.createPopWindow();
      popUtil.setOnSelectedListener(new PopupWindowUtil.OnSelectedListener() {
        @Override
        public void getIndex(int parentIndex, int itemIndex) {
          // TODO Auto-generated method stub
          if (parentIndex == 0 && itemIndex == 6 || parentIndex == 0
              && itemIndex == 7) {
            return;
          }
          if (itemIndex != Select_HNSSCActivity.this.itemIndex
              || parentIndex != Select_HNSSCActivity.this.parentIndex) {
            Select_HNSSCActivity.this.parentIndex = parentIndex;
            Select_HNSSCActivity.this.itemIndex = itemIndex;
            initSelectAll();
            changePlayType();
            AppTools.totalCount = 0;
            clear();
          }
          rote(2);// 旋转动画 向下
        }
      });
      rote(1);// 旋转动画 向上
      break;
    }
  }

  /**
   * 设置全选按钮{0={0=一星复式, 1=二星复式, 2=二星组选, 3=三星复式, 4=三星组三, 5=三星组六, 6=五星复式, 7=五星通选,
   * 8=大小单双},1={0=三星组三胆拖,1=三星组六胆拖}}
   * 
   * 
   * 一星复式，二星组选，三星组三，三星组三胆拖，三星组六，三星组六胆拖，
   */
  private void initSelectAll() {
    if (parentIndex == 0) {
      if (itemIndex == 0 || itemIndex == 2 || itemIndex == 4 || itemIndex == 5) {
        select_all_rl.setVisibility(View.VISIBLE);
      } else {
        select_all_rl.setVisibility(View.GONE);
      }
    } else if (parentIndex == 1) {
      select_all_rl.setVisibility(View.VISIBLE);
    }
  }

  public void changePlayType() {
    btn_playtype.setText(data.get(parentIndex).get(itemIndex));
    layout_shake.setVisibility(View.INVISIBLE);
    if (parentIndex == 0) {
      switch (itemIndex) {
      case 0:// 一星复式
        if (itemIndex != 0)
          clear();
        itemIndex = 0;
        showGridView();
        layout_shake.setVisibility(View.VISIBLE);
        SmanagerView.registerSensorManager(mSmanager, getApplicationContext(),
            this);
        vibrator = VibratorView.getVibrator(getApplicationContext());
        break;
      case 1:// 二星复式
        if (itemIndex != 1)
          clear();
        itemIndex = 1;
        showGridView();
        layout_shake.setVisibility(View.VISIBLE);
        SmanagerView.registerSensorManager(mSmanager, getApplicationContext(),
            this);
        vibrator = VibratorView.getVibrator(getApplicationContext());
        break;
      case 2:// 二星组选
        if (itemIndex != 2)
          clear();
        itemIndex = 2;
        showGridView();
        vibrator = null;
        SmanagerView.unregisterSmanager(mSmanager, this);
        break;
      case 3:// 三星复式
        if (itemIndex != 3)
          clear();
        itemIndex = 3;
        showGridView();
        layout_shake.setVisibility(View.VISIBLE);
        SmanagerView.registerSensorManager(mSmanager, getApplicationContext(),
            this);
        vibrator = VibratorView.getVibrator(getApplicationContext());
        break;
      case 4:// 三星组三
        if (itemIndex != 4)
          clear();
        itemIndex = 4;
        showGridView();
        vibrator = null;
        SmanagerView.unregisterSmanager(mSmanager, this);
        break;
      case 5:// 三星组六
        if (itemIndex != 5)
          clear();
        itemIndex = 5;
        showGridView();
        vibrator = null;
        SmanagerView.unregisterSmanager(mSmanager, this);
        break;
      // case 6:// 五星复式
      // if (itemIndex != 6)
      // clear();
      // itemIndex = 6;
      // showGridView();
      // layout_shake.setVisibility(View.VISIBLE);
      // SmanagerView.registerSensorManager(mSmanager,
      // getApplicationContext(), this);
      // vibrator = VibratorView.getVibrator(getApplicationContext());
      // break;
      // case 7:// 五星通选
      // if (itemIndex != 7)
      // clear();
      // itemIndex = 7;
      // showGridView();
      // vibrator = null;
      // SmanagerView.unregisterSmanager(mSmanager, this);
      // break;
      case 8:// 大小单双
        if (itemIndex != 8)
          clear();
        itemIndex = 8;
        showGridView();
        vibrator = null;
        SmanagerView.unregisterSmanager(mSmanager, this);
        break;
      }
    }
    if (parentIndex == 1) {
      switch (itemIndex) {
      case 0:
        // Toast.makeText(Select_HNSSCActivity.this, "三星组三胆拖",
        // Toast.LENGTH_SHORT).show();
        if (itemIndex != 0)
          clear();
        itemIndex = 0;
        showGridView();
        vibrator = null;
        SmanagerView.unregisterSmanager(mSmanager, this);
        break;
      case 1:
        if (itemIndex != 1)
          clear();
        itemIndex = 1;
        showGridView();
        vibrator = null;
        SmanagerView.unregisterSmanager(mSmanager, this);
        // Toast.makeText(Select_HNSSCActivity.this, "三星组六胆拖",
        // Toast.LENGTH_SHORT).show();
        break;

      }
    }
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

  /** 从投注页面跳转过来 将投注页面的值 显示出来 */
  public void getItem() {
    Intent intent = Select_HNSSCActivity.this.getIntent();
    /* 幸运选号 回到选号页面时候 */
    if (AppTools.list_numbers != null && AppTools.list_numbers.size() != 0)
      playType = AppTools.list_numbers.get(0).getPlayType();
    if (intent != null) {
      bundle = intent.getBundleExtra("SSCBundle");
    }

    if (null != bundle) {
      itemIndex = bundle.getInt("type") - 1;
      playType = bundle.getInt("playtype");
      // 胆拖的话
      if (playType == 9216 || playType == 9217) {
        parentIndex = 1;
        if (playType == 9216) {
          strSXZSDT = bundle.getString("Bet_sxzsdt");
          String danMa = strSXZSDT.split(",")[0];
          String tuoMa = strSXZSDT.split(",")[1];
          String[] dList = danMa.split(" ");
          String[] tList = tuoMa.split(" ");
          for (int i = 0; i < dList.length; i++) {
            mAdapterDan.addOne(dList[i]);
          }
          for (int i = 0; i < tList.length; i++) {
            mAdapterTuo.addOne(tList[i]);
          }
        } else if (playType == 9217) {
          strSXZLDT = bundle.getString("Bet_sxzldt");
          String danMa = strSXZLDT.split(",")[0];
          String tuoMa = strSXZLDT.split(",")[1];
          String[] dList = danMa.split(" ");
          String[] tList = tuoMa.split(" ");
          for (int i = 0; i < dList.length; i++) {
            mAdapterDan.addOne(dList[i]);
          }
          for (int i = 0; i < tList.length; i++) {
            mAdapterTuo.addOne(tList[i]);
          }

        }
      } else if (itemIndex == 8) {
        strDXDS = bundle.getString("Bet_dxds");

        if (strDXDS != null && strDXDS.split(",").length == 2) {
          if (strDXDS.split(",")[0].contains("大"))
            mAdapterSix.addIndex(0);
          if (strDXDS.split(",")[0].contains("小"))
            mAdapterSix.addIndex(1);
          if (strDXDS.split(",")[0].contains("单"))
            mAdapterSix.addIndex(2);
          if (strDXDS.split(",")[0].contains("双"))
            mAdapterSix.addIndex(3);

          if (strDXDS.split(",")[1].contains("大"))
            mAdapterSeven.addIndex(0);
          if (strDXDS.split(",")[1].contains("小"))
            mAdapterSeven.addIndex(1);
          if (strDXDS.split(",")[1].contains("单"))
            mAdapterSeven.addIndex(2);
          if (strDXDS.split(",")[1].contains("双"))
            mAdapterSeven.addIndex(3);
        }
        mAdapterSix.notifyDataSetChanged();
        mAdapterSeven.notifyDataSetChanged();
      } else {
        list = bundle.getStringArrayList("oneSet");
        mAdapterOne.setOneSet(list);
        list2 = bundle.getStringArrayList("twoSet");
        mAdapterTwo.setOneSet(list2);
        list3 = bundle.getStringArrayList("threeSet");
        mAdapterThree.setOneSet(list3);
        list4 = bundle.getStringArrayList("fourSet");
        mAdapterFour.setOneSet(list4);
        list5 = bundle.getStringArrayList("fiveSet");
        mAdapterFive.setOneSet(list5);
        mAdapterOne.notifyDataSetChanged();
        mAdapterTwo.notifyDataSetChanged();
        mAdapterThree.notifyDataSetChanged();
        mAdapterFour.notifyDataSetChanged();
        mAdapterFive.notifyDataSetChanged();
      }
      setTotalCount();
      changePlayType();
    }

  }

  /** 显示GridView */
  private void showGridView() {
    rl_six.setVisibility(View.GONE);
    vibrator = VibratorView.getVibrator(Select_HNSSCActivity.this);
    if (parentIndex == 0) {
      switch (itemIndex) {
      case 0:
        showGridView(View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE);
        btn_playtype.setText("一星复式");
        playType = 9203;
        tv_show1.setText("个位");
        tv_tip.setText("请至少选择一个号码");
        break;
      case 1:
        showGridView(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE,
            View.GONE);
        tv_show2.setText("个位");
        tv_show1.setText("十位");
        btn_playtype.setText("二星复式");
        tv_tip.setText("每位至少选择一个号码");
        playType = 9203;
        break;
      case 2:
        showGridView(View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE);
        tv_show1.setText("选号");
        btn_playtype.setText("二星组选");
        tv_tip.setText("至少选择二个号码");
        playType = 9206;
        break;
      case 3:
        showGridView(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE,
            View.GONE);
        tv_show1.setText("百位");
        tv_show2.setText("十位");
        tv_show3.setText("个位");
        btn_playtype.setText("三星复式");
        tv_tip.setText("每位至少选择一个号码");
        playType = 9203;
        break;
      case 4:
        showGridView(View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE);
        tv_show1.setText("选号");
        btn_playtype.setText("三星组三");
        tv_tip.setText("至少选择二个号码");
        playType = 9211;
        break;
      case 5:
        showGridView(View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE);
        tv_show1.setText("选号");
        btn_playtype.setText("三星组六");
        tv_tip.setText("至少选择三个号码");
        playType = 9212;
        break;
      case 6:
        showGridView(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
            View.VISIBLE);
        tv_show1.setText("万位");
        tv_show2.setText("千位");
        tv_show3.setText("百位");
        tv_show4.setText("十位");
        tv_show5.setText("个位");
        playType = 9203;
        btn_playtype.setText("五星复式");
        tv_tip.setText("每位至少选择一个号码");
        break;
      case 7:
        showGridView(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
            View.VISIBLE);
        tv_show1.setText("万位");
        tv_show2.setText("千位");
        tv_show3.setText("百位");
        tv_show4.setText("十位");
        tv_show5.setText("个位");
        playType = 9205;
        btn_playtype.setText("五星通选");
        tv_tip.setText("每位至少选择一个号码");
        break;
      case 8:
        showGridView(View.GONE, View.GONE, View.GONE, View.GONE, View.GONE);
        btn_playtype.setText("大小单双");
        playType = 9204;
        tv_show6.setText("选号");
        rl_six.setVisibility(View.VISIBLE);
        tv_tip.setText("请选择大小单双");
        break;
      }
    }
    if (parentIndex == 1) {
      switch (itemIndex) {
      case 0:
        showGridView(View.GONE, View.GONE, View.GONE, View.GONE, View.GONE);
        rl_dan.setVisibility(View.VISIBLE);
        rl_tuo.setVisibility(View.VISIBLE);
        playType = 9216;
        tv_tip.setText("请选择胆拖号码");
        break;
      case 1:
        showGridView(View.GONE, View.GONE, View.GONE, View.GONE, View.GONE);
        rl_dan.setVisibility(View.VISIBLE);
        rl_tuo.setVisibility(View.VISIBLE);
        playType = 9217;
        tv_tip.setText("请选择胆拖号码");
        break;
      }

    }
  }

  /** 显示GridView */
  private void showGridView(int one, int two, int three, int four, int five) {
    rl_one.setVisibility(one);
    rl_two.setVisibility(two);
    rl_three.setVisibility(three);
    rl_four.setVisibility(four);
    rl_five.setVisibility(five);
    rl_dan.setVisibility(View.GONE);
    rl_tuo.setVisibility(View.GONE);
  }

  /** 玩法说明 */
  private void playExplain() {
    Intent intent = new Intent(Select_HNSSCActivity.this, PlayDescription.class);
    Select_HNSSCActivity.this.startActivity(intent);
  }

  /* 提供震动 */
  public void vibrator() {
    if (null != vibrator)
      vibrator.vibrate(300);
  }

  /** 机选 按钮点击 */
  public void selectRandom() {
    switch (itemIndex) {
    case 0:
      mAdapterOne.setOneSet(NumberTools.getRandomNum(1, 9));
      vibrator();
      break;
    case 1:
      mAdapterOne.setOneSet(NumberTools.getRandomNum(1, 9));
      mAdapterTwo.setOneSet(NumberTools.getRandomNum(1, 9));
      vibrator();
      break;
    case 3:
      mAdapterOne.setOneSet(NumberTools.getRandomNum(1, 9));
      mAdapterTwo.setOneSet(NumberTools.getRandomNum(1, 9));
      mAdapterThree.setOneSet(NumberTools.getRandomNum(1, 9));
      vibrator();
      break;
    case 6:
      /* 五星通选摇一摇 机选一组 */
      // case 8:
      mAdapterOne.setOneSet(NumberTools.getRandomNum(1, 9));
      mAdapterTwo.setOneSet(NumberTools.getRandomNum(1, 9));
      mAdapterThree.setOneSet(NumberTools.getRandomNum(1, 9));
      mAdapterFour.setOneSet(NumberTools.getRandomNum(1, 9));
      mAdapterFive.setOneSet(NumberTools.getRandomNum(1, 9));
      vibrator();
      break;
    /* 大小单双摇一摇功能 */
    // case 9:
    // mAdapterSix.setIndexSet(NumberTools.getRandomNum(1, 4));
    // mAdapterSeven.setIndexSet(NumberTools.getRandomNum(1, 4));
    // break;
    default:
      break;
    }
    update();
    setTotalCount();
  }

  private void update() {
    if (null == mAdapterOne || null == mAdapterTwo || null == mAdapterThree)
      return;
    mAdapterOne.notifyDataSetChanged();
    mAdapterTwo.notifyDataSetChanged();
    mAdapterThree.notifyDataSetChanged();
    mAdapterFour.notifyDataSetChanged();
    mAdapterFive.notifyDataSetChanged();
    mAdapterSix.notifyDataSetChanged();
    mAdapterSeven.notifyDataSetChanged();
    Log.i("x", "刷新了update");
  }

  /** 提交号码 */
  private void submitNumber() {
    if (AppTools.totalCount == 0) {
      MyToast.getToast(Select_HNSSCActivity.this, "请至少选择一注").show();
    } else if (AppTools.totalCount > 10000) {
      MyToast.getToast(Select_HNSSCActivity.this, "单注金额不能超过20000").show();
    } else {
      Intent intent = new Intent(Select_HNSSCActivity.this,
          Bet_HNSSCActivity.class);
      Bundle bundle = new Bundle();
      bundle.putString("one", AppTools.sortSet(mAdapterOne.getOneSet())
          .toString().replace("[", "").replace("]", ""));
      bundle.putString("two", AppTools.sortSet(mAdapterTwo.getOneSet())
          .toString().replace("[", "").replace("]", ""));
      bundle.putString("three", AppTools.sortSet(mAdapterThree.getOneSet())
          .toString().replace("[", "").replace("]", ""));
      bundle.putString("four", AppTools.sortSet(mAdapterFour.getOneSet())
          .toString().replace("[", "").replace("]", ""));
      bundle.putString("five", AppTools.sortSet(mAdapterFive.getOneSet())
          .toString().replace("[", "").replace("]", ""));
      bundle.putString("dan", AppTools.sortSet(mAdapterDan.getOneSet())
          .toString().replace("[", "").replace("]", ""));
      bundle.putString("tuo", AppTools.sortSet(mAdapterTuo.getOneSet())
          .toString().replace("[", "").replace("]", ""));
      String d = "";
      for (Integer i : mAdapterSix.getIndexSet()) {
        d += dxds[i];
      }
      String d2 = "";
      for (Integer i : mAdapterSeven.getIndexSet()) {
        d2 += dxds[i];
      }
      d += "," + d2;
      bundle.putString("dxds", d);

      bundle.putInt("type", itemIndex + 1);
      bundle.putInt("playType", playType);
      intent.putExtra("bundle", bundle);
      Select_HNSSCActivity.this.startActivity(intent);
    }

  }

  /** 清空 */
  private void clear() {
    if (null == mAdapterOne || null == mAdapterTwo || null == mAdapterThree
        || null == mAdapterFour || null == mAdapterFive || null == mAdapterSix
        || null == mAdapterSeven) {
      return;
    }
    mAdapterOne.clear();
    mAdapterTwo.clear();
    mAdapterThree.clear();
    mAdapterFour.clear();
    mAdapterFive.clear();
    mAdapterSix.clear();
    mAdapterSeven.clear();
    mAdapterDan.clear();
    mAdapterTuo.clear();
    mAdapterDan.notifyDataSetChanged();
    mAdapterTuo.notifyDataSetChanged();
    mAdapterOne.notifyDataSetChanged();
    mAdapterTwo.notifyDataSetChanged();
    mAdapterThree.notifyDataSetChanged();
    mAdapterFour.notifyDataSetChanged();
    mAdapterFive.notifyDataSetChanged();
    mAdapterSix.notifyDataSetChanged();
    mAdapterSeven.notifyDataSetChanged();
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
    // TODO Auto-generated method stub

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
    if (speed >= 500 && currentUpdateTime - vTime > 700) {
      vTime = System.currentTimeMillis();
      selectRandom();
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
      if (mAdapterOne.getOneSetSize() != 0 || mAdapterTwo.getOneSetSize() != 0
          || mAdapterThree.getOneSetSize() != 0
          || mAdapterFour.getOneSetSize() != 0
          || mAdapterFive.getOneSetSize() != 0
          || mAdapterSix.getIndexSetSize() != 0
          || mAdapterSeven.getIndexSetSize() != 0
          || mAdapterDan.getOneSetSize() != 0
          || mAdapterTuo.getOneSetSize() != 0) {
        dialog.show();
        dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
        dialog
            .setDialogResultListener(new ConfirmDialog.DialogResultListener() {
              @Override
              public void getResult(int resultCode) {
                // TODO Auto-generated method stub
                if (1 == resultCode) {// 确定
                  clear();
                  AppTools.totalCount = 0;
                  for (int i = 0; i < App.activityS1.size(); i++) {
                    App.activityS1.get(i).finish();
                  }
                }
              }
            });
      } else {
        clear();
        for (int i = 0; i < App.activityS1.size(); i++) {
          App.activityS1.get(i).finish();
        }
      }
    } else if (AppTools.list_numbers != null
        && AppTools.list_numbers.size() != 0) {
      if (mAdapterOne.getOneSetSize() != 0 || mAdapterTwo.getOneSetSize() != 0
          || mAdapterThree.getOneSetSize() != 0
          || mAdapterFour.getOneSetSize() != 0
          || mAdapterFive.getOneSetSize() != 0
          || mAdapterSix.getIndexSetSize() != 0
          || mAdapterSeven.getIndexSetSize() != 0
          || mAdapterDan.getOneSetSize() != 0
          || mAdapterTuo.getOneSetSize() != 0) {
        dialog.show();
        dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
        dialog
            .setDialogResultListener(new ConfirmDialog.DialogResultListener() {
              @Override
              public void getResult(int resultCode) {
                // TODO Auto-generated method stub
                if (1 == resultCode) {// 确定
                  clear();
                  Intent intent = new Intent(Select_HNSSCActivity.this,
                      Bet_HNSSCActivity.class);
                  Bundle bundle = new Bundle();
                  bundle.putInt("playType", playType);
                  bundle.putInt("btnIndex", itemIndex + 1);
                  intent.putExtra("bundle1", bundle);
                  Select_HNSSCActivity.this.startActivity(intent);
                  Select_HNSSCActivity.this.finish();
                }
              }
            });
      } else {
        clear();
        Intent intent = new Intent(Select_HNSSCActivity.this,
            Bet_HNSSCActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("playType", playType);
        bundle.putInt("btnIndex", itemIndex + 1);
        intent.putExtra("bundle1", bundle);
        Select_HNSSCActivity.this.startActivity(intent);
        Select_HNSSCActivity.this.finish();
      }
    }
  }

  /** 刷新Adapter */
  public void updateAdapter() {
    tv_tatol_count.setText(AppTools.totalCount + "");
    tv_tatol_money.setText((AppTools.totalCount * 2) + "");
  }

  /** 异步任务 用来后台获取数据 */
  class MyAsynTask extends AsyncTask<Integer, Integer, String> {
    @Override
    protected String doInBackground(Integer... params) {
      return AppTools.getDate(playID + "", Select_HNSSCActivity.this);
    }

    @Override
    protected void onPostExecute(String result) {
      // TODO Auto-generated method stub
      if ("-500".equals(result)) {
        myHandler.sendEmptyMessage(-500);
        return;
      }
      if (result != null)
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
        index = 1;
        AppTools.isCanBet = true;
        for (Lottery ll : HallFragment.listLottery) {
          if (ll.getLotteryID().equals("62") || ll.getLotteryID().equals("70")) {
            AppTools.lottery = ll;
          }
        }
        Log.i("x", "lottery.倒计时====="
            + (AppTools.lottery.getDistanceTime() - System.currentTimeMillis()));
        break;
      case -500:
        MyToast.getToast(getApplicationContext(), "连接超时，请检查网络").show();
        break;
      case 100:
        initNum();
        break;
      case 101:
        MyToast.getToast(getApplicationContext(), "获取中奖信息失败！").show();
        break;
      }
      super.handleMessage(msg);
    }
  }

  @Override
  protected void onDestroy() {
    App.activityS.remove(this);
    super.onDestroy();
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

}
