package com.md.hjyg.adapter;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.RedEnvelopeModel;
import com.md.hjyg.entity.RedEnvelopeType;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.ScreenUtils;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ClassName: AddRateDialogListAdapter date: 2016-7-4 下午2:39:09
 * remark:选择加息券弹窗列表适配器
 * 
 * @author pyc
 */
@SuppressLint("InflateParams")
public class AddRateDialogListAdapter extends MyBaseAdapter<RedEnvelopeModel> {
	private static final int 加息券 = 0;
	private static final int 红包 = 1;
	private int[] fram_Whs;
	private int[] imgok_Whs;
	private int[] imgtype_WHs;
	private int[] amount_WHs;
	private float textSize;
	private int bitW, size;

	public AddRateDialogListAdapter(List<RedEnvelopeModel> lists, Context context) {
		super(lists, context);
		fram_Whs = Save.getScaleBitmapWangH(520, 165);
		imgok_Whs = Save.getScaleBitmapWangH(82, 82);
		imgtype_WHs = Save.getScaleBitmapWangH(28, 28);
		amount_WHs = Save.getScaleBitmapWangH(114, 108);
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		textSize = amount_WHs[0] * 0.7f / (6) * 1.5f / fontScale;
		bitW = imgtype_WHs[1];
		size = (int) (ScreenUtils.sp2px(context, 12) * 1.4);
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		if (lists.get(position).Type == RedEnvelopeType.加息券) {
			return 加息券;
			
		}else {
			return 红包;
		}
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int type = getItemViewType(position);
		HoldView0 holdView0 = null;
		HoldView1 holdView1 = null;
		if (convertView == null) {  
	        switch(type) {  
	            case 加息券:  
	                convertView = inflater.inflate(R.layout.layout_addratedialog_listitem_type0, null);  
	                holdView0 = new HoldView0(convertView);  
	                convertView.setTag(holdView0);  
	                break;  
	            case 红包:  
	            	convertView = inflater.inflate(R.layout.layout_addratedialog_listitem_type2, null);  
	                holdView1 = new HoldView1(convertView);  
	                convertView.setTag(holdView1); 
	                break;  
	            default:  
	                break;  
	        }  
	    } else {  
	        switch(type) {  
	            case 加息券:  
	            	holdView0 = (HoldView0)convertView.getTag();  
	                break;  
	            case 红包:  
	            	holdView1 = (HoldView1)convertView.getTag();  
	                break;  
	            default:  
	                break;  
	        }  
	    }  
		RedEnvelopeModel model = lists.get(position);
		
		switch (type) {
		case 加息券:  
			holdView0.showSelect(model.isSelect());
			//利率
			holdView0.tv_rate.setText("+" + Constants.StringToCurrency(model.IncreaseRate +""));
			//期限
			holdView0.tv_time.setText("期限：" + model.DateTerm +  model.dType);
			//到期日期
			holdView0.tv_term.setText("到期日期：" + model.eTime);
			//使用条件
			holdView0.tv_condition.setText("使用条件：" + model.Remark);
			//加息券预期收益
			holdView0.tv_income.setText("加息券收益:" + model.LoanInterest + "元");
			if (position == lists.size()) {
				
			}else {
				
			}
            break;  
        case 红包:  
        	holdView1.showSelect(model.isSelect());
//        	if (model.Type == RedEnvelopeType.体验金红包
//    				|| model.Type == RedEnvelopeType.生日红包) {
//        		holdView1.img.setImageResource(R.drawable.redpacket_tyj);
//        		holdView1.tv_type.setText("体验金红包");
//    		} else if (model.Type == RedEnvelopeType.现金红包 ) {
//    			holdView1.img.setImageResource(R.drawable.redpacket_xj);
//    			holdView1.tv_type.setText("现金红包");
//    		} else {
//    			holdView1.img.setImageResource(R.drawable.redpacket_tz);
//    			holdView1.tv_type.setText("投资红包");
//    		}
        	String typetemp = "";
			int icon = 0;
			if (model.Type == RedEnvelopeType.体验金红包) {
                typetemp = "体验金红包";
                icon = R.drawable.redpacket_tyj;
            }
            else if (model.Type == RedEnvelopeType.生日红包) {
                typetemp = "生日红包";
                icon = R.drawable.redpacket_tyj;
            }
            else if (model.Type == RedEnvelopeType.现金红包) {
                icon = R.drawable.redpacket_xj;
                typetemp = "现金红包";
            }
            else if (model.Type == RedEnvelopeType.充值红包) {
                icon = R.drawable.redpacket_tz;
                typetemp = "充值红包";
            }
            else if (model.Type == RedEnvelopeType.注册红包) {
                icon = R.drawable.redpacket_tz;
                typetemp = "注册红包";
            }
            else {
                icon = R.drawable.redpacket_tz;
                typetemp = "投资红包";
            }
			holdView1.tv_type.setText(typetemp);
			holdView1.img.setImageResource(icon);
        	//投资红包金额
        	double amounttemp = model.RedEnvelopeAmount;
        	if (amounttemp > 100000) {
        		holdView1.tv_amount.setText(Constants.StringToCurrency(
    					Double.valueOf(amounttemp) / 10000 + "").replace(".00", "")
    					+ "万元");
    		} else {
    			holdView1.tv_amount.setText(Constants.StringToCurrency(amounttemp +"").replace(".00", "") + "元");
    		}
        	//到期日期
        	holdView1.tv_term.setText("到期日期：" + model.eTime);
			//使用条件
        	holdView1.tv_condition.setText("使用条件：" + model.Remark);
			//加息券预期收益
        	holdView1.tv_income.setText("红包收益:" + model.LoanInterest + "元");
            break;  
        default:  
            break;  
		}
		return convertView;
	}

