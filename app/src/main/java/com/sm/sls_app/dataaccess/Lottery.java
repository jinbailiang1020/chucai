package com.sm.sls_app.dataaccess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** 功能：彩票实体类 版本 */
public class Lottery implements Serializable {
  private String lotteryID; // 彩种id
  private String lotteryName; // 彩种名称
  private String lotteryDescription; // 描述
  private String lotteryAgainst; // 竞彩最新对阵
  private String isuseId; // 该彩种当前期号id
  private String isuseName; // 奖期名称
  private String starttime; // 奖期开始时间
  private String endtime; // 奖期结束时间
  private String lastIsuseName; // 最后已开奖的期名
  private String lastWinNumber; // 最后已开奖的号码
  private String currIsuseEndDateTime;// 开奖截至时间
  private String originalTime; //
  // private long distanceTime2;
  private String explanation; // 该彩奖池
  private List<String> dtCanChaseIsuses; // 该彩种可追奖期数
  private List<String> isuseNameList;

  private long distanceTime; // 倒计时的时间
  private long distanceTime2;// 选好倒计时时间
  private List<String> preSaleInfo; // 预售期期号信息

  private int type;

  private List<List<DtMatch>> list_Matchs; // 比赛对阵信息

  private List<List<DtMatch_Basketball>> DtMatch_Basketball; // 比赛对阵信息

  private List<List<DtMatch>> list_singlepass_Matchs; // 单关比赛对阵信息

  private String dtmatch; // 第一场比赛

  private int isChase;// 是否是追号 0.普通 1.追号
  private int chaseTaskID;// 当前投注追号任务号
  private List<String> beiList;// 倍数的集合

  public int getIsChase() {
    return isChase;
  }

  public void setIsChase(int isChase) {
    this.isChase = isChase;
  }

  public int getChaseTaskID() {
    return chaseTaskID;
  }

  public void setChaseTaskID(int chaseTaskID) {
    this.chaseTaskID = chaseTaskID;
  }

  public List<List<DtMatch_Basketball>> getDtMatch_Basketball() {
    return DtMatch_Basketball;
  }

  public void setDtMatch_Basketball(
      List<List<DtMatch_Basketball>> dtMatch_Basketball) {
    DtMatch_Basketball = dtMatch_Basketball;
  }

  public String getLotteryDescription() {
    return lotteryDescription;
  }

  public void setLotteryDescription(String lotteryDescription) {
    this.lotteryDescription = lotteryDescription;
  }

  public String getLotteryAgainst() {
    return lotteryAgainst;
  }

  public void setLotteryAgainst(String lotteryAgainst) {
    this.lotteryAgainst = lotteryAgainst;
  }

  public List<List<DtMatch>> getList_singlepass_Matchs() {
    return list_singlepass_Matchs;
  }

  public void setList_singlepass_Matchs(
      List<List<DtMatch>> list_singlepass_Matchs) {
    this.list_singlepass_Matchs = list_singlepass_Matchs;
  }

  public String getDtmatch() {
    return dtmatch;
  }

  public void setDtmatch(String dtmatch) {
    this.dtmatch = dtmatch;
  }

  public String getExplanation() {
    return explanation;
  }

  public void setExplanation(String explanation) {
    this.explanation = explanation;
  }

  public long getDistanceTime() {
    return distanceTime;
  }

  public void setDistanceTime(long distanceTime) {
    this.distanceTime = distanceTime;
  }

  public long getDistanceTime2() {
    return distanceTime2;
  }

  public void setDistanceTime2(long distanceTime2) {
    this.distanceTime2 = distanceTime2;
  }

  public String getLotteryID() {
    return lotteryID;
  }

  public void setLotteryID(String lotteryID) {
    this.lotteryID = lotteryID;
  }

  public String getIsuseId() {
    return isuseId;
  }

  public void setIsuseId(String isuseId) {
    this.isuseId = isuseId;
  }

  public String getIsuseName() {
    return isuseName;
  }

  public void setIsuseName(String isuseName) {
    this.isuseName = isuseName;
  }

  public String getStarttime() {
    return starttime;
  }

  public void setStarttime(String starttime) {
    this.starttime = starttime;
  }

  public String getEndtime() {
    return endtime;
  }

  public void setEndtime(String endtime) {
    this.endtime = endtime;
  }

  public String getLastIsuseName() {
    return lastIsuseName;
  }

  public void setLastIsuseName(String lastIsuseName) {
    this.lastIsuseName = lastIsuseName;
  }

  public String getLastWinNumber() {
    return lastWinNumber;
  }

  public void setLastWinNumber(String lastWinNumber) {
    this.lastWinNumber = lastWinNumber;
  }

  public List<String> getDtCanChaseIsuses() {
    return dtCanChaseIsuses;
  }

  public void setDtCanChaseIsuses(List<String> dtCanChaseIsuses) {
    this.dtCanChaseIsuses = new ArrayList<String>();
    for (String str : dtCanChaseIsuses) {
      this.dtCanChaseIsuses.add(str);
    }
  }

  public List<String> getIsuseNameList() {
    return isuseNameList;
  }

  public void setIsuseNameList(List<String> isuseNameList) {
    this.isuseNameList = new ArrayList<String>();
    for (String str : isuseNameList) {
      this.isuseNameList.add(str);
    }
  }

  public List<String> getPreSaleInfo() {
    return preSaleInfo;
  }

  public void setPreSaleInfo(List<String> preSaleInfo) {
    this.preSaleInfo = new ArrayList<String>();
    for (String str : preSaleInfo) {
      this.preSaleInfo.add(str);
    }
  }

  public String getLotteryName() {
    return lotteryName;
  }

  public void setLotteryName(String lotteryName) {
    this.lotteryName = lotteryName;
  }

  public List<List<DtMatch>> getList_Matchs() {
    return list_Matchs;
  }

  public void setList_Matchs(List<List<DtMatch>> _list_Matchs) {
    list_Matchs = new ArrayList<List<DtMatch>>();
    for (List<DtMatch> list : _list_Matchs) {
      List<DtMatch> listM = new ArrayList<DtMatch>();
      for (DtMatch dt : list) {
        listM.add(dt);
      }
      list_Matchs.add(listM);
    }
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getOriginalTime() {
    return originalTime;
  }

  public void setOriginalTime(String originalTime) {
    this.originalTime = originalTime;
  }

  public List<String> getBeiList() {
    return beiList;
  }

  public void setBeiList(List<String> beiList) {
    this.beiList = beiList;
  }

  public String getCurrIsuseEndDateTime() {
    return currIsuseEndDateTime;
  }

  public void setCurrIsuseEndDateTime(String currIsuseEndDateTime) {
    this.currIsuseEndDateTime = currIsuseEndDateTime;
  }

  // public long getDistanceTime2() {
  // return distanceTime2;
  // }
  //
  // public void setDistanceTime2(long distanceTime2) {
  // this.distanceTime2 = distanceTime2;
  // }

}
