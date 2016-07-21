package com.md.hjyg.utility;

import java.util.List;
import java.util.TimerTask;

import com.md.hjyg.adapter.GoldBeanRecordAdapter;
import com.md.hjyg.adapter.LotteryMainListAdapter;
import com.md.hjyg.adapter.MeetingListViewAdapter;
import com.md.hjyg.entity.AnnualMeetingLotteryInfoModel.MemberLotteryLogModel;
import com.md.hjyg.entity.GoldBeanLotteryInfoModel.GoldBeanMemberLotteryLog;
import com.md.hjyg.entity.MemberLotteryLog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

public class TimeTaskScroll extends TimerTask {
	
	private ListView listView;
	
	public TimeTaskScroll(Context context, ListView listView, List<MemberLotteryLogModel> list ,int type){
		this.listView = listView;
		listView.setAdapter(new LotteryMainListAdapter(context, list,type));
	}
	public TimeTaskScroll(Context context, ListView listView, List<MemberLotteryLog> list ){
		this.listView = listView;
		listView.setAdapter(new MeetingListViewAdapter(context, list,true));
	}
	public TimeTaskScroll(Context context, ListView listView, List<GoldBeanMemberLotteryLog> list ,String i){
		this.listView = listView;
		listView.setAdapter(new GoldBeanRecordAdapter(list,context));
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			listView.smoothScrollBy(1, 0);
		};
	};

	@Override
	public void run() {
		Message msg = handler.obtainMessage();
		handler.sendMessage(msg);
	}

}
