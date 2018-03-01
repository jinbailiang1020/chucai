package com.sm.sls_app.ui;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.utils.AppTools;

/**
 * 玩法说明
 * 
 * @author Administrator
 * 
 */
public class PlayDescription extends Activity {
	private final static String TAG = "PlayDescription";
	private ImageButton btn_back; // 返回
	private ImageView iv_up_down;// 玩法提示图标
	private Button btn_playtype;// 玩法
	private ImageButton btn_help;// 帮助
	private TextView tv_Play;
	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_play_description);
		init();
	}

	class MyAsynTask extends AsyncTask<Void, Integer, String> {
		private String Message;

		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			String key = MD5.md5(AppTools.MD5_key);

			String info = "";

			String time = RspBodyBaseBean.getTime();

			String imei = RspBodyBaseBean.getIMEI(PlayDescription.this);

			String crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");

			String auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");

			String opt = "52";

			String[] values = { opt, auth, info };

			String result = HttpUtils.doPost(AppTools.names, values,
					AppTools.path);

			try {
				JSONObject object = new JSONObject(result);
				String error = object.optString("error");
				String serverTime = object.optString("serverTime");
				String investAgreement = null;
				try {
					investAgreement = URLDecoder.decode(
							object.optString("investAgreement"), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message = Html.fromHtml(investAgreement).toString();

				return error;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			switch (Integer.parseInt(result)) {
			case 0:
				tv_Play.setText(Message);
				break;
			default:
				break;
			}

		}

	}

	private void init() {
		type = getIntent().getIntExtra("type", 1);
		tv_Play = (TextView) findViewById(R.id.number_sv_Play);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		iv_up_down = (ImageView) findViewById(R.id.iv_up_down);
		btn_playtype = (Button) findViewById(R.id.btn_playtype);
		btn_help = (ImageButton) findViewById(R.id.btn_help);
		btn_help.setVisibility(View.GONE);
		iv_up_down.setVisibility(View.GONE);
		btn_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				PlayDescription.this.finish();// 返回
			}
		});
		String LotterID = AppTools.lottery.getLotteryID();
		String lotteryName = AppTools.lottery.getLotteryName();
		String strFilePath = "";
		if (type == 0) {
			btn_playtype.setText("委托投注规则");
			MyAsynTask myAsynTask = new MyAsynTask();
			myAsynTask.execute();
		} else {
			btn_playtype.setText(lotteryName + "玩法说明");
			strFilePath = "play/" + LotterID + ".txt";
			String Play = ReadTxtFile(strFilePath);
			tv_Play.setText(Play);
		}
	}

	public String ReadTxtFile(String StrFile) {
		String res = "";
		try {
			// 读取raw文件夹中的txt文件,将它放入输入流中
			// InputStream in = getResources().openRawResource(R.raw.ansi);
			// 读取assets文件夹中的txt文件,将它放入输入流中
			InputStream in = getResources().getAssets().open(StrFile);
			// 获得输入流的长度
			int length = in.available();
			// 创建字节输入
			byte[] buffer = new byte[length];
			// 放入字节输入中
			in.read(buffer);
			// 获得编码格式
			res = EncodingUtils.getString(buffer, "UTF-8");
			// 关闭输入流
			in.close();
		} catch (Exception e) {
			// TODO: handle exception
			Log.i("x", "错误" + e.getMessage());
		}
		return res;
	}

}
