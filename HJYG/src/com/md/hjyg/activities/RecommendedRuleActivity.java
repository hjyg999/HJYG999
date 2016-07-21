package com.md.hjyg.activities;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.FinancialPlannerModel;
import com.md.hjyg.entity.FinancialPlannerModel.LevelInfo;
import com.md.hjyg.entity.FinancialPlannerModel.MemberFinancialPlannerInfo;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.utility.Save;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.Listener;

/**
 * ClassName: RecommendedRuleActivity date: 2015-12-7 上午11:54:42
 * remark:理财师--规则说明
 * 
 * @author pyc
 */
public class RecommendedRuleActivity extends BaseActivity implements
		OnClickListener {

	private LinearLayout lin_apply;
	private ImageView rul_gold, rul_silver;
	private Button btn_apply;
	private TextView apply_hit,leve1_name,leve2_name;
	/**生效条件*/
	private TextView silver_condition,gold_condition;
	/**备注/奖励发放*/
	private TextView tv_remark,tv_reward, silver_reward_twoLevel, gold_reward_twoLevel;
	/**奖励方式*/
	private TextView silver_reward,gold_reward,tv_level_change;
	private FinancialPlannerModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_recommendedrule_activity);

		findViewandInit();
		getWebService();
	}

	private void findViewandInit() {
		HeaderViewControler.setHeaderView(this, "规则说明", this);
		lin_apply = (LinearLayout) findViewById(R.id.lin_apply);

		btn_apply = (Button) findViewById(R.id.btn_apply);
		btn_apply.setOnClickListener(this);

		apply_hit = (TextView) findViewById(R.id.apply_hit);
		leve1_name = (TextView) findViewById(R.id.leve1_name);
		leve2_name = (TextView) findViewById(R.id.leve2_name);
		silver_condition = (TextView) findViewById(R.id.silver_condition);
		gold_condition = (TextView) findViewById(R.id.gold_condition);
		silver_reward = (TextView) findViewById(R.id.silver_reward);
		gold_reward = (TextView) findViewById(R.id.gold_reward);
		tv_remark = (TextView) findViewById(R.id.tv_remark);
		tv_reward = (TextView) findViewById(R.id.tv_reward);
		silver_reward_twoLevel = (TextView) findViewById(R.id.silver_reward_twoLevel);
		gold_reward_twoLevel = (TextView) findViewById(R.id.gold_reward_twoLevel);
		tv_level_change = (TextView) findViewById(R.id.tv_level_change);

		rul_gold = (ImageView) findViewById(R.id.rul_gold);
		rul_gold.setImageBitmap(Save.ScaleBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.rec_gold_level), this, 15));
		rul_silver = (ImageView) findViewById(R.id.rul_silver);
		rul_silver.setImageBitmap(Save.ScaleBitmap(BitmapFactory
				.decodeResource(getResources(), R.drawable.rec_silver_level),
				this, 15));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.btn_apply:
//			startActivity(new Intent(this, RecommendedApplyActivity.class));
			startActivityForResult(new Intent(this, RecommendedApplyActivity.class), 100);
			overTransition(2);
			break;
		default:
			break;
		}

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//当提交申请成功后
		if (requestCode == 100 && resultCode == 101) {
			apply_hit.setVisibility(View.VISIBLE);
			btn_apply.setText("审核中");
			btn_apply.setEnabled(false);
			btn_apply.setBackgroundResource(R.color.red);
		}
	}

	@Override
	public void onBackPressed() {
		this.finish();
		overTransition(1);
	}

	/** 获取理财师申请信息 */
	private void getWebService() {
		dft.getNetInfoById(Constants.FinancialPlanner_URL,
				Request.Method.GET, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject jsonObject) {
						model = (FinancialPlannerModel) dft
								.GetResponseObject(jsonObject,
										FinancialPlannerModel.class);
						if (model != null) {
							setDataUI();
						}
					}
				});
	}
	
	private void setDataUI(){
		MemberFinancialPlannerInfo info = model.MemberFinancialPlannerInfo;
		//设置申请按钮的状态
		if (info.IsFinancial) {//已是理财师，隐藏申请按钮
			lin_apply.setVisibility(View.GONE);
		}else {//不是理财师
			lin_apply.setVisibility(View.GONE);
			if (info.IsExamine) {//审核中
				apply_hit.setVisibility(View.VISIBLE);
				btn_apply.setText("审核中");
				btn_apply.setEnabled(false);
				btn_apply.setBackgroundResource(R.color.red);
			}else {
				apply_hit.setVisibility(View.GONE);
				if (info.IsApply) {//可以申请
					btn_apply.setText("申请理财师");
					btn_apply.setEnabled(true);
					btn_apply.setBackgroundResource(R.drawable.btn_selector_red);
				}else {
					btn_apply.setText("申请理财师");
					btn_apply.setEnabled(false);
					btn_apply.setBackgroundResource(R.color.gray_q);
				}
			}
		}
		
		//设置规则信息
		tv_remark.setText(model.RecRuleRemark);
		tv_reward.setText(model.RecRuleRewardDes);
		silver_condition.setText(model.RecRuleSliDes);
		silver_reward.setText(model.RecRuleRewardSliDes);
		gold_condition.setText(model.RecRuleGolDes);
		gold_reward.setText(model.RecRuleRewardGolDes);
		silver_reward_twoLevel.setText(model.RecRuleRewardTwoLevelSliDes);
		gold_reward_twoLevel.setText(model.RecRuleRewardTwoLevelGolDes);
		tv_level_change.setText(model.RecRuleLevelChangeDes);
		
		LevelInfo silver = info.TwoLevelInfo;
		leve1_name.setText(silver.name);
//		silver_condition.setText("1、自购"+(silver.OwnAmount+"").replace(".0", "")+"元以上;\n2、推荐"+silver.RecommendedTotal+
//				"位及以上好友成功购买过项目;\n3、全部好友的累计购买金额达"+(silver.FriendsBuyAmount/10000 +"").replace(".0", "")+
//				"万元及以上。\n(注：1+2或1+3任意条件满足其一即可)");
//		silver_reward.setText("一级好友奖励：好友的月返收益的"+(silver.Percent+"").replace(".0", "")+"%;\n二级好友奖励：好友的月返收益的"+(silver.TwoPercent+"").replace(".0", "")+"%。");
		LevelInfo gold = info.OneLevelInfo;
		leve2_name.setText(gold.name);
		//1、自购5万元以上；\n2、推荐3位及以上好友成功购买过项目；\n3、全部好友的累计购买金额达20万元及以上。\n（注：1+2或1+3任意条件满足其一即可）
//		gold_condition.setText("1、自购"+(gold.OwnAmount/10000+"").replace(".0", "")+"万元以上;\n2、推荐"+gold.RecommendedTotal+
//				"位及以上好友成功购买过项目;\n3、全部好友的累计购买金额达"+(gold.FriendsBuyAmount/10000 +"").replace(".0", "")+
//				"万元及以上。\n(注：1+2或1+3任意条件满足其一即可)");
		//一级好友奖励：好友的月返收益的15%;\n二级好友奖励：好友的月返收益的5%。
//		gold_reward.setText("一级好友奖励：好友的月返收益的"+(gold.Percent+"").replace(".0", "")+"%;\n二级好友奖励：好友的月返收益的"+(gold.TwoPercent+"").replace(".0", "")+"%。");
	}

}
