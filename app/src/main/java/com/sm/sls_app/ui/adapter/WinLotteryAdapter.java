package com.sm.sls_app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.FileUtils;

import java.util.List;
import java.util.Map;

/** 历史中奖号码Adapter */
public class WinLotteryAdapter extends BaseAdapter {

  private Context context;
  // 装图片的集合

  private List<Map<String, String>> list;

  private float scale = 1f;

  public WinLotteryAdapter(Context context, List<Map<String, String>> list,
      Activity parent) {
    scale = AppTools.getDpi(parent);
    Log.i("屏幕比例", scale + "");
    this.context = context;
    this.list = list;
    System.out.println("==========list==" + list);

  }

  @Override
  public int getCount() {
    // TODO Auto-generated method stub
    return list.size();
  }

  @Override
  public Object getItem(int position) {
    // TODO Auto-generated method stub
    return list.get(position);
  }

  @Override
  public long getItemId(int position) {
    // TODO Auto-generated method stub
    return position;
  }

  @Override
  public View getView(int position, View view, ViewGroup parent) {
    ViewHolder holder;
    // 判断View是否为空
    if (view == null) {
      holder = new ViewHolder();
      LayoutInflater inflater = LayoutInflater.from(context);
      // 得到布局文件
      view = inflater.inflate(R.layout.win_lottery_items, null);
      // 得到控件
      holder.image = (ImageView) view.findViewById(R.id.win_items_image);
      holder.lotteryName = (TextView) view
          .findViewById(R.id.win_tv_lotteryName);
      holder.lotteryQI = (TextView) view.findViewById(R.id.win_tv_lotteryQi);
      holder.linearLayout1 = (LinearLayout) view
          .findViewById(R.id.lottery_ll_t);
      holder.win_tv_lottery_date = (TextView) view
          .findViewById(R.id.win_tv_lottery_date);
      holder.win_lottery_tv_hint = (TextView) view
          .findViewById(R.id.win_lottery_tv_hint);

      view.setTag(holder);
    } else {
      holder = (ViewHolder) view.getTag();
    }
    if (list.isEmpty()) {
      return view;
    }
    Map<String, String> item = list.get(position);
    // 给控件赋值
    System.out.println("大小：" + list.size());
    System.out.println("==========position==" + position);
    Log.i("x", "开奖公告 item: " + item.get("winLotteryNumber"));
    holder.image.setBackgroundResource(AppTools.allLotteryLogo.get(item
        .get("lotteryId")));
    holder.lotteryName.setText(FileUtils.getTitleText(item.get("lotteryId")));

    holder.lotteryQI.setText(item.get("name").equals("") ? " " : item
        .get("name") + "期");
    holder.linearLayout1.removeAllViews();
    holder.win_tv_lottery_date.setVisibility(View.GONE);
    holder.win_lottery_tv_hint.setVisibility(View.GONE);

    String num = null;
    if (item.get("winLotteryNumber") != null
        && !item.get("winLotteryNumber").equals("")) {
      num = item.get("winLotteryNumber").replaceAll("\\s?[\\+]\\s?", "-");
    }

    // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
    //
    // Date temp = null;
    // try {
    // temp = dateFormat.parse(lottery.getStarttime());
    // } catch (ParseException e) {
    // Log.e(" dateFormat.parse(lottery.getStarttime())", e.toString());
    // }
    // String date = dateFormat.format(temp);
    String hint = null;
    switch (Integer.parseInt(item.get("lotteryId"))) {
    // 双色球
    case 5:
      if (hint == null) {
        hint = "每周二、四、日晚 21:30开奖";
      }

      // 大乐透
    case 39:
      if (hint == null) {
        hint = "每周一、三、六晚 21:30开奖";
      }

      // 七乐彩
    case 13:
      if (hint == null) {
        hint = "每周一、三、五晚 21:30开奖";
      }
      holder.win_lottery_tv_hint.setVisibility(View.VISIBLE);
      holder.win_lottery_tv_hint.setText(hint);
      if (num != null && num.contains("-")) {
        addNumBall(holder.linearLayout1, num.split("-")[0].split(" "), null, 0);
        addNumBall(holder.linearLayout1, num.split("-")[1].split(" "), null, 1);
      }
      break;
    case 62:// 十一运夺金
    case 70:// 11选5
    case 78:// 广东11选5
      if (hint == null) {
        if (Integer.parseInt(item.get("lotteryId")) == 62) {
          hint = "每天78期  10分钟一期";
        } else {
          hint = "每天84期  10分钟一期";
        }
      }
      holder.win_lottery_tv_hint.setVisibility(View.VISIBLE);
      holder.win_lottery_tv_hint.setText(hint);
      if (num != null) {
        addNumBall(holder.linearLayout1, num.split("-")[0].split(" "), null, 0);

      }
      break;
    case 94:
      if (hint == null) {
        hint = "09:07--23:57共179期 5分钟一期";
      }
      holder.win_lottery_tv_hint.setVisibility(View.VISIBLE);
      holder.win_lottery_tv_hint.setText(hint);
      if (num != null) {
        addNumBall(holder.linearLayout1, num.split("-")[0].split(" "), null, 0);

      }
      break;
    // case 3: // 七星彩
    // if (hint == null) {
    // hint = "每周二、五、日晚 20:30开奖";
    // }
    case 6: // 3D
      if (hint == null) {
        hint = "每天 20:30开奖";
      }
    case 28: // 时时彩
      if (hint == null) {
        hint = "每天120期 10分钟一期";
      }
    case 61:
    case 66:
    case 93:
    case 92:
    case 63: // 排列3
      if (hint == null) {
        if (Integer.parseInt(item.get("lotteryId")) == 61) {

          hint = "每天84期 10分钟一期";
        } else if (Integer.parseInt(item.get("lotteryId")) == 92
            || Integer.parseInt(item.get("lotteryId")) == 93) {
          hint = "24小时共288期 5分钟一期";

        } else if (Integer.parseInt(item.get("lotteryId")) == 66) {
          hint = "每天96期，10分钟一期";

        } else {
          hint = "每天 20:30开奖";
        }
      }
    case 64: // 排列5
      if (hint == null) {
        hint = "每天 20:30开奖";
      }
    case 83: // 江苏快3
      if (hint == null) {
        hint = "2元赢取￥240元";
      }
      holder.win_lottery_tv_hint.setVisibility(View.VISIBLE);
      holder.win_lottery_tv_hint.setText(hint);
      addNumBall(holder.linearLayout1, null, num, 0);
      break;
    // case 74:
    // case 75:
    // addNumBall(holder.linearLayout1, null, num, 2);
    // break;
    default:
      break;
    }

    return view;
  }

