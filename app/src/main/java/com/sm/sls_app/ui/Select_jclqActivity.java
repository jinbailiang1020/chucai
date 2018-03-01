package com.sm.sls_app.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.DtMatch_Basketball;
import com.sm.sls_app.ui.adapter.ExpandAdapter_jclq;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.view.ConfirmDialog;
import com.sm.sls_app.view.IphoneTreeView;
import com.sm.sls_app.view.MyJCDialog_jclq;
import com.sm.sls_app.view.MyToast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** 竞彩篮球 */
public class Select_jclqActivity extends Activity implements OnClickListener {

	/* 头部 */
	private ImageButton btn_back; // 返回
	private ImageButton btn_screen; // 筛选
	private ImageView iv_up_down;// 玩法提示图标
	private LinearLayout layout_select_playtype;// 玩法选择
	private Button btn_playtype;// 玩法
	private Animation animation = null;
	private ImageButton btn_playinfo;// 帮助

	private Playtype_adapter adapter;
	private boolean isChecked = false;
	private Button btn_ok, btn_clear;
	private TextView tv_showCount; // 显示选择场数
	private IphoneTreeView ex_listView;
	private ExpandAdapter_jclq exAdapter;
	private Set<String> set;
	private MyJCDialog_jclq myDialog;
	private Intent intent;
	public int total = 0;
	private int type = 7301;
	private boolean sf = true, dx = false;
	public Button btn_shengfu;// 胜负
	public Button btn_daxiao;// 大小
	public int select_witch = 0;// 选中哪一个
	public PopupWindow popWindow;
	public static RelativeLayout select_jclq_title;
	private String[] type_name = { "胜负", "让分胜负", "胜分差", "大小分", "混合过关", "" };
	// 7301胜负 7302让分胜负 7303胜分差 7404 大小分 7306 混合过关
	List<String> listgamea = new ArrayList<String>();
	private ConfirmDialog dialog;// 提示框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_select_jclq);
		App.activityS.add(this);
		App.activityS1.add(this);
		findView();
		setListener();
		init();
		changeTextShow();
	}

	/** 初始界面 **/
	private void init() {

		isChecked = getIntent().getBooleanExtra("canChange", true);
		if (type == 7301)
			show_sf();
		else if (type == 7304)
			show_dx();
		else if (type == 7303)
			show_sfc();
		else if (type == 7302)
			show_rfsf();
		else if (type == 7306)
			show_hhgg();
		dialog = new ConfirmDialog(this, R.style.dialog);
	}

	/** 初始化UI */
	private void findView() {
		// TODO Auto-generated method stub
		layout_select_playtype = (LinearLayout) this
				.findViewById(R.id.layout_select_playtype);

		tv_showCount = (TextView) findViewById(R.id.tv_tip_jc);

		btn_screen = (ImageButton) this.findViewById(R.id.btn_screen);
		btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		iv_up_down = (ImageView) findViewById(R.id.iv_up_down);
		btn_playtype = (Button) this.findViewById(R.id.btn_playtype);
		btn_ok = (Button) findViewById(R.id.btn_submit);
		btn_clear = (Button) findViewById(R.id.btn_clearall);
		btn_playinfo = (ImageButton) this.findViewById(R.id.btn_help);

		ex_listView = (IphoneTreeView) this.findViewById(R.id.jclq_exListView);

		View view = getLayoutInflater().inflate(
				R.layout.select_jczq_spf_groups, ex_listView, false);
		ex_listView.setHeaderView(view);
		exAdapter = new ExpandAdapter_jclq(this,
				this.setList_Matchs(AppTools.lottery.getDtMatch_Basketball()),
				ex_listView);

		// 把筛选的队存起来
		set = new HashSet<String>();// 存“欧冠”等
		for (List<DtMatch_Basketball> listMatch : AppTools.lottery
				.getDtMatch_Basketball()) {
			for (DtMatch_Basketball match : listMatch) {
				if (!set.contains(match.getGame())) {
					set.add(match.getGame());
				}
			}
		}

		ex_listView.setAdapter(exAdapter);

		// 设置全部展开
		for (int i = 0; i < exAdapter.getGroupCount(); i++) {
			ex_listView.expandGroup(i);
		}
		select_jclq_title = (RelativeLayout) findViewById(R.id.select_jclq_title);
		myDialog = new MyJCDialog_jclq(Select_jclqActivity.this, set,
				R.style.dialog_screen);
        type = getIntent().getIntExtra("playType", 7301);
	}

	class Playtype_adapter extends BaseAdapter {
		int index = 0;// 选中下标

		private void setindex(int index) {
			this.index = index;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return type_name.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return type_name[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			// 判断View是否为空
			if (view == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater
						.from(Select_jclqActivity.this);
				// 得到布局文件
				view = inflater.inflate(R.layout.item_pop_lv_gv, null);
				// 得到控件
				holder.type_name = (TextView) view
						.findViewById(R.id.gv_tv_playType);
				holder.type_name.setBackgroundResource(R.drawable.btn_playtype);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			holder.type_name.setText(type_name[position]);
			if (index == position)
				holder.type_name
						.setBackgroundResource(R.drawable.btn_playtype_select);
			return view;
		}

	}

	class ViewHolder {
		TextView type_name;
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

	/** 创建popWindow */
	private void createPopWindow() {
		rote(1);
		LayoutInflater inflact = LayoutInflater.from(Select_jclqActivity.this);
		View view = inflact.inflate(R.layout.pop_jclq, null);

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);

		popWindow = new PopupWindow(view, metric.widthPixels,
				metric.heightPixels);

		popWindow.setTouchable(true); // 设置PopupWindow可触摸
		popWindow.setOutsideTouchable(true); // 设置PopupWindow外部区域是否可触摸
		popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				rote(2);
			}
		});// 设置popwindow的消失事件
			// 设置之后点击返回键 popwindow 会消失
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.setFocusable(true);
		if (adapter == null)
			adapter = new Playtype_adapter();
		GridView type_name = (GridView) view.findViewById(R.id.gv_type_jclq);
		type_name.setAdapter(adapter);
		type_name.setOnItemClickListener(new daapter_Listener());

		// 监听返回按钮事件，因为此时焦点在popupwindow上，如果不监听，返回按钮没有效果
		OnKeyListener listener = new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				System.out.println("点击了");
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					if (popWindow != null && popWindow.isShowing()) {
						popWindow.dismiss();
						rote(2);
					}
					break;
				}
				return true;
			}
		};

		// 监听点击事件，点击其他位置，popupwindow小窗口消失
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popWindow != null && popWindow.isShowing()) {
					popWindow.dismiss();
					rote(2);
					popWindow = null;
				}
				return true;
			}
		});
	}

	/**
	 * 关闭popwindow
	 */
	private void dismissPopWindow() {
		if (null != popWindow && popWindow.isShowing()) {
			popWindow.dismiss();
		}
	}

	class daapter_Listener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			switch (position) {
			case 0:
				show_sf();
				break;
			case 1:
				show_rfsf();
				break;
			case 2:
				show_sfc();
				break;
			case 3:
				show_dx();
				break;
			case 4:
				show_hhgg();
				break;
			case 5:
				dismissPopWindow();
				return;
			default:
				break;
			}
			adapter.setindex(position);
			btn_playtype.setText(type_name[position]);
			dismissPopWindow();
			rote(2);// 关闭popwindow
		}
	}

	/** 绑定监听 */
	private void setListener() {
		// TODO Auto-generated method stub
		btn_back.setOnClickListener(this);
		btn_playinfo.setOnClickListener(this);
		btn_screen.setOnClickListener(this);
		btn_playtype.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
		btn_clear.setOnClickListener(this);
		layout_select_playtype.setOnClickListener(this);
		btn_playinfo.setOnClickListener(this);
	}

	/** 刷新 */
	public void updateAdapter() {
		exAdapter.notifyDataSetChanged();
		changeTextShow();
	}

	/** 刷新 */
	public void updateAdapter(List<String> list, int type) {
		Log.i("x", "刷新");
		System.out.println(list.toString());
		System.out.println(type);
		listgamea = list;
		exAdapter.setList_Matchs(setList_Matchs(list, type));
		// 设置全部展开
		for (int i = 0; i < exAdapter.getGroupCount(); i++) {
			ex_listView.expandGroup(i);
		}
		updateAdapter();
	}

	/** 根据所选的赛事 筛选出队 */
	public List<List<DtMatch_Basketball>> setList_Matchs(List<String> _list,
			int type) {
		List<List<DtMatch_Basketball>> list_Matchs = new ArrayList<List<DtMatch_Basketball>>();

		for (List<DtMatch_Basketball> list : AppTools.lottery
				.getDtMatch_Basketball()) {

			List<DtMatch_Basketball> listM = new ArrayList<DtMatch_Basketball>();

			for (DtMatch_Basketball dt : list) {
				for (String str : _list) {
					if (dt.getGame().equals(str)) {
						switch (type) {
						case 0:
							if (Integer.parseInt(dt.getLetScore()) == 0)
								listM.add(dt);
							break;
						case 1:
							if (Integer.parseInt(dt.getLetScore()) == 0)
								listM.add(dt);
							break;
						case 100:
							listM.add(dt);
							break;
						}
					}
				}
			}
			if (listM.size() != 0)
				list_Matchs.add(listM);
		}

		return list_Matchs;
	}

	/** 点击 **/
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/** 筛选 **/
		case R.id.btn_screen:
			myDialog.set(listgamea);
			myDialog.show();
			break;
		/** 点击下拉框 **/
		case R.id.btn_back:
			System.out.println("back------------");
			onKeyDown(KeyEvent.KEYCODE_BACK, new KeyEvent(10, 10));
			break;
		/** 点击下拉框 **/
		case R.id.layout_select_playtype:
		case R.id.btn_playtype:
			if (isChecked) {
				createPopWindow();
				popWindow.showAsDropDown(select_jclq_title);
			}
			break;
		/** 点击选好了 **/
		case R.id.btn_submit:
			submit();
			break;
		/** 点击清空 **/
		case R.id.btn_clearall:
			clearSelect();
			break;
		case R.id.btn_help:
			intent = new Intent(Select_jclqActivity.this, PlayDescription.class);
			Select_jclqActivity.this.startActivity(intent);
			break;
		}
	}

	/** 根据玩法筛选出 队伍 **/
	private List<List<DtMatch_Basketball>> setList_Matchs(
			List<List<DtMatch_Basketball>> _list) {
		List<List<DtMatch_Basketball>> list = new ArrayList<List<DtMatch_Basketball>>();
		for (List<DtMatch_Basketball> list_dt : _list) {
			List<DtMatch_Basketball> list2 = new ArrayList<DtMatch_Basketball>();
			for (DtMatch_Basketball dt : list_dt) {
				if (setList_Matchs(dt))
					if (setList_Matchs(dt))
						list2.add(dt);
			}
			list.add(list2);
		}
		return list;
	}

	/** 根据类型筛选出 队伍 **/
	private boolean setList_Matchs(DtMatch_Basketball dt) {
		switch (this.type) {
		case 7301:
			return dt.isSF();
		case 7304:
			return dt.isDXF();
		case 7302:
			return dt.isSF();
		case 7303:
			return dt.isSFC();
		case 7306:
			return true;
		default:
			return false;
		}
	}

	/** 显示胜负 **/
	private void show_sf() {
		btn_playtype.setText("  胜 负");
		type = 7301;
		select_witch = 1;
		exAdapter.setPlayType(1);
		btn_playtype.setText(type_name[0]);
		exAdapter.setList_Matchs(this.setList_Matchs(AppTools.lottery
				.getDtMatch_Basketball()));
		ExpandAdapter_jclq.map_hashMap_dx.clear();
		ExpandAdapter_jclq.map_hashMap_cbf.clear();
		ExpandAdapter_jclq.map_hashMap_rfsf.clear();
		ExpandAdapter_jclq.map_hashMap_hhtz.clear();
		this.updateAdapter();
	}

	/** 显示大小分 **/
	private void show_dx() {
		type = 7304;
		select_witch = 2;
		exAdapter.setPlayType(2);
		btn_playtype.setText(type_name[3]);
		exAdapter.setList_Matchs(this.setList_Matchs(AppTools.lottery
				.getDtMatch_Basketball()));
		ExpandAdapter_jclq.map_hashMap_sf.clear();
		ExpandAdapter_jclq.map_hashMap_cbf.clear();
		ExpandAdapter_jclq.map_hashMap_rfsf.clear();
		ExpandAdapter_jclq.map_hashMap_hhtz.clear();
		this.updateAdapter();
	}

	/** 让分胜负 **/
	private void show_rfsf() {
		type = 7302;
		select_witch = 3;
		exAdapter.setPlayType(3);
		btn_playtype.setText(type_name[1]);
		exAdapter.setList_Matchs(this.setList_Matchs(AppTools.lottery
				.getDtMatch_Basketball()));
		ExpandAdapter_jclq.map_hashMap_dx.clear();
		ExpandAdapter_jclq.map_hashMap_sf.clear();
		ExpandAdapter_jclq.map_hashMap_cbf.clear();
		ExpandAdapter_jclq.map_hashMap_hhtz.clear();
		this.updateAdapter();
	}

	/** 显示胜分差 **/
	private void show_sfc() {
		type = 7303;
		select_witch = 4;
		exAdapter.setPlayType(4);
		btn_playtype.setText(type_name[2]);
		exAdapter.setList_Matchs(this.setList_Matchs(AppTools.lottery
				.getDtMatch_Basketball()));
		ExpandAdapter_jclq.map_hashMap_dx.clear();
		ExpandAdapter_jclq.map_hashMap_sf.clear();
		ExpandAdapter_jclq.map_hashMap_rfsf.clear();
		ExpandAdapter_jclq.map_hashMap_hhtz.clear();
		this.updateAdapter();
	}

	/** 显示混合过关 **/
	private void show_hhgg() {
		type = 7306;
		select_witch = 5;
		exAdapter.setPlayType(5);
		btn_playtype.setText(type_name[4]);
		exAdapter.setList_Matchs(this.setList_Matchs(AppTools.lottery
				.getDtMatch_Basketball()));
		ExpandAdapter_jclq.map_hashMap_dx.clear();
		ExpandAdapter_jclq.map_hashMap_sf.clear();
		ExpandAdapter_jclq.map_hashMap_cbf.clear();
		ExpandAdapter_jclq.map_hashMap_rfsf.clear();
		this.updateAdapter();
	}

	/** 提交投注信息 */
	private void submit() {
		Log.i("x", "点击提交");
		if (total < 2) {
			MyToast.getToast(Select_jclqActivity.this, "请至少选泽两场比赛").show();
		} else {
			intent = new Intent(Select_jclqActivity.this,
                    Bet_JCLQ_Activity.class);
			if (select_witch == 1 || select_witch == 0)
				intent.putExtra("type", 7301);
			else if (select_witch == 2)
				intent.putExtra("type", 7304);
			else if (select_witch == 3)
				intent.putExtra("type", 7302);
			else if (select_witch == 4)
				intent.putExtra("type", 7303);
			else if (select_witch == 5)
				intent.putExtra("type", 7306);
			Select_jclqActivity.this.startActivity(intent);
			this.finish();
		}
	}

	/** 清空所选的 比赛 */
	public void clearSelect() {
		ExpandAdapter_jclq.map_hashMap_dx.clear();
		ExpandAdapter_jclq.map_hashMap_sf.clear();
		ExpandAdapter_jclq.map_hashMap_cbf.clear();
		ExpandAdapter_jclq.map_hashMap_rfsf.clear();
		ExpandAdapter_jclq.map_hashMap_hhtz.clear();
		changeTextShow();
		exAdapter.notifyDataSetChanged();
	}

	/** 改变显示值 */
	public void changeTextShow() {
		total = 0;
		System.out.println("exAdapter.getPlayType()=="
				+ exAdapter.getPlayType());
		switch (exAdapter.getPlayType()) {
		case 1:
			if (null != ExpandAdapter_jclq.map_hashMap_sf) {
				for (int i = 0; i < 3; i++) {
					if (ExpandAdapter_jclq.map_hashMap_sf.containsKey(i))
						total += ExpandAdapter_jclq.map_hashMap_sf.get(i)
								.size();
				}
			}
			break;
		case 2:
			if (null != ExpandAdapter_jclq.map_hashMap_dx) {
				for (int i = 0; i < 3; i++) {
					if (ExpandAdapter_jclq.map_hashMap_dx.containsKey(i))
						total += ExpandAdapter_jclq.map_hashMap_dx.get(i)
								.size();
				}
			}
			break;
		case 3:
			if (null != ExpandAdapter_jclq.map_hashMap_rfsf) {
				for (int i = 0; i < 3; i++) {
					if (ExpandAdapter_jclq.map_hashMap_rfsf.containsKey(i))
						total += ExpandAdapter_jclq.map_hashMap_rfsf.get(i)
								.size();
				}
			}
			break;
		case 4:
			if (null != ExpandAdapter_jclq.map_hashMap_cbf) {
				for (int i = 0; i < 3; i++) {
					if (ExpandAdapter_jclq.map_hashMap_cbf.containsKey(i))
						total += ExpandAdapter_jclq.map_hashMap_cbf.get(i)
								.size();
				}
			}
			break;
		case 5:
			if (null != ExpandAdapter_jclq.map_hashMap_hhtz) {
				for (int i = 0; i < 3; i++) {
					if (ExpandAdapter_jclq.map_hashMap_hhtz.containsKey(i))
						total += ExpandAdapter_jclq.map_hashMap_hhtz.get(i)
								.size();
				}
			}
			break;
		default:
			break;
		}
		if (total == 0)
			tv_showCount.setText("请至少选择2场");
		else
			tv_showCount.setText("你已经选择了" + total + "场");
	}

	/** 异步任务 用来后台获取数据 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> {
		@Override
		protected String doInBackground(Void... params) {
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

	}

	/** 重写返回键事件 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (total != 0) {
				dialog.show();
				dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
					@Override
					public void getResult(int resultCode) {
						// TODO Auto-generated method stub
						if (1 == resultCode) {// 确定
							clearSelect();
							for (int i = 0; i < App.activityS1.size(); i++) {
								App.activityS1.get(i).finish();
							}
						}
					}
				});
			} else {
				clearSelect();
				for (int i = 0; i < App.activityS1.size(); i++) {
					App.activityS1.get(i).finish();
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/** 点击确认退出程序 */
	class positiveClick implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			clearSelect();
			Select_jclqActivity.this.startActivity(new Intent(
					Select_jclqActivity.this, MainActivity.class));
			Select_jclqActivity.this.finish();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		super.onNewIntent(intent);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		init();
	}

}
