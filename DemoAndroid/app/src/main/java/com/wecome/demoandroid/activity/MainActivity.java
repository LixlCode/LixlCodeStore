package com.wecome.demoandroid.activity;

import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wecome.demoandroid.R;
import com.wecome.demoandroid.adapter.MainViewPagerAdapter;
import com.wecome.demoandroid.demoutils.UiUtil;
import com.wecome.demoandroid.fragment.FivePagerFragment;
import com.wecome.demoandroid.fragment.FourPagerFragment;
import com.wecome.demoandroid.fragment.OnePagerFragment;
import com.wecome.demoandroid.fragment.ThreePagerFragment;
import com.wecome.demoandroid.fragment.TwoPagerFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
                        implements View.OnClickListener{
    private Toolbar mToolBar;
    private ViewPager mViewPager;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonOne;
    private RadioButton mRadioButtonTwo;
    private RadioButton mRadioButtonThree;
    private RadioButton mRadioButtonFour;
    private RadioButton mRadioButtonFive;
    private PopupWindow mPopupWindow;
    private TextView mMainTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        initView();
        //自定义ToolBar
        initToolBar();
    }

    /**
     * 自定有ToolBar
     */
    private void initToolBar() {
        //设置App的logo
        //mToolBar.setLogo(R.mipmap.ic_launcher_round);
        //设置主标题，默认为app_label的名字
        mToolBar.setTitle("");
        mToolBar.setTitleTextColor(getResources().getColor(R.color.black));
        //设置副标题
        //mToolBar.setSubtitle("Sub");
        //mToolBar.setSubtitleTextColor(Color.parseColor("#FD87A9"));
        //设置侧边栏按钮
        mToolBar.setNavigationIcon(R.mipmap.title_back);
        //取代原本的AntionBar
        setSupportActionBar(mToolBar);
        //设置NavigationIcon的点击事件,需要放在setSupportActionBar之后设置才会生效,
        //因为setSupportActionBar里面也会setNavigationOnClickListener
        mToolBar.setNavigationOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                UiUtil.showToast("点击了返回按钮");
            }
        });
        //设置toolBar上的MenuItem点击事件
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    /*case R.id.action_edit:
                        UiUtil.showToast("点击了编辑条目");
                        Intent intent = new Intent(
                                MainActivity.this,FoldTextViewActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_share:
                        UiUtil.showToast("点击了分享条目");
                        break;*/
                    case R.id.action_overflow:
                        //除了PopupWindow意外的地方变暗，形成这招的效果
                        Window window=getWindow();
                        WindowManager.LayoutParams wl = window.getAttributes();
                        wl.alpha=0.6f;   //这句就是设置窗口里崆件的透明度的．0.0全透明．1.0不透明．
                        window.setAttributes(wl);
                        //弹出自定义overflow
                        popUpMyOverflow();
                        return true;
                }
                return true;
            }
        });
        //设置ToolBar里面还可以包含子的控件
        /*mToolBar.findViewById(R.id.btn_diy).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mToast.setText("点击自定义按钮");
                mToast.show();
            }
        });*/
        mToolBar.findViewById(R.id.tv_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiUtil.showToast("点击了标题");
            }
        });
    }

    //如果有Menu,创建完后,系统会自动添加到ToolBar上
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * 弹出自定义的popWindow
     */
    public void popUpMyOverflow() {
        //获取状态栏高度
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        //状态栏高度+toolbar的高度
        int yOffset = frame.top + mToolBar.getHeight();
        if (null == mPopupWindow) {
            //初始化PopupWindow的布局
            View popView = getLayoutInflater()
                    .inflate(R.layout.main_popwindow, null);
            //popView即popupWindow的布局，ture设置focusAble.
            mPopupWindow = new PopupWindow(popView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
            //必须设置BackgroundDrawable后setOutsideTouchable(true)才会有效
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
            //点击外部关闭。
            mPopupWindow.setOutsideTouchable(true);
            //设置一个动画。
            mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
            //设置Gravity，让它显示在右上角。
            mPopupWindow.showAtLocation(mToolBar,
                    Gravity.END | Gravity.TOP, 0, yOffset);
            //设置item的点击监听
            popView.findViewById(R.id.ll_item1).setOnClickListener(this);
            popView.findViewById(R.id.ll_item2).setOnClickListener(this);
            popView.findViewById(R.id.ll_item3).setOnClickListener(this);
        } else {
            mPopupWindow.showAtLocation(mToolBar,
                    Gravity.END | Gravity.TOP, 0, yOffset);
        }
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Window window=getWindow();
                WindowManager.LayoutParams wl = window.getAttributes();
                wl.alpha=1.0f;   //这句就是设置窗口里崆件的透明度的．0.0全透明．1.0不透明．
                window.setAttributes(wl);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_item1:
                UiUtil.showToast("拍照");
                break;
            case R.id.ll_item2:
                UiUtil.showToast("分享");
                break;
            case R.id.ll_item3:
                UiUtil.showToast("付款");
                break;
        }
        //点击PopWindow的item后,关闭此PopWindow
        if (null != mPopupWindow && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    /**
     * 初始化主界面
     */
    private void initView() {
        mToolBar = (Toolbar) findViewById(R.id.tb_toolbar);

        mMainTitle = (TextView) findViewById(R.id.tv_title);

        mViewPager = (ViewPager) findViewById(R.id.vp_viewPager);
        mRadioGroup = (RadioGroup) findViewById(R.id.rg_content_Buttom);

        mRadioButtonOne = (RadioButton) findViewById(R.id.rb_home);
        mRadioButtonTwo = (RadioButton) findViewById(R.id.rb_newscenter);
        mRadioButtonThree = (RadioButton) findViewById(R.id.rb_smartservice);
        mRadioButtonFour = (RadioButton) findViewById(R.id.rb_govaffairs);
        mRadioButtonFive = (RadioButton) findViewById(R.id.rb_settings);

        initData();//初始化数据
    }

    /**
     * 初始化页面
     */
    private void initData() {
        //给ViewPager设置适配器
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new OnePagerFragment());
        fragments.add(new TwoPagerFragment());
        fragments.add(new ThreePagerFragment());
        fragments.add(new FourPagerFragment());
        fragments.add(new FivePagerFragment());

        mViewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager(),fragments));
        //给ViewPager设置监听器
        mViewPager.addOnPageChangeListener(new ViewPagerOnPageChangeListener());

        // 监听底部的单选按钮，实现单选按钮和5个页面的关联
        mRadioGroup.setOnCheckedChangeListener(new RadioGroupOnCheckedChangeListener());

        //默认选择首页
        mRadioButtonOne.setChecked(true);
    }

    /**
     * ViewPager的监听器
     */
    private class ViewPagerOnPageChangeListener
            implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position,
                   float positionOffset, int positionOffsetPixels) {
        }
        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0:
                    mMainTitle.setText(R.string.app_one);
                    mRadioButtonOne.setChecked(true);
                    break;
                case 1:
                    mMainTitle.setText(R.string.app_two);
                    mRadioButtonTwo.setChecked(true);
                    break;
                case 2:
                    mMainTitle.setText(R.string.app_three);
                    mRadioButtonThree.setChecked(true);
                    break;
                case 3:
                    mMainTitle.setText(R.string.app_four);
                    mRadioButtonFour.setChecked(true);
                    break;
                case 4:
                    mMainTitle.setText(R.string.app_five);
                    mRadioButtonFive.setChecked(true);
                    break;
                default:
                    break;
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    /**
     * 底部五个按钮的监听器
     */
    private class RadioGroupOnCheckedChangeListener
            implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup radioGroup,int checkedId) {
            switch (checkedId) {
                case R.id.rb_home:
                    //让ViewPager切换到首页
                    //参数2：表示是否带滑动的效果，我们这里不需要，所以传false
                    mViewPager.setCurrentItem(0, false);
                    //enableSildingMenu(false);
                    mMainTitle.setText(R.string.app_one);
                    break;
                case R.id.rb_newscenter:
                    //让ViewPager切换到新闻中心页面
                    mViewPager.setCurrentItem(1, false);
                    //enableSildingMenu(true);
                    mMainTitle.setText(R.string.app_two);
                    break;
                case R.id.rb_smartservice:
                    //让ViewPager切换到智慧服务页面
                    mViewPager.setCurrentItem(2, false);
                    //enableSildingMenu(true);
                    mMainTitle.setText(R.string.app_three);
                    break;
                case R.id.rb_govaffairs:
                    //让ViewPager切换到政务页面
                    mViewPager.setCurrentItem(3, false);
                    //enableSildingMenu(true);
                    mMainTitle.setText(R.string.app_four);
                    break;
                case R.id.rb_settings:
                    //让ViewPager切换到设置页面
                    mViewPager.setCurrentItem(4, false);
                    //enableSildingMenu(false);
                    mMainTitle.setText(R.string.app_five);
                    break;
                default:
                    break;
            }
        }
    }
}
