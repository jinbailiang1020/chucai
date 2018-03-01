package com.sm.sls_app.dataaccess;

import java.io.Serializable;

public class FollowTaskDetails implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int id;                //id
	private String datetime;       //发起时间
	private String title;          //任务标题
	private String lotteryId;      //彩种ID
	private double stopWhenWinMoney;//停止任务条件 
	private String lotteryName;    //彩种名称
	private int isuseCount;         //总期数
	private int quashCount;		    //已撤销期数
	private double buyedMoney;      //已完成金额
	private double quashedMoney;    //已撤销金额
	private int SerialNumber;       //数据在全部数据中的位置
	private int recordCount;        //总记录数 
	
	
	
}
