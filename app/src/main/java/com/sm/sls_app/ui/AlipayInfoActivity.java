package com.sm.sls_app.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.BaseHelper;
import com.sm.sls_app.utils.StringUtils;
import com.sm.sls_app.view.MyBankSpinner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * @author 作者 : hxj
 * @version 创建时间：2015-12-14 下午7:51:21 类说明
 */
public class AlipayInfoActivity extends Activity implements OnClickListener {
	private String opt, auth, info, time, imei, crc; // 格式化后的参数
	public EditText et_question, et_answer, et_custom, et_user, et_user_sec,
			et_alipay_name;
	public int question_index = -1;
	private RelativeLayout gone_rl, rl_question, rl_answer;
	private ImageView line, line_question, line_answer;
	private ImageButton back, btn_question;
	private Button btn_ok;
	/** 没有选自定义问题 */
	public static boolean isCustom = false;
	private List<Map<String, String>> listQuestion = new ArrayList<Map<String, String>>();
	private MyBankSpinner spinner_question;
	private String securityQuestionId, securityQuestionAnswer, alipayName,
			alipayUserName;
	private ProgressDialog mProgress;
	private MyHandler myHander = new MyHandler();
	private MyAsynTask myAsynTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alipay);
		getData(listQuestion, "question", R.xml.question);
		App.activityS.add(this);
		initView();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		btn_ok = (Button) findViewById(R.id.alipay_btn_ok);
		btn_ok.setOnClickListener(this);
		rl_question = (RelativeLayout) findViewById(R.id.alipay_question_relativelayout);
		rl_answer = (RelativeLayout) findViewById(R.id.alipay_answer_relativelayout);
		line_question = (ImageView) findViewById(R.id.alipay_question_line);
		line_answer = (ImageView) findViewById(R.id.alipay_answer_line);
		back = (ImageButton) findViewById(R.id.alipay_btn_back);
		back.setOnClickListener(this);
		et_question = (EditText) findViewById(R.id.alipay_et_soft);
		et_answer = (EditText) findViewById(R.id.alipay_et_answer);
		et_custom = (EditText) findViewById(R.id.alipay_et_custom_question);
		et_user = (EditText) findViewById(R.id.alipay_et_user);
		et_user_sec = (EditText) findViewById(R.id.alipay_et_user_sec);
		et_alipay_name = (EditText) findViewById(R.id.alipay_et_real_name);
		btn_question = (ImageButton) findViewById(R.id.alipay_btn_soft);
		btn_question.setOnClickListener(this);
		gone_rl = (RelativeLayout) findViewById(R.id.alipay_gone_relativelayout);
		line = (ImageView) findViewById(R.id.alipay_gone_line);
		setGoneView(isCustom);
		setQuestionAndAnswerView();
	}

	private void setQuestionAndAnswerView() {
		if (!StringUtils.isEmpty(AppTools.user.getSecurityquestion())) {
			rl_answer.setVisibility(View.GONE);
			rl_question.setVisibility(View.GONE);
			line_question.setVisibility(View.GONE);
			line_answer.setVisibility(View.GONE);
		} else {
			rl_answer.setVisibility(View.VISIBLE);
			rl_question.setVisibility(View.VISIBLE);
			line_question.setVisibility(View.VISIBLE);
			line_answer.setVisibility(View.VISIBLE);
		}
	}

	/** 解析银行和省的XML */
	private void getData(List<Map<String, String>> list, String name2, int xml) {
		// 先清除
		list.clear();
		XmlResourceParser xrp = getResources().getXml(xml);
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
						String nm = xrp.getAttributeValue(null, name2);// 通过属性名来获取属性值

						map.put("id", id);
						map.put("name", nm);

						list.add(map);
					}
				}
				xrp.next();// 获取解析下一个事件
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 初始隐藏自定义问题
	 */
	public void setGoneView(boolean is) {
		if (is) {
			gone_rl.setVisibility(View.VISIBLE);
			line.setVisibility(View.VISIBLE);
		} else {
			gone_rl.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		if (id == R.id.alipay_btn_back) {
			finish();
		} else if (id == R.id.alipay_btn_soft) {
			spinner_question = new MyBankSpinner(AlipayInfoActivity.this,
					listQuestion, question_index, AppTools.ALIPAY_TYPE,
					R.style.dialog);
			spinner_question.show();
		} else if (id == R.id.alipay_btn_ok) {
			doBind();
		}
	}

	/**
	 * 拿到 ID
	 * 
	 * @param index
	 * @param list
	 * @return
	 */
	private String getID(int index, List<Map<String, String>> list) {
		// 判断是否选择了省份
		if (index != -1) {
			Iterator i = list.get(index).entrySet().iterator();
			while (i.hasNext()) {
				Entry entry = (Entry) i.next();
				String key = (String) entry.getKey();
				if ("id".equals(key)) {
					return (String) entry.getValue();
				}
			}
		}
		return "8";
	}

	/**
	 * 提交绑定信息
	 */
	private void doBind() {
		if (isPerfect()) {
			myAsynTask = new MyAsynTask();
			myAsynTask.execute();

		}
	}

	/**
	 * 判断资料是否完善securityQuestionId, securityQuestionAnswer, alipayName,
	 * alipayUserName;
	 */
	private boolean isPerfect() {
		securityQuestionId = this.getID(question_index, listQuestion);
		if (StringUtils.isEmpty(et_user.getText().toString().trim())
				|| StringUtils.isEmpty(et_user_sec.getText().toString().trim())
				|| StringUtils.isEmpty(et_alipay_name.getText().toString()
						.trim())) {
			Toast.makeText(AlipayInfoActivity.this, "信息不完善", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (StringUtils.isEmpty(AppTools.user.getSecurityquestion())
				&& StringUtils.isEmpty(et_answer.getText().toString().trim())) {
			Toast.makeText(AlipayInfoActivity.this, "答案不能为空", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (!et_user.getText().toString().trim()
				.equals(et_user_sec.getText().toString().trim())) {
			Toast.makeText(AlipayInfoActivity.this, "两次输入的账号不一致",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (StringUtils.isEmpty(AppTools.user.getSecurityquestion())) {
			if (StringUtils.isEmpty(et_custom.getText().toString().trim())
					&& StringUtils.isEmpty(et_question.getText().toString()
							.trim())) {
				Toast.makeText(AlipayInfoActivity.this, "安全问题不能为空",
						Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		if (question_index == 8) {
			if (StringUtils.isEmpty(et_custom.getText().toString().trim())) {
				Toast.makeText(AlipayInfoActivity.this, "信息不完善",
						Toast.LENGTH_SHORT).show();
				return false;
			}

			securityQuestionAnswer = et_custom.getText().toString().trim()
					+ "|" + et_answer.getText().toString().trim();
		} else {
			securityQuestionAnswer = et_answer.getText().toString().trim();
		}
		alipayName = et_user.getText().toString().trim();
		alipayUserName = et_alipay_name.getText().toString().trim();
		return true;
	}

	String msgs = "";

	class MyAsynTask extends AsyncTask<Integer, Integer, String> {
		String error = "-1001";

		@Override
		protected String doInBackground(Integer... params) {
			opt = "68";
			System.out.println("alipayName" + alipayName + "alipayUserName"
					+ alipayUserName + "securityQuestionId"
					+ securityQuestionId + "securityQuestionAnswer"
					+ securityQuestionAnswer);
			info = RspBodyBaseBean.changeAlipayInfo_info(alipayName,
					alipayUserName, securityQuestionId, securityQuestionAnswer);
			System.out.println("支付宝信息的info:" + info);

			String key = MD5
					.md5(AppTools.user.getUserPass() + AppTools.MD5_key);
			crc = RspBodyBaseBean.getCrc(time, imei, key, info,
					AppTools.user.getUid());
			auth = RspBodyBaseBean.getAuth(crc, time, imei,
					AppTools.user.getUid());

			String[] values = { opt, auth, info };
			String result = HttpUtils.doPost(AppTools.names, values,
					AppTools.path);
			System.out.println("result:----" + result);
			try {
				JSONObject ob = new JSONObject(result);
				if ("0".equals(ob.getString("error"))) {
					return "0";
				} else {
					error = ob.getString("error");
					msgs = ob.getString("msg");
				}
			} catch (Exception e) {
				e.printStackTrace();

			}
			return error;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (mProgress != null)
				mProgress.dismiss();
			myHander.sendEmptyMessage(Integer.parseInt(result));
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgress = BaseHelper.showProgress(AlipayInfoActivity.this, null,
					"绑定中....", true, true);
		}

	}

	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Toast.makeText(AlipayInfoActivity.this, "绑定成功",
						Toast.LENGTH_LONG).show();
				AlipayInfoActivity.this.finish();
				break;
			case -169:
				Toast.makeText(AlipayInfoActivity.this, msgs,
						Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(AlipayInfoActivity.this, "绑定失败，请重试",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}

	}

}
