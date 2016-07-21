package com.md.hjyg.adapter;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.GoldBeanFinanceLogsModel.GoldBeanFinanceLogModel;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.utils.ViewParamsSetUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/** 
 * ClassName: GoldBeanRecoraListAdapter 
 * date: 2016-5-26 上午11:57:02 
 * remark:金豆记录
 * @author pyc
 */
public class GoldBeanRecoraListAdapter extends MyBaseAdapter<GoldBeanFinanceLogModel>{

	public GoldBeanRecoraListAdapter(List<GoldBeanFinanceLogModel> lists, Context context,int tab) {
		super(lists, context);
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HoldView hold = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.adapter_goldbeanrecord_layout, null);
			hold = new HoldView(convertView);
			convertView.setTag(hold);
		}else {
			hold = (HoldView) convertView.getTag();
		}
		GoldBeanFinanceLogModel model = lists.get(position);
		hold.tv_title.setText(model.Remark);
		hold.tv_time.setText(DateUtil.changto(model.CreateTime, "yyyy/MM/dd HH:mm", "MM/dd HH:mm"));
		hold.tv_type.setText("剩余: "+model.LeaveGoldBean);
		if (Integer.parseInt(model.IncomeNumber) == 0) {
			hold.tv_amount.setText("-" + model.PayNumber);
			hold.tv_amount.setTextColor(context.getResources().getColor(R.color.gray_gold));
		}else {
			hold.tv_amount.setText("+" + model.IncomeNumber);
			hold.tv_amount.setTextColor(context.getResources().getColor(R.color.green_99CC33));
		}
		
		switch (model.Type) {
		case GoldBeanFinanceLogModel.充值:
			hold.tv_img.setImageResource(R.drawable.gb_record0);
			
			break;
		case GoldBeanFinanceLogModel.收回:
			hold.tv_img.setImageResource(R.drawable.gb_record1);
			break;
		case GoldBeanFinanceLogModel.退回回收:
			hold.tv_img.setImageResource(R.drawable.gb_record2);
			break;
		case GoldBeanFinanceLogModel.投资:
			hold.tv_img.setImageResource(R.drawable.gb_record3);
			break;
		case GoldBeanFinanceLogModel.推荐金牌理财师:
			hold.tv_img.setImageResource(R.drawable.gb_record4);
			break;
		case GoldBeanFinanceLogModel.推荐互联网理财师:
			hold.tv_img.setImageResource(R.drawable.gb_record5);
			break;
		case GoldBeanFinanceLogModel.投资消费标:
			hold.tv_img.setImageResource(R.drawable.gb_record3);
			break;
		case GoldBeanFinanceLogModel.抽奖:
			hold.tv_img.setImageResource(R.drawable.gb_record7);
			break;
		case GoldBeanFinanceLogModel.中奖:
			hold.tv_img.setImageResource(R.drawable.gb_record8);
			break;
		case GoldBeanFinanceLogModel.签到:
			hold.tv_img.setImageResource(R.drawable.gb_record9);
			break;
		case GoldBeanFinanceLogModel.签到礼品:
			hold.tv_img.setImageResource(R.drawable.gb_record10);
			break;

		default:
			break;
		}
		return convertView;
	}
	
	class HoldView{
		TextView tv_title,tv_amount,tv_type,tv_time;
		ImageView tv_img;
		public HoldView(View v){
			tv_img = (ImageView) v.findViewById(R.id.tv_img);
			tv_title = (TextView) v.findViewById(R.id.tv_title);
			tv_amount = (TextView) v.findViewById(R.id.tv_amount);
			tv_type = (TextView) v.findViewById(R.id.tv_type);
			tv_time = (TextView) v.findViewById(R.id.tv_time);
			
			ViewParamsSetUtil.setViewParams(tv_img, 138, 138, true);
		}
		
	}

}
