package com.md.hjyg.activities;

import java.util.Timer;

import com.md.hjyg.R;
import com.md.hjyg.adapter.MeetingListViewAdapter;
import com.md.hjyg.entity.ExhibitionSalelotteryModel;
import com.md.hjyg.entity.MeetingGoodluckModel;
import com.md.hjyg.layoutEntities.LotteryCountDownView;
import com.md.hjyg.layoutEntities.LotteryMeetDiskView;
import com.md.hjyg.layoutEntities.RefreshableScrollView;
import com.md.hjyg.layoutEntities.RefreshableScrollView.RefreshListener;
import com.md.hjyg.utility.MeetingWebServiceManager;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utility.TimeTaskScroll;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.utils.ScreenUtils;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * ClassName: LotteryMeetingActivity date: 2016-2-23 下午1:38:01 remark: 会销-活动抽奖
 * 
 * @author pyc
 */
@SuppressLint("HandlerLeak")
public class LotteryMeetingActivity extends BaseActivity implements
		OnClickListener {
	private ImageView lay_back_investment;
	private TextView myawards;
	private Intent intent;
	private ImageView img_top;
	private ImageView img_btn, img_pointer;
	private int btnBitmaps[];
	private Context context;
	/**
	 * 原图，缩放模板
	 */
	private Bitmap topimg;
	//img_top_r,
//	private Bitmap  img_cer_r;
	private RelativeLayout rel_lottery;
	private FrameLayout frameLayout;
	// private LinearLayout lin_lottery;
	// private LotteryDiskView mView;
	private LotteryMeetDiskView mView;
	private ImageView img_bot_cr;
	private ImageView img_namelist, img_namebg, img_line, img_namerule,
			img_namerulebg;
	private int bots[];
	private int title_imgs[];
	private boolean isRunning = true;
	private int n;

	private ImageView img_meeting_viewbg;
	//mRotateBgBitmap,
	private int   bitmap_pops[];

	private RelativeLayout rel_zj_pop;
	private ImageView img_zj, img_clos;
	private LinearLayout lin_jjks_pop;
	private ImageView img_jjks;
	private TextView tv_zjmsg, tv_btn_zj;

	private Bitmap time_bg_Bitmap;
	private TextView tv_starttime;
	private int ActivityId = -1;
	private int HxCodeId = -1;
	private ExhibitionSalelotteryModel model;
	private MeetingWebServiceManager manager;
	private boolean imgLoadover;
	private LinearLayout lin_nodate, lin_listView, lin_listbg;
	private ListView mlistView;
	private MeetingListViewAdapter adapter;
	private RelativeLayout re_mDownView;
	private LotteryCountDownView mDownView;
	private ImageView down_nubimg;
//	private Bitmap bitmaps[];
	private int bitmapIDs[];

	private ImageView img_load;
	private AnimationDrawable frameAnim, frameAnim2;
	private MeetingGoodluckModel goodluckModel;

	private LinearLayout rel_havezj;
	private ImageView img_havezj;
	private TextView tv_havezjmsg;
	// private LinearLayout lin_allview;
	private ScrollView mScrollView;
	private LinearLayout lin_loading;
	private ImageView img_loading_m;

	/** 下拉刷新控件 */
	private RefreshableScrollView refreshableScrollView;
	/** 是否在刷新 */
	boolean isrefreshing = false;
	
	private TextView tv_rule;
	/** 回到顶部按钮 */
	private ImageButton to_top;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_lotterymeeting_layout);
		findViewAndInit();
		setImg();
		manager = new MeetingWebServiceManager(context, dft, mHandler);
		manager.ExhibitionSalelottery(ActivityId, HxCodeId);

		// 下拉刷新监听
		refreshableScrollView.setRefreshListener(new RefreshListener() {

			@Override
			public void onRefresh(RefreshableScrollView view) {
				if (!isrefreshing) {
					isrefreshing = true;
					manager.ExhibitionSalelottery(ActivityId, HxCodeId);
				}

				// mHandler.sendEmptyMessage(0);
			}
		}, 205);
	}

	private void findViewAndInit() {
		context = getBaseContext();
		intent = getIntent();
		ActivityId = intent.getIntExtra("ActivityId", -1);
		HxCodeId = intent.getIntExtra("HxCodeId", -1);

		mScrollView = (ScrollView) findViewById(R.id.mScrollView);
		mScrollView.setVisibility(View.INVISIBLE);
		// 进入页面时的加载框
		lin_loading = (LinearLayout) findViewById(R.id.lin_loading);
		img_loading_m = (ImageView) findViewById(R.id.img_loading_m);
		frameAnim2 = (AnimationDrawable) getResources().getDrawable(
				R.drawable.meeting_loading_m);
		img_loading_m.setImageDrawable(frameAnim2);
		lin_loading.setVisibility(View.VISIBLE);
		frameAnim2.start();
		// lin_allview = (LinearLayout) findViewById(R.id.lin_allview);
		// lin_allview.setVisibility(View.GONE);
		lay_back_investment = (ImageView) findViewById(R.id.lay_back_investment);
		lay_back_investment.setOnClickListener(this);

		img_btn = (ImageView) findViewById(R.id.img_btn);
		img_btn.setOnClickListener(this);
		img_top = (ImageView) findViewById(R.id.img_top);
		img_pointer = (ImageView) findViewById(R.id.img_pointer);
		img_bot_cr = (ImageView) findViewById(R.id.img_bot_cr);
		img_namelist = (ImageView) findViewById(R.id.img_namelist);
		img_namebg = (ImageView) findViewById(R.id.img_namebg);
		img_line = (ImageView) findViewById(R.id.img_line);
		img_namerule = (ImageView) findViewById(R.id.img_namerule);
		img_namerulebg = (ImageView) findViewById(R.id.img_namerulebg);
		img_meeting_viewbg = (ImageView) findViewById(R.id.img_meeting_viewbg);

		frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
		rel_lottery = (RelativeLayout) findViewById(R.id.rel_lottery);

		rel_zj_pop = (RelativeLayout) findViewById(R.id.rel_zj_pop);
		img_zj = (ImageView) findViewById(R.id.img_zj);
		img_clos = (ImageView) findViewById(R.id.img_clos);
		img_clos.setOnClickListener(this);
		tv_zjmsg = (TextView) findViewById(R.id.tv_zjmsg);
		lin_jjks_pop = (LinearLayout) findViewById(R.id.lin_jjks_pop);
		img_jjks = (ImageView) findViewById(R.id.img_jjks);
		tv_btn_zj = (TextView) findViewById(R.id.tv_btn_zj);
		tv_btn_zj.setOnClickListener(this);

		tv_starttime = (TextView) findViewById(R.id.tv_starttime);

		myawards = (TextView) findViewById(R.id.myawards);
		myawards.setOnClickListener(this);
		lin_nodate = (LinearLayout) findViewById(R.id.lin_nodate);
		lin_listView = (LinearLayout) findViewById(R.id.lin_listView);
		lin_listbg = (LinearLayout) findViewById(R.id.lin_listbg);
		mlistView = (ListView) findViewById(R.id.mlistView);

		// 倒计时
		re_mDownView = (RelativeLayout) findViewById(R.id.re_mDownView);
		mDownView = (LotteryCountDownView) findViewById(R.id.mDownView);
		down_nubimg = (ImageView) findViewById(R.id.down_nubimg);
		ViewParamsSetUtil.setViewParams(down_nubimg, 240, 240, false);
		// 抽奖中
		img_load = (ImageView) findViewById(R.id.img_load);

		// 已抽中产品显示
		rel_havezj = (LinearLayout) findViewById(R.id.rel_havezj);
		img_havezj = (ImageView) findViewById(R.id.img_havezj);
		tv_havezjmsg = (TextView) findViewById(R.id.tv_havezjmsg);

		// 下拉刷新控件
		refreshableScrollView = (RefreshableScrollView) findViewById(R.id.refreshableScrollView);
		tv_rule = (TextView) findViewById(R.id.tv_rule);
		to_top = (ImageButton) findViewById(R.id.to_top);
		to_top.setOnClickListener(this);
	}

	/**
	 * 缩放图片并加载图片
	 */
	private void setImg() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				imgLoadover = false;
				Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
				topimg = BitmapFactory.decodeResource(getResources(),
						R.drawable.meeting_topimg);
				int w = ScreenUtils.getScreenWidth(context);
				int width2 = topimg.getWidth();
