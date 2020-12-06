package com.wecome.demo.model;

public class ListModel {

    protected boolean isShowRedPoint = false;
    protected int leftImgResId;
    protected int rightImgResId;
    protected int leftText;
    protected String rightText;
    protected String specialRightText;
    protected boolean isShowRightText;
    protected boolean isShowSpecialRightText;
    protected int id = -1;
    public boolean showMedal = false;

    public ListModel(int leftText, int leftImgResId, int id) {
        this.leftText = leftText;
        this.leftImgResId = leftImgResId;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isShowRedPoint() {
        return isShowRedPoint;
    }

    public void setShowRedPoint(boolean isShowRedPoint) {
        this.isShowRedPoint = isShowRedPoint;
    }

    public int getLeftImgResId() {
        return leftImgResId;
    }

    public void setLeftImgResId(int leftImgResId) {
        this.leftImgResId = leftImgResId;
    }

    public int getLeftText() {
        return leftText;
    }

    public void setLeftText(int leftText) {
        this.leftText = leftText;
    }

    public String getRightText() {
        return rightText;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
    }

    public boolean isShowRightText() {
        return isShowRightText;
    }

    public void setShowRightText(boolean isShowRightText) {
        this.isShowRightText = isShowRightText;
    }

    public String getSpecialRightText() {
        return specialRightText;
    }

    public void setSpecialRightText(String specialRightText) {
        this.specialRightText = specialRightText;
    }

    public boolean isShowSpecialRightText() {
        return isShowSpecialRightText;
    }

    public void setShowSpecialRightText(boolean isShowSpecialRightText) {
        this.isShowSpecialRightText = isShowSpecialRightText;
    }

}
