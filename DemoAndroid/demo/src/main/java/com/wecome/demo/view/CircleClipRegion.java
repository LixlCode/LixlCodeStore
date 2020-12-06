package com.wecome.demo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by chenjie07 on 17/8/27.
 */

public class CircleClipRegion extends LinearLayout {

    Path path;
    Region region;
    GestureDetector gestureDetector;
    View.OnClickListener listener;

    public CircleClipRegion(Context context) {
        super(context);
        init();
    }

    public CircleClipRegion(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleClipRegion(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public CircleClipRegion(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    private void init() {
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent event) {
                float[] loc = new float[]{event.getX(), event.getY()};
                if (listener != null && region.contains((int) loc[0], (int) loc[1])) {
                    listener.onClick(null);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        setWillNotDraw(false);

        path = new Path();
        path.addCircle(w / 2.0f, h / 2.0f, w / 2.0f, Path.Direction.CW);
        path.close();

        Region global = new Region(0, 0, w, h);
        region = new Region();
        region.setPath(path, global);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
