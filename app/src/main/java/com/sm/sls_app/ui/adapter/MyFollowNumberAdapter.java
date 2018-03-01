package com.sm.sls_app.ui.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.BuyContent;
import com.sm.sls_app.dataaccess.LotteryContent;
import com.sm.sls_app.utils.NumberTools;
import com.sm.sls_app.view.MyToast;

/**
 * 合买跟单 ListView 的Adapter
 * 
 * @author SLS003
 * 
 */
public class MyFollowNumberAdapter extends BaseAdapter {

  private Context mContext;
  private List<BuyContent> buyContents;
  private String fangshi;// 方式

  public MyFollowNumberAdapter(Context context, List<BuyContent> buyContents) {
    this.mContext = context;
    this.buyContents = buyContents;
  }

  @Override
  public int getCount() {
    return buyContents.size();
  }

  @Override
  public Object getItem(int position) {
    return buyContents.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View view, ViewGroup arg2) {
    ViewHolder holder;
    // 判断View是否为空
    if (view == null) {
      holder = new ViewHolder();
      LayoutInflater inflater = LayoutInflater.from(mContext);
      // 得到布局文件
      view = inflater.inflate(R.layout.follow_number_item, null);
      // 得到控件
      holder.tv_item_fangshi = (TextView) view
          .findViewById(R.id.tv_item_fangshi);
      holder.tv_item_touzhu = (TextView) view.findViewById(R.id.tv_item_touzhu);
      holder.tv_item_zhushu = (TextView) view.findViewById(R.id.tv_item_zhushu);
      view.setTag(holder);
    } else {
      holder = (ViewHolder) view.getTag();
    }

    BuyContent buyContent = buyContents.get(position);
    String[] str = transNum(buyContent.getLotteryNumber());
    Spanned number;
    if (str.length == 2) {
      // 给控件赋值
      number = Html.fromHtml("<font color='#BE0205'>" + str[0] + "</FONT>"
          + "<font color='#4060ff'>" + " " + str[1] + "</FONT>");
    } else {
      number = Html.fromHtml("<font color='#BE0205'>" + str[0] + "</FONT>");
    }
    holder.tv_item_fangshi.setText(transPlayName(buyContent.getPlayType()));
    holder.tv_item_touzhu.setText(number);
    holder.tv_item_zhushu.setText(buyContent.getSumNum());

    return view;
  }

  private String[] transNum(String num) {
    return num.split("\\s?[+]\\s?");
  }

  private String transPlayName(String playID) {
    String str = null;
    if (TextUtils.isEmpty(playID)) {
      return str;
    }
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
    case 6618:
      str = "三星直选和值";
      break;
    case 6121:
    case 6621:
      str = "三组包胆";
      break;
    case 2816:
    case 9316:
      str = " 三星组三胆拖";
      break;
    case 2817:
    case 6119:
    case 6619:
    case 9317:
      str = "三星组六胆拖";
      break;

    case 2818:
    case 9318:
      str = "二星和值";
      break;
    case 6100:
    case 6600:
    case 9300:
      str = "混合投注";
      break;
    case 6101:
    case 6601:
    case 9301:
      str = "单式";
      break;
    case 6102:
    case 6602:
    case 9302:
      str = "复式";
      break;
    case 6103:
    case 6603:
    case 9303:
      str = "组合玩法";
      break;
    case 6104:
    case 6604:
      str = "猜大小";
      break;
    case 6105:
    case 6605:
      str = "五星单复式通选";
      break;
    case 6106:
    case 6606:
    case 9206:
    case 9306:
      str = "二星组选";
      break;
    case 6107:
    case 2807:
    case 6607:
    case 9207:
    case 9307:
      str = "二星组选分位";
      break;
    case 6108:
    case 2808:
    case 6608:
    case 9208:
    case 9308:
      str = "二星组选包点";
      break;
    case 6109:
    case 2809:
    case 6609:
    case 9209:
    case 9309:
      str = "二星组选包胆";
      break;
    case 6110:
    case 6610:
    case 9218:
      str = "二星和值";
      break;
    case 6111:
    case 6611:
    case 9211:
      str = "三星组三";
      break;
    case 6112:
    case 6612:
    case 9212:
      str = "三星组六";
      break;
    case 6113:
    case 6613:
    case 2813:
    case 9213:
    case 9313:
      str = "三星直选组合";
      break;
    case 6114:
    case 6614:
    case 2814:
    case 9214:
    case 9314:
      str = "三星组三胆拖";
      break;
    case 6115:
    case 6615:
    case 2815:
    case 9215:
    case 9315:
      str = "三星组选包点";
      break;
    case 6116:
    case 6616:
    case 9216:
      str = "任选一";
      break;
    case 6117:
    case 6617:
    case 9217:
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
    case 9304:
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
    case 9310:
      str = "三星和值";
      break;
    case 2811:
    case 9011:
    case 9311:
      str = "三星组三";
      break;
    case 2812:
    case 9312:
      str = "三星组六";
      break;
    case 6120:
    case 6620:
      str = "四星单复式通选";
      break;

    }
    return str;
  }

  static class ViewHolder {
    TextView tv_item_fangshi;
    TextView tv_item_touzhu;
    TextView tv_item_zhushu;
  }

}
