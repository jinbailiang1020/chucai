package com.sm.sls_app.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.BaseHelper;
import com.sm.sls_app.utils.StringUtils;
import com.sm.sls_app.view.MyBankSpinner;
import com.sm.sls_app.view.MyToast;

/**
 * 银行信息
 * 
 * @author SLS003
 * 
 */
public class BankInfoActivity extends Activity implements OnClickListener {

	private String opt, auth, info, time, imei, crc; // 格式化后的参数

	public EditText et_bankName, et_shi, et_sheng, et_question, et_answer,
			et_bankNumber, et_custom, et_bankUserName;

	private RelativeLayout btn_bank, btn_sheng, rl_question, rl_answer;
	private ImageButton btn_question, btn_back;
	private ImageView line, line_question, line_answer;
	private Button btn_ok;
	private ProgressDialog mProgress;
	private RelativeLayout gone_rl;
	private List<Map<String, String>> listBank = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> listProvince = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> listCity = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> listZhi = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> listQuestion = new ArrayList<Map<String, String>>();

	private String bankInProvinceId = ""; // 省id

	private MyBankSpinner spinner_bank, spinner_province, spinner_city,
			spinner_zhi, spinner_question;

	public int bank_index = 1;// 银行类型
	public int province_index = -1;// 省id
	public int city_index = -1;// 市id
	public int zhi_index = -1;//
	public int question_index = -1;// 问题id

	/** 所有下拉框参数的id **/
	private String bankTypeId = "", bankId = "", bankCardNumber = "",
			bankInCityId = "", securityQuestionId = "",
			securityQuestionAnswer = "", bankUserName = "";

	/** 城市iD **/
	private String cityId;

	// private MyAsynTask myAsynTask;

	private MyAsynTask2 myA;
	private MyHander myHander;

