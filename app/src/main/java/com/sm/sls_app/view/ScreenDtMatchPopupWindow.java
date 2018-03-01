package com.sm.sls_app.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.DtMatch;
import com.sm.sls_app.indicator.widget.TabPageIndicator;
import com.sm.sls_app.indicator.widget.TabPageIndicator.OnTabReselectedListener;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.view.SelectJCZQDialog.DialogResultListener;

/**
 * 过关方式弹出框
 * 
 * @author Kinwee 修改日期2014-12-12
 * 
 */
public class ScreenDtMatchPopupWindow {
	private final static String TAG = "ScreenDtMatchPopupWindow";
	private PopupWindow popWindow;// 弹出窗口
	private Context context;
	private MyGridView pop_screen_gv;
	private MyGridViewAdapterScreenDM adapter;
	private View relyView;// 依赖的view
	private Button btn_cancel;//取消
	private Button btn_confirm;//确定
	private Button btn_select_all;//全选
	private Button btn_select_anti;//反选
	private List<List<DtMatch>> dtMatch_list;//所有对阵
	private ArrayList<String> name_list;//存储所有对阵的赛事名称
	private ArrayList<String> select_name_list=new ArrayList<String>();//存储已选对阵的赛事名称
	private List<List<DtMatch>> screenMatch_list;//筛选之后的所有对阵
	
	private LayoutInflater inflater;

	public ScreenDtMatchPopupWindow(Context context,
			List<List<DtMatch>> dtMatch_list, View relyView) {
		this.context = context;
		this.relyView = relyView;
		this.dtMatch_list = dtMatch_list;
		init();
	}

	public void init() {
		// 把筛选的队存起来
		name_list = new ArrayList<String>();
		select_name_list = new ArrayList<String>();
		if (null != AppTools.lottery) {
			for (List<DtMatch> listMatch : dtMatch_list) {
				for (DtMatch match : listMatch) {
					if (!name_list.contains(match.getGame())) {
						name_list.add(match.getGame());
						select_name_list.add(match.getGame());
					}
				}
			}
		}
	}

	/** 创建popWindow */
	public void createPopWindow() {
		inflater = LayoutInflater.from(context);
		View parent = inflater.inflate(R.layout.pop_screen_dtmatch, null);
		btn_cancel = (Button) parent.findViewById(R.id.btn_cancel);
		btn_confirm = (Button) parent.findViewById(R.id.btn_confirm);
		btn_select_anti = (Button) parent.findViewById(R.id.btn_select_anti);
		btn_select_all = (Button) parent.findViewById(R.id.btn_select_all);
		pop_screen_gv = (MyGridView) parent.findViewById(R.id.pop_screen_gv);
		adapter=new MyGridViewAdapterScreenDM();
		pop_screen_gv.setAdapter(adapter);
		btn_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {// 取消
				// TODO Auto-generated method stub
				if (null != ScreenDtMatchPopupWindow.this.listener) {
					listener.getResult(0, screenMatch_list,select_name_list);
				}
				popWindow.dismiss();
			}
		});
		btn_confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {// 确定
				// TODO Auto-generated method stub
				if(1>select_name_list.size()){//未选择赛事
					MyToast.getToast(context, "请至少选择一场赛事").show();
				}else{
					if (null != ScreenDtMatchPopupWindow.this.listener) {
						setScreenMatchList(select_name_list, dtMatch_list);//根据筛选赛事筛选对阵
						listener.getResult(1, screenMatch_list,select_name_list);
					}
					popWindow.dismiss();
				}
			}
		});
		btn_select_all.setOnClickListener(new View.OnClickListener() {//全选
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				select_name_list=new ArrayList<String>();
				for (int i = 0; i < name_list.size(); i++) {
					select_name_list.add(name_list.get(i));
				}
				adapter.notifyDataSetChanged();
			}
		});
		btn_select_anti.setOnClickListener(new View.OnClickListener() {//反选
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(0==select_name_list.size()){//如果未选
					select_name_list=new ArrayList<String>();
					for (int i = 0; i < name_list.size(); i++) {
						select_name_list.add(name_list.get(i));
					}
				}else{
					ArrayList<String> list=new ArrayList<String>();
					for (int i = 0; i < name_list.size(); i++) {
						if(!select_name_list.contains(name_list.get(i))){//如果未选
							list.add(name_list.get(i));
						}
					}
					select_name_list=list;
				}
				adapter.notifyDataSetChanged();
			}
		});
		popWindow = new PopupWindow(parent, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT, true);
		popWindow.setFocusable(true); // 设置PopupWindow可获得焦点
		popWindow.setTouchable(true); // 设置PopupWindow可触摸
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.setOutsideTouchable(false); // 设置PopupWindow外部区域是否可触摸
		popWindow.showAtLocation(relyView, Gravity.CENTER, 0, 0);// 设置显示的位置
	}
	
	/** 
	 * 根据筛选赛事筛选对阵
	 * @param select_name_list 已选的赛事
	 * @param dtMatch_list 所有对阵
	 */
	public void setScreenMatchList(List<String> select_name_list,List<List<DtMatch>> dtMatch_list) {
		screenMatch_list=new ArrayList<List<DtMatch>>();
		for (List<DtMatch> list : dtMatch_list) {
			List<DtMatch> listM = new ArrayList<DtMatch>();
			for (DtMatch dt : list) {
				for (String str : select_name_list) {
					if (dt.getGame().equals(str)) {
							listM.add(dt);
							break;
					}
				}
			}
			if (listM.size() != 0)
				screenMatch_list.add(listM);
		}
	}
	
	/**
	 * 设置已选对阵的赛事名称集合
	 * @param select_name_list
	 */
	public void setSelectDMName(ArrayList<String> select_name_list){
		this.select_name_list=select_name_list;
	}

	class MyGridViewAdapterScreenDM extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return name_list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return name_list.get(position);
		}

		@Override
		public long getItemId(int index) {
			// TODO Auto-generated method stub
			return index;
		}

		@Override
		public View getView(final int position, View contentView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			final Holder holder;
			if (null == contentView) {
				contentView = inflater.inflate(R.layout.item_pop_screen_gv, null);
				holder = new Holder();
				holder.tv_dm_name = (TextView) contentView
						.findViewById(R.id.tv_dm_name);
				contentView.setTag(holder);
			} else {
				holder = (Holder) contentView.getTag();
			}
			holder.tv_dm_name.setText(name_list.get(position));
			holder.tv_dm_name.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);//设置背景
			if (select_name_list.contains(name_list.get(position))) {
				holder.tv_dm_name.setBackgroundResource(R.drawable.btn_playtype_select);//设置背景
			}
			holder.tv_dm_name
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (select_name_list.contains(name_list.get(position))) {// 如果包含
								select_name_list.remove(name_list.get(position));// 移除
							} else {// 反之添加
								select_name_list.add(name_list.get(position));
							}
							notifyDataSetChanged();
						}
					});
			return contentView;
		}
	}


	static class Holder {
		private TextView tv_dm_name;
	}

	private DialogResultListener listener;

	public void setDialogResultListener(DialogResultListener listener) {
		this.listener = listener;
	}

	public interface DialogResultListener {
		/**
		 * 
		  * * 获取结果的方法
		 * 
		 * @param resultCode
		 *            0.取消 1.确定
		 * @param screenMatch_list
		 *            筛选的结果集合
		 * @param select_name_list
		 * 			     已筛选对阵所属赛事名称集合
		 */
		

		void getResult(int resultCode, List<List<DtMatch>> screenMatch_list,ArrayList<String> select_name_list);
	}

}
