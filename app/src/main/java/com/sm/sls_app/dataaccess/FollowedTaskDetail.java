package com.sm.sls_app.dataaccess;

import java.io.Serializable;

/**功能：追号订单明细类 版本*/
public class FollowedTaskDetail implements Serializable{

	private String id; // 任务明细Id
	private String chaseTaskID; // 任务Id
	private String dateTime; // 任务发起时间
	private String playTypeId; // 玩法Id
	private String playTypeName; // 玩法名
	private String isuseId; // 奖期ＩＤ
	private String IsuseName; // 奖期名
	private int multiple; // 倍数
	private double money; // 明细金额
	private int quashStatus; // 撤单状态（0 表示未撤单；1表示已撤单;2 表示系统撤销）
	private String SchemeID; // 方案ID
	private String lotteryNumber; // 追号内容

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChaseTaskID() {
		return chaseTaskID;
	}

	public void setChaseTaskID(String chaseTaskID) {
		this.chaseTaskID = chaseTaskID;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getPlayTypeId() {
		return playTypeId;
	}

	public void setPlayTypeId(String playTypeId) {
		this.playTypeId = playTypeId;
	}

	public String getPlayTypeName() {
		return playTypeName;
	}

	public void setPlayTypeName(String playTypeName) {
		this.playTypeName = playTypeName;
	}

	public String getIsuseId() {
		return isuseId;
	}

	public void setIsuseId(String isuseId) {
		this.isuseId = isuseId;
	}

	public String getIsuseName() {
		return IsuseName;
	}

	public void setIsuseName(String isuseName) {
		IsuseName = isuseName;
	}

	public int getMultiple() {
		return multiple;
	}

	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public int getQuashStatus() {
		return quashStatus;
	}

	public void setQuashStatus(int quashStatus) {
		this.quashStatus = quashStatus;
	}

	public String getSchemeID() {
		return SchemeID;
	}

	public void setSchemeID(String schemeID) {
		SchemeID = schemeID;
	}

	public String getLotteryNumber() {
		return lotteryNumber;
	}

	public void setLotteryNumber(String lotteryNumber) {
		this.lotteryNumber = lotteryNumber;
	}

}
