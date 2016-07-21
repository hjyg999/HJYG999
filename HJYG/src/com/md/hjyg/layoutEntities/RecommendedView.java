package com.md.hjyg.layoutEntities;

import com.md.hjyg.R;
import com.md.hjyg.utils.ScreenUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/** 
 * ClassName: RecommendedView 
 * date: 2015-12-5 上午11:22:06 
 * remark:
 * @author pyc
 */   
public class RecommendedView  extends View{
	
	/** 
     * 文本 
     */  
    private String time = "2015/10";  
    private String total = "66.29";  
    private String one_acount = "98.04";  
    private String two_acount = "0.00";  
    private String total_hit = "待收奖励(元)";  
    private String one_acount_hit = "一级好友待收(元)";  
    private String two_acount_hit = "二级好友待收(元)";  
    /** 
     * 文本的颜色 
     */  
    private int mTitleTextColor = 0; 
    /**线框的颜色*/
    private int mStrokColor = 0;  
    /**圆点的颜色*/
    private int mCircleColor = 0;  
    /** 
     * 文本的大小 
     */  
    private int mTitleTextSize = 20;  
    private int totalTextSize = 30;  
    private int TextSize = 10;  
  
    /** 
     * 绘制时控制文本绘制的范围 
     */  
    private Rect mBound;  
    private Paint mPaint;  
    private int pad = 40;  
    private Context context;

	
	public RecommendedView(Context context) {
		this(context,null);
	}
	
	public RecommendedView(Context context, AttributeSet attrs)  
    {  
        this(context, attrs, 0);  
    }  
	
	public RecommendedView(Context context, AttributeSet attrs, int defStyle)  
    { 
		super(context,attrs,defStyle);
		this.context = context;
		 mPaint = new Paint();  
		 mPaint.setStrokeWidth(2);
		 mPaint.setAntiAlias(true);
	     mPaint.setTextSize(mTitleTextSize);
	     mBound = new Rect();  
	     mPaint.getTextBounds(time, 0, time.length(), mBound); 
    }
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		 mPaint.setColor(Color.GRAY);  
//	     canvas.drawText(mTitleText, getWidth() / 2 - mBound.width() / 2, (float) (getHeight()*0.08), mPaint); 
	     drawText(canvas, mTitleTextSize, time, R.color.gray, (float) (getHeight()*0.08));
	     int Cr = ScreenUtils.dip2px(context, 5);
	     canvas.drawLine(0, (float) (getHeight()*0.1) + 15, getWidth()/2 -Cr-Cr/2, (float) (getHeight()*0.1) + 15, mPaint);
	     canvas.drawLine(getWidth()/2 +Cr+Cr/2, (float) (getHeight()*0.1) + 15, getWidth(), (float) (getHeight()*0.1) + 15, mPaint);
	     
	     if (mCircleColor == 0) {
	    	 mPaint.setColor(context.getResources().getColor(R.color.red));  
		}else {
			mPaint.setColor(mCircleColor);  
		}
	     canvas.drawCircle(getWidth() / 2, (float) (getHeight()*0.1) + 15, Cr, mPaint);
	     
//	     RectF r= new RectF(left, top, right, bottom);;
//		canvas.drawRect(r, mPaint);
//	     mPaint.setColor(context.getResources().getColor(mStrokColor));
	     if (mStrokColor == 0) {
	    	 mPaint.setColor(context.getResources().getColor(R.color.red));
		}else {
			mPaint.setColor(mStrokColor);
		}
	     float a = getWidth()*0.2f ;
	     float b = (float) (getHeight()*0.2);
	     float r = 20;
	     float dot = (float) (getHeight()*0.17);
	     float d = getHeight()*0.03f;
	     
