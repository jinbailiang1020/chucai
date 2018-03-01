package com.sm.sls_app.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.ui.Select_jczqActivity;

/** 竞彩 筛选框 **/
public class MyJCDialog extends Dialog implements OnClickListener {

	private Context context;
	private Select_jczqActivity activity;
	/** 所选集合下标 **/
	private Set<Integer> setCheck = new HashSet<Integer>();
	
	/** 让球类型 **/
	private int type = 100, oldType = 100;
	private int passType=-1;//过关类型   1.为过关   2.为单关

	public MyJCDialog(Context _context, Set<String> set, boolean cancelable,
			OnCancelListener cancelListener) 
	{
		super(_context, cancelable, cancelListener);
		init(_context, set);
		// TODO Auto-generated constructor stub
	}

	public MyJCDialog(Context _context, Set<String> set, int theme,int pass_type) 
	{
		super(_context, theme);
		passType=pass_type;
		init(_context, set);
		// TODO Auto-generated constructor stub
	}

	public MyJCDialog(Context _context, Set<String> set)
	{
		super(_context);
		init(_context, set);
		// TODO Auto-generated constructor stub
	}

	private Button btn_ok, btn_quit, btn_selectAll, btn_invert;

//	private LinearLayout ll_loseBall;

	private RadioGroup rg;

	private GridView gv;
	/** 存储所有赛事的集合 game名称 **/
	private List<String> list_all = new ArrayList<String>();
	private List<String> list_all_single= new ArrayList<String>();
	
	/** 存储所选赛事的集合 game名称 **/
	private List<String> listGame = new ArrayList<String>();

	private GridAdapter gAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dialog_jczq);

		findView();

		setListener();
		selectAll();
	}

//	/**设置选中RadioGroup的下标*/
//	private void initRadioSelect() 
//	{
//		switch (type) 
//		{
//		case 0:
//			rg.check(R.id.rb_fei);
//			break;
//		case 1:
//			rg.check(R.id.rb_rang);
//			break;
////		case 100:
////			rg.check(R.id.rb_all);
////			break;
//		}
//	}
	
	public void resetSetDate(Set<String> set){
		init(set);
	}
	
	private void init(Set<String> set)
	{
		Iterator<String> it = set.iterator();
		if(1==passType){//过关
			while (it.hasNext()) 
			{
				this.list_all.add(it.next());
			}
		}else if(2==passType){//单关
			while (it.hasNext()) 
			{
				this.list_all_single.add(it.next());
			}
		}
	}
	/**初始化*/
	private void init(Context _context, Set<String> set)
	{
		this.context = _context;
		activity = (Select_jczqActivity) context;
		Iterator<String> it = set.iterator();
		if(1==passType){//过关
			while (it.hasNext()) 
			{
				this.list_all.add(it.next());
			}
		}else if(2==passType){//单关
			while (it.hasNext()) 
			{
				this.list_all_single.add(it.next());
			}
		}
	}

	/**初始化UI*/
	private void findView()
	{
		btn_ok = (Button) this.findViewById(R.id.btn_ok);
		btn_quit = (Button) this.findViewById(R.id.btn_quit);
		btn_selectAll = (Button) this.findViewById(R.id.btn_allSelect);
		btn_invert = (Button) this.findViewById(R.id.btn_notAllSelect);

//		ll_loseBall = (LinearLayout) this.findViewById(R.id.ll_loseBall);

		rg = (RadioGroup) this.findViewById(R.id.dialog_rg);
		gv = (GridView) this.findViewById(R.id.spf_dialog_gd);
		gAdapter = new GridAdapter(context);
		gv.setAdapter(gAdapter);
	}

//	/** 设置 让球选择 是否可见 **/
//	public void setRadioVisible(int v) 
//	{
//		ll_loseBall.setVisibility(v);
//	}

	/**绑定监听*/
	private void setListener() 
	{
		btn_ok.setOnClickListener(this);
		btn_quit.setOnClickListener(this);
		btn_selectAll.setOnClickListener(this);
		btn_invert.setOnClickListener(this);
//		rg.setOnCheckedChangeListener(new MyCheckedChange());
	}

