package com.md.hjyg.activities;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response.Listener;
import com.md.hjyg.R;
import com.md.hjyg.adapter.AboutAsListAdapter;
import com.md.hjyg.entity.AboutUsInfoModel;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.xinTouConstant.Constants;

/**
 * 平台简介
 */
public class AboutXintouActivity extends BaseActivity implements
		View.OnClickListener {
	
	private ListView mListView;
	private AboutAsListAdapter adapter;
	private AboutUsInfoModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutas_layout);
		HeaderViewControler.setHeaderView(this, "关于我们", this);
		
		mListView = (ListView) findViewById( R.id.mListView);

		GetAboutUsInfo();

	}

	private void GetAboutUsInfo() {
		dft.getNetInfoById(Constants.GetAboutUsInfo_URL, Request.Method.GET,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						model = (AboutUsInfoModel) dft
								.GetResponseObject(response,
										AboutUsInfoModel.class);
						setDataUI();
					}
				});
	}
	
	private void setDataUI(){
		adapter = new AboutAsListAdapter(model.aboutUsDesModel, this);
		mListView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			this.finish();
			overTransition(1);
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		this.finish();
		overTransition(1);
	}
}
