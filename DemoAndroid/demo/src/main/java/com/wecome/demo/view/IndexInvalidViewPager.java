package com.wecome.demo.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by chenjie07 on 17/9/18.
 * 更新数据时滑动ViewPager索引异常问题保护
 */

public class IndexInvalidViewPager extends ViewPager {
    public IndexInvalidViewPager(Context context) {
        super(context);
    }

    public IndexInvalidViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (getChildCount() == 0) {
            return false;
        }
        boolean result = false;
        try {
            result = super.onTouchEvent(ev);
        } catch (IndexOutOfBoundsException e) {
            // ViewPager.performDrag  越界问题保护
        }
        return result;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (getChildCount() == 0) {
            return false;
        }
        boolean result = false;
        try {
            result = super.onInterceptTouchEvent(ev);
        } catch (IndexOutOfBoundsException e) {
            // ViewPager.performDrag 越界问题保护
        }
        return result;
    }
}
