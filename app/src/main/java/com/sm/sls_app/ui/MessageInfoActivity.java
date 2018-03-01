package com.sm.sls_app.ui;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.MyMessage;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.utils.AppTools;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

/** 站内信详情 **/
public class MessageInfoActivity extends Activity implements OnClickListener {

	private String opt = "28"; // 格式化后的 opt
	private String auth, info, time, imei, crc; // 格式化后的参数
	private MyMessage m;
	private ImageButton btn_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_msg_info);
		super.onCreate(savedInstanceState);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		TextView tv_title = (TextView) this.findViewById(R.id.tv_title);
		TextView tv_content = (TextView) this.findViewById(R.id.tv_content);
		btn_back.setOnClickListener(this);
		m = StationMessageActivity.mes;
		if (null != m) {
			tv_title.setText(m.getTitle());
			tv_content.setText("   " + m.getContent());
			if (!m.isRead()) {
				MyAsynTask myAsynTask = new MyAsynTask();
				myAsynTask.execute();
			}
		}
	}

	/*** 异步任务 用来后台获取数据 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> {
		/** 在后台执行的程序 */
		@Override
		protected String doInBackground(Void... params) {
			time = RspBodyBaseBean.getTime();
			imei = RspBodyBaseBean.getIMEI(MessageInfoActivity.this);
			info = RspBodyBaseBean.changeMsg_info(m.getId(), 0);
			String key = MD5
					.md5(AppTools.user.getUserPass() + AppTools.MD5_key);
			crc = RspBodyBaseBean.getCrc(time, imei, key, info,
					AppTools.user.getUid());
			auth = RspBodyBaseBean.getAuth(crc, time, imei,
					AppTools.user.getUid());
			String values[] = { opt, auth, info };
			String result = HttpUtils.doPost(AppTools.names, values,
					AppTools.path);
			Log.i("x", "更改状态---result--" + result);
			m.setRead(true);

			return null;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		}
	}
}
