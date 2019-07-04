package com.store.stack.other.observe;

public interface IWxServerSubject {

    // 添加观察者
    void attchObserver(IObserver iObserver);

    // 移出观察者
    void detachObserver(IObserver iObserver);

    // 通知观察者
    void notifyObserver();
}
