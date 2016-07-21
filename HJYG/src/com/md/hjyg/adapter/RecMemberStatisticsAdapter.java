package com.md.hjyg.adapter;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.OneLevelRecRewModel;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * ClassName: RecMemberStatisticsAdapter date: 2016-5-30 下午2:02:41
 * remark:会员统计list适配器
 * 
 * @author pyc
 */
public class RecMemberStatisticsAdapter extends
		MyBaseAdapter<OneLevelRecRewModel> {

	public RecMemberStatisticsAdapter(List<OneLevelRecRewModel> lists,
			Context context) {
		super(lists, context);
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HoldView holdView = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.adapter_recommendlist_body_layout, null);
			holdView = new HoldView(convertView);
			convertView.setTag(holdView);
		} else {
			holdView = (HoldView) convertView.getTag();
		}
		holdView.setDate(position);
		return convertView;
	}

	/**
	 * 列表
	 */
	class HoldView {
		TextView tv_name, member_phone, tv_no, member_state, member_time;

		public HoldView(View v) {
			tv_name = (TextView) v.findViewById(R.id.tv_name);
			member_phone = (TextView) v.findViewById(R.id.member_phone);
			tv_no = (TextView) v.findViewById(R.id.tv_no);
			member_state = (TextView) v.findViewById(R.id.member_state);
			member_time = (TextView) v.findViewById(R.id.member_time);
		}

		public void setDate(int p) {
			OneLevelRecRewModel model = lists.get(p);
			tv_no.setText(Constants.StringToCurrency(model.InvestCount+"").replace(".00", ""));
			tv_name.setText(model.RealName);
			if (model.MobilePhone != null && model.MobilePhone.length() > 0) {
				member_phone.setText(model.MobilePhone);
			} else {
				member_phone.setText(model.RealName);

			}
			member_time.setText(model.RegTime);
			// member_award.setText(lists.get(position).Amount);
			if (model.IsActivation) {
				member_state.setText("已投资");
				member_state.setTextColor(context.getResources().getColor(
						R.color.gray));
				// member_award.setTextColor(context.getResources().getColor(R.color.gray)
				// );

			} else {
				member_state.setText("未投资");
				member_state.setTextColor(context.getResources().getColor(
						R.color.red));
				// holder.member_award.setTextColor(context.getResources().getColor(R.color.red));
			}
		}

	}

}
