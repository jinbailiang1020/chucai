package com.sm.sls_app.dataaccess;

/** 显示的对阵信息 **/
public class LotteryDtMatch {
	private String id;
	private String matchNumber;
	private String weekDay;
	private String game;
	private String mainTeam;
	private String guestTeam;
	private String stopSellTime;
	private String matchDate;
	private int loseBall;

	private String spfResult;
	private String zjqResult;
	private String cbfResult;

	private String sfResult;
	private String dxResult;
	private String result;
	private int loseScore;

	public int getLoseScore() {
		return loseScore;
	}

	public void setLoseScore(int loseScore) {
		this.loseScore = loseScore;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMatchNumber() {
		return matchNumber;
	}

	public void setMatchNumber(String matchNumber) {
		this.matchNumber = matchNumber;
	}

	public String getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public String getMainTeam() {
		return mainTeam;
	}

	public void setMainTeam(String mainTeam) {
		this.mainTeam = mainTeam;
	}

	public String getGuestTeam() {
		return guestTeam;
	}

	public void setGuestTeam(String guestTeam) {
		this.guestTeam = guestTeam;
	}

	public String getStopSellTime() {
		return stopSellTime;
	}

	public void setStopSellTime(String stopSellTime) {
		this.stopSellTime = stopSellTime;
	}

	public String getMatchDate() {
		return matchDate;
	}

	public void setMatchDate(String marchDate) {
		this.matchDate = marchDate;
	}

	public String getSpfResult() {
		return spfResult;
	}

	public void setSpfResult(String spfResult) {
		this.spfResult = spfResult;
	}

	public String getZjqResult() {
		return zjqResult;
	}

	public void setZjqResult(String zjqResult) {
		this.zjqResult = zjqResult;
	}

	public String getCbfResult() {
		return cbfResult;
	}

	public void setCbfResult(String cbfResult) {
		this.cbfResult = cbfResult;
	}

	public String getSfResult() {
		return sfResult;
	}

	public void setSfResult(String sfResult) {
		this.sfResult = sfResult;
	}

	public String getDxResult() {
		return dxResult;
	}

	public void setDxResult(String dxResult) {
		this.dxResult = dxResult;
	}

	public int getLoseBall() {
		return loseBall;
	}

	public void setLoseBall(int loseBall) {
		this.loseBall = loseBall;
	}

}
