package com.sm.sls_app.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.Lottery;
import com.sm.sls_app.dataaccess.SelectedNumbers;
import com.sm.sls_app.fragment.HallFragment;
import com.sm.sls_app.ui.adapter.GridViewCJDLTAdapter;
import com.sm.sls_app.ui.adapter.LuckPopAdapter;
import com.sm.sls_app.ui.adapter.MyGridViewAdapter;
import com.sm.sls_app.ui.adapter.MyGridViewAdapterFC3D;
import com.sm.sls_app.ui.adapter.MyGridViewAdapterPL3;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.NumberTools;
import com.sm.sls_app.view.MyDateTimeDialog;
import com.sm.sls_app.view.MyLuckNumber;
import com.sm.sls_app.view.MyLuckyView;
import com.sm.sls_app.view.MyToast;
import com.sm.sls_app.wheel.widget.NumericWheelAdapter;


/**
 * 功能：幸运号码Activity，实现幸运号码的获取 版本
 * 
 * @author Administrator
 */
public class LuckNumberActivity extends Activity implements OnClickListener {

	private Button btn_select, btn_select_name, btn_select_ql, btn_select_sr,
			btn_lottery, btn_count, btn_xz, btn_sx, btn_sr, btn_ql, btn_name;

	private RelativeLayout rl_img_xz_sx, rl_name, rl_ql, rl_sr;

	private EditText et_name, et_name1, et_name2;
	private TextView tv_year, tv_month, tv_day;

	private Animation rotate;
	private PopupWindow popWindow;
	
	private ImageButton btn_back;

	private MyLuckNumber luckNumber_dialog;
	private String[] luckNumbers;
	private int redNum, blueNum, redMax, blueMax;
	private boolean isZero, canZero, canSelect = true;

	private String lotteryId;
	private Class _betClass;
	private final String[] lotteryItems = { "双色球","3D", "大乐透",  "排列三", "排列五","江苏快3",
			"重庆时时彩","十一运夺金", "江西11选5","广东11选5","江西时时彩"};
	
	private final String[] numItems = { "1注", "5注", "10注" };
//	private final String[] numItems = { "1注", "2注", "3注", "4注", "5注", "6注",
//			"7注", "8注", "9注", "10注" };

	
	private int lotterySelect = 0, numSelect = 0;
	private int popType = 1;

	private ImageView img_back, img_up, img_type;

