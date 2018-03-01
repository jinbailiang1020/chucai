package com.sm.sls_app.ui.adapter;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.Lottery;
import com.sm.sls_app.ui.MainActivity;
import com.sm.sls_app.ui.adapter.LotteryTypeListAdapter.MyAsynTask;
import com.sm.sls_app.ui.adapter.LotteryTypeListAdapter.MyHandler;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.view.CustomDigitalClock;
import com.sm.sls_app.view.MyToast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;

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

public class GridView_HallLotteryAdapter extends BaseAdapter {

	private final static String TAG = "GridView_HallLotteryAdapter";
	// 上下文本
	private Context context;
	// 装彩票的集合
	private List<Lottery> listLotterys;
	private MainActivity activity;
	private MyHandler myHandler;

	// 存放选球
	public static List<String> oneSet = new ArrayList<String>();

	public GridView_HallLotteryAdapter(Context context,
			List<Lottery> listLotterys) {
		this.context = context;
		activity = (MainActivity) context;
		this.listLotterys = listLotterys;
		myHandler = new MyHandler();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (0 == listLotterys.size() % 2) {
			return listLotterys.size();
		} else {
			return listLotterys.size() + 1;
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listLotterys.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			// 得到布局文件
			view = inflater.inflate(R.layout.gridview_item_hall, null);
			// 得到控件
			holder.iv_lottery = (ImageView) view.findViewById(R.id.iv_lottery);
			holder.tv_hall_lotteryname = (TextView) view
					.findViewById(R.id.tv_hall_lotteryname);
			holder.tv_hall_describ = (TextView) view
					.findViewById(R.id.tv_hall_describ);
			holder.cdc_hall_time = (CustomDigitalClock) view
					.findViewById(R.id.cdc_hall_time);
			holder.tv_hall_dm = (TextView) view.findViewById(R.id.tv_hall_dm);
			holder.iv_hall_jiajiang = (TextView) view
					.findViewById(R.id.iv_hall_jiajiang);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		if (0 == listLotterys.size() % 2) {
			final Lottery lottery = listLotterys.get(position);
			// // 给控件赋值
			holder.cdc_hall_time.setVisibility(View.GONE);
			holder.tv_hall_lotteryname.setText(lottery.getLotteryName());
			holder.iv_lottery.setBackgroundResource(AppTools.allLotteryLogo
					.get(lottery.getLotteryID()));

			if (lottery.getEndtime() == null)
				holder.cdc_hall_time.setVisibility(View.GONE);
			holder.tv_hall_describ.setText(lottery.getLotteryDescription());
			// 判断是否有期号 或 是足球是由有对阵
			if ("72".equals(lottery.getLotteryID())
					|| "73".equals(lottery.getLotteryID())) {
				holder.tv_hall_dm.setVisibility(View.VISIBLE);
				holder.tv_hall_dm.setText(lottery.getLotteryAgainst());
			} else {
				if (lottery.getEndtime() != null
						&& lottery.getDistanceTime()
								- System.currentTimeMillis() > 0) {
					holder.cdc_hall_time.setMTickStop(false);
					// holder.cdc_hall_time.setType(1);
					holder.cdc_hall_time.setEndTime(lottery.getDistanceTime());
					holder.cdc_hall_time.setVisibility(View.VISIBLE);
				}
				// else {
				// if (lottery.getDistanceTime2() - System.currentTimeMillis() >
				// 0) {
				// holder.cdc_hall_time.setMTickStop(false);
				// // holder.cdc_hall_time.setType(2);
				// holder.cdc_hall_time.setEndTime(lottery
				// .getDistanceTime2());
				// holder.cdc_hall_time.setVisibility(View.VISIBLE);
				// }
				// }
				holder.tv_hall_dm.setVisibility(View.GONE);
			}
			// 倒计时
			holder.cdc_hall_time
					.setClockListener(new CustomDigitalClock.ClockListener() {
						@Override
						public void timeEnd() {
							// if (holder.cdc_hall_time.getType() == 1) {
							// holder.cdc_hall_time.setMTickStop(false);
							// holder.cdc_hall_time.setType(2);
							// holder.cdc_hall_time.setEndTime(lottery
							// .getDistanceTime2());
							// } else {
							MyAsynTask mm = new MyAsynTask();
							mm.execute(lottery.getLotteryID());
							// }
						}

						@Override
						public void remainFiveMinutes() {
							// The clock time is remain five minutes.
						}
					});
		} else {
			if (listLotterys.size() != position) {
				final Lottery lottery = listLotterys.get(position);
				// // 给控件赋值
				holder.cdc_hall_time.setVisibility(View.GONE);
				holder.tv_hall_lotteryname.setText(lottery.getLotteryName());
				holder.iv_lottery.setBackgroundResource(AppTools.allLotteryLogo
						.get(lottery.getLotteryID()));

				if (lottery.getEndtime() == null)
					holder.cdc_hall_time.setVisibility(View.GONE);
				holder.tv_hall_describ.setText(lottery.getLotteryDescription());
				// 判断是否有期号 或 是足球是由有对阵
				if ("72".equals(lottery.getLotteryID())
						|| "73".equals(lottery.getLotteryID())) {
					holder.tv_hall_dm.setVisibility(View.VISIBLE);
					holder.tv_hall_dm.setText(lottery.getLotteryAgainst());
				} else {
					if (lottery.getEndtime() != null
							&& lottery.getDistanceTime()
									- System.currentTimeMillis() > 0) {
						holder.cdc_hall_time.setMTickStop(false);
						// holder.cdc_hall_time.setType(1);
						holder.cdc_hall_time.setEndTime(lottery
								.getDistanceTime());
						holder.cdc_hall_time.setVisibility(View.VISIBLE);
					}
					// else {
					// if (lottery.getDistanceTime2()
					// - System.currentTimeMillis() > 0) {
					// holder.cdc_hall_time.setMTickStop(false);
					// holder.cdc_hall_time.setType(2);
					// holder.cdc_hall_time.setEndTime(lottery
					// .getDistanceTime2());
					// holder.cdc_hall_time.setVisibility(View.VISIBLE);
					// }
					// }
					holder.tv_hall_dm.setVisibility(View.GONE);
				}
				// 倒计时
				holder.cdc_hall_time
						.setClockListener(new CustomDigitalClock.ClockListener() {
							@Override
							public void timeEnd() {
								// if (holder.cdc_hall_time.getType() == 1) {
								// holder.cdc_hall_time.setMTickStop(false);
								// holder.cdc_hall_time.setType(2);
								// holder.cdc_hall_time.setEndTime(lottery
								// .getDistanceTime2());
								// } else {
								MyAsynTask mm = new MyAsynTask();
								mm.execute(lottery.getLotteryID());
								// }
							}

							@Override
							public void remainFiveMinutes() {
								// The clock time is remain five minutes.
							}
						});
			} else {
				holder.cdc_hall_time.setVisibility(View.GONE);
				holder.tv_hall_lotteryname.setText("");
				holder.iv_lottery
						.setBackgroundResource(R.drawable.selector_hall_gvitem);
				holder.tv_hall_describ.setText("");
				holder.tv_hall_dm.setVisibility(View.GONE);
			}

		}
		return view;
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

	/** 静态类 */
	static class ViewHolder {
		ImageView iv_lottery; // 彩种图片
		TextView tv_hall_lotteryname; // 彩种名
		TextView tv_hall_describ;// 彩种描述
		CustomDigitalClock cdc_hall_time; // 截止时间
		TextView tv_hall_dm; // 最近比赛
		TextView iv_hall_jiajiang; // 加奖
	}
}
