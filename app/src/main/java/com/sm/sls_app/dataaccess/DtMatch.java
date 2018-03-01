package com.sm.sls_app.dataaccess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** 对阵信息 */
public class DtMatch implements Serializable {

	private static final long serialVersionUID = 1L;

	private String matchId; // 对阵ID
	private String matchNumber;
	private String matchDate;
	private String matchDate1;
	private String game;
	private String mainTeam;
	private String guestTeam;
	private String stopSellTime; // 停售时间
	private int mainLoseBall;
	private String beginTime;
	private String matchWeek;
	// 让球胜平负
	private String win;
	private String flat;
	private String lose;
	// 胜平负
	private String spfwin;
	private String spfflat;
	private String spflose;

	/** 猜比分 **/
	private String sother;
	private String s10;
	private String s20;
	private String s21;
	private String s30;
	private String s31;
	private String s32;
	private String s40;
	private String s41;
	private String s42;
	private String s50;
	private String s51;
	private String s52;
	private String pother;
	private String p00;
	private String p11;
	private String p22;
	private String p33;
	private String fother;
	private String f01;
	private String f02;
	private String f12;
	private String f03;
	private String f13;
	private String f23;
	private String f04;
	private String f14;
	private String f24;
	private String f05;
	private String f15;
	private String f25;
	/** 半全场 **/
	private String ss;
	private String sp;
	private String sf;
	private String ps;
	private String pp;
	private String pf;
	private String fs;
	private String fp;
	private String ff;
	/** 总进球 **/
	private String in0;
	private String in1;
	private String in2;
	private String in3;
	private String in4;
	private String in5;
	private String in6;
	private String in7;

	private boolean isSPF;
	private boolean isZJQ;
	private boolean isCBF;
	private boolean isNewSPF;
	private boolean isHHTZ;
	private boolean isBQC;

	/** 竞彩足球 **/
	private String mainLose;
	private String mainWin;
	private String bigSmallScore;
	private String small;
	private String big;
	private boolean isSF;
	private boolean isDXF;

	public List<String> getspl() {//
		List<String> list = new ArrayList<String>();
		list.add(s10);
		list.add(s20);
		list.add(s21);
		list.add(s30);
		list.add(s31);
		list.add(s32);
		list.add(s40);
		list.add(s41);
		list.add(s42);
		list.add(s50);
		list.add(s51);
		list.add(s52);
		list.add(sother);
		list.add(" ");
		list.add(p00);
		list.add(p11);
		list.add(p22);
		list.add(p33);
		list.add(pother);
		list.add(" ");
		list.add(f01);
		list.add(f02);
		list.add(f12);
		list.add(f03);
		list.add(f13);
		list.add(f23);
		list.add(f04);
		list.add(f14);
		list.add(f24);
		list.add(f05);
		list.add(f15);
		list.add(f25);
		list.add(fother);
		list.add("");
		return list;
	}

	public String getSs() {
		return ss;
	}

	public void setSs(String ss) {
		this.ss = ss;
	}

	public String getSp() {
		return sp;
	}

	public void setSp(String sp) {
		this.sp = sp;
	}

	public String getSf() {
		return sf;
	}

	public void setSf(String sf) {
		this.sf = sf;
	}

	public String getPs() {
		return ps;
	}

	public void setPs(String ps) {
		this.ps = ps;
	}

	public String getPp() {
		return pp;
	}

	public void setPp(String pp) {
		this.pp = pp;
	}

	public String getPf() {
		return pf;
	}

	public void setPf(String pf) {
		this.pf = pf;
	}

	public String getFs() {
		return fs;
	}

	public void setFs(String fs) {
		this.fs = fs;
	}

	public String getFp() {
		return fp;
	}

	public void setFp(String fp) {
		this.fp = fp;
	}

	public String getFf() {
		return ff;
	}

