package com.md.hjyg.adapter;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.MeetingMemberLotteryLogModel;
import com.md.hjyg.entity.MemberLotteryLog;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ClassName: MeetingExpandaListAdapter date: 2016-3-14 上午10:58:12 remark:
 * 
 * @author pyc
 */
public class MeetingExpandaListAdapter extends BaseExpandableListAdapter {
	private List<MeetingMemberLotteryLogModel> list;
	private LayoutInflater inflater;

	public MeetingExpandaListAdapter(List<MeetingMemberLotteryLogModel> list,
			Context context) {
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getGroupCount() {
		return list.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return list.get(groupPosition).MemberLotteryLogList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return list.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return list.get(groupPosition).MemberLotteryLogList.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View view = inflater.inflate(
				R.layout.expandablelistview_meetingp_layout, null);
		TextView tv_group = (TextView) view.findViewById(R.id.tv_group);
		TextView tv_count = (TextView) view.findViewById(R.id.tv_count);
		ImageView img_group = (ImageView) view.findViewById(R.id.img_group);
		List<MemberLotteryLog> MemberLotteryLogList = list.get(groupPosition).MemberLotteryLogList;
		// if (list.get(groupPosition).type == 0) {
		// tv_group.setText(list.get(groupPosition).PrizeName +"");
		// }else if (list.get(groupPosition).type == 3) {
		// // if (MemberLotteryLogList != null && MemberLotteryLogList.size()>0)
		// {
		// // tv_group.setText((int)MemberLotteryLogList.get(0).InvestRedAmount
		// +"元体验金");
		// // }else {
		// // }
		// tv_group.setText(list.get(groupPosition).PrizeName);
		// // tv_group.setText(list.get(groupPosition).PrizeName +"体验金");
		// }else if (list.get(groupPosition).type == 1) {
		// if (MemberLotteryLogList != null && MemberLotteryLogList.size()>0) {
		// tv_group.setText((int)MemberLotteryLogList.get(0).InvestRedAmount
		// +"元投资红包");
		// }else {
		// tv_group.setText("投资红包");
		// }
		// }else if (list.get(groupPosition).type == 2) {
		// if (MemberLotteryLogList != null && MemberLotteryLogList.size()>0) {
		// tv_group.setText((int)MemberLotteryLogList.get(0).InvestRedAmount
		// +"元现金红包");
		// }else {
		// tv_group.setText("现金红包");
		// }
		// // tv_group.setText(list.get(groupPosition).PrizeName +"现金红包");
		// }
		if (list.get(groupPosition).type == 0) {
			tv_group.setText(list.get(groupPosition).PrizeName + "");
		} else if (list.get(groupPosition).type == 5) {
			tv_group.setText("加息券+"+Constants.StringToCurrency(MemberLotteryLogList.get(0).IncreaseRate +"") +"%"  );
		} 
		else {
			if (MemberLotteryLogList != null && MemberLotteryLogList.size() > 0) {
				tv_group.setText( (int)MemberLotteryLogList.get(0).RedEnvelopeAmount +"元" + list.get(groupPosition).PrizeName );
			}
		}
		tv_count.setText(list.get(groupPosition).PrizeNumber + "");
		if (isExpanded) {
			img_group.setImageResource(R.drawable.gold_isexpanded);
		} else {
			img_group.setImageResource(R.drawable.gold_noexpanded);
		}
		return view;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View view = inflater.inflate(
				R.layout.expandablelistview_meetingc_layout, null);
		TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
		TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
		MemberLotteryLog log = list.get(groupPosition).MemberLotteryLogList
				.get(childPosition);
		tv_phone.setText(Constants.NewreplacePhoneNumberformat(log.MobilePhone));
		tv_time.setText(DateUtil.changto(log.CreateTime));
		return view;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}
