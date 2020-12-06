package com.wecome.demo.learn.wolf;

/**
 * 抽象观察者---普通的狼
 */
public abstract class NormalWolf {

    // 拿到被观察者（狼王）的引用
    protected IWolf iWolf;

    // 收到狼王下达的命令
    public abstract void reciveCommand(String string);

}
