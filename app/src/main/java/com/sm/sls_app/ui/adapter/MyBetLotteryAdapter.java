package com.sm.sls_app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.Schemes;
import com.sm.sls_app.view.MyGridView;

import java.util.List;

/** 将 投注内容格式化 **/
public class MyBetLotteryAdapter extends BaseAdapter {

	private Context context;
	private Schemes scheme;
	private List<String> numbers;
	private List<Integer> max;

	public MyBetLotteryAdapter(Context context, Schemes scheme,
			List<String> numbers, List<Integer> max) {
		this.context = context;
		this.scheme = scheme;
		this.numbers = numbers;
		this.max = max;
		// setNumbers();
	}

	// public void setNumbers() {
	// String str = scheme.getLotteryNumber();
	// Log.i("x", "setNUmbers === lotteryNumber" + scheme.getLotteryNumber());
	// numbers = scheme.getLotteryNumber().split("\r\n");
	// max = new int[numbers.length];
	// for (int i = 0; i < numbers.length; i++) {
	// if (!numbers[i].contains("-")) {
	// max[i] = 100;
	// } else {
	// max[i] = numbers[i].substring(0, numbers[i].indexOf("-"))
	// .split(" ").length;
	// }
	// }
	// }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return numbers.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return numbers.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder holder;
		if (null == view) {
			LayoutInflater inflact = LayoutInflater.from(context);
			view = inflact.inflate(R.layout.my_betlottery_item, null);
			holder = new ViewHolder();
			holder.tv_lotteryName = (TextView) view
					.findViewById(R.id.tv_lotteryName);
			holder.tv_bei = (TextView) view.findViewById(R.id.tv_bei);
			holder.tv_num = (TextView) view.findViewById(R.id.tv_other_num);
			holder.gv_number = (MyGridView) view
					.findViewById(R.id.gv_betNumber);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.tv_num.setVisibility(View.GONE);
		// holder.gv_number.setVisibility(View.GONE);
		if (numbers.get(position).equals("/")) {
			return view;
		}
		holder.tv_lotteryName.setText(transPlayName(numbers.get(position)
				.split("/")[1]));

		String num = numbers.get(position).split("/")[0].trim().replace(",", " ");
		MyBetShowAdapter adapter = new MyBetShowAdapter(context,
				R.layout.betball_item);

		/** 设置开奖号码 **/
		if (null != scheme.getWinNumber() && scheme.getWinNumber().length() > 0) {
			scheme.getWinNumber().replaceAll("\\s?[\\+-]\\s?", "+");
			if (scheme.getWinNumber().contains("+"))
				adapter.setWinNumber(
						scheme.getWinNumber().split("\\+")[0].split(" "),
						scheme.getWinNumber().split("\\+")[1].split(" "));
			else
				adapter.setWinNumber(scheme.getWinNumber().split(" "), null);

		}
		/** 设置显示号码 */
		adapter.setNumber(transNum(num), max.get(position));
		switch (Integer.parseInt(scheme.getLotteryID())) {

		case 5:// 双色球
		case 39:// 七乐彩
			holder.gv_number.setAdapter(adapter);
			break;

		// case 3:
		// case 6:
		// case 63:
		// case 64:
		// adapter.setNumber(getStrings(num),100);
		// adapter.setWinNumber(getStrings(scheme.getWinNumber()),null);
		// holder.gv_number.setAdapter(adapter);
		// break;
		case 13:// 七乐彩
		case 70:
		case 62:
		case 69:
			adapter.setNumber(num.replaceAll("\\s?[\\|]\\s?", " ").split(" "),
					100);
			adapter.setWinNumber(
					scheme.getWinNumber().replaceAll("\\s?[\\+-]\\s?", " ")
							.split(" "), null);
			holder.gv_number.setAdapter(adapter);
			break;
		case 87:
			holder.gv_number.setAdapter(adapter);
			break;

		default: // 3D,排 3 ，5,七星彩
			holder.gv_number.setVisibility(View.GONE);
			holder.tv_num.setVisibility(View.VISIBLE);
			holder.tv_num.setText(num.replaceAll("\\s?[\\|]\\s?", " ") + "");
			break;
		}
		return view;
	}

	/** 将String拆分成 数组 */
	private String[] getStrings(String number) {
		if (null == number)
			return null;
		String str[] = new String[number.length()];
		for (int i = 0; i < number.length(); i++) {
			str[i] = number.substring(i, i + 1) + "";
		}
		return str;
	}

	private String[] transNum(String num) {
		num.replace("-", "+");
		if (num.contains(" + ")) {
			return num.replace(" + ", " ").split(" ");
		} else if (num.contains("+")) {
			return num.replace("+", " ").split(" ");
		}
		return num.split(" ");
	}

