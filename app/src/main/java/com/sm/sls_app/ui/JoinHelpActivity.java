package com.sm.sls_app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.sm.sls_app.activity.R;

/**
 * 合买帮助
 * @author SLS003
 */
public class JoinHelpActivity extends Activity {
	private ImageButton btn_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.join_help);
		btn_back=(ImageButton) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				JoinHelpActivity.this.finish();
			}
		});
	}

}
