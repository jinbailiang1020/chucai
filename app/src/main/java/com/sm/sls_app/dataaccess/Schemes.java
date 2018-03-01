package com.sm.sls_app.dataaccess;

import java.io.Serializable;
import java.util.List;

/** 功能：方案实体类 版本 */
public class Schemes implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 基本数据 */
	private String id; // 方案ID
	private String schemeNumber; // 订单号
	private double money; // 方案总金额
	private String lotteryID; // 彩种ＩＤ
	private String lotteryName; // 彩种名称
	private int playTypeID; // 玩法ID
	private String playTypeName; // 玩法名称
	private String countNotes;// 玩法注数

	private String lotteryNumber; // 投注内容
	private List<LotteryContent> content_lists; // 投注玩法内容
	private String dateTime; // 方案发起时间
	private int multiple; // 倍数
	private String isuseID; // 奖期ID
	private String winNumber; // 开奖号码
	private String isuseName; // 奖期名称
	private int SerialNumber; // 数据在全部数据中的位置
	private int RecordCount; // 总记录数
	private int FromClient; // 来自哪？ 1表示来自网页，2表示来自手机应用

	/** 合买数据 */
	private int shareMoney; // 每份金额
	private int share; // 每份数
	private int surplusShare; // 剩余份数
	private String initiateUserID; // 发起人ID
	private String initiateName; // 发起人名称
	private int level; // 发起人历史战绩
	private double assureMoney; // 保底金额
	private int assureShare; // 保底份数
	private String title; // 方案标题
	private double schemeBonusScale; // 佣金比
	private int secrecyLevel; // 保密设置 ( 0 不保密 1 到截止 2 到开奖 3 永远)
	private int schedule; // 进度
	private String description; // 方案描述
	private String myBuyMoney; // 认购金额
	private int myBuyShare; // 认购份数

	/** 状态 **/
	private int quashStatus; // 撒单状态 ( 0未撤单 )
	private double winMoneyNoWithTax; // 中奖状态
	private String schemeIsOpened; // 是否开奖 (False 表示未开奖，True表示已开奖)
	private String isPurchasing; // 是否代购 (True 代购 False 合买)
	private String buyed; // False 未出票 true 已出票
	private int isChase; // 是否是追号
	private double stopWhenWinMoney; // 中奖金额为多少时 停止追号

	/** 追号数据 */
	private int sumChaseCount; // 总共追多少期
	private double SumSchemeMoney; // 追号方案金额
	private boolean isExecuted; // 方案是否执行
	private int chaseTaskID; // 追号任务ID
	private String chaseTaskTime; // 执行追号任务时间
	private int sumCompletedCount; // 执行期数
	private int sumUnCompletedCount; // 未执行期数
	private int sumIsuseNum;//共多少期
	
	public int getSumIsuseNum() {
		return sumIsuseNum;
	}

	public void setSumIsuseNum(int sumIsuseNum) {
		this.sumIsuseNum = sumIsuseNum;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSchemeNumber() {
		return schemeNumber;
	}

	public void setSchemeNumber(String schemeNumber) {
		this.schemeNumber = schemeNumber;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public int getShareMoney() {
		return shareMoney;
	}

	public void setShareMoney(int shareMoney) {
		this.shareMoney = shareMoney;
	}

	public int getShare() {
		return share;
	}

	public void setShare(int share) {
		this.share = share;
	}

	public int getSurplusShare() {
		return surplusShare;
	}

	public void setSurplusShare(int surplusShare) {
		this.surplusShare = surplusShare;
	}

	public String getInitiateUserID() {
		return initiateUserID;
	}

	public String getCountNotes() {
		return countNotes;
	}

	public void setCountNotes(String countNotes) {
		this.countNotes = countNotes;
	}

	public void setInitiateUserID(String initiateUserID) {
		this.initiateUserID = initiateUserID;
	}

	public String getInitiateName() {
		return initiateName;
	}

	public void setInitiateName(String initiateName) {
		this.initiateName = initiateName;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public double getAssureMoney() {
		return assureMoney;
	}

	public void setAssureMoney(double assureMoney) {
		this.assureMoney = assureMoney;
	}

	public int getAssureShare() {
		return assureShare;
	}

	public void setAssureShare(int assureShare) {
		this.assureShare = assureShare;
	}

	public int getQuashStatus() {
		return quashStatus;
	}

	public void setQuashStatus(int quashStatus) {
		this.quashStatus = quashStatus;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getWinMoneyNoWithTax() {
		return winMoneyNoWithTax;
	}

	public void setWinMoneyNoWithTax(double winMoneyNoWithTax) {
		this.winMoneyNoWithTax = winMoneyNoWithTax;
	}

	public double getSchemeBonusScale() {
		return schemeBonusScale;
	}

	public void setSchemeBonusScale(double schemeBonusScale) {
		this.schemeBonusScale = schemeBonusScale;
	}

	public int getSecrecyLevel() {
		return secrecyLevel;
	}

	public void setSecrecyLevel(int secrecyLevel) {
		this.secrecyLevel = secrecyLevel;
	}

	public int getSchedule() {
		return schedule;
	}

	public void setSchedule(int schedule) {
		this.schedule = schedule;
	}

	public String getSchemeIsOpened() {
		return schemeIsOpened;
	}

	public void setSchemeIsOpened(String schemeIsOpened) {
		this.schemeIsOpened = schemeIsOpened;
	}

	public String getLotteryNumber() {
		return lotteryNumber;
	}

	public void setLotteryNumber(String lotteryNumber) {
		this.lotteryNumber = lotteryNumber;
	}

	public String getIsPurchasing() {
		return isPurchasing;
	}

	public void setIsPurchasing(String isPurchasing) {
		this.isPurchasing = isPurchasing;
	}

	public String getLotteryID() {
		return lotteryID;
	}

	public void setLotteryID(String lotteryID) {
		this.lotteryID = lotteryID;
	}

	public String getLotteryName() {
		return lotteryName;
	}

	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}

	public int getPlayTypeID() {
		return playTypeID;
	}

	public void setPlayTypeID(int playTypeID) {
		this.playTypeID = playTypeID;
	}

	public String getPlayTypeName() {
		return playTypeName;
	}

	public void setPlayTypeName(String playTypeName) {
		this.playTypeName = playTypeName;
	}

	public String getIsuseID() {
		return isuseID;
	}

	public void setIsuseID(String isuseID) {
		this.isuseID = isuseID;
	}

	public String getIsuseName() {
		return isuseName;
	}

	public void setIsuseName(String isuseName) {
		this.isuseName = isuseName;
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

	public String getWinNumber() {
		return winNumber;
	}

	public void setWinNumber(String winNumber) {
		this.winNumber = winNumber;
	}

	public String getBuyed() {
		return buyed;
	}

	public void setBuyed(String buyed) {
		this.buyed = buyed;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMultiple() {
		return multiple;
	}

	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}

	public int getIsChase() {
		return isChase;
	}

	public void setIsChase(int isChase) {
		this.isChase = isChase;
	}

	public String getMyBuyMoney() {
		return myBuyMoney;
	}

	public void setMyBuyMoney(String myBuyMoney) {
		this.myBuyMoney = myBuyMoney;
	}

	public int getMyBuyShare() {
		return myBuyShare;
	}

	public void setMyBuyShare(int myBuyShare) {
		this.myBuyShare = myBuyShare;
	}

	public int getFromClient() {
		return FromClient;
	}

	public void setFromClient(int fromClient) {
		FromClient = fromClient;
	}

	public int getSumChaseCount() {
		return sumChaseCount;
	}

	public void setSumChaseCount(int sumChaseCount) {
		this.sumChaseCount = sumChaseCount;
	}

	public double getSumSchemeMoney() {
		return SumSchemeMoney;
	}

	public void setSumSchemeMoney(double sumSchemeMoney) {
		SumSchemeMoney = sumSchemeMoney;
	}

	public boolean isExecuted() {
		return isExecuted;
	}

	public void setExecuted(boolean isExecuted) {
		this.isExecuted = isExecuted;
	}

	public int getChaseTaskID() {
		return chaseTaskID;
	}

	public void setChaseTaskID(int chaseTaskID) {
		this.chaseTaskID = chaseTaskID;
	}

	public String getChaseTaskTime() {
		return chaseTaskTime;
	}

	public void setChaseTaskTime(String chaseTaskTime) {
		this.chaseTaskTime = chaseTaskTime;
	}

	public double getStopWhenWinMoney() {
		return stopWhenWinMoney;
	}

	public void setStopWhenWinMoney(double stopWhenWinMoney) {
		this.stopWhenWinMoney = stopWhenWinMoney;
	}

	public int getSumCompletedCount() {
		return sumCompletedCount;
	}

	public void setSumCompletedCount(int sumCompletedCount) {
		this.sumCompletedCount = sumCompletedCount;
	}

	public int getSumUnCompletedCount() {
		return sumUnCompletedCount;
	}

	public void setSumUnCompletedCount(int sumUnCompletedCount) {
		this.sumUnCompletedCount = sumUnCompletedCount;
	}

	public List<LotteryContent> getContent_lists() {
		return content_lists;
	}

	public void setContent_lists(List<LotteryContent> content_lists) {
		this.content_lists = content_lists;
	}
}
