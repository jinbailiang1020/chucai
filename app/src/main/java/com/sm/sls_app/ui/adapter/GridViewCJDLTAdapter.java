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
import com.sm.sls_app.ui.Buy_DLT_Activit;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.utils.NumberTools;
import com.sm.sls_app.view.MyToast;
import com.sm.sls_app.view.VibratorView;

import java.util.HashSet;

public class GridViewCJDLTAdapter extends BaseAdapter {
	public static int playType = 3901; // 选号类型
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
	public static HashSet<String> blueTuoSet = new HashSet<String>();
	// 选球界面
	private Buy_DLT_Activit activity;

	/** 构造方法 实现初始化 */
	public GridViewCJDLTAdapter(Context context, Integer[] Numbers, int c,
			int type) {
		this.context = context;
		this.Numbers = Numbers;
		this.color = c;
		this.type = type;
		vibrator = VibratorView.getVibrator(context);
		activity = (Buy_DLT_Activit) context;
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
			holder.btn.setTextColor(context.getResources().getColor(
					R.color.main_red));
			holder.btn
					.setBackgroundResource(R.drawable.icon_ball_red_unselected);
		} else {
			holder.btn.setTextColor(context.getResources().getColor(
					R.color.blue));
			holder.btn
					.setBackgroundResource(R.drawable.icon_ball_blue_unselected);
		}

