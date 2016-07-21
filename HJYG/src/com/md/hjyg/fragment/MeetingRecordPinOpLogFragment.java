package com.md.hjyg.fragment;

import com.md.hjyg.R;
import com.md.hjyg.entity.MeetingPinOpLogModel;
import com.md.hjyg.utility.MeetingWebServiceManager;
import com.md.hjyg.utils.DateUtil;
import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * ClassName: MeetingRecordPinOpLogFragment date: 2016-3-12 下午5:10:40
 * remark:会销操作记录Fragment
 * 
 * @author pyc
 */
public class MeetingRecordPinOpLogFragment extends
		ListFragment<MeetingPinOpLogModel> {

	private MeetingWebServiceManager meetingManager;
	private int ActivityId;

	public MeetingRecordPinOpLogFragment(MeetingWebServiceManager meetingManager,int ActivityId) {
		this.meetingManager = meetingManager;
		this.ActivityId = ActivityId;
	}

	@Override
	public void CallLoanListWebservice(int pageIndex) {
		meetingManager.GetMeetingPinOpLogList(ActivityId);
	}
	
	

	@SuppressLint("InflateParams")
	@Override
	public View getListItemView(int position, View convertView, ViewGroup parent) {
		HoldView holdView = null;
		if (convertView == null) {
			convertView = adapter.getInflater().inflate(R.layout.list_meetinghostpinoplog_layout, null);
			holdView = new HoldView(convertView);
			convertView.setTag(holdView);
		}else {
			holdView = (HoldView) convertView.getTag();
		}
		MeetingPinOpLogModel model = lists.get(position);
		holdView.tv_title.setText(model.Title);
		String time = DateUtil.changto(model.startTime)+" 起\n" + DateUtil.changto(model.endTime) + " 止";
		holdView.tv_time.setText(time);
		
		return convertView;
	}

	class HoldView {
		TextView tv_time, tv_title;

		public HoldView(View v) {
			tv_time = (TextView) v.findViewById(R.id.tv_time);
			tv_title = (TextView) v.findViewById(R.id.tv_title);
		}
	}

}
