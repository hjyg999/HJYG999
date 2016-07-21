package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.entity.AccountLoginDetails;
import com.md.hjyg.entity.HomeApiModel;
import com.md.hjyg.entity.MobileArticlesModel;
import com.md.hjyg.entity.MoreInfoModel;
import com.md.hjyg.fragment.HomeAccountFragment;
import com.md.hjyg.fragment.HomeGiftFragment;
import com.md.hjyg.fragment.HomeInvestFragment;
import com.md.hjyg.fragment.HomeMoreFragment;
import com.md.hjyg.fragment.HomeNologinFragment;
import com.md.hjyg.layoutEntities.FooterView;
import com.md.hjyg.layoutEntities.SlidingMenu;
import com.md.hjyg.layoutEntities.SlidingMenu.ScrollListener;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.HomeWebServiceManager;
import com.md.hjyg.utility.NoviceViewPagerManager;
import com.md.hjyg.utility.SlidingMenuManager;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

/**
 * ClassName: HomeFragmentActivity date: 2016-2-2 下午4:11:07 remark:
 * 
 * @author pyc
 */
public class HomeFragmentActivity extends BaseFragmentActivity implements
		OnClickListener, ScrollListener {

	private int tab;
	private FooterView mFooterView;
	private HomeInvestFragment homeInvestFragment;
	private HomeNologinFragment homeNologinFragment;
	private HomeAccountFragment homeAccountFragment;
	private HomeMoreFragment homeMoreFragment;
	// private HomeGiftFragment homeGiftFragment;
	private HomeGiftFragment homeGiftFragment;
	private FragmentManager fm;
	private Fragment[] fragments;
	private Context context;
	private Context applicationContext;
	private Intent intent;
	/** 是否有其他页面用了startActivity改变了frament */
	private boolean haveNewIntent;
	private LinearLayout mSlidingView;
	private SlidingMenu mSlidingMenu;
	private SlidingMenuManager slidingManager;
//	private HomeWebServiceManager webManager;
	/**
	 * 弹窗
	 */
	private RelativeLayout rel_pop;
//	private ImageView img_pop, img_pop_closs;
	private TextView tv_toinvst;
//	private Bitmap[] bitmaps;
	private HomeApiModel model;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homefragment_activity);
		context = getBaseContext();
		applicationContext = getApplicationContext();
		findViewandInit();
		setUIData();
//		Save.loadingImg(mHandler, context, new int[] { R.drawable.gift_home,
//				R.drawable.gift_close }, 401);
//		webManager.updateVersionWebservice(false);
	}
	
	/**
	 * 初始化
	 */
	private void findViewandInit() {
		fragments = new Fragment[5];

		mFooterView = (FooterView) findViewById(R.id.mFooterView);
		mFooterView.setOnClickmListener(this);
		mFooterView.setFooterTab(tab);

		mSlidingView = (LinearLayout) findViewById(R.id.mSlidingView);
		mSlidingMenu = (SlidingMenu) findViewById(R.id.mSlidingMenu);
		mSlidingMenu.setScrollListener(this);

//		webManager = new HomeWebServiceManager(this, mHandler, applicationContext);
		slidingManager = new SlidingMenuManager(mSlidingView, this);

		rel_pop = (RelativeLayout) findViewById(R.id.rel_pop);
		rel_pop.setOnClickListener(this);
//		img_pop = (ImageView) findViewById(R.id.img_pop);
//		img_pop_closs = (ImageView) findViewById(R.id.img_pop_closs);
		tv_toinvst = (TextView) findViewById(R.id.tv_toinvst);
		tv_toinvst.setOnClickListener(this);
//		img_pop_closs.setOnClickListener(this);
	}

	/**
	 * 退出我的账户
	 */
	public void signOut() {
		if (fragments[2] != null) {

			fm = getSupportFragmentManager();
			FragmentTransaction transaction = fm.beginTransaction();
			transaction.remove(fragments[2]);
			transaction.commitAllowingStateLoss();
			// transaction.commitAllowingStateLoss();
			homeAccountFragment = null;
			fragments[2] = null;
		}
		tab = 3;
		setUIData();
	}

	/**
	 * 设置与tab相关的Fragment
	 * */
	private void setFragment() {
		fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();

		if (fragments[tab] == null) {

			if (tab == 0) {
				if (homeInvestFragment == null) {
					homeInvestFragment = new HomeInvestFragment();
				}
				fragments[tab] = homeInvestFragment;

			} else if (tab == 1) {
				if (homeGiftFragment == null) {
					homeGiftFragment = new HomeGiftFragment();
				}
				fragments[tab] = homeGiftFragment;

			} else if (tab == 2) {
				if (homeAccountFragment == null) {
					homeAccountFragment = new HomeAccountFragment();
				}
				fragments[tab] = homeAccountFragment;

			} else if (tab == 3) {
				if (homeNologinFragment == null) {
					homeNologinFragment = new HomeNologinFragment();
				}
				fragments[tab] = homeNologinFragment;

			} else if (tab == 4) {
				if (homeMoreFragment == null) {
					homeMoreFragment = new HomeMoreFragment();
				}
				fragments[tab] = homeMoreFragment;
			}
			transaction.add(R.id.id_content, fragments[tab]);

		}
		for (int i = 0; i < fragments.length; i++) {
			if (fragments[i] != null) {
				if (i == tab) {
					transaction.show(fragments[i]);
				} else {
					transaction.hide(fragments[i]);
				}
			}
		}

		transaction.commitAllowingStateLoss();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lin_invest:// 理财
			if (tab != 0) {
				tab = 0;
				setUIData();
			}
			break;
		case R.id.lin_gift:// 有礼
			if (tab != 1) {
				tab = 1;
				setUIData();
			}
			break;
		case R.id.lin_account:// 我的资产
			choiceTab2();
			break;
		case R.id.lin_more:// 更多
			if (tab != 4) {
				tab = 4;
				setUIData();
			}
			break;
		case R.id.tv_toinvst:// 弹窗-去投资按钮
			if (Constants.GetResult_AuthToken(this).length() > 0) {
				tab = 2;
				setUIData();
			} else {
				intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
				overTransition(2);
			}
			rel_pop.setVisibility(View.GONE);
			break;
		case R.id.img_pop_closs:// 关闭弹窗
			rel_pop.setVisibility(View.GONE);
			break;

		default:
			break;
		}

	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HomeWebServiceManager.ACCOUNT_INF:
				if (homeAccountFragment != null) {
					homeAccountFragment
							.setUIData((AccountLoginDetails) msg.obj);
				}
				break;
			case HomeWebServiceManager.HOMAPI_INF:// 主页轮播图和理财信息
				model = (HomeApiModel) msg.obj;
				if (homeInvestFragment != null) {
					homeInvestFragment.setUIData(model);
				}
