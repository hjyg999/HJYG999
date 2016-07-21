package com.md.hjyg.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.md.hjyg.R;
import com.md.hjyg.entity.ConsumptionInfoModel;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.ViewParamsSetUtil;

/**
 * ClassName: ConsumptionGridViewAdapter date: 2016-6-28 下午3:00:36 remark:消费标分类
 * GridView 适配器
 * 
 * @author pyc
 */
public class ConsumptionGridViewAdapter extends
		MyBaseAdapter<ConsumptionInfoModel> {

	private int[] img_wh;
	private int spacing;

	public ConsumptionGridViewAdapter(List<ConsumptionInfoModel> lists,
			Context context) {
		super(lists, context);
		img_wh = Save.getScaleBitmapWangH(354, 300);
		spacing = AppController.screenWidth / 2 - img_wh[0];
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HoldView holdView = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.adapter_giftgridview_layout, null);
			holdView = new HoldView(convertView);
			convertView.setTag(holdView);
		} else {
			holdView = (HoldView) convertView.getTag();
		}
		ConsumptionInfoModel model = lists.get(position);
		holdView.img.setImageResource(model.getImgRid());
		holdView.tv_title.setText(model.getTitle());
		holdView.tv_hit.setText(model.getContent());
		if (position % 2 == 0) {
			if (position == 0) {
				convertView.setPadding(0, spacing * 2, spacing, spacing);
			} else {
				convertView.setPadding(0, spacing, spacing, spacing);
			}
		} else {
			if (position == 1) {
				convertView.setPadding(spacing, spacing * 2, 0, spacing);
			} else {
				convertView.setPadding(spacing, spacing, 0, spacing);
			}
		}
		return convertView;
	}

	class HoldView {
		ImageView img;
		TextView tv_title, tv_hit;

		public HoldView(View v) {
			img = (ImageView) v.findViewById(R.id.img);
			tv_title = (TextView) v.findViewById(R.id.tv_title);
			tv_hit = (TextView) v.findViewById(R.id.tv_hit);
			ViewParamsSetUtil.setViewHandW_fra(img, img_wh[1], img_wh[0]);
		}
	}

}
