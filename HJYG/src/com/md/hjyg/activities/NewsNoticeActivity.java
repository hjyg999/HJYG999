package com.md.hjyg.activities;

import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.NewsNoticeListAdapter;
import com.md.hjyg.entity.ArticlesPgedListModel;
import com.md.hjyg.entity.NewsNoticeDetails;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.LoadMoreListView;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Request;
import com.android.volley.Response;

/**
 * 官方公告
 */
public class NewsNoticeActivity extends BaseListActivity implements
		OnClickListener, OnItemClickListener {

	public String Loadmore = "";
	int page = 1;
	// public String Method_name ="Articles/GetArticles";
	DataFetchService dft;
	NewsNoticeDetails newsNoticeDetails;
	NewsNoticeListAdapter newsNoticeListAdapter;
	Context mcontext;
	List<ArticlesPgedListModel> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_newsnotice);
		AppController.listActivity.add(this);
		mcontext = getBaseContext();
		findViews();
		init();
		CallNewsNoticeWebservice(page + "");
	}

	protected void findViews() {

	}

	protected void init() {
		dft = new DataFetchService(this);
		HeaderViewControler.setHeaderView(this, "官方公告", this);
		// tv_title.setTextColor(Color.RED);
		this.getListView().setOnItemClickListener(this);
	}

	/** 获取系统消息公告列表 */
	public void CallNewsNoticeWebservice(final String page) {

		dft.getNewsNoticeList(page, Constants.GetArticles_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						newsNoticeDetails = (NewsNoticeDetails) dft
								.GetResponseObject(response,
										NewsNoticeDetails.class);
						if (Integer.parseInt(page) == 1) {
							SetUIData("");
						} else {
							SetUIData("Loadmore");
						}

					}

				}, null);

	}

	public void SetUIData(String Loadmore) {
		if (Loadmore.isEmpty()) {
			if (newsNoticeDetails.List.size() < 10) {
				((LoadMoreListView) getListView()).onLoadMoreComplete();
				if (newsNoticeDetails.List.size() == 0) {
					((LoadMoreListView) getListView()).noDataLoad("暂无数据");
				}
			}
			// Set adaptor
			list = newsNoticeDetails.List;
			newsNoticeListAdapter = new NewsNoticeListAdapter(mcontext, list);
			setListAdapter(newsNoticeListAdapter);
		} else {
			if (!newsNoticeDetails.List.isEmpty()) {
				list.addAll(newsNoticeDetails.List);
				newsNoticeListAdapter.notifyDataSetChanged();
				((LoadMoreListView) getListView()).onLoadMoreComplete();

			} else {
				((LoadMoreListView) getListView()).onLoadMoreComplete();
			}
		}

		if (newsNoticeDetails.List.size() <= Constants.PAGESIZE) {
			((LoadMoreListView) getListView()).setOnLoadMoreListener(null);
			((LoadMoreListView) getListView()).onLoadMoreComplete();

		} else {

			((LoadMoreListView) getListView())
					.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
						public void onLoadMore() {
							// Do the work to load more items at the end of list
							// here
							page++;
							CallNewsNoticeWebservice("" + page);
						}
					});
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			this.finish();
			overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		NewsNoticeActivity.this.finish();
		overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AppController.listActivity.remove(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, NewsNoticeDetailsActivity.class);
		intent.putExtra("Contents",
				newsNoticeListAdapter.getItem(position).Contents);
		intent.putExtra("Title", newsNoticeListAdapter.getItem(position).Title);
		intent.putExtra("CreateTime",
				newsNoticeListAdapter.getItem(position).CreateTime);
		startActivity(intent);
		overridePendingTransition(R.anim.trans_right_in, R.anim.trans_lift_out);
	}

}
