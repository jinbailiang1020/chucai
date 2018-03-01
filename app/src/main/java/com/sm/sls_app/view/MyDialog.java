package com.sm.sls_app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.wheel.widget.OnWheelScrollListener;
import com.sm.sls_app.wheel.widget.WheelAdapter;
import com.sm.sls_app.wheel.widget.WheelView;

/**
 * 自定义对话框 用于幸运选号,合买跟单页面
 * 
 * @author SLS003
 * 
 */
public class MyDialog extends Dialog implements View.OnClickListener {

	private Context context;
	private WheelAdapter adapter;
	private OnWheelScrollListener scrollList;
	private Button btn_ok;
	private Button btn_quit;
	private WheelView wheel;
	private Button btn;

	private int old = -1;
	public int currentId = -1;
	public String content;

	public MyDialog(Context context, int theme, WheelAdapter adapter, Button btn) {
		super(context, theme);
		this.context = context;
		this.adapter = adapter;
		this.btn = btn;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.luck_dialog);
		findView();
		setListener();
	}

	/**
	 * 初始化UI
	 * 
	 * @param view
	 */
	private void findView()
	{
		wheel = (WheelView) this.findViewById(R.id.luck_wheel);
		//wheel.setDEF_VISIBLE_ITEMS(5);
		wheel.setAdapter(adapter);
		btn_ok = (Button) this.findViewById(R.id.btn_ok);
		btn_quit = (Button) this.findViewById(R.id.btn_no);
		wheel.setCyclic(true);

		scrollList = new OnWheelScrollListener() 
		{
			@Override
			public void onScrollingStarted(WheelView wheel) 
			{
			}

			@Override
			public void onScrollingFinished(WheelView wheel) 
			{
				old = currentId;
				currentId = wheel.getCurrentItem();
			}
		};
	}

	/**
	 * 绑定监听
	 */
	private void setListener() 
	{
		btn_ok.setOnClickListener(this);
		btn_quit.setOnClickListener(this);

		wheel.addScrollingListener(scrollList);
	}

	/**
	 * 按钮点击
	 */
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.btn_ok:
			old=currentId;
			MyDialog.this.dismiss();
			break;
		case R.id.btn_no:
			currentId = old;
			MyDialog.this.dismiss();
			break;
		}
		
		if (currentId != -1)
			wheel.setCurrentItem(currentId);
		content = adapter.getItem(currentId);
		btn.setText(content);

	}

}
