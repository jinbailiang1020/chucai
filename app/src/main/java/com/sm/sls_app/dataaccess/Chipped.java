package com.sm.sls_app.dataaccess;

import java.io.Serializable;

/**功能：合买实体类 版本*/
public class Chipped implements Serializable{

	private int lotteryID; // 合买的彩票ID

	private int totalMoney; // 合买总金额

	private int eachMoney; // 每份的金额

	private int commission; // 佣金比

	private String userName; // 发起者

	private String programTitle; // 方案标题

	private String programDescribe; // 方案描述

	private Schemes program; // 具体方案

	public int getLotteryID() {
		return lotteryID;
	}

	public void setLotteryID(int lotteryID) {
		this.lotteryID = lotteryID;
	}

	public int getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(int totalMoney) {
		this.totalMoney = totalMoney;
	}

	public int getEachMoney() {
		return eachMoney;
	}

	public void setEachMoney(int eachMoney) {
		this.eachMoney = eachMoney;
	}

	public int getCommission() {
		return commission;
	}

	public void setCommission(int commission) {
		this.commission = commission;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProgramTitle() {
		return programTitle;
	}

	public void setProgramTitle(String programTitle) {
		this.programTitle = programTitle;
	}

	public String getProgramDescribe() {
		return programDescribe;
	}

	public void setProgramDescribe(String programDescribe) {
		this.programDescribe = programDescribe;
	}

	public Schemes getProgram() {
		return program;
	}

	public void setProgram(Schemes program) {
		this.program = program;
	}

}
