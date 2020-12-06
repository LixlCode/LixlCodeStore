package com.wecome.demo.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wecome.demo.R;
import com.wecome.demo.activity.SearchActivity;
import com.wecome.demo.adapter.FeasibleFragmentAdapter;
import com.wecome.demo.adapter.FeasibleHeaderViewPagerAdapter;
import com.wecome.demo.base.BaseFragment;
import com.wecome.demo.model.FeasibleFragmentModel;
import com.wecome.demo.network.RetrofitHttpService;
import com.wecome.demo.network.RetrofitServiceManager;
import com.wecome.demo.utils.SimpleToolbar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FeasibleFragment extends BaseFragment {

    private List<FeasibleFragmentModel.ResultsBean> results;
    private FeasibleFragmentAdapter adapter;
    private View view;

    // ------------------ 轮播图 start----------------
    private ViewPager mViewPager;
    private TextView mText;
    private LinearLayout mLLDot;
    private static final int VIEWPAGER_CHANGED = 0;
    private int pagesize;
    private View currentDot; //存放白色的点

    private int[] imageResIds = {
            R.mipmap.a,
            R.mipmap.b,
            R.mipmap.c,
            R.mipmap.d,
            R.mipmap.e,
    };
    private String[] descs = {
            "巩俐不低俗，我就不能低俗",
            "扑树又回来啦！再唱经典老歌引万人大合唱",
            "揭秘北京电影如何升级",
            "乐视网TV版大派送",
            "热血屌丝的反杀",
    };

    //存放ImageView的数组
    ImageView[] imageViews = new ImageView[imageResIds.length];
    //存放点的数组
    View[] dots = new View[imageResIds.length];

    // ------------------ 轮播图 end----------------

    @Override
    protected View initView() {
        view = View.inflate(mActivity, R.layout.fragment_feasible, null);
        // initToolBar(view);
        return view;
    }

    private void initToolBar(View view) {
        SimpleToolbar simple_toolbar = view.findViewById(R.id.simple_toolbar);
        simple_toolbar.setMainTitle("创新");
        simple_toolbar.setRightTitleDrawable(R.mipmap.icon_searchk);
        simple_toolbar.setRightTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // 跳转到搜索页面
                mActivity.startActivity(new Intent(mActivity, SearchActivity.class));
            }
        });
    }

    private void initRecyclerView(View view) {
        RecyclerView mRecyclerView = view.findViewById(R.id.rv_feasible_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new FeasibleFragmentAdapter(mActivity, R.layout.feasible_page_list_item, results);
        mRecyclerView.setAdapter(adapter);

        setFirstHeader();
        // setSecondHeader();
        setSecondHeader2();
    }

    private void setSecondHeader() {
        // 给RecyclerView添加第二个头部
        // 给ViewPager准备数据
        ArrayList<Drawable> headData = new ArrayList<>();
        headData.add(getResources().getDrawable(R.mipmap.icon_bg_2));
        headData.add(getResources().getDrawable(R.mipmap.icon_bg_3));
        headData.add(getResources().getDrawable(R.mipmap.icon_bg_4));

        ArrayList<ImageView> iv = new ArrayList<>();
        for (int i = 0; i < headData.size(); i++) {
            ImageView imageView = new ImageView(mActivity);
            imageView.setImageDrawable(headData.get(i));
            iv.add(imageView);
        }

        View header2 = mActivity.getLayoutInflater().inflate(R.layout.feasible_list_header_2, null);
        ViewPager mViewPager = header2.findViewById(R.id.vp_view_page);
        FeasibleHeaderViewPagerAdapter viewPagerAdapter = new FeasibleHeaderViewPagerAdapter(mActivity,iv);
        mViewPager.setAdapter(viewPagerAdapter);

        // 滑动冲突，暂时不添加这个头部
        // adapter.addHeaderView(header2);
    }

    // ---------------------------------------------------------

    private void setSecondHeader2() {
        // 给RecyclerView添加第二个头部
        ArrayList<Drawable> headData = new ArrayList<>();
        headData.add(getResources().getDrawable(R.mipmap.icon_bg_2));
        headData.add(getResources().getDrawable(R.mipmap.icon_bg_3));
        headData.add(getResources().getDrawable(R.mipmap.icon_bg_4));

        View header2 = mActivity.getLayoutInflater().inflate(R.layout.feasible_list_header_2, null);
        ViewPager mViewPager = header2.findViewById(R.id.vp_view_page_9);

        ArrayList<ImageView> iv = new ArrayList<>();
        for (int i = 0; i < headData.size(); i++) {
            ImageView imageView = new ImageView(mActivity);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageDrawable(headData.get(i));
            iv.add(imageView);
        }

        FeasibleHeaderViewPagerAdapter viewPagerAdapter = new FeasibleHeaderViewPagerAdapter(mActivity, iv);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setPageMargin(30); // 设置item之间的间距
        mViewPager.setOffscreenPageLimit(3); // 缓存页面，为了加载流畅
        mViewPager.setPageTransformer(true, new CustPagerTransformer(mActivity)/*RotatePageTransformer()*/);

        adapter.addHeaderView(header2);
    }


    public class RotatePageTransformer implements ViewPager.PageTransformer {

        private int degress = 25;

        @Override
        public void transformPage(@NonNull View page, float position) {
            int width = page.getWidth();
            int height = page.getHeight();
            // page ViewPager的条目 当前条目和预加载的条目
            // position 条目移动的位置

            if (position < -1) {// <-1 条目划出预加载长度，被viewpager删除
                page.setRotation(0);
            } else if (position < 0) {// -1~0 当前显示的条目滑到左边，或者左相邻条目滑动到显示

                page.setRotation(position * degress);
                // 设置旋转的中心点为 控件下边中心
                page.setPivotX(width / 2);
                page.setPivotY(height);

            } else if (position <= 1) {// 0~1 当前显示的条目滑到右边，或者右相邻条目滑动到显示

                page.setRotation(position * degress);
                // 设置旋转的中心点为 控件下边中心
                page.setPivotX(width / 2);
                page.setPivotY(height);

            } else {// > 1 条目划出预加载长度，被viewpager删除
                page.setRotation(0);
            }
        }
    }


    public class CustPagerTransformer implements ViewPager.PageTransformer {

        private int maxTranslateOffsetX;
        private ViewPager viewPager;

        CustPagerTransformer(Context context) {
            this.maxTranslateOffsetX = dp2px(context, 180);
        }

        public void transformPage(@NonNull View view, float position) {
            if (viewPager == null) {
                viewPager = (ViewPager) view.getParent();
            }
            int leftInScreen = view.getLeft() - viewPager.getScrollX();
            int centerXInViewPager = leftInScreen + view.getMeasuredWidth() / 2;
            int offsetX = centerXInViewPager - viewPager.getMeasuredWidth() / 2;
            float offsetRate = (float) offsetX * 0.38f / viewPager.getMeasuredWidth();
            float scaleFactor = 1 - Math.abs(offsetRate);
            if (scaleFactor > 0) {
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                view.setTranslationX(-maxTranslateOffsetX * offsetRate);
            }
        }
        /**
         * dp和像素转换
         */
        private int dp2px(Context context, float dipValue) {
            float m = context.getResources().getDisplayMetrics().density;
            return (int) (dipValue * m + 0.5f);
        }
    }

    // ---------------------------------------------------------

    private void setFirstHeader() {
        // 给RecyclerView添加头部
        View header1 = mActivity.getLayoutInflater().inflate(R.layout.feasible_list_header, null);
        mViewPager = (ViewPager) header1.findViewById(R.id.viewpager);
        mText = (TextView) header1.findViewById(R.id.viewpager_text);
        mLLDot = (LinearLayout) header1.findViewById(R.id.ll_dot);


        //图片放到ImageView,imageView放到ViewPager中展示
        //根据图片创建imageview,有几张图片创建几个imageView
        for (int i = 0; i < imageResIds.length; i++) {
            //创建ImageView
            createImageView(i);
            //根据图片的张数，创建相应的个数的点
            createDot(i);
        }
        //当界面切换到相应的界面的时候显示相应的文本，就需要设置viewpager的界面切换监听
        mViewPager.addOnPageChangeListener(onPageChangeListener);
        //设置第一个界面显示文本和点
        changed(0);

        //设置最大条目数
        pagesize = imageResIds.length * 1000 * 100;

        //跟listview类似
        mViewPager.setAdapter(new Myadapter());
        //设置刚进入的时候也可以从第一个界面滑动到最后一个界面
        int currentItem = pagesize/2;
        //设置显示currentItem对应的条目
        mViewPager.setCurrentItem(currentItem);//设置当前显示的条目，item:显示条目的索引


        adapter.addHeaderView(header1);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case VIEWPAGER_CHANGED:
                    //界面切换
                    switchPage();
                    break;
                default:
                    break;
            }
        };
    };


    /**
     * 自动切换界面
     */
    protected void switchPage() {
        //0->1  1->2  2->3  3->4  4->0
        //获取当前显示界面的索引
        if (mViewPager == null) return;
        int currentItem = mViewPager.getCurrentItem();//获取当前显示界面的索引
        //如果是最后一个界面，从0开始
        //getAdapter : 获取viewpager的adapter
        if ( mViewPager.getAdapter() != null && currentItem == mViewPager.getAdapter().getCount()-1) {
            currentItem=0;
        }else{
            currentItem++;
        }
        //重新设置当前显示界面
        mViewPager.setCurrentItem(currentItem);
        //重新发送消息，进行下一次切换
        handler.sendEmptyMessageDelayed(VIEWPAGER_CHANGED, 3000);
    }


    /**
     * 切换文本和点
     * @param position
     */
    protected void changed(int position) {
        position = position % imageResIds.length;
        //切换文本
        mText.setText(descs[position]);
        //切换点
        //白色 -> 黑色   黑色 -> 白色  ，同一时刻只能有一个白点
        //判断如果上一个界面的点是白色，改为黑色
        if (currentDot != null) {
            currentDot.setSelected(false);
        }
        //设置下一个点是白色的点
        dots[position].setSelected(true);
        //保存白色的点
        currentDot = dots[position];
    }

    /**
     * 创建点的操作
     * @param i
     */
    private void createDot(int i) {
        //保存创建的点
        dots[i] = new View(mActivity);
        //LayoutParams : 设置view的属性
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(5, 5);
        //设置背景图片
        dots[i].setBackgroundResource(R.drawable.selector_dot);
        params.rightMargin = 5;//设置距离右边的距离
        //设置属性给view
        dots[i].setLayoutParams(params);
        //将view添加到点的容器中显示
        mLLDot.addView(dots[i]);
    }

    /**
     * 创建ImageView---每一个ViewPager的页面放一个ImageView，用来显示图片
     * @param i
     */
    private void createImageView(int i) {
        //因为要创建很多的imageview来使用
        imageViews[i] = new ImageView(mActivity);
        //设置imageView显示图片
        imageViews[i].setBackgroundResource(imageResIds[i]);
    }


    /**
     * 更新页面
     */
    @Override
    protected void initData() {
        super.initData();
        RetrofitServiceManager.getInstance(mContext).create(RetrofitHttpService.class)
                .getAndroidDataForFeasibleFragmentNoBackpressure()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                 //不支持背压
                .subscribe(new Observer<FeasibleFragmentModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FeasibleFragmentModel feasibleFragmentModel) {
                        results = feasibleFragmentModel.getResults();
                        Log.e("lxl", "results==>" + results.get(2).getWho());
                        initRecyclerView(view);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

                // 支持背压 有问题（拿不到数据）
//                .subscribe(new Subscriber<FeasibleFragmentModel>() {
//                    @Override
//                    public void onSubscribe(Subscription s) {
//
//                    }
//
//                    @Override
//                    public void onNext(FeasibleFragmentModel feasibleFragmentModel) {
//                        results = feasibleFragmentModel.getResults();
//                        Log.e("lxl", "result=---="+results.get(2).getUrl());
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        Toast.makeText(mActivity, "网络异常", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }

    @Override
    public void onStart() {
        super.onStart();
        //每隔一段时间给handler发送一个消息
        handler.sendEmptyMessageDelayed(VIEWPAGER_CHANGED, 3000);
    }

    @Override
    public void onStop() {
        super.onStop();
        //取消切换操作
        handler.removeMessages(VIEWPAGER_CHANGED);//停止发送消息
    }

    /**
     * viewpager界面切换的监听器
     */
    private ViewPager.OnPageChangeListener onPageChangeListener
                            = new ViewPager.OnPageChangeListener() {

        //当viewpager界面切换完成的时候调用
        //position : 当前界面的索引
        @Override
        public void onPageSelected(int position) {
            //切换文本和点
            changed(position);
        }
        //当viewpager切换的时候调用
        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {

        }
        //当viewpager界面切换状态改变调用的
        //state : ViewPager状态
        @Override
        public void onPageScrollStateChanged(int state) {
            //ViewPager.SCROLL_STATE_IDLE;//空闲状态
            //ViewPager.SCROLL_STATE_DRAGGING;//拖动的状态
            //ViewPager.SCROLL_STATE_SETTLING;//拖动到最后一个的状态
            //如果是滑动状态取消自动滑动
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                handler.sendEmptyMessageDelayed(VIEWPAGER_CHANGED, 3000);
            }else{
                handler.removeMessages(VIEWPAGER_CHANGED);
            }
        }
    };


    /**
     * ViewPager对应的适配器
     * @author Administrator
     *
     */
    private class Myadapter extends PagerAdapter {
        //设置条目的个数
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return pagesize;
        }
        //判断viewpager的页面的对象是否和instantiateItem返回添加界面的对象一致
        //判断当前要显示界面对象是否和原先已经创建出来的界面对象一致，一致：添加到viewpager显示，不一致：不做操作
        //view:viewpager界面的对象
        //object : instantiateItem返回的对象
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
        //添加viewPager的条目
        //container : viewpager
        //position : 条目的位置
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            //01234 5%5 = 0   6%5 = 1  7%5 = 2
            position = position % imageResIds.length;
            //1.根据条目的位置获取相应的ImageView
            ImageView imageView = imageViews[position];
            //2.添加到viewpager中展示了
            container.addView(imageView);
            //3.viewpager添加了什么对象，返回什么对象，表示返回ViewPager界面的对象
            return imageView;
        }
        //销毁viewpager的条目
        //container : ViewPager
        //position : 条目的索引
        //object : instantiateItem返回的对象
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            // TODO Auto-generated method stub
            //super.destroyItem(container, position, object);//抛出异常
            container.removeView((View)object);
        }
    }


}
