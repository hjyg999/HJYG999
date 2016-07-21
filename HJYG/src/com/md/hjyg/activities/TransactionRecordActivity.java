package com.md.hjyg.activities;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.TransactionRecordAdapter;
import com.md.hjyg.entity.TransactionListMain;
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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * 交易记录界面
 */
public class TransactionRecordActivity extends BaseActivity implements
		View.OnClickListener {
	
	private TextView  tv_no_of_record, tv_do_login,tv_right;
	// ListView
	private ListView list_transaction_record;

	private TransactionRecordAdapter transaction_record_adapter;
	// LayoutEntities
	private static final String TAG = "Transaction Details";

	private ProgressDialog pDialog;
	private Context mcontext;
	private int total_no_of_records;
	private String Intent_EncryptedID;
	// Entities
	private TransactionListMain transaction_list;
	private ImageView img_bg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_transaction_record);
		mcontext = getBaseContext();
		findViews();
		init();
		getIntentData();
		GetWebserviceTransactionDetailsAPI(mcontext);
		colorText();
		Save.loadingImg(mHandler, mcontext, new int[]{R.drawable.home_huoqibao_bg});

	}

	private void colorText() {
		
		Spannable wordTwo = new SpannableString(" 登录 ");

		wordTwo.setSpan(new ForegroundColorSpan(Color.RED), 0,
				wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_do_login.setText(wordTwo);
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
		transaction_list = new TransactionListMain();
		tv_right.setText("金额(元)");

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

	public void getIntentData() {
		Intent intent = getIntent();
		Intent_EncryptedID = intent.getStringExtra("EncrytedID");
	}

	public void GetWebserviceTransactionDetailsAPI(Context mcontext) {
		dft.getTransactionDetails(Constants.GetTransactionRecord_URL + Intent_EncryptedID,
				Request.Method.GET, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						Log.d(TAG, "" + response);

						transaction_list = (TransactionListMain) dft
								.GetResponseObject(response,
										TransactionListMain.class);

						setData();
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						Log.d(TAG, "" + volleyError);
					}

				}, null);
	}

	public void setData() {
		if (Constants.GetResult_AuthToken(mcontext).isEmpty()) {
			list_transaction_record.setVisibility(View.GONE);
			tv_do_login.setVisibility(View.VISIBLE);
			tv_do_login.setOnClickListener(this);
			transaction_record_adapter = new TransactionRecordAdapter(mcontext,
					transaction_list.TransactionList);
			list_transaction_record.setAdapter(transaction_record_adapter);
		} else {
			list_transaction_record.setVisibility(View.VISIBLE);
			tv_do_login.setVisibility(View.GONE);
			transaction_record_adapter = new TransactionRecordAdapter(mcontext,
					transaction_list.TransactionList);
			list_transaction_record.setAdapter(transaction_record_adapter);
		}
		total_no_of_records = transaction_list.TransactionList.size();
		tv_no_of_record.setText(String.valueOf( "共" + total_no_of_records + "条记录"));

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
			startActivity(intent );
			overTransition(2);
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		this.finish();
		overTransition(1);
	}
}
