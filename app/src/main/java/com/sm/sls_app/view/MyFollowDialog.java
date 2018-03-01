package com.sm.sls_app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.fragment.FollowFragment;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.wheel.widget.OnWheelScrollListener;
import com.sm.sls_app.wheel.widget.WheelAdapter;
import com.sm.sls_app.wheel.widget.WheelView;

/**
 * 自定义对话框 用于幸运选号页面
 * @author SLS003
 */
public class MyFollowDialog extends Dialog implements View.OnClickListener {

	public MyFollowDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

//	private Context context;
//	private WheelAdapter adapter;
//	private OnWheelScrollListener scrollList;
//	private Button btn_ok;
//	private Button btn_quit;
//	private WheelView wheel;
//	private Button btn;
//
//	private int old = AppTools.allLotteryName.size();
//	public int currentId = AppTools.allLotteryName.size();
//	public String content;
//	
//	private FollowFragment activity;
//
////	public MyFollowDialog(Context context, int theme, WheelAdapter adapter, Button btn,FollowFragment fFragment) {
////		super(context, theme);
////		this.context = context;
////		this.adapter = adapter;
////		this.btn = btn;
////		this.activity=fFragment;
////	}
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.luck_dialog);
//		findView();
//		setListener();
//	}
//
//	/**初始化UI*/
//	private void findView() 
//	{
//		wheel = (WheelView) this.findViewById(R.id.luck_wheel);
//		wheel.setAdapter(adapter);
//		wheel.setCurrentItem(currentId);
//		
//		btn_ok = (Button) this.findViewById(R.id.btn_ok);
//		btn_quit = (Button) this.findViewById(R.id.btn_no);
//		wheel.setCyclic(true);
//
//		scrollList = new OnWheelScrollListener() 
//		{
//			@Override
//			public void onScrollingStarted(WheelView wheel) 
//			{
//			}
//
//			@Override
//			public void onScrollingFinished(WheelView wheel)
//			{
//				old = currentId;
//				currentId = wheel.getCurrentItem();
//			}
//		};
//	}
//
//	/**绑定监听 */
//	private void setListener() 
//	{
//		btn_ok.setOnClickListener(this);
//		btn_quit.setOnClickListener(this);
//
//		wheel.addScrollingListener(scrollList);
//	}
//
//	/** 按钮点击*/
//	@Override
//	public void onClick(View v) 
//	{
//		switch (v.getId())
//		{
//		case R.id.btn_ok:
//			/*if(old == currentId)
//			{
//				return ;
//			}*/
//			old=currentId;
//			
//			if(currentId==AppTools.allLotteryName.size())
//			{
//				FollowFragment.lotteryId=AppTools.lotteryIds+",72,73";
//			}
//			else
//			{
//				FollowFragment.lotteryId=AppTools.allLotteryName.get(adapter.getItem(currentId));
//			}
//			FollowFragment.index=currentId;
//			activity.max=0;
////			activity.updateListView();
//			MyFollowDialog.this.dismiss();
//			break;
//		case R.id.btn_no:
//			currentId = old;
//			MyFollowDialog.this.dismiss();
//			break;
//		}
//		if (currentId != -1)
//			wheel.setCurrentItem(currentId);
//		content = adapter.getItem(currentId);
//		btn.setText(content);
//	}

}
