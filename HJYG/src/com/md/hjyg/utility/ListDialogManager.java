package com.md.hjyg.utility;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.adapter.MyBaseAdapter;
import com.md.hjyg.entity.MeetListDialogModel;
import com.md.hjyg.utils.ScreenUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * ClassName: ListDialogManager date: 2016-3-9 下午4:39:06 remark:
 * 自定义带list列表的Dialog
 * 
 * @author pyc
 */
public class ListDialogManager {

	private Activity mActivity;
	private Dialog mDialog;
	private ListDialogAdapter adapter;
	private String title = "";
	private List<MeetListDialogModel> lists ;
	private Handler handler;
	private Runnable runnable;
	private TextView tv;
	

	public ListDialogManager(Activity mActivity, String title,
			List<MeetListDialogModel> lists) {
		this.mActivity = mActivity;
		handler = new Handler();
		this.title = title;
		this.lists = lists;
		adapter = new ListDialogAdapter(lists, mActivity);
		runnable = new Runnable() {
			
			@Override
			public void run() {
				dismiss();
			}
		};
		initListDialog();
	}

	@SuppressLint("InflateParams")
	public void initListDialog() {
		mDialog = new Dialog(mActivity, R.style.m_dialogstyle);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				mDialog = null;
			}
		});

		View view = LayoutInflater.from(mActivity).inflate(
				R.layout.dialog_list_red_layout, null);

		tv = (TextView) view.findViewById(R.id.tv_title);
		tv.setText(title);

		ListView mListView = (ListView) view.findViewById(R.id.mListView);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				handler.removeCallbacks(runnable);
				for (int i = 0; i < lists.size(); i++) {
					if (i == position) {
						lists.get(i).setChoice(true);
					}else {
						lists.get(i).setChoice(false);
					}
				}
				adapter.notifyDataSetChanged();
				handler.postDelayed(runnable, 30);
			}
		});

		mDialog.setContentView(view);

		Window dialogWindow = mDialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = (int) (ScreenUtils.getScreenWidth(mActivity) * 0.8);
		dialogWindow.setAttributes(lp);

	}
	
	/**
	 * 隐藏标题
	 */
	public void goneTitle(){
		if (tv != null) {
			tv.setVisibility(View.GONE);
		}
	}

	public void Show() {
		if (!mActivity.isFinishing() && mDialog != null) {
			try {// 抓捕异常，防止程序崩溃
				mDialog.show();
			} catch (Exception e) {
			}
		}
	}

	public void dismiss() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}

	/**
	 * 设置按钮消失之后的操作
	 * 
	 * @param listener
	 */
	public void setOnDismissListener(OnDismissListener listener) {
		if (mDialog != null) {
			mDialog.setOnDismissListener(listener);
		}
	}
	

	public class ListDialogAdapter extends MyBaseAdapter<MeetListDialogModel> {

		/**
		 * @param lists
		 * @param context
		 */
		public ListDialogAdapter(List<MeetListDialogModel> lists,
				Context context) {
			super(lists, context);
		}

		@SuppressLint({ "ViewHolder", "InflateParams" })
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = inflater
					.inflate(R.layout.meetlistdialog_view_layout, null);
			v.setTag(position);
			TextView tv_content = (TextView) v.findViewById(R.id.tv_content);
			TextView tv_time = (TextView) v.findViewById(R.id.tv_time);
			ImageView img = (ImageView) v.findViewById(R.id.img);
			MeetListDialogModel model = lists.get(position);
			tv_content.setText(model.getContent());
			if (model.getState() == 0 ) {
				tv_content.setTextColor(mActivity.getResources().getColor(R.color.gray_gold));
			}else if (model.getState() == 1) {
				tv_content.setTextColor(mActivity.getResources().getColor(R.color.red));
			}else if (model.getState() == 2) {
				tv_content.setTextColor(mActivity.getResources().getColor(R.color.gray_sq));
			}
			if (model.isChoice()) {
				img.setVisibility(View.VISIBLE);
			} else {
				img.setVisibility(View.GONE);
			}
			if (model.getDate() != null && model.getDate().length() > 0) {
				tv_time.setText(model.getDate());
				tv_time.setVisibility(View.VISIBLE);

			} else {

				tv_time.setVisibility(View.GONE);
			}
			
//			v.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					int p = (Integer) v.getTag();
//					for (int i = 0; i < lists.size(); i++) {
//						if (i == p) {
//							lists.get(p).setChoice(true);
//						}else {
//							lists.get(p).setChoice(false);
//						}
//					}
//					notifyDataSetChanged();
////					dismiss();
//				}
//			});
			return v;
		}

	}

}
