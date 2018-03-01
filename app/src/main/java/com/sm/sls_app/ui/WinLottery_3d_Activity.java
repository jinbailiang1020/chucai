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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.Lottery;
import com.sm.sls_app.dataaccess.WinDetail;
import com.sm.sls_app.dataaccess.WinLottery;
import com.sm.sls_app.fragment.HallFragment;
import com.sm.sls_app.ui.WinLottery_ssq_Activity;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.view.MyToast;
/**
 * 大乐透 期次详情Activity
 * 
 * @author SLS003
 * 
 */
public class WinLottery_3d_Activity extends Activity implements
		OnClickListener {

	private TextView tv_qi; // 期号
	private TextView tv_time; // 开奖时间
	private TextView tv_red_num; // 开奖号码
	private TextView tv_lotteryMoney; // 奖池奖金
	// 奖期名称
	private TextView tv_name1, tv_name2, tv_name3, lottery_name,tv_name4, tv_name5, tv_name6,
					 tv_name7, tv_name8, tv_name9,tv_name10, tv_name11, tv_name12,
					 tv_name13, tv_name14, tv_name15,tv_name16, tv_name17, tv_name18
					 ,tv_name19, tv_name20, tv_name21,tv_name22, tv_name23, tv_name24,
					 tv_name25, tv_name26, tv_name27,tv_name28, tv_name29, tv_name30,
					 tv_name31, tv_name32, tv_name33,tv_name34;

	private ImageView lottery_img;

	private LinearLayout win_num_content_one, win_num_content_two,
			win_num_content_third,win_num_center,win_num_center2;
	private TextView center_tv_payCount2 , center_tv_lotteryMoney2;
	// 中奖注数
	private TextView tv_one_count, tv_two_count, tv_three_count, tv_four_count, tv_five_count, tv_six_count,
					 tv_seven_count, tv_eight_count, tv_nine_count, tv_ten_count, tv_eleven_count, tv_twelve_count,
					 tv_thirteen_count, tv_fourteen_count, tv_fifteen_count, tv_sixteen_count, tv_seventeen_count, tv_eighteen_count,
					 tv_nineteen_count, tv_twenty_count, tv_twentyone_count,tv_22_count, tv_23_count,
					 tv_24_count, tv_25_count, tv_26_count, tv_27_count,tv_28_count,
					 tv_29_count, tv_30_count, tv_31_count, tv_32_count, tv_33_count,tv_34_count;
	// 每注奖金
	private TextView tv_one, tv_two, tv_three,tv_four,tv_five,tv_six,
					 tv_seven, tv_eight, tv_nine,tv_ten,tv_eleven,tv_twelve,
					 tv_thirteen, tv_fourteen, tv_fifteen,tv_sixteen,tv_seventeen,tv_eighteen,
					 tv_nineteen, tv_twenty, tv_twentyone, tv_22, tv_23,
					 tv_24,tv_25,tv_26,tv_27, tv_28, 
					 tv_29, tv_30,tv_31,tv_32,tv_33,tv_34;

	private Intent intent;
	private WinLottery winLottery;
	private List<WinDetail> listWinDetail;
	private Button btn_bet;
	private String lotteryId = "3";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_winlottery_number_3d);
		
		App.activityS.add(this);
		
		findView();
		
		init();
		
		setListener();

	}

	/** 初始化UI */
	private void findView() {
		// 奖名
		tv_name1 = (TextView) this.findViewById(R.id.center_tv_level1);
		tv_name2 = (TextView) this.findViewById(R.id.center_tv_level2);
		tv_name3 = (TextView) this.findViewById(R.id.center_tv_level3);
		tv_name4 = (TextView) this.findViewById(R.id.center_tv_level4);
		tv_name5 = (TextView) this.findViewById(R.id.center_tv_level5);
		tv_name6 = (TextView) this.findViewById(R.id.center_tv_level6);
		tv_name7 = (TextView) this.findViewById(R.id.center_tv_level7);
		tv_name8 = (TextView) this.findViewById(R.id.center_tv_level8);
		tv_name9 = (TextView) this.findViewById(R.id.center_tv_level9);
		tv_name10 = (TextView) this.findViewById(R.id.center_tv_level10);
		tv_name11 = (TextView) this.findViewById(R.id.center_tv_level11);
		tv_name12 = (TextView) this.findViewById(R.id.center_tv_level12);
		tv_name13 = (TextView) this.findViewById(R.id.center_tv_level13);
		tv_name14 = (TextView) this.findViewById(R.id.center_tv_level14);
		tv_name15 = (TextView) this.findViewById(R.id.center_tv_level15);
		tv_name16 = (TextView) this.findViewById(R.id.center_tv_level16);
		tv_name17 = (TextView) this.findViewById(R.id.center_tv_level17);
		tv_name18 = (TextView) this.findViewById(R.id.center_tv_level18);
		tv_name19 = (TextView) this.findViewById(R.id.center_tv_level19);
		tv_name20 = (TextView) this.findViewById(R.id.center_tv_level20);
		tv_name21 = (TextView) this.findViewById(R.id.center_tv_level21);
		tv_name22 = (TextView) this.findViewById(R.id.center_tv_level22);
		tv_name23 = (TextView) this.findViewById(R.id.center_tv_level23);
		tv_name24 = (TextView) this.findViewById(R.id.center_tv_level24);
		tv_name25 = (TextView) this.findViewById(R.id.center_tv_level25);
		tv_name26 = (TextView) this.findViewById(R.id.center_tv_level26);
		tv_name27 = (TextView) this.findViewById(R.id.center_tv_level27);
		tv_name28 = (TextView) this.findViewById(R.id.center_tv_level28);
		tv_name29 = (TextView) this.findViewById(R.id.center_tv_level29);
		tv_name30 = (TextView) this.findViewById(R.id.center_tv_level30);
		tv_name31 = (TextView) this.findViewById(R.id.center_tv_level31);
		tv_name32 = (TextView) this.findViewById(R.id.center_tv_level32);
		tv_name33 = (TextView) this.findViewById(R.id.center_tv_level33);
		tv_name34 = (TextView) this.findViewById(R.id.center_tv_level34);
		lottery_name = (TextView) this.findViewById(R.id.lottery_name);
		lottery_img = (ImageView) this.findViewById(R.id.lottery_img);
		// 中奖注数
		tv_one_count = (TextView) this.findViewById(R.id.win_one_count);
		tv_two_count = (TextView) this.findViewById(R.id.win_two_count);
		tv_three_count = (TextView) this.findViewById(R.id.win_three_count);
		tv_four_count =(TextView) this.findViewById(R.id.win_four_count);
		tv_five_count =(TextView) this.findViewById(R.id.win_five_count);
		tv_six_count =(TextView) this.findViewById(R.id.win_six_count);
		tv_seven_count = (TextView) this.findViewById(R.id.win_seven_count);
		tv_eight_count = (TextView) this.findViewById(R.id.win_eight_count);
		tv_nine_count = (TextView) this.findViewById(R.id.win_nine_count);
		tv_ten_count = (TextView) this.findViewById(R.id.win_ten_count);
		tv_eleven_count =(TextView) this.findViewById(R.id.win_eleven_count);
		tv_twelve_count =(TextView) this.findViewById(R.id.win_twelve_count);
		tv_thirteen_count =(TextView) this.findViewById(R.id.win_thirteen_count);
		tv_fourteen_count = (TextView) this.findViewById(R.id.win_fourteen_count);
		tv_fifteen_count = (TextView) this.findViewById(R.id.win_fifteen_count);
		tv_sixteen_count = (TextView) this.findViewById(R.id.win_sixteen_count);
		tv_seventeen_count =(TextView) this.findViewById(R.id.win_seventeen_count);
		tv_eighteen_count =(TextView) this.findViewById(R.id.win_eighteen_count);
		tv_nineteen_count =(TextView) this.findViewById(R.id.win_nineteen_count);
		tv_twenty_count = (TextView) this.findViewById(R.id.win_twenty_count);
		tv_twentyone_count = (TextView) this.findViewById(R.id.win_twentyone_count);
		tv_22_count = (TextView) this.findViewById(R.id.win_22_count);
		tv_23_count = (TextView) this.findViewById(R.id.win_23_count);
		tv_24_count = (TextView) this.findViewById(R.id.win_24_count);
		tv_25_count = (TextView) this.findViewById(R.id.win_25_count);
		tv_26_count = (TextView) this.findViewById(R.id.win_26_count);
		tv_27_count = (TextView) this.findViewById(R.id.win_27_count);
		tv_28_count = (TextView) this.findViewById(R.id.win_28_count);
		tv_29_count = (TextView) this.findViewById(R.id.win_29_count);
		tv_30_count = (TextView) this.findViewById(R.id.win_30_count);
		tv_31_count = (TextView) this.findViewById(R.id.win_31_count);
		tv_32_count = (TextView) this.findViewById(R.id.win_32_count);
		tv_33_count = (TextView) this.findViewById(R.id.win_33_count);
		tv_34_count = (TextView) this.findViewById(R.id.win_34_count);
		// 中奖金额
		tv_one = (TextView) this.findViewById(R.id.lottery_one);
		tv_two = (TextView) this.findViewById(R.id.lottery_two);
		tv_three = (TextView) this.findViewById(R.id.lottery_three);
		tv_four = (TextView) this.findViewById(R.id.lottery_four);
		tv_five = (TextView) this.findViewById(R.id.lottery_five);
		tv_six = (TextView) this.findViewById(R.id.lottery_six);
		tv_seven = (TextView) this.findViewById(R.id.lottery_seven);
		tv_eight = (TextView) this.findViewById(R.id.lottery_eight);
		tv_nine = (TextView) this.findViewById(R.id.lottery_nine);
		tv_ten = (TextView) this.findViewById(R.id.lottery_ten);
		tv_eleven = (TextView) this.findViewById(R.id.lottery_eleven);
		tv_twelve = (TextView) this.findViewById(R.id.lottery_twelve);
		tv_thirteen = (TextView) this.findViewById(R.id.lottery_thirteen);
		tv_fourteen = (TextView) this.findViewById(R.id.lottery_fourteen);
		tv_fifteen = (TextView) this.findViewById(R.id.lottery_fifteen);
		tv_sixteen = (TextView) this.findViewById(R.id.lottery_sixteen);
		tv_seventeen = (TextView) this.findViewById(R.id.lottery_seventeen);
		tv_eighteen = (TextView) this.findViewById(R.id.lottery_eighteen);
		tv_nineteen = (TextView) this.findViewById(R.id.lottery_nineteen);
		tv_twenty = (TextView) this.findViewById(R.id.lottery_twenty);
		tv_twentyone = (TextView) this.findViewById(R.id.lottery_twentyone);
		tv_22 = (TextView) this.findViewById(R.id.lottery_22);
		tv_23 = (TextView) this.findViewById(R.id.lottery_23);
		tv_24 = (TextView) this.findViewById(R.id.lottery_24);
		tv_25 = (TextView) this.findViewById(R.id.lottery_25);
		tv_26 = (TextView) this.findViewById(R.id.lottery_26);
		tv_27 = (TextView) this.findViewById(R.id.lottery_27);
		tv_28 = (TextView) this.findViewById(R.id.lottery_28);
		tv_29 = (TextView) this.findViewById(R.id.lottery_29);
		tv_30 = (TextView) this.findViewById(R.id.lottery_30);
		tv_31 = (TextView) this.findViewById(R.id.lottery_31);
		tv_32 = (TextView) this.findViewById(R.id.lottery_32);
		tv_33 = (TextView) this.findViewById(R.id.lottery_33);
		tv_34 = (TextView) this.findViewById(R.id.lottery_34);
		
		tv_time = (TextView) this.findViewById(R.id.lottery_time);
		tv_qi = (TextView) this.findViewById(R.id.lottery_qi);
		tv_lotteryMoney = (TextView) this
				.findViewById(R.id.center_tv_lotteryMoney2);
		tv_red_num = (TextView) this.findViewById(R.id.lottery_num_red);
		
		center_tv_payCount2 = (TextView) this.findViewById(R.id.center_tv_payCount2);
		center_tv_lotteryMoney2 = (TextView) this.findViewById(R.id.center_tv_lotteryMoney2);
		
		btn_bet = (Button) this.findViewById(R.id.btn_to_bet);

		win_num_content_one = (LinearLayout) this
				.findViewById(R.id.win_num_content_one);
		win_num_content_two = (LinearLayout) this
				.findViewById(R.id.win_num_content_two);
		win_num_content_third = (LinearLayout) this
				.findViewById(R.id.win_num_content_third);
		win_num_center =(LinearLayout) this
				.findViewById(R.id.win_num_center);
		win_num_center2 =(LinearLayout) this
				.findViewById(R.id.win_num_center2);
	}

	/** 初始化属性 */
	private void init() {
		intent = getIntent();
		winLottery = (WinLottery) intent.getSerializableExtra("wLottery");

		tv_qi.setText("第" + winLottery.getName() + "期");
		tv_time.setText("开奖时间:" + winLottery.getEndTime());
		tv_red_num.setText(winLottery.getRedNum() + "");
		center_tv_payCount2.setText(winLottery.getSales());
		listWinDetail = new ArrayList<WinDetail>();
		listWinDetail = winLottery.getListWinDetail();
		
		if (null != listWinDetail && listWinDetail.size() > 0) {
			tv_name1.setText(listWinDetail.get(0).getBonusName());
			tv_one.setText(listWinDetail.get(0).getBonusValue());
			tv_one_count.setText(listWinDetail.get(0).getWinningCount() + "");

			if(winLottery.getLotteryId().equals("6")){

				tv_name2.setText(listWinDetail.get(1).getBonusName());
				tv_two.setText(listWinDetail.get(1).getBonusValue());
				tv_two_count.setText(listWinDetail.get(1).getWinningCount() + "");

				tv_name3.setText(listWinDetail.get(2).getBonusName());
				tv_three.setText(listWinDetail.get(2).getBonusValue());
				tv_three_count.setText(listWinDetail.get(2).getWinningCount() + "");
				tv_name4.setText(listWinDetail.get(3).getBonusName());
				tv_four.setText(listWinDetail.get(3).getBonusValue());
				tv_four_count.setText(listWinDetail.get(3).getWinningCount() + "");
				tv_name5.setText(listWinDetail.get(4).getBonusName());
				tv_five.setText(listWinDetail.get(4).getBonusValue());
				tv_five_count.setText(listWinDetail.get(4).getWinningCount() + "");
				tv_name6.setText(listWinDetail.get(5).getBonusName());
				tv_six.setText(listWinDetail.get(5).getBonusValue());
				tv_six_count.setText(listWinDetail.get(5).getWinningCount() + "");
				tv_name7.setText(listWinDetail.get(6).getBonusName());
				tv_seven.setText(listWinDetail.get(6).getBonusValue());
				tv_seven_count.setText(listWinDetail.get(6).getWinningCount() + "");
				tv_name8.setText(listWinDetail.get(7).getBonusName());
				tv_eight.setText(listWinDetail.get(7).getBonusValue());
				tv_eight_count.setText(listWinDetail.get(7).getWinningCount() + "");
				tv_name9.setText(listWinDetail.get(8).getBonusName());
				tv_nine.setText(listWinDetail.get(8).getBonusValue());
				tv_nine_count.setText(listWinDetail.get(8).getWinningCount() + "");
				tv_name10.setText(listWinDetail.get(9).getBonusName());
				tv_ten.setText(listWinDetail.get(9).getBonusValue());
				tv_ten_count.setText(listWinDetail.get(9).getWinningCount() + "");
				tv_name11.setText(listWinDetail.get(10).getBonusName());
				tv_eleven.setText(listWinDetail.get(10).getBonusValue());
				tv_eleven_count.setText(listWinDetail.get(10).getWinningCount() + "");
				tv_name12.setText(listWinDetail.get(11).getBonusName());
				tv_twelve.setText(listWinDetail.get(11).getBonusValue());
				tv_twelve_count.setText(listWinDetail.get(11).getWinningCount() + "");
				tv_name13.setText(listWinDetail.get(12).getBonusName());
				tv_thirteen.setText(listWinDetail.get(12).getBonusValue());
				tv_thirteen_count.setText(listWinDetail.get(12).getWinningCount() + "");
				tv_name14.setText(listWinDetail.get(13).getBonusName());
				tv_fourteen.setText(listWinDetail.get(13).getBonusValue());
				tv_fourteen_count.setText(listWinDetail.get(13).getWinningCount() + "");
				tv_name15.setText(listWinDetail.get(14).getBonusName());
				tv_fifteen.setText(listWinDetail.get(14).getBonusValue());
				tv_fifteen_count.setText(listWinDetail.get(14).getWinningCount() + "");
				tv_name16.setText(listWinDetail.get(15).getBonusName());
				tv_sixteen.setText(listWinDetail.get(15).getBonusValue());
				tv_sixteen_count.setText(listWinDetail.get(15).getWinningCount() + "");
				tv_name17.setText(listWinDetail.get(16).getBonusName());
				tv_seventeen.setText(listWinDetail.get(16).getBonusValue());
				tv_seventeen_count.setText(listWinDetail.get(16).getWinningCount() + "");
				tv_name18.setText(listWinDetail.get(17).getBonusName());
				tv_eighteen.setText(listWinDetail.get(17).getBonusValue());
				tv_eighteen_count.setText(listWinDetail.get(17).getWinningCount() + "");
				tv_name19.setText(listWinDetail.get(18).getBonusName());
				tv_nineteen.setText(listWinDetail.get(18).getBonusValue());
				tv_nineteen_count.setText(listWinDetail.get(18).getWinningCount() + "");
				tv_name20.setText(listWinDetail.get(19).getBonusName());
				tv_twenty.setText(listWinDetail.get(19).getBonusValue());
				tv_twenty_count.setText(listWinDetail.get(19).getWinningCount() + "");
				tv_name21.setText(listWinDetail.get(20).getBonusName());
				tv_twentyone.setText(listWinDetail.get(20).getBonusValue());
				tv_twentyone_count.setText(listWinDetail.get(20).getWinningCount() + "");
				tv_name22.setText(listWinDetail.get(21).getBonusName());
				tv_22.setText(listWinDetail.get(21).getBonusValue());
				tv_22_count.setText(listWinDetail.get(21).getWinningCount() + "");
				tv_name23.setText(listWinDetail.get(22).getBonusName());
				tv_23.setText(listWinDetail.get(22).getBonusValue());
				tv_23_count.setText(listWinDetail.get(22).getWinningCount() + "");
				tv_name24.setText(listWinDetail.get(23).getBonusName());
				tv_24.setText(listWinDetail.get(23).getBonusValue());
				tv_24_count.setText(listWinDetail.get(23).getWinningCount() + "");
				tv_name25.setText(listWinDetail.get(24).getBonusName());
				tv_25.setText(listWinDetail.get(24).getBonusValue());
				tv_25_count.setText(listWinDetail.get(24).getWinningCount() + "");
				tv_name26.setText(listWinDetail.get(25).getBonusName());
				tv_26.setText(listWinDetail.get(25).getBonusValue());
				tv_26_count.setText(listWinDetail.get(25).getWinningCount() + "");
				tv_name27.setText(listWinDetail.get(26).getBonusName());
				tv_27.setText(listWinDetail.get(26).getBonusValue());
				tv_27_count.setText(listWinDetail.get(26).getWinningCount() + "");
				tv_name28.setText(listWinDetail.get(27).getBonusName());
				tv_28.setText(listWinDetail.get(27).getBonusValue());
				tv_28_count.setText(listWinDetail.get(27).getWinningCount() + "");
				tv_name29.setText(listWinDetail.get(28).getBonusName());
				tv_29.setText(listWinDetail.get(28).getBonusValue());
				tv_29_count.setText(listWinDetail.get(28).getWinningCount() + "");
				tv_name30.setText(listWinDetail.get(29).getBonusName());
				tv_30.setText(listWinDetail.get(29).getBonusValue());
				tv_30_count.setText(listWinDetail.get(29).getWinningCount() + "");
				tv_name31.setText(listWinDetail.get(30).getBonusName());
				tv_31.setText(listWinDetail.get(30).getBonusValue());
				tv_31_count.setText(listWinDetail.get(30).getWinningCount() + "");
				tv_name32.setText(listWinDetail.get(31).getBonusName());
				tv_32.setText(listWinDetail.get(31).getBonusValue());
				tv_32_count.setText(listWinDetail.get(31).getWinningCount() + "");
				tv_name33.setText(listWinDetail.get(32).getBonusName());
				tv_33.setText(listWinDetail.get(32).getBonusValue());
				tv_33_count.setText(listWinDetail.get(32).getWinningCount() + "");
				tv_name34.setText(listWinDetail.get(33).getBonusName());
				tv_34.setText(listWinDetail.get(33).getBonusValue());
				tv_34_count.setText(listWinDetail.get(33).getWinningCount() + "");
			}
			
		}else{
			tv_name1.setText("直选");
			tv_name2.setText("组选三");
			tv_name3.setText("组选六");
			tv_name4.setText("1D");
			tv_name5.setText("猜1D-中1");
			tv_name6.setText("猜1D-中2");
			tv_name7.setText("猜1D-中3");
			tv_name8.setText("2D");
			tv_name9.setText("猜2D-同号");
			tv_name10.setText("猜2D-不同号");
			tv_name11.setText("通选1");
			tv_name12.setText("通选2");
			tv_name13.setText("和数0或27");
			tv_name14.setText("和数1或26");
			tv_name15.setText("和数2或25");
			tv_name16.setText("和数3或24");
			tv_name17.setText("和数4或23");
			tv_name18.setText("和数5或22");
			tv_name19.setText("和数6或21");
			tv_name20.setText("和数7或20");
			tv_name21.setText("和数8或19");
			tv_name22.setText("和数9或18");
			tv_name23.setText("和数10或17");
			tv_name24.setText("和数11或16");
			tv_name25.setText("和数12或15");
			tv_name26.setText("和数13或14");
			tv_name27.setText("包选三全中");
			tv_name28.setText("包选三组中");
			tv_name29.setText("包选六全中");
			tv_name30.setText("包选六组中");
			tv_name31.setText("猜大小");
			tv_name32.setText("猜三同");
			tv_name33.setText("拖拉机");
			tv_name34.setText("猜奇偶");
		}
		Intent intent = this.getIntent();

		WinLottery wLottery = (WinLottery) intent
				.getSerializableExtra("wLottery");

		if (wLottery != null) {
			lotteryId = wLottery.getLotteryId();
			lottery_name.setText("3D");
			lottery_img.setBackgroundResource(R.drawable.logo_sd);
			btn_bet.setText("  去3D投注  ");
			win_num_content_two.setVisibility(View.VISIBLE);
			win_num_content_third.setVisibility(View.VISIBLE);
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
						MyToast.getToast(WinLottery_3d_Activity.this, "该奖期已结束，请等下一期").show();
						return; 
					}
 				}
			}			
			for (Lottery lottery : HallFragment.listLottery) {
				if (lottery.getLotteryID().equals(lotteryId)) {
					AppTools.lottery = lottery;
				}
			}
			Intent intent = null;
			intent = new Intent(WinLottery_3d_Activity.this,
					SelectNumberActivityFC3D.class);
			WinLottery_3d_Activity.this.startActivity(intent);
			break;

		default:
			break;
		}

	}

}
