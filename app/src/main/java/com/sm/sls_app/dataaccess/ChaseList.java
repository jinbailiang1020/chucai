package com.sm.sls_app.dataaccess;

import java.io.Serializable;

/**
 * @author 作者 : hxj
 * @version 创建时间：2016-1-28 上午8:47:21 类说明
 */
public class ChaseList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String isuseName;
	private String multiple;
	private String money;
	private String sumMoney;

	public String getIsuseName() {
		return isuseName;
	}

	public void setIsuseName(String isuseName) {
		this.isuseName = isuseName;
	}

	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getSumMoney() {
		return sumMoney;
	}

	public void setSumMoney(String sumMoney) {
		this.sumMoney = sumMoney;
	}
}
