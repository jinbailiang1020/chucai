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
import com.sm.sls_app.view.MyToast;

/**
 * 胜负彩 期次详情Activity
 * 
 * @author SLS002
 * 
 */
public class WinLottery_sfc_Activity extends Activity implements
		OnClickListener {

	private TextView tv_lotteryName;
	private TextView tv_qi; // 期号
	private TextView tv_time; // 开奖时间
	private TextView tv_red_num; // 开奖号码
	private TextView tv_saleCount; // 销量
	// 奖期名称
	private TextView tv_name1, tv_name2;
	
	private ImageView img_logo;

	private LinearLayout layout ;      //二等奖
	// 中奖注数
	private TextView tv_one_count, tv_two_count;
	// 每注奖金
	private TextView tv_one, tv_two;

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
		setContentView(R.layout.activity_winlottery_number_sfc);

		App.activityS.add(this);
		
		findView();

		init();

		setListener();

	}

	/**
	 * 初始化UI
	 */
	private void findView() {
		
		layout  =  (LinearLayout) this.findViewById(R.id.win_num_content_two);
		// 奖名
		tv_name1 = (TextView) this.findViewById(R.id.center_tv_level1);
		tv_name2 = (TextView) this.findViewById(R.id.center_tv_level2);

		// 中奖注数
		tv_one_count = (TextView) this.findViewById(R.id.win_one_count);
		tv_two_count = (TextView) this.findViewById(R.id.win_two_count);

		// 中奖金额
		tv_one = (TextView) this.findViewById(R.id.lottery_one);
		tv_two = (TextView) this.findViewById(R.id.lottery_two);

		tv_lotteryName = (TextView) this.findViewById(R.id.lottery_name);
		tv_time = (TextView) this.findViewById(R.id.lottery_time);
		tv_qi = (TextView) this.findViewById(R.id.lottery_qi);
		tv_saleCount = (TextView) this.findViewById(R.id.center_tv_payCount2);
		tv_red_num = (TextView) this.findViewById(R.id.lottery_num_red);

		btn_bet = (Button) this.findViewById(R.id.btn_to_bet);
		
		img_logo = (ImageView) this.findViewById(R.id.lottery_img);
	}

	/**
	 * 初始化属性
	 */
	private void init() {
		intent = getIntent();
		winLottery = (WinLottery) intent.getSerializableExtra("wLottery");
		if("75".equals(winLottery.getLotteryId() ) )
		{
			tv_lotteryName.setText("任选九");
			btn_bet.setText("去任选九投注");
			img_logo.setBackgroundResource(R.drawable.logo_rxj);
			layout.setVisibility(View.INVISIBLE);   //任选九开将没有设置二等奖
			
		}
		tv_qi.setText("第" + winLottery.getName() + "期");
		tv_time.setText("开奖时间:" + winLottery.getEndTime());
		tv_red_num.setText(winLottery.getRedNum() + "");
		tv_saleCount.setText(winLottery.getSales());
		listWinDetail = new ArrayList<WinDetail>();
		listWinDetail = winLottery.getListWinDetail();
		//胜负彩设奖两个  任选九奖项只有一个
		if (null != listWinDetail && listWinDetail.size() > 0) {
			tv_name1.setText(listWinDetail.get(0).getBonusName());
			tv_one.setText(listWinDetail.get(0).getBonusValue());
			tv_one_count.setText(listWinDetail.get(0).getWinningCount() + "");
           if(winLottery.getLotteryId().equals("74")){
			tv_name2.setText(listWinDetail.get(1).getBonusName());
			tv_two.setText(listWinDetail.get(1).getBonusValue());
			tv_two_count.setText(listWinDetail.get(1).getWinningCount() + "");
           }
		}
	
	}

	private void setListener() {
		btn_bet.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		Intent intent = null;
		switch (view.getId()) {
		case R.id.btn_to_bet:
			long currentTime = System.currentTimeMillis();			
			for (Lottery lottery : HallFragment.listLottery) {
				if(lottery.getLotteryID().equals(winLottery.getLotteryId())){
					if(lottery.getDistanceTime()-currentTime <= 0){
						MyToast.getToast(WinLottery_sfc_Activity.this, "该奖期已结束，请等下一期").show();
						return; 
					}
 				}
			}			
			if (winLottery.getLotteryId().equals("74")) {
				intent = new Intent(WinLottery_sfc_Activity.this,
						Buy_SFC_Activity.class);
			} else if (winLottery.getLotteryId().equals("75")) {
				intent = new Intent(WinLottery_sfc_Activity.this,
						Buy_RX9_Activit.class);
			}
			WinLottery_sfc_Activity.this.startActivity(intent);
			break;
		default:
			break;
		}
	}
}