	     Paint paint = new Paint();
	     paint.setStrokeWidth(2.5f);
	     paint.setStyle(Paint.Style.STROKE);
	     if (mStrokColor == 0) {
	    	 paint.setColor(context.getResources().getColor(R.color.red));
		}else {
			paint.setColor(mStrokColor);
		}
	     paint.setAntiAlias(true);
	     canvas.drawLine(getWidth()/2 , dot, getWidth()/2 -d, b, mPaint);
	     canvas.drawLine(getWidth()/2 -d , b, a,b, mPaint);
	     canvas.drawArc(new RectF(a-r, b, a+r, b+2*r), 180, 90, false, paint);
	     float bot = getHeight()*0.6f;
	     canvas.drawLine(a-r , b+r, a-r, bot, mPaint);
	     canvas.drawArc(new RectF(a-r, bot-r, a+r, bot+r), 90, 90, false, paint);
	     float ri = getWidth()*0.8f;
	     canvas.drawLine(a , bot+r, ri, bot+r, mPaint);
	     canvas.drawArc(new RectF(ri-r, bot-r, ri+r, bot+r), 0, 90, false, paint);
	     canvas.drawLine(ri+r, bot, ri+r, b+r, mPaint);
	     canvas.drawArc(new RectF(ri-r, b, ri+r,  b+2*r), 270, 90, false, paint);
	     canvas.drawLine(getWidth()/2 +d , b, ri,b, mPaint);
	     canvas.drawLine(getWidth()/2 , dot, getWidth()/2 +d , b, mPaint);
	     
//	     Paint mPaint= new Paint(); 
//	     mPaint.setTextSize(totalTextSize);
//	     mPaint.setAntiAlias(true);
//	     Rect Bound = new Rect();  
//	     mPaint.getTextBounds(total, 0, total.length(), Bound);
//	     canvas.drawText(total, getWidth() / 2 - Bound.width() / 2, (float) (getHeight()*0.1) + getHeight()*0.1f+60, mPaint);
	     drawText(canvas, totalTextSize, total, R.color.red, (float) (getHeight()*0.1) + getHeight()*0.2f);
	     drawText(canvas, TextSize, total_hit, R.color.gray, (float) (getHeight()*0.1) + getHeight()*0.25f);
	     drawText(canvas, mTitleTextSize, one_acount, R.color.gray, (float) (getHeight()*0.1) + getHeight()*0.32f);
	     drawText(canvas, TextSize, one_acount_hit, R.color.gray, (float) (getHeight()*0.1) + getHeight()*0.35f);
	     drawText(canvas, mTitleTextSize, two_acount, R.color.gray, (float) (getHeight()*0.1) + getHeight()*0.42f);
	     drawText(canvas, TextSize, two_acount_hit, R.color.gray, (float) (getHeight()*0.1) + getHeight()*0.45f);
	}
	
	
	/**写字*/
	private void drawText(Canvas canvas,int textSize, String text,int colorID,float y) {
		
		 Paint mPaint= new Paint(); 
//	     mPaint.setTextSize(ScreenUtils.px2sp(context, textSize));
	     mPaint.setTextSize(ScreenUtils.sp2px(context, textSize));
	     mPaint.setAntiAlias(true);
	     mPaint.setColor(context.getResources().getColor(colorID));
	     Rect Bound = new Rect();  
	     mPaint.getTextBounds(text, 0, text.length(), Bound);
	     canvas.drawText(text, getWidth() / 2 - Bound.width() / 2, y, mPaint);
		
	}
	/**刷新界面*/
	public void commit(){
		invalidate();
	}
	
	public void SetAcount(String time,String total,String one_acount,String two_acount) {
		this.time = time;
		this.total = total;
		this.one_acount = one_acount;
		this.two_acount = two_acount;
	}
	public void SetHitText(String total_hit,String one_acount_hit,String two_acount_hit) {
		this.total_hit = total_hit;
		this.one_acount_hit = one_acount_hit;
		this.two_acount_hit = two_acount_hit;
	}
	
	public void setColor(int mStrokColor,int mCircleColor){
		this.mStrokColor = mStrokColor;
		this.mCircleColor = mCircleColor;
	}

}
