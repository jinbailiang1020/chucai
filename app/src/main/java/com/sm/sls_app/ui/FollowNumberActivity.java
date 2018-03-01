package com.sm.sls_app.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.BuyContent;
import com.sm.sls_app.dataaccess.ChaseList;
import com.sm.sls_app.dataaccess.Schemes;
import com.sm.sls_app.dataaccess.UserList;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.ui.adapter.MyFollowNumberAdapter;
import com.sm.sls_app.ui.adapter.MyFollowOtherAdapter;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.StringUtils;
import com.sm.sls_app.view.MyListView2;
import com.sm.sls_app.view.MyToast;

public class FollowNumberActivity extends Activity implements OnClickListener {

  private ListView listView;
  private MyFollowNumberAdapter fAdapter;
  private Bundle bundle;
  private Schemes scheme;
  private TextView tv_touzhu;// 显示注数
  private ImageButton btn_back;// 返回
  private String id;
  private String opt, auth, info, time, imei, crc; // 格式化后的参数
  private MyAsyncTask myAsyncTask;
  private MyHandler myHandler;
  private List<BuyContent> buyContents = new ArrayList<BuyContent>();
  private List<ChaseList> chaseLists = new ArrayList<ChaseList>();
  private List<UserList> userLists = new ArrayList<UserList>();
  private LinearLayout ll_fangan, ll_hemai;
  private ImageButton fangan_hide_btn, hemai_hide_btn;
  private MyListView2 lv_fangan, lv_hemai;
  private ImageView fangan_divider, hemai_divider;
  private MyFollowOtherAdapter oAdapter1, oAdapter2;
  private TextView tv_fangan, tv_hemai;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // 设置无标题
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_follow_number_notjc);
    init();
    findView();
    setListener();

  }

  class MyAsyncTask extends AsyncTask<Void, Integer, String> {

    @Override
    protected String doInBackground(Void... params) {
      opt = "70";
      time = RspBodyBaseBean.getTime();
      imei = RspBodyBaseBean.getIMEI(getApplicationContext());
      info = RspBodyBaseBean.follow_info(id);

      crc = RspBodyBaseBean.getCrc(time, imei,
          MD5.md5(AppTools.user.getUserPass() + AppTools.MD5_key), info,
          AppTools.user.getUid());

      auth = RspBodyBaseBean.getAuth(crc, time, imei, AppTools.user.getUid());

      String[] values = { opt, auth, info };
      String result = HttpUtils.doPost(AppTools.names, values, AppTools.path);

      if ("-500".equals(result)) {
        return "-500";
      }
      System.out.println("FollowNumberActivity---" + result);
      try {
        JSONObject object = new JSONObject(result);
        if ("0".equals(object.optString("error"))) {
          JSONArray buyaArray = object.optJSONArray("buyContent");
          BuyContent buyContent = null;
          if (null != buyaArray) {
            for (int i = 0; i < buyaArray.length(); i++) {
              buyContent = new BuyContent();
              JSONObject item = buyaArray.optJSONObject(i);
              buyContent.setPlayName(item.optString("PlayName"));
              buyContent.setLotteryNumber(item.optString("lotteryNumber"));
              buyContent.setPlayType(item.optString("playType"));
              buyContent.setSumMoney(item.optString("sumMoney"));
              buyContent.setSumNum(item.optString("sumNum"));
              buyContents.add(buyContent);
            }
          }
          JSONArray chaseArray = object.optJSONArray("chaseList");
          ChaseList chaseList = null;
          if (null != chaseArray) {
            for (int i = 0; i < chaseArray.length(); i++) {
              chaseList = new ChaseList();
              JSONObject item = chaseArray.optJSONObject(i);
              chaseList.setIsuseName(item.optString("isuseName"));
              chaseList.setMoney(item.optString("money"));
              chaseList.setMultiple(item.optString("multiple"));
              chaseList.setSumMoney(item.optString("sumMoney"));
              chaseLists.add(chaseList);
            }
          }
          JSONArray userArray = object.optJSONArray("userList");
          UserList userList = null;
          if (null != userArray) {
            for (int i = 0; i < userArray.length(); i++) {
              userList = new UserList();
              JSONObject item = userArray.optJSONObject(i);
              userList.setDetailMoney(item.optString("detailMoney"));
              userList.setName(item.optString("name"));
              userList.setScalePercent(item.optDouble("scalePercent"));
              userList.setShare(item.optString("share"));
              userLists.add(userList);
            }
          }
          return "0";
        } else {
          return object.optString("error");
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }

      return "1001";
    }

    @Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);
      myHandler.sendEmptyMessage(Integer.parseInt(result));
    }

  }

  class MyHandler extends Handler {

    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
      case 0:
        int f = 0;
        int u = 0;
        // 彩票号码部分
        if (null != buyContents) {
          fAdapter = new MyFollowNumberAdapter(FollowNumberActivity.this,
              buyContents);
          listView.setAdapter(fAdapter);
        }
        // 方案部分
        if (null != chaseLists) {
          oAdapter1 = new MyFollowOtherAdapter(chaseLists, null,
              FollowNumberActivity.this, 1);
          lv_fangan.setAdapter(oAdapter1);
          f = chaseLists.size();
        }
        // 合买部分
        if (null != chaseLists) {
          oAdapter2 = new MyFollowOtherAdapter(null, userLists,
              FollowNumberActivity.this, 2);
          lv_hemai.setAdapter(oAdapter2);
          u = userLists.size();
        }
        tv_fangan.setText(String.format("(%d条)", f));
        tv_hemai.setText(String.format("(%d条)", u));
        break;
      default:
        Toast.makeText(FollowNumberActivity.this, "数据记载失败", Toast.LENGTH_SHORT)
            .show();
        break;
      }
    }
  }

  /** 初始化 */
  private void init() {
    bundle = getIntent().getBundleExtra("bundle");
    scheme = (Schemes) bundle.getSerializable("schem");
    id = scheme.getId();
    if (!StringUtils.isEmpty(id)) {
      if (AppTools.user != null) {
        myHandler = new MyHandler();
        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
      } else {
        MyToast.getToast(FollowNumberActivity.this, "请先登陆").show();
        Intent intent = new Intent(FollowNumberActivity.this,
            LoginActivity.class);
        intent.putExtra("loginType", "bet");
        FollowNumberActivity.this.startActivity(intent);
      }
    } else {
      MyToast.getToast(getApplicationContext(), "获取彩票详情失败").show();
    }
  }

  /** 初始化UI */
  private void findView() {
    tv_touzhu = (TextView) this.findViewById(R.id.tv_touzhu);
    btn_back = (ImageButton) this.findViewById(R.id.btn_back);
    tv_touzhu.setText("投注（" + scheme.getCountNotes() + "注）");
    listView = (ListView) this.findViewById(R.id.followinfo_jc_listView);

    ll_fangan = (LinearLayout) findViewById(R.id.ll_fangan);
    ll_hemai = (LinearLayout) findViewById(R.id.ll_hemai);
    fangan_hide_btn = (ImageButton) findViewById(R.id.fangan_hide_btn);
    hemai_hide_btn = (ImageButton) findViewById(R.id.hemai_hide_btn);
    lv_fangan = (MyListView2) findViewById(R.id.lv_fangan);
    lv_hemai = (MyListView2) findViewById(R.id.lv_hemai);
    fangan_divider = (ImageView) findViewById(R.id.fangan_divider);
    hemai_divider = (ImageView) findViewById(R.id.hemai_divider);
    tv_fangan = (TextView) findViewById(R.id.tv_fangan);
    tv_hemai = (TextView) findViewById(R.id.tv_hemai);
  }

  private void setListener() {
    ll_fangan.setOnClickListener(this);
    ll_hemai.setOnClickListener(this);
    fangan_hide_btn.setOnClickListener(this);
    hemai_hide_btn.setOnClickListener(this);
    btn_back.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    final int id = v.getId();
    if (id == R.id.btn_back) {
      FollowNumberActivity.this.finish();
    }
    if (id == R.id.ll_fangan || id == R.id.fangan_hide_btn) {
      if (fangan_hide_btn.isSelected()) {
        fangan_hide_btn.setSelected(false);
        lv_fangan.setVisibility(View.VISIBLE);
        fangan_divider.setVisibility(View.VISIBLE);
      } else {
        fangan_hide_btn.setSelected(true);
        lv_fangan.setVisibility(View.GONE);
        fangan_divider.setVisibility(View.GONE);
      }
    }
    if (id == R.id.ll_hemai || id == R.id.hemai_hide_btn) {

      if (hemai_hide_btn.isSelected()) {
        hemai_hide_btn.setSelected(false);
        lv_hemai.setVisibility(View.VISIBLE);
        hemai_divider.setVisibility(View.VISIBLE);
      } else {
        hemai_hide_btn.setSelected(true);
        lv_hemai.setVisibility(View.GONE);
        hemai_divider.setVisibility(View.GONE);
      }

    }
  }
}
