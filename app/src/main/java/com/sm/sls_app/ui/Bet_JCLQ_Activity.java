package com.sm.sls_app.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.DtMatch_Basketball;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.ui.adapter.ExpandAdapter_jclq;
import com.sm.sls_app.ui.adapter.MyBetLotteryJCLQAdapter;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.NumberTools;
import com.sm.sls_app.view.ConfirmDialog;
import com.sm.sls_app.view.MyListView2;
import com.sm.sls_app.view.MyToast;
import com.sm.sls_app.view.SelectPassTypePopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 功能：竞彩足球投注页面 版本
 * 
 * @author Administrator
 */
@SuppressLint({ "UseSparseArrays", "HandlerLeak" })
public class Bet_JCLQ_Activity extends Activity implements OnClickListener {
	
	private static final String TAG="Bet_JCLQ_Activity";

	private RelativeLayout layout_main, layout_cbs, layout_notjc, layout_jc;

	private ImageView bet_btn_deleteall;

	private LinearLayout btn_continue_select;

	private ImageButton btn_back;

	private String opt = "11"; // 格式化后的 opt

	private String auth, info, time, imei, crc; // 格式化后的参数

	private Button btn_clear, btn_join, btn_pay;
	
	private TextView bet_tv_guize; // 委托投注规则
	// 发起合买
	private EditText et_bei;

	private TextView tv_count, tv_money, btn_type;

	private Intent intent;

	private MyHandler myHandler;

//	private MyAsynTask myAsynTask;

	private MyListView2 listView;

	private MyBetLotteryJCLQAdapter betAdapter;

//	/** 所选的集合 */
	public HashMap<Integer, HashMap<Integer, String>> select_hashMap = new HashMap<Integer, HashMap<Integer, String>>();

	// private List<DtMatch> listMatch;
	/** 装 组下标 — 子类下标 的集合 **/
	private List<String> listStr;

	private List<String> listResult = new ArrayList<String>();
	private List<String> listResult_dan = new ArrayList<String>();

	private Set<Integer> checkIndex;

	private int radio_index = -1;

	/** 选择了几场比赛 **/
	public int total = 0;

	private long totalCount = 0;

	/** 玩法Id **/
	private int type2;

	private TextView tv_title;

	private String passType = "";

	private SelectPassTypePopupWindow selectPTpop;// 选择过关方式

	private int viewPagerCurrentIndex = 0;

	private ArrayList<String> playtype_list;
	
