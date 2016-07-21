package com.md.hjyg.utils;

import org.json.JSONObject;

import com.md.hjyg.activities.HuoQibaoDetailsActivity;
import com.md.hjyg.activities.RegisterActivity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.webkit.JavascriptInterface;

/** 
 * ClassName: JsOperator 
 * date: 2016-1-29 上午11:19:31
 * remark:微信页面与app交互类
 * @author rw
 */
public class JsOperator {
	private Context context;  
	  
    public JsOperator(Context context) {  
        this.context = context;  
    }
    
    @JavascriptInterface
    public void ToRegister(){
    	Intent intent = new Intent();
    	intent.setClass(context, RegisterActivity.class);
    	context.startActivity(intent);
    }
    
    @JavascriptInterface
    public void ToHuoQiBaoDetails(){
    	Intent intent = new Intent();
    	intent.setClass(context, HuoQibaoDetailsActivity.class);
    	context.startActivity(intent);
    }
  
    /** 
     * 弹出消息对话框 
     */  
    @JavascriptInterface  
    public void showDialog(String message) {  
  
        AlertDialog.Builder builder = new Builder(context);  
        builder.setMessage(message);  
        builder.setTitle("提示");  
        builder.setPositiveButton("确认", new OnClickListener() {  
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
  
            }  
        });  
        builder.setNegativeButton("取消", new OnClickListener() {  
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
            }  
        });  
        builder.create().show();  
    }  
      
    /** 
     * 获取登录的用户名和密码 
     * @return JSON格式的字符串 
     */  
    @JavascriptInterface  
    public String getLoginInfo(){  
        try{  
            JSONObject login = new JSONObject();  
            login.put("Username", "YLD");  
            login.put("Password", "111");  
              
            return login.toString();  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
          
        return null;  
    }  

}
