package com.sm.sls_app.dataaccess;

import java.io.Serializable;

/**中奖详情 */
public class WinDetail implements Serializable {

	private String bonusName;
	private String bonusValue;
	private int winningCount;
	
	
	public String getBonusName() {
		return bonusName;
	}
	public void setBonusName(String bonusName) {
		this.bonusName = bonusName;
	}
	public String getBonusValue() {
		return bonusValue;
	}
	public void setBonusValue(String bonusValue) {
		this.bonusValue = bonusValue;
	}
	public int getWinningCount() {
		return winningCount;
	}
	public void setWinningCount(int winningCount) {
		this.winningCount = winningCount;
	}
	
	
}
