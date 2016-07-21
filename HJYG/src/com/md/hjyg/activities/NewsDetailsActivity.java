package com.md.hjyg.activities;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.NewsDetails;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * 消息详情
 */
public class NewsDetailsActivity extends BaseActivity implements
		View.OnClickListener {

	private TextView tv_news_title, tv_sent_date, tv_news_details;
	private NewsDetails news_details;

	private int NewsId;
	private int pageIndex = 1;
	private String newsId, newsTitle, newsDetails, date;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_news_details);
		findViews();
		init();
		getIntentId();
		CallNewsDetailsWebservice(newsId, "" + pageIndex);
		tv_news_title.setOnClickListener(this);
	}

	public void getIntentId() {
		Intent intent = getIntent();
		NewsId = intent.getIntExtra("NewsID", 0);
		newsId = String.valueOf(NewsId);
	}

	private void init() {

		HeaderViewControler.setHeaderView(this, "消息详情", this);
		news_details = new NewsDetails();
	}

	private void findViews() {
		// TextView
		tv_news_title = (TextView) findViewById(R.id.tv_news_title);
		tv_news_details = (TextView) findViewById(R.id.tv_news_details);
		tv_sent_date = (TextView) findViewById(R.id.tv_sent_date);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			Intent back = new Intent(NewsDetailsActivity.this,
					NewsActivity.class);
			startActivity(back);
			overTransition(1);
			finish();
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		Intent back = new Intent(NewsDetailsActivity.this,
				NewsActivity.class);
		startActivity(back);
		overTransition(1);
		finish();
	}

	public void CallNewsDetailsWebservice(String newsId, String pageIndex) {

		dft.getNewsDetails(newsId, pageIndex, Constants.MessageDetails_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						news_details = (NewsDetails) dft.GetResponseObject(
								response, NewsDetails.class);

						SetUIData();

					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Show error or whatever...

					}
				});

	}

	public void SetUIData() {
		newsTitle = news_details.message.Title.toString();
		newsDetails = news_details.message.Contents.toString();
		date = news_details.message.CreateTime.toString();
		tv_news_title.setText(newsTitle);
		tv_news_details.setText(newsDetails.replace("<br />", "\n"));
		tv_sent_date.setText(DateUtil.changto(date, "yyyy/MM/dd HH:mm:ss"));
	}

}
