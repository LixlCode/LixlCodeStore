package com.baidu.mapframework.app.mvc;

/**
 * Controller 接口</p>
 * <p>
 * <p>Controller的设计遵循单一职责原则，避免过多的复杂逻辑。</p>
 *
 * @author liguoqing
 * @version 1.0
 * @date 13-6-9 上午9:59
 */
public interface Controller {

    /**
     * 注册View
     *
     * @param view
     */
    void registerView(View view);

    void unRegisterView(View view);

    void notifyChange(Object obj);

}
