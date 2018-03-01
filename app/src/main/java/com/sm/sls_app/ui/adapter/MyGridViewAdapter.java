package com.sm.sls_app.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.ui.SelectNumberActivity;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.NumberTools;
import com.sm.sls_app.view.MyToast;
import com.sm.sls_app.view.VibratorView;

import java.util.HashSet;

/**
 * 选球Adapter
 *
 * @author Administrator
 */
public class MyGridViewAdapter extends BaseAdapter {

    public static int playType = 501; // 选号类型
    // 上下文本
    private Context context;
    // 装彩票的集合
    private Integer[] Numbers;
    private int color;
    private int type = 1; // 投注界面类型

    private Vibrator vibrator; // 震动器

    private long count = 0; // 投注注数

    // 存放胆区红球的 集合
    public static HashSet<String> redSet = new HashSet<String>();

    public static HashSet<String> redTuoSet = new HashSet<String>();
    public static HashSet<String> blueSet = new HashSet<String>();

    // 选球界面
    private SelectNumberActivity activity;

    /**
     * 构造方法 实现初始化
     */
    public MyGridViewAdapter(Context context, Integer[] Numbers, int c, int type) {
        this.context = context;
        this.Numbers = Numbers;
        this.color = c;
        this.type = type;
        vibrator = VibratorView.getVibrator(context);
        activity = (SelectNumberActivity) context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Numbers.length;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return Numbers[arg0];
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        // 判断View是否为空
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            // 得到布局文件
            view = inflater.inflate(R.layout.gridview_items, null);

            // 得到控件
            holder.btn = (TextView) view.findViewById(R.id.btn_showNum);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // 给控件赋值
        if (Numbers[position] < 10) {
            holder.btn.setText("0" + Numbers[position] + "");
        } else {
            holder.btn.setText(Numbers[position] + "");
        }

        // 设置字体显示颜色 和背景图片
        if (Color.RED == color) {
            holder.btn.setTextColor(context.getResources()
                    .getColor(R.color.main_red));
            holder.btn.setBackgroundResource(R.drawable.icon_ball_red_unselected);
        } else {
            holder.btn.setTextColor(context.getResources().getColor(
                    R.color.blue));
            holder.btn.setBackgroundResource(R.drawable.icon_ball_blue_unselected);
        }


        String str = (position + 1) + "";
        if (position < 9) {
            str = "0" + (position + 1);
        }

        if (type == 1) {
            // 看是否被选中
            if (redSet.contains(str)) {
                holder.btn.setBackgroundResource(R.drawable.icon_ball_red_selected);
                holder.btn.setTextColor(Color.WHITE);
            }
        } else if (type == 2) {
            // 看是否被选中
            if (redTuoSet.contains(str)) {
                holder.btn.setBackgroundResource(R.drawable.icon_ball_red_selected);
                holder.btn.setTextColor(Color.WHITE);
            }
        } else if (type == 3) {
            // 看是否被选中
            if (blueSet.contains(str)) {
                holder.btn.setBackgroundResource(R.drawable.icon_ball_blue_selected);
                holder.btn.setTextColor(Color.WHITE);
            }
        }

        // 给按钮添加点击事件
        holder.btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != vibrator) {
                    // 震动 100 毫秒
                    vibrator.vibrate(100);
                }
                // 得到选号的内容
                String content = holder.btn.getText().toString();
                // 如果为普通玩法
                if (playType == 501) {
                    // 如果为红球
                    if (type == 1) {
                        // 判断球是否被选中
                        if (redSet.contains(content)) {
                            redSet.remove(content);
                        } else {
                            // 判断选球数量 不能超过20个
                            if (redSet.size() >= 20) {
                                Toast.makeText(context, "红球数量不能超过20个",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                redSet.add(content);
                            }
                        }
                    }
                    // 如果为蓝球
                    else if (type == 3) {
                        // 判断球是否被选中
                        if (blueSet.contains(content)) {
                            blueSet.remove(content);
                        } else {
                            blueSet.add(content);
                        }
                    }
                }
                // 如果为 胆拖 玩法
                else if (playType == 502) {
                    if (type == 1) {
                        // 判断球是否被选中 选中删除
                        if (redSet.contains(content)) {
                            redSet.remove(content);
                        } //没选中
                        else {
                            // 判断数量大于5
                            if (redSet.size() >= 5) {
                                MyToast.getToast(context, "最多只能选中5个").show();
                            } else {
                                // 如果拖区选中了
                                if (redTuoSet.contains(content)) {
                                    redTuoSet.remove(content);
                                }
                                redSet.add(content);
                            }
                        }
                    } else if (type == 2) {
                        if (redTuoSet.contains(content)) {
                            redTuoSet.remove(content);
                        } else {

                            if (redTuoSet.size() + redSet.size() == 20) {
                                MyToast.getToast(context, "所有红球最多只能选20个").show();
                            }else{
                                if (redSet.contains(content)) {
                                    redSet.remove(content);
                                }
                                redTuoSet.add(content);
                            }
                        }

                    } else if (type == 3) {
                        if (blueSet.contains(content)) {
                            blueSet.remove(content);
                        } else {
                            blueSet.add(content);
                        }
                    }
                }

                getTotalCount();

                if (count > 10000) {
                    MyToast.getToast(context, "投注金额不能超过20000").show();
                    if (type == 1) {
                        redSet.remove(content);
                    } else if (type == 2) {
                        redTuoSet.remove(content);
                    } else if (type == 3) {
                        blueSet.remove(content);
                    }
                    getTotalCount();
                } else {
                    if (playType == 502 && redTuoSet.size() < 2) {
                        count = 0;
                    }
                }
                AppTools.totalCount = count;
                activity.updateAdapter();
                activity.tv_tatol_count.setText(+AppTools.totalCount + "");
                activity.tv_tatol_money.setText((AppTools.totalCount * 2) + "");
            }
        });
        return view;
    }

    /**
     * 计算总注数
     */
    private void getTotalCount() {
        if (MyGridViewAdapter.playType == 501) {
            // 得到注数
            count = NumberTools.getCountForSD(redSet.size(), blueSet.size(), 6,
                    1);
        }
        if (MyGridViewAdapter.playType == 502) {
            // 得到注数
            count = NumberTools.getCountForSSQ_tuo(redSet.size(),
                    redTuoSet.size(), blueSet.size(), 6, 1);
        }
    }

    /**
     * 将传进来的值选中
     */
    public void setNumByRandom() {
        activity.updateAdapter();
        getTotalCount();
        AppTools.totalCount = count;
        activity.tv_tatol_count.setText(AppTools.totalCount + "");
        activity.tv_tatol_money.setText((AppTools.totalCount * 2) + "");
    }

    /**
     * 静态类
     */
    static class ViewHolder {
        TextView btn;
    }

}