//				img_top_r = Save.ScaleBitmap(topimg, context);
//				img_cer_r = Save.ScaleBitmap(BitmapFactory.decodeResource(
//						getResources(), R.drawable.meeting_lotteryimg), w,
//						width2);
				btnBitmaps = new int[3];
				btnBitmaps[0] = R.drawable.meeting_btn_yes;
				btnBitmaps[1] = R.drawable.meeting_btn_load;
				btnBitmaps[2] = R.drawable.meeting_btn_no;
//				btnBitmaps[3] = R.drawable.meeting_pointer;
//				btnBitmaps[0] = Save.ScaleBitmap(BitmapFactory.decodeResource(
//						getResources(), R.drawable.meeting_btn_yes), w, width2);
//				btnBitmaps[1] = Save.ScaleBitmap(BitmapFactory.decodeResource(
//						getResources(), R.drawable.meeting_btn_load), w, width2);
//				btnBitmaps[2] = Save.ScaleBitmap(BitmapFactory.decodeResource(
//						getResources(), R.drawable.meeting_btn_no), w, width2);
//				btnBitmaps[3] = Save.ScaleBitmap(BitmapFactory.decodeResource(
//						getResources(), R.drawable.meeting_pointer), w, width2);
				bots = new int[2];
				bots[0] = R.drawable.meeting_cr1;
				bots[1] = R.drawable.meeting_cr2;
