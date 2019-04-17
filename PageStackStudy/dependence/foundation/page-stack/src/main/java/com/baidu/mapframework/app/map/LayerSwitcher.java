package com.baidu.mapframework.app.map;

public class LayerSwitcher {

    public static final int NO_CHANGE = 0b0000000;
    public static final int SATELLITE = 0b0000001;
    public static final int STREETSCAPE = 0b0000010;
    public static final int TRAFFIC = 0b0000100;
    public static final int HOTMAP = 0b0001000;
    public static final int MISTMAP = 0b0010000;
    public static final int INDOORMAP = 0b0100000;
    public static final int TRAFFIC_UGC = 0b1000000;

    public static final int UNCERTAIN = Integer.MAX_VALUE;

    public final int enable;
    public final int disable;
    public final boolean restore;
    public final int theme;
    public final int scene;
    public final boolean backCleanLocLayer;

    public LayerSwitcher(int enable, int disable, boolean restore, int theme, int scene, boolean backCleanLocLayer) {
        this.enable = enable;
        this.disable = disable;
        this.restore = restore;
        this.theme = theme;
        this.scene = scene;
        this.backCleanLocLayer = backCleanLocLayer;
    }

    public static class Builder {
        private int enable = NO_CHANGE;
        private int disable = NO_CHANGE;
        private boolean restore = true;
        private int theme = -1;
        private int scene = -1;
        private boolean backCleanLocLayer = false;

        public Builder enable(int enable) {
            this.enable |= enable;
            return this;
        }

        public Builder disable(int disable) {
            this.disable |= disable;
            return this;
        }

        public Builder restore(boolean restore) {
            this.restore = restore;
            return this;
        }

        public Builder theme(int theme) {
            this.theme = theme;
            return this;
        }

        public Builder scene(int scene) {
            this.scene = scene;
            return this;
        }

        public Builder backCleanLocLayer(boolean backCleanLocLayer) {
            this.backCleanLocLayer = backCleanLocLayer;
            return this;
        }

        public LayerSwitcher build() {
            return new LayerSwitcher(enable, disable, restore, theme, scene, backCleanLocLayer);
        }
    }
}
