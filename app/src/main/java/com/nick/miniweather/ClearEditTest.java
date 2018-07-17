package com.nick.miniweather;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

/**
 * Created by weiming.chen on 2018/7/18.
 */

public class ClearEditTest extends EditText implements View.OnFocusChangeListener, TextWatcher {

    private Drawable mClearDrawable;

    public ClearEditTest(Context context) {
        this(context, null);
    }

    public ClearEditTest(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, android.R.attr.editTextStyle);
    }

    public ClearEditTest(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        init();
    }

    private void init() {
        mClearDrawable = getResources().getDrawable(R.drawable.key_icon_delete_n);
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        setClearIconVisiable(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    /**
     * 设置清除图标的显示与隐藏
     *
     * @param visile
     */
    protected void setClearIconVisiable(boolean visile) {
        Drawable mDelIcon = visile ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], mDelIcon, getCompoundDrawables()[3]);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    /**
     * 当输入框里面内容发生变化的时候回调
     *
     * @param charSequence
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
        setClearIconVisiable(charSequence.length() > 0);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (hasFocus()) {
            setClearIconVisiable(getText().length() > 0);
        } else {
            setClearIconVisiable(false);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                boolean touchable = event.getX() > (getWidth() - getPaddingRight() - mClearDrawable.getIntrinsicWidth())
                        && (event.getX() < (getWidth() - getPaddingRight()));
                if (touchable) {
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 设置晃动动画
     */
    public void setShakeAnimation() {
        this.setAnimation(shakeAnimation(5));
    }

    public static Animation shakeAnimation(int counts) {
        Animation translationAnimation = new TranslateAnimation(0, 10, 0, 0);
        translationAnimation.setInterpolator(new CycleInterpolator(counts));
        translationAnimation.setDuration(1000);
        return translationAnimation;
    }
}
