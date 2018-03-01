package com.sm.sls_app.dataaccess;

import java.io.Serializable;

/**功能：追号订单类 版本*/ 
public class FollowedTask implements Serializable{

	private String id; // 任务Id
	private String dateTime; // 发起时间
	private String title; // 标题
	private String stopWhenWinMoney; // 停止任务条件（指奖金大于等于该值时，任务自动停止）
	private String lotteryId; // 彩种ＩＤ
	private String lotteryName; // 彩种名称
	private int SerialNumber; // 数据在全数据中的位置
	private int RecordCount; // 总记录数
	private int isuseCount; // 总期数
	private int completeCount; // 已完成期数
	private int quashCount; // 已撤销期数
	private double sumMoney; // 任务总金额
	private double buyedMoney; // 已完成金额
	private double quashedMoney; // 已撤销金额

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStopWhenWinMoney() {
		return stopWhenWinMoney;
	}

	public void setStopWhenWinMoney(String stopWhenWinMoney) {
		this.stopWhenWinMoney = stopWhenWinMoney;
	}

	public String getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}

	public String getLotteryName() {
		return lotteryName;
	}

	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}

	public int getSerialNumber() {
		return SerialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		SerialNumber = serialNumber;
	}

	public int getRecordCount() {
		return RecordCount;
	}

	public void setRecordCount(int recordCount) {
		RecordCount = recordCount;
	}

	public int getIsuseCount() {
		return isuseCount;
	}

	public void setIsuseCount(int isuseCount) {
		this.isuseCount = isuseCount;
	}

	public int getCompleteCount() {
		return completeCount;
	}

	public void setCompleteCount(int completeCount) {
		this.completeCount = completeCount;
	}

	public int getQuashCount() {
		return quashCount;
	}

	public void setQuashCount(int quashCount) {
		this.quashCount = quashCount;
	}

	public double getSumMoney() {
		return sumMoney;
	}

	public void setSumMoney(double sumMoney) {
		this.sumMoney = sumMoney;
	}

	public double getBuyedMoney() {
		return buyedMoney;
	}

	public void setBuyedMoney(double buyedMoney) {
		this.buyedMoney = buyedMoney;
	}

	public double getQuashedMoney() {
		return quashedMoney;
	}

	public void setQuashedMoney(double quashedMoney) {
		this.quashedMoney = quashedMoney;
	}

}
