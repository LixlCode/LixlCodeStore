package com.baidu.mapframework.app.fpstack;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * 页面历史记录</p>
 *
 * @author liguoqing
 * @version 1.0
 * @date 13-6-8 下午6:43
 */
public class HistoryRecord implements Parcelable {

  public HistoryRecord(String task, String page) {
    this.componentId = "map.android.baidu.mainmap"; // ComConstant.COM_ID_MAIN;
    this.taskName = task;
    this.pageName = page;
    this.taskSignature = "";
    this.pageSignature = "";
  }

  public HistoryRecord(String componentId, String task, String page) {
    if (!TextUtils.isEmpty(componentId)) {
      this.componentId = componentId;
    }
    this.taskName = task;
    this.pageName = page;
    this.taskSignature = "";
    this.pageSignature = "";
  }

  public HistoryRecord(Parcel in) {
    this.taskName = in.readString();
    this.taskSignature = in.readString();
    this.pageName = in.readString();
    this.pageSignature = in.readString();
    this.componentId = in.readString();
  }

  public String taskName;
  public String pageName;
  public String componentId;

  /**
   * Task 标识，使用task的hashCode
   */
  public String taskSignature;

  /**
   * page 标识，使用page的tag
   */
  public String pageSignature;

  @Override
  public String toString() {
    return String.format("%s|%s%s|%s%s", componentId, taskName, taskSignature, pageName, pageSignature);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;

    if (obj == null)
      return false;

    if (((Object) this).getClass() != obj.getClass())
      return false;

    HistoryRecord record = (HistoryRecord) obj;
    boolean sameComp = componentId.equals(record.componentId);
    boolean sameTask = taskName.equals(record.taskName) && taskSignature.equals(record.taskSignature);
    boolean samePage = false;
    if (record.pageName != null && this.pageName != null)
      samePage = pageName.equals(record.pageName) && pageSignature.equals(record.pageSignature);
    if (record.pageName == null && this.pageName == null)
      samePage = false;
    return sameComp && sameTask && samePage;

  }

  public boolean equalsIgnoreSig(Object obj) {
    if (this == obj)
      return true;

    if (obj == null)
      return false;

    if (((Object) this).getClass() != obj.getClass())
      return false;

    HistoryRecord record = (HistoryRecord) obj;
    boolean sameTask = taskName.equals(record.taskName);
    boolean samePage = false;
    if (record.pageName != null && this.pageName != null)
      samePage = pageName.equals(record.pageName);
    if (record.pageName == null && this.pageName == null)
      samePage = false;
    return sameTask && samePage;
  }

  public static String genSignature(Object obj) {
    return "@" + Integer.toHexString(obj.hashCode());
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.taskName);
    dest.writeString(this.taskSignature);
    dest.writeString(this.pageName);
    dest.writeString(this.pageSignature);
    dest.writeString(this.componentId);
  }

  public static final Parcelable.Creator<HistoryRecord> CREATOR = new Parcelable.Creator<HistoryRecord>() {
    public HistoryRecord createFromParcel(Parcel in) {
      return new HistoryRecord(in);
    }

    public HistoryRecord[] newArray(int size) {
      return new HistoryRecord[size];
    }
  };
}
