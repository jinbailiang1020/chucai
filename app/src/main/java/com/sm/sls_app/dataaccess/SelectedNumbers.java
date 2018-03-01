package com.sm.sls_app.dataaccess;

import android.annotation.SuppressLint;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/** 所选号码 **/
public class SelectedNumbers implements Serializable {

	private static final long serialVersionUID = 1L;

	private String lotteryId; // 投注彩种ID
	private String lotteryName; // 投注彩种名称

	private List<String> redNumbers; // 已选红色号码
	private List<String> redTuoNum; // 已选红色拖区号码
	private List<String> blueNumbers; // 已选蓝色号码
	private List<String> blueTuoNum; // 已选红色拖区号码
	private String lotteryNumber; // 已选号码
	private String showLotteryNumber; // 展示号码
	private String number; // 用来区分号码的
	private int playType; // 玩法ID
	private int type;
	private long count; // 投注数量
	private long money; // 金额
	private List<Map<Integer, String>> List_Map;

	private boolean flag;// 区分福彩3D组三单式和组六单式

	public List<Map<Integer, String>> getList_Map() {
		return List_Map;
	}

	@SuppressLint("UseSparseArrays")
	public void setList_Map(List<Map<Integer, String>> list_Map) {
		this.List_Map = new ArrayList<Map<Integer, String>>();
		for (Map<Integer, String> map : list_Map) {
			Iterator<Entry<Integer, String>> it = map.entrySet().iterator();
			Map<Integer, String> map2 = new HashMap<Integer, String>();
			while (it.hasNext()) {
				Entry<Integer, String> entry = (Entry<Integer, String>) it
						.next();
				int key = Integer.parseInt(entry.getKey().toString());
				String value = entry.getValue().toString();
				System.out.println("key" + key + "value" + value);
				map2.put(key, value);
			}
			this.List_Map.add(map2);
		}
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public List<String> getBlueTuoNum() {
		return blueTuoNum;
	}

	public void setBlueTuoNum(List<String> blueTuoNum) {
		this.blueTuoNum = blueTuoNum;
	}

	public String getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getMoney() {
		return money;
	}

	public void setMoney(long money) {
		this.money = money;
	}

	public String getLotteryName() {
		return lotteryName;
	}

	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}

	public int getPlayType() {
		return playType;
	}

	public void setPlayType(int playType) {
		this.playType = playType;
	}

	public List<String> getRedNumbers() {
		return redNumbers;
	}

	public void setRedNumbers(List<String> redNumbers) {
		this.redNumbers = new ArrayList<String>();
		for (int i = 0; i < redNumbers.size(); i++) {
			this.redNumbers.add(redNumbers.get(i));
		}
	}

	public List<String> getRedTuoNum() {
		return redTuoNum;
	}

	public void setRedTuoNum(List<String> redTuoNum) {
		this.redTuoNum = new ArrayList<String>();
		for (int i = 0; i < redTuoNum.size(); i++) {
			this.redTuoNum.add(redTuoNum.get(i));
		}
	}

	public void setblueTuoNum(List<String> blueTuoNum) {
		this.blueTuoNum = new ArrayList<String>();
		for (int i = 0; i < blueTuoNum.size(); i++) {
			this.blueTuoNum.add(blueTuoNum.get(i));
		}
	}

	public List<String> getBlueNumbers() {
		return blueNumbers;
	}

	public void setBlueNumbers(List<String> blueNumbers) {
		this.blueNumbers = new ArrayList<String>();
		for (int i = 0; i < blueNumbers.size(); i++) {
			this.blueNumbers.add(blueNumbers.get(i));
		}
	}

	public String getLotteryNumber() {
		return lotteryNumber;
	}

	public void setLotteryNumber(String lotteryNumber) {
		this.lotteryNumber = lotteryNumber;
	}

	public String getShowLotteryNumber() {
		return showLotteryNumber;
	}

	public void setShowLotteryNumber(String showLotteryNumber) {
		this.showLotteryNumber = showLotteryNumber;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
