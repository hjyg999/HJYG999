package com.md.hjyg.adapter;

import java.util.ArrayList;
import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.AnnualMeetingLotteryInfoModel.MemberLotteryLogModel;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * ClassName: LotteryMainListAdapter date: 2015-10-26 上午8:45:05 remark:
 * 抽奖互动首页用户中奖记录列表
 * 
 * @author rw
 */
public class LotteryMainListAdapter extends BaseAdapter {
	LayoutInflater layoutInflater;
	Context context;
	public List<MemberLotteryLogModel> messageList = new ArrayList<MemberLotteryLogModel>();
	private int type;

	public LotteryMainListAdapter(Context context,
			List<MemberLotteryLogModel> messageList,int type) {
		this.type = type;
		this.context = context;
		this.messageList = messageList;
		layoutInflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (messageList.size() > 7){
			return Integer.MAX_VALUE;
		} else {
			return messageList.size();
		}
	}

	@Override
	public Object getItem(int positon) {
		if (messageList.size() > 7){
			return messageList.get(positon % messageList.size());
		} else {
			return messageList.get(positon);
		}
	}

	@Override
	public long getItemId(int positon) {
		if (messageList.size() > 7){
			return positon % messageList.size();
		} else {
			return positon;
		}
	}

	@Override
	public View getView(int positon, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		View showLeaveStatus = convertView;
		if (showLeaveStatus == null) {
			holder = new ViewHolder();
			showLeaveStatus = layoutInflater.inflate(
					R.layout.lottery_main_listcount_item, null);
			holder.lottery_username_tv = (TextView) showLeaveStatus
					.findViewById(R.id.lottery_username_tv);
			holder.lottery_prizename_tv = (TextView) showLeaveStatus
					.findViewById(R.id.lottery_prizename_tv);
			holder.lottery_time_tv = (TextView) showLeaveStatus
					.findViewById(R.id.lottery_time_tv);
			if (type == 1) {
				holder.lottery_username_tv.setTextColor(context.getResources().getColor(R.color.white));
				holder.lottery_prizename_tv.setTextColor(context.getResources().getColor(R.color.white));
				holder.lottery_time_tv.setTextColor(context.getResources().getColor(R.color.white));
			}
		} else {
			holder = (ViewHolder) showLeaveStatus.getTag();
		}
		showLeaveStatus.setTag(holder);
		
//		if (messageList.get(positon % messageList.size()).type == 0) {// 实物奖品
//			holder.lottery_prizename_tv.setText(messageList.get(positon % messageList.size()).PrizeName);
//		} else if (messageList.get(positon % messageList.size()).type == 1) {// 理财红包
//			holder.lottery_prizename_tv.setText("红包" + (int) messageList.get(positon % messageList.size()).InvestRedAmount + "元");
//		} else {// 现金红包
//			holder.lottery_prizename_tv.setText("红包" + (int) messageList.get(positon % messageList.size()).RedEnvelopeAmount + "元");
//		}
//		holder.lottery_time_tv.setText(messageList.get(positon % messageList.size()).CreateTime);
		MemberLotteryLogModel model = messageList.get(positon % messageList.size());
		String string =  DateUtil.removeS(model.CreateTime) ;
		holder.lottery_time_tv.setText(string.replaceAll("-", "/"));
		String MobilePhone = Constants.NewreplacePhoneNumberformat(model.MobilePhone);
		holder.lottery_username_tv.setText(MobilePhone);
		if (type == 1) {
			if (model.type == 0) {
				holder.lottery_prizename_tv.setText(model.PrizeName);
			}else {
				if (model.ExperienceAmount>=10000) {
					holder.lottery_prizename_tv.setText((int)model.ExperienceAmount/10000 +"万元" + model.PrizeName);
				}else {
					holder.lottery_prizename_tv.setText((int)model.ExperienceAmount +"元" + model.PrizeName);
				}
				
			}
		}else {
			if (model.type == 0) {
				holder.lottery_prizename_tv.setText(model.PrizeName);
			}else if(model.type == 2){
				holder.lottery_prizename_tv.setText((int)model.RedEnvelopeAmount +"元" );
			}else if (model.type == 3) {
				holder.lottery_prizename_tv.setText((int)model.ExperienceAmount +"元" + model.PrizeName);
			}else if (model.type == 1) {
				holder.lottery_prizename_tv.setText((int)model.InvestRedAmount+"元" );
			}
			
		}
		return showLeaveStatus;
	}

	public class ViewHolder {
		TextView lottery_username_tv, lottery_prizename_tv, lottery_time_tv;
	}

}
