package com.store.stack.page;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.baidu.mapframework.app.fpstack.BasePage;
import com.store.stack.R;
import com.store.stack.utils.WifiUtils;

public class MainFramePage extends BasePage {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.page_frame_main, null);

        inflate.findViewById(R.id.btn_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean openWifi = WifiUtils.openWifi(getContext());
                Log.e("lxl", "openWifi=="+openWifi);
            }
        });
        inflate.findViewById(R.id.btn_off).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               boolean closeWifi = WifiUtils.closeWifi(getContext());
               Log.e("lxl", "closeWifi=="+closeWifi);
           }
       });

        return inflate;
    }

}