	public void setFf(String ff) {
		this.ff = ff;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public boolean isHHTZ() {
		return isSPF && isNewSPF;
	}

	public void setHHTZ(boolean isHHTZ) {
		this.isHHTZ = isHHTZ;
	}

	public boolean isSPF() {
		return isSPF;
	}

	public void setSPF(boolean isSPF) {
		this.isSPF = isSPF;
	}

	public boolean isZJQ() {
		return isZJQ;
	}

	public void setZJQ(boolean isZJQ) {
		this.isZJQ = isZJQ;
	}

	public boolean isCBF() {
		return isCBF;
	}

	public void setCBF(boolean isCBF) {
		this.isCBF = isCBF;
	}

	public boolean isBQC() {
		return isBQC;
	}

	public void setBQC(boolean isBQC) {
		this.isBQC = isBQC;
	}

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

	public String getSother() {
		return sother;
	}

	public void setSother(String sother) {
		this.sother = sother;
	}

	public String getS10() {
		return s10;
	}

	public void setS10(String s10) {
		this.s10 = s10;
	}

	public String getS20() {
		return s20;
	}

	public void setS20(String s20) {
		this.s20 = s20;
	}

	public String getS21() {
		return s21;
	}

	public void setS21(String s21) {
		this.s21 = s21;
	}

	public String getS30() {
		return s30;
	}

	public void setS30(String s30) {
		this.s30 = s30;
	}

	public String getS31() {
		return s31;
	}

	public void setS31(String s31) {
		this.s31 = s31;
	}

	public String getS32() {
		return s32;
	}

	public void setS32(String s32) {
		this.s32 = s32;
	}

	public String getS40() {
		return s40;
	}

	public void setS40(String s40) {
		this.s40 = s40;
	}

	public String getS41() {
		return s41;
	}

	public void setS41(String s41) {
		this.s41 = s41;
	}

	public String getS42() {
		return s42;
	}

	public void setS42(String s42) {
		this.s42 = s42;
	}

	public String getS50() {
		return s50;
	}

	public void setS50(String s50) {
		this.s50 = s50;
	}

	public String getS51() {
		return s51;
	}

	public void setS51(String s51) {
		this.s51 = s51;
	}

	public String getS52() {
		return s52;
	}

	public void setS52(String s52) {
		this.s52 = s52;
	}

	public String getPother() {
		return pother;
	}

	public void setPother(String pother) {
		this.pother = pother;
	}

	public String getP00() {
		return p00;
	}

	public void setP00(String p00) {
		this.p00 = p00;
	}

	public String getP11() {
		return p11;
	}

	public void setP11(String p11) {
		this.p11 = p11;
	}

	public String getP22() {
		return p22;
	}

	public void setP22(String p22) {
		this.p22 = p22;
	}

	public String getP33() {
		return p33;
	}

	public void setP33(String p33) {
		this.p33 = p33;
	}

	public String getFother() {
		return fother;
	}

	public void setFother(String fother) {
		this.fother = fother;
	}

	public String getF01() {
		return f01;
	}

	public void setF01(String f01) {
		this.f01 = f01;
	}

	public String getF02() {
		return f02;
	}

	public void setF02(String f02) {
		this.f02 = f02;
	}

	public String getF12() {
		return f12;
	}

	public void setF12(String f12) {
		this.f12 = f12;
	}

	public String getF03() {
		return f03;
	}

	public void setF03(String f03) {
		this.f03 = f03;
	}

	public String getF13() {
		return f13;
	}

	public void setF13(String f13) {
		this.f13 = f13;
	}

	public String getF23() {
		return f23;
	}

	public void setF23(String f23) {
		this.f23 = f23;
	}

	public String getF04() {
		return f04;
	}

	public void setF04(String f04) {
		this.f04 = f04;
	}

	public String getF14() {
		return f14;
	}

	public void setF14(String f14) {
		this.f14 = f14;
	}

	public String getF24() {
		return f24;
	}

	public void setF24(String f24) {
		this.f24 = f24;
	}

	public String getF05() {
		return f05;
	}

	public void setF05(String f05) {
		this.f05 = f05;
	}

	public String getF15() {
		return f15;
	}

	public void setF15(String f15) {
		this.f15 = f15;
	}

	public String getF25() {
		return f25;
	}

	public void setF25(String f25) {
		this.f25 = f25;
	}

	public String getIn0() {
		return in0;
	}

	public void setIn0(String in0) {
		this.in0 = in0;
	}

	public String getIn1() {
		return in1;
	}

	public void setIn1(String in1) {
		this.in1 = in1;
	}

	public String getIn2() {
		return in2;
	}

	public void setIn2(String in2) {
		this.in2 = in2;
	}

	public String getIn3() {
		return in3;
	}

	public void setIn3(String in3) {
		this.in3 = in3;
	}

	public String getIn4() {
		return in4;
	}

	public void setIn4(String in4) {
		this.in4 = in4;
	}

	public String getIn5() {
		return in5;
	}

	public void setIn5(String in5) {
		this.in5 = in5;
	}

	public String getIn6() {
		return in6;
	}

	public void setIn6(String in6) {
		this.in6 = in6;
	}

	public String getIn7() {
		return in7;
	}

	public void setIn7(String in7) {
		this.in7 = in7;
	}

	public String getStopSellTime() {
		return stopSellTime;
	}

	public void setStopSellTime(String stopSellTime) {
		this.stopSellTime = stopSellTime;
	}

	public int getMainLoseBall() {
		return mainLoseBall;
	}

	public void setMainLoseBall(int mainLoseBall) {
		this.mainLoseBall = mainLoseBall;
	}

	public String getWin() {
		return win;
	}

	public void setWin(String win) {
		this.win = win;
	}

	public String getFlat() {
		return flat;
	}

	public void setFlat(String flat) {
		this.flat = flat;
	}

	public String getLose() {
		return lose;
	}

	public void setLose(String lose) {
		this.lose = lose;
	}

	public String getMatchDate() {
		return matchDate;
	}

	public void setMatchDate(String matchDate) {
		this.matchDate = matchDate;
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

	public boolean isSF() {
		return isSF;
	}

	public void setSF(boolean isSF) {
		this.isSF = isSF;
	}

	public boolean isDXF() {
		return isDXF;
	}

	public void setDXF(boolean isDX) {
		this.isDXF = isDX;
	}

	public String getSpfwin() {
		return spfwin;
	}

	public void setSpfwin(String spfwin) {
		this.spfwin = spfwin;
	}

	public String getSpfflat() {
		return spfflat;
	}

	public void setSpfflat(String spfflat) {
		this.spfflat = spfflat;
	}

	public String getSpflose() {
		return spflose;
	}

	public void setSpflose(String spflose) {
		this.spflose = spflose;
	}

	public boolean isNewSPF() {
		return isNewSPF;
	}

	public void setNewSPF(boolean isNewSPF) {
		this.isNewSPF = isNewSPF;
	}

	public String getMatchWeek() {
		return matchWeek;
	}

	public void setMatchWeek(String matchWeek) {
		this.matchWeek = matchWeek;
	}

	@Override
	public String toString() {
		return "DtMatch [matchDate=" + matchDate + ", matchDate1=" + matchDate1
				+ ", isSPF=" + isSPF + ", isZJQ=" + isZJQ + ", isCBF=" + isCBF
				+ ", isNewSPF=" + isNewSPF + ", isHHTZ=" + isHHTZ + "]";
	}
}
