package com.sm.sls_app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.ui.Select_YNSSCActivity.MyAsynTask1;
import com.sm.sls_app.ui.adapter.GridView11X5Adapter;
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
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author 作者 : hxj
 * @version 创建时间：2016-4-5 上午8:55:58 类说明 北京pk10选号界面
 */
public class Select_BJPK10Activity extends Activity implements OnClickListener {
  private final static String TAG = "Select_BJPK10Activity";
  private TextView tv_show1, tv_show2, tv_show3, tv_show4, tv_show5, tv_tip;//
  private RelativeLayout rl_one, rl_two, rl_three, rl_four, rl_five;
  private String[] numbers = new String[] { "01", "02", "03", "04", "05", "06",
      "07", "08", "09", "10" };
  private MyGridView gv_one, gv_two, gv_three, gv_four, gv_five;
  private GridView11X5Adapter mAdapterOne, mAdapterTwo, mAdapterThree,
      mAdapterFour, mAdapterFive;
  private Bundle bundle;
  public Vibrator vibrator; // 震动器
  private SensorManager mSmanager; // 传感器
  private Animation animation = null;
  private int playType = 9401;
  private int itemIndex = 0;
  private int parentIndex = 0;
  private Map<Integer, Map<Integer, String>> data = new HashMap<Integer, Map<Integer, String>>();
  private Map<Integer, Integer> playtypeMap = new HashMap<Integer, Integer>();
  /** 传感器 */
  float bx = 0;
  float by = 0;
  float bz = 0;
  long btime = 0;// 这一次的时间
  private long vTime = 0; // 震动的时间
  private SharedPreferences settings;
  private Editor editor;
  private PopupWindowUtil popUtil;
  /* 头部 */
  private RelativeLayout layout_top_select;// 顶部布局
  private ImageButton btn_back; // 返回
  private LinearLayout layout_select_playtype;// 玩法选择
  private ImageView iv_up_down;// 玩法提示图标
  private Button btn_playtype;// 玩法
  private ImageButton btn_help;// 帮助
  private ConfirmDialog dialog;// 提示框
  /* 中间部分 */
  private TextView tv_lotteryname, tv_lotteryqi;// 彩种名,最后开奖期号
  private CustomDigitalClock2 select_time;// 本期倒计时
  /* 全选部分 */
  // private RelativeLayout select_all_rl;
  private Button btn_select_all;// 全选按钮
  /* 尾部 */
  private Button btn_clearall; // 清除全部
  private Button btn_submit; // 选好了
  public TextView tv_tatol_count;// 总注数
  public TextView tv_tatol_money;// 总钱数

  private MyHandler mHandler;
  private String auth, info, time, imei, crc; // 格式化后的参数
  private String opt = "13"; // 格式化后的 opt
  // 中奖的红色蓝色号码
  private TextView tv_selected_redball;
  private String selected_redball;// 中奖的红球号码 tv_selected_redball
  private String selected_blueball; // 中奖的蓝球号码 tv_selected_blueball
  private MyAsyncTask mAsyncTask;

