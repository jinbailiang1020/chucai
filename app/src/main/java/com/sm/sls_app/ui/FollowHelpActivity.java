package com.sm.sls_app.ui;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.utils.AppTools;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

public class FollowHelpActivity extends Activity {

	private TextView tv_Help,tv_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_help_description);

		init();
	}

	
	
	
	private void init() 
	{
		tv_Help = (TextView) findViewById(R.id.number_sv_Help);
		tv_title= (TextView) findViewById(R.id.tv_title);
		String strFilePath = "help/00.txt";	
		String Play = ReadTxtFile(strFilePath);
		tv_Help.setText(Play);
	}

	public String ReadTxtFile(String StrFile) 
	{
		String res = "";
		try 
		{
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
		} 
		catch (Exception e) 
		{
			// TODO: handle exception
			Log.i("x", "错误"+e.getMessage());
		}
		return res;
	}

}
