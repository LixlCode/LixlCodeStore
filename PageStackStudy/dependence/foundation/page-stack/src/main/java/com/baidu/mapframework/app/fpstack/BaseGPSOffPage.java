package com.baidu.mapframework.app.fpstack;

/**
 * 不需要开启gps的页面基类
 *
 * @author shixianfang
 * @version 1.0.0
 * @since 2013年11月12日
 */
public class BaseGPSOffPage extends BasePage {

    /**
     * 修改父类的此方法重写,继承此类的界面不使用GPS.
     */
    @Override
    protected boolean changeGPSRequest() {
        return false;
    }
}
