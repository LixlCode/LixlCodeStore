package com.baidu.observer.wolf;

/**
 * 抽象被观察者功能
 */
public interface IWolf {

    // 添加观察者
    void attchObserver(NormalWolf observer) ;

    // 移除观察者
    void detchObserver(NormalWolf observer) ;

    // 通知观察者
    void notifyObserver(String str) ;

}
