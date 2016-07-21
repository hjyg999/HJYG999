package com.md.hjyg.adapter;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.MemberLotteryLog;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * ClassName: MeetingListViewAdapter date: 2016-3-15 上午10:37:20 remark:
 * 
 * @author pyc
 */
public class MeetingListViewAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;;

	private List<MemberLotteryLog> Lists;
	private boolean isScroll;

	public MeetingListViewAdapter(Context context,
			List<MemberLotteryLog> Lists, boolean isScroll) {
		this.Lists = Lists;
		layoutInflater = LayoutInflater.from(context);
		this.isScroll = isScroll;

	}

	@Override
	public int getCount() {
		if (isScroll){
			return Integer.MAX_VALUE;
		} else {
			return Lists.size();
		}
	}

	@Override
	public Object getItem(int position) {
		if (isScroll){
			return Lists.get(position % Lists.size());
		} else {
			return Lists.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		if (isScroll){
			return position % Lists.size();
		} else {
			return position;
		}
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.list_meeting_layout, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		MemberLotteryLog model = Lists.get(position % Lists.size());
		holder.tv_phone.setText(Constants.NewreplacePhoneNumberformat(model.MobilePhone));
		if (model.type == 0) {
			holder.tv_prizename.setText(model.PrizeName);
		}else if (model.type == 3) {
			holder.tv_prizename.setText((int)model.RedEnvelopeAmount + "元体验金红包");
		}else if (model.type == 1) {
			holder.tv_prizename.setText((int)model.RedEnvelopeAmount + "元投资红包");
		}else if (model.type == 2) {
			holder.tv_prizename.setText((int)model.RedEnvelopeAmount + "元投资红包");
		}else if (model.type == 5) {
			holder.tv_prizename.setText( "+" +Constants.StringToCurrency(model.IncreaseRate +"") + "%加息券");
		}
		holder.tv_time.setText(DateUtil.removeYS(model.CreateTime));
		return convertView;
	}
	
	class ViewHolder {
		TextView tv_phone, tv_prizename, tv_time;
		
		public ViewHolder(View v){
			tv_phone = (TextView) v.findViewById(R.id.tv_phone);
			tv_prizename = (TextView) v.findViewById(R.id.tv_prizename);
			tv_time = (TextView) v.findViewById(R.id.tv_time);
		}
	}

}
