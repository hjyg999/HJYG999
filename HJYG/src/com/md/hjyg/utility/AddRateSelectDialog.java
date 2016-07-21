package com.md.hjyg.utility;

import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.AddRateDialogListAdapter;
import com.md.hjyg.entity.Notification;
import com.md.hjyg.entity.RedEnvelopeModel;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.Listener;

/**
 * ClassName: AddRateSelectDialog date: 2016-7-4 下午4:18:42 remark:加息券选择弹窗
 * 
 * @author pyc
 */
@SuppressLint("InflateParams")
public class AddRateSelectDialog implements OnItemClickListener {
	private Dialog mDialog;
	private List<RedEnvelopeModel> lists;
	private AddRateDialogListAdapter adapter;
	private TextView tv_income;
	public final static int BTN_ID = R.id.tv_btn;
	private int selectPosition = -1;
	private Activity activity;

	/**
	 * 
	 * @param context
	 * @param lists
	 * @param isShowhit
	 *            是否是购买之后的操作
	 */
	public AddRateSelectDialog(Activity context, List<RedEnvelopeModel> lists,
			OnClickListener listener, boolean isShowhit) {
		activity = context;
		this.lists = lists;
		mDialog = new Dialog(context, R.style.m_dialogstyle);
		View view = LayoutInflater.from(context).inflate(
				R.layout.layout_addratedialog, null);
		TextView tv_hit = (TextView) view.findViewById(R.id.tv_hit);
		TextView tv_btn = (TextView) view.findViewById(R.id.tv_btn);
		tv_btn.setOnClickListener(listener);
		tv_income = (TextView) view.findViewById(R.id.tv_income);
		if (isShowhit) {
			tv_hit.setVisibility(View.VISIBLE);
		} else {
			tv_hit.setVisibility(View.GONE);
		}

		LinearLayout lin_list = (LinearLayout) view.findViewById(R.id.lin_list);
		LinearLayout.LayoutParams params = (LayoutParams) lin_list
				.getLayoutParams();
		ListView mListView = (ListView) view.findViewById(R.id.mListView);
		adapter = new AddRateDialogListAdapter(this.lists, context);
		if (lists != null && lists.size() > 2) {
			View listItem = adapter.getView(0, null, mListView);
			listItem.measure(0, 0);
			int itemHeight = listItem.getMeasuredHeight();
			params.height = (int) (itemHeight * 2.5);
		} else {
			params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
		}
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);

		mDialog.setContentView(view);
		// 设置弹窗大小
		Window dialogWindow = mDialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = (int) (AppController.screenWidth * 0.8);
		// hight = h/3 - h/2;
		lp.y = AppController.screenWidth / 40;
		dialogWindow.setAttributes(lp);

		mDialog.show();
	}

	public void dismiss() {
		mDialog.dismiss();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		for (int i = 0; i < lists.size(); i++) {
			if (i == position) {
				lists.get(i).setSelect(!lists.get(i).isSelect());
			} else {
				lists.get(i).setSelect(false);
			}
		}
		adapter.notifyDataSetChanged();
		if (lists.get(position).isSelect()) {
			tv_income.setText("额外收益：" + lists.get(position).LoanInterest + "元");
			selectPosition = position;
		} else {
			tv_income.setText("额外收益：0.00元");
			selectPosition = -1;
		}
	}

	public int getSelectPosition() {
		return selectPosition;
	}

	public void setSelectPosition(int selectPosition) {
		this.selectPosition = selectPosition;
	}

	/**
	 * 选择相应红包增加红包使用记录接口
	 */
	public void postAddRedEnvelopeLog(final DataFetchService dft,
			final DialogProgressManager dialog, int InvestId, int Type,
			final DialogManager mDialog, final boolean finish) {
		if (selectPosition == -1) {// 没有选择红包
			Toast.makeText(activity, "请先选择您要使用的红包加息券！", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		dismiss();
		dialog.initDialog();
		final RedEnvelopeModel model = lists.get(selectPosition);
		dft.postAddRedEnvelopeLog(InvestId, model.Id, model.gid, Type,
				Constants.AddRedEnvelopeLog_URL, Request.Method.POST,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject jsonObject) {
						Notification notification = (Notification) dft
								.GetResponseObject(jsonObject,
										Notification.class);
						dialog.dismiss();
						if (notification.ProcessResult == 1) {
							if (finish) {
								mDialog.initOneBtnDialog(true, "选择成功",
										"结标后可获得额外的" + model.LoanInterest
												+ "元收益",
										new OnDismissListener() {

											@Override
											public void onDismiss(
													DialogInterface dialog) {
												activity.finish();
												activity.overridePendingTransition(
														R.anim.trans_lift_in,
														R.anim.trans_right_out);
											}
										});
							} else {
								mDialog.initOneBtnDialog(true, "选择成功",
										"截标后可获得额外的" + model.LoanInterest
												+ "元收益", new OnDismissListener() {
													
													@Override
													public void onDismiss(DialogInterface dialog) {
														Refresh();
													}
												});
							}
						} else {
							if (finish) {
								mDialog.initOneBtnDialog(false, "选择失败",
										"您可进入我的投资界面进行查看", null);
							}else  {
								mDialog.initOneBtnDialog(false, "选择失败",
										"操作失败，请重试！", null);
							}
						}
					}
				});
	}
	
	/**
	 * 刷新
	 */
	public void Refresh(){
		
	}

}
