package com.hl.foundation.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hl.foundation.R;
import com.hl.foundation.library.manager.AppManager;
import com.hl.foundation.library.utils.StringUtils;
import com.hl.foundation.library.utils.UIUtil;


/**
 * des: 标题栏封装
 * Created by HL
 * on 2016/12/28 0028.
 */

public class CTopBar extends RelativeLayout {

    private final String TAG_LEFT_MENU_TEXT = "LeftMenuTextTag";
    private final String TAG_TITLE = "TitleTag";


    private Context mContext;

    private boolean mIsLeftShow = true;  //  左边整个view是否可见
    private boolean isShowLeftDrawable = true;  // 左边view的图片是否可见
    private boolean mLeftMenuTextIsShow = true; // 左边view的文字是否可见

    // 中间
    private TextView mTitleTextView;// 标题控件
    private String mTitleText;// 标题内容
    private int mTitleTextColor;// 标题字体颜色
    private int mTitleTextSize;// 标题字体大小

    // 左边
    private LinearLayout leftLayout;
    private ImageView leftButton; // 左边图片
    private Drawable mLeftButtonDrawable;// 左菜单按钮图标
    private TextView mLeftMenuTextView;// 左菜单文本控件
    private String mLeftMenuText;// 左菜单文本内容
    private OnClickListener mOnMenuLeftClickListener;

    public CTopBar(Context context) {
        this(context, null);
    }

    public CTopBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CTopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;

        // 获取自定义属性
        TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.CTopBar,
                defStyleAttr, 0);
        if (null != tArray) {
            for (int i = 0; i < tArray.getIndexCount(); i++) {
                int attr = tArray.getIndex(i);


                if (attr == R.styleable.CTopBar_mTitleText) {
                    mTitleText = tArray.getString(attr);
                } else if (attr == R.styleable.CTopBar_mTitleTextColor) {
                    mTitleTextColor = tArray.getColor(attr, Color.WHITE);
                } else if (attr == R.styleable.CTopBar_mTitleTextSize) {
                    mTitleTextSize = tArray.getInteger(attr, 18);
                } else if (attr == R.styleable.CTopBar_mIsLeftShow) {
                    mIsLeftShow = tArray.getBoolean(attr, true);
                } else if (attr == R.styleable.CTopBar_mLeftButtonDrawable) {
                    mLeftButtonDrawable = tArray.getDrawable(attr);
                    if (null == mLeftButtonDrawable) {
                        /**
                         * getResrouce().getDrawable(int resId)方法已过时或低版本API不能用，所以用ContextCompat
                         * .getDrawable
                         * @see http://www.bubuko.com/infodetail-1118953.html
                         * FreddyChen
                         */
                        mLeftButtonDrawable = ContextCompat.getDrawable(getContext(), R.drawable
                                .ic_back);
                    }
                } else if (attr == R.styleable.CTopBar_mLeftMenuText) {
                    mLeftMenuText = tArray.getString(attr);
                } else if (attr == R.styleable.CTopBar_mLeftMenuTextIsShow) {
                    mLeftMenuTextIsShow = tArray.getBoolean(attr, true);
                }
            }
        }

        initTopBar();
    }

    private void initTopBar() {

        createTitleView();

        if (mIsLeftShow) {
            createLeftView();
        }
    }

    /**
     * 创建左边view
     * 完整的形式是包括一个返回的图片和文字
     */
    private void createLeftView() {

        leftLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams leftLayoutParams = new LinearLayout.LayoutParams(LayoutParams
                .WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        leftLayoutParams.leftMargin = UIUtil.dp2px(12);
        leftLayout.setLayoutParams(leftLayoutParams);
        leftLayout.setOrientation(LinearLayout.HORIZONTAL);
        leftLayout.setGravity(Gravity.CENTER_VERTICAL);
        leftLayout.setId(R.id.topbar_menu_left);

        if (isShowLeftDrawable) {
            createLeftDrawable();
            leftLayout.addView(leftButton);
        }

        if (mLeftMenuTextIsShow) {
            if (!StringUtils.isEmpty(mLeftMenuText)) {
                createLeftText();
                leftLayout.addView(mLeftMenuTextView);
            }
        }

        // 为左边设置点击事件
        leftLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null == mOnMenuLeftClickListener) {
                    AppManager.getAppManager().finishActivity();
                } else {
                    mOnMenuLeftClickListener.onClick(v);
                }
            }
        });

        LayoutParams mLeftMenuLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        mLeftMenuLayoutParams.leftMargin = UIUtil.dp2px(12);
        mLeftMenuLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
        mLeftMenuLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        addView(leftLayout, mLeftMenuLayoutParams);

        // 扩展返回按钮点击区域
        UIUtil.expendTouchArea(leftLayout, UIUtil.dp2px(24));
    }

    /**
     * 创建左边文字
     */
    private void createLeftText() {

        mLeftMenuTextView = new TextView(getContext());
        mLeftMenuTextView.setText(mLeftMenuText);
        mLeftMenuTextView.setTextColor(Color.WHITE);
        mLeftMenuTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        mLeftMenuTextView.setSingleLine(true);
        mLeftMenuTextView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        mLeftMenuTextView.setGravity(Gravity.CENTER);
        mLeftMenuTextView.setTag(TAG_LEFT_MENU_TEXT);
    }

    /**
     * 左边按钮图片
     */
    private void createLeftDrawable() {

        leftButton = new ImageView(mContext);
        leftButton.setImageDrawable(mLeftButtonDrawable);

    }

    /**
     * 标题部分
     */
    private void createTitleView() {

        mTitleTextView = new TextView(getContext());
        mTitleTextView.setId(R.id.topbar_title);
        mTitleTextView.setText(mTitleText);
        mTitleTextView.setTextColor(mTitleTextColor);
        mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTitleTextSize);
        mTitleTextView.setSingleLine(true);
        mTitleTextView.setEllipsize(TextUtils.TruncateAt.MIDDLE);

        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setTag(TAG_TITLE);
        LayoutParams mTitleLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        mTitleLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);

        addView(mTitleTextView, mTitleLayoutParams);
    }

    public void setTitle(String title) {
        mTitleTextView.setText(title);
    }

}
