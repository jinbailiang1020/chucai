package com.sm.sls_app.ui.adapter;

import java.util.ArrayList;
import java.util.List;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.view.MyToast;
import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class ZhuiqiAdapter extends BaseAdapter {
	private Context context;
	private List<String> qi;
	private int num;// 多少期
	private long zhu;// 总注数
	private int bei;// 统一倍数
	private LayoutInflater inflater;
	private Refresh refresh;

	public ZhuiqiAdapter(Context context, List<String> qi) {
		super();
		this.context = context;
		this.qi = qi;
		inflater = LayoutInflater.from(context);
	}

	public void setListener(Refresh listener) {
		this.refresh = listener;
	}

	private class BeiWatcher implements TextWatcher {
		private QiViewHolder holder;
		private int b = -1;

		public BeiWatcher(QiViewHolder holder) {
			this.holder = holder;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		@Override
		public void afterTextChanged(Editable edt) {
			// String value = edt.toString();
			int position = (Integer) holder.bei.getTag();
			// System.out.println(" pos=" + position + " value=" + value);
			int bb = 10000;
			if (position < AppTools.beiList.size()) {
				bb = AppTools.beiList.get(position);
			}
			if (edt.toString().trim().length() != 0) {
				if (Integer.parseInt(edt.toString().trim()) == 0) {
					holder.bei.setText("1");
					b = 1;
				} else {
					b = Integer.parseInt(edt.toString().trim());
				}
				holder.bei.setSelection(holder.bei.getText().length());
			} else {
				b = 1;
				if (position < AppTools.beiList.size()) {
					AppTools.beiList.set(position, b);
					holder.money.setText(String.format("¥%d元", b * zhu * 2));
				}
				// holder.bei.setText("1");
				// b = 1;
				// holder.bei.setSelection(holder.bei.getText().length());
			}
			setCursorPosition(holder.bei);
			if (position < AppTools.beiList.size()) {
				AppTools.beiList.set(position, b);
				holder.money.setText(String.format("¥%d元", b * zhu * 2));
			}
			if (null != refresh) {
				if (bb != b) {
					refresh.refreshData();
					System.out.println("通知回调=========");
				}
			}
		}
	}

	public void setCursorPosition(EditText et) {
		CharSequence text = et.getText();
		if (text instanceof Spannable) {
			Selection.setSelection((Spannable) text, text.length());
		}
	}

	public interface Refresh {
		void refreshData();
	}

	/**
	 * 设置追期条数
	 * 
	 * @param num
	 */

	public void setData(int num, long zhu, int bei) {
		if (num == this.num && zhu == this.zhu && bei == this.bei) {
			return;
		}
		// System.out.println("setdata=============");
		if (null != AppTools.beiList) {
			AppTools.beiList.clear();
		} else {
			AppTools.beiList = new ArrayList<Integer>();
		}
		for (int i = 0; i < AppTools.qi; i++) {
			AppTools.beiList.add(AppTools.bei);
		}
		if (zhu > 0)
			this.zhu = zhu;
		else
			this.zhu = 0;
		if (num > 0) {
			this.num = num;
		} else
			this.num = 0;
		if (bei > 0)
			this.bei = bei;
		else
			this.bei = 0;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// System.out.println("getCount num=" + num);
		if (num > 0) {
			return num;
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return qi.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// System.out.println("getView pos=" + position);

		QiViewHolder holder;
		if (null == convertView) {
			holder = new QiViewHolder();
			convertView = inflater.inflate(R.layout.zhuiqi_item, null);
			holder.qihao = (TextView) convertView
					.findViewById(R.id.zhuiqi_item_qihao);
			holder.bei = (EditText) convertView
					.findViewById(R.id.zhuiqi_item_bei);
			holder.money = (TextView) convertView
					.findViewById(R.id.zhuiqi_item_money);
			holder.bei.setTag(position);
			holder.bei.addTextChangedListener(new BeiWatcher(holder));
			convertView.setTag(holder);
		} else {
			holder = (QiViewHolder) convertView.getTag();
			holder.bei.setTag(position);
		}
		holder.qihao.setText(String.format("%s期", qi.get(position)));
		holder.bei.setText(String.valueOf(AppTools.beiList.get(position)));
		holder.money.setText(String.format("¥%d元",
				AppTools.beiList.get(position) * zhu * 2));
		return convertView;
	}

	class QiViewHolder {
		private TextView qihao;
		private EditText bei;
		private TextView money;
	}

}
