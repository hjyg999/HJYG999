package com.md.hjyg.fragment;

import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.activities.HomeFragmentActivity;
import com.md.hjyg.adapter.MyBaseAdapter;
import com.md.hjyg.entity.AddRateRedpacketList;
import com.md.hjyg.entity.AddRateRedpacketList.IncreaseRateCouponItemModel;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.Listener;

/**
 * ClassName: MvAddRateRedpacketFragment date: 2016-7-5 下午3:11:35
 * remark:我的红包-加息券fragment
 * 
 * @author pyc
 */
public class MyAddRateRedpacketFragment extends
		LoadMoreListFragment<IncreaseRateCouponItemModel> {
	
	@Override
	protected void setInit() {
		super.setInit();
		isneedLoadMore = false;
		dialog.initDialog();
	}

	@Override
	protected void getWebData(String ftype, final int page) {
		dft.getNetInfoById(Constants.GetIncreaseRateCouponLog_URL, Request.Method.GET, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				AddRateRedpacketList addRateRedpacketList = (AddRateRedpacketList) dft
						.GetResponseObject(response,
								AddRateRedpacketList.class);
				if (page == startPage) {
					setData(false, addRateRedpacketList.list);
				}else {
					setData(true, null);
				}
			}
		});
	}

	@Override
	protected MyBaseAdapter<IncreaseRateCouponItemModel> getAdapter(
			List<IncreaseRateCouponItemModel> lists, Activity activity) {
		return new AddRateRedpacketAdapter(lists, activity);
	}

	/**
	 * 加息券list适配器
	 */
	public class AddRateRedpacketAdapter extends MyBaseAdapter<IncreaseRateCouponItemModel> {

		public AddRateRedpacketAdapter(List<IncreaseRateCouponItemModel> lists,
				Context context) {
			super(lists, context);
		}
		
		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}
		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return false;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HoldView holdView = null;
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.adapter_listredpack_addrate, null);
				holdView = new HoldView(convertView);
				convertView.setTag(holdView);
			} else {
				holdView = (HoldView) convertView.getTag();

			}
			if (position == lists.size() - 1) {
				convertView.setPadding(0, 30, 0, 30);
			}else {
				convertView.setPadding(0, 30, 0, 0);
			}
			IncreaseRateCouponItemModel model = lists.get(position);
			
			holdView.tv_time.setText("期限："+model.DateTerm + model.DateType);
			holdView.tv_term.setText("有效期："+model.startTime +"-" + model.endTime);
			holdView.tv_condition.setText("使用条件："+model.Remark);
			holdView.tv_rate.setText("+"+model.IncreaseRate);
			
			int dType = Integer.valueOf(model.dType);
			if (dType == 0) {//可使用
				holdView.img_usetype.setImageResource(R.drawable.redpacked_addrate_cu);
				holdView.mFrameLayout.setBackgroundResource(R.drawable.redpacked_orange);
				holdView.tv_toinvset_1.setVisibility(View.VISIBLE);
				holdView.tv_toinvset_1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								HomeFragmentActivity.class);
						intent.putExtra("tab", 0);
						getActivity().startActivity(intent);
						getActivity().overridePendingTransition(
								R.anim.trans_lift_in,
								R.anim.trans_right_out);
					}
				});
				
			}else if(dType == 1){//已使用
				holdView.img_usetype.setImageResource(R.drawable.redpacked_addrate_yy);
				holdView.mFrameLayout.setBackgroundResource(R.drawable.redpacked_green);
				holdView.tv_toinvset_1.setVisibility(View.GONE);
			}else {
				holdView.img_usetype.setImageResource(R.drawable.redpacked_addrate_gq);
				holdView.mFrameLayout.setBackgroundResource(R.drawable.redpacked_gray);
				holdView.tv_toinvset_1.setVisibility(View.GONE);
				
			}
			return convertView;
		}

		class HoldView {
			FrameLayout mFrameLayout;
			ImageView img_usetype;
			TextView tv_time,tv_term,tv_condition,tv_rate,tv_toinvset_1;

			public HoldView(View v) {
				mFrameLayout = (FrameLayout) v.findViewById(R.id.mFrameLayout);
				img_usetype = (ImageView) v.findViewById(R.id.img_usetype);
				tv_time = (TextView) v.findViewById(R.id.tv_time);
				tv_term = (TextView) v.findViewById(R.id.tv_term);
				tv_condition = (TextView) v.findViewById(R.id.tv_condition);
				tv_rate = (TextView) v.findViewById(R.id.tv_rate);
				tv_toinvset_1 = (TextView) v.findViewById(R.id.tv_toinvset_1);
				
				ViewParamsSetUtil.setViewParams(mFrameLayout, 660, 210, true);
				ViewParamsSetUtil.setViewParams(img_usetype, 112, 107);
				
			}
		}

	}
	
}
