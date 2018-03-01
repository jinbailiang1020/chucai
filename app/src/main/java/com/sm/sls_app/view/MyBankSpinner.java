package com.sm.sls_app.view;

import java.util.List;
import java.util.Map;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.ui.AlipayInfoActivity;
import com.sm.sls_app.ui.AlipayWithdrawalActivity;
import com.sm.sls_app.ui.BankInfoActivity;
import com.sm.sls_app.ui.WithdrawalActivity;
import com.sm.sls_app.ui.adapter.MySpinnerAdapter;
import com.sm.sls_app.utils.AppTools;

public class MyBankSpinner extends Dialog {

	private ListView listView;
	private MySpinnerAdapter sAdapter;
	private List<Map<String, String>> listString;
	private Context context;
	private int index;
	private int type; // 类型

	public MyBankSpinner(Context context, List<Map<String, String>> list,
			int index, int type) {
		super(context);
		init(context, list, index, type);
		// TODO Auto-generated constructor stub
	}

	public MyBankSpinner(Context context, List<Map<String, String>> list,
			int index, int type, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context, list, index, type);
		// TODO Auto-generated constructor stub
	}

	public MyBankSpinner(Context context, List<Map<String, String>> list,
			int index, int type, int theme) {
		super(context, theme);
		init(context, list, index, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spinner_dialog);
		listView = (ListView) this.findViewById(R.id.spinner_dialog_listView);
		sAdapter = new MySpinnerAdapter(context, listString, index);
		listView.setAdapter(sAdapter);
		listView.setOnItemClickListener(new MyItemClickListener());
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 * @param list
	 */
	private void init(Context context, List<Map<String, String>> list,
			int index, int type) {
		this.context = context;
		this.listString = list;
		this.index = index;
		this.type = type;
	}

	public void updateAdapter() {
		sAdapter.notifyDataSetChanged();
	}

	/**
	 * ListView Item 点击监听
	 * 
	 * @author SLS003
	 * 
	 */
	class MyItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long arg3) {
			String str = listString.get(position).get("name");
			switch (type) {
			case AppTools.BANK_TYPE:
				BankInfoActivity activity = (BankInfoActivity) context;
				activity.et_bankName.setText(str);
				activity.bank_index = position;
				break;
			case AppTools.PROVINCE_TYPE:
				activity = (BankInfoActivity) context;
				// 如果省份改变了 则将城市的值清空
				if (activity.province_index != position) {
					activity.city_index = -1;
					activity.et_shi.setText("");
					activity.zhi_index = -1;
//					activity.et_zhi.setText("");
				}
				activity.et_sheng.setText(str);
				activity.province_index = position;
				sAdapter.setIndex(position);
				activity.changCity();
				break;
			case AppTools.CITY_TYPE:
				activity = (BankInfoActivity) context;
				// 如果省份改变了 则将城市的值清空
				if (activity.city_index != position) {
					activity.zhi_index = -1;
//					activity.et_zhi.setText("");
				}
				activity.et_shi.setText(str);
				activity.city_index = position;
				break;
//			case AppTools.ZHI_TYPE:
//				activity = (BankInfoActivity) context;
//				activity.et_zhi.setText(str);
//				activity.zhi_index = position;
//				break;
			case AppTools.QUESTION_TYPE:
				activity = (BankInfoActivity) context;
				activity.et_question.setText(str);
				activity.question_index = position;
				if (position == 8) {
					activity.isCustom = true;
					activity.setGoneView(activity.isCustom);
				} else {
					activity.isCustom = false;
					activity.setGoneView(activity.isCustom);
				}

				break;
			case AppTools.QUESTION_TYPE2:
				WithdrawalActivity activity2 = (WithdrawalActivity) context;
				activity2.et_question.setText(str);
				activity2.question_index = position;
				if (position == 8) {
					activity2.isCustom = true;
					activity2.setGoneView(activity2.isCustom);
				} else {
					activity2.isCustom = false;
					activity2.setGoneView(activity2.isCustom);
				}
				break;
			case AppTools.ALIPAY_TYPE:
				AlipayInfoActivity activity3 = (AlipayInfoActivity) context;
				activity3.et_question.setText(str);
				activity3.question_index = position;
				if (position == 8) {
					activity3.isCustom = true;
					activity3.setGoneView(activity3.isCustom);
				} else {
					activity3.isCustom = false;
					activity3.setGoneView(activity3.isCustom);
				}
				break;
			case AppTools.ALIPAY_WITHDRAWAL_TYPE:
				AlipayWithdrawalActivity activity4 = (AlipayWithdrawalActivity) context;
				activity4.et_question.setText(str);
				activity4.question_index = position;
				if (position == 8) {
					activity4.isCustom = true;
					activity4.setGoneView(activity4.isCustom);
				} else {
					activity4.isCustom = false;
					activity4.setGoneView(activity4.isCustom);
				}
				break;
			}
			sAdapter.setIndex(position);
			sAdapter.notifyDataSetChanged();
			MyBankSpinner.this.dismiss();
		}

	}

}
