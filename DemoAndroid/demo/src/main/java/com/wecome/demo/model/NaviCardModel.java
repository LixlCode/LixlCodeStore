package com.wecome.demo.model;

import java.util.ArrayList;

/**
 * Created by chenjie07 on 17/8/26.
 */

public class NaviCardModel {
    public enum State {
        LOADING,
        SUCCESS,
        FAILURE
    }

    /**
     * 数据加载状态
     */
    public State type = State.LOADING;
    /**
     * 总里程
     */
    public long totalDistance = 0;
    /**
     * 里程明细信息
     */
    public ArrayList<NaviModel> data = new ArrayList<>();
}
