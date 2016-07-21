package com.md.hjyg.layoutEntities;

import com.md.hjyg.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * ClassName: LotteryPopupWindow 
 * date: 2015-10-21 下午3:03:40 
 * remark:抽奖弹窗
 * 
 * @author pyc
 */
public class LotteryPopupWindow extends PopupWindow {

	TextView popu_text_btn ,award_text_btn1,award_text_btn2;
	private View mWindowView;
	Activity context;

	/**注册和充值弹窗*/
	public LotteryPopupWindow(Activity context, OnClickListener itemsOnClick,int Width,
			String title,String content1,String content2,String btnstr) {
		super(context);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mWindowView = inflater.inflate(R.layout.layout_lottery_popupwindow_reg, null); 
		popu_text_btn = (TextView) mWindowView.findViewById(R.id.popu_text_btn);
		popu_text_btn.setText(btnstr);
		TextView popu_text1 = (TextView) mWindowView.findViewById(R.id.popu_text1);
		TextView popu_text2 = (TextView) mWindowView.findViewById(R.id.popu_text2);
		TextView popu_text3 = (TextView) mWindowView.findViewById(R.id.popu_text3);
		
		if (title == null || title.length() == 0) {
			popu_text1.setVisibility(View.GONE);
		}else {
			popu_text1.setVisibility(View.VISIBLE);
			popu_text1.setText(title);
		}
		popu_text2.setText(content1);
		popu_text3.setText(content2);
		//设置按钮监听  
		popu_text_btn.setOnClickListener(itemsOnClick);
		//设置SelectPicPopupWindow的View  
        this.setContentView(mWindowView);  
        //设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(Width);  
        //设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(Width);  
        //设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        //设置SelectPicPopupWindow弹出窗体动画效果  
        this.setAnimationStyle(R.style.AnimBottom); 
        //设置背景图片及透明度
        Bitmap bmp=BitmapFactory.decodeResource(context.getResources(), R.drawable.lottery_step1_iv);
        bmp = setAlpha(bmp, 90);
        Drawable drawable =new BitmapDrawable(bmp);
        this.setBackgroundDrawable(drawable);  
		
	}
	/**中奖弹窗*/
	public LotteryPopupWindow(Activity context, OnClickListener itemsOnClick,int Width,Bitmap bitmap,String msg,String redAmount) {
		super(context);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mWindowView = inflater.inflate(R.layout.layout_lottery_popupwindow_win, null); 
		award_text_btn1 = (TextView) mWindowView.findViewById(R.id.award_text_btn1);
		award_text_btn2 = (TextView) mWindowView.findViewById(R.id.award_text_btn2);
		ImageView award_img = (ImageView) mWindowView.findViewById(R.id.award_img);
		award_img.setImageBitmap(bitmap);
		//抽中奖励文字提示信息
		TextView award_text = (TextView) mWindowView.findViewById(R.id.award_text);
		LinearLayout lin_award = (LinearLayout) mWindowView.findViewById(R.id.lin_award);
		award_text.setText(msg);
		TextView award_redtext = (TextView) mWindowView.findViewById(R.id.award_redtext);
		if (redAmount == null) {
			lin_award.setVisibility(View.GONE);
		}else {
			lin_award.setVisibility(View.VISIBLE);
			award_redtext.setText(redAmount);
		}
		//设置按钮监听  
		award_text_btn1.setOnClickListener(itemsOnClick);
		award_text_btn2.setOnClickListener(itemsOnClick);
		//设置SelectPicPopupWindow的View  
        this.setContentView(mWindowView);  
        //设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(Width);  
        //设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(Width);  
        //设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        //设置SelectPicPopupWindow弹出窗体动画效果  
        this.setAnimationStyle(R.style.AnimBottom); 
        //设置背景图片及透明度
        Bitmap bmp=BitmapFactory.decodeResource(context.getResources(), R.drawable.lottery_winaward_bg);
        bmp = setAlpha(bmp, 90);
        Drawable drawable =new BitmapDrawable(bmp);
        this.setBackgroundDrawable(drawable);  
		
	}
	
