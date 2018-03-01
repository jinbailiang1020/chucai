package com.sm.sls_app.dataaccess;

import java.io.Serializable;
import java.util.List;

/**投注方案*/
public class AppObject implements Serializable{
	private String upgrade; //是否有更新true false
	private String apkName; // app名称
	private String versionCode; // app版本号
	private String downLoadUrl; // 可供跟新的url地址
	private int sort = 3;//当前app彩种排序方式
	public String getUpgrade() {
		return upgrade;
	}
	public void setUpgrade(String upgrade) {
		this.upgrade = upgrade;
	}
	public String getApkName() {
		return apkName;
	}
	public void setApkName(String apkName) {
		this.apkName = apkName;
	}
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getDownLoadUrl() {
		return downLoadUrl;
	}
	public void setDownLoadUrl(String downLoadUrl) {
		this.downLoadUrl = downLoadUrl;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}

	

}
