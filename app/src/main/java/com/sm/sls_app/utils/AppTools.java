package com.sm.sls_app.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ParseException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.AppObject;
import com.sm.sls_app.dataaccess.Lottery;
import com.sm.sls_app.dataaccess.SelectedNumbers;
import com.sm.sls_app.dataaccess.Users;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.fragment.HallFragment;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * App的 工具类
 * 
 * @author SLS003
 */
public class AppTools {

  private static final String TAG = "AppTools";

  public static String version;
  // 用户
  public static Users user = null;

  public static AppObject appobject = new AppObject();

  public static boolean isShow = true;

  public static String imei = "";

  public static int index = 1; // 第几次进入主界面
  public static int schemeId; // 方案id

  public final static String MD5_key = "Q56GtyNkop97H334TtyturfgErvvv98a";

  // public static Typeface aFont; // 字体
  // Typeface.createFromFile("/mnt/sdcard/kai.ttf");

  public static long totalCount; // 用户所选总注数
  public static List<Integer> beiList;// 用户倍投集合
  public static List<SelectedNumbers> list_numbers; // 用户所投的集合

  public static Lottery lottery; // 彩种对象
  public static int bei = 1;
  public static int qi = 1; // 追多少期
  public static String type; // 竞彩过关类型
  public static String ball; // 竞彩过关数据---
  public static boolean isCanBet = true; // 高频彩是否能投注
  public static String followCommissionScale = ""; // 合买佣金比列
  public static String followLeastBuyScale = ""; // 合买最少购买比列

  // 请求路径
  /** 演示站点 **/

//  public final static String path = "http://www.lgw111.com/ajax/AppGateway.ashx";
  public final static String path = "http://www.raj91.cn/ajax/AppGateway.ashx";


  public final static String zPath = "http://www.lgw111.com/Home/Room/OnlinePay/AlipayApp/Trade.aspx";
  public final static String ylPath = "http://www.lgw111.com/Home/Room/OnlinePay/YLAPP/purchase.aspx";
  // public final static String zPath =
  // "http://52.test.shovesoft.net/Home/Room/OnlinePay/AlipayApp/Trade.aspx";

  // public final static String path =
  // "http://sls52.demo.shovesoft.net/ajax/AppGateway.ashx";
  // public final static String zPath =
  // "http://sls52.demo.shovesoft.net/Home/Room/OnlinePay/AlipayApp/Trade.aspx";
  // public final static String ylPath =
  // "http://sls52.demo.shovesoft.net/Home/Room/OnlinePay/YLAPP/purchase.aspx";

  // public final static String path =
  // "http://www.666caip.com/ajax/AppGateway.ashx";
  // public final static String zPath =
  // "http://www.666caip.com/Home/Room/OnlinePay/AlipayApp/Trade.aspx";
  // public final static String zPath =
  // "http://wyf.test.shovesoft.net/Home/Room/OnlinePay/AlipayApp/Trade.aspx";

  // public final static String path =
  // "http://sls52.demo.shovesoft.net/ajax/AppGateway.ashx";
  // public final static String zPath
  // ="http://sls52.demo.shovesoft.net/Home/Room/OnlinePay/AlipayApp/Trade.aspx";

  public final static String server_url = "http://www.lgw111.com/x.htm";
  public static String serverTime = "";
  public final static String[] names = { "opt", "auth", "info" };
  // 标准版JC - 29时时彩
  // public final static String lotteryIds =
  // "6,62,70,28,13,5,3,39,72,73,83,63,64,74,75,78";
  public final static String lotteryIds = "6,28,70,62,78,72,73,5,39,63,64,61,92,83,94,66,93";
  // public final static String lotteryIds =
  // "6,62,70,28,5,39,72,73,83,63,64,78";

  // 彩种ID 对应 的 图片
  public static Map<String, Integer> allLotteryLogo = null;
  // 彩种ID 对应的 名称
  public static Map<String, String> allLotteryName = null;

  public static long sum_Income_Money = 0; // 总收入
  public static long sum_Expense_Money = 0; // 总支出
  public static long sum_Bonus_Money = 0; // 中奖

