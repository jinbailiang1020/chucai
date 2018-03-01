package com.sm.sls_app.dataaccess;

import java.io.Serializable;

/**
 * @author 作者 : hxj
 * @version 创建时间：2016-1-28 上午8:48:44 类说明
 */
public class UserList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String share;
	private String detailMoney;
	private Double scalePercent;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShare() {
		return share;
	}

	public void setShare(String share) {
		this.share = share;
	}

	public String getDetailMoney() {
		return detailMoney;
	}

	public void setDetailMoney(String detailMoney) {
		this.detailMoney = detailMoney;
	}

	public Double getScalePercent() {
		return scalePercent;
	}

	public void setScalePercent(Double scalePercent) {
		this.scalePercent = scalePercent;
	}

}
