package com.md.hjyg.fragment;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.activities.AboutXintouActivity;
import com.md.hjyg.activities.HomeFragmentActivity;
import com.md.hjyg.activities.InformationActivity;
import com.md.hjyg.activities.LoginActivity;
import com.md.hjyg.activities.NewsNoticeActivity;
import com.md.hjyg.activities.NoviceGuideActivity;
import com.md.hjyg.activities.RecommendedShareActivity;
import com.md.hjyg.activities.TaiPingYangCPICActivity;
import com.md.hjyg.adapter.InfListAdapter;
import com.md.hjyg.entity.InformationModel;
import com.md.hjyg.entity.MobileArticlesModel;
import com.md.hjyg.entity.MoreInfoModel;
import com.md.hjyg.layoutEntities.HeaderView;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.LoadingMenager;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * ClassName: HomeMoreFragment date: 2016-3-19 下午3:22:31 remark: 新版更多界面
 * 
 * @author pyc
 */
public class HomeMoreFragment extends Fragment implements OnClickListener {

	private HeaderView mheadView;
	private int[] Rid = { R.drawable.shuju, R.drawable.more_qd,
			R.drawable.more_xszy, R.drawable.more_logo, R.drawable.more_laba,
			R.drawable.more_adv_img, R.drawable.more_laba_hashot,
			R.drawable.more_frend_img, R.drawable.aliyun };
	private Bitmap[] imgBitmaps;
	private LinearLayout lin_sj, lin_abouas, lin_news_notice, lin_xszy;
	private ImageView img_qd, img_xszy, more_logo, img_laba, img_hd1, img_hd2,
			img_hd3;
	private TextView more_days_tv, more_amount_tv, more_percent_tv;
	private Intent intent;
	private HomeFragmentActivity mActivity;
	private MoreInfoModel moreInfoModel;
	private ScrollView mScrollView;
	private LoadingMenager loadingMenager;
	private DialogManager dialogManager;
//	private LinearLayout lin_info;
	private ListView mListView;
	private List<InformationModel> lists;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_homemore_layout, container,
				false);
		findViewandInit(v);
		loadingMenager = new LoadingMenager(getActivity(),
				v.findViewById(LoadingMenager.Root_ID), this);
		Save.loadingImg(mActivity.getmHandler(), getActivity(), Rid, 405);
		moreInfoModel = mActivity.getWebManager().getMoreInfoModel();
		if (moreInfoModel == null) {
			mActivity.getWebManager().GetMoreInfoWebService(false);
		} else {
			setViewWHandUI();
		}
		mActivity.getWebManager().GetMobileArticlesListofNews(0, 2, false);
		return v;
	}

	private void findViewandInit(View v) {
		mActivity = (HomeFragmentActivity) getActivity();

		// 设置头部标题栏
		mheadView = (HeaderView) v.findViewById(R.id.mheadView);
		mheadView.setUIData("更多", null);
		ViewParamsSetUtil.setViewHight(mheadView, 0.076f, getActivity());
		dialogManager = new DialogManager(mActivity);

		lin_sj = (LinearLayout) v.findViewById(R.id.lin_sj);
		lin_abouas = (LinearLayout) v.findViewById(R.id.lin_abouas);
		lin_abouas.setOnClickListener(this);
		lin_news_notice = (LinearLayout) v.findViewById(R.id.lin_news_notice);
		lin_news_notice.setOnClickListener(this);
		lin_xszy = (LinearLayout) v.findViewById(R.id.lin_xszy);
		lin_xszy.setOnClickListener(this);

		img_hd1 = (ImageView) v.findViewById(R.id.img_hd1);
		img_hd2 = (ImageView) v.findViewById(R.id.img_hd2);
		img_hd3 = (ImageView) v.findViewById(R.id.img_hd3);
		img_hd1.setOnClickListener(this);
		img_hd2.setOnClickListener(this);
		img_hd3.setOnClickListener(this);

		img_xszy = (ImageView) v.findViewById(R.id.img_xszy);
		more_logo = (ImageView) v.findViewById(R.id.more_logo);
		img_laba = (ImageView) v.findViewById(R.id.img_laba);
		img_qd = (ImageView) v.findViewById(R.id.img_qd);

		more_days_tv = (TextView) v.findViewById(R.id.more_days_tv);
		more_amount_tv = (TextView) v.findViewById(R.id.more_amount_tv);
		more_percent_tv = (TextView) v.findViewById(R.id.more_percent_tv);

		mScrollView = (ScrollView) v.findViewById(R.id.mScrollView);
		mScrollView.setVisibility(View.INVISIBLE);
		
		mListView = (ListView) v.findViewById(R.id.mListView);
		mListView.setFocusable(false);
		
//		tv_more = (TextView) v.findViewById(R.id.tv_more);
//		tv_more1 = (TextView) v.findViewById(R.id.tv_more1);
//		tv_more.setOnClickListener(this);
//		tv_more1.setOnClickListener(this);
		
//		lin_info = (LinearLayout) v.findViewById(R.id.lin_info);
//		lin_info.setOnClickListener(this);
//		line_ZX = (LinearLayout) v.findViewById(R.id.line_ZX);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_top_left:
			break;
		case R.id.lin_abouas:// 关于我们
			intent = new Intent(mActivity, AboutXintouActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
			break;
		case R.id.lin_news_notice:// 官方公告
			intent = new Intent(mActivity, NewsNoticeActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
			break;
		case R.id.lin_xszy:// 新手指引
			intent = new Intent(mActivity, NoviceGuideActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
			break;
		case R.id.img_hd1://
			mActivity.ChangeFragment(1);
			break;
		case R.id.img_hd2://
			inviteFriend();
			break;
		case R.id.img_hd3://
			intent = new Intent(mActivity, TaiPingYangCPICActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
			break;
//		case R.id.tv_more://更多资讯
//		case R.id.tv_more1:
//			intent = new Intent(mActivity, InformationActivity.class);
//			startActivity(intent);
//			mActivity.overTransition(2);
//			break;
//		case R.id.lin_info://资讯详情
//			intent = new Intent(mActivity, InformationDetailsActivity.class);
//			startActivity(intent);
//			mActivity.overTransition(2);
//			break;
		default:
			break;
		}
	}

	public void setBitmaps(Bitmap[] imgBitmaps) {
		this.imgBitmaps = imgBitmaps;
		setViewWHandUI();
	}

	public void setMoreInfoModel(MoreInfoModel moreInfoModel) {
		this.moreInfoModel = moreInfoModel;
		setViewWHandUI();
	}

	/**
	 * 设置控件的宽高和UI
	 */
	private void setViewWHandUI() {
		if (imgBitmaps != null && imgBitmaps.length > 0
				&& moreInfoModel != null) {
			int h = imgBitmaps[0].getHeight();
			int w = imgBitmaps[0].getWidth();
			ViewParamsSetUtil.setViewHandW_lin(lin_sj, h, w);
			ViewParamsSetUtil.setViewHandW_lin(lin_abouas, h / 2, w);

			img_qd.setImageBitmap(imgBitmaps[1]);
			img_xszy.setImageBitmap(imgBitmaps[2]);
			more_logo.setImageBitmap(imgBitmaps[3]);
			img_hd1.setImageBitmap(imgBitmaps[5]);
			img_hd2.setImageBitmap(imgBitmaps[7]);
			img_hd3.setImageBitmap(imgBitmaps[8]);

			if (moreInfoModel.isHotCount > 0) {
				img_laba.setImageBitmap(imgBitmaps[6]);
			} else {
				img_laba.setImageBitmap(imgBitmaps[4]);
			}
			more_days_tv.setText(moreInfoModel.days + "天");
			more_amount_tv.setText(moreInfoModel.amount);
			more_percent_tv.setText(moreInfoModel.percent);

			mScrollView.setVisibility(View.VISIBLE);
			loadingMenager.dismiss();
		}
	}
	
	/**
	 * 设置资讯信息
	 */
	public void setZXInf(final MobileArticlesModel model){
		if (model == null || model.list.size() == 0) {
			mListView.setVisibility(View.GONE);
			return;
		}
		lists = model.list;
		InfListAdapter adapter = new InfListAdapter(lists, getActivity(), InfListAdapter.more);
		mListView.setAdapter(adapter);
		Constants.setListViewHeightBasedOnChildren(mListView);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				intent = new Intent(mActivity, InformationDetailsActivity.class);
//				intent.putExtra("Contents", lists.get(position).Contents);
//				intent.putExtra("Title", lists.get(position).Title);
//				intent.putExtra("CreateTime", lists.get(position).CreateTime);
//				intent.putExtra("shareUrl", model.shareUrl + lists.get(position).Id);
//				intent.putExtra("media", lists.get(position).media);
//				startActivity(intent);
//				mActivity.overTransition(2);
				Intent intent = new Intent(mActivity,
						InformationActivity.class);
				mActivity.startActivity(intent);
				mActivity.overridePendingTransition(
						R.anim.trans_right_in, R.anim.trans_lift_out);
				
				
			}
		});
	}

	private void inviteFriend() {
		if (Constants.GetResult_AuthToken(mActivity).length() == 0) {
			dialogManager.initTwoBtnDialog("下次再说", "马上去", "提示", "您需要登录后才能邀请好友",
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							switch (v.getId()) {
							case DialogManager.CANCEL_BTN:
								dialogManager.dismiss();
								break;
							case DialogManager.CONFIRM_BTN:
								dialogManager.dismiss();
								AppController.isFromHomeActivity = true;
								intent = new Intent(mActivity,
										LoginActivity.class);
								startActivity(intent);
								mActivity.overTransition(2);
								break;

							default:
								break;
							}
						}
					});

		} else {
			intent = new Intent(mActivity, RecommendedShareActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
		}
	}
}
