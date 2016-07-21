package com.md.hjyg.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.md.hjyg.R;
import com.md.hjyg.entity.VersionDetails;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.ConfirmDialog;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.tmassistantsdk.common.TMAssistantDownloadSDKTaskState;
import com.tencent.tmassistantsdk.selfUpdateSDK.ITMSelfUpdateSDKListener;
import com.tencent.tmassistantsdk.selfUpdateSDK.TMSelfUpdateSDK;
import com.tencent.tmassistantsdk.selfUpdateSDK.TMSelfUpdateSDKUpdateInfo;
import com.umeng.analytics.MobclickAgent;

/**
 * 版本更新处理类
 */
public class UpdateVersionManager {

	private static final String TAG = "UpdateManager";
	private Context mContext;

	private ConfirmDialog dialog;// 提示框

	// titile标头
	private String updateMsg = "快点下载哦！";

	// 传入的下载地址
	private String apkUrl;

	private Dialog noticeDialog;

	private Dialog downloadDialog;
	/* 路径 */
	private static final String savePath = "/sdcard/xintouapp/";

	private static final String saveFileName = savePath + "XintouRelease.apk";

	/* 进度 */
	private ProgressBar mProgress; // 进度

	private static final int DOWN_UPDATE = 1;

	private static final int DOWN_OVER = 2;

	private int progress;

	private Thread downLoadThread;

	private boolean interceptFlag = false;

	private VersionDetails versionDetails = null;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				Log.i(TAG, "progress--  " + progress);
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:
				// 开始安装APK
				downloadDialog.dismiss();
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public UpdateVersionManager(Context context, String url,
			VersionDetails versionDetails) {
		this.mContext = context;
		this.apkUrl = url;
		this.versionDetails = versionDetails;
	}

	public void checkUpdateInfo() {
		showNoticeDialog();
	}

//	private void showNoticeDialog() {
//		final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//				mContext, R.style.updateDialog);
//		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
//		View convertView = (View) inflater.inflate(
//				R.layout.layout_version_update, null);
//		TextView tip1 = (TextView) convertView.findViewById(R.id.tip1);
//		String sureStr = "";
//		String cancelStr = "";
//		if (versionDetails.upgrade != null
//				&& versionDetails.upgrade.equals("false")) {
//			tip1.setText("1,您已更新至最新版本V" + versionDetails.strVersion);
//			sureStr = "确定";
//			cancelStr = "取消";
//		} else {
//			// tip1.setText("1,您有最新版本可供下载V" + versionDetails.strVersion);
//			tip1.setText(versionDetails.Description);
//			sureStr = "现在下载";
//			cancelStr = "下次再说";
//		}
//		alertDialog.setView(convertView);
//		alertDialog.setTitle("版本更新");
//		alertDialog.setPositiveButton(sureStr,
//				new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						showDownloadDialog();// 更新
//					}
//				});
//		alertDialog.setNegativeButton(cancelStr,
//				new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						alertDialog.setCancelable(true);
//
//					}
//				});
//
//		alertDialog.show();
//	}
	
