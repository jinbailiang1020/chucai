package com.sm.sls_app.view;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.dataaccess.DtMatch;
import com.sm.sls_app.utils.AppTools;

/**
 * 竞彩足球选球弹出框
 * 
 * @author Kinwee 修改日期2014-12-8
 * 
 */
@SuppressLint({ "UseSparseArrays", "ResourceAsColor" })
public class SelectJCZQDialog extends Dialog implements OnClickListener {

	private final static String TAG = "SelectJCZQDialog";
	private Context context;

	private TextView tv_mainteam;// 客队
	private TextView tv_guestteam;// 主队
	private Button dl_btn_cancel;// 取消
	private Button dl_btn_confirm;// 确定

	private ArrayList<String> selectResult = new ArrayList<String>();// 选中的结果集合

	private LinearLayout layout_rangqiu_notrangqiu;// 让球和非让球布局
	private LinearLayout layout_frq;// 非让球布局
	private LinearLayout zjq_bottom;// 让球布局
	private LinearLayout layout_bifen;// 比分布局
	private LinearLayout layout_zjq;// 总进球布局
	private LinearLayout layout_bqc;// 半全场布局

	private TextView tv_rangqiu;// 主队让球
	// 非让球
	private LinearLayout layout_item101, layout_item102, layout_item103;// 胜，平，负
	private TextView tv_101, tv_102, tv_103;// 标签
	private TextView tv_item101, tv_item102, tv_item103;// 赔率
	// 让球
	private LinearLayout layout_item501, layout_item502, layout_item503;// 胜，平，负
	private TextView tv_501, tv_502, tv_503;// 标签
	private TextView tv_item501, tv_item502, tv_item503;// 赔率
	// 比分
	private LinearLayout layout_item301, layout_item302, layout_item303,
			layout_item304, layout_item305, layout_item306, layout_item307,
			layout_item308, layout_item309, layout_item310, layout_item311,
			layout_item312, layout_item313, layout_item314, layout_item315,
			layout_item316, layout_item317, layout_item318, layout_item319,
			layout_item320, layout_item321, layout_item322, layout_item323,
			layout_item324, layout_item325, layout_item326, layout_item327,
			layout_item328, layout_item329, layout_item330, layout_item331;
	private TextView tv_item301, tv_item302, tv_item303, tv_item304,
			tv_item305, tv_item306, tv_item307, tv_item308, tv_item309,
			tv_item310, tv_item311, tv_item312, tv_item313, tv_item314,
			tv_item315, tv_item316, tv_item317, tv_item318, tv_item319,
			tv_item320, tv_item321, tv_item322, tv_item323, tv_item324,
			tv_item325, tv_item326, tv_item327, tv_item328, tv_item329,
			tv_item330, tv_item331;// 赔率
	private TextView tv_301, tv_302, tv_303, tv_304, tv_305, tv_306, tv_307,
			tv_308, tv_309, tv_310, tv_311, tv_312, tv_313, tv_314, tv_315,
			tv_316, tv_317, tv_318, tv_319, tv_320, tv_321, tv_322, tv_323,
			tv_324, tv_325, tv_326, tv_327, tv_328, tv_329, tv_330, tv_331;// 标签
	// 总进球
	private LinearLayout layout_item201, layout_item202, layout_item203,
			layout_item204, layout_item205, layout_item206, layout_item207,
			layout_item208;
	private TextView tv_201, tv_202, tv_203, tv_204, tv_205, tv_206, tv_207,
			tv_208;// 标签
	private TextView tv_item201, tv_item202, tv_item203, tv_item204,
			tv_item205, tv_item206, tv_item207, tv_item208;// 赔率
	// 半全场
	private LinearLayout layout_item401, layout_item402, layout_item403,
			layout_item404, layout_item405, layout_item406, layout_item407,
			layout_item408, layout_item409;
	private TextView tv_401, tv_402, tv_403, tv_404, tv_405, tv_406, tv_407,
			tv_408, tv_409;// 标签
	private TextView tv_item401, tv_item402, tv_item403, tv_item404,
			tv_item405, tv_item406, tv_item407, tv_item408, tv_item409;// 赔率
	private DtMatch dm;// 比赛对阵
	
	private LinearLayout layout_rq;
	private TextView tv_rq;

	public SelectJCZQDialog(Context context, DtMatch dm) {
		super(context);
		this.dm = dm;
	}

