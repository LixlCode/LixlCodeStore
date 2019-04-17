package com.baidu.mapframework.app.fpstack;

import android.os.Bundle;

/**
 * 页面工厂接口</p>
 * <p/>
 * 页面为单实例，不支持同一个页面的多个实例</p>
 *
 * @author liguoqing
 * @version 1.0
 * @Date: 13-6-8 下午4:33
 */
interface PageFactory {
    String DEFAULT_PAGE_TAG = "";

    /**
     * 获取页面实例
     *
     * @param pageClsName 页面类名
     *
     * @return 页面实例
     */
    BasePage getBasePageInstance(String pageClsName);

    BasePage getBasePageInstance(String pageClsName, String pageTagString);

    BasePage getBasePageInstance(ClassLoader classLoader, String pageClsName);

    BasePage getBasePageInstance(ClassLoader classLoader, String pageClsName, String pageTagString);

    void removePage(BasePage page);

    /**
     * 清空缓存
     */
    void clearCache();

    /**
     * 获取缓存的页面参数
     *
     * @param pageClsName
     * @param pageTagString
     *
     * @return
     */
    Bundle getPageSavedArguments(String pageClsName, String pageTagString);
}
