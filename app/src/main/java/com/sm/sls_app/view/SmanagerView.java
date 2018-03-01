package com.sm.sls_app.view;

import com.sm.sls_app.utils.AppTools;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**自定义传感器类**/
public class SmanagerView {

	public static void init(Context context)
	{	//设置是否启动摇一摇功能
		SharedPreferences settings = context
				.getSharedPreferences("app_user", 0);
		AppTools.isSensor = settings.getBoolean("isSensor", true);
	}

	public synchronized static void registerSensorManager(
			SensorManager sManager, Context mContext,
			SensorEventListener sListener)
	{
		init(mContext);
		if (!AppTools.isSensor) 
		{
			return;
		}
		sManager.registerListener(sListener,
				sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);

	}

	public synchronized static void unregisterSmanager(SensorManager sManager,
			SensorEventListener sListener)
	{
		if (null != sManager) 
		{
			sManager.unregisterListener(sListener,
					sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
			sManager.unregisterListener(sListener,
					sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION));
		}
	}

}
