package com.sm.sls_app.protocol;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.sm.sls_app.dataaccess.SelectedNumbers;
import com.sm.sls_app.utils.AppTools;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 功能： 协议类，将传递信息变成json格式
 * 
 * @author Administrator
 */
public class RspBodyBaseBean {

	private static final String TAG = "RspBodyBaseBean";

	/**
	 * 登录参数 auth 格式
	 * 
	 * @param crc
	 * @param time_stamp
	 *            时间
	 * @param imei
	 *            手机识别码
	 * @return
	 */
	public static String changeLogin_Auth(String crc, String time_stamp,
			String imei) {
		return "{\"loginType\":\"1\",\"app_key\":\"123456\",\"imei\":\"" + imei
				+ "\"," + "\"os\":\"Iphoneos\",\"os_version\":\"5.0\","
				+ "\"app_version\":\"1.0.0\",\"source_id\":\"Yek_test\","
				+ "\"ver\":\"0.9\",\"uid\":\"-1\"," + "\"crc\":\"" + crc
				+ "\"," + "\"time_stamp\":\"" + time_stamp + "\"}";

	}

	/**
	 * 登录参数 info格式
	 * 
	 * @param name
	 *            用户名
	 * @param pass
	 *            密码
	 * @return
	 */
	public static String changeLogin_Info(String name, String pass) {
		return "{\"name\":\"" + name + "\",\"password\":\"" + pass + "\"}";
	}

	/**
	 * 参数 auth 格式
	 * 
	 * @param crc
	 * @param time_stamp
	 * @param imei
	 * @param uid
	 *            用户ID
	 * @return
	 */
	public static String getAuth(String crc, String time_stamp, String imei,
			String uid) {
		return "{\"loginType\":\"1\",\"app_key\":\"123456\",\"imei\":\""
				+ imei
				+ "\","
				+ "\"os\":\"Android\",\"os_version\":\"5.0\",\"app_version\":\""
				+ AppTools.version + "\","
				+ "\"source_id\":\"Yek_test\",\"ver\":\"0.9\",\"uid\":\"" + uid
				+ "\"" + ",\"crc\":\"" + crc + "\",\"time_stamp\":" + "\""
				+ time_stamp + "\"}";
	}

	/**
	 * 获得我的方案 参数Info的格式规范
	 * 
	 * @param lotteryID
	 *            彩种ID
	 * @param pageIndex
	 *            页码
	 * @param pageSize
	 *            每页显示行数
	 * @param sort
	 *            参数解释 ： 0 按方案进度 1 按方案总金额 2 按每份金额 3 按方案置顶状态 4 按发起人战绩 5 按方案发起时间
	 * @param status
	 *            0 从大到小 1 或 其他 从小到大
	 * @param isPurchasing
	 *            0 合买 1 代购 其他 返回所有
	 * @return
	 */
	public static String changeMyLotteryInfo_Info(String lotteryID,
			int pageIndex, int pageSize, int sort, int isPurchasing, int status) {
		return "{\"lotteryId\":\"" + lotteryID + "\", \"pageIndex\":\""
				+ pageIndex + "\",\"pageSize\":\"" + pageSize + "\","
				+ "\"sort\":\"" + sort
				+ "\", \"sortType\":\"0\", \"isPurchasing\": \"" + isPurchasing
				+ "\",\"status\":\"" + status + "\"}";
	}

	/**
	 * 获取我的追号详情 参数info的格式规范
	 * 
	 * @param lotteryId
	 *            彩种ID
	 * @param pageIndex
	 *            页码
	 * @param pageSize
	 *            显示几行
	 * @param sort
	 *            排序方法 （5 按方案发起时间）
	 * @param sortType
	 *            0 从大到小 1 或 其他 从小到大
	 * @return
	 */
	public static String changeMyFollow_info(String lotteryId, int pageIndex,
			int pageSize, int sort, int sortType) {
		return "{\"lotteryId\":\"" + lotteryId + "\", \"pageIndex\":\""
				+ pageIndex + "\"," + "\"pageSize\":\"" + pageSize
				+ "\", \"sort\":\"" + sort + "\",\"sortType\":\"" + sortType
				+ "\"}";
	}