	private ConfirmDialog dialog;//提示框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bet_jczq);
		App.activityS.add(this);
		App.activityS1.add(this);
	}

	/** 初始化属性 */
	private void init() {
		type2 = getIntent().getIntExtra("type", 7301);
		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(Bet_JCLQ_Activity.this);
		dialog = new ConfirmDialog(this, R.style.dialog);
	}

	/** 初始化UI */
	private void findView() {
		myHandler = new MyHandler();
		layout_main = (RelativeLayout) this.findViewById(R.id.layout_main);
		tv_title = (TextView) this.findViewById(R.id.btn_playtype);
		tv_title.setText("竞彩篮球投注");
		bet_tv_guize = (TextView) this.findViewById(R.id.bet_tv_guize);
		layout_cbs = (RelativeLayout) this.findViewById(R.id.layout_cbs);
		layout_notjc = (RelativeLayout) this.findViewById(R.id.layout_notjc);
		layout_jc = (RelativeLayout) this.findViewById(R.id.layout_jc);
		bet_btn_deleteall = (ImageView) this
				.findViewById(R.id.bet_btn_deleteall);
		btn_continue_select = (LinearLayout) this
				.findViewById(R.id.btn_continue_select);
		btn_back = (ImageButton) this.findViewById(R.id.btn_back);

		btn_clear = (Button) this.findViewById(R.id.btn_clearall);
		btn_pay = (Button) findViewById(R.id.btn_submit);
		btn_join = (Button) this.findViewById(R.id.btn_follow);
		btn_type = (TextView) this.findViewById(R.id.tv_show_passway);

		et_bei = (EditText) this.findViewById(R.id.et_bei);
		tv_count = (TextView) this.findViewById(R.id.tv_tatol_count);
		tv_money = (TextView) this.findViewById(R.id.tv_tatol_money);
		listView = (MyListView2) this.findViewById(R.id.bet_lv_scheme);
		setListStr();

		betAdapter = new MyBetLotteryJCLQAdapter(Bet_JCLQ_Activity.this,
				listStr, type2);
		setApp();
		// 隐藏与显示
		btn_clear.setVisibility(View.GONE);
		// iv_up_down.setVisibility(View.GONE);
		layout_notjc.setVisibility(View.GONE);
		layout_cbs.setVisibility(View.GONE);
		btn_join.setVisibility(View.VISIBLE);
		layout_jc.setVisibility(View.VISIBLE);
		Spanned text = Html.fromHtml(AppTools.changeStringColor("#808080",
				"过关方式（")
				+ AppTools.changeStringColor("#e3393c", "必选")
				+ AppTools.changeStringColor("#808080", "）"));
		btn_type.setText(text);// 过关
		btn_type.setBackgroundResource(R.drawable.select_jc_bg_white);// 设置背景
        btn_pay.setText("付款");
	}

	/** 给选的值赋值 **/
	public void setListStr(HashMap<Integer, HashMap<Integer, String>> hashMap) {
//		System.out.println("bet_toString ====="+hashMap.toString());
		listStr = new ArrayList<String>();
		Set set = hashMap.entrySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			HashMap<Integer, String> mm = new HashMap<Integer, String>();
			String str = "";
			Map.Entry mapentry = (Map.Entry) it.next();
			str = (Integer) mapentry.getKey() + "";
			Set set2 = (hashMap.get(mapentry.getKey())).entrySet();
			Iterator it2 = set2.iterator();
			while (it2.hasNext()) {
				String str2 = "";
				Map.Entry map = (Map.Entry) it2.next();

				mm.put(Integer.parseInt(map.getKey().toString()), map
						.getValue().toString());

				str2 = str + "-" + map.getKey();
				listStr.add(str2);
			}
			select_hashMap.put(Integer.parseInt(mapentry.getKey().toString()),
					mm);
//			System.out.println("bet_toString ====="+select_hashMap.toString());
		}
	}

	public void setListStr() {
		switch (type2) {
		case 7301:
			setListStr(ExpandAdapter_jclq.map_hashMap_sf);
			break;
		case 7304:
			setListStr(ExpandAdapter_jclq.map_hashMap_dx);
			break;
		case 7302:
			setListStr(ExpandAdapter_jclq.map_hashMap_rfsf);
			break;
		case 7303:
			setListStr(ExpandAdapter_jclq.map_hashMap_cbf);
			break;
		case 7306:
			setListStr(ExpandAdapter_jclq.map_hashMap_hhtz);
			break;
		}
	}

	/** 设置监听 */
	private void setListener() {
		listView.setAdapter(betAdapter);
		bet_btn_deleteall.setOnClickListener(this);
		btn_continue_select.setOnClickListener(this);
		bet_tv_guize.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		btn_type.setOnClickListener(this);
		btn_pay.setOnClickListener(this);
		btn_join.setOnClickListener(this);
	}

	/** 当文本的值改变时 */
	private TextWatcher bei_textWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
		}

		@Override
		public void afterTextChanged(Editable edt) {
			// TODO Auto-generated method stub
			if (edt.toString().trim().length() != 0) {
				if (Integer.parseInt(edt.toString().trim()) > 999) {
					et_bei.setText("999");
					et_bei.setSelection(et_bei.getText().length());
					MyToast.getToast(getApplicationContext(), "最大倍数为999")
							.show();
				}
				if (Integer.parseInt(edt.toString().trim()) == 0) {
					et_bei.setText("1");
					et_bei.setSelection(et_bei.getText().length());
					MyToast.getToast(getApplicationContext(), "最小为1倍").show();
				}
				if (edt.toString().substring(0, 1).equals("0")) {
					et_bei.setText(edt.toString().substring(1,
							edt.toString().length()));
					et_bei.setSelection(0);
				}
			}
			setCountText();
		}
	};

	/** update Adapter **/
	public void updateAdapter() {
		total = 0;
		betAdapter.notifyDataSetChanged();
		for (int i = 0; i < select_hashMap.size(); i++) {
			total += select_hashMap.get(i).size();
		}

		setTotalCount();
		setCountText();
	}

	/** 清空 **/
	public void doClear() {
		dialog.show();
		dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
		dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
			@Override
			public void getResult(int resultCode) {
				// TODO Auto-generated method stub
				if (1 == resultCode) {// 确定
					for (String str : listStr) {
						Integer s = Integer.parseInt(str.split("-")[0]);
						// 清空
						select_hashMap.get(s).clear();
					}
					betAdapter.clear();
					Bet_JCLQ_Activity.this.passType = "";
					if (null != checkIndex)
						Bet_JCLQ_Activity.this.checkIndex.clear();
					Bet_JCLQ_Activity.this.radio_index = -1;
					updateAdapter();
				}
			}
		});
	}

	/** 增加一注 **/
	public void doAdd() {
		// this.btn_type.setText("选择过关方式(必选)");
		this.passType = "";
		if (null != checkIndex)
			this.checkIndex.clear();
		this.radio_index = -1;
		setListResult();
		betAdapter.list_dan.clear();
		intent = new Intent(Bet_JCLQ_Activity.this, Select_jclqActivity.class);
		intent.putExtra("playType", type2);
		intent.putExtra("canChange", false);
		Bet_JCLQ_Activity.this.startActivity(intent);
	}

	/** 给所选集合赋值 **/
	public void setListResult() {
		HashMap<Integer, HashMap<Integer, String>> hashMap = null;
		switch (type2) {
		case 7301:
			hashMap = ExpandAdapter_jclq.map_hashMap_sf;
			break;
		case 7304:
			hashMap = ExpandAdapter_jclq.map_hashMap_dx;
			break;
		case 7302:
			hashMap = ExpandAdapter_jclq.map_hashMap_rfsf;
			break;
		case 7303:
			hashMap = ExpandAdapter_jclq.map_hashMap_cbf;
			break;
		case 7306:
			hashMap = ExpandAdapter_jclq.map_hashMap_hhtz;
			break;
		}
		listStr.clear();
		Set set = select_hashMap.entrySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			HashMap<Integer, String> mm = new HashMap<Integer, String>();
			String str = "";
			Map.Entry mapentry = (Map.Entry) it.next();
			str = (Integer) mapentry.getKey() + "";
			Set set2 = (select_hashMap.get(mapentry.getKey())).entrySet();
			Iterator it2 = set2.iterator();
			while (it2.hasNext()) {
				String str2 = "";
				Map.Entry map = (Map.Entry) it2.next();

				mm.put(Integer.parseInt(map.getKey().toString()), map
						.getValue().toString());

				str2 = str + "-" + map.getKey();
				listStr.add(str2);
			}
			hashMap.put(Integer.parseInt(mapentry.getKey().toString()), mm);
		}
	}

	/** 跳到合买 */
	private void doJoin() {
		if (passType.length() == 0) {
			MyToast.getToast(getApplicationContext(), "请先选择过关方式。").show();
			return;
		}
		if (totalCount == 0) {
			MyToast.getToast(getApplicationContext(), "您还没有选择对阵").show();
			return;
		}
		setApp();
		intent = new Intent(Bet_JCLQ_Activity.this, JoinActivity.class);
		intent.putExtra("totalMoney", (totalCount * 2 * AppTools.bei) + "");
		Bet_JCLQ_Activity.this.startActivity(intent);
	}

	/** 点击事件 **/
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/** 清空 **/
		case R.id.bet_btn_deleteall:
			doClear();
			break;
		/** 清空 **/
		case R.id.btn_back:
			esc();
			break;
		/** 付款 **/
		case R.id.btn_submit:
			setEnabled(false);
