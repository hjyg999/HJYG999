package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.layoutEntities.wheel.ChangeBirthDialog;
import com.md.hjyg.layoutEntities.wheel.ChangeBirthDialog.OnBirthListener;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/** 
 * ClassName: GoldBaoBeanSearchActivity 
 * date: 2016-1-26 上午8:59:14 
 * remark: 金豆明细查询界面--查询方式的选择
 * @author pyc
 */
public class GoldBaoBeanSearchActivity extends BaseActivity implements OnClickListener{
	
	private Activity mActivity;
	HeaderControler header;
	private View header_bottom_line;
	private Intent intent;
	/** 返回键 */
	private LinearLayout lay_back_investment;
	private TextView tv_month1,tv_month3,tv_month6;
	private TextView tv_startDate , tv_endDate;
	private String type = "";
	private Button btn_comfirm;
	/** 选择日期Dialog */
	private ChangeBirthDialog myDialog;
	
	private String newDate;
	private boolean choiceStartDate,choiceEndDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goldbaobeansearch_activity);
		mActivity = this;
		findViewandInit();
	}
	
	private void findViewandInit() {
		// 标题栏

		header = new HeaderControler(this, true, false, "查询",
				Constants.CheckAuthtoken(getBaseContext()));

		header_bottom_line = findViewById(R.id.header_bottom_line);
		header_bottom_line.setBackgroundColor(getResources().getColor(
				R.color.gray_line));
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
		lay_back_investment.setOnClickListener(this);
		
		tv_month1 = (TextView) findViewById(R.id.tv_month1);
		tv_month1.setOnClickListener(this);
		tv_month3 = (TextView) findViewById(R.id.tv_month3);
		tv_month3.setOnClickListener(this);
		tv_month6 = (TextView) findViewById(R.id.tv_month6);
		tv_month6.setOnClickListener(this);
		
		tv_startDate = (TextView) findViewById(R.id.tv_startDate);
		tv_startDate.setOnClickListener(this);
		tv_endDate = (TextView) findViewById(R.id.tv_endDate);
		tv_endDate.setOnClickListener(this);
		
		newDate = DateUtil.getNewDate();
		tv_startDate.setText(newDate);
		tv_endDate.setText(newDate);
		
		btn_comfirm = (Button) findViewById(R.id.btn_comfirm);
		btn_comfirm.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_back_investment:// 返回
			mActivity.finish();
			overTransition(1);
			break;
		case R.id.tv_month1:// 近1个月
		case R.id.tv_month3:// 近3个月
		case R.id.tv_month6:// 近6个月
			setBtnUI(v);
			break;
		case R.id.tv_startDate://起始日期
			searchDateDilog((TextView)v);
			break;
		case R.id.tv_endDate://结束日期
			searchDateDilog((TextView)v);
			break;
		case R.id.btn_comfirm:// 确认
			if (type.length() == 0) {//表示是按日查询
				if (!choiceEndDate || !choiceStartDate) {
					Constants.showOkPopup(mActivity, "请选择需要查询的时间段!");
					return;
				}
				if (!DateUtil.newTimeIsAfteroldTime(changeDateStyp(tv_endDate.getText().toString()),
						changeDateStyp(tv_startDate.getText().toString()))) {
					Constants.showOkPopup(mActivity, "请正确选择需要查询的时间段!");
					return;
				}
				type = tv_startDate.getText().toString() + tv_endDate.getText().toString();
			}
			intent = new Intent();
			intent.putExtra("type", type);
			setResult(1, intent);
			mActivity.finish();
			break;
		default:
			break;
		}
	}
	
	private String changeDateStyp(String str){
		str = str.replace("年", "-");
		str = str.replace("月", "-");
		str = str.replace("日", " ");
		str = str +"00:00:00" ;
		return str;
		
	}
	
	/***
	 * 根据选择的查询方式设置页面按钮的UI
	 */
	private void setBtnUI( View v){
		
		if (v == tv_month1) {
			type = "近1个月";
			tv_month1.setTextColor(getResources().getColor(R.color.red));
			tv_month1.setBackgroundResource(R.drawable.bg_mra_red_strok);
			tv_month3.setTextColor(getResources().getColor(R.color.gray_q));
			tv_month3.setBackgroundResource(R.drawable.bg_grad_strok);
			tv_month6.setTextColor(getResources().getColor(R.color.gray_q));
			tv_month6.setBackgroundResource(R.drawable.bg_grad_strok);
			
			tv_startDate.setTextColor(getResources().getColor(R.color.gray_q));
			tv_endDate.setTextColor(getResources().getColor(R.color.gray_q));
			choiceStartDate = false;
			choiceEndDate = false;
			
		}else if (v == tv_month3) {
			type = "近3个月";
			tv_month3.setTextColor(getResources().getColor(R.color.red));
			tv_month3.setBackgroundResource(R.drawable.bg_mra_red_strok);
			tv_month1.setTextColor(getResources().getColor(R.color.gray_q));
			tv_month1.setBackgroundResource(R.drawable.bg_grad_strok);
			tv_month6.setTextColor(getResources().getColor(R.color.gray_q));
			tv_month6.setBackgroundResource(R.drawable.bg_grad_strok);
			
			tv_startDate.setTextColor(getResources().getColor(R.color.gray_q));
			tv_endDate.setTextColor(getResources().getColor(R.color.gray_q));
			choiceStartDate = false;
			choiceEndDate = false;
		}else if (v == tv_month6) {
			type = "近6个月";
			tv_month6.setTextColor(getResources().getColor(R.color.red));
			tv_month6.setBackgroundResource(R.drawable.bg_mra_red_strok);
			tv_month3.setTextColor(getResources().getColor(R.color.gray_q));
			tv_month3.setBackgroundResource(R.drawable.bg_grad_strok);
			tv_month1.setTextColor(getResources().getColor(R.color.gray_q));
			tv_month1.setBackgroundResource(R.drawable.bg_grad_strok);
			
			tv_startDate.setTextColor(getResources().getColor(R.color.gray_q));
			tv_endDate.setTextColor(getResources().getColor(R.color.gray_q));
			choiceStartDate = false;
			choiceEndDate = false;
		}else {
			type = "";
			tv_month3.setTextColor(getResources().getColor(R.color.gray_q));
			tv_month3.setBackgroundResource(R.drawable.bg_grad_strok);
			tv_month1.setTextColor(getResources().getColor(R.color.gray_q));
			tv_month1.setBackgroundResource(R.drawable.bg_grad_strok);
			tv_month6.setTextColor(getResources().getColor(R.color.gray_q));
			tv_month6.setBackgroundResource(R.drawable.bg_grad_strok);
			if (v == tv_startDate) {
				choiceStartDate = true;
			}else {
				choiceEndDate = true;
			}
		}
	}
	
	/***
	 * 弹出显示日期的Dilog
	 */
	private void searchDateDilog(final TextView v){
		myDialog = new ChangeBirthDialog(this);
		myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题
		Window dialogWindow = myDialog.getWindow();
		dialogWindow.setWindowAnimations(R.style.Animcardtype);
		myDialog.setCanceledOnTouchOutside(false);
		myDialog.setStyle(true, true, true);
//		myDialog.setDate(2016, 01, 29);
		myDialog.setDate(0, 0, 0);
		myDialog.show();
		myDialog.setBirthdayListener(new OnBirthListener() {

			@Override
			public void onClick(String year, String month,String day) {
				
				v.setText(year + "年" + month + "月" + day +"日");
				v.setTextColor(getResources().getColor(R.color.red));
				
				setBtnUI(v);
			}
		});
	}
	
	
	@Override
	public void onBackPressed() {
		mActivity.finish();
		overTransition(1);
	}

}
