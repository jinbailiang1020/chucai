package com.sm.sls_app.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class MyToast {

	private static Toast toast = null;

	@SuppressLint("ShowToast")
	public static Toast getToast(Context context, String msg) 
	{
		if (toast == null) {
			toast = Toast.makeText(context, msg, 1000);
			toast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			toast.setText(msg);
		}

		return toast;
	}
}