	/**
	 * 注册 参数info的格式规范
	 * 
	 * @param name
	 *            用户名
	 * @param password
	 *            密码
	 * @return
	 */
	public static String changeRegister_info(String name, String password) {
		return "{\"name\":\"" + name + "\",\"password\":\"" + password
				+ "\",\"email\":" + "\"\",\"idcardnumber\":\"\","
				+ "\"realityname\":\"\",\"mobile\":\"\"}";
	}

	/**
	 * 获得彩种信息 参数info的格式规范
	 * 
	 * @param lotteryId
	 *            彩种ID
	 * @return
	 */
	public static String changeLottery_info(String lotteryId, String playType) {
		return " {\"lotteryId\":\"" + lotteryId + "\",\"playType\":\""
				+ playType + "\"}";
	}

	/**
	 * 获得彩种信息 参数info的格式规范
	 * 
	 * @param lotteryId
	 *            彩种ID
	 * @return
	 */
	public static String changeLottery_info(String lotteryId) {
		return " {\"lotteryId\":\"" + lotteryId + "\"}";
	}

	/**
	 * 获得彩种信息 参数info的格式规范
	 * 
	 * @param lotteryId
	 *            彩种ID
	 * @return
	 */
	public static String changeLottery_infoAgainst(String lotteryId) {
		return "{\"lotteryId\":\"" + lotteryId + "\"}";
	}

	// /** 投注 参数info的格式规范 */
	// public static String changeBet_info(String lotteryId, String isuseId,
	// int multiple, int share, long count, int assureShare,
	// int schemeBonusScale, String title, String description,
	// int secrecyLevel, long schemeSumMoney, long schemeSumNum,
	// int chase, double chaseBuyedMoney, String list, String playType)
	// {
	//
	// return "{\"description\":\"" + description + "\"," + "\"lotteryId\":\""
	// + lotteryId + "\",\"isuseId\":\"" + isuseId + "\","
	// + "\"multiple\":\"" + multiple + "\",\"share\":\"" + share
	// + "\",\"buyShare\":\"" + count + "\"," + "\"assureShare\":\""
	// + assureShare + "\",\"title\":\"" + title
	// + "\",\"secrecyLevel\":" + "\"" + secrecyLevel
	// + "\",\"schemeBonusScale\":\"" + schemeBonusScale
	// + "\",\"schemeSumMoney\":\"" + schemeSumMoney + "\","
	// + "\"schemeSumNum\":\"" + schemeSumNum + "\",\"chase\":\""
	// + chase + "\",\"chaseBuyedMoney\":\"" + chaseBuyedMoney + "\","
	// + "\"buyContent\":["
	// + getByContent(list, playType, schemeSumMoney, schemeSumNum)
	// + "],\"chaseList\": [] }";
	// }
	//
	// /** 得到 竞彩 的ByContent 参数 **/
	// private static String getByContent(String str, String playType,
	// long sumMoney, long count)
	// {
	// return "{\"lotteryNumber\":\"" + str + "\",\"playType\":" + playType
	// + ",\"sumMoney\": " + sumMoney + " ,\"sumNum\":" + count + "}";
	// }

