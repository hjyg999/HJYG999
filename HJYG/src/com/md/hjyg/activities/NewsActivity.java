package com.md.hjyg.activities;

import java.util.ArrayList;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.NewsAdapter;
import com.md.hjyg.adapter.NewsAdapter.CheckboxIscheckedListener;
import com.md.hjyg.entity.MessageList;
import com.md.hjyg.entity.NewsDetails;
import com.md.hjyg.entity.NewsStatusDetails;
import com.md.hjyg.entity.Notifications;
import com.md.hjyg.layoutEntities.HeaderView;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.LoadMoreListView;
import com.md.hjyg.xinTouConstant.Constants;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * 系统消息
 */
public class NewsActivity extends BaseListActivity implements
		View.OnClickListener, AdapterView.OnItemClickListener {

	HeaderView headerView;
	// Button
	Button btn_sign_success, btn_delete, btn_unread, btn_read, btn_checkAll;
	// ProgressDialog
	ProgressDialog progressDialog;
	// DataEntities
	NewsDetails news_details;
	NewsStatusDetails news_status;
	NewsAdapter news_adapter;
	Notifications notifications;
	DataFetchService dft;
	Context mcontext;
	Constants con;
	LinearLayout footer_ll;

	public static int pageIndex = 1;
	int pageSize = 9, newsId;
	boolean isLoading = false, isEditing = false, isAllCheck = false;
	int status;
	public String Loadmore = "", NewsId, statusMessage;

	public ArrayList<MessageList> messageList = new ArrayList<MessageList>();
	
	DialogManager dManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_news);
		AppController.listActivity.add(this);
		mcontext = getBaseContext();
		findViews();
		init();
		pageIndex = 1;
		CallNewsListWebservice("" + pageIndex, "" + pageSize);
	}

	private void init() {
		dft = new DataFetchService(this);
		news_details = new NewsDetails();
		news_status = new NewsStatusDetails();
		notifications = new Notifications();
		progressDialog = Constants.getProgressDialog(this);

	}

	private void findViews() {
		headerView = (HeaderView) findViewById(R.id.mheadView);
		btn_read = (Button) findViewById(R.id.btn_read);
		btn_delete = (Button) findViewById(R.id.btn_delete);
		btn_unread = (Button) findViewById(R.id.btn_unread);
		btn_checkAll = (Button) findViewById(R.id.btn_checkall);
		footer_ll = (LinearLayout) findViewById(R.id.footer_ll);
		dManager = new DialogManager(NewsActivity.this);
		this.getListView().setOnItemClickListener(this);
		btn_delete.setOnClickListener(this);
		btn_unread.setOnClickListener(this);
		btn_read.setOnClickListener(this);
		btn_checkAll.setOnClickListener(this);
	}

	OnCheckedChangeListener checkedListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			for (int i = 0; i < messageList.size(); i++) {
				messageList.get(i).isChecked = isChecked;
			}
			news_adapter.notifyDataSetChanged();
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_delete:
			deleteNewsListItems();
			break;
		case R.id.btn_read:
			readNewsListItems();
			break;
		case R.id.btn_unread:
			unreadNewsListItems();
			break;
		case R.id.img_top_left:
			this.finish();
			overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
			break;
		case R.id.tv_top_right:
			if (!isEditing) {
				footer_ll.setVisibility(View.VISIBLE);
				headerView.setViewUI(this, "我的消息", "取消");
				isEditing = true;
				news_adapter.setIsShowCheckBox(isEditing);
				news_adapter.notifyDataSetChanged();
			} else {
				footer_ll.setVisibility(View.GONE);
				headerView.setViewUI(this, "我的消息", "编辑");
				isEditing = false;
				news_adapter.setIsShowCheckBox(isEditing);
				news_adapter.notifyDataSetChanged();
			}
			break;
		case R.id.btn_checkall:
			if (!isAllCheck) {
				isEditing = true;
				isAllCheck = true;
				for (int i = 0; i < messageList.size(); i++) {
					messageList.get(i).isChecked = isAllCheck;
				}
				news_adapter.notifyDataSetChanged();
			} else {
				isAllCheck = false;
				isEditing = false;
				for (int i = 0; i < messageList.size(); i++) {
					messageList.get(i).isChecked = isAllCheck;
				}
				news_adapter.notifyDataSetChanged();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		this.finish();
		overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
	}

	// 删除消息
	private void deleteNewsListItems() {
		String newsIdListColanseperated = "";
		for (MessageList newsItem : messageList) {
			if (newsItem.isChecked) {
				if (newsIdListColanseperated.equalsIgnoreCase("")) {
					newsIdListColanseperated = "" + newsItem.Id;
				} else {
					newsIdListColanseperated = newsIdListColanseperated + ","
							+ newsItem.Id;
				}
			}
		}
		toDelete(newsIdListColanseperated);
	}
	// 删除消息操作
	private void toDelete(final String newsIdListColanseperated) {
		try {
			if (!newsIdListColanseperated.equals("")) {
				dManager.initTwoBtnDialog(new OnClickListener() {

					@Override
					public void onClick(View v) {
						switch (v.getId()) {
						case R.id.tv_dialog_cancel:
							dManager.dismiss();
							break;
						case R.id.tv_dialog_confirm:
							CallNewsDeleteWebservice(newsIdListColanseperated, "" + pageIndex,
									"" + pageSize);
							dManager.dismiss();
							break;
						default:
							break;
						}
					}
				});
				dManager.setTitleandContent("提示", "确认删除?");
				dManager.setButText("取消", "确定");
				dManager.Show();
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readNewsListItems() {
		String newsIdListColanseperated = "";

		for (MessageList newsItem : messageList) {
			if (newsItem.isChecked) {
				if (newsIdListColanseperated.equalsIgnoreCase("")) {
					newsIdListColanseperated = "" + newsItem.Id;
				} else {
					newsIdListColanseperated = newsIdListColanseperated + ","
							+ newsItem.Id;
				}
			}
		}

		try {
			CallNewsReadWebservice(newsIdListColanseperated, "" + pageIndex, ""
					+ pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void unreadNewsListItems() {
		String newsIdListColanseperated = "";

		for (MessageList newsItem : messageList) {
			if (newsItem.isChecked) {
				if (newsIdListColanseperated.equalsIgnoreCase("")) {
					newsIdListColanseperated = "" + newsItem.Id;
				} else {
					newsIdListColanseperated = newsIdListColanseperated + ","
							+ newsItem.Id;
				}
			}
		}
		try {
			CallNewsUnReadWebservice(newsIdListColanseperated, "" + pageIndex,
					"" + pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Webservice for Newslist
	 * */
	public void CallNewsListWebservice(String pageIndex, String pageSize) {

		dft.getNewsList(pageIndex, pageSize, Constants.Message_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						news_details = (NewsDetails) dft.GetResponseObject(
								response, NewsDetails.class);
						SetUIData("");
						NewsId = Constants.NewsID;
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});

	}

	public void SetUIData(String Loadmore) {
		if (Loadmore.isEmpty()) {
			if (news_details.MessageList.size() < 9) {
				((LoadMoreListView) getListView()).onLoadMoreComplete();
				if (news_details.MessageList.size() == 0) {
					this.getListView().setBackgroundColor(this.getResources().getColor(R.color.white));
					((LoadMoreListView) getListView()).noDataLoad("暂无数据");
					headerView.setViewUI(this, "我的消息", "");
				} else {
					headerView.setViewUI(this, "我的消息", "编辑");
				}
			} else {
				headerView.setViewUI(this, "我的消息", "编辑");
			}
			// Set adaptor
			messageList = news_details.MessageList;
			news_adapter = new NewsAdapter(mcontext, messageList);
			setListAdapter(news_adapter);

			news_adapter
					.SetCheckboxIscheckedListener(new CheckboxIscheckedListener() {
						@Override
						public void CheckboxIschecked(Boolean isAll) {
							// btn_checkBox.setOnCheckedChangeListener(null);
							// btn_checkBox.setChecked(isAll);
							// btn_checkBox
							// .setOnCheckedChangeListener(checkedListener);
						}
					});
		} else {
			if (!news_details.MessageList.isEmpty()) {
				messageList.addAll(news_details.MessageList);
				news_adapter.notifyDataSetChanged();
				((LoadMoreListView) getListView()).onLoadMoreComplete();
				isLoading = true;
				// btn_checkBox.setOnCheckedChangeListener(null);
				// btn_checkBox.setChecked(false);
				// btn_checkBox.setOnCheckedChangeListener(checkedListener);
			} else {
				((LoadMoreListView) getListView()).onLoadMoreComplete();
			}
		}

		if (news_details.MessageList.size() <= Constants.PAGESIZE) {
			((LoadMoreListView) getListView()).setOnLoadMoreListener(null);
			((LoadMoreListView) getListView()).onLoadMoreComplete();
		} else {
			((LoadMoreListView) getListView())
					.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
						public void onLoadMore() {
							pageIndex++;
							CallNewsListWebservice_Loadmore("" + pageIndex, ""
									+ pageSize);
						}
					});
		}

	}

	/**
	 * 加载更多
	 * */
	public void CallNewsListWebservice_Loadmore(String ftype, String page) {

		dft.getNewsList(ftype, page, Constants.Message_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						news_details = (NewsDetails) dft.GetResponseObject(
								response, NewsDetails.class);
						Loadmore = "loadmore";
						SetUIData(Loadmore);

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});
	}

	/**
	 * 标记为已读
	 * */
	public void CallNewsReadWebservice(String NewsId, String pageIndex,
			final String pageSize) {

		dft.getNewsRead(NewsId, pageIndex, pageSize, Constants.MessageRead_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						news_status = (NewsStatusDetails) dft
								.GetResponseObject(response,
										NewsStatusDetails.class);
						Loadmore = "loadmore";
						// SetUIData(Loadmore);
						status = news_status.Notifications.ProcessResult;
						statusMessage = news_status.Notifications.ProcessMessage
								.toString();

						if (status == 1) {
							dManager.initOneBtnDialog();
							dManager.setConfirmBitOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									dManager.dismiss();
									NewsActivity.pageIndex = 1;
									headerView.setViewUI(NewsActivity.this, "我的消息",
											"编辑");
									isEditing = false;
									footer_ll.setVisibility(View.GONE);
									CallNewsListWebservice("" + NewsActivity.pageIndex,
											"" + pageSize);
								}
							});
							dManager.setTitleandContent("提示", statusMessage);
							dManager.setButText("确定");
							dManager.Show();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Show error or whatever...
					}
				});
	}

	/**
	 * 删除我的消息
	 * */
	public void CallNewsDeleteWebservice(String NewsId, final String pageIndex,
			final String pageSize) {

		dft.getNewsDelete(NewsId, pageIndex, pageSize,
				Constants.MessageDelete_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						news_status = (NewsStatusDetails) dft
								.GetResponseObject(response,
										NewsStatusDetails.class);
						Loadmore = "loadmore";

						status = news_status.Notifications.ProcessResult;
						statusMessage = news_status.Notifications.ProcessMessage
								.toString();
						if (status == 1) {
							dManager.initOneBtnDialog();
							dManager.setConfirmBitOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									dManager.dismiss();
									NewsActivity.pageIndex = 1;
									headerView.setViewUI(NewsActivity.this,
											"我的消息", "编辑");
									isEditing = false;
									footer_ll.setVisibility(View.GONE);
									CallNewsListWebservice(""
											+ NewsActivity.pageIndex, ""
											+ pageSize);
								}
							});
							dManager.setTitleandContent("提示", statusMessage);
							dManager.setButText("确定");
							dManager.Show();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});
	}

	/**
	 * 标记为未读
	 * */
	public void CallNewsUnReadWebservice(String NewsId, String pageIndex,
			final String pageSize) {

		dft.getNewsUnRead(NewsId, pageIndex, pageSize,
				Constants.MessageUnread_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						news_status = (NewsStatusDetails) dft
								.GetResponseObject(response,
										NewsStatusDetails.class);
						Loadmore = "loadmore";
						// SetUIData(Loadmore);

						status = news_status.Notifications.ProcessResult;
						statusMessage = news_status.Notifications.ProcessMessage
								.toString();
						if (status == 1) {
							dManager.initOneBtnDialog();
							dManager.setConfirmBitOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									dManager.dismiss();
									NewsActivity.pageIndex = 1;
									headerView.setViewUI(
											NewsActivity.this, "我的消息",
											"编辑");
									isEditing = false;
									footer_ll.setVisibility(View.GONE);
									CallNewsListWebservice(""
											+ NewsActivity.pageIndex,
											"" + pageSize);
								}
							});
							dManager.setTitleandContent("提示", statusMessage);
							dManager.setButText("确定");
							dManager.Show();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (id >= 0) {
			// newsId = news_details.MessageList.get(position).Id;
			newsId = ((MessageList) news_adapter.getItem(position)).Id;
			NewsId = String.valueOf(newsId);
			// Log.d(TAG, "" + newsId);
			Intent news_details = new Intent(NewsActivity.this,
					NewsDetailsActivity.class);
			news_details.putExtra("NewsID", newsId);
			startActivity(news_details);
			overridePendingTransition(R.anim.trans_right_in, R.anim.trans_lift_out);
			readNewsListItems();
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppController.listActivity.remove(this);
	}

}
