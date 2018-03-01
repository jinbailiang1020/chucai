package com.sm.sls_app.ui;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.xml.sax.XMLReader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.Html.ImageGetter;
import android.text.Html.TagHandler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.Information;
import com.sm.sls_app.dataaccess.utils.HttpUtils;
import com.sm.sls_app.protocol.MD5;
import com.sm.sls_app.protocol.RspBodyBaseBean;
import com.sm.sls_app.ui.MainActivity;
import com.sm.sls_app.utils.App;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.BaseHelper;
import com.sm.sls_app.utils.NetWork;
import com.sm.sls_app.view.MyToast;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMSsoHandler;

public class Information_Detail extends Activity implements OnClickListener {
	private ScrollView information_center_show;
	private String opt = "45"; // 格式化后的 opt
	private String auth, info, time, imei, crc; // 格式化后的参数
	private LinearLayout ll;
	private ProgressBar pb;
	private TextView tx_show, informationDetail_Datetime,
			informationDetail_title, informationDetail_Current,
			informationDetail_Sum;
	private Button informationDetail_Pervious, informationDetail_Next;
	private ImageButton information_detail_share, btn_back;
	private Context context;
	private MyHandler handler;
	private MyAsynTask myAsynTask;
	private List<Information> informations = new ArrayList<Information>();
	private Information informations_show = new Information();

	private int newType = 2;
	private long currentNewId;
	private long[] ids;
	private int newIndex = 1;
	private UMSocialService mController;
	private static final String SHARE_PAGE = "com.umeng.share";
	private static final String SHARE_CONTENT = "我使用了umeng分享接口。。。";
	private static final String QZ_APPID = "100424468";
	private static final String QZ_APPKEY = "c7394704798a158208a74ab60104f0ba";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_informationdetail);
		App.activityS.add(this);
		findView();
		intilBundle();
		setListener();
