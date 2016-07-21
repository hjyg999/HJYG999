package com.md.hjyg.fragment;

import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.activities.BaseFragmentActivity;
import com.md.hjyg.adapter.GoldBeanRecoraListAdapter;
import com.md.hjyg.entity.GoldBeanFinanceLogsModel;
import com.md.hjyg.entity.GoldBeanFinanceLogsModel.GoldBeanFinanceLogModel;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.services.DataFetchService.DftErrorListener;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.utility.LoadMoreListView;
import com.md.hjyg.utility.LoadMoreListView.OnLoadMoreListener;
import com.md.hjyg.xinTouConstant.Constants;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.Listener;

/**
 * ClassName: GoldBeanRecordFragment date: 2016-5-26 上午10:49:17
 * remark:金豆记录Fragment
 * 
 * @author pyc
 */
public class GoldBeanRecordFragment extends BaseFragment implements
		DftErrorListener, OnLoadMoreListener {
	private int tab;
	private LoadMoreListView mLoadMoreListView;
	private LinearLayout lin_nodata;
	private DialogProgressManager dialog;
	private DataFetchService dft;
	private TextView tv_nodate;
	private ImageView img_nodate;
	private final static int startPage = 1;
	private int page = startPage;
	private String ftype[] = { "", "0", "1" };

	private GoldBeanRecoraListAdapter adapter;
	private List<GoldBeanFinanceLogModel> lists;

	public GoldBeanRecordFragment(int tab) {
		this.tab = tab;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_goldbeanrecord_layout,
				container, false);
		findViewandInit(v);
		// dialog.initDialog();
		getWebDate(page);
		return v;
	}

	private void findViewandInit(View v) {
		dft = ((BaseFragmentActivity) getActivity()).getDft();
		dialog = new DialogProgressManager(getActivity(), "努力加载中...");
		dft.setOnDftErrorListener(this);

		lin_nodata = (LinearLayout) v.findViewById(R.id.lin_nodata);
		tv_nodate = (TextView) v.findViewById(R.id.tv_nodate);
		img_nodate = (ImageView) v.findViewById(R.id.img_nodate);
		mLoadMoreListView = (LoadMoreListView) v.findViewById(R.id.mListView);
	}

	private void getWebDate(final int page) {
		dft.GoldBeanFinanceLogs(ftype[tab], page,
				Constants.GoldBeanFinanceLogs_URL, Request.Method.POST,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject josnbject) {
						GoldBeanFinanceLogsModel model = (GoldBeanFinanceLogsModel) dft
								.GetResponseObject(josnbject,
										GoldBeanFinanceLogsModel.class);
						if (page == startPage) {
							setData(false, model.list);
						}else {
							setData(true, model.list);
						}
					}
				}, null);
	}

	private void setData(boolean isLoadmore, List<GoldBeanFinanceLogModel> lists) {
		if (!isLoadmore) {
			if (lists == null || lists.size() == 0) {
				lin_nodata.setVisibility(View.VISIBLE);
				tv_nodate.setText("暂无数据！");
				img_nodate.setImageResource(R.drawable.jxw_ku_142x180);
				mLoadMoreListView.setVisibility(View.GONE);
			} else {
				lin_nodata.setVisibility(View.GONE);
				mLoadMoreListView.setVisibility(View.VISIBLE);
				this.lists = lists;
				adapter = new GoldBeanRecoraListAdapter(lists, getActivity(),
						tab);
				mLoadMoreListView.setAdapter(adapter);
			}
			dialog.dismiss();
		} else {
			if (lists != null && lists.size() > 0) {
				this.lists.addAll(lists);
				adapter.notifyDataSetChanged();
			}
		}

		if (lists == null || (lists.size() <= Constants.PAGESIZE)) {
			mLoadMoreListView.setOnLoadMoreListener(null);
		} else {
			mLoadMoreListView.setOnLoadMoreListener(this);
		}
		mLoadMoreListView.onLoadMoreComplete();
	}

	@Override
	public void webLoadError() {
		dialog.dismiss();
	}

	@Override
	public void onLoadMore() {
		page++;
		getWebDate(page);
	}

}
