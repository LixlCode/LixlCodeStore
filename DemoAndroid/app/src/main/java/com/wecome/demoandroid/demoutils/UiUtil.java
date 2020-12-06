package com.wecome.demoandroid.demoutils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.wecome.demoandroid.MainApplication;

import java.util.Random;

/**
 * 有关UI的一个工具类，包含的功如下：
 * 1.根据id获取字符串
 * 2.根据id获取图片
 * 3.根据id获取颜色值
 * 4.根据id获取尺寸
 * 5.根据id获取字符串数组
 * 6.dp转px
 * 7.px转dp
 * 8.加载布局文件
 * 9.判断当前是否运行在主线程
 * 10.保证当前的操作运行在UI主线程(把Runnable中的代码运行在UI线程)
 * 11.把Runnable中的代码运行在子线程
 * 12.在屏幕的中央显示土司
 * 13.获取资源文件
 * 14.创建一个随机颜色
 * 15.创建一个有随机颜色图形的选择器的TextView
 * 16.创建一个有随机颜色图形的选择器
 * 17.创建图形的Drawable
 * 
 * @author lenovo
 */
public class UiUtil {

	public static Context getContext() {
		return MainApplication.getContext();
	}

	public static int getMainThreadId() {
		return MainApplication.getMainThreadId();
	}

	public static Handler getHandler() {
		return MainApplication.getHandler();
	}

	/**
	 * 根据id获取字符串
	 */
	public static String getString(int id) {
		return getContext().getResources().getString(id);
	}

	/**
	 * 根据id获取图片
	 */
	@SuppressWarnings("deprecation")
	public static Drawable getDrawable(int id) {
		return getContext().getResources().getDrawable(id);
	}

	/**
	 * 根据id获取颜色值
	 */
	@SuppressWarnings("deprecation")
	public static int getColor(int id) {
		return getContext().getResources().getColor(id);
	}

	/**
	 * 根据id获取尺寸
	 */
	public static int getDimen(int id) {
		return getContext().getResources().getDimensionPixelSize(id);
	}

	/**
	 * 根据id获取字符串数组
	 */
	public static String[] getStringArray(int id) {
		return getContext().getResources().getStringArray(id);
	}

	/**
	 * dp转px
	 */
	public static int dip2px(float dp) {
		float density = getContext().getResources().getDisplayMetrics().density;
		return (int) (density * dp + 0.5);
	}
	
	/**
	 * px转dp
	 */
	public static float px2dip(float px) {
		float density = getContext().getResources().getDisplayMetrics().density;
		return px / density;
	}

	/**
	 * 根据手机的分辨率将 sp 值转换为 px 值，保证文字大小不变
	 */
	public static int sp2px(float spValue) {
		final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 加载布局文件
	 */
	public static View inflate(int layoutId) {
		return View.inflate(getContext(), layoutId, null);
	}

	/**
	 * 判断当前是否运行在主线程
	 * @return true/false
	 */
	public static boolean isRunOnUiThread() {
		return getMainThreadId() == android.os.Process.myTid();
	}

	/**
	 * 保证当前的操作运行在UI主线程(把Runnable中的代码运行在UI线程)
	 */
	public static void runOnUiThread(Runnable runnable) {
		if (isRunOnUiThread()) {
			runnable.run();
		} else {
			getHandler().post(runnable);
		}
	}

	/**
	 * 把Runnable中的代码运行在子线程
	 */
	public static void runOnSubThread(Runnable runnable){
		new Thread(runnable){}.start();
	}
	
	/**
	 * 在屏幕的中央显示土司
	 */
	public static void showToast(CharSequence text){
		Toast toast = Toast.makeText(MainApplication.getContext(), text, Toast.LENGTH_SHORT);
		//让土司在屏幕的中央显示
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/**
	 * 获取资源文件
	 */
	public static Resources getResources(){
		return MainApplication.getContext().getResources();
	}

	/**
	 * 创建一个随机颜色
	 * @return 颜色值
	 */
	public static int creatrRandomColor() {
		//创建随机数对象，显示不同的颜色
		Random random = new Random();
		int red = 50 + random.nextInt(151); //50——200之间的随机颜色值
		int green = 50 + random.nextInt(151);
		int blue = 50 + random.nextInt(151);
		int color = Color.rgb(red, green, blue);
		return color;
	}
	
	/**
	 * 创建一个有随机颜色图形的选择器的TextView
	 * @return TextView对象
	 */
	@SuppressWarnings("deprecation")
	public static TextView createRandomColorShapeSelectorTextView() {
		TextView textView = new TextView(MainApplication.getContext());
		textView.setGravity(Gravity.CENTER);
		textView.setPadding(8, 8, 8, 8);
		textView.setTextColor(Color.BLACK);
		textView.setBackgroundDrawable(createRandomColorShapeSelector());
		textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TextView textView = (TextView) v;
				showToast(textView.getText());
			}
		});
		return textView;
	}
	
	/**
	 * 创建一个有随机颜色图形的选择器
	 * @return 选择器对象
	 */
	public static Drawable createRandomColorShapeSelector() {
		// 创建选择器对象
		StateListDrawable stateListDrawable = new StateListDrawable();
		//创建按下状态，和按下状态要使用的Drawable
		int[] pressState = {android.R.attr.state_enabled, android.R.attr.state_pressed};
		Drawable pressDrawable = createRandomColorShapeDrawable();
		//创建正常状态，和正常状态要使用的Drawable
		int[] normalState  = {};
		Drawable normalDrawable = createRandomColorShapeDrawable();
		
		//按下时显示按下的状态
		stateListDrawable.addState(pressState, pressDrawable);
		//显示正常的状态
		stateListDrawable.addState(normalState, normalDrawable);
		return stateListDrawable;
	}

	/**
	 * 创建图形的Drawable
	 */
	private static Drawable createRandomColorShapeDrawable() {
		// 创建一个图形的Drawable对象
		GradientDrawable drawable = new GradientDrawable();
		//设定图形为矩形
		drawable.setShape(GradientDrawable.RECTANGLE);
		//设定矩形四个角为圆角
		drawable.setCornerRadius(6);
		//设置矩形颜色
		drawable.setColor(creatrRandomColor());
		return drawable;
	}

}