	@SuppressLint("InflateParams")
	public void showNoticeDialog() {
		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setWindowAnimations(R.style.Animcardtype);
		dialog.setCanceledOnTouchOutside(!versionDetails.isForceUpdate);
		View View = LayoutInflater.from(mContext).inflate(
				R.layout.layout_versionupdate_dialogview, null);
		dialog.setContentView(View);
		//信息提示
		TextView dialog_msg = (TextView) View.findViewById(R.id.dialog_msg);
		//确定按钮
		TextView dialog_ok = (TextView) View.findViewById(R.id.dialog_ok);
		//取消按钮
		TextView dialog_cancel = (TextView) View.findViewById(R.id.dialog_cancel);
		//省流量更新按钮
		TextView save_update = (TextView) View.findViewById(R.id.save_update_tv);
		if (versionDetails.isForceUpdate) {
			dialog_cancel.setText("退出");
		}
		dialog_ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (dialog.isShowing()) {
					dialog.dismiss();
					showDownloadDialog();// 更新
				}
			}
		});
		//关闭按钮
		ImageView dialog_close = (ImageView) View.findViewById(R.id.dialog_close);
		dialog_close.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				if (versionDetails.isForceUpdate) {
					for (Activity activity : AppController.listActivity) {
						activity.finish();
					}

					MobclickAgent.onKillProcess(mContext);
					System.exit(0);
				}
			}
		});
		dialog_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				if (versionDetails.isForceUpdate) {
					for (Activity activity : AppController.listActivity) {
						activity.finish();
					}

					MobclickAgent.onKillProcess(mContext);
					System.exit(0);
				}
			}
		});
		save_update.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					TMSelfUpdateSDK sdk = TMSelfUpdateSDK.getInstance();
					sdk.initTMSelfUpdateSDK(mContext.getApplicationContext(), Constants.APP_ID, Constants.CHANNEL_ID, selfupdateListener);
					// 省流量更新之前，先判断有没有安装应用宝
					int yybInstalled = TMSelfUpdateSDK.getInstance().checkYYBInstalled();
					if (yybInstalled == TMAssistantDownloadSDKTaskState.UN_INSTALLED){
						// 没有安装应用宝，提示安装应用宝
//						Toast.makeText(mContext, "应用宝未安装，请先安装应用宝", Toast.LENGTH_SHORT).show();
						showDownYYBDialog();
					} else {
						// 安装了应用宝，直接进行更新
						TMSelfUpdateSDK.getInstance().startSaveUpdate(v.getContext());
					}
                } catch (Exception e) {
                    Log.e("CheckError", e.getMessage());
                }
			}
		});
		String msg = versionDetails.Description;
		if (msg.indexOf("\n1") != -1) {
			msg = msg.replaceFirst("\n1", "1");
		}
		
		dialog_msg.setText(msg);
		if (!((Activity) mContext).isFinishing()) {
			try {//抓捕异常，防止程序崩溃
				dialog.show();
			} catch (Exception e) {
			} 
		}
		
	}
	
	@SuppressLint("InflateParams")
	public void showDownYYBDialog() {
		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setWindowAnimations(R.style.Animcardtype);
		dialog.setCanceledOnTouchOutside(!versionDetails.isForceUpdate);
		View View = LayoutInflater.from(mContext).inflate(
				R.layout.layout_yybdowload_dialog, null);
		dialog.setContentView(View);
		//信息提示
		TextView dialog_msg = (TextView) View.findViewById(R.id.dialog_msg);
		//确定按钮
		TextView dialog_ok = (TextView) View.findViewById(R.id.dialog_ok);
		//取消按钮
		TextView dialog_cancel = (TextView) View.findViewById(R.id.dialog_cancel);
		if (versionDetails.isForceUpdate) {
			dialog_cancel.setText("退出");
		}
		dialog_ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (dialog.isShowing()) {
					dialog.dismiss();
					TMSelfUpdateSDK.getInstance().startSaveUpdate(v.getContext());
				}
			}
		});
		//关闭按钮
		ImageView dialog_close = (ImageView) View.findViewById(R.id.dialog_close);
		dialog_close.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		});
		dialog_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		});
		
		String msg = "您还没有下载应用宝，是否下载应用宝？";
		if (msg.indexOf("\n1") != -1) {
			msg = msg.replaceFirst("\n1", "1");
		}
		
		dialog_msg.setText(msg);
		if (!((Activity) mContext).isFinishing()) {
			try {//抓捕异常，防止程序崩溃
				dialog.show();
			} catch (Exception e) {
			} 
		}
		
	}

