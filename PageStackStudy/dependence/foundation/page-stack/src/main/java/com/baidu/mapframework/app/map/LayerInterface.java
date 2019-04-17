package com.baidu.mapframework.app.map;

public interface LayerInterface {
    interface Switcher {
        LayerSwitcher layerSwitcher();
    }

    interface Create {
        void onCreate();
    }

    interface Destroy {
        void onDestroy();
    }

    interface LayerTransition {
        void onLayerTransition(Switcher outGoing, Switcher inComing);
    }
}
