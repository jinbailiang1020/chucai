package com.sm.sls_app.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.SelectedNumbers;
import com.sm.sls_app.ui.BetActivityFC3D;
import com.sm.sls_app.utils.AppTools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** 功能：显示我的彩票 的 ListView的Adapter */
public class MyBetLotteryListAdapterFC3D extends BaseAdapter {

	// 上下文本
	private Context context;
	// 装彩票的集合
	private List<SelectedNumbers> listSchemes;
	private BetActivityFC3D betActivity;

	/** 构造方法 实现初始化 */
	public MyBetLotteryListAdapterFC3D(Context context,
			List<SelectedNumbers> listSchemes) {
		this.context = context;
		this.listSchemes = listSchemes;
		betActivity = (BetActivityFC3D) context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listSchemes.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listSchemes.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	/** 获得视图 */
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		final int index = position;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			// 得到布局文件
			view = inflater.inflate(R.layout.item_bet_lv, null);
			// 得到控件
			holder.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
			holder.tv_show_number = (TextView) view
					.findViewById(R.id.tv_show_number);

			holder.tv_type_count_money = (TextView) view
					.findViewById(R.id.tv_type_count_money);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		SelectedNumbers num = listSchemes.get(position);
		// 给控件赋值

		String[] red = checkString(num.getShowLotteryNumber().trim()).split("");
		if (614 == num.getPlayType() || 615 == num.getPlayType()) {
			red = new String[] { num.getShowLotteryNumber().trim() + "" };
		}

		String numberString = "";
		if (red.length > 0) {
			for (String str : red) {
				numberString += (1 == num.getPlayType() ? str : sort(str))
						+ " ";
			}
			if (614 == num.getPlayType() || 615 == num.getPlayType()) {
				numberString = num.getShowLotteryNumber().trim();
			}
			if (610 == num.getPlayType()) {
				numberString = num.getShowLotteryNumber().trim();
				numberString.replace(" ", "   ");
			}
			numberString = numberString.trim();
			Spanned number = Html.fromHtml("<font color='#BE0205'>"
					+ numberString + "</FONT>");
			holder.tv_show_number.setText(number);

			if (6301 == num.getPlayType() || 601 == num.getPlayType())
				holder.tv_type_count_money.setText("直选   " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (602 == num.getPlayType())
				holder.tv_type_count_money.setText("组三   " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (603 == num.getPlayType())
				holder.tv_type_count_money.setText("组六   " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (610 == num.getPlayType())
				holder.tv_type_count_money.setText("和数   " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (604 == num.getPlayType())
				holder.tv_type_count_money.setText("1D   " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (605 == num.getPlayType())
				holder.tv_type_count_money.setText("猜1D   " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (606 == num.getPlayType())
				holder.tv_type_count_money.setText("2D   " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (607 == num.getPlayType())
				holder.tv_type_count_money.setText("猜2D-二同号   "
						+ num.getCount() + "注  "
						+ listSchemes.get(position).getMoney() + "元");
			else if (608 == num.getPlayType())
				holder.tv_type_count_money.setText("猜2D-二不同   "
						+ num.getCount() + "注  "
						+ listSchemes.get(position).getMoney() + "元");
			else if (609 == num.getPlayType())
				holder.tv_type_count_money.setText("通选   " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (611 == num.getPlayType())
				holder.tv_type_count_money.setText("包选3   " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (612 == num.getPlayType())
				holder.tv_type_count_money.setText("包选6   " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (613 == num.getPlayType())
				holder.tv_type_count_money.setText("猜大小   " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (614 == num.getPlayType())
				holder.tv_type_count_money.setText("猜三同   " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (615 == num.getPlayType())
				holder.tv_type_count_money.setText("拖拉机   " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (616 == num.getPlayType())
				holder.tv_type_count_money.setText("猜奇偶   " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			holder.iv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					AppTools.list_numbers.remove(index);
					betActivity.adapter.notifyDataSetChanged();
					betActivity.changeTextShow();
				}
			});
		}
		return view;
	}

	private String checkString(String str) {
		if (str == null)
			return "";
		if (str.contains(" ")) {
//            String[] array=str.split("#");
//            StringBuffer num=new StringBuffer();
//            for (int i = 0; i < array.length; i++) {
//				if (1 != array[i].length()) {
//					num.append("(" + array[i] + ")");
//				} else {
//					num.append(array[i]);
//				}
//            }
//            return num.toString();
            return str.replace(" "," , ");
		}
        return str;
	}

	private String sort(String str) {
		if (str != "") {
			List<String> alList = new ArrayList<String>();
			for (char ch : str.toCharArray()) {
				alList.add(ch + "");
			}
			Collections.sort(alList);
			str = "";
			for (String s : alList) {
				str += s;
			}
		}
		return str;
	}

	/** 静态类 */
	static class ViewHolder {
		ImageView iv_delete;
		TextView tv_show_number;
		TextView tv_type_count_money;
	}
}
