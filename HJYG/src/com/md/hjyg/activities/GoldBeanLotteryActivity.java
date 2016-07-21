package com.md.hjyg.activities;

import java.util.Timer;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.GoldBeanRecordAdapter;
import com.md.hjyg.entity.GoldBeanGoodLuckModel;
import com.md.hjyg.entity.GoldBeanLotteryInfoModel;
import com.md.hjyg.layoutEntities.HeaderView;
import com.md.hjyg.layoutEntities.LotteryGoldbeanDiskView;
import com.md.hjyg.layoutEntities.RippleView;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utility.TimeTaskScroll;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.utils.ScreenUtils;
import com.md.hjyg.utils.TextUtil;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.Listener;

/**
 * ClassName: GoldBeanLotteryActivity date: 2016-5-26 下午2:06:39 remark: 金豆抽奖
 * 
 * @author pyc
 */
public class GoldBeanLotteryActivity extends BaseActivity implements
		OnClickListener {

	private LinearLayout line_lotterysum,lin_listView;
	private LotteryGoldbeanDiskView mDiskView;
	private ImageView img_btn, img_nodate, img_load;
	private RippleView mRippleView;
	private GoldBeanLotteryInfoModel model;
	private TextView tv_bgsum, tv_ConsumeGoldBean;
	private ListView mlistView;
	private AnimationDrawable frameAnim;
	private GoldBeanGoodLuckModel goodLuckModel;
	private DialogManager mDialog;
	private boolean isfrist = true;
	private HeaderView mheadView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goldbeanlottery_layout);

		findViewandInit();
		getLotteryInfo();
	}

	private void findViewandInit() {
		mheadView = (HeaderView) findViewById(R.id.mheadView);
		mheadView.setViewUI(this, "金豆抽奖", "");
		mheadView.setRightImg(R.drawable.gb_gift_rh);
		mDialog = new DialogManager(this);

		line_lotterysum = (LinearLayout) findViewById(R.id.line_lotterysum);
		ViewParamsSetUtil.setViewParams(line_lotterysum, 720, 793, true);

		img_btn = (ImageView) findViewById(R.id.img_btn);
		img_btn.setOnClickListener(this);
		ViewParamsSetUtil.setViewParams(img_btn, 220, 274, false);

		img_nodate = (ImageView) findViewById(R.id.img_nodate);
		ViewParamsSetUtil.setViewParams(img_nodate, 243, 172, true);

		mDiskView = (LotteryGoldbeanDiskView) findViewById(R.id.mDiskView);
		mRippleView = (RippleView) findViewById(R.id.mRippleView);
		mRippleView.setOnClickListener(this);
		int mw[] = Save.getScaleBitmapWangH(98*2/3, 98*2/3); 
		mRippleView.setWandH(mw[0]);

		tv_bgsum = (TextView) findViewById(R.id.tv_bgsum);
		tv_ConsumeGoldBean = (TextView) findViewById(R.id.tv_ConsumeGoldBean);
		mlistView = (ListView) findViewById(R.id.mlistView);
		frameAnim = (AnimationDrawable) getResources().getDrawable(
				R.drawable.meeting_load);
		img_load = (ImageView) findViewById(R.id.img_load);
		lin_listView = (LinearLayout) findViewById(R.id.lin_listView);
	}

	/**
	 * 获取界面信息
	 */
	private void getLotteryInfo() {
		dft.getNetInfoById(Constants.GoldBeanLotteryInfo_URL,
				Request.Method.GET, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject josnbject) {
						model = (GoldBeanLotteryInfoModel) dft
								.GetResponseObject(josnbject,
										GoldBeanLotteryInfoModel.class);
						setDate();
					}
				});
	}

	private void setDate() {
		if (model != null) {
			String sum = "我的金豆：" + model.LeaveGoldBean + "个";
			tv_bgsum.setText(TextUtil.getRedString(sum, 5, 5 + model.LeaveGoldBean.length()));
			if (model.goldBeanLottery != null) {
				setMemLotLogList();
				if (!isfrist) {
					isfrist = false;
					return;
				}
				tv_ConsumeGoldBean.setText("抽奖一次将消耗"
						+ (int) model.goldBeanLottery.ConsumeGoldBean + "金豆");
				mDiskView.setLottryInfo(model.goldBeanLottery.Prize, mHandler);

				if (!DateUtil.isStart(model.goldBeanLottery.StartTime)) {// 即将开始
					img_btn.setImageResource(R.drawable.gb_lottery_btn_off);
					img_btn.setEnabled(false);
					mRippleView.setVisibility(View.GONE);
				} else if (DateUtil.isStart(model.goldBeanLottery.EndTime)) {// 已经结束
					img_btn.setImageResource(R.drawable.gb_lottery_btn_end);
					mRippleView.setVisibility(View.GONE);
					img_btn.setEnabled(false);
				} else {
					img_btn.setImageResource(R.drawable.gb_lottery_btn);
					mRippleView.setVisibility(View.VISIBLE);
					img_btn.setEnabled(true);
				}
			}
		}

	}

	/**
	 * 中奖名单信息
	 */
	private void setMemLotLogList() {
		if (model.MemberLotteryLogList == null
				|| model.MemberLotteryLogList.size() == 0) {
			img_nodate.setVisibility(View.VISIBLE);
			mlistView.setVisibility(View.GONE);
		} else {
			if (model.MemberLotteryLogList.size() > 7) {
				ViewParamsSetUtil.setViewHandW_lin(lin_listView, ScreenUtils.dip2px(this, 180), 0);
				new Timer().schedule(new TimeTaskScroll(
						GoldBeanLotteryActivity.this, mlistView,
						model.MemberLotteryLogList, ""), 50, 50);
			} else {
				mlistView.setAdapter(new GoldBeanRecordAdapter(
						model.MemberLotteryLogList, this));
				Constants.setListViewHeightBasedOnChildren(mlistView);
			}

			mlistView.setVisibility(View.VISIBLE);
			img_nodate.setVisibility(View.GONE);
		}
	}

	/**
	 * 获取界面信息
	 */
	private void GoldBeanGoodLuck() {
		dft.getNetInfoById(Constants.GoldBeanGoodLuck_URL, Request.Method.GET,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject josnbject) {
						goodLuckModel = (GoldBeanGoodLuckModel) dft
								.GetResponseObject(josnbject,
										GoldBeanGoodLuckModel.class);
						if (goodLuckModel.notification.ProcessResult == 1) {
							mDiskView.StartRotate();
							tv_bgsum.setText("我的金豆："
									+ goodLuckModel.LeaveGoldBean + "个");
							AppController.AccountInfIsChange = true;
							Constants.LeaveGoldBean = Constants
									.StringToCurrency(
											goodLuckModel.LeaveGoldBean + "")
									.replace(".00", "");
							for (int i = 0; i < model.goldBeanLottery.Prize
									.size(); i++) {
								if (goodLuckModel.GoldBeanLotteryPrizeId == model.goldBeanLottery.Prize
										.get(i).Id) {
									mDiskView.luckyStart_2(i);
								}
							}
						} else {
							mDialog.initOneBtnDialog(false,
									goodLuckModel.notification.ProcessMessage,
									null);
							frameAnim.stop();
							img_load.setVisibility(View.GONE);
							mRippleView.setVisibility(View.VISIBLE);
							img_btn.setEnabled(true);
							img_btn.setImageResource(R.drawable.gb_lottery_btn);
						}

					}
				});
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 6:
				getLotteryInfo();
				mDialog.initTwoBtnDialog(true, "确定", "查看奖品", "提示", "恭喜，抽到"
						+ goodLuckModel.notification.ProcessMessage,
						GoldBeanLotteryActivity.this);
				frameAnim.stop();
				img_load.setVisibility(View.GONE);
				mRippleView.setVisibility(View.VISIBLE);
				img_btn.setEnabled(true);
				img_btn.setImageResource(R.drawable.gb_lottery_btn);
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderView.left_img_ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.mRippleView:
		case R.id.img_btn:
			if (model == null) {
				return;
			}
			img_btn.setImageResource(R.drawable.gb_lottery_loadbg);
			mRippleView.setVisibility(View.GONE);
			img_btn.setEnabled(false);
			img_load.setImageDrawable(frameAnim);
			img_load.setVisibility(View.VISIBLE);
			frameAnim.start();
			GoldBeanGoodLuck();
			break;
		case DialogManager.CANCEL_BTN:
			mDialog.dismiss();
			break;
		case DialogManager.CONFIRM_BTN:
		case HeaderView.rightimg_ID:
			mDialog.dismiss();
			startActivity(new Intent(this, LotteryObtainedActivity.class));
			overTransition(2);
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		this.finish();
		overTransition(1);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mRippleView.stop();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		mRippleView.start();
	}
	@Override
	protected void onDestroy() {
		mRippleView.stop();
		super.onDestroy();
	}

}