//			myAsynTask = new MyAsynTask();
//			myAsynTask.execute();
			break;
		/** 合买 **/
		case R.id.btn_follow:
			doJoin();
			break;
		/** 增加选号 **/
		case R.id.btn_continue_select:
			doAdd();
			break;
		case R.id.tv_show_passway:
			selectType();
			break;
		case R.id.tv_ckName2:
			Intent intent = new Intent(Bet_JCLQ_Activity.this,
					PlayDescription.class);
			intent.putExtra("type", 0);
			Bet_JCLQ_Activity.this.startActivity(intent);
			break;
		case R.id.bet_tv_guize:
			intent = new Intent(Bet_JCLQ_Activity.this,
					PlayDescription.class);
			intent.putExtra("type", 0);
			Bet_JCLQ_Activity.this.startActivity(intent);
			break;
		default:
			break;
		}
	}

	/** 设置是否可用 **/
	private void setEnabled(boolean isEna) {
		listView.setEnabled(isEna);
		bet_btn_deleteall.setEnabled(isEna);
		btn_continue_select.setEnabled(isEna);
		btn_back.setEnabled(isEna);
		btn_type.setEnabled(isEna);
		btn_pay.setEnabled(isEna);
		btn_join.setEnabled(isEna);
	}

	public void clean_passType() {
		viewPagerCurrentIndex = 0;
		passType = "";
		Spanned text = Html.fromHtml(AppTools.changeStringColor("#808080",
				"过关方式（")
				+ AppTools.changeStringColor("#e3393c", "必选")
				+ AppTools.changeStringColor("#808080", "）"));
		btn_type.setText(text);// 过关
		btn_type.setBackgroundResource(R.drawable.select_jc_bg_white);// 设置背景
	}

	/** 选择过关方式 */
	private void selectType() {

		selectPTpop = new SelectPassTypePopupWindow(getApplicationContext(),
				betAdapter.list_dan.size(), betAdapter.getIndexsize(),
				viewPagerCurrentIndex, layout_main);
		if (0 != passType.length()) {// 不为空
			playtype_list = new ArrayList<String>();
			for (int i = 0; i < passType.split(",").length; i++) {
				playtype_list.add(passType.split(",")[i]);
			}
			selectPTpop.setSelectPassType(playtype_list);
		}
		selectPTpop.createPopWindow();
		selectPTpop
				.setDialogResultListener(new SelectPassTypePopupWindow.DialogResultListener() {
					@Override
					public void getResult(int resultCode,
							ArrayList<String> selectResult, int type) {
						// TODO Auto-generated method stub
						if (1 == resultCode) {// 确定
							viewPagerCurrentIndex = type;
							passType = "";
							for (int i = 0; i < selectResult.size(); i++) {
								if (i == 0)
									passType += selectResult.get(i);
								else
									passType += "," + selectResult.get(i);
							}
							setPassText(setText(passType));
						}
					}
				});
		updateAdapter();
	}

	public void setPassType(String s) {
		this.passType = s;
	}

	/** 计算总注数 **/
	public void setTotalCount() {
//		Log.i("x", "计算总注数");
		this.setListResult();
		this.totalCount = 0;
//		System.out.println("setTotalCount======" + passType);
		String type = passType;
		if (type.length() == 0) {
			return;
		}
//		Log.i("x", "调用setList方法");
		setList();
//		Log.i("x", "调用setList方法完毕");
		List<String> list = new ArrayList<String>();
		List<String> list_dan = new ArrayList<String>();
//		System.out.println("====--====" + listResult);
		for (int i = 0; i < listResult.size(); i++) {
			if(7306!=type2){//非混合投注
				String str = "";
//				Log.i(TAG, "元素"+i);
				for (int j = 0; j < listResult.get(i).split(",").length; j++) {
					str += "1";
//					Log.i(TAG, listResult.get(i).split(",")[j]);
				}
				list.add(str);
			}else{//混合投注
				StringBuffer sb_sf=new StringBuffer();
				StringBuffer sb_rfsf=new StringBuffer();
				StringBuffer sb_dxf=new StringBuffer();
				StringBuffer sb_sfc=new StringBuffer();
				String[] arry=listResult.get(i).split(",");
				for (int j = 0; j < arry.length; j++) {
					if(arry[j].length()<3){//判断长度是否小于3,则为胜分差
						sb_sfc.append("4,");
					}else{
						String first=arry[j].charAt(0)+"";
						if("1".equals(first)){//胜负
							sb_sf.append("1,");
						}else if("2".equals(first)){//让分胜负
							sb_rfsf.append("2,");
						}else if("3".equals(first)){//大小分
							sb_dxf.append("3,");
						}
					}
				}
				StringBuffer sb_hhtz=new StringBuffer();
				if(0!=sb_sf.length()){//选了胜负
					String str_sf="";
					if(0==sb_rfsf.length()&&0==sb_dxf.length()&&0==sb_sfc.length()){//没有让分胜负，大小分，胜分差
						str_sf=sb_sf.substring(0, sb_sf.length()-1);
					}else{
						str_sf=sb_sf.substring(0, sb_sf.length()-1)+"|";
					}
					sb_hhtz.append(str_sf);
				}
				if(0!=sb_rfsf.length()){//选了让分胜负
					String str_rfsf="";
					if(0==sb_dxf.length()&&0==sb_sfc.length()){//没有大小分，胜分差
						str_rfsf=sb_rfsf.substring(0, sb_rfsf.length()-1);
					}else{
						str_rfsf=sb_rfsf.substring(0, sb_rfsf.length()-1)+"|";
					}
					sb_hhtz.append(str_rfsf);
				}
				if(0!=sb_dxf.length()){//选了让分胜负
					String str_dxf="";
					if(0==sb_sfc.length()){//没有胜分差
						str_dxf=sb_dxf.substring(0, sb_dxf.length()-1);
					}else{
						str_dxf=sb_dxf.substring(0, sb_dxf.length()-1)+"|";
					}
					sb_hhtz.append(str_dxf);
				}
				if(0!=sb_sfc.length()){//选了胜分差
					String str_sfc="";
					str_sfc=sb_sfc.substring(0, sb_sfc.length()-1);
					sb_hhtz.append(str_sfc);
				}
//				Log.i(TAG, "混合投注算注数格式"+sb_hhtz.toString());
				list.add(sb_hhtz.toString());
			}
		}
//		Log.i("x", "betAdapter.list_dan的大小======" + betAdapter.list_dan.size());
		if (betAdapter.list_dan.size() == 0) {
			if(7306==type2){//混合投注
				setTotalCountHHTZ(type, list);
			}else{
				setTotalCount(type, list);
			}
		} else {
//			Log.i("x", "listResult_dan的大小======" + listResult_dan.size());
			for (int i = 0; i < listResult_dan.size(); i++) {
				String str = "";
				for (int j = 0; j < listResult_dan.get(i).split(",").length; j++) {
					str += "1";
				}
				list_dan.add(str);
			}
//			Log.i("x", "list_dan的大小======" + list_dan.size());
			setTotalCount(type, list_dan, list);
		}
	}
	
	/** 计算总注数-混合投注 **/
	public void setTotalCountHHTZ(String type, List<String> list) {
		if (type.contains("AA"))
			this.totalCount += NumberTools.getAll2G1Mixed_hunhe(list);
		if (type.contains("AB"))
			this.totalCount += NumberTools.getAll3G1Mixed_hunhe(list);
		if (type.contains("AC"))
			this.totalCount += NumberTools.getAll3G3Mixed_hunhe(list);
		if (type.contains("AD"))
			this.totalCount += NumberTools.getAll3G4Mixed_hunhe(list);
		if (type.contains("AE"))
			this.totalCount += NumberTools.getAll4G1Mixed_hunhe(list);
		if (type.contains("AF"))
			this.totalCount += NumberTools.getAll4G4Mixed_hunhe(list);
		if (type.contains("AG"))
			this.totalCount += NumberTools.getAll4G5Mixed_hunhe(list);
		if (type.contains("AH"))
			this.totalCount += NumberTools.getAll4G6Mixed_hunhe(list);
		if (type.contains("AI"))
			this.totalCount += NumberTools.getAll4G11Mixed_hunhe(list);
		if (type.contains("AJ"))
			this.totalCount += NumberTools.getAll5G1Mixed_hunhe(list);
		if (type.contains("AK"))
			this.totalCount += NumberTools.getAll5G5Mixed_hunhe(list);
		if (type.contains("AL"))
			this.totalCount += NumberTools.getAll5G6Mixed_hunhe(list);
		if (type.contains("AM"))
			this.totalCount += NumberTools.getAll5G10Mixed_hunhe(list);
		if (type.contains("AN"))
			this.totalCount += NumberTools.getAll5G16Mixed_hunhe(list);
		if (type.contains("AO"))
			this.totalCount += NumberTools.getAll5G20Mixed_hunhe(list);
		if (type.contains("AP"))
			this.totalCount += NumberTools.getAll5G26Mixed_hunhe(list);
		if (type.contains("AQ"))
			this.totalCount += NumberTools.getAll6G1Mixed_hunhe(list);
		if (type.contains("AR"))
			this.totalCount += NumberTools.getAll6G6Mixed_hunhe(list);
		if (type.contains("AS"))
			this.totalCount += NumberTools.getAll6G7Mixed_hunhe(list);
		if (type.contains("AT"))
			this.totalCount += NumberTools.getAll6G15Mixed_hunhe(list);
		if (type.contains("AU"))
			this.totalCount += NumberTools.getAll6G20Mixed_hunhe(list);
		if (type.contains("AV"))
			this.totalCount += NumberTools.getAll6G22Mixed_hunhe(list);
		if (type.contains("AW"))
			this.totalCount += NumberTools.getAll6G35Mixed_hunhe(list);
		if (type.contains("AX"))
			this.totalCount += NumberTools.getAll6G42Mixed_hunhe(list);
		if (type.contains("AY"))
			this.totalCount += NumberTools.getAll6G50Mixed_hunhe(list);
		if (type.contains("AZ"))
			this.totalCount += NumberTools.getAll6G57Mixed_hunhe(list);
		if (type.contains("BA"))
			this.totalCount += NumberTools.getAll7G1Mixed_hunhe(list);
		if (type.contains("BB"))
			this.totalCount += NumberTools.getAll7G7Mixed_hunhe(list);
		if (type.contains("BC"))
			this.totalCount += NumberTools.getAll7G8Mixed_hunhe(list);
		if (type.contains("BD"))
			this.totalCount += NumberTools.getAll7G21Mixed_hunhe(list);
		if (type.contains("BE"))
			this.totalCount += NumberTools.getAll7G35Mixed_hunhe(list);
		if (type.contains("BF"))
			this.totalCount += NumberTools.getAll7G120Mixed_hunhe(list);
		if (type.contains("BG"))
			this.totalCount += NumberTools.getAll8G1Mixed_hunhe(list);
		if (type.contains("BH"))
			this.totalCount += NumberTools.getAll8G8Mixed_hunhe(list);
		if (type.contains("BI"))
			this.totalCount += NumberTools.getAll8G9Mixed_hunhe(list);
		if (type.contains("BJ"))
			this.totalCount += NumberTools.getAll8G28Mixed_hunhe(list);
		if (type.contains("BK"))
			this.totalCount += NumberTools.getAll8G56Mixed_hunhe(list);
		if (type.contains("BL"))
			this.totalCount += NumberTools.getAll8G70Mixed_hunhe(list);
		if (type.contains("BM"))
			this.totalCount += NumberTools.getAll8G247Mixed_hunhe(list);
	}

	/** 给总注数赋值 没胆的情况下 **/
	public void setTotalCount(String type, List<String> list_dan,
			List<String> list) {
		if (type.contains("AA"))
			this.totalCount += NumberTools
					.getAllnG1Mixed_dan(list_dan, list, 2);
		if (type.contains("AB"))
			this.totalCount += NumberTools
					.getAllnG1Mixed_dan(list_dan, list, 3);
		if (type.contains("AC"))
			this.totalCount += NumberTools.getAll3G3Mixed_dan(list_dan, list);
		if (type.contains("AD"))
			this.totalCount += NumberTools.getAll3G4Mixed_dan(list_dan, list);
		if (type.contains("AE"))
			this.totalCount += NumberTools
					.getAllnG1Mixed_dan(list_dan, list, 4);
		if (type.contains("AF"))
			this.totalCount += NumberTools.getAll4G4Mixed_dan(list_dan, list);
		if (type.contains("AG"))
			this.totalCount += NumberTools.getAll4G5Mixed_dan(list_dan, list);
		if (type.contains("AH"))
			this.totalCount += NumberTools.getAll4G6_11Mixed_dan(list_dan,
					list, 6);
		if (type.contains("AI"))
			this.totalCount += NumberTools.getAll4G6_11Mixed_dan(list_dan,
					list, 11);
		if (type.contains("AJ"))
			this.totalCount += NumberTools
					.getAllnG1Mixed_dan(list_dan, list, 5);
		if (type.contains("AK"))
			this.totalCount += NumberTools.getAll5G5Mixed_dan(list_dan, list);
		if (type.contains("AL"))
			this.totalCount += NumberTools.getAll5G6Mixed_dan(list_dan, list);
		if (type.contains("AM"))
			this.totalCount += NumberTools.getAll5G10_16_20Mixed_dan(list_dan,
					list, 10);
		if (type.contains("AN"))
			this.totalCount += NumberTools.getAll5G10_16_20Mixed_dan(list_dan,
					list, 16);
		if (type.contains("AO"))
			this.totalCount += NumberTools.getAll5G10_16_20Mixed_dan(list_dan,
					list, 20);
		if (type.contains("AP"))
			this.totalCount += NumberTools.getAll5G26Mixed_dan(list_dan, list);
		if (type.contains("AQ"))
			this.totalCount += NumberTools
					.getAllnG1Mixed_dan(list_dan, list, 6);
		if (type.contains("AR"))
			this.totalCount += NumberTools.getAll6G6Mixed_dan(list_dan, list);
		if (type.contains("AS"))
			this.totalCount += NumberTools.getAll6G7Mixed_dan(list_dan, list);
		if (type.contains("AT"))
			this.totalCount += NumberTools.getAll6G15_20_22_50Mixed_dan(
					list_dan, list, 15);
		if (type.contains("AU"))
			this.totalCount += NumberTools.getAll6G15_20_22_50Mixed_dan(
					list_dan, list, 20);
		if (type.contains("AV"))
			this.totalCount += NumberTools.getAll6G15_20_22_50Mixed_dan(
					list_dan, list, 22);
		if (type.contains("AW"))
			this.totalCount += NumberTools.getAll6G35Mixed_dan(list_dan, list);
		if (type.contains("AX"))
			this.totalCount += NumberTools.getAll6G42Mixed_dan(list_dan, list);
		if (type.contains("AY"))
			this.totalCount += NumberTools.getAll6G15_20_22_50Mixed_dan(
					list_dan, list, 50);
		if (type.contains("AZ"))
			this.totalCount += NumberTools.getAll6G57Mixed_dan(list_dan, list);
		if (type.contains("BA"))
			this.totalCount += NumberTools
					.getAllnG1Mixed_dan(list_dan, list, 7);
		if (type.contains("BB"))
			this.totalCount += NumberTools.getAll7G7_21_35Mixed_dan(list_dan,
					list, 7);
		if (type.contains("BC"))
			this.totalCount += NumberTools.getAll7G8Mixed_dan(list_dan, list);
		if (type.contains("BD"))
			this.totalCount += NumberTools.getAll7G7_21_35Mixed_dan(list_dan,
					list, 21);
		if (type.contains("BE"))
			this.totalCount += NumberTools.getAll7G7_21_35Mixed_dan(list_dan,
					list, 35);
		if (type.contains("BF"))
			this.totalCount += NumberTools.getAll7G7_21_35Mixed_dan(list_dan,
					list, 120);
		if (type.contains("BG"))
			this.totalCount += NumberTools
					.getAllnG1Mixed_dan(list_dan, list, 8);
		if (type.contains("BH"))
			this.totalCount += NumberTools.getAll8G8_28_56_70_247Mixed_dan(
					list_dan, list, 8);
		if (type.contains("BI"))
			this.totalCount += NumberTools.getAll8G9Mixed_dan(list_dan, list);
		if (type.contains("BJ"))
			this.totalCount += NumberTools.getAll8G8_28_56_70_247Mixed_dan(
					list_dan, list, 28);
		if (type.contains("BK"))
			this.totalCount += NumberTools.getAll8G8_28_56_70_247Mixed_dan(
					list_dan, list, 56);
		if (type.contains("BL"))
			this.totalCount += NumberTools.getAll8G8_28_56_70_247Mixed_dan(
					list_dan, list, 70);
		if (type.contains("BM"))
			this.totalCount += NumberTools.getAll8G8_28_56_70_247Mixed_dan(
					list_dan, list, 247);
	}

	/** 给总注数赋值 没胆的情况下 **/
	public void setTotalCount(String type, List<String> list) {
//		System.out.println("算注－－－－");
		System.out.println(type);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
//		System.out.println("＊＊＊＊＊＊＊＊");
		if (type.contains("AA"))
			this.totalCount += NumberTools.getAll2G1Mixed(list);
		if (type.contains("AB"))
			this.totalCount += NumberTools.getAll3G1Mixed(list);
		if (type.contains("AC"))
			this.totalCount += NumberTools.getAll3G3Mixed(list);
		if (type.contains("AD"))
			this.totalCount += NumberTools.getAll3G4Mixed(list);
		if (type.contains("AE"))
			this.totalCount += NumberTools.getAll4G1Mixed(list);
		if (type.contains("AF"))
			this.totalCount += NumberTools.getAll4G4Mixed(list);
		if (type.contains("AG"))
			this.totalCount += NumberTools.getAll4G5Mixed(list);
		if (type.contains("AH"))
			this.totalCount += NumberTools.getAll4G6Mixed(list);
		if (type.contains("AI"))
			this.totalCount += NumberTools.getAll4G11Mixed(list);
		if (type.contains("AJ"))
			this.totalCount += NumberTools.getAll5G1Mixed(list);
		if (type.contains("AK"))
			this.totalCount += NumberTools.getAll5G5Mixed(list);
		if (type.contains("AL"))
			this.totalCount += NumberTools.getAll5G6Mixed(list);
		if (type.contains("AM"))
			this.totalCount += NumberTools.getAll5G10Mixed(list);
		if (type.contains("AN"))
			this.totalCount += NumberTools.getAll5G16Mixed(list);
		if (type.contains("AO"))
			this.totalCount += NumberTools.getAll5G20Mixed(list);
		if (type.contains("AP"))
			this.totalCount += NumberTools.getAll5G26Mixed(list);
		if (type.contains("AQ"))
			this.totalCount += NumberTools.getAll6G1Mixed(list);
		if (type.contains("AR"))
			this.totalCount += NumberTools.getAll6G6Mixed(list);
		if (type.contains("AS"))
			this.totalCount += NumberTools.getAll6G7Mixed(list);
		if (type.contains("AT"))
			this.totalCount += NumberTools.getAll6G15Mixed(list);
		if (type.contains("AU"))
			this.totalCount += NumberTools.getAll6G20Mixed(list);
		if (type.contains("AV"))
			this.totalCount += NumberTools.getAll6G22Mixed(list);
		if (type.contains("AW"))
			this.totalCount += NumberTools.getAll6G35Mixed(list);
		if (type.contains("AX"))
			this.totalCount += NumberTools.getAll6G42Mixed(list);
		if (type.contains("AY"))
			this.totalCount += NumberTools.getAll6G50Mixed(list);
		if (type.contains("AZ"))
			this.totalCount += NumberTools.getAll6G57Mixed(list);
		if (type.contains("BA"))
			this.totalCount += NumberTools.getAll7G1Mixed(list);
		if (type.contains("BB"))
			this.totalCount += NumberTools.getAll7G7Mixed(list);
		if (type.contains("BC"))
			this.totalCount += NumberTools.getAll7G8Mixed(list);
		if (type.contains("BD"))
			this.totalCount += NumberTools.getAll7G21Mixed(list);
		if (type.contains("BE"))
			this.totalCount += NumberTools.getAll7G35Mixed(list);
		if (type.contains("BF"))
			this.totalCount += NumberTools.getAll7G120Mixed(list);
		if (type.contains("BG"))
			this.totalCount += NumberTools.getAll8G1Mixed(list);
		if (type.contains("BH"))
			this.totalCount += NumberTools.getAll8G8Mixed(list);
		if (type.contains("BI"))
			this.totalCount += NumberTools.getAll8G9Mixed(list);
		if (type.contains("BJ"))
			this.totalCount += NumberTools.getAll8G28Mixed(list);
		if (type.contains("BK"))
			this.totalCount += NumberTools.getAll8G56Mixed(list);
		if (type.contains("BL"))
			this.totalCount += NumberTools.getAll8G70Mixed(list);
		if (type.contains("BM"))
			this.totalCount += NumberTools.getAll8G247Mixed(list);
	}

	/** 得到总注数 **/
	public long getTotalCount() {
		setTotalCount();
		return this.totalCount;
	}

	/** 重写返回键事件 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			esc();
		}
		return super.onKeyDown(keyCode, event);
	}

	/** 点击确认退出程序 */
	class positiveClick implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {

			ExpandAdapter_jclq.map_hashMap_sf.clear();
			ExpandAdapter_jclq.map_hashMap_dx.clear();
			Bet_JCLQ_Activity.this.finish();
		}
	}

	private void esc() {
		dialog.show();
		dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
		dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
			@Override
			public void getResult(int resultCode) {
				// TODO Auto-generated method stub
				if (1 == resultCode) {// 确定
					ExpandAdapter_jclq.map_hashMap_sf.clear();
					ExpandAdapter_jclq.map_hashMap_dx.clear();
					for (int i = 0; i < App.activityS1.size(); i++) {
						App.activityS1.get(i).finish();
					}
				}
			}
		});
	}

	/** 异步任务 用来后台获取数据 */