	/** 没有选取省份 **/
	private final static int ERROR_PROVINCE = 1;
	/** 没有选取城市 **/
	private final static int ERROR_CITY = 2;
	/** 没有选取银行类型 **/
	private final static int ERROR_BANK_TYPE = 3;
	/** 没有选取银行支行类型 **/
	private final static int ERROR_BANK_ZHI = 4;
	/** 没有选自定义问题 */
	public static boolean isCustom = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bankinfo);

		App.activityS.add(this);

		findView();

		setListener();

		getData(listProvince, "provincename", R.xml.province);

		getData(listBank, "bankname", R.xml.bank);

		getData(listQuestion, "question", R.xml.question);
	}

	// /** 初始化拿分行的参数 */
	// private void init() {
	// if (isFull()) {
	// opt = "40";
	// time = RspBodyBaseBean.getTime();
	// imei = RspBodyBaseBean.getIMEI(this);
	// info = RspBodyBaseBean.changeZhihang_info(cityId, bankTypeId);
	//
	// String key = MD5
	// .md5(AppTools.user.getUserPass() + AppTools.MD5_key);
	// crc = RspBodyBaseBean.getCrc(time, imei, key, info,
	// AppTools.user.getUid());
	// auth = RspBodyBaseBean.getAuth(crc, time, imei,
	// AppTools.user.getUid());
	//
	// myAsynTask = new MyAsynTask();
	// myAsynTask.execute();
	// }
	// }

	/** 判断银行信息 和省信息是否为空 */
	private boolean isFull() {
		boolean b = true;
		// 判断银行类型是否为空
		if (bank_index != -1) {
			Iterator i = listBank.get(bank_index).entrySet().iterator();
			while (i.hasNext()) {
				Entry entry = (Entry) i.next();
				String key = (String) entry.getKey();
				if ("id".equals(key)) {
					bankTypeId = (String) entry.getValue();
				}
			}
		} else {
			myHander.sendEmptyMessage(ERROR_BANK_TYPE);
			b = false;
		}
		// 判断是否选择城市
		if (city_index != -1) {
			Iterator i = listCity.get(city_index).entrySet().iterator();
			while (i.hasNext()) {
				Entry entry = (Entry) i.next();
				String key = (String) entry.getKey();
				if ("id".equals(key)) {
					cityId = (String) entry.getValue();
				}
			}
		} else {
			myHander.sendEmptyMessage(ERROR_CITY);
			b = false;
		}
		return b;
	}

	/** 得到支行信息 */
	private int getZhi() {
		listZhi.clear();
		String[] values = { opt, auth, info };

		Log.i("x", "info=====" + info);
		String result = HttpUtils.doPost(AppTools.names, values, AppTools.path);

		Log.i("x", "得到支行信息" + result);
		try {
			JSONObject object = new JSONObject(result);

			if ("0".equals(object.optString("error"))) {
				// 得到所有支行信息
				String dtBranchBankInfo = object.optString("dtBranchBankInfo");
				JSONArray array = new JSONArray(dtBranchBankInfo);
				if (array.toString().length() > 0) {
					Map<String, String> map = null;
					for (int i = 0; i < array.length(); i++) {
						map = new HashMap<String, String>();
						JSONObject items = array.getJSONObject(i);
						map.put("id", items.getString("id"));
						map.put("name", items.getString("bankName"));
						listZhi.add(map);
					}
				}
				return 0;
			} else {
				return -108;
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("s", "得分行的时候出错" + e.getMessage());
			return -100;
		}
	}

	/** 初始化UI */
	private void findView() {
		rl_question = (RelativeLayout) findViewById(R.id.bankinfo_question_relativelayout);
		rl_answer = (RelativeLayout) findViewById(R.id.bankinfo_answer_relativelayout);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_bank = (RelativeLayout) this.findViewById(R.id.bankinfo_btn_name);
		btn_sheng = (RelativeLayout) this
				.findViewById(R.id.bankinfo_btn_location);
		btn_question = (ImageButton) this.findViewById(R.id.bankinfo_btn_soft);
		btn_ok = (Button) this.findViewById(R.id.bankInfo_btn_ok);
		et_custom = (EditText) findViewById(R.id.bangkinfo_et_custom_question);
		et_bankName = (EditText) this.findViewById(R.id.bangkinfo_et_name);
		et_sheng = (EditText) this.findViewById(R.id.bankinfo_et_location);
		et_shi = (EditText) this.findViewById(R.id.bangkinfo_et_location2);
		et_question = (EditText) this.findViewById(R.id.bankinfo_et_soft);
		et_answer = (EditText) this.findViewById(R.id.bangkinfo_et_answer);
		et_bankNumber = (EditText) this.findViewById(R.id.bangkinfo_et_cardNum);
		et_bankUserName = (EditText) this
				.findViewById(R.id.bangkinfo_et_bankusername);
		line = (ImageView) findViewById(R.id.bankinfo_gone_line);
		line_question = (ImageView) findViewById(R.id.bankinfo_question_gone_line);
		line_answer = (ImageView) findViewById(R.id.bankinfo_answer_gone_line);
		gone_rl = (RelativeLayout) findViewById(R.id.bankinfo_gone_relativelayout);
		setQuestionAndAnswerView();
		setGoneView(isCustom);
		myHander = new MyHander();

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

	/** 绑定监听 */
	private void setListener() {
		// TODO Auto-generated method stub
		btn_bank.setOnClickListener(this);
		btn_sheng.setOnClickListener(this);
		btn_question.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
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

	/** 解析城市的XML */
	private void getData2(String bankInProvinceId) {
		listCity.clear();
		XmlResourceParser xrp = getResources().getXml(R.xml.city);
		try {
			// 直到文档的结尾处
			while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
				// 如果遇到了开始标签
				if (xrp.getEventType() == XmlResourceParser.START_TAG) {
					String tagName = xrp.getName();// 获取标签的名字
					if (tagName.equals("row")) {
						Map<String, String> map = new HashMap<String, String>();

						String proId = xrp
								.getAttributeValue(null, "provinceid");// 通过属性名来获取属性值
						if (bankInProvinceId.equals(proId)) {
							String id = xrp.getAttributeValue(null, "id");// 通过属性名来获取属性值
							String cityname = xrp.getAttributeValue(null,
									"cityname");// 通过属性名来获取属性值
							map.put("id", id);
							map.put("name", cityname);
							listCity.add(map);
						}
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.bankinfo_btn_name:
			spinner_bank = new MyBankSpinner(BankInfoActivity.this, listBank,
					bank_index, AppTools.BANK_TYPE, R.style.dialog);
			spinner_bank.show();
			break;
		case R.id.bankinfo_btn_location:
			spinner_province = new MyBankSpinner(BankInfoActivity.this,
					listProvince, province_index, AppTools.PROVINCE_TYPE,
					R.style.dialog);
			spinner_province.show();
			break;
		// case R.id.bangkinfo_et_fullName:
		//
		// case R.id.bankinfo_btn_fullName:
		// init();
		// break;
		case R.id.bankinfo_btn_soft:
			spinner_question = new MyBankSpinner(BankInfoActivity.this,
					listQuestion, question_index, AppTools.QUESTION_TYPE,
					R.style.dialog);
			spinner_question.show();
			break;
		case R.id.bankInfo_btn_ok:
			doBind();
			// myA = new MyAsynTask2();
			// myA.execute();
			break;
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
		return "";
	}

	/**
	 * 提交绑定信息 bankTypeId + "---" + bankId + "-----" + bankInCityId + "----" +
	 * securityQuestionId + "---" + securityQuestionAnswer + "---" +
	 * bankCardNumber
	 */
	private void doBind() {
		bankTypeId = this.getID(bank_index, listBank);
		bankId = this.getID(zhi_index, listZhi);
		bankInCityId = this.getID(city_index, listCity);
		securityQuestionId = this.getID(question_index, listQuestion);
		if (null == securityQuestionId || securityQuestionId.length() == 0) {
			securityQuestionId = "8";
		}
		if (question_index == 8) {
			if (StringUtils.isEmpty(et_custom.getText().toString().trim())
					|| StringUtils.isEmpty(et_answer.getText().toString()
							.trim())) {
				Toast.makeText(BankInfoActivity.this, "信息不完善",
						Toast.LENGTH_SHORT).show();
				return;
			}
			securityQuestionAnswer = et_custom.getText().toString().trim()
					+ "|" + et_answer.getText().toString().trim();
		} else {
			securityQuestionAnswer = et_answer.getText().toString().trim();
		}
		bankCardNumber = et_bankNumber.getText().toString().trim();
		bankUserName = et_bankUserName.getText().toString().trim();
		if (StringUtils.isEmpty(AppTools.user.getSecurityquestion())) {
			if (securityQuestionId.length() != 0
					&& securityQuestionAnswer.length() != 0
					&& bankCardNumber.length() != 0 && bankTypeId.length() != 0
					&& bankInProvinceId.length() != 0
					&& bankInCityId.length() != 0 && bankUserName.length() != 0) {
				myA = new MyAsynTask2();
				myA.execute();

			} else {
				Toast.makeText(BankInfoActivity.this, "信息不完善",
						Toast.LENGTH_SHORT).show();
			}
		} else if (!StringUtils.isEmpty(AppTools.user.getSecurityquestion())) {
			if (bankTypeId.length() != 0 && bankInProvinceId.length() != 0
					&& bankInCityId.length() != 0
					&& bankCardNumber.length() != 0
					&& bankUserName.length() != 0) {
				myA = new MyAsynTask2();
				myA.execute();

			} else {
				Toast.makeText(BankInfoActivity.this, "信息不完善",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	/** 刷新 Adapter */
	public void updateAdapter() {
		spinner_bank.updateAdapter();
		spinner_province.updateAdapter();
		spinner_city.updateAdapter();
	}

	/** 异步任务 */
	// class MyAsynTask extends AsyncTask<Void, Integer, String> {
	//
	// @Override
	// protected void onPreExecute() {
	// // TODO Auto-generated method stub
	// super.onPreExecute();
	// mProgress = BaseHelper.showProgress(BankInfoActivity.this, null,
	// "加载中....", true, true);
	// }
	//
	// @Override
	// protected String doInBackground(Void... params) {
	// int i = getZhi();
	// return i + "";
	// }
	//
	// @Override
	// protected void onPostExecute(String result) {
	// // TODO Auto-generated method stub
	// if (Integer.parseInt(result) == 0) {
	// if (spinner_zhi != null && spinner_zhi.isShowing()) {
	// spinner_zhi.dismiss();
	// spinner_zhi = null;
	// }
	// spinner_zhi = new MyBankSpinner(BankInfoActivity.this, listZhi,
	// zhi_index, AppTools.ZHI_TYPE, R.style.dialog);
	// spinner_zhi.show();
	// if (mProgress != null)
	// mProgress.dismiss();
	//
	// } else {
	// myHander.sendEmptyMessage(Integer.parseInt(result));
	// }
	// super.onPostExecute(result);
	// }
	// }
	private String msgs = "";

	/** 异步任务 */
	class MyAsynTask2 extends AsyncTask<Void, Integer, String> {
		String error = "-1001";

		@Override
		protected String doInBackground(Void... params) {
			Log.i("x", "绑定操作");
			opt = "36";
			System.out.println(bankTypeId + "---" + bankId + "-----"
					+ bankInCityId + "----" + securityQuestionId + "---"
					+ securityQuestionAnswer + "---" + bankCardNumber);
			info = RspBodyBaseBean.changeBankinfo_info(bankTypeId, bankId,
					bankCardNumber, bankInProvinceId, bankInCityId,
					bankUserName, securityQuestionId, securityQuestionAnswer);

			System.out.println("银行账户信息的info:" + info);

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
				} else if ("-169".equals(ob.getString("error"))) {
					 msgs = ob.getString("msg");
					 error = ob.getString("error");
				} else {
					error = ob.getString("error");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			return error;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgress = BaseHelper.showProgress(BankInfoActivity.this, null,
					"绑定中....", true, true);
		}

		@Override
		protected void onPostExecute(String result) {
			if (mProgress != null)
				mProgress.dismiss();
			myHander.sendEmptyMessage(Integer.parseInt(result));
			super.onPostExecute(result);
		}

	}

	/** 重写返回键事件 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			int s = App.activityS.size() - 1;
			App.activityS.get(s).finish();
			App.activityS.get(s - 1).finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/** MyHander */
	@SuppressLint("HandlerLeak")
	class MyHander extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ERROR_BANK_TYPE:
				Toast.makeText(BankInfoActivity.this, "银行不能为空",
						Toast.LENGTH_SHORT).show();
				break;
			case ERROR_BANK_ZHI:
				Toast.makeText(BankInfoActivity.this, "银行支行不能为空",
						Toast.LENGTH_SHORT).show();
				break;
			case ERROR_CITY:
				Toast.makeText(BankInfoActivity.this, "城市不能为空",
						Toast.LENGTH_SHORT).show();
				break;
			case ERROR_PROVINCE:
				Toast.makeText(BankInfoActivity.this, "省份不能为空",
						Toast.LENGTH_SHORT).show();
				break;
			case 0:
				Toast.makeText(BankInfoActivity.this, "绑定成功", Toast.LENGTH_LONG)
						.show();
				for (Activity a : AccountInformationActivity.list) {
					a.finish();
				}
				BankInfoActivity.this.finish();
				break;
			case -100:
				MyToast.getToast(getApplicationContext(), "银行卡号格式正确。").show();
				break;
			case -3601:
				MyToast.getToast(getApplicationContext(), "银行卡长度不正确").show();
				break;
			case -108:
				MyToast.getToast(getApplicationContext(), "查询不到指定的银行的支行。")
						.show();

				break;
			case -169:
				MyToast.getToast(getApplicationContext(), msgs).show();

				break;
			}
			super.handleMessage(msg);
		}

	}

	public void changCity() {
		// 判断是否选择了省份
		if (spinner_city != null && spinner_city.isShowing()) {
			spinner_city.dismiss();
		}
		if (province_index != -1) {
			Iterator i = listProvince.get(province_index).entrySet().iterator();
			while (i.hasNext()) {
				Entry entry = (Entry) i.next();
				String key = (String) entry.getKey();
				if ("id".equals(key)) {
					bankInProvinceId = (String) entry.getValue();
					getData2(bankInProvinceId);
				}
			}
			spinner_city = new MyBankSpinner(BankInfoActivity.this, listCity,
					city_index, AppTools.CITY_TYPE, R.style.dialog);
			spinner_city.show();
		} else {
			myHander.sendEmptyMessage(ERROR_PROVINCE);
		}
	}

}
