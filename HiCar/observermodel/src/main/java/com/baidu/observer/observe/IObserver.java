package com.baidu.observer.observe;


/**
 * Created by lxl
 *
 * 定义观察者接口,即关注公号的微信用户共同属性
 */
public interface IObserver {

    // 观察者收到信息,内容为 info
    void reciveContent(String info);

}
