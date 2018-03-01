package com.sm.sls_app.ui.adapter;

import java.util.HashSet;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.ui.SelectNumberActivityFC3D;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.NumberTools;
import com.sm.sls_app.view.MyToast;
import com.sm.sls_app.view.VibratorView;

import android.R.string;
import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/** 红球的显示 */
public class MyGridViewAdapterFC3D extends BaseAdapter {

	public static int playType = 6301; // 选号类型
	// 上下文本
	private Context context;
	// 装彩票的集合
	private Integer[] Numbers;
	private String[] haomas;
	private String haoma;
	private int color;
	private int type = 1; // 投注界面类型

	private Vibrator vibrator; // 震动器

	private long count = 0; // 投注注数

	public static String LotteryId = "6";
	

	// 存放胆区红球的 集合
	public static HashSet<String> baiSet = new HashSet<String>();
	public static HashSet<String> shiSet = new HashSet<String>();
	public static HashSet<String> geSet = new HashSet<String>();
	public static HashSet<String> hezhiSet = new HashSet<String>();
	public static HashSet<String> zixuanhezhiSet = new HashSet<String>();
	public static HashSet<String> daxiaoSet = new HashSet<String>();
	public static HashSet<String> jiouSet = new HashSet<String>();
	public static boolean isCaist=false;
	public static boolean isTuolaji=false;
	// 选球界面
	private SelectNumberActivityFC3D activity;

	/** 构造方法 实现初始化 */
	public MyGridViewAdapterFC3D(Context context, Integer[] Numbers, int c,
			int type) {
		this.context = context;
		this.Numbers = Numbers;
		this.color = c;
		this.type = type;
		vibrator = VibratorView.getVibrator(context);
		activity = (SelectNumberActivityFC3D) context;
	}
	
