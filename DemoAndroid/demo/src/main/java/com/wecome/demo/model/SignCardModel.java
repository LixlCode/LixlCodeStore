package com.wecome.demo.model;

import java.util.ArrayList;

/**
 * Created by chenjie07 on 17/8/26.
 */

public class SignCardModel {
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
     * 总签到次数
     */
    public String totalNum = "";
    /**
     * 时间周期
     */
    public String timeDutation = "";

    /**
     * 签到详细信息
     */
    public ArrayList<SignModel> data = new ArrayList<>();
}
