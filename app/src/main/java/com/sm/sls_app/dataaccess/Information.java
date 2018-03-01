package com.sm.sls_app.dataaccess;

/** 功能：彩票资讯实体类 版本 */
public class Information {
	private long id; // 资讯ID
	private int parentTypeId;// 上级ID
	private String parentTypeName;// 上级名称
	private String title;// 标题
	private String content;// 内容
	private String newType;// 新闻列别
	private String recordCount;// 点击次数
	private int serialNumber;// 序列号
	private int readCount;// 点击率
	private String dateTime;// 发表时间
	private String url;// 链接地址

	public int getReadCount() {
		return readCount;
	}

	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}

	public int getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(String recordCount) {
		this.recordCount = recordCount;
	}

	public String getNewType() {
		return newType;
	}

	public void setNewType(String newType) {
		this.newType = newType;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public float getParentTypeId() {
		return parentTypeId;
	}

	public void setParentTypeId(int parentTypeId) {
		this.parentTypeId = parentTypeId;
	}

	public String getParentTypeName() {
		return parentTypeName;
	}

	public void setParentTypeName(String parentTypeName) {
		this.parentTypeName = parentTypeName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
