package com.wecome.demo.learn.wolf;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义具体的被观察者----狼王
 *
 * 由于一个狼群中只有一个狼王，所以狼王是一个单例
 */
public class LangWang implements IWolf{

    private volatile static LangWang mInstance;

    private LangWang() {}

    public static LangWang getInstance() {
        if (mInstance == null) {
            synchronized (LangWang.class) {
                if (mInstance == null) {
                    mInstance = new LangWang();
                }
            }
        }
        return mInstance;
    }

    // 除过狼王外的狼「观察者」
    private List<NormalWolf> observers = new ArrayList<>() ;

    // 狼王下达的命令
    private String order  ;

    @Override
    public void attchObserver(NormalWolf observer) {
        observers.add(observer);
    }

    @Override
    public void detchObserver(NormalWolf observer) {
        if (observers.contains(observer)) {
            observers.remove(observer);
        }
    }

    @Override
    public void notifyObserver(String str) {
        for (NormalWolf observer : observers) {
            observer.reciveCommand(str);
        }
    }


    /**
     * 狼王下达命令
     */
    public void underCommand(String order) {
        this.order = order;
        this.notifyObserver(order);
    }


}
