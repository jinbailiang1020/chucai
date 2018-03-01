package com.sm.sls_app.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.Schemes;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.fragment.FollowFragment;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.view.MyToast;
import com.sm.sls_app.view.RoundProgressBar;

import org.json.JSONObject;

/**
 * 合买 选号详情
 *
 * @author Kinwee 修改时间 2014-12-5
 */
@SuppressLint("HandlerLeak")
public class FollowInfoActivity extends Activity implements OnClickListener {
    private static final String TAG = "FollowInfoActivity";

    private Schemes schemes;
    private TextView tv_yong, tv_numberInfo, tv_title, tv_content,
            tv_userName2,
            follow_lv_tv_lotteryname,
            follow_lv_iv_eachmoney, // 彩种名称// //每份金额
            follow_lv_iv_remain, follow_lv_iv_tatolmoney, tv_playType,
            tv_playType2, follow_lv_tv_qi, follow_lv_tv_baodi,
            follow_lv_tv_qihao; // 剩余份数// 总金额
    /*private ImageView follow_lv_iv_user_record1, follow_lv_iv_user_record2,
            follow_lv_iv_user_record3, follow_lv_iv_user_record4;*/
    private Button btn_info, btn_submit; // 查看详情// 付款
    private EditText et_count; // 购买份数

    // private TextView tv_showMoney; // 底部显示金额栏
    private TextView follow_detail_tv_remain; // 剩余份数

    private ImageButton btn_back;// 返回

    private Intent intent;
    private Bundle bundle;

    private int buyShare = 1, remainShare = -1, schedule = 1;

    private String opt, auth, info, time, imei, crc; // 格式化后的参数
    private MyAsynTask myAsynTask;
    private MyHandler myHandler;

