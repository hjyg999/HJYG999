package com.md.hjyg.adapter;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.RedPackageObtainLogsModel.MemberShareRedEnvelopeLog;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/** 
 * ClassName: RedpacketListAdapter 
 * date: 2015-12-21 下午4:46:46 
 * remark:
 * @author pyc
 */
public class RedpacketListAdapter extends BaseAdapter{
	private List<MemberShareRedEnvelopeLog> lists;
	private LayoutInflater layoutInflater;
	private int type;
	
	public RedpacketListAdapter(Context context,List<MemberShareRedEnvelopeLog> lists){
		this.lists = lists;
		layoutInflater = LayoutInflater.from(context);
	}
	public RedpacketListAdapter(Context context,List<MemberShareRedEnvelopeLog> lists,int type){
		this.lists = lists;
		layoutInflater = LayoutInflater.from(context);
		this.type = type;
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			if (type == 1) {
				convertView = layoutInflater.inflate(
						R.layout.adapter_share_list, null);
				holder.member_phone = (TextView) convertView
						.findViewById(R.id.member_phone);
				holder.member_time = (TextView) convertView
						.findViewById(R.id.member_time);
				holder.member_award = (TextView) convertView
						.findViewById(R.id.member_award);
				holder.member_state = (TextView) convertView
						.findViewById(R.id.member_state);
				
			}else {
				convertView = layoutInflater.inflate(
						R.layout.layout_redpacket_list_adapter, null);
				
				holder.mobile = (TextView) convertView
						.findViewById(R.id.mobile);
				holder.time = (TextView) convertView
						.findViewById(R.id.time);
				holder.amount = (TextView) convertView
						.findViewById(R.id.amount);
			}
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MemberShareRedEnvelopeLog model = lists.get(position);
		if (type == 1) {
			holder.member_phone.setText(model.Mobile);
			holder.member_time.setText(model.CreateTime);
			holder.member_award.setText(model.AmountV);
			holder.member_state.setText(model.Type);
			
		}else {
			
			holder.mobile.setText(model.Mobile);
			holder.time.setText(model.CreateTime);
			holder.amount.setText(model.Amount +"");
		}
		return convertView;
	}
	
	class ViewHolder {
		TextView mobile, time, amount;
		/** 会员电话 */
		TextView member_phone;
		/** 创建日期 */
		TextView member_time;
		/** 推荐奖励 */
		TextView member_award;
		/** 奖励状态 */
		TextView member_state;
	}

}
