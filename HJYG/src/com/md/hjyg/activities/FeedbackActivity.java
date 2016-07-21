package com.md.hjyg.activities;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.FeedbackDetails;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.validators.EmptyValidator;
import com.md.hjyg.xinTouConstant.Constants;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * 意见反馈
 */
public class FeedbackActivity extends BaseActivity implements
		View.OnClickListener {
	// UI variables
	TextView tv_title, tv_register, tv_forgot_password;
	LinearLayout lay_back_investment;
	// LayoutEntities
	HeaderControler header;
	// Button
	Button btn_feedback_submit;
	// EditText
	EditText ed_feedback_username, ed_feedback_emailid, ed_comment;
	// ProgressDialog
	ProgressDialog progressDialog;

	Context mcontext;
	String emailid, username, feedback, statusMessage;
	int status;
//	public String Method_Name = "FeedbackLogApi/AddFeedback";

	FeedbackDetails feedbackDetails;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_feedback);
		mcontext = getBaseContext();
		findViews();
		init();
		lay_back_investment.setOnClickListener(this);
		btn_feedback_submit.setOnClickListener(this);
	}

	private void init() {

		header = new HeaderControler(this, true, false, "意见反馈",
				Constants.CheckAuthtoken(getBaseContext()));
		progressDialog = Constants.getProgressDialog(this);
		feedbackDetails = new FeedbackDetails();

	}

	private void findViews() {
		tv_forgot_password = (TextView) findViewById(R.id.tv_forgot_password);
		// LinearLayout
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
		// Button
		btn_feedback_submit = (Button) findViewById(R.id.btn_feedback_submit);
		// EditText
		ed_feedback_emailid = (EditText) findViewById(R.id.ed_feedback_emailid);
		ed_feedback_username = (EditText) findViewById(R.id.ed_feedback_username);
		ed_comment = (EditText) findViewById(R.id.ed_comment);
		// Please Enter Username
		addvalidator(new EmptyValidator(this,ed_feedback_username, "请输入用户名"));
		// Please Enter EmailId
		addvalidator(new EmptyValidator(this,ed_feedback_emailid, "请输入电子邮件标识"));
		// Please Enter Comment
		addvalidator(new EmptyValidator(this,ed_comment, "请输入评论"));
	}

	@Override
	public void onClick(View v) {
		if (v == lay_back_investment) {
//			Intent back = new Intent(FeedbackActivity.this,
//					AboutUsActivity.class);
//			startActivity(back);
			FeedbackActivity.this.finish();
			overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
		} else if (v == btn_feedback_submit) {
			getData();
			if (validateForm()) {
				callFeedbackWebserviceAPI(emailid, username, feedback);
			}

		}
	}

	private void callFeedbackWebserviceAPI(String emailid, String username,
			String feedback) {

		dft.getFeedbackDetails(username, emailid, feedback, Constants.AddFeedback_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						feedbackDetails = (FeedbackDetails) dft
								.GetResponseObject(response,
										FeedbackDetails.class);

						status = feedbackDetails.ProcessResult;
						statusMessage = feedbackDetails.ProcessMessage
								.toString();

						if (status == 1) {
							Constants.showOkPopup(FeedbackActivity.this,
									statusMessage,
									new View.OnClickListener() {

										@Override
										public void onClick(View v) {

//											Intent i = new Intent(
//													FeedbackActivity.this,
//													AboutUsActivity.class);
//											startActivity(i);
											if (Constants.dialog != null && Constants.dialog.isShowing()) {
												Constants.dialog.dismiss();
											}
											FeedbackActivity.this.finish();
											overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
										}
									});

						} else {
							Constants.showOkPopup(FeedbackActivity.this, statusMessage);
//							Constants.showOkPopup(FeedbackActivity.this,
//									statusMessage,
//									new DialogInterface.OnClickListener() {
//
//										@Override
//										public void onClick(
//												DialogInterface dialog,
//												int which) {
//
////											Intent i = new Intent(
////													FeedbackActivity.this,
////													FeedbackActivity.class);
////											startActivity(i);
//											dialog.dismiss();
//										}
//									});
						}
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Show error or whatever...
					}
				});
	}

	private void getData() {

		emailid = ed_feedback_emailid.getText().toString().replaceAll(" ", "");
		username = ed_feedback_username.getText().toString().replaceAll(" ", "");
		feedback = ed_comment.getText().toString().replaceAll(" ", "");

	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
//		Intent back = new Intent(FeedbackActivity.this, AboutUsActivity.class);
//		startActivity(back);
		FeedbackActivity.this.finish();
		overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
		
	}
}
