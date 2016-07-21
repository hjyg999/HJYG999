package com.md.hjyg.fragment;

import com.md.hjyg.R;
import com.md.hjyg.activities.LotteryMeetingHostActivity;
import com.md.hjyg.entity.LotteryMeetingActionModel;
import com.md.hjyg.utility.MeetingWebServiceManager;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utility.ToastManager;
import com.md.hjyg.utils.DateUtil;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ClassName: MeetingCountdownFragment date: 2016-3-1 下午4:38:34 remark:
 * 会销-主持人倒计时界面
 * 
 * @author pyc
 */
public class MeetingCountdownFragment extends BaseFragment implements
		OnClickListener {

	private TextView tv_showdialog,tv_time,tv_timehit;
	private LinearLayout line_mbg;
	private Bitmap meeting_host_mc;
	private ImageView img_btn,img_time;
	private Bitmap djs_bBitmaps[];
	private MeetingWebServiceManager meetingManager;
//	private int ActivityId ;
	private LotteryMeetingActionModel ActionModel;
	private long ustartTime;
	private boolean isRun;
	private int timeCha;
	private LinearLayout lin_view;
	
	public MeetingCountdownFragment(MeetingWebServiceManager meetingManager){
		this.meetingManager = meetingManager;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_lotterymeethost1_layout,
				container, false);
		findViewandInit(v);
		loadingImg();
		

		return v;
	}

	private void findViewandInit(View v) {
		lin_view = (LinearLayout) v.findViewById(R.id.lin_view);
		lin_view.setVisibility(View.INVISIBLE);
		// 选择活动
		tv_showdialog = (TextView) v.findViewById(R.id.tv_showdialog);
		tv_time = (TextView) v.findViewById(R.id.tv_time);
		tv_timehit = (TextView) v.findViewById(R.id.tv_timehit);
		tv_showdialog.setOnClickListener(this);
		// 大圆圈得布局
		line_mbg = (LinearLayout) v.findViewById(R.id.line_mbg);
		// 倒计时按钮
		img_btn = (ImageView) v.findViewById(R.id.img_btn);
		img_time = (ImageView) v.findViewById(R.id.img_time);
		img_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_showdialog:
			((LotteryMeetingHostActivity)getActivity()).showChooseDialog();;

			break;
		case R.id.img_btn:
			if (ActionModel == null || tv_showdialog.getText().toString().length() == 0) {
				ToastManager.mToastshow(getActivity(), "还未选择活动，请先选择");
			}else {
				
				meetingManager.UpdateMeetingPin(ActionModel.Id, !ActionModel.Start);
				
			}
			
			break;

		default:
			break;
		}
	}
	
	public void setDate(LotteryMeetingActionModel ActionModel,String nowTime){
		this.ActionModel = ActionModel;
		tv_showdialog.setText(ActionModel.Title);
		if (DateUtil.isStart(ActionModel.startTime)
				&& !DateUtil.isStart(ActionModel.endTime)){
			img_btn.setImageBitmap(djs_bBitmaps[0]);
			img_btn.setEnabled(true);
			ustartTime = DateUtil.StrToLong(ActionModel.UstartTime);
			timeCha = (int) (System.currentTimeMillis() - DateUtil.StrToLong(nowTime));
			isRun = ActionModel.Start;
			if (ActionModel.Start) {
				showtime();
			}else {
				tv_timehit.setText("倒计时");
				tv_time.setText("10");
			}
		}else {
			isRun = false;
			img_btn.setImageBitmap(djs_bBitmaps[1]);
			img_btn.setEnabled(false);
			if (!DateUtil.isStart(ActionModel.startTime)) {
				tv_timehit.setText("即将开始");
				tv_time.setText("10");
				img_time.setImageBitmap(djs_bBitmaps[2]);
			}else{
				tv_timehit.setText("已结束");
				tv_time.setText("00:00");
				img_time.setImageBitmap(djs_bBitmaps[3]);
			}
		}
		
	}
	
	/**
	 * 显示活动已经的进行时间
	 */
	private void showtime(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
				while (isRun) {
					mHandler.sendEmptyMessage(1);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}).start();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				setViewHight(line_mbg, meeting_host_mc);
				img_btn.setImageBitmap(djs_bBitmaps[0]);
				lin_view.setVisibility(View.VISIBLE);

				break;
			case 1:
				if (isRun) {
					tv_time.setText(timeToString());
				}
				break;

			default:
				break;
			}
		};
	};
	
	private String timeToString(){
		String timeString = "";
		int time = (int) ((System.currentTimeMillis() - ustartTime - timeCha)/1000);
		if (time < 0) {
			tv_timehit.setText("倒计时");
			img_time.setImageBitmap(djs_bBitmaps[2]);
			return unitFormat(-time);
		}
		tv_timehit.setText("抽奖计时");
		img_time.setImageBitmap(djs_bBitmaps[3]);
		int hour = 0;
		int minute = 0;
		int second = 0;
		minute = time / 60;
		if (minute < 60) {
			second = time % 60;
			
			timeString =  unitFormat(minute) + ":" + unitFormat(second) ;

		} else {
			hour = minute / 60;
			minute = minute % 60;
			second = time - hour * 3600 - minute * 60;
			timeString = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second) ;
		}
		
		return timeString;
	}
	
	private String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}

	/**
	 * 加载图片
	 */
	private void loadingImg() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

				Bitmap reimg = BitmapFactory.decodeResource(getResources(),
						R.drawable.novice_red_bg);
				meeting_host_mc = Save.ScaleBitmap(BitmapFactory
						.decodeResource(getResources(),
								R.drawable.meeting_host_mc), getActivity(),
						reimg);

				djs_bBitmaps = new Bitmap[4];

				djs_bBitmaps[0] = Save.ScaleBitmap(BitmapFactory
						.decodeResource(getResources(),
								R.drawable.meeting_djs_btn_yes), getActivity(),
						reimg);
				djs_bBitmaps[1] = Save.ScaleBitmap(BitmapFactory
						.decodeResource(getResources(),
								R.drawable.meeting_djs_btn_no), getActivity(),
						reimg);
				djs_bBitmaps[2] = Save.ScaleBitmap(
						BitmapFactory.decodeResource(getResources(),
								R.drawable.meeting_djs), getActivity(), reimg);
				djs_bBitmaps[3] = Save.ScaleBitmap(BitmapFactory
						.decodeResource(getResources(), R.drawable.meeting_js),
						getActivity(), reimg);

				mHandler.sendEmptyMessage(0);
			}
		}).start();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		isRun = false;
	}

}
