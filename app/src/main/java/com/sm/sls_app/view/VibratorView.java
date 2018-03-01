package com.sm.sls_app.view;

import com.sm.sls_app.utils.AppTools;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
public class VibratorView{
	private static  Vibrator vibrator;
	
	public static void init(Context context){
		SharedPreferences settings = context.getSharedPreferences("app_user", 0);
		AppTools.isVibrator = settings.getBoolean("isVibrator", true);
	}
	
	public static synchronized Vibrator getVibrator(Context context)
	{
		init(context);
		if(!AppTools.isVibrator)
		{
			return null;
		}
		if(null == vibrator){
			vibrator = (Vibrator) context
					.getSystemService(Context.VIBRATOR_SERVICE);
		}
		return vibrator;
	}

}
