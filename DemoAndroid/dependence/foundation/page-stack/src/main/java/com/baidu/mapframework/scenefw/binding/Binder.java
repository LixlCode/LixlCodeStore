/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.mapframework.scenefw.binding;

import java.util.LinkedList;

/**
 * binder 主要为预加载及card数据同步提供工具
 * <p>
 * 如：在onLoadData时，卡片UI可能还未创建，此时可以使用Var<T>存储请求数据，预加载
 * Var<String> preload = new Var<>
 * // 加载数据，preload.set(value)
 * <p>
 * 在UI构造后，通过 connect，绑定2个数据结果
 * binder.connect (preload, card.getXXX)
 * <p>
 * 生命周期
 * binder 对象会在onShow时，自动 binder.bind 触发同步
 * binder 对象会在onHide时，自动 binder.unbind 触发断开
 */
public class Binder {

    private boolean isBind = false;
    private LinkedList<Binding> bindings = new LinkedList<>();

    /**
     * 链接两个 var 对象，使其内容自动同步
     * 在页面每次onShow的时候，自动调用onValue通知最新值
     */
    public <T> void connect(Var<T> from, final Var<T> to) {
        connect(from, to, true);
    }

    /**
     * 链接两个 var 对象，使其内容自动同步
     * onBindNotify，在页面每次onShow的时候，是否自动调用onValue通知最新值
     */
    public <T> void connect(Var<T> from, final Var<T> to, final boolean onBindNotify) {
        // TODO 优化数据结构，提高效率
        for (Binding b : bindings) {
            if (b.getFrom() == from && b.getTo() == to) {
                return;
            }
        }

        Binding<T> binding;
        if (from instanceof TaskVar && to instanceof TaskVar) {
            final TaskVar<T> from1 = (TaskVar<T>) from;
            final TaskVar<T> to1 = (TaskVar<T>) to;
            TaskBinding<T> callback = createTaskBinding(from1, to1);
            from1.subscribeTask(callback);
            binding = callback;
        } else {
            VarBinding<T> callback = createVarBinding(from, to);
            from.subscribe(callback);
            binding = callback;
        }
        synchronized (this) {
            binding.onBindNotify = onBindNotify;
            bindings.add(binding);
        }
    }

    /**
     * 取消链接两个 var 对象，使其内容不再自动同步
     */
    public <T> void disconnect(Var<T> from, Var<T> to) {
        LinkedList<Binding> currentBindings;
        synchronized (this) {
            currentBindings = new LinkedList<>(bindings);
        }

        for (Binding binding : currentBindings) {
            if (binding.getFrom().equals(from) && binding.getTo().equals(to)) {
                binding.destroy();
            }
        }
    }

    /**
     * 通过一个 var 对象，产生新对象，并使其同步
     * 等于
     * Var v = new Var<T>
     * connect(from, v)
     * <p>
     * 在页面每次onShow的时候，自动调用onValue通知最新值
     */
    public <T> Var<T> newConnect(Var<T> from) {
        return newConnect(from, true);
    }

    /**
     * 通过一个 var 对象，产生新对象，并使其同步
     * 等于
     * Var v = new Var<T>
     * connect(from, v)
     * <p>
     * onBindNotify，在页面每次onShow的时候，是否自动调用onValue通知最新值
     */
    public <T> Var<T> newConnect(Var<T> from, final boolean onBindNotify) {
        if (from instanceof TaskVar) {
            TaskVar<T> r = new TaskVar<>();
            connect(from, r, onBindNotify);
            return r;
        } else {
            Var<T> r = new Var<>();
            connect(from, r, onBindNotify);
            return r;
        }
    }

    /**
     * 使所有连接生效
     */
    public void bind() {
        isBind = true;
        for (Binding binding : bindings) {
            binding.bind();
        }
    }

    /**
     * 使所有连接暂时终止
     */
    public void unbind() {
        isBind = false;
        for (Binding binding : bindings) {
            binding.unbind();
        }
    }

    /**
     * 取消全部连接
     */
    public void destroy() {
        isBind = false;
        for (Binding binding : bindings) {
            binding.destroy();
        }
        bindings.clear();
    }

    private abstract class Binding<T> {

        abstract Var<T> getFrom();

        abstract Var<T> getTo();

        abstract void bind();

        abstract void unbind();

        boolean isDestroy = false;
        boolean onBindNotify = true;

        void destroy() {
            isDestroy = true;
            if (getFrom() instanceof TaskVar) {
                ((TaskVar<T>) getFrom()).unSubscribeTask((TaskVar.TaskStageCallback<T>) this);
            } else {
                getFrom().unSubscribe((Var.ValueCallback<T>) this);
            }
        }
    }

    private <T> VarBinding<T> createVarBinding(final Var<T> from, final Var<T> to) {
        return new VarBinding<T>() {
            @Override
            public Var<T> getFrom() {
                return from;
            }

            @Override
            public Var<T> getTo() {
                return to;
            }

            @Override
            void onBinding(T t) {
                to.set(t);
            }
        };
    }

    private abstract class VarBinding<T> extends Binding<T> implements Var.ValueCallback<T> {

        @Override
        public void onValue(T t) {
            if (isBind && !isDestroy) {
                onBinding(t);
            }
        }

        abstract void onBinding(T t);

        public void bind() {
            if (!isDestroy && onBindNotify) {
                onBinding(getFrom().get());
            }
        }

        public void unbind() {
        }
    }


    private <T> TaskBinding<T> createTaskBinding(final TaskVar<T> from, final TaskVar<T> to) {
        return new TaskBinding<T>() {
            @Override
            public Var<T> getFrom() {
                return from;
            }

            @Override
            public Var<T> getTo() {
                return to;
            }

            @Override
            void onBinding(Task.Stage stage, T value, Exception e) {
                Tracker tracker = to.getTask().getTracker();
                if (stage == Task.Stage.SUCCESS) {
                    tracker.setSuccess(value);
                }
                if (stage == Task.Stage.LOADING) {
                    tracker.setLoading();
                }
                if (stage == Task.Stage.FAILED) {
                    tracker.setFailed(e);
                }
            }
        };
    }

    private abstract class TaskBinding<T> extends Binding<T> implements TaskVar.TaskStageCallback<T> {

        @Override
        public void onNotStart() {
            if (isBind && !isDestroy) {
                onBinding(Task.Stage.NOT_START, null, null);
            }
        }

        @Override
        public void onLoading() {
            if (isBind && !isDestroy) {
                onBinding(Task.Stage.LOADING, null, null);
            }
        }

        @Override
        public void onSuccess(T t) {
            if (isBind && !isDestroy) {
                onBinding(Task.Stage.SUCCESS, t, null);
            }
        }

        @Override
        public void onFailed(Exception e) {
            if (isBind && !isDestroy) {
                onBinding(Task.Stage.FAILED, null, e);
            }
        }

        abstract void onBinding(Task.Stage t, T value, Exception e);

        public void bind() {
            if (!isDestroy && onBindNotify) {
                if (((TaskVar<T>) getFrom()).getTask() != null) {
                    Tracker<T> tracker = ((TaskVar<T>) getFrom()).getTask().getTracker();
                    onBinding(tracker.getStage(), tracker.getResult(), tracker.getException());
                }
            }
        }

        public void unbind() {
        }
    }

    public static class StubTask<T> implements Task<T> {
        @Override
        public void execute() {

        }

        @Override
        public void cancel() {

        }

        Tracker<T> tracker = new Tracker<>();

        @Override
        public Tracker<T> getTracker() {
            return tracker;
        }
    }
}
