package com.sm.sls_app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.Schemes;
import com.sm.sls_app.ui.adapter.OrderIndoAdapter_SSQ;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.view.MyListView2;

/**
 * 彩票订单详情
 * 
 * @author SLS003
 */
public class MyLotteryInfoActivity_SSQ extends Activity {

	private TextView tv_qi;
	private TextView tv_money; // 方案金额
	private TextView tv_money2; // 方案金额
	private TextView tv_type;
	private TextView tv_time;
	private TextView tv_orderNumber;
	private TextView tv_winMoney; // 中奖金额
	private TextView tv_winMoney2; // 中奖金额
	private TextView tv_name; // 发起人
	private TextView tv_yong;
	private TextView tv_shareMoney; // 每份金额
	private TextView tv_win_redNum; // 开奖号码 红色
	private TextView tv_win_blueNum; // 开奖号码 蓝色
	private TextView tv_playName,tv_myMoney,tv_myShare;

	private TextView tv_bet_redNum; // 开奖号码 红色
	private TextView tv_bet_blueNum; // 开奖号码 蓝色

	private Button btn_buy;
	private Button btn_go;

	private TextView tv_title, tv_content;

	private LinearLayout ll_center;
	private LinearLayout ll_center2;

	private LinearLayout ll_title;
	private LinearLayout ll_content;

	private ImageView img_logo;
	private ScrollView sv;

	private MyListView2 listView;
	private OrderIndoAdapter_SSQ adapter;
	private String[] strNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.orderinfo_ssq);

		findView();

		init();

		setListener();
	}

	/** myScrollView.smoothScrollTo(0,20); 初始化UI */
	private void findView() 
	{
		sv = (ScrollView) this.findViewById(R.id.orderinfo_sv_info);
		listView = (MyListView2) this.findViewById(R.id.orderinfo_listView);

		img_logo = (ImageView) this.findViewById(R.id.orderIndo_top_img);

		tv_qi = (TextView) this.findViewById(R.id.orderinfo_top_tv_qi);
		tv_money = (TextView) this.findViewById(R.id.orderinfo_top_tv_money);
		tv_money2 = (TextView) this.findViewById(R.id.center_tv_schemeMoney);

		tv_type = (TextView) this.findViewById(R.id.orderinfo_center_tv_type);

		tv_winMoney = (TextView) this.findViewById(R.id.orderinfo_center_tv_winning);
		tv_winMoney2 = (TextView) this.findViewById(R.id.tv_win_money2);

		tv_time = (TextView) this.findViewById(R.id.right_bottom_tv_time);
		tv_orderNumber = (TextView) this.findViewById(R.id.tv_orderNumber);

		tv_name = (TextView) this.findViewById(R.id.center_tv_userName);
		tv_yong = (TextView) this.findViewById(R.id.center_tv_yong);

		tv_shareMoney = (TextView) this.findViewById(R.id.center_tv_shareMoney);

		tv_win_redNum = (TextView) this.findViewById(R.id.orderinfo_win_redNum);
		tv_win_blueNum = (TextView) this
				.findViewById(R.id.orderinfo_win_blueNum);

		tv_playName = (TextView) this.findViewById(R.id.orderinfo_center_tv_typeName);
		

		tv_title = (TextView) this.findViewById(R.id.tv_schemeTitle2);
		tv_content = (TextView) this.findViewById(R.id.tv_schemeDetail2);
		tv_myMoney = (TextView) this.findViewById(R.id.tv_my_money);
		tv_myShare = (TextView) this.findViewById(R.id.tv_my_share);

		ll_center = (LinearLayout) this.findViewById(R.id.win_num_center);
		ll_center2 = (LinearLayout) this.findViewById(R.id.orderinfo_info);

		ll_title = (LinearLayout) this.findViewById(R.id.orderinfo_schemeTitle);
		ll_content = (LinearLayout) this
				.findViewById(R.id.orderinfo_schemeDetail);
	}

	/** 初始化 */
	private void init() 
	{
		Schemes scheme = (Schemes) getIntent().getSerializableExtra("schemes");
		tv_qi.setText("第" + scheme.getIsuseName() + "期");
		tv_money.setText(scheme.getMoney() + "元");
		tv_playName.setText(""+scheme.getPlayTypeName());

		img_logo.setBackgroundResource(AppTools.allLotteryLogo.get(scheme.getLotteryID()));

		if ("False".equals(scheme.getIsPurchasing())) 
		{
			tv_type.setText("合买");
			tv_name.setText(scheme.getInitiateName());

			System.out.println("发起人" + scheme.getInitiateName());

			tv_yong.setText( (int)(scheme.getSchemeBonusScale()*100) + "%");
			tv_money2.setText(scheme.getMoney() + "元");
			tv_shareMoney.setText(scheme.getShareMoney() + "元");
			tv_title.setText(scheme.getTitle());
			tv_content.setText(scheme.getDescription());
			tv_myShare.setText("认购份数:"+scheme.getMyBuyShare());
			tv_myMoney.setText("认购金额:"+scheme.getMyBuyMoney()+"元");
			
		} 
		else 
		{
			tv_myShare.setVisibility(View.GONE);
			tv_myMoney.setVisibility(View.GONE);
			
			if (scheme.getIsChase() == 0)
				tv_type.setText("代购");
			else
				tv_type.setText("追号");
			ll_center.setVisibility(View.GONE);
			ll_center2.setVisibility(View.GONE);
			ll_title.setVisibility(View.GONE);
			ll_content.setVisibility(View.GONE);
		}

		// 投注时间
		tv_time.setText(scheme.getDateTime());

		tv_orderNumber.setText("订单号：" + scheme.getSchemeNumber());

		/**是否撤单*/
		if(0 != scheme.getQuashStatus())
		{
			tv_winMoney.setText("已撤单");
			tv_winMoney2.setText("已撤单");
		}
		else
		{
			// 是否开奖
			if ("True".equals(scheme.getSchemeIsOpened())) 
			{
				String winNum[] = scheme.getWinNumber().split("-");
				if (winNum.length == 2)
				{
					tv_win_redNum.setText(winNum[0]);
					tv_win_blueNum.setText(winNum[1]);
				} 
				else 
				{
					tv_win_redNum.setText(winNum[0]);
					tv_win_blueNum.setText("");
				}
				
				// 是否中奖
				if (scheme.getWinMoneyNoWithTax() == 0) 
				{
					tv_winMoney.setText("未中奖");
					tv_winMoney2.setText("未中奖");
				}
				else 
				{
					tv_winMoney.setText(scheme.getWinMoneyNoWithTax() + "元");
					tv_winMoney2.setText(scheme.getWinMoneyNoWithTax() + "元");
				}
			}
			// 未开奖
			else 
			{
				tv_winMoney.setText("待开奖");
				tv_winMoney2.setText("待开奖");
				tv_win_redNum.setText("还未开奖 ");
				tv_win_blueNum.setText("");
				// 待开奖
			}
		}

		if (scheme.getLotteryNumber().contains("\n"))
		{
			String[] str = scheme.getLotteryNumber().split("\\n");
			System.out.println("数组长度：" + str.length);
			strNumber = str;
		} 
		else 
		{
			strNumber = new String[1];
			strNumber[0] = scheme.getLotteryNumber();
		}
		adapter = new OrderIndoAdapter_SSQ(this, strNumber,scheme.getMultiple());
	}

	/** 绑定监听 */
	private void setListener() 
	{
		listView.setAdapter(adapter);
		sv.smoothScrollTo(0, 20);
	}

}