    private RoundProgressBar RoundPr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        // 设置无标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_followinfo);
        App.activityS.add(this);
        findView();
        init();
        setListener();
    }

    /**
     * 初始化UI
     */
    private void findView() {
        follow_lv_tv_lotteryname = (TextView) this
                .findViewById(R.id.follow_lv_tv_lotteryname);
        follow_lv_iv_eachmoney = (TextView) this
                .findViewById(R.id.follow_lv_iv_eachmoney);
        follow_lv_iv_remain = (TextView) this
                .findViewById(R.id.follow_lv_iv_remain);
        follow_lv_iv_tatolmoney = (TextView) this
                .findViewById(R.id.follow_lv_iv_tatolmoney);
        // follow_lv_tv_username = (TextView) this
        // .findViewById(R.id.follow_lv_tv_username);
        follow_lv_tv_qihao = (TextView) findViewById(R.id.activity_followinfo_qihao);
        follow_lv_tv_qi = (TextView) findViewById(R.id.activity_followinfo_qi);
        follow_lv_tv_baodi = (TextView) findViewById(R.id.follow_lv_iv_baodi);
        // tv_bao = (TextView) this.findViewById(R.id.f_tv_top);
        RoundPr = (RoundProgressBar) this.findViewById(R.id.RoundProgressBar);
        tv_userName2 = (TextView) this.findViewById(R.id.info_tv_userName2);
        tv_yong = (TextView) this.findViewById(R.id.info_tv_yongjin2);
        tv_numberInfo = (TextView) this.findViewById(R.id.info_tv_numberInfo2);
        tv_title = (TextView) this.findViewById(R.id.info_tv_title2);
        tv_content = (TextView) this.findViewById(R.id.info_tv_content2);
        tv_playType = (TextView) this.findViewById(R.id.follow_tv_playType);
        tv_playType2 = (TextView) this.findViewById(R.id.follow_tv_playType2);
        follow_detail_tv_remain = (TextView) this
                .findViewById(R.id.follow_detail_tv_remain);
        btn_back = (ImageButton) this.findViewById(R.id.btn_back);

/*		follow_lv_iv_user_record1 = (ImageView) this
                .findViewById(R.id.follow_lv_iv_user_record1);
		follow_lv_iv_user_record2 = (ImageView) this
				.findViewById(R.id.follow_lv_iv_user_record2);
		follow_lv_iv_user_record3 = (ImageView) this
				.findViewById(R.id.follow_lv_iv_user_record3);
		follow_lv_iv_user_record4 = (ImageView) this
				.findViewById(R.id.follow_lv_iv_user_record4);*/

        et_count = (EditText) this.findViewById(R.id.et_count);
        // tv_showMoney = (TextView)
        // this.findViewById(R.id.info_bottom_tv_money);

        btn_info = (Button) this.findViewById(R.id.btn_numberInfo);
        btn_submit = (Button) this.findViewById(R.id.btn_submit);
        // btn_max = (Button) this.findViewById(R.id.btn_maxCount);

    }

    /**
     * 初始化属性
     */
    private void init() {
        myHandler = new MyHandler();
        intent = getIntent();
        bundle = intent.getBundleExtra("bundle");
        schemes = (Schemes) bundle.getSerializable("schem");
        // follow_lv_tv_username.setText(schemes.getInitiateName());
        tv_userName2.setText(schemes.getInitiateName());
        tv_yong.setText((int) (schemes.getSchemeBonusScale() * 1000) + "‰");
        follow_lv_tv_lotteryname.setText(schemes.getLotteryName());
        int remain = schemes.getSurplusShare();
        follow_detail_tv_remain.setText(remain + "");
        follow_lv_iv_remain.setText(remain + "份");
        follow_lv_tv_qi
                .setText(String.format("共%d期", schemes.getSumIsuseNum()));
        follow_lv_tv_qihao.setText(String.format("%s期",
                schemes.getSchemeNumber()));
        follow_lv_tv_baodi.setText(String.valueOf(schemes.getAssureShare()));
        follow_lv_iv_eachmoney.setText(schemes.getShareMoney() + "元");
        follow_lv_iv_tatolmoney.setText(schemes.getMoney() + "元");

        tv_playType2.setText(schemes.getPlayTypeName());

        RoundPr.setjidu(schemes.getSchedule(), (int) (schemes.getAssureMoney()
                / schemes.getMoney() * 100)); // 方案进度 保底进度 半径

        if (schemes.getLevel() == 0) {
//            follow_lv_iv_user_record1.setVisibility(View.VISIBLE);
        }else if (schemes.getLevel() == 1) {
//            follow_lv_iv_user_record1.setVisibility(View.VISIBLE);

            int level = schemes.getLevel();
            // int level = 9999;
            if (9999 < level) {// 1000以上
            /*    setxin(follow_lv_iv_user_record1, follow_lv_iv_user_record2,
                        follow_lv_iv_user_record3, follow_lv_iv_user_record4,
                        View.VISIBLE);
                follow_lv_iv_user_record1
                        .setBackgroundResource(AppTools.level_crown_list.get(8));
                follow_lv_iv_user_record2
                        .setBackgroundResource(AppTools.level_cup_list.get(8));
                follow_lv_iv_user_record3
                        .setBackgroundResource(AppTools.level_medal_list.get(8));
                follow_lv_iv_user_record4
                        .setBackgroundResource(AppTools.level_star_list.get(8));*/
            } else if (level <= 9999 && level > 999) {// 含皇冠,奖杯，奖牌，星星
             /*   setxin(follow_lv_iv_user_record1, follow_lv_iv_user_record2,
                        follow_lv_iv_user_record3, follow_lv_iv_user_record4,
                        View.VISIBLE);*/
                int crown = level / 1000;// 皇冠个数
//                follow_lv_iv_user_record1.setBackgroundResource(AppTools.level_crown_list.get(crown - 1));


                int cup = (level - 1000 * crown) / 100;// 奖杯个数
//                follow_lv_iv_user_record2.setBackgroundResource(AppTools.level_cup_list.get(cup - 1));


                int medal = (level - 1000 * crown - cup * 100) / 10;// 奖杯个数
//                follow_lv_iv_user_record3.setBackgroundResource(AppTools.level_medal_list.get(medal - 1));


                int star = level - 1000 * crown - cup * 100 - medal * 10;// 星星个数
//                follow_lv_iv_user_record4.setBackgroundResource(AppTools.level_star_list.get(star - 1));


            } else if (level <= 999 && level > 99) {// 含奖杯，奖牌，星星

//				follow_lv_iv_user_record2.setVisibility(View.VISIBLE);
//				follow_lv_iv_user_record3.setVisibility(View.VISIBLE);
//				follow_lv_iv_user_record4.setVisibility(View.VISIBLE);
                int cup = level / 100;// 奖杯个数
//				follow_lv_iv_user_record2.setBackgroundResource(AppTools.level_cup_list.get(cup - 1));


                int medal = (level - cup * 100) / 10;// 奖杯个数
//                follow_lv_iv_user_record3.setBackgroundResource(AppTools.level_medal_list.get(medal - 1));


                int star = level - cup * 100 - medal * 10;// 星星个数
//                follow_lv_iv_user_record4.setBackgroundResource(AppTools.level_star_list.get(star - 1));


            } else if (level <= 99 && level > 9) {// 含奖牌，星星
//                follow_lv_iv_user_record3.setVisibility(View.VISIBLE);
//                follow_lv_iv_user_record4.setVisibility(View.VISIBLE);
                int medal = level / 10;// 奖杯个数
//                follow_lv_iv_user_record3.setBackgroundResource(AppTools.level_medal_list.get(medal - 1));


                int star = level - medal * 10;// 星星个数
//                follow_lv_iv_user_record4.setBackgroundResource(AppTools.level_star_list.get(star - 1));


            } else if (level >= 1) {
//                follow_lv_iv_user_record4.setVisibility(View.VISIBLE);
//                follow_lv_iv_user_record4.setBackgroundResource(AppTools.level_star_list.get(level - 1));
            }

            switch (schemes.getSecrecyLevel()) {
                case 0:
                    tv_numberInfo.setText("公开");
                    break;
                case 1:
                    tv_numberInfo.setText("到截止");
                    if (AppTools.user == null
                            || !AppTools.user.getName().equals(
                            tv_userName2.getText().toString().trim()))
                        btn_info.setVisibility(View.GONE);
                    break;
                case 2:
                    tv_numberInfo.setText("到开奖");
                    if (AppTools.user == null
                            || !AppTools.user.getName().equals(
                            tv_userName2.getText().toString().trim()))
                        btn_info.setVisibility(View.GONE);
                    break;
            }
        }
        if ("null".equals(schemes.getTitle())) {
            tv_title.setText("无标题");
        } else {
            tv_title.setText(schemes.getTitle());
        }
        if ("null".equals(schemes.getDescription())) {
            tv_content.setText("无方案描述");
        } else {
            tv_content.setText(schemes.getDescription());
        }
        opt = "12";
        time = RspBodyBaseBean.getTime();
        imei = RspBodyBaseBean.getIMEI(getApplicationContext());
    }

    public void setxin(View v, View v1, View v2, View v3, int visible) {
        v.setVisibility(visible);
        v1.setVisibility(visible);
        v2.setVisibility(visible);
        v3.setVisibility(visible);
    }

    /**
     * 绑定监听
     */
    private void setListener() {
        btn_info.setOnClickListener(this);
        // btn_max.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        et_count.addTextChangedListener(watcher);
    }

    /**
     * 当文本的值改变时
     */
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
        }

        @Override
        public void afterTextChanged(Editable edt) {
            if (edt.toString().trim().length() != 0) {
                if (Integer.parseInt(edt.toString().trim()) > schemes
                        .getSurplusShare()) {
                    et_count.setText(schemes.getSurplusShare() + "");
                    MyToast.getToast(getApplicationContext(),
                            "最多购买" + schemes.getSurplusShare() + "份").show();
                }
                if (edt.toString().substring(0, 1).equals("0")) {
                    et_count.setText(edt.toString().substring(1,
                            edt.toString().length()));
                }
            }
            show();
        }
    };

    /**
     * 按钮点击监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /** 查看投注详情 **/
            case R.id.btn_numberInfo:
                betInfo();
                break;
            /** 付款 **/
            case R.id.btn_submit:
                dopay();
                break;
            case R.id.btn_back:// 返回
                FollowInfoActivity.this.finish();
                break;
        }
    }

    /**
     * 显示
     **/
    private void show() {
        if (et_count.getText().toString().trim().length() == 0)
            buyShare = 1;
        else
            buyShare = Integer.parseInt(et_count.getText().toString().trim());
        // tv_showMoney.setText(buyShare + "份×" + schemes.getShareMoney() + "元="
        // + (buyShare * schemes.getShareMoney()) + "元");
    }

    /**
     * 付款
     */
    private void dopay() {
        if (AppTools.user != null) {
            setEnabled(false);
            myAsynTask = new MyAsynTask();
            myAsynTask.execute();
        } else {
            MyToast.getToast(FollowInfoActivity.this, "请先登陆").show();
            Intent intent = new Intent(FollowInfoActivity.this,
                    LoginActivity.class);
            intent.putExtra("loginType", "bet");
            FollowInfoActivity.this.startActivity(intent);
        }
    }

    /**
     * 设置是否可用
     **/
    private void setEnabled(boolean isEna) {
        btn_submit.setEnabled(isEna);
        et_count.setEnabled(isEna);

    }

    /**
     * 查看投注详情
     */
    private void betInfo() {
        if ("73".equals(schemes.getLotteryID())
                || "72".equals(schemes.getLotteryID())) {
            intent = new Intent(FollowInfoActivity.this,
                    FollowNumberActivity_jc.class);
        } else {
            intent = new Intent(FollowInfoActivity.this,
                    FollowNumberActivity.class);
        }
        intent.putExtra("bundle", bundle);
        FollowInfoActivity.this.startActivity(intent);
    }

    /**
     * 异步任务
     */
    class MyAsynTask extends AsyncTask<Void, Integer, String> {
        /**
         * 在后台执行的程序
         */
        @Override
        protected String doInBackground(Void... params) {

            Log.i("x", "buyShare------" + buyShare);

            info = RspBodyBaseBean.changeFollow_info(schemes.getId(), buyShare,
                    schemes.getShareMoney());

            crc = RspBodyBaseBean.getCrc(time, imei,
                    MD5.md5(AppTools.user.getUserPass() + AppTools.MD5_key),
                    info, AppTools.user.getUid());

            auth = RspBodyBaseBean.getAuth(crc, time, imei,
                    AppTools.user.getUid());

            String[] values = {opt, auth, info};
            String result = HttpUtils.doPost(AppTools.names, values,
                    AppTools.path);

            // remainShare
            Log.i("x", "加入合买的info====" + info);
            Log.i("x", "加入合买的result====" + result);
            if ("-500".equals(result))
                return result;

            System.out.println("FollowInfoActivity---" + result);
            try {
                JSONObject object = new JSONObject(result);

                if ("0".equals(object.optString("error"))) {
                    Log.i("x", "加入合买成功  ");
                    AppTools.user.setBalance(object.getLong("balance"));
                    AppTools.user.setFreeze(object.getDouble("freeze"));
                    remainShare = object.getInt("remainShare");
                    schedule = object.getInt("currentSchedule");
                    return 0 + "";
                } else {
                    return object.optString("error");
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e("s", e.getMessage());
            }
            return "-100";
        }

        @Override
        protected void onPostExecute(String result) {
            myHandler.sendEmptyMessage(Integer.parseInt(result));
            super.onPostExecute(result);
        }

    }

    /**
     * 处理页面显示的
     */
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            setEnabled(true);
            switch (msg.what) {
                case AppTools.ERROR_SUCCESS:
                    MyToast.getToast(FollowInfoActivity.this, "加入合买成功").show();
                    for (int i = 0; i < FollowFragment.listSchemes.size(); i++) {
                        if (FollowFragment.listSchemes.get(i).getId()
                                .equals(schemes.getId())) {
                            FollowFragment.listSchemes.get(i).setSurplusShare(
                                    remainShare);
                            FollowFragment.listSchemes.get(i).setSchedule(schedule);
                            if (schedule == 100)
                                FollowFragment.listSchemes.remove(i);
                        }
                    }
                    FollowInfoActivity.this.finish();
                    break;
                case -134:
                    break;
                case -500:
                    MyToast.getToast(FollowInfoActivity.this, "连接超时").show();
                    break;
                case -113:
                    MyToast.getToast(FollowInfoActivity.this, "方案已截止").show();
                    break;
                case -116:
                    MyToast.getToast(FollowInfoActivity.this,
                            "方案剩余份数已不足一注，请手动刷新页面。").show();
                    break;
                case -115:
                    Toast.makeText(FollowInfoActivity.this, "余额不足",
                            Toast.LENGTH_SHORT).show();
                    intent = new Intent(FollowInfoActivity.this,
                            RechargeActivity.class);
                    FollowInfoActivity.this.startActivity(intent);
                    break;
                default:
                    break;
            }
            if (myAsynTask != null
                    && myAsynTask.getStatus() == AsyncTask.Status.RUNNING) {
                myAsynTask.cancel(true); // 如果Task还在运行，则先取消它
                Log.i("x", "执行了");
            }

            super.handleMessage(msg);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        super.onNewIntent(intent);
    }

}
