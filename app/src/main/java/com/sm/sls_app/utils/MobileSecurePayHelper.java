package com.sm.sls_app.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.sm.sls_app.activity.R;

/**
 * 支付宝
 * 检测安全支付服务是否正确安装，如果没有安装进行本地安装，或者下载安装， 检测安全支付服务版本，有新版本时进行下载。
 * 
 */
public class MobileSecurePayHelper {
	static final String TAG = "MobileSecurePayHelper";

	private ProgressDialog mProgress = null;
	Context mContext = null;

	public MobileSecurePayHelper(Context context) {
		this.mContext = context;
	}

	/**
	 * 检测安全支付服务是否安装
	 * 
	 * @return
	 */
	public boolean detectMobile_sp() {
		boolean isMobile_spExist = isMobile_spExist();
		Log.i("x", "是否安装----"+isMobile_spExist);
		if (!isMobile_spExist) {
			Log.i("x", "检测安全支付服务是否安装");
			// get the cacheDir.
			// 获取系统缓冲绝对路径 获取/data/data//cache目录
			File cacheDir = mContext.getCacheDir();
			final String cachePath = cacheDir.getAbsolutePath() + "/temp.apk";
			
			Log.i("x", "----cachePath-----"+cachePath);
			//
			// 捆绑安装
			retrieveApkFromAssets(mContext, PartnerConfig.ALIPAY_PLUGIN_NAME,
					cachePath);

			mProgress = BaseHelper.showProgress(mContext, null, "正在检测安全支付服务版本",
					false, true);

			// 实例新线程检测是否有新版本进行下载
			new Thread(new Runnable() {
				public void run() {
					//
					// 检测是否有新的版本。
					PackageInfo apkInfo = getApkInfo(mContext, cachePath);
					Log.i("x", "apkInfo"+apkInfo);
					String newApkdlUrl = checkNewUpdate(apkInfo);

					// 动态下载
					if (newApkdlUrl != null)
						retrieveApkFromNet(mContext, newApkdlUrl, cachePath);

					// send the result back to caller.
					// 发送结果
					Message msg = new Message();
					msg.what = AppTools.RQF_INSTALL_CHECK;
					msg.obj = cachePath;
					mHandler.sendMessage(msg);
				}
			}).start();
		}else{
			Log.i("x","已经安装---");
		}
		// else ok.

		return isMobile_spExist;
	}

