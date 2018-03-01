package com.sm.sls_app.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.ui.RechargeActivity;
import com.sm.sls_app.view.ConfirmDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FileUtils {

	public static String Long2DateStr_detail(long time) {
		String format = "yyyy-M-d HH:mm";
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String long_time = sdf.format(date);
		return long_time;
	}

	/**
	 * 根据彩种id 判断彩种名称
	 * 
	 * @param LotteryId
	 * @return
	 */
	public static String getTitleText(String LotteryId) {

		String lotteryNameString = "双色球";
		if ("6".equals(LotteryId)) {
			lotteryNameString = "3D";
		} else if ("63".equals(LotteryId)) {
			lotteryNameString = "排列三";
		} else if ("78".equals(LotteryId)) {
			lotteryNameString = "广东11选5";
		} else if ("64".equals(LotteryId)) {
			lotteryNameString = "排列五";
		} else if ("3".equals(LotteryId)) {
			lotteryNameString = "七星彩";
		} else if ("39".equals(LotteryId)) {
			lotteryNameString = "大乐透";
		} else if ("13".equals(LotteryId)) {
			lotteryNameString = "七乐彩";
		} else if ("74".equals(LotteryId)) {
			lotteryNameString = "胜负彩";
		} else if ("75".equals(LotteryId)) {
			lotteryNameString = "任九场";
		} else if ("62".equals(LotteryId)) {
			lotteryNameString = "十一运夺金";
		} else if ("82".equals(LotteryId)) {
			lotteryNameString = "幸运彩";
		} else if ("28".equals(LotteryId)) {
			lotteryNameString = "重庆时时彩";
		} else if ("70".equals(LotteryId)) {
			lotteryNameString = "江西11选5";
		} else if ("83".equals(LotteryId)) {
			lotteryNameString = "江苏快3";
		} else if ("72".equals(LotteryId)) {
			lotteryNameString = "竞彩足球";
		} else if ("73".equals(LotteryId)) {
			lotteryNameString = "竞彩篮球";
		} else if ("61".equals(LotteryId)) {
			lotteryNameString = "江西时时彩";
		} else if ("92".equals(LotteryId)) {
			lotteryNameString = "河内时时彩";
		} else if ("94".equals(LotteryId)) {
			lotteryNameString = "北京PK10";
		} else if ("93".equals(LotteryId)) {
			lotteryNameString = "印尼时时彩";
		} else if ("66".equals(LotteryId)) {
			lotteryNameString = "新疆时时彩";
		}
		return lotteryNameString;
	}

	/**
	 * 充值提示框
	 */
	public static void showMoneyLess(final Context context,
			final long totalMoney) {
		ConfirmDialog dialog = new ConfirmDialog(context, R.style.dialog);
		dialog.show();
		dialog.setDialogContent(context.getResources().getString(
				R.string.recharge));
		dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {

			@Override
			public void getResult(int resultCode) {
				if (1 == resultCode) {// 确定
					Intent intent = new Intent(context, RechargeActivity.class);
					intent.putExtra("money", Double.valueOf(totalMoney));
					context.startActivity(intent);
				}
			}
		});
	}

	/**
	 * 三个月以内的消息
	 * 
	 * @param list
	 * @return
	 */
	public static boolean checkThreeMonth(List<String> list) {
		try {
			if (list != null && list.size() != 0) {
				int topYear = Integer.valueOf(list.get(0).split("-")[0]);
				int topMonth = Integer.valueOf(list.get(0).split("-")[1]);

				int lowYear = Integer.valueOf(list.get(list.size() - 1).split(
						"-")[0]);
				int lowMonth = Integer.valueOf(list.get(list.size() - 1).split(
						"-")[1]);

				if (topYear - lowYear > 1) {
					return false;
				} else if (topYear - lowYear == 1) {
					// 三个月以内
					if ((topMonth + 12) - lowMonth < 3) {
						return true;
					}
				} else {
					if ((topMonth - lowMonth) < 3) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("checkThreeMonth  :" + e.toString());
		}
		return false;
	}

	/**
	 * 比较时间，相差三个月返回true
	 * 
	 * @param date
	 * @param date1
	 * @return
	 */
	public static boolean compareMonth(String date, String date1) {
		try {
			if (!TextUtils.isEmpty(date) && date.contains("-")
					&& !TextUtils.isEmpty(date1) && date1.contains("-")) {
				int topYear = Integer.valueOf(date.split("-")[0]);
				int topMonth = Integer.valueOf(date.split("-")[1]);

				int lowYear = Integer.valueOf(date1.split("-")[0]);
				int lowMonth = Integer.valueOf(date1.split("-")[1]);

				if (topYear - lowYear > 1) {
					return false;
				} else if (topYear - lowYear == 1) {
					// 三个月以内
					if ((topMonth + 12) - lowMonth < 3) {
						return true;
					}
				} else {
					if ((topMonth - lowMonth) < 3) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("checkThreeMonth  :" + e.toString());
		}
		return false;
	}

	/**
	 * 拼接订单编号
	 * 
	 * @param lotteryId
	 * @param isuseName
	 * @param schemeids
	 * @return
	 */
	public static String getSchemeNum(String lotteryId, String isuseName,
			int schemeids) {
		String schemeNumMiddle = "";
		if ("6".equals(lotteryId)) {
			// lotteryNameString = "3D";
			schemeNumMiddle = "3D";
		} else if ("63".equals(lotteryId)) {
			// lotteryNameString = "排列三";
			schemeNumMiddle = "PL3";
		} else if ("5".equals(lotteryId)) {
			// lotteryNameString = "双色球";
			schemeNumMiddle = "SSQ";
		} else if ("62".equals(lotteryId)) {
			// TODO 十一运夺金暂无
			// lotteryNameString = "十一运夺金";
			schemeNumMiddle = "";
		} else if ("64".equals(lotteryId)) {
			// lotteryNameString = "排列五";
			schemeNumMiddle = "PL5";
		} else if ("3".equals(lotteryId)) {
			// TODO 七星彩需要检查
			// lotteryNameString = "七星彩";
			schemeNumMiddle = "QXC";
		} else if ("39".equals(lotteryId)) {
			// lotteryNameString = "大乐透";
			schemeNumMiddle = "TCCJDLT";
		} else if ("13".equals(lotteryId)) {
			// lotteryNameString = "七乐彩";
			schemeNumMiddle = "QLC";
		} else if ("74".equals(lotteryId)) {
			// lotteryNameString = "胜负彩";
			schemeNumMiddle = "SFC";
		} else if ("75".equals(lotteryId)) {
			// lotteryNameString = "任九场";
			schemeNumMiddle = "RJC";
		} else if ("82".equals(lotteryId)) {
			// lotteryNameString = "幸运彩";
			// TODO 幸运彩需要检查
			schemeNumMiddle = "XYC";
		} else if ("28".equals(lotteryId)) {
			// lotteryNameString = "时时彩";
			schemeNumMiddle = "CQSSC";
		} else if ("70".equals(lotteryId)) {
			// lotteryNameString = "11选5";
			schemeNumMiddle = "JX11X5";
		} else if ("83".equals(lotteryId)) {
			// lotteryNameString = "江苏快3";
			schemeNumMiddle = "JSK3";
		} else if ("72".equals(lotteryId)) {
			// lotteryNameString = "竞彩足球";
			schemeNumMiddle = "JCZQ";
		} else if ("73".equals(lotteryId)) {
			// TODO 竞彩篮球需要检查
			// lotteryNameString = "竞彩篮球";
			schemeNumMiddle = "JCLQ";
		} else if ("94".equals(lotteryId)) {
			// lotteryNameString = "北京PK10";
			schemeNumMiddle = "BJPK10";
		}
		return isuseName + schemeNumMiddle + schemeids;
	}

	/**
	 * 返回当前的"yyyy-MM-dd"
	 * 
	 * @param now
	 * @return
	 */
	public static String getSchemeTime(long now) {
		String time = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		time = dateFormat.format(new Date(now));
		return time;
	}
}
