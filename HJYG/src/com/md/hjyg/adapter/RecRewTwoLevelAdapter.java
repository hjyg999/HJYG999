package com.md.hjyg.adapter;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.TwoLevelRecRewModel;
import com.md.hjyg.utils.TextUtil;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/** 
 * ClassName: RecRewTwoLevelAdapter 
 * date: 2015-11-28 下午3:21:32
 * remark:二级奖励适配器
 * @author rw
 */
public class RecRewTwoLevelAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;
	private List<TwoLevelRecRewModel> lists;

	public RecRewTwoLevelAdapter(Context context, List<TwoLevelRecRewModel> lists) {
		layoutInflater = LayoutInflater.from(context);
		this.lists = lists;
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
		TwoLevelRecRewModel model = lists.get(position);
		if (model.MobilePhone != null && model.MobilePhone.length() >0) {
			holder.member_phone.setText(model.MobilePhone);
		}else {
			holder.member_phone.setText(model.RealName);
			
		}
		holder.member_time.setText(model.RegTime);
		holder.member_award.setText(model.Amount);
		String state = model.RegRecTotal;
		int idx = state.indexOf("/") ;
		holder.member_state.setText(TextUtil.getRedString(state, 0, idx));
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
