package com.md.hjyg.utils;

import com.md.hjyg.activities.BaseActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

/**
 * ClassName: MyAnimation date: 2015-12-17 下午12:02:04 remark: 设置动画
 * 
 * @author pyc
 */
public class MyAnimation {
	/** 图片的摆动动画 */
	public static void startShakeAnimation(View v, Bitmap bitmap, float start,
			float end) {
		// int pivot = Animation.RELATIVE_TO_SELF;
		CycleInterpolator interpolator = new CycleInterpolator(3.0f);
		// RotateAnimation animation = new RotateAnimation(0, 15, pivot,
		// 0.5f,pivot, 0.5f);
		RotateAnimation animation = new RotateAnimation(start, end,
				bitmap.getWidth() / 2, bitmap.getHeight());
		animation.setStartOffset(0);
		animation.setDuration(2000);
		// 表示重复多次
		animation.setRepeatCount(Animation.INFINITE);
		animation.setInterpolator(interpolator);
		v.startAnimation(animation);
	}
	/**设置缩放动画*/
	public static void setScaleAnimation(View v ,AnimationListener listener){
		
		ScaleAnimation animation =new ScaleAnimation(1f, 0.5f, 1f, 0.5f, 
		Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f); 
		animation.setDuration(100);//设置动画持续时间 
		/** 常用方法 */ 
		animation.setRepeatCount(0);//设置重复次数 
		animation.setFillAfter(false);//动画执行完后是否停留在执行完的状态 
		animation.setStartOffset(0);//执行前的等待时间 
//		v.setAnimation(animation); 
        v.startAnimation(animation);
        if (listener != null) {
        	animation.setAnimationListener(listener);
		}

	}
	
	/**设置缩放动画*/
	public static void setScaleAnimation(View v ){
		
		ScaleAnimation animation =new ScaleAnimation(0f, 1f, 0f, 1f, 
		Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f); 
		animation.setDuration(1000);//设置动画持续时间 
		/** 常用方法 */ 
		animation.setRepeatCount(0);//设置重复次数 
		animation.setFillAfter(false);//动画执行完后是否停留在执行完的状态 
		animation.setStartOffset(0);//执行前的等待时间 
//		v.setAnimation(animation); 
        v.startAnimation(animation);

	}
	
	/**设置缩放动画,动画结束后跳转页面*/
	public static void setScaleAnimation(View v ,final Intent intent,final BaseActivity mActivity){
		if (intent == null || mActivity == null) {
			return;
		}
		ScaleAnimation animation =new ScaleAnimation(1f, 0.5f, 1f, 0.5f, 
		Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f); 
		animation.setDuration(100);//设置动画持续时间 
		/** 常用方法 */ 
		animation.setRepeatCount(0);//设置重复次数 
		animation.setFillAfter(false);//动画执行完后是否停留在执行完的状态 
		animation.setStartOffset(0);//执行前的等待时间 
//		v.setAnimation(animation); 
        v.startAnimation(animation);
        
        animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				mActivity.startActivity(intent);
				mActivity.overTransition(2);
			}
		});
		

	}

}
