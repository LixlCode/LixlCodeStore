package com.wecome.demo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.wecome.demo.R;

public class TestActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_1;
    private TextView tv_2;
    private TextView tv_3;
    private TextView tv_4;
    private TextView tv_learn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_3 = (TextView) findViewById(R.id.tv_3);
        tv_4 = (TextView) findViewById(R.id.tv_4);
        tv_learn = (TextView) findViewById(R.id.tv_learn);
        tv_1.setOnClickListener(this);
        tv_2.setOnClickListener(this);
        tv_3.setOnClickListener(this);
        tv_4.setOnClickListener(this);
        tv_learn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_1:
                String str1 = "今天<font color='#FF0000'>天气不错</font>";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    tv_1.setText(Html.fromHtml(str1, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tv_1.setText(Html.fromHtml(str1));
                }
                break;
            case R.id.tv_2:
                String str2 = "今天<font color='#FF0000'><small>天气不错</small></font>";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    tv_2.setText(Html.fromHtml(str2, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tv_2.setText(Html.fromHtml(str2));
                }
                break;
            case R.id.tv_3:
                String str3 = "今天<font color='#FF0000'><big>天气不错</big></font>";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    tv_3.setText(Html.fromHtml(str3, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tv_3.setText(Html.fromHtml(str3));
                }
                break;
            case R.id.tv_4:
                SpannableString str = new SpannableString("今天天气不错");
                /*
                setSpan方法有四个参数:
                第一个参数：文本设置前景色，也就是文字颜色。如果要为文字添加背景颜色，可替换为BackgroundColorSpan
                第二个参数：文本颜色改变的起始位置
                第三个参数：spannableString.length()为文本颜色改变的结束位置
                第四个参数：布尔型，可以传入以下四种：
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE 从起始下标到终止下标，包括起始下标
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE 从起始下标到终止下标，同时包括起始下标和终止下标
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE 从起始下标到终止下标，但都不包括起始下标和终止下标
                    Spanned.SPAN_EXCLUSIVE_INCLUSIVE 从起始下标到终止下标，包括终止下标
                */
                str.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")),
                        2, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_4.setText(str);
                break;
            case R.id.tv_learn:
                startActivity(new Intent(TestActivity.this, MapsActivity.class));
                break;
            default:
                break;
        }
    }
}
