package com.md.hjyg.layoutEntities;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.utils.ScreenUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * ClassName: FormView date: 2016-3-22 上午11:11:33 remark:标格
 * 
 * @author pyc
 */
public class FormView extends View {
	private Context context;
	private int width, height;
	private int pad = 30;

	/**
	 * 绘制线条的画笔
	 */
	private Paint mlinePaint;
	/**
	 * 绘制线条的画笔
	 */
	// private Paint mTextPaint;
	private String text[][] = { { "", "活期", "1个月", "3个月", "6个月", "12个月" },
			{ "XX牛", "6.5%", "12%", "8.5%", "9.6%", "11%" },
			{ "信投宝", "6.0%", "10%", "12%", "13%", "14%" },
			{ "银行", "0.30%", "-", "1.35%", "1.55%", "1.75%" } };
	
	public void setData(List<String> interestList){
		for (int i = 0; i < interestList.size(); i++) {
			text[i] = interestList.get(i).split(",");
		}
		invalidate();
	}

	public FormView(Context context) {
		this(context, null);
	}

	public FormView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FormView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;

		mlinePaint = new Paint();
		// 设置画笔宽度
		mlinePaint.setStrokeWidth(1);
		// 消除锯齿
		mlinePaint.setAntiAlias(true);

	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		int r = 6;
		float dh = (float) (height - 1) / (float) (r);
		int s = 4;
		float dw = (float) (width - 2 * pad) / (float) s;
		mlinePaint.setColor(getResources().getColor(R.color.yellow_FFF5EB));
		canvas.drawRect(new Rect((int) (pad + dw * 2), 0, (int) (pad + dw * 3),
				height - 1), mlinePaint);
		mlinePaint.setColor(getResources().getColor(R.color.gray_q));
		// 画横线
		for (int i = 0; i <= r; i++) {
			canvas.drawLine(pad, dh * i, width - pad, dh * i, mlinePaint);
		}
		// 画竖线
		for (int i = 0; i <= s; i++) {
			canvas.drawLine(pad + dw * i, 0, pad + dw * i, height - 1,
					mlinePaint);
		}
		canvas.drawLine(pad, 0, pad + dw, dh, mlinePaint);

		Path path = new Path();
		for (int i = 0; i < text.length; i++) {// 列
			if (i == 2) {
				mlinePaint.setColor(getResources().getColor(R.color.yellow_FF9934));
			}else {
				mlinePaint.setColor(getResources().getColor(R.color.gray));
			}
			for (int j = 0; j < text[i].length; j++) {//行
				if (text[i][j].length() > 0) {
					path.reset();
					path.moveTo(pad + i*dw, dh / 2 + dh*j);
					path.lineTo(pad + dw + + i*dw, dh / 2 + dh*j);
					mlinePaint.setTextSize(dw / 6);
					float textWidth = mlinePaint.measureText(text[i][j]);
					float hOffset = (float) (dw - textWidth) / 2;
					canvas.drawTextOnPath(text[i][j], path, hOffset, dw / 12,
							mlinePaint);
				}
			}
		}
		
		mlinePaint.setColor(getResources().getColor(R.color.gray));
		path.reset();
		path.moveTo(pad , dh / 4 );
		path.lineTo(pad + dw , dh / 4 );
		mlinePaint.setTextSize(dw / 6 *0.75f);
		float textWidth = mlinePaint.measureText("预期年化");
		float hOffset = (float) (dw - textWidth -4);
		canvas.drawTextOnPath("预期年化", path, hOffset, dw / 12 *0.75f,
				mlinePaint);
		path.reset();
		path.moveTo(pad , dh / 4 *3);
		path.lineTo(pad + dw , dh / 4 *3);
		mlinePaint.setTextSize(dw / 6 *0.75f);
		textWidth = mlinePaint.measureText("期限");
		hOffset = (float) (8);
		canvas.drawTextOnPath("期限", path, hOffset, dw / 12 *0.75f-4,
				mlinePaint);

	}

	/**
	 * 设置控件为正方形
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = ScreenUtils.getScreenWidth(context);
		height = (int) (width * 0.65);
		setMeasuredDimension(width, height);
	}

}
