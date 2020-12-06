package com.wecome.demoandroid.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.wecome.demoandroid.R;

/**
 * 这是状态的布局，里面是自定义控件，封装了四种状态： 加载成功，加载失败，加载为空，正常的界面。
 */
public class StateLayout extends FrameLayout {

	private View loadingView;
	private View failView;
	private View emptyView;
	private View contentView;

	public StateLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 创建一个StateLayout的实现
	 * @param context
	 * @param layoutIdOrView：可以传一个布局的id，也可以传一个View对象
	 * @return
	 */
	public static StateLayout newInstance(Context context, Object layoutIdOrView){
		//调用自定义控件StateLayout---调用之后他就拥有了前三种状态---第四种状态每个界面不一样，是变化的不能在这里写死
		StateLayout state_layout = (StateLayout) View.inflate(context, R.layout.state_layout, null);
		//设置第四种状态----正常界面的状态
		state_layout.setContentView(layoutIdOrView);
		return state_layout;
	}
	
	
	/**
	 * 显示正在加载的View
	 */
	public void showLoadingView() {
		showView(loadingView);
	}

	/**
	 * 显示加载失败的View
	 */
	public void showFailView() {
		showView(failView);
	}

	/**
	 * 显示加载为空的View
	 */
	public void showEmptyView() {
		showView(emptyView);
	}

	/**
	 * 显示正常界面的View
	 */
	public void showContentView() {
		showView(contentView);
	}

	/**
	 * 设置正常界面的View
	 * @param layoutIdOrView
	 *            可以创一个View，也可以传一布局id
	 */
	public void setContentView(Object layoutIdOrView) {
		if (layoutIdOrView instanceof Integer) { // 如果是布局id,就转成View对象
			int layoutId = (Integer) layoutIdOrView;
			contentView = View.inflate(getContext(), layoutId, null);
		} else { // 是一个View，就直接赋给contentView
			contentView = (View) layoutIdOrView;
		}
		// 将正常界面的View添加到状态布局容器中
		super.addView(contentView);
		//默认显示loadingView
		contentView.setVisibility(View.GONE);
	}

	/**
	 * 把xml中的View填充成Java的View对象
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		loadingView = findViewById(R.id.loadingView);
		failView = findViewById(R.id.failView);
		emptyView = findViewById(R.id.emptyView);
		showLoadingView();
	}


	/**
	 * 指定要显示的View ，隐藏其他的view
	 * @param view 指定要显示的View
	 */
	private void showView(View view) {
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i); // 获取孩子控件
			child.setVisibility(child == view ? View.VISIBLE : View.GONE);
		}
	}

}