  public static boolean isVibrator;
  public static boolean isSensor;

  public static final int BASE_ID = 0;
  public static final int RQF_PAY = BASE_ID + 1;
  public static final int RQF_INSTALL_CHECK = RQF_PAY + 1;
  public static final String VERSION = "version";
  public static final String partner = "partner";

  public static final String action = "action";
  public static final String actionUpdate = "update";
  public static final String data = "data";
  public static final String platform = "platform";

  public static final int Lottery_ALL = 1; // 个人中心 查看类型 为 全部
  public static final int Lottery_WIN = 2; // 个人中心 查看类型 为 中奖
  public static final int Lottery_WAIT = 3; // 个人中心 查看类型 为 待开奖
  public static final int Lottery_FOLLOW = 4; // 个人中心 查看类型 为 追号
  public static final int Lottery_CHIPPED = 5; // 个人中心 查看类型 为 合买

  public static final int ERROR_SUCCESS = 0;
  public static final int ERROR_UNLONGIN = -100;
  public static final int ERROR_TOTAL = -102;
  public static final int ERROR_MONEY = -134;

  /** 绑定银行信息的类型 **/
  public static final int BANK_TYPE = 1;
  public static final int PROVINCE_TYPE = 2;
  public static final int CITY_TYPE = 3;
  public static final int ZHI_TYPE = 4;
  public static final int QUESTION_TYPE = 5;
  public static final int QUESTION_TYPE2 = 6;
  /** 绑定支付宝问题 */
  public static final int ALIPAY_TYPE = 7;
  /** 支付宝提现问题 */
  public static final int ALIPAY_WITHDRAWAL_TYPE = 8;

  public static ArrayList<Integer> level_star_list = null;
  public static ArrayList<Integer> level_medal_list = null;
  public static ArrayList<Integer> level_cup_list = null;
  public static ArrayList<Integer> level_crown_list = null;
  /** 登录标志位 */
  public static boolean flag = false;

  /** 设置ListView 的高度 */
  public static void setHight(BaseAdapter adapter, ListView listView) {
    int totalHeight = 0;
    for (int i = 0, len = adapter.getCount(); i < len; i++) {
      // listAdapter.getCount()返回数据项的数目
      View listItem = adapter.getView(i, null, listView);
      listItem.measure(0, 0); // 计算子项View 的宽高
      totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
    }

    ViewGroup.LayoutParams params = listView.getLayoutParams();
    // listView.getDividerHeight()获取子项间分隔符占用的高度
    params.height = totalHeight
        + (listView.getDividerHeight() * (adapter.getCount() - 1));
    // params.height最后得到整个ListView完整显示需要的高度
    listView.setLayoutParams(params);
  }

  /**
   * 设置战绩等级图片
   */
  public static void setLevelList() {
    level_star_list = new ArrayList<Integer>();
    int[] star = { R.drawable.star_1, R.drawable.star_2, R.drawable.star_3,
        R.drawable.star_4, R.drawable.star_5, R.drawable.star_6,
        R.drawable.star_7, R.drawable.star_8, R.drawable.star_9 };
    for (int i = 0; i < star.length; i++) {
      level_star_list.add(star[i]);
    }
    level_medal_list = new ArrayList<Integer>();
    int[] medal = { R.drawable.medal_1, R.drawable.medal_2, R.drawable.medal_3,
        R.drawable.medal_4, R.drawable.medal_5, R.drawable.medal_6,
        R.drawable.medal_7, R.drawable.medal_8, R.drawable.medal_9 };
    for (int i = 0; i < medal.length; i++) {
      level_medal_list.add(medal[i]);
    }
    level_cup_list = new ArrayList<Integer>();
    int[] cup = { R.drawable.cup_1, R.drawable.cup_2, R.drawable.cup_3,
        R.drawable.cup_4, R.drawable.cup_5, R.drawable.cup_6, R.drawable.cup_7,
        R.drawable.cup_8, R.drawable.cup_9 };
    for (int i = 0; i < cup.length; i++) {
      level_cup_list.add(cup[i]);
    }
    level_crown_list = new ArrayList<Integer>();
    int[] crown = { R.drawable.crown_1, R.drawable.crown_2, R.drawable.crown_3,
        R.drawable.crown_4, R.drawable.crown_5, R.drawable.crown_6,
        R.drawable.crown_7, R.drawable.crown_8, R.drawable.crown_9 };
    for (int i = 0; i < crown.length; i++) {
      level_crown_list.add(crown[i]);
    }
  }