	/** 投注 参数info的格式规范 */
	public static String changeBet_info2(String lotteryId, String isuseId,
			int multiple, int share, int buyShare, int assureShare,
			int schemeBonusScale, String title, String description,
			int secrecyLevel, double schemeSumMoney, long schemeSumNum,
			int chase, double chaseBuyedMoney, List<SelectedNumbers> list,
			int autoStopAtWinMoney) {
		if (chaseBuyedMoney < 0.1) {
			chaseBuyedMoney = schemeSumMoney;
			double schemeSumMoney2 = getSchemeSumMoney(list);
			return "{\"autoStopAtWinMoney\":\"" + autoStopAtWinMoney
					+ "\",\"lotteryId\":\"" + lotteryId + "\",\"isuseId\":\""
					+ isuseId + "\"," + "\"multiple\":\"" + multiple
					+ "\",\"share\":\"" + share + "\",\"buyShare\":\""
					+ buyShare + "\"," + "\"assureShare\":\"" + assureShare
					+ "\",\"title\":\"" + title + "\",\"description\":\""
					+ description + "\",\"secrecyLevel\":" + "\""
					+ secrecyLevel + "\",\"schemeBonusScale\":\""
					+ schemeBonusScale + "\",\"schemeSumMoney\":\""
					+ schemeSumMoney2 + "\"," + "\"schemeSumNum\":\""
					+ schemeSumNum + "\",\"chase\":\"" + chase
					+ "\",\"chaseBuyedMoney\":\"" + chaseBuyedMoney + "\","
					+ "\"buyContent\": [ " + getBuyContent(list)
					+ "], \"chaseList\": [" + "{\"isuseId\":\"" + isuseId
					+ "\",\"multiple\":\"" + AppTools.beiList.get(0) + "\","
					+ "\"money\":\"" + AppTools.beiList.get(0) * schemeSumNum
					* 2 + "\"}" + "] }";
		}
		double schemeSumMoney2 = getSchemeSumMoney(list);
		return "{\"autoStopAtWinMoney\":\"" + autoStopAtWinMoney
				+ "\",\"lotteryId\":\"" + lotteryId + "\",\"isuseId\":\""
				+ isuseId + "\"," + "\"multiple\":\"" + multiple
				+ "\",\"share\":\"" + share + "\",\"buyShare\":\"" + buyShare
				+ "\"," + "\"assureShare\":\"" + assureShare
				+ "\",\"title\":\"" + title + "\",\"description\":\""
				+ description + "\",\"secrecyLevel\":" + "\"" + secrecyLevel
				+ "\",\"schemeBonusScale\":\"" + schemeBonusScale
				+ "\",\"schemeSumMoney\":\"" + schemeSumMoney2 + "\","
				+ "\"schemeSumNum\":\"" + schemeSumNum + "\",\"chase\":\""
				+ chase + "\",\"chaseBuyedMoney\":\"" + chaseBuyedMoney + "\","
				+ "\"buyContent\": [ " + getBuyContent(list)
				+ "], \"chaseList\": [" + getChaseList(chase, schemeSumNum)
				+ "] }";
	}

	public static double getSchemeSumMoney(List<SelectedNumbers> list) {
		double sum = 0.0;
		for (int i = 0; i < list.size(); i++) {
			sum = sum + list.get(i).getMoney();
		}
		return sum;
	}

	public static String getBuyContent(List<SelectedNumbers> list) {
		StringBuffer str = new StringBuffer();
		int playType = AppTools.list_numbers.get(0).getPlayType();
		Log.i(TAG, "list大小--" + list.size());
		SelectedNumbers sNumber = null;
		for (int i = 0; i < list.size(); i++) {
			sNumber = list.get(i);

			if (i == 0) {

				System.out.println("||||||||" + list.get(i).getLotteryNumber());
				str.append("{\"playType\":\"" + list.get(i).getPlayType()
						+ "\",\"sumMoney\":\"" + list.get(i).getMoney()
						+ "\",\"sumNum\":\"" + list.get(i).getCount() + "\""
						+ ",\"lotteryNumber\":\""
						+ list.get(i).getLotteryNumber() + "\"}");
			} else {
				System.out.println("||||||||" + sNumber);
				str.append(",{\"playType\":\"" + list.get(i).getPlayType()
						+ "\",\"sumMoney\":\"" + list.get(i).getMoney()
						+ "\",\"sumNum\":\"" + list.get(i).getCount() + "\""
						+ ",\"lotteryNumber\":\""
						+ list.get(i).getLotteryNumber() + "\"}");
			}
		}
		return str.toString();

	}

	/** 时时彩投注 参数info的格式规范 */
	public static String changeBet_info_ssc(String lotteryId, String isuseId,
			int multiple, int share, int buyShare, int assureShare,
			int schemeBonusScale, String title, String description,
			int secrecyLevel, double schemeSumMoney, long schemeSumNum,
			int chase, double chaseBuyedMoney, List<SelectedNumbers> list,
			int autoStopAtWinMoney)

