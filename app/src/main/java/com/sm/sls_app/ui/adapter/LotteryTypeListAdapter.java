package com.sm.sls_app.ui.adapter;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.Lottery;
import com.sm.sls_app.ui.MainActivity;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.view.CustomDigitalClock;
import com.sm.sls_app.view.MyToast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 显示彩票分类的 ListView的Adapter
 * 
 * @author Administrator
 */
public class LotteryTypeListAdapter extends BaseAdapter {

	// 上下文本
	private Context context;
	// 装彩票的集合
	private List<Lottery> listLotterys;
	private MainActivity activity;
	private MyHandler myHandler;
	private Map<String, String> map = new HashMap<String, String>();

	/** 构造方法 实现初始化 */
	public LotteryTypeListAdapter(final Context context, List<Lottery> list) {
		this.context = context;
		this.listLotterys = list;
		activity = (MainActivity) context;
		myHandler = new MyHandler();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listLotterys.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listLotterys.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	/** 异步任务 用来后台获取数据 */
	class MyAsynTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {

			return AppTools.getDate(params[0], context);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if ("-500".equals(result)) {
				myHandler.sendEmptyMessage(-500);
				return;
			} else
				myHandler.sendEmptyMessage(1);
			super.onPostExecute(result);
		}
	}

	/** 处理页面显示的 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				activity.update();
				Log.i("x", "update");
				break;
			case -500:
				MyToast.getToast(context, "连接超时，请手动刷新获得数据").show();
				break;
			default:

				break;
			}
			super.handleMessage(msg);
		}
	}

	/** 获得视图 */
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			// 得到布局文件
			view = inflater.inflate(R.layout.lottery_type_items, null);
			// 得到控件
			holder.tv_lotteryName = (TextView) view
					.findViewById(R.id.lottery_tv_lotteryName);
			holder.lottery_tv_wintime = (TextView) view
					.findViewById(R.id.lottery_tv_wintime);
			holder.img_logo = (ImageView) view
					.findViewById(R.id.type_items_image);
			holder.tv_lotteryEndTime = (CustomDigitalClock) view
					.findViewById(R.id.lottery_tv_lotteryEnd);
			holder.tv_pk = (TextView) view.findViewById(R.id.lottery_tv_zqlq);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		view.setBackgroundResource(R.drawable.item_back_change);
		final Lottery lottery = listLotterys.get(position);

		// // 给控件赋值
		// holder.tv_pk.setText("");
		holder.tv_lotteryEndTime.setVisibility(View.GONE);
		// holder.tv_lotteryMoney.setText(lottery.getShowText());
		holder.tv_lotteryName.setText(lottery.getLotteryName());
		holder.lottery_tv_wintime.setText(lottery.getExplanation());

		holder.img_logo.setBackgroundResource(AppTools.allLotteryLogo
				.get(lottery.getLotteryID()));

		if (lottery.getEndtime() == null)
			holder.tv_lotteryEndTime.setVisibility(View.GONE);

		if (lottery.getEndtime() != null
				&& lottery.getDistanceTime() - System.currentTimeMillis() > 0) {
			holder.tv_lotteryEndTime.setMTickStop(false);
//			holder.tv_lotteryEndTime.setType(1);
			holder.tv_lotteryEndTime.setEndTime(lottery.getDistanceTime());
			holder.tv_lotteryEndTime.setVisibility(View.VISIBLE);
		} else {
//			if (lottery.getDistanceTime2() - System.currentTimeMillis() > 0) {
//				holder.tv_lotteryEndTime.setMTickStop(false);
//				holder.tv_lotteryEndTime.setType(2);
//				holder.tv_lotteryEndTime.setEndTime(lottery.getDistanceTime2());
//				holder.tv_lotteryEndTime.setVisibility(View.VISIBLE);
//			}
		}
		// 判断是否有期号 或 是足球是由有对阵
		if ("72".equals(lottery.getLotteryID())
				|| "73".equals(lottery.getLotteryID())) {
			holder.tv_lotteryEndTime.setVisibility(View.GONE);
			holder.tv_pk.setVisibility(View.VISIBLE);
			if (lottery.getDtmatch() == null
					|| lottery.getDtmatch().length() == 0) {
				holder.lottery_tv_wintime.setText(lottery.getExplanation());
				holder.tv_pk.setText("最高可得501万");
				view.setBackgroundResource(R.drawable.item_back_select2);
			} else {
				holder.tv_pk.setText(lottery.getDtmatch());
			}
		} else {
			holder.tv_pk.setVisibility(View.GONE);
			if (lottery.getIsuseName() == "" || lottery.getIsuseName() == null) {
				holder.lottery_tv_wintime.setText("没有期号");
				view.setBackgroundResource(R.drawable.item_back_select2);
			}
		}
		// 倒计时
		holder.tv_lotteryEndTime
				.setClockListener(new CustomDigitalClock.ClockListener() {
					@Override
					public void timeEnd() {
//						if (holder.tv_lotteryEndTime.getType() == 1) {
//							holder.tv_lotteryEndTime.setMTickStop(false);
//							holder.tv_lotteryEndTime.setType(2);
//							holder.tv_lotteryEndTime.setEndTime(lottery
//									.getDistanceTime2());
//						} else {
//							MyAsynTask mm = new MyAsynTask();
//							mm.execute(lottery.getLotteryID());
//						}
					}

					@Override
					public void remainFiveMinutes() {
						// The clock time is remain five minutes.
					}
				});
		if (!"0".equals(lottery.getEndtime()) && null != lottery.getEndtime()) {
			Date date = new Date(1);
			String nowTime = date.toString().substring(0, 10);
			String endTime = lottery.getEndtime().substring(0, 10);
			if (nowTime.equals(endTime)) {
				// holder.img_jiang.setBackgroundResource(R.drawable.kaijiang2);
			}
		}

		return view;
	}

	/** 静态类 */
	static class ViewHolder {
		RelativeLayout mian;
		TextView tv_lotteryName; // 彩种名
		TextView lottery_tv_wintime;// 开奖时间
		CustomDigitalClock tv_lotteryEndTime; // 截止时间
		TextView tv_lotteryQI; // 当前期
		TextView tv_lotteryMoney; // 奖池奖金
		ImageView img_logo; // 彩种图片
		ImageView img_jiang; // 今日开奖图片
		TextView tv_pk; // 最近比赛
	}

}