//	/**radioGroup ClickChangeListener */
//	class MyCheckedChange implements RadioGroup.OnCheckedChangeListener 
//	{
//		@Override
//		public void onCheckedChanged(RadioGroup group, int checkedId) 
//		{
//			switch (checkedId)
//			{
////			case R.id.rb_all:
////				type = 100;
////				break;
//			case R.id.rb_rang:
//				type = 1;
//				break;
//			case R.id.rb_fei:
//				type = 0;
//				break;
//			default:
//				break;
//			}
//		}
//	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.btn_ok:
			if(setCheck.size() == 0)
			{
				MyToast.getToast(context, "请至少选择一个赛事").show();
				return;
			}
			listGame.clear();
			if(1==passType){//过关
				for (Integer i : setCheck)
				{
					listGame.add(list_all.get(i));
				}
			}else if(2==passType){//单关
				for (Integer i : setCheck)
				{
					listGame.add(list_all_single.get(i));
				}
			}
//			activity.updateAdapter(listGame, type);
//			oldType = type;
//			activity.clearSelect();
			this.dismiss();
			break;
		case R.id.btn_quit:
			this.dismiss();
//			type = oldType;
//			initRadioSelect();
			break;
		case R.id.btn_allSelect:
			selectAll();
			break;
		case R.id.btn_notAllSelect:
			invertSelect();
			break;
		}
	}

	/**全选*/
	private void selectAll() 
	{
		setCheck.clear();
		if(1==passType){//过关
			for (int i = 0; i < list_all.size(); i++) 
			{
				setCheck.add(i);
			}
		}else if(2==passType){//单关
			for (int i = 0; i < list_all_single.size(); i++) 
			{
				setCheck.add(i);
			}
		}
		gAdapter.notifyDataSetChanged();
	
	}

	/**反选*/
	private void invertSelect() 
	{ 
		
		if(1==passType){//过关
			for (int i = 0; i < list_all.size(); i++)
			{
				if (setCheck.contains(i)) 
				{
					setCheck.remove(i);
					
				} 
				else
				{
					setCheck.add(i);
				}
			}
			
		}else if(2==passType){//单关
			for (int i = 0; i < list_all_single.size(); i++)
			{
				if (setCheck.contains(i)) 
				{
					setCheck.remove(i);
					
				} 
				else
				{
					setCheck.add(i);
				}
			}
		}
		gAdapter.notifyDataSetChanged();
	}

	/**适配器*/
	class GridAdapter extends BaseAdapter 
	{
		private Context mContext;

		private GridAdapter(Context context) 
		{
			this.mContext = context;
		}

		@Override
		public int getCount() 
		{
			// TODO Auto-generated method stub
			if(1==passType){//过关
				return list_all.size();
			}else if(2==passType){//单关
				return list_all_single.size();
			}else 
				return 0;
		}

		@Override
		public Object getItem(int arg0) 
		{
			if(1==passType){//过关
				return list_all.get(arg0);
			}else if(2==passType){//单关
				return list_all_single.get(arg0);
			}else 
				return null;
		}

		@Override
		public long getItemId(int arg0) 
		{
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2)
		{
			final int index = position;
			final CheckBox cb = new CheckBox(mContext, null, R.style.MyCheckBox);
			cb.setBackgroundResource(R.drawable.game_back2);
			if(1==passType){//过关
				cb.setText(list_all.get(position));
			}else if(2==passType){//单关
				cb.setText(list_all_single.get(position));
			}
			cb.setTextSize(14);
			cb.setTextColor(Color.BLACK);
			cb.setButtonDrawable(null);
//			cb.setPadding(40, 0, 0, 0);
			cb.setGravity(Gravity.CENTER_VERTICAL);
			cb.setFocusable(true);
			cb.setEnabled(true);
			cb.setClickable(true);
			
			if (null == setCheck)
			{ 
				setCheck = new HashSet<Integer>();
				selectAll();
			}
			else 
			{
				if (setCheck.contains(position)) 
				{
					cb.setChecked(true);
					cb.setBackgroundResource(R.drawable.game_back_select2);
//					cb.setPadding(40, 0, 0, 0);
				}
			}

			/**
			 * 复选框点击
			 */
			cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() 
			{
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) 
				{
					Log.i("x", "点击复选框");
					if (isChecked) 
					{
						buttonView.setBackgroundResource(R.drawable.game_back_select2);
						setCheck.add(index);
//						cb.setPadding(40, 0, 0, 0);
					} 
					else 
					{
						setCheck.remove(index);
						buttonView.setBackgroundResource(R.drawable.game_back2);
//						cb.setPadding(40, 0, 0, 0);
					}
				}
			});
			return cb;
		}
	}

}
