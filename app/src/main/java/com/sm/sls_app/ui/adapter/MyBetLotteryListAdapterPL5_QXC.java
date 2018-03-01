package com.sm.sls_app.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.SelectedNumbers;
import com.sm.sls_app.ui.BetActivityPL5_QXC;
import com.sm.sls_app.utils.AppTools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 功能：显示我的彩票 的 ListView的Adapter 版本
 */
public class MyBetLotteryListAdapterPL5_QXC extends BaseAdapter {

    // 上下文本
    private Context context;
    // 装彩票的集合
    private List<SelectedNumbers> listSchemes;
    private BetActivityPL5_QXC betActivity;

    /**
     * 构造方法 实现初始化
     */
    public MyBetLotteryListAdapterPL5_QXC(Context context,
                                          List<SelectedNumbers> listSchemes) {
        this.context = context;
        this.listSchemes = listSchemes;
        betActivity = (BetActivityPL5_QXC) context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listSchemes.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return listSchemes.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    /**
     * 获得视图
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        final int index = position;
        // 判断View是否为空
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            // 得到布局文件
            view = inflater.inflate(R.layout.item_bet_lv, null);
            // 得到控件
            holder.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
            holder.tv_show_number = (TextView) view
                    .findViewById(R.id.tv_show_number);

            holder.tv_type_count_money = (TextView) view
                    .findViewById(R.id.tv_type_count_money);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        SelectedNumbers num = listSchemes.get(position);
        // 给控件赋值

        Log.i("x", "num的值-----" + num.getLotteryNumber());

        String[] red = num.getShowLotteryNumber().trim().split(" ");

        String numberString = "";
        if (red.length > 0) {
            for (String str : red) {
                numberString += (num.getPlayType() == 1 ? str : sort(str))
                        + " ";
            }
            numberString = numberString.trim();
            Spanned number = Html.fromHtml("<font color='#BE0205'>"
                    + numberString + "</FONT>");

//			holder.tv_show_number.setText(number);
            holder.tv_show_number.setText(Html.fromHtml("<font color='#BE0205'>"
                    + setWrapper(numberString) + "</FONT>"));

            if (num.getPlayType() == 6401 || 301 == num.getPlayType())
                holder.tv_type_count_money.setText("普通投注  " + num.getCount()
                        + "注  " + listSchemes.get(position).getMoney() + "元");

            holder.iv_delete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    AppTools.list_numbers.remove(index);
                    betActivity.adapter.notifyDataSetChanged();
                    betActivity.changeTextShow();

                }
            });
        }

        return view;
    }

    /**
     * 号码外面包装括号
     *
     * @param number
     * @return
     */
    private String setWrapper(String number) {
        if (number.contains(" ")) {
            String[] temp = number.split(" ");
            if (temp.length == 0)
                return number;
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < temp.length; i++) {
                if (temp[i].length() > 1) {
                    buffer.append("(" + temp[i] + ") ");
                } else {
                    buffer.append(temp[i] + " ");
                }
            }
            return buffer.toString();
        }
        return number;
    }

    private String sort(String str) {
        if (str != "") {
            List<String> alList = new ArrayList<String>();
            for (char ch : str.toCharArray()) {
                alList.add(ch + "");
            }
            Collections.sort(alList);
            str = "";
            for (String s : alList) {
                str += s;
            }
        }
        return str;
    }

    /**
     * 静态类
     */
    static class ViewHolder {
        ImageView iv_delete;
        TextView tv_show_number;
        TextView tv_type_count_money;
    }
}
