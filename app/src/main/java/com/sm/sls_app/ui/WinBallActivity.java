package com.sm.sls_app.ui;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.utils.App;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class WinBallActivity extends Activity {

	private ExpandableListView elistView;
	private TextView tv_name1,tv_name2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_win_ball);
		
		App.activityS.add(this);
		
		init();

		findView();

		setListener();
	}

	/**初始化属性*/
	private void init() {
		// TODO Auto-generated method stub

	}

	/**初始化UI*/
	private void findView() {
		// TODO Auto-generated method stub
		elistView = (ExpandableListView) findViewById(R.id.win_ball_exListView);
		tv_name1 = (TextView) this.findViewById(R.id.top_scroll);
		tv_name2 = (TextView) this.findViewById(R.id.top_kedui);

		tv_name1.setText("主队/让球");
		tv_name1.setText("客队");
	}

	/**
	 * 绑定监听
	 */
	private void setListener() {
		// TODO Auto-generated method stub

	}

}