	private String transPlayName(String playID) {
		String str = null;
		switch (Integer.valueOf(playID)) {
		case 601:
			str = "单选";
			break;
		case 602:
			str = "组选3";
			break;
		case 603:
			str = "组选6";
			break;
		case 604:
			str = "1D";
			break;
		case 605:
			str = "猜1D";
			break;
		case 606:
			str = "2D";
			break;
		case 607:
			str = "两同号";
			break;
		case 608:
			str = "两不同号";
			break;
		case 609:
			str = "通选";
			break;
		case 610:
			str = "和数";
			break;
		case 611:
			str = "包选三";
			break;
		case 612:
			str = "包选六";
			break;
		case 613:
			str = "猜大小";
			break;
		case 614:
			str = "猜三同 ";
			break;
		case 615:
			str = "拖拉机";
			break;
		case 616:
			str = "猜奇偶";
			break;
		case 6118:
			str = "三星直选和值";
			break;
		case 6121:
			str = "三组包胆";
			break;
		case 2816:
			str = "三星组三胆拖";
			break;
		case 2817:
		case 6119:
		case 9217:
			str = "三星组六胆拖";
			break;
		case 6100:
			str = "混合投注";
			break;
		case 6101:
			str = "单式";
			break;
		case 6102:
			str = "复式";
			break;

		case 6103:
			str = "组合玩法";
			break;
		case 6104:
			str = "猜大小";
			break;
		case 6105:
			str = "五星单复式通选";
			break;
		case 6106:
		case 9206:
			str = "二星组选";
			break;
		case 6107:
		case 2807:
		case 9207:
			str = "二星组选分位";
			break;
		case 6108:
		case 2808:
		case 9208:
			str = "二星组选包点";
			break;
		case 6109:
		case 2809:
		case 9209:
			str = "二星组选包胆";
			break;
		case 6110:
		case 2818:
		case 9218:
			str = "二星和值";
			break;
		case 6111:
		case 9211:
			str = "三星组三";
			break;
		case 6112:
		case 9212:
			str = "三星组六";
			break;
		case 6113:
		case 2813:
		case 9213:
			str = "三星直选组合";
			break;
		case 6114:
		case 9216:
			str = "三星组三胆拖";
			break;
		case 2814:
			str = "三星组选包胆";
			break;
		case 6115:
		case 2815:
		case 9215:
			str = "三星组选包点";
			break;
		case 6116:
			str = "任选一";
			break;
		case 6117:
			str = "任选二";
			break;
		case 2800:
		case 9200:
			str = "混合投注";
			break;
		case 2801:
		case 9201:
			str = "单式";
			break;
		case 2802:
		case 9202:
			str = "复式";
			break;
		case 2803:
		case 9203:
			str = "组合玩法";
			break;
		case 2804:
		case 9204:
			str = "大小单双";
			break;
		case 2805:
			str = "五星单复式通选";
			break;
		case 6201:
		case 7801:
		case 7001:
			str = "前一";
			break;
		case 6202:
		case 7802:
		case 7002:
			str = "任选二";
			break;
		case 6203:
		case 7803:
		case 7003:
			str = "任选三";
			break;
		case 6204:
		case 7804:
		case 7004:
			str = "任选四";
			break;
		case 6205:
		case 7805:
		case 7005:
			str = "任选五";
			break;
		case 6206:
		case 7806:
		case 7006:
			str = "任选六";
			break;
		case 6207:
		case 7807:
		case 7007:
			str = "任选七";
			break;
		case 6208:
		case 7808:
		case 7008:
			str = "任选八";
			break;
		case 6209:
		case 7809:
		case 7009:
			str = "直选二";
			break;
		case 6210:
		case 7810:
		case 7010:
			str = "直选三";
			break;
		case 6211:
		case 7811:
		case 7011:
			str = "组选二";
			break;
		case 6212:
		case 7812:
		case 7012:
			str = "组选三";
			break;
		case 6213:
		case 7813:
		case 7013:
			str = "组选前二胆拖";
			break;
		case 6214:
		case 7814:
		case 7014:
			str = "组选前三胆拖";
			break;
		case 6215:
		case 7815:
		case 7015:
			str = "任选二胆拖";
			break;
		case 6216:
		case 7816:
		case 7016:
			str = "任选三胆拖";
			break;
		case 6217:
		case 7817:
		case 7017:
			str = "任选四胆拖";
			break;
		case 6218:
		case 7818:
		case 7018:
			str = "任选五胆拖";
			break;
		case 6219:
		case 7819:
		case 7019:
			str = "任选六胆拖";
			break;
		case 6220:
		case 7820:
		case 7020:
			str = "任选七胆拖";
			break;
		case 6221:
		case 7821:
		case 7021:
			str = "任选八胆拖";
			break;
		case 6301:
		case 6401:
			str = "普通投注";
			break;
		case 6302:
			str = "组选单式";
			break;
		case 6303:
			str = "组选6复式";
			break;
		case 6304:
			str = "组选3复式";
			break;
		case 6305:
			str = "直选和值";
			break;
		case 6306:
			str = "组选和值";
			break;
		case 2810:
		case 9210:
			str = "三星和值";
			break;
		case 2811:
			str = "三星组三";
			break;
		case 2812:
			str = "三星组六";
			break;
		case 6120:
			str = "四星单复式通选";
			break;

		}
		return str;
	}

	static class ViewHolder {
		TextView tv_lotteryName, tv_bei, tv_num;
		MyGridView gv_number;
	}

}
