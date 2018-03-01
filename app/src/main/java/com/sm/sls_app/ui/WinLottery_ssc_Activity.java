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
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.view.MyToast;
/**
 * 时时彩 中奖信息详情Activity
 * 
 * @author SLS003
 * 
 */
public class WinLottery_ssc_Activity extends Activity implements
		OnClickListener {

	
	private TextView tv_qi; // 期号
	private TextView tv_time; // 开奖时间
	private TextView tv_red_num; // 开奖号码
	private TextView tv_payCount2 ; //销售额；
	// 奖期名称
	private TextView tv_name1, tv_name2, tv_name3, tv_name4,tv_name5,
					tv_name6,tv_name7,tv_name8,tv_name9,tv_name10,
					tv_name11,tv_name12,lottery_name;

	private ImageView lottery_img;

	private LinearLayout win_num_content_one, win_num_content_two,
			win_num_content_third, win_num_content_four, win_num_content_five;
	
	// 每注奖金
	private TextView tv_one, tv_two, tv_three, tv_four, tv_five,
					 tv_six, tv_eight, tv_nine, tv_ten, tv_seven,
					 tv_eleven, tv_twelve;
	

	private Intent intent;
	private WinLottery winLottery;
	private List<WinDetail> listWinDetail;
	private Button btn_bet;
	private String lotteryId = "28";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_winlottery_number_ssc);
		
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
		lottery_name = (TextView) this.findViewById(R.id.lottery_name);
		lottery_img = (ImageView) this.findViewById(R.id.lottery_img);
		tv_payCount2 = (TextView) this.findViewById(R.id.center_tv_payCount2);
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

		
		tv_time = (TextView) this.findViewById(R.id.lottery_time);
		tv_qi = (TextView) this.findViewById(R.id.lottery_qi);
		
		tv_red_num = (TextView) this.findViewById(R.id.lottery_num_red);
		btn_bet = (Button) this.findViewById(R.id.btn_to_bet);

		win_num_content_one = (LinearLayout) this
				.findViewById(R.id.win_num_content_one);
		win_num_content_two = (LinearLayout) this
				.findViewById(R.id.win_num_content_two);
		win_num_content_third = (LinearLayout) this
				.findViewById(R.id.win_num_content_third);
		win_num_content_four = (LinearLayout) this
				.findViewById(R.id.win_num_content_four);
		win_num_content_five = (LinearLayout) this
				.findViewById(R.id.win_num_content_five);
	}

	/** 初始化属性 */
	private void init() {
		intent = getIntent();
		winLottery = (WinLottery) intent.getSerializableExtra("wLottery");

		tv_qi.setText("第" + winLottery.getName() + "期");
		tv_time.setText("开奖时间:" + winLottery.getEndTime());
		tv_red_num.setText(winLottery.getRedNum() + "");
		listWinDetail = new ArrayList<WinDetail>();
		listWinDetail = winLottery.getListWinDetail();
		
		if (null != listWinDetail && listWinDetail.size() > 0) {
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
		Intent intent = this.getIntent();

		WinLottery wLottery = (WinLottery) intent
				.getSerializableExtra("wLottery");

		

	}

	private void setListener() {
		btn_bet.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_to_bet:
			for (Lottery lottery : HallFragment.listLottery) {
				
				if (lottery.getLotteryID().equals(lotteryId)) {
					AppTools.lottery = lottery;
				}
			}
			Intent intent = null;
			System.out.println("AppTools.lottery.getLotteryID()=="+AppTools.lottery.getLotteryID());
			if ("28".equals(AppTools.lottery.getLotteryID())) {
				intent = new Intent(WinLottery_ssc_Activity.this,
						Select_SSCActivity.class);
			} else{
				MyToast.getToast(WinLottery_ssc_Activity.this, "该奖期已结束，请等下一期").show();
				break;
			}
			WinLottery_ssc_Activity.this.startActivity(intent);
			break;

		default:
			break;
		}

	}

}
