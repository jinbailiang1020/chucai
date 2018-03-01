package com.sm.sls_app.dataaccess;

import java.io.Serializable;
import java.util.List;

/**投注方案*/
public class BetSchemes implements Serializable{
	private String lotteryId;
	private String isuseId; // 奖期ID
	private String multiple; // 投注倍数
	private int share; // 方案总份数
	private int buyShare; // 购买份数
	private int assureShare; // 保底份数
	private int schemeBonusScalc; // 佣金比
	private String title; // 方案标题
	private int secrecyLevel; // 保密类型
	private long schemeSumMoney; // 方案金额
	private long schemeSumNum; // 方案总注数
	private int chase; // 是否追号
	private long chaseBuyedMoney; // 追号总金额

	
	private List<SelectedNumbers> buyContent; // 投注内容
	private String chaseList; // 追号信息

	public String getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}

	public String getIsuseId() {
		return isuseId;
	}

	public void setIsuseId(String isuseId) {
		this.isuseId = isuseId;
	}

	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}

	public int getShare() {
		return share;
	}

	public void setShare(int share) {
		this.share = share;
	}

	public int getBuyShare() {
		return buyShare;
	}

	public void setBuyShare(int buyShare) {
		this.buyShare = buyShare;
	}

	public int getAssureShare() {
		return assureShare;
	}

	public void setAssureShare(int assureShare) {
		this.assureShare = assureShare;
	}

	public int getSchemeBonusScalc() {
		return schemeBonusScalc;
	}

	public void setSchemeBonusScalc(int schemeBonusScalc) {
		this.schemeBonusScalc = schemeBonusScalc;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getSecrecyLevel() {
		return secrecyLevel;
	}

	public void setSecrecyLevel(int secrecyLevel) {
		this.secrecyLevel = secrecyLevel;
	}

	public long getSchemeSumMoney() {
		return schemeSumMoney;
	}

	public void setSchemeSumMoney(long schemeSumMoney) {
		this.schemeSumMoney = schemeSumMoney;
	}

	public long getSchemeSumNum() {
		return schemeSumNum;
	}

	public void setSchemeSumNum(long schemeSumNum) {
		this.schemeSumNum = schemeSumNum;
	}

	public int getChase() {
		return chase;
	}

	public void setChase(int chase) {
		this.chase = chase;
	}

	public long getChaseBuyedMoney() {
		return chaseBuyedMoney;
	}

	public void setChaseBuyedMoney(long chaseBuyedMoney) {
		this.chaseBuyedMoney = chaseBuyedMoney;
	}

	public List<SelectedNumbers> getBuyContent() {
		return buyContent;
	}

	public void setBuyContent(List<SelectedNumbers> buyContent) {
		this.buyContent = buyContent;
	}

	public String getChaseList() {
		return chaseList;
	}

	public void setChaseList(String chaseList) {
		this.chaseList = chaseList;
	}

}