//				bots[0] = Save.ScaleBitmap(BitmapFactory.decodeResource(
//						getResources(), R.drawable.meeting_cr1), w, width2);
//				bots[1] = Save.ScaleBitmap(BitmapFactory.decodeResource(
//						getResources(), R.drawable.meeting_cr2), w, width2);
				title_imgs = new int[5];
				title_imgs[0] = R.drawable.meeting_namelist;
				title_imgs[1] = R.drawable.meeting_rule;
				title_imgs[2] = R.drawable.meeting_rbg;
				title_imgs[3] = R.drawable.meeting_line;
				title_imgs[4] = R.drawable.meeting_mbg_c;
//				title_imgs[0] = Save.ScaleBitmap(BitmapFactory.decodeResource(
//						getResources(), R.drawable.meeting_namelist), w, width2);
//				title_imgs[1] = Save.ScaleBitmap(BitmapFactory.decodeResource(
//						getResources(), R.drawable.meeting_rule), w, width2);
//				title_imgs[2] = Save.ScaleBitmap(BitmapFactory.decodeResource(
//						getResources(), R.drawable.meeting_rbg), w, width2);
//				title_imgs[3] = Save.ScaleBitmap(BitmapFactory.decodeResource(
//						getResources(), R.drawable.meeting_line), w, width2);
//				title_imgs[4] = Save.ScaleBitmap(BitmapFactory.decodeResource(
//						getResources(), R.drawable.meeting_mbg_c), w, width2);

//				meeting_viewbg = Save.ScaleBitmap(BitmapFactory.decodeResource(
//						getResources(), R.drawable.meeting_viewbg), w, width2);
//				mRotateBgBitmap = Save.ScaleBitmap(BitmapFactory
//						.decodeResource(getResources(),
//								R.drawable.meeting_rotate_bg), w, width2);

				bitmap_pops = new int[5];
				bitmap_pops[0] = R.drawable.meeting_zj_bg;
				bitmap_pops[1] = R.drawable.meeting_bb_zj;
				bitmap_pops[2] = R.drawable.meeting_jjks_bg;
				bitmap_pops[3] = R.drawable.meeting_bb_jjks;
				bitmap_pops[4] = R.drawable.meeting_zj_rbg;