	/**年会中奖弹窗*/
//	public LotteryPopupWindow(Activity context, int Width,Bitmap bitmap,String msg,String redAmount) {
//		super(context);
//		this.context = context;
//		LayoutInflater inflater = (LayoutInflater) context
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		mWindowView = inflater.inflate(R.layout.layout_lotteryyear_popup_win, null); 
////		award_text_btn1 = (TextView) mWindowView.findViewById(R.id.award_text_btn1);
////		award_text_btn2 = (TextView) mWindowView.findViewById(R.id.award_text_btn2);
//		ImageView close = (ImageView) mWindowView.findViewById(R.id.close);
//		close.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				dismiss();
//			}
//		});
//		ImageView award_img = (ImageView) mWindowView.findViewById(R.id.award_img);
//		award_img.setImageBitmap(bitmap);
//		//抽中奖励文字提示信息
//		TextView award_text = (TextView) mWindowView.findViewById(R.id.award_text);
//		award_text.setText(msg);
//		TextView award_redtext = (TextView) mWindowView.findViewById(R.id.award_redtext);
//		if (redAmount == null) {
//			award_redtext.setVisibility(View.GONE);
//		}else {
//			award_redtext.setVisibility(View.VISIBLE);
//			award_redtext.setText(TextUtil.getRelativeSize(redAmount, redAmount.length()-1, redAmount.length(), 0.8f));
//		}
//		//设置SelectPicPopupWindow的View  
//        this.setContentView(mWindowView);  
//        //设置SelectPicPopupWindow弹出窗体的宽  
//        this.setWidth(Width);  
//        //设置SelectPicPopupWindow弹出窗体的高  
//        this.setHeight(Width);  
//        //设置SelectPicPopupWindow弹出窗体可点击  
//        this.setFocusable(true);  
//        //设置SelectPicPopupWindow弹出窗体动画效果  
//        this.setAnimationStyle(R.style.AnimBottom); 
//        //设置背景图片及透明度
//        Bitmap bmp=BitmapFactory.decodeResource(context.getResources(), R.drawable.lottery_year_win_bg);
//        bmp = setAlpha(bmp, 90);
//        Drawable drawable =new BitmapDrawable(bmp);
//        this.setBackgroundDrawable(drawable);  
//		
//	}
	/**投资引导弹窗*/
	public LotteryPopupWindow(Activity context, OnClickListener itemsOnClick,int Width) {
		super(context);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mWindowView = inflater.inflate(R.layout.layout_lottery_pop_inv, null); 
		TextView popu_text_btn3 = (TextView) mWindowView.findViewById(R.id.popu_text_btn3);
		//设置按钮监听  
		popu_text_btn3.setOnClickListener(itemsOnClick);
		//设置SelectPicPopupWindow的View  
        this.setContentView(mWindowView);  
        //设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(Width);  
        //设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(Width);  
        //设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        //设置SelectPicPopupWindow弹出窗体动画效果  
        this.setAnimationStyle(R.style.AnimBottom); 
        //设置背景图片及透明度
        Bitmap bmp=BitmapFactory.decodeResource(context.getResources(), R.drawable.lottery_inv_bg);
       // bmp = setAlpha(bmp, 90);
        Drawable drawable =new BitmapDrawable(bmp);
        this.setBackgroundDrawable(drawable);  
		
	}
	
	/**主页引导弹窗*/
	public LotteryPopupWindow(Activity context, OnClickListener itemsOnClick) {
		super(context);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mWindowView = inflater.inflate(R.layout.layout_lottery_home, null); 
		TextView lottery_home_btn = (TextView) mWindowView.findViewById(R.id.lottery_home_btn);
		//设置按钮监听  
		lottery_home_btn.setOnClickListener(itemsOnClick);
		ImageView lottery_home_s = (ImageView) mWindowView.findViewById(R.id.lottery_home_s);
		lottery_home_s.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		//设置SelectPicPopupWindow的View  
        this.setContentView(mWindowView);  
        //设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(LayoutParams.MATCH_PARENT);  
        //设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(LayoutParams.MATCH_PARENT);  
        //设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        //设置SelectPicPopupWindow弹出窗体动画效果  
        this.setAnimationStyle(R.style.AnimRghtTop); 
        //设置背景图片及透明度
        Bitmap bmp=BitmapFactory.decodeResource(context.getResources(), R.drawable.lottery_step1_iv);
        bmp = setAlpha(bmp, 70);
        Drawable drawable =new BitmapDrawable(bmp);
        this.setBackgroundDrawable(drawable);  
		
	}
	
	/**
	 * 设置图片的透明度
	 * @param sourceImg 资源图片
	 * @param number 0-100，完全透明—不透明
	 * @return Bitmap 一张设置好透明度的图片
	 */
	public Bitmap setAlpha(Bitmap sourceImg, int number) {

        int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];

        sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0, sourceImg

                        .getWidth(), sourceImg.getHeight());// 获得图片的ARGB值

        number = number * 255 / 100;

        for (int i = 0; i < argb.length; i++) {

                argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);

        }

        sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg

                        .getHeight(), Config.ARGB_8888);

        return sourceImg;

}

}