  /** 静态类 */
  static class ViewHolder {
    ImageView image;
    TextView lotteryName, lotteryQI, tv_1, tv_2, win_tv_lottery_date,
        win_lottery_tv_hint;
    GridView gv_num;
    LinearLayout linearLayout1;
  }

  /**
   * 添加数字球
   * 
   * @param layout
   * @param nums
   * @param num
   * @param flag
   *          0为红球，1为蓝球，2为矩形
   */
  private void addNumBall(LinearLayout layout, String[] nums, String num,
      int flag) {
    if (nums == null || nums.length == 0) {
      if (TextUtils.isEmpty(num))
        return;

      StringBuffer buffer = new StringBuffer();
      for (int i = 0; i < num.length(); i++) {
        buffer.append(num.substring(i, i + 1));
        if (i != (num.length() - 1)) {
          buffer.append("-");
        }
      }
      nums = buffer.toString().split("-");
    }
    switch (flag) {
    case 0: // 添加红球
      TextView red_ball = null;
      for (int i = 0; i < nums.length; i++) {
        red_ball = (TextView) LayoutInflater.from(context).inflate(
            R.layout.win_lottery_ball, null);
        red_ball.setText(nums[i]);
        layout.addView(red_ball);
      }

      break;

    case 1:// 添加蓝球
      TextView blue_ball = null;
      for (int i = 0; i < nums.length; i++) {
        blue_ball = (TextView) LayoutInflater.from(context).inflate(
            R.layout.win_lottery_ball, null);
        blue_ball.setBackgroundResource(R.drawable.win_lottery_blue_ball_shape);
        blue_ball.setText(nums[i]);
        layout.addView(blue_ball);
      }
      break;
    case 2:// 添加矩形
      TextView rectangle = null;
      for (int i = 0; i < nums.length; i++) {
        rectangle = (TextView) LayoutInflater.from(context).inflate(
            R.layout.win_lottery_ball, null);
        rectangle.setBackgroundResource(R.drawable.win_lottery_rectangle_shape);
        rectangle.setText(nums[i]);
        layout.addView(rectangle);
      }
      break;
    }
  }
}