	/**
	 * 显示确认安装的提示
	 * 
	 * @param context
	 *            上下文环境
	 * @param cachePath
	 *            安装文件路径
	 */
	public void showInstallConfirmDialog(final Context context,
			final String cachePath) {
		
		Log.i("x", "显示确认安装的提示");
		
		AlertDialog.Builder tDialog = new AlertDialog.Builder(context);
		tDialog.setIcon(R.drawable.info);
		tDialog.setTitle("安装提示");
		tDialog.setMessage("为保证您的交易安全，需要您安装支付宝安全支付服务，才能进行付款。\n\n点击确定，立即安装。");

		tDialog.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//
						// 修改apk权限
						BaseHelper.chmod("777", cachePath);

						// install the apk.
						// 安装安全支付服务APK
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.setDataAndType(Uri.parse("file://" + cachePath),
								"application/vnd.android.package-archive");
						context.startActivity(intent);
					}
				});

		tDialog.setNegativeButton(
				"取消",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});

		tDialog.show();
	}

	/**
	 * 遍历程序列表，判断是否安装安全支付服务
	 * 
	 * @return
	 */
	public boolean isMobile_spExist() {
		Log.i("x", "遍历程序列表，判断是否安装安全支付服务");
		PackageManager manager = mContext.getPackageManager();
		List<PackageInfo> pkgList = manager.getInstalledPackages(0);
		for (int i = 0; i < pkgList.size(); i++) {
			PackageInfo pI = pkgList.get(i);
			Log.i("x","packageName========---   "+pI.packageName);
			if (pI.packageName.equalsIgnoreCase("com.alipay.android.app"))
				return true;
		}
		return false;
	}

	/**
	 * 安装安全支付服务，安装assets文件夹下的apk
	 * 
	 * @param context
	 *            上下文环境
	 * @param fileName
	 *            apk名称
	 * @param path
	 *            安装路径
	 * @return
	 */
	public boolean retrieveApkFromAssets(Context context, String fileName,
			String path) {
		Log.i("x", "安装安全支付服务，");
		boolean bRet = false;

		try {
			InputStream is = context.getAssets().open(fileName);

			File file = new File(path);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);

			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}

			fos.close();
			is.close();

			bRet = true;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return bRet;
	}

	/**
	 * 获取未安装的APK信息
	 * 
	 * @param context
	 * @param archiveFilePath
	 *            APK文件的路径。如：/sdcard/download/XX.apk
	 */
	public static PackageInfo getApkInfo(Context context, String archiveFilePath) {
		Log.i("x", "获取未安装的APK信息");
		PackageManager pm = context.getPackageManager();
		PackageInfo apkInfo = pm.getPackageArchiveInfo(archiveFilePath,
				PackageManager.GET_META_DATA);
		return apkInfo;
	}

	/**
	 * 检查是否有新版本，如果有，返回apk下载地址
	 * 
	 * @param packageInfo
	 *            {@link PackageInfo}
	 * @return
	 */
	public String checkNewUpdate(PackageInfo packageInfo) {
		Log.i("x", "检查是否有新版本，");
		String url = null;

		try {
			JSONObject resp = sendCheckNewUpdate(packageInfo.versionName);
			// JSONObject resp = sendCheckNewUpdate("1.0.0");
			if (resp.getString("needUpdate").equalsIgnoreCase("true")) {
				url = resp.getString("updateUrl");
			}
			// else ok.
		} catch (Exception e) {
			e.printStackTrace();
		}

		return url;
	}

	/**
	 * 发送当前版本信息，返回是否需要升级 如果需要升级返回更新apk地址
	 * 
	 * @param versionName
	 *            当前版本号
	 * @return
	 */
	public JSONObject sendCheckNewUpdate(String versionName) {
		Log.i("x", "发送当前版本信息");
		JSONObject objResp = null;
		try {
			JSONObject req = new JSONObject();
			req.put(AppTools.action, AppTools.actionUpdate);

			JSONObject data = new JSONObject();
			data.put(AppTools.platform, "android");
			data.put(AppTools.VERSION, versionName);
			data.put(AppTools.partner, "");

			req.put(AppTools.data, data);

			objResp = sendRequest(req.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return objResp;
	}

	/**
	 * 发送json数据
	 * 
	 * @param content
	 * @return
	 */
	public JSONObject sendRequest(final String content) {
		
		Log.i("x", "发送json数据");
		NetworkManager nM = new NetworkManager(this.mContext);

		//
		JSONObject jsonResponse = null;
		try {
			String response = null;

			synchronized (nM) {
				//
				response = nM.SendAndWaitResponse(content, AppTools.server_url);
			}

			jsonResponse = new JSONObject(response);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//
		if (jsonResponse != null)
			BaseHelper.log(TAG, jsonResponse.toString());

		return jsonResponse;
	}

	/**
	 * 动态下载apk
	 * 
	 * @param context
	 *            上下文环境
	 * @param strurl
	 *            下载地址
	 * @param filename
	 *            文件名称
	 * @return
	 */
	public boolean retrieveApkFromNet(Context context, String strurl,
			String filename) {
		
		Log.i("x", "动态下载APK");
		boolean bRet = false;

		try {
			NetworkManager nM = new NetworkManager(this.mContext);
			bRet = nM.urlDownloadToFile(context, strurl, filename);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bRet;
	}

	//
	// close the progress bar
	void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//
	// the handler use to receive the install check result.
	// 此处接收安装检测结果
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
				case AppTools.RQF_INSTALL_CHECK: {
					//
					closeProgress();
					String cachePath = (String) msg.obj;

					showInstallConfirmDialog(mContext, cachePath);
				}
					break;
				}

				super.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
}