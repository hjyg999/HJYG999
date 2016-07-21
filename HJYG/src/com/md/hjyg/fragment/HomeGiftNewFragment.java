package com.md.hjyg.fragment;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.activities.BaseFragmentActivity;
import com.md.hjyg.adapter.HomeGiftListAdapter;
import com.md.hjyg.entity.SendGiftlistModel;
import com.md.hjyg.layoutEntities.RefreshableView;
import com.md.hjyg.layoutEntities.RefreshableView.PullToRefreshListener;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.utility.LoadingMenager;
import com.md.hjyg.utility.LruCacheWebBitmapManager;
import com.md.hjyg.utility.Save;
import com.md.hjyg.xinTouConstant.Constants;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response.Listener;

/**
 * ClassName: HomeGiftNewFragment date: 2016-3-25 下午5:24:59 remark: 有礼新版活动
 * 
 * @author pyc
 */
public class HomeGiftNewFragment extends Fragment implements OnClickListener {

	private ListView mListView;
	private HomeGiftListAdapter adapter;
	private LoadingMenager loadingMenager;
	private RefreshableView refreshable_view;
	private boolean isLoading;
	private LruCacheWebBitmapManager lruCacheBitmap;
	private SendGiftlistModel model;
	private DataFetchService dft;
	private int[] Bitmap_wh;
	private LinearLayout lin_nodata;
	/**
	 * 新手标 = 0, 旅游标 = 1, 婚庆标 = 2, 健身标 = 3, 美容标 = 4, 教育标 = 5
	 */
	private int GroupType;

	public HomeGiftNewFragment(int GroupType) {
		this.GroupType = GroupType;

	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_homegiftnew_layout,
				container, false);
		findViewandInit(v);
		loadingMenager = new LoadingMenager(getActivity(),
				v.findViewById(LoadingMenager.Root_ID), this);
		getSendGiftlistlistinfo();
		Bitmap_wh = Save.getScaleBitmapWangH(610, 190);
		return v;
	}

	private void findViewandInit(View v) {
		dft = ((BaseFragmentActivity) getActivity()).getDft();
		lruCacheBitmap = LruCacheWebBitmapManager.getInstance();
		mListView = (ListView) v.findViewById(R.id.mListView);
		lin_nodata = (LinearLayout) v.findViewById(R.id.lin_nodata);
		lin_nodata.setVisibility(View.GONE);
		// 下拉刷新
		refreshable_view = (RefreshableView) v
				.findViewById(R.id.refreshable_view);
		refreshable_view.setOnRefreshListener(new PullToRefreshListener() {

			@Override
			public void onRefresh() {
				if (!isLoading) {
					isLoading = true;
					getSendGiftlistlistinfo();
				} else {
					refreshable_view.finishRefreshing();
					isLoading = false;
				}
			}
		}, 502);
	}

//	private void getSendGiftlistlistinfo() {
//		dft.getNetInfoById(Constants.GetSendGiftlist_URL, Request.Method.GET,
//				new Listener<JSONObject>() {
//
//					@Override
//					public void onResponse(JSONObject response) {
//						model = (SendGiftlistModel) dft.GetResponseObject(
//								response, SendGiftlistModel.class);
//						setUIDate();
//					}
//				}, null);
//	}
	private void getSendGiftlistlistinfo() {
		dft.postSendGroupGift(GroupType,Constants.SendGroupGift_URL, Request.Method.POST,
				new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				loadingMenager.dismiss();
				model = (SendGiftlistModel) dft.GetResponseObject(
						response, SendGiftlistModel.class);
				setUIDate();
			}
		}, null);
	}

	private void setUIDate() {
		if (model == null) {
			return;
		}
//		if (!model.isShowSep) {
//			mListView.setVisibility(View.GONE);
//			return;
//		}

		refreshable_view.finishRefreshing();
		isLoading = false;
		if (model.MemberLoanList == null || model.MemberLoanList.size() == 0) {
			lin_nodata.setVisibility(View.VISIBLE);
			refreshable_view.setVisibility(View.GONE);
		}else {
			lin_nodata.setVisibility(View.GONE);
			refreshable_view.setVisibility(View.VISIBLE);
			// 新手专享标
			adapter = new HomeGiftListAdapter(model.MemberLoanList, getActivity(),
					lruCacheBitmap, Bitmap_wh, model.showType);
			mListView.setAdapter(adapter);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;
		}
	}

}
