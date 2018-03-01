package com.sm.sls_app.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.DtMatch;
import com.sm.sls_app.dataaccess.DtMatch_Basketball;
import com.sm.sls_app.dataaccess.Lottery;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.ui.Buy_DLT_Activit;
import com.sm.sls_app.ui.Buy_RX9_Activit;
import com.sm.sls_app.ui.Buy_SFC_Activity;
import com.sm.sls_app.ui.LoginActivity;
import com.sm.sls_app.ui.LuckNumberActivity;
import com.sm.sls_app.ui.MainActivity;
import com.sm.sls_app.ui.SelectNumberActivity;
import com.sm.sls_app.ui.SelectNumberActivityFC3D;
import com.sm.sls_app.ui.SelectNumberActivityPL3;
import com.sm.sls_app.ui.SelectNumberActivityPL5_QXC;
import com.sm.sls_app.ui.Select_11X5Activity;
import com.sm.sls_app.ui.Select_BJPK10Activity;
import com.sm.sls_app.ui.Select_HNSSCActivity;
import com.sm.sls_app.ui.Select_JXSSCActivity;
import com.sm.sls_app.ui.Select_QlcActivity;
import com.sm.sls_app.ui.Select_SSCActivity;
import com.sm.sls_app.ui.Select_XJSSCActivity;
import com.sm.sls_app.ui.Select_YNSSCActivity;
import com.sm.sls_app.ui.Select_jclqActivity;
import com.sm.sls_app.ui.Select_jczqActivity;
import com.sm.sls_app.ui.Select_k3Activity;
import com.sm.sls_app.ui.SettingActivity;
import com.sm.sls_app.ui.adapter.GridView_HallLotteryAdapter;
import com.sm.sls_app.ui.adapter.MyMenuGridViewAdapter;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.BaseHelper;
import com.sm.sls_app.utils.NetWork;
import com.sm.sls_app.view.MarqueeTextView;
import com.sm.sls_app.view.MyMenu;
import com.sm.sls_app.view.MyToast;
import com.sm.sls_app.view.PullToRefreshView;
import com.sm.sls_app.view.PullToRefreshView.OnHeaderRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * * 购彩
 * 
 * @author Kinwee 修改日期2014-12-20
 * 
 */
