package com.md.hjyg.fragment;

import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.activities.BaseFragmentActivity;
import com.md.hjyg.activities.HomeFragmentActivity;
import com.md.hjyg.activities.HuoQibaoBuyActivity;
import com.md.hjyg.adapter.RedPacketAdapter;
import com.md.hjyg.adapter.RedPacketAdapter.UseRedEnvelopeListener;
import com.md.hjyg.entity.Notification;
import com.md.hjyg.entity.RedEnvelopeDetails;
import com.md.hjyg.entity.RedPacketList;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.services.DataFetchService.DftErrorListener;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.utility.LoadMoreListView;
import com.md.hjyg.utility.LoadMoreListView.OnLoadMoreListener;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;

/**
 * ClassName: MyRedPacketFragment date: 2016-5-10 下午4:12:30 remark:会员专享-我的红包
 * 
 * @author pyc
 */
public class MyRedPacketFragment extends BaseFragment implements
		OnLoadMoreListener, DftErrorListener, UseRedEnvelopeListener {
	private LoadMoreListView mLoadMoreListView;
	private LinearLayout lin_nodata;
	private DialogProgressManager dialog;
	private DataFetchService dft;
	private int tab;
	private final static int startPage = 1;
	private List<RedPacketList> lists;
	private RedPacketAdapter adapter;
	private int page = startPage;
	private String[] ftype = { "0", "1", "2" };
	private TextView tv_nodate;
	private ImageView img_nodate;
	private DialogManager dialogManager;

	public MyRedPacketFragment(int tab) {
		this.tab = tab;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_redpacket_layout,
				container, false);
		findViewandInit(v);
		dialog.initDialog();
		CallRedEnvelopeDetailsWebservice(ftype[tab], page);
		return v;
	}

	private void findViewandInit(View v) {
		dft = ((BaseFragmentActivity) getActivity()).getDft();
		dialog = new DialogProgressManager(getActivity(), "努力加载中...");
		dialogManager = new DialogManager(getActivity());
		dft.setOnDftErrorListener(this);

		lin_nodata = (LinearLayout) v.findViewById(R.id.lin_nodata);
		tv_nodate = (TextView) v.findViewById(R.id.tv_nodate);
		img_nodate = (ImageView) v.findViewById(R.id.img_nodate);
		mLoadMoreListView = (LoadMoreListView) v.findViewById(R.id.mListView);
	}

	public void CallRedEnvelopeDetailsWebservice(String ftype, final int page) {

		dft.getRedEnvelope(ftype, page + "", Constants.GetRedEnvelope_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						RedEnvelopeDetails redEnvelopeDetails = (RedEnvelopeDetails) dft
								.GetResponseObject(response,
										RedEnvelopeDetails.class);

						if (page == startPage) {
							setData(false, redEnvelopeDetails.List);
						} else {
							setData(true, redEnvelopeDetails.List);
						}

					}
				}, null);

	}

	private void setData(boolean isLoadmore, List<RedPacketList> lists) {
		if (!isLoadmore) {
			if (lists == null || lists.size() == 0) {
				lin_nodata.setVisibility(View.VISIBLE);
				if (tab == 0) {
					tv_nodate.setText("红包福利陆续来袭，敬请留意平台动态！");
					img_nodate.setImageResource(R.drawable.jxw_kx_142x180);
				} else {
					tv_nodate.setText("暂无数据！");
					img_nodate.setImageResource(R.drawable.jxw_ku_142x180);
				}
				mLoadMoreListView.setVisibility(View.GONE);
			} else {
				lin_nodata.setVisibility(View.GONE);
				mLoadMoreListView.setVisibility(View.VISIBLE);
				this.lists = lists;
				adapter = new RedPacketAdapter(lists, getActivity(), tab,this);
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
	public void onLoadMore() {
		page++;
		CallRedEnvelopeDetailsWebservice(ftype[tab], page);
	}

	@Override
	public void webLoadError() {
		dialog.dismiss();
	}

	@Override
	public void postUseRedEnvelope(final int p) {
		dialog.initDialog();
		dft.postUseRedEnvelope(lists.get(p).id, lists.get(p).pid, Constants.GetUseRedEnvelope_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						dialog.dismiss();
						Notification notification = (Notification) dft
								.GetResponseObject(response,
										Notification.class);
						if (notification.ProcessResult == 1) {
							AppController.AccountInfIsChange = true;
							if (lists.get(p).newType == RedPacketList.现金红包) {
								Intent intent = new Intent(getActivity(),
										HomeFragmentActivity.class);
								intent.putExtra("tab", 2);
								getActivity().startActivity(intent);
								getActivity().overridePendingTransition(
										R.anim.trans_lift_in,
										R.anim.trans_right_out);
							}else {
								startActivity(new Intent(getActivity(), HuoQibaoBuyActivity.class));
								getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_lift_out);
							}
						}else {
							dialogManager.initOneBtnDialog(notification.ProcessMessage, null);
						}

					}
				}, null);

	}
	
	public void reStartInit(boolean isShowDia){
		if (isShowDia) {
			dialog.initDialog();
		}
		page = startPage;
		CallRedEnvelopeDetailsWebservice(ftype[tab], page);
	}

}
