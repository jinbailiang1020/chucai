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
import com.sm.sls_app.ui.Select_k3Activity.MyAsynTask1;
import com.sm.sls_app.ui.adapter.MyGridViewAdapter;
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

/**
 * 江苏快3 的选球页面 功能： 选号界面，实现选号
 * 
 * @author Kinwee 修改日期2014-12-30
 * 
 */
public class Select_k3Activity extends Activity implements OnClickListener,
    SensorEventListener {
  private final static String TAG = "Select_k3Activity";

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
  private Button btn_select_all;
  /* 中间部分 */
  private TextView tv_lotteryname, tv_lotteryqi;// 彩种名,最后开奖期号
  private CustomDigitalClock2 select_time;// 本期倒计时
  // 中奖的红色蓝色号码
  private TextView tv_selected_redball, tv_selected_blueball;
  private LinearLayout layout_shake;// 摇一摇
  private ImageView iv_shake;// 摇一摇
  private TextView tv_shake;// 摇一摇
  private LinearLayout layout_shake2;// 摇一摇
  private ImageView iv_shake2;// 摇一摇
  private TextView tv_shake2;// 摇一摇

  private String selected_redball;// 中奖的红球号码 tv_selected_redball
  private String selected_blueball; // 中奖的蓝球号码 tv_selected_blueball

  private TextView tv_top; // 选号上面的提示
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
  private int itemIndex = 0;

  private Map<Integer, Integer> playtypeMap = new HashMap<Integer, Integer>();
  private List<String> list1 = new ArrayList<String>();
  private List<String> list2 = new ArrayList<String>();
  private int playID = 8301;

  private Adpater adpater, adpater1, adpater2;
  private GridView gridView, gridView_ertonghaodan_1, gridView_ertonghaodan_2;
  private TextView tv_title, tv_dan, tv_tuo;// 界面两个gridView 胆拖和二同单选
  // 1 为和值 2 为二同和胆拖 3 为快速选号
  private RelativeLayout relativeLayout1, relativeLayout2;
  private int index = 1; // 倒计时标签

  private MyAsynTask myAsynTask;
  private MyHandler myHandler;

  private Spanned tip = null;
  private String auth, info, time, imei, crc; // 格式化后的参数
  private String opt = "13"; // 格式化后的 opt
  private MyAsynTask1 mAsynTask1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    // 设置无标题
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_select_k3);
    clearHashSet();
    App.activityS.add(this);
    App.activityS1.add(this);
    findView();
    init();
    if (NetWork.isConnect(Select_k3Activity.this) == true) {
      if (AppTools.lottery != null) {
        mAsynTask1 = new MyAsynTask1();
        mAsynTask1.execute();
      }
    } else {
      Toast.makeText(Select_k3Activity.this, "网络连接异常，获得数据失败！",
          Toast.LENGTH_SHORT).show();
    }
  }

  /*** 异步任务 用来后台获取数据 */
  class MyAsynTask1 extends AsyncTask<Void, Integer, String> {
    /** 在后台执行的程序 */
    @Override
    protected String doInBackground(Void... params) {
      time = RspBodyBaseBean.getTime();
      imei = RspBodyBaseBean.getIMEI(Select_k3Activity.this);
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
    MyGridViewAdapter.playType = 501;
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
    layout_shake2 = (LinearLayout) findViewById(R.id.layout_shake2);
    iv_shake2 = (ImageView) findViewById(R.id.iv_shake2);
    tv_shake2 = (TextView) findViewById(R.id.tv_shake2);
    tv_selected_redball = (TextView) findViewById(R.id.tv_selected_redball);
    tv_selected_blueball = (TextView) findViewById(R.id.tv_selected_blueball);
    tv_tatol_count = (TextView) this.findViewById(R.id.tv_tatol_count);
    tv_tatol_money = (TextView) this.findViewById(R.id.tv_tatol_money);
    mSmanager = (SensorManager) getSystemService(SENSOR_SERVICE);
    layout_top_select = (RelativeLayout) findViewById(R.id.layout_top_select);
    tv_top = (TextView) this.findViewById(R.id.textViewtop);

    myHandler = new MyHandler();
    // 跳出玩法按钮
    // 胆拖
    tv_title = (TextView) this.findViewById(R.id.textView2);
    tv_dan = (TextView) this.findViewById(R.id.textView5);
    tv_tuo = (TextView) this.findViewById(R.id.textView6);
    // 注数
    relativeLayout1 = (RelativeLayout) this
        .findViewById(R.id.relativeLayout_hezhi);
    relativeLayout2 = (RelativeLayout) this
        .findViewById(R.id.relativeLayout_ertonghao);
    // 二同号单选和胆拖玩法
    gridView = (GridView) this.findViewById(R.id.gridView_hezhi);
    gridView.setNumColumns(4);
    adpater = new Adpater(list1, list2, Select_k3Activity.this);
    gridView.setAdapter(adpater);
    // 默认二同单选号
    adpater1 = new Adpater(list1, new ArrayList<String>(),
        Select_k3Activity.this);
    adpater2 = new Adpater(list2, new ArrayList<String>(),
        Select_k3Activity.this);
    gridView_ertonghaodan_1 = (GridView) this
        .findViewById(R.id.gridView_ertonghaodan_1);
    gridView_ertonghaodan_1.setAdapter(adpater1);
    gridView_ertonghaodan_2 = (GridView) this
        .findViewById(R.id.gridView_ertonghaodan_2);
    gridView_ertonghaodan_2.setAdapter(adpater2);
    mSmanager = (SensorManager) getSystemService(SENSOR_SERVICE);

  }

  private void initNum() {
    if (NetWork.isConnect(Select_k3Activity.this) == true) {
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
          if (NetWork.isConnect(Select_k3Activity.this) == true) {
            if (AppTools.lottery != null) {
              if (mAsynTask1 != null) {
                mAsynTask1 = null;
              }
              mAsynTask1 = new MyAsynTask1();
              mAsynTask1.execute();
            }
          } else {
            Toast.makeText(Select_k3Activity.this, "网络连接异常，获得数据失败！",
                Toast.LENGTH_SHORT).show();
          }
        }

        @Override
        public void remainFiveMinutes() {

        }
      });
    } else {
      Toast.makeText(Select_k3Activity.this, "网络连接异常，获得数据失败！",
          Toast.LENGTH_SHORT).show();
    }
  }

  /** 初始化属性 上期开奖号码 */
  private void init() {
    SmanagerView
        .registerSensorManager(mSmanager, getApplicationContext(), this);// 注册传感器
    vibrator = VibratorView.getVibrator(getApplicationContext());
    // d得到数据
    setData();
    // 设置监听
    setList();
    // 给Adapter赋值

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
    layout_shake2.setOnClickListener(this);
    iv_shake2.setOnClickListener(this);
    tv_shake2.setOnClickListener(this);
    btn_select_all.setOnClickListener(this);
    settings = getSharedPreferences("app_user", 0);// 获取SharedPreference对象
    editor = settings.edit();// 获取编辑对象
    tv_lotteryname.setText("江苏快3");
    Map<Integer, String> playType = new HashMap<Integer, String>();
    playType.put(0, "和值");
    playType.put(1, "三同号单选");
    playType.put(2, "二同号单选");
    playType.put(3, "三不同号");
    playType.put(4, "二不同号");
    playType.put(5, "三同号通选");
    playType.put(6, "二同号复选");
    playType.put(7, "三连号通选");
    playType.put(8, "");
    Set<Integer> set = playType.keySet();
    int[] playtype_array = { 8301, 8303, 8305, 8306, 8307, 8302, 8304, 8308 };
    for (Integer i : set) {
      if (i < 8) {
        playtypeMap.put(playtype_array[i], i);
      }
    }
    data.put(0, playType);
    dialog = new ConfirmDialog(this, R.style.dialog);
    tip = Html.fromHtml("请至少选出一个号码");
    btn_playtype.setText(data.get(parentIndex).get(itemIndex));
    tv_top.setText(tip);
  }

  private void setData() {
    list1.clear();
    list2.clear();
    if (playID == 8301) {
      for (int i = 3; i < 19; i++) {
        list1.add(i + "");
      }
      list2.add("奖金298元");
      list2.add("奖金102元");
      list2.add("奖金56元");
      list2.add("奖金36元");
      list2.add("奖金24元");
      list2.add("奖金18元");
      list2.add("奖金16元");
      list2.add("奖金14元");
      list2.add("奖金14元");
      list2.add("奖金16元");
      list2.add("奖金18元");
      list2.add("奖金24元");
      list2.add("奖金36元");
      list2.add("奖金56元");
      list2.add("奖金102元");
      list2.add("奖金298元");
    } else if (playID == 8303) {
      for (int i = 1; i < 7; i++) {
        list1.add(i + "" + i + "" + i + "");
        list2.add("奖金298元");
      }
    } else if (playID == 8305) {
      for (int i = 1; i < 7; i++) {
        list2.add(i + "");
        list1.add(i + "" + i);
      }

    } else if (8309 == playID || 8310 == playID) {
      for (int i = 1; i < 7; i++) {
        list2.add(i + "");
        list1.add(i + "");
      }
    } else if (playID == 8307 || playID == 8306) {
      for (int i = 1; i < 7; i++) {
        list1.add(i + "");
      }
    } else if (playID == 8302) {
      list1.add("三同号通选");
    } else if (playID == 8304) {
      for (int i = 1; i < 7; i++) {
        list1.add(i + "" + i + "*");
      }
    } else if (playID == 8308) {
      list1.add("三连号通选");
    }
  }

  private void setList() {
    gridView.setOnItemClickListener(new OnItemClickListenerHezhi());
    gridView_ertonghaodan_1.setOnItemClickListener(new erbutong1());
    gridView_ertonghaodan_2.setOnItemClickListener(new erbutong2());
  }

  class OnItemClickListenerHezhi implements OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
      if (null != vibrator)
        vibrator.vibrate(150);
      // TODO Auto-generated method stub
      LinearLayout rl = (LinearLayout) view
          .findViewById(R.id.relativeLayout_pop_k3);
      TextView tv1 = (TextView) view.findViewById(R.id.pop_k3_text1);
      TextView tv2 = (TextView) view.findViewById(R.id.pop_k3_text2);
      String index;
      if (playID == 8301) {
        index = arg2 + 3 + "";
      } else
        index = arg2 + 1 + "";
      if (adpater.onSet1.contains(index)) {
        adpater.onSet1.remove(index);
        tv1.setTextColor(Color.RED);
        tv2.setTextColor(Color.BLACK);
        rl.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
      } else {
        System.out.println("index ====" + index);
        adpater.onSet1.add(index);
        tv1.setTextColor(Color.WHITE);
        tv2.setTextColor(Color.WHITE);
        rl.setBackgroundResource(R.drawable.bet_btn_dan_selected);
      }
      setCount();
    }
  }

  /**
   * 设置全选按钮 (0, "和值"); (1, "三同号单选"); (2, "二同号单选"); (3, "三不同号"); (4, "二不同号"); (5,
   * "三同号通选"); (6, "二同号复选"); (7, "三连号通选");
   * 
   * 三同号单选，三不同号，二同号复选 ，二同号单选，二不同号。
   */
  private void selectAll() {
    if (itemIndex == 1 || itemIndex == 2 || itemIndex == 3 || itemIndex == 4
        || itemIndex == 6) {
      if (itemIndex == 2) {
        adpater1.removeAll();
        adpater2.addAll();
      } else {
        String index;
        adpater.removeAll();
        for (int i = 0; i < list1.size(); i++) {
          if (playID == 8301) {
            index = i + 3 + "";
          } else
            index = i + 1 + "";
          if (adpater.onSet1.contains(index)) {
            adpater.onSet1.remove(index);
          } else {
            System.out.println("index ====" + index);
            adpater.onSet1.add(index);
          }
        }
        adpater.notifyDataSetChanged();
      }
      setCount();
    } else {
    }
  }

  class erbutong1 implements OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
      if (null != vibrator)
        vibrator.vibrate(150);
      // TODO Auto-generated method stub
      LinearLayout rl = (LinearLayout) view
          .findViewById(R.id.relativeLayout_pop_k3);
      TextView tv1 = (TextView) view.findViewById(R.id.pop_k3_text1);
      TextView tv2 = (TextView) view.findViewById(R.id.pop_k3_text2);
      String index = arg2 + 1 + "";

      if (adpater1.onSet1.contains(index)) {
        adpater1.onSet1.remove(index);
        rl.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
        tv1.setTextColor(Color.RED);
        tv2.setTextColor(Color.BLACK);
      } else {
        // if(playID == 8309){
        // if(adpater1.onSet1.size()>1){
        // Toast.makeText(context, "最多只能选择2个胆码", 1500).show();
        // return ;
        // }
        // }
        // else if (playID == 8310){
        // if(adpater1.onSet1.size()>0){
        // Toast.makeText(context, "最多只能选择2个胆码", 1500).show();
        // return ;
        // }
        // }
        if (adpater2.onSet1.contains(index)) {
          adpater2.onSet1.remove(index);
          adpater2.notifyDataSetChanged();
        }
        adpater1.onSet1.add(index);
        rl.setBackgroundResource(R.drawable.bet_btn_dan_selected);
        tv1.setTextColor(Color.WHITE);
        tv2.setTextColor(Color.WHITE);
      }
      setCount();
    }

  }

  class erbutong2 implements OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
      if (null != vibrator)
        vibrator.vibrate(150);
      // TODO Auto-generated method stub
      LinearLayout rl = (LinearLayout) view
          .findViewById(R.id.relativeLayout_pop_k3);
      TextView tv1 = (TextView) view.findViewById(R.id.pop_k3_text1);
      TextView tv2 = (TextView) view.findViewById(R.id.pop_k3_text2);
      String index = arg2 + 1 + "";
      if (adpater2.onSet1.contains(index)) {
        adpater2.onSet1.remove(index);
        rl.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
        tv1.setTextColor(Color.RED);
        tv2.setTextColor(Color.BLACK);
      } else {
        if (adpater1.onSet1.contains(index)) {
          adpater1.onSet1.remove(index);
          adpater1.notifyDataSetChanged();
        }
        adpater2.onSet1.add(index);
        rl.setBackgroundResource(R.drawable.bet_btn_dan_selected);
        tv1.setTextColor(Color.WHITE);
        tv2.setTextColor(Color.WHITE);
      }
      setCount();
    }

  }

  private void setCount() {
    if (playID == 8305 || 8309 == playID || 8310 == playID)
      AppTools.totalCount = NumberTools.getCountFor_k3(playID, adpater1.onSet1,
          adpater2.onSet1);
    else {
      AppTools.totalCount = NumberTools.getCountFor_k3(playID, adpater.onSet1,
          new ArrayList<String>());
    }
    tv_tatol_count.setText(+AppTools.totalCount + "");
    tv_tatol_money.setText((AppTools.totalCount * 2) + "");
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
      popUtil = new PopupWindowUtil(this, data, layout_top_select);
      popUtil.setSelectIndex(parentIndex, itemIndex);
      popUtil.createPopWindow();
      popUtil.setOnSelectedListener(new PopupWindowUtil.OnSelectedListener() {
        @Override
        public void getIndex(int parentIndex, int itemIndex) {
          // TODO Auto-generated method stub
          if (itemIndex <= 7) {
            if (itemIndex != Select_k3Activity.this.itemIndex) {
              Select_k3Activity.this.parentIndex = parentIndex;
              Select_k3Activity.this.itemIndex = itemIndex;
              initSelectAll();
              changePlayType();
              AppTools.totalCount = 0;
              clear();
            }
          }
          rote(2);// 旋转动画 向下
        }

      });
      rote(1);// 旋转动画 向上
      break;
    }
    gridView.setAdapter(adpater);
    update();
    setCount();
  }

  /**
   * 设置全选按钮 (0, "和值"); (1, "三同号单选"); (2, "二同号单选"); (3, "三不同号"); (4, "二不同号"); (5,
   * "三同号通选"); (6, "二同号复选"); (7, "三连号通选");
   * 
   * 三同号单选，三不同号，二同号复选 ，二同号单选，二不同号。
   */
  private void initSelectAll() {
    if (itemIndex == 1 || itemIndex == 2 || itemIndex == 3 || itemIndex == 4
        || itemIndex == 6) {
      select_all_rl.setVisibility(View.VISIBLE);
    } else {
      select_all_rl.setVisibility(View.GONE);
    }
  }

  private void update() {
    if (adpater.onSet1 != null && adpater1.onSet1 != null
        && adpater2.onSet1 != null) {
      adpater.notifyDataSetChanged();
      adpater1.notifyDataSetChanged();
      adpater2.notifyDataSetChanged();
    }
  }

  public void changePlayType() {
    setShakeBtnVisible(View.VISIBLE);
    btn_playtype.setText(data.get(parentIndex).get(itemIndex));
    relativeLayout1.setVisibility(View.GONE);
    relativeLayout2.setVisibility(View.GONE);
    switch (itemIndex) {
    case 0:// 和 值
      relativeLayout1.setVisibility(View.VISIBLE);
      playID = 8301;
      setData();// 拿取數據
      tip = Html.fromHtml("请至少选出一个号码");
      tv_top.setText(tip);
      gridView.setNumColumns(4);
      adpater.notifyDataSetChanged();
      break;
    case 1:// 三同号单选
      relativeLayout1.setVisibility(View.VISIBLE);
      playID = 8303;
      setData();// 拿取數據
      tip = Html.fromHtml("单注奖金固定为"
          + AppTools.changeStringColor("#e3393c", "298") + "元");
      tv_top.setText(tip);
      gridView.setNumColumns(3);
      adpater.notifyDataSetChanged();
      break;
    case 2:// 二同号单选
      playID = 8305;
      setData();// 拿取數據
      relativeLayout2.setVisibility(View.VISIBLE);
      gridView_ertonghaodan_1.setAdapter(adpater1);
      gridView_ertonghaodan_2.setAdapter(adpater2);
      tv_dan.setVisibility(View.VISIBLE);
      tv_tuo.setVisibility(View.VISIBLE);
      tv_title.setVisibility(View.VISIBLE);
      tip = Html.fromHtml("单注奖金固定为"
          + AppTools.changeStringColor("#e3393c", "102") + "元");
      tv_title.setText(tip);
      tv_dan.setText("同号");
      tv_tuo.setText("不同号");
      adpater1.notifyDataSetChanged();
      adpater2.notifyDataSetChanged();
      break;
    case 3:// 三不同号
      relativeLayout1.setVisibility(View.VISIBLE);
      playID = 8306;
      setData();// 拿取數據
      tip = Html.fromHtml("单注奖金固定为"
          + AppTools.changeStringColor("#e3393c", "56") + "元");
      tv_top.setText(tip);
      gridView.setNumColumns(6);
      adpater.notifyDataSetChanged();
      break;
    case 4:// 二不同号
      relativeLayout1.setVisibility(View.VISIBLE);
      playID = 8307;
      setData();// 拿取數據
      tip = Html.fromHtml("单注奖金固定为"
          + AppTools.changeStringColor("#e3393c", "12") + "元");
      tv_top.setText(tip);
      gridView.setNumColumns(3);
      adpater.notifyDataSetChanged();
      break;
    case 5:// 三同号通选
      setShakeBtnVisible(View.GONE);
      relativeLayout1.setVisibility(View.VISIBLE);
      playID = 8302;
      setData();// 拿取數據
      tip = Html.fromHtml("单注奖金固定为"
          + AppTools.changeStringColor("#e3393c", "62") + "元");
      tv_top.setText(tip);
      gridView.setNumColumns(1);
      adpater.notifyDataSetChanged();
      break;
    case 6:// 二同号复选
      relativeLayout1.setVisibility(View.VISIBLE);
      playID = 8304;
      setData();// 拿取數據
      tip = Html.fromHtml("单注奖金固定为"
          + AppTools.changeStringColor("#e3393c", "22") + "元");
      tv_top.setText(tip);
      gridView.setNumColumns(3);
      adpater.notifyDataSetChanged();
      break;
    case 7:// 三连号通选
      setShakeBtnVisible(View.GONE);
      relativeLayout1.setVisibility(View.VISIBLE);
      playID = 8308;
      setData();// 拿取數據
      tip = Html.fromHtml("单注奖金固定为"
          + AppTools.changeStringColor("#e3393c", "16") + "元");
      tv_top.setText(tip);
      gridView.setNumColumns(1);
      adpater.notifyDataSetChanged();
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

  /** 设置机选按钮是否可见 */
  private void setShakeBtnVisible(int v) {
    layout_shake.setVisibility(v);
    layout_shake2.setVisibility(v);
  }

  /** 从投注页面跳转过来 将投注页面的值 显示出来 */
  public void getItem() {
    Intent intent = Select_k3Activity.this.getIntent();
    bundle = intent.getBundleExtra("k_3Bundle");
    if (null != bundle) {
      System.out.println("----///---");
      playID = bundle.getInt("type");
      itemIndex = playtypeMap.get(playID);
      changePlayType();
      if (bundle.getInt("shouji") == 0) {
        return;
      } else {
        if (playID != 8305 && playID != 8309 && playID != 8310) {
          ArrayList<String> list = bundle.getStringArrayList("oneSet");
          adpater.setonSet1(list);
          System.out.println("select_+" + adpater.onSet1.toString());
        } else {
          ArrayList<String> list2 = bundle.getStringArrayList("oneSet");
          System.out.println("slele+list2" + list2);
          adpater1.setonSet1(list2);
          ArrayList<String> list3 = bundle.getStringArrayList("twoSet");
          System.out.println("slele+list3" + list3);
          adpater2.setonSet1(list3);

        }
        adpater.notifyDataSetChanged();
        adpater1.notifyDataSetChanged();
        adpater2.notifyDataSetChanged();
        // tv_Count .setText(AppTools.totalCount+"注");
        // tv_Money .setText(AppTools.totalCount*2+"元");
        setCount();
      }
    }

  }

  /** 玩法说明 */
  private void playExplain() {
    Intent intent = new Intent(Select_k3Activity.this, PlayDescription.class);
    Select_k3Activity.this.startActivity(intent);
  }

  /** 机选 按钮点击 */
  public void selectRandom() {
    List<String> list = new ArrayList<String>();
    if (null != vibrator)
      vibrator.vibrate(150);
    switch (playID) {
    case 8301: // 和值
      list.add((int) ((Math.random() * 16) + 3) + "");
      adpater.setonSet1(list);
      break;
    case 8302: // 三同通选
    case 8308: // 三连号通选
      list.add("1");
      adpater.setonSet1(list);
      break;
    case 8303: // 三同单选
    case 8304: // 二同复选
      adpater.setonSet1(NumberTools.getRandomNum6(1, 6));
      break;
    case 8307:// 二不同号
      adpater.setonSet1(NumberTools.getRandomNum6(2, 6));
      break;
    case 8306:// 三不同号
      adpater.setonSet1(NumberTools.getRandomNum6(3, 6));
      break;
    case 8305:// 二同单选
      list = NumberTools.getRandomNum6(1, 6);
      adpater1.setonSet1(list);
      while (true) {
        list = NumberTools.getRandomNum6(1, 6);
        if (!adpater1.onSet1.get(0).equals(list.get(0))) {
          adpater2.setonSet1(list);
          break;
        } else {
          continue;
        }
      }
      break;
    }
    update();
    setCount();

  }

  /** 提交号码 */
  private void submitNumber() {
    if (AppTools.totalCount < 1) {
      MyToast.getToast(Select_k3Activity.this, "请至少选择一注").show();
      return;
    }
    Intent intent1 = new Intent(Select_k3Activity.this, Bet_k3Activity.class);
    Bundle bundle = new Bundle();
    if (playID != 8305 && playID != 8309 && playID != 8310) {
      Collections.sort(adpater.onSet1);
      bundle.putString("one", adpater.onSet1.toString().replace("[", "")
          .replace("]", ""));
    } else {
      Collections.sort(adpater1.onSet1);
      Collections.sort(adpater2.onSet1);
      bundle.putString("one", adpater1.onSet1.toString().replace("[", "")
          .replace("]", ""));
      bundle.putString("two", adpater2.onSet1.toString().replace("[", "")
          .replace("]", ""));
    }
    bundle.putInt("playType", playID);
    intent1.putExtra("bundle", bundle);
    Select_k3Activity.this.startActivity(intent1);
  }

  /** 清空 */
  private void clear() {
    if (null != adpater.onSet1) {
      adpater.onSet1.clear();
    }
    if (null != adpater1.onSet1) {
      adpater1.onSet1.clear();
    }
    if (null != adpater2.onSet1) {
      adpater2.onSet1.clear();
    }
    update();
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
      if (0 != adpater.onSet1.size() || 0 != adpater1.onSet1.size()
          || 0 != adpater2.onSet1.size()) {
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
      if (0 != adpater.onSet1.size() || 0 != adpater1.onSet1.size()
          || 0 != adpater2.onSet1.size()) {
        dialog.show();
        dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
        dialog
            .setDialogResultListener(new ConfirmDialog.DialogResultListener() {
              @Override
              public void getResult(int resultCode) {
                // TODO Auto-generated method stub
                if (1 == resultCode) {// 确定
                  clear();
                  Intent intent = new Intent(Select_k3Activity.this,
                      Bet_k3Activity.class);
                  Select_k3Activity.this.startActivity(intent);
                  Select_k3Activity.this.finish();
                }
              }
            });
      } else {
        clear();
        Intent intent = new Intent(Select_k3Activity.this, Bet_k3Activity.class);
        Select_k3Activity.this.startActivity(intent);
        Select_k3Activity.this.finish();
      }
    }
  }

  /** 刷新Adapter */
  public void updateAdapter() {
    tv_tatol_count.setText(AppTools.totalCount + "");
    tv_tatol_money.setText((AppTools.totalCount * 2) + "");
  }

  /** 清空选中情况 */
  public static void clearHashSet() {

  }

  /** 异步任务 用来后台获取数据 */
  class MyAsynTask extends AsyncTask<Integer, Integer, String> {
    @Override
    protected String doInBackground(Integer... params) {
      return AppTools.getDate(playID + "", Select_k3Activity.this);
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
      case 100:
        initNum();
        break;
      case 101:
        MyToast.getToast(getApplicationContext(), "获取中奖信息失败！").show();
        break;
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
      }
      super.handleMessage(msg);
    }
  }

  class Adpater extends BaseAdapter {
    Context context;
    List<String> list1;
    List<String> list2;
    private List<String> onSet1 = new ArrayList<String>();

    public Adpater(List<String> list1, List<String> list2, Context context) {
      this.context = context;
      this.list1 = list1;
      this.list2 = list2;
    }

    public void setonSet1(List<String> list) {
      this.onSet1 = list;
    }

    public void removeAll() {
      onSet1.clear();
    }

    /**
     * 只适用部分玩法，如二同号单选
     */
    public void addAll() {
      for (String n : list1) {
        if (!onSet1.contains(n)) {
          onSet1.add(n);
        }
      }
      notifyDataSetChanged();
    }

    @Override
    public int getCount() {
      return list1.size();
    }

    @Override
    public Object getItem(int arg0) {
      return list1.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
      return arg0;
    }

    @Override
    public View getView(int position, View view, ViewGroup arg2) {
      ViewHolder holder;
      final String index = position + 1 + "";
      // 判断View是否为空
      if (view == null) {
        holder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(context);
        // 得到布局文件
        view = inflater.inflate(R.layout.item_k3, null);
        // 得到控件
        holder.relativeLayout_poo = (LinearLayout) view
            .findViewById(R.id.relativeLayout_pop_k3);

        holder.hezhi = (TextView) view.findViewById(R.id.pop_k3_text1);
        holder.jiangjin = (TextView) view.findViewById(R.id.pop_k3_text2);
        view.setTag(holder);
      } else {
        holder = (ViewHolder) view.getTag();
      }
      // holder.hezhi.setTextSize(15);
      holder.hezhi.setTextColor(Color.RED);
      holder.jiangjin.setTextColor(Color.BLACK);
      holder.hezhi.setText(list1.get(position));
      if (list2.size() != 0) {
        holder.jiangjin.setText(list2.get(position));
        holder.jiangjin.setVisibility(View.VISIBLE);
      } else {
        holder.jiangjin.setVisibility(View.GONE);
      }

      // if (playID > 8302) {
      // holder.hezhi.setTextSize(25);
      if (8302 == playID || 8308 == playID) {
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        // param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
        holder.relativeLayout_poo.setLayoutParams(param);
      } else {
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
            LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        // param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
        holder.relativeLayout_poo.setLayoutParams(param);
      }
      // holder.relativeLayout_poo.setBackgroundResource(R.drawable.btn_k3_);
      // }
      // } else {
      // holder.hezhi.setTextSize(17);
      // }
      holder.relativeLayout_poo
          .setBackgroundResource(R.drawable.bet_btn_dan_unselected);
      if (playID == 8301) {
        if (adpater.onSet1.contains(position + 3 + "")) {
          holder.relativeLayout_poo
              .setBackgroundResource(R.drawable.bet_btn_dan_selected);
          holder.hezhi.setTextColor(Color.WHITE);
          holder.jiangjin.setTextColor(Color.WHITE);
        }
      } else {
        if (onSet1.contains(position + 1 + "")) {
          holder.relativeLayout_poo
              .setBackgroundResource(R.drawable.bet_btn_dan_selected);
          holder.hezhi.setTextColor(Color.WHITE);
          holder.jiangjin.setTextColor(Color.WHITE);
        }
      }

      return view;
    }

    class ViewHolder {
      private TextView hezhi, jiangjin;
      private LinearLayout relativeLayout_poo;
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
