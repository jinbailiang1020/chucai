package com.sm.sls_app.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.Lottery;
import com.sm.sls_app.dataaccess.LotteryContent;
import com.sm.sls_app.dataaccess.Schemes;
import com.sm.sls_app.fragment.HallFragment;
import com.sm.sls_app.ui.adapter.MyBetLotteryAdapter;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.BaseHelper;
import com.sm.sls_app.utils.FileUtils;
import com.sm.sls_app.utils.NumberTools;
import com.sm.sls_app.view.MyGridView;
import com.sm.sls_app.view.MyListView2;
import com.sm.sls_app.view.MyToast;

import java.util.ArrayList;
import java.util.List;

/**
 * 彩票 普通投注 订单详情
 * 
 * @author SLS003
 */
public class MyCommonLotteryInfo extends Activity implements OnClickListener {
  private ScrollView scrollView;
  private TextView tv_lotteryName, tv_money, tv_state, tv_winMoney, tv_count,
      tv_time, tv_orderId, tv_betType, tv_lotteryNum, tv_name, tv_yong,
      tv_scheme, tv_buyCount, tv_title, tv_content, tv_bei,
      tv_lotteryName_issue;
  private ImageView img_logo, ll_divider;
  private LinearLayout ll_numberCount;
  private ImageButton betinfo_hide_btn, btn_back;
  private MyListView2 mListView;
  private MyBetLotteryAdapter adapter;

  private MyGridView gv_winNumber;

  private Schemes scheme;

  private RelativeLayout rl_join1, rl_join2;

  private MyAsynTask myAsynTask;

  private String[] numbers;
  private List<String> show_numbers;
  private List<Integer> max;

  private int pageindex = 1;
  private int pagesize = 10;