//				showPOP();
				break;
			case HomeWebServiceManager.ZeroActivity_INF:// 礼品标信息
				break;
			case HomeWebServiceManager.SendGift_INF:// 新手标和礼品标
//				if (homeGiftNewFragment != null) {
//					homeGiftNewFragment
//					.setNewUserSpecialistModel((SendGiftlistModel) msg.obj);
//				}
				break;
			case HomeWebServiceManager.MORE_INF:// 更多信息
				if (homeMoreFragment != null) {
					homeMoreFragment.setMoreInfoModel((MoreInfoModel) msg.obj);
				}
				if (homeNologinFragment != null) {
					homeNologinFragment
							.setMoreInfoModel((MoreInfoModel) msg.obj);
				}
				break;
			case 201:
//				if (homeGiftNewFragment != null) {
//					homeGiftNewFragment.setBitmaps((Bitmap[]) msg.obj);
//				}
				break;
			case 401:
//				bitmaps = (Bitmap[]) msg.obj;
//				showPOP();
				break;
			case 405:
				if (homeMoreFragment != null) {
					homeMoreFragment.setBitmaps((Bitmap[]) msg.obj);
				}
			case NoviceViewPagerManager.Handler_What:
//				if (homeGiftNewFragment != null) {
//					homeGiftNewFragment.setNovice_img_titile();
//				}
				break;
			case HomeWebServiceManager.ZX_INF://资讯
				if (homeMoreFragment != null) {
					homeMoreFragment.setZXInf((MobileArticlesModel) msg.obj);
				}
				break;
			case HomeWebServiceManager.UpdateV://资讯
				if (homeInvestFragment != null) {
					homeInvestFragment.showPop();
				}
				break;

			default:
				break;
			}
		};
	};

	/**
	 * 设置进人界面时显示的界面
	 */
	// public void setAuthTokenTab() {
	// if (Constants.GetResult_AuthToken(context).length() > 0) {// 已经登录
	// tab = 1;
	// } else {
	// tab = 0;
	// if (!isFrist) {
	// webManager.GetWebserviceHomeAPI(true);
	// isFrist = true;
	// }
	// }
	// setUIData();
	// }

	private void setUIData() {
		if (tab > 2) {
			mFooterView.setFooterTab(tab - 1);
		} else {
			mFooterView.setFooterTab(tab);
		}
		setFragment();
		mSlidingMenu.setTab(tab);
	}

	public void ChangeFragment(int tab) {
		this.tab = tab;
		setUIData();
	}

	/**
	 * 选择我的账户时 需要判断是否已经登录
	 * */
	private void choiceTab2() {
		if (Constants.GetResult_AuthToken(this).length() == 0) {
			tab = 3;
		}else {
			tab = 2;
		}
		
		setUIData();
		
	}

	public int getTab() {
		return this.tab;
	}

	/**
	 * 退出当前Fragment
	 */
	public void finishFragment() {
		if (tab == 0) {
			return;
		}
		if (fragments[tab] != null) {

			fm = getSupportFragmentManager();
			FragmentTransaction transaction = fm.beginTransaction();
			transaction.remove(fragments[tab]);
			// transaction.commit();
			transaction.commitAllowingStateLoss();
			fragments[tab] = null;
			if (tab == 1) {
				homeGiftFragment = null;
			} else if (tab == 2) {
				goneSlidingView();
				homeAccountFragment = null;
			} else if (tab == 4) {
				homeMoreFragment = null;
			}else if (tab == 3) {
				homeNologinFragment = null;
			}
		}
		tab = 0;
		setUIData();

	}

	/**
	 * 点击出现侧滑，并隐藏头像
	 */
	public void showSlidingView() {
		mSlidingMenu.smoothScrollTo(0, 0);
		mSlidingMenu.setIsShow(true);
		if (tab == 1) {
			// homeInvestFragment.getHeaderView().setheadViewGone();
		} else if (tab == 2) {
			homeAccountFragment.setHeadGone();
		}
	}

	/**
	 * 隐藏侧滑
	 */
	public void goneSlidingView() {
		mSlidingMenu.smoothScrollTo(mSlidingMenu.getmMenuWidth(), 0);
		mSlidingMenu.setIsShow(false);
//		if (homeInvestFragment != null && tab == 1) {
//			// homeInvestFragment.getHeaderView().setheadViewVISIBLE();
//		}
		if (homeAccountFragment != null && tab == 2) {
			homeAccountFragment.setHeadVISIBLE();
		}
	}

	@Override
	protected void onResumeFragments() {// 此方法可以避免状态丢失

		// 无效的用户名时的操作
		if (tab == 2 && Constants.GetResult_AuthToken(context).length() == 0) {
			signOut();
		}
		super.onResumeFragments();

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		slidingManager.onRestart();

		// 理财师申请成功
		if (AppController.isFromHomeActivity) {
			Constants.isFinancialplan(this, dft);
			AppController.isFromHomeActivity = false;
		} else {
			if (getIntent().getBooleanExtra("fromLogin", false)) {
				Constants.isFinancialplan(this, dft);
			}
		}

		if (haveNewIntent) {
			haveNewIntent = false;
			// 跳转到此页面时的操作
			intent = getIntent();
			int mtab = intent.getIntExtra("tab", tab);
			if (mtab != tab) {
				tab = mtab;
				if (tab > 4) {
					tab = 4;
				}
				
				if (tab == 2 || tab == 3) {
					choiceTab2();
				}else {
					setUIData();
				}
			}

		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);// Activity以singleTask模式启动，intent传值的解决办法
		// intent.putExtra("DateIsChange", true);
		haveNewIntent = true;
		setIntent(intent);
		getIntent().putExtras(intent);
		// getIntent().putExtra("DateIsChange", true);
	}

	private long exitTime = 0;

	/**
	 * 返回键监听，按两次返回键退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (mSlidingMenu.isShow()) {
				goneSlidingView();
				return true;
			}
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				for (Activity activity : AppController.listActivity) {
					activity.finish();
				}

				MobclickAgent.onKillProcess(this);
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void showSliding() {
		if (homeInvestFragment != null && tab == 0) {
			// homeInvestFragment.getHeaderView().setheadViewGone();
		}
		if (homeAccountFragment != null && tab == 2) {
			homeAccountFragment.setHeadGone();
		}

	}

	@Override
	public void goneSliding() {
		if (homeInvestFragment != null && tab == 0) {
			// homeInvestFragment.getHeaderView().setheadViewVISIBLE();
		}
		if (homeAccountFragment != null && tab == 2) {
			homeAccountFragment.setHeadVISIBLE();
		}
	}

	public HomeWebServiceManager getWebManager() {
		return null;
	}

	public Handler getmHandler() {
		return mHandler;
	}

	/**
	 * 显示弹窗，一天一次
	 */
//	@SuppressLint("SimpleDateFormat")
//	private void showPOP() {
//		if (bitmaps == null || model == null || !model.ZeroActionIsOpen) {
//			return;
//		}
//		img_pop.setImageBitmap(bitmaps[0]);
//		img_pop_closs.setImageBitmap(bitmaps[1]);
//		String openPopupWindowTime = Constants.GetOpenPopupWindowTime(this);
//		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.YYYY_MM_DD);
//		if (openPopupWindowTime == null || openPopupWindowTime.equals("")) {
//			// 第一次进入自动弹出窗口,并保存弹出的时间，控制一天只弹出一次
//			rel_pop.setVisibility(View.VISIBLE);
//			String perviousDate = sdf.format(new Date());
//			Constants.SetOpenPopupWindowTime(perviousDate, this);
//		} else {
//			// 如果当天已经弹出，则判断时间是否是第二天，如果是第二天再弹出一次
//			String nowTimeStr = sdf.format(new Date());
//			if (DateUtil.dateSubtraction(openPopupWindowTime, nowTimeStr) > 0) {
//				rel_pop.setVisibility(View.VISIBLE);
//				Constants.SetOpenPopupWindowTime(nowTimeStr, this);
//			}
//		}
//
//	}
	
	

}
