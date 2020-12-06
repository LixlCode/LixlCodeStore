package com.wecome.demoandroid.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2016/7/27.
 * 实现仿QQ侧滑删除的控件
 */
public class SwipeLayout extends FrameLayout {

    private ViewDragHelper viewDragHelper;
    private ViewGroup mainView;
    private ViewGroup menuView;
    private int mWidth;
    private int mHeight;
    private int maxDragRange;
    private GestureDetector gestureDetector;

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //1、初始化ViewDragHelper
        viewDragHelper = ViewDragHelper.create(this, callback);
        gestureDetector = new GestureDetector(context,simpleOnGestureListener);
    }
    private GestureDetector.SimpleOnGestureListener simpleOnGestureListener=new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceX)>Math.abs(distanceY)){
                requestDisallowInterceptTouchEvent(true);
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    };
    //定义状态
    public enum DragState{
        OPEN,DRAGGING,CLOSE
    }
    private DragState currentState=DragState.CLOSE;//当前状态
    private DragState preState=DragState.CLOSE;//保存上一次状态
    //定义接口
    public interface OnSwipeStateChangedListener{
        public void onOpen(SwipeLayout swipeLayout);
        public void onClose(SwipeLayout swipeLayout);
    }
    private OnSwipeStateChangedListener onSwipeStateChangedListener;
    public void setOnSwipeStateChangedListener(OnSwipeStateChangedListener onSwipeStateChangedListener) {
        this.onSwipeStateChangedListener = onSwipeStateChangedListener;
    }

    //4、重写回调方法
    private ViewDragHelper.Callback callback=new ViewDragHelper.Callback() {
        /**
         * 尝试捕获一个view
         * @param child     被捕获的view
         * @param pointerId     多点触碰
         * @return      返回值决定了view能否被拖拽
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        /**
         * 限制view的滑动范围，这个方法在在view移动之前被调用
         * @param child
         * @param left      告诉ViewDragHelper下一次滑到的距离
         * @param dx
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child==mainView){
                //当拖拽主界面的时候限制主界面拖拽范围
                if (left<-maxDragRange){
                    left=-maxDragRange;
                }else if (left>0){
                    left=0;
                }
            }else{
                //当拖拽菜单的时候限制菜单拖拽范围
                if (left<mWidth-maxDragRange){
                    left=mWidth-maxDragRange;
                }else if (left>mWidth){
                    left=mWidth;
                }
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView==mainView){
                //把主界面的偏移量给菜单，让菜单移动
                menuView.offsetLeftAndRight(dx);
            }else{
                //把菜单的偏移量给主界面，让主界面移动
                mainView.offsetLeftAndRight(dx);
            }
            //执行接口回调
            executeListener(mainView.getLeft());
            invalidate();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (xvel==0 && mainView.getLeft()<-maxDragRange*0.5f){
                open();
            }else if(xvel<0){
                open();
            }else{
                close(true);
            }
        }
    };
    //接口回调的方法
    private void executeListener(int left) {
        //保存上一次状态
        preState=currentState;
        //更新当前的拖拽状态
        currentState=updateState(left);
        if (onSwipeStateChangedListener!=null&&preState!=currentState){
            if (currentState==DragState.OPEN){
                onSwipeStateChangedListener.onOpen(this);
            }else if (currentState==DragState.CLOSE){
                onSwipeStateChangedListener.onClose(this);
            }
        }
    }

    private DragState updateState(int left) {
        if (left==-maxDragRange){
            return DragState.OPEN;
        }else if (left==0){
            return DragState.CLOSE;
        }
        return DragState.DRAGGING;
    }

    //关闭才菜单
    public void close(boolean isSmooth) {
        if (isSmooth){
            //发起动画，返回值是true表示要执行动画
            if (viewDragHelper.smoothSlideViewTo(mainView,0,0)){
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }else{
            //摆放到初始化状态
            mainView.layout(0,0,mWidth,mHeight);
            menuView.layout(mWidth,0,maxDragRange+mWidth,mHeight);
            //把当前状态设置close
            currentState=DragState.CLOSE;
            if(onSwipeStateChangedListener!=null){
                onSwipeStateChangedListener.onClose(this);
            }
        }

    }
    //打开菜单
    private void open() {
        if (viewDragHelper.smoothSlideViewTo(mainView,-maxDragRange,0)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    //2、把事件交给ViewDragHelper去拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        requestDisallowInterceptTouchEvent(true);
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }
    //3、把事件交给viewDragHelper处理

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //初始化主界面和菜单的位置，也就是关闭状态
        mainView.layout(0,0,mWidth,mHeight);
        menuView.layout(mWidth,0,maxDragRange+mWidth,mHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //主界面的宽高，也是屏幕的宽
        mWidth = mainView.getMeasuredWidth();
        mHeight = mainView.getMeasuredHeight();
        //获取最大拖拽范围,也就是菜单的宽度
        maxDragRange = menuView.getMeasuredWidth();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //健壮性判断
        if (getChildCount()!=2){
            throw new IllegalStateException("you must have two only children");
        }
        //必须是ViewGroup类型
        if (!(getChildAt(0) instanceof ViewGroup)||!(getChildAt(1) instanceof ViewGroup)){
            throw new IllegalArgumentException("your child must be instance of ViewGroup");
        }
        menuView = (ViewGroup) getChildAt(0);
        mainView = (ViewGroup) getChildAt(1);
    }
}