		String str = (position + 1) + "";
		if (position < 9) {
			str = "0" + (position + 1);
		}
		if (type == 1) {
			// 看是否被选中
			if (redSet.contains(str)) {
				holder.btn
						.setBackgroundResource(R.drawable.icon_ball_red_selected);
				holder.btn.setTextColor(Color.WHITE);
			}
		} else if (type == 2) {
			// 看是否被选中
			if (redTuoSet.contains(str)) {
				holder.btn
						.setBackgroundResource(R.drawable.icon_ball_red_selected);
				holder.btn.setTextColor(Color.WHITE);
			}
		} else if (type == 3) {
			// 看是否被选中
			if (blueSet.contains(str)) {
				holder.btn
						.setBackgroundResource(R.drawable.icon_ball_blue_selected);
				holder.btn.setTextColor(Color.WHITE);
			}
		} else if (type == 4) {
			// 看是否被选中
			if (blueTuoSet.contains(str)) {
				holder.btn
						.setBackgroundResource(R.drawable.icon_ball_blue_selected);
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
				if (playType == 3901) {
					// 如果为红球
					if (type == 1) {
						// 判断球是否被选中
						if (redSet.contains(content)) {
							redSet.remove(content);
						} else {
							// 判断选球数量 不能超过20个
							if (redSet.size() >= 16) {
								MyToast.getToast(context, "最多允许选16个").show();
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
				else if (playType == 3903) {
					if (type == 1) {
						// 判断球是否被选中
						if (redSet.contains(content)) {
							redSet.remove(content);
						} else {
							// 如果选中了
							if (redSet.size() == 4) {
								MyToast.getToast(context, "最多允许选4个").show();
								return;
							} else {
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
							if (redSet.contains(content)) {
								redSet.remove(content);
							}
							redTuoSet.add(content);
						}
					} else if (type == 3) {
						if (blueSet.contains(content)) {
							blueSet.remove(content);
						} else {
							blueSet.add(content);
						}
					}
				}
				// 如果为 后区胆拖 玩法
				else if (playType == 3906) {
					if (type == 1) {
						// 判断球是否被选中
						if (redSet.contains(content)) {
							redSet.remove(content);
						} else {
							if (redSet.size() == 5) {
								MyToast.getToast(context, "最多允许选5个").show();
							} else
								redSet.add(content);
						}
					} else if (type == 3) {
						if (blueSet.contains(content)) {
							blueSet.remove(content);
						} else {
							if (blueSet.size() == 1) {
								MyToast.getToast(context, "最多允许选1个").show();
								return;
							} else if (blueTuoSet.contains(content)) {
								blueTuoSet.remove(content);
							}
							blueSet.add(content);
						}
					} else if (type == 4) {
						if (blueTuoSet.contains(content)) {
							blueTuoSet.remove(content);
						} else {
							if (blueSet.contains(content)) {
								blueSet.remove(content);
							}
							blueTuoSet.add(content);
						}
					}
				}
				// 如果为 双区胆拖 玩法
				else if (playType == 3907) {
					if (type == 1) {
						// 判断球是否被选中
						if (redSet.contains(content)) {
							redSet.remove(content);
						} else {
							// 如果选中了
							if (redSet.size() == 4) {
								MyToast.getToast(context, "最多允许选4个").show();
								return;
							} else {
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
							if (redSet.contains(content)) {
								redSet.remove(content);
							}
							redTuoSet.add(content);
						}
					} else if (type == 3) {
						if (blueSet.contains(content)) {
							blueSet.remove(content);
						} else {
							blueSet.clear();
							if (blueTuoSet.contains(content)) {
								blueTuoSet.remove(content);
							}
							blueSet.add(content);
						}
					} else if (type == 4) {
						if (blueTuoSet.contains(content)) {
							blueTuoSet.remove(content);
						} else {
							if (blueSet.contains(content)) {
								blueSet.remove(content);
							}
							blueTuoSet.add(content);
						}
					}
				}
				getTotalCount();
				if (count > 10000) {
					Toast.makeText(context, "投注金额不能超过20000", Toast.LENGTH_SHORT)
							.show();
					holder.btn.performClick();
				} else {
                    if (playType==3907&&redSet.size()+redTuoSet.size()<6) {
                        AppTools.totalCount=0;
                    } else {
                        AppTools.totalCount = count;
                    }
                }
				activity.updateAdapter();
				activity.tv_tatol_count.setText(+AppTools.totalCount + "");
				activity.tv_tatol_money.setText((AppTools.totalCount * 2) + "");

			}
		});
		return view;
	}

	/** 计算总注数 */
	private void getTotalCount() {
		// 3902 和 3904为追加投注。
		if (GridViewCJDLTAdapter.playType == 3901
				|| GridViewCJDLTAdapter.playType == 3902) {
			// 得到注数
			count = NumberTools.getCountForSD(redSet.size(), blueSet.size(), 5,
					2);
		} else if (GridViewCJDLTAdapter.playType == 3903
				|| GridViewCJDLTAdapter.playType == 3904) {
			// 得到注数
			if (redTuoSet.size() >= 2
					&& (redTuoSet.size() + redSet.size()) >= 5) {
				count = NumberTools.getCountForSSQ_tuo(redSet.size(),
						redTuoSet.size(), blueSet.size(), 5, 2);
				if (count < 2)
					count = 0;//不足2注，则为0
			} else {
				count = 0;
			}
		} else if (GridViewCJDLTAdapter.playType == 3906
				|| GridViewCJDLTAdapter.playType == 3908) {
			if (blueTuoSet.size() >= 2) {
				count = NumberTools.getCountForDLT(redSet.size(),
						redTuoSet.size(), blueSet.size(), blueTuoSet.size(), 1);
			} else {
				count = 0;
			}
		} else if (GridViewCJDLTAdapter.playType == 3907
				|| GridViewCJDLTAdapter.playType == 3909) {
			count = NumberTools.getCountForDLT(redSet.size(), redTuoSet.size(),
					blueSet.size(), blueTuoSet.size(), 2);
		}
	}

	/** 将传进来的值选中 */
	public void setNumByRandom() {
		activity.updateAdapter();
		getTotalCount();
		AppTools.totalCount = count;
		activity.tv_tatol_count.setText(+AppTools.totalCount + "");
		activity.tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}

	/** 静态类 */
	static class ViewHolder {
		TextView btn;
	}
}
