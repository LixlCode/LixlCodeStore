package com.store.stack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.baidu.mapframework.app.fpstack.BaseTask;
import com.baidu.mapframework.app.fpstack.HistoryRecord;
import com.baidu.mapframework.app.fpstack.TaskManager;
import com.baidu.mapframework.app.fpstack.TaskManagerFactory;
import com.store.stack.page.MainFramePage;

public class MainStackActivity extends BaseTask {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TaskManagerFactory.getTaskManager().registerRootTask(MainStackActivity.class.getName());
        setRootRecord();
        setContentView(R.layout.base_stack_main);
        mNavigator.setContainerIds(R.id.persist_container, R.id.replace_container);
        mNavigator.setContainerActivity(this);
        ViewGroup viewGroup = findViewById(R.id.fragment_container);
        mNavigator.setPageContainer(viewGroup);
        navigateTo(MainFramePage.class.getName(), null, null);
        TaskManagerFactory.getTaskManager().clear();//清栈
    }

    private void setRootRecord() {
        // set root page record
        final TaskManager taskManager = TaskManagerFactory.getTaskManager();
        taskManager.attach(this);
        HistoryRecord record = new HistoryRecord(MainStackActivity.class.getName(), MainFramePage.class.getName());
        record.taskSignature = HistoryRecord.genSignature(this);
        taskManager.setRootRecord(record);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TaskManagerFactory.getTaskManager().clear();
    }

}
