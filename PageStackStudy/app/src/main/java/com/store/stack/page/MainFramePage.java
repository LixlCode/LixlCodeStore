package com.store.stack.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapframework.app.fpstack.BasePage;
import com.store.stack.R;

public class MainFramePage extends BasePage {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.page_frame_main, null);
        return inflate;
    }

}
