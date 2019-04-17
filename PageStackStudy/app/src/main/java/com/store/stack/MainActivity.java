package com.store.stack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView mHelloWorld;
    private TextView mVersion;
    private TextView mUpdate;
    private TextView mTerms;
    private static Boolean isShow = false;

    // -------head viewPage start---------
    private ViewPager mViewPage;
    private RadioGroup mRadioGroup;
    private int images[] = {R.mipmap.img01, R.mipmap.img02, R.mipmap.img03,
            R.mipmap.img04, R.mipmap.img05, R.mipmap.img06};
    //当前索引位置以及上一个索引位置
    private int index;
    private int preIndex;
    private Timer timer = new Timer();
    //是否需要轮播标志
    private boolean isContinue = true;
    private static final float DEFAULT_MIN_ALPHA = 0.3f;
    private float mMinAlpha = DEFAULT_MIN_ALPHA;
    private static final float DEFAULT_MAX_ROTATE = 15.0f;
    private float mMaxRotate = DEFAULT_MAX_ROTATE;
    // -------head viewPage end---------

    private int updateInfos[] = {R.mipmap.update_1, R.mipmap.update_2, R.mipmap.update_3, R.mipmap.update_4};
    private static Boolean isBackgroundUpdate = false;
    private static Boolean mTagFirstClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHelloWorld = findViewById(R.id.tv_hello_world);
        mVersion = findViewById(R.id.tv_version);
        mUpdate = findViewById(R.id.tv_update);
        mTerms = findViewById(R.id.tv_terms);
        showChannelAndVersion();
        initViewPage();
        initNewViewPage();
    }

    private void initNewViewPage() {
        ViewPager mNewViewPage = findViewById(R.id.vp_view_page_new);
        mNewViewPage.setAdapter(viewNewPageAdapter);
        mNewViewPage.setPageMargin(30);
        mNewViewPage.setOffscreenPageLimit(3);
        mNewViewPage.setCurrentItem(images.length * 100, true);
        mNewViewPage.setPageTransformer(true, new CustomPageTransformer(this));
    }

    PagerAdapter viewNewPageAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            position = position % images.length;
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setImageResource(images[position]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    };

    public class CustomPageTransformer implements ViewPager.PageTransformer {

        private int maxTranslateOffsetX;
        private ViewPager viewPager;

        CustomPageTransformer(Context context) {
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

    private void showChannelAndVersion() {
        mHelloWorld.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                try {
                    // 获取版本信息
                    PackageInfo packageInfo = getPackageManager()
                            .getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
                    String versionName = packageInfo.versionName;
                    long versionCode = packageInfo.versionCode;
                    // 获取渠道信息和包名
                    ApplicationInfo appInfo = getPackageManager()
                            .getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
                    Object channel = appInfo.metaData.get("CHANNEL");
                    String packageName = appInfo.packageName;

                    if (!isShow) {
                        mVersion.setText("versionName="+versionName+", versionCode="+versionCode);
                        isShow = true;
                    } else {
                        if (channel != null) {
                            String channelIdStr = TextUtils.isEmpty(channel
                                    .toString()) ? "1000" : channel.toString();
                            mVersion.setText("channel="+channelIdStr + ", packageName="+packageName);
                        }
                        isShow = false;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });


        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mTagFirstClick) {
                    showUpdateDialog();
                } else {
                    Toast.makeText(MainActivity.this,"正在后台升级",Toast.LENGTH_SHORT).show();
                }
            }
        });


        mTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNewPageStack();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTagFirstClick = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;
    }

    private void initViewPage() {
        mViewPage = findViewById(R.id.vp_view_page);
        mRadioGroup = findViewById(R.id.rg_radio_group);
        mViewPage.setPageMargin(30);
        mViewPage.setOffscreenPageLimit(3);
        mViewPage.setAdapter(mViewPageAdapter);
        mViewPage.addOnPageChangeListener(onPageChangeListener);
        mViewPage.setPageTransformer(true, new PageTransformer());
        mViewPage.setCurrentItem(images.length * 100);
        initRadioButton(images.length);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isContinue) {
                    handler.sendEmptyMessage(1);
                }
            }
        },2000, 2000);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    index++;
                    mViewPage.setCurrentItem(index);
            }
        }
    };

    PagerAdapter mViewPageAdapter = new PagerAdapter() {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            position = position % images.length;
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setImageResource(images[position]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    };

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int l) {

        }

        @Override
        public void onPageSelected(int position) {
            index = position;
            setCurrentDot(index % images.length);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private void setCurrentDot(int i) {
        if (mRadioGroup.getChildAt(i) != null) {
            //当前按钮不可改变
            mRadioGroup.getChildAt(i).setEnabled(false);
        }
        if (mRadioGroup.getChildAt(preIndex) != null) {
            //上个按钮可以改变
            mRadioGroup.getChildAt(preIndex).setEnabled(true);
            //当前位置变为上一个，继续下次轮播
            preIndex = i;
        }
    }

    private void initRadioButton(int length) {
        for (int i = 0; i < length; i++) {
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setImageResource(R.drawable.viewpage_point_selector);
            imageView.setPadding(20, 0, 0, 0);
            mRadioGroup.addView(imageView, ViewGroup
                    .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mRadioGroup.getChildAt(0).setEnabled(false);
        }
    }

    /**
     * [-Infinity,-1)(1,+Infinity][-1,1]
     * 三个区间，对于前两个，拿我们的页面上目前显示的3个Page来说，前两个分别对应左右两个露出一点的Page，
     * 那么对于alpha值，只需要设置为最小值即可
     * <p>
     * [-1,1]第一页->第二页这个过程来说，主要看position的变化
     * 页1的position变化为：从0到-1, 页2的position变化为：从1到0
     * 第一页到第二页，实际上就是左滑，第一页到左边，第二页成为currentItem到达中间，
     * 那么对应alpha的变化应该是：页1到左边，对应alpha应该是：1到minAlpha
     * 页2到中间，成为currentItem，对应alpha应该是：minAlpha到1
     */
    public class PageTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(@NonNull View view, float position) {
            if (position < -1) {
                //透明度
                view.setAlpha(mMinAlpha);
                //旋转
                view.setRotation(mMaxRotate * -1);
                view.setPivotX(view.getWidth());
                view.setPivotY(view.getHeight());
            } else if (position <= 1) {
                if (position < 0) {
                    //position是0到-1的变化,1+position就是从1到0的变化
                    //(1 - mMinAlpha) * (1 + position)就是(1 - mMinAlpha)到0的变化
                    //再加上一个mMinAlpha，就变为1到mMinAlpha的变化。
                    float factor = mMinAlpha + (1 - mMinAlpha) * (1 + position);
                    view.setAlpha(factor);

                    view.setRotation(mMaxRotate * position);
                    //position为width/2到width的变化
                    view.setPivotX(view.getWidth() * 0.5f * (1 - position));
                    view.setPivotY(view.getHeight());
                } else {
                    //minAlpha到1的变化
                    float factor = mMinAlpha + (1 - mMinAlpha) * (1 - position);
                    view.setAlpha(factor);

                    view.setRotation(mMaxRotate * position);
                    view.setPivotX(view.getWidth() * 0.5f * (1 - position));
                    view.setPivotY(view.getHeight());
                }
            } else {
                view.setAlpha(mMinAlpha);

                view.setRotation(mMaxRotate);
                view.setPivotX(0);
                view.setPivotY(view.getHeight());
            }
        }
    }

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        @SuppressLint("InflateParams")
        View inflate = LayoutInflater.from(this).inflate(R.layout.update_dialog, null);
        ViewPager updateInfo = inflate.findViewById(R.id.vp_update_info);
        final Button nowUpdate = inflate.findViewById(R.id.btn_now_update);
        Button backgroundUpdate = inflate.findViewById(R.id.btn_background);
        final LinearLayout updateProgress = inflate.findViewById(R.id.ll_update_progress);

        final AlertDialog dialog = builder.setView(inflate).show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (isBackgroundUpdate) {
                    // TODO 开一个服务，进行下载和更新的操作
                    Toast.makeText(MainActivity
                            .this,"已转后台下载更新", Toast.LENGTH_LONG).show();
                    mTagFirstClick = true;
                }
            }
        });

        updateInfo.setAdapter(mViewPageUpdateAdapter);
        updateInfo.setPageMargin(30);
        updateInfo.setOffscreenPageLimit(3);
        updateInfo.setCurrentItem(updateInfos.length * 100);
        updateInfo.setPageTransformer(true, /*new RotatePageTransformer()*/
                                      new CustomPageTransformer(this));

        nowUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowUpdate.setVisibility(View.GONE);
                updateProgress.setVisibility(View.VISIBLE);
                isBackgroundUpdate = true;
            }
        });

        backgroundUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    PagerAdapter mViewPageUpdateAdapter = new PagerAdapter() {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            position = position % updateInfos.length;
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setImageResource(updateInfos[position]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    };


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

    private void initNewPageStack() {
        Intent intent = new Intent(this, MainStackActivity.class);
        startActivity(intent);
    }

}
