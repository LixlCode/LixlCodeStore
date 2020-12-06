/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */

package com.wecome.demo.model;

/**
 * 月报入口数据模型
 * <p>
 * 控制是否展示月报入口，服务端下发字段
 *
 * @author chenjie07
 * @version v10.4.0
 * @since 17/11/27
 */

public class MonthReportModel {
    /**
     * 是否有新的月版 0： 没有， 1：有
     */
    public int hasNewReport = 0;
    /**
     * 月报类型, 0: 普通月报 1： 国庆报告 2: 本周出行报告
     */
    public int type = 0;
    /**
     * 月报日期， 如 201709
     */
    public String date = "";
    public String title = "";
}
