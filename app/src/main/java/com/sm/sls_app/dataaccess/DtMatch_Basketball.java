package com.sm.sls_app.dataaccess;

import java.io.Serializable;
import java.util.List;

/**投注方案*/
public class DtMatch_Basketball implements Serializable{

	private String matchId; //对阵ID
	private String matchNumber;
	private String matchDate;
	private String game;
	private String mainTeam;
	private String guestTeam;
	private String stopSellTime;   //停售时间
	private String matchDate1;
	private String mainLose;
	private String mainWin;
	private String bigSmallScore;
	private String small;
	private String big;
	private String letScore;
	private String letMainLose;
	private String letMainWin;
	private String matchDate2;
	
	private String differGuest1_5;
	private String differGuest6_10;
	private String differGuest11_15;
	private String differGuest16_20;
	private String differGuest21_25;
	private String differGuest26;
	private String differMain1_5;
	private String differMain6_10;
	private String differMain11_15;
	private String  differMain16_20;
	private String differMain21_25;
	private String  differMain26;
	private boolean  isSF;
	private boolean isDXF;
	private boolean isRFSF;
	private boolean isSFC;
	public String getMatchId() {
		return matchId;
	}
	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}
	public String getMatchNumber() {
		return matchNumber;
	}
	public void setMatchNumber(String matchNumber) {
		this.matchNumber = matchNumber;
	}
	public String getMatchDate() {
		return matchDate;
	}
	public void setMatchDate(String matchDate) {
		this.matchDate = matchDate;
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
	public String getMatchDate1() {
		return matchDate1;
	}
	public void setMatchDate1(String matchDate1) {
		this.matchDate1 = matchDate1;
	}
	public String getMainLose() {
		return mainLose;
	}
	public void setMainLose(String mainLose) {
		this.mainLose = mainLose;
	}
	public String getMainWin() {
		return mainWin;
	}
	public void setMainWin(String mainWin) {
		this.mainWin = mainWin;
	}
	public String getBigSmallScore() {
		return bigSmallScore;
	}
	public void setBigSmallScore(String bigSmallScore) {
		this.bigSmallScore = bigSmallScore;
	}
	public String getSmall() {
		return small;
	}
	public void setSmall(String small) {
		this.small = small;
	}
	public String getBig() {
		return big;
	}
	public void setBig(String big) {
		this.big = big;
	}
	public String getLetScore() {
		return letScore;
	}
	public void setLetScore(String letScore) {
		this.letScore = letScore;
	}
	public String getLetMainLose() {
		return letMainLose;
	}
	public void setLetMainLose(String letMainLose) {
		this.letMainLose = letMainLose;
	}
	public String getLetMainWin() {
		return letMainWin;
	}
	public void setLetMainWin(String letMainWin) {
		this.letMainWin = letMainWin;
	}
	public String getMatchDate2() {
		return matchDate2;
	}
	public void setMatchDate2(String matchDate2) {
		this.matchDate2 = matchDate2;
	}
	public String getDifferGuest1_5() {
		return differGuest1_5;
	}
	public void setDifferGuest1_5(String differGuest1_5) {
		this.differGuest1_5 = differGuest1_5;
	}
	public String getDifferGuest6_10() {
		return differGuest6_10;
	}
	public void setDifferGuest6_10(String differGuest6_10) {
		this.differGuest6_10 = differGuest6_10;
	}
	public String getDifferGuest11_15() {
		return differGuest11_15;
	}
	public void setDifferGuest11_15(String differGuest11_15) {
		this.differGuest11_15 = differGuest11_15;
	}
	public String getDifferGuest16_20() {
		return differGuest16_20;
	}
	public void setDifferGuest16_20(String differGuest16_20) {
		this.differGuest16_20 = differGuest16_20;
	}
	public String getDifferGuest21_25() {
		return differGuest21_25;
	}
	public void setDifferGuest21_25(String differGuest21_25) {
		this.differGuest21_25 = differGuest21_25;
	}
	public String getDifferGuest26() {
		return differGuest26;
	}
	public void setDifferGuest26(String differGuest26) {
		this.differGuest26 = differGuest26;
	}
	public String getDifferMain1_5() {
		return differMain1_5;
	}
	public void setDifferMain1_5(String differMain1_5) {
		this.differMain1_5 = differMain1_5;
	}
	public String getDifferMain6_10() {
		return differMain6_10;
	}
	public void setDifferMain6_10(String differMain6_10) {
		this.differMain6_10 = differMain6_10;
	}
	public String getDifferMain11_15() {
		return differMain11_15;
	}
	public void setDifferMain11_15(String differMain11_15) {
		this.differMain11_15 = differMain11_15;
	}
	public String getDifferMain16_20() {
		return differMain16_20;
	}
	public void setDifferMain16_20(String differMain16_20) {
		this.differMain16_20 = differMain16_20;
	}
	public String getDifferMain21_25() {
		return differMain21_25;
	}
	public void setDifferMain21_25(String differMain21_25) {
		this.differMain21_25 = differMain21_25;
	}
	public String getDifferMain26() {
		return differMain26;
	}
	public void setDifferMain26(String differMain26) {
		this.differMain26 = differMain26;
	}
	public boolean isSF() {
		return isSF;
	}
	public void setSF(boolean isSF) {
		this.isSF = isSF;
	}
	public boolean isDXF() {
		return isDXF;
	}
	public void setDXF(boolean isDXF) {
		this.isDXF = isDXF;
	}
	public boolean isRFSF() {
		return isRFSF;
	}
	public void setRFSF(boolean isRFSF) {
		this.isRFSF = isRFSF;
		
	}
	public boolean isSFC() {
		return isSFC;
	}
	public void setSFC(boolean isSFC) {
		this.isSFC = isSFC;
	}

	
	
}
