package com.baidu.mapframework.scenefw.binding;


import com.baidu.mapframework.scenefw.binding.functions.Func1;
import com.baidu.mapframework.scenefw.binding.functions.Func2;
import com.baidu.mapframework.scenefw.binding.functions.Func3;
import com.baidu.mapframework.scenefw.binding.functions.Predicate;
import com.baidu.mapframework.scenefw.binding.schedulers.Scheduler;

import java.util.LinkedList;

public class Var<T> {

    private T value;
    private LinkedList<ValueCallback<T>> callbacks = new LinkedList<>();

    public void set(T value) {
        this.value = value;
        notifyCallbacks();
    }

    public void setWithoutNotify(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public synchronized void subscribe(ValueCallback<T> callback) {
        callbacks.add(callback);
    }

    public synchronized void unSubscribe(ValueCallback<T> callback) {
        callbacks.remove(callback);
    }

    private void notifyCallbacks() {
        LinkedList<ValueCallback<T>> currentCallbacks;
        synchronized (this) {
            currentCallbacks = new LinkedList<>(callbacks);
        }

        for (ValueCallback<T> callback : currentCallbacks) {
            callback.onValue(value);
        }
    }

    public interface ValueCallback<T> {
        void onValue(T t);
    }

    @Override
    public String toString() {
        return "Var{" + "value=" + value + '}';
    }

    /**
     * 变换数据类型
     *
     * @param mapper 转换函数
     * @param <R>    输出类型
     * @return 原始数据，经过转换函数后，生成的Var<R>
     */
    public <R> Var<R> map(final Func1<? super T, ? extends R> mapper) {
        final Var<T> source = this;
        return new Var<R>() {
            @Override
            public void subscribe(final ValueCallback<R> callback) {
                super.subscribe(callback);
                source.subscribe(new ValueCallback<T>() {
                    @Override
                    public void onValue(T t) {
                        set(mapper.apply(t));
                    }
                });
            }
        };
    }

    /**
     * 过滤
     *
     * @param predicate 过滤函数
     * @return 经过过滤后的数据
     */
    public Var<T> filter(final Predicate<? super T> predicate) {
        final Var<T> source = this;
        return new Var<T>() {
            @Override
            public void subscribe(final ValueCallback<T> callback) {
                super.subscribe(callback);
                source.subscribe(new ValueCallback<T>() {
                    @Override
                    public void onValue(T t) {
                        if (predicate.test(t)) {
                            set(t);
                        }

                    }
                });
            }
        };
    }

    /**
     * 组合, 二对一
     */
    public static <T1, T2, R> Var<R> combine(final Var<T1> t1Var, final Var<T2> t2Var, final Func2<T1, T2, R> func2) {
        return new Var<R>() {
            @Override
            public void subscribe(final ValueCallback<R> callback) {
                super.subscribe(callback);
                t1Var.subscribe(new ValueCallback<T1>() {
                    @Override
                    public void onValue(T1 t1) {
                        T2 t2 = t2Var.get();
                        if (t2 == null) {
                            return;
                        }

                        set(func2.apply(t1, t2));
                    }
                });

                t2Var.subscribe(new ValueCallback<T2>() {
                    @Override
                    public void onValue(T2 t2) {
                        T1 t1 = t1Var.get();
                        if (t1 == null) {
                            return;
                        }
                        set(func2.apply(t1, t2));
                    }
                });
            }
        };
    }

    /**
     * 组合, 三对一
     */
    public static <T1, T2, T3, R> Var<R> combine(final Var<T1> t1Var, final Var<T2> t2Var, final Var<T3> t3Var,
                                                 final Func3<T1, T2, T3, R> func3) {
        return new Var<R>() {
            @Override
            public void subscribe(final ValueCallback<R> callback) {
                super.subscribe(callback);
                t1Var.subscribe(new ValueCallback<T1>() {
                    @Override
                    public void onValue(T1 t1) {
                        T2 t2 = t2Var.get();
                        T3 t3 = t3Var.get();
                        if (t2 == null || t3 == null) {
                            return;
                        }

                        set(func3.apply(t1, t2, t3));
                    }
                });

                t2Var.subscribe(new ValueCallback<T2>() {
                    @Override
                    public void onValue(T2 t2) {
                        T1 t1 = t1Var.get();
                        T3 t3 = t3Var.get();
                        if (t1 == null || t3 == null) {
                            return;
                        }

                        set(func3.apply(t1, t2, t3));
                    }
                });

                t3Var.subscribe(new ValueCallback<T3>() {
                    @Override
                    public void onValue(T3 t3) {
                        T1 t1 = t1Var.get();
                        T2 t2 = t2Var.get();
                        if (t1 == null || t2 == null) {
                            return;
                        }

                        set(func3.apply(t1, t2, t3));
                    }
                });
            }
        };
    }


    /**
     * 切换执行线程
     */
    public Var<T> scheduleOn(final Scheduler scheduler) {
        final Var<T> source = this;
        return new Var<T>() {
            @Override
            public void subscribe(final ValueCallback<T> callback) {
                super.subscribe(callback);
                source.subscribe(new ValueCallback<T>() {
                    @Override
                    public void onValue(final T t) {
                        scheduler.scheduleDirect(new Runnable() {
                            @Override
                            public void run() {
                                set(t);
                            }
                        });
                    }
                });
            }
        };
    }
}
