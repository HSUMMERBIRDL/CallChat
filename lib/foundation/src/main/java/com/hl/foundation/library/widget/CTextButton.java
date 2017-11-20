package com.hl.foundation.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * 自定义的线性布局，通过改变透明度来区分按钮的各种状态。
 * 
 * Created by FreddyChen on 2016/3/4.
 */
public class CTextButton extends TextView {

//	private final int mOpacityNormal = 255;
//	private final int mOpacityPressed = (int) (255 * 0.6);
//	private final int mOpacityDisabled = (int) (255 * 0.3);
	private final float mOpacityNormal = 1;
	private final float mOpacityPressed = 0.3f;
	private final float mOpacityDisabled = 0.3f;

	public CTextButton(Context context) {
		this(context, null);
	}
	
	public CTextButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CTextButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		init();
	}

	private void init() {
		setClickable(true);
		setOnTouchListener(onTouchChangeOpacityListener);
		if (!isEnabled()) {
			setCustomAlpha(mOpacityDisabled);
		}
	}

	private OnTouchListener onTouchChangeOpacityListener = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (isEnabled()) {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					setCustomAlpha(mOpacityPressed);
					
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					//if (!isPressed()) {
					setCustomAlpha(mOpacityNormal);
					//}
				}
			}
			return false;
		}

	};
	
	@Override
	public void setEnabled(boolean enabled) {
		if (enabled) {
			setCustomAlpha(mOpacityNormal);
		} else {
			setCustomAlpha(mOpacityDisabled);
		}
		super.setEnabled(enabled);
	}
	
//	@Override
//	public void onWindowFocusChanged(boolean hasWindowFocus) {
//		super.onWindowFocusChanged(hasWindowFocus);
//		//页面跳转后再回来，需要重设按钮透明度
//		if (hasWindowFocus) {
//			setEnabled(isEnabled());
//		}
//	}
	
//	@Override
//	public void onAttachedToWindow() {
//		super.onAttachedToWindow();
//		setEnabled(isEnabled());
//	}
	
	private void setCustomAlpha(float value) {
//		if (null != getBackground()) {
//			getBackground().setAlpha(value);
//		}
//		setTextColor(getTextColors().withAlpha(value));
		setAlpha(value);
	}
}
