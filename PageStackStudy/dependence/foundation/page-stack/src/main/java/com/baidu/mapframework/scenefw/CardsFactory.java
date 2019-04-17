package com.baidu.mapframework.scenefw;

import java.lang.reflect.Constructor;

import com.baidu.mapframework.app.fpstack.TaskManagerFactory;

import android.content.Context;

/**
 * Cards Factory
 *
 * @author liguoqing
 * @version 1.0 10/03/2017
 */

public class CardsFactory {

    /**
     * 根据卡片类型创建卡片对象
     * <p>
     * TODO 组件内部的卡片对象创建
     *
     * @param cardClz
     *
     * @return
     */
    public static Card createCard(Class<? extends Card> cardClz) {

        try {
            Constructor constructor = cardClz.getConstructor(Context.class);
            if (constructor != null) {
                Card card = (Card) constructor.newInstance(TaskManagerFactory.getTaskManager().getContainerActivity());
                card.onCreate();
                return card;
            } else {
                SFLog.e("createCard constructor not found");
            }
        } catch (Exception e) {
            SFLog.e("createCard exception", e);
        }
        return null;
    }
}