	{
		if (chaseBuyedMoney < 0.1) {
			chaseBuyedMoney = schemeSumMoney;
			double schemeSumMoney2 = getSchemeSumMoney(list);
			return "{\"autoStopAtWinMoney\":\"" + autoStopAtWinMoney
					+ "\",\"lotteryId\":\"" + lotteryId + "\",\"isuseId\":\""
					+ isuseId + "\"," + "\"multiple\":\"" + multiple
					+ "\",\"share\":\"" + share + "\",\"buyShare\":\""
					+ buyShare + "\"," + "\"assureShare\":\"" + assureShare
					+ "\",\"title\":\"" + title + "\",\"description\":\""
					+ description + "\",\"secrecyLevel\":" + "\""
					+ secrecyLevel + "\",\"schemeBonusScale\":\""
					+ schemeBonusScale + "\",\"schemeSumMoney\":\""
					+ schemeSumMoney2 + "\"," + "\"schemeSumNum\":\""
					+ schemeSumNum + "\",\"chase\":\"" + chase
					+ "\",\"chaseBuyedMoney\":\"" + chaseBuyedMoney + "\","
					+ "\"buyContent\": [ " + getBuyContent(list)
					+ "], \"chaseList\": [" + "{\"isuseId\":\"" + isuseId
					+ "\",\"multiple\":\"" + AppTools.beiList.get(0) + "\","
					+ "\"money\":\"" + AppTools.beiList.get(0) * schemeSumNum
					* 2 + "\"}" + "] }";
		}
		double schemeSumMoney2 = getSchemeSumMoney(list);
		return "{\"autoStopAtWinMoney\":\"" + autoStopAtWinMoney
				+ "\",\"lotteryId\":\"" + lotteryId + "\",\"isuseId\":\""
				+ isuseId + "\"," + "\"multiple\":\"" + multiple
				+ "\",\"share\":\"" + share + "\",\"buyShare\":\"" + buyShare
				+ "\"," + "\"assureShare\":\"" + assureShare
				+ "\",\"title\":\"" + title + "\",\"description\":\""
				+ description + "\",\"secrecyLevel\":" + "\"" + secrecyLevel
				+ "\",\"schemeBonusScale\":\"" + schemeBonusScale
				+ "\",\"schemeSumMoney\":\"" + schemeSumMoney2 + "\","
				+ "\"schemeSumNum\":\"" + schemeSumNum + "\",\"chase\":\""
				+ chase + "\",\"chaseBuyedMoney\":\"" + chaseBuyedMoney + "\","
				+ "\"buyContent\": [ " + getBuyContent_ssc(list)
				+ "], \"chaseList\": [" + getChaseList(chase, schemeSumNum)
				+ "] }";
	}

	public static String getBuyContent_ssc(List<SelectedNumbers> list) {
		StringBuffer str = new StringBuffer();
		Log.i(TAG, "list大小--" + list.size());
		SelectedNumbers sNumber = null;
		int y = 0;
		for (int i = 0; i < list.size(); i++) {
			sNumber = list.get(i);
			int playType = sNumber.getPlayType();
			if (2804 == playType || 6104 == playType) {// 大小单双
				String str1 = "";
				if (sNumber.getLotteryNumber().contains(",")) {
					String[] s = sNumber.getLotteryNumber().split(",");
					for (int j = 0; j < s[0].length(); j++) {
						for (int k = 0; k < s[1].length(); k++) {
							if (y == 0)
								str1 += s[0].substring(j, j + 1)
										+ s[1].substring(k, k + 1);
							else
								str1 += "\\n" + s[0].substring(j, j + 1)
										+ s[1].substring(k, k + 1);
							y++;
						}
					}
				} else {
					str1 = sNumber.getLotteryNumber();
				}
				if (i == 0) {
					str.append("{\"playType\":\"" + list.get(i).getPlayType()
							+ "\",\"sumMoney\":\"" + list.get(i).getMoney()
							+ "\",\"sumNum\":\"" + list.get(i).getCount()
							+ "\"" + ",\"lotteryNumber\":\"" + str1 + "\"}");
				} else {
					str.append(",{\"playType\":\"" + list.get(i).getPlayType()
							+ "\",\"sumMoney\":\"" + list.get(i).getMoney()
							+ "\",\"sumNum\":\"" + list.get(i).getCount()
							+ "\"" + ",\"lotteryNumber\":\"" + str1 + "\"}");
				}
			} else {
				if (i == 0) {
					str.append("{\"playType\":\"" + list.get(i).getPlayType()
							+ "\",\"sumMoney\":\"" + list.get(i).getMoney()
							+ "\",\"sumNum\":\"" + list.get(i).getCount()
							+ "\"" + ",\"lotteryNumber\":\""
							+ list.get(i).getLotteryNumber() + "\"}");
				} else {
					str.append(",{\"playType\":\"" + list.get(i).getPlayType()
							+ "\",\"sumMoney\":\"" + list.get(i).getMoney()
							+ "\",\"sumNum\":\"" + list.get(i).getCount()
							+ "\"" + ",\"lotteryNumber\":\""
							+ list.get(i).getLotteryNumber() + "\"}");
				}
			}
		}
		return str.toString();

	}

