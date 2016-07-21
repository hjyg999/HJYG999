package com.md.hjyg.activities;

import java.util.ArrayList;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.HuoQibaoTransactionRecordAdapter;
import com.md.hjyg.entity.HuoQibaoTransactionModel;
import com.md.hjyg.entity.SingleLoanInvestModel;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.utility.Save;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;

/**
 * ClassName: HuoqiBaoTransactionRecordActivity date: 2015-11-13 上午8:41:09
 * remark:活期宝交易记录界面
 * 
 * @author pyc
 */
public class HuoqiBaoTransactionRecordActivity extends BaseActivity implements OnClickListener{

	// TextView
	private TextView  tv_no_of_record, tv_do_login,tv_right;
	// Linear Layout
	// ListView
	private ListView list_transaction_record;

	private HuoQibaoTransactionRecordAdapter transaction_record_adapter;
	// LayoutEntities

	private ProgressDialog pDialog;
	private Context mcontext;
	private ArrayList<SingleLoanInvestModel> lists = new ArrayList<SingleLoanInvestModel>();
//	private int total_no_of_records;
	private int huoqibaoID;
	// Entities
	private HuoQibaoTransactionModel huoQibaoTransactionModel;
	private ImageView img_bg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_transaction_record);
		mcontext = getBaseContext();
		findViews();
		init();
		colorText();
		huoqibaoID = getIntent().getIntExtra("huoqibaoID", 0);
		if (Constants.GetResult_AuthToken(mcontext).isEmpty()) {
			list_transaction_record.setVisibility(View.GONE);
			tv_do_login.setVisibility(View.VISIBLE);
			tv_do_login.setOnClickListener(this);
		}else {
			postSingleLoanInfoListWebservice();
		}
		
		Save.loadingImg(mHandler, mcontext, new int[]{R.drawable.home_huoqibao_bg});

	}

	private void colorText() {
		Spannable wordTwo = new SpannableString(" 登录 ");

		wordTwo.setSpan(new ForegroundColorSpan(Color.RED), 0,
				wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_do_login.setText(wordTwo);
		// to read the corporation
		Spannable wordThree = new SpannableString("后才能进行查看");

		wordThree.setSpan(new ForegroundColorSpan(Color.rgb(21, 222, 255)), 0,
				wordThree.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_do_login.append(wordThree);
	}

	private void init() {
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);
		HeaderViewControler.setHeaderView(this, "交易记录", this);
	}

	private void findViews() {
		// TextView
		tv_do_login = (TextView) findViewById(R.id.tv_do_login);
		tv_no_of_record = (TextView) findViewById(R.id.tv_no_of_record);
		tv_right = (TextView) findViewById(R.id.tv_right);
		img_bg = (ImageView) findViewById(R.id.img_bg);
		// LinearLayout
		// ListView
		list_transaction_record = (ListView) findViewById(R.id.list_transaction_record);
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Bitmap[] bitmaps =  (Bitmap[]) msg.obj;
				img_bg.setImageBitmap(bitmaps[0]);
				break;

			default:
				break;
			}
		};
	};


	public void postSingleLoanInfoListWebservice() {
		dft.postSingleLoanInfoList(huoqibaoID, Constants.SingleLoanInfo_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						huoQibaoTransactionModel = (HuoQibaoTransactionModel) dft
								.GetResponseObject(response,
										HuoQibaoTransactionModel.class);
						if (huoQibaoTransactionModel.notification.ProcessResult == 1) {
							setData();
						}else {
							Constants.showOkPopup(HuoqiBaoTransactionRecordActivity.this, getString(R.string.data_error), new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									if (Constants.dialog != null && Constants.dialog.isShowing()) {
										Constants.dialog.dismiss();
									}
									HuoqiBaoTransactionRecordActivity.this.finish();
									overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
									
								}
							});
						}

					}

				});
	}

	public void setData() {
//		list_transaction_record.setVisibility(View.VISIBLE);
//		tv_do_login.setVisibility(View.GONE);
//		lists = huoQibaoTransactionModel.singleLoanInvestList;
//		transaction_record_adapter = new HuoQibaoTransactionRecordAdapter(mcontext, lists);
//		list_transaction_record.setAdapter(transaction_record_adapter);
		
		if (Constants.GetResult_AuthToken(mcontext).isEmpty()) {
			list_transaction_record.setVisibility(View.GONE);
			tv_do_login.setVisibility(View.VISIBLE);
			tv_do_login.setOnClickListener(this);
		} else {
			list_transaction_record.setVisibility(View.VISIBLE);
			tv_do_login.setVisibility(View.GONE);
			lists = huoQibaoTransactionModel.singleLoanInvestList;
			transaction_record_adapter = new HuoQibaoTransactionRecordAdapter(mcontext, lists);
			list_transaction_record.setAdapter(transaction_record_adapter);
		}
//		total_no_of_records = lists.size();
		tv_no_of_record.setText("购买人");
		tv_right.setText("购买金额(元)");
		

	}

	public void dismissProgressDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}

	public void showProgressDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.tv_do_login:
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			overTransition(2);
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		this.finish();
		overTransition(1);
	}

}
