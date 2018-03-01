package com.sm.sls_app.dataaccess;

public class TeamArray {
	private String id;
	private String game;
	private String matchNumber;
	private String matchDate;
	private String mainTeam;
	private String guestTeam;
	private String date;
	private String time;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGame() {
		return game;
	}
	public void setGame(String game) {
		this.game = game;
	}
	public String getMatchNumber() {
		return matchNumber;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
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
	
}
