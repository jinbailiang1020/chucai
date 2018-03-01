package com.sm.sls_app.ui;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.Schemes;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.ui.adapter.MyFollowAdapter;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.view.MyListView;
import com.sm.sls_app.view.MyToast;

public class FollowProgress {

	private View view;
	private MyListView listView;
	private MyFollowAdapter fAdapter;
	private Context context;
	private MyHandler myHandler;
	private MyAsynTask myAsynTask;
	
	private List<Schemes> listSchemes = new ArrayList<Schemes>();
	
	private String opt = "14", auth, info, time, imei, crc, key; // 格式化后的参数

	private int size = 30;
	private int pageIndex = 1; // 页码
	private int pageSize = size; // 每页显示行数
	private int sort = 5; // 排序方式
	private int isPurchasing = 3; // 返回类型
	
	private String lotteryId;
	
	/** 要更改的 **/
	private LinearLayout ll;
	private ProgressBar pb;
	private int isEnd = 0;
	
	// 最后可见条目的索引
	private int lastVisibleIndex;
	
	public  FollowProgress(Context context,View v)  
	{
		this.view = v;
		this.context = context;
		myHandler = new MyHandler();
		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(context);
		key = MD5.md5( AppTools.MD5_key );
		
		listView = (MyListView) view.findViewById(R.id.follow_listView);
		
		fAdapter = new MyFollowAdapter(context);
		fAdapter.setList(listSchemes);
		
		/** 要更改的 新加的加载图片 **/
		ll = new LinearLayout(context);
		pb = new ProgressBar(context);
		ll.setGravity(Gravity.CENTER);
		ll.addView(pb);
		
		
		listView.setAdapter(fAdapter);
		listView.setOnItemClickListener(new MyListItemClick());
	}
	
	public void doBack(String lotteryId)
	{
		listSchemes.clear();
		listView.addFooterView(ll);
		this.lotteryId = lotteryId;
		myAsynTask = new MyAsynTask();
		myAsynTask.execute();
	}
	
	
	/** listView Item 点击事件 */
	class MyListItemClick implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {

			if (fAdapter.getList().size() != 0) 
			{
				Intent intent = new Intent(context, FollowInfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("schem",
						fAdapter.getList().get(position - 1));
				intent.putExtra("bundle", bundle);
				context.startActivity(intent);
			} 
			else
			{
				System.out.println("FollowSchemeActivity点击了。。。。");
			}
		}
	}
	
	/** 异步任务 用来后台获取数据 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> {
		String error ="-1001";
		@Override
		protected String doInBackground(Void... params) {
			info = RspBodyBaseBean.changeJoin_info(lotteryId, pageIndex,
					pageSize, sort, 0);
			Log.i("x", "Follow---info---" + info);

			crc = RspBodyBaseBean.getCrc(time, imei, MD5.md5(AppTools.MD5_key),
					info, "0");
			auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");

			String[] values = { opt, auth, info };

			try 
			{
				String result = HttpUtils.doPost(AppTools.names, values,AppTools.path);
				System.out.println("  FollowSchemeActivity---result:=="
						+ result);
				if ("-500".equals(result))
					return "-500";
				if (null != result) 
				{
					JSONObject object = new JSONObject(result);
					if ("0".equals(object.opt("error"))) 
					{
						JSONArray array = new JSONArray(
								object.optString("schemeList"));

						Schemes schemes = null;
						for (int i = 0; i < array.length(); i++)
						{
							if (myAsynTask.isCancelled()) 
							{
								Log.i("x", "取消了");
								return null;
							}

							JSONObject items = array.getJSONObject(i);
							
							Log.i("x", "合买======"+items);

							schemes = new Schemes();
							schemes.setId(items.optString("Id"));
							schemes.setLotteryID(items.getString("lotteryID"));
							schemes.setLotteryName(items
									.getString("lotteryName"));
							schemes.setInitiateUserID(items
									.optString("initiateUserID"));
							schemes.setInitiateName(items
									.optString("initiateName"));
							schemes.setMoney(items.optLong("money"));
							schemes.setShareMoney(items.optInt("shareMoney"));
							schemes.setShare(items.optInt("share"));

							schemes.setSurplusShare(items
									.optInt("surplusShare"));
							schemes.setMultiple(items.optInt("multiple"));

							schemes.setAssureShare(items.optInt("assureShare"));
							schemes.setAssureMoney(items
									.optDouble("assureMoney"));
							schemes.setSchemeBonusScale(items
									.optDouble("schemeBonusScale"));
							schemes.setSecrecyLevel(items
									.optInt("secrecyLevel"));
							schemes.setQuashStatus(items.optInt("quashStatus"));
							schemes.setLevel(items.optInt("level"));
							schemes.setWinMoneyNoWithTax(items
									.optDouble("winMoneyNoWithTax"));
							schemes.setSchedule(items.optInt("schedule"));
							schemes.setSchemeIsOpened(items
									.optString("schemeIsOpened"));
							schemes.setTitle(items.getString("title"));
							schemes.setLotteryNumber(items
									.optString("lotteryNumber"));

							schemes.setPlayTypeID(items.optInt("PlayTypeID"));
							
							schemes.setPlayTypeName(items.optString("playTypeName"));
							schemes.setSerialNumber(items
									.getInt("SerialNumber"));
							schemes.setRecordCount(items.optInt("RecordCount"));
							
							schemes.setDescription(items.getString("description"));

							this.publishProgress();

							listSchemes.add(schemes);
						}
						
						if (myAsynTask.isCancelled()) 
						{
							return null;
						}
						error = "0";
					}
				} 
				else 
				{
					Log.i("x", "result == null");
					return "-1001";
				}
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("FlooleFragment 错误" + e.getMessage());
				return "-1001";
			}
			return error;
		}
		
		/** 任务结束时执行 */
		@Override
		protected void onPostExecute(String result)
		{
			// TODO Auto-generated method stub
			myHandler.sendEmptyMessage(Integer.parseInt(result));
			super.onPostExecute(result);
		}
	}
	
	/** 处理页面显示的 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				listView.onRefreshComplete();
				fAdapter.setList(listSchemes);
				fAdapter.notifyDataSetChanged();
				if(listSchemes.size() < 30)
					listView.removeFooterView(ll);
				break;
			case 2:
				listSchemes.clear();
				fAdapter.clear();
				fAdapter.notifyDataSetChanged();
				break;
			case -500:
				MyToast.getToast(context, "网络不稳定，连接超时.").show();
				listView.removeFooterView(ll);
				break;
			default:
				MyToast.getToast(context, "系统异常").show();
				listView.removeFooterView(ll);
				break;
			}
			super.handleMessage(msg);
		}
	}
	
	
}