	public MyGridViewAdapterFC3D(Context context, String[] Numbers, int c,
			int type){
		this.context = context;
		this.haomas = Numbers;
		this.color = c;
		this.type = type;
		vibrator = VibratorView.getVibrator(context);
		activity = (SelectNumberActivityFC3D) context;
	}
	public MyGridViewAdapterFC3D(Context context, String Numbers, int c,
			int type){
		this.context = context;
		this.haoma = Numbers;
		this.color = c;
		this.type = type;
		vibrator = VibratorView.getVibrator(context);
		activity = (SelectNumberActivityFC3D) context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(type<=5){
			return Numbers.length;
		}else if(type==6||type==7){
			return haomas.length;
		}else return 1;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		if(type<=5){
			return Numbers[arg0];
		}else if(type==6||type==7){
			return haomas[arg0];
		}else return haoma;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	/** 得到View */
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			// 得到布局文件

			// 得到控件
			if(type<=5){
				view = inflater.inflate(R.layout.gridview_items, null);
				holder.btn = (TextView) view.findViewById(R.id.btn_showNum);
			}else {
				view = inflater.inflate(R.layout.gridview_items_3d, null);
				holder.btn = (TextView) view.findViewById(R.id.btn_shownum_frame);
			}
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		// 给控件赋值
		if(type<=5){
			holder.btn.setText(Numbers[position] + "");
		}else if(type==6||type==7){
			holder.btn.setText(haomas[position]);
		}else 
			holder.btn.setText(haoma);

		if(type<=5){
			// 设置字体显示颜色 和背景图片
			if (Color.RED == color){
				holder.btn.setTextColor(context.getResources()
						.getColor(R.color.main_red));
				holder.btn.setBackgroundResource(R.drawable.icon_ball_red_unselected);
			}else{
				holder.btn.setTextColor(context.getResources().getColor(
						R.color.blue));
				holder.btn.setBackgroundResource(R.drawable.icon_ball_blue_unselected);
			}
		}else{
			holder.btn.setBackgroundResource(R.drawable.common_select_bg_redframe);	
		}
		int num = position;
//		if(type == 4)
//			num = position+1;
		String str ="";
		if(type<=5){
			str = num + "";
		}else if(type==6||type==7){
			str=haomas[position];
		}else 
			str=haoma;
		if (type == 1) {
			// 看是否被选中
			if (baiSet.contains(str)) {
				holder.btn.setBackgroundResource(R.drawable.icon_ball_red_selected);
				holder.btn.setTextColor(Color.WHITE);
			}
		}
		if (type == 2) {
			// 看是否被选中
			if (shiSet.contains(str)) {
				holder.btn.setBackgroundResource(R.drawable.icon_ball_red_selected);
				holder.btn.setTextColor(Color.WHITE);
			}
		}
		if (type == 3) {
			// 看是否被选中
			if (geSet.contains(str)) {
				holder.btn.setBackgroundResource(R.drawable.icon_ball_red_selected);
				holder.btn.setTextColor(Color.WHITE);
			}
		}
		if (type == 4 ) {
			// 看是否被选中
			if (hezhiSet.contains(str)) {
				holder.btn.setBackgroundResource(R.drawable.icon_ball_red_selected);
				holder.btn.setTextColor(Color.WHITE);
			}
		}
		if (type == 5) {
			// 看是否被选中
			if (zixuanhezhiSet.contains(str)) {
				holder.btn.setBackgroundResource(R.drawable.icon_ball_red_selected);
				holder.btn.setTextColor(Color.WHITE);
			}
		}
		if(type==6){
			if(daxiaoSet.contains(str)){
				holder.btn.setBackgroundResource(R.drawable.common_select_bg_redframe_selected);
				holder.btn.setTextColor(Color.WHITE);
			}else {
				holder.btn.setBackgroundResource(R.drawable.common_select_bg_redframe);
				holder.btn.setTextColor(Color.RED);
			}
		}
		if(type==7){
			if(jiouSet.contains(str)){
				holder.btn.setBackgroundResource(R.drawable.common_select_bg_redframe_selected);
				holder.btn.setTextColor(Color.WHITE);
			}else {
				holder.btn.setBackgroundResource(R.drawable.common_select_bg_redframe);
				holder.btn.setTextColor(Color.RED);
			}
		}
		if(type==8){
			if(isCaist){
				holder.btn.setBackgroundResource(R.drawable.common_select_bg_redframe_selected);
				holder.btn.setTextColor(Color.WHITE);
			}else {
				holder.btn.setBackgroundResource(R.drawable.common_select_bg_redframe);
				holder.btn.setTextColor(Color.RED);
			}
		}
		if(type==9){
			if(isTuolaji){
				holder.btn.setBackgroundResource(R.drawable.common_select_bg_redframe_selected);
				holder.btn.setTextColor(Color.WHITE);
			}else {
				holder.btn.setBackgroundResource(R.drawable.common_select_bg_redframe);
				holder.btn.setTextColor(Color.RED);
			}
		}
		// 给按钮添加点击事件
		holder.btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != vibrator) {
					// 震动 80 毫秒
					vibrator.vibrate(80);
				}
				// 得到选号的内容
				String content = holder.btn.getText().toString();
				
				selectNumber(playType, type, content);
				