//	class MyAsynTask extends AsyncTask<Void, Integer, String> {
//		String error = "-1001";
//
//		@Override
//		protected String doInBackground(Void... params) {
//			if (null == AppTools.user) {
//				return AppTools.ERROR_UNLONGIN + "";
//			}
//
//			if (passType.length() == 0) {
//				return "-3";
//			}
//
//			if (totalCount == 0) {
//				return "-4";
//			}
//			setApp();
//
//			// 格式
//			// 7207;[116734(1,2)][116732(1)|116733(1)|116735(1,2,3)];[AB1];[1]
//			// st 就是格式
//			info = RspBodyBaseBean.changeBet_info("73", "-1", AppTools.bei, 1,
//					1, 0, 0, "", "", 0, (totalCount * 2 * AppTools.bei),
//					totalCount, 0, 0, AppTools.ball, type2 + "");
//
//			System.out.println("bet---info---->" + info);
//
//			// 06-19 10:45:45.124: I/System.out(271):
//			// info---->{"description":"","lotteryId":"72","isuseId":"-1","multiple":"1","share":"1","buyShare":"1","assureShare":"0","title":"","secrecyLevel":"0","schemeBonusScale":"0","schemeSumMoney":"8","schemeSumNum":"4","chase":"0","chaseBuyedMoney":"0.0","buyContent":[{"lotteryNumber":"7201;[116718(1,2)|116719(1,2)];[AA1]","playType":7201,"sumMoney":
//			// 8 ,"sumNum":4}],"chaseList": [] }
//
//			String key = MD5
//					.md5(AppTools.user.getUserPass() + AppTools.MD5_key);
//
//			crc = RspBodyBaseBean.getCrc(time, imei, key, info,
//					AppTools.user.getUid());
//
//			auth = RspBodyBaseBean.getAuth(crc, time, imei,
//					AppTools.user.getUid());
//
//			String[] values = { opt, auth, info };
//
//			// int a = 0;
//			// if (a == 0)
//			// return "-100500";
//
//			String result = HttpUtils.doPost(AppTools.names, values,
//					AppTools.path);
//
//			Log.i("x", "篮球投注result  " + result);
//			if ("-500".equals(result))
//				return "-500";
//			try {
//				JSONObject object = new JSONObject(result);
//				error = object.getString("error");
//
//				if (0 == object.getInt("error")) {
//					AppTools.user.setBalance(object.optDouble("balance"));
//					AppTools.user.setFreeze(object.optDouble("freeze"));
//					AppTools.schemeId = object.optInt("schemeids");
//					AppTools.lottery.setChaseTaskID(object.optInt("chasetaskids"));
//				}
//				return error;
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				error = "-1001";
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				error = "-1001";
//			}
//			return error;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			myHandler.sendEmptyMessage(Integer.parseInt(result));
//			super.onPostExecute(result);
//		}
//	}

	public void clearTypedialog() {
		if (btn_type.getText().toString().contains(",")
				|| btn_type.getText().toString().length() == 3) {
			String[] array = btn_type.getText().toString().split(",");
//			System.out.println("000" + array.length);
			int a = betAdapter.list_dan.size();
			for (int i = 0; i < array.length; i++) {
				int b = Integer.parseInt(array[i].substring(0, 1));
				if (b <= a) {
					String[] ar = new String[array.length - 1];
					for (int j = 0; j < array.length - 1; j++) {
						ar[j] = array[j + 1];
					}
					String str = "";
					for (int j = 0; j < ar.length; j++) {
						if (j == ar.length - 1) {
							str += (String) ar[j];
						} else
							str += (String) ar[j] + ",";
					}
					setPassText(str);
//					System.out.println(str);
				}
			}
		}

		checkIndex = null;
		passType = "";

	}

	public int getType() {
		return this.type2;
	}

	/** 给过关方式按钮赋值 **/
	public void setPassText(String str) {
		if (str.length() == 0) {
			Spanned text = Html.fromHtml(AppTools.changeStringColor("#808080",
					"过关方式（")
					+ AppTools.changeStringColor("#e3393c", "必选")
					+ AppTools.changeStringColor("#808080", "）"));
			btn_type.setText(text);// 过关
			btn_type.setBackgroundResource(R.drawable.select_jc_bg_white);// 设置背景//
																			// 设置背景
		} else {
			this.btn_type.setText(str);
			this.btn_type.setTextColor(Color.WHITE);
			this.btn_type.setBackgroundResource(R.drawable.select_jc_bg_red);// 设置背景
		}
		setCountText();
	}

	/** 设置显示 **/
	public void setCountText() {
		if (et_bei.getText().toString().trim().length() == 0)
			AppTools.bei = 1;
		else
			AppTools.bei = Integer.parseInt(et_bei.getText().toString().trim());

		tv_count.setText(this.getTotalCount() + "");
		tv_money.setText(this.getTotalCount() * AppTools.bei * 2 + ""); // 倍数修改
	}

	/** 格式化投注信息 **/
	private void setList() {
		listResult.clear();
		listResult_dan.clear();
		/** 没有格式化的投注内容 **/
		String betNum = "";
		/** 格式化后的投注内容 **/
		String betNum2 = "";

//		Log.i("x", "setList方法里面的ListStr=======" + listStr.size());

		for (int j = 0; j < listStr.size(); j++) {
			String str = listStr.get(j);

//			Log.i("x", "setList方法里面的-=====Str=======" + str);

			/** 得到 HashMap 的键 **/
			Integer s = Integer.parseInt(str.split("-")[0]);
			Integer s2 = Integer.parseInt(str.split("-")[1]);
			// 拿到投注内容
			betNum = select_hashMap.get(s).get(s2);
//			Log.i("x", "setList方法里面的=====投注内容====" + betNum);
			if (betNum.length() == 0)
				continue;
			if (betNum.contains(",")) {
				betNum2 = betNum;
			} else {
				betNum2 = "";
				if (type2 != 7306) {
					for (int i = 0; i < betNum.length() - 1; i++) {
						String g = betNum.substring(i, i + 1);
						betNum2 += g + ",";
					}
					if (betNum.length() != 0)
						betNum2 += betNum.substring(betNum.length() - 1);
				}
				else{
					betNum2 = betNum;
				}
			}
//			Log.i("x", "setList方法里面的=====投注内容=2222===" + betNum2);

//			Log.i("x", "setList==fffffff===list_dan的大小======"
//					+ betAdapter.list_dan.size());
			boolean isDan = false;
			for (String index : betAdapter.list_dan) {
//				Log.i("x", "胆的值====" + index + "j====得值====" + j);
				if (Integer.parseInt(index) == j) {
//					Log.i("x", "胆的值====" + j);
					listResult_dan.add(betNum2);
					isDan = true;
				}
			}
			if (!isDan)
				listResult.add(betNum2);
		}
	}

	/** 封装投注格式 */
	private void setApp() {
		setList();
		if (listResult.size() == 0)
			return;
		/** 装所有对阵信息的集合 **/
		List<DtMatch_Basketball> list = betAdapter.getListDtMatch();
		// 7207 ; [116734(1,2)] [116732(1) | 116733(1) | 116735(1,2,3) ];
		// [AB1,AC1,AD1] ; [1]
		/** 拼凑字符串 **/
		String st = type2 + ";[";

		for (int i = 0; i < list.size(); i++) {
			System.out.println("list===" + list.get(i).getMatchId());
		}
		System.out.println("listResult_dan.size()===" + listResult_dan.size());
		for (int i = 0; i < betAdapter.list_dan.size(); i++) {
			System.out.println("dan========" + betAdapter.list_dan.get(i));
		}
		for (int i = 0; i < betAdapter.list_dan.size(); i++) {
			if (type2 != 7306) {
				st += list.get(Integer.parseInt(betAdapter.list_dan.get(i)))
						.getMatchId() + "(" + listResult_dan.get(i) + ")";
			} else {
				String[] array = listResult_dan.get(i).split(",");
				for (int k = 0; k < array.length; k++) {
				}
				for (int k = 0; k < array.length; k++) {
					if (Integer.parseInt(array[k]) < 15) {
						array[k] = chang_sfc_hhtz(array[k]);
					}
				}
				for (int k = 0; k < array.length; k++) {
				}
				String aaa = "";
				for (int k2 = 0; k2 < array.length; k2++) {
					if (k2 == 0)
						aaa = array[k2];
					else
						aaa += "," + array[k2];
				}
				st += list.get(Integer.parseInt(betAdapter.list_dan.get(i)))
						.getMatchId() + "(" + aaa + ")";
			}
			if (listResult_dan.size() - 1 == i) {
				st += "]";
			} else
				st += "|";
			if (betAdapter.list_dan.size() - i == 1)
				st += "[";
		}
		List<DtMatch_Basketball> lista = new ArrayList<DtMatch_Basketball>();
		for (int i = 0; i < betAdapter.list_dan.size(); i++) {
			DtMatch_Basketball mm = list.get(Integer
					.parseInt(betAdapter.list_dan.get(i)));
			lista.add(mm);
		}
		for (int i = 0; i < lista.size(); i++) {
			list.remove(lista.get(i));
		}
		for (int i = 0; i < list.size(); i++) {
			if (type2 != 7306)
				st += list.get(i).getMatchId() + "(" + listResult.get(i) + ")";
			else {
				String[] array = listResult.get(i).split(",");
				for (int k = 0; k < array.length; k++) {
				}
				for (int k = 0; k < array.length; k++) {
					if (Integer.parseInt(array[k]) < 15) {
						array[k] = chang_sfc_hhtz(array[k]);
					}
				}
				for (int k = 0; k < array.length; k++) {
				}
				String aaa = "";
				for (int k2 = 0; k2 < array.length; k2++) {
					if (k2 == 0)
						aaa = array[k2];
					else
						aaa += "," + array[k2];
				}
				st += list.get(i).getMatchId() + "(" + aaa + ")";
			}
			if (list.size() - 1 == i) {
				st += "]";
			} else
				st += "|";
		}

		// int i = 0;
		// Log.i("x", "listResult_dan的大小===5555==" + listResult_dan.size());
		// if (listResult_dan.size() != 0) {
		// for (; i < listResult_dan.size(); i++) {
		//
		// if (type2 != 7306)
		// st += list
		// .get(Integer.parseInt(betAdapter.list_dan.get(i)))
		// .getMatchId()
		// + "(" + listResult_dan.get(i) + ")";
		// else {
		// String[] array = listResult_dan.get(i).split(",");
		// for (int k = 0; k < array.length; k++) {
		// }
		// for (int k = 0; k < array.length; k++) {
		// if (Integer.parseInt(array[k]) < 15) {
		// array[k] = chang_sfc_hhtz(array[k]);
		// }
		// }
		// for (int k = 0; k < array.length; k++) {
		// }
		// String aaa = "";
		// for (int k2 = 0; k2 < array.length; k2++) {
		// if (k2 == 0)
		// aaa = array[k2];
		// else
		// aaa += "," + array[k2];
		// }
		// st += list
		// .get(Integer.parseInt(betAdapter.list_dan.get(i)))
		// .getMatchId()
		// + "(" + aaa + ")";
		// }
		// if (i == listResult_dan.size() - 1)
		// st += "]";
		// else
		// st += "|";
		// }
		// st += "[";
		// }
		// int j = 0;
		// for (; i < list.size(); i++) {
		// if (type2 != 7306)
		// st += list.get(i).getMatchId() + "(" + listResult.get(j) + ")";
		// else {
		// String[] array = listResult.get(j).split(",");
		// for (int k = 0; k < array.length; k++) {
		// }
		// for (int k = 0; k < array.length; k++) {
		// if (Integer.parseInt(array[k]) < 15) {
		// array[k] = chang_sfc_hhtz(array[k]);
		// }
		// }
		// for (int k = 0; k < array.length; k++) {
		// }
		// String aaa = "";
		// for (int k2 = 0; k2 < array.length; k2++) {
		// if (k2 == 0)
		// aaa = array[k2];
		// else
		// aaa += "," + array[k2];
		// }
		// st += list.get(i).getMatchId() + "(" + aaa + ")";
		// }
		// if (i == list.size() - 1)
		// st += "]";
		// else
		// st += "|";
		// j++;
		// }
		// 12-17 06:53:27.611: I/x(4730):
		// st========7306;[316(100,301,409,410,300)|317(301,200,408,411,401)];[AA1]
		System.out.println(passType);
		System.out.println(passType.split(",").length);
		System.out.println(passType.toString());
		// 拿到过关方式类型 passType = [AA, AB]
		String str_type[] = passType.split(",");
		// 拼凑 过关方式和倍数
		System.out.println("STR_TYPE_SIZE()====" + str_type.length);
		String a = "";
		for (int k = 0; k < str_type.length; k++) {
			if (k == str_type.length - 1) {
				a += str_type[k] + AppTools.bei;
			} else
				a += str_type[k] + AppTools.bei + ",";
		}
		st += ";[" + a + "]";

//		Log.i("x", "st========" + st);

		if (listResult_dan.size() != 0)
			st += ";[" + listResult_dan.size() + "]";

		AppTools.ball = st;
		AppTools.lottery.setType(type2);
	}

	private String chang_sfc_hhtz(String sfc_result) {
		switch (Integer.parseInt(sfc_result)) {
		case 1:
			return "400";
		case 2:
			return "406";
		case 3:
			return "401";
		case 4:
			return "407";
		case 5:
			return "402";
		case 6:
			return "408";
		case 7:
			return "403";
		case 8:
			return "409";
		case 9:
			return "404";
		case 10:
			return "410";
		case 11:
			return "405";
		case 12:
			return "411";
		default:
			break;
		}
		return null;
	}

	/** 消息机制 **/
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AppTools.ERROR_SUCCESS:
				ExpandAdapter_jclq.map_hashMap_dx.clear();
				ExpandAdapter_jclq.map_hashMap_sf.clear();
				AppTools.totalCount = 0;
				// 结束所有的跳到主页面
				Intent intent = new Intent(getApplicationContext(),
						PaySuccessActivity.class);
				viewPagerCurrentIndex = 0;
				passType="";
				intent.putExtra("paymoney", (totalCount * 2 * AppTools.bei));
				Bet_JCLQ_Activity.this.startActivity(intent);
				break;
			/** 余额不足 **/
			case AppTools.ERROR_MONEY:
				Toast.makeText(Bet_JCLQ_Activity.this, "余额不足",
						Toast.LENGTH_SHORT).show();
				intent = new Intent(Bet_JCLQ_Activity.this,
						RechargeActivity.class);
				intent.putExtra("money", (totalCount * 2));
				Bet_JCLQ_Activity.this.startActivity(intent);
				break;
			/** 尚未登陆 **/
			case AppTools.ERROR_UNLONGIN:
				MyToast.getToast(Bet_JCLQ_Activity.this, "请先登陆").show();
				intent = new Intent(Bet_JCLQ_Activity.this, LoginActivity.class);
				intent.putExtra("loginType", "bet");
				Bet_JCLQ_Activity.this.startActivity(intent);
				break;
			/** 点击付款时 所选注数为 0 **/
			case AppTools.ERROR_TOTAL:
				MyToast.getToast(Bet_JCLQ_Activity.this, "请至少选择一注").show();
				break;
			case -2:
				MyToast.getToast(Bet_JCLQ_Activity.this, "请选择倍数").show();
				break;
			case -3:
				MyToast.getToast(Bet_JCLQ_Activity.this, "请先选择过关方式").show();
				break;
			case -4:
				MyToast.getToast(Bet_JCLQ_Activity.this, "请先至少选择一注").show();
				break;
			case -500:
				MyToast.getToast(Bet_JCLQ_Activity.this, "连接超时").show();
				break;
			default:
				Toast.makeText(Bet_JCLQ_Activity.this, "网络异常，购买失败。请重新点击付款购买。",
						Toast.LENGTH_SHORT).show();
				break;
			}