//	private void showDownloadDialog() {
//		AlertDialog.Builder builder = new Builder(mContext);
//		builder.setTitle("下载中,请稍候...");
//
//		final LayoutInflater inflater = LayoutInflater.from(mContext);
//		View v = inflater.inflate(R.layout.progress, null);
//		mProgress = (ProgressBar) v.findViewById(R.id.progress);
//
//		builder.setView(v);
//		builder.setNegativeButton("取消", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// MainActivity.genxinHandler.sendEmptyMessage(-1);
//				dialog.dismiss();
//				interceptFlag = true;
//			}
//		});
//		downloadDialog = builder.create();
//		downloadDialog.show();
//
//		downloadApk();
//	}
	
	public  void showDownloadDialog() {
		downloadDialog = new Dialog(mContext);
		downloadDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
		Window dialogWindow = downloadDialog.getWindow();
//		dialogWindow.setWindowAnimations(R.style.Animcardtype);
		downloadDialog.setCanceledOnTouchOutside(true);
		View View = LayoutInflater.from(mContext).inflate(
				R.layout.layout_downloaddialog_view, null);
		downloadDialog.setContentView(View);
		downloadDialog.setOnKeyListener(keylistener);
		//关闭按钮
		ImageView dialog_close = (ImageView) View.findViewById(R.id.dialog_close);
		mProgress = (ProgressBar) View.findViewById(R.id.progress);
		//确定按钮
		TextView dialog_ok = (TextView) View.findViewById(R.id.dialog_ok);
		
		dialog_ok.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (downloadDialog.isShowing()) {
						downloadDialog.dismiss();
						interceptFlag = true;
						if (versionDetails.isForceUpdate) {
							for (Activity activity : AppController.listActivity) {
								activity.finish();
							}
							MobclickAgent.onKillProcess(mContext);
							System.exit(0);
						}
					}
				}
		});
		
		dialog_close.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (downloadDialog.isShowing()) {
					downloadDialog.dismiss();
					interceptFlag = true;
				}
			}
		});
		if (!((Activity) mContext).isFinishing()) {
			try {//抓捕异常，防止程序崩溃
				downloadDialog.show();
				downloadApk();
			} catch (Exception e) {
			} 
		}
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);

				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestProperty("Accept-Encoding", "identity");
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdir();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 开始下载
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 读取完毕
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * 下载apk
	 * 
	 * @param url
	 */
	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();

	}

	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent();
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setAction(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
	
	/** 返回键监听，屏蔽掉按返回键Dialog消失 */
	private OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				return true;
			} else {
				return false;
			}
		}
	};
	
	// 下载应用宝监听事件
	private ITMSelfUpdateSDKListener selfupdateListener = new ITMSelfUpdateSDKListener(){

		@Override
		public void OnDownloadYYBStateChanged(String url, final int state,int errorCode, String errorMsg) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					String str = "完成";
					switch (state) {
					case TMAssistantDownloadSDKTaskState.DownloadSDKTaskState_DOWNLOADING:
						str = "下载中";
						break;
					case TMAssistantDownloadSDKTaskState.DownloadSDKTaskState_FAILED:
						str = "下载失败";
						break;
					case TMAssistantDownloadSDKTaskState.DownloadSDKTaskState_SUCCEED:
						str = "下载成功";
						break;
					case TMAssistantDownloadSDKTaskState.DownloadSDKTaskState_PAUSED:
						str = "暂停中";
						break;
					default:
						break;
					}
					
					Toast.makeText(mContext.getApplicationContext(), str, Toast.LENGTH_SHORT).show();
				}
			});
			
		}

		@Override
		public void OnDownloadYYBProgressChanged( final String url,final long receiveDataLen, final long totalDataLen) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					//Toast.makeText(getApplicationContext(), "sdk : channelId="+param.channelId+"; yybid="+param.yybAppId+"; url ="+url+"; receiveDataLen="+receiveDataLen+"; totalDataLen="+totalDataLen, Toast.LENGTH_SHORT).show();
//					Log.i(TAG, "sdk : url ="+url+"; receiveDataLen="+receiveDataLen+"; totalDataLen="+totalDataLen);
 				}
			});
			}

		@Override
		public void OnDownloadAppStateChanged( final int state, final int errorCode,final String errorMsg) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
						 //Toast.makeText(getApplicationContext(), "yyb state:channelId="+param.channelId+"; yybid="+param.yybAppId+"; state ="+state+"; errorCode="+errorCode+"; errorMsg="+errorMsg, Toast.LENGTH_SHORT).show();
//					Log.i(TAG, "yyb or app state: state ="+state+"; errorCode="+errorCode+"; errorMsg="+errorMsg);
				}
			});
		}

		@Override
		public void OnDownloadAppProgressChanged(final long arg0, final long arg1) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
				    //Toast.makeText(getApplicationContext(), "yyb state:channelId="+param.channelId+"; yybid="+param.yybAppId+"; state ="+state+"; errorCode="+errorCode+"; errorMsg="+errorMsg, Toast.LENGTH_SHORT).show();
//					Log.i(TAG, "yyb or app OnDownloadAppProgressChanged: rec ="+arg0+"; total="+arg1);
				}
			});
 		}

		@Override
		public void OnCheckNeedUpdateInfo(TMSelfUpdateSDKUpdateInfo arg0) {
//			Log.i(TAG, "OnCheckNeedUpdateInfo: NewApkSize ="+arg0.getNewApkSize()+"; PatchSize="+arg0.getPatchSize()+"; status="+arg0.getStatus()+";UpdateMethod="+arg0.getUpdateMethod()+"UpdateDownloadUrl="+arg0.getUpdateDownloadUrl());
			try {
				TMSelfUpdateSDK.getInstance().downloadGenApk(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
 	};
	
}
