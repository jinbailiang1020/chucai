package com.sm.sls_app.utils;

import java.util.Map;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.view.MyGridView;
import com.sm.sls_app.view.MyListView2;

/**
 * 玩法弹出框
 * 
 * @author Kinwee 修改日期2014-12-12
 * 
 */
public class PopupWindowUtil {
	private final static String TAG = "PopupWindowUtil";
	private PopupWindow popWindow;// 弹出窗口
	private Activity activity;// 依赖的activity
	private ListView playType_list;
	private MyGridViewAdapter gvAdapter;
	private MyListViewAdapter listAdapter;
	private Map<Integer, Map<Integer, String>> data;
	private View relyView;// 依赖的控件
	/** 父id */
	private int selectParentIndex;// 父id
	/** 子id */
	private int selectItemIndex;// 子id
	private LayoutInflater inflact;
	private float gv_item_wid;
	private int size;
	private int nunCuloms;

	public PopupWindowUtil(Activity activity,
			Map<Integer, Map<Integer, String>> data, View relyView) {
		this.activity = activity;
		this.data = data;
		this.relyView = relyView;
	}

	/** 创建popWindow */
	public void createPopWindow() {
		inflact = LayoutInflater.from(activity);
		View view = inflact.inflate(R.layout.layout_popwindow, null);
		playType_list = (ListView) view.findViewById(R.id.lv_pop);
		listAdapter = new MyListViewAdapter();
		playType_list.setAdapter(listAdapter);
		playType_list.setSelection(selectParentIndex);
		gv_item_wid = activity.getResources().getDimension(
				R.dimen.btn_gv_item_width);
		size = data.get(0).size();
		nunCuloms = size;
		if (size > 3) {
			nunCuloms = 3;
		}
		int width = nunCuloms * (int) gv_item_wid;
		popWindow = new PopupWindow(view, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT, true);
		popWindow.setFocusable(true); // 设置PopupWindow可获得焦点
		popWindow.setTouchable(true); // 设置PopupWindow可触摸
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.setOutsideTouchable(true); // 设置PopupWindow外部区域是否可触摸
		popWindow
				.showAsDropDown(relyView, (relyView.getWidth() - width) / 2, 0);
		popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				if (null != listener) {
					listener.getIndex(selectParentIndex, selectItemIndex);
				}
			}
		});// 设置popwindow的消失事件
			// 设置之后点击返回键 popwindow 会消失
			// popWindow.setBackgroundDrawable(new BitmapDrawable());
		// popWindow.setFocusable(true);

		// 监听返回按钮事件，因为此时焦点在popupwindow上，如果不监听，返回按钮没有效果
		OnKeyListener listener = new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					popWindow.dismiss();
					break;
				}
				return true;
			}
		};

		// 监听点击事件，点击其他位置，popupwindow小窗口消失
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				popWindow.dismiss();
				return true;
			}
		});
	}

	public void setSelectIndex(int selectParentIndex, int selectItemIndex) {
		this.selectParentIndex = selectParentIndex;
		this.selectItemIndex = selectItemIndex;
	}

	class MyListViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.keySet().size();
		}

		@Override
		public Object getItem(int posotion) {
			// TODO Auto-generated method stub
			return data.get(posotion);
		}

		@Override
		public long getItemId(int index) {
			// TODO Auto-generated method stub
			return index;
		}

		@Override
		public View getView(int position, View contentView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			MyListViewHolder holder = null;
			if (null == contentView) {
				holder = new MyListViewHolder();
				contentView = inflact.inflate(R.layout.item_pop_lv, null);
				holder.tv_playType = (TextView) contentView
						.findViewById(R.id.tv_playType);
				holder.gv_playType = (MyGridView) contentView
						.findViewById(R.id.gv_playType);
				contentView.setTag(holder);
			} else {
				holder = (MyListViewHolder) contentView.getTag();
			}
			if (1 == data.keySet().size()) {
				holder.tv_playType.setVisibility(View.GONE);// 设置不可见
			} else if (1 < data.keySet().size()) {
				holder.tv_playType.setVisibility(View.VISIBLE);// 设置可见
				if (0 == position) {
					holder.tv_playType.setText("普通投注");
				} else if (1 == position) {
					holder.tv_playType.setText("胆拖投注");
				}
			}
			gvAdapter = new MyGridViewAdapter(position);
			int size = data.get(0).size();
			int num = size;
			if (size > 3) {
				num = 3;
			}
			holder.gv_playType.setNumColumns(num);
			holder.gv_playType.setAdapter(gvAdapter);
			return contentView;
		}

	}

	class MyGridViewAdapter extends BaseAdapter {
		private int pIndex;

		public MyGridViewAdapter(int pIndex) {
			this.pIndex = pIndex;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.get(pIndex).size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int index) {
			// TODO Auto-generated method stub
			return index;
		}

		@Override
		public View getView(final int position, View contentView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			MyGridViewHolder holder = null;
			if (null == contentView) {
				holder = new MyGridViewHolder();
				contentView = inflact.inflate(R.layout.item_pop_lv_gv, null);
				holder.gv_tv_playType = (TextView) contentView
						.findViewById(R.id.gv_tv_playType);
				contentView.setTag(holder);
			} else {
				holder = (MyGridViewHolder) contentView.getTag();
			}
			holder.gv_tv_playType
					.setBackgroundResource(R.drawable.btn_playtype);
			if (selectItemIndex == position && selectParentIndex == pIndex) {
				holder.gv_tv_playType
						.setBackgroundResource(R.drawable.btn_playtype_select);// 设置选中状态
			}
			holder.gv_tv_playType.setText(data.get(pIndex).get(position));
			holder.gv_tv_playType
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							selectParentIndex = pIndex;
							selectItemIndex = position;
							popWindow.dismiss();
						}
					});
			return contentView;
		}
	}

	static class MyListViewHolder {
		private TextView tv_playType;
		private MyGridView gv_playType;
	}

	static class MyGridViewHolder {
		private TextView gv_tv_playType;
	}

	private OnSelectedListener listener;

	public void setOnSelectedListener(OnSelectedListener listener) {
		this.listener = listener;
	}

	public interface OnSelectedListener {
		void getIndex(int parentIndex, int itemIndex);
	}
}
