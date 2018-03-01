package com.sm.sls_app.view;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.ui.IdCardActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MyBankDialog extends Dialog implements OnClickListener{

	private Context context;
	private String name, reallyName;
	public MyBankDialog(Context context,String name,String reallyName,String cardNum) {
		super(context);
		init(context, name, reallyName);
		// TODO Auto-generated constructor stub
	}

	public MyBankDialog(Context context,String name,String reallyName,String cardNum, boolean cancelable,
			OnCancelListener cancelListener) 
	{
		super(context, cancelable, cancelListener);
		init(context, name, reallyName);
		// TODO Auto-generated constructor stub
	}

	public MyBankDialog(Context context,String name,String reallyName, int theme) 
	{
		super(context, theme);
		init(context, name, reallyName);
		// TODO Auto-generated constructor stub
	}
	
	
	private Button btn_ok,btn_quit;
	
	private TextView tv_name,tv_reallyName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bang_dialog);
		findView();
		setListener();
	}

	
	private void init(Context context,String name,String reallyName)
	{
		this.context=context;
		this.name=name;
		this.reallyName=reallyName;
	}
	
	/**初始化UI*/
	private void findView() 
	{
		tv_name=(TextView) this.findViewById(R.id.bank_dialog_name);
		tv_reallyName=(TextView) this.findViewById(R.id.bank_dialog_reallyName);
		btn_ok=(Button) this.findViewById(R.id.bank_dialog_btn_ok);
		btn_quit=(Button) this.findViewById(R.id.bank_dialog_btn_quit);
		
		tv_name.setText("用户名： "+name);
		tv_reallyName.setText("真实姓名： "+reallyName);
	}
	
	/**绑定监听*/
	private void setListener() 
	{
		btn_ok.setOnClickListener(this);
		btn_quit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId()) 
		{
		case R.id.bank_dialog_btn_ok:
			showMessage();
			break;
		case R.id.bank_dialog_btn_quit:
			this.dismiss();
			break;	
		}
	}
	
	private void showMessage()
	{
		IdCardActivity activity=(IdCardActivity)context;
		activity.toNext();
		this.dismiss();
	}

}
