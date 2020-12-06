package com.wecome.demoandroid.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wecome.demoandroid.R;
import com.wecome.demoandroid.demoutils.utils.Constant;
import com.wecome.demoandroid.view.SwipeLayout;
import com.wecome.demoandroid.view.foldview.MoreLineTextView;

/**
 * Created by Administrator on 2017/8/30.
 * 生活模块RecyclerView的Adapter，用第三方封装好的Adapter
 */

public class ThreeRecyclerViewAdapter extends
        RecyclerView.Adapter<ThreeRecyclerViewAdapter.ViewHolder> {

    private Context mContext;

    public ThreeRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext)
                .inflate(R.layout.activity_three_item, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mNoticeDes.setText(Constant.content3);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private final SwipeLayout mSwipeLayout;
        private final TextView mNoticeDelete;
        private final TextView mNoticeTime;
        private final TextView mNoticeTitle;
        private final MoreLineTextView mNoticeDes;

        public ViewHolder(View itemView) {
            super(itemView);

            mSwipeLayout = itemView.findViewById(R.id.sl_swipeLayout);
            mNoticeDelete = itemView.findViewById(R.id.tv_notice_delete);
            mNoticeTime = itemView.findViewById(R.id.tv_notice_time);
            mNoticeTitle = itemView.findViewById(R.id.tv_notice_title);
            mNoticeDes = itemView.findViewById(R.id.tvf_notice_des);
        }
    }

}
