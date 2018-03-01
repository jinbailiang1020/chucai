package com.sm.sls_app.dataaccess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** 开奖号码 */
public class WinLottery implements Serializable {

	private static final long serialVersionUID = 1L;
	private String lotteryId; // 彩种id
	private String lotteryName; // 彩种名称
	private String id; // id
	private String name; // 奖期
	private String stateUpdateTime; // 更新时间
	private String endTime; // 期号
	private String redNum; // 红色号码
	private String blueNum; // 蓝色号码
	private String totalMoney; //
	private String sales; //
	private List<WinDetail> listWinDetail; // 开奖号码
	private String dtWinNumberInfo;

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getSales() {
		return sales;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setSales(String sales) {
		this.sales = sales;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStateUpdateTime() {
		return stateUpdateTime;
	}

	public void setStateUpdateTime(String stateUpdateTime) {
		this.stateUpdateTime = stateUpdateTime;
	}

	public String getRedNum() {
		return redNum;
	}

	public void setRedNum(String redNum) {
		this.redNum = redNum;
	}

	public String getBlueNum() {
		return blueNum;
	}

	public void setBlueNum(String blueNum) {
		this.blueNum = blueNum;
	}

	public String getDtWinNumberInfo() {
		return dtWinNumberInfo;
	}

	public void setDtWinNumberInfo(String dtWinNumberInfo) {
		this.dtWinNumberInfo = dtWinNumberInfo;
	}

	public List<WinDetail> getListWinDetail() {
		return listWinDetail;
	}

	public void setListWinDetail(List<WinDetail> listWinDetail) {
		this.listWinDetail = new ArrayList<WinDetail>();
		for (WinDetail detail : listWinDetail) {
			this.listWinDetail.add(detail);
		}
	}

}