	// /** 投注 参数info的格式规范 */
	// public static String changeBet_info3(String lotteryId, String isuseId,
	// int multiple, int share, int buyShare, int assureShare,
	// int schemeBonusScale, String title, String description,
	// int secrecyLevel, double schemeSumMoney, long schemeSumNum,
	// int chase, double chaseBuyedMoney, List<SelectedNumbers> list,
	// int autoStopAtWinMoney) {
	//
	// return "{\"autoStopAtWinMoney\":\"" + autoStopAtWinMoney
	// + "\",\"lotteryId\":\"" + lotteryId + "\",\"isuseId\":\""
	// + isuseId + "\"," + "\"multiple\":\"" + multiple
	// + "\",\"share\":\"" + share + "\",\"buyShare\":\"" + buyShare
	// + "\"," + "\"assureShare\":\"" + assureShare
	// + "\",\"title\":\"" + title + "\",\"description\":\""
	// + description + "\",\"secrecyLevel\":" + "\"" + secrecyLevel
	// + "\",\"schemeBonusScale\":\"" + schemeBonusScale
	// + "\",\"schemeSumMoney\":\"" + schemeSumMoney + "\","
	// + "\"schemeSumNum\":\"" + schemeSumNum + "\",\"chase\":\""
	// + chase + "\",\"chaseBuyedMoney\":\"" + chaseBuyedMoney + "\","
	// + "\"buyContent\": [ " + getBuyContent2(list)
	// + "], \"chaseList\": ["
	// // + getChaseList(chase, multiple, schemeSumMoney)
	// + "] }";
	// }

	public static String getBuyContent2(List<SelectedNumbers> list) {
		StringBuffer str = new StringBuffer();
		Log.i(TAG, "list大小--" + list.size());
		SelectedNumbers sNumber = null;
		for (int i = 0; i < list.size(); i++) {
			sNumber = list.get(i);
			int playType = AppTools.list_numbers.get(0).getPlayType();
			System.out.println("////////" + playType);
			int type = AppTools.list_numbers.get(0).getType();

			if (i == 0) {

				str.append("{\"playType\":\"" + list.get(i).getPlayType()
						+ "\",\"sumMoney\":\"" + list.get(i).getMoney()
						+ "\",\"sumNum\":\"" + list.get(i).getCount() + "\""
						+ ",\"lotteryNumber\":\"" + getLotteryNum2(list)
						+ "\"}");

			} else {

				str.append(",{\"playType\":\"" + list.get(i).getPlayType()
						+ "\",\"sumMoney\":\"" + list.get(i).getMoney()
						+ "\",\"sumNum\":\"" + list.get(i).getCount() + "\""
						+ ",\"lotteryNumber\":\"" + getLotteryNum2(list)
						+ "\"}");

			}

		}
		return str.toString();

	}

	/** 得到追号的信息 **/
	public static String getChaseList(int num, long sumCount) {
		String str = "";
		if (num <= 1)
			return str;
		else {
			for (int i = 0; i < num; i++) {
				str += ",{\"isuseId\":\""
						+ AppTools.lottery.getDtCanChaseIsuses().get(i)
						+ "\",\"multiple\":\"" + AppTools.beiList.get(i)
						+ "\"," + "\"money\":\"" + AppTools.beiList.get(i)
						* sumCount * 2 + "\"}";
			}
		}
		if (str.length() > 1)
			str = str.substring(1);
		return str;
	}

	/** 拿到投注的信息 **/
	private static String getLotteryNum(List<SelectedNumbers> list) {
		String str = "";
		SelectedNumbers sNumber = null;
		for (int i = 0; i < list.size(); i++) {
			sNumber = list.get(i);
			if (i == 0) {
				str = sNumber.getLotteryNumber().trim();
			} else {
				str += "\\n" + sNumber.getLotteryNumber().trim();
			}
		}
		return str;
	}

