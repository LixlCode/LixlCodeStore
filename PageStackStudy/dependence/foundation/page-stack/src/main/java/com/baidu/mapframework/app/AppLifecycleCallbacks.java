package com.baidu.mapframework.app;

//import com.baidu.mapframework.nirvana.module.Module;

/**
 * App 生命周期回调
 *
 * @author liguoqing, guangongbo, liuda
 * @version 1.1
 * @date 13-7-2 上午11:45
 */
public interface AppLifecycleCallbacks {

    //Module getModule();

    /**
     * 应用启动（不是从后台到前台）
     */
    void onStartup();

    /**
     * 后台
     */
    void onBackground();

    /**
     * 前台
     */
    void onForeground();

    /**
     * 应用退出
     */
    void onExit();

    abstract class DefaultImpl implements AppLifecycleCallbacks {
        @Override
        public void onStartup() {
        }

        @Override
        public void onBackground() {
        }

        @Override
        public void onForeground() {
        }

        @Override
        public void onExit() {
        }
    }
}
