package com.md.hjyg.adapter;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.GoldBeanLotteryInfoModel.GoldBeanMemberLotteryLog;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/** 
 * ClassName: GoldBeanRecordAdapter 
 * date: 2016-6-16 下午2:22:00 
 * remark:金豆中奖记录
 * @author pyc
 */
public class GoldBeanRecordAdapter extends BaseAdapter{
	private LayoutInflater inflater;;

	private List<GoldBeanMemberLotteryLog> lists;
	private int size;

	/**
	 * @param lists
	 * @param context
	 */
	public GoldBeanRecordAdapter(List<GoldBeanMemberLotteryLog> lists,
			Context context) {
		this.lists = lists;
		inflater = LayoutInflater.from(context);
		size = lists.size();
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HoldView holdView = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.adapter_goldbeanlist_layout, null);
			holdView = new HoldView(convertView);
			convertView.setTag(holdView);
		}else {
			holdView = (HoldView) convertView.getTag();
		}
		GoldBeanMemberLotteryLog model = lists.get(position % size);
		holdView.tv_Phone.setText(Constants.NewreplacePhoneNumberformat(model.MobilePhone));
		switch (model.Type) {
		case 0:
			holdView.tv_PrizeName.setText(model.PrizeName);
			break;
		case 1:
			holdView.tv_PrizeName.setText(model.PrizeName );
			break;
		case 2:
			holdView.tv_PrizeName.setText(model.PrizeName);
			break;
		case 3:
			holdView.tv_PrizeName.setText(model.PrizeName);
			break;
		case 4:
			holdView.tv_PrizeName.setText("金豆" + (int)model.GoldBeanNumber + "个");
			break;
		case 5:
			holdView.tv_PrizeName.setText(model.PrizeName);
			break;
		default:
			holdView.tv_PrizeName.setText(model.PrizeName);
			break;
		}
		return convertView;
	}
	
	class HoldView{
		TextView tv_Phone,tv_PrizeName;
		public HoldView (View v){
			tv_Phone = (TextView) v.findViewById(R.id.tv_Phone);
			tv_PrizeName = (TextView) v.findViewById(R.id.tv_PrizeName);
		}
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position % size);
		
	}

	@Override
	public long getItemId(int position) {
		return position % size;
	}

}
