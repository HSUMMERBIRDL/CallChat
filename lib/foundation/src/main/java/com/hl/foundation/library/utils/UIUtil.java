/*
 * Copyright (c) 2014, KJFrameForAndroid 张涛 (kymjs123@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hl.foundation.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hl.foundation.frame.app.BaseApplication;
import com.hl.foundation.library.manager.AppManager;


/**
 * 系统屏幕的一些操作<br>
 * 
 * <b>创建时间</b> 2014-8-14
 * 
 * @author kymjs(kymjs123@gmail.com)
 * @version 1.1
 */
public final class UIUtil {
	
	/** 当前屏幕密度 */
	public static final float DENSITY = Resources.getSystem().getDisplayMetrics().density;
	
	/** 字符串 */
	public static final String ELLIPSIZE_FIXED = "\u200b";

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(int dpValue) {
        return (int) (dpValue * DENSITY + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        return (int) (pxValue / DENSITY + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
     */
    public static int px2sp(float pxValue) {
        float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px
     */
    public static int sp2px(float spValue) {
        float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
    
	
	/**
	 * 设置 textView 的文字，解决某些国产机省略号显示不正常的bug
	 * @param textView
	 * @param text
	 */
	public static void setText(TextView textView, String text) {
		if(TextUtils.isEmpty(text)) {
			textView.setText(text);
		} else {
			textView.setText(text + ELLIPSIZE_FIXED);
		}
	}
    
	
	/**
	 * 扩展点击区域的范围（扩展尺寸为宽/高最大值的二分之一）
	 * @param view 需要扩展的元素，此元素必需要有父级元素
	 */
	public static void expendTouchArea(final View view) {
		if (view != null) {
			expendTouchArea(view, (View) view.getParent());
		}
	}
	
	/**
	 * 扩展点击区域的范围
	 * 
	 * @param targetView 需要扩展的元素
	 * @param parentView 需要扩展的元素的祖父级元素
	 */
	public static void expendTouchArea(final View targetView, final View parentView) {
		if (targetView != null && parentView != null) {
			//如果太早执行本函数，会获取rect失败，因为此时UI界面尚未开始绘制，无法获得正确的坐标
			targetView.post(new Runnable() {
				@Override
				public void run() {
					//因为这是异步执行，需要在这里在执行一次保护
					if (targetView != null && parentView != null) {
						Rect rect = new Rect();
						targetView.getHitRect(rect);
						View tmpView = (View) targetView.getParent();
						View rootView = tmpView.getRootView();
						while (tmpView != null 
								&& tmpView != parentView 
								&& tmpView.getParent() != rootView) {
							rect.offset(tmpView.getLeft(), tmpView.getTop());
							tmpView = (View) tmpView.getParent();
						}
						
						int expendSize = Math.min(rect.width() / 2, rect.height() / 2);
						//expendSize = Math.max(expendSize, QMUIKit.dpToPx(50));
						rect.left -= expendSize;
						rect.top -= expendSize;
						rect.right += expendSize;
						rect.bottom += expendSize;
						parentView.setTouchDelegate(new TouchDelegate(rect, targetView));
					}
				}
			});
		}
	}
    
	/**
	 * 扩展点击区域的范围
	 * @param view 需要扩展的元素，此元素必需要有父级元素
	 * @param expendSize 需要扩展的尺寸（以dp为单位的）
	 */
	public static void expendTouchArea(final View view, final int expendSize) {
		if (view != null) {
			final View parentView = (View) view.getParent();
			
			parentView.post(new Runnable() {
				@Override
				public void run() {
				Rect rect = new Rect();
				view.getHitRect(rect); //如果太早执行本函数，会获取rect失败，因为此时UI界面尚未开始绘制，无法获得正确的坐标
				rect.left -= expendSize;
				rect.top -= expendSize;
				rect.right += expendSize;
				rect.bottom += expendSize;
				parentView.setTouchDelegate(new TouchDelegate(rect, view));
				}
			});
		}
	}
	
	private static int SCREEN_WIDTH = 0;
	private static int SCREEN_HEIGHT = 0;
	
    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth() {
    	if (SCREEN_WIDTH == 0) {
			SCREEN_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
		}
        return SCREEN_WIDTH;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight() {
        if (SCREEN_HEIGHT == 0) {
        	SCREEN_HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels;
		}
        return SCREEN_HEIGHT;
    }
    
    /**
     * 截图
     * 
     * @param v
     *            需要进行截图的控件
     * @return 该控件截图的Bitmap对象。
     */
    public static Bitmap captureView(View v) {
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        return v.getDrawingCache();
    }

    /**
     * 创建快捷方式
     * 
     * @param cxt
     *            Context
     * @param icon
     *            快捷方式图标
     * @param title
     *            快捷方式标题
     * @param cls
     *            要启动的类
     */
    public void createDeskShortCut(Context cxt, int icon,
            String title, Class<?> cls) {
        // 创建快捷方式的Intent
        Intent shortcutIntent = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重复创建
        shortcutIntent.putExtra("duplicate", false);
        // 需要现实的名称
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        // 快捷图片
        Parcelable ico = Intent.ShortcutIconResource.fromContext(
                cxt.getApplicationContext(), icon);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                ico);
        Intent intent = new Intent(cxt, cls);
        // 下面两个属性是为了当应用程序卸载时桌面上的快捷方式会删除
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        // 点击快捷图片，运行的程序主入口
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        // 发送广播。OK
        cxt.sendBroadcast(shortcutIntent);
    }
    
    /**
     * 是否应该隐藏键盘
     * @param v
     * @param event
     * @return
     */
    public static boolean isShouldHideInput(View v, MotionEvent event) {  
	    if (v != null && (v instanceof EditText)) {  
	        int[] leftTop = { 0, 0 };  
	        //获取输入框当前的location位置  
	        v.getLocationInWindow(leftTop);  
	        int left = leftTop[0];  
	        int top = leftTop[1];  
	        int bottom = top + v.getHeight();  
	        int right = left + v.getWidth();  
	        if (event.getX() > left && event.getX() < right  
	                && event.getY() > top && event.getY() < bottom) {  
	            // 点击的是输入框区域，保留点击EditText的事件  
	            return false;  
	        } else {  
	            return true;  
	        }  
	    }  
	    return false;  
	}

	/**
	 * 是否点击了除指定View外的其它区域
	 * @param v
	 * @param event
	 * @return
	 */
	public static boolean isTouchOtherRegion(View v, MotionEvent event) {
		if(null != v) {
			int leftTop[] = { 0, 0 };
			//获取View当前的location位置
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			int right = left + v.getWidth();

			if(event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击的是指定View内的区域
				return false;
			}

			// 点击的是其它区域
			return true;
		}

		return false;
	}
    
    /**
	 * 隐藏键盘
	 * @param context
	 * @param v 输入框
	 */
	public static void hideSoftInput(Context context, View v) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
		}
	}


	/**
	 * @param context
	 */
	public static void hideSoftInput(Activity context) {

		try{
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			IBinder ibinder = context.getCurrentFocus().getWindowToken();
			if((imm != null) && (ibinder != null)){
				imm.hideSoftInputFromWindow(ibinder, 0);
			}
		}catch (Exception e){

		}
	}


	public static void showSoftInput(Context context, View v) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (!imm.isActive()) {
			imm.showSoftInput(v, 0);
		}
	}
	
	public static int getStatusBarHeight() { 
		Rect rectangle = new Rect();
		Window window = AppManager.getAppManager().currentActivity().getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
		int statusBarHeight = rectangle.top;
		return statusBarHeight;
	}

	public static void setWindowBackgroundAlpha(Activity activity, float alpha) {
		WindowManager.LayoutParams params = activity.getWindow().getAttributes();
		params.alpha = alpha;
		activity.getWindow().setAttributes(params);
	}

	/**
	 * 重新计算ListView高度，避免嵌套使用时高度显示错误的问题
	 * 可用于ListView嵌套ListView、ScrollView嵌套ListView等场景
	 * @param 
	 * 		listView 需要重新计算高度的ListView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
        //获取ListView对应的Adapter
	    ListAdapter listAdapter = listView.getAdapter(); 
	    if (listAdapter == null) {
	        return;
	    }
	
	    int totalHeight = 0;
	    for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   //listAdapter.getCount()返回数据项的数目
	        View listItem = listAdapter.getView(i, null, listView);
	        listItem.measure(0, 0);  //计算子项View 的宽高
	        totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度
	    }
	
	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	    //listView.getDividerHeight()获取子项间分隔符占用的高度
	    //params.height最后得到整个ListView完整显示需要的高度
	    listView.setLayoutParams(params);
	}

    public static int getDimensionPixelSize(int resId) {
        return BaseApplication.getAppContext().getResources().getDimensionPixelSize(resId);
    }

	private final static int UPPER_LEFT_X = 0;
	private final static int UPPER_LEFT_Y = 0;
	public static Drawable convertViewToDrawable(View view) {
		int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(spec, spec);
		view.layout(UPPER_LEFT_X, UPPER_LEFT_Y, view.getMeasuredWidth(), view.getMeasuredHeight());
		Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		c.translate(-view.getScrollX(), -view.getScrollY());
		view.draw(c);
		view.setDrawingCacheEnabled(true);
		Bitmap cacheBmp = view.getDrawingCache();
		Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
		cacheBmp.recycle();
		view.destroyDrawingCache();
		return new BitmapDrawable(viewBmp);
	}
}