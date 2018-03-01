package com.sm.sls_app.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.Inflater;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.DtMatch;
import com.sm.sls_app.ui.Select_jczqActivity;

/** 猜比分框 **/
@SuppressLint("UseSparseArrays")
public class SelectScoreDialog_BQC extends Dialog implements OnClickListener {

	private Context context;

	private Select_jczqActivity activity;


	private LayoutInflater inFlater;
	public SelectScoreDialog_BQC(Context context, DtMatch dtm) {
		super(context);
		init(context, dtm);
	}

	public SelectScoreDialog_BQC(Context context, DtMatch dtm, 
			 boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context, dtm);
	}

	public SelectScoreDialog_BQC(Context context, DtMatch dtm, int theme) {
		super(context, theme);
		init(context, dtm);
	}


	private Button btn_quit, btn_ok;
	private GridView gv_check;
	private static MyGridViewAdapter gAdapter;

	private TextView tv_mainTeam, tv_gusTeam;



	private int groupId, itemId;

	private ArrayList<DMBean> data=new ArrayList<SelectScoreDialog_BQC.DMBean>();
	
	private DtMatch dm;//传入进来的比赛对阵

	/** 所选集合下标 **/
	private Set<Integer> setCheck = new HashSet<Integer>();
	
	private boolean isSelected=false;//是否被选中
	

	public void init(Context context, DtMatch dtm) {
		this.context = context;
		dm=dtm;
		inFlater=LayoutInflater.from(context);
		activity = (Select_jczqActivity) context;
		setDate();
	}
	
	/**
	 * 设置赛果和赔率
	 */
	public void setDate(){
		DMBean db_ss=new DMBean();
		db_ss.result="胜胜";
		db_ss.scale=dm.getSs();
		DMBean db_sp=new DMBean();
		db_sp.result="胜平";
		db_sp.scale=dm.getSp();
		DMBean db_sf=new DMBean();
		db_sf.result="胜负";
		db_sf.scale=dm.getSf();
		
		DMBean db_ps=new DMBean();
		db_ps.result="平胜";
		db_ps.scale=dm.getPs();
		DMBean db_pp=new DMBean();
		db_pp.result="平平";
		db_pp.scale=dm.getPp();
		DMBean db_pf=new DMBean();
		db_pf.result="平负";
		db_pf.scale=dm.getPf();
		
		DMBean db_fs=new DMBean();
		db_fs.result="负胜";
		db_fs.scale=dm.getFs();
		DMBean db_fp=new DMBean();
		db_fp.result="负平";
		db_fp.scale=dm.getFp();
		DMBean db_ff=new DMBean();
		db_ff.result="负负";
		db_ff.scale=dm.getFf();
		data.add(db_ss);
		data.add(db_sp);
		data.add(db_sf);
		data.add(db_ps);
		data.add(db_pp);
		data.add(db_pf);
		data.add(db_fs);
		data.add(db_fp);
		data.add(db_ff);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_score_dialog_bqc);

		btn_quit = (Button) this.findViewById(R.id.btn_quit);
		btn_ok = (Button) this.findViewById(R.id.btn_ok);

		tv_mainTeam = (TextView) this.findViewById(R.id.tv_mainTeam);
		tv_gusTeam = (TextView) this.findViewById(R.id.tv_gusTeam);

		gv_check = (GridView) this.findViewById(R.id.check_gridview);
		gAdapter=new MyGridViewAdapter();
		gv_check.setAdapter(gAdapter);

		tv_mainTeam.setText(dm.getMainTeam());//主队
		tv_gusTeam.setText(dm.getGuestTeam());//客队
		activity = (Select_jczqActivity) context;

		setListner();
	}


	public void setId(int groupId, int itemId) {
		this.groupId = groupId;
		this.itemId = itemId;
	}

	/** 绑定监听 **/
	private void setListner() {
		btn_quit.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
	}

	/** 点击监听 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_quit:
			SelectScoreDialog_BQC.this.dismiss();
			break;
		case R.id.btn_ok:
//			activity.setBQCStr(groupId, itemId,setBQCStr());
			SelectScoreDialog_BQC.this.dismiss();
			break;
		}
	}


	/** 设置 半全场按钮显示值 **/
	private String setBQCStr() {
		StringBuffer selected=new StringBuffer();//已选的赛果的下标
		Iterator<Integer> it=setCheck.iterator();
		ArrayList<Integer> list=new ArrayList<Integer>();
		while (it.hasNext()){
			list.add(it.next());
		}
		int set[]= new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			set[i]=list.get(i);
		}
		Arrays.sort(set);
		for (int i = 0; i < set.length; i++) {
			selected.append(set[i]+" ");
		}
		return selected.toString().trim();
	}

	/** GridView Adapter **/
	class MyGridViewAdapter extends BaseAdapter {


		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int arg0) {
			return data.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}


		@Override
		public View getView(final int position, View view, ViewGroup parents) {
			Holder holder=null;
			if(null==view){
				view=inFlater.inflate(R.layout.gridview_items_dialog_bqc,null );
				holder=new Holder();
				holder.layout_item_dialog=(LinearLayout) view.findViewById(R.id.layout_item_dialog);
				holder.tv_item_dialog_result=(TextView) view.findViewById(R.id.tv_item_dialog_result);
				holder.tv_item_dialog_scale=(TextView) view.findViewById(R.id.tv_item_dialog_scale);
				view.setTag(holder);
			}else{
				holder=(Holder) view.getTag();
			}
			holder.layout_item_dialog.setBackgroundResource(R.drawable.bg_gridview_item_unselected);
			holder.tv_item_dialog_result.setTextColor(Color.BLACK);
			holder.tv_item_dialog_scale.setTextColor(Color.BLACK);
			if(setCheck.contains(position)){//包含下标改变背景
				holder.layout_item_dialog.setBackgroundResource(R.drawable.bg_gridview_item_selected);
				holder.tv_item_dialog_result.setTextColor(Color.WHITE);
				holder.tv_item_dialog_scale.setTextColor(Color.WHITE);
			}
			holder.tv_item_dialog_result.setText(data.get(position).result);
			holder.tv_item_dialog_scale.setText(data.get(position).scale);
			holder.layout_item_dialog.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(setCheck.contains(position)){
						setCheck.remove(position);
					}else {
						setCheck.add(position);
					}
					gAdapter.notifyDataSetChanged();//刷新页面
				}
			});
			return view;
		}
	}

	/** 给存储选中的下标赋值 **/
	public void set_check(String ss) {
		setCheck.clear();
		Log.i("x", "传入的值" + ss);
		if (ss.length() == 0) {
			return;
		}
		String[] str = ss.split(" ");
		setCheck.clear();
		for (int j = 0; j < str.length; j++) {
			int i = Integer.parseInt(str[j]);
			setCheck.add(i);
		}
	}
	
	static class Holder{
		LinearLayout layout_item_dialog;
		TextView tv_item_dialog_result;
		TextView tv_item_dialog_scale;
	}
	private class DMBean{
		String result;//赛果
		String scale;//赔率
	}
	
}
