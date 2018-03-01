package com.sm.sls_app.dataaccess;

import java.io.Serializable;

/**
 * @author 作者 : hxj
 * @version 创建时间：2016-1-28 上午8:44:58 类说明
 */
public class BuyContent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String PlayName;
	private String playType;
	private String lotteryNumber;
	private String sumNum;
	private String sumMoney;

	public String getPlayName() {
		return PlayName;
	}

	public void setPlayName(String playName) {
		PlayName = playName;
	}

	public String getPlayType() {
		return playType;
	}

	public void setPlayType(String playType) {
		this.playType = playType;
	}

	public String getLotteryNumber() {
		return lotteryNumber;
	}

	public void setLotteryNumber(String lotteryNumber) {
		this.lotteryNumber = lotteryNumber;
	}

	public String getSumNum() {
		return sumNum;
	}

	public void setSumNum(String sumNum) {
		this.sumNum = sumNum;
	}

	public String getSumMoney() {
		return sumMoney;
	}

	public void setSumMoney(String sumMoney) {
		this.sumMoney = sumMoney;
	}

}
