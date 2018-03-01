package com.sm.sls_app.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.Lottery;
import com.sm.sls_app.dataaccess.WinDetail;
import com.sm.sls_app.dataaccess.WinLottery;
import com.sm.sls_app.fragment.HallFragment;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.view.MyToast;

public class WinLottery_k3_Activity extends Activity implements
		OnClickListener {

	private TextView tv_qi; // 期号
	private TextView tv_time; // 开奖时间
	private TextView tv_red_num; // 开奖号码

	// 奖期名称
	private TextView tv_name13,tv_name14,tv_name15,tv_name16,tv_name17,
			tv_name18,tv_name19,tv_name110,tv_name111,tv_name112,tv_name113,tv_name114,
			tv_name115,tv_name116,tv_name117,tv_name118, tv_name2, tv_name3, tv_name4, tv_name5,
			tv_name6,tv_name7,tv_name8;
	// 每注奖金
	private TextView tv_he3,tv_he4,tv_he5,tv_he6,tv_he7,tv_he8,tv_he9,tv_he10,tv_he11,tv_he12,
			tv_he13,tv_he14,tv_he15,tv_he16,tv_he17,tv_he18, tv_two, tv_three,
			tv_four, tv_five, tv_six,tv_seven,tv_eight;

	private Intent intent;
	private WinLottery winLottery;
	private List<WinDetail> listWinDetail;

	private Button btn_bet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_winlottery_number_k3);
		App.activityS.add(this);
		findView();
		init();
		setListener();

	}

	/**
	 * 初始化UI
	 */
	private void findView() {
		// 奖名
		tv_name13 = (TextView) this.findViewById(R.id.center_tv_level13);
		tv_name14 = (TextView) this.findViewById(R.id.center_tv_level14);
		tv_name15 = (TextView) this.findViewById(R.id.center_tv_level15);
		tv_name16 = (TextView) this.findViewById(R.id.center_tv_level16);
		tv_name17 = (TextView) this.findViewById(R.id.center_tv_level17);
		tv_name18 = (TextView) this.findViewById(R.id.center_tv_level18);
		tv_name19 = (TextView) this.findViewById(R.id.center_tv_level19);
		tv_name110 = (TextView) this.findViewById(R.id.center_tv_level110);
		tv_name111 = (TextView) this.findViewById(R.id.center_tv_level111);
		tv_name112 = (TextView) this.findViewById(R.id.center_tv_level112);
		tv_name113 = (TextView) this.findViewById(R.id.center_tv_level113);
		tv_name114 = (TextView) this.findViewById(R.id.center_tv_level114);
		tv_name115 = (TextView) this.findViewById(R.id.center_tv_level115);
		tv_name116 = (TextView) this.findViewById(R.id.center_tv_level116);
		tv_name117 = (TextView) this.findViewById(R.id.center_tv_level117);
		tv_name118 = (TextView) this.findViewById(R.id.center_tv_level118);
		tv_name2 = (TextView) this.findViewById(R.id.center_tv_level2);
		tv_name3 = (TextView) this.findViewById(R.id.center_tv_level3);
		tv_name4 = (TextView) this.findViewById(R.id.center_tv_level4);
		tv_name5 = (TextView) this.findViewById(R.id.center_tv_level5);
		tv_name6 = (TextView) this.findViewById(R.id.center_tv_level6);
		tv_name7 = (TextView) this.findViewById(R.id.center_tv_level7);
		tv_name8 = (TextView) this.findViewById(R.id.center_tv_level8);
		// 中奖金额
		tv_he3 = (TextView) this.findViewById(R.id.lottery_he3);
		tv_he4 = (TextView) this.findViewById(R.id.lottery_he4);
		tv_he5 = (TextView) this.findViewById(R.id.lottery_he5);
		tv_he6 = (TextView) this.findViewById(R.id.lottery_he6);
		tv_he7 = (TextView) this.findViewById(R.id.lottery_he7);
		tv_he8 = (TextView) this.findViewById(R.id.lottery_he8);
		tv_he9 = (TextView) this.findViewById(R.id.lottery_he9);
		tv_he10 = (TextView) this.findViewById(R.id.lottery_he10);
		tv_he11 = (TextView) this.findViewById(R.id.lottery_he11);
		tv_he12 = (TextView) this.findViewById(R.id.lottery_he12);
		tv_he13 = (TextView) this.findViewById(R.id.lottery_he13);
		tv_he14 = (TextView) this.findViewById(R.id.lottery_he14);
		tv_he15 = (TextView) this.findViewById(R.id.lottery_he15);
		tv_he16 = (TextView) this.findViewById(R.id.lottery_he16);
		tv_he17 = (TextView) this.findViewById(R.id.lottery_he17);
		tv_he18 = (TextView) this.findViewById(R.id.lottery_he18);
		tv_two = (TextView) this.findViewById(R.id.lottery_two);
		tv_three = (TextView) this.findViewById(R.id.lottery_three);
		tv_four = (TextView) this.findViewById(R.id.lottery_four);
		tv_five = (TextView) this.findViewById(R.id.lottery_five);
		tv_six = (TextView) this.findViewById(R.id.lottery_six);
		tv_seven = (TextView) this.findViewById(R.id.lottery_seven);
		tv_eight = (TextView) this.findViewById(R.id.lottery_eight);
		tv_time = (TextView) this.findViewById(R.id.lottery_time);
		tv_qi = (TextView) this.findViewById(R.id.lottery_qi);
		tv_red_num = (TextView) this.findViewById(R.id.lottery_num_red);
		btn_bet = (Button) this.findViewById(R.id.btn_to_bet);
	}

	/**
	 * 初始化属性
	 */
	private void init() {
		intent = getIntent();
		winLottery = (WinLottery) intent.getSerializableExtra("wLottery");
		tv_qi.setText("第" + winLottery.getName() + "期");
		tv_time.setText("开奖时间:" + winLottery.getEndTime());
		tv_red_num.setText(winLottery.getRedNum() + "");
		listWinDetail = new ArrayList<WinDetail>();
		listWinDetail = winLottery.getListWinDetail();
		if (listWinDetail != null && listWinDetail.size() > 0) {
//			tv_name1.setText(listWinDetail.get(0).getBonusName());
//			tv_one.setText("最大"+listWinDetail.get(0).getBonusValue());
//
//			tv_name2.setText(listWinDetail.get(1).getBonusName());
//			tv_two.setText(listWinDetail.get(1).getBonusValue());
//
//			tv_name3.setText(listWinDetail.get(2).getBonusName());
//			tv_three.setText(listWinDetail.get(2).getBonusValue());
//
//			tv_name4.setText(listWinDetail.get(3).getBonusName());
//			tv_four.setText(listWinDetail.get(3).getBonusValue());
//
//			tv_name5.setText(listWinDetail.get(4).getBonusName());
//			tv_five.setText(listWinDetail.get(4).getBonusValue());
//			
//			tv_name6.setText(listWinDetail.get(5).getBonusName());
//			tv_six.setText(listWinDetail.get(5).getBonusValue());
//			
//			tv_name7.setText(listWinDetail.get(6).getBonusName());
//			tv_seven.setText(listWinDetail.get(6).getBonusValue());
//			
//			tv_name8.setText(listWinDetail.get(7).getBonusName());
//			tv_eight.setText(listWinDetail.get(7).getBonusValue());
			tv_name13.setText(listWinDetail.get(0).getBonusName());
			tv_he3.setText(listWinDetail.get(0).getBonusValue());
			tv_name14.setText(listWinDetail.get(1).getBonusName());
			tv_he4.setText(listWinDetail.get(1).getBonusValue());
			tv_name15.setText(listWinDetail.get(2).getBonusName());
			tv_he5.setText(listWinDetail.get(2).getBonusValue());
			tv_name16.setText(listWinDetail.get(3).getBonusName());
			tv_he6.setText(listWinDetail.get(3).getBonusValue());
			tv_name17.setText(listWinDetail.get(4).getBonusName());
			tv_he7.setText(listWinDetail.get(4).getBonusValue());
			tv_name18.setText(listWinDetail.get(5).getBonusName());
			tv_he8.setText(listWinDetail.get(5).getBonusValue());
			tv_name19.setText(listWinDetail.get(6).getBonusName());
			tv_he9.setText(listWinDetail.get(6).getBonusValue());
			tv_name110.setText(listWinDetail.get(7).getBonusName());
			tv_he10.setText(listWinDetail.get(7).getBonusValue());
			tv_name111.setText(listWinDetail.get(8).getBonusName());
			tv_he11.setText(listWinDetail.get(8).getBonusValue());
			tv_name112.setText(listWinDetail.get(9).getBonusName());
			tv_he12.setText(listWinDetail.get(9).getBonusValue());
			tv_name113.setText(listWinDetail.get(10).getBonusName());
			tv_he13.setText(listWinDetail.get(10).getBonusValue());
			tv_name114.setText(listWinDetail.get(11).getBonusName());
			tv_he14.setText(listWinDetail.get(11).getBonusValue());
			tv_name115.setText(listWinDetail.get(12).getBonusName());
			tv_he15.setText(listWinDetail.get(12).getBonusValue());
			tv_name116.setText(listWinDetail.get(13).getBonusName());
			tv_he16.setText(listWinDetail.get(13).getBonusValue());
			tv_name117.setText(listWinDetail.get(14).getBonusName());
			tv_he17.setText(listWinDetail.get(14).getBonusValue());
			tv_name118.setText(listWinDetail.get(15).getBonusName());
			tv_he18.setText(listWinDetail.get(15).getBonusValue());
			
			tv_name2.setText(listWinDetail.get(16).getBonusName());
			tv_two.setText(listWinDetail.get(16).getBonusValue());
			
			tv_name3.setText(listWinDetail.get(17).getBonusName());
			tv_three.setText(listWinDetail.get(17).getBonusValue());
			
			tv_name4.setText(listWinDetail.get(18).getBonusName());
			tv_four.setText(listWinDetail.get(18).getBonusValue());
			
			tv_name5.setText(listWinDetail.get(19).getBonusName());
			tv_five.setText(listWinDetail.get(19).getBonusValue());
			
			tv_name6.setText(listWinDetail.get(20).getBonusName());
			tv_six.setText(listWinDetail.get(20).getBonusValue());
			
			tv_name7.setText(listWinDetail.get(21).getBonusName());
			tv_seven.setText(listWinDetail.get(21).getBonusValue());
			
			tv_name8.setText(listWinDetail.get(22).getBonusName());
			tv_eight.setText(listWinDetail.get(22).getBonusValue());
		}

	}

	private void setListener() {
		btn_bet.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_to_bet:
			long currentTime = System.currentTimeMillis();			
			for (Lottery lottery : HallFragment.listLottery) {
				if(lottery.getLotteryID().equals(winLottery.getLotteryId())){
					if(lottery.getDistanceTime()-currentTime <= 0){
						MyToast.getToast(WinLottery_k3_Activity.this, "该奖期已结束，请等下一期").show();
						return; 
					}
 				}
			}
			for (Lottery lottery : HallFragment.listLottery) {
				if (lottery.getLotteryID().equals("83")) {
					AppTools.lottery = lottery;
				}
			}
			Intent intent = new Intent(WinLottery_k3_Activity.this,
					Select_k3Activity.class);
			WinLottery_k3_Activity.this.startActivity(intent);
			break;

		default:
			break;
		}

	}

}
