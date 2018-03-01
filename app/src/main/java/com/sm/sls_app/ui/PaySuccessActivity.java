package com.sm.sls_app.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.Lottery;
import com.sm.sls_app.dataaccess.LotteryContent;
import com.sm.sls_app.dataaccess.Schemes;
import com.sm.sls_app.dataaccess.ShowDtMatch;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.fragment.HallFragment;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.BaseHelper;
import com.sm.sls_app.utils.FileUtils;
import com.sm.sls_app.view.MyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PaySuccessActivity extends Activity implements OnClickListener {
  private static final String TAG = "PaySuccessActivity";
  private Context context = PaySuccessActivity.this;
  private TextView tv_tip;// 提示
  private Button btn_select_schemeinfo;// 订单详情
  private Button btn_continue_bet;// 继续投注
  private Button btn_back_tohall;// 返回首页
  private String lotteryId;// 彩种id
  private int schemeId;// 方案id
  private ProgressDialog pd;

  private MyHandler myHandler;
  private MyAsynTask myAsynTask;
  private String opt, auth, info, time, imei, crc, key; // 格式化后的参数

  private long paymoney;// 花费的钱
  private String balance;// 余额
  Schemes scheme;
  /**
   * 竞彩
   */
  private List<ShowDtMatch> list_show;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_paysuc);
    App.activityS.add(this);
    findView();
    setListener();
    init();
  }

  public void findView() {
    tv_tip = (TextView) findViewById(R.id.tv_tip);
    btn_select_schemeinfo = (Button) findViewById(R.id.btn_select_schemeinfo);
    btn_continue_bet = (Button) findViewById(R.id.btn_continue_bet);
    btn_back_tohall = (Button) findViewById(R.id.btn_back_tohall);
    scheme = new Schemes();
  }

  public void setListener() {
    btn_select_schemeinfo.setOnClickListener(this);
    btn_continue_bet.setOnClickListener(this);
    btn_back_tohall.setOnClickListener(this);
  }

  public void init() {
    myHandler = new MyHandler();
    lotteryId = AppTools.lottery.getLotteryID();// 获取彩种id
    schemeId = AppTools.schemeId;// 获取方案id
    paymoney = getIntent().getLongExtra("paymoney", 0);// 获取花费钱
    balance = AppTools.user.getBalance();// 获取方案id
    Spanned text = Html.fromHtml("您共花费 "
        + AppTools.changeStringColor("#e3393c", paymoney + "") + " 元，余额为 "
        + AppTools.changeStringColor("#e3393c", balance) + " 元");
    tv_tip.setText(text);
  }

  @Override
  public void onClick(View v) {
    // TODO Auto-generated method stub
    int id = v.getId();
    switch (id) {
    case R.id.btn_select_schemeinfo:// 订单详情
      pd = BaseHelper.showProgress(PaySuccessActivity.this, "", "加载中...", true,
          false);
      Log.i(TAG, "期号" + AppTools.lottery.getIsuseId());
      Log.i(TAG, "彩种id" + AppTools.lottery.getLotteryID());
      if (AppTools.qi > 1) {// 追号
        AppTools.lottery.setIsChase(1);
      } else {
        AppTools.lottery.setIsChase(0);
      }
      if (TextUtils.isEmpty(AppTools.lottery.getLotteryID())) {
        MyToast.getToast(context, "LotteryID为空").show();
        return;
      }
      if (TextUtils.isEmpty(AppTools.lottery.getChaseTaskID() + "")) {
        MyToast.getToast(context, "ChaseTaskID为空").show();
        return;
      }
      scheme.setIsuseID(AppTools.lottery.getIsuseId());
      scheme.setIsuseName(AppTools.lottery.getIsuseName());
      scheme.setLotteryID(AppTools.lottery.getLotteryID());
      scheme.setLotteryName(AppTools.lottery.getLotteryName());
      scheme.setIsChase(AppTools.lottery.getIsChase());
      scheme.setMultiple(AppTools.bei);
      scheme.setChaseTaskID(AppTools.lottery.getChaseTaskID());
      myAsynTask = new MyAsynTask();
      myAsynTask.execute();

      break;
    case R.id.btn_continue_bet:// 继续投注
      goToSelectLottery();
      break;
    case R.id.btn_back_tohall:// 返回首页
      backToHall();
      break;
    }
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    // TODO Auto-generated method stub
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      backToHall();
    }
    return super.onKeyDown(keyCode, event);
  }

  /**
   * 异步任务
   */
  class MyAsynTask extends AsyncTask<Void, Integer, String> {

    /**
     * 在后台执行的程序
     */
    @Override
    protected String doInBackground(Void... params) {
      info = "{\"id\":" + schemeId + "}";
      opt = "15";
      Log.i("x", "投注接口 opt :" + opt);
      Log.i("x", "投注方案详情  info  " + info);

      crc = RspBodyBaseBean.getCrc(time, imei, key, info,
          AppTools.user.getUid());
      auth = RspBodyBaseBean.getAuth(crc, time, imei, AppTools.user.getUid());
      String[] values = { opt, auth, info };
      String result = HttpUtils.doPost(AppTools.names, values, AppTools.path);

      Log.i("x", "得到投注内容   " + result);

      if ("-500".equals(result))
        return result;

      if (null == result)
        return "1";

      try {

        JSONObject item = new JSONObject(result);

        if ("0".equals(item.optString("error"))) {
          String schemeList = item.optString("schemeList");

          JSONArray array = new JSONArray(schemeList);

          JSONArray jsonArray2 = new JSONArray(array.toString());

          if (jsonArray2.length() == 0)
            return "1";
          // 如果取消了 则停止
          if (this.isCancelled()) {
            Log.i("x", "取消了异步。。。。");
            return null;
          }
          JSONObject items2 = jsonArray2.getJSONObject(0);
          Log.i("x", "items to string ==== " + items2.toString());
          scheme.setId(items2.optString("Id"));
          // 设置订单编号
          String schemeNum = items2.optString("schemeNumber");
          scheme
              .setSchemeNumber(schemeNum.equals("") ? FileUtils.getSchemeNum(
                  lotteryId, AppTools.lottery.getIsuseName(), schemeId)
                  : schemeNum);

          scheme.setAssureMoney(items2.optDouble("assureMoney"));
          scheme.setAssureShare(items2.optInt("assureShare"));
          scheme.setBuyed(items2.optString("buyed"));
          scheme.setInitiateName(items2.optString("initiateName"));
          scheme.setInitiateUserID(items2.optString("initiateUserID"));
          scheme.setIsPurchasing(items2.optString("isPurchasing"));
          // 设置订单期号
          scheme.setIsuseID(AppTools.lottery.getIsuseId());
          scheme.setIsuseName(AppTools.lottery.getIsuseName());

          scheme.setLevel(items2.optInt("level"));
          scheme.setLotteryID(items2.optString("LotteryID"));
          scheme.setLotteryName(items2.optString("lotteryName"));
          scheme.setLotteryNumber(items2.optString("lotteryNumber"));
          scheme.setMoney(items2.optInt("money"));
          scheme.setPlayTypeID(items2.optInt("playTypeID"));
          scheme.setPlayTypeName(items2.optString("playTypeName"));
          scheme.setQuashStatus(items2.optInt("quashStatus"));
          scheme.setRecordCount(items2.optInt("RecordCount"));
          scheme.setSchedule(items2.optInt("schedule"));
          scheme.setSchemeBonusScale(items2.optDouble("schemeBonusScale"));
          scheme.setSchemeIsOpened(items2.optString("schemeIsOpened"));
          scheme.setSecrecyLevel(items2.optInt("secrecyLevel"));
          scheme.setSerialNumber(items2.optInt("SerialNumber"));
          scheme.setShare(items2.optInt("share"));
          scheme.setShareMoney(items2.optInt("shareMoney"));
          scheme.setSurplusShare(items2.optInt("surplusShare"));
          scheme.setTitle(items2.optString("title"));
          scheme.setWinMoneyNoWithTax(items2.optInt("winMoneyNoWithTax"));
          scheme.setWinNumber(items2.optString("winNumber"));

          // 设置订单下单时间
          scheme
              .setDateTime(FileUtils.getSchemeTime(System.currentTimeMillis()));

          scheme.setDescription(items2.optString("description"));
          scheme.setIsChase(items2.optInt("isChase"));
          scheme.setChaseTaskID(items2.optInt("chaseTaskID"));
          // 设置倍数
          scheme.setMultiple(AppTools.bei);

          scheme.setFromClient(items2.optInt("FromClient"));
          scheme.setMyBuyMoney(items2.optInt("myBuyMoney") + "");
          scheme.setMyBuyShare(items2.optInt("myBuyShare"));

          JSONArray array_contents = new JSONArray(
              items2.optString("buyContent"));
          if (array_contents != null) {
            List<LotteryContent> contents = new ArrayList<LotteryContent>();
            LotteryContent lotteryContent = null;
            for (int k = 0; k < array_contents.length(); k++) {
              lotteryContent = new LotteryContent();
              try {
                JSONArray array2 = new JSONArray(array_contents.optString(k));

                lotteryContent.setLotteryNumber(array2.getJSONObject(0)
                    .optString("lotteryNumber"));
                lotteryContent.setPlayType(array2.getJSONObject(0).optString(
                    "playType"));
                lotteryContent.setSumMoney(array2.getJSONObject(0).optString(
                    "sumMoney"));
                lotteryContent.setSumNum(array2.getJSONObject(0).optString(
                    "sumNum"));
                contents.add(lotteryContent);
              } catch (Exception e) {
                JSONObject array2 = new JSONObject(array_contents.optString(k));

                lotteryContent.setLotteryNumber(array2
                    .optString("lotteryNumber"));
                lotteryContent.setPlayType(array2.optString("playType"));
                lotteryContent.setSumMoney(array2.optString("sumMoney"));
                lotteryContent.setSumNum(array2.optString("sumNum"));
                contents.add(lotteryContent);
              }
            }
            scheme.setContent_lists(contents);

          }
          if (this.isCancelled()) {
            pd.dismiss();
            Log.i("x", "取消了异步。。。。");
            return null;
          }
          return "0";
        }
      } catch (Exception e) {
        Log.i("x", "myAllLottery 错误" + e.getMessage());
      }

      return "-1";
    }

    @Override
    protected void onPostExecute(String result) {
      // TODO Auto-generated method stub
      if (null == result)
        return;
      if ("1" == result) {
        myHandler.sendEmptyMessage(-1);
      }
      myHandler.sendEmptyMessage(Integer.parseInt(result));
      super.onPostExecute(result);
    }
  }

  /**
   * 进行页面刷新
   */
  @SuppressLint("HandlerLeak")
  class MyHandler extends Handler {
    @Override
    public void handleMessage(Message msg) {
      if (pd != null) {
        pd.dismiss();
      }
      switch (msg.what) {
      case 0:
        Intent intent_info = null;
        if ("72".equals(scheme.getLotteryID())
            || "73".equals(scheme.getLotteryID()))
          intent_info = new Intent(context, MyCommonLotteryInfo_jc.class);
        else
          intent_info = new Intent(context, MyCommonLotteryInfo.class);
        intent_info.putExtra("scheme", scheme);
        Log.i("paysuccess", "是否合买=== " + scheme.getIsPurchasing());
        context.startActivity(intent_info);
        break;
      default:
        break;

      case -1:
        MyToast.getToast(getApplicationContext(), "没有数据").show();
        break;

      case -500:
        MyToast.getToast(getApplicationContext(), "连接超时").show();
        break;
      case 100:
        // List<List<DtMatch>> list_Matchs1 = AppTools.lottery
        // .getList_Matchs();
        backToHall();
        Intent intent = new Intent(PaySuccessActivity.this,
            Select_jczqActivity.class);
        PaySuccessActivity.this.startActivity(intent);
        break;
      case 110:
        // List<List<DtMatch>> list_Matchs2 = AppTools.lottery
        // .getList_Matchs();
        backToHall();
        intent = new Intent(PaySuccessActivity.this, Select_jclqActivity.class);
        PaySuccessActivity.this.startActivity(intent);
        break;
      }
      super.handleMessage(msg);
    }
  }

  /**
   * 返回首页
   */
  public void backToHall() {
    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
    this.startActivity(intent);
    this.finish();
  }

  /**
   * 根据彩种id跳转不同选号页面
   */
  private void goToSelectLottery() {
    Intent intent = null;
    int lotteryId = Integer.parseInt(this.lotteryId);
    switch (lotteryId) {
    case 5:// 双色球
      long currentTime_ssq = System.currentTimeMillis();
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(lotteryId + "")) {
          if (lottery.getDistanceTime() - currentTime_ssq <= 0) {
            MyToast.getToast(PaySuccessActivity.this, "该奖期已结束，请等下一期").show();
            return;
          }
        }
      }
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(lotteryId + "")) {
          AppTools.lottery = lottery;
        }
      }
      intent = new Intent(PaySuccessActivity.this, SelectNumberActivity.class);
      PaySuccessActivity.this.startActivity(intent);
      break;
    case 39:// 大乐透
      long currentTime_dlt = System.currentTimeMillis();
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(lotteryId + "")) {
          if (lottery.getDistanceTime() - currentTime_dlt <= 0) {
            MyToast.getToast(PaySuccessActivity.this, "该奖期已结束，请等下一期").show();
            return;
          }
        }
      }
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(lotteryId + "")) {
          AppTools.lottery = lottery;
        }
      }
      intent = new Intent(getApplicationContext(), Buy_DLT_Activit.class);
      PaySuccessActivity.this.startActivity(intent);
      break;
    case 6:// 3D
      long currentTime_3d = System.currentTimeMillis();
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(lotteryId + "")) {
          if (lottery.getDistanceTime() - currentTime_3d <= 0) {
            MyToast.getToast(PaySuccessActivity.this, "该奖期已结束，请等下一期").show();
            return;
          }
        }
      }
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(lotteryId + "")) {
          AppTools.lottery = lottery;
        }
      }
      intent = new Intent(getApplicationContext(),
          SelectNumberActivityFC3D.class);
      PaySuccessActivity.this.startActivity(intent);
      break;
    case 63:// 排列三
    case 64:// 排列五
      long currentTime = System.currentTimeMillis();
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(lotteryId + "")) {
          if (lottery.getDistanceTime() - currentTime <= 0) {
            MyToast.getToast(PaySuccessActivity.this, "该奖期已结束，请等下一期").show();
            return;
          }
        }
      }
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(lotteryId + "")) {
          AppTools.lottery = lottery;
        }
      }
      if (("64").equals(AppTools.lottery.getLotteryID())) {
        intent = new Intent(PaySuccessActivity.this,
            SelectNumberActivityPL5_QXC.class);
      } else {
        intent = new Intent(PaySuccessActivity.this,
            SelectNumberActivityPL3.class);
      }
      PaySuccessActivity.this.startActivity(intent);
      break;
    case 75:// 任选九
    case 74:// 胜负彩
      long currentTime_sfc = System.currentTimeMillis();
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(lotteryId + "")) {
          if (lottery.getDistanceTime() - currentTime_sfc <= 0) {
            MyToast.getToast(PaySuccessActivity.this, "该奖期已结束，请等下一期").show();
            return;
          }
        }
      }
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(lotteryId + "")) {
          AppTools.lottery = lottery;
        }
      }
      if (AppTools.lottery.getLotteryID().equals("74")) {
        intent = new Intent(PaySuccessActivity.this, Buy_SFC_Activity.class);
      } else if (AppTools.lottery.getLotteryID().equals("75")) {
        intent = new Intent(PaySuccessActivity.this, Buy_RX9_Activit.class);
      }
      PaySuccessActivity.this.startActivity(intent);
      break;
    case 3:// 七星彩
      long currentTime_qxc = System.currentTimeMillis();
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(lotteryId + "")) {
          if (lottery.getDistanceTime() - currentTime_qxc <= 0) {
            MyToast.getToast(PaySuccessActivity.this, "该奖期已结束，请等下一期").show();
            return;
          }
        }
      }
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals("3")) {
          AppTools.lottery = lottery;
        }
      }
      intent = new Intent(PaySuccessActivity.this,
          SelectNumberActivityPL5_QXC.class);
      PaySuccessActivity.this.startActivity(intent);
      break;
    case 13:// 七乐彩
      long currentTime_qlc = System.currentTimeMillis();
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(lotteryId + "")) {
          if (lottery.getDistanceTime() - currentTime_qlc <= 0) {
            MyToast.getToast(PaySuccessActivity.this, "该奖期已结束，请等下一期").show();
            return;
          }
        }
      }

      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals("13")) {
          AppTools.lottery = lottery;
          intent = new Intent(PaySuccessActivity.this, Select_QlcActivity.class);
          PaySuccessActivity.this.startActivity(intent);
        }
      }
      PaySuccessActivity.this.startActivity(intent);
      break;
    case 62:// 十一运夺金
      long currentTime_11x5y = System.currentTimeMillis();
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(lotteryId + "")) {
          if (lottery.getDistanceTime() - currentTime_11x5y <= 0) {
            MyToast.getToast(PaySuccessActivity.this, "该奖期已结束，请等下一期").show();
            return;
          }
        }
      }
      for (Lottery lottery : HallFragment.listLottery) {
        System.out.println("-+-++" + lottery.getLotteryID());
        if (lottery.getLotteryID().equals("62")) {
          AppTools.lottery = lottery;
          intent = new Intent(PaySuccessActivity.this,
              Select_11X5Activity.class);
        }
      }

      PaySuccessActivity.this.startActivity(intent);
      PaySuccessActivity.this.finish();
      break;
    case 70:// 11选5
      // backToHall();// Todo 返回大厅，再跳转，选号页面会重新生成
      long currentTime_11x5 = System.currentTimeMillis();
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(lotteryId + "")) {
          if (lottery.getDistanceTime() - currentTime_11x5 <= 0) {
            MyToast.getToast(PaySuccessActivity.this, "该奖期已结束，请等下一期").show();
            return;
          }
        }
      }
      for (Lottery lottery : HallFragment.listLottery) {
        System.out.println("-+-++" + lottery.getLotteryID());
        if (lottery.getLotteryID().equals("70")) {
          AppTools.lottery = lottery;
          intent = new Intent(PaySuccessActivity.this,
              Select_11X5Activity.class);
        }
      }

      PaySuccessActivity.this.startActivity(intent);
      PaySuccessActivity.this.finish();
      break;
    case 94:
      // 北京pk10
      long currentTime_pk10 = System.currentTimeMillis();
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(lotteryId + "")) {
          if (lottery.getDistanceTime() - currentTime_pk10 <= 0) {
            MyToast.getToast(PaySuccessActivity.this, "该奖期已结束，请等下一期").show();
            return;
          }
        }
      }
      for (Lottery lottery : HallFragment.listLottery) {
        System.out.println("-+-++" + lottery.getLotteryID());
        if (lottery.getLotteryID().equals("94")) {
          AppTools.lottery = lottery;
          intent = new Intent(PaySuccessActivity.this,
              Select_BJPK10Activity.class);
        }
      }

      PaySuccessActivity.this.startActivity(intent);
      PaySuccessActivity.this.finish();
      break;
    case 28:// 时时彩
    case 61:
    case 92:
    case 66:
    case 93:
      // backToHall();
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(lotteryId + "")) {
          AppTools.lottery = lottery;
        }
      }
      if ("28".equals(AppTools.lottery.getLotteryID())) {
        intent = new Intent(PaySuccessActivity.this, Select_SSCActivity.class);
      } else if ("92".equals(AppTools.lottery.getLotteryID())) {
        intent = new Intent(PaySuccessActivity.this, Select_HNSSCActivity.class);
      } else if ("61".equals(AppTools.lottery.getLotteryID())) {
        intent = new Intent(PaySuccessActivity.this, Select_JXSSCActivity.class);
      } else if ("66".equals(AppTools.lottery.getLotteryID())) {
        intent = new Intent(PaySuccessActivity.this, Select_XJSSCActivity.class);
      } else if ("93".equals(AppTools.lottery.getLotteryID())) {
        intent = new Intent(PaySuccessActivity.this, Select_YNSSCActivity.class);
      } else {

        MyToast.getToast(PaySuccessActivity.this, "该奖期已结束，请等下一期").show();
        break;
      }
      PaySuccessActivity.this.startActivity(intent);
      PaySuccessActivity.this.finish();
      break;
    case 83:// 江苏快3
      // backToHall();
      long currentTime_k3 = System.currentTimeMillis();
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(lotteryId + "")) {
          if (lottery.getDistanceTime() - currentTime_k3 <= 0) {
            MyToast.getToast(PaySuccessActivity.this, "该奖期已结束，请等下一期").show();
            return;
          }
        }
      }
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals("83")) {
          AppTools.lottery = lottery;
        }
      }
      intent = new Intent(PaySuccessActivity.this, Select_k3Activity.class);
      PaySuccessActivity.this.startActivity(intent);
      PaySuccessActivity.this.finish();
      break;

    case 72:// 竞彩足球
    case 73:// 竞彩篮球
      long currentTime_jc = System.currentTimeMillis();
      for (int i = 0; i < HallFragment.listLottery.size(); i++) {
        if ("72".equals(HallFragment.listLottery.get(i).getLotteryID())
            || "73".equals(HallFragment.listLottery.get(i).getLotteryID())) {
          if (HallFragment.listLottery.get(i).getDistanceTime()
              - currentTime_jc <= 0) {
            MyToast.getToast(getApplicationContext(), "该奖期已结束，请等下一期").show();
            return;
          }
          AppTools.lottery = HallFragment.listLottery.get(i);
          MyAsynTask2 myAsynTask2 = new MyAsynTask2();
          HallFragment.refreType = true;
          // 对阵信息是否为空
          if (AppTools.lottery.getDtmatch() != null
              && AppTools.lottery.getDtmatch().length() != 0) {
            if (72 == lotteryId
                && "72".equals(HallFragment.listLottery.get(i).getLotteryID())) {// 竞彩足球
              pd = BaseHelper.showProgress(PaySuccessActivity.this, "",
                  "对阵加载中...", true, false);
              myAsynTask2.execute(1);
              break;
            } else if (73 == lotteryId
                && "73".equals(HallFragment.listLottery.get(i).getLotteryID())) {// 竞彩篮球
              pd = BaseHelper.showProgress(PaySuccessActivity.this, "",
                  "对阵加载中...", true, false);
              myAsynTask2.execute(2);
              break;
            }
          } else {
            MyToast.getToast(PaySuccessActivity.this, "没有对阵信息").show();
          }
        }
      }
    }
  }

  /**
   * 异步任务 用来后台获取数据
   */
  class MyAsynTask2 extends AsyncTask<Integer, Integer, String> {
    String error = "0";

    @Override
    protected String doInBackground(Integer... params) {
      switch (params[0]) {
      case 1:// 足球
        error = new HallFragment().getBallData();
        if (error.equals("0")) {
          error = "100";
        }
        break;
      case 2:// 篮球
        error = new HallFragment().getBasketball();
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
        // 请求足球完成
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
  protected void onStop() {
    // TODO Auto-generated method stub
    if (myAsynTask != null) {
      myAsynTask.cancel(true);
    }
    if (pd != null) {
      pd.dismiss();
    }
    super.onStop();
  }
}