@SuppressLint({ "NewApi", "DefaultLocale" })
public class HallFragment extends Fragment implements OnClickListener,
    OnScrollListener, OnHeaderRefreshListener {

  private static final String TAG = "HallFragment";
  public static String Message = null;
  private GridView_HallLotteryAdapter gvAdapter;
  private PullToRefreshView mPullToRefreshView;
  private GridView gv_hall_lottry;

  private MarqueeTextView rollTextView;

  public static List<Lottery> listLottery;
  public static boolean refreType = true;

  private MyAsynTask myAsynTask;
  private Context context;

  private String opt = "10"; // 格式化后的 opt
  private String auth, info, time, imei, crc; // 格式化后的参数

  /** 自定义Menu **/
  private MyMenu mMenu;
  private LinearLayout layout;
  private MyMenuGridViewAdapter menuAdapter;
  private List<Integer> menuList = new ArrayList<Integer>();
  // private MyItemCLickListener menuItemClick;
  /** 自定义Menu **/

  private Button btn_luck;
  private MyHandler myHandler;
  private Intent intent;

  private Integer index = 1;

  private ProgressDialog pd;

  // private RelativeLayout layout_por;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.activity_hall, container, false);
    init();
    findView(v);
    setListener();
    return v;
  }

  /** 初始化 */
  private void init() {
    context = getActivity();
    time = RspBodyBaseBean.getTime();
  }

  /** 初始化UI */
  private void findView(View v) {
    Log.i("x", "findVivew---");
    myHandler = new MyHandler();
    gv_hall_lottry = (GridView) v.findViewById(R.id.gv_hall_lottry);
    mPullToRefreshView = (PullToRefreshView) v
        .findViewById(R.id.mPullToRefreshView);
    myAsynTask = new MyAsynTask();
    myAsynTask.execute(1);
    gvAdapter = new GridView_HallLotteryAdapter(context, getData());
    gv_hall_lottry.setAdapter(gvAdapter);
    mPullToRefreshView.setOnHeaderRefreshListener(this);
    rollTextView = (MarqueeTextView) v.findViewById(R.id.rolltextView1);
    btn_luck = (Button) v.findViewById(R.id.btn_luckyBtn);

    /** 要更改的 **/
    menuList.add(R.drawable.menu_refresh_change);
    menuList.add(R.drawable.menu_setting_change);
    menuList.add(R.drawable.menu_changeuser_change);
    menuList.add(R.drawable.menu_exit_change);

    if (NetWork.isConnect(context)) {
      myAsynTask = new MyAsynTask();
      myAsynTask.execute(index);
    } else {
      MyToast.getToast(context, "网络连接异常，请检查网络").show();
      MainActivity.ll_main.setVisibility(View.GONE);
      // list_lottery.setOnItemClickListener(new ItemsClickListener());
    }
    System.out.println("------------*" + HallFragment.Message);
    if (HallFragment.Message != null && !HallFragment.Message.equals("")) {
      rollTextView.setRollText(HallFragment.Message, 1);
    } else
      rollTextView.setRollText("----没有中奖信息----", 0);
  }

  /** 绑定监听 */
  private void setListener() {
    btn_luck.setOnClickListener(this);
    // list_lottery.setAdapter(adapter);
    gv_hall_lottry.setAdapter(gvAdapter);
    gv_hall_lottry.setOnItemClickListener(new ItemsClickListener());
  }

  /** 得到竞彩 篮球数据 */
  public String getBasketball() {
    String key2 = MD5.md5(AppTools.MD5_key);
    info = RspBodyBaseBean.changeLottery_info("73", "-1");
    crc = RspBodyBaseBean.getCrc(time, imei, key2, info, "-1");
    auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");
    String[] values2 = { opt, auth, info };
    System.out.println("opt=" + opt);
    System.out.println("auth==" + auth);
    System.out.println("info==" + info);
    String result2 = HttpUtils.doPost(AppTools.names, values2, AppTools.path);
    System.out.println("拿到竞彩篮球的值--" + result2);
    if ("-500".equals(result2))
      return result2;

    try {
      if (null != result2) {
        JSONObject object2 = new JSONObject(result2);
        String error2 = object2.optString("error");
        if ("0".equals(error2)) {
          String detail2 = object2.optString("dtMatch");
          // detail2 =
          if (detail2.length() == 0) {
            Log.i("x", "无数据");
          } else {
            // 拿到对阵信息组
            JSONArray array2 = new JSONArray(new JSONArray(detail2).toString());

            List<List<DtMatch_Basketball>> listMatch = new ArrayList<List<DtMatch_Basketball>>();

            for (int i = 0; i < array2.length(); i++) {

              JSONObject ob = array2.getJSONObject(i);

              String arr_l = ob.optString("table1");
              String arr2_l = ob.optString("table2");
              String arr3_l = ob.optString("table3");

              // 判断对阵 是否有
              listMatch.add(setList2(arr_l));
              listMatch.add(setList2(arr2_l));
              listMatch.add(setList2(arr3_l));

            }

            for (int i = 0; i < listLottery.size(); i++) {
              if ("73".equals(listLottery.get(i).getLotteryID())) {
                listLottery.get(i).setDtMatch_Basketball(listMatch);
                listLottery.get(i).setExplanation(
                    object2.optString("explanation"));
              }
            }
          }
        }
      } else {
        Log.i("x", "拿竞彩足球数据为空");
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.out.println("错误" + e.getMessage());
    }
    return "0";
  }

  /** 篮球绑定数据 **/
  private List<DtMatch_Basketball> setList2(String arr) {
    List<DtMatch_Basketball> list_m = new ArrayList<DtMatch_Basketball>();
    if (arr.length() > 5) {
      JSONArray Arr;
      try {
        Arr = new JSONArray(arr);
        DtMatch_Basketball dtmatch = null;
        for (int j = 0; j < Arr.length(); j++) {
          JSONObject item = Arr.getJSONObject(j);
          dtmatch = new DtMatch_Basketball();
          dtmatch.setMatchId(item.optString("matchId"));
          dtmatch.setMatchNumber(item.optString("matchNumber"));
          dtmatch.setMatchDate(item.optString("matchDate"));
          dtmatch.setGame(item.getString("game"));
          dtmatch.setGuestTeam(item.optString("guestTeam"));
          dtmatch.setMainTeam(item.getString("mainTeam"));
          dtmatch.setStopSellTime(item.optString("stopSellTime"));
          dtmatch.setMatchDate1(item.optString("matchDate1"));
          dtmatch.setMainLose(item.getString("mainLose"));
          dtmatch.setMainWin(item.getString("mainWin"));
          dtmatch.setSmall(item.getString("small"));
          dtmatch.setBigSmallScore(item.getString("bigSmallScore"));
          dtmatch.setBig(item.getString("big"));
          dtmatch.setLetScore(item.getString("letScore"));
          dtmatch.setLetMainLose(item.getString("letMainLose"));
          dtmatch.setLetMainWin(item.getString("letMainWin"));
          dtmatch.setMatchDate2(item.getString("matchWeek"));
          dtmatch.setDifferGuest1_5(item.optString("differGuest1_5"));
          dtmatch.setDifferGuest6_10(item.optString("differGuest6_10"));
          dtmatch.setDifferGuest11_15(item.optString("differGuest11_15"));
          dtmatch.setDifferGuest16_20(item.optString("differGuest16_20"));
          dtmatch.setDifferGuest21_25(item.optString("differGuest21_25"));
          dtmatch.setDifferGuest26(item.optString("differGuest26"));
          dtmatch.setDifferMain1_5(item.optString("differMain1_5"));
          dtmatch.setDifferMain6_10(item.optString("differMain6_10"));
          dtmatch.setDifferMain11_15(item.optString("differMain11_15"));
          dtmatch.setDifferMain16_20(item.optString("differMain16_20"));
          dtmatch.setDifferMain21_25(item.optString("differMain21_25"));
          dtmatch.setDifferMain26(item.optString("differMain26"));
          dtmatch.setSF(Boolean.parseBoolean(item.getString("isSF")
              .toLowerCase()));
          dtmatch.setDXF(Boolean.parseBoolean(item.getString("isDXF")
              .toLowerCase()));
          dtmatch.setRFSF(Boolean.parseBoolean(item.getString("isRFSF")
              .toLowerCase()));
          dtmatch.setSFC(Boolean.parseBoolean(item.getString("isSFC")
              .toLowerCase()));
          list_m.add(dtmatch);
        }
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        Log.i("x", "HallFragment错误" + e.getMessage());
      }
    }
    return list_m;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // TODO Auto-generated method stub
    switch (item.getItemId()) {
    /** 刷新 **/
    case 1:
      getData();
      // list_lottery.state = MyListView.REFRESHING;
      // list_lottery.changeHeaderViewByState();
      // list_lottery.onRefresh();
      break;
    /** 设置 **/
    case 2:
      intent = new Intent(context, SettingActivity.class);
      context.startActivity(intent);
      break;
    /** 更改账户 **/
    case 3:
      intent = new Intent(context, LoginActivity.class);
      intent.putExtra("loginType", "genggai");
      context.startActivity(intent);
      break;
    /** 退出 **/
    case 4:
      for (Activity activity : App.activityS) {
        activity.finish();
      }
      break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    menu.clear();

    int sysVersion = Integer.parseInt(VERSION.SDK);
    if (sysVersion > 10) {
      menu.add(1, 1, 0, "刷新");
      menu.add(1, 2, 0, "设置");
      menu.add(1, 3, 0, "更换账户");
      menu.add(1, 4, 0, "退出");
    } else {
      menu.add(1, 1, 0, "").setIcon(R.drawable.menu_refresh_select);
      menu.add(1, 2, 0, "").setIcon(R.drawable.menu_setting_select);
      menu.add(1, 3, 0, "").setIcon(R.drawable.menu_changeuser_select);
      menu.add(1, 4, 0, "").setIcon(R.drawable.menu_exit_select);
    }
    super.onCreateOptionsMenu(menu, inflater);
  }

  /** Menu的点击监听 */
  // class MyItemCLickListener implements OnItemClickListener {
  // @Override
  // public void onItemClick(AdapterView<?> arg0, View view, int position,
  // long arg3) {
  // switch (position) {
  // /** 刷新 **/
  // case 0:
  // getData();
  // list_lottery.state = MyListView.REFRESHING;
  // list_lottery.changeHeaderViewByState();
  // list_lottery.onRefresh();
  // break;
  // /** 设置 **/
  // case 1:
  // intent = new Intent(context, SettingActivity.class);
  // context.startActivity(intent);
  // break;
  // /** 更改账户 **/
  // case 2:
  // intent = new Intent(context, LoginActivity.class);
  // intent.putExtra("loginType", "genggai");
  // context.startActivity(intent);
  // break;
  // /** 退出 **/
  // case 3:
  // for (Activity activity : App.activityS) {
  // activity.finish();
  // }
  // break;
  // }
  // mMenu.dismiss();
  // mMenu = null;
  // }
  // }

  /** listView Item 的点击监听 */
  class ItemsClickListener implements OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position,
        long arg3) {
      // TODO Auto-generated method stub
      if (0 == listLottery.size() % 2) {
        long currentTime = System.currentTimeMillis();

        System.out.println("==========得到的id"
            + listLottery.get(position).getLotteryID());
        // if (!"73".equals(listLottery.get(position).getLotteryID())
        // && !"72".equals(listLottery.get(position)
        // .getLotteryID())) {
        // if (listLottery.get(position).getDistanceTime()
        // - currentTime <= 0) {
        // MyToast.getToast(context, "该奖期已结束，请等下一期").show();
        // return;
        // }
        // }
        if (listLottery.size() > 0) {

          AppTools.lottery = listLottery.get(position);
          System.out.println("==============="
              + AppTools.lottery.getLotteryID());
          if (AppTools.lottery.getIsuseId() != null
              && AppTools.lottery.getIsuseId() != ""
              && !"72".equals(AppTools.lottery.getLotteryID())
              && !"73".equals(AppTools.lottery.getLotteryID())) {
            System.out.println("==================================2222");
            if (AppTools.lottery.getLotteryID() == "6") {
              intent = new Intent(context, SelectNumberActivityFC3D.class);
            } else if (AppTools.lottery.getLotteryID() == "63") {
              intent = new Intent(context, SelectNumberActivityPL3.class);
            } else if (AppTools.lottery.getLotteryID() == "3"
                || AppTools.lottery.getLotteryID() == "64") {
              intent = new Intent(context, SelectNumberActivityPL5_QXC.class);
            } else if (AppTools.lottery.getLotteryID() == "5") {
              intent = new Intent(context, SelectNumberActivity.class);
            } else if (AppTools.lottery.getLotteryID() == "39") {
              intent = new Intent(context, Buy_DLT_Activit.class);
            } else if (AppTools.lottery.getLotteryID() == "74") {
              intent = new Intent(context, Buy_SFC_Activity.class);
            } else if (AppTools.lottery.getLotteryID() == "75") {
              intent = new Intent(context, Buy_RX9_Activit.class);
            } else if (AppTools.lottery.getLotteryID() == "13") {
              intent = new Intent(context, Select_QlcActivity.class);
            } else if (AppTools.lottery.getLotteryID() == "62"
                || AppTools.lottery.getLotteryID() == "70"
                || AppTools.lottery.getLotteryID() == "78") {
              intent = new Intent(context, Select_11X5Activity.class);
            } else if (AppTools.lottery.getLotteryID() == "28") {
              intent = new Intent(context, Select_SSCActivity.class);
              // 新增印尼时时彩
            } else if (AppTools.lottery.getLotteryID() == "93") {
              intent = new Intent(context, Select_YNSSCActivity.class);
            } else if (AppTools.lottery.getLotteryID() == "83") {
              intent = new Intent(context, Select_k3Activity.class);
            } else if (AppTools.lottery.getLotteryID() == "61") {
              intent = new Intent(context, Select_JXSSCActivity.class);
              // 新增新疆时时彩
            } else if (AppTools.lottery.getLotteryID() == "66") {
              intent = new Intent(context, Select_XJSSCActivity.class);
              // 新增的河内时时彩
            } else if (AppTools.lottery.getLotteryID() == "92") {
              intent = new Intent(context, Select_HNSSCActivity.class);
            } else if (AppTools.lottery.getLotteryID() == "94") {
              intent = new Intent(context, Select_BJPK10Activity.class);
            } else {
              MyToast.getToast(context, "正在开发").show();
              return;
            }
            context.startActivity(intent);
          } else {
            MyAsynTask2 myAsynTask2 = new MyAsynTask2();
            refreType = true;
            // 对阵信息是否为空
            if (AppTools.lottery.getDtmatch() != null
                && AppTools.lottery.getDtmatch().length() != 0) {
              if ("72".equals(AppTools.lottery.getLotteryID())) {
                pd = BaseHelper.showProgress(context, "", "对阵加载中...", true,
                    false);
                myAsynTask2.execute(1);

              } else if ("73".equals(AppTools.lottery.getLotteryID())) {
                pd = BaseHelper.showProgress(context, "", "对阵加载中...", true,
                    false);
                myAsynTask2.execute(2);
              }
            } else {
              MyToast.getToast(context, "没有对阵信息").show();
            }
          }
        } else {
          MyToast.getToast(context, "没有当前期号").show();
        }
      } else {
        if (listLottery.size() != position) {
          long currentTime = System.currentTimeMillis();
          // if
          // (!"73".equals(listLottery.get(position).getLotteryID())
          // && !"72".equals(listLottery.get(position)
          // .getLotteryID())) {
          // if (listLottery.get(position).getDistanceTime()
          // - currentTime <= 0) {
          // MyToast.getToast(context, "该奖期已结束，请等下一期").show();
          // return;
          // }
          // }
          if (listLottery.size() > 0) {

            AppTools.lottery = listLottery.get(position);
            if (AppTools.lottery.getIsuseId() != null
                && AppTools.lottery.getIsuseId() != ""
                && !"72".equals(AppTools.lottery.getLotteryID())
                && !"73".equals(AppTools.lottery.getLotteryID())) {
              if (AppTools.lottery.getLotteryID() == "6") {
                intent = new Intent(context, SelectNumberActivityFC3D.class);
              } else if (AppTools.lottery.getLotteryID() == "63") {
                intent = new Intent(context, SelectNumberActivityPL3.class);
              } else if (AppTools.lottery.getLotteryID() == "3"
                  || AppTools.lottery.getLotteryID() == "64") {
                intent = new Intent(context, SelectNumberActivityPL5_QXC.class);
              } else if (AppTools.lottery.getLotteryID() == "5") {
                intent = new Intent(context, SelectNumberActivity.class);
              } else if (AppTools.lottery.getLotteryID() == "39") {
                intent = new Intent(context, Buy_DLT_Activit.class);
              } else if (AppTools.lottery.getLotteryID() == "74") {
                intent = new Intent(context, Buy_SFC_Activity.class);
              } else if (AppTools.lottery.getLotteryID() == "75") {
                intent = new Intent(context, Buy_RX9_Activit.class);
              } else if (AppTools.lottery.getLotteryID() == "13") {
                intent = new Intent(context, Select_QlcActivity.class);
              } else if (AppTools.lottery.getLotteryID() == "62"
                  || AppTools.lottery.getLotteryID() == "70"
                  || AppTools.lottery.getLotteryID() == "78") {
                intent = new Intent(context, Select_11X5Activity.class);
              } else if (AppTools.lottery.getLotteryID() == "28") {
                intent = new Intent(context, Select_SSCActivity.class);
                // 新增印尼时时彩
              } else if (AppTools.lottery.getLotteryID() == "93") {
                intent = new Intent(context, Select_YNSSCActivity.class);
              } else if (AppTools.lottery.getLotteryID() == "83") {
                intent = new Intent(context, Select_k3Activity.class);
              } else if (AppTools.lottery.getLotteryID() == "61") {
                intent = new Intent(context, Select_JXSSCActivity.class);
                // 新增新疆时时彩
              } else if (AppTools.lottery.getLotteryID() == "66") {
                intent = new Intent(context, Select_XJSSCActivity.class);
              } else if (AppTools.lottery.getLotteryID() == "92") {
                intent = new Intent(context, Select_HNSSCActivity.class);
              } else if (AppTools.lottery.getLotteryID() == "94") {
                intent = new Intent(context, Select_BJPK10Activity.class);
                return;
              } else {
                MyToast.getToast(context, "正在开发").show();
                return;
              }
              context.startActivity(intent);
            } else {
              MyAsynTask2 myAsynTask2 = new MyAsynTask2();
              refreType = true;
              // 对阵信息是否为空
              if (AppTools.lottery.getDtmatch() != null
                  && AppTools.lottery.getDtmatch().length() != 0) {
                if ("72".equals(AppTools.lottery.getLotteryID())) {
                  pd = BaseHelper.showProgress(context, "", "对阵加载中...", true,
                      false);
                  myAsynTask2.execute(1);
                } else if ("73".equals(AppTools.lottery.getLotteryID())) {
                  pd = BaseHelper.showProgress(context, "", "对阵加载中...", true,
                      false);
                  myAsynTask2.execute(2);
                }
              } else {
                MyToast.getToast(context, "没有对阵信息").show();
              }
            }
          } else {
            MyToast.getToast(context, "没有当前期号").show();
          }
        }
      }
    }
  }

  /** 获得所有彩种名称 以及 彩种ID */
  private List<Lottery> getData() {
    if (listLottery == null)
      listLottery = new ArrayList<Lottery>();

    if (refreType) {
      // 清空数据
      listLottery.clear();
      Lottery lottery = null;
      if (null != AppTools.allLotteryName) {
        Iterator iterator = AppTools.allLotteryName.entrySet().iterator();
        while (iterator.hasNext()) {
          Map.Entry entry = (Map.Entry) iterator.next();
          String key = entry.getKey().toString();
          String value = entry.getValue().toString();
          lottery = new Lottery();
          lottery.setLotteryID(value);
          lottery.setLotteryName(key);
          listLottery.add(lottery);
        }
      }
    }
    return listLottery;
  }

  /** 后台获得所有彩票信息 */
  public String getLotteryData() {
    if (listLottery == null)
      listLottery = new ArrayList<Lottery>();

    if (refreType) {
      return AppTools.getDate(AppTools.lotteryIds, context);

    }
    return "0";
  }

  /** 异步任务 用来后台获取数据 */
  class MyAsynTask extends AsyncTask<Integer, Integer, String> {
    String error = "0";

    @Override
    protected String doInBackground(Integer... params) {
      switch (params[0]) {
      case 1:
        error = getLotteryData();
        break;
      case 2:
        error = AppTools.doLogin(context);
        break;
      default:
        break;
      }
      return error;
    }

    @Override
    protected void onPostExecute(String result) {
      // TODO Auto-generated method stub
      if ("-500".equals(result)) {
        myHandler.sendEmptyMessage(-500);
        return;
      }
      if (2 == index) {
        refreType = false;
        myHandler.sendEmptyMessage(3);
      } else {
        myHandler.sendEmptyMessage(2);
      }
      myHandler.sendEmptyMessage(0);

      super.onPostExecute(result);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
      // TODO Auto-generated method stub
      super.onProgressUpdate(values);
    }

  }

  /** 异步任务 用来后台获取数据 */
  class MyAsynTask2 extends AsyncTask<Integer, Integer, String> {
    String error = "0";

    @Override
    protected String doInBackground(Integer... params) {
      switch (params[0]) {
      case 1:
        error = getBallData();
        if (error.equals("0")) {
          error = "100";
        }
        break;
      case 2:
        error = getBasketball();
        if (error.equals("0")) {
          error = "110";
        }
        break;
      default:
        break;
      }
      return error;
    }

    @Override
    protected void onPostExecute(String result) {
      // TODO Auto-generated method stub
      if ("-500".equals(result)) {
        myHandler.sendEmptyMessage(-500);
        return;
      }
      if (result.equals("100")) {
        // 请求篮球完成
        myHandler.sendEmptyMessage(100);
      } else if (result.equals("110")) {
        // 请求篮球完成
        myHandler.sendEmptyMessage(110);
      }
      super.onPostExecute(result);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
      // TODO Auto-generated method stub
      super.onProgressUpdate(values);

    }

  }

  @Override
  public void onScroll(AbsListView view, int firstVisibleItem,
      int visibleItemCount, int totalItemCount) {
    // list_lottery.setFirstItemIndex(firstVisibleItem);
  }

  @Override
  public void onScrollStateChanged(AbsListView view, int scrollState) {
    // TODO Auto-generated method stub
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.btn_luckyBtn:
      ToLuckActivity();
      break;
    }
  }

  @Override
  public void onResume() {
    // TODO Auto-generated method stub
    update();
    super.onResume();
  }

  /** 跳到幸运数字页面 */
  private void ToLuckActivity() {
    intent = new Intent(context, LuckNumberActivity.class);
    context.startActivity(intent);
  }

  /** 处理页面显示的 */
  @SuppressLint("HandlerLeak")
  class MyHandler extends Handler {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
      case 0:
        update();
        break;
      case 1:
        /** 是否第一次进入程序 不是的话就自动登录 */
        if (AppTools.index <= 1) {
          AppTools.isShow = false;
          ((View) (MainActivity.ll_main)).setAnimation(AnimationUtils
              .loadAnimation(context, android.R.anim.fade_out));
          AppTools.index++;
          index = 2;
          myAsynTask = new MyAsynTask();
          myAsynTask.execute(index);
        } else {
          index = 1;
        }
        gvAdapter.notifyDataSetChanged();
        MainActivity.ll_main.setVisibility(View.GONE);
        // list_lottery.setOnItemClickListener(new
        // ItemsClickListener());
        break;
      case 2:
        index++;
        // list_lottery.onRefreshComplete();
        // adapter.notifyDataSetChanged();
        if (index < 3) {
          myAsynTask = new MyAsynTask();
          myAsynTask.execute(index);
        }
        break;
      case 3:
        if (HallFragment.Message == "" || HallFragment.Message == null) {
          System.out.println("---555-");
          MyAsynTask3 myAsynTask3 = new MyAsynTask3();
          myAsynTask3.execute();
        }
        if (AppTools.index <= 1) {
          AppTools.isShow = false;
        }
        AppTools.index++;
        MainActivity.ll_main.setVisibility(View.GONE);
        // list_lottery.setOnItemClickListener(new
        // ItemsClickListener());
        index = 1;
        break;
      case 100:
        pd.dismiss();
        intent = new Intent(context, Select_jczqActivity.class);
        context.startActivity(intent);
        break;
      case 110:
        pd.dismiss();
        intent = new Intent(context, Select_jclqActivity.class);
        context.startActivity(intent);
        break;
      case -500:
        if (null != pd) {
          pd.dismiss();
        }
        MyToast.getToast(context, "连接超时，请手动刷新获得数据").show();
        myHandler.sendEmptyMessage(1);
        break;
      default:
        break;
      }
      super.handleMessage(msg);
    }
  }

  /** 得到竞彩 足球 数据 */
  public String getBallData() {
    if (refreType) {
      String key = MD5.md5(AppTools.MD5_key);
      /** 01 胜平负 02 猜比分 03 总进球 **/
      info = RspBodyBaseBean.changeLottery_info("72", "-1");

      Log.i("x", "拿足球的info   " + info);

      crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");

      auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");

      String[] values = { opt, auth, info };

      String result = HttpUtils.doPost(AppTools.names, values, AppTools.path);

      System.out.println("opt====" + opt);

      System.out.println("拿竞彩足球的数据---" + result);

      if ("-500".equals(result))
        return result;
      try {
        if (null != result) {
          JSONObject object = new JSONObject(result);
          String error = object.optString("error");
          if ("0".equals(error)) {
            String detail = object.optString("dtMatch");
            String detail_singlepass = object.optString("Singlepass");
            if (detail.length() == 0) {
              Log.i("x", "无数据");
            } else {
              // 拿到对阵信息组
              JSONArray array = new JSONArray(new JSONArray(detail).toString());

              List<List<DtMatch>> listMatch = new ArrayList<List<DtMatch>>();

              for (int i = 0; i < array.length(); i++) {
                JSONObject ob = array.getJSONObject(i);

                String arr = ob.optString("table1");
                String arr2 = ob.optString("table2");
                String arr3 = ob.optString("table3");

                // 判断对阵 是否有
                listMatch.add(setList(arr));
                listMatch.add(setList(arr2));
                listMatch.add(setList(arr3));

              }
              for (int i = 0; i < listLottery.size(); i++) {
                if ("72".equals(listLottery.get(i).getLotteryID())) {
                  // 拿到单关对阵信息组
                  JSONArray array_singlepass = new JSONArray(new JSONArray(
                      detail_singlepass).toString());
                  List<List<DtMatch>> listMatch_singlepass = new ArrayList<List<DtMatch>>();

                  for (int j = 0; j < array_singlepass.length(); j++) {
                    JSONObject ob = array_singlepass.getJSONObject(j);

                    String arr = ob.optString("table1");
                    if (!"".equals(arr)) {// 如果第一个不是空的
                      listMatch_singlepass.add(setList(arr));
                    }
                    String arr2 = ob.optString("table2");
                    if (!"".equals(arr2)) {// 如果第二个不是空的
                      listMatch_singlepass.add(setList(arr2));
                    }

                    String arr3 = ob.optString("table3");
                    if (!"".equals(arr3)) {// 如果第三个不是空的
                      listMatch_singlepass.add(setList(arr3));
                    }
                  }
                  listLottery.get(i).setList_singlepass_Matchs(
                      listMatch_singlepass);
                  listLottery.get(i).setList_Matchs(listMatch);
                  listLottery.get(i).setExplanation(
                      object.optString("explanation"));
                  AppTools.lottery = listLottery.get(i);
                }
              }
            }
          }
        } else {
          Log.i("x", "拿竞彩足球数据为空");
        }
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        System.out.println("错误" + e.getMessage());
      }

    }
    return "0";
  }

  /** 将对阵信息绑定 **/
  @SuppressLint("DefaultLocale")
  private List<DtMatch> setList(String arr) {
    List<DtMatch> list_m = new ArrayList<DtMatch>();
    if (arr.length() > 5) {
      JSONArray Arr;
      try {
        Arr = new JSONArray(arr);
        DtMatch dtmatch = null;
        for (int j = 0; j < Arr.length(); j++) {
          JSONObject item = Arr.getJSONObject(j);
          dtmatch = new DtMatch();
          dtmatch.setMatchId(item.optString("matchId"));
          dtmatch.setMatchNumber(item.optString("matchNumber"));
          dtmatch.setGame(item.getString("game"));
          dtmatch.setGuestTeam(item.optString("guestTeam"));
          dtmatch.setMainTeam(item.getString("mainTeam"));
          dtmatch.setStopSellTime(item.optString("stopSellTime"));
          dtmatch.setMainLoseBall(item.optInt("mainLoseBall"));
          dtmatch.setMatchWeek(item.optString("matchWeek"));
          /** 胜平负 */
          dtmatch.setSpfwin(item.optString("spfwin"));
          dtmatch.setSpfflat(item.optString("spfflat"));
          dtmatch.setSpflose(item.optString("spflose"));
          /** 让球胜平负 */
          dtmatch.setWin(item.optString("win"));
          dtmatch.setFlat(item.optString("flat"));
          dtmatch.setLose(item.optString("lose"));
          dtmatch.setMatchDate(item.optString("matchDate"));
          /** 猜比分 **/
          dtmatch.setSother(item.getString("sother"));
          dtmatch.setS10(item.optString("s10"));
          dtmatch.setS20(item.optString("s20"));
          dtmatch.setS21(item.optString("s21"));
          dtmatch.setS30(item.optString("s30"));
          dtmatch.setS31(item.optString("s31"));
          dtmatch.setS32(item.optString("s32"));
          dtmatch.setS40(item.optString("s40"));
          dtmatch.setS41(item.optString("s41"));
          dtmatch.setS42(item.optString("s42"));
          dtmatch.setS50(item.optString("s50"));
          dtmatch.setS51(item.optString("s51"));
          dtmatch.setS52(item.optString("s52"));
          dtmatch.setPother(item.getString("pother"));
          dtmatch.setP00(item.getString("p00"));
          dtmatch.setP11(item.getString("p11"));
          dtmatch.setP22(item.getString("p22"));
          dtmatch.setP33(item.getString("p33"));
          dtmatch.setFother(item.getString("fother"));
          dtmatch.setF01(item.getString("f01"));
          dtmatch.setF02(item.getString("f02"));
          dtmatch.setF12(item.getString("f12"));
          dtmatch.setF03(item.getString("f03"));
          dtmatch.setF13(item.getString("f13"));
          dtmatch.setF23(item.getString("f23"));
          dtmatch.setF04(item.getString("f04"));
          dtmatch.setF14(item.getString("f14"));
          dtmatch.setF24(item.getString("f24"));
          dtmatch.setF05(item.getString("f05"));
          dtmatch.setF15(item.getString("f15"));
          dtmatch.setF25(item.getString("f25"));

          /** 半全场 **/
          dtmatch.setSs(item.getString("ss"));
          dtmatch.setSp(item.getString("sp"));
          dtmatch.setSf(item.getString("sf"));
          dtmatch.setPs(item.getString("ps"));
          dtmatch.setPp(item.getString("pp"));
          dtmatch.setPf(item.getString("pf"));
          dtmatch.setFs(item.getString("fs"));
          dtmatch.setFp(item.getString("fp"));
          dtmatch.setFf(item.getString("ff"));

          /** 总进球 **/
          dtmatch.setIn0(item.getString("in0"));
          dtmatch.setIn1(item.getString("in1"));
          dtmatch.setIn2(item.getString("in2"));
          dtmatch.setIn3(item.getString("in3"));
          dtmatch.setIn4(item.getString("in4"));
          dtmatch.setIn5(item.getString("in5"));
          dtmatch.setIn6(item.getString("in6"));
          dtmatch.setIn7(item.getString("in7"));

          dtmatch.setSPF(Boolean.parseBoolean(item.getString("isSPF")
              .toLowerCase()));
          dtmatch.setCBF(Boolean.parseBoolean(item.getString("isCBF")
              .toLowerCase()));
          dtmatch.setZJQ(Boolean.parseBoolean(item.getString("isZJQ")
              .toLowerCase()));
          dtmatch.setNewSPF(Boolean.parseBoolean(item.getString("isNewSPF")
              .toLowerCase()));
          dtmatch.setBQC(Boolean.parseBoolean(item.getString("isBQC")
              .toLowerCase()));

          list_m.add(dtmatch);
        }
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        Log.i("x", "错误" + e.getMessage());
      }
    }
    return list_m;

  }

  class MyAsynTask3 extends AsyncTask<Integer, Integer, String> {
    String error = "0";

    @Override
    protected String doInBackground(Integer... params) {

      String key = MD5.md5(AppTools.MD5_key);

      if (time == null) {
        time = RspBodyBaseBean.getTime();
      }
      if (imei == null) {
        imei = RspBodyBaseBean.getIMEI(context);
      }

      info = "{\"lotteryIds\":\"" + AppTools.lotteryIds + "\"}";

      crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");

      auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");

      String[] values = { "51", auth, info }; // 请求广播消息，opt=“51” info =
      // "5,6,7"

      String result = HttpUtils.doPost(AppTools.names, values, AppTools.path);

      System.out.println("----" + result);

      try {
        JSONObject json = new JSONObject(result);
        if (json.optString("error").equals("0")) {
          HallFragment.Message = json.optString("winInfo");
          return "0";
        }
      } catch (JSONException e) {
        // TODO Auto-generated catch block
      }
      return "-1";
    }

    @Override
    protected void onPostExecute(String result) {
      // TODO Auto-generated method stub
      super.onPostExecute(result);
      if (HallFragment.Message != null && !HallFragment.Message.equals("")) {
        rollTextView.setRollText(HallFragment.Message, 1);
      } else
        rollTextView.setRollText("----没有中奖信息----", 0);
    }
  }

  public void update() {
    if (null == gvAdapter) {
      if (null == gv_hall_lottry)
        return;
      gvAdapter = new GridView_HallLotteryAdapter(context, listLottery);
      gv_hall_lottry.setAdapter(gvAdapter);
    } else {
      gvAdapter.notifyDataSetChanged();
    }
  }

  @Override
  public void onHeaderRefresh(PullToRefreshView view) {
    // TODO Auto-generated method stub
    mPullToRefreshView.postDelayed(new Runnable() {
      @Override
      public void run() {
        if (NetWork.isConnect(context)) {
          refreType = true;
          myAsynTask = new MyAsynTask();
          myAsynTask.execute(1);
        } else {
          MyToast.getToast(context, "网络连接异常，请检查网络").show();
        }
        mPullToRefreshView.onHeaderRefreshComplete();
      }
    }, 1000);
  }

}