	/** 拿到投注的信息 **/
	public static String getLotteryNum2(List<SelectedNumbers> list) {
		String str = "";
		int y = 0;
		SelectedNumbers sNumber = null;
		for (int i = 0; i < list.size(); i++) {
			sNumber = list.get(i);
			Log.i("x", "大小单双    " + sNumber.getLotteryNumber());
			if (sNumber.getLotteryNumber().contains(","))

			{
				String[] s = sNumber.getLotteryNumber().split(",");
				for (int j = 0; j < s[0].length(); j++) {
					for (int k = 0; k < s[1].length(); k++) {
						if (y == 0)
							str += s[0].substring(j, j + 1)
									+ s[1].substring(k, k + 1);
						else
							str += "\\n" + s[0].substring(j, j + 1)
									+ s[1].substring(k, k + 1);
						y++;
					}
				}
			} else {
				str = sNumber.getLotteryNumber();
			}
		}
		Log.i("x", "大小单双  lottery值     " + str);
		return str;
	}

	/**
	 * 拿到投注的详情
	 * 
	 * @param list
	 * @param playType
	 *            501 503
	 * @param sumMoney
	 * @param sumNum
	 * @param lotteryNumber
	 * @return
	 */
	public String getLotteryInfo(List<SelectedNumbers> list, int playType,
			long sumMoney, int sumNum, String lotteryNumber) {

		StringBuffer sbuffer = new StringBuffer();

		for (int i = 0; i < list.size(); i++) {
			if (i == list.size() - 1)
				sbuffer.append("{\"playType\":\"" + playType
						+ "\",\"sumMoney\":\"" + sumMoney + "\",\"sumNum\":\""
						+ sumNum + "\",\"lotteryNumber\":\"" + lotteryNumber
						+ "\"}");
			else
				sbuffer.append("{\"playType\":\"" + playType
						+ "\",\"sumMoney\":\"" + sumMoney + "\",\"sumNum\":\""
						+ sumNum + "\",\"lotteryNumber\":\"" + lotteryNumber
						+ "\"},");
		}
		return sbuffer.toString();
	}

	/**
	 * 获得中奖信息 参数Info的规范
	 * 
	 * @param type
	 *            查询类型 -1 表示查询全部，1 表示查询最新的 2 表示按照时间段查询。
	 * @param lotteryID
	 *            彩种ID
	 * @param pageIndex
	 *            页码
	 * @param pageSize
	 *            每页条数
	 * @param sort
	 *            排序规则
	 * @param sortType
	 *            排序类型
	 * @param searchTotal
	 *            查询记录数 (searchType的值为1时候有效)
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static String changeWinLottery_info(int type, String lotteryID,
			int pageIndex, int pageSize, int sort, int sortType,
			int searchTotal, String startTime, String endTime) {
		return "{\"searchType\":\"" + type + "\",\"lotteryID\":\"" + lotteryID
				+ "\",\"pageIndex\":\"" + pageIndex + "\"" + ",\"pageSize\":\""
				+ pageSize + "\",\"sort\":\"" + sort + "\",\"sortType\":\""
				+ sortType + "\"," + "\"searchTotal\":\"" + searchTotal
				+ "\",\"startTime\":\"" + startTime + "\"" + ",\"endTime\":\""
				+ endTime + "\"}";
	}

	/**
	 * 获取彩票资讯
	 * 
	 * @param pageIndex
	 *            索引
	 * @param pageSize
	 *            总数量
	 * @param infoType
	 *            资讯类型
	 * @return
	 */
	public static String getLotteryInformation(int pageIndex, int pageSize,
			int infoType) {
		return "{\"requestType\":\"" + 1 + "\",\"pageIndex\":\"" + pageIndex
				+ "\",\"pageSize\":\"" + pageSize + "\"" + ",\"infoType\":\""
				+ infoType + "\"}";
	}

	/**
	 * 得到资讯类容
	 * 
	 * @param infoType
	 *            资讯类型
	 * @param currentId
	 *            资讯的id
	 * @return
	 */
	public static String getInformationDetail(int infoType, long currentId) {
		return "{\"requestType\":\"2\" ,\"infoType\":\"" + infoType
				+ "\" ,\"informationId\":\"" + currentId + "\"}";
	}