	/** 弹出日期框 **/
	private MyDateTimeDialog dateDialog;
	private NumericWheelAdapter yearAdapter, monthAdapter, dayAdapter;
	private int year, month, days;
	private int num=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_luck_number);
		App.activityS.add(this);
		findView();
		init();
		setRandom(0);
		setListener();
	}

	/** 初始化UI */
	private void findView() {
		btn_select = (Button) this.findViewById(R.id.select);
		btn_select_name = (Button) this.findViewById(R.id.selectName);
		btn_select_ql = (Button) this.findViewById(R.id.selectName_ql);
		btn_select_sr = (Button) this.findViewById(R.id.select_birthday);
		btn_lottery = (Button) this.findViewById(R.id.button_lottery);
		btn_count = (Button) this.findViewById(R.id.button_count);

		btn_xz = (Button) this.findViewById(R.id.img_1);
		btn_sx = (Button) this.findViewById(R.id.img_2);
		btn_sr = (Button) this.findViewById(R.id.img_3);
		btn_name = (Button) this.findViewById(R.id.img_4);
		btn_ql = (Button) this.findViewById(R.id.img_5);

		rl_ql = (RelativeLayout) this.findViewById(R.id.luck_rl_ql);
		rl_sr = (RelativeLayout) this.findViewById(R.id.luck_rl_sr);

		rl_img_xz_sx = (RelativeLayout) this.findViewById(R.id.luck_rl_xz_sx);
		rl_name = (RelativeLayout) this.findViewById(R.id.luck_rl_name);

		et_name = (EditText) this.findViewById(R.id.img_edit);
		et_name1 = (EditText) this.findViewById(R.id.img_edit_1);
		et_name2 = (EditText) this.findViewById(R.id.img_edit_2);
		tv_year = (TextView) this.findViewById(R.id.img_edit_year);
		tv_month = (TextView) this.findViewById(R.id.img_edit_month);
		tv_day = (TextView) this.findViewById(R.id.img_edit_day);

		img_back = (ImageView) this.findViewById(R.id.img_bottom);
		img_up = (ImageView) this.findViewById(R.id.img_up);
		img_type = (ImageView) this.findViewById(R.id.img_type);
		btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		@SuppressWarnings("unused")
		final MyLuckyView luckView = new MyLuckyView(img_back, img_up);
	}

	private void init() {
		// 得到Calendar类的实例。
		Calendar now = Calendar.getInstance();
		year = now.get(Calendar.YEAR);
		month = now.get(Calendar.MONTH) + 1;
		days = now.get(Calendar.DATE);

		yearAdapter = new NumericWheelAdapter(1880, 2050);
		monthAdapter = new NumericWheelAdapter(1, 12);
		dayAdapter = new NumericWheelAdapter(1,
				AppTools.getLastDay(year, month));

		dateDialog = new MyDateTimeDialog(LuckNumberActivity.this,
				R.style.dialog, yearAdapter, monthAdapter, dayAdapter,
				new MyClickListener());
		dateDialog.initDay(year, month, days);
	}

	/** Date dialog 点击监听 */
	class MyClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.funds_btn_ok:
				dateDialog.dismiss();
				setDays(dateDialog.y, dateDialog.m, dateDialog.d);
				break;
			case R.id.funds_btn_no:
				dateDialog.dismiss();
				break;
			}
			setDays();
			dateDialog.setCheckItem();

		}
	}

	/** 设置日期 **/
	public void setDays(int y, int m, int d) {
		// 得到Calendar类的实例。
		Calendar now = Calendar.getInstance();
		int nowyear = now.get(Calendar.YEAR);
		int nowmonth = now.get(Calendar.MONTH) + 1;
		int nowdays = now.get(Calendar.DATE);
		boolean istrue=dateDialog.compareData(nowyear, nowmonth, nowdays, y, m, d);
		if(istrue){
			this.year = y;
			this.month = m;
			this.days = d;
			tv_year.setText(year + "年");
			tv_month.setText(month + "月");
			tv_day.setText(days + "日");
		}else {
			tv_year.setText("");
			tv_month.setText("");
			tv_day.setText("");
			MyToast.getToast(getApplicationContext(), "您选择的出生日期超出当前时间，请重新选择").show();
		}
	}

	public void setDays() {
		dateDialog.initDay(year, month, days);
	}

	/** 旋转动画 */
	private void rotateImage() {
		rotate = AnimationUtils.loadAnimation(LuckNumberActivity.this,
				R.anim.rote);
		rl_img_xz_sx.startAnimation(rotate);
		rotate.setAnimationListener(new MyAnimationListener());
	}

	/** 绑定监听 */
	private void setListener() {
		btn_select.setOnClickListener(this);
		btn_select_name.setOnClickListener(this);
		btn_select_ql.setOnClickListener(this);
		btn_select_sr.setOnClickListener(this);
		btn_lottery.setOnClickListener(this);
		btn_count.setOnClickListener(this);

		btn_xz.setOnClickListener(this);
		btn_sx.setOnClickListener(this);
		btn_name.setOnClickListener(this);
		btn_sr.setOnClickListener(this);
		btn_ql.setOnClickListener(this);

		rl_sr.setOnClickListener(this);
		tv_year.setOnClickListener(this);
		tv_month.setOnClickListener(this);
		tv_day.setOnClickListener(this);
		btn_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.select:
			if (canSelect) {
				if (null != rotate)
					return;
				if (popWindow != null && popWindow.isShowing())
					return;
				rotateImage();
			} else
				MyToast.getToast(LuckNumberActivity.this, "该彩种奖期已结束，请选择其他彩种")
						.show();
			break;
		case R.id.selectName:
			if (canSelect) {
				if (et_name.getText().toString().trim().length() == 0) {
					Toast.makeText(LuckNumberActivity.this, "请输入您的名字", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				showLuckyNumberDialog();
			} else
				MyToast.getToast(LuckNumberActivity.this, "该彩种奖期已结束，请选择其他彩种")
						.show();
			break;
		case R.id.selectName_ql:
			if (canSelect) {
				if (et_name1.getText().toString().trim().length() == 0
						|| et_name2.getText().toString().trim().length() == 0) {
					Toast.makeText(LuckNumberActivity.this, "请输入您和您另一半的名字",
                            Toast.LENGTH_SHORT).show();
					return;
				}
				showLuckyNumberDialog();
			} else
				MyToast.getToast(LuckNumberActivity.this, "该彩种奖期已结束，请选择其他彩种")
						.show();
			break;
		case R.id.select_birthday:
			if (canSelect) {
				if (tv_year.getText().toString().trim().length() == 0
						|| tv_month.getText().toString().trim().length() == 0
						|| tv_day.getText().toString().trim().length() == 0) {
					Toast.makeText(LuckNumberActivity.this, "请输入您出生年月", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				showLuckyNumberDialog();
			} else
				MyToast.getToast(LuckNumberActivity.this, "该彩种奖期已结束，请选择其他彩种")
						.show();
			break;
		case R.id.button_lottery:
			selectLottery();
			break;
		case R.id.button_count:
			selectCount();
			break;
		case R.id.img_1:
			changeBtnImageBack(1);
			break;
		case R.id.img_2:
			changeBtnImageBack(2);
			break;
		case R.id.img_3:
			changeBtnImageBack(3);
			break;
		case R.id.img_4:
			changeBtnImageBack(4);
			break;
		case R.id.img_5:
			changeBtnImageBack(5);
			break;
		case R.id.luck_rl_sr:
		case R.id.img_edit_year:
		case R.id.img_edit_month:
		case R.id.img_edit_day:
			dateDialog.show();
			break;
		case R.id.btn_back:
			this.finish();
			break;
		default:
			break;
		}
	}

	/** 改变按钮背景 */
	private void changeBtnImageBack(int type) {
		switch (type) {
		/** 星座 */
		case 1:
			img_type.setBackgroundResource(R.drawable.luck_type_xingzuo);
			img_up.setBackgroundResource(R.drawable.xingzuo);

			setRelativeLayoutVisible(rl_img_xz_sx, btn_select);
			break;
		/** 属象 */
		case 2:
			img_type.setBackgroundResource(R.drawable.luck_type_sx);
			img_up.setBackgroundResource(R.drawable.shengxiao);
			setRelativeLayoutVisible(rl_img_xz_sx, btn_select);
			break;
		/** 生日 */
		case 3:
			img_type.setBackgroundResource(R.drawable.luck_type_sr);

			setRelativeLayoutVisible(rl_sr, btn_select_name);
			break;
		/** 姓名 */
		case 4:
			et_name.setText("");
			img_type.setBackgroundResource(R.drawable.luck_type_xm);

			setRelativeLayoutVisible(rl_name, btn_select_name);
			break;
		/** 情侣 */
		case 5:
			et_name1.setText("");
			et_name2.setText("");
			img_type.setBackgroundResource(R.drawable.luck_type_ql);

			setRelativeLayoutVisible(rl_ql, btn_select_name);
			break;
		}
	}

	/** 设置转盘类型 隐藏可见 */
	private void setRelativeLayoutVisible(RelativeLayout rl, Button btn) {
		rl_img_xz_sx.setVisibility(View.GONE);
		rl_name.setVisibility(View.GONE);
		rl_ql.setVisibility(View.GONE);
		rl_sr.setVisibility(View.GONE);

		btn_select.setVisibility(View.GONE);
		btn_select_name.setVisibility(View.GONE);

		rl.setVisibility(View.VISIBLE);
		btn.setVisibility(View.VISIBLE);
	}

	/** 点击 选择彩种Button */
	private void selectLottery() {
		popType = 1;
		if (popWindow != null && popWindow.isShowing()) {
			popWindow.dismiss();
			popWindow = null;
		} else {
			createPopWindow(lotteryItems, lotterySelect);
			popWindow.showAsDropDown(btn_lottery);
		}

	}

	/** 点击 选择注数 Button */
	private void selectCount() {
		popType = 2;
		if (popWindow != null && popWindow.isShowing()) {
			popWindow.dismiss();
			popWindow = null;
		} else {
			createPopWindow(numItems, numSelect);
			popWindow.showAsDropDown(btn_count, 15, 0);
		}
	}

	/** 创建popWindow */
	private void createPopWindow(String[] strItems, int _select) {
		LayoutInflater inflact = LayoutInflater.from(LuckNumberActivity.this);

		View view = inflact.inflate(R.layout.luck_popwindow, null);
		ListView popList = (ListView) view.findViewById(R.id.luck_pop_listView);
		LuckPopAdapter luckAdapter = new LuckPopAdapter(
				getApplicationContext(), strItems);
		luckAdapter.setSelect(_select);
		popList.setAdapter(luckAdapter);

		float scale=AppTools.getDpi(this);
		popWindow = new PopupWindow(view,(int)(120*scale), (int)(180*scale));

		popWindow.setFocusable(true); // 设置PopupWindow可获得焦点
		popWindow.setTouchable(true); // 设置PopupWindow可触摸
		popWindow.setOutsideTouchable(true); // 设置PopupWindow外部区域是否可触摸

		popList.setOnItemClickListener(new MyPopItemClickListener());

		// 监听返回按钮事件，因为此时焦点在popupwindow上，如果不监听，返回按钮没有效果
		popList.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					if (popWindow != null && popWindow.isShowing()) {
						popWindow.dismiss();
					}

					break;
				}

				return true;
			}
		});

		// 监听点击事件，点击其他位置，popupwindow小窗口消失
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popWindow != null && popWindow.isShowing()) {
					popWindow.dismiss();
					popWindow = null;
				}

				return true;
			}
		});
	}

	/** popWindow中ListView item 点击监听 **/
	class MyPopItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (popWindow != null && popWindow.isShowing()) {
				popWindow.dismiss();
				popWindow = null;
			}

			if (1 == popType) {
				lotterySelect = position;
				btn_lottery.setText(lotteryItems[position]);
				setRandom(position);
			} else if (2 == popType) {
				numSelect = position;
				btn_count.setText(numItems[position]);
			}
		}
	}

	
	/** 设置幸运选号的值 */
	private void setRandom(int positon) {
		switch (positon) {
		/** 双色球 **/
		case 0:
			lotteryId = "5";
			_betClass = BetActivity.class;
//			MyGridViewAdapterFC3D.playType = 501;
			setRandom(6, 1, 33, 16, true, false);
			break;
			/** 3D **/
		case 1:
			lotteryId = "6";
			_betClass = BetActivityFC3D.class;
//			MyGridViewAdapterFC3D.playType = 601;
			setRandom(3, 0, 9, 0, false, true);
			break;
		/** 大乐透 */
		case 2:
			lotteryId = "39";
			_betClass = Bet_DLT_Activity.class;
//			MyGridViewAdapterFC3D.playType = 3901;
			setRandom(5, 2, 35, 12, true, false);
			break;
		
		/** 排列三 **/
		case 3:
			lotteryId = "63";
			_betClass = BetActivityPL3.class;
//			MyGridViewAdapterFC3D.playType = 6301;
			setRandom(3, 0, 9, 0, false, true);
			break;
		/** 排列五 **/
		case 4:
			lotteryId = "64";
			_betClass = BetActivityPL5_QXC.class;
			setRandom(5, 0, 9, 0, false, true);
			break;
			 /**快3 **/
		 case 5:
		 lotteryId = "83";
		 _betClass = Bet_k3Activity.class;
		 setRandom(3, 0, 6, 0, false, false);
		 break;
//		/** 七星彩 */
//		case 5:
//			lotteryId = "3";
//			_betClass = BetActivityPL5_QXC.class;
//			setRandom(7, 0, 9, 0, false, true);
//			break;
//		/** 七乐彩 */
//		case 6:
//			lotteryId = "13";
//			_betClass = Bet_QLC_Activity.class;
//			setRandom(7, 0, 30, 0, true, false);
//			break;
		/**重庆 时时彩 */
		case 6:
			lotteryId = "28";
			_betClass = Bet_SSCActivity.class;
			setRandom(5, 0, 9, 0, false, true);
			break;
			
			 /*十一运夺金*/
        case 7:
            lotteryId = "62";
            _betClass = Bet_11x5Activity.class;
            setRandom(5, 0, 11, 0, true, false);
            break;
		/** 江西11选5 **/
        case 8:
            lotteryId = "70";
            _betClass = Bet_11x5Activity.class;
            setRandom(5, 0, 11, 0, true, false);
            break;
       
        /*广东11选5*/
        case 9:
            lotteryId = "78";
            _betClass = Bet_11x5Activity.class;
            setRandom(5, 0, 11, 0, true, false);
            break;
            /*江西时时彩*/
        case 10:
            lotteryId = "61";
            _betClass = Bet_JXSSCActivity.class;
			setRandom(5, 0, 9, 0, false, true);
            break; 

		default:
			break;
		}
		/** 拿到彩种信息 */
		for (Lottery lottery : HallFragment.listLottery) {
			if (lottery.getLotteryID().equals(lotteryId)) {
				AppTools.lottery = lottery;
			}
		}

		if (AppTools.lottery.getDistanceTime() - System.currentTimeMillis() <= 0) {
			MyToast.getToast(LuckNumberActivity.this, "该彩种奖期已结束，请选择其他彩种")
					.show();
			canSelect = false;
		} else {
			canSelect = true;
		}

	}

	private void setRandom(int _redNume, int _blueNum, int _redMax,
			int _blueMax, boolean _isZero, boolean _canZero) {
		this.redNum = _redNume;
		this.blueNum = _blueNum;
		this.redMax = _redMax;
		this.blueMax = _blueMax;
		this.isZero = _isZero;
		this.canZero = _canZero;
	}

	/** Animation listener */
	class MyAnimationListener implements AnimationListener {
		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			rotate = null;
			showLuckyNumberDialog();
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}
	}

	/** 弹出幸运号码 **/
	private void showLuckyNumberDialog() {
		if (null != AppTools.list_numbers)
			AppTools.list_numbers.clear();
		if(0==numSelect){
			num=numSelect + 1;
		}else if(1==numSelect){
			num=numSelect + 4;
		}else if(2==numSelect){
			num=numSelect + 8;
		}
		luckNumbers = new String[num];
		luckNumbers = NumberTools.getRandom(num, redNum, blueNum,
				redMax, blueMax, isZero, canZero);
		System.out.println("-----"+luckNumbers);
		luckNumber_dialog = new MyLuckNumber(LuckNumberActivity.this,
				R.style.dialog, luckNumbers, _betClass);
		setList_numbers(luckNumbers);
		luckNumber_dialog.show();
	}

	/** 将投注号码格式化 */
	private void setList_numbers(String[] numbers) {
		if (null == AppTools.list_numbers)
			AppTools.list_numbers = new ArrayList<SelectedNumbers>();
		AppTools.totalCount = num;
		
		
		switch (Integer.parseInt(lotteryId)) {
		case 5:
		case 39:
			AppTools.list_numbers.clear();
			SelectedNumbers sNumbers = null;
			for (int i = 0; i < numbers.length; i++) {
				sNumbers = new SelectedNumbers();
				sNumbers.setCount(1);
				sNumbers.setLotteryId(lotteryId);
				sNumbers.setMoney(sNumbers.getCount() * 2);

				String s = numbers[i].split("-")[0].replace("  ", ",").replace(
						" ", "");
				String s2[] = s.split(",");
				List<String> list = new ArrayList<String>();
				for (int j = 0; j < s2.length; j++) {
					list.add(s2[j]);
				}
				sNumbers.setRedNumbers(list);
				list.clear();
				String ss = numbers[i].split("-")[1].replace("  ", ",")
						.replace(" ", "");
				String ss2[] = ss.split(",");
				for (int j = 0; j < ss2.length; j++) {
					list.add(ss2[j]);
				}
				sNumbers.setBlueNumbers(list);
				sNumbers.setPlayType(Integer.parseInt(lotteryId + "01"));
				
				if(lotteryId.equals("5"))
				{
					MyGridViewAdapter.playType = 501;
				}
				if(lotteryId.equals("39"))
				{
					GridViewCJDLTAdapter.playType = 3901;
				}
				sNumbers.setLotteryNumber(numbers[i].trim().replace("  ", " ")
						.replace("- ", "-"));
				AppTools.list_numbers.add(sNumbers);
			}
			break;
		case 63:
		case 64:
		case 6:
		case 3:
		case 69:
		case 83:
			AppTools.list_numbers.clear();
			SelectedNumbers sNumbers2 = null;
			for (int i = 0; i < numbers.length; i++) {
				sNumbers2 = new SelectedNumbers();
				sNumbers2.setCount(1);
				sNumbers2.setLotteryId(lotteryId);
				sNumbers2.setMoney(sNumbers2.getCount() * 2);
				sNumbers2.setPlayType(Integer.parseInt(lotteryId + "01"));
				if(lotteryId.equals("83")){
					sNumbers2.setPlayType(Integer.parseInt(lotteryId + "06"));
				}
				sNumbers2.setShowLotteryNumber(numbers[i].replace("  ", " ")
						.trim());
				sNumbers2.setLotteryNumber(NumberTools
						.lotteryNumberFormatConvert(
								sNumbers2.getPlayType(),
								sNumbers2.getShowLotteryNumber()));
				if(lotteryId.equals("6")){
					sNumbers2.setLotteryNumber(numbers[i].replace("  ", ",").trim());
				}
				AppTools.list_numbers.add(sNumbers2);
			}
			if(lotteryId.equals("6"))
			{
				MyGridViewAdapterFC3D.playType = 601;
			}
			if(lotteryId.equals("63"))
			{
				MyGridViewAdapterPL3.playType = 6301;
			}
			break;
		case 62:
		case 70:
			AppTools.list_numbers.clear();
			SelectedNumbers sNumbers4 = null;
			for (int i = 0; i < numbers.length; i++) {
				sNumbers4 = new SelectedNumbers();
				sNumbers4.setCount(1);
				sNumbers4.setLotteryId(lotteryId);
				sNumbers4.setMoney(sNumbers4.getCount() * 2);

				sNumbers4.setPlayType(Integer.parseInt(lotteryId + "05"));

				sNumbers4.setShowLotteryNumber(numbers[i].replace("  ", " ")
						.trim());

				sNumbers4.setNumber(numbers[i].replace("  ", " ").trim());

				sNumbers4.setLotteryNumber(NumberTools
						.lotteryNumberFormatConvert(sNumbers4.getPlayType(),
								sNumbers4.getShowLotteryNumber()));

				AppTools.list_numbers.add(sNumbers4);
			}
			break;
		case 28:
		case 61:
			AppTools.list_numbers.clear();
			SelectedNumbers sNumbers5 = null;
			for (int i = 0; i < numbers.length; i++) {
				sNumbers5 = new SelectedNumbers();
				sNumbers5.setCount(1);
				sNumbers5.setLotteryId(lotteryId);
				sNumbers5.setMoney(sNumbers5.getCount() * 2);
				sNumbers5.setType(7);
				sNumbers5.setPlayType(Integer.parseInt(lotteryId + "03"));

				sNumbers5.setShowLotteryNumber(numbers[i].replace("  ", "")
						.trim());

				sNumbers5.setNumber(numbers[i].replace("  ", "-").trim());

				sNumbers5.setLotteryNumber(NumberTools
						.lotteryNumberFormatConvert(sNumbers5.getPlayType(),
								sNumbers5.getShowLotteryNumber()));

				AppTools.list_numbers.add(sNumbers5);
			}
			break;
		case 13:
			AppTools.list_numbers.clear();
			SelectedNumbers sNumbers3 = null;
			for (int i = 0; i < numbers.length; i++) {
				sNumbers3 = new SelectedNumbers();
				sNumbers3.setCount(1);
				sNumbers3.setLotteryId(lotteryId);
				sNumbers3.setMoney(sNumbers3.getCount() * 2);

				sNumbers3.setPlayType(Integer.parseInt(lotteryId + "01"));

				sNumbers3
						.setLotteryNumber(numbers[i].replace("  ", " ").trim());

				String[] s = sNumbers3.getLotteryNumber().split(" ");
				List<String> redList = new ArrayList<String>();
				for (int j = 0; j < s.length; j++) {
					redList.add(s[j]);
				}
				sNumbers3.setRedNumbers(redList);

				AppTools.list_numbers.add(sNumbers3);
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		App.activityS.remove(this);
		super.onDestroy();
	}

}
