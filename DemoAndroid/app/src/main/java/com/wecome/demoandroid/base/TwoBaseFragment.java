package com.wecome.demoandroid.base;

import java.util.Collection;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wecome.demoandroid.view.StateLayout;

/**
 * 二级Fragment页面的Base类
 */
public abstract class TwoBaseFragment extends Fragment {
	/** 
	 * 上下文，以后在内部类中需要上下文的时候再也不怕麻烦了^_^ 
	 */
	protected Activity context;
	
	protected StateLayout state_layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			context = getActivity();

			//为了不多次访问网络加载数据，复用StateLayout
			if (state_layout == null) {
				//说明Fragment的onCreateView方法时第一次执行
				state_layout = StateLayout.newInstance(context, getContentView()); //优化上面的代码后的结果
				
				//创建初始化控件的方法
				initView();
				//创建初始化监听器方法
				initListener();
				//创建初始化数据的方法
				initData();
				
				System.out.println("第一次创建");
			}else{
				ViewGroup parent = (ViewGroup) state_layout.getParent();
				if (parent != null) {
					parent.removeView(state_layout);
				}
				System.out.println("复用StateLayout");
			}
		return state_layout;
	}
	
	/** 
	 *  检查初始化数据，根据数据的不同情况显示对应状态的View 
	 */
	public boolean checkData(Collection<?> datas) {
		boolean result = false;
		if (datas == null) {
			state_layout.showFailView();
		} else if (datas.isEmpty()) {
			state_layout.showEmptyView();
		} else {
			state_layout.showContentView();
			result = true;
		}
		return result;
	}
	
	/** 
	 *  检查初始化数据，根据数据的不同情况显示对应状态的View 
	 */
	public boolean checkData(Map<?,?> datas) {
		boolean result = false;
		if (datas == null) {
			state_layout.showFailView();
		} else if (datas.isEmpty()) {
			state_layout.showEmptyView();
		} else {
			state_layout.showContentView();
			result = true;
		}
		return result;
	}
	
	/**
	 * 返回正常界面，可以是布局的id，也可以是view
	 */
	public abstract Object getContentView();

	/**
	 * 初始化控件
	 */
	public abstract void initView();
	
	/**
	 * 初始化监听器
	 */
	public abstract void initListener();
	
	/**
	 * 初始化数据
	 */
	public abstract void initData();

	/** 
	 * 查找View，省去强转操作
	 */
	protected <T> T findView(int id) {
		@SuppressWarnings("unchecked")
		T view = (T) state_layout.findViewById(id);
		return view;
	}

	/**
	 * 获取自己打的标题的抽象方法，子类必须实现
	 * 
	 * @return
	 */
	public abstract CharSequence getTitle();
	
}