  private Button btn_jixu;
  private Button btn_touzhu;
  private TextView footer;
  private int temp_length = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_betlottey_info);
    App.activityS.add(this);
    findView();
    initView();
    myAsynTask = new MyAsynTask();
    myAsynTask.execute();
  }

  /**
   * myScrollView.smoothScrollTo(0,20); 初始化UI
   */
  private void findView() {
    scrollView = (ScrollView) findViewById(R.id.scrollView);
    tv_lotteryName = (TextView) findViewById(R.id.tv_lotteryName);
    tv_money = (TextView) findViewById(R.id.tv_money2);
    tv_state = (TextView) findViewById(R.id.tv_state2);
    tv_winMoney = (TextView) findViewById(R.id.tv_winMoney2);
    tv_count = (TextView) findViewById(R.id.tv_numberCount);
    tv_time = (TextView) findViewById(R.id.tv_time2);
    tv_orderId = (TextView) findViewById(R.id.tv_orderId2);
    tv_betType = (TextView) findViewById(R.id.tv_orderType2);
    tv_lotteryNum = (TextView) findViewById(R.id.tv_num1);
    tv_lotteryName_issue = (TextView) findViewById(R.id.tv_lotteryName_issue);
    tv_name = (TextView) findViewById(R.id.tv_name2);
    tv_yong = (TextView) findViewById(R.id.tv_yong2);
    tv_scheme = (TextView) findViewById(R.id.tv_scheme2);
    tv_buyCount = (TextView) findViewById(R.id.tv_buy2);
    tv_title = (TextView) findViewById(R.id.tv_schemetitle2);
    tv_content = (TextView) findViewById(R.id.tv_schemeContent2);
    tv_bei = (TextView) findViewById(R.id.tv_bei);
    img_logo = (ImageView) findViewById(R.id.img_logo);
    mListView = (MyListView2) findViewById(R.id.lv_betInfo);

    gv_winNumber = (MyGridView) findViewById(R.id.gv_winNumber);

    rl_join1 = (RelativeLayout) findViewById(R.id.rl_joinInfo);
    rl_join2 = (RelativeLayout) findViewById(R.id.rl_joinInfo2);

    btn_jixu = (Button) findViewById(R.id.btn_jixu);
    btn_touzhu = (Button) findViewById(R.id.btn_touzhu);

    betinfo_hide_btn = (ImageButton) findViewById(R.id.betinfo_hide_btn);
    ll_numberCount = (LinearLayout) findViewById(R.id.ll_numberCount);
    ll_divider = (ImageView) findViewById(R.id.ll_divider);
    btn_back = (ImageButton) findViewById(R.id.btn_back);

    footer = new TextView(MyCommonLotteryInfo.this);
    footer.setLayoutParams(new AbsListView.LayoutParams(
        LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(
            R.dimen.bet_lottery_item_title_height)));
    footer.setTextSize(14);
    footer.setGravity(Gravity.CENTER);
    footer.setTextColor(getResources().getColor(
        R.color.vpi_text_unselected_gray));
    footer.setText("下一页");
    footer.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        pageindex++;
        myAsynTask = new MyAsynTask();
        myAsynTask.execute();
      }
    });
    mListView.addFooterView(footer);

  }

  private void setWinNumber() {

    if (scheme.getLotteryID() == null)
      return;
    if (null == scheme.getWinNumber())
      return;
    Log.i("x", "彩种id   " + scheme.getLotteryID());
    Spanned number = null;
    String temp_win = scheme.getWinNumber().replaceAll("\\s?[\\+-]\\s?", "+");
    if (temp_win.contains("+")) {
      number = Html.fromHtml("<font color='#BE0205'>" + scheme.getWinNumber()
          + "</FONT>");
      tv_lotteryNum.setText(number);
      return;
    }

    String red = temp_win.split("\\+")[0];
    String blue = "";

    if (temp_win.split("\\+").length == 2)
      blue = scheme.getWinNumber().split("\\+")[1];

    number = Html.fromHtml("<font color='#BE0205'>" + red + "</FONT>"
        + "<font color='#4060ff'>" + " " + blue + "</FONT>");
    tv_lotteryNum.setText(number);
  }

  /**
   * 给控件赋值
   */
  private void initView() {
    btn_back.setOnClickListener(this);
    betinfo_hide_btn.setOnClickListener(this);
    ll_numberCount.setOnClickListener(this);
    btn_jixu.setOnClickListener(this);
    btn_touzhu.setOnClickListener(this);

    if (null == scheme)
      return;

    rl_join1.setVisibility(View.GONE);
    rl_join2.setVisibility(View.GONE);
    if ("74".equals(scheme.getLotteryID())
        || "75".equals(scheme.getLotteryID())) {
      btn_jixu.setVisibility(View.GONE);
    } else
      btn_jixu.setVisibility(View.VISIBLE);
    btn_touzhu.setText(FileUtils.getTitleText(scheme.getLotteryID()) + "投注");
    tv_lotteryName.setText(FileUtils.getTitleText(scheme.getLotteryID()));

    tv_lotteryName_issue.setText(scheme.getIsuseName() == null ? "" : scheme
        .getIsuseName() + "期");
    img_logo.setBackgroundResource(AppTools.allLotteryLogo.get(scheme
        .getLotteryID()));
    tv_money.setText(scheme.getMoney() + "元");
    tv_time.setText(scheme.getDateTime());
    tv_orderId.setText(scheme.getSchemeNumber());

    if (scheme.getFromClient() == 1)
      tv_betType.setText("网页投注");
    else if (scheme.getFromClient() == 2)
      tv_betType.setText("手机APP投注");

    tv_winMoney.setText("--");

    Log.i(
        "x",
        "方案ID====  " + scheme.getId() + " === 是否开奖  "
            + scheme.getSchemeIsOpened() + "是否撤单  === "
            + scheme.getQuashStatus() + "IssueName  " + scheme.getIsuseName());

    tv_count.setText(scheme.getContent_lists().size() + "条");
    // tv_bei.setText(scheme.getMultiple() + "倍");
    if (0 != scheme.getQuashStatus()
        || (scheme.getQuashStatus() + "").length() == 0) {
      tv_state.setText("已撤单");
      setWinNumber();
      System.out.println("}{{{" + scheme.getSchemeIsOpened());
    } else {
      System.out.println("}}}}" + scheme.getSchemeIsOpened());
      // 是否合买
      if (scheme.getIsPurchasing().equals("False")) {
        rl_join1.setVisibility(View.VISIBLE);
        rl_join2.setVisibility(View.VISIBLE);

        tv_name.setText(scheme.getInitiateName());
        tv_yong.setText((int) (scheme.getSchemeBonusScale() * 1000) + "‰");
        System.out.println("x=====佣金" + scheme.getSchemeBonusScale() * 1000);
        tv_scheme.setText(scheme.getShare() + "份,共" + scheme.getMoney()
            + "元,每份" + scheme.getShareMoney() + "元");
        tv_buyCount.setText(scheme.getMyBuyShare() + "份  共"
            + scheme.getMyBuyMoney() + "元");
        if ("null".equals(scheme.getTitle())) {
          tv_title.setText("无标题");
        } else {
          tv_title.setText(scheme.getTitle());
        }
        if ("null".equals(scheme.getDescription())) {
          tv_content.setText("无描述");
        } else {
          tv_content.setText(scheme.getDescription());
        }
      }

      if ("False".equals(scheme.getSchemeIsOpened())
          || scheme.getSchemeIsOpened().length() == 0) {
        tv_state.setText("未开奖");
        tv_lotteryNum.setText("- - - - - - - -");
      } else if ("True".equals(scheme.getSchemeIsOpened())) {
        Log.i("x", "开奖 " + scheme.getSchemeIsOpened());
        setWinNumber();
        if (scheme.getWinMoneyNoWithTax() > 0) {
          tv_state.setText("中奖");
          tv_winMoney.setText(scheme.getWinMoneyNoWithTax() + "元");
        } else {
          Log.i("x", "未中奖 " + scheme.getWinMoneyNoWithTax());
          tv_state.setText("未中奖");
        }
      }
    }
    String numbers = scheme.getLotteryNumber();
    System.out.println("投注号码----->\n" + numbers);
    System.out.println("玩法id----" + scheme.getPlayTypeID());
    adapter = new MyBetLotteryAdapter(getApplicationContext(), scheme,
        show_numbers, max);
    mListView.setAdapter(adapter);

  }

  @Override
  protected void onResume() {
    // TODO Auto-generated method stub
    super.onResume();
    gv_winNumber.setFocusable(false);
    mListView.setFocusable(false);
    scrollView.smoothScrollTo(0, 0);
  }

  /**
   * 异步任务
   */
  class MyAsynTask extends AsyncTask<Void, Integer, String> {
    private ProgressDialog dialog;

    @Override
    protected void onPreExecute() {
      // TODO Auto-generated method stub
      super.onPreExecute();
      dialog = BaseHelper.showProgress(MyCommonLotteryInfo.this, null, "加载中",
          true, false);
    }

    /**
     * 在后台执行的程序
     */
    @Override
    protected String doInBackground(Void... params) {
      int result = 1;
      if (pageindex == 1) {
        scheme = (Schemes) getIntent().getSerializableExtra("scheme");
        Log.i("x", "中奖号码=== " + scheme.getWinNumber());
        Log.i("x", "投注的倍数=== " + scheme.getMultiple());
        Log.i("x", "是否合买=== " + scheme.getIsPurchasing());
        Log.i("x", "name=== " + scheme.getInitiateName());
        if (scheme == null) {
          return "-1";
        }
        List<LotteryContent> list = scheme.getContent_lists();
        // if (list == null || list.isEmpty()) {
        // return "-1";
        // }
        numbers = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
          numbers[i] = list.get(i).getLotteryNumber() + "/"
              + list.get(i).getPlayType();
          Log.i("投注的内容numbers[" + i + "]", numbers[i]);
        }

        show_numbers = new ArrayList<String>();
        max = new ArrayList<Integer>();
        result = 0;
      }

      temp_length = Math.min(pagesize, numbers.length - pagesize
          * (pageindex - 1));

      for (int i = 0; i < temp_length; i++) {
        int index = i + temp_length * (pageindex - 1);
        show_numbers.add(numbers[index]);
        numbers[index].replace("-", "+");
        if (!numbers[index].contains("+")) {
          max.add(100);
        } else {
          max.add(numbers[index].substring(0, numbers[index].indexOf("+"))
              .trim().split(" ").length);
        }
      }

      return result + "";
    }

    @Override
    protected void onPostExecute(String result) {
      // TODO Auto-generated method stub
      super.onPostExecute(result);
      dialog.dismiss();
      if (show_numbers != null && numbers != null) {
        footer.setText("下一页 (" + show_numbers.size() + "/" + numbers.length
            + ")");

      }
      switch (Integer.valueOf(result)) {
      case 0:
        initView();
        break;

      case 1:
        adapter.notifyDataSetChanged();
        break;
      case -1:
        MyToast.getToast(MyCommonLotteryInfo.this, "数据加载失败");
        break;
      }
      if (temp_length < pagesize || (numbers.length == pagesize)) {
        mListView.removeFooterView(footer);
      }
    }
  }

  @Override
  public void onClick(View arg0) {
    // TODO Auto-generated method stub
    String lotteryID = scheme.getLotteryID();
    switch (arg0.getId()) {
    case R.id.btn_jixu:// 继续本次号码投注
      goToBetLottery(lotteryID);
      break;
    case R.id.btn_touzhu:// 去往本彩种投注
      goToSelectLottery(lotteryID);
      break;
    case R.id.betinfo_hide_btn:
    case R.id.ll_numberCount:

      if (!betinfo_hide_btn.isSelected()) {
        betinfo_hide_btn.setSelected(true);
        ll_divider.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);

      } else {
        betinfo_hide_btn.setSelected(false);
        ll_divider.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.VISIBLE);
      }
      break;

    case R.id.btn_back:
      finish();
      break;
    }
  }

  /**
   * 根据彩种id跳转不同投注页面
   * 
   * @param lotteryID
   *          彩种id
   */
  private void goToBetLottery(String lotteryID) {

    System.out.println("彩种id-----" + scheme.getPlayTypeID());
    System.out.println("玩法id-----" + lotteryID);
    if (TextUtils.isEmpty(lotteryID)) {
      MyToast.getToast(MyCommonLotteryInfo.this, "lotteryID 为空").show();
      return;
    }
    int id = Integer.parseInt(lotteryID);
    Intent intent;
    if (!NumberTools.changeSchemesToSelectedNumbers(scheme)) {
      MyToast.getToast(MyCommonLotteryInfo.this, "内容记录错误").show();
      return;
    }
    switch (id) {
    case 5:// 双色球

      intent = new Intent(getApplicationContext(), BetActivity.class);
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    case 39:// 大乐透
      intent = new Intent(getApplicationContext(), Bet_DLT_Activity.class);
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    case 6:// 3D
      intent = new Intent(getApplicationContext(), BetActivityFC3D.class);
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    case 63:// 排列三
      intent = new Intent(getApplicationContext(), BetActivityPL3.class);
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    case 64:// 排列五
      intent = new Intent(getApplicationContext(), BetActivityPL5_QXC.class);
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    case 3:// 七星彩
      intent = new Intent(getApplicationContext(), BetActivityPL5_QXC.class);
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    case 13:// 七乐彩
      intent = new Intent(getApplicationContext(), Bet_QLC_Activity.class);
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    case 62:// 十一运夺金
      intent = new Intent(getApplicationContext(), Bet_11x5Activity.class);
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    case 70:// 11选5
      intent = new Intent(getApplicationContext(), Bet_11x5Activity.class);
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    case 28:// 时时彩
      intent = new Intent(getApplicationContext(), Bet_SSCActivity.class);
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    case 92:
      intent = new Intent(getApplicationContext(), Bet_HNSSCActivity.class);
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    case 61:// 江西时时彩
      intent = new Intent(getApplicationContext(), Bet_JXSSCActivity.class);
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    case 83:// 江苏快3
      intent = new Intent(getApplicationContext(), Bet_k3Activity.class);
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    case 94:// 北京pk10
      intent = new Intent(getApplicationContext(), Bet_BJPK10Activity.class);
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    case 66:// 新疆时时彩
      intent = new Intent(getApplicationContext(), Bet_XJSSCActivity.class);
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    case 93:// 印尼时时彩
      intent = new Intent(getApplicationContext(), Bet_YNSSCActivity.class);
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    }
  }

  /**
   * 根据彩种id跳转不同选号页面
   * 
   * @param lotteryID
   *          彩种id
   */
  private void goToSelectLottery(String lotteryID) {
    int id = Integer.parseInt(lotteryID);
    Intent intent = null;
    switch (id) {
    case 5:// 双色球
      long currentTime_ssq = System.currentTimeMillis();
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(scheme.getLotteryID())) {
          if (lottery.getDistanceTime() - currentTime_ssq <= 0) {
            MyToast.getToast(MyCommonLotteryInfo.this, "该奖期已结束，请等下一期").show();
            return;
          }
        }
      }
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals("5")
            && scheme.getLotteryID().equals("5")) {
          AppTools.lottery = lottery;
          intent = new Intent(MyCommonLotteryInfo.this,
              SelectNumberActivity.class);
        }
      }
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    case 39:// 大乐透
      long currentTime_dlt = System.currentTimeMillis();
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(scheme.getLotteryID())) {
          if (lottery.getDistanceTime() - currentTime_dlt <= 0) {
            MyToast.getToast(MyCommonLotteryInfo.this, "该奖期已结束，请等下一期").show();
            return;
          }
        }
      }
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals("39")) {
          AppTools.lottery = lottery;
        }
      }
      intent = new Intent(getApplicationContext(), Buy_DLT_Activit.class);
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    case 6:// 3D
      long currentTime_3d = System.currentTimeMillis();
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(scheme.getLotteryID())) {
          if (lottery.getDistanceTime() - currentTime_3d <= 0) {
            MyToast.getToast(MyCommonLotteryInfo.this, "该奖期已结束，请等下一期").show();
            return;
          }
        }
      }
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(scheme.getLotteryID())) {
          AppTools.lottery = lottery;
        }
      }
      intent = new Intent(getApplicationContext(),
          SelectNumberActivityFC3D.class);
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    case 63:// 排列三
    case 64:// 排列五
      long currentTime = System.currentTimeMillis();
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(scheme.getLotteryID())) {
          if (lottery.getDistanceTime() - currentTime <= 0) {
            MyToast.getToast(MyCommonLotteryInfo.this, "该奖期已结束，请等下一期").show();
            return;
          }
        }
      }
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(scheme.getLotteryID())) {
          AppTools.lottery = lottery;
        }
      }
      if ("64".equals(AppTools.lottery.getLotteryID())) {
        intent = new Intent(MyCommonLotteryInfo.this,
            SelectNumberActivityPL5_QXC.class);
      } else {
        intent = new Intent(MyCommonLotteryInfo.this,
            SelectNumberActivityPL3.class);
      }
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    case 75:// 任选九
    case 74:// 胜负彩
      long currentTime_sfc = System.currentTimeMillis();
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(scheme.getLotteryID())) {
          if (lottery.getDistanceTime() - currentTime_sfc <= 0) {
            MyToast.getToast(MyCommonLotteryInfo.this, "该奖期已结束，请等下一期").show();
            return;
          }
        }
      }
      if (scheme.getLotteryID().equals("74")) {
        intent = new Intent(MyCommonLotteryInfo.this, Buy_SFC_Activity.class);
      } else if (scheme.getLotteryID().equals("75")) {
        intent = new Intent(MyCommonLotteryInfo.this, Buy_RX9_Activit.class);
      }
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    case 3:// 七星彩
      long currentTime_qxc = System.currentTimeMillis();
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(scheme.getLotteryID())) {
          if (lottery.getDistanceTime() - currentTime_qxc <= 0) {
            MyToast.getToast(MyCommonLotteryInfo.this, "该奖期已结束，请等下一期").show();
            return;
          }
        }
      }
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals("3")) {
          AppTools.lottery = lottery;
        }
      }
      intent = new Intent(MyCommonLotteryInfo.this,
          SelectNumberActivityPL5_QXC.class);
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    case 13:// 七乐彩
      long currentTime_qlc = System.currentTimeMillis();
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(scheme.getLotteryID())) {
          if (lottery.getDistanceTime() - currentTime_qlc <= 0) {
            MyToast.getToast(MyCommonLotteryInfo.this, "该奖期已结束，请等下一期").show();
            return;
          }
        }
      }

      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals("13")) {
          AppTools.lottery = lottery;
          intent = new Intent(MyCommonLotteryInfo.this,
              Select_QlcActivity.class);
          MyCommonLotteryInfo.this.startActivity(intent);
        }
      }
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    case 94:// 北京pk10
      long currentTime_pk10 = System.currentTimeMillis();
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(scheme.getLotteryID())) {
          if (lottery.getDistanceTime() - currentTime_pk10 <= 0) {
            MyToast.getToast(MyCommonLotteryInfo.this, "该奖期已结束，请等下一期").show();
            return;
          }
        }
      }
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals("94")) {
          AppTools.lottery = lottery;
        }
      }
      intent = new Intent(MyCommonLotteryInfo.this, Select_BJPK10Activity.class);
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    case 62:// 十一运夺金
    case 70:// 11选5
      long currentTime_11x5 = System.currentTimeMillis();
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(scheme.getLotteryID())) {
          if (lottery.getDistanceTime() - currentTime_11x5 <= 0) {
            MyToast.getToast(MyCommonLotteryInfo.this, "该奖期已结束，请等下一期").show();
            return;
          }
        }
      }
      for (Lottery lottery : HallFragment.listLottery) {
        System.out.println("-+-++" + lottery.getLotteryID());
        if (scheme.getLotteryID().equals("62")
            && lottery.getLotteryID().equals("62")) {
          AppTools.lottery = lottery;
        } else if (scheme.getLotteryID().equals("70")
            && lottery.getLotteryID().equals("70")) {
          AppTools.lottery = lottery;
        }
      }
      intent = new Intent(getApplicationContext(), Select_11X5Activity.class);
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    case 28:// 时时彩
    case 61:
    case 92:
    case 66:
    case 93:
      for (Lottery lottery : HallFragment.listLottery) {

        if (lottery.getLotteryID().equals(scheme.getLotteryID())) {
          AppTools.lottery = lottery;
        }
      }
      if ("92".equals(AppTools.lottery.getLotteryID())) {
        intent = new Intent(MyCommonLotteryInfo.this,
            Select_HNSSCActivity.class);
      } else if ("28".equals(AppTools.lottery.getLotteryID())) {
        intent = new Intent(MyCommonLotteryInfo.this, Select_SSCActivity.class);
      } else if ("61".equals(AppTools.lottery.getLotteryID())) {
        intent = new Intent(MyCommonLotteryInfo.this,
            Select_JXSSCActivity.class);
      } else if ("66".equals(AppTools.lottery.getLotteryID())) {
        intent = new Intent(MyCommonLotteryInfo.this,
            Select_XJSSCActivity.class);
      } else if ("93".equals(AppTools.lottery.getLotteryID())) {
        intent = new Intent(MyCommonLotteryInfo.this,
            Select_YNSSCActivity.class);
      } else {
        MyToast.getToast(MyCommonLotteryInfo.this, "该奖期已结束，请等下一期").show();
        break;
      }
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    case 83:// 江苏快3
      long currentTime_k3 = System.currentTimeMillis();
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals(scheme.getLotteryID())) {
          if (lottery.getDistanceTime() - currentTime_k3 <= 0) {
            MyToast.getToast(MyCommonLotteryInfo.this, "该奖期已结束，请等下一期").show();
            return;
          }
        }
      }
      for (Lottery lottery : HallFragment.listLottery) {
        if (lottery.getLotteryID().equals("83")) {
          AppTools.lottery = lottery;
        }
      }
      intent = new Intent(MyCommonLotteryInfo.this, Select_k3Activity.class);
      MyCommonLotteryInfo.this.startActivity(intent);
      break;
    }
  }

}