	/**
	 * 拿到所有合买信息 info参数的规范化
	 * 
	 * @param lotteryId
	 *            彩种id
	 * @param pageIndex
	 *            页码
	 * @param pageSize
	 *            每页显示行数
	 * @param sort
	 *            排序
	 * @param sortType
	 *            排序类型
	 * @return
	 */
	public static String changeJoin_info(String lotteryId, int pageIndex,
			int pageSize, int sort, int sortType) {
		return "{\"lotteryId\":\"" + lotteryId + "\", \"pageIndex\":\""
				+ pageIndex + "\"" + ", \"pageSize\":\"" + pageSize
				+ "\", \"sort\":\"" + sort + "\", \"sortType\":\"" + sortType
				+ "\"}";
	}

	/**
	 * 加入合买信息 info参数的规范化
	 * 
	 * @param schemeId
	 * 
	 * @param buyShare
	 * 
	 * @param shareMoney
	 * 
	 * @return
	 */
	public static String changeFollow_info(String schemeId, int buyShare,
			long shareMoney) {
		return "{\"schemeId\":\"" + schemeId + "\",\"buyShare\":\"" + buyShare
				+ "\",\"shareMoney\":\"" + shareMoney + "\"}";
	}

	/**
	 * opt 70 传id
	 * 
	 * @param realityName
	 * @return
	 */
	public static String follow_info(String id) {
		return "{\"id\":\"" + id + "\"}";
	}

	/**
	 * opt 43 完善个人信息 格式化 info参数
	 * 
	 * @param realityName
	 * @param idCardNO
	 * @return
	 */
	public static String changeImprove_info(String realityName) {
		return "{\"realityName\":\"" + realityName + "\"}";
	}

	/**
	 * 银行卡信息绑定 info参数的规范化
	 * 
	 * @param bankTypeId
	 *            银行类型的id
	 * @param bankId
	 *            支行名称id
	 * @param bankCardNumber
	 *            银行卡账号
	 * @param bankInProvinceId
	 *            银行所在省id
	 * @param bankInCityId
	 *            银行所在市id
	 * @param bankUserName
	 *            开户名
	 * @param securityQuestionId
	 *            安全问题id
	 * @param securityQuestionAnswer
	 *            安全问题的答案
	 * @return
	 */
	public static String changeBankinfo_info(String bankTypeId, String bankId,
			String bankCardNumber, String bankInProvinceId,
			String bankInCityId, String bankUserName,
			String securityQuestionId, String securityQuestionAnswer) {
		String info = "{\"bankTypeId\":\"" + bankTypeId + "\",\"bankId\":\""
				+ bankId + "\"," + "\"bankCardNumber\":\"" + bankCardNumber
				+ "\",\"bankInProvinceId\":" + "\"" + bankInProvinceId
				+ "\",\"bankInCityId\":\"" + bankInCityId + "\","
				+ "\"bankUserName\":\"" + bankUserName
				+ "\",\"securityQuestionId\":\"" + securityQuestionId + "\","
				+ "\"securityQuestionAnswer\":\"" + securityQuestionAnswer
				+ "\"}";
		return info;
	}

	public static String changeAlipayInfo_info(String alipayName,
			String alipayUserName, String securityQuestionId,
			String securityQuestionAnswer) {
		String info = "{\"alipayName\":\"" + alipayName
				+ "\",\"alipayUserName\":\"" + alipayUserName
				+ "\",\"securityQuestionId\":\"" + securityQuestionId + "\","
				+ "\"securityQuestionAnswer\":\"" + securityQuestionAnswer
				+ "\",\"realityName\":\"" + AppTools.user.getRealityName()
				+ "\"}";
		return info;

	}

	/**
	 * 提款申请 info参数的规范化
	 * 
	 * @param money
	 *            提款金额
	 * @param securityQuestionId
	 *            安全问题id
	 * @param securityQuestionAnswer
	 *            安全问题的答案
	 * @return
	 */
	public static String changeWithdrawal_info(String money,
			String securityQuestionId, String securityQuestionAnswer) {
		String info = "{\"money\":\"" + money + "\",\"securityQuestionId\":\""
				+ securityQuestionId + "\"," + "\"securityQuestionAnswer\":\""
				+ securityQuestionAnswer + "\"}";
		return info;
	}

