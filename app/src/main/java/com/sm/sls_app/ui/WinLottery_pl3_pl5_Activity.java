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
public class WinLottery_pl3_pl5_Activity extends Activity implements
		OnClickListener {

	private TextView tv_qi; // 期号
	private TextView tv_time; // 开奖时间
	private TextView tv_red_num; // 开奖号码
	private TextView tv_lotteryMoney; // 奖池奖金
	// 奖期名称
	private TextView tv_name1, tv_name2, tv_name3, lottery_name,tv_name4, tv_name5, tv_name6;

	private ImageView lottery_img;

	private LinearLayout win_num_content_one, win_num_content_two,
			win_num_content_third,win_num_center,win_num_center2;
	private TextView center_tv_payCount2 , center_tv_lotteryMoney2;
	// 中奖注数
	private TextView tv_one_count, tv_two_count, tv_three_count, tv_four_count, tv_five_count, tv_six_count;
	// 每注奖金
	private TextView tv_one, tv_two, tv_three,tv_four,tv_five,tv_six;

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
		setContentView(R.layout.activity_winlottery_number_pl3_pl5);
		
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
		lottery_name = (TextView) this.findViewById(R.id.lottery_name);
		lottery_img = (ImageView) this.findViewById(R.id.lottery_img);
		// 中奖注数
		tv_one_count = (TextView) this.findViewById(R.id.win_one_count);
		tv_two_count = (TextView) this.findViewById(R.id.win_two_count);
		tv_three_count = (TextView) this.findViewById(R.id.win_three_count);
		tv_four_count =(TextView) this.findViewById(R.id.win_four_count);
		tv_five_count =(TextView) this.findViewById(R.id.win_five_count);
		tv_six_count =(TextView) this.findViewById(R.id.win_six_count);
		// 中奖金额
		tv_one = (TextView) this.findViewById(R.id.lottery_one);
		tv_two = (TextView) this.findViewById(R.id.lottery_two);
		tv_three = (TextView) this.findViewById(R.id.lottery_three);
		tv_four = (TextView) this.findViewById(R.id.lottery_four);
		tv_five = (TextView) this.findViewById(R.id.lottery_five);
		tv_six = (TextView) this.findViewById(R.id.lottery_six);
		
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

			if(winLottery.getLotteryId().equals("6") || winLottery.getLotteryId().equals("63")){

				tv_name2.setText(listWinDetail.get(1).getBonusName());
				tv_two.setText(listWinDetail.get(1).getBonusValue());
				tv_two_count.setText(listWinDetail.get(1).getWinningCount() + "");

				tv_name3.setText(listWinDetail.get(2).getBonusName());
				tv_three.setText(listWinDetail.get(2).getBonusValue());
				tv_three_count.setText(listWinDetail.get(2).getWinningCount() + "");
			}
			
			else if(winLottery.getLotteryId().equals("69")){

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
			}
		}
		Intent intent = this.getIntent();

		WinLottery wLottery = (WinLottery) intent
				.getSerializableExtra("wLottery");

		if (wLottery != null) {
			lotteryId = wLottery.getLotteryId();
			 if ("63".equals(lotteryId)) {
				lottery_name.setText("排列三");
				lottery_img.setBackgroundResource(R.drawable.logo_pls);
				btn_bet.setText("  去排列三投注  ");
				win_num_content_two.setVisibility(View.VISIBLE);
				win_num_content_third.setVisibility(View.VISIBLE);
			} else if ("64".equals(lotteryId)) {
				lottery_name.setText("排列五");
				lottery_img.setBackgroundResource(R.drawable.logo_plw);
				btn_bet.setText("  去排列五投注  ");
				win_num_content_two.setVisibility(View.GONE);
				win_num_content_third.setVisibility(View.GONE);
				}
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
						MyToast.getToast(WinLottery_pl3_pl5_Activity.this, "该奖期已结束，请等下一期").show();
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
			if ("64".equals(AppTools.lottery.getLotteryID())) {
				intent = new Intent(WinLottery_pl3_pl5_Activity.this,
						SelectNumberActivityPL5_QXC.class);
			} else{
				intent = new Intent(WinLottery_pl3_pl5_Activity.this,
						SelectNumberActivityPL3.class);
			}
			WinLottery_pl3_pl5_Activity.this.startActivity(intent);
			break;

		default:
			break;
		}

	}

}