				activity.updateAdapter();
				getTotalCount(playType);
				if (count > 10000) {
					Toast.makeText(context, "投注金额不能超过20000", Toast.LENGTH_SHORT)
							.show();
					holder.btn.performClick();
				} else {
					AppTools.totalCount = count;
				}
				activity.tv_tatol_count.setText( AppTools.totalCount + "");
				activity.tv_tatol_money.setText((AppTools.totalCount * 2) + "");
			}
		});		
		getTotalCount(playType);
		activity.tv_tatol_count.setText( AppTools.totalCount + "");
		activity.tv_tatol_money.setText((AppTools.totalCount * 2) + "");
		return view;
	}

	/**
	 * 选球
	 * @param playType
	 *            玩法
	 * @param type
	 *            选球位置
	 * @param content
	 *            点击的球上面显示的数字
	 */
	private void selectNumber(int playType, int type, String content) {
		
		MyGridViewAdapterFC3D.playType = playType;
		if (playType == 601 || playType == 609) {
			switch (type) {
			case 1:
				if (baiSet.contains(content)) {
					baiSet.remove(content);
				} else {
					baiSet.add(content);
				}
				break;
			case 2:
				if (shiSet.contains(content)) {
					shiSet.remove(content);
				} else {
					shiSet.add(content);
				}
				break;
			case 3:
				if (geSet.contains(content)) {
					geSet.remove(content);
				} else {
					geSet.add(content);
				}
				break;
			}
		} else if (playType == 611) {//包选3
			switch (type) {
			case 1:
				if (baiSet.contains(content)) {
					baiSet.remove(content);
				}else {
					if (shiSet.contains(content)) {
						shiSet.remove(content);
					}
					baiSet.clear();
					baiSet.add(content);
				}
				break;
			case 2:
				if (shiSet.contains(content)) {
					shiSet.remove(content);
				}else {
					if (baiSet.contains(content)) {
						baiSet.remove(content);
					}
					shiSet.add(content);
				}
				break;
			}
		}else if (playType == 610) {
			if (hezhiSet.contains(content)) {
				hezhiSet.remove(content);
			} else
				hezhiSet.add(content);
		}else if (playType == 613) {//猜大小
			if (daxiaoSet.contains(content)) {
				daxiaoSet.remove(content);
			} else{
				daxiaoSet.clear();
				daxiaoSet.add(content);
			}
		}else if (playType == 616) {//猜奇偶
			if (jiouSet.contains(content)) {
				jiouSet.remove(content);
			} else{
				jiouSet.clear();
				jiouSet.add(content);
			}
		}else if (playType == 614) {
			isCaist=!isCaist;
		}else if (playType == 615) {
			isTuolaji=!isTuolaji;
		}else if (playType == Integer.parseInt(SelectNumberActivityFC3D.id_1d)) {//1d玩法
			switch (type) {
			case 1:
				if (baiSet.contains(content)) {
					baiSet.remove(content);
				} else {
					baiSet.add(content);
				}
				break;
			case 2:
				if (shiSet.contains(content)) {
					shiSet.remove(content);
				} else {
					shiSet.add(content);
				}
				break;
			case 3:
				if (geSet.contains(content)) {
					geSet.remove(content);
				} else {
					geSet.add(content);
				}
				break;
			}
		}else if (playType ==612) {//包选6
			switch (type) {
			case 1://百
				if (baiSet.contains(content)) {
					baiSet.remove(content);
				} else {
					if(shiSet.contains(content)){
						shiSet.remove(content);
					}
					if(geSet.contains(content)){
						geSet.remove(content);
					}
					baiSet.add(content);
				}
				break;
			case 2://十
				if (shiSet.contains(content)) {
					shiSet.remove(content);
				} else {
					if(baiSet.contains(content)){
						baiSet.remove(content);
					}
					if(geSet.contains(content)){
						geSet.remove(content);
					}
					shiSet.add(content);
				}
				break;
			case 3://个
				if (geSet.contains(content)) {
					geSet.remove(content);
				} else {
					if(baiSet.contains(content)){
						baiSet.remove(content);
					}
					if(shiSet.contains(content)){
						shiSet.remove(content);
					}
					geSet.add(content);
				}
				break;
			}
		} else if (playType == Integer.parseInt(SelectNumberActivityFC3D.id_2d)) {//2d玩法
			switch (type) {
			case 1:
				if (baiSet.contains(content)) {
					baiSet.remove(content);
				} else {
					baiSet.add(content);
				}
				break;
			case 2:
				if (shiSet.contains(content)) {
					shiSet.remove(content);
				} else {
					shiSet.add(content);
				}
				break;
			case 3:
				if (geSet.contains(content)) {
					geSet.remove(content);
				} else {
					geSet.add(content);
				}
				break;
			}
		} else {
			if (baiSet.contains(content)) {
				baiSet.remove(content);
			} else {
				baiSet.add(content);
			}
		}

	}

	/** 计算总注数 */
	private void getTotalCount(int playType) {
		
		count = NumberTools.getCountFor3D(playType, baiSet.size(),
				shiSet.size(), geSet.size(),hezhiSet,zixuanhezhiSet,daxiaoSet.size(),jiouSet.size(),isCaist,isTuolaji);
		AppTools.totalCount = count;
	}

	/** 将传进来的值选中 */
	public void setNumByRandom() {
		activity.updateAdapter();
		getTotalCount(playType);
		AppTools.totalCount = count;
		System.out.println(AppTools.totalCount);
		activity.tv_tatol_count.setText( AppTools.totalCount + "");
		activity.tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}

	/** 静态类 */
	static class ViewHolder {
		TextView btn;
	}

}
