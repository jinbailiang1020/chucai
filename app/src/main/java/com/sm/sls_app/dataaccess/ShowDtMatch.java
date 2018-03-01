package com.sm.sls_app.dataaccess;

/**显示的对阵信息**/
public class ShowDtMatch {

	private int SchemeId;
	private int  playType;
	private String playName;
	private String matchNumber;
	private String game;
	private String mainTeam;
	private String GuestTeam;
	private String stopSelling;
	private int letBile;   //是否是胆拖  0不是 1是
	private String score;  //比赛分数
	private String result;   //比赛结果
	private String passType;
	private String [] select;
	private double [] odds;
	private int[] InvestWay;
	private boolean isMixed;
	
	public int getSchemeId() {
		return SchemeId;
	}
	public void setSchemeId(int schemeId) {
		SchemeId = schemeId;
	}
	public int getPlayType() {
		return playType;
	}
	public void setPlayType(int playType) {
		this.playType = playType;
	}
	
	public String getPlayName() {
		return playName;
	}
	public void setPlayName(String playName) {
		this.playName = playName;
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
		return GuestTeam;
	}
	public void setGuestTeam(String guestTeam) {
		GuestTeam = guestTeam;
	}
	public String getStopSelling() {
		return stopSelling;
	}
	public void setStopSelling(String stopSelling) {
		this.stopSelling = stopSelling;
	}
	public int getLetBile() {
		return letBile;
	}
	public void setLetBile(int letBile) {
		this.letBile = letBile;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getPassType() {
		return passType;
	}
	public void setPassType(String passType) {
		this.passType = passType;
	}
	
	public String[] getSelect() {
		return select;
	}
	public void setSelect(String[] select) {
		this.select = new String [select.length];
		for(int i=0;i<select.length;i++){
			this.select [i] = select[i]; 
		}
	}
	public double[] getOdds() {
		return odds;
	}
	public void setOdds(double[] odds) {
		this.odds = new double [odds.length];
		for(int i=0;i<odds.length;i++){
			this.odds [i] = odds[i]; 
		}
	}
	public int[] getInvestWay() {
		return InvestWay;
	}
	public void setInvestWay(int[] investWay) {
		InvestWay = investWay;
	}
	public boolean isMixed() {
		return isMixed;
	}
	public void setMixed(boolean isMixed) {
		this.isMixed = isMixed;
	}
	
	
	
	//{"error":"0","msg":"","serverTime":"2013-05-08 20:12:23",
		//"informationId":"[ { "ID":"102","SchemeID":"14582","PlayType":"7201","MatchNumber":"周三001",
		//"Game":"意　　甲","MaiTeam":"佩斯卡","GuestTeam":"AC米兰","StopSelling":"2013-5-9 0:00:00","LetBile":"0",
		//"Score":"","Results":"","PassType":"2x1","issue":"","Status":"","EndTime":"","investContent":"平-7.40"},
		
		//{ "ID":"103","SchemeID":"14582","PlayType":"7201","MatchNumber":"周三002","Game":"挪　　超","MaiTeam":"罗森博","GuestTeam":"特罗姆","StopSelling":"2013-5-9 1:00:00","LetBile":"0","Score":"","Results":"","PassType":"2x1","issue":"","Status":"","EndTime":"","investContent":"平-7.40负-5.65"}, { "ID":"104","SchemeID":"14582","PlayType":"7201","MatchNumber":"周三003","Game":"挪　　超","MaiTeam":"瓦勒伦","GuestTeam":"赫纳福","StopSelling":"2013-5-9 1:00:00","LetBile":"0","Score":"","Results":"","PassType":"2x1","issue":"","Status":"","EndTime":"","investContent":"平-7.40负-5.65负-4.25"} ]"}

}
