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

/**
 * 大乐透 期次详情Activity
 * 
 * @author SLS003
 * 
 */
public class WinLottery_dlt_Activity extends Activity implements
		OnClickListener {

	private TextView tv_qi; // 期号
	private TextView tv_time; // 开奖时间
	private TextView tv_red_num; // 开奖号码
	private TextView tv_blue_num; // 开奖号码

	private TextView tv_saleCount; // 销量
	private TextView tv_lotteryMoney; // 奖池奖金

	/** 奖金名称 */
	private TextView tv_name1, tv_name2, tv_name3, tv_name4, tv_name5,
			tv_name6, tv_name7, tv_name8, tv_name9, tv_name10, tv_name11,
			tv_name12;

	// 中奖注数
	private TextView tv_one_count, tv_one_zhui_count, tv_two_count,
			tv_two_zhui_count, tv_three_count, tv_three_zhui_count,
			tv_four_count, tv_four_zhui_count, tv_five_count,
			tv_five_zhui_count, tv_six_count, tv_six_zhui_count;

	// 每注奖金
	private TextView tv_one, tv_one_zhui, tv_two, tv_two_zhui, tv_three,
			tv_three_zhui, tv_four, tv_four_zhui, tv_five, tv_five_zhui,
			tv_six, tv_six_zhui;

	private Intent intent;
	private WinLottery winLottery;
	private List<WinDetail> listWinDetail;

	private Button btn_bet;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_winlottery_number);

		App.activityS.add(this);
		
		findView();

		init();

		setListener();
	}

	/** 初始化UI */
	private void findView() 
	{

		tv_time = (TextView) this.findViewById(R.id.lottery_time);
		tv_qi = (TextView) this.findViewById(R.id.lottery_qi);
		tv_lotteryMoney = (TextView) this
				.findViewById(R.id.center_tv_lotteryMoney2);
		tv_saleCount = (TextView) this.findViewById(R.id.center_tv_payCount2);
		tv_red_num = (TextView) this.findViewById(R.id.lottery_num_red);
		tv_blue_num = (TextView) this.findViewById(R.id.lottery_num_blue);

		// 奖等级名称
		tv_name1 = (TextView) this.findViewById(R.id.center_tv_name1);
		tv_name2 = (TextView) this.findViewById(R.id.center_tv_name2);
		tv_name3 = (TextView) this.findViewById(R.id.center_tv_name3);
		tv_name4 = (TextView) this.findViewById(R.id.center_tv_name4);
		tv_name5 = (TextView) this.findViewById(R.id.center_tv_name5);
		tv_name6 = (TextView) this.findViewById(R.id.center_tv_name6);
		tv_name7 = (TextView) this.findViewById(R.id.center_tv_name7);
		tv_name8 = (TextView) this.findViewById(R.id.center_tv_name8);
		tv_name9 = (TextView) this.findViewById(R.id.center_tv_name9);
		tv_name10 = (TextView) this.findViewById(R.id.center_tv_name10);
		tv_name11 = (TextView) this.findViewById(R.id.center_tv_name11);
//		tv_name12 = (TextView) this.findViewById(R.id.center_tv_name12);
//		tv_name13 = (TextView) this.findViewById(R.id.center_tv_name13);
//		tv_name14 = (TextView) this.findViewById(R.id.center_tv_name14);
//		tv_name15 = (TextView) this.findViewById(R.id.center_tv_name15);

		// 中奖注数
		tv_one_count = (TextView) this.findViewById(R.id.win_one_count);
		tv_one_zhui_count = (TextView) this
				.findViewById(R.id.win_one_zhui_count);
		tv_two_count = (TextView) this.findViewById(R.id.win_two_count);
		tv_two_zhui_count = (TextView) this
				.findViewById(R.id.win_two_zhui_count);
		tv_three_count = (TextView) this.findViewById(R.id.win_three_count);
		tv_three_zhui_count = (TextView) this
				.findViewById(R.id.win_three_zhui_count);
		tv_four_count = (TextView) this.findViewById(R.id.win_four_count);
		tv_four_zhui_count = (TextView) this
				.findViewById(R.id.win_four_zhui_count);
		tv_five_count = (TextView) this.findViewById(R.id.win_five_count);
		tv_five_zhui_count = (TextView) this
				.findViewById(R.id.win_five_zhui_count);
		tv_six_count = (TextView) this.findViewById(R.id.win_six_count);
//		tv_six_zhui_count = (TextView) this
//				.findViewById(R.id.win_six_zhui_count);
//		tv_seven_count = (TextView) this.findViewById(R.id.win_seven_count);
//		tv_seven_zhui_count = (TextView) this
//				.findViewById(R.id.win_seven_zhui_count);
//		tv_eight_count = (TextView) this.findViewById(R.id.win_eight_count);

		// 中奖金额
		tv_one = (TextView) this.findViewById(R.id.lottery_one);
		tv_one_zhui = (TextView) this.findViewById(R.id.lottery_one_zhui);
		tv_two = (TextView) this.findViewById(R.id.lottery_two);
		tv_two_zhui = (TextView) this.findViewById(R.id.lottery_two_zhui);
		tv_three = (TextView) this.findViewById(R.id.lottery_three);
		tv_three_zhui = (TextView) this.findViewById(R.id.lottery_three_zhui);
		tv_four = (TextView) this.findViewById(R.id.lottery_four);
		tv_four_zhui = (TextView) this.findViewById(R.id.lottery_four_zhui);
		tv_five = (TextView) this.findViewById(R.id.lottery_five);
		tv_five_zhui = (TextView) this.findViewById(R.id.lottery_five_zhui);
		tv_six = (TextView) this.findViewById(R.id.lottery_six);
//		tv_six_zhui = (TextView) this.findViewById(R.id.lottery_six_zhui);
//		tv_seven = (TextView) this.findViewById(R.id.lottery_seven);
//		tv_seven_zhui = (TextView) this.findViewById(R.id.lottery_seven_zhui);
//		tv_eight = (TextView) this.findViewById(R.id.lottery_eight);

		btn_bet = (Button) this.findViewById(R.id.btn_to_bet);
	}

	/** 初始化属性 */
	private void init() 
	{
		// TODO Auto-generated method stub
		intent = getIntent();
		winLottery = (WinLottery) intent.getSerializableExtra("wLottery");

		tv_qi.setText("第" + winLottery.getName() + "期");
		tv_time.setText("开奖时间:" + winLottery.getEndTime());
		tv_red_num.setText(winLottery.getRedNum() + "");
		tv_blue_num.setText(winLottery.getBlueNum() + "");
		tv_saleCount.setText(winLottery.getSales());
		tv_lotteryMoney.setText(winLottery.getTotalMoney());
		listWinDetail = new ArrayList<WinDetail>();
		listWinDetail = winLottery.getListWinDetail();

		if (null != listWinDetail && listWinDetail.size() > 0)
		{

			tv_name1.setText(listWinDetail.get(0).getBonusName());
			tv_one.setText(listWinDetail.get(0).getBonusValue());
			tv_one_count.setText(listWinDetail.get(0).getWinningCount() + "");

			tv_name2.setText(listWinDetail.get(1).getBonusName());
			tv_one_zhui_count.setText(listWinDetail.get(1).getWinningCount()
					+ "");
			tv_one_zhui.setText(listWinDetail.get(1).getBonusValue());

			tv_name3.setText(listWinDetail.get(2).getBonusName());
			tv_two.setText(listWinDetail.get(2).getBonusValue());
			tv_two_count.setText(listWinDetail.get(2).getWinningCount() + "");

			tv_name4.setText(listWinDetail.get(3).getBonusName());
			tv_two_zhui_count.setText(listWinDetail.get(3).getWinningCount()
					+ "");
			tv_two_zhui.setText(listWinDetail.get(3).getBonusValue());

			tv_name5.setText(listWinDetail.get(4).getBonusName());
			tv_three.setText(listWinDetail.get(4).getBonusValue());
			tv_three_count.setText(listWinDetail.get(4).getWinningCount() + "");

			tv_name6.setText(listWinDetail.get(5).getBonusName());
			tv_three_zhui_count.setText(listWinDetail.get(5).getWinningCount()
					+ "");
			tv_three_zhui.setText(listWinDetail.get(5).getBonusValue());

			tv_name7.setText(listWinDetail.get(6).getBonusName());
			tv_four.setText(listWinDetail.get(6).getBonusValue());
			tv_four_count.setText(listWinDetail.get(6).getWinningCount() + "");

			tv_name8.setText(listWinDetail.get(7).getBonusName());
			tv_four_zhui_count.setText(listWinDetail.get(7).getWinningCount()
					+ "");
			tv_four_zhui.setText(listWinDetail.get(7).getBonusValue());

			tv_name9.setText(listWinDetail.get(8).getBonusName());
			tv_five.setText(listWinDetail.get(8).getBonusValue());
			tv_five_count.setText(listWinDetail.get(8).getWinningCount() + "");

			tv_name10.setText(listWinDetail.get(9).getBonusName());
			tv_five_zhui_count.setText(listWinDetail.get(9).getWinningCount()
					+ "");
			tv_five_zhui.setText(listWinDetail.get(9).getBonusValue());

			tv_name11.setText(listWinDetail.get(10).getBonusName());
			tv_six.setText(listWinDetail.get(10).getBonusValue());
			tv_six_count.setText(listWinDetail.get(10).getWinningCount() + "");

//			tv_name12.setText(listWinDetail.get(11).getBonusName());
//			tv_six_zhui_count.setText(listWinDetail.get(11).getWinningCount()
//					+ "");
//			tv_six_zhui.setText(listWinDetail.get(11).getBonusValue());

//			tv_name13.setText(listWinDetail.get(12).getBonusName());
//			tv_seven.setText(listWinDetail.get(12).getBonusValue());
//			tv_seven_count
//					.setText(listWinDetail.get(12).getWinningCount() + "");
//
//			tv_name14.setText(listWinDetail.get(13).getBonusName());
//			tv_seven_zhui_count.setText(listWinDetail.get(13).getWinningCount()
//					+ "");
//			tv_seven_zhui.setText(listWinDetail.get(13).getBonusValue());
//
//			tv_name15.setText(listWinDetail.get(14).getBonusName());
//			tv_eight.setText(listWinDetail.get(14).getBonusValue());
//			tv_eight_count
//					.setText(listWinDetail.get(14).getWinningCount() + "");
		}

	}

	private void setListener() 
	{
		btn_bet.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) 
	{
		switch (view.getId())
		{
		case R.id.btn_to_bet:
			long currentTime = System.currentTimeMillis();			
			for (Lottery lottery : HallFragment.listLottery) {
				if(lottery.getLotteryID().equals(winLottery.getLotteryId())){
					if(lottery.getDistanceTime()-currentTime <= 0){
						MyToast.getToast(WinLottery_dlt_Activity.this, "该奖期已结束，请等下一期").show();
						return; 
					}
 				}
			}			
			for (Lottery lottery : HallFragment.listLottery) 
			{
				if (lottery.getLotteryID().equals("39")) 
				{
					AppTools.lottery = lottery;
				}
			}
			Intent intent = new Intent(WinLottery_dlt_Activity.this,
					Buy_DLT_Activit.class);
			WinLottery_dlt_Activity.this.startActivity(intent);
			break;
		default:
			break;
		}

	}

}
