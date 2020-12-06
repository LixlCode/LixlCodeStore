package com.wecome.demo.activity;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.wecome.demo.R;
import com.wecome.demo.adapter.ViewPagerAdapter;
import com.wecome.demo.base.BaseFragment;
import com.wecome.demo.fragment.FeasibleFragment;
import com.wecome.demo.fragment.HomeFragment;
import com.wecome.demo.fragment.SetFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager vp_viewPager;
    private LinearLayout rb_tab_1,rb_tab_2,rb_tab_3;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBase();
        // 设置状态栏为透明状态
        setStatusBarTransparent(getWindow());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 适配全面屏
            setFullScreenWindowLayout(getWindow());
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setFullScreenWindowLayout(Window window) {
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        //设置页面全屏显示
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        //设置页面延伸到刘海区显示
        window.setAttributes(lp);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarTransparent(Window window){
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    private void initBase() {
        vp_viewPager = findViewById(R.id.vp_ViewPager);
        rb_tab_1 = findViewById(R.id.rb_tab_1);
        rb_tab_2 = findViewById(R.id.rb_tab_2);
        rb_tab_3 = findViewById(R.id.rb_tab_3);
        rb_tab_1.setOnClickListener(this);
        rb_tab_2.setOnClickListener(this);
        rb_tab_3.setOnClickListener(this);

        ArrayList<BaseFragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new FeasibleFragment());
        fragments.add(new SetFragment());

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        vp_viewPager.setAdapter(adapter);

        vp_viewPager.setCurrentItem(0);
        rb_tab_1.setSelected(true);

        vp_viewPager.addOnPageChangeListener(new ViewPageChangeListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_tab_1:
                vp_viewPager.setCurrentItem(0);
                updateTabStatus(true, false, false);
                break;
            case R.id.rb_tab_2:
                vp_viewPager.setCurrentItem(1);
                updateTabStatus(false, true, false);
                break;
            case R.id.rb_tab_3:
                vp_viewPager.setCurrentItem(2);
                updateTabStatus(false, false, true);
                break;
            default:
                break;
        }
    }

    private void updateTabStatus(boolean isTab_1, boolean isTab_2, boolean isTab_3){
        rb_tab_1.setSelected(isTab_1);
        rb_tab_2.setSelected(isTab_2);
        rb_tab_3.setSelected(isTab_3);
    }

    private class ViewPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int i, float v, int i1) {
            Log.d("lxl","onPageScrooled");
        }

        @Override
        public void onPageSelected(int i) {
            Log.d("lxl","onPageSelected");
            int currentItem = vp_viewPager.getCurrentItem();
            if (currentItem == 0){
                updateTabStatus(true, false, false);
            } else if (currentItem == 1){
                updateTabStatus(false, true, false);
            } else {
                updateTabStatus(false, false, true);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {
            Log.d("lxl","onPageScrollStateChanged");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(),
                    "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

}
