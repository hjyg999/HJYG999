package com.md.hjyg.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.AccountInformationDetails;
import com.md.hjyg.entity.Notification;
import com.md.hjyg.layoutEntities.CircularImage;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

/***
 * 新增界面--个人信息界面
 */
public class PersonalInformationActivity extends BaseActivity implements
		OnClickListener {

	private CircularImage set_head_img;
	private TextView name, type, number, ifo_recommended;
//	HeaderControler header;
//	private LinearLayout lay_back_investment;
	private AccountInformationDetails account_info_details;
	private Context mcontext;
	private String localTempImageFileName;
	private static int TAKE_PICKTURE = 1;
	private static int RESULT_LOAD_IMAGE = 2;
	private RelativeLayout set_head;

	private String savefilename;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_personalinformation);
		mcontext = getBaseContext();
		findViewandInit();
		GetWebserviceVerifyAccountAPI(mcontext);
	}

	private void findViewandInit() {
		HeaderViewControler.setHeaderView(this, "个人信息", this);
		set_head_img = (CircularImage) findViewById(R.id.set_head_img);
		set_head_img.setOnClickListener(this);

//		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
//		lay_back_investment.setOnClickListener(this);

		set_head = (RelativeLayout) findViewById(R.id.set_head);
		set_head.setOnClickListener(this);

		name = (TextView) findViewById(R.id.name);
		type = (TextView) findViewById(R.id.type);
		number = (TextView) findViewById(R.id.number);
		ifo_recommended = (TextView) findViewById(R.id.ifo_recommended);

	}

	/** 获取个人信息 */
	private void GetWebserviceVerifyAccountAPI(Context mcontext) {

		dft.getVerifiyDetails(Constants.RealName_URL, Request.Method.GET,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						account_info_details = (AccountInformationDetails) dft
								.GetResponseObject(response,
										AccountInformationDetails.class);
						setUIData();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});
	}

	private void setUIData() {
		savefilename = account_info_details.RegName.toString() + ".jpg";
		
		String namel = "";
		if (account_info_details.RealName != null && account_info_details.RealName.length() > 0) {
			namel = account_info_details.RealName;
		}else if (account_info_details.RegName != null && account_info_details.RegName.length() > 0) {
			namel = account_info_details.RegName;
		}else {
			namel = "";
		}

		name.setText(namel);

		for (int i = 0; i < account_info_details.CardType.size(); i++) {
			if (account_info_details.CardType.get(i).Selected) {
				type.setText(account_info_details.CardType.get(i).Text);
				break;
			}
		}

		number.setText(account_info_details.IDNumber);

		if (account_info_details.RecommendName != null
				&& account_info_details.RecommendName.length() > 0) {
			ifo_recommended.setText(account_info_details.RecommendName);
		} else {
			ifo_recommended.setText("无");
		}

		// 判断是否已经设置了头像
		if (Save.isSaveBitmap(savefilename)) {
			Bitmap image = Save.getBitmap(savefilename);
			set_head_img.setImageBitmap(image);
			set_head_img.setVisibility(View.VISIBLE);
		} else {// 没有设置，用默认的图片
			// set_head_img.setImageResource(R.drawable.circle_head_mr);
			set_head_img.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			this.finish();
			overTransition(1);
			break;
		case R.id.set_head_img:
			showDialog();
			break;
		case R.id.set_head:
			showDialog();
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onBackPressed() {
		this.finish();
		overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
	}

	/** 现在不同的方式来设置头像 */
	@SuppressLint("NewApi")
	public void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this, 3);
		
		builder.setTitle("选择相片").setItems(R.array.choicepicture_method,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String status = Environment.getExternalStorageState();
						if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断SD卡是否正常
							if (which == 0) {// 拍照
								try {
									long filename = (DateUtil.getDate()).getTime();

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

									PersonalInformationActivity.this
											.startActivityForResult(intent,
													TAKE_PICKTURE);

								} catch (ActivityNotFoundException e) {
								}

							} else {// 从相册中获取
								Intent i = new Intent(
										Intent.ACTION_PICK,
										android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

								PersonalInformationActivity.this
										.startActivityForResult(i,
												RESULT_LOAD_IMAGE);

							}

						} else {
							Toast.makeText(mcontext, "SD卡异常，请检查SD卡", Toast.LENGTH_LONG).show();
						}

					}
				});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
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
			}else {
				Toast.makeText(PersonalInformationActivity.this, "数据读取异常，请检查您的内存卡！", Toast.LENGTH_LONG).show();
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
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 140);
		intent.putExtra("outputY", 140);
		intent.putExtra("return-data", true);
		this.startActivityForResult(intent, CLIP_PICTURE);
	}
	/**上传头像图片*/
	public void uploadPictures(final Bitmap bitmap){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] imgbyte = baos.toByteArray();
		String ImageBinary = Base64.encodeToString(imgbyte, Base64.DEFAULT); 
		
		dft.postHeadimg(ImageBinary, Constants.UpdateMemberImageBinaryById_URL, Request.Method.POST, 
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Notification notification = (Notification) dft
								.GetResponseObject(response,
										Notification.class);
						if (notification.ProcessResult == 1) {
							AppController.AccountInfIsChange = true;
							Toast.makeText(PersonalInformationActivity.this, "头像上传成功", Toast.LENGTH_SHORT).show();
							Save.saveBitmap(savefilename, bitmap);
							set_head_img.setImageBitmap(bitmap);
						}else {
							Toast.makeText(PersonalInformationActivity.this, "头像上传失败，请重试", Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

}
