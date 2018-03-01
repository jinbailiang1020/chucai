package com.sm.sls_app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.DtMatch;
import com.sm.sls_app.fragment.DtMatchFragmentJczq;
import com.sm.sls_app.ui.adapter.ExpandAdapterJCZQPassMore;
import com.sm.sls_app.ui.adapter.ExpandAdapterJCZQPassSingle;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.PopupWindowUtil;
import com.sm.sls_app.view.ConfirmDialog;
import com.sm.sls_app.view.MyToast;
import com.sm.sls_app.view.ScreenDtMatchPopupWindow;

/**
 * 竞彩足球 的选球页面 功能：选号界面，实现选号功能 版本
 * 
 * @author Kinwee 修改日期：2014-12-5
 * 
 */
public class Select_jczqActivity extends FragmentActivity implements
		OnClickListener {
	private final static String TAG = "Select_jczqActivity";

	/* 头部 */
	private RelativeLayout layout_top_select;// 顶部布局
	private RelativeLayout layout_main;// 主布局
	private ImageButton btn_back; // 返回
	private ImageButton btn_screen; // 筛选
	private LinearLayout layout_select_playtype;// 玩法选择
	private ImageView iv_up_down;// 玩法提示图标
	private Button btn_playtype;// 玩法
	private ImageButton btn_help;// 帮助
	private ConfirmDialog dialog;// 提示框

	private Animation animation = null;

	private Bundle bundle;
	private int playtype = 7206;// 玩法
	private int passtype = 0;// 过关方式 0.过多关 1.过单关
	// 选玩法控件
	private PopupWindowUtil popUtil;
	private Map<Integer, Map<Integer, String>> data = new HashMap<Integer, Map<Integer, String>>();
	private int parentIndex;
	private int itemIndex;
	public DtMatchFragmentJczq fragment;
	private FragmentManager fragmentManager;
	private static List<List<DtMatch>> passMoreDMList = new ArrayList<List<DtMatch>>();
	private static List<List<DtMatch>> passSingleDMList = new ArrayList<List<DtMatch>>();
	private ScreenDtMatchPopupWindow screenPop;

	private ArrayList<String> passMoreDMName = new ArrayList<String>();
	private ArrayList<String> passSingleDMName = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_select_jczq);
		App.activityS.add(this);
		App.activityS1.add(this);
		findView();
		init();
		setListener();
		fragmentManager = getSupportFragmentManager();
		fragment = DtMatchFragmentJczq.newInstance(this, playtype, passtype,
				passMoreDMList, passSingleDMList);
		fragmentManager.beginTransaction().add(R.id.fl_jczq, fragment).commit();
	}

	/** 初始化UI */
	private void findView() {
		bundle = new Bundle();
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_screen = (ImageButton) findViewById(R.id.btn_screen);
		btn_playtype = (Button) findViewById(R.id.btn_playtype);
		btn_help = (ImageButton) findViewById(R.id.btn_help);
		iv_up_down = (ImageView) findViewById(R.id.iv_up_down);
		layout_select_playtype = (LinearLayout) findViewById(R.id.layout_select_playtype);
		layout_top_select = (RelativeLayout) findViewById(R.id.layout_top_select);
		layout_main = (RelativeLayout) findViewById(R.id.layout_main);
	}

	/** 初始化属性 上期开奖号码 */
	private void init() {
		if(isEmptJCZQ()){
			passMoreDMList = AppTools.lottery.getList_Matchs();
			passSingleDMList = AppTools.lottery.getList_singlepass_Matchs();
		}
		playtype = getIntent().getIntExtra("playtype", 7206);
		passtype = getIntent().getIntExtra("passtype", 0);
		Log.i(TAG, "传过来的值");
		for (Integer i : ExpandAdapterJCZQPassMore.map_hashMap_cbf.keySet()) {
			ExpandAdapterJCZQPassMore.map_hashMap_cbf.get(i);
			for (Integer j : ExpandAdapterJCZQPassMore.map_hashMap_cbf.get(i)
					.keySet()) {
				ArrayList<String> list = ExpandAdapterJCZQPassMore.map_hashMap_cbf
						.get(i).get(j);
				for (int k = 0; k < list.size(); k++) {
					Log.i(TAG, list.get(k));
				}
			}
		}
		if (!isEmptJCZQ()) {
			iv_up_down.setEnabled(false);
			btn_playtype.setEnabled(false);
			layout_select_playtype.setEnabled(false);
		}
		Map<Integer, String> playType = new HashMap<Integer, String>();
		playType.put(0, "混合投注");
		playType.put(1, "让球胜平负");
		playType.put(2, "胜平负");
		playType.put(3, "总进球");
		playType.put(4, "比分");
		playType.put(5, "半全场");
		data.put(0, playType);
		int index = 0;
		switch (playtype) {
		case 7201:// 让球胜平负
			index = 1;
			break;
		case 7202:// 比分
			index = 4;
			break;
		case 7203:// 总进球
			index = 3;
			break;
		case 7204:// 半全场
			index = 5;
			break;
		case 7206:// 混合投注
			index = 0;
			break;
		case 7207:// 胜平负
			index = 2;
			break;

		}

		btn_playtype.setText(playType.get(index));
		dialog = new ConfirmDialog(this, R.style.dialog);
		btn_screen.setVisibility(View.VISIBLE);
	}

	/** 绑定监听 */
	private void setListener() {
		// 绑定Adapter
		btn_back.setOnClickListener(this);
		layout_select_playtype.setOnClickListener(this);
		iv_up_down.setOnClickListener(this);
		btn_playtype.setOnClickListener(this);
		btn_screen.setOnClickListener(this);
		btn_help.setOnClickListener(this);
	}

	/** 公共点击监听 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/** 返回 **/
		case R.id.btn_back:
			exit();
			break;
		/** 筛选 **/
		case R.id.btn_screen:
			screenDMList();
			break;
		/** 玩法说明 **/
		case R.id.btn_help:
			playExplain();
			break;
		/** 选玩法 **/
		case R.id.layout_select_playtype:
		case R.id.btn_playtype:
		case R.id.iv_up_down:
			popUtil = new PopupWindowUtil(this, data, layout_top_select);
			popUtil.setSelectIndex(parentIndex, itemIndex);
			popUtil.createPopWindow();
			popUtil.setOnSelectedListener(new PopupWindowUtil.OnSelectedListener() {
				@Override
				public void getIndex(int parentIndex, int itemIndex) {
					// TODO Auto-generated method stub
					if (Select_jczqActivity.this.itemIndex != itemIndex) {
						Select_jczqActivity.this.parentIndex = parentIndex;
						Select_jczqActivity.this.itemIndex = itemIndex;
						changePlayType();
					}
					rote(2);// 旋转动画 向下
				}
			});
			rote(1);// 旋转动画 向上
			break;
		}
	}

	/**
	 * 筛选
	 */
	public void screenDMList() {
		
		if (0 ==fragment.viewPager.getCurrentItem()) {// 过关
			screenPop = new ScreenDtMatchPopupWindow(this,
					AppTools.lottery.getList_Matchs(), layout_main);
			if (0 != passMoreDMName.size()) {// 不为空
				screenPop.setSelectDMName(passMoreDMName);
			}
			screenPop.createPopWindow();
			screenPop
					.setDialogResultListener(new ScreenDtMatchPopupWindow.DialogResultListener() {

						@Override
						public void getResult(int resultCode,
								List<List<DtMatch>> screenMatch_list,
								ArrayList<String> select_name_list) {
							// TODO Auto-generated method stub
							if (1 == resultCode) {// 确定
								passMoreDMName = select_name_list;
								passMoreDMList = screenMatch_list;
								passtype=0;
								changeFragment();
							}
						}
					});
		} else if(1==fragment.viewPager.getCurrentItem()){// 单关
			screenPop = new ScreenDtMatchPopupWindow(this,
					AppTools.lottery.getList_singlepass_Matchs(), layout_main);
			if (0 != passSingleDMName.size()) {// 不为空
				screenPop.setSelectDMName(passSingleDMName);
			}
			screenPop.createPopWindow();
			screenPop
					.setDialogResultListener(new ScreenDtMatchPopupWindow.DialogResultListener() {

						@Override
						public void getResult(int resultCode,
								List<List<DtMatch>> screenMatch_list,
								ArrayList<String> select_name_list) {
							// TODO Auto-generated method stub
							if (1 == resultCode) {// 确定
								passSingleDMName = select_name_list;
								passSingleDMList = screenMatch_list;
								passtype=1;
								changeFragment();
							}
						}
					});
		}
	}

	public void changePlayType() {
		btn_playtype.setText(data.get(parentIndex).get(itemIndex));
		switch (itemIndex) {
		case 0:// 混合投注
			playtype = 7206;
			break;
		case 1:// 让球胜平负
			playtype = 7201;
			break;
		case 2:// 胜平负
			playtype = 7207;
			break;
		case 3:// 总进球
			playtype = 7203;
			break;
		case 4:// 比分
			playtype = 7202;
			break;
		case 5:// 半全场
			playtype = 7204;
			break;
		}
		changeFragment();
	}

	public void changeFragment() {
		// 清空过关和单关的所有数据
		ExpandAdapterJCZQPassMore.clearSelectMap();
		ExpandAdapterJCZQPassSingle.clearSelectMap();
		fragment = DtMatchFragmentJczq.newInstance(this, playtype, passtype,
				passMoreDMList, passSingleDMList);
		fragmentManager.beginTransaction().replace(R.id.fl_jczq, fragment)
				.commit();
	}

	/**
	 * 旋转
	 * 
	 * @param type
	 *            1.向上 2.向下
	 */
	public void rote(int type) {
		if (1 == type) {
			animation = AnimationUtils.loadAnimation(getApplicationContext(),
					R.anim.rote_playtype_up);
		} else if (2 == type) {
			animation = AnimationUtils.loadAnimation(getApplicationContext(),
					R.anim.rote_playtype_down);
		}
		LinearInterpolator lin = new LinearInterpolator();
		animation.setInterpolator(lin);
		animation.setFillAfter(true);
		if (iv_up_down != null) {
			iv_up_down.startAnimation(animation);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (animation != null && iv_up_down != null && animation.hasStarted()) {
			iv_up_down.clearAnimation();
			iv_up_down.startAnimation(animation);
		}
	}

	/** 玩法说明 */
	private void playExplain() {
		Intent intent = new Intent(Select_jczqActivity.this,
				PlayDescription.class);
		Select_jczqActivity.this.startActivity(intent);
	}

	/** 重写返回键事件 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK)
			exit();
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		if (!isEmptJCZQ()) {
			dialog.show();
			dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
			dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
				@Override
				public void getResult(int resultCode) {
					// TODO Auto-generated method stub
					if (1 == resultCode) {// 确定
						ExpandAdapterJCZQPassMore.clearAllDate();
						ExpandAdapterJCZQPassSingle.clearAllDate();
						clearAllData();
						Select_jczqActivity.this.finish();
						for (int i = 0; i < App.activityS1.size(); i++) {
							App.activityS1.get(i).finish();
						}
					}
				}
			});
		} else {
			ExpandAdapterJCZQPassMore.clearAllDate();
			ExpandAdapterJCZQPassSingle.clearAllDate();
			clearAllData();
			Select_jczqActivity.this.finish();
			for (int i = 0; i < App.activityS1.size(); i++) {
				App.activityS1.get(i).finish();
			}
		}
	}

	/** 刷新Adapter */
	public void updateAdapter() {
	}

	/** 清空选中情况 */
	public static void clearHashSet() {
		ExpandAdapterJCZQPassMore.clearAllDate();
		ExpandAdapterJCZQPassSingle.clearAllDate();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		super.onNewIntent(intent);
	}

	/**
	 * 判断竞彩足球的所有map是否为空
	 * 
	 * @return
	 */
	public static boolean isEmptJCZQ() {
		if (0 == ExpandAdapterJCZQPassMore.map_hashMap_spf.size()
				&& 0 == ExpandAdapterJCZQPassMore.map_hashMap_bqc.size()
				&& 0 == ExpandAdapterJCZQPassMore.map_hashMap_cbf.size()
				&& 0 == ExpandAdapterJCZQPassMore.map_hashMap_hhtz.size()
				&& 0 == ExpandAdapterJCZQPassMore.map_hashMap_zjq.size()
				&& 0 == ExpandAdapterJCZQPassSingle.map_hashMap_spf.size()
				&& 0 == ExpandAdapterJCZQPassSingle.map_hashMap_bqc.size()
				&& 0 == ExpandAdapterJCZQPassSingle.map_hashMap_cbf.size()
				&& 0 == ExpandAdapterJCZQPassSingle.map_hashMap_zjq.size()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 清空筛选对阵
	 */
	public static void clearAllData(){
		if(null!=passMoreDMList){
			passMoreDMList.clear();
		}
		if(null!=passSingleDMList){
			passSingleDMList.clear();
		}
	}
}