	/**
	 * 提款记录 info参数的规范化
	 * 
	 * @param searchType
	 *            查询类型
	 * @param pageIndex
	 *            页码
	 * @param pageSize
	 *            每页条数
	 * @param sort
	 *            排序规则 0 根据ID排序 1 根据提款时间排序 2 根据提款金额排序
	 * @param sortType
	 *            排序类型 0 降序 1 升序
	 * @param searchTotal
	 *            查询记录数
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	public static String changeJilu_info(String searchType, int pageIndex,
			int pageSize, String sort, String sortType, long searchTotal,
			String startTime, String endTime) {
		String info = "{\"searchType\":\"" + searchType + "\",\"pageIndex\":\""
				+ pageIndex + "\"," + "\"pageSize\":\"" + pageSize
				+ "\",\"sort\":\"" + sort + "\",\"sortType\":\"" + sortType
				+ "\"," + "\"searchTotal\":\"" + searchTotal
				+ "\",\"startTime\":\"" + startTime + "\"," + "\"endTime\":\""
				+ endTime + "\"}";
		return info;
	}

	/**
	 * 获取支行 信息 info参数的规范化
	 * 
	 * @param cityId
	 * @param bankTypeId
	 * @return
	 */
	public static String changeZhihang_info(String cityId, String bankTypeId) {
		String info = "{\"cityId\":\"" + cityId + "\",\"bankTypeId\":\""
				+ bankTypeId + "\"}";
		return info;
	}

	/** 得到竞彩开奖结果 info **/
	public static String changeJcLottery_info(String lotteryId, String time) {
		return "{\"lotteryId\":\"" + lotteryId + "\" ,\"lastDay\":\"" + time
				+ " 00:00:00\"}";
	}

	/** 得到站内信 info */
	public static String changeMsg_info(int typeId, int pageIndex,
			int pageSize, int isRead) {
		return "{\"typeId\":\"" + typeId + "\", \"pageIndex\":\"" + pageIndex
				+ "\", \"pageSize\":\"" + pageSize + "\", \"isRead\":\""
				+ isRead + "\", \"sortType\":\"0\"}";
	}

	/**
	 * 账户明细
	 * 
	 * @param searchCondition
	 *            searchCondition -1 表示查询全部， 1 表示查询收入， 2 表示查询支出
	 * @param pageIndex
	 * @param pageSize
	 * @param sortType
	 *            0 降序 1 升序
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static String changeFundsInfo_info(int searchCondition,
			int pageIndex, int pageSize, String sortType, String startTime,
			String endTime) {
		return "{\"searchCondition\":\"" + searchCondition
				+ "\" ,\"pageIndex\":\"" + pageIndex + "\","
				+ "\"pageSize\":\"" + pageSize + "\" ,\"sortType\":\""
				+ sortType + "\" ," + "\"startTime\":\"" + startTime
				+ "\" ,\"endTime\":\"" + endTime + "\"}";
	}

	/** 反馈建议 info **/
	public static String changeSuggest_info(String tel, String email,
			String content, String title) {
		return "{\"tel\":\"" + tel + "\" ,\"email\":\"" + email + "\" ,"
				+ "\"title\":\"" + title + "\" ,\"content\":\"" + content
				+ "\"}";
	}

	/** 改变站内信状态 info **/
	public static String changeMsg_info(int id, int type) {
		return "{\"ID\":\"" + id + "\",\"operateType\":\"" + type + "\"}";
	}

	// {opt:"28",auth:"{"loginType":"1","app_key":"123456","imei":"444012","os":"Iphone
	// os","os_version":"5.0","app_version":"1.0.0","source_id":"Yek_test","ver":"0.9","uid":"-1","crc":"3e64055bf4056d1dc68b85dd4365d649","time_stamp":"20090310113016"}",info:"{"ID":"1666","operateType":"0"}"}

	/**
	 * 获得竞彩 开奖信息
	 * 
	 * @param schemeId
	 * @return
	 */
	public static String changeJC_info(String schemeId) {
		String info = "{\"schemeId\":\"" + schemeId + "\"}";
		return info;
	}

	/** 得到手机识别码 */
	public static String getIMEI(Context context) {
		TelephonyManager mTelephonyMgr = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);
		String imei = mTelephonyMgr.getDeviceId();
		return imei;
	}

	/** 将当前时间 格式化 */
	public static String getTime() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}

	/** 得到 特定 key --- Crc */
	public static String getCrc(String time, String imei, String key,
			String info, String uid) {
		// crc = MD5(auth.time_stamp + auth.imei + auth.uid +MD5（password+key）+
		// Info, "utf-8")
		return MD5.md5(time + imei + uid + key + info);
	}

}