//				bitmap_pops[0] = Save.ScaleBitmap(BitmapFactory.decodeResource(
//						getResources(), R.drawable.meeting_zj_bg), w, width2);
//				bitmap_pops[1] = Save.ScaleBitmap(BitmapFactory.decodeResource(
//						getResources(), R.drawable.meeting_bb_zj), w, width2);
//				bitmap_pops[2] = Save.ScaleBitmap(BitmapFactory.decodeResource(
//						getResources(), R.drawable.meeting_jjks_bg), w, width2);
//				bitmap_pops[3] = Save.ScaleBitmap(BitmapFactory.decodeResource(
//						getResources(), R.drawable.meeting_bb_jjks), w, width2);
//				bitmap_pops[4] = Save.ScaleBitmap(BitmapFactory.decodeResource(
//						getResources(), R.drawable.meeting_zj_rbg), w, width2);

				time_bg_Bitmap = Save.ScaleBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.meeting_time_bg), w, width2);

				initBitmips();
				imgLoadover = true;

				mHandler.sendEmptyMessage(3);

			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_back_investment:// 返回
			finish();
			overTransition(1);
			break;
		case R.id.myawards:// 我的奖品
		case R.id.tv_btn_zj:// 我的奖品
			intent = new Intent(this, LotteryObtainedActivity.class);
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.img_btn:// 抽奖
			img_btn.setEnabled(false);
			img_btn.setImageResource(btnBitmaps[1]);
			img_load.setVisibility(View.VISIBLE);
			frameAnim = (AnimationDrawable) getResources().getDrawable(
					R.drawable.meeting_load);
			img_load.setImageDrawable(frameAnim);
			frameAnim.start();
			mView.StartRotate();
			manager.ExhibitionSalelotteryGoodLuck(ActivityId, HxCodeId);
			break;
		case R.id.img_clos:// 关闭中间按钮
			rel_zj_pop.setVisibility(View.GONE);
			break;
		case R.id.to_top:// 回到顶部按钮
			mScrollView.scrollTo(0, 0);
			break;

		default:
			break;
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 0:// 园点闪烁
				setHotUI();
				break;
			case 1:

				if (n == 0) {
					n = 1;
				} else {
					n = 0;
				}
				img_bot_cr.setImageResource(bots[n]);
				break;
			case 3:
				setDateUI();
				break;
			case 4:
				int i = (Integer) msg.obj;
				if (bitmapIDs != null && bitmapIDs[i] != 0) {
//					down_nubimg.setImageBitmap(bitmaps[i]);
					down_nubimg.setImageResource(bitmapIDs[i]);
				}
				break;
			case 5:
				re_mDownView.setVisibility(View.GONE);
				img_btn.setImageResource(btnBitmaps[0]);
				img_btn.setEnabled(true);
				break;
			case 6:// 抽奖结束
				if (goodluckModel.notification.ProcessResult == 1) {

					tv_zjmsg.setText("恭喜您！"
							+ goodluckModel.notification.ProcessMessage);
					rel_zj_pop.setVisibility(View.VISIBLE);
					// img_btn.setEnabled(false);
					// img_btn.setImageBitmap(btnBitmaps[1]);
					img_load.setVisibility(View.GONE);
					frameAnim.stop();
					img_btn.setEnabled(true);
					img_btn.setImageResource(btnBitmaps[0]);

					tv_havezjmsg.setText("恭喜您！\n已"
							+ goodluckModel.notification.ProcessMessage);
					rel_havezj.setVisibility(View.VISIBLE);
				}
				break;
			case 207:// 获取页面信息
				model = (ExhibitionSalelotteryModel) msg.obj;
				setDateUI();
				break;
			case 208: // 抽奖返回信息
				goodluckModel = (MeetingGoodluckModel) msg.obj;
				if (goodluckModel.notification.ProcessResult == 1) {// 成功
					for (int j = 0; j < model.lotPrizeList.size(); j++) {
						if (model.lotPrizeList.get(j).Id == goodluckModel.LotteryActivityPrizeId) {
							mView.luckyStart_2(j);
							return;
						}
					}

				} else {
					mView.errorEnd();
					Constants.showOkPopup(LotteryMeetingActivity.this,
							goodluckModel.notification.ProcessMessage);
					img_load.setVisibility(View.GONE);
					frameAnim.stop();
				}
				break;

			default:
				break;
			}
		};
	};

	private void setDateUI() {
		img_load.setVisibility(View.GONE);

		if (!imgLoadover || model == null) {
			return;
		}
		
		if ( !isrefreshing) {
		ViewParamsSetUtil.setViewParams(img_top, 720, 240, false);
			
//		img_top.setImageBitmap(img_top_r);
		ViewParamsSetUtil.setViewParams(img_btn, 146, 146, false);
		img_btn.setImageResource(btnBitmaps[0]);
		ViewParamsSetUtil.setViewParams(img_pointer, 340, 340, false);
//		img_pointer.setImageBitmap(btnBitmaps[3]);
		
		ViewParamsSetUtil.setViewParams(img_namelist, 660, 70, true);
//		img_namelist.setImageBitmap(title_imgs[0]);
		ViewParamsSetUtil.setViewParams(img_namebg, 660, 480, false);
//		img_namebg.setImageResource(title_imgs[2]);
		ViewParamsSetUtil.setViewParams(img_line, 720, 110, true);
//		img_line.setImageBitmap(title_imgs[3]);
		ViewParamsSetUtil.setViewParams(img_namerulebg, 660, 480, false);
//		img_namerulebg.setImageResource(title_imgs[2]);
		ViewParamsSetUtil.setViewParams(img_namerule, 660, 70, true);
//		img_namerule.setImageResource(title_imgs[1]);
//		img_meeting_viewbg.setImageBitmap(meeting_viewbg);
		ViewParamsSetUtil.setViewParams(img_meeting_viewbg, 680, 680, false);

//		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rel_lottery
//				.getLayoutParams();
//		params.height = img_cer_r.getHeight();
//		rel_lottery.setLayoutParams(params);
		ViewParamsSetUtil.setViewParams(rel_lottery, 720, 720, true);
		
		ViewParamsSetUtil.setViewParams(img_bot_cr, 560, 560, false);
		img_bot_cr.setImageResource(bots[0]);
		mHandler.sendEmptyMessage(0);

		ViewParamsSetUtil.setViewParams(lin_listbg, 720, 620, true);
//		ViewParamsSetUtil.setViewHandW_lin(lin_listbg, title_imgs[4]);
		// 设置已中奖窗口的宽高
//		ViewParamsSetUtil.setViewHandW_rel(rel_havezj, bitmap_pops[4]);
		ViewParamsSetUtil.setViewParams(rel_havezj, 510, 510, false);
		ViewParamsSetUtil.setViewParams(img_havezj, 142, 180, true);
		img_havezj.setImageResource(bitmap_pops[1]);

		// 设置中奖弹窗的宽高
		ViewParamsSetUtil.setViewParams(rel_zj_pop, 660, 520, false);
//		ViewParamsSetUtil.setViewHandW_rel(rel_zj_pop, bitmap_pops[0]);

		ViewParamsSetUtil.setViewParams(img_zj, 142, 180, true);
		img_zj.setImageResource(bitmap_pops[1]);
		// 设置即将开始弹窗的宽高

		ViewParamsSetUtil.setViewParams(lin_jjks_pop, 660, 380, false);
//		ViewParamsSetUtil.setViewHandW_rel(lin_jjks_pop, bitmap_pops[2]);
		ViewParamsSetUtil.setViewParams(img_jjks, 114, 144, true);
//		img_jjks.setImageBitmap(bitmap_pops[3]);

		// 设置活动开始时间
		ViewParamsSetUtil.setViewHandW_rel(tv_starttime, time_bg_Bitmap);

		// lin_allview.setVisibility(View.VISIBLE);
		lin_loading.setVisibility(View.GONE);
		frameAnim2.stop();
		mScrollView.setVisibility(View.VISIBLE);
		}
		final float fontScale = getResources().getDisplayMetrics().scaledDensity;
		String s_time = "活动开始时间："
				+ DateUtil.changto(model.lotteryActivity.startTime);
		tv_starttime.setText(s_time);
		tv_starttime.setTextSize(TypedValue.COMPLEX_UNIT_SP,
				(time_bg_Bitmap.getWidth() - 30) / (s_time.length()) * 1.5f
				/ fontScale);
		setDiskView();
		setMemLotLogList();
		setStartState();
		
		tv_rule.setText(model.exhSaleRule);

		
		if (isrefreshing) {
			// 刷新完成
			refreshableScrollView.finishRefresh();
			isrefreshing = false;
		}
	}

	/**
	 * 设置开始状态
	 */
	private void setStartState() {
		if (model.oldPrizeInfo != null && model.oldPrizeInfo.length() > 0) {// 已抽奖品
			tv_havezjmsg.setText("恭喜您！\n已" + model.oldPrizeInfo);
			rel_havezj.setVisibility(View.VISIBLE);
			return;
		}
		// ----------------未抽奖品-----------------------
		rel_havezj.setVisibility(View.GONE);
		
		// 结束抽奖时间
		long endTime = DateUtil.getLongTime(model.lotteryActivity.endTime);
		// 开始活动时间
		long startTime = DateUtil.getLongTime(model.lotteryActivity.startTime);
		// 服务器时间
		long nowTime = DateUtil.getLongTime(model.nowTime);
		// 手机时间
		// long myTime = System.currentTimeMillis();
		lin_jjks_pop.setVisibility(View.GONE);
		rel_zj_pop.setVisibility(View.GONE);
		if (nowTime < startTime) {//即将开始
			lin_jjks_pop.setVisibility(View.VISIBLE);
		}else if (nowTime >=endTime) {// 活动已经结束
			
			img_btn.setImageResource(btnBitmaps[2]);
			img_btn.setEnabled(false);
		}else {
			if (model.lotteryActivity.Start) {//主持人点击了开始按钮
				// 开始抽奖时间
				long UstartTime = DateUtil
						.getLongTime(model.lotteryActivity.UstartTime);
				int distanceTime = (int) ((UstartTime - nowTime) / 1000);
				if (distanceTime <= 10 && distanceTime > 0) {// 显示倒计时
					lin_jjks_pop.setVisibility(View.GONE);
					rel_zj_pop.setVisibility(View.GONE);
					re_mDownView.setVisibility(View.VISIBLE);
					mDownView.timeStart(distanceTime,true);
					startDownTime(distanceTime);
				}else if(distanceTime <= 0){
					img_btn.setImageResource(btnBitmaps[0]);
					img_btn.setEnabled(true);
				}else  if(distanceTime > 10){
					img_btn.setImageResource(btnBitmaps[2]);
					img_btn.setEnabled(false);
				}
				
			}else {//主持人未点击开始按钮
				lin_jjks_pop.setVisibility(View.GONE);
				rel_zj_pop.setVisibility(View.GONE);
				img_btn.setImageResource(btnBitmaps[2]);
				img_btn.setEnabled(false);
			}
		}
//		if (nowTime > endTime) {// 活动已经结束
//			img_btn.setImageBitmap(btnBitmaps[2]);
//			img_btn.setEnabled(false);
//		} else {
//			img_btn.setImageBitmap(btnBitmaps[0]);
//			img_btn.setEnabled(true);
//			if (model.lotteryActivity.Start) {// 活动已经开始
//				lin_jjks_pop.setVisibility(View.GONE);
//				rel_zj_pop.setVisibility(View.GONE);
//			} else {// 活动即将开始
//
//				// 手机时间和服务器时间的误差值
//				// int chaTime = (int) (myTime - nowTime);
//				// 距离抽奖开始时间
//				int distanceTime = (int) ((UstartTime - nowTime) / 1000);
//				lin_jjks_pop.setVisibility(View.VISIBLE);
//				rel_zj_pop.setVisibility(View.GONE);
//
//				// if (distanceTime > 10) {// 开始时间大于10秒
//				// lin_jjks_pop.setVisibility(View.VISIBLE);
//				// rel_zj_pop.setVisibility(View.GONE);
//				// } else
//				if (distanceTime <= 10 && distanceTime > 0) {// 显示倒计时
//					lin_jjks_pop.setVisibility(View.GONE);
//					rel_zj_pop.setVisibility(View.GONE);
//					re_mDownView.setVisibility(View.VISIBLE);
//					mDownView.timeStart(distanceTime);
//					startDownTime(distanceTime);
//				}
//
//			}

//		}

	}

	/**
	 * 10秒倒计时
	 */
	private void startDownTime(final int downTime) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				int i = downTime;
				Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
				while (i >= 0) {
					Message msg = mHandler.obtainMessage(4);
					msg.obj = i;
					mHandler.sendMessage(msg);
					try {
						Thread.sleep(1000);
						i--;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				mHandler.sendEmptyMessage(5);
			}
		}).start();
	}

	/**
	 * 设置转盘信息
	 */
	private void setDiskView() {
		frameLayout.removeAllViews();
//		ViewParamsSetUtil.setViewHandW_rel(frameLayout, mRotateBgBitmap);
		ViewParamsSetUtil.setViewParams(frameLayout, 510, 510,false);

		mView = new LotteryMeetDiskView(LotteryMeetingActivity.this);

		FrameLayout.LayoutParams params_v = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER);
		mView.setLayoutParams(params_v);
		mView.setData(model.lotPrizeList, mHandler);
		frameLayout.addView(mView);
	}

	/**
	 * 中奖名单信息
	 */
	private void setMemLotLogList() {
		if (model.memLotLogList == null || model.memLotLogList.size() == 0) {
			lin_nodate.setVisibility(View.VISIBLE);
			lin_listView.setVisibility(View.GONE);
		} else {
			int[] img_Wh = Save.getScaleBitmapWangH(660, 480);
			ViewParamsSetUtil.setViewHandW_rel(lin_listView, img_Wh[1], img_Wh[0]);
//			ViewParamsSetUtil.setViewHandW_rel(lin_listView, title_imgs[2]);
			adapter = new MeetingListViewAdapter(this, model.memLotLogList,
					false);
			mlistView.setAdapter(adapter);
			int h = Constants.getListViewHeight(mlistView);
			if (h > img_Wh[1]) {
				adapter = null;
				new Timer().schedule(new TimeTaskScroll(
						LotteryMeetingActivity.this, mlistView,
						model.memLotLogList), 20, 20);
			}

			lin_listView.setVisibility(View.VISIBLE);
			lin_nodate.setVisibility(View.GONE);
		}
	}

	/**
	 * 园点闪烁
	 */
	private void setHotUI() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
				while (isRunning) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					mHandler.sendEmptyMessage(1);
				}
			}
		}).start();
	}

	@Override
	public void onBackPressed() {
		finish();
		overTransition(1);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isRunning = false;
	}

	/**
	 * 倒计时数字图片
	 */
	private void initBitmips() {
		bitmapIDs = new int[11];
		bitmapIDs[0] = R.drawable.meeting_sz0;
		bitmapIDs[1] = R.drawable.meeting_sz1;
		bitmapIDs[2] = R.drawable.meeting_sz2;
		bitmapIDs[3] = R.drawable.meeting_sz3;
		bitmapIDs[4] = R.drawable.meeting_sz4;
		bitmapIDs[5] = R.drawable.meeting_sz5;
		bitmapIDs[6] = R.drawable.meeting_sz6;
		bitmapIDs[7] = R.drawable.meeting_sz7;
		bitmapIDs[8] = R.drawable.meeting_sz8;
		bitmapIDs[9] = R.drawable.meeting_sz9;
		bitmapIDs[10] = R.drawable.meeting_sz10;
//		bitmaps = new Bitmap[11];
//		bitmaps[0] = BitmapFactory.decodeResource(getResources(),
//				R.drawable.meeting_sz0);
//		bitmaps[1] = BitmapFactory.decodeResource(getResources(),
//				R.drawable.meeting_sz1);
//		bitmaps[2] = BitmapFactory.decodeResource(getResources(),
//				R.drawable.meeting_sz2);
//		bitmaps[3] = BitmapFactory.decodeResource(getResources(),
//				R.drawable.meeting_sz3);
//		bitmaps[4] = BitmapFactory.decodeResource(getResources(),
//				R.drawable.meeting_sz4);
//		bitmaps[5] = BitmapFactory.decodeResource(getResources(),
//				R.drawable.meeting_sz5);
//		bitmaps[6] = BitmapFactory.decodeResource(getResources(),
//				R.drawable.meeting_sz6);
//		bitmaps[7] = BitmapFactory.decodeResource(getResources(),
//				R.drawable.meeting_sz7);
//		bitmaps[8] = BitmapFactory.decodeResource(getResources(),
//				R.drawable.meeting_sz8);
//		bitmaps[9] = BitmapFactory.decodeResource(getResources(),
//				R.drawable.meeting_sz9);
//		bitmaps[10] = BitmapFactory.decodeResource(getResources(),
//				R.drawable.meeting_sz10);
	}

}
