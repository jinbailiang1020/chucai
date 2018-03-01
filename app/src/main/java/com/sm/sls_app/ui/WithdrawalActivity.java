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
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.ui.WithdrawalActivity;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.StringUtils;
import com.sm.sls_app.view.MyBankSpinner;
import com.sm.sls_app.view.MyToast;

/**
 * 银行卡提款
 * 
 * @author SLS003
 */
public class WithdrawalActivity extends Activity implements OnClickListener {
  private TextView tv_bankName, tv_cardNum, tv_name;
  private EditText et_money;
  public EditText et_question;
  private EditText et_answer, et_custom;
  private Button btn_ok;
  private ImageButton btn_back, btn_question;
  private String opt, auth, info, time, imei, crc; // 格式化后的参数

  private MyBankSpinner spinner_question;
  public int question_index = -1;

  private MyAsynTask myAsynTask;

  private String qId, qAnswer, money;

  private List<Map<String, String>> listQuestion = new ArrayList<Map<String, String>>();

  private MyHander mHander = new MyHander();
  public static boolean isCustom = false;
  public LinearLayout gone_ll;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_withdrawal);
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

  /** 初始化提款申请参数 */
  private void init() {
    info = RspBodyBaseBean.changeWithdrawal_info(et_money.getText().toString()
        .trim(), qId, qAnswer);
    String key = MD5.md5(AppTools.user.getUserPass() + AppTools.MD5_key);
    crc = RspBodyBaseBean.getCrc(time, imei, key, info, AppTools.user.getUid());
    auth = RspBodyBaseBean.getAuth(crc, time, imei, AppTools.user.getUid());
  }

  /** 初始化UI */
  private void findView() {
    opt = "37";
    time = RspBodyBaseBean.getTime();
    imei = RspBodyBaseBean.getIMEI(this);
    gone_ll = (LinearLayout) findViewById(R.id.withdrawal_custom_linearlayout);
    btn_back = (ImageButton) findViewById(R.id.btn_back);
    tv_bankName = (TextView) this.findViewById(R.id.withdrawal_tv_bankName2);
    // tv_location = (TextView) this
    // .findViewById(R.id.withdrawal_tv_location2);
    // tv_zhiName = (TextView)
    // this.findViewById(R.id.withdrawal_tv_fullName2);
    tv_cardNum = (TextView) this.findViewById(R.id.withdrawal_tv_bankNum2);
    tv_name = (TextView) this.findViewById(R.id.withdrawal_tv_name2);

    et_money = (EditText) this.findViewById(R.id.withdrawal_et_money);
    et_question = (EditText) this.findViewById(R.id.withdrawal_et_soft);
    et_answer = (EditText) this.findViewById(R.id.withdrawal_et_answer);
    et_custom = (EditText) findViewById(R.id.withdrawal_custom_et_question);
    btn_question = (ImageButton) this.findViewById(R.id.withDrawal_btn_soft);
    btn_ok = (Button) this.findViewById(R.id.withdrawal_btn_ok);

    tv_bankName.setText(AppTools.user.getBankName());
    // tv_location.setText(AppTools.user.getProvinceName()
    // + AppTools.user.getCityName());
    // tv_zhiName.setText(AppTools.user.getFullName());
    tv_cardNum.setText(AppTools.user.getBangNum());
    tv_name.setText(AppTools.user.getRealityName());
    setGoneView(isCustom);
    if (listQuestion.size() == 0) {
      getData();
    }
  }

  /** 绑定监听 */
  private void setListener() {
    btn_back.setOnClickListener(this);
    btn_ok.setOnClickListener(this);
    btn_question.setOnClickListener(this);
    et_money.addTextChangedListener(watcher);
  }

  /** 点击监听 */
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.withdrawal_btn_ok:
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
        Toast.makeText(WithdrawalActivity.this, "保密问题不能为空", Toast.LENGTH_SHORT)
            .show();
        return;
      }

      if (qAnswer.length() == 0) {
        MyToast.getToast(WithdrawalActivity.this, "答案不能为空").show();
        return;
      } else if (money.length() == 0) {
        MyToast.getToast(WithdrawalActivity.this, "提款金额不能为空").show();
        return;
      } else if (Integer.parseInt(money) <= 1) {
        MyToast.getToast(WithdrawalActivity.this, "提款金额必须大于1元。").show();
      } else {
        if (question_index == 8) {
          if (StringUtils.isEmpty(et_custom.getText().toString().trim())) {
            MyToast.getToast(WithdrawalActivity.this, "自定义问题为空").show();
            return;
          }
        }
        btn_ok.setEnabled(false);
        myAsynTask = new MyAsynTask();
        myAsynTask.execute();
      }
      break;
    case R.id.withDrawal_btn_soft:
      spinner_question = new MyBankSpinner(WithdrawalActivity.this,
          listQuestion, question_index, AppTools.QUESTION_TYPE2, R.style.dialog);
      spinner_question.show();
      break;
    case R.id.btn_back:
      finish();
      break;
    }
  }

  /** 文本框 监听 */
  private TextWatcher watcher = new TextWatcher() {

    @Override
    public void afterTextChanged(Editable s) {
      if ((s.toString().trim()).length() == 0) {
        et_money.setText("1");
        money = "1";
        return;
      } else if (Double.parseDouble(s.toString().trim()) > Double
          .parseDouble(AppTools.user.getBalance())) {
        et_money.setText(AppTools.user.getBalance());
        Toast.makeText(WithdrawalActivity.this, "提款金额不能大于账户余额",
            Toast.LENGTH_LONG).show();
        return;
      }
      money = s.toString().trim();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
        int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
  };

  String msgs;

  /** 异步任务 */
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

    /** 最开始执行的 */
    @Override
    protected void onPreExecute() {
      // TODO Auto-generated method stub
      super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
      // TODO Auto-generated method stub
      mHander.sendEmptyMessage(Integer.parseInt(result));
      super.onPostExecute(result);
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

  @SuppressLint("HandlerLeak")
  class MyHander extends Handler {
    @Override
    public void handleMessage(Message msg) {
      btn_ok.setEnabled(true);
      switch (msg.what) {
      case 0:
        MyToast.getToast(WithdrawalActivity.this, "提款申请成功，请等待处理。").show();
        WithdrawalActivity.this.finish();
        break;
      case -174:
        MyToast.getToast(WithdrawalActivity.this, msgs).show();
        et_answer.setText("");
        break;
      case -178:
        MyToast.getToast(WithdrawalActivity.this, "余额不足").show();
        break;
      case -500:
        MyToast.getToast(WithdrawalActivity.this, "连接超时").show();
        break;
      case -176:
        MyToast.getToast(WithdrawalActivity.this, "请完善银行卡信息，开户名未绑定").show();
        break;
      default:
        MyToast.getToast(WithdrawalActivity.this, "操作失败").show();
        break;
      }
      super.handleMessage(msg);
    }
  }
}
