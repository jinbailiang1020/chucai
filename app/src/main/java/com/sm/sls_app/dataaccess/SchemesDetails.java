package com.sm.sls_app.dataaccess;

import java.io.Serializable;
import java.util.List;

/**
 * @author 作者 : hxj
 * @version 创建时间：2016-1-28 上午8:42:21 类说明
 */
public class SchemesDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String error;
	private String msg;
	private String serverTime;
	private List<BuyContent> buyContent;
	private List<ChaseList> chaseList;
	private List<UserList> userList;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getServerTime() {
		return serverTime;
	}

	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}

	public List<BuyContent> getBuyContent() {
		return buyContent;
	}

	public void setBuyContent(List<BuyContent> buyContent) {
		this.buyContent = buyContent;
	}

	public List<ChaseList> getChaseList() {
		return chaseList;
	}

	public void setChaseList(List<ChaseList> chaseList) {
		this.chaseList = chaseList;
	}

	public List<UserList> getUserList() {
		return userList;
	}

	public void setUserList(List<UserList> userList) {
		this.userList = userList;
	}

}
