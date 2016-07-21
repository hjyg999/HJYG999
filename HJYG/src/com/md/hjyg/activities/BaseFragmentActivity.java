package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.utility.AppController;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.umeng.analytics.MobclickAgent;

/** 
 * ClassName: BaseFragmentActivity 
 * date: 2015-10-31 下午3:59:06 
 * remark:
 * @author pyc
 */
public class BaseFragmentActivity extends FragmentActivity {
	
	protected DataFetchService dft;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppController.listActivity.add(this);
		dft = new DataFetchService(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppController.listActivity.remove(this);
	}
	
	/**
     * 定义窗口进出方式
     * @param sty int型 1为左进右出，2为右进左出
     */
    public void overTransition(int sty)
    {
        switch (sty)
        {
        case 1:
            overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
            break;
        case 2:
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_lift_out);
            break;
        default:
            break;
        }
    }
    
    public DataFetchService getDft(){
    	return this.dft;
    }

}