//			if (myAsynTask != null
//					&& myAsynTask.getStatus() == AsyncTask.Status.RUNNING) {
//				myAsynTask.cancel(true); // 如果Task还在运行，则先取消它
//			}
			setEnabled(true);
			super.handleMessage(msg);
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
        init();
        findView();
		setListener();
		setCountText();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		this.setListStr();
		betAdapter.setListDtmatch(listStr);
		betAdapter.notifyDataSetChanged();
		super.onNewIntent(intent);
	}

	/** 拿到过关方式的显示值 */
	private String setText(String Type) {
		String showType = "";

		String passType = Type.replace("[", "").replace("]", "")
				.replace(" ", "");

		System.out.println("passType" + passType);

		System.out.println("passTypesize" + passType.length());

		if (passType.length() == 0) {
			return showType;
		}

		if (passType.contains("AA"))
			showType += ",2串1";
		if (passType.contains("AB"))
			showType += ",3串1";
		if (passType.contains("AC"))
			showType += ",3串3";
		if (passType.contains("AD"))
			showType += ",3串4";
		if (passType.contains("AE"))
			showType += ",4串1";
		if (passType.contains("AF"))
			showType += ",4串4";
		if (passType.contains("AG"))
			showType += ",4串5";
		if (passType.contains("AH"))
			showType += ",4串6";
		if (passType.contains("AI"))
			showType += ",4串11";
		if (passType.contains("AJ"))
			showType += ",5串1";
		if (passType.contains("AK"))
			showType += ",5串5";
		if (passType.contains("AL"))
			showType += ",5串6";
		if (passType.contains("AM"))
			showType += ",5串10";
		if (passType.contains("AN"))
			showType += ",5串16";
		if (passType.contains("AO"))
			showType += ",5串20";
		if (passType.contains("AP"))
			showType += ",5串26";
		if (passType.contains("AQ"))
			showType += ",6串1";
		if (passType.contains("AR"))
			showType += ",6串6";
		if (passType.contains("AS"))
			showType += ",6串7";
		if (passType.contains("AT"))
			showType += ",6串15";
		if (passType.contains("AU"))
			showType += ",6串20";
		if (passType.contains("AV"))
			showType += ",6串22";
		if (passType.contains("AW"))
			showType += ",6串35";
		if (passType.contains("AX"))
			showType += ",6串42";
		if (passType.contains("AY"))
			showType += ",6串50";
		if (passType.contains("AZ"))
			showType += ",6串57";
		if (passType.contains("BA"))
			showType += ",7串1";
		if (passType.contains("BB"))
			showType += ",7串7";
		if (passType.contains("BC"))
			showType += ",7串8";
		if (passType.contains("BD"))
			showType += ",7串21";
		if (passType.contains("BE"))
			showType += ",7串35";
		if (passType.contains("BF"))
			showType += ",7串120";
		if (passType.contains("BG"))
			showType += ",8串1";
		if (passType.contains("BH"))
			showType += ",8串8";
		if (passType.contains("BI"))
			showType += ",8串9";
		if (passType.contains("BJ"))
			showType += ",8串28";
		if (passType.contains("BK"))
			showType += ",8串56";
		if (passType.contains("BL"))
			showType += ",8串70";
		if (passType.contains("BM"))
			showType += ",8串247";

		return showType.substring(1);
	}
}
