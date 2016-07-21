package com.md.hjyg.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.HappySmallWritingModel;
import com.md.hjyg.entity.MeetListDialogModel;
import com.md.hjyg.entity.MicroTextCompetitionLog;
import com.md.hjyg.entity.Notification;
import com.md.hjyg.entity.UpLoadMicroTextImgModel;
import com.md.hjyg.layoutEntities.HeaderView;
import com.md.hjyg.layoutEntities.wheel.AbstractWheelTextAdapter;
import com.md.hjyg.layoutEntities.wheel.OnWheelChangedListener;
import com.md.hjyg.layoutEntities.wheel.OnWheelScrollListener;
import com.md.hjyg.layoutEntities.wheel.WheelView;
import com.md.hjyg.services.DataFetchService.DftErrorListener;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.utility.ListDialogManager;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.utils.TextUtil;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.Listener;
import com.squareup.picasso.Picasso;

/**
 * ClassName: HappySmallWritingSubmitActivity date: 2016-7-13 上午11:17:28
 * remark:快乐微文提交界面
 * 
 * @author pyc
 */
public class HappySmallWritingSubmitActivity extends BaseActivity implements
		OnClickListener, DftErrorListener ,OnFocusChangeListener,TextWatcher{

	private HeaderView mheadView;
	private ImageView img_tx;
	private Dialog mDialog;
	private ArrayList<String> lists = new ArrayList<String>();
	private int minTextSize = 14;
	private String selectAge = 6 + "";
	private TextView tv_selsctAge, tv_Rule, tv_btn,tv_tx;
	private DialogProgressManager progressManager;
	private EditText ed_name, ed_School, ed_Introduction, ed_Title, ed_Text;
	private boolean isEdit;
	private String localTempImageFileName;
	private static int TAKE_PICKTURE = 1;
	private static int RESULT_LOAD_IMAGE = 2;
	private Context mContext;
	private HappySmallWritingModel model;
	/**
	 * 用户输入的信息
	 */
	private MicroTextCompetitionLog infoModel;
	private DialogManager dialog;
	private int focus;
	private LinearLayout lin_Titlehit,lin_Texthit;
	private TextView tv_Titlehit,tv_Texthit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_happysmallwritingsubmit_layout);
		mContext = getBaseContext();
		findView();

		setisEdit();
		progressManager.initDialog();
		getWebInfo();
	}

	private void findView() {
		mheadView = (HeaderView) findViewById(R.id.mheadView);
		mheadView.setViewUI(this, "快乐微文竞赛活动", "");
		mheadView.setRightImg(R.drawable.happysmallwriting_torule);
		progressManager = new DialogProgressManager(this, "数据加载中....");
		dialog = new DialogManager(this);

		img_tx = (ImageView) findViewById(R.id.img_tx);
		ViewParamsSetUtil.setViewParams(img_tx, 184, 240, true);

		tv_selsctAge = (TextView) findViewById(R.id.tv_selsctAge);
		tv_selsctAge.setOnClickListener(this);

		for (int i = 6; i < 16; i++) {
			lists.add(i + "");
		}

		ed_name = (EditText) findViewById(R.id.ed_name);
		ed_School = (EditText) findViewById(R.id.ed_School);
		ed_Introduction = (EditText) findViewById(R.id.ed_Introduction);
		ed_Title = (EditText) findViewById(R.id.ed_Title);
		ed_Text = (EditText) findViewById(R.id.ed_Text);
		tv_btn = (TextView) findViewById(R.id.tv_btn);
		tv_Rule = (TextView) findViewById(R.id.tv_Rule);
		tv_tx = (TextView) findViewById(R.id.tv_tx);
		
		lin_Titlehit = (LinearLayout) findViewById(R.id.lin_Titlehit);
		tv_Titlehit = (TextView) findViewById(R.id.tv_Titlehit);
		lin_Texthit = (LinearLayout) findViewById(R.id.lin_Texthit);
		tv_Texthit = (TextView) findViewById(R.id.tv_Texthit);
		
		tv_Rule.setOnClickListener(this);
		tv_btn.setOnClickListener(this);
		img_tx.setOnClickListener(this);

		infoModel = new MicroTextCompetitionLog();
		infoModel.MemberId = Integer.parseInt(Constants.GetMemberId(mContext));
		
		ed_Title.setOnFocusChangeListener(this);
		ed_Title.addTextChangedListener(this);
		ed_Text.setOnFocusChangeListener(this);
		ed_Text.addTextChangedListener(this);
	}

	/**
	 * 获取已经提交的信息和是否可编辑
	 */
	private void getWebInfo() {
		
		dft.getNetInfoById(Constants.HappyMicroTextInvolvement_URL,
				Request.Method.GET, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject josnbject) {
						progressManager.dismiss();
				 		model = (HappySmallWritingModel) dft.GetResponseObject(
								josnbject, HappySmallWritingModel.class);
						isEdit = model.isEdit;
						setisEdit();
						setData();
					}
				});
	}

	/**
	 * 设置是否可以编辑
	 */
	private void setisEdit() {
		img_tx.setEnabled(isEdit);
		ed_name.setEnabled(isEdit);
		tv_selsctAge.setEnabled(isEdit);
		ed_School.setEnabled(isEdit);
		ed_Introduction.setEnabled(isEdit);
		ed_Title.setEnabled(isEdit);
		ed_Text.setEnabled(isEdit);
		tv_btn.setEnabled(isEdit);
		if (isEdit) {
			tv_btn.setText("提交");
			tv_btn.setTextColor(getResources().getColor(R.color.red_E23924));
			tv_tx.setVisibility(View.VISIBLE);
		}else {
			tv_btn.setText("已提交");
			tv_btn.setTextColor(getResources().getColor(R.color.gray_sq));
			tv_tx.setVisibility(View.INVISIBLE);
		}
	}

	private void setData() {
		if (model == null || model.microTextCompetitionLog == null) {
			return;
		}
		MicroTextCompetitionLog logInfo = model.microTextCompetitionLog;
		// 显示头像
		if (!TextUtil.isEmpty(logInfo.Photo)) {
			Picasso.with(mContext).load(logInfo.Photo)
					.error(R.drawable.happysmallwriting_tx).into(img_tx);
		}
		if (!TextUtil.isEmpty(logInfo.Name)) {
			ed_name.setText(logInfo.Name);
		}
		if (!TextUtil.isEmpty(logInfo.Age + "")) {
			tv_selsctAge.setText(logInfo.Age + "岁");
		}
		if (!TextUtil.isEmpty(logInfo.School)) {
			ed_School.setText(logInfo.School);
		}
		if (!TextUtil.isEmpty(logInfo.Introduction)) {
			ed_Introduction.setText(logInfo.Introduction);
		}
		if (!TextUtil.isEmpty(logInfo.Title)) {
			ed_Title.setText(logInfo.Title);
		}
		if (!TextUtil.isEmpty(logInfo.Text)) {
			ed_Text.setText(logInfo.Text);
		}

	}

	/**
	 * 获取已经提交的信息和是否可编辑
	 */
	private void AddMicroTextCompetition() {
		progressManager.initDialog();
		dft.postAddMicroTextCompetition(infoModel.MemberId, infoModel.Photo,
				infoModel.Name, infoModel.Age, infoModel.School,
				infoModel.Title, infoModel.Introduction, infoModel.Text,
				infoModel.Status, infoModel.Score, infoModel.PraiseNumber,
				infoModel.CreateTime, Constants.AddMicroTextCompetition_URL,
				Request.Method.POST, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject josnbject) {
						Notification notification = (Notification) dft
								.GetResponseObject(josnbject,
										Notification.class);
						if (notification.ProcessResult == 1) {
							dialog.initOneBtnDialog(true, "提交成功！", null);
							getWebInfo();
						}else  {
							progressManager.dismiss();
							dialog.initOneBtnDialog(false, notification.ProcessMessage, null);
						}
					}
				}, null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderView.left_img_ID:
			onBackPressed();
			break;
		case R.id.tv_Rule:
		case HeaderView.rightimg_ID:
			startActivity(new Intent(this, HappySmallWritingRuleActivity.class));
			overTransition(2);
			break;
		case R.id.tv_selsctAge:
			selsctAgeDialog();
			break;
		case R.id.tv_dialog_confirm:
			if (selectAge == null || selectAge.length() == 0) {
				Toast.makeText(this, "请选择年龄！", Toast.LENGTH_SHORT).show();
			} else {
				if (mDialog != null) {
					mDialog.dismiss();
				}
				tv_selsctAge.setText(selectAge + "岁");
			}
			break;
		case R.id.tv_dialog_cancel:

			if (mDialog != null) {
				mDialog.dismiss();
			}
			break;
		case R.id.img_tx:
			choiceTypeDialog();
			break;
		case R.id.tv_btn:
			infoModel.Name = ed_name.getText().toString().trim();
			infoModel.Age = Integer.parseInt(selectAge);
			infoModel.School = ed_School.getText().toString().trim();
			infoModel.Title = ed_Title.getText().toString().trim();
			infoModel.Text = ed_Text.getText().toString().trim();
			infoModel.Introduction = ed_Introduction.getText().toString()
					.trim();
			if (TextUtil.isEmpty(infoModel.Name)) {
				dialog.initOneBtnDialog(false, "请输入姓名", null);
				return;
			}
			if (TextUtil.isEmpty(infoModel.Photo)) {
				dialog.initOneBtnDialog(false, "请上传头像", null);
				return;
			}
			if (TextUtil.isEmpty(tv_selsctAge.getText().toString().trim())) {
				dialog.initOneBtnDialog(false, "请选择年龄", null);
				return;
			}
			if (TextUtil.isEmpty(infoModel.Title)) {
				dialog.initOneBtnDialog(false, "请输入作文标题", null);
				return;
			}
			if (TextUtil.isEmpty(infoModel.Text)) {
				dialog.initOneBtnDialog(false, "请输入作文内容", null);
				return;
			}

			dialog.initTwoBtnDialog(R.drawable.tishi_bule_28x28, "取消", "确定",
					"提示", "信息提交后不能修改，确认提交？", new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							switch (v.getId()) {
							case DialogManager.CANCEL_BTN:
								if (dialog != null) {
									dialog.dismiss();
								}
								break;
							case DialogManager.CONFIRM_BTN:
								if (dialog != null) {
									dialog.dismiss();
								}
								AddMicroTextCompetition();
								break;

							default:
								break;
							}
						}
					});
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

	/** 选择证件类型 */
	private void choiceTypeDialog() {
		final List<MeetListDialogModel> lists = new ArrayList<MeetListDialogModel>();

		lists.add(new MeetListDialogModel("拍照", false));
		lists.add(new MeetListDialogModel("从相册中选择", false));
		ListDialogManager mListDialog = new ListDialogManager(this, "", lists);
		mListDialog.goneTitle();
		mListDialog.Show();
		mListDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				for (int i = 0; i < lists.size(); i++) {
					if (lists.get(i).isChoice()) {
						String status = Environment.getExternalStorageState();
						if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断SD卡是否正常
							if (i == 0) {// 拍照
								try {
									long filename = (DateUtil.getDate())
											.getTime();

									localTempImageFileName = String
											.valueOf(filename) + ".jpg";
									File filePath = AppController.FILE_PIC_SCREENSHOT;
									if (!filePath.exists()) {
										filePath.mkdirs();
									}
									Intent intent = new Intent(
											android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
									File f = new File(filePath,
											localTempImageFileName);
									Uri u = Uri.fromFile(f);
									intent.putExtra(MediaStore.EXTRA_OUTPUT, u);

									HappySmallWritingSubmitActivity.this
											.startActivityForResult(intent,
													TAKE_PICKTURE);

								} catch (ActivityNotFoundException e) {
								}

							} else {// 从相册中获取
								Intent intent = new Intent(
										Intent.ACTION_PICK,
										android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

								HappySmallWritingSubmitActivity.this
										.startActivityForResult(intent,
												RESULT_LOAD_IMAGE);

							}

						} else {
							Toast.makeText(
									HappySmallWritingSubmitActivity.this,
									"SD卡异常，请检查SD卡", Toast.LENGTH_LONG).show();
						}
						return;
					}
				}

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (TAKE_PICKTURE == requestCode) {
			try {// 拍照后得到图片进行裁剪
				File f = new File(AppController.FILE_PIC_SCREENSHOT,
						localTempImageFileName);
				startPhotoZoom(Uri.fromFile(f));
			} catch (Exception e) {
			}
		} else if (CLIP_PICTURE == requestCode) {
			if (data != null) {// 裁剪后得到的图片，直接赋值到控件上，并保存
				Bundle extras = data.getExtras();
				if (extras != null) {
					Bitmap photo = extras.getParcelable("data");
					uploadPictures(photo);

				}
			}
		} else if (requestCode == RESULT_LOAD_IMAGE
				&& resultCode == Activity.RESULT_OK && null != data) {
			// 从相册中得到的图片进行裁剪
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				// ImageView imageView = (ImageView) findViewById(R.id.imgView);
				// imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

				File f = new File(picturePath);
				startPhotoZoom(Uri.fromFile(f));
			} else {
				Toast.makeText(HappySmallWritingSubmitActivity.this,
						"数据读取异常，请检查您的内存卡！", Toast.LENGTH_LONG).show();
			}

		}
	}

	public int CLIP_PICTURE = 88;

	/** 裁剪图片 **/
	public void startPhotoZoom(Uri uri) {
		// 调用以上代码会跳转到Android系统自带的一个图片剪裁页面，点击确定之后就会得到一张图片。
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1.333);
		intent.putExtra("outputX", 184 * 2);
		intent.putExtra("outputY", 240 * 2);
		intent.putExtra("return-data", true);
		this.startActivityForResult(intent, CLIP_PICTURE);
	}

	/** 上传头像图片 */
	public void uploadPictures(final Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] imgbyte = baos.toByteArray();
		String ImageBinary = Base64.encodeToString(imgbyte, Base64.DEFAULT);

		dft.postHeadimg(ImageBinary, Constants.UpLoadMicroTextImg_URL,
				Request.Method.POST, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						UpLoadMicroTextImgModel imgModel = (UpLoadMicroTextImgModel) dft
								.GetResponseObject(response, UpLoadMicroTextImgModel.class);
						if (imgModel.notification.ProcessResult == 1) {
							Toast.makeText(
									HappySmallWritingSubmitActivity.this,
									"头像上传成功", Toast.LENGTH_SHORT).show();
							img_tx.setImageBitmap(bitmap);
							infoModel.Photo = imgModel.photo;
						} else {
							Toast.makeText(
									HappySmallWritingSubmitActivity.this,
									"头像上传失败，请重试", Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	@SuppressLint("InflateParams")
	private void selsctAgeDialog() {
		mDialog = new Dialog(this, R.style.m_dialogstyle);
		mDialog.setCanceledOnTouchOutside(true);
		View view = LayoutInflater.from(this).inflate(
				R.layout.dialog_happysmallwriting_layout, null);
		WheelView mWheelView = (WheelView) view.findViewById(R.id.mWheelView);
		TextView tv_dialog_cancel = (TextView) view
				.findViewById(R.id.tv_dialog_cancel);
		TextView tv_dialog_confirm = (TextView) view
				.findViewById(R.id.tv_dialog_confirm);
		tv_dialog_cancel.setOnClickListener(this);
		tv_dialog_confirm.setOnClickListener(this);
		int indx = getCurrentItem(selectAge);
		final AgeTextAdapter mYearAdapter = new AgeTextAdapter(this, lists,
				indx, 24, minTextSize);
		mWheelView.setViewAdapter(mYearAdapter);
		mWheelView.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mYearAdapter.getItemText(wheel
						.getCurrentItem());
				setTextviewSize(currentText, mYearAdapter);
				selectAge = currentText;
			}
		});
		mWheelView.setLable("岁");
		mWheelView.setVisibleItems(5);
		mWheelView.setCurrentItem(indx);
		mWheelView.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mYearAdapter.getItemText(wheel
						.getCurrentItem());
				setTextviewSize(currentText, mYearAdapter);
			}
		});

		mDialog.setContentView(view);
		ViewParamsSetUtil.setDialogPosition(mDialog);
		mDialog.show();
	}

	private class AgeTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected AgeTextAdapter(Context context, ArrayList<String> list,
				int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem,
					maxsize, minsize);
			this.list = list;
			setItemTextResource(R.id.tempValue);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index) + "";
		}
	}

	/**
	 * 设置字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSize(String curriteItemText, AgeTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(minTextSize);
				textvew.setTextColor(Color.RED);
			} else {
				textvew.setTextSize(minTextSize);
				textvew.setTextColor(this.getResources().getColor(R.color.gray));
			}
		}
	}

	private int getCurrentItem(String age) {
		if (age == null || age.length() == 0 || lists == null
				|| lists.size() == 0) {
			return 0;
		}
		for (int i = 0; i < lists.size(); i++) {
			if (lists.get(i).equals(age)) {
				return i;
			}
		}
		return 0;
	}

	@Override
	public void webLoadError() {
		progressManager.dismiss();
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.ed_Title:
			focus = 1;
			if (hasFocus) {
				lin_Titlehit.setVisibility(View.VISIBLE);
				tv_Titlehit.setText("您还可以输入"+ (12 - ed_Title.getText().toString().length()) +"个字符！");
			}else {
				lin_Titlehit.setVisibility(View.GONE);
			}
			break;
		case R.id.ed_Text:
			focus = 2;
			if (hasFocus) {
				lin_Texthit.setVisibility(View.VISIBLE);
				int len = ed_Text.getText().toString().length();
				if (len<=100) {
					tv_Texthit.setText("您已经输入"+ len +"个字符！");
					
				}else {
					tv_Texthit.setText("已超出"+ (ed_Text.getText().toString().length() -100) +"个字符！");
				}
			}else {
				lin_Texthit.setVisibility(View.GONE);
			}
			break;

		default:
			focus = 0;
			break;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (focus == 1) {
			tv_Titlehit.setText("您还可以输入"+ (12 - ed_Title.getText().toString().length()) +"个字符！");
		}else if (focus == 2) {
			int len = ed_Text.getText().toString().length();
			if (len<=100) {
				tv_Texthit.setText("您已经输入"+ len +"个字符！");
				
			}else {
				tv_Texthit.setText("已超出"+ (ed_Text.getText().toString().length() -100) +"个字符！");
			}
		}
	}

}