  public static void setLogo() {
    allLotteryLogo = new LinkedHashMap<String, Integer>();
    /** 双色球 **/
    AppTools.allLotteryLogo.put("5", R.drawable.log_lottery_ssq);
    /** 大乐透 **/
    AppTools.allLotteryLogo.put("39", R.drawable.log_lottery_dlt);
    /** 江西11选5 **/
    AppTools.allLotteryLogo.put("70", R.drawable.log_lottery_11x5);
    /** 重庆时时彩 **/
    AppTools.allLotteryLogo.put("28", R.drawable.log_lottery_ssc);
    /** 河内时时彩 */
    AppTools.allLotteryLogo.put("92", R.drawable.log_lottery_ssc);
    /** 江西时时彩 **/
    AppTools.allLotteryLogo.put("61", R.drawable.log_lottery_ssc);
    /** 新疆时时彩 **/
    AppTools.allLotteryLogo.put("66", R.drawable.log_lottery_ssc);
    /** 印尼时时彩 **/
    AppTools.allLotteryLogo.put("93", R.drawable.log_lottery_ssc);
    /** 竞彩足球 **/
    AppTools.allLotteryLogo.put("72", R.drawable.log_lottery_jczq);
    /** 竞彩篮球 **/
    AppTools.allLotteryLogo.put("73", R.drawable.log_lottery_jclq);
    /** 江苏快3 **/
    AppTools.allLotteryLogo.put("83", R.drawable.log_lottery_k3);
    /** 3D **/
    AppTools.allLotteryLogo.put("6", R.drawable.log_lottery_3d);
    // /** 胜负彩 **/
    // AppTools.allLotteryLogo.put("74", R.drawable.log_lottery_sfc);
    // /** 任选九 **/
    // AppTools.allLotteryLogo.put("75", R.drawable.log_lottery_rx9);
    /** 排列三 **/
    AppTools.allLotteryLogo.put("63", R.drawable.log_lottery_pl3);
    /** 排列五 **/
    AppTools.allLotteryLogo.put("64", R.drawable.log_lottery_pl5);
    /** 七星彩 **/
    // AppTools.allLotteryLogo.put("3", R.drawable.log_lottery_qxc);
    // /** 七乐彩 **/
    // AppTools.allLotteryLogo.put("13", R.drawable.log_lottery_qlc);
    /** 十一运夺金 **/
    AppTools.allLotteryLogo.put("62", R.drawable.log_lottery_syydj);
    /** 广东11选5 **/
    AppTools.allLotteryLogo.put("78", R.drawable.log_lottery_gd11x5);
    /** 北京PK10 */
    AppTools.allLotteryLogo.put("94", R.drawable.log_lottery_pk10);
    addlottery();
  }

