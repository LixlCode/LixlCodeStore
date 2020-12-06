package com.baidu.mapframework.app.mvc;

import android.os.Bundle;

/**
 * Created with IntelliJ IDEA.
 *
 * @author liguoqing
 * @version 1.0
 * @date 13-6-24 下午10:51
 */
public interface ViewModel {

    /**
     * 保存ViewModel的数据
     *
     * @param bundle
     */
    void saveData(Bundle bundle);

    /**
     * 恢复ViewModel的数据
     *
     * @param bundle
     */
    void restoreData(Bundle bundle);

}
