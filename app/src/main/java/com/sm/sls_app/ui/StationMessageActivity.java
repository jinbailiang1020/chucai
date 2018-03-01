package com.sm.sls_app.ui;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.MyMessage;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.view.MyToast;

/** 站内信 **/
public class StationMessageActivity extends Activity implements
		OnClickListener, OnScrollListener {

	private String opt = "16"; // 格式化后的 opt
	private String auth, info, time, imei, crc; // 格式化后的参数
	private ImageButton btn_back;
	private Button btn_sys, btn_mine;

	private ListView mListView;
	private MyAdapter mAdapter;

	public static MyMessage mes = null;

	private int type = 2; // 默认请求个人消息
	/** 2是个人信息 1 是系统信息 */

	private int pageSize = 20;

	private int pageIndex = 1;

	private List<MyMessage> listMessage = new ArrayList<MyMessage>();;

	private MyAsynTask myAsynTask;

	private MyHandler myHandler;

	private int lastVisibleIndex;
	/** 要更改的 listView foodview image **/
	private LinearLayout ll;
	private ProgressBar pb;

	private int isEnd = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statiion_msg);
		findView();
		init();
	}

	private void findView() {
		// TODO Auto-generated method stub
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		mListView = (ListView) this.findViewById(R.id.msg_listView);
		btn_sys = (Button) this.findViewById(R.id.btn_sys_msg);
		btn_mine = (Button) this.findViewById(R.id.btn_mine_msg);

		/** 要更改的 新加的加载图片 **/
		ll = new LinearLayout(StationMessageActivity.this);
		pb = new ProgressBar(StationMessageActivity.this);

		// Drawable d = this.getResources().getDrawable(R.drawable.progress);
		// Canvas can = new Canvas();
		// can.rotate(0.5f, 0.5f, 0.5f);
		// d.draw(can);
		//
		// pb.setIndeterminateDrawable(d);

		ll.setGravity(Gravity.CENTER);
		ll.addView(pb);

		mListView.addFooterView(ll);

		mAdapter = new MyAdapter(listMessage);
		mListView.setAdapter(mAdapter);
		btn_back.setOnClickListener(this);
		btn_sys.setOnClickListener(this);
		btn_mine.setOnClickListener(this);
		mListView.setOnScrollListener(this);
		mListView.setOnItemClickListener(new MyItemClick());
	}

	/** listView item点击监听 **/
	class MyItemClick implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			mes = mAdapter.listm.get(position);
			Intent intent = new Intent(StationMessageActivity.this,
					MessageInfoActivity.class);
			StationMessageActivity.this.startActivity(intent);
		}
	}

	/** 初始化 **/
	private void init() {
		myHandler = new MyHandler();
		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(StationMessageActivity.this);
		myAsynTask = new MyAsynTask();
		myAsynTask.execute();
	}

	@Override
	public void onClick(View v) {
		if (null != myAsynTask && myAsynTask.getStatus() == Status.RUNNING) {
			myAsynTask.cancel(true);
		}
		if (v.getId() == R.id.btn_back) {
			finish();
			return;
		}
		if (null != mAdapter) {
			mAdapter.clear();
			mAdapter.notifyDataSetChanged();
		}
		mListView.removeFooterView(ll);
		mListView.addFooterView(ll);
		listMessage.clear();
		pageIndex = 1;
		switch (v.getId()) {
		case R.id.btn_sys_msg:
			changeBtnBack(btn_sys);
			type = 1;
			break;
		case R.id.btn_mine_msg:
			changeBtnBack(btn_mine);
			type = 2;
			break;
		}
		myAsynTask = new MyAsynTask();
		myAsynTask.execute();
	}

	/** 改变Button背景 **/
	private void changeBtnBack(Button btn) {
		btn_sys.setBackgroundResource(R.drawable.btn_playinfo);
		btn_mine.setBackgroundResource(R.drawable.btn_playinfo);
		btn.setBackgroundResource(R.drawable.btn_playinfo_select);
	}

	/** ListView Adapter **/
	class MyAdapter extends BaseAdapter {
		private List<MyMessage> listm;

		public MyAdapter(List<MyMessage> list) {
			setList(list);
		}

		public void clear() {
			this.listm.clear();
		}

		/** 给数组赋值 **/
		public void setList(List<MyMessage> list) {
			this.listm = new ArrayList<MyMessage>();
			for (int i = 0; i < list.size(); i++) {
				this.listm.add(list.get(i));
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listm.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listm.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (null == convertView) {
				holder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater
						.from(StationMessageActivity.this);
				// 得到布局文件
				convertView = inflater.inflate(R.layout.message_item, null);
				holder.tv_title = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.tv_content = (TextView) convertView
						.findViewById(R.id.tv_content);
				holder.tv_time = (TextView) convertView
						.findViewById(R.id.tv_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_content.getPaint().setFakeBoldText(false);
			holder.tv_title.getPaint().setFakeBoldText(false);
			holder.tv_time.getPaint().setFakeBoldText(false);

			if (!listm.get(position).isRead()) {
				holder.tv_content.getPaint().setFakeBoldText(true);
				holder.tv_title.getPaint().setFakeBoldText(true);
				holder.tv_time.getPaint().setFakeBoldText(true);
			}

			holder.tv_content.setText(listm.get(position).getContent());
			holder.tv_time.setText(listm.get(position).getTime());
			holder.tv_title.setText(listm.get(position).getTitle());
			return convertView;
		}
	}

	static class ViewHolder {
		TextView tv_content, tv_time, tv_title;
	}

	/*** 异步任务 用来后台获取数据 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> {
		/** 在后台执行的程序 */
		@Override
		protected String doInBackground(Void... params) {
			String error = "-1";
			String key = MD5
					.md5(AppTools.user.getUserPass() + AppTools.MD5_key);
			info = RspBodyBaseBean
					.changeMsg_info(type, pageIndex, pageSize, -1);

			Log.i("x", "info-----" + info);
			crc = RspBodyBaseBean.getCrc(time, imei, key, info,
					AppTools.user.getUid());
			auth = RspBodyBaseBean.getAuth(crc, time, imei,
					AppTools.user.getUid());
			String value[] = { opt, auth, info };
			String result = HttpUtils.doPost(AppTools.names, value,
					AppTools.path);
			Log.i("x", "信息----------" + result);

			if ("-500".equals(result))
				return "-500";
			try {
				JSONObject object = new JSONObject(result);
				String smsList = object.getString("stationSMSList");
				JSONArray array = new JSONArray(smsList);
				error = object.getString("error");
				MyMessage msg = null;
				Log.i("x", "array-------" + array);
				isEnd = array.length();
				for (int i = 0; i < array.length(); i++) {
					JSONObject item = array.getJSONObject(i);
					Log.i("x", "item-----------" + item);
					msg = new MyMessage();
					msg.setId(item.optInt("Id"));
					msg.setContent(item.getString("content"));
					msg.setTitle(item.getString("title"));
					msg.setRead(item.optBoolean("isRead"));
					msg.setRecordCount(item.getLong("RecordCount"));
					msg.setSerialNumber(item.getString("SerialNumber"));
					msg.setTime(item.getString("dateTime"));

					for (MyMessage m : listMessage) {
						if (m.getId() == msg.getId()) {
							listMessage.remove(m);
							isEnd--;
						}
					}
					listMessage.add(msg);
				}
				if (myAsynTask.isCancelled()) {
					return null;
				}
			} catch (Exception e) {
				Log.i("x", "拿站内信错误->" + e.getMessage());
			}
			return error;
		}

		@Override
		protected void onPostExecute(String result) {
			ll.setVisibility(View.GONE);
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
			case AppTools.ERROR_SUCCESS:
				Log.i("x", "isEnd----" + isEnd);
				// MyToast.getToast(StationMessageActivity.this, "成功").show();
				if (0 == listMessage.size()) {
					mListView.removeFooterView(ll);
					MyToast.getToast(StationMessageActivity.this, "没有消息")
							.show();
					return;
				}
				if (0 == isEnd) {
					mListView.removeFooterView(ll);
				}
				ll.setVisibility(View.GONE);
				mAdapter.setList(listMessage);
				mAdapter.notifyDataSetChanged();

				Log.i("x", "刷新ListAdapter" + listMessage.size());
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		// 滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& lastVisibleIndex == mAdapter.getCount() && isEnd != 0) {
			ll.setVisibility(View.VISIBLE);
			pageIndex++;
			myAsynTask = new MyAsynTask();
			myAsynTask.execute();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		// 计算最后可见条目的索引
		lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.i("x", "刷新了------");
		mAdapter.notifyDataSetChanged();
		super.onResume();
	}

}
