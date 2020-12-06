package com.baidu.mapframework.app.mvc;

import java.util.Observable;
import java.util.Observer;

import android.os.Handler;
import android.os.Looper;

/**
 * Created with IntelliJ IDEA.
 *
 * @author liguoqing
 * @version 1.0
 * @date 13-6-24 下午10:42
 */
public class BaseController extends Observable implements Controller, Observer {
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    @Override
    public void registerView(View view) {
        addObserver(view);
    }

    @Override
    public void unRegisterView(View view) {
        deleteObserver(view);
    }

    public void registerView(Observer view) {
        addObserver(view);
    }

    public void unRegisterView(Observer view) {
        deleteObserver(view);
    }

    @Override
    public void notifyChange(final Object obj) {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    changed(obj);
                }
            });
        } else {
            changed(obj);
        }
    }

    private void changed(Object obj) {
        setChanged();
        notifyObservers(obj);
    }

    /**
     * 引擎消息结果处理
     *
     * @param observable
     * @param obj
     */
    @Override
    public void update(Observable observable, Object obj) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
