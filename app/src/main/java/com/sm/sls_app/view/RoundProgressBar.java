package com.sm.sls_app.view;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.utils.AppTools;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class RoundProgressBar extends View {
 

	   private Paint paint; //画笔对象的引用 
	   
	   private int  roundColor ,roundColor2 ;//圆环的颜色
	   
	   private int roundProgressColor;//圆环进度的颜色
	   
	   private int textColor;//中间进度百分比的字符串的颜色 

	   private float textSize;//中间进度百分比的字符串的字体

	   private float roundWidth;//圆环的宽度

	   private int max;//最大进度
	   
	   private int progress ; //当前进度
	   
	   private int progress2 ; //当前进度
	   
	   private boolean textIsDisplayable; //是否显示中间的进度
	   
	   private int style;//进度的风格，实心或者空心 
	   
	   public static final int STROKE = 0;
	   
	   public static final int FILL = 1; 
	   public static float scale;//屏幕比列
	   
//	   private final int radius = 29;
   
	   public void setjidu( int a,int b) {
		   
		   this.progress = a;
		   
		   this.progress2 = b ;
		   	   
	   }
	   public RoundProgressBar(Context context, AttributeSet attrs) {
		   this(context, attrs, 0);  
		   
	   }   
	      public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {  
		           super(context, attrs, defStyle);  
		           scale= AppTools.getDpi((Activity)context);
		             
		           paint = new Paint();  
		     
		             
		          TypedArray mTypedArray = context.obtainStyledAttributes(attrs,  
		                   R.styleable.RoundProgressBar);  
		             
		           //获取自定义属性和默认值  
//		           roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.WHITE); 
//		           roundColor2 = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.WHITE); 
//		           roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, R.color.follow_lv_item_progressbar); 
		           roundColor = 0xffdbdbdb; //灰色
		           roundColor2 = 0xffdbdbdb;  //灰色
		           roundProgressColor = 0xfffdae24;//黄色  
//		           roundProgressColor = 0x7f070020;  
		          
		           textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.BLACK); 
		           
		           textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 20*scale);  
		           roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 12*scale);  
		           max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);  
		           textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);  
		           style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);  
		             
		           mTypedArray.recycle();  
		       } 
	      @Override  
	          protected void onDraw(Canvas canvas) {  
	              super.onDraw(canvas);  
	              int px = getMeasuredWidth(); 
	              int px2=getWidth();
	              roundWidth = px / 7;
	              /** 
	               * 画最外层的大圆环 
	               */  
	              int centre = px/2; //获取圆心的x坐标  
	              int radius = (int) (centre - roundWidth/2); //圆环的半径  
//	              int radius = 20;
	              if(progress2==0)
	            	  paint.setColor(roundColor2); //设置圆环的颜色  
	              else
	                  paint.setColor(roundColor);
	              
	              paint.setStyle(Paint.Style.STROKE); //设置空心  
	              paint.setStrokeWidth(roundWidth); //设置圆环的宽度  
	              paint.setAntiAlias(true);  //消除锯齿              
	              canvas.drawCircle(centre, centre, radius, paint); //画出圆环  
                  /** 
	               * 画进度百分比 
	              */  
	              paint.setStrokeWidth(0);   
	              paint.setColor(textColor);  
	              paint.setTextSize(textSize);  
	              paint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体  
	              int percent = (int)(((float)progress / (float)max) * 100);  //中间的进度百分比，先转换成float在进行除法运算，不然都为0  
	              float textWidth = paint.measureText(percent + "%");   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间  
	                
	              if(textIsDisplayable && percent != 0 && style == STROKE ){  
	            	  if(progress2!=0){
	            		   paint.setTextSize(px2 / 6);
	            		   String content1=percent+"%";
	            		   canvas.drawText(content1, centre - getTextWidth(paint, content1) / 2, centre, paint);
	            		   paint.setColor(getResources().getColor(R.color.orange));
	            		   String content2="保"+progress2+ "%";
	            		   paint.setTextSize(px2 / 7);
	            		   canvas.drawText(content2, centre  - getTextWidth(paint, content2) / 2, centre + px2 / 7, paint); //画出进度百分比  
	            	  }
	            	  else{
	            		  paint.setTextSize(px2 / 5);
	            		  String content3=progress + "%";
	            		  canvas.drawText(content3, centre  - getTextWidth(paint, content3) / 2, centre + (px2 / 5)/2, paint); 
	            	  }
	              }  
	              
	                
	              /** 
	               * 画圆弧 ，画圆环的进度 
	               */  
	              //设置进度是实心还是空心  
	              paint.setStrokeWidth(roundWidth); //设置圆环的宽度  
	              paint.setColor(roundProgressColor);  //设置进度的颜色  
	              RectF oval = new RectF(centre - radius, centre - radius, centre  
	                      + radius, centre + radius);  //用于定义的圆弧的形状和大小的界限  
	                
	              switch (style) {  
	              case STROKE:{  
	                  paint.setStyle(Paint.Style.STROKE);  
	                  canvas.drawArc(oval, -90, 360 * progress / max, false, paint);  //根据进度画圆弧  
	                
//	                  paint.setColor(getResources().getColor(R.color.orange));  //设置进度的颜色    
//	                  if(progress +progress2>100){
//	                	  canvas.drawArc(oval, 360 * progress / max-90,360 *  (100 - progress) / max, false, paint);
//	                  }
//	                  else
//	                      canvas.drawArc(oval, 360 * progress / max-90,360 *  progress2 / max, false, paint);  //根据进度画圆弧  
	                  break;  
	             }  
	              case FILL:{  
	                  paint.setStyle(Paint.Style.FILL_AND_STROKE);  
	                  if(progress !=0)  
	                      canvas.drawArc(oval, 0, 360 * progress / max, true, paint);  //根据进度画圆弧  
	                  break;  
	              }  
	              }  
	                
	          }  
	          
	      
	      	  public float getTextWidth(Paint paint,String content){
	      		  return paint.measureText(content);
	      	  }
	            
	          public synchronized int getMax() {  
	              return max;  
	          }  
	       
	          /** 
	           * 设置进度的最大值 
	           * @param max 
	           */  
	          public synchronized void setMax(int max) {  
	              if(max < 0){  
	                  throw new IllegalArgumentException("max not less than 0");  
	             }  
	             this.max = max;  
	          }  
	        
	          /** 
	          * 获取进度.需要同步 
	           * @return 
	           */  
	          public synchronized int getProgress() {  
	              return progress;  
	          }  
	        
	          /** 
	           * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步 
	           * 刷新界面调用postInvalidate()能在非UI线程刷新 
	           * @param progress 
	           */  
	          public synchronized void setProgress(int progress) {  
	              if(progress < 0){  
	                  throw new IllegalArgumentException("progress not less than 0");  
	              }  
	              if(progress > max){  
	                  progress = max;  
	              }  
	              if(progress <= max){  
	                  this.progress = progress;  
	                  postInvalidate();  
	              }  
	                
	          }  
	            
	            
	          public int getCricleColor() {  
	              return roundColor;  
	          }  
	        
	         public void setCricleColor(int cricleColor) {  
	              this.roundColor = cricleColor;  
	          }  
	        
	          public int getCricleProgressColor() {  
	              return roundProgressColor;  
	         }  
	        
	          public void setCricleProgressColor(int cricleProgressColor) {  
	              this.roundProgressColor = cricleProgressColor;  
	          }  
	        
	          public int getTextColor() {  
	              return textColor;  
	          }  
	        
	          public void setTextColor(int textColor) {  
	              this.textColor = textColor;  
	          }  
	        
	          public float getTextSize() {  
	              return textSize;  
	          }  
	        
	          public void setTextSize(float textSize) {  
	              this.textSize = textSize;  
	          }  
	        
	          public float getRoundWidth() {  
	              return roundWidth;  
	          }  
	        
	          public void setRoundWidth(float roundWidth) {  
	              this.roundWidth = roundWidth;  
	          }  
	        
		   
	}
