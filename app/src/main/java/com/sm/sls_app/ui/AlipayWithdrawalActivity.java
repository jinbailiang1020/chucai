package com.sm.sls_app.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.ui.WithdrawalActivity.MyAsynTask;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.StringUtils;
import com.sm.sls_app.view.MyBankSpinner;
import com.sm.sls_app.view.MyToast;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author 作者 : hxj
 * @version 创建时间：2015-12-15 下午7:08:24 类说明
 */
public class AlipayWithdrawalActivity extends Activity implements
    OnClickListener {
  public EditText et_question;
  private EditText et_money, et_custom, et_answer;
  private TextView tv_name;
  private Button btn_ok;
  private ImageButton btn_question, btn_back;
  private LinearLayout gone_ll;
  public static boolean isCustom = false;
  private MyBankSpinner spinner_question;
  private List<Map<String, String>> listQuestion = new ArrayList<Map<String, String>>();
  public int question_index = -1;
  private String qId, qAnswer, money;
  private MyAsynTask myAsynTask;
  private String opt, auth, info, time, imei, crc; // 格式化后的参数
  private MyHander mHander = new MyHander();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_alipay_withdrawal);
    findView();
    setListener();
  }

  /**
   * 初始隐藏自定义问题
   */
  public void setGoneView(boolean is) {
    if (is) {
      gone_ll.setVisibility(View.VISIBLE);
    } else {
      gone_ll.setVisibility(View.GONE);
    }
  }

  /**
   * 设置监听
   */
  private void setListener() {
    btn_back.setOnClickListener(this);
    btn_ok.setOnClickListener(this);
    btn_question.setOnClickListener(this);
  }

  /**
   * 初始化控件
   */
  private void findView() {
    opt = "69";
    time = RspBodyBaseBean.getTime();
    imei = RspBodyBaseBean.getIMEI(this);
    et_question = (EditText) findViewById(R.id.alipay_withdrawal_et_soft);
    et_money = (EditText) findViewById(R.id.alipay_withdrawal_et_money);
    et_custom = (EditText) findViewById(R.id.alipay_withdrawal_custom_et_question);
    et_answer = (EditText) findViewById(R.id.alipay_withdrawal_et_answer);
    tv_name = (TextView) findViewById(R.id.alipay_withdrawal_tv_alipay_name);
    tv_name.setText(AppTools.user.getAlipayName());
    btn_ok = (Button) findViewById(R.id.alipay_withdrawal_btn_ok);
    btn_question = (ImageButton) findViewById(R.id.alipay_withdrawal_btn_soft);
    btn_back = (ImageButton) findViewById(R.id.alipay_withdrawal_btn_back);
    gone_ll = (LinearLayout) findViewById(R.id.alipay_withdrawal_custom_linearlayout);
    setGoneView(isCustom);
    if (listQuestion.size() == 0) {
      getData();
    }
  }

  /** 解析问题的XML */
  private void getData() {
    // 先清除
    listQuestion.clear();
    XmlResourceParser xrp = getResources().getXml(R.xml.question);
    try {
      Map<String, String> map = null;
      // 直到文档的结尾处
      while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
        // 如果遇到了开始标签
        if (xrp.getEventType() == XmlResourceParser.START_TAG) {
          String tagName = xrp.getName();// 获取标签的名字
          if (tagName.equals("row")) {
            map = new HashMap<String, String>();
            String id = xrp.getAttributeValue(null, "id");// 通过属性名来获取属性值
            String nm = xrp.getAttributeValue(null, "question");// 通过属性名来获取属性值

            map.put("id", id);
            map.put("name", nm);

            listQuestion.add(map);
          }
        }
        xrp.next();// 获取解析下一个事件
      }
    } catch (XmlPullParserException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.alipay_withdrawal_btn_ok:
      if (question_index == 8) {
        qAnswer = et_custom.getText().toString().trim() + "|"
            + et_answer.getText().toString().trim();
      } else {
        qAnswer = et_answer.getText().toString().trim();
      }
      money = et_money.getText().toString().trim();
      if (question_index != -1) {
        Iterator i = listQuestion.get(question_index).entrySet().iterator();
        while (i.hasNext()) {
          Entry entry = (Entry) i.next();
          String key = (String) entry.getKey();
          if ("id".equals(key)) {
            qId = (String) entry.getValue();
          }
        }
      } else {
        Toast.makeText(AlipayWithdrawalActivity.this, "保密问题不能为空",
            Toast.LENGTH_SHORT).show();
        return;
      }

      if (qAnswer.length() == 0) {
        MyToast.getToast(AlipayWithdrawalActivity.this, "答案不能为空").show();
        return;
      } else if (money.length() == 0) {
        MyToast.getToast(AlipayWithdrawalActivity.this, "提款金额不能为空").show();
        return;
      } else if (Integer.parseInt(money) <= 1) {
        MyToast.getToast(AlipayWithdrawalActivity.this, "提款金额必须大于1元。").show();
      } else {
        if (question_index == 8) {
          if (StringUtils.isEmpty(et_custom.getText().toString().trim())) {
            MyToast.getToast(AlipayWithdrawalActivity.this, "自定义问题为空").show();
            return;
          }
        }
        btn_ok.setEnabled(false);
        myAsynTask = new MyAsynTask();
        myAsynTask.execute();
      }
      break;
    case R.id.alipay_withdrawal_btn_soft:
      spinner_question = new MyBankSpinner(AlipayWithdrawalActivity.this,
          listQuestion, question_index, AppTools.ALIPAY_WITHDRAWAL_TYPE,
          R.style.dialog);
      spinner_question.show();
      break;
    case R.id.alipay_withdrawal_btn_back:
      finish();
      break;
    default:
      break;
    }
  }

  /** 初始化提款申请参数 */
  private void init() {
    info = RspBodyBaseBean.changeWithdrawal_info(et_money.getText().toString()
        .trim(), qId, qAnswer);
    String key = MD5.md5(AppTools.user.getUserPass() + AppTools.MD5_key);
    crc = RspBodyBaseBean.getCrc(time, imei, key, info, AppTools.user.getUid());
    auth = RspBodyBaseBean.getAuth(crc, time, imei, AppTools.user.getUid());
  }

  String msgs;

  class MyAsynTask extends AsyncTask<Integer, Integer, String> {

    @Override
    protected String doInBackground(Integer... params) {
      init();
      String[] values = { opt, auth, info };
      System.out.println(info);
      String result = HttpUtils.doPost(AppTools.names, values, AppTools.path);
      System.out.println("提款提示：" + result);

      if ("-500".equals(result))
        return result;

      JSONObject object;
      try {
        object = new JSONObject(result);
        if ("0".equals(object.getString("error"))) {
          String userInfo = object.getString("dtUserInfo");
          JSONArray array = new JSONArray(userInfo);
          JSONObject item = array.getJSONObject(0);
          AppTools.user.setBalance(item.getLong("balance"));
          AppTools.user.setFreeze(item.getDouble("freeze"));
          Log.i("x", "提款成功");
        } else {
          msgs = object.getString("msg");
        }
        // 更新消息
        return object.getString("error");
      } catch (Exception e) {
        e.printStackTrace();
        Log.i("x", "提款失败" + e.getMessage());
      }
      return "1";
    }

    @Override
    protected void onPostExecute(String result) {
      // TODO Auto-generated method stub
      mHander.sendEmptyMessage(Integer.parseInt(result));
      super.onPostExecute(result);
    }

    @Override
    protected void onPreExecute() {
      // TODO Auto-generated method stub
      super.onPreExecute();
    }

  }

  class MyHander extends Handler {
    @Override
    public void handleMessage(Message msg) {
      btn_ok.setEnabled(true);
      switch (msg.what) {
      case 0:
        MyToast.getToast(AlipayWithdrawalActivity.this, "提款申请成功，请等待处理。").show();
        AlipayWithdrawalActivity.this.finish();
        break;

      default:
        MyToast.getToast(AlipayWithdrawalActivity.this, "操作失败:" + msgs).show();
        break;
      }
      super.handleMessage(msg);
    }
  }
}
