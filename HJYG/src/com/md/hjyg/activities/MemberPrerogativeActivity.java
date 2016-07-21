package com.md.hjyg.activities;

import java.util.ArrayList;
import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.ExhibitionSalelotteryListModel;
import com.md.hjyg.entity.LotteryMeetingActionModel;
import com.md.hjyg.entity.MeetListDialogModel;
import com.md.hjyg.entity.MyRewardinfoModel;
import com.md.hjyg.entity.ShareRedPackageInfoModel;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.ListDialogManager;
import com.md.hjyg.utility.LotteryWebServiceManager;
import com.md.hjyg.utility.MeetingWebServiceManager;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utility.WeixinRedPacketManager;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.utils.MyAnimation;
import com.md.hjyg.utils.ScreenUtils;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ClassName: MemberPrerogativeActivity date: 2015-12-16 下午3:28:45
 * remark:我的账户--会员专享
 * 
 * @author pyc
 */
public class MemberPrerogativeActivity extends BaseActivity implements
		OnClickListener {

	private Intent intent;
	private Activity mActivity;
	private ImageView my_redpacket, share_redpacket, Lottery, assarting;
	private TextView my_redpacket_vale, share_redpacket_vale, Lottery_vale,
			assarting_vale;
	private Bitmap bitmapbg, bitmap1, bitmap2, bitmap3, bitmap4;
	private int textSize;
	private int redpacket_acount;
	private String MemberId;
	private WeixinRedPacketManager manager;
	private LotteryWebServiceManager lotteryManager;
	private MeetingWebServiceManager meetingManager;
	private ShareRedPackageInfoModel shareRedPackageInfoModel;
	private MyRewardinfoModel myRewardinfoModel;

	private Dialog mDialog;
	private TextView tv_dialog_cancel, tv_dialog_tolottrey, tv_dialog_hit;
	private EditText ed_dialog;
	private ListDialogManager mListDialog;
	/**
	 * 账户冻结弹框
	 */
	private DialogManager whitleDialog;
	/**
	 * 是否是主持人
	 */
	private boolean isCompere;
	private boolean isCompereloadOver;
	private ExhibitionSalelotteryListModel model;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_member_prerogative_activity);
		context = getBaseContext();
		findViewAndInit();
		manager = new WeixinRedPacketManager(mActivity, handler, dft);
		manager.getShareExpInfo();
		if (Constants.lotteryIsOpen) {
			lotteryManager = new LotteryWebServiceManager(this, handler, dft);
			lotteryManager.GetNetInfo(1);
		}

		meetingManager = new MeetingWebServiceManager(mActivity, dft, handler);
		meetingManager.getIsCompere();
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				shareRedPackageInfoModel = manager
						.getShareRedPackageInfoModel();
				setAnimation();
				break;
			case 101:
				myRewardinfoModel = (MyRewardinfoModel) msg.obj;
				setLotteryAnimation();
				break;
			case 201://是否是主持人
				isCompereloadOver = true;
				isCompere = (Boolean) msg.obj;
				break;
			case 202://验证兑换码，获取活动信息
				tv_dialog_tolottrey.setEnabled(true);
				tv_dialog_tolottrey.setTextColor(getResources().getColor(R.color.red));
				tv_dialog_tolottrey.setText("去抽奖");
				
				model = (ExhibitionSalelotteryListModel) msg.obj;
				if (model.notification.ProcessResult == 0) {//兑换码错误
					if (model.lotActResultModel.HaveNumber >0) {
						tv_dialog_hit.setText(model.notification.ProcessMessage);
						tv_dialog_hit.setVisibility(View.VISIBLE);
					}else {//已经输错6次
						if (mDialog != null && mDialog.isShowing()) {
							mDialog.dismiss();
						}
						showWhitleDialog(model.notification.ProcessMessage);
					}
				}else {//兑换码正确
					if (mDialog != null && mDialog.isShowing()) {
						mDialog.dismiss();
					}
					showChooseDialog(model.list);
				}
				break;

			default:
				break;
			}
		};
	};

	private void findViewAndInit() {
		mActivity = this;
		HeaderViewControler.setHeaderView(mActivity, "会员专享", this);
		initBitmap();
		int size = bitmap2.getWidth() / 4 - 8;
		textSize = ScreenUtils.px2sp(mActivity, size);
		// textSize = size;
		intent = getIntent();
		redpacket_acount = intent.getIntExtra("redpacket_acount", 0);
		MemberId = intent.getStringExtra("MemberId");
		// 我的红包
		my_redpacket = (ImageView) findViewById(R.id.my_redpacket);
		my_redpacket_vale = (TextView) findViewById(R.id.my_redpacket_vale);
		my_redpacket_vale.setTextSize(textSize);
		my_redpacket_vale.setText("红包加息券\n" + redpacket_acount + "个");
		my_redpacket.setImageBitmap(bitmap1);
		my_redpacket.setOnClickListener(this);
		// 分享红包
		share_redpacket = (ImageView) findViewById(R.id.share_redpacket);
		share_redpacket_vale = (TextView) findViewById(R.id.share_redpacket_vale);
		share_redpacket_vale.setTextSize(textSize);
		share_redpacket.setImageBitmap(bitmap2);
		share_redpacket.setOnClickListener(this);
		// 开垦中
		assarting = (ImageView) findViewById(R.id.assarting);
		assarting_vale = (TextView) findViewById(R.id.assarting_vale);
		assarting_vale.setTextSize(textSize);
		assarting.setImageBitmap(bitmap3);
		assarting.setOnClickListener(this);
		// 抽奖活动
		Lottery = (ImageView) findViewById(R.id.Lottery);
		Lottery_vale = (TextView) findViewById(R.id.Lottery_vale);
		Lottery_vale.setTextSize(textSize);
		Lottery_vale.setText("抽奖机会\n0次");
		Lottery.setImageBitmap(bitmap4);
		Lottery.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:// 返回
			mActivity.finish();
			overTransition(1);
			break;
		case R.id.my_redpacket:// 我的红包
//			intent = new Intent(mActivity, RedPacketActivity.class);
			intent = new Intent(mActivity, MyRedPacketFragmentActivity.class);
			MyAnimation.setScaleAnimation(my_redpacket_vale, null);
			MyAnimation.setScaleAnimation(my_redpacket, intent, this);
			break;
		case R.id.share_redpacket:// 分享红包
			// intent = new Intent(mActivity,
			// ShareWeixinRedPacketActivity.class);
			intent = new Intent(mActivity,
					HuoQiExperiencegoldShareActivity.class);
			MyAnimation.setScaleAnimation(share_redpacket_vale, null);
			MyAnimation.setScaleAnimation(share_redpacket, intent, this);
			break;
		case R.id.Lottery:// 抽奖活动

			if (Constants.lotteryIsOpen) {
				intent = new Intent(mActivity, LotteryMainActivity.class);
			} else {

				intent = new Intent(mActivity, LotteryObtainedActivity.class);
				intent.putExtra("MemberId", MemberId);
			}
			MyAnimation.setScaleAnimation(Lottery_vale, null);
			MyAnimation.setScaleAnimation(Lottery, intent, this);
			break;
		case R.id.assarting:// 会销抽奖
//			intent = new Intent(mActivity, LotteryMeetingActivity.class);
//			MyAnimation.setScaleAnimation(assarting_vale, null);
//			MyAnimation.setScaleAnimation(assarting, intent, this);
			// ShowDialog();
			// intent = new Intent(mActivity, LotteryMeetingHostActivity.class);
			// startActivity(intent);
			// overTransition(2);
			if (isCompereloadOver) {
				if (isCompere) {
					showHotDialog();
				}else {
					ShowDialog();
				}
			}else {
				Toast.makeText(mActivity, getString(R.string.data_loading), Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.tv_dialog_cancel:// 取消dialog
			if (mDialog != null && mDialog.isShowing()) {
				mDialog.dismiss();
			}
			break;
		case R.id.tv_dialog_tolottrey:// dialog去抽奖按钮
			if (mDialog != null && mDialog.isShowing()) {
				String hxcode = ed_dialog.getText().toString().trim();
//				String hxcode = "73882e9776";
				if (hxcode.length() == 0) {
					tv_dialog_hit.setText("请输入兑换码！");
					tv_dialog_hit.setVisibility(View.VISIBLE);
				}else {
					tv_dialog_tolottrey.setEnabled(false);
					tv_dialog_tolottrey.setTextColor(getResources().getColor(R.color.gray));
					tv_dialog_tolottrey.setText("处理中");
					//验证兑换码，获取活动信息
					meetingManager.getExhibitionSalelotteryList(hxcode);
				}
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		mActivity.finish();
		overTransition(1);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		setAnimation();
		setLotteryAnimation();
	}

	/** 根据屏幕缩放图片 */
	private void initBitmap() {
		bitmapbg = BitmapFactory.decodeResource(getResources(),
				R.drawable.member_bg);
		bitmap1 = BitmapFactory.decodeResource(getResources(),
				R.drawable.member1);
		bitmap2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.member2);
		bitmap3 = BitmapFactory.decodeResource(getResources(),
				R.drawable.member3);
		bitmap4 = BitmapFactory.decodeResource(getResources(),
				R.drawable.member4);
		bitmap1 = Save.ScaleBitmap(bitmap1, mActivity, bitmapbg, false);
		bitmap2 = Save.ScaleBitmap(bitmap2, mActivity, bitmapbg, false);
		bitmap3 = Save.ScaleBitmap(bitmap3, mActivity, bitmapbg, false);
		bitmap4 = Save.ScaleBitmap(bitmap4, mActivity, bitmapbg, false);
	}

	/** 设置抽奖动画 */
	private void setLotteryAnimation() {
		if (myRewardinfoModel != null) {
			// int haveNumber = myRewardinfoModel.HaveNumber;
			int HavetotalNumber = myRewardinfoModel.HavetotalNumber;
			int HaveGet = myRewardinfoModel.HaveGet;
			if (myRewardinfoModel.EndTime == null
					|| myRewardinfoModel.NowTime == null) {
				return;
			}
			long EndTime = DateUtil.StrToLong(myRewardinfoModel.EndTime
					.replace("00:00:00", "23:59:59"));
			long NowTime = DateUtil.StrToLong(myRewardinfoModel.NowTime);
			if (EndTime < NowTime || HaveGet == 2) {
				Lottery_vale.setText("抽奖机会\n0次");
			} else if (HavetotalNumber - HaveGet > 0) {
				Lottery_vale.setText("抽奖机会\n已有" + (HavetotalNumber - HaveGet)
						+ "次");
				MyAnimation.startShakeAnimation(Lottery_vale, bitmap4, 0, 4);
				MyAnimation.startShakeAnimation(Lottery, bitmap4, 0, 4);
				// remaind_times_tv.setText(HavetotalNumber - HaveGet +"");
			} else {
				// tv_hit.setText("剩余");
				// remaind_times_tv.setText(2 - HaveGet +"");
				Lottery_vale.setText("抽奖机会\n剩余" + (2 - HaveGet) + "次");
				if (2 - HaveGet > 0) {
					MyAnimation
							.startShakeAnimation(Lottery_vale, bitmap4, 0, 4);
					MyAnimation.startShakeAnimation(Lottery, bitmap4, 0, 4);
				}

			}
		}

	}

	/** 设置动画 */
	private void setAnimation() {
		if (redpacket_acount > 0) {
			MyAnimation.startShakeAnimation(my_redpacket, bitmap1, 0, 4);
			MyAnimation.startShakeAnimation(my_redpacket_vale, bitmap1, 0, 4);
		}

		if (shareRedPackageInfoModel.IsShare) {
			// 头部文字
			String[] strs = shareRedPackageInfoModel.ShareDes.split("-");
			String str = strs[0].replace(",", "") + strs[1];
			share_redpacket_vale.setText("分享体验金\n" + str);
			MyAnimation.startShakeAnimation(share_redpacket, bitmap2, 0, 4);
			MyAnimation
					.startShakeAnimation(share_redpacket_vale, bitmap2, 0, 4);
		} else {
			share_redpacket_vale.setText("分享体验金\n已过期");
		}

	}

	/**
	 * 输入兑换码
	 */
	@SuppressLint("InflateParams")
	private void ShowDialog() {
		mDialog = new Dialog(mActivity, R.style.m_dialogstyle);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				mDialog = null;
			}
		});

		View view = LayoutInflater.from(mActivity).inflate(
				R.layout.dialog_menbermeetinglottrey_layout, null);
		// 取消按钮
		tv_dialog_cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
		tv_dialog_cancel.setOnClickListener(this);
		// 去抽奖按钮
		tv_dialog_tolottrey = (TextView) view
				.findViewById(R.id.tv_dialog_tolottrey);
		tv_dialog_tolottrey.setOnClickListener(this);
		// 兑换码编辑框
		ed_dialog = (EditText) view.findViewById(R.id.ed_dialog);
		ed_dialog.setOnClickListener(this);
		// 编辑框提示信息
		tv_dialog_hit = (TextView) view.findViewById(R.id.tv_dialog_hit);
		tv_dialog_hit.setOnClickListener(this);

		mDialog.setContentView(view);

		Window dialogWindow = mDialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = (int) (ScreenUtils.getScreenWidth(mActivity) * 0.8);
		dialogWindow.setAttributes(lp);

		if (!mActivity.isFinishing()) {
			try {// 抓捕异常，防止程序崩溃
				mDialog.show();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 角色选择Dialog
	 */
	private void showHotDialog() {
		final List<MeetListDialogModel> lists = new ArrayList<MeetListDialogModel>();
		lists.add(new MeetListDialogModel("我是主持人", false));
		lists.add(new MeetListDialogModel("我是用户", false));
		mListDialog = new ListDialogManager(mActivity, "选择角色", lists);
		mListDialog.Show();
		mListDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				mListDialog = null;
				for (int i = 0; i < lists.size(); i++) {
					if (lists.get(i).isChoice()) {
						if (lists.get(i).getContent().equals("我是主持人")) {
							intent = new Intent(mActivity, LotteryMeetingHostActivity.class);
							startActivity(intent);
							MemberPrerogativeActivity.this.overTransition(2);
						}else if (lists.get(i).getContent().equals("我是用户")) {
							ShowDialog();
						}
					}
				}

			}
		});
	}
	
	/**
	 * 选择抽奖活动
	 */
	private void showChooseDialog(final List<LotteryMeetingActionModel> actionlist) {
		final List<MeetListDialogModel> lists = new ArrayList<MeetListDialogModel>();
		for (int i = 0; i < actionlist.size(); i++) {
			LotteryMeetingActionModel model = actionlist.get(i);
			String date = "起止时间：" + DateUtil.removeYS(model.startTime) + "-" + DateUtil.removeYS(model.endTime);
			lists.add(new MeetListDialogModel(model.Title, date, false));
		}
		mListDialog = new ListDialogManager(mActivity, "选择抽奖活动", lists);
		mListDialog.Show();
		mListDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				mListDialog = null;
				for (int i = 0; i < lists.size(); i++) {
					if (lists.get(i).isChoice() ) {
						intent = new Intent(mActivity, LotteryMeetingActivity.class);
						intent.putExtra("ActivityId", actionlist.get(i).Id);
						intent.putExtra("HxCodeId", actionlist.get(i).HxCodeId);
						startActivity(intent);
						MemberPrerogativeActivity.this.overTransition(2);
						return;
					}
				}
				
			}
		});
	}
	
	/**
	 * 账户冻结弹框
	 */
	private void showWhitleDialog(String msg){
		if (whitleDialog == null) {
			whitleDialog = new DialogManager(this);
		}
		
		whitleDialog.initOneBtnDialog();
		whitleDialog.setTitleandContent("提示", msg);
		whitleDialog.setConfirmBitOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				whitleDialog.dismiss();
				Constants.ClearSharePref(context);
				Constants.Clear_Cookie(context);
				intent = new Intent(MemberPrerogativeActivity.this,
						LoginActivity.class);
				MemberPrerogativeActivity.this.startActivity(intent);
				MemberPrerogativeActivity.this.finish();
			}
		});
		whitleDialog.Show();
	}

}
