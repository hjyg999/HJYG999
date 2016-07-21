package com.md.hjyg.layoutEntities;

import com.md.hjyg.R;
import com.md.hjyg.utility.Save;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ClassName: ProjectDetailsView date: 2016-2-19 上午8:41:37 remark: 项目详情头部公共布局
 * 
 * @author pyc
 */
@SuppressLint("HandlerLeak")
public class ProjectDetailsView extends LinearLayout {
	private Context context;
	private RelativeLayout mrel;
	private ImageView img_question;
	private TextView tv_rate, tv_rateadd, tv_ratehit, tv_left, tv_lefthit,
			tv_centre, tv_centrehit, tv_right, tv_righthit, tv_surplus,
			tv_Progress,tv_right_jx;
	private ProgressBar mProgressBar;
	private double per;

	public ProjectDetailsView(Context context) {
		this(context, null);
	}

	public ProjectDetailsView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ProjectDetailsView(Context context, AttributeSet attrs, int paramInt) {
		super(context, attrs, paramInt);
		inflate(context, R.layout.projectdeatlls_top_layout, this);
		this.context = context;
		// // 如果是编辑模式，则跳过
		// if (isInEditMode()) {
		// return;
		//
		// }

		FindView();

		// 设置控件的高度
		setViewHeight();
	}

	private void FindView() {
		mrel = (RelativeLayout) findViewById(R.id.mrel);
		img_question = (ImageView) findViewById(R.id.img_question);
//		tv_rateadd, tv_ratehit, tv_left, tv_lefthit,
//		tv_centre, tv_centrehit, tv_right, tv_righthit, tv_surplus,
//		tv_Progress;
		tv_rate = (TextView) findViewById(R.id.tv_rate);
		tv_rateadd = (TextView) findViewById(R.id.tv_rateadd);
		tv_ratehit = (TextView) findViewById(R.id.tv_ratehit);
		tv_left = (TextView) findViewById(R.id.tv_left);
		tv_lefthit = (TextView) findViewById(R.id.tv_lefthit);
		tv_centre = (TextView) findViewById(R.id.tv_centre);
		tv_centrehit = (TextView) findViewById(R.id.tv_centrehit);
		tv_right = (TextView) findViewById(R.id.tv_right);
		tv_righthit = (TextView) findViewById(R.id.tv_righthit);
		tv_surplus = (TextView) findViewById(R.id.tv_surplus);
		tv_Progress = (TextView) findViewById(R.id.tv_Progress);
		tv_right_jx = (TextView) findViewById(R.id.tv_right_jx);
		mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
		
	}

	/***
	 * 设置控件的高度
	 */
	private void setViewHeight() {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mrel
				.getLayoutParams();
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.detail_top_bg);
		bitmap = Save.ScaleBitmap(bitmap, context);
		params.height = bitmap.getHeight();
		mrel.setLayoutParams(params);
	}
	
	/**
	 * 设置点击监听
	 * @param l
	 */
	public void setOnMClickListener(OnClickListener l){
		img_question.setOnClickListener(l);
	}
	
	/**
	 * 是否显示常见问题图标
	 * @param isHave true为显示
	 */
	public void setHaveQuestion(boolean isHave){
		if (isHave) {
			img_question.setVisibility(View.VISIBLE);
		}else {
			img_question.setVisibility(View.GONE);
		}
	}
	
	/***
	 * 设置年化利率及奖励利率
	 * @param rate 年化利率
	 * @param rateadd 奖励利率(以及%符号+号等)
	 * @param ratehit 提示信息
	 */
	public void setRate(CharSequence rate,CharSequence rateadd,CharSequence ratehit){
		tv_rate.setText(rate);
		tv_rateadd.setText(rateadd);
		if (rateadd !=null && rateadd.length() > 0) {
			tv_right_jx.setVisibility(View.VISIBLE);
		}else {
			tv_right_jx.setVisibility(View.GONE);
		}
		tv_ratehit.setText(ratehit);
	}
	
	/**
	 * 设置左边信息--每万元收益(活期宝)，期限(项目)等
	 * @param left 每万元收益 或 期限值
	 * @param lefthit 显示每万元收益 或 期限
	 */
	public void setLeftInfo(CharSequence left,CharSequence lefthit){
		tv_left.setText(left);
		tv_lefthit.setText(lefthit);
	}
	
	/**
	 * 设置中间信息--收益起始日(活期宝)，起投金额(项目)等
	 * @param centre
	 * @param centrehit
	 */
	public void setCentreInfo(CharSequence centre,CharSequence centrehit){
		tv_centre.setText(centre);
		tv_centrehit.setText(centrehit);
	}
	
	/**
	 * 设置右边信息--起投金额(活期宝)，项目总额(项目)等
	 * @param right
	 * @param righthit
	 */
	public void setRightInfo(CharSequence right,CharSequence righthit){
		tv_right.setText(right);
		tv_righthit.setText(righthit);
	}
	
	/**
	 * 设置剩余金额和进度
	 * @param surplus 剩余金额
	 * @param per 进度百分比
	 * @param isHouQibao 是否为活期宝，true 则隐藏进度条
	 */
	public void setProgressInfo(CharSequence surplus,double per,boolean isHouQibao){
		tv_surplus.setText(surplus);
		if (isHouQibao) {
			tv_Progress.setVisibility(View.GONE);
			mProgressBar.setProgress(0);
		}else {
			this.per = per;
			tv_Progress.setVisibility(View.VISIBLE);
			setProgressAnim();
		}
	}
	
	/**
	 * 设置进度条动画
	 */
	private void setProgressAnim(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
				int i = 0;
				while (i <= per) {
					Message msg = mHandler.obtainMessage(0);
					msg.obj = i;
					mHandler.sendMessage(msg );
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					i =i + 5;
				}
				
				Message msg = mHandler.obtainMessage(0);
				msg.obj = i;
				mHandler.sendMessage(msg );
			}
		}).start();
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				int i =(Integer) msg.obj;
				if (i<per) {
					mProgressBar.setProgress(i);
					tv_Progress.setText(i + "%");
				}else if (i >= per && per == 100) {
					mProgressBar.setProgress((int)per);
					tv_Progress.setText(100 + "%");
				}else {
					mProgressBar.setProgress((int)per);
					tv_Progress.setText(Constants.doubleTrans(per) + "%");
				}
			}
		};
	};
	
}