	public SelectJCZQDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener, DtMatch dm) {
		super(context, cancelable, cancelListener);
		this.dm = dm;
	}

	public SelectJCZQDialog(Context context, int theme, DtMatch dm) {
		super(context, theme);
		this.dm = dm;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_jczq_dialog);
		
		this.setCancelable(false);
		findView();
		init();
		setListner();
	}

	public void findView() {
		layout_rq = (LinearLayout) findViewById(R.id.layout_rq);
		tv_rq = (TextView) findViewById(R.id.tv_rq);
		layout_rangqiu_notrangqiu = (LinearLayout) findViewById(R.id.layout_rangqiu_notrangqiu);
		layout_frq = (LinearLayout) findViewById(R.id.layout_frq);
		zjq_bottom = (LinearLayout) findViewById(R.id.zjq_bottom);
		layout_bifen = (LinearLayout) findViewById(R.id.layout_score);
		layout_zjq = (LinearLayout) findViewById(R.id.layout_zjq);
		layout_bqc = (LinearLayout) findViewById(R.id.layout_bqc);
		tv_mainteam = (TextView) findViewById(R.id.tv_mainteam);
		tv_guestteam = (TextView) findViewById(R.id.tv_guestteam);
		tv_rangqiu = (TextView) findViewById(R.id.tv_rangqiu);
		dl_btn_cancel = (Button) findViewById(R.id.dl_btn_cancel);
		dl_btn_confirm = (Button) findViewById(R.id.dl_btn_confirm);

		// 非让球
		// 胜平负
		layout_item101 = (LinearLayout) findViewById(R.id.layout_item101);
		layout_item102 = (LinearLayout) findViewById(R.id.layout_item102);
		layout_item103 = (LinearLayout) findViewById(R.id.layout_item103);
		// 赔率
		tv_item101 = (TextView) findViewById(R.id.tv_item101);
		tv_item102 = (TextView) findViewById(R.id.tv_item102);
		tv_item103 = (TextView) findViewById(R.id.tv_item103);
		tv_101 = (TextView) findViewById(R.id.tv_101);
		tv_102 = (TextView) findViewById(R.id.tv_102);
		tv_103 = (TextView) findViewById(R.id.tv_103);
		// 让球
		// 胜平负
		layout_item501 = (LinearLayout) findViewById(R.id.layout_item501);
		layout_item502 = (LinearLayout) findViewById(R.id.layout_item502);
		layout_item503 = (LinearLayout) findViewById(R.id.layout_item503);
		// 赔率
		tv_item501 = (TextView) findViewById(R.id.tv_item501);
		tv_item502 = (TextView) findViewById(R.id.tv_item502);
		tv_item503 = (TextView) findViewById(R.id.tv_item503);
		tv_501 = (TextView) findViewById(R.id.tv_501);
		tv_502 = (TextView) findViewById(R.id.tv_502);
		tv_503 = (TextView) findViewById(R.id.tv_503);

		// 比分
		// 布局
		layout_item301 = (LinearLayout) findViewById(R.id.layout_item301);
		layout_item302 = (LinearLayout) findViewById(R.id.layout_item302);
		layout_item303 = (LinearLayout) findViewById(R.id.layout_item303);
		layout_item304 = (LinearLayout) findViewById(R.id.layout_item304);
		layout_item305 = (LinearLayout) findViewById(R.id.layout_item305);
		layout_item306 = (LinearLayout) findViewById(R.id.layout_item306);
		layout_item307 = (LinearLayout) findViewById(R.id.layout_item307);
		layout_item308 = (LinearLayout) findViewById(R.id.layout_item308);
		layout_item309 = (LinearLayout) findViewById(R.id.layout_item309);
		layout_item310 = (LinearLayout) findViewById(R.id.layout_item310);
		layout_item311 = (LinearLayout) findViewById(R.id.layout_item311);
		layout_item312 = (LinearLayout) findViewById(R.id.layout_item312);
		layout_item313 = (LinearLayout) findViewById(R.id.layout_item313);
		layout_item314 = (LinearLayout) findViewById(R.id.layout_item314);
		layout_item315 = (LinearLayout) findViewById(R.id.layout_item315);
		layout_item316 = (LinearLayout) findViewById(R.id.layout_item316);
		layout_item317 = (LinearLayout) findViewById(R.id.layout_item317);
		layout_item318 = (LinearLayout) findViewById(R.id.layout_item318);
		layout_item319 = (LinearLayout) findViewById(R.id.layout_item319);
		layout_item320 = (LinearLayout) findViewById(R.id.layout_item320);
		layout_item321 = (LinearLayout) findViewById(R.id.layout_item321);
		layout_item322 = (LinearLayout) findViewById(R.id.layout_item322);
		layout_item323 = (LinearLayout) findViewById(R.id.layout_item323);
		layout_item324 = (LinearLayout) findViewById(R.id.layout_item324);
		layout_item325 = (LinearLayout) findViewById(R.id.layout_item325);
		layout_item326 = (LinearLayout) findViewById(R.id.layout_item326);
		layout_item327 = (LinearLayout) findViewById(R.id.layout_item327);
		layout_item328 = (LinearLayout) findViewById(R.id.layout_item328);
		layout_item329 = (LinearLayout) findViewById(R.id.layout_item329);
		layout_item330 = (LinearLayout) findViewById(R.id.layout_item330);
		layout_item331 = (LinearLayout) findViewById(R.id.layout_item331);
		// 赔率
		tv_item301 = (TextView) findViewById(R.id.tv_item301);
		tv_item302 = (TextView) findViewById(R.id.tv_item302);
		tv_item303 = (TextView) findViewById(R.id.tv_item303);
		tv_item304 = (TextView) findViewById(R.id.tv_item304);
		tv_item305 = (TextView) findViewById(R.id.tv_item305);
		tv_item306 = (TextView) findViewById(R.id.tv_item306);
		tv_item307 = (TextView) findViewById(R.id.tv_item307);
		tv_item308 = (TextView) findViewById(R.id.tv_item308);
		tv_item309 = (TextView) findViewById(R.id.tv_item309);
		tv_item310 = (TextView) findViewById(R.id.tv_item310);
		tv_item311 = (TextView) findViewById(R.id.tv_item311);
		tv_item312 = (TextView) findViewById(R.id.tv_item312);
		tv_item313 = (TextView) findViewById(R.id.tv_item313);
		tv_item314 = (TextView) findViewById(R.id.tv_item314);
		tv_item315 = (TextView) findViewById(R.id.tv_item315);
		tv_item316 = (TextView) findViewById(R.id.tv_item316);
		tv_item317 = (TextView) findViewById(R.id.tv_item317);
		tv_item318 = (TextView) findViewById(R.id.tv_item318);
		tv_item319 = (TextView) findViewById(R.id.tv_item319);
		tv_item320 = (TextView) findViewById(R.id.tv_item320);
		tv_item321 = (TextView) findViewById(R.id.tv_item321);
		tv_item322 = (TextView) findViewById(R.id.tv_item322);
		tv_item323 = (TextView) findViewById(R.id.tv_item323);
		tv_item324 = (TextView) findViewById(R.id.tv_item324);
		tv_item325 = (TextView) findViewById(R.id.tv_item325);
		tv_item326 = (TextView) findViewById(R.id.tv_item326);
		tv_item327 = (TextView) findViewById(R.id.tv_item327);
		tv_item328 = (TextView) findViewById(R.id.tv_item328);
		tv_item329 = (TextView) findViewById(R.id.tv_item329);
		tv_item330 = (TextView) findViewById(R.id.tv_item330);
		tv_item331 = (TextView) findViewById(R.id.tv_item331);
		// 标签
		tv_301 = (TextView) findViewById(R.id.tv_301);
		tv_302 = (TextView) findViewById(R.id.tv_302);
		tv_303 = (TextView) findViewById(R.id.tv_303);
		tv_304 = (TextView) findViewById(R.id.tv_304);
		tv_305 = (TextView) findViewById(R.id.tv_305);
		tv_306 = (TextView) findViewById(R.id.tv_306);
		tv_307 = (TextView) findViewById(R.id.tv_307);
		tv_308 = (TextView) findViewById(R.id.tv_308);
		tv_309 = (TextView) findViewById(R.id.tv_309);
		tv_310 = (TextView) findViewById(R.id.tv_310);
		tv_311 = (TextView) findViewById(R.id.tv_311);
		tv_312 = (TextView) findViewById(R.id.tv_312);
		tv_313 = (TextView) findViewById(R.id.tv_313);
		tv_314 = (TextView) findViewById(R.id.tv_314);
		tv_315 = (TextView) findViewById(R.id.tv_315);
		tv_316 = (TextView) findViewById(R.id.tv_316);
		tv_317 = (TextView) findViewById(R.id.tv_317);
		tv_318 = (TextView) findViewById(R.id.tv_318);
		tv_319 = (TextView) findViewById(R.id.tv_319);
		tv_320 = (TextView) findViewById(R.id.tv_320);
		tv_321 = (TextView) findViewById(R.id.tv_321);
		tv_322 = (TextView) findViewById(R.id.tv_322);
		tv_323 = (TextView) findViewById(R.id.tv_323);
		tv_324 = (TextView) findViewById(R.id.tv_324);
		tv_325 = (TextView) findViewById(R.id.tv_325);
		tv_326 = (TextView) findViewById(R.id.tv_326);
		tv_327 = (TextView) findViewById(R.id.tv_327);
		tv_328 = (TextView) findViewById(R.id.tv_328);
		tv_329 = (TextView) findViewById(R.id.tv_329);
		tv_330 = (TextView) findViewById(R.id.tv_330);
		tv_331 = (TextView) findViewById(R.id.tv_331);
		// 总进球
		// 布局
		layout_item201 = (LinearLayout) findViewById(R.id.layout_item201);
		layout_item202 = (LinearLayout) findViewById(R.id.layout_item202);
		layout_item203 = (LinearLayout) findViewById(R.id.layout_item203);
		layout_item204 = (LinearLayout) findViewById(R.id.layout_item204);
		layout_item205 = (LinearLayout) findViewById(R.id.layout_item205);
		layout_item206 = (LinearLayout) findViewById(R.id.layout_item206);
		layout_item207 = (LinearLayout) findViewById(R.id.layout_item207);
		layout_item208 = (LinearLayout) findViewById(R.id.layout_item208);
		// 赔率
		tv_item201 = (TextView) findViewById(R.id.tv_item201);
		tv_item202 = (TextView) findViewById(R.id.tv_item202);
		tv_item203 = (TextView) findViewById(R.id.tv_item203);
		tv_item204 = (TextView) findViewById(R.id.tv_item204);
		tv_item205 = (TextView) findViewById(R.id.tv_item205);
		tv_item206 = (TextView) findViewById(R.id.tv_item206);
		tv_item207 = (TextView) findViewById(R.id.tv_item207);
		tv_item208 = (TextView) findViewById(R.id.tv_item208);
		// 标签
		tv_201 = (TextView) findViewById(R.id.tv_201);
		tv_202 = (TextView) findViewById(R.id.tv_202);
		tv_203 = (TextView) findViewById(R.id.tv_203);
		tv_204 = (TextView) findViewById(R.id.tv_204);
		tv_205 = (TextView) findViewById(R.id.tv_205);
		tv_206 = (TextView) findViewById(R.id.tv_206);
		tv_207 = (TextView) findViewById(R.id.tv_207);
		tv_208 = (TextView) findViewById(R.id.tv_208);
		// 半全场
		// 布局
		layout_item401 = (LinearLayout) findViewById(R.id.layout_item401);
		layout_item402 = (LinearLayout) findViewById(R.id.layout_item402);
		layout_item403 = (LinearLayout) findViewById(R.id.layout_item403);
		layout_item404 = (LinearLayout) findViewById(R.id.layout_item404);
		layout_item405 = (LinearLayout) findViewById(R.id.layout_item405);
		layout_item406 = (LinearLayout) findViewById(R.id.layout_item406);
		layout_item407 = (LinearLayout) findViewById(R.id.layout_item407);
		layout_item408 = (LinearLayout) findViewById(R.id.layout_item408);
		layout_item409 = (LinearLayout) findViewById(R.id.layout_item409);
		// 赔率
		tv_item401 = (TextView) findViewById(R.id.tv_item401);
		tv_item402 = (TextView) findViewById(R.id.tv_item402);
		tv_item403 = (TextView) findViewById(R.id.tv_item403);
		tv_item404 = (TextView) findViewById(R.id.tv_item404);
		tv_item405 = (TextView) findViewById(R.id.tv_item405);
		tv_item406 = (TextView) findViewById(R.id.tv_item406);
		tv_item407 = (TextView) findViewById(R.id.tv_item407);
		tv_item408 = (TextView) findViewById(R.id.tv_item408);
		tv_item409 = (TextView) findViewById(R.id.tv_item409);
		// 标签
		tv_401 = (TextView) findViewById(R.id.tv_401);
		tv_402 = (TextView) findViewById(R.id.tv_402);
		tv_403 = (TextView) findViewById(R.id.tv_403);
		tv_404 = (TextView) findViewById(R.id.tv_404);
		tv_405 = (TextView) findViewById(R.id.tv_405);
		tv_406 = (TextView) findViewById(R.id.tv_406);
		tv_407 = (TextView) findViewById(R.id.tv_407);
		tv_408 = (TextView) findViewById(R.id.tv_408);
		tv_409 = (TextView) findViewById(R.id.tv_409);
	}

	public void init() {
		//隐藏布局
		if(!dm.isSPF()){//让球胜平负
			tv_rangqiu.setVisibility(View.GONE);
			zjq_bottom.setVisibility(View.GONE);// 让球布局
		}
		if(!dm.isNewSPF()){//胜平负
			layout_frq.setVisibility(View.GONE);// 非让球布局
		}
		if(!dm.isCBF()){//比分
			layout_bifen.setVisibility(View.GONE);// 比分布局
		}
		if(!dm.isZJQ()){//总进球
			layout_zjq.setVisibility(View.GONE);// 总进球布局
		}
		if(!dm.isBQC()){//半全场
			layout_bqc.setVisibility(View.GONE);// 半全场布局
		}
		tv_mainteam.setText(dm.getMainTeam());
		tv_guestteam.setText(dm.getGuestTeam());
		String color="";
		String ball="";
		if(dm.getMainLoseBall()>0){
			ball="+"+dm.getMainLoseBall();
			color="#e3393c";
		}else {
			ball=""+dm.getMainLoseBall();
			color="#0d9930";
		}
		Spanned text = Html.fromHtml(AppTools.changeStringColor("#808080",
				"主队（" + dm.getMainTeam() + "）让球  ")
				+ AppTools.changeStringColor(color, 
						ball+ ""));
		tv_rangqiu.setText(text);
		// 胜平负
		tv_item101.setText(dm.getSpfwin());
		tv_item102.setText(dm.getSpfflat());
		tv_item103.setText(dm.getSpflose());
		// 让球胜平负
		tv_item501.setText(dm.getWin());
		tv_item502.setText(dm.getFlat());
		tv_item503.setText(dm.getLose());
		// 比分
		// 胜
		tv_item301.setText(dm.getS10());
		tv_item302.setText(dm.getS20());
		tv_item303.setText(dm.getS21());
		tv_item304.setText(dm.getS30());
		tv_item305.setText(dm.getS31());
		tv_item306.setText(dm.getS32());
		tv_item307.setText(dm.getS40());
		tv_item308.setText(dm.getS41());
		tv_item309.setText(dm.getS42());
		tv_item310.setText(dm.getS50());
		tv_item311.setText(dm.getS51());
		tv_item312.setText(dm.getS52());
		tv_item313.setText(dm.getSother());
		// 平
		tv_item314.setText(dm.getP00());
		tv_item315.setText(dm.getP11());
		tv_item316.setText(dm.getP22());
		tv_item317.setText(dm.getP33());
		tv_item318.setText(dm.getPother());
		// 负
		tv_item319.setText(dm.getF01());
		tv_item320.setText(dm.getF02());
		tv_item321.setText(dm.getF12());
		tv_item322.setText(dm.getF03());
		tv_item323.setText(dm.getF13());
		tv_item324.setText(dm.getF23());
		tv_item325.setText(dm.getF04());
		tv_item326.setText(dm.getF14());
		tv_item327.setText(dm.getF24());
		tv_item328.setText(dm.getF05());
		tv_item329.setText(dm.getF15());
		tv_item330.setText(dm.getF25());
		tv_item331.setText(dm.getFother());
		// 总进球
		tv_item201.setText(dm.getIn0());
		tv_item202.setText(dm.getIn1());
		tv_item203.setText(dm.getIn2());
		tv_item204.setText(dm.getIn3());
		tv_item205.setText(dm.getIn4());
		tv_item206.setText(dm.getIn5());
		tv_item207.setText(dm.getIn6());
		tv_item208.setText(dm.getIn7());
		// 半全场
		tv_item401.setText(dm.getSs());
		tv_item402.setText(dm.getSp());
		tv_item403.setText(dm.getSf());
		tv_item404.setText(dm.getPs());
		tv_item405.setText(dm.getPp());
		tv_item406.setText(dm.getPf());
		tv_item407.setText(dm.getFs());
		tv_item408.setText(dm.getFp());
		tv_item409.setText(dm.getFf());
	}

	/**
	 * 设置选中的集合
	 * 
	 * @param selectResult
	 */
	public void setSelect(ArrayList<String> selectResult) {
		this.selectResult = selectResult;
		// 胜平负
		setAllBackGround("101", layout_item101, tv_101, tv_item101);
		setAllBackGround("102", layout_item102, tv_102, tv_item102);
		setAllBackGround("103", layout_item103, tv_103, tv_item103);
		// 让球胜平负
		setAllBackGround("501", layout_item501, tv_501, tv_item501);
		setAllBackGround("502", layout_item502, tv_502, tv_item502);
		setAllBackGround("503", layout_item503, tv_503, tv_item503);
		// 比分
		setAllBackGround("301", layout_item301, tv_301, tv_item301);
		setAllBackGround("302", layout_item302, tv_302, tv_item302);
		setAllBackGround("303", layout_item303, tv_303, tv_item303);
		setAllBackGround("304", layout_item304, tv_304, tv_item304);
		setAllBackGround("305", layout_item305, tv_305, tv_item305);
		setAllBackGround("306", layout_item306, tv_306, tv_item306);
		setAllBackGround("307", layout_item307, tv_307, tv_item307);
		setAllBackGround("308", layout_item308, tv_308, tv_item308);
		setAllBackGround("309", layout_item309, tv_309, tv_item309);
		setAllBackGround("310", layout_item310, tv_310, tv_item310);
		setAllBackGround("311", layout_item311, tv_311, tv_item311);
		setAllBackGround("312", layout_item312, tv_312, tv_item312);
		setAllBackGround("313", layout_item313, tv_313, tv_item313);
		setAllBackGround("314", layout_item314, tv_314, tv_item314);
		setAllBackGround("315", layout_item315, tv_315, tv_item315);
		setAllBackGround("316", layout_item316, tv_316, tv_item316);
		setAllBackGround("317", layout_item317, tv_317, tv_item317);
		setAllBackGround("318", layout_item318, tv_318, tv_item318);
		setAllBackGround("319", layout_item319, tv_319, tv_item319);
		setAllBackGround("320", layout_item320, tv_320, tv_item320);
		setAllBackGround("321", layout_item321, tv_321, tv_item321);
		setAllBackGround("322", layout_item322, tv_322, tv_item322);
		setAllBackGround("323", layout_item323, tv_323, tv_item323);
		setAllBackGround("324", layout_item324, tv_324, tv_item324);
		setAllBackGround("325", layout_item325, tv_325, tv_item325);
		setAllBackGround("326", layout_item326, tv_326, tv_item326);
		setAllBackGround("327", layout_item327, tv_327, tv_item327);
		setAllBackGround("328", layout_item328, tv_328, tv_item328);
		setAllBackGround("329", layout_item329, tv_329, tv_item329);
		setAllBackGround("330", layout_item330, tv_330, tv_item330);
		setAllBackGround("331", layout_item331, tv_331, tv_item331);
		// 总进球
		setAllBackGround("201", layout_item201, tv_201, tv_item201);
		setAllBackGround("202", layout_item202, tv_202, tv_item202);
		setAllBackGround("203", layout_item203, tv_203, tv_item203);
		setAllBackGround("204", layout_item204, tv_204, tv_item204);
		setAllBackGround("205", layout_item205, tv_205, tv_item205);
		setAllBackGround("206", layout_item206, tv_206, tv_item206);
		setAllBackGround("207", layout_item207, tv_207, tv_item207);
		setAllBackGround("208", layout_item208, tv_208, tv_item208);
		// 半全场
		setAllBackGround("401", layout_item401, tv_401, tv_item401);
		setAllBackGround("402", layout_item402, tv_402, tv_item402);
		setAllBackGround("403", layout_item403, tv_403, tv_item403);
		setAllBackGround("404", layout_item404, tv_404, tv_item404);
		setAllBackGround("405", layout_item405, tv_405, tv_item405);
		setAllBackGround("406", layout_item406, tv_406, tv_item406);
		setAllBackGround("407", layout_item407, tv_407, tv_item407);
		setAllBackGround("408", layout_item408, tv_408, tv_item408);
		setAllBackGround("409", layout_item409, tv_409, tv_item409);
	}

	/**
	 * 设置让球和非让球是否可见
	 * 
	 * @param visible
	 */
	public void setSpfLayoutVisible(int visible) {
		layout_rangqiu_notrangqiu.setVisibility(visible);
	}

	/**
	 * 设置比分是否可见
	 * 
	 * @param visible
	 */
	public void setBifenLayoutVisible(int visible) {
		layout_bifen.setVisibility(visible);
	}

	/**
	 * 设置总进球是否可见
	 * 
	 * @param visible
	 */
	public void setZjqLayoutVisible(int visible) {
		layout_zjq.setVisibility(visible);
	}

	/**
	 * 设置半全场是否可见
	 * 
	 * @param visible
	 */
	public void setBqcLayoutVisible(int visible) {
		layout_bqc.setVisibility(visible);
	}

	/** 绑定监听 **/
	private void setListner() {
		dl_btn_cancel.setOnClickListener(this);
		dl_btn_confirm.setOnClickListener(this);
		// 胜平负
		layout_item101.setOnClickListener(this);
		layout_item102.setOnClickListener(this);
		layout_item103.setOnClickListener(this);
		// 让球胜平负
		layout_item501.setOnClickListener(this);
		layout_item502.setOnClickListener(this);
		layout_item503.setOnClickListener(this);
		// 比分
		layout_item301.setOnClickListener(this);
		layout_item302.setOnClickListener(this);
		layout_item303.setOnClickListener(this);
		layout_item304.setOnClickListener(this);
		layout_item305.setOnClickListener(this);
		layout_item306.setOnClickListener(this);
		layout_item307.setOnClickListener(this);
		layout_item308.setOnClickListener(this);
		layout_item309.setOnClickListener(this);
		layout_item310.setOnClickListener(this);
		layout_item311.setOnClickListener(this);
		layout_item312.setOnClickListener(this);
		layout_item313.setOnClickListener(this);
		layout_item314.setOnClickListener(this);
		layout_item315.setOnClickListener(this);
		layout_item316.setOnClickListener(this);
		layout_item317.setOnClickListener(this);
		layout_item318.setOnClickListener(this);
		layout_item319.setOnClickListener(this);
		layout_item320.setOnClickListener(this);
		layout_item321.setOnClickListener(this);
		layout_item322.setOnClickListener(this);
		layout_item323.setOnClickListener(this);
		layout_item324.setOnClickListener(this);
		layout_item325.setOnClickListener(this);
		layout_item326.setOnClickListener(this);
		layout_item327.setOnClickListener(this);
		layout_item328.setOnClickListener(this);
		layout_item329.setOnClickListener(this);
		layout_item330.setOnClickListener(this);
		layout_item331.setOnClickListener(this);
		// 总进球
		layout_item201.setOnClickListener(this);
		layout_item202.setOnClickListener(this);
		layout_item203.setOnClickListener(this);
		layout_item204.setOnClickListener(this);
		layout_item205.setOnClickListener(this);
		layout_item206.setOnClickListener(this);
		layout_item207.setOnClickListener(this);
		layout_item208.setOnClickListener(this);
		// 半全场
		layout_item401.setOnClickListener(this);
		layout_item402.setOnClickListener(this);
		layout_item403.setOnClickListener(this);
		layout_item404.setOnClickListener(this);
		layout_item405.setOnClickListener(this);
		layout_item406.setOnClickListener(this);
		layout_item407.setOnClickListener(this);
		layout_item408.setOnClickListener(this);
		layout_item409.setOnClickListener(this);
	}

	/** 点击监听 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dl_btn_cancel:// 取消
			if (null != this.listener) {
				listener.getResult(0, selectResult);
			}
			this.dismiss();
//			selectResult.clear();
			break;
		case R.id.dl_btn_confirm:// 确定
			if (null != this.listener) {
				listener.getResult(1, selectResult);
			}
			this.dismiss();
//			selectResult.clear();
			break;
		// 胜平负
		case R.id.layout_item101:
			addAndRemove("101", layout_item101, tv_101, tv_item101);
			break;
		case R.id.layout_item102:
			addAndRemove("102", layout_item102, tv_102, tv_item102);
			break;
		case R.id.layout_item103:
			addAndRemove("103", layout_item103, tv_103, tv_item103);
			break;
		// 让球胜平负
		case R.id.layout_item501:
			addAndRemove("501", layout_item501, tv_501, tv_item501);
			break;
		case R.id.layout_item502:
			addAndRemove("502", layout_item502, tv_502, tv_item502);
			break;
		case R.id.layout_item503:
			addAndRemove("503", layout_item503, tv_503, tv_item503);
			break;
		// 比分
		case R.id.layout_item301:
			addAndRemove("301", layout_item301, tv_301, tv_item301);
			break;
		case R.id.layout_item302:
			addAndRemove("302", layout_item302, tv_302, tv_item302);
			break;
		case R.id.layout_item303:
			addAndRemove("303", layout_item303, tv_303, tv_item303);
			break;
		case R.id.layout_item304:
			addAndRemove("304", layout_item304, tv_304, tv_item304);
			break;
		case R.id.layout_item305:
			addAndRemove("305", layout_item305, tv_305, tv_item305);
			break;
		case R.id.layout_item306:
			addAndRemove("306", layout_item306, tv_306, tv_item306);
			break;
		case R.id.layout_item307:
			addAndRemove("307", layout_item307, tv_307, tv_item307);
			break;
		case R.id.layout_item308:
			addAndRemove("308", layout_item308, tv_308, tv_item308);
			break;
		case R.id.layout_item309:
			addAndRemove("309", layout_item309, tv_309, tv_item309);
			break;
		case R.id.layout_item310:
			addAndRemove("310", layout_item310, tv_310, tv_item310);
			break;
		case R.id.layout_item311:
			addAndRemove("311", layout_item311, tv_311, tv_item311);
			break;
		case R.id.layout_item312:
			addAndRemove("312", layout_item312, tv_312, tv_item312);
			break;
		case R.id.layout_item313:
			addAndRemove("313", layout_item313, tv_313, tv_item313);
			break;
		case R.id.layout_item314:
			addAndRemove("314", layout_item314, tv_314, tv_item314);
			break;
		case R.id.layout_item315:
			addAndRemove("315", layout_item315, tv_315, tv_item315);
			break;
		case R.id.layout_item316:
			addAndRemove("316", layout_item316, tv_316, tv_item316);
			break;
		case R.id.layout_item317:
			addAndRemove("317", layout_item317, tv_317, tv_item317);
			break;
		case R.id.layout_item318:
			addAndRemove("318", layout_item318, tv_318, tv_item318);
			break;
		case R.id.layout_item319:
			addAndRemove("319", layout_item319, tv_319, tv_item319);
			break;
		case R.id.layout_item320:
			addAndRemove("320", layout_item320, tv_320, tv_item320);
			break;
		case R.id.layout_item321:
			addAndRemove("321", layout_item321, tv_321, tv_item321);
			break;
		case R.id.layout_item322:
			addAndRemove("322", layout_item322, tv_322, tv_item322);
			break;
		case R.id.layout_item323:
			addAndRemove("323", layout_item323, tv_323, tv_item323);
			break;
		case R.id.layout_item324:
			addAndRemove("324", layout_item324, tv_324, tv_item324);
			break;
		case R.id.layout_item325:
			addAndRemove("325", layout_item325, tv_325, tv_item325);
			break;
		case R.id.layout_item326:
			addAndRemove("326", layout_item326, tv_326, tv_item326);
			break;
		case R.id.layout_item327:
			addAndRemove("327", layout_item327, tv_327, tv_item327);
			break;
		case R.id.layout_item328:
			addAndRemove("328", layout_item328, tv_328, tv_item328);
			break;
		case R.id.layout_item329:
			addAndRemove("329", layout_item329, tv_329, tv_item329);
			break;
		case R.id.layout_item330:
			addAndRemove("330", layout_item330, tv_330, tv_item330);
			break;
		case R.id.layout_item331:
			addAndRemove("331", layout_item331, tv_331, tv_item331);
			break;

		// 总进球
		case R.id.layout_item201:
			addAndRemove("201", layout_item201, tv_201, tv_item201);
			break;
		case R.id.layout_item202:
			addAndRemove("202", layout_item202, tv_202, tv_item202);
			break;
		case R.id.layout_item203:
			addAndRemove("203", layout_item203, tv_203, tv_item203);
			break;
		case R.id.layout_item204:
			addAndRemove("204", layout_item204, tv_204, tv_item204);
			break;
		case R.id.layout_item205:
			addAndRemove("205", layout_item205, tv_205, tv_item205);
			break;
		case R.id.layout_item206:
			addAndRemove("206", layout_item206, tv_206, tv_item206);
			break;
		case R.id.layout_item207:
			addAndRemove("207", layout_item207, tv_207, tv_item207);
			break;
		case R.id.layout_item208:
			addAndRemove("208", layout_item208, tv_208, tv_item208);
			break;
		// 半全场
		case R.id.layout_item401:
			addAndRemove("401", layout_item401, tv_401, tv_item401);
			break;
		case R.id.layout_item402:
			addAndRemove("402", layout_item402, tv_402, tv_item402);
			break;
		case R.id.layout_item403:
			addAndRemove("403", layout_item403, tv_403, tv_item403);
			break;
		case R.id.layout_item404:
			addAndRemove("404", layout_item404, tv_404, tv_item404);
			break;
		case R.id.layout_item405:
			addAndRemove("405", layout_item405, tv_405, tv_item405);
			break;
		case R.id.layout_item406:
			addAndRemove("406", layout_item406, tv_406, tv_item406);
			break;
		case R.id.layout_item407:
			addAndRemove("407", layout_item407, tv_407, tv_item407);
			break;
		case R.id.layout_item408:
			addAndRemove("408", layout_item408, tv_408, tv_item408);
			break;
		case R.id.layout_item409:
			addAndRemove("409", layout_item409, tv_409, tv_item409);
			break;
		}
	}

	/**
	 * 添加或移除选中元素
	 * 
	 * @param content
	 *            内容
	 * @param layout
	 *            布局
	 * @param tv
	 *            标签
	 * @param tv_odds
	 *            赔率
	 */
	public void addAndRemove(String content, LinearLayout layout, TextView tv,
			TextView tv_odds) {
		if (selectResult.contains(content)) {// 包含则移除
			selectResult.remove(content);
			setBackGround(layout, R.drawable.select_jczq_tv_bolder_white, tv,
					R.color.black, tv_odds, R.color.gray);
		} else {// 不包含则添加
			selectResult.add(content);
			setBackGround(layout, R.drawable.select_jczq_tv_bolder_red, tv,
					Color.WHITE, tv_odds, Color.WHITE);
		}
	}

	public void setAllBackGround(String content, LinearLayout layout,
			TextView tv, TextView tv_odds) {
		if (selectResult.contains(content)) {// 包含
			setBackGround(layout, R.drawable.select_jczq_tv_bolder_red, tv,
					Color.WHITE, tv_odds, Color.WHITE);
		} else {// 不包含
			setBackGround(layout, R.drawable.select_jczq_tv_bolder_white, tv,
					R.color.black, tv_odds, R.color.gray);
		}
	}

	private DialogResultListener listener;

	public void setDialogResultListener(DialogResultListener listener) {
		this.listener = listener;
	}

	public interface DialogResultListener {
		/**
		 * * 获取结果的方法
		 * 
		 * @param resultCode
		 *            0.取消 1.确定
		 * @param selectResult
		 *            选择的结果集合
		 */

		void getResult(int resultCode, ArrayList<String> selectResult);
	}

	/**
	 * 设置布局和字体颜色
	 * 
	 * @param layout
	 *            布局
	 * @param layoutbg
	 *            布局背景
	 * @param tv
	 *            文本-标签
	 * @param tvcolore
	 *            字体颜色
	 * @param tv_odds
	 *            文本-赔率
	 * @param tv_odds_colore
	 *            字体颜色
	 */
	public void setBackGround(LinearLayout layout, int layoutbg, TextView tv,
			int tvcolore, TextView tv_odds, int tv_odds_colore) {
		layout.setBackgroundResource(layoutbg);
		tv.setTextColor(tvcolore);
		tv_odds.setTextColor(tv_odds_colore);
	}

}
