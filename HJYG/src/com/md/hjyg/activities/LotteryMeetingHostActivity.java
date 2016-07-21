package com.md.hjyg.activities;

import java.util.ArrayList;
import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.LotteryActionModel;
import com.md.hjyg.entity.LotteryMeetingActionModel;
import com.md.hjyg.entity.MeetListDialogModel;
import com.md.hjyg.entity.MeetingMemberLotteryLogListModel;
import com.md.hjyg.entity.MeetingPinOpLogListModel;
import com.md.hjyg.entity.Notification;
import com.md.hjyg.fragment.MeetingCountdownFragment;
import com.md.hjyg.fragment.MeetingRecordFragment;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.utility.ListDialogManager;
import com.md.hjyg.utility.MeetingWebServiceManager;
import com.md.hjyg.utility.ToastManager;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

/**
 * ClassName: LotteryMeetingHostActivity date: 2016-3-1 下午3:13:54 remark:
 * 会销-主持人界面
 * 
 * @author pyc
 */
public class LotteryMeetingHostActivity extends BaseFragmentActivity implements
		OnClickListener {

	HeaderControler header;
	/** 返回按钮 */
	private LinearLayout lay_back_investment;
	private FragmentManager fm;
	private Fragment[] fragments;
	private int tab;
	private float xDistance, yDistance,  xLast, yLast;
	private int move = 0;
	private int rmove = 0;
	private MeetingWebServiceManager meetingManager;
	private ListDialogManager mListDialog;
	private LotteryActionModel model;
	private Context context;
	private boolean isfromBtn;
	private int ActivityId = -1;
	private MeetingRecordFragment recordFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lotterymeetinghost_layout);
		context = getBaseContext();
		findViewandInit();
		setFragment(0);
		meetingManager.GetLotteryActivityList();
	}

	private void findViewandInit() {

		tab = 0;
		fragments = new Fragment[2];

		header = new HeaderControler(this, true, false, "活动抽奖",
				Constants.CheckAuthtoken(getBaseContext()));
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
		lay_back_investment.setOnClickListener(this);

		meetingManager = new MeetingWebServiceManager(this, dft, handler);

	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 203:
				model = (LotteryActionModel) msg.obj;
				if (isfromBtn) {
					for (int i = 0; i < model.list.size(); i++) {
						if (model.list.get(i).Id == ActivityId) {
							((MeetingCountdownFragment) fragments[0]).setDate(
									model.list.get(i), model.nowTime);
						}
					}
				}
				break;
			case 204:
				Notification notification = (Notification) msg.obj;
				if (notification.ProcessResult == 1) {
					isfromBtn = true;
					meetingManager.GetLotteryActivityList();
				} else {
					ToastManager.mToastshow(context, notification.ProcessMessage);
				}
				break;
			case 205:
				MeetingPinOpLogListModel pinOpLogListModel = (MeetingPinOpLogListModel) msg.obj;
				recordFragment.setTabTwoData(pinOpLogListModel.list);
				
				break;
			case 206:
				MeetingMemberLotteryLogListModel lotteryLogListModel = (MeetingMemberLotteryLogListModel) msg.obj;
				recordFragment.setexpandableListViewDate(lotteryLogListModel.list);
				
				break;

			default:
				break;
			}
		};
	};

	/**
	 * 选择抽奖活动
	 */
	public void showChooseDialog() {
		if (model == null || model.list == null) {
			return;
		}
		final List<MeetListDialogModel> lists = new ArrayList<MeetListDialogModel>();
		for (int i = 0; i < model.list.size(); i++) {
			LotteryMeetingActionModel Actionmodel = model.list.get(i);
			int state = 0;// 状态 1进行中，0未开始，2已结束
			if (DateUtil.isStart(Actionmodel.startTime)
					&& !DateUtil.isStart(Actionmodel.endTime)) {
				state = 1;
			} else if (DateUtil.isStart(Actionmodel.endTime)) {
				state = 2;
			} else if (!DateUtil.isStart(Actionmodel.startTime)) {
				state = 0;
			}

			lists.add(new MeetListDialogModel(Actionmodel.Title, false, state));
		}
		mListDialog = new ListDialogManager(this, "选择活动", lists);
		mListDialog.Show();
		mListDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				mListDialog = null;
				for (int i = 0; i < lists.size(); i++) {
					if (lists.get(i).isChoice() && fragments[0] != null) {
						((MeetingCountdownFragment) fragments[0]).setDate(
								model.list.get(i), model.nowTime);
						ActivityId = model.list.get(i).Id;
					}
				}

			}
		});
	}

	@SuppressLint("Recycle")
	private void setFragment(int n) {
		fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		if (n == 2) {
			transaction.setCustomAnimations(R.anim.trans_lift_in,
					R.anim.trans_right_out);
		}else if (n == 1) {
			transaction.setCustomAnimations(R.anim.trans_right_in,
					R.anim.trans_lift_out);
		}
		
		if (fragments[tab] == null) {
			if (tab == 0) {
				fragments[tab] = new MeetingCountdownFragment(meetingManager);
			} else {
				if (recordFragment == null) {
					recordFragment = new MeetingRecordFragment(meetingManager,ActivityId);
				}
				fragments[tab] = recordFragment;
			}
			transaction.add(R.id.mframelayout, fragments[tab]);
		}

		for (int i = 0; i < fragments.length; i++) {
			if (fragments[i] != null) {
				if (i == tab) {
					transaction.show(fragments[i]);
				} else {
					transaction.hide(fragments[i]);
				}
			}
			if (tab == 0 && fragments[1] != null) {
				transaction.remove(fragments[1]);
				fragments[1] = null;
				recordFragment = null;
			}
		}

		transaction.commit();
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDistance = yDistance = 0.0f;
//			xMove = yMove = 0.0f;
			xLast = event.getX();
			yLast = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			float curX = event.getX();
			float curY = event.getY();

			xDistance += (curX - xLast);
			yDistance += curY - yLast;
			if (xDistance < 0 && Math.abs(xDistance) > 6.0f && Math.abs(xDistance) > Math.abs(yDistance)) {
				move++;
			}else if(xDistance > 0 && Math.abs(xDistance) > 6.0f && Math.abs(xDistance) > Math.abs(yDistance)){
				rmove ++;
			}

			break;
		case MotionEvent.ACTION_UP:
			if (move > 0) {
				move = 0;
				if (tab == 0) {
					if (ActivityId == -1) {
						ToastManager.mToastshow(this, "还未选择活动，请先选择");
					}else {
						tab = 1;
						setFragment(1);
					}
				}
			}
			if (rmove > 0) {
				rmove = 0;
				if (tab == 1) {
					tab = 0;
					setFragment(2);
				}
			}
			

			break;

		default:
			break;
		}
		return super.dispatchTouchEvent(event);
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_back_investment:
			this.finish();
			overTransition(1);
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

}
