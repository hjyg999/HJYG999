package com.md.hjyg.adapter;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.StatsMonthlyRewardListsModel.StatsMonthlyReward;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/** 
 * ClassName: RecommendedRewardAdapter 
 * date: 2015-11-27 上午8:32:33
 * remark:已收奖励，待收奖励列表适配器
 * @author rw
 */
public class RecommendedRewardAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	Context context;
	private List<StatsMonthlyReward> lists ;
	private int type;
	
	public RecommendedRewardAdapter(Context context,List<StatsMonthlyReward> lists,int type) {
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
		this.lists = lists;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.recommendedreward_item, null);
			convertView.setTag(holder);
			
			holder.reward_time = (TextView) convertView.findViewById(R.id.reward_time);
			holder.reward_self_get = (TextView) convertView.findViewById(R.id.reward_self_get);
			holder.reward_friends_get = (TextView) convertView.findViewById(R.id.reward_friends_get);
			
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		StatsMonthlyReward model = lists.get(position);
		String time = model.YearMonth.replace("\\", "");
		holder.reward_time.setText(time);
		switch (type) {
		case 1://已收奖励
//			if (model.Total == null || model.Total.length() == 0) {
//				holder.reward_self_get.setText("0.00");
//			}else {
//				holder.reward_self_get.setText(Constants.StringToCurrency(model.Total));
//				
//			}
//			if (model.FirstInCome == null || model.FirstInCome.length() == 0) {
//				holder.reward_friends_get.setText("0.00");
//			}else {
//				holder.reward_friends_get.setText(Constants.StringToCurrency(model.FirstInCome));
//				
//			}
			
			break;
		case 2://代收奖励
			
//			if (model.FirstUnfinishedInterestReward == null || model.FirstUnfinishedInterestReward.length() == 0) {
//				holder.reward_self_get.setText("0.00");
//			}else {
//				holder.reward_self_get.setText(Constants.StringToCurrency(model.FirstUnfinishedInterestReward));
//				
//			}
//
//			if (model.FirstInCome == null || model.FirstInCome.length() == 0) {
//				holder.reward_friends_get.setText("0.00");
//			}else {
//				holder.reward_friends_get.setText(Constants.StringToCurrency(model.FirstInCome));
//				
//			}
			
			break;

		default:
			break;
		}
		
		return convertView;
	}
	
	public class ViewHolder {

		TextView reward_time,reward_self_get,reward_friends_get;
	}

}