  // private LinearLayout layout_shake;// 摇一摇
  // private ImageView iv_shake;// 摇一摇
  // private TextView tv_shake;// 摇一摇

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_select_pk10);
    App.activityS.add(this);
    App.activityS1.add(this);
    clear();
    findView();
    init();
    if (NetWork.isConnect(Select_BJPK10Activity.this) == true) {
      if (null != AppTools.lottery) {
        mAsyncTask = new MyAsyncTask();
        mAsyncTask.execute();
      } else {
        Toast.makeText(Select_BJPK10Activity.this, "网络连接异常，获得数据失败！",
            Toast.LENGTH_SHORT).show();
      }
    }
  }

  class MyAsyncTask extends android.os.AsyncTask<Void, Integer, String> {

    @Override
    protected String doInBackground(Void... params) {
      time = RspBodyBaseBean.getTime();
      imei = RspBodyBaseBean.getIMEI(Select_BJPK10Activity.this);
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
      mHandler.sendEmptyMessage(Integer.parseInt(result));
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
        MyToast.getToast(getApplicationContext(), "获取中奖信息失败！").show();
        break;
      default:
        break;
      }
    }

  }

  /**
   * 中奖信息
   */
  private void initNum() {
    if (NetWork.isConnect(Select_BJPK10Activity.this) == true) {
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
          if (NetWork.isConnect(Select_BJPK10Activity.this) == true) {
            if (AppTools.lottery != null) {
              if (mAsyncTask != null) {
                mAsyncTask = null;
              }
              mAsyncTask = new MyAsyncTask();
              mAsyncTask.execute();
            }
          } else {
            Toast.makeText(Select_BJPK10Activity.this, "网络连接异常，获得数据失败！",
                Toast.LENGTH_SHORT).show();
          }
        }

        @Override
        public void remainFiveMinutes() {

        }
      });
    } else {
      Toast.makeText(Select_BJPK10Activity.this, "网络连接异常，获得数据失败！",
          Toast.LENGTH_SHORT).show();
    }
  }

  /**
   * 清空
   */
  private void clear() {
    if (null == mAdapterOne || null == mAdapterTwo || null == mAdapterThree
        || null == mAdapterFour || null == mAdapterFive) {
      return;
    }
    mAdapterOne.clear();
    mAdapterTwo.clear();
    mAdapterThree.clear();
    mAdapterFour.clear();
    mAdapterFive.clear();
    mAdapterOne.notifyDataSetChanged();
    mAdapterTwo.notifyDataSetChanged();
    mAdapterThree.notifyDataSetChanged();
    mAdapterFour.notifyDataSetChanged();
    mAdapterFive.notifyDataSetChanged();
    AppTools.totalCount = 0;
    tv_tatol_count.setText(+AppTools.totalCount + "");
    tv_tatol_money.setText((AppTools.totalCount * 2) + "");
  }

  /**
   * 初始化
   */
  private void findView() {
    AppTools.isCanBet = true;// 好像没什么限制，不用管
    bundle = new Bundle();
    btn_select_all = (Button) findViewById(R.id.btn_select_all);
    // select_all_rl = (RelativeLayout) findViewById(R.id.select_all_rl);
    btn_playtype = (Button) findViewById(R.id.btn_playtype);
    btn_help = (ImageButton) findViewById(R.id.btn_help);
    iv_up_down = (ImageView) findViewById(R.id.iv_up_down);
    layout_select_playtype = (LinearLayout) findViewById(R.id.layout_select_playtype);
    btn_back = (ImageButton) findViewById(R.id.btn_back);
    tv_show1 = (TextView) this.findViewById(R.id.tv_show);
    tv_show2 = (TextView) this.findViewById(R.id.tv_show2);
    tv_show3 = (TextView) this.findViewById(R.id.tv_show3);
    tv_show4 = (TextView) this.findViewById(R.id.tv_show4);
    tv_show5 = (TextView) this.findViewById(R.id.tv_show5);
    tv_tip = (TextView) this.findViewById(R.id.tv_tip);
    rl_one = (RelativeLayout) this.findViewById(R.id.number_sv_center_rlOne);
    rl_two = (RelativeLayout) this.findViewById(R.id.number_sv_center_rlTwo);
    rl_three = (RelativeLayout) this
        .findViewById(R.id.number_sv_center_rlThree);
    rl_four = (RelativeLayout) this.findViewById(R.id.number_sv_center_rlFour);
    rl_five = (RelativeLayout) this.findViewById(R.id.number_sv_center_rlFive);
    gv_one = (MyGridView) findViewById(R.id.number_sv_center_gv_showOne);
    gv_two = (MyGridView) findViewById(R.id.number_sv_center_gv_showTwo);
    gv_three = (MyGridView) findViewById(R.id.number_sv_center_gv_showThree);
    gv_four = (MyGridView) findViewById(R.id.number_sv_center_gv_showFour);
    gv_five = (MyGridView) findViewById(R.id.number_sv_center_gv_showFive);
    tv_selected_redball = (TextView) findViewById(R.id.tv_selected_redball);
    mAdapterOne = new GridView11X5Adapter(Select_BJPK10Activity.this, numbers,
        true);
    mAdapterTwo = new GridView11X5Adapter(Select_BJPK10Activity.this, numbers,
        true);
    mAdapterThree = new GridView11X5Adapter(Select_BJPK10Activity.this,
        numbers, true);
    mAdapterFour = new GridView11X5Adapter(Select_BJPK10Activity.this, numbers,
        true);
    mAdapterFive = new GridView11X5Adapter(Select_BJPK10Activity.this, numbers,
        true);
    gv_one.setAdapter(mAdapterOne);
    gv_two.setAdapter(mAdapterTwo);
    gv_three.setAdapter(mAdapterThree);
    gv_four.setAdapter(mAdapterFour);
    gv_five.setAdapter(mAdapterFive);
    gv_one.setOnItemClickListener(new MyItemClickListener_1());
    gv_two.setOnItemClickListener(new MyItemClickListener_2());
    gv_three.setOnItemClickListener(new MyItemClickListener_3());
    gv_four.setOnItemClickListener(new MyItemClickListener_4());
    gv_five.setOnItemClickListener(new MyItemClickListener_5());
    tv_lotteryname = (TextView) findViewById(R.id.tv_lotteryname);
    tv_lotteryqi = (TextView) findViewById(R.id.tv_lotteryqi);
    select_time = (CustomDigitalClock2) findViewById(R.id.tv_selected_time);
    // layout_shake = (LinearLayout) findViewById(R.id.layout_shake);
    // iv_shake = (ImageView) findViewById(R.id.iv_shake);
    // tv_shake = (TextView) findViewById(R.id.tv_shake);
    tv_tatol_count = (TextView) this.findViewById(R.id.tv_tatol_count);
    tv_tatol_money = (TextView) this.findViewById(R.id.tv_tatol_money);
    mSmanager = (SensorManager) getSystemService(SENSOR_SERVICE);
    layout_top_select = (RelativeLayout) findViewById(R.id.layout_top_select);
    btn_clearall = (Button) findViewById(R.id.btn_clearall);
    btn_submit = (Button) findViewById(R.id.btn_submit);
    mHandler = new MyHandler();
    // 跳出玩法按钮
    // 胆拖
    mSmanager = (SensorManager) getSystemService(SENSOR_SERVICE);
  }

  class MyItemClickListener_1 implements OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
        long id) {
      if (null != vibrator)
        vibrator.vibrate(100);
      TextView tv = (TextView) view.findViewById(R.id.btn_showNum);
      String str = (position + 1) + "";
      if (position < 9) {
        str = "0" + (position + 1);
      }
      if (mAdapterOne.getOneSet().contains(str)) {
        mAdapterOne.removeOne(str);
        tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
        tv.setTextColor(Select_BJPK10Activity.this.getResources().getColor(
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
      String str = (position + 1) + "";
      if (position < 9) {
        str = "0" + (position + 1);
      }
      if (mAdapterTwo.getOneSet().contains(str)) {
        mAdapterTwo.removeOne(str);
        tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
        tv.setTextColor(Select_BJPK10Activity.this.getResources().getColor(
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
      String str = (position + 1) + "";
      if (position < 9) {
        str = "0" + (position + 1);
      }
      if (mAdapterThree.getOneSet().contains(str)) {
        mAdapterThree.removeOne(str);
        tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
        tv.setTextColor(Select_BJPK10Activity.this.getResources().getColor(
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
      String str = (position + 1) + "";
      if (position < 9) {
        str = "0" + (position + 1);
      }
      if (mAdapterFour.getOneSet().contains(str)) {
        mAdapterFour.removeOne(str);
        tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
        tv.setTextColor(Select_BJPK10Activity.this.getResources().getColor(
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
      String str = (position + 1) + "";
      if (position < 9) {
        str = "0" + (position + 1);
      }
      if (mAdapterFive.getOneSet().contains(str)) {
        mAdapterFive.removeOne(str);
        tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
        tv.setTextColor(Select_BJPK10Activity.this.getResources().getColor(
            R.color.red));
      } else {
        mAdapterFive.addOne(str);
        tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
        tv.setTextColor(Color.WHITE);
      }
      setTotalCount();
    }
  }

  /**
   * 数据初始化
   */
  private void init() {
    // SmanagerView.registerSensorManager(mSmanager,
    // getApplicationContext(),
    // this);
    btn_back.setOnClickListener(this);
    layout_select_playtype.setOnClickListener(this);
    iv_up_down.setOnClickListener(this);
    btn_playtype.setOnClickListener(this);
    btn_help.setOnClickListener(this);
    btn_clearall.setOnClickListener(this);
    btn_submit.setOnClickListener(this);
    btn_select_all.setOnClickListener(this);
    // layout_shake.setOnClickListener(this);
    // iv_shake.setOnClickListener(this);
    // tv_shake.setOnClickListener(this);
    settings = getSharedPreferences("app_user", 0);// 获取SharedPreference对象
    editor = settings.edit();// 获取编辑对象
    tv_lotteryname.setText("北京PK10");
    Map<Integer, String> playType = new HashMap<Integer, String>();
    playType.put(0, "猜冠军");
    playType.put(1, "猜冠亚军");
    playType.put(2, "猜前三名");
    playType.put(3, "猜前四名");
    playType.put(4, "猜前五名");
    Set<Integer> set = playType.keySet();
    int[] playtype_array = { 9401, 9402, 9403, 9404, 9405 };
    for (Integer i : set) {
      playtypeMap.put(playtype_array[i], i);
    }
    data.put(0, playType);
    dialog = new ConfirmDialog(this, R.style.dialog);
    showGridView();
  }

  /** 显示GridView */
  private void showGridView() {
    vibrator = VibratorView.getVibrator(Select_BJPK10Activity.this);
    switch (itemIndex) {
    case 0:
      showGridView(View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE);
      btn_playtype.setText("猜冠军");
      playType = 9401;
      tv_show1.setText("冠军");
      tv_tip.setText("请至少选择一个号码");
      break;
    case 1:
      showGridView(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.GONE);
      tv_show1.setText("冠军");
      tv_show2.setText("亚军");
      btn_playtype.setText("猜冠亚军");
      tv_tip.setText("每位至少选择一个号码");
      playType = 9402;
      break;
    case 2:
      showGridView(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE,
          View.GONE);
      tv_show1.setText("前一");
      tv_show2.setText("前二");
      tv_show3.setText("前三");
      btn_playtype.setText("猜前三名");
      tv_tip.setText("每位至少选择一个号码");
      playType = 9403;
      break;
    case 3:
      showGridView(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
          View.GONE);
      tv_show1.setText("前一");
      tv_show2.setText("前二");
      tv_show3.setText("前三");
      tv_show4.setText("前四");
      btn_playtype.setText("猜前四名");
      tv_tip.setText("每位至少选择一个号码");
      playType = 9404;
      break;
    case 4:
      showGridView(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
          View.VISIBLE);
      tv_show1.setText("前一");
      tv_show2.setText("前二");
      tv_show3.setText("前三");
      tv_show4.setText("前四");
      tv_show5.setText("前五");
      btn_playtype.setText("猜前五名");
      tv_tip.setText("每位至少选择一个号码");
      playType = 9405;
      break;
    default:
      break;
    }
  }

  /** 显示GridView */
  private void showGridView(int one, int two, int three, int four, int five) {
    rl_one.setVisibility(one);
    rl_two.setVisibility(two);
    rl_three.setVisibility(three);
    rl_four.setVisibility(four);
    rl_five.setVisibility(five);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.btn_select_all:
      selectAll();
      break;
    case R.id.btn_back:
      exit();
      break;
    /** 清空 **/
    case R.id.btn_clearall:
      clear();
      break;
    /** 玩法说明 **/
    case R.id.btn_help:
      playExplain();
      break;
    case R.id.btn_submit:
      submitNumber();
      break;
    /** 选玩法 **/
    case R.id.layout_select_playtype:
    case R.id.btn_playtype:
    case R.id.iv_up_down:
      /**
       * 9401 猜冠军9402 猜冠亚军 9403 猜前三名 9404 猜前四名 9405 猜前五名
       */
      popUtil = new PopupWindowUtil(this, data, layout_top_select);
      popUtil.setSelectIndex(parentIndex, itemIndex);
      popUtil.createPopWindow();
      popUtil.setOnSelectedListener(new PopupWindowUtil.OnSelectedListener() {
        @Override
        public void getIndex(int parentIndex, int itemIndex) {
          if (itemIndex != Select_BJPK10Activity.this.itemIndex
              || parentIndex != Select_BJPK10Activity.this.parentIndex) {
            Select_BJPK10Activity.this.parentIndex = parentIndex;
            Select_BJPK10Activity.this.itemIndex = itemIndex;
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
    // /** 机选 **/
    // case R.id.layout_shake:
    // case R.id.iv_shake:
    // case R.id.tv_shake:
    // case R.id.layout_shake2:
    // case R.id.iv_shake2:
    // case R.id.tv_shake2:
    // if (null != vibrator)
    // vibrator.vibrate(300);
    // selectRandom();// 机选
    // break;
    default:
      break;
    }
  }

  /**
   * 设置全选按钮
   */
  private void initSelectAll() {
    // select_all_rl.setVisibility(View.VISIBLE);
  }

  private void selectAll() {
    if (null != vibrator)
      vibrator.vibrate(100);
    mAdapterOne.addAll();
    mAdapterTwo.addAll();
    mAdapterThree.addAll();
    mAdapterFour.addAll();
    mAdapterFive.addAll();
    setTotalCount();
  }

  /** 提交号码 */
  private void submitNumber() {
    if (AppTools.totalCount == 0) {
      MyToast.getToast(Select_BJPK10Activity.this, "请至少选择一注").show();
    } else {
      Intent intent = new Intent(Select_BJPK10Activity.this,
          Bet_BJPK10Activity.class);
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
      bundle.putInt("playType", playType);
      intent.putExtra("bundle", bundle);
      startActivity(intent);
    }
  }

  /**
   * 玩法说明
   */
  private void playExplain() {
    Intent intent = new Intent(Select_BJPK10Activity.this,
        PlayDescription.class);
    startActivity(intent);
  }

  /**
	 * 
	 */
  public void changePlayType() {
    btn_playtype.setText(data.get(parentIndex).get(itemIndex));
    // layout_shake.setVisibility(View.INVISIBLE);// 初始化摇一摇 默认不显示
    // 根据需要在下方设置是否显示
    switch (itemIndex) {
    case 0:// 冠军
      if (itemIndex != 0)
        clear();
      itemIndex = 0;
      showGridView();
      // layout_shake.setVisibility(View.VISIBLE);
      // SmanagerView.registerSensorManager(mSmanager,
      // getApplicationContext(), this);
      vibrator = VibratorView.getVibrator(getApplicationContext());
      break;
    case 1:// 冠亚军
      if (itemIndex != 1)
        clear();
      itemIndex = 1;
      showGridView();
      // layout_shake.setVisibility(View.VISIBLE);
      // SmanagerView.registerSensorManager(mSmanager,
      // getApplicationContext(), this);
      vibrator = VibratorView.getVibrator(getApplicationContext());
      break;
    case 2:// 前三
      if (itemIndex != 2)
        clear();
      itemIndex = 2;
      showGridView();
      // layout_shake.setVisibility(View.VISIBLE);
      // SmanagerView.registerSensorManager(mSmanager,
      // getApplicationContext(), this);
      vibrator = VibratorView.getVibrator(getApplicationContext());
      break;
    case 3:// 前四
      if (itemIndex != 3)
        clear();
      itemIndex = 3;
      showGridView();
      // layout_shake.setVisibility(View.VISIBLE);
      // SmanagerView.registerSensorManager(mSmanager,
      // getApplicationContext(), this);
      vibrator = VibratorView.getVibrator(getApplicationContext());
      break;
    case 4:// 前五
      if (itemIndex != 4)
        clear();
      itemIndex = 4;
      showGridView();
      // layout_shake.setVisibility(View.VISIBLE);
      // SmanagerView.registerSensorManager(mSmanager,
      // getApplicationContext(), this);
      vibrator = VibratorView.getVibrator(getApplicationContext());
      break;
    default:
      break;
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

  /**
   * 退出
   */
  private void exit() {
    if (AppTools.list_numbers == null
        || (AppTools.list_numbers != null && AppTools.list_numbers.size() == 0)) {
      if (mAdapterOne.getOneSetSize() != 0 || mAdapterTwo.getOneSetSize() != 0
          || mAdapterThree.getOneSetSize() != 0
          || mAdapterFour.getOneSetSize() != 0
          || mAdapterFive.getOneSetSize() != 0) {
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
          || mAdapterFive.getOneSetSize() != 0) {
        dialog.show();
        dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
        dialog
            .setDialogResultListener(new ConfirmDialog.DialogResultListener() {
              @Override
              public void getResult(int resultCode) {
                if (1 == resultCode) {// 确定
                  clear();
                  Intent intent = new Intent(Select_BJPK10Activity.this,
                      Bet_BJPK10Activity.class);
                  Bundle bundle = new Bundle();
                  bundle.putInt("playType", playType);
                  bundle.putInt("btnIndex", itemIndex + 1);
                  intent.putExtra("bundle1", bundle);
                  Select_BJPK10Activity.this.startActivity(intent);
                  Select_BJPK10Activity.this.finish();
                }
              }
            });
      } else {
        clear();
        Intent intent = new Intent(Select_BJPK10Activity.this,
            Bet_BJPK10Activity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("playType", playType);
        bundle.putInt("btnIndex", itemIndex + 1);
        intent.putExtra("bundle1", bundle);
        Select_BJPK10Activity.this.startActivity(intent);
        Select_BJPK10Activity.this.finish();
      }
    }

  }

  @Override
  protected void onDestroy() {
    App.activityS.remove(this);
    super.onDestroy();
  }

  /** 从投注页面跳转过来 将投注页面的值 显示出来 */
  public void getItem() {
    Intent intent = Select_BJPK10Activity.this.getIntent();
    if (intent != null) {
      bundle = intent.getBundleExtra("PK10Bundle");
    }
    if (null != bundle) {
      playType = bundle.getInt("playtype");

      if (null != playtypeMap.get(playType)) {
        itemIndex = playtypeMap.get(playType);
      }
      changePlayType();
      ArrayList<String> list = bundle.getStringArrayList("oneSet");
      mAdapterOne.setOneSet(list);
      mAdapterOne.notifyDataSetChanged();
      if (playType >= 9402) {
        ArrayList<String> list2 = bundle.getStringArrayList("twoSet");
        mAdapterTwo.setOneSet(list2);
        mAdapterTwo.notifyDataSetChanged();
      }
      if (playType >= 9403) {
        ArrayList<String> list3 = bundle.getStringArrayList("threeSet");
        mAdapterThree.setOneSet(list3);
        mAdapterThree.notifyDataSetChanged();
      }
      if (playType >= 9404) {
        ArrayList<String> list4 = bundle.getStringArrayList("fourSet");
        mAdapterFour.setOneSet(list4);
        mAdapterFour.notifyDataSetChanged();
      }
      if (playType >= 9405) {
        ArrayList<String> list5 = bundle.getStringArrayList("fiveSet");
        mAdapterFive.setOneSet(list5);
        mAdapterFive.notifyDataSetChanged();
      }
    }
  }

  public void register() {
    getItem();
    tv_tatol_count.setText(+AppTools.totalCount + "");
    tv_tatol_money.setText((AppTools.totalCount * 2) + "");
    // SmanagerView.registerSensorManager(mSmanager,
    // getApplicationContext(),
    // this);// 注册传感器
    vibrator = VibratorView.getVibrator(getApplicationContext());
  }

  public void unregister() {
    clear();
    vibrator = null;
    // SmanagerView.unregisterSmanager(mSmanager, this);
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

  // @Override
  // public void onSensorChanged(SensorEvent event) {
  // // 现在检测时间
  // long currentUpdateTime = System.currentTimeMillis();
  // if (vTime == 0) {
  // vTime = currentUpdateTime;
  // Log.i("x", "执行了vTime---===");
  // }
  // // 两次检测的时间间隔
  // long timeInterval = currentUpdateTime - btime;
  // // 判断是否达到了检测时间间隔
  // if (timeInterval < 150)
  // return;
  // // 现在的时间变成last时间
  // btime = currentUpdateTime;
  // // 获得x,y,z坐标
  // float x = event.values[0];
  // float y = event.values[1];
  // float z = event.values[2];
  // // 获得x,y,z的变化值
  // float deltaX = x - bx;
  // float deltaY = y - by;
  // float deltaZ = z - bz;
  // // 将现在的坐标变成last坐标
  // bx = x;
  // by = y;
  // bz = z;
  //
  // double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
  // * deltaZ)
  // / timeInterval * 10000;
  //
  // // Log.i("x", "间隔==  "+(vTime - currentUpdateTime));
  // // 达到速度阀值，发出提示
  // if (speed >= 500 && currentUpdateTime - vTime > 700) {
  // vTime = System.currentTimeMillis();
  // selectRandom();
  // }
  //
  // }

  // @Override
  // public void onAccuracyChanged(Sensor sensor, int accuracy) {
  //
  // }

  // public void selectRandom() {
  // switch (itemIndex) {
  // case 0:
  // mAdapterOne.setOneSet(NumberTools.getRandomNum5(1, 10));
  // vibrator();
  // break;
  // case 1:
  // mAdapterOne.setOneSet(NumberTools.getRandomNum5(1, 10));
  // mAdapterTwo.setOneSet(NumberTools.getRandomNum5(1, 10));
  // vibrator();
  // break;
  // case 2:
  // mAdapterOne.setOneSet(NumberTools.getRandomNum5(1, 10));
  // mAdapterTwo.setOneSet(NumberTools.getRandomNum5(1, 10));
  // mAdapterThree.setOneSet(NumberTools.getRandomNum5(1, 10));
  // vibrator();
  // break;
  // case 3:
  // mAdapterOne.setOneSet(NumberTools.getRandomNum5(1, 10));
  // mAdapterTwo.setOneSet(NumberTools.getRandomNum5(1, 10));
  // mAdapterThree.setOneSet(NumberTools.getRandomNum5(1, 10));
  // mAdapterFour.setOneSet(NumberTools.getRandomNum5(1, 10));
  // vibrator();
  // break;
  // case 4:
  // mAdapterOne.setOneSet(NumberTools.getRandomNum5(1, 10));
  // mAdapterTwo.setOneSet(NumberTools.getRandomNum5(1, 10));
  // mAdapterThree.setOneSet(NumberTools.getRandomNum5(1, 10));
  // mAdapterFour.setOneSet(NumberTools.getRandomNum5(1, 10));
  // mAdapterFive.setOneSet(NumberTools.getRandomNum5(1, 10));
  // vibrator();
  // break;
  // default:
  // break;
  // }
  // update();
  // setTotalCount();
  // }

  private void update() {
    if (null == mAdapterOne || null == mAdapterTwo || null == mAdapterThree
        || null == mAdapterFour || null == mAdapterFive)
      return;
    mAdapterOne.notifyDataSetChanged();
    mAdapterTwo.notifyDataSetChanged();
    mAdapterThree.notifyDataSetChanged();
    mAdapterFour.notifyDataSetChanged();
    mAdapterFive.notifyDataSetChanged();
    Log.i("x", "刷新了update");
  }

  /** 计算注数 */
  private void setTotalCount() {
    AppTools.totalCount = NumberTools.getBJPK10Count(mAdapterOne.getOneSet(),
        mAdapterTwo.getOneSet(), mAdapterThree.getOneSet(),
        mAdapterFour.getOneSet(), mAdapterFive.getOneSet(), itemIndex);
    tv_tatol_count.setText(+AppTools.totalCount + "");
    tv_tatol_money.setText((AppTools.totalCount * 2) + "");
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

  /* 提供震动 */
  public void vibrator() {
    if (null != vibrator)
      vibrator.vibrate(300);
  }
}
