package com.wecome.demo.activity;

import android.os.Bundle;
import android.view.ViewGroup;

import com.baidu.mapframework.app.fpstack.BasePage;
import com.baidu.mapframework.app.fpstack.BaseTask;
import com.baidu.mapframework.app.fpstack.HistoryRecord;
import com.baidu.mapframework.app.fpstack.Page;
import com.baidu.mapframework.app.fpstack.TaskManager;
import com.baidu.mapframework.app.fpstack.TaskManagerFactory;
import com.wecome.demo.R;
import com.wecome.demo.fragment.page.MapFramePage;

import java.util.Stack;

public class MapsActivity extends BaseTask {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TaskManagerFactory.getTaskManager().registerRootTask(MapsActivity.class.getName());
        setRootRecord();
        //create(savedInstanceState);

        setContentView(R.layout.base_stack);

        mNavigator.setContainerIds(R.id.persist_container, R.id.replace_container);
        mNavigator.setContainerActivity(this);
        ViewGroup viewGroup = findViewById(R.id.fragment_container);
        mNavigator.setPageContainer(viewGroup);

        navigateTo(MapFramePage.class.getName(), null, null);
        TaskManagerFactory.getTaskManager().clear();//清栈

        // 设置全面屏
        // FullScreenUtil.getInstance().setFullScreenUtil(getWindow());

        /*if (savedInstanceState != null) {
            //系统恢复的状态，直接跳转到主页，这时页面栈已经清空
            TaskManagerFactory.getTaskManager().navigateTo(TaskManagerFactory
                    .getTaskManager().getContext(), MapFramePage.class.getName());
            return;
        }

        TaskManagerFactory.getTaskManager()
                .registerPageStackChangedListener(pageStackChangedListener);*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TaskManagerFactory.getTaskManager().clear();
        /*TaskManagerFactory.getTaskManager()
                .unregisterPageStackChangedListener(pageStackChangedListener);*/
    }

    private void setRootRecord() {
        // set root page record
        final TaskManager taskManager = TaskManagerFactory.getTaskManager();
        taskManager.attach(this);
        HistoryRecord record = new HistoryRecord(MapsActivity.class
                .getName(), MapFramePage.class.getName());
        record.taskSignature = HistoryRecord.genSignature(this);
        taskManager.setRootRecord(record);
    }

    TaskManager.IPageStackChangedListener
            pageStackChangedListener = new TaskManager.IPageStackChangedListener() {

        @Override
        public void onPageStackChanged(boolean isEmpty) {

            final Stack<Page> pages = MapsActivity.this.getPageStack();
            if (!pages.isEmpty()) {
                BasePage top = (BasePage) pages.peek();
                if (top != null) {
                    int topMargin = top.voiceTopMargin();
                    if (topMargin == 0) {
                        topMargin = dip2px(60);
                    }
                }
            }
        }
    };

    public int dip2px(int dip) {
        return (int) (0.5F + getResources().getDisplayMetrics().density * dip);
    }


}
