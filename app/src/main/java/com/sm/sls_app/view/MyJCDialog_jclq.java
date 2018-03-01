package com.sm.sls_app.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SearchViewCompat.OnCloseListenerCompat;
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
import android.widget.RadioGroup;
import android.widget.TextView;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.ui.Select_jclqActivity;
import com.sm.sls_app.ui.Select_jczqActivity;
import com.sm.sls_app.view.ScreenDtMatchPopupWindow.Holder;

/** 竞彩 筛选框 **/
public class MyJCDialog_jclq extends Dialog implements OnClickListener {

	private Context context;
	private Select_jclqActivity activity;
	/** 所选集合下标 **/
	private Set<Integer> setCheck = new HashSet<Integer>();
	/** 让球类型 **/
	private int type = 100, oldType = 100;
	private Button btn_cancel, btn_confirm, btn_select_anti, btn_select_all;
	private GridView gv;
	/** 存储所有赛事的集合 game名称 **/
	private List<String> list_all = new ArrayList<String>();
	/** 存储所选赛事的集合 game名称 **/
	private List<String> listGame = new ArrayList<String>();
	private GridAdapter gAdapter;

	public MyJCDialog_jclq(Context _context, Set<String> set,
			boolean cancelable, OnCancelListener cancelListener) {
		super(_context, cancelable, cancelListener);
		init(_context, set);
		// TODO Auto-generated constructor stub
	}

	public MyJCDialog_jclq(Context _context, Set<String> set, int theme) {
		super(_context, theme);
		init(_context, set);
		// TODO Auto-generated constructor stub
	}

	public MyJCDialog_jclq(Context _context, Set<String> set) {
		super(_context);
		init(_context, set);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.pop_screen_dtmatch);
		findView();
		setListener();
		selectAll();
	}

	/** 初始化 */
	private void init(Context _context, Set<String> set) {

		this.context = _context;
		activity = (Select_jclqActivity) context;
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			this.list_all.add(it.next());
		}
	}

	/** 初始化UI */
	private void findView() {
		btn_cancel = (Button) this.findViewById(R.id.btn_cancel);
		btn_confirm = (Button) this.findViewById(R.id.btn_confirm);
		btn_select_anti = (Button) this.findViewById(R.id.btn_select_anti);
		btn_select_all = (Button) this.findViewById(R.id.btn_select_all);
		gv = (MyGridView) this.findViewById(R.id.pop_screen_gv);
		gAdapter = new GridAdapter(context);
		gv.setAdapter(gAdapter);
	}

	/** 绑定监听 */
	private void setListener() {
		btn_cancel.setOnClickListener(this);
		btn_confirm.setOnClickListener(this);
		btn_select_anti.setOnClickListener(this);
		btn_select_all.setOnClickListener(this);
	}

	public void set(List<String> listgamea) {
		setCheck.clear();
		if (listgamea.size() == 0) {
			for (int i = 0; i < list_all.size(); i++) {
				setCheck.add(i);
			}
		}
		for (int i = 0; i < listgamea.size(); i++) {
			for (int j = 0; j < list_all.size(); j++) {
				if (listgamea.get(i).equals(list_all.get(j)))
					setCheck.add(j);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:
			if (setCheck.size() == 0) {
				MyToast.getToast(context, "请至少选择一个赛事").show();
				return;
			}
			listGame.clear();
			for (Integer i : setCheck) {
				listGame.add(list_all.get(i));
			}
			activity.updateAdapter(listGame, 100);
			oldType = type;
			activity.clearSelect();
			this.dismiss();
			break;
		case R.id.btn_cancel:
			this.dismiss();
			type = oldType;
			gAdapter.notifyDataSetChanged();
			break;
		case R.id.btn_select_all:
			selectAll();
			break;
		case R.id.btn_select_anti:
			invertSelect();
			break;
		}
	}

	/** 全选 */
	private void selectAll() {
		for (int i = 0; i < list_all.size(); i++) {
			setCheck.add(i);
		}
		gAdapter.notifyDataSetChanged();
	}

	/** 反选 */
	private void invertSelect() {
		for (int i = 0; i < list_all.size(); i++) {
			if (setCheck.contains(i)) {
				setCheck.remove(i);
			} else {
				setCheck.add(i);
			}
		}
		gAdapter.notifyDataSetChanged();
	}

	/** 适配器 */
	class GridAdapter extends BaseAdapter {
		private Context mContext;

		/** 所选集合下标 **/

		private GridAdapter(Context context) {
			this.mContext = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list_all.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list_all.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(final int position, View contentView, ViewGroup arg2) {
			final ViewHolder holder;
			if (null == contentView) {
				holder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(context);
				// 得到布局文件
				contentView = inflater.inflate(R.layout.item_pop_screen_gv,
						null);
				holder.tv_dm_name = (TextView) contentView
						.findViewById(R.id.tv_dm_name);
				contentView.setTag(holder);
			} else {
				holder = (ViewHolder) contentView.getTag();
			}
			holder.tv_dm_name.setText(list_all.get(position));
			holder.tv_dm_name
					.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
			final Iterator<Integer> it = setCheck.iterator();
			while (it.hasNext()) {
				if (it.next() == position) {
					holder.tv_dm_name
							.setBackgroundResource(R.drawable.btn_playtype_select);// 设置背景
				}
			}
			holder.tv_dm_name.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Iterator<Integer> it2 = setCheck.iterator();
					int a = 0;
					while (it2.hasNext()) {
						if (it2.next() == position) {
							setCheck.remove(position);// 设置背景
							holder.tv_dm_name
									.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
							a += 1;
							return;
						}
					}
					if (a == 0) {
						setCheck.add(position);
						holder.tv_dm_name
								.setBackgroundResource(R.drawable.btn_playtype_select);
					}
					notifyDataSetChanged();
				}
			});
			return contentView;
		}
	}
	class ViewHolder {
		private TextView tv_dm_name;
	}
}