  /* 要求的彩种的几种排序方式 */
  public static void addlottery() {
    allLotteryName = new LinkedHashMap<String, String>();
    switch (appobject.getSort()) {
    case 1:
      allLotteryName.put("双色球", "5");
      allLotteryName.put("竞彩足球", "72");
      allLotteryName.put("大乐透", "39");
      allLotteryName.put("竞彩篮球", "73");
      allLotteryName.put("3D", "6");
      allLotteryName.put("排列三", "63");
      allLotteryName.put("七星彩", "3");
      allLotteryName.put("七乐彩", "13");
      allLotteryName.put("排列五", "64");
      allLotteryName.put("胜负彩", "74");
      allLotteryName.put("任选九", "75");
      allLotteryName.put("时时彩", "28");
      allLotteryName.put("十一运夺金", "62");
      allLotteryName.put("11选5", "70");
      allLotteryName.put("江苏快3", "83");
      break;
    case 2:
      allLotteryName.put("竞彩足球", "72");
      allLotteryName.put("双色球", "5");
      allLotteryName.put("3D", "6");
      allLotteryName.put("大乐透", "39");
      allLotteryName.put("竞彩篮球", "73");
      allLotteryName.put("排列三", "63");
      allLotteryName.put("胜负彩", "74");
      allLotteryName.put("任选九", "75");
      allLotteryName.put("七星彩", "3");
      allLotteryName.put("七乐彩", "13");
      allLotteryName.put("排列五", "64");
      allLotteryName.put("时时彩", "28");
      allLotteryName.put("十一运夺金", "62");
      allLotteryName.put("11选5", "70");
      allLotteryName.put("江苏快3", "83");
      allLotteryName.put("11选5", "70");
      allLotteryName.put("江苏快3", "83");
      allLotteryName.put("11选5", "70");
      allLotteryName.put("江苏快3", "83");
      break;
    case 3:
      allLotteryName.put("重庆时时彩", "28");
      allLotteryName.put("新疆时时彩", "66");
      allLotteryName.put("河内时时彩", "92");
      allLotteryName.put("印尼时时彩", "93");
      allLotteryName.put("江苏快3", "83");
      allLotteryName.put("北京PK10", "94");
      allLotteryName.put("江西11选5", "70");
      allLotteryName.put("十一运夺金", "62");
      allLotteryName.put("广东11选5", "78");
      allLotteryName.put("3D", "6");
      allLotteryName.put("排列三", "63");
      allLotteryName.put("排列五", "64");
      allLotteryName.put("竞彩足球", "72");
      allLotteryName.put("竞彩篮球", "73");
      allLotteryName.put("双色球", "5");
      allLotteryName.put("大乐透", "39");

      // allLotteryName.put("七星彩", "3");
      // allLotteryName.put("七乐彩", "13");
      // allLotteryName.put("胜负彩", "74");
      // allLotteryName.put("任选九", "75");
      break;
    default:
      break;
    }
  }

  /** 将Set进行排序 */
  public static List<String> sortSet(Set<String> set) {
    List<String> list = new ArrayList<String>();
    list.addAll(set);
    Collections.sort(list);
    return list;
  }

  /** 将值转换成 毫秒 */
  @SuppressLint("SimpleDateFormat")
  public static long getTimestamp(String date) throws ParseException,
      java.text.ParseException {
    Log.v("OpUtil", "getTimestamp the date is :" + date);
    Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
    long l = date1.getTime();
    return l;
    // return l / 1000;
  }

  /** 根据年月 得到当月的共有几天 */
  public static int getLastDay(int year, int month) {
    Calendar time = Calendar.getInstance();
    time.clear();
    time.set(Calendar.YEAR, year); // year 为 int
    time.set(Calendar.MONTH, month - 1);// 注意,Calendar对象默认一月为0
    int day = time.getActualMaximum(Calendar.DAY_OF_MONTH);// 本月份的天数
    return day;
  }

  /** 传入一个毫秒类型转成 mm:ss 的类型的字符串 */
  public static String formatDuring(long mss) {
    long days = mss / (1000 * 60 * 60 * 24);
    long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
    long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
    long seconds = (mss % (1000 * 60)) / 1000;
    String showDays = "0";
    String showHour = "0";
    String showMinutes = "0";
    String showSeconds = "0";

    if (days < 10) {
      showDays = "0" + days;
    } else {
      showDays = days + "";
    }
    if (hours < 10) {
      showHour = "0" + hours;
    } else {
      showHour = "" + hours;
    }
    if (minutes < 10) {
      showMinutes = "0" + minutes;
    } else {
      showMinutes = "" + minutes;
    }
    if (seconds < 10) {
      showSeconds = "0" + seconds;
    } else {
      showSeconds = "" + seconds;
    }

    if (mss > 1000 * 60 * 60 * 24)
      return showDays + "天" + showHour + "时";
    else if (mss > 1000 * 60 * 60)
      return showHour + "时" + showMinutes + "分";
    else if (mss > 1000 * 60)
      return showMinutes + "分" + showHour + "秒";
    else
      return showMinutes + "分" + showSeconds + "秒";
  }

