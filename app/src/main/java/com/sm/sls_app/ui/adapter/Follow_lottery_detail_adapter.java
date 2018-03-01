package com.sm.sls_app.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.Schemes;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.ui.MyFollowLotteryInfo;
import com.sm.sls_app.ui.adapter.CenterLotteryAdapter.ViewHolder2;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.BaseHelper;
import com.sm.sls_app.utils.StringUtils;
import com.sm.sls_app.view.MyToast;
import com.umeng.socialize.facebook.controller.utils.ToastUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Follow_lottery_detail_adapter extends BaseAdapter {
	private List<Schemes> myList;
	private int id = -1;
	private Context mContext;
	private int type = 2; // 1显示彩种名字 2显示期号
	private MyHandler myHandler;
	private MyAsyncTask myAsyncTask;
	private String opt = "21";// 撤单接口
	private String auth, info, time, imei, crc;

	public Follow_lottery_detail_adapter(Context context, List<Schemes> aList,
			int type) {
		this.mContext = context;
		setData(aList);
	}

	public void setData(List<Schemes> aList) {
		if (myList == null)
			myList = new ArrayList<Schemes>();
		myList.clear();
		for (Schemes schemes : aList) {
			myList.add(schemes);
		}
	}

	@Override
	public int getCount() {
		return myList.size();
	}

	@Override
	public Object getItem(int position) {
		return myList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder2 holder;
		if (null == convertView) {
			LayoutInflater inflact = LayoutInflater.from(mContext);
			convertView = inflact.inflate(
					R.layout.follow_lottery_detail_adapter_item, null);
			holder = new ViewHolder2();
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_lotteryName);
			holder.tv_money = (TextView) convertView
					.findViewById(R.id.tv_money);
			holder.tv_type = (TextView) convertView
					.findViewById(R.id.tv_playType);
			holder.tv_win = (TextView) convertView.findViewById(R.id.tv_isWin);
			holder.tv_id = (TextView) convertView.findViewById(R.id.tv_id);
			holder.img_logo = (ImageView) convertView
					.findViewById(R.id.img_win);
			holder.revoke = (Button) convertView.findViewById(R.id.bt_revoke);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder2) convertView.getTag();
		}

		holder.tv_id.setText(id + "");
		holder.tv_id.setVisibility(View.GONE);
		holder.img_logo.setVisibility(View.GONE);
		holder.revoke.setVisibility(View.GONE);

		Schemes scheme = myList.get(position);
		if (type == 2)
			holder.tv_name.setText("第" + scheme.getIsuseName() + "期");
		else {
			holder.tv_name.setText(scheme.getLotteryName());
		}
		holder.tv_money.setText(scheme.getMoney() + "元");

		if ("False".equals(scheme.getIsPurchasing())) {
			holder.tv_type.setText("合买订单");
		} else if ("True".equals(scheme.getIsPurchasing())) {
			if (scheme.getIsChase() == 0)
				holder.tv_type.setText("普通订单");
			else
				holder.tv_type.setText("追号订单");
		}
		if (type == 2) {
			if (0 != scheme.getQuashStatus()) {
				holder.tv_win.setText("已撤单");
			} else if (!scheme.isExecuted()) {
				holder.tv_win.setText("追号中");
				if (!StringUtils.isEmpty(AppTools.user.getUid())) {
					if (AppTools.user.getUid().equals(
							scheme.getInitiateUserID())) {
						holder.revoke.setVisibility(View.VISIBLE);
					}
				}
			} else {
				if ("False".equals(scheme.getSchemeIsOpened())) {
					holder.tv_win.setText("未开奖");
					holder.tv_win.setTextColor(Color.BLACK);
				} else if ("True".equals(scheme.getSchemeIsOpened())) {
					if (scheme.getWinMoneyNoWithTax() > 0) {
						holder.tv_win.setText("中奖"
								+ scheme.getWinMoneyNoWithTax() + "元");
						// 设置中奖颜色为红色
						holder.tv_win.setTextColor(Color.RED);
						holder.img_logo.setVisibility(View.VISIBLE);
					} else {
						holder.tv_win.setText("未中奖");
						holder.tv_win.setTextColor(Color.BLACK);
					}
				}
			}
		} else {
			if (0 != scheme.getQuashStatus()) {
				holder.tv_win.setText("已撤单");
				holder.tv_win.setTextColor(Color.BLACK);
			} else if ("False".equals(scheme.getSchemeIsOpened())) {
				holder.tv_win.setText("未开奖");
				holder.tv_win.setTextColor(Color.BLACK);
			} else if ("True".equals(scheme.getSchemeIsOpened())) {
				if (scheme.getWinMoneyNoWithTax() > 0) {
					holder.tv_win.setText("中奖" + scheme.getWinMoneyNoWithTax()
							+ "元");
					// 设置中奖颜色为红色
					holder.tv_win.setTextColor(Color.RED);
					holder.img_logo.setVisibility(View.VISIBLE);
				} else {
					holder.tv_win.setText("未中奖");
					holder.tv_win.setTextColor(Color.BLACK);
				}
			}
		}
		holder.revoke.setOnClickListener(new MyClickListener(scheme));
		return convertView;
	}

	class MyClickListener implements OnClickListener {
		private Schemes schemes;

		public MyClickListener(Schemes schemes) {
			this.schemes = schemes;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bt_revoke:
				if (type == 2) {
					// ToastUtil.showToast(mContext, "打算撤销" + schemes.getId());
					myAsyncTask = new MyAsyncTask(schemes);
					myAsyncTask.execute();
				} else {
					// ToastUtil.showToast(mContext, "打算撤销" + schemes.getId());
					myAsyncTask = new MyAsyncTask(schemes);
					myAsyncTask.execute();
				}
				break;

			default:
				break;
			}
		}

	}

	class MyAsyncTask extends AsyncTask<Void, Integer, String> {
		String error = "0";
		private Schemes schemes;

		public MyAsyncTask(Schemes schemes) {
			this.schemes = schemes;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(Void... params) {
			String key = MD5
					.md5(AppTools.user.getUserPass() + AppTools.MD5_key);
			info = "{\"chaseDetailId\":\"" + schemes.getId() + "\"}";
			if (time == null)
				time = RspBodyBaseBean.getTime();
			if (imei == null)
				imei = RspBodyBaseBean.getIMEI(mContext);
			Log.i("x", "  info  " + info);
			crc = RspBodyBaseBean.getCrc(time, imei, key, info,
					AppTools.user.getUid());
			auth = RspBodyBaseBean.getAuth(crc, time, imei,
					AppTools.user.getUid());
			String values[] = { opt, auth, info };
			String result = HttpUtils.doPost(AppTools.names, values,
					AppTools.path);
			/**
			 * {"error":"0","msg":"","serverTime":"2015-12-14 11:09:37",
			 * "msgCountAll":"0","msgCount":"0"}"
			 */
			Log.i("x", "追号详情内容      " + result);
			if ("-500".equals(result))
				return result;

			if (result.length() == 0) {
				// Log.i("x", "没有得到数据--！");
				return "-1";
			}
			try {
				JSONObject object = new JSONObject(result);

				error = object.getString("error");
				if (!"0".equals(error))
					return error;
			} catch (Exception e) {
				// TODO: handle exception
			}

			return error;
		}

		@Override
		protected void onPostExecute(String result) {
			myHandler = new MyHandler(schemes);
			myHandler.sendEmptyMessage(Integer.parseInt(result));
		}
	}

	class MyHandler extends Handler {
		private Schemes schemes;

		public MyHandler(Schemes schemes) {
			this.schemes = schemes;
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case -1:
				MyToast.getToast(mContext, "没有数据").show();
				break;
			case 0:
				MyToast.getToast(mContext, "撤单成功").show();
				schemes.setQuashStatus(1);
				notifyDataSetChanged();
				break;
			case 500:
				MyToast.getToast(mContext, "连接超时").show();
				break;
			}
		}
	}
}
