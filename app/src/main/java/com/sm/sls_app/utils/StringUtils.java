package com.sm.sls_app.utils;

import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.provider.Settings.Secure;
import android.text.TextUtils;

public class StringUtils {
	/**
	 * 判断字符串是否为空 Object为null返回true
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj) {

		boolean b = true;
		if (obj == null) {
			return b;
		}
		if (obj.getClass() == String.class) {

			String temp = (String) obj;
			temp = temp.trim();
			if (temp != null && temp.length() > 0)
				b = false;
		}
		return b;
	}

	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNum(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		/*
		 * 3 5 7 8 中的 7 为将要出现的号段 备用
		 */
		// "[1]"代表第1位为数字1，"[3578]"代表第二位可以为3、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		String telRegex = "[1][34578]\\d{9}";
		if (StringUtils.isEmpty(mobiles)) {
			return false;
		} else {
			return mobiles.matches(telRegex);
		}
	}

	/**
	 * 判断email格式是否正确
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		// 定义正则字符串
		String strPatten = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
		Pattern p = Pattern.compile(strPatten);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**********************************************
	 * 验证身份证号码
	 * 
	 * @param id_number
	 *            身份证号码
	 * @return true 身份证符合规范 false 身份证有误
	 */
	public static boolean checkNID(String number) {
		String pattern = "((11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65|71|81|82|91)\\d{4})((((19|20)(([02468][048])|([13579][26]))0229))|((20[0-9][0-9])|(19[0-9][0-9]))((((0[1-9])|(1[0-2]))((0[1-9])|(1\\d)|(2[0-8])))|((((0[1,3-9])|(1[0-2]))(29|30))|(((0[13578])|(1[02]))31))))((\\d{3}(x|X))|(\\d{4}))";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(number);
		return m.matches();
	}

	/**
	 * 格式化文件大小
	 * 
	 * @param size
	 * @return
	 */
	public static String getFormatSize(double size) {
		if (size <= 0) {
			return 0 + "B";
		}
		double kiloByte = size / 1024;
		if (kiloByte <= 1) {
			// return size + "Byte(s)";
			return size + "B";
		}

		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "KB";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "MB";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
				+ "TB";
	}

	/**
	 * 根据返回的录音地址，获得录音文件名
	 * 
	 * @param contentStr
	 * @return
	 */
	public static String getAudioName(String contentStr) {
		if (StringUtils.isEmpty(contentStr)) {
			return null;
		}
		if (contentStr.contains("#")) {
			return contentStr.substring(0, contentStr.indexOf("#"));
		}
		return contentStr;
	}

	/**
	 * 根据返回的录音地址，获得录音文件时长
	 * 
	 * @param contentStr
	 * @return
	 */
	public static String getAudioLenth(String contentStr) {
		if (StringUtils.isEmpty(contentStr)) {
			return null;
		}

		if (contentStr.contains("#")) {
			int audioLenth = -1;
			if (!TextUtils.isEmpty(contentStr.substring(contentStr
					.lastIndexOf("#") + 1))) {
				float f = Float.parseFloat(contentStr.substring(contentStr
						.lastIndexOf("#") + 1));
				audioLenth = (int) f;
			}
			return String.valueOf(audioLenth);
		} else
			return contentStr;
	}

	/**
	 * 获取发送或接收的文件URL
	 * 
	 * @param fileUrlPath
	 * @return
	 */
	public static String getChatFileURL(String fileUrlPath) {
		if (StringUtils.isEmpty(fileUrlPath)) {
			return null;
		}
		return getAudioName(fileUrlPath);
	}

	/**
	 * 获取发送或接收的文件的长度， ###隔开 ps:只适用于发送或者接收的文件
	 * 
	 * @param fileUrlPath
	 *            文件网络地址
	 * @return
	 */
	public static String getChatFileLength(String fileUrlPath) {
		if (StringUtils.isEmpty(fileUrlPath)) {
			return null;
		}
		String temp = "###";
		int firstIndex = 0, lastIndex = 0;
		firstIndex = fileUrlPath.indexOf(temp) + temp.length();
		lastIndex = fileUrlPath.lastIndexOf(temp);
		return fileUrlPath.substring(firstIndex, lastIndex);
	}

	/**
	 * 获取发送或接收的文件的长度， ###隔开 ps:只适用于发送或者接收的文件
	 * 
	 * @param fileUrlPath
	 *            文件网络地址
	 * @return
	 */
	public static String getChatFileName(String fileUrlPath) {
		if (StringUtils.isEmpty(fileUrlPath)) {
			return null;
		}
		String temp = "###";
		return fileUrlPath.substring((fileUrlPath.lastIndexOf(temp) + temp
				.length()));
	}

	/**
	 * 获取文件长度，并且格式化 ps:用于显示文件大小
	 */
	public static String getFileLength(String fileAbs) {
		if (!StringUtils.isEmpty(fileAbs)) {
			File file = new File(fileAbs);
			if (file.exists()) {
				return StringUtils.getFormatSize(file.length());
			} else {
				return null;
			}
		}
		return null;
	}

	/** 判断字符串首字符是否为英文 */
	public static boolean isEnglishLetter(String s) {
		if (StringUtils.isEmpty(s)) {
			return false;
		}
		char c = s.charAt(0);
		return ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z');
	}

	/** 判断字符是否为中文 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/***
	 * 获取一个字符串中的数字
	 * 
	 * @param s
	 * @return
	 */
	public static String getNumber(String s) {
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(s);
		String rate_str = m.replaceAll("").trim();
		return rate_str;
	}

	/** 获取被遮挡后的电话号码 */
	public static String getCoverMobile(String mobile) {
		if (StringUtils.isEmpty(mobile)) {
			return "";
		}

		int len = mobile.length();
		int startIndex = 3;
		int stopIndex = startIndex + 4;
		String startString = mobile.substring(0, startIndex);
		String middleString = "****";
		String stopString = mobile.substring(stopIndex, len);
		String resultString = startString + middleString + stopString;
		return resultString;
	}



	/**
	 * 获取百分比数字（保留小数点后两位）
	 * 
	 * @param d
	 *            大于 0 时，先除以100 进行计算，小于 0 时，直接转换成百分比
	 * @return
	 */
	public static String getPercentString(double d) {

		if (d > 0) {
			d = d / 100;
		}
		NumberFormat num = NumberFormat.getPercentInstance();
		// //最大小数位数
		num.setMaximumFractionDigits(2);
		// //最大整数位数
		// num.setMaximumIntegerDigits(3);
		String str = num.format(d);
		return str;
	}

	/**
	 * 获取金钱文字 每三位显示"," 如10000 则返回 10,000 <br>
	 * PS:只保留整数位
	 * 
	 * @param d
	 * @return
	 */
	public static String getMoneyString(double d) {
		NumberFormat nFromat = NumberFormat.getNumberInstance();
		String s = nFromat.format(d);
		return s;
	}



	/**
	 * 按照参数顺序，返回第一个不为空的字符串
	 * 
	 * @param first
	 * @param second
	 * @param third
	 * 
	 * @return
	 */
	public static String getFirstNotNullString(String first, String second,
			String third) {
		String s = "";
		if (!StringUtils.isEmail(first)) {
			s = first;
		} else {
			s = getFirstNotNullString(second, third);
		}
		return s;
	}

	/**
	 * 按照参数顺序，返回第一个不为空的字符串
	 * 
	 * @param first
	 * @param second
	 * 
	 * @return
	 */
	public static String getFirstNotNullString(String first, String second) {
		String s = "";
		if (!StringUtils.isEmail(first)) {
			s = first;
		} else if (!StringUtils.isEmail(second)) {
			s = second;
		}
		return s;
	}
	
	/**
	 * 获取安卓设备唯一的序列号
	 */
	public static String getUniqureNum(Context context)
	{
	    if(null!=context)
	    {
	        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
	    }
	    
	    return null;
	}
	

	// /**
	// * 根据消息类型获取文字<br>
	// * 适用类型：
	// * <blockquote>
	// * AUDIO<br>
	// * IMAGE<br>
	// * FILE<br>
	// * VEDIO<br>
	// */
	// public static String getMessageFormatText(MessageFormat format) {
	// if (format == null) {
	// return "";
	// }
	// String content = "";
	// switch (format) {
	// case AUDIO:
	// content = "[声音]";
	// break;
	// case IMAGE:
	// content = "[图片]";
	// break;
	// case FILE:
	// content = "[文件]";
	// break;
	// case VEDIO:
	// content = "[视频]";
	// break;
	// default:
	// break;
	// }
	// return content;
	// }
}
