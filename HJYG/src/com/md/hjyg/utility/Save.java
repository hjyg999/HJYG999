package com.md.hjyg.utility;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.md.hjyg.R;
import com.md.hjyg.utils.ScreenUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;

public class Save
{
    
    /**SD卡中是否保存了图片**/
    public static boolean isSaveBitmap(String name)
    {
    	String status = Environment.getExternalStorageState();
    	if (!status.equals(Environment.MEDIA_MOUNTED)) {
    		//SD卡异常
			return false;
		}
        File file = AppController.FILE_PIC_SCREENSHOT;
        if (!file.exists())
        {
            return false;
        }
        File[] files = file.listFiles();
        if (files != null && files.length > 0)
        {
            for (File file2 : files)
            {
                if (file2.getName().equals(name))
                {
                    return true;
                }
            }
        }

        return false;

    }

    /**保存图片*/
    public static boolean saveBitmap(String name, Bitmap bitmap)
    {
        File file = AppController.FILE_PIC_SCREENSHOT;
        if (!file.exists())
        {
            file.mkdirs();
        }
        try
        {
            File filename = new File(file, name);
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(filename));

            boolean boi = bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.close();//必须关闭流，关闭即可刷新流，不然文件为空
            return boi;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    /**获取图片*/
    public static Bitmap getBitmap(String name)
    {
        Bitmap bitmap = BitmapFactory.decodeFile(AppController.FILE_PIC_SCREENSHOT +"/"+ name);
        return bitmap;

    }
    
    /**根据屏幕的宽度缩放图片*/
    public static Bitmap ScaleBitmap(Bitmap bitmap,Context context){
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int w = ScreenUtils.getScreenWidth(context);
		float scaleRate = (float)w/(float)width;
		Matrix matrix = new Matrix();
		matrix.postScale( scaleRate, scaleRate);
		Bitmap newIcon = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		return newIcon;
		
	}
    /**根据文字的高度缩放图片*/
    public static Bitmap ScaleBitmap(Bitmap bitmap,Context context,int sp){
    	int width = bitmap.getWidth();
    	int height = bitmap.getHeight();
    	int h = ScreenUtils.sp2px(context, sp);
    	float scaleRate = (float)h/(float)height;
    	Matrix matrix = new Matrix();
    	matrix.postScale( scaleRate, scaleRate);
    	Bitmap newIcon = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    	return newIcon;
    	
    }
    /**根据View占屏幕高度的比例缩放图片*/
    public static Bitmap ScaleBitmap(Bitmap bitmap,Context context,double dh){
    	int width = bitmap.getWidth();
    	int height = bitmap.getHeight();
    	int h = (int) (ScreenUtils.getScreenHeight(context)*dh);
    	float scaleRate = (float)h/(float)height;
    	Matrix matrix = new Matrix();
    	matrix.postScale( scaleRate, scaleRate);
    	Bitmap newIcon = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    	return newIcon;
    	
    }
    /**根据bitmap2图片缩放效果缩放图片bitmap1，bitmap2图片是通过屏幕宽度缩放的*/
    public static Bitmap ScaleBitmap(Bitmap bitmap1,Context context,Bitmap bitmap2){
    	int width = bitmap1.getWidth();
    	int width2 = bitmap2.getWidth();
    	int height = bitmap1.getHeight();
    	int w = ScreenUtils.getScreenWidth(context);
    	float scaleRate = (float)w/(float)width2;
    	Matrix matrix = new Matrix();
    	matrix.postScale( scaleRate, scaleRate);
    	Bitmap newIcon = Bitmap.createBitmap(bitmap1, 0, 0, width, height, matrix, true);
    	return newIcon;
    	
    }
    /**根据宽度缩放效果缩放图片bitmap1，bitmap2图片是通过屏幕宽度缩放的*/
    public static Bitmap ScaleBitmap(Bitmap bitmap1,int w,float width2){
    	int width = bitmap1.getWidth();
//    	int width2 = bitmap2.getWidth();
    	int height = bitmap1.getHeight();
//    	int w = ScreenUtils.getScreenWidth(context);
    	float scaleRate = (float)w/width2;
    	Matrix matrix = new Matrix();
    	matrix.postScale( scaleRate, scaleRate);
    	Bitmap newIcon = Bitmap.createBitmap(bitmap1, 0, 0, width, height, matrix, true);
    	return newIcon;
    	
    }
    /**根据bitmap2图片整体缩放效果缩放图片bitmap1*/
    public static Bitmap ScaleBitmap(Bitmap bitmap1,Context context,Bitmap bitmap2,boolean bol){
    	if (!bol) {
    		return ScaleBitmap(bitmap1, context, bitmap2);
		}
    	int width = bitmap1.getWidth();
    	int width2 = bitmap2.getWidth();
    	int height2 = bitmap2.getHeight();
    	int height = bitmap1.getHeight();
    	int w = ScreenUtils.getScreenWidth(context);
    	int h = ScreenUtils.getScreenHeight(context);
    	float scaleRatew = (float)w/(float)width2;
    	float scaleRateh = (float)h/(float)height2;
    	Matrix matrix = new Matrix();
    	matrix.postScale( scaleRatew, scaleRateh);
    	Bitmap newIcon = Bitmap.createBitmap(bitmap1, 0, 0, width, height, matrix, true);
    	return newIcon;
    	
    }
    
     /**
	 * 加载图片
	 */
    public static void loadingImg(final Handler mHandler,final Context context,final int[] Rid) {
    	if (Rid == null) {
    		mHandler.sendEmptyMessage(0);
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

				Bitmap reimg = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.novice_red_bg);
				int w = ScreenUtils.getScreenWidth(context);
				int width2 = reimg.getWidth();
				Bitmap[] imgBitmaps = new Bitmap[Rid.length];
				for (int i = 0; i < Rid.length; i++) {
					imgBitmaps[i]= ScaleBitmap(BitmapFactory.decodeResource( context.getResources(), Rid[i]), w, width2);
				}
				Message msg = mHandler.obtainMessage(0, imgBitmaps);
				mHandler.sendMessage(msg);
			}
			
		}).start();
	}
    /**
     * 加载图片
     */
    public static void loadingImg(final Handler mHandler,final Context context,final int[] Rid,final int what) {
    	if (Rid == null) {
    		mHandler.sendEmptyMessage(what);
    	}
    	new Thread(new Runnable() {
    		
    		@Override
    		public void run() {
    			Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
    			
    			Bitmap reimg = BitmapFactory.decodeResource(context.getResources(),
    					R.drawable.novice_red_bg);
    			int w = ScreenUtils.getScreenWidth(context);
    			int width2 = reimg.getWidth();
    			Bitmap[] imgBitmaps = new Bitmap[Rid.length];
    			for (int i = 0; i < Rid.length; i++) {
    				imgBitmaps[i]= ScaleBitmap(BitmapFactory.decodeResource( context.getResources(), Rid[i]), w, width2);
    			}
    			Message msg = mHandler.obtainMessage(what, imgBitmaps);
    			mHandler.sendMessage(msg);
    		}
    		
    	}).start();
    }
    
    /**
     * 图片的宽度和高度
     * @param w
     * @param h
     * @return
     */
    public static int[] getScaleBitmapWangH(int w ,int h){
    	int[] w_h = new int[2];
    	float scaleRatew = (float)AppController.screenWidth/(float)720;
    	w_h[0] = (int) (w*scaleRatew);
    	w_h[1] = (int) (h*scaleRatew);
    	return w_h;
    }

}