  public static String getDate(String lotteryId, Context context) {
    String key = MD5.md5(AppTools.MD5_key);
    String time = RspBodyBaseBean.getTime();
    String info = RspBodyBaseBean.changeLottery_info(lotteryId);
    Log.i("x", "拿普通Info====   " + info);

    if (imei.length() == 0)
      imei = RspBodyBaseBean.getIMEI(context);

    String crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");

    String auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");

    String[] values = { "10", auth, info };
    System.out.println("=========auth==" + auth);

    String result = HttpUtils.doPost(AppTools.names, values, AppTools.path);

    Log.i("x", "普通result---" + result);

    if ("-500".equals(result))
      return result;

    if (null == result)
      return "-1001";
    try {
      JSONObject isusesInfo = new JSONObject(result);
      if (null != isusesInfo) {
        AppTools.serverTime = isusesInfo.optString("serverTime");

        JSONArray array = new JSONArray(isusesInfo.getString("isusesInfo"));

        for (int i = 0; i < array.length(); i++) {
          if (array.get(i).toString().length() < 5) {
            continue;
          }
          JSONObject object = array.getJSONObject(i);
          Log.i("x", "普通彩票详情 item :" + object.toString());

          if (null != object) {
            for (int j = 0; j < HallFragment.listLottery.size(); j++) {
              if (HallFragment.listLottery.get(j).getLotteryID()
                  .equals(object.optString("lotteryid"))) {
                // 绑定值
                HallFragment.listLottery.get(j).setLotteryName(
                    HallFragment.listLottery.get(j).getLotteryName());
                HallFragment.listLottery.get(j).setIsuseId(
                    object.optString("isuseId"));
                HallFragment.listLottery.get(j).setLotteryDescription(
                    object.optString("Description"));
                HallFragment.listLottery.get(j).setLotteryAgainst(
                    object.optString("Against"));
                HallFragment.listLottery.get(j).setIsuseName(
                    object.optString("isuseName"));
                HallFragment.listLottery.get(j).setStarttime(
                    object.optString("starttime"));
                HallFragment.listLottery.get(j).setEndtime(
                    object.optString("endtime"));
                HallFragment.listLottery.get(j).setLastIsuseName(
                    object.optString("lastIsuseName"));
                HallFragment.listLottery.get(j).setLastWinNumber(
                    object.optString("lastWinNumber"));
                HallFragment.listLottery.get(j).setOriginalTime(
                    object.optString("originalTime"));
                HallFragment.listLottery.get(j).setExplanation(
                    object.optString("explanation"));
                long nowTime = 0;
                try {
                  long endtime = AppTools.getTimestamp(HallFragment.listLottery
                      .get(j).getEndtime());
                  // long originalTime = AppTools
                  // .getTimestamp(HallFragment.listLottery
                  // .get(j).getOriginalTime());

                  long se = AppTools.getTimestamp(AppTools.serverTime);

                  nowTime = System.currentTimeMillis();

                  HallFragment.listLottery.get(j).setDistanceTime(
                      endtime - se + nowTime);
                  // HallFragment.listLottery
                  // .get(j)
                  // .setDistanceTime2(
                  // originalTime - se + nowTime);
                } catch (Exception ex) {
                  HallFragment.listLottery.get(j).setDistanceTime(0);
                  Log.i("x", "拿倒计时报错" + ex.getMessage());
                  ex.printStackTrace();
                }
                long l = HallFragment.listLottery.get(j).getDistanceTime()
                    - nowTime;

                if (HallFragment.listLottery.get(j).getLotteryID().equals("28"))
                  Log.i("x", HallFragment.listLottery.get(j).getLotteryName()
                      + "=====倒计时=   " + l);

                String dtCanChase = object.optString("dtCanChaseIsuses");

                List<String> dt = new ArrayList<String>();
                List<String> it = new ArrayList<String>();
                if (dtCanChase.length() != 0) {
                  JSONArray arr = new JSONArray(dtCanChase);

                  for (int k = 0; k < arr.length(); k++) {
                    JSONObject ob = arr.getJSONObject(k);
                    dt.add(ob.optString("isuseId"));
                    it.add(ob.optString("isuseName"));
                  }
                }

                String detail = object.optString("dtMatch");
                System.out.println("dtmatch==========" + detail);
                JSONArray array2 = new JSONArray(
                    new JSONArray(detail).toString());
                if (array2 != null && array2.length() != 0) {
                  HallFragment.listLottery.get(j).setDtmatch(
                      array2.getJSONObject(0).optString("mainTeam") + "vs"
                          + array2.getJSONObject(0).optString("guestTeam"));
                }
                HallFragment.listLottery.get(j).setDtCanChaseIsuses(dt);
                HallFragment.listLottery.get(j).setIsuseNameList(it);
              }
            }
          }
        }
      }

    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      Log.d("HallActivity", "大厅得到数据出错：" + e.getMessage());
      return "-1001";
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      Log.d("HallActivity", "2大厅得到数据出错：" + e.getMessage());
      return "-1001";
    }
    setLotteryShow();
    return "0";
  }

