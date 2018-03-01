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
 * 期次详情Activity
 * 
 * @author SLS003
 * 
 */
public class WinLottery_11ydj_Activity extends Activity implements
		OnClickListener {

	private TextView tv_qi; // 期号
	private TextView tv_time; // 开奖时间
	private TextView tv_red_num; // 开奖号码

	private TextView tv_lotteryMoney; // 奖池奖金

	/** 奖金名称 */
	private TextView tv_name1, tv_name2, tv_name3, tv_name4, tv_name5,
			tv_name6, tv_name7, tv_name8, tv_name9, tv_name10, tv_name11,
			tv_name12;
	private TextView lottery_name ;
	// 每注奖金
	private TextView tv_one,  tv_two, tv_three,tv_four, tv_five, 
			         tv_six,  tv_seven,  tv_eight, tv_nine, tv_ten, tv_eleven, tv_twelve; 

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
		setContentView(R.layout.activity_winlottery_number_11ydj);

		App.activityS.add(this);
		
		findView();

		init();

		setListener();
	}

	/** 初始化UI */
	private void findView() 
	{
		lottery_name = (TextView) this.findViewById(R.id.lottery_name);
		tv_time = (TextView) this.findViewById(R.id.lottery_time);
		tv_qi = (TextView) this.findViewById(R.id.lottery_qi);
		tv_red_num = (TextView) this.findViewById(R.id.lottery_num_red);

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
		tv_name12 = (TextView) this.findViewById(R.id.center_tv_name12);

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

		btn_bet = (Button) this.findViewById(R.id.btn_to_bet);
	}

	/** 初始化属性 */
	private void init() 
	{
		// TODO Auto-generated method stub
		intent = getIntent();
		winLottery = (WinLottery) intent.getSerializableExtra("wLottery");
        if(winLottery.getLotteryId().equals("70")){
        	lottery_name.setText("11选5");
        	btn_bet.setText("去11选5投注");
        	}else if(winLottery.getLotteryId().equals("78")){
            	lottery_name.setText("广东11选5");
            	btn_bet.setText("去广东11选5投注");
            	}
		tv_qi.setText("第" + winLottery.getName() + "期");
		tv_time.setText("开奖时间:" + winLottery.getEndTime());
		tv_red_num.setText(winLottery.getRedNum() + "");
		listWinDetail = new ArrayList<WinDetail>();
		listWinDetail = winLottery.getListWinDetail();

		if (null != listWinDetail && listWinDetail.size() > 0)
		{

			tv_name1.setText(listWinDetail.get(0).getBonusName());
			tv_one.setText(listWinDetail.get(0).getBonusValue());

			tv_name2.setText(listWinDetail.get(1).getBonusName());
			tv_two.setText(listWinDetail.get(1).getBonusValue());

			tv_name3.setText(listWinDetail.get(2).getBonusName());
			tv_three.setText(listWinDetail.get(2).getBonusValue());

			tv_name4.setText(listWinDetail.get(3).getBonusName());
			tv_four.setText(listWinDetail.get(3).getBonusValue());

			tv_name5.setText(listWinDetail.get(4).getBonusName());
			tv_five.setText(listWinDetail.get(4).getBonusValue());

			tv_name6.setText(listWinDetail.get(5).getBonusName());
			tv_six.setText(listWinDetail.get(5).getBonusValue());

			tv_name7.setText(listWinDetail.get(6).getBonusName());
			tv_seven.setText(listWinDetail.get(6).getBonusValue());

			tv_name8.setText(listWinDetail.get(7).getBonusName());
			tv_eight.setText(listWinDetail.get(7).getBonusValue());
			
			tv_name9.setText(listWinDetail.get(8).getBonusName());
			tv_nine.setText(listWinDetail.get(8).getBonusValue());

			tv_name10.setText(listWinDetail.get(9).getBonusName());
			tv_ten.setText(listWinDetail.get(9).getBonusValue());

			tv_name11.setText(listWinDetail.get(10).getBonusName());
			tv_eleven.setText(listWinDetail.get(10).getBonusValue());

			tv_name12.setText(listWinDetail.get(11).getBonusName());
			tv_twelve.setText(listWinDetail.get(11).getBonusValue());
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
						MyToast.getToast(WinLottery_11ydj_Activity.this, "该奖期已结束，请等下一期").show();
						return; 
					}
 				}
			}			
			for (Lottery lottery : HallFragment.listLottery) 
			{
				System.out.println("-+-++"+lottery.getLotteryID());
				if (winLottery.getLotteryId().equals("62") && lottery.getLotteryID().equals("62")) 						
				{
					AppTools.lottery = lottery;
				}
				else if(winLottery.getLotteryId().equals("70") && lottery.getLotteryID().equals("70")){
					AppTools.lottery = lottery;
				}
				else if(winLottery.getLotteryId().equals("78") && lottery.getLotteryID().equals("78")){
					AppTools.lottery = lottery;
				}
			}
			Intent intent = new Intent(WinLottery_11ydj_Activity.this,
					Select_11X5Activity.class);
			WinLottery_11ydj_Activity.this.startActivity(intent);
			break;
		default:
			break;
		}

	}

}
