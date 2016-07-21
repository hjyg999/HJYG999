package com.md.hjyg.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.ProjectDetailsAdapter;
import com.md.hjyg.entity.AssureList;
import com.md.hjyg.entity.ProjectDetails;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.LoadMoreListView;
import com.md.hjyg.xinTouConstant.Constants;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * 项目详情介绍
 */
public class ProjectDetailsActivity extends BaseListActivity implements
		View.OnClickListener {
	// UI variables
	//tv_do_login
	TextView tv_title, tv_header_title;
	LinearLayout lay_back_investment;
	// WebView
	//WebView webview_project_details;
	// LayoutEntities
	HeaderControler header;
	// DateFetchService
	DataFetchService dft;
	// Entities
	public ProjectDetails project_details;

	private ProgressDialog pDialog;
//	public String MethodName = "LoanApi/GetProjectDetails/";
	String Intent_EncryptedID;
	Context mcontext;
//	private static final String TAG = "Project Details";
	String proj_details;
	List<AssureList> AssureList = new ArrayList<AssureList>();
	ProjectDetailsAdapter project_details_adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_project_details);
		AppController.listActivity.add(this);
		mcontext = getBaseContext();
		findViews();
		init();
		getIntentData();
		
		GetWebserviceProjectDetailsAPI(mcontext);
		
		lay_back_investment.setOnClickListener(this);
		
//		if (Constants.GetResult_AuthToken(mcontext).isEmpty()) {
//			webview_project_details.setVisibility(View.GONE);
//			tv_do_login.setVisibility(View.VISIBLE);
//		} else {
//
//			GetWebserviceProjectDetailsAPI(mcontext);
//			tv_do_login.setVisibility(View.GONE);
//			webview_project_details.setVisibility(View.VISIBLE);
//		}
//		colorText();
		
		//webview_project_details.setVisibility(View.VISIBLE);
		
		//webview_project_details.getSettings().setJavaScriptEnabled(false);
	}

	private void init() {
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);
		dft = new DataFetchService(this);
		project_details = new ProjectDetails();
		header = new HeaderControler(this, true, false, "项目详情",
				Constants.CheckAuthtoken(getBaseContext()));
	}

	private void findViews() {
		// TextView
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_header_title = (TextView) findViewById(R.id.tv_header_title);
		//tv_do_login = (TextView) findViewById(R.id.tv_do_login);
		// LinearLayout
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
		// WebView
		//webview_project_details = (WebView) findViewById(R.id.webview_project_details);
	}

//	private void colorText() {
//		// Please
//		Spannable word = new SpannableString("请");
//
//		word.setSpan(new ForegroundColorSpan(Color.rgb(21, 222, 255)), 0,
//				word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//		tv_do_login.setText(word); // login
//		Spannable wordTwo = new SpannableString(" 登录 ");
//
//		wordTwo.setSpan(new ForegroundColorSpan(Color.RED), 0,
//				wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		tv_do_login.append(wordTwo);
//		// to read the corporation
//		Spannable wordThree = new SpannableString("阅读公司");
//
//		wordThree.setSpan(new ForegroundColorSpan(Color.rgb(21, 222, 255)), 0,
//				wordThree.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		tv_do_login.append(wordThree);
//	}

	public void getIntentData() {
		Intent intent = getIntent();
		Intent_EncryptedID = intent.getStringExtra("EncrytedID");
	}

	public void showProgressDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	@Override
	public void onClick(View v) {
		if (v == lay_back_investment) {
			ProjectDetailsActivity.this.finish();
			overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
		}
	}

	public void GetWebserviceProjectDetailsAPI(Context mcontext) {
		dft.getProjectDetails(Constants.GetProjectDetails_URL + Intent_EncryptedID,
				Request.Method.GET, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						project_details = (ProjectDetails) dft
								.GetResponseObject(response,
										ProjectDetails.class);

						setData();
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
					}

				}, null);
	}

	public void dismissProgressDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}

	public void setData() {
//        if(project_details == null || project_details.Details == null)
//        {
//        	Toast.makeText(this, "项目详情暂时没有发布", 1).show();
//        	return;
//        } 
		//proj_details = project_details.Details.toString();
		//webview_project_details.loadData(proj_details, "text/html", "UTF-8");//API提供的标准用法，无法解决乱码问题
		//webview_project_details.getSettings().setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
		//webview_project_details.loadData(proj_details, "text/html; charset=UTF-8", null);//这种写法可以正确解码

		//新版项目详情介绍
		
			((LoadMoreListView) getListView()).onLoadMoreComplete();
			if(project_details.AssureList.size() == 0)
			{
				((LoadMoreListView) getListView()).noDataLoad("项目详情暂时没有发布");
			}
		
		// Set adaptor
	    AssureList = project_details.AssureList;
	    project_details_adapter = new ProjectDetailsAdapter(mcontext, AssureList);
		setListAdapter(project_details_adapter);
	}

	@Override
	public void onBackPressed() {

		ProjectDetailsActivity.this.finish();
		overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AppController.listActivity.remove(this);
	}

}