//		struct();

	}

	private void findView() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;// 宽度
		int height = dm.heightPixels;// 高度
		float density = dm.density; // 密度
		System.out.println("高度====" + height + "============//宽度===" + width
				+ "密度===" + density);
		// TODO Auto-generated method stub
		handler = new MyHandler();
		information_detail_share = (ImageButton) findViewById(R.id.information_detail_share);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		information_center_show = (ScrollView) this
				.findViewById(R.id.information_center_show);
		tx_show = (TextView) this.findViewById(R.id.tx_show);
		informationDetail_Current = (TextView) this
				.findViewById(R.id.informationDetail_Current);
		informationDetail_Sum = (TextView) this
				.findViewById(R.id.informationDetail_Sum);
		informationDetail_title = (TextView) this
				.findViewById(R.id.information_detail_title);
		informationDetail_Datetime = (TextView) this
				.findViewById(R.id.informationDetail_Datetime);
		// informationDetail_recordCount = (TextView) this
		// .findViewById(R.id.informationDetail_recordCount);
		informationDetail_Pervious = (Button) this
				.findViewById(R.id.informationDetail_Pervious);
		informationDetail_Next = (Button) this
				.findViewById(R.id.informationDetail_Next);
		context = getApplicationContext();
		ll = new LinearLayout(this);
		pb = new ProgressBar(this);
		ll.setGravity(Gravity.CENTER);
		ll.addView(pb);

	}

	private void setListener() {
		// TODO Auto-generated method stub
		informationDetail_Pervious.setOnClickListener(this);
		informationDetail_Next.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		information_detail_share.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		intilBundle();
		intil();
	}

	private void intilBundle() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("informationList");
		if (bundle != null) {
			newType = bundle.getInt("newType");
			currentNewId = bundle.getLong("currentNewId");
			ids = bundle.getLongArray("ids");
			newIndex = bundle.getInt("newIndex");
		}
	}

	private void getInformation() {
		Log.i("x", "执行 Information---");
		Boolean isrequest = true;
		for (Information information : informations) {
			if (information.getId() == currentNewId) {
				isrequest = false;
				informations_show = information;
				break;
			}
		}
		if (isrequest) {
			if (NetWork.isConnect(getApplicationContext()) == true) {
				imei = RspBodyBaseBean.getIMEI(context);
				time = RspBodyBaseBean.getTime();
				String uid = "-1";
				String password = "";
				if (null != AppTools.user) {
					uid = AppTools.user.getUid();
					password = AppTools.user.getUserPass();
				}
				info = RspBodyBaseBean.getInformationDetail(newType,
						currentNewId);
				System.out.println("info=" + info);

				crc = RspBodyBaseBean.getCrc(time, imei,
						MD5.md5(password + AppTools.MD5_key), info, uid);
				auth = RspBodyBaseBean.getAuth(crc, time, imei, uid);
				myAsynTask = new MyAsynTask();
				myAsynTask.execute();
			} else {
				MyToast.getToast(getApplicationContext(), "网络连接异常，请检查网络")
						.show();
			}
		} else {
			informationDetail_title.setText(informations_show.getTitle());
			informationDetail_Datetime.setText("发表："
					+ informations_show.getDateTime());
			// informationDetail_recordCount.setText("点击："
			// + informations_show.getReadCount());
			// System.out.println("html的content====="
			// + Html.fromHtml("<html><body>"
			// + informations_show.getContent()
			// + "</body></html>"));
			tx_show.setText(Html.fromHtml(
					"<html><body>" + informations_show.getContent()
							+ "</body></html>", imgGetter, _TagHandler));

			// informationDetail_title.setText(informations_show.getTitle());
			// informationDetail_Datetime.setText("发表："
			// + informations_show.getDateTime());
			// informationDetail_recordCount.setText("点击："
			// + informations_show.getReadCount());
			// System.out
			// .println("html="
			// + Html.fromHtml("<html><body>"
			// + informations_show.getContent()
			// + "</body></html>"));
			// tx_show.setText(Html.fromHtml(
			// "<html><body>" + informations_show.getContent()
			// + "</body></html>").toString());
		}

	}

	// public static void struct() {
	// StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
	// .detectDiskReads().detectDiskWrites().detectNetwork() // or
	// // .detectAll()
	// // for
	// // all
	// // detectable
	// // problems
	// .penaltyLog().build());
	// StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
	// .detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
	// .penaltyLog() // 打印logcat
	// .penaltyDeath().build());
	// }

	// 这里面的resource就是fromhtml函数的第一个参数里面的含有的url
	ImageGetter imgGetter = new Html.ImageGetter() {

		public Drawable getDrawable(String source) {
			System.out.println("=================执行imgGetter===");
			System.out.printf("resource=====" + source);
			Drawable drawable = null;
			URL url;
			try {
				url = new URL(source);
				Log.i("RG", "url---?>>>" + url);
				drawable = Drawable.createFromStream(url.openStream(), ""); // 获取网路图片
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth()
					* MainActivity.density * 2, drawable.getIntrinsicHeight()
					* MainActivity.density * 2);

			// drawable.setBounds(0, 0, 600,450);
			Log.i("RG", "url---?>>>" + url);
			return drawable;
		}
	};

	TagHandler _TagHandler = new Html.TagHandler() {
		// @Override
		// public void handleTag(boolean opening, String tag, Editable output,
		// XMLReader xmlReader) {
		// Toast.makeText(TestActivity.this, tag, 2000).show();
		// }

		@Override
		public void handleTag(boolean opening, String tag, Editable output,
				XMLReader xmlReader) {
			// TODO Auto-generated method stub
			// Toast.makeText(Information_Detail.this, tag, 2000).show();
		}
	};

	private void intil() {
		// TODO Auto-generated method stub
		// information_center_show.addView(ll);
		informationDetail_Current.setText((newIndex + 1) + "");
		informationDetail_Sum.setText(ids.length + "");
		getInformation();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.information_detail_share:
			initUMShare();

			mController.openShare(this, false);
			break;
		case R.id.informationDetail_Pervious:
			if (ids == null) {
				MyToast.getToast(getApplicationContext(), "数据异常").show();
				return;
			}
			if (ids.length <= 0) {
				MyToast.getToast(getApplicationContext(), "数据异常").show();
				return;
			} else if (ids.length == 1) {
				return;
			} else {
				if (newIndex == 0) {
					return;
				} else {
					newIndex = newIndex - 1;
				}
				currentNewId = ids[newIndex];
			}
			intil();
			break;
		case R.id.informationDetail_Next:
			if (ids == null) {
				MyToast.getToast(getApplicationContext(), "数据异常").show();
				return;
			}
			if (ids.length <= 0) {
				MyToast.getToast(getApplicationContext(), "数据异常").show();
				return;
			} else if (ids.length == 1) {
				return;
			} else {
				if (newIndex == ids.length - 1) {
					return;
				} else {
					newIndex = newIndex + 1;
				}
				currentNewId = ids[newIndex];
			}
			intil();
			break;
		default:
			break;
		}
	}

	private void addInformation(JSONObject items) {
		if (informations_show == null) {
			informations_show = new Information();
		}
		currentNewId = items.optLong("informationId");
		informations_show.setId(currentNewId);
		informations_show.setReadCount(items.optInt("readCount"));
		informations_show.setParentTypeId(newType);
		informations_show.setDateTime(items.optString("dateTime"));
		informations_show.setUrl(items.optString("URL"));
		informations_show.setTitle(items.optString("title"));

		try {
			informations_show.setContent(URLDecoder.decode(
					items.optString("content"), "utf-8"));

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		informations.add(informations_show);

	}

	/** 异步任务 用来后台获取数据 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> {
		/** 在后台执行的程序 */
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = BaseHelper.showProgress(Information_Detail.this, null,
					"加载中..", false, true);
			dialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {

			String values[] = { opt, auth, info };

			String result = HttpUtils.doPost(AppTools.names, values,
					AppTools.path);
			Log.i("x", "请求次数---------1");

			System.out.println("lotteryinformationDeatil result " + result);

			if ("-500".equals(result))
				return result;

			try {
				System.out.println("istrue=0");
				JSONObject item = new JSONObject(result);
				System.out.println("istrue="
						+ ("0".equals(item.optString("error"))));
				if ("0".equals(item.optString("error"))) {
					for (Information infor : informations) {
						if (currentNewId == infor.getId()) {
							informations.remove(infor);
						}
					}
					addInformation(item);

				} else {
					System.out.println(item.optString("msg"));
					// MyToast.getToast(context, item.optString("msg")).show();
				}

			} catch (Exception ex) {
				System.out.println("lotteryinformationdetail--错误"
						+ ex.getMessage());
				ex.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (dialog != null) {
				dialog.dismiss();
			}
			handler.sendEmptyMessage(1);
			super.onPostExecute(result);
		}
	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				showContent();
				break;
			case 2:
				MyToast.getToast(getApplicationContext(), "暂无内容").show();
				break;
			case -500:
				MyToast.getToast(Information_Detail.this, "连接超时").show();
				break;
			}
		}

		private void showContent() {
			// TODO Auto-generated method stub
			System.out.println("id=" + informations_show.getId());
			if (informations_show != null) {
				informationDetail_title.setText(informations_show.getTitle());
				informationDetail_Datetime.setText("发表："
						+ informations_show.getDateTime());
//				informationDetail_recordCount.setText("点击："
//						+ informations_show.getReadCount());

				System.out.println("html============"
						+ Html.fromHtml("<html><body>"
								+ informations_show.getContent()
								+ "</body></html>"));
				if (informations_show.getContent() != null) {
					System.out.println("infomation_contnet===="
							+ informations_show.getContent());
					Spanned text = Html.fromHtml(
							informations_show.getContent(), imgGetter,
							_TagHandler);
					// tx_show.setText(Html.fromHtml("<html><body>"+informations_show.getContent()+"</body></html>"));
					tx_show.setText(Html.fromHtml(
							"<html><body>" + informations_show.getContent()
									+ "</body></html>", imgGetter, _TagHandler));
					// tx_show.setText(Html.fromHtml(informations_show.getContent(),imgGetter,_TagHandler));
				} else
					tx_show.setText("资讯已修改，请刷新，谢谢");

			}
			// // TODO Auto-generated method stub
			// System.out.println("id=" + informations_show.getId());
			// if (informations_show != null) {
			// informationDetail_title.setText(informations_show.getTitle());
			// informationDetail_Datetime.setText("发表："
			// + informations_show.getDateTime());
			// informationDetail_recordCount.setText("点击："
			// + informations_show.getReadCount());
			//
			// System.out.println("html="
			// + Html.fromHtml("<html><body>"
			// + informations_show.getContent()
			// + "</body></html>"));
			// if (informations_show.getContent() != null) {
			// Spanned text = Html
			// .fromHtml(informations_show.getContent());
			// //
			// tx_show.setText(Html.fromHtml("<html><body>"+informations_show.getContent()+"</body></html>"));
			//
			// tx_show.setText(text.toString());
			// } else
			// tx_show.setText("资讯已修改，请刷新，谢谢");
			//
			// }
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	private void initUMShare() {
		if (mController == null) {
			mController = UMServiceFactory.getUMSocialService(SHARE_PAGE);
			/**
			 * 
			 mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN_CIRCLE,
			 * SHARE_MEDIA.SMS);
			 * mController.getConfig().setPlatformOrder(SHARE_MEDIA.QZONE,
			 * SHARE_MEDIA.SINA, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.TENCENT,
			 * SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.SMS); // 添加短信
			 * SmsHandler smsHandler = new SmsHandler();
			 * smsHandler.addToSocialSDK();
			 * 
			 * // qqSso UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this,
			 * "100424468", "c7394704798a158208a74ab60104f0ba");
			 * qqSsoHandler.addToSocialSDK(); // qq空间Sso QZoneSsoHandler
			 * qZoneSsoHandler = new QZoneSsoHandler(this, "100424468",
			 * "c7394704798a158208a74ab60104f0ba");
			 * qZoneSsoHandler.addToSocialSDK();
			 * 
			 * // 设置新浪SSO handler mController.getConfig().setSsoHandler(new
			 * SinaSsoHandler());
			 * 
			 * // 设置腾讯微博SSO handler mController.getConfig().setSsoHandler(new
			 * TencentWBSsoHandler());
			 * 
			 * // 添加人人网SSO授权功能 // APPID:201874 // API
			 * Key:28401c0964f04a72a14c812d6132fcef //
			 * Secret:3bf66e42db1e4fa9829b955cc300b737 RenrenSsoHandler
			 * renrenSsoHandler = new RenrenSsoHandler(this, "201874",
			 * "28401c0964f04a72a14c812d6132fcef",
			 * "3bf66e42db1e4fa9829b955cc300b737");
			 * mController.getConfig().setSsoHandler(renrenSsoHandler);
			 */

			QZoneSsoHandler qz = new QZoneSsoHandler(this, QZ_APPID, QZ_APPKEY);
			qz.addToSocialSDK();
			SmsHandler sms = new SmsHandler();
			sms.addToSocialSDK();
			EmailHandler email = new EmailHandler();
			email.addToSocialSDK();

			mController.setShareContent(informations_show.getTitle()
					+ informations_show.getUrl() + "?ID="
					+ informations_show.getId());
		}
	}

}