	/**
	 * ClassName: HoldView0 date: 2016-7-4 下午2:46:03 remark:加息券
	 * 
	 * @author pyc
	 */
	class HoldView0 {
		FrameLayout mFrameLayout;
		View v_albg,v_line;
		ImageView img_ok;
		TextView tv_time, tv_term, tv_condition, tv_rate, tv_income;

		public HoldView0(View v) {
			mFrameLayout = (FrameLayout) v.findViewById(R.id.mFrameLayout);
			v_albg = v.findViewById(R.id.v_albg);
			v_line = v.findViewById(R.id.v_line);
			img_ok = (ImageView) v.findViewById(R.id.img_ok);
			tv_time = (TextView) v.findViewById(R.id.tv_time);
			tv_term = (TextView) v.findViewById(R.id.tv_term);
			tv_condition = (TextView) v.findViewById(R.id.tv_condition);
			tv_rate = (TextView) v.findViewById(R.id.tv_rate);
			tv_income = (TextView) v.findViewById(R.id.tv_income);

			// 设置控件的大小
			ViewParamsSetUtil.setViewHandW_lin(mFrameLayout, fram_Whs[1],
					fram_Whs[0]);
			ViewParamsSetUtil.setViewHandW_fra(img_ok, imgok_Whs[1],
					imgok_Whs[0]);
		}
		
		/**
		 * 显示的状态-是否选中
		 * @param isSelect 是否选中
		 */
		public void showSelect(boolean isSelect){
			if (isSelect) {
				img_ok.setVisibility(View.VISIBLE);
				v_albg.setVisibility(View.VISIBLE);
			}else {
				img_ok.setVisibility(View.GONE);
				v_albg.setVisibility(View.GONE);
			}
		}

	}

	/**
	 * ClassName: HoldView1 date: 2016-7-4 下午2:46:17 remark:红包
	 * 
	 * @author pyc
	 */
	class HoldView1 {
		FrameLayout mFrameLayout;
		View v_albg,v_line;
		ImageView img_ok, img;
		TextView tv_type, tv_term, tv_condition, tv_income, tv_amount;
		LinearLayout lin_amount;

		public HoldView1(View v) {
			mFrameLayout = (FrameLayout) v.findViewById(R.id.mFrameLayout);
			v_albg = v.findViewById(R.id.v_albg);
			v_line = v.findViewById(R.id.v_line);
			img_ok = (ImageView) v.findViewById(R.id.img_ok);
			img = (ImageView) v.findViewById(R.id.img);
			tv_type = (TextView) v.findViewById(R.id.tv_type);
			tv_term = (TextView) v.findViewById(R.id.tv_term);
			tv_income = (TextView) v.findViewById(R.id.tv_income);
			tv_condition = (TextView) v.findViewById(R.id.tv_condition);
			tv_amount = (TextView) v.findViewById(R.id.tv_amount);
			lin_amount = (LinearLayout) v.findViewById(R.id.lin_amount);

			// 设置控件的大小
			ViewParamsSetUtil.setViewHandW_lin(mFrameLayout, fram_Whs[1],
					fram_Whs[0]);
			ViewParamsSetUtil.setViewHandW_fra(img_ok, imgok_Whs[1],
					imgok_Whs[0]);
			ViewParamsSetUtil.setViewHandW_rel(lin_amount, amount_WHs[1],
					amount_WHs[0]);
			ViewParamsSetUtil.setViewHandW_rel(img, imgtype_WHs[1],
					imgtype_WHs[0]);

			tv_amount.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) img
					.getLayoutParams();
			params.height = bitW;
			params.width = bitW;
			if (size > bitW) {
				// img.setPadding(0, (size-bitW)/2, 0, 0);
				params.setMargins(0, (size - bitW) / 2, 0, 0);
			} else {
				tv_type.setPadding(0, (bitW - size) / 2, 0, 0);
			}
			img.setLayoutParams(params);
		}
		
		public void showSelect(boolean isSelect){
			if (isSelect) {
				img_ok.setVisibility(View.VISIBLE);
				v_albg.setVisibility(View.VISIBLE);
			}else {
				img_ok.setVisibility(View.GONE);
				v_albg.setVisibility(View.GONE);
			}
		}

	}

}
