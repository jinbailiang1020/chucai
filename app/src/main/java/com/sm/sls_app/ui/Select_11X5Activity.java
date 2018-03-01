package com.sm.sls_app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
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

/**
 * 11选5 的选球页面 功能： 选号界面，实现选号
 * 
 * @author Kinwee 修改日期2015-1-6
 * 
 */
public class Select_11X5Activity extends Activity implements OnClickListener,
    SensorEventListener {
  private final static String TAG = "Select_11X5Activity";
  private Context context = Select_11X5Activity.this;

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

  private RelativeLayout rl_one, rl_two, rl_three;

  private MyGridView gv_one, gv_two, gv_three;
  private GridView11X5Adapter mAdapterOne, mAdapterTwo, mAdapterThree;

  private TextView tv_show1, tv_show2, tv_show3;

  private String[] numbers = new String[] { "1", "2", "3", "4", "5", "6", "7",
      "8", "9", "10", "11" };
  /* 全选部分 */
  private RelativeLayout select_all_rl;
  private Button btn_select_all;
  /* 中间部分 */
  private ScrollView sv_show_ball;
  private TextView tv_lotteryname, tv_lotteryqi;// 彩种名,最后开奖期号
  private CustomDigitalClock2 select_time;// 本期倒计时
  // 中奖的红色蓝色号码
  private TextView tv_selected_redball, tv_selected_blueball;
  // 6201 任一 02 03 04 05 06 07 08 任几 09 10 直二(三)
  // 11 12组二(三) 13 14 组选前二(三)胆拖 15 16 17 18 19 20 任选二(三四五六七)
  private int playType = 6202;

  private int play, play2;

  private LinearLayout layout_shake;// 摇一摇
  private ImageView iv_shake;// 摇一摇
  private TextView tv_shake;// 摇一摇

  private String selected_redball;// 中奖的红球号码 tv_selected_redball
  private String selected_blueball; // 中奖的蓝球号码 tv_selected_blueball

  private Bundle bundle;

  public Vibrator vibrator; // 震动器
  private SensorManager mSmanager; // 传感器

  private int type = 1;

  /**
   * 传感器
   */
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
  private int itemIndex = 0;

  private Map<Integer, Integer> playtypeMap;
  private Map<Integer, Integer> playtypeMapDan;
  private String[] normal_play = { "任选二", "任选三", "任选四", "任选五", "任选六", "任选七",
      "任选八", "前一", "前二直选", "前二组选", "前三直选", "前三组选" };
  private String[] dan_play = { "任选二", "任选三", "任选四", "任选五", "任选六", "任选七", "前二",
      "前三", "" };
  private int[] ids_play = { 2, 3, 4, 5, 6, 7, 8, 1, 9, 11, 10, 12 };
  private int[] ids_play_dan = { 15, 16, 17, 18, 19, 20, 13, 14 };
  private String auth, info, time, imei, crc; // 格式化后的参数
  private String opt = "13"; // 格式化后的 opt
  private MyAsynTask mAsynTask;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // 设置无标题
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_select_11x5);
    App.activityS.add(this);
    App.activityS1.add(this);
    findView();
    init();
    setListener();
    if (NetWork.isConnect(context)) {
      if (null != AppTools.lottery) {
        mAsynTask = new MyAsynTask();
        mAsynTask.execute();
      }
    } else {
      Toast.makeText(context, "网络连接异常，获得数据失败！", Toast.LENGTH_SHORT).show();
    }
  }

  /*** 异步任务 用来后台获取数据 */
  class MyAsynTask extends AsyncTask<Void, Integer, String> {
    /** 在后台执行的程序 */
    @Override
    protected String doInBackground(Void... params) {
      time = RspBodyBaseBean.getTime();
      imei = RspBodyBaseBean.getIMEI(Select_11X5Activity.this);
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
        e.printStackTrace();
      }

      return "101";
    }

    @Override
    protected void onPostExecute(String result) {
      Myhandler mHandler = new Myhandler();
      mHandler.sendEmptyMessage(Integer.parseInt(result));
      super.onPostExecute(result);
    }
  }

  class Myhandler extends Handler {

    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
      case 100:
        initNum();
        break;
      case 101:
        Toast.makeText(context, "获取中奖信息失败!", Toast.LENGTH_SHORT).show();
        break;
      default:
        break;
      }
    }

  }

  /**
   * 初始化中奖号码信息
   */
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
    if (null != selected_blueball) {
      tv_selected_blueball.setText("  " + selected_blueball);
    } else {
      tv_selected_blueball.setText(" ");
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
        if (NetWork.isConnect(Select_11X5Activity.this) == true) {
          if (AppTools.lottery != null) {
            if (mAsynTask != null) {
              mAsynTask = null;
            }
            mAsynTask = new MyAsynTask();
            mAsynTask.execute();
          }
        } else {
          Toast.makeText(Select_11X5Activity.this, "网络连接异常，获得数据失败！",
              Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void remainFiveMinutes() {

      }
    });
  }

  /**
   * 初始化UI
   */
  private void findView() {
    bundle = new Bundle();
    btn_back = (ImageButton) findViewById(R.id.btn_back);
    btn_playtype = (Button) findViewById(R.id.btn_playtype);
    btn_help = (ImageButton) findViewById(R.id.btn_help);
    iv_up_down = (ImageView) findViewById(R.id.iv_up_down);
    layout_select_playtype = (LinearLayout) findViewById(R.id.layout_select_playtype);
    btn_clearall = (Button) findViewById(R.id.btn_clearall);
    btn_submit = (Button) findViewById(R.id.btn_submit);
    select_all_rl = (RelativeLayout) findViewById(R.id.select_all_rl);
    btn_select_all = (Button) findViewById(R.id.btn_select_all);
    tv_lotteryname = (TextView) findViewById(R.id.tv_lotteryname);
    tv_lotteryqi = (TextView) findViewById(R.id.tv_lotteryqi);
    select_time = (CustomDigitalClock2) findViewById(R.id.tv_selected_time);
    layout_shake = (LinearLayout) findViewById(R.id.layout_shake);
    iv_shake = (ImageView) findViewById(R.id.iv_shake);
    tv_shake = (TextView) findViewById(R.id.tv_shake);
    sv_show_ball = (ScrollView) findViewById(R.id.sv_show_ball);
    tv_selected_redball = (TextView) findViewById(R.id.tv_selected_redball);
    tv_selected_blueball = (TextView) findViewById(R.id.tv_selected_blueball);
    tv_tatol_count = (TextView) this.findViewById(R.id.tv_tatol_count);
    tv_tatol_money = (TextView) this.findViewById(R.id.tv_tatol_money);

    mSmanager = (SensorManager) getSystemService(SENSOR_SERVICE);
    layout_top_select = (RelativeLayout) findViewById(R.id.layout_top_select);

    // 选码区
    rl_one = (RelativeLayout) this.findViewById(R.id.number_sv_center_rlOne);
    rl_two = (RelativeLayout) this.findViewById(R.id.number_sv_center_rlTwo);
    rl_three = (RelativeLayout) this
        .findViewById(R.id.number_sv_center_rlThree);
    // 选码区提示
    tv_show1 = (TextView) this.findViewById(R.id.tv_show);
    tv_show2 = (TextView) this.findViewById(R.id.tv_show2);
    tv_show3 = (TextView) this.findViewById(R.id.tv_show3);
    // 选码gridview
    gv_one = (MyGridView) this.findViewById(R.id.number_sv_center_gv_showOne);
    gv_two = (MyGridView) this.findViewById(R.id.number_sv_center_gv_showTwo);
    gv_three = (MyGridView) this
        .findViewById(R.id.number_sv_center_gv_showThree);
    // 选码adapter
    mAdapterOne = new GridView11X5Adapter(context, numbers, true);
    mAdapterTwo = new GridView11X5Adapter(context, numbers, true);
    mAdapterThree = new GridView11X5Adapter(context, numbers, true);

  }

  /**
   * 初始化属性 上期开奖号码
   */
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
    btn_select_all.setOnClickListener(this);
    settings = getSharedPreferences("app_user", 0);// 获取SharedPreference对象
    editor = settings.edit();// 获取编辑对象
    if ("70".equals(AppTools.lottery.getLotteryID())) {
      tv_lotteryname.setText("江西11选5");
      playType = 7002;
    } else if ("62".equals(AppTools.lottery.getLotteryID())) {
      tv_lotteryname.setText("十一运夺金");
      playType = 6202;
    } else if ("78".equals(AppTools.lottery.getLotteryID())) {
      tv_lotteryname.setText("广东11选5");
      playType = 7802;
    }
    play2 = Integer.parseInt(AppTools.lottery.getLotteryID()) * 100;
    play = playType - Integer.parseInt(AppTools.lottery.getLotteryID()) * 100;/**/
    initData();
    String tip = "";
    if (0 == parentIndex) {// 普通
      tip = "普通-";
    } else if (1 == parentIndex) {// 胆拖
      tip = "胆拖-";
    }
    btn_playtype.setText(tip + data.get(parentIndex).get(itemIndex));
    changePlayType();
    dialog = new ConfirmDialog(this, R.style.dialog);
  }

  private void initData() {
    Map<Integer, String> playType = new HashMap<Integer, String>();
    for (int i = 0; i < normal_play.length; i++) {
      playType.put(i, normal_play[i]);
    }
    Set<Integer> set = playType.keySet();
    playtypeMap = new HashMap<Integer, Integer>();
    for (Integer i : set) {
      playtypeMap.put(
          ids_play[i] + Integer.valueOf(AppTools.lottery.getLotteryID()) * 100,
          i);
    }
    data.put(0, playType);
    Map<Integer, String> playType_dan = new HashMap<Integer, String>();
    for (int i = 0; i < dan_play.length; i++) {
      playType_dan.put(i, dan_play[i]);
    }
    Set<Integer> setDan = playType_dan.keySet();
    playtypeMapDan = new HashMap<Integer, Integer>();
    for (Integer i : setDan) {
      if (i <= 7) {
        playtypeMapDan.put(
            ids_play_dan[i] + Integer.valueOf(AppTools.lottery.getLotteryID())
                * 100, i);
      }
    }
    data.put(1, playType_dan);
  }

  /**
   * 绑定监听
   */
  private void setListener() {
    // 绑定Adapter
    gv_one.setAdapter(mAdapterOne);
    gv_two.setAdapter(mAdapterTwo);
    gv_three.setAdapter(mAdapterThree);
    gv_one.setOnItemClickListener(new MyItemClickListener_1());
    gv_two.setOnItemClickListener(new MyItemClickListener_2());
    gv_three.setOnItemClickListener(new MyItemClickListener_3());
  }

  class MyItemClickListener_1 implements OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
        long id) {
      TextView tv = (TextView) view.findViewById(R.id.btn_showNum);
      if (null != vibrator)
        vibrator.vibrate(150);
      String str = (position + 1) + "";
      if (position < 9) {
        str = "0" + (position + 1);
      }
      if (mAdapterOne.getOneSet().contains(str)) {
        mAdapterOne.removeOne(str);
        tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
        tv.setTextColor(Select_11X5Activity.this.getResources().getColor(
            R.color.red));
      } else {
        if (play > 12 && play < 15) {
          if (play - mAdapterOne.getOneSetSize() <= 12) {
            MyToast.getToast(Select_11X5Activity.this,
                "最多只能选" + (play - 12) + "个").show();
            return;
          }
        }
        if (play > 14) {
          if (play - mAdapterOne.getOneSetSize() <= 14) {
            MyToast.getToast(Select_11X5Activity.this,
                "最多只能选" + (play - 14) + "个").show();
            return;
          }
        }

        if (play > 12) {
          mAdapterTwo.removeOne(str);
          mAdapterTwo.notifyDataSetChanged();
          mAdapterOne.addOne(str);
          tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
          tv.setTextColor(Color.WHITE);
        } else {
          mAdapterOne.addOne(str);
          tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
          tv.setTextColor(Color.WHITE);
        }
      }
      setTotalCount();
    }
  }

  class MyItemClickListener_2 implements OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
        long id) {
      TextView tv = (TextView) view.findViewById(R.id.btn_showNum);
      if (null != vibrator)
        vibrator.vibrate(150);
      String str = (position + 1) + "";
      if (position < 9) {
        str = "0" + (position + 1);
      }
      if (mAdapterTwo.getOneSet().contains(str)) {
        mAdapterTwo.removeOne(str);
        tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
        tv.setTextColor(Select_11X5Activity.this.getResources().getColor(
            R.color.red));
      } else {
        if (play > 12) {
          mAdapterOne.removeOne(str);
          mAdapterOne.notifyDataSetChanged();
          mAdapterTwo.addOne(str);
          tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
          tv.setTextColor(Color.WHITE);
        } else {
          mAdapterTwo.addOne(str);
          tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
          tv.setTextColor(Color.WHITE);
        }
      }
      setTotalCount();
    }
  }

  class MyItemClickListener_3 implements OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
        long id) {
      TextView tv = (TextView) view.findViewById(R.id.btn_showNum);
      if (null != vibrator)
        vibrator.vibrate(150);
      String str = (position + 1) + "";
      if (position < 9) {
        str = "0" + (position + 1);
      }
      if (mAdapterThree.getOneSet().contains(str)) {
        mAdapterThree.removeOne(str);
        tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
        tv.setTextColor(Select_11X5Activity.this.getResources().getColor(
            R.color.red));
      } else {
        mAdapterThree.addOne(str);
        tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
        tv.setTextColor(Color.WHITE);
      }
      setTotalCount();
    }
  }

  /** 计算注数 */
  private void setTotalCount() {

    switch (play) {
    case 1:
      AppTools.totalCount = NumberTools.get11X5Count(
          mAdapterOne.getOneSetSize(), 1);
      break;
    case 2:
      AppTools.totalCount = NumberTools.get11X5Count(
          mAdapterOne.getOneSetSize(), 2);
      break;
    case 3:
    case 12:
      AppTools.totalCount = NumberTools.get11X5Count(
          mAdapterOne.getOneSetSize(), 3);
      break;
    case 4:
      AppTools.totalCount = NumberTools.get11X5Count(
          mAdapterOne.getOneSetSize(), 4);
      break;
    case 5:
      AppTools.totalCount = NumberTools.get11X5Count(
          mAdapterOne.getOneSetSize(), 5);
      break;
    case 6:
      AppTools.totalCount = NumberTools.get11X5Count(
          mAdapterOne.getOneSetSize(), 6);
      break;
    case 7:
      AppTools.totalCount = NumberTools.get11X5Count(
          mAdapterOne.getOneSetSize(), 7);
      break;
    case 8:
      AppTools.totalCount = NumberTools.get11X5Count(
          mAdapterOne.getOneSetSize(), 8);
      break;
    case 9:
      AppTools.totalCount = NumberTools.get11X5zuer(mAdapterOne.getOneSet(),
          mAdapterTwo.getOneSet());
      break;
    case 10:
      AppTools.totalCount = NumberTools.get11X5zusan(mAdapterOne.getOneSet(),
          mAdapterTwo.getOneSet(), mAdapterThree.getOneSet());
      break;
    case 13:
    case 15:
      if (mAdapterOne.getOneSetSize() == 1)
        AppTools.totalCount = mAdapterTwo.getOneSetSize();
      else
        AppTools.totalCount = 0;
      break;
    case 11:
      AppTools.totalCount = NumberTools.get11X5Count(
          mAdapterOne.getOneSetSize(), 2);
      break;
    // 组选前三胆拖
    case 14:
    case 16:
      AppTools.totalCount = NumberTools.get11X5Count_dan(
          mAdapterOne.getOneSetSize(), mAdapterTwo.getOneSetSize(), 3);
      break;
    case 17:
      AppTools.totalCount = NumberTools.get11X5Count_dan(
          mAdapterOne.getOneSetSize(), mAdapterTwo.getOneSetSize(), 4);
      break;
    case 18:
      AppTools.totalCount = NumberTools.get11X5Count_dan(
          mAdapterOne.getOneSetSize(), mAdapterTwo.getOneSetSize(), 5);
      break;
    case 19:
      AppTools.totalCount = NumberTools.get11X5Count_dan(
          mAdapterOne.getOneSetSize(), mAdapterTwo.getOneSetSize(), 6);
      break;
    case 20:
      AppTools.totalCount = NumberTools.get11X5Count_dan(
          mAdapterOne.getOneSetSize(), mAdapterTwo.getOneSetSize(), 7);
      break;
    }
    tv_tatol_count.setText(+AppTools.totalCount + "");
    tv_tatol_money.setText((AppTools.totalCount * 2) + "");
  }

  /**
   * 公共点击监听
   */
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
      if (null != vibrator)
        vibrator.vibrate(300);
      selectRandom();// 机选
      break;
    /** 选玩法 **/
    case R.id.layout_select_playtype:
    case R.id.btn_playtype:
    case R.id.iv_up_down:
      /**
       * {0={0=任选二, 1=任选三, 2=任选四, 3=任选五, 4=任选六, 5=任选七, 6=任选八, 7=前一, 8=前二直选,
       * 9=前二组选, 10=前三直选, 11=前三组选}, 1={0=任选二, 1=任选三, 2=任选四, 3=任选五, 4=任选六, 5=任选七,
       * 6=前二, 7=前三, 8=}}
       */
      popUtil = new PopupWindowUtil(this, data, layout_top_select);
      popUtil.setSelectIndex(parentIndex, itemIndex);
      popUtil.createPopWindow();
      popUtil.setOnSelectedListener(new PopupWindowUtil.OnSelectedListener() {
        @Override
        public void getIndex(int parentIndex, int itemIndex) {
          // TODO Auto-generated method stub
          // Log.i("itemIndex", itemIndex + "");
          // Log.i("parentIndex", parentIndex + "");
          // Log.i("Select_11X5Activity.this.itemIndex",
          // Select_11X5Activity.this.itemIndex + "");
          // Log.i("Select_11X5Activity.this.parentIndex",
          // Select_11X5Activity.this.parentIndex + "");
          if (parentIndex == 0 || (parentIndex == 1 && itemIndex <= 7)) {
            if (Select_11X5Activity.this.parentIndex != parentIndex
                || (Select_11X5Activity.this.parentIndex == parentIndex && Select_11X5Activity.this.itemIndex != itemIndex)) {
              Select_11X5Activity.this.parentIndex = parentIndex;
              Select_11X5Activity.this.itemIndex = itemIndex;
              initSelect();
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

  /**
   * 全选功能
   */
  private void selectAll() {
    if (parentIndex == 0) {
      if (itemIndex == 8 || itemIndex == 10) {
        // 不需要全选的
      } else {
        // 需要全选的
        mAdapterOne.removeAll();
        String str;
        for (int i = 0; i < numbers.length; i++) {
          str = (i + 1) + "";
          if (i < 9) {
            str = "0" + (i + 1);
          }
          if (mAdapterOne.getOneSet().contains(str)) {
            mAdapterOne.removeOne(str);
          } else {
            if (play > 12 && play < 15) {
              if (play - mAdapterOne.getOneSetSize() <= 12) {
                MyToast.getToast(Select_11X5Activity.this,
                    "最多只能选" + (play - 12) + "个").show();
                return;
              }
            }
            if (play > 14) {
              if (play - mAdapterOne.getOneSetSize() <= 14) {
                MyToast.getToast(Select_11X5Activity.this,
                    "最多只能选" + (play - 14) + "个").show();
                return;
              }
            }

            if (play > 12) {
              mAdapterTwo.removeOne(str);
              mAdapterTwo.notifyDataSetChanged();
              mAdapterOne.addOne(str);
            } else {
              mAdapterOne.addOne(str);
            }
          }
        }
        mAdapterOne.notifyDataSetChanged();
        setTotalCount();
      }
    } else if (parentIndex == 1) {
      // 需要全选的
      mAdapterOne.removeAll();
      mAdapterTwo.removeAll();
      String str;
      for (int i = 0; i < numbers.length; i++) {
        str = (i + 1) + "";
        if (i < 9) {
          str = "0" + (i + 1);
        }
        if (mAdapterTwo.getOneSet().contains(str)) {
          mAdapterTwo.removeOne(str);
        } else {
          if (play > 12) {
            mAdapterOne.removeOne(str);
            mAdapterOne.notifyDataSetChanged();
            mAdapterTwo.addOne(str);
          } else {
            mAdapterTwo.addOne(str);
          }
        }
      }
      mAdapterOne.notifyDataSetChanged();
      mAdapterTwo.notifyDataSetChanged();
      setTotalCount();
    }
  }

  /**
   * 初始化全选按钮 {0={0=任选二, 1=任选三, 2=任选四, 3=任选五, 4=任选六, 5=任选七, 6=任选八, 7=前一, 8=前二直选,
   * 9=前二组选, 10=前三直选, 11=前三组选}, 1={0=任选二, 1=任选三, 2=任选四, 3=任选五, 4=任选六, 5=任选七,
   * 6=前二, 7=前三, 8=}}
   * 
   * 任选二，任选三，任选四，任选五，任选六，任选七，任选八，前一，前二组选
   * ，前三组选，任选二胆拖，任选三胆拖，任选四胆拖，任选五胆拖，任选六胆拖，任选七胆拖， 胆拖前二，胆拖前三
   */
  private void initSelect() {
    if (parentIndex == 0) {
      if (itemIndex == 8 || itemIndex == 10) {
        select_all_rl.setVisibility(View.GONE);
      } else {
        select_all_rl.setVisibility(View.VISIBLE);
      }
    } else if (parentIndex == 1) {
      select_all_rl.setVisibility(View.VISIBLE);
    }
  }

  public void changePlayType() {
    SmanagerView
        .registerSensorManager(mSmanager, getApplicationContext(), this);// 注册传感器
    vibrator = VibratorView.getVibrator(getApplicationContext());
    String tip = "";
    if (0 == parentIndex) {// 普通
      tip = "普通-";
      SmanagerView.registerSensorManager(mSmanager, getApplicationContext(),
          this);
      vibrator = VibratorView.getVibrator(getApplicationContext());
    } else if (1 == parentIndex) {// 胆拖
      tip = "胆拖-";
      vibrator = null;
      SmanagerView.unregisterSmanager(mSmanager, this);
    }
    btn_playtype.setText(tip + data.get(parentIndex).get(itemIndex));
    switch (parentIndex) {
    case 0:// 普通投注
      switch (itemIndex) {
      case 0:// 普通投注
        if (play2 + 2 != playType)
          clear();
        playType = play2 + 2;
        showGridView();
        break;
      case 1:// 胆拖投注
        if (play2 + 3 != playType)
          clear();
        playType = play2 + 3;
        showGridView();
        break;
      case 2:// 胆拖投注
        if (play2 + 4 != playType)
          clear();
        playType = play2 + 4;
        showGridView();
        break;
      case 3:// 胆拖投注
        if (play2 + 5 != playType)
          clear();
        playType = play2 + 5;
        showGridView();
        break;
      case 4:// 胆拖投注
        if (play2 + 6 != playType)
          clear();
        playType = play2 + 6;
        showGridView();
        break;
      case 5:// 胆拖投注
        if (play2 + 7 != playType)
          clear();
        playType = play2 + 7;
        showGridView();
        break;
      case 6:// 胆拖投注
        if (play2 + 8 != playType)
          clear();
        playType = play2 + 8;
        showGridView();
        break;
      case 7:// 胆拖投注
        if (play2 + 1 != playType)
          clear();
        playType = play2 + 1;
        showGridView();
        break;
      case 8:// 胆拖投注
        if (play2 + 9 != playType)
          clear();
        playType = play2 + 9;
        showGridView();
        break;
      case 9:// 胆拖投注
        if (play2 + 11 != playType)
          clear();
        playType = play2 + 11;
        showGridView();
        break;
      case 10:// 胆拖投注
        if (play2 + 10 != playType)
          clear();
        playType = play2 + 10;
        showGridView();
        break;
      case 11:// 胆拖投注
        if (play2 + 12 != playType)
          clear();
        playType = play2 + 12;
        showGridView();
        break;
      }
      break;
    case 1:// 胆拖投注
      switch (itemIndex) {
      case 0:// 普通投注
        if (play2 + 15 != playType)
          clear();
        playType = play2 + 15;
        showGridView();
        break;
      case 1:// 胆拖投注
        if (play2 + 16 != playType)
          clear();
        playType = play2 + 16;
        showGridView();
        break;
      case 2:// 胆拖投注
        if (play2 + 17 != playType)
          clear();
        playType = play2 + 17;
        showGridView();
        break;
      case 3:// 胆拖投注
        if (play2 + 18 != playType)
          clear();
        playType = play2 + 18;
        showGridView();
        break;
      case 4:// 胆拖投注
        if (play2 + 19 != playType)
          clear();
        playType = play2 + 19;
        showGridView();
        break;
      case 5:// 胆拖投注
        if (play2 + 20 != playType)
          clear();
        playType = play2 + 20;
        showGridView();
        break;
      case 6:// 胆拖投注
        if (play2 + 13 != playType)
          clear();
        playType = play2 + 13;
        showGridView();
        break;
      case 7:// 胆拖投注
        if (play2 + 14 != playType)
          clear();
        playType = play2 + 14;
        showGridView();
        break;
      }
      break;
    }
    AppTools.totalCount = 0;
    updateAdapter();
    sv_show_ball.scrollTo(0, 0);
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

  /**
   * 从投注页面跳转过来 将投注页面的值 显示出来
   */
  public void getItem() {
    Intent intent = Select_11X5Activity.this.getIntent();
    Bundle bundle = intent.getBundleExtra("11X5Bundle");
    if (null != bundle) {
      // if (AppTools.list_numbers.size() > 0)
      playType = bundle.getInt("type");
      if (null != playtypeMap.get(playType)) {
        itemIndex = playtypeMap.get(playType);
        parentIndex = 0;
      }
      if (null != playtypeMapDan.get(playType)) {
        itemIndex = playtypeMapDan.get(playType);
        parentIndex = 1;
      }
      // Log.i(TAG, "itemIndex"+itemIndex);
      // Log.i(TAG, "playtypeMapDan"+playtypeMapDan);
      // Log.i(TAG,
      // "playtypeMapDan.get(playType)"+playtypeMapDan.get(playType));
      // Log.i(TAG, "itemIndex"+itemIndex);
      // Log.i("x", "跳过来传入的类型值===" + playType);
      changePlayType();

      ArrayList<String> list = bundle.getStringArrayList("oneSet");
      mAdapterOne.setOneSet(list);

      ArrayList<String> list2 = bundle.getStringArrayList("twoSet");
      mAdapterTwo.setOneSet(list2);

      ArrayList<String> list3 = bundle.getStringArrayList("threeSet");
      mAdapterThree.setOneSet(list3);
      mAdapterOne.notifyDataSetChanged();
      mAdapterTwo.notifyDataSetChanged();
      mAdapterThree.notifyDataSetChanged();
    }
  }

  /**
   * 玩法说明
   */
  private void playExplain() {
    Intent intent = new Intent(context, PlayDescription.class);
    startActivity(intent);
  }

  /**
   * 机选 按钮点击
   */
  public void selectRandom() {
    List<String> list = new ArrayList<String>();
    System.out.println("play == " + play);
    switch (play) {
    case 1:
    case 2:
    case 3:
    case 4:
    case 5:
    case 6:
    case 7:
    case 8:
      mAdapterOne.setOneSet(NumberTools.getRandomNum5(play, 11));
      System.out.println("mAdapterOne.getOneSet();" + mAdapterOne.getOneSet());
      break;
    case 9:
      list = NumberTools.getRandomNum5(2, 11);
      mAdapterOne.clear();
      mAdapterOne.addOne(list.get(0));
      list.remove(0);
      mAdapterTwo.setOneSet(list);
      break;
    case 10:
      list = NumberTools.getRandomNum5(3, 11);
      mAdapterOne.clear();
      mAdapterOne.addOne(list.get(0));
      mAdapterTwo.clear();
      mAdapterTwo.addOne(list.get(1));
      mAdapterThree.clear();
      mAdapterThree.addOne(list.get(2));
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
  }

  /**
   * 提交号码
   */
  private void submitNumber() {
    if (AppTools.totalCount == 0) {
      MyToast.getToast(Select_11X5Activity.this, "请至少选择一注").show();
    } else {
      Intent intent = new Intent(Select_11X5Activity.this,
          Bet_11x5Activity.class);
      Bundle bundle = new Bundle();
      bundle.putString("one", AppTools.sortSet(mAdapterOne.getOneSet())
          .toString().replace("[", "").replace("]", ""));
      bundle.putString("two", AppTools.sortSet(mAdapterTwo.getOneSet())
          .toString().replace("[", "").replace("]", ""));
      bundle.putString("three", AppTools.sortSet(mAdapterThree.getOneSet())
          .toString().replace("[", "").replace("]", ""));
      bundle.putInt("playType", playType);
      intent.putExtra("bundle", bundle);
      Select_11X5Activity.this.startActivity(intent);
    }

  }

  /**
   * 清空
   */
  private void clear() {
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

  public void unregister() {
    clear();
    vibrator = null;
    SmanagerView.unregisterSmanager(mSmanager, this);
    super.onStop();
  }

  /**
   * 注册传感器 和 振动器
   */
  @Override
  protected void onResume() {
    // TODO Auto-generated method stub
    super.onResume();
    register();
  }

  /**
   * 销毁传感器 和 振动器
   */
  @Override
  protected void onStop() {
    // TODO Auto-generated method stub
    super.onStop();
  }

  /**
   * 精确传感器 状态改变
   */
  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
    // TODO Auto-generated method stub
  }

  /**
   * 当传感器 状态改变的时候
   */
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
      if (play < 11) {
        if (null != vibrator)
          vibrator.vibrate(300);
      }
      clear();
      selectRandom();
      // //修改页面机选号码号码后面的值
      // setRandomNum(0,1);
      // setRandomNum(0,2);
    }
  }

  /**
   * 重写返回键事件
   */
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
          || mAdapterThree.getOneSetSize() != 0) {
        dialog.show();
        dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
        dialog
            .setDialogResultListener(new ConfirmDialog.DialogResultListener() {
              @Override
              public void getResult(int resultCode) {
                // TODO Auto-generated method stub
                if (1 == resultCode) {// 确定
                  clear();
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
          || mAdapterThree.getOneSetSize() != 0) {
        dialog.show();
        dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
        dialog
            .setDialogResultListener(new ConfirmDialog.DialogResultListener() {
              @Override
              public void getResult(int resultCode) {
                // TODO Auto-generated method stub
                if (1 == resultCode) {// 确定
                  Select_11X5Activity.this.startActivity(new Intent(
                      Select_11X5Activity.this, Bet_11x5Activity.class));
                  Select_11X5Activity.this.finish();
                }
              }
            });
      } else {
        Select_11X5Activity.this.startActivity(new Intent(
            Select_11X5Activity.this, Bet_11x5Activity.class));
        Select_11X5Activity.this.finish();
      }
    }
  }

  /**
   * 刷新Adapter
   */
  public void updateAdapter() {
    if (null != mAdapterOne) {
      mAdapterOne.clear();
      mAdapterOne.notifyDataSetChanged();
    }
    if (null != mAdapterTwo) {
      mAdapterTwo.clear();
      mAdapterTwo.notifyDataSetChanged();
    }
    if (null != mAdapterThree) {
      mAdapterThree.clear();
      mAdapterThree.notifyDataSetChanged();
    }
  }

  /** 显示GridView */
  private void showGridView(int one, int two, int three) {
    updateAdapter();
    rl_one.setVisibility(one);
    rl_two.setVisibility(two);
    rl_three.setVisibility(three);
  }

  /** 显示GridView */
  private void showGridView() {
    play = playType - Integer.parseInt(AppTools.lottery.getLotteryID()) * 100;/**/
    if (play >= 1 && play <= 10) {
      SmanagerView.registerSensorManager(mSmanager, getApplicationContext(),
          this);// 注册传感器
      vibrator = VibratorView.getVibrator(getApplicationContext());
      layout_shake.setVisibility(View.VISIBLE);
    }
    if (play >= 11 && play <= 20) {
      vibrator = null;
      SmanagerView.unregisterSmanager(mSmanager, this);
      layout_shake.setVisibility(View.INVISIBLE);
    }
    switch (play) {
    case 1:// 普通-前一
      tv_show1.setText("请至少选择一个号码");
      tv_show2.setVisibility(View.GONE);
      tv_show3.setVisibility(View.GONE);
      showGridView(View.VISIBLE, View.GONE, View.GONE);
      break;
    case 2:// 普通-任二
      tv_show1.setText("请至少选出二个号码");
      tv_show2.setVisibility(View.GONE);
      tv_show3.setVisibility(View.GONE);
      showGridView(View.VISIBLE, View.GONE, View.GONE);
      break;
    case 3:// 普通-任三
      tv_show1.setText("请至少选出三个号码");
      tv_show2.setVisibility(View.GONE);
      tv_show3.setVisibility(View.GONE);
      showGridView(View.VISIBLE, View.GONE, View.GONE);
      break;
    case 4:// 普通-任四
      tv_show1.setText("请至少选出四个号码");
      tv_show2.setVisibility(View.GONE);
      tv_show3.setVisibility(View.GONE);
      showGridView(View.VISIBLE, View.GONE, View.GONE);
      break;
    case 5:// 普通-任五
      tv_show1.setText("请至少选出五个号码");
      tv_show2.setVisibility(View.GONE);
      tv_show3.setVisibility(View.GONE);
      showGridView(View.VISIBLE, View.GONE, View.GONE);
      break;
    case 6:// 普通-任六
      tv_show1.setText("请至少选出六个号码");
      tv_show2.setVisibility(View.GONE);
      tv_show3.setVisibility(View.GONE);
      showGridView(View.VISIBLE, View.GONE, View.GONE);
      break;
    case 7:// 普通-任七
      tv_show1.setText("请至少选出七个号码");
      tv_show2.setVisibility(View.GONE);
      tv_show3.setVisibility(View.GONE);
      showGridView(View.VISIBLE, View.GONE, View.GONE);
      break;
    case 8:// 普通-任八
      tv_show1.setText("请至少选出八个号码");
      tv_show2.setVisibility(View.GONE);
      tv_show3.setVisibility(View.GONE);
      showGridView(View.VISIBLE, View.GONE, View.GONE);
      break;
    case 9:// 普通-直选二
      tv_show1.setText("每位至少选择一个号码");
      tv_show2.setVisibility(View.GONE);
      tv_show3.setVisibility(View.GONE);
      showGridView(View.VISIBLE, View.VISIBLE, View.GONE);
      break;
    case 10:// 普通-直选三
      tv_show1.setText("每位至少选择一个号码");
      tv_show2.setVisibility(View.GONE);
      tv_show3.setVisibility(View.GONE);
      showGridView(View.VISIBLE, View.VISIBLE, View.VISIBLE);
      break;
    case 11:// 普通-组选二
      layout_shake.setVisibility(View.INVISIBLE);
      tv_show1.setText("请至少选择二个号码");
      tv_show2.setVisibility(View.GONE);
      tv_show3.setVisibility(View.GONE);
      showGridView(View.VISIBLE, View.GONE, View.GONE);
      break;
    case 12:// 普通-组选三
      layout_shake.setVisibility(View.INVISIBLE);
      tv_show1.setText("请至少选择三个号码");
      tv_show2.setVisibility(View.GONE);
      tv_show3.setVisibility(View.GONE);
      showGridView(View.VISIBLE, View.GONE, View.GONE);
      break;
    case 13:// 胆拖-前二组选
      tv_show1.setText("胆码,请至少选择一个号码");
      tv_show2.setVisibility(View.VISIBLE);
      showGridView(View.VISIBLE, View.VISIBLE, View.GONE);
      break;
    case 14:// 胆拖-前三组选
      tv_show1.setText("胆码,请至少选择一个号码");
      tv_show2.setVisibility(View.VISIBLE);
      showGridView(View.VISIBLE, View.VISIBLE, View.GONE);
      break;
    case 15:// 胆拖-任二
      tv_show1.setText("胆码,请至少选择一个号码");
      tv_show2.setVisibility(View.VISIBLE);
      showGridView(View.VISIBLE, View.VISIBLE, View.GONE);
      break;
    case 16:// 胆拖-任三
      tv_show1.setText("胆码,请至少选择一个号码");
      tv_show2.setVisibility(View.VISIBLE);
      showGridView(View.VISIBLE, View.VISIBLE, View.GONE);
      break;
    case 17:// 胆拖-任四
      tv_show1.setText("胆码,请至少选择一个号码");
      tv_show2.setVisibility(View.VISIBLE);
      showGridView(View.VISIBLE, View.VISIBLE, View.GONE);
      break;
    case 18:// 胆拖-任五
      tv_show1.setText("胆码,请至少选择一个号码");
      tv_show2.setVisibility(View.VISIBLE);
      showGridView(View.VISIBLE, View.VISIBLE, View.GONE);
      break;
    case 19:// 胆拖-任六
      tv_show1.setText("胆码,请至少选择一个号码");
      tv_show2.setVisibility(View.VISIBLE);
      showGridView(View.VISIBLE, View.VISIBLE, View.GONE);
      break;
    case 20:// 胆拖-任七
      tv_show1.setText("胆码,请至少选择一个号码");
      tv_show2.setVisibility(View.VISIBLE);
      showGridView(View.VISIBLE, View.VISIBLE, View.GONE);
      break;
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
