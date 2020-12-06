package com.wecome.demo.model;

public class SettingCenterModel extends ListModel {

    protected int secondaryLeftText;
    protected boolean isShowSecondaryLeftText;

    // 设置Item样式，默认为normal
    protected int style = 0;
    // CHECKABLE 样式，是否选中
    protected boolean checked;

    public SettingCenterModel(int leftText, int leftImgResId, int id) {
        super(leftText, leftImgResId, id);
    }

    public SettingCenterModel(int leftText, int id) {
        super(leftText, 0, id);
    }

    public int getSecondaryLeftText() {
        return secondaryLeftText;
    }

    public void setSecondaryLeftText(int secondaryLeftText) {
        this.secondaryLeftText = secondaryLeftText;
    }

    public boolean isShowSecondaryLeftText() {
        return isShowSecondaryLeftText;
    }

    public void setShowSecondaryLeftText(boolean isShowSecondaryLeftText) {
        this.isShowSecondaryLeftText = isShowSecondaryLeftText;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
