package com.md.hjyg.adapter;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.OneLevelRecRewModel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/** 
 * ClassName: RecRewOneLevelAdapter 
 * date: 2015-11-28 下午3:21:52
 * remark:推荐人会员统计listView适配器
 * @author rw
 */
public class RecRewOneLevelAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;
	private List<OneLevelRecRewModel> lists;
	Context context;

	public RecRewOneLevelAdapter(Context context, List<OneLevelRecRewModel> lists) {
		layoutInflater = LayoutInflater.from(context);
		this.lists = lists;
		this.context = context;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(
					R.layout.adapter_memberstatistics_layout, null);
			holder.member_phone = (TextView) convertView
					.findViewById(R.id.member_phone);
			holder.member_time = (TextView) convertView
					.findViewById(R.id.member_time);
			holder.member_award = (TextView) convertView
					.findViewById(R.id.member_award);
			holder.member_state = (TextView) convertView
					.findViewById(R.id.member_state);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (lists.get(position).MobilePhone !=null && lists.get(position).MobilePhone.length() > 0) {
			holder.member_phone.setText(lists.get(position).MobilePhone);
		}else {
			holder.member_phone.setText(lists.get(position).RealName);
			
		}
		holder.member_time.setText(lists.get(position).RegTime);
		holder.member_award.setText(lists.get(position).Amount);
		if (lists.get(position).IsActivation) {
			holder.member_state.setText("已领取");
			holder.member_state.setTextColor(context.getResources().getColor(R.color.gray) );
			holder.member_award.setTextColor(context.getResources().getColor(R.color.gray) );
			
			
		}else {
			holder.member_state.setText("未激活");
			holder.member_state.setTextColor(context.getResources().getColor(R.color.red));
			holder.member_award.setTextColor(context.getResources().getColor(R.color.red));
		}
		return convertView;
	}

	class ViewHolder {
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