  /** 自动登录 **/
  public static String doLogin(Context context) {
    SharedPreferences settings = context.getSharedPreferences("app_user", 0);
    boolean isLogin = false;
    String name = "";
    String pass = "";
    if (settings.contains("isLogin")) {
      isLogin = settings.getBoolean("isLogin", false);
    }
    if (settings.contains("name")) {
      name = settings.getString("name", null);
    }

    // 判断是否有存 用户名
    if (isLogin) {
      // 判断是否有存 密码
      if (settings.contains("pass")) {
        pass = settings.getString("pass", null);
      }
    }
    if (flag) {
      return AppTools.ERROR_SUCCESS + "";
    }
    if (null != pass && null != name && !"".equals(name) && !"".equals(pass)
        && name.length() != 0 && pass.length() != 0) {
      // 对密码进行MD5加密
      String pass2 = MD5.md5(pass + AppTools.MD5_key);

      String time = RspBodyBaseBean.getTime();
      if (imei.length() == 0)
        imei = RspBodyBaseBean.getIMEI(context);
      String opt = "2";
      String info = RspBodyBaseBean.changeLogin_Info(name, pass2);
      String auth = RspBodyBaseBean.changeLogin_Auth(RspBodyBaseBean.getCrc(
          time, imei, MD5.md5(AppTools.MD5_key), info, "-1"), time, imei);

      String[] values = { opt, auth, info };

      String result = HttpUtils.doPost(AppTools.names, values, AppTools.path);

      if ("-500".equals(result))
        return "-500";

      Log.i("x", "login==" + result);
      try {
        JSONObject item = new JSONObject(result);
        if ("0".equals(item.optString("error"))) {
          AppTools.user = new Users();
          AppTools.user.setUid(item.optString("uid"));
          AppTools.user.setName(item.optString("name"));
          AppTools.user.setRealityName(item.optString("realityName"));
          AppTools.user.setBalance(item.optDouble("balance"));
          AppTools.user.setFreeze(item.optDouble("freeze"));
          AppTools.user.setEmail(item.optString("email"));
          // AppTools.user.setIdcardnumber(item
          // .optString("idcardnumber"));
          AppTools.user.setRechargeKey(item.optString("rechargeKey"));
          AppTools.user.setMobile(item.optString("mobile"));
          AppTools.user.setMsgCount(item.optInt("msgCount"));
          AppTools.user.setMsgCountAll(item.optInt("msgCountAll"));
          AppTools.user.setScoring(item.optInt("scoring"));
          // 用户密码 （没加密的）
          AppTools.user.setUserPass(pass);
          flag = true;
          return AppTools.ERROR_SUCCESS + "";
        } else {
          Log.i("x", item.optString("msg"));
          return item.optString("error");
        }
      } catch (Exception ex) {
        Log.i("login", "登录异常---" + ex.getMessage());
        ex.printStackTrace();
        return "-110";
      }
    }
    /** 没有保存用户名或者密码 */
    else {
      return "-100";
    }
  }

