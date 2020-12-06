package com.wecome.demo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wecome.demo.R;
import com.wecome.demo.adapter.SearchAdapter;
import com.wecome.demo.data.SearchData;
import com.wecome.demo.model.SearchModel;
import com.wecome.demo.utils.SimpleToolbar;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class SearchActivity extends AppCompatActivity {

    private EditText editText;
    private ImageView clean_text;
    private SearchAdapter adapter;
    private RadioButton tv_baidu;
    private RadioButton tv_meizu_yes;
    private RadioButton tv_meizu_no;
    private int TOTAL_COUNTER = 50;
    private int mCurrentCounter = 0;
    private Boolean isErr = false;
    private int PAGE_SIZE = 3;
    private ArrayList<SearchModel> openApiData;
    private RecyclerView rv_recyclerView;
    private SwipeRefreshLayout srl_swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initToolBar();
        initEditText();
        initSelected();
        initData();
        initSwipeRefresh();
        initRecyclerView();
    }

    private void initToolBar() {
        SimpleToolbar simple_toolbar = findViewById(R.id.simple_toolbar);
        simple_toolbar.setMainTitle("创新搜索");
        simple_toolbar.setRightHide();
        simple_toolbar.hideLeftAppName();
        simple_toolbar.setLeftshow();
        simple_toolbar.setLeftTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        openApiData = new ArrayList<>();
        tv_baidu.setChecked(true);
        openApiData.clear();
        Map<String, String> dataMap = SearchData.getOpenApiDataBaidu();
        saveDataToBean(dataMap);
    }

    private void saveDataToBean(Map<String, String> dataMap) {
        Set<Map.Entry<String, String>> entrys = dataMap.entrySet();
        for (Map.Entry<String, String> entry : entrys) {
            String key = entry.getKey();
            String value = entry.getValue();
            SearchModel bean = new SearchModel();
            bean.key = key;
            bean.value = value;
            openApiData.add(bean);
        }
    }

    private ArrayList<SearchModel> addDataToBean(Map<String, String> dataMap) {
        ArrayList<SearchModel> data = new ArrayList<>();
        Set<Map.Entry<String, String>> entrys = dataMap.entrySet();
        for (Map.Entry<String, String> entry : entrys) {
            String key = entry.getKey();
            String value = entry.getValue();
            SearchModel bean = new SearchModel();
            bean.key = key;
            bean.value = value;
            openApiData.add(bean);
        }
        return data;
    }

    private void initRecyclerView() {
        rv_recyclerView = (RecyclerView) findViewById(R.id.rv_recyclerView);
        rv_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchAdapter(openApiData);
        rv_recyclerView.setAdapter(adapter);

        rv_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String value = openApiData.get(position).value;
                if (!value.isEmpty()) {
                    executeOpenApi(value);
                } else {
                    Toast.makeText(SearchActivity.this,
                            "OpenApi链接无效", Toast.LENGTH_SHORT).show();
                }
            }
        });
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN); // 条目加载动画

        // 添加头部
        // adapter.addHeaderView(getLayoutInflater().inflate(R.layout.adapter_head, null));

        // 添加尾部
        // adapter.addFooterView(getLayoutInflater().inflate(R.layout.adapter_foot, null));

        // 上拉加载更多
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                rv_recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mCurrentCounter >= TOTAL_COUNTER) {
                            //数据全部加载完毕
                            adapter.loadMoreEnd();
                        } else {
                            if (isErr) {
                                //获取更多数据失败
                                isErr = true;
                                //同理，加载失败也要主动调用加载失败来停止加载
                                adapter.loadMoreFail();
                            } else {
                                //成功获取更多数据（可以直接往适配器添加数据）
                                Map<String, String> dataMap = SearchData.getLoadMore(PAGE_SIZE);
                                adapter.addData(addDataToBean(dataMap));
                                mCurrentCounter = adapter.getData().size();
                                //主动调用加载完成，停止加载
                                adapter.loadMoreComplete();
                            }
                        }
                    }
                }, 2000);
            }
        }, rv_recyclerView);
    }

    private void initSwipeRefresh() {
        srl_swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_swipeRefreshLayout);
        srl_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl_swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_baidu.setChecked(true);
                        tv_meizu_yes.setChecked(false);
                        tv_meizu_no.setChecked(false);
                        openApiData.clear();
                        Map<String, String> dataMap = SearchData.getOpenApiDataBaidu();
                        saveDataToBean(dataMap);
                        adapter.notifyDataSetChanged();
                        srl_swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        srl_swipeRefreshLayout.setColorSchemeColors(Color.GREEN, Color.YELLOW, Color.RED);
    }

    private void initSelected() {
        tv_baidu = (RadioButton) findViewById(R.id.tv_baidu);
        tv_baidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApiData.clear();
                Map<String, String> dataMap = SearchData.getOpenApiDataBaidu();
                saveDataToBean(dataMap);
                adapter.notifyDataSetChanged();
            }
        });

        tv_meizu_yes = (RadioButton) findViewById(R.id.tv_meizu_yes);
        tv_meizu_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApiData.clear();
                Map<String, String> dataMap = SearchData.getOpenApiDataYes();
                saveDataToBean(dataMap);
                adapter.notifyDataSetChanged();
            }
        });

        tv_meizu_no = (RadioButton) findViewById(R.id.tv_meizu_no);
        tv_meizu_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApiData.clear();
                Map<String, String> dataMap = SearchData.getOpenApiDataNo();
                saveDataToBean(dataMap);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initEditText() {
        editText = (EditText) findViewById(R.id.execute_api);
        clean_text = (ImageView) findViewById(R.id.clean_text);
        ImageView btn_go = (ImageView) findViewById(R.id.btn_go);
        clean_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.getText().clear();
            }
        });
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditView();
            }
        });

        // 不显示光标，不显示就不能复制粘贴了
        //editText.setCursorVisible(false);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    showEditView();
                    return true;
                }
                return false;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    clean_text.setVisibility(View.GONE);
                } else {
                    clean_text.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void executeOpenApi(String openApi) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(openApi));
        startActivity(intent);
    }

    private void showEditView() {
        String openApi = editText.getText().toString();
        if (!openApi.isEmpty()) {
            executeOpenApi(openApi);
        } else {
            Toast.makeText(SearchActivity.this,
                    "请输入正确的OpenApi链接", Toast.LENGTH_SHORT).show();
        }
    }

}
