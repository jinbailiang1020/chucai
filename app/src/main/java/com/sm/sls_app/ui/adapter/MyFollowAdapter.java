package com.sm.sls_app.ui.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.Schemes;
import com.sm.sls_app.utils.AppTools;
import com.sm.sls_app.view.RoundProgressBar;

/**
 * 合买跟单 ListView 的Adapter
 * @author SLS003
 */
public class MyFollowAdapter extends BaseAdapter{

	private Context mContext;
	private List<Schemes> listSchemes=new ArrayList<Schemes>();
	
	public MyFollowAdapter(Context context){
		this.mContext =context;
	}
	
	public void setList(List<Schemes> list){
		clear();
		for(Schemes schemes :list){
			this.listSchemes.add(schemes);
		}
	}
	
	public void clear(){
		this.listSchemes.clear();
	}
	
	public List<Schemes> getList(){
		return this.listSchemes;
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
   public void setxin(View v,View v1,View v2,View v3,int visible){
	   v.setVisibility(visible);
	   v1.setVisibility(visible);
	   v2.setVisibility(visible);
	   v3.setVisibility(visible);
   }
	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder holder;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(mContext);
			// 得到布局文件
			view = inflater.inflate(R.layout.follow_item, null);
			// 得到控件
//			holder.LinearLayout1 = (LinearLayout) view.findViewById(R.id.follow_left);
			holder.follow_lv_iv_user_record1 =(ImageView) view.findViewById(R.id.follow_lv_iv_user_record1);
			holder.follow_lv_iv_user_record2 =(ImageView) view.findViewById(R.id.follow_lv_iv_user_record2);
			holder.follow_lv_iv_user_record3 =(ImageView) view.findViewById(R.id.follow_lv_iv_user_record3);
			holder.follow_lv_iv_user_record4 =(ImageView) view.findViewById(R.id.follow_lv_iv_user_record4);
			holder.follow_lv_tv_lotteryname=(TextView) view.findViewById(R.id.follow_lv_tv_lotteryname);
			holder.follow_lv_tv_username=(TextView) view.findViewById(R.id.follow_lv_tv_username);
			holder.follow_lv_iv_tatolmoney=(TextView) view.findViewById(R.id.follow_lv_iv_tatolmoney);
			holder.follow_lv_iv_eachmoney=(TextView) view.findViewById(R.id.follow_lv_iv_eachmoney);
			holder.follow_lv_iv_remain=(TextView) view.findViewById(R.id.follow_lv_iv_remain);
			holder.RoundProgressBar =( RoundProgressBar)view.findViewById(R.id.RoundProgressBar);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		Schemes schemes=listSchemes.get(position);
		setxin(holder.follow_lv_iv_user_record1,holder.follow_lv_iv_user_record2,holder.follow_lv_iv_user_record3,holder.follow_lv_iv_user_record4,View.GONE);
		
		//给控件赋值
		holder.follow_lv_tv_lotteryname.setText(schemes.getLotteryName()+"");
		holder.follow_lv_tv_username.setText(schemes.getInitiateName()+"");
		DecimalFormat   df   =   new   DecimalFormat("#####0.0");   
		holder.follow_lv_iv_tatolmoney.setText(df.format(schemes.getMoney())+"元");
		holder.follow_lv_iv_eachmoney.setText(df.format(schemes.getShareMoney())+"元");
		holder.follow_lv_iv_remain.setText(schemes.getSurplusShare()+"份");		
		int level=schemes.getLevel();
//		int level=9999;
		if(9999<level){//1000以上
			setxin(holder.follow_lv_iv_user_record1,holder.follow_lv_iv_user_record2,holder.follow_lv_iv_user_record3,holder.follow_lv_iv_user_record4,View.VISIBLE);
			holder.follow_lv_iv_user_record1.setBackgroundResource(AppTools.level_crown_list.get(8));
			holder.follow_lv_iv_user_record2.setBackgroundResource(AppTools.level_cup_list.get(8));
			holder.follow_lv_iv_user_record3.setBackgroundResource(AppTools.level_medal_list.get(8));
			holder.follow_lv_iv_user_record4.setBackgroundResource(AppTools.level_star_list.get(8));
		}else if(level<=9999&&level>999){//含皇冠,奖杯，奖牌，星星
			setxin(holder.follow_lv_iv_user_record1,holder.follow_lv_iv_user_record2,holder.follow_lv_iv_user_record3,holder.follow_lv_iv_user_record4,View.VISIBLE);
			int crown=level/1000;//皇冠个数
			holder.follow_lv_iv_user_record1.setBackgroundResource(AppTools.level_crown_list.get(crown-1));
			int cup=(level-1000*crown)/100;//奖杯个数
			holder.follow_lv_iv_user_record2.setBackgroundResource(AppTools.level_cup_list.get(cup-1));
			int medal=(level-1000*crown-cup*100)/10;//奖杯个数
			holder.follow_lv_iv_user_record3.setBackgroundResource(AppTools.level_medal_list.get(medal-1));
			int star=level-1000*crown-cup*100-medal*10;//星星个数
			holder.follow_lv_iv_user_record4.setBackgroundResource(AppTools.level_star_list.get(star-1));
		}else if(level<=999&&level>99){//含奖杯，奖牌，星星
			holder.follow_lv_iv_user_record2.setVisibility(View.VISIBLE);
			holder.follow_lv_iv_user_record3.setVisibility(View.VISIBLE);
			holder.follow_lv_iv_user_record4.setVisibility(View.VISIBLE);
			int cup=level/100;//奖杯个数
			holder.follow_lv_iv_user_record2.setBackgroundResource(AppTools.level_cup_list.get(cup-1));
			int medal=(level-cup*100)/10;//奖杯个数
			holder.follow_lv_iv_user_record3.setBackgroundResource(AppTools.level_medal_list.get(medal-1));
			int star=level-cup*100-medal*10;//星星个数
			holder.follow_lv_iv_user_record4.setBackgroundResource(AppTools.level_star_list.get(star-1));
		}else if(level<=99&&level>9){//含奖牌，星星
			holder.follow_lv_iv_user_record3.setVisibility(View.VISIBLE);
			holder.follow_lv_iv_user_record4.setVisibility(View.VISIBLE);
			int medal=level/10;//奖杯个数
			holder.follow_lv_iv_user_record3.setBackgroundResource(AppTools.level_medal_list.get(medal-1));
			int star=level-medal*10;//星星个数
			holder.follow_lv_iv_user_record4.setBackgroundResource(AppTools.level_star_list.get(star-1));
		}else if(level>=1){
			holder.follow_lv_iv_user_record4.setVisibility(View.VISIBLE);
			holder.follow_lv_iv_user_record4.setBackgroundResource(AppTools.level_star_list.get(level-1));
		}
        holder.RoundProgressBar.setjidu(schemes.getSchedule(),(int)(schemes.getAssureMoney()/schemes.getMoney()*100)) ;  // 方案进度  保底进度  半径
		return view;
	}
	
	
	static class ViewHolder{
		TextView follow_lv_tv_lotteryname;
		TextView follow_lv_tv_username;
		TextView follow_lv_iv_tatolmoney;
		TextView follow_lv_iv_eachmoney;
		TextView follow_lv_iv_remain;
		ImageView follow_lv_iv_user_record1 , follow_lv_iv_user_record2,follow_lv_iv_user_record3,follow_lv_iv_user_record4;
		RoundProgressBar RoundProgressBar ;
	}

}