  // "5,39,6,63,64,3,13,74,75";
  /** 设置显示的值 **/
  public static void setLotteryShow() {
    for (int j = 0; j < HallFragment.listLottery.size(); j++) {
      if (HallFragment.listLottery.get(j).getExplanation() != null
          && !HallFragment.listLottery.get(j).getExplanation().equals("")) {
        HallFragment.listLottery.get(j).setExplanation(
            HallFragment.listLottery.get(j).getExplanation());
      } else {
        switch (Integer
            .parseInt(HallFragment.listLottery.get(j).getLotteryID())) {
        case 5:
          HallFragment.listLottery.get(j).setExplanation("奖池2530万");
          break;
        case 39:
          HallFragment.listLottery.get(j).setExplanation("奖池2530万");
          break;
        case 83:
          HallFragment.listLottery.get(j).setExplanation("2元赢取￥240元");
          break;
        case 6:
          HallFragment.listLottery.get(j).setExplanation("简单3位赢千元");
          break;
        case 63:
          HallFragment.listLottery.get(j).setExplanation("3位数字赢千元");
          break;
        case 64:
          HallFragment.listLottery.get(j).setExplanation("2元赢取10万元");
          break;
        case 3:
          HallFragment.listLottery.get(j).setExplanation("奖池2530万");
          break;
        case 13:
          HallFragment.listLottery.get(j).setExplanation("30选7赢百万");
          break;
        case 74:
          HallFragment.listLottery.get(j).setExplanation("猜胜负赢500万");
          break;
        case 75:
          HallFragment.listLottery.get(j).setExplanation("猜胜负赢500万");
          break;
        case 72:
          HallFragment.listLottery.get(j).setExplanation("2串1倍投收益高");
          break;
        case 73:
          HallFragment.listLottery.get(j).setExplanation("玩2串1易中奖");
          break;
        case 28:
        case 92:
        case 66:
          HallFragment.listLottery.get(j).setExplanation("每天288期 5分钟一期");
          break;
        case 62:
        case 82:
        case 68:
          HallFragment.listLottery.get(j).setExplanation("每天78期  10分钟一期");
          break;
        case 69:
          HallFragment.listLottery.get(j).setExplanation("每日20:30开奖");
        case 87:
        case 70:
        case 78:// 广东11选5
        case 61:
        case 93:
          HallFragment.listLottery.get(j).setExplanation("每天84期   10分钟一期");
          break;
        default:
          break;
        }
      }
    }
  }

  public static float getDpi(Activity context) {
    DisplayMetrics metric = new DisplayMetrics();
    context.getWindowManager().getDefaultDisplay().getMetrics(metric);

    int width = metric.widthPixels; // 宽度（PX）
    int height = metric.heightPixels; // 高度（PX）

    float density = metric.density; // 密度（0.75 / 1.0 / 1.5）
    int densityDpi = metric.densityDpi; // 密度DPI（120 / 160 / 240）
    return density;
  }

  /**
   * 获取版本名称
   * 
   * @param context
   * @return
   */
  public static String getVerName(Context context) {
    String verName = "";
    try {
      verName = context.getPackageManager().getPackageInfo(
          "com.sm.sls_app.activity", 0).versionName;
      Log.i("版本号--->", verName);
    } catch (NameNotFoundException e) {
      Log.e("erro", e.getMessage());
    }
    return verName;
  }

  /**
   * 改变字体的颜色
   * 
   * @param color
   *          字体颜色
   * @param content
   *          内容
   * @return
   */
  public static String changeStringColor(String color, String content) {
    return "<font color='" + color + "'>" + content + "</FONT>";
  }
}
