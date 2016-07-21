package com.md.hjyg.adapter;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.activities.InformationActivity;
import com.md.hjyg.entity.InformationModel;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.utils.ScreenUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * ClassName: InfListAdapter date: 2016-5-16 上午11:03:16 remark:资讯adapter
 * 
 * @author pyc
 */
public class InfListAdapter extends MyBaseAdapter<InformationModel> {
	/**
	 * 更多
	 */
	public static final int more = 0;
	/**
	 * 列表界面
	 */
	public static final int m_list = 1;
	private int tab;
	private Activity mActivity;

	public InfListAdapter(List<InformationModel> lists, Activity context,
			int tab) {
		super(lists, context);
		this.mActivity = context;
		this.tab = tab;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HoldView hold = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.inflistitem_layout, null);
			hold = new HoldView(convertView);
			convertView.setTag(hold);
		} else {
			hold = (HoldView) convertView.getTag();
		}
		InformationModel model = lists.get(position);
		hold.tv_title.setText(model.Title);
		hold.tv_time.setText(DateUtil.changto(model.CreateTime, "yyyy/MM/dd"));
		Picasso.with(context).load(model.PictureUrl).into(hold.img_title);

		switch (tab) {
		case more:
			if (lists.size() == 1) {
				hold.v_line1.setVisibility(View.GONE);
				hold.v_line2.setVisibility(View.GONE);
			} else {
				if (position == 0) {
					hold.v_line1.setVisibility(View.GONE);
				} else {
					hold.v_line2.setVisibility(View.GONE);
				}
			}
			hold.tv_type.setText("更多资讯");
			break;
		case m_list:
			hold.tv_type.setText(model.media);

			break;

		default:
			break;
		}

		return convertView;
	}

	class HoldView {
		ImageView img_title;
		TextView tv_title, tv_time, tv_type;
		View v_line1, v_line2;

		public HoldView(View v) {
			img_title = (ImageView) v.findViewById(R.id.img_title);
			tv_title = (TextView) v.findViewById(R.id.tv_title);
			// 必须设置TextView的宽度，不然嵌套时，当文本换行时无法准确测量此控件的高度
			LinearLayout.LayoutParams params = (LayoutParams) tv_title
					.getLayoutParams();
			params.width = AppController.screenWidth - ScreenUtils.dip2px(context, 20+5+55+5+2);
			tv_title.setLayoutParams(params);
			
			tv_time = (TextView) v.findViewById(R.id.tv_time);
			tv_type = (TextView) v.findViewById(R.id.tv_type);
			v_line1 = v.findViewById(R.id.v_line1);
			v_line2 = v.findViewById(R.id.v_line2);
			if (tab == more) {
				tv_type.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent intent = new Intent(mActivity,
								InformationActivity.class);
						mActivity.startActivity(intent);
						mActivity.overridePendingTransition(
								R.anim.trans_right_in, R.anim.trans_lift_out);

					}
				});
			}
		}
		
	}

}
