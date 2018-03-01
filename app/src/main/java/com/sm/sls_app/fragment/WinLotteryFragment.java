package com.sm.sls_app.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.Lottery;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.ui.LoginActivity;
import com.sm.sls_app.ui.SettingActivity;
import com.sm.sls_app.ui.WinLotteryInfoActivity;
import com.sm.sls_app.ui.WinLottery_jc_Activity;
import com.sm.sls_app.ui.adapter.WinLotteryAdapter;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.NetWork;
import com.sm.sls_app.view.MyToast;
import com.sm.sls_app.view.RollTextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开奖公告
 * 
 * @author lian
 * 
 */
@SuppressLint("NewApi")
public class WinLotteryFragment extends Fragment {

  private PullToRefreshListView listView;
  private TextView win_lottery_hint;
  private WinLotteryAdapter adapter;
  private Context context;
  private Intent intent;
  private RollTextView rollTextView;
  private MyAsynTask myAsynTask;
  private List<Map<String, String>> list_open;
  private List<Map<String, String>> list_open_temp;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    setHasOptionsMenu(true);
    View v = inflater.inflate(R.layout.activity_lottery, container, false);
    context = getActivity();
    findView(v);
    setListener();
    return v;
  }

  /** 初始化UI */
  private void findView(View v) {
    listView = (PullToRefreshListView) v
        .findViewById(R.id.win_lottery_listView);
    rollTextView = (RollTextView) v.findViewById(R.id.rolltextView1);
    win_lottery_hint = (TextView) v.findViewById(R.id.win_lottery_hint);
    list_open = new ArrayList<Map<String, String>>();
    list_open_temp = new ArrayList<Map<String, String>>();

    adapter = new WinLotteryAdapter(context, list_open, getActivity());
    System.out.println("=====================放adapter====");
    if (HallFragment.Message != null && HallFragment.Message != "")
      rollTextView.setText(HallFragment.Message);
    else
      rollTextView.setText("----没有中奖信息----");
    getHttpRes();
  }

  /** 绑定监听 */
  private void setListener() {
    // LinearLayout header = listView.getHeaderView();
    // header.setBackgroundResource(R.color.my_center_bg2);
    listView.setAdapter(adapter);
    listView.setOnItemClickListener(new MyItemsClickListener());
    listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

      @Override
      public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        listView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(
            "最近更新: " + Long2DateStr_detail(System.currentTimeMillis()));
        getHttpRes();
      }
    });
  }

  public String Long2DateStr_detail(long time) {
    String format = "yyyy-M-d HH:mm";
    Date date = new Date(time);
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    String long_time = sdf.format(date);
    return long_time;
  }

  private void getHttpRes() {
    if (NetWork.isConnect(context)) {
      if (myAsynTask != null) {
        myAsynTask.cancel(true);
      }
      myAsynTask = new MyAsynTask();
      myAsynTask.execute();
    } else {
      MyToast.getToast(context, "网络连接异常，请检查网络").show();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // TODO Auto-generated method stub
    Log.i("x", "select---" + item.getItemId());
    switch (item.getItemId()) {
    /** 刷新 **/
    case 1:
      if (NetWork.isConnect(context)) {
        myAsynTask = new MyAsynTask();
        myAsynTask.execute();
      } else {
        MyToast.getToast(context, "网络连接异常，请检查网络").show();
      }
      break;
    /** 设置 **/
    case 2:
      intent = new Intent(context, SettingActivity.class);
      context.startActivity(intent);
      break;
    /** 更改账户 **/
    case 3:
      Intent intent = new Intent(context, LoginActivity.class);
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

  /** ListView 子项点击监听 */
  class MyItemsClickListener implements OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position,
        long arg3) {
      position = position - 1;
      Map<String, String> item = list_open.get(position);
      if (item.get("isOpen").equals("0")) {
        MyToast.getToast(context, "没有该期开奖信息").show();
        return;
      }
      if ("72".equals(item.get("lotteryId"))
          || "73".equals(item.get("lotteryId"))) {
        intent = new Intent(context, WinLottery_jc_Activity.class);
        intent.putExtra("lotteryId", item.get("lotteryId"));
        intent.putExtra("date", item.get("name"));
        context.startActivity(intent);
      } else {
        intent = new Intent(context, WinLotteryInfoActivity.class);
        intent.putExtra("lotteryId", item.get("lotteryId"));
        context.startActivity(intent);
      }
    }
  }

  /** 异步任务 用来后台获取数据 */
  class MyAsynTask extends AsyncTask<Void, Integer, String> {

    @Override
    protected String doInBackground(Void... params) {
      String opt = "13";
      String key = MD5.md5(AppTools.MD5_key);
      String time = RspBodyBaseBean.getTime();
      String info = RspBodyBaseBean.changeLottery_info(AppTools.lotteryIds);
      Log.i("x", "拿普通Info====   " + info);

      String imei = RspBodyBaseBean.getIMEI(context);

      String crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");

      String auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");

      String[] values = { opt, auth, info };

      String result = HttpUtils.doPost(AppTools.names, values, AppTools.path);

      Log.i("x", "普通result---" + result);

      if (result == null)
        return "0";
      if (result.equals("-500"))
        return "-1";

      try {
        JSONObject jsonObject = new JSONObject(result);
        if (jsonObject != null) {
          list_open_temp.clear();
          JSONArray dtOpenInfo = new JSONArray(
              jsonObject.getString("dtOpenInfo"));
          if (dtOpenInfo != null) {
            Map<String, String> map = null;
            System.out.println("============得到彩种的大小==" + dtOpenInfo.length());
            for (int i = 0; i < dtOpenInfo.length(); i++) {
              map = new HashMap<String, String>();
              map.put("lotteryId",
                  dtOpenInfo.getJSONObject(i).optString("lotteryId"));
              map.put("name", dtOpenInfo.getJSONObject(i).optString("isuseName"));
              map.put("winLotteryNumber", dtOpenInfo.getJSONObject(i)
                  .optString("winLotteryNumber"));
              map.put("isOpen", "1");
              list_open_temp.add(map);
            }
          }
          JSONArray dtMatch = new JSONArray(jsonObject.getString("dtMatch"));
          if (dtMatch != null && dtMatch.length() != 0) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("lotteryId", "72");

            map.put("name", dtMatch.getJSONObject(0).optString("matchDate")
                .split(" ")[0]);
            map.put("winLotteryNumber", "");
            map.put("isOpen", "1");
            list_open_temp.add(map);
          }
          JSONArray dtMatchBasket = new JSONArray(
              jsonObject.getString("dtMatchBasket"));
          if (dtMatchBasket != null && dtMatchBasket.length() != 0) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("lotteryId", "73");
            map.put("name",
                dtMatchBasket.getJSONObject(0).optString("matchDate")
                    .split(" ")[0]);
            map.put("winLotteryNumber", "");
            map.put("isOpen", "1");
            list_open_temp.add(map);
          }
          addOther();
          return "1";
        }
      } catch (Exception e) {
        Log.w("x", "Exception" + e.toString());
      }

      return "-1";
    }

    @Override
    protected void onPostExecute(String result) {
      listView.onRefreshComplete();
      switch (Integer.valueOf(result)) {
      case -1:
        win_lottery_hint.setText("连接超时，请重试");
        break;
      case 0:
        win_lottery_hint.setText("暂时没有开奖信息");
        break;
      case 1:
        list_open.clear();
        sort(list_open_temp);
        adapter.notifyDataSetChanged();
        break;

      default:
        break;
      }
      super.onPostExecute(result);
    }
  }

  private void addOther() {
    Map<String, String> map = null;
    for (Lottery lottery : HallFragment.listLottery) {
      boolean contain = false;
      for (int i = 0; i < list_open_temp.size(); i++) {
        if (list_open_temp.get(i).get("lotteryId")
            .equals(lottery.getLotteryID())) {
          contain = true;
          break;
        }
      }
      if (!contain) {
        map = new HashMap<String, String>();
        map.put("lotteryId",
            lottery.getLotteryID() == null ? "0" : lottery.getLotteryID());
        map.put(
            "name",
            lottery.getLastIsuseName() == null ? "" : lottery
                .getLastIsuseName());
        map.put("winLotteryNumber", lottery.getLastWinNumber() == null ? ""
            : lottery.getLastWinNumber());
        map.put("isOpen", "0");
        list_open_temp.add(map);
      }
    }
  }

  private void sort(List<Map<String, String>> temp) {
    String[] sortByLotteryId = { "28", "70", "62", "78", "61", "92", "66",
        "93", "6", "63", "83", "94", "64", "5", "39", "72", "73" };
    if (temp != null) {
      for (int i = 0; i < sortByLotteryId.length; i++) {
        for (int j = 0; j < temp.size(); j++) {
          if (sortByLotteryId[i].equals(temp.get(j).get("lotteryId"))) {
            list_open.add(temp.get(j));
          }
        }
      }
    }
  }
}
